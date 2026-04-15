package org.mozilla.javascript;

import java.math.BigInteger;
import org.mozilla.classfile.ByteCode;
import org.objectweb.asm.signature.SignatureVisitor;

class DToA {
    private static final int Bias = 1023;
    private static final int Bletch = 16;
    private static final int Bndry_mask = 1048575;
    static final int DTOSTR_EXPONENTIAL = 3;
    static final int DTOSTR_FIXED = 2;
    static final int DTOSTR_PRECISION = 4;
    static final int DTOSTR_STANDARD = 0;
    static final int DTOSTR_STANDARD_EXPONENTIAL = 1;
    private static final int Exp_11 = 1072693248;
    private static final int Exp_mask = 2146435072;
    private static final int Exp_mask_shifted = 2047;
    private static final int Exp_msk1 = 1048576;
    private static final long Exp_msk1L = 4503599627370496L;
    private static final int Exp_shift = 20;
    private static final int Exp_shift1 = 20;
    private static final int Exp_shiftL = 52;
    private static final int Frac_mask = 1048575;
    private static final int Frac_mask1 = 1048575;
    private static final long Frac_maskL = 4503599627370495L;
    private static final int Int_max = 14;
    private static final int Log2P = 1;
    private static final int P = 53;
    private static final int Quick_max = 14;
    private static final int Sign_bit = Integer.MIN_VALUE;
    private static final int Ten_pmax = 22;
    private static final double[] bigtens = new double[]{1.0E16d, 1.0E32d, 1.0E64d, 1.0E128d, 1.0E256d};
    private static final int[] dtoaModes = new int[]{0, 0, 3, 2, 2};
    private static final int n_bigtens = 5;
    private static final double[] tens = new double[]{1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};

    DToA() {
    }

    private static char BASEDIGIT(int digit) {
        return (char) (digit >= 10 ? digit + 87 : digit + 48);
    }

    private static int lo0bits(int y) {
        int x = y;
        if ((x & 7) == 0) {
            int k = 0;
            if ((65535 & x) == 0) {
                k = 16;
                x >>>= 16;
            }
            if ((x & ByteCode.IMPDEP2) == 0) {
                k += 8;
                x >>>= 8;
            }
            if ((x & 15) == 0) {
                k += 4;
                x >>>= 4;
            }
            if ((x & 3) == 0) {
                k += 2;
                x >>>= 2;
            }
            if ((x & 1) != 0) {
                return k;
            }
            k++;
            if (((x >>> 1) & 1) == 0) {
                return 32;
            }
            return k;
        } else if ((x & 1) != 0) {
            return 0;
        } else {
            if ((x & 2) != 0) {
                return 1;
            }
            return 2;
        }
    }

    private static int hi0bits(int x) {
        int k = 0;
        if ((-65536 & x) == 0) {
            k = 16;
            x <<= 16;
        }
        if ((-16777216 & x) == 0) {
            k += 8;
            x <<= 8;
        }
        if ((-268435456 & x) == 0) {
            k += 4;
            x <<= 4;
        }
        if ((-1073741824 & x) == 0) {
            k += 2;
            x <<= 2;
        }
        if ((Sign_bit & x) == 0) {
            k++;
            if ((1073741824 & x) == 0) {
                return 32;
            }
        }
        return k;
    }

    private static void stuffBits(byte[] bits, int offset, int val) {
        bits[offset] = (byte) (val >> 24);
        bits[offset + 1] = (byte) (val >> 16);
        bits[offset + 2] = (byte) (val >> 8);
        bits[offset + 3] = (byte) val;
    }

    private static BigInteger d2b(double d, int[] e, int[] bits) {
        byte[] dbl_bits;
        int k;
        int i;
        long dBits = Double.doubleToLongBits(d);
        int d0 = (int) (dBits >>> 32);
        int d1 = (int) dBits;
        int z = d0 & 1048575;
        int de = (d0 & Integer.MAX_VALUE) >>> 20;
        if (de != 0) {
            z |= Exp_msk1;
        }
        int y = d1;
        if (d1 != 0) {
            dbl_bits = new byte[8];
            k = lo0bits(y);
            y >>>= k;
            if (k != 0) {
                stuffBits(dbl_bits, 4, (z << (32 - k)) | y);
                z >>= k;
            } else {
                stuffBits(dbl_bits, 4, y);
            }
            stuffBits(dbl_bits, 0, z);
            if (z != 0) {
                i = 2;
            } else {
                i = 1;
            }
        } else {
            dbl_bits = new byte[4];
            k = lo0bits(z);
            z >>>= k;
            stuffBits(dbl_bits, 0, z);
            k += 32;
            i = 1;
        }
        if (de != 0) {
            e[0] = ((de - 1023) - 52) + k;
            bits[0] = 53 - k;
        } else {
            e[0] = (((de - 1023) - 52) + 1) + k;
            bits[0] = (i * 32) - hi0bits(z);
        }
        return new BigInteger(dbl_bits);
    }

    static String JS_dtobasestr(int base, double d) {
        if (2 > base || base > 36) {
            throw new IllegalArgumentException("Bad base: " + base);
        } else if (Double.isNaN(d)) {
            return "NaN";
        } else {
            if (Double.isInfinite(d)) {
                return d > 0.0d ? "Infinity" : "-Infinity";
            } else {
                if (d == 0.0d) {
                    return "0";
                }
                boolean negative;
                String intDigits;
                if (d >= 0.0d) {
                    negative = false;
                } else {
                    negative = true;
                    d = -d;
                }
                double dfloor = Math.floor(d);
                long lfloor = (long) dfloor;
                if (((double) lfloor) == dfloor) {
                    if (negative) {
                        lfloor = -lfloor;
                    }
                    intDigits = Long.toString(lfloor, base);
                } else {
                    long mantissa;
                    long floorBits = Double.doubleToLongBits(dfloor);
                    int exp = ((int) (floorBits >> 52)) & Exp_mask_shifted;
                    if (exp == 0) {
                        mantissa = (Frac_maskL & floorBits) << 1;
                    } else {
                        mantissa = (Frac_maskL & floorBits) | Exp_msk1L;
                    }
                    if (negative) {
                        mantissa = -mantissa;
                    }
                    exp -= 1075;
                    BigInteger x = BigInteger.valueOf(mantissa);
                    if (exp > 0) {
                        x = x.shiftLeft(exp);
                    } else if (exp < 0) {
                        x = x.shiftRight(-exp);
                    }
                    intDigits = x.toString(base);
                }
                if (d == dfloor) {
                    return intDigits;
                }
                StringBuilder buffer = new StringBuilder();
                buffer.append(intDigits).append('.');
                double df = d - dfloor;
                long dBits = Double.doubleToLongBits(d);
                int word0 = (int) (dBits >> 32);
                int word1 = (int) dBits;
                int[] e = new int[1];
                BigInteger b = d2b(df, e, new int[1]);
                int s2 = -((word0 >>> 20) & Exp_mask_shifted);
                if (s2 == 0) {
                    s2 = -1;
                }
                s2 += 1076;
                BigInteger mlo = BigInteger.valueOf(1);
                BigInteger mhi = mlo;
                if (word1 == 0 && (1048575 & word0) == 0 && (2145386496 & word0) != 0) {
                    s2++;
                    mhi = BigInteger.valueOf(2);
                }
                b = b.shiftLeft(e[0] + s2);
                BigInteger s = BigInteger.valueOf(1).shiftLeft(s2);
                BigInteger bigBase = BigInteger.valueOf((long) base);
                boolean done = false;
                do {
                    BigInteger[] divResult = b.multiply(bigBase).divideAndRemainder(s);
                    b = divResult[1];
                    int digit = (char) divResult[0].intValue();
                    if (mlo == mhi) {
                        mhi = mlo.multiply(bigBase);
                        mlo = mhi;
                    } else {
                        mlo = mlo.multiply(bigBase);
                        mhi = mhi.multiply(bigBase);
                    }
                    int j = b.compareTo(mlo);
                    BigInteger delta = s.subtract(mhi);
                    int j1 = delta.signum() <= 0 ? 1 : b.compareTo(delta);
                    if (j1 == 0 && (word1 & 1) == 0) {
                        if (j > 0) {
                            digit++;
                        }
                        done = true;
                    } else if (j < 0 || (j == 0 && (word1 & 1) == 0)) {
                        if (j1 > 0) {
                            b = b.shiftLeft(1);
                            if (b.compareTo(s) > 0) {
                                digit++;
                            }
                        }
                        done = true;
                    } else if (j1 > 0) {
                        digit++;
                        done = true;
                    }
                    buffer.append(BASEDIGIT(digit));
                } while (!done);
                return buffer.toString();
            }
        }
    }

    static int word0(double d) {
        return (int) (Double.doubleToLongBits(d) >> 32);
    }

    static double setWord0(double d, int i) {
        return Double.longBitsToDouble((((long) i) << 32) | (4294967295L & Double.doubleToLongBits(d)));
    }

    static int word1(double d) {
        return (int) Double.doubleToLongBits(d);
    }

    static BigInteger pow5mult(BigInteger b, int k) {
        return b.multiply(BigInteger.valueOf(5).pow(k));
    }

    static boolean roundOff(StringBuilder buf) {
        int i = buf.length();
        while (i != 0) {
            i--;
            char c = buf.charAt(i);
            if (c != '9') {
                buf.setCharAt(i, (char) (c + 1));
                buf.setLength(i + 1);
                return false;
            }
        }
        buf.setLength(0);
        return true;
    }

    /* JADX WARNING: Missing block: B:46:0x0135, code skipped:
            r25 = false;
     */
    /* JADX WARNING: Missing block: B:47:0x0137, code skipped:
            if (r29 < 0) goto L_0x0346;
     */
    /* JADX WARNING: Missing block: B:49:0x013f, code skipped:
            if (r29 > 14) goto L_0x0346;
     */
    /* JADX WARNING: Missing block: B:50:0x0141, code skipped:
            if (r46 == false) goto L_0x0346;
     */
    /* JADX WARNING: Missing block: B:51:0x0143, code skipped:
            r26 = 0;
            r14 = r55;
            r35 = r34;
            r30 = r29;
            r28 = 2;
     */
    /* JADX WARNING: Missing block: B:52:0x014d, code skipped:
            if (r34 <= 0) goto L_0x025c;
     */
    /* JADX WARNING: Missing block: B:53:0x014f, code skipped:
            r21 = tens[r34 & 15];
            r32 = r34 >> 4;
     */
    /* JADX WARNING: Missing block: B:54:0x0159, code skipped:
            if ((r32 & 16) == 0) goto L_0x0167;
     */
    /* JADX WARNING: Missing block: B:55:0x015b, code skipped:
            r32 = r32 & 15;
            r55 = r55 / bigtens[4];
            r28 = 2 + 1;
     */
    /* JADX WARNING: Missing block: B:56:0x0167, code skipped:
            if (r32 == 0) goto L_0x0214;
     */
    /* JADX WARNING: Missing block: B:58:0x016b, code skipped:
            if ((r32 & 1) == 0) goto L_0x0175;
     */
    /* JADX WARNING: Missing block: B:59:0x016d, code skipped:
            r28 = r28 + 1;
            r21 = r21 * bigtens[r26];
     */
    /* JADX WARNING: Missing block: B:60:0x0175, code skipped:
            r32 = r32 >> 1;
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:70:0x01f8, code skipped:
            if (r59 > 0) goto L_0x01fc;
     */
    /* JADX WARNING: Missing block: B:71:0x01fa, code skipped:
            r59 = 1;
     */
    /* JADX WARNING: Missing block: B:72:0x01fc, code skipped:
            r26 = r59;
            r31 = r59;
            r29 = r59;
     */
    /* JADX WARNING: Missing block: B:74:0x0206, code skipped:
            r26 = (r59 + r34) + 1;
            r29 = r26;
            r31 = r26 - 1;
     */
    /* JADX WARNING: Missing block: B:75:0x020e, code skipped:
            if (r26 > 0) goto L_0x0135;
     */
    /* JADX WARNING: Missing block: B:77:0x0214, code skipped:
            r55 = r55 / r21;
     */
    /* JADX WARNING: Missing block: B:78:0x0216, code skipped:
            if (r36 == false) goto L_0x0224;
     */
    /* JADX WARNING: Missing block: B:80:0x021c, code skipped:
            if (r55 >= 1.0d) goto L_0x0224;
     */
    /* JADX WARNING: Missing block: B:81:0x021e, code skipped:
            if (r29 <= 0) goto L_0x0224;
     */
    /* JADX WARNING: Missing block: B:82:0x0220, code skipped:
            if (r31 > 0) goto L_0x0280;
     */
    /* JADX WARNING: Missing block: B:83:0x0222, code skipped:
            r25 = true;
     */
    /* JADX WARNING: Missing block: B:84:0x0224, code skipped:
            r23 = (((double) r28) * r55) + 7.0d;
            r23 = setWord0(r23, word0(r23) - 54525952);
     */
    /* JADX WARNING: Missing block: B:85:0x023f, code skipped:
            if (r29 != 0) goto L_0x02ac;
     */
    /* JADX WARNING: Missing block: B:86:0x0241, code skipped:
            r5 = null;
            r55 = r55 - 5.0d;
     */
    /* JADX WARNING: Missing block: B:87:0x024b, code skipped:
            if (r55 <= r23) goto L_0x028b;
     */
    /* JADX WARNING: Missing block: B:88:0x024d, code skipped:
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:89:0x025c, code skipped:
            r33 = -r34;
     */
    /* JADX WARNING: Missing block: B:90:0x0261, code skipped:
            if (r33 == 0) goto L_0x0216;
     */
    /* JADX WARNING: Missing block: B:91:0x0263, code skipped:
            r55 = r55 * tens[r33 & 15];
            r32 = r33 >> 4;
     */
    /* JADX WARNING: Missing block: B:92:0x026d, code skipped:
            if (r32 == 0) goto L_0x0216;
     */
    /* JADX WARNING: Missing block: B:94:0x0271, code skipped:
            if ((r32 & 1) == 0) goto L_0x027b;
     */
    /* JADX WARNING: Missing block: B:95:0x0273, code skipped:
            r28 = r28 + 1;
            r55 = r55 * bigtens[r26];
     */
    /* JADX WARNING: Missing block: B:96:0x027b, code skipped:
            r32 = r32 >> 1;
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:97:0x0280, code skipped:
            r29 = r31;
            r34 = r34 - 1;
            r55 = r55 * 10.0d;
            r28 = r28 + 1;
     */
    /* JADX WARNING: Missing block: B:99:0x0292, code skipped:
            if (r55 >= (-r23)) goto L_0x02aa;
     */
    /* JADX WARNING: Missing block: B:100:0x0294, code skipped:
            r61.setLength(0);
            r61.append('0');
     */
    /* JADX WARNING: Missing block: B:101:0x02aa, code skipped:
            r25 = true;
     */
    /* JADX WARNING: Missing block: B:102:0x02ac, code skipped:
            if (r25 != false) goto L_0x0335;
     */
    /* JADX WARNING: Missing block: B:103:0x02ae, code skipped:
            r25 = true;
     */
    /* JADX WARNING: Missing block: B:104:0x02b0, code skipped:
            if (r38 == false) goto L_0x0394;
     */
    /* JADX WARNING: Missing block: B:105:0x02b2, code skipped:
            r23 = (0.5d / tens[r29 - 1]) - r23;
            r26 = 0;
     */
    /* JADX WARNING: Missing block: B:106:0x02c0, code skipped:
            r3 = (long) r55;
            r55 = r55 - ((double) r3);
            r61.append((char) ((int) (48 + r3)));
     */
    /* JADX WARNING: Missing block: B:107:0x02df, code skipped:
            if (r55 >= r23) goto L_0x02e5;
     */
    /* JADX WARNING: Missing block: B:110:0x02eb, code skipped:
            if ((1.0d - r55) >= r23) goto L_0x032d;
     */
    /* JADX WARNING: Missing block: B:111:0x02ed, code skipped:
            r37 = r61.charAt(r61.length() - 1);
            r61.setLength(r61.length() - 1);
     */
    /* JADX WARNING: Missing block: B:112:0x030e, code skipped:
            if (r37 == '9') goto L_0x0322;
     */
    /* JADX WARNING: Missing block: B:113:0x0310, code skipped:
            r61.append((char) (r37 + 1));
     */
    /* JADX WARNING: Missing block: B:115:0x0326, code skipped:
            if (r61.length() != 0) goto L_0x02ed;
     */
    /* JADX WARNING: Missing block: B:116:0x0328, code skipped:
            r34 = r34 + 1;
            r37 = '0';
     */
    /* JADX WARNING: Missing block: B:117:0x032d, code skipped:
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:118:0x0333, code skipped:
            if (r26 < r29) goto L_0x038a;
     */
    /* JADX WARNING: Missing block: B:119:0x0335, code skipped:
            if (r25 == false) goto L_0x0346;
     */
    /* JADX WARNING: Missing block: B:120:0x0337, code skipped:
            r61.setLength(0);
            r55 = r14;
            r34 = r35;
            r29 = r30;
     */
    /* JADX WARNING: Missing block: B:122:0x034a, code skipped:
            if (r13[0] < 0) goto L_0x04bb;
     */
    /* JADX WARNING: Missing block: B:124:0x0352, code skipped:
            if (r34 > 14) goto L_0x04bb;
     */
    /* JADX WARNING: Missing block: B:125:0x0354, code skipped:
            r21 = tens[r34];
     */
    /* JADX WARNING: Missing block: B:126:0x0358, code skipped:
            if (r59 >= 0) goto L_0x042e;
     */
    /* JADX WARNING: Missing block: B:127:0x035a, code skipped:
            if (r29 > 0) goto L_0x042e;
     */
    /* JADX WARNING: Missing block: B:128:0x035c, code skipped:
            r5 = null;
     */
    /* JADX WARNING: Missing block: B:129:0x0360, code skipped:
            if (r29 < 0) goto L_0x0374;
     */
    /* JADX WARNING: Missing block: B:131:0x0368, code skipped:
            if (r55 < (5.0d * r21)) goto L_0x0374;
     */
    /* JADX WARNING: Missing block: B:132:0x036a, code skipped:
            if (r58 != false) goto L_0x041f;
     */
    /* JADX WARNING: Missing block: B:134:0x0372, code skipped:
            if (r55 != (5.0d * r21)) goto L_0x041f;
     */
    /* JADX WARNING: Missing block: B:135:0x0374, code skipped:
            r61.setLength(0);
            r61.append('0');
     */
    /* JADX WARNING: Missing block: B:136:0x038a, code skipped:
            r23 = r23 * 10.0d;
            r55 = r55 * 10.0d;
     */
    /* JADX WARNING: Missing block: B:137:0x0394, code skipped:
            r23 = r23 * tens[r29 - 1];
            r26 = 1;
     */
    /* JADX WARNING: Missing block: B:138:0x039e, code skipped:
            r3 = (long) r55;
            r55 = r55 - ((double) r3);
            r61.append((char) ((int) (48 + r3)));
     */
    /* JADX WARNING: Missing block: B:139:0x03bf, code skipped:
            if (r26 != r29) goto L_0x0418;
     */
    /* JADX WARNING: Missing block: B:141:0x03c7, code skipped:
            if (r55 <= (0.5d + r23)) goto L_0x0409;
     */
    /* JADX WARNING: Missing block: B:142:0x03c9, code skipped:
            r37 = r61.charAt(r61.length() - 1);
            r61.setLength(r61.length() - 1);
     */
    /* JADX WARNING: Missing block: B:143:0x03ea, code skipped:
            if (r37 == '9') goto L_0x03fe;
     */
    /* JADX WARNING: Missing block: B:144:0x03ec, code skipped:
            r61.append((char) (r37 + 1));
     */
    /* JADX WARNING: Missing block: B:146:0x0402, code skipped:
            if (r61.length() != 0) goto L_0x03c9;
     */
    /* JADX WARNING: Missing block: B:147:0x0404, code skipped:
            r34 = r34 + 1;
            r37 = '0';
     */
    /* JADX WARNING: Missing block: B:149:0x040f, code skipped:
            if (r55 >= (0.5d - r23)) goto L_0x0335;
     */
    /* JADX WARNING: Missing block: B:150:0x0411, code skipped:
            stripTrailingZeroes(r61);
     */
    /* JADX WARNING: Missing block: B:151:0x0418, code skipped:
            r26 = r26 + 1;
            r55 = r55 * 10.0d;
     */
    /* JADX WARNING: Missing block: B:152:0x041f, code skipped:
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:153:0x042e, code skipped:
            r26 = 1;
     */
    /* JADX WARNING: Missing block: B:154:0x0430, code skipped:
            r3 = (long) (r55 / r21);
            r55 = r55 - (((double) r3) * r21);
            r61.append((char) ((int) (48 + r3)));
     */
    /* JADX WARNING: Missing block: B:155:0x0455, code skipped:
            if (r26 != r29) goto L_0x04ad;
     */
    /* JADX WARNING: Missing block: B:156:0x0457, code skipped:
            r55 = r55 + r55;
     */
    /* JADX WARNING: Missing block: B:157:0x045b, code skipped:
            if (r55 > r21) goto L_0x046d;
     */
    /* JADX WARNING: Missing block: B:159:0x045f, code skipped:
            if (r55 != r21) goto L_0x049e;
     */
    /* JADX WARNING: Missing block: B:161:0x0469, code skipped:
            if ((1 & r3) != 0) goto L_0x046d;
     */
    /* JADX WARNING: Missing block: B:162:0x046b, code skipped:
            if (r58 == false) goto L_0x049e;
     */
    /* JADX WARNING: Missing block: B:163:0x046d, code skipped:
            r37 = r61.charAt(r61.length() - 1);
            r61.setLength(r61.length() - 1);
     */
    /* JADX WARNING: Missing block: B:164:0x048e, code skipped:
            if (r37 == '9') goto L_0x04a2;
     */
    /* JADX WARNING: Missing block: B:165:0x0490, code skipped:
            r61.append((char) (r37 + 1));
     */
    /* JADX WARNING: Missing block: B:168:0x04a6, code skipped:
            if (r61.length() != 0) goto L_0x046d;
     */
    /* JADX WARNING: Missing block: B:169:0x04a8, code skipped:
            r34 = r34 + 1;
            r37 = '0';
     */
    /* JADX WARNING: Missing block: B:170:0x04ad, code skipped:
            r55 = r55 * 10.0d;
     */
    /* JADX WARNING: Missing block: B:171:0x04b5, code skipped:
            if (r55 == 0.0d) goto L_0x049e;
     */
    /* JADX WARNING: Missing block: B:172:0x04b7, code skipped:
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:173:0x04bb, code skipped:
            r39 = r10;
            r40 = r11;
            r41 = null;
     */
    /* JADX WARNING: Missing block: B:174:0x04c3, code skipped:
            if (r38 == false) goto L_0x04e3;
     */
    /* JADX WARNING: Missing block: B:176:0x04cb, code skipped:
            if (r57 >= 2) goto L_0x057c;
     */
    /* JADX WARNING: Missing block: B:177:0x04cd, code skipped:
            if (r17 == false) goto L_0x0574;
     */
    /* JADX WARNING: Missing block: B:178:0x04cf, code skipped:
            r26 = r13[0] + 1075;
     */
    /* JADX WARNING: Missing block: B:179:0x04d9, code skipped:
            r10 = r10 + r26;
            r43 = r43 + r26;
            r41 = java.math.BigInteger.valueOf(1);
     */
    /* JADX WARNING: Missing block: B:180:0x04e3, code skipped:
            if (r39 <= 0) goto L_0x04f5;
     */
    /* JADX WARNING: Missing block: B:181:0x04e5, code skipped:
            if (r43 <= 0) goto L_0x04f5;
     */
    /* JADX WARNING: Missing block: B:183:0x04eb, code skipped:
            if (r39 >= r43) goto L_0x0599;
     */
    /* JADX WARNING: Missing block: B:184:0x04ed, code skipped:
            r26 = r39;
     */
    /* JADX WARNING: Missing block: B:185:0x04ef, code skipped:
            r10 = r10 - r26;
            r39 = r39 - r26;
            r43 = r43 - r26;
     */
    /* JADX WARNING: Missing block: B:186:0x04f5, code skipped:
            if (r11 <= 0) goto L_0x0514;
     */
    /* JADX WARNING: Missing block: B:187:0x04f7, code skipped:
            if (r38 == false) goto L_0x059d;
     */
    /* JADX WARNING: Missing block: B:188:0x04f9, code skipped:
            if (r40 <= 0) goto L_0x050a;
     */
    /* JADX WARNING: Missing block: B:189:0x04fb, code skipped:
            r41 = pow5mult(r41, r40);
            r8 = r41.multiply(r8);
     */
    /* JADX WARNING: Missing block: B:190:0x050a, code skipped:
            r32 = r11 - r40;
     */
    /* JADX WARNING: Missing block: B:191:0x050c, code skipped:
            if (r32 == 0) goto L_0x0514;
     */
    /* JADX WARNING: Missing block: B:192:0x050e, code skipped:
            r8 = pow5mult(r8, r32);
     */
    /* JADX WARNING: Missing block: B:193:0x0514, code skipped:
            r5 = java.math.BigInteger.valueOf(1);
     */
    /* JADX WARNING: Missing block: B:194:0x051a, code skipped:
            if (r44 <= 0) goto L_0x0522;
     */
    /* JADX WARNING: Missing block: B:195:0x051c, code skipped:
            r5 = pow5mult(r5, r44);
     */
    /* JADX WARNING: Missing block: B:196:0x0522, code skipped:
            r45 = false;
     */
    /* JADX WARNING: Missing block: B:197:0x052a, code skipped:
            if (r57 >= 2) goto L_0x054d;
     */
    /* JADX WARNING: Missing block: B:199:0x0530, code skipped:
            if (word1(r55) != 0) goto L_0x054d;
     */
    /* JADX WARNING: Missing block: B:201:0x053b, code skipped:
            if ((word0(r55) & 1048575) != 0) goto L_0x054d;
     */
    /* JADX WARNING: Missing block: B:203:0x0545, code skipped:
            if ((word0(r55) & 2145386496) == 0) goto L_0x054d;
     */
    /* JADX WARNING: Missing block: B:204:0x0547, code skipped:
            r10 = r10 + 1;
            r43 = r43 + 1;
            r45 = true;
     */
    /* JADX WARNING: Missing block: B:205:0x054d, code skipped:
            r6 = r5.toByteArray();
            r7 = 0;
            r27 = 0;
     */
    /* JADX WARNING: Missing block: B:207:0x055a, code skipped:
            if (r27 >= 4) goto L_0x05a3;
     */
    /* JADX WARNING: Missing block: B:208:0x055c, code skipped:
            r7 = r7 << 8;
     */
    /* JADX WARNING: Missing block: B:209:0x0565, code skipped:
            if (r27 >= r6.length) goto L_0x0571;
     */
    /* JADX WARNING: Missing block: B:210:0x0567, code skipped:
            r7 = r7 | (r6[r27] & org.mozilla.classfile.ByteCode.IMPDEP2);
     */
    /* JADX WARNING: Missing block: B:211:0x0571, code skipped:
            r27 = r27 + 1;
     */
    /* JADX WARNING: Missing block: B:212:0x0574, code skipped:
            r26 = 54 - r12[0];
     */
    /* JADX WARNING: Missing block: B:213:0x057c, code skipped:
            r32 = r29 - 1;
     */
    /* JADX WARNING: Missing block: B:214:0x0582, code skipped:
            if (r40 < r32) goto L_0x0590;
     */
    /* JADX WARNING: Missing block: B:215:0x0584, code skipped:
            r40 = r40 - r32;
     */
    /* JADX WARNING: Missing block: B:216:0x0586, code skipped:
            r26 = r29;
     */
    /* JADX WARNING: Missing block: B:217:0x0588, code skipped:
            if (r29 >= 0) goto L_0x04d9;
     */
    /* JADX WARNING: Missing block: B:218:0x058a, code skipped:
            r39 = r39 - r26;
            r26 = 0;
     */
    /* JADX WARNING: Missing block: B:219:0x0590, code skipped:
            r32 = r32 - r40;
            r44 = r44 + r32;
            r11 = r11 + r32;
            r40 = 0;
     */
    /* JADX WARNING: Missing block: B:220:0x0599, code skipped:
            r26 = r43;
     */
    /* JADX WARNING: Missing block: B:221:0x059d, code skipped:
            r8 = pow5mult(r8, r11);
     */
    /* JADX WARNING: Missing block: B:222:0x05a3, code skipped:
            if (r44 == 0) goto L_0x0631;
     */
    /* JADX WARNING: Missing block: B:223:0x05a5, code skipped:
            r49 = 32 - hi0bits(r7);
     */
    /* JADX WARNING: Missing block: B:224:0x05ab, code skipped:
            r26 = (r49 + r43) & 31;
     */
    /* JADX WARNING: Missing block: B:225:0x05af, code skipped:
            if (r26 == 0) goto L_0x05b3;
     */
    /* JADX WARNING: Missing block: B:226:0x05b1, code skipped:
            r26 = 32 - r26;
     */
    /* JADX WARNING: Missing block: B:228:0x05b9, code skipped:
            if (r26 <= 4) goto L_0x0635;
     */
    /* JADX WARNING: Missing block: B:229:0x05bb, code skipped:
            r26 = r26 - 4;
            r10 = r10 + r26;
            r39 = r39 + r26;
            r43 = r43 + r26;
     */
    /* JADX WARNING: Missing block: B:230:0x05c3, code skipped:
            if (r10 <= 0) goto L_0x05c9;
     */
    /* JADX WARNING: Missing block: B:231:0x05c5, code skipped:
            r8 = r8.shiftLeft(r10);
     */
    /* JADX WARNING: Missing block: B:232:0x05c9, code skipped:
            if (r43 <= 0) goto L_0x05d1;
     */
    /* JADX WARNING: Missing block: B:233:0x05cb, code skipped:
            r5 = r5.shiftLeft(r43);
     */
    /* JADX WARNING: Missing block: B:234:0x05d1, code skipped:
            if (r36 == false) goto L_0x05f9;
     */
    /* JADX WARNING: Missing block: B:236:0x05d7, code skipped:
            if (r8.compareTo(r5) >= 0) goto L_0x05f9;
     */
    /* JADX WARNING: Missing block: B:237:0x05d9, code skipped:
            r34 = r34 - 1;
            r8 = r8.multiply(java.math.BigInteger.valueOf(10));
     */
    /* JADX WARNING: Missing block: B:238:0x05e7, code skipped:
            if (r38 == false) goto L_0x05f7;
     */
    /* JADX WARNING: Missing block: B:239:0x05e9, code skipped:
            r41 = r41.multiply(java.math.BigInteger.valueOf(10));
     */
    /* JADX WARNING: Missing block: B:240:0x05f7, code skipped:
            r29 = r31;
     */
    /* JADX WARNING: Missing block: B:241:0x05f9, code skipped:
            if (r29 > 0) goto L_0x0656;
     */
    /* JADX WARNING: Missing block: B:243:0x0601, code skipped:
            if (r57 <= 2) goto L_0x0656;
     */
    /* JADX WARNING: Missing block: B:244:0x0603, code skipped:
            if (r29 < 0) goto L_0x061b;
     */
    /* JADX WARNING: Missing block: B:245:0x0605, code skipped:
            r26 = r8.compareTo(r5.multiply(java.math.BigInteger.valueOf(5)));
     */
    /* JADX WARNING: Missing block: B:246:0x0615, code skipped:
            if (r26 < 0) goto L_0x061b;
     */
    /* JADX WARNING: Missing block: B:247:0x0617, code skipped:
            if (r26 != 0) goto L_0x0647;
     */
    /* JADX WARNING: Missing block: B:248:0x0619, code skipped:
            if (r58 != false) goto L_0x0647;
     */
    /* JADX WARNING: Missing block: B:249:0x061b, code skipped:
            r61.setLength(0);
            r61.append('0');
     */
    /* JADX WARNING: Missing block: B:250:0x0631, code skipped:
            r49 = 1;
     */
    /* JADX WARNING: Missing block: B:252:0x063b, code skipped:
            if (r26 >= 4) goto L_0x05c3;
     */
    /* JADX WARNING: Missing block: B:253:0x063d, code skipped:
            r26 = r26 + 28;
            r10 = r10 + r26;
            r39 = r39 + r26;
            r43 = r43 + r26;
     */
    /* JADX WARNING: Missing block: B:254:0x0647, code skipped:
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:255:0x0656, code skipped:
            if (r38 == false) goto L_0x080d;
     */
    /* JADX WARNING: Missing block: B:256:0x0658, code skipped:
            if (r39 <= 0) goto L_0x0662;
     */
    /* JADX WARNING: Missing block: B:257:0x065a, code skipped:
            r41 = r41.shiftLeft(r39);
     */
    /* JADX WARNING: Missing block: B:258:0x0662, code skipped:
            r42 = r41;
     */
    /* JADX WARNING: Missing block: B:259:0x0664, code skipped:
            if (r45 == false) goto L_0x0672;
     */
    /* JADX WARNING: Missing block: B:260:0x0666, code skipped:
            r41 = r42.shiftLeft(1);
     */
    /* JADX WARNING: Missing block: B:261:0x0672, code skipped:
            r26 = 1;
     */
    /* JADX WARNING: Missing block: B:262:0x0674, code skipped:
            r20 = r8.divideAndRemainder(r5);
            r8 = r20[1];
            r18 = (char) (r20[0].intValue() + 48);
            r32 = r8.compareTo(r42);
            r16 = r5.subtract(r41);
     */
    /* JADX WARNING: Missing block: B:263:0x069b, code skipped:
            if (r16.signum() > 0) goto L_0x06d1;
     */
    /* JADX WARNING: Missing block: B:264:0x069d, code skipped:
            r33 = 1;
     */
    /* JADX WARNING: Missing block: B:265:0x069f, code skipped:
            if (r33 != 0) goto L_0x06ec;
     */
    /* JADX WARNING: Missing block: B:266:0x06a1, code skipped:
            if (r57 != 0) goto L_0x06ec;
     */
    /* JADX WARNING: Missing block: B:268:0x06a9, code skipped:
            if ((word1(r55) & 1) != 0) goto L_0x06ec;
     */
    /* JADX WARNING: Missing block: B:270:0x06b1, code skipped:
            if (r18 != '9') goto L_0x06d8;
     */
    /* JADX WARNING: Missing block: B:271:0x06b3, code skipped:
            r61.append('9');
     */
    /* JADX WARNING: Missing block: B:272:0x06c0, code skipped:
            if (roundOff(r61) == false) goto L_0x06cd;
     */
    /* JADX WARNING: Missing block: B:273:0x06c2, code skipped:
            r34 = r34 + 1;
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:275:0x06d1, code skipped:
            r33 = r8.compareTo(r16);
     */
    /* JADX WARNING: Missing block: B:276:0x06d8, code skipped:
            if (r32 <= 0) goto L_0x06e1;
     */
    /* JADX WARNING: Missing block: B:277:0x06da, code skipped:
            r18 = (char) (r18 + 1);
     */
    /* JADX WARNING: Missing block: B:278:0x06e1, code skipped:
            r61.append(r18);
     */
    /* JADX WARNING: Missing block: B:279:0x06ec, code skipped:
            if (r32 < 0) goto L_0x06fa;
     */
    /* JADX WARNING: Missing block: B:280:0x06ee, code skipped:
            if (r32 != 0) goto L_0x0752;
     */
    /* JADX WARNING: Missing block: B:281:0x06f0, code skipped:
            if (r57 != 0) goto L_0x0752;
     */
    /* JADX WARNING: Missing block: B:283:0x06f8, code skipped:
            if ((word1(r55) & 1) != 0) goto L_0x0752;
     */
    /* JADX WARNING: Missing block: B:284:0x06fa, code skipped:
            if (r33 <= 0) goto L_0x0747;
     */
    /* JADX WARNING: Missing block: B:285:0x06fc, code skipped:
            r33 = r8.shiftLeft(1).compareTo(r5);
     */
    /* JADX WARNING: Missing block: B:286:0x0708, code skipped:
            if (r33 > 0) goto L_0x0718;
     */
    /* JADX WARNING: Missing block: B:287:0x070a, code skipped:
            if (r33 != 0) goto L_0x0747;
     */
    /* JADX WARNING: Missing block: B:289:0x0714, code skipped:
            if ((r18 & 1) == 1) goto L_0x0718;
     */
    /* JADX WARNING: Missing block: B:290:0x0716, code skipped:
            if (r58 == false) goto L_0x0747;
     */
    /* JADX WARNING: Missing block: B:291:0x0718, code skipped:
            r19 = (char) (r18 + 1);
     */
    /* JADX WARNING: Missing block: B:292:0x0725, code skipped:
            if (r18 != '9') goto L_0x0745;
     */
    /* JADX WARNING: Missing block: B:293:0x0727, code skipped:
            r61.append('9');
     */
    /* JADX WARNING: Missing block: B:294:0x0734, code skipped:
            if (roundOff(r61) == false) goto L_0x0741;
     */
    /* JADX WARNING: Missing block: B:295:0x0736, code skipped:
            r34 = r34 + 1;
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:297:0x0745, code skipped:
            r18 = r19;
     */
    /* JADX WARNING: Missing block: B:298:0x0747, code skipped:
            r61.append(r18);
     */
    /* JADX WARNING: Missing block: B:299:0x0752, code skipped:
            if (r33 <= 0) goto L_0x078c;
     */
    /* JADX WARNING: Missing block: B:301:0x075a, code skipped:
            if (r18 != '9') goto L_0x077a;
     */
    /* JADX WARNING: Missing block: B:302:0x075c, code skipped:
            r61.append('9');
     */
    /* JADX WARNING: Missing block: B:303:0x0769, code skipped:
            if (roundOff(r61) == false) goto L_0x0776;
     */
    /* JADX WARNING: Missing block: B:304:0x076b, code skipped:
            r34 = r34 + 1;
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:306:0x077a, code skipped:
            r61.append((char) (r18 + 1));
     */
    /* JADX WARNING: Missing block: B:307:0x078c, code skipped:
            r61.append(r18);
     */
    /* JADX WARNING: Missing block: B:308:0x0797, code skipped:
            if (r26 != r29) goto L_0x07ca;
     */
    /* JADX WARNING: Missing block: B:309:0x0799, code skipped:
            r32 = r8.shiftLeft(1).compareTo(r5);
     */
    /* JADX WARNING: Missing block: B:310:0x07a5, code skipped:
            if (r32 > 0) goto L_0x07b5;
     */
    /* JADX WARNING: Missing block: B:311:0x07a7, code skipped:
            if (r32 != 0) goto L_0x0842;
     */
    /* JADX WARNING: Missing block: B:313:0x07b1, code skipped:
            if ((r18 & 1) == 1) goto L_0x07b5;
     */
    /* JADX WARNING: Missing block: B:314:0x07b3, code skipped:
            if (r58 == false) goto L_0x0842;
     */
    /* JADX WARNING: Missing block: B:316:0x07b9, code skipped:
            if (roundOff(r61) == false) goto L_0x0845;
     */
    /* JADX WARNING: Missing block: B:317:0x07bb, code skipped:
            r34 = r34 + 1;
            r61.append('1');
     */
    /* JADX WARNING: Missing block: B:318:0x07ca, code skipped:
            r8 = r8.multiply(java.math.BigInteger.valueOf(10));
     */
    /* JADX WARNING: Missing block: B:319:0x07da, code skipped:
            if (r42 != r41) goto L_0x07f0;
     */
    /* JADX WARNING: Missing block: B:320:0x07dc, code skipped:
            r41 = r41.multiply(java.math.BigInteger.valueOf(10));
            r42 = r41;
     */
    /* JADX WARNING: Missing block: B:321:0x07ec, code skipped:
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:322:0x07f0, code skipped:
            r42 = r42.multiply(java.math.BigInteger.valueOf(10));
            r41 = r41.multiply(java.math.BigInteger.valueOf(10));
     */
    /* JADX WARNING: Missing block: B:323:0x080d, code skipped:
            r26 = 1;
     */
    /* JADX WARNING: Missing block: B:324:0x080f, code skipped:
            r20 = r8.divideAndRemainder(r5);
            r8 = r20[1];
            r18 = (char) (r20[0].intValue() + 48);
            r61.append(r18);
     */
    /* JADX WARNING: Missing block: B:325:0x0831, code skipped:
            if (r26 >= r29) goto L_0x0799;
     */
    /* JADX WARNING: Missing block: B:326:0x0833, code skipped:
            r8 = r8.multiply(java.math.BigInteger.valueOf(10));
            r26 = r26 + 1;
     */
    /* JADX WARNING: Missing block: B:327:0x0842, code skipped:
            stripTrailingZeroes(r61);
     */
    /* JADX WARNING: Missing block: B:360:?, code skipped:
            return (r34 + 1) + 1;
     */
    /* JADX WARNING: Missing block: B:361:?, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:362:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:363:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:364:?, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:365:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:366:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:367:?, code skipped:
            return (r34 + 1) + 1;
     */
    /* JADX WARNING: Missing block: B:368:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:369:?, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:370:?, code skipped:
            return (r34 + 1) + 1;
     */
    /* JADX WARNING: Missing block: B:371:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:372:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:373:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:374:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:375:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:376:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:377:?, code skipped:
            return r34 + 1;
     */
    /* JADX WARNING: Missing block: B:378:?, code skipped:
            return r34 + 1;
     */
    static int JS_dtoa(double r55, int r57, boolean r58, int r59, boolean[] r60, java.lang.StringBuilder r61) {
        /*
        r49 = 1;
        r0 = r49;
        r13 = new int[r0];
        r49 = 1;
        r0 = r49;
        r12 = new int[r0];
        r49 = word0(r55);
        r50 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r49 = r49 & r50;
        if (r49 == 0) goto L_0x005a;
    L_0x0016:
        r49 = 0;
        r50 = 1;
        r60[r49] = r50;
        r49 = word0(r55);
        r50 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r49 = r49 & r50;
        r0 = r55;
        r2 = r49;
        r55 = setWord0(r0, r2);
    L_0x002d:
        r49 = word0(r55);
        r50 = 2146435072; // 0x7ff00000 float:NaN double:1.06047983E-314;
        r49 = r49 & r50;
        r50 = 2146435072; // 0x7ff00000 float:NaN double:1.06047983E-314;
        r0 = r49;
        r1 = r50;
        if (r0 != r1) goto L_0x0064;
    L_0x003d:
        r49 = word1(r55);
        if (r49 != 0) goto L_0x0061;
    L_0x0043:
        r49 = word0(r55);
        r50 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
        r49 = r49 & r50;
        if (r49 != 0) goto L_0x0061;
    L_0x004e:
        r49 = "Infinity";
    L_0x0050:
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
    L_0x0059:
        return r49;
    L_0x005a:
        r49 = 0;
        r50 = 0;
        r60[r49] = r50;
        goto L_0x002d;
    L_0x0061:
        r49 = "NaN";
        goto L_0x0050;
    L_0x0064:
        r49 = 0;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 != 0) goto L_0x007f;
    L_0x006a:
        r49 = 0;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 48;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = 1;
        goto L_0x0059;
    L_0x007f:
        r0 = r55;
        r8 = d2b(r0, r13, r12);
        r49 = word0(r55);
        r49 = r49 >>> 20;
        r0 = r49;
        r0 = r0 & 2047;
        r26 = r0;
        if (r26 == 0) goto L_0x017a;
    L_0x0093:
        r49 = word0(r55);
        r50 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
        r49 = r49 & r50;
        r50 = 1072693248; // 0x3ff00000 float:1.875 double:5.299808824E-315;
        r49 = r49 | r50;
        r0 = r55;
        r2 = r49;
        r14 = setWord0(r0, r2);
        r0 = r26;
        r0 = r0 + -1023;
        r26 = r0;
        r17 = 0;
    L_0x00b0:
        r49 = 4609434218613702656; // 0x3ff8000000000000 float:0.0 double:1.5;
        r49 = r14 - r49;
        r51 = 4598887322496222049; // 0x3fd287a7636f4361 float:4.413627E21 double:0.289529654602168;
        r49 = r49 * r51;
        r51 = 4595512376519870643; // 0x3fc68a288b60c8b3 float:-4.329182E-32 double:0.1760912590558;
        r49 = r49 + r51;
        r0 = r26;
        r0 = (double) r0;
        r51 = r0;
        r53 = 4599094494223104507; // 0x3fd34413509f79fb float:2.14045716E10 double:0.301029995663981;
        r51 = r51 * r53;
        r21 = r49 + r51;
        r0 = r21;
        r0 = (int) r0;
        r34 = r0;
        r49 = 0;
        r49 = (r21 > r49 ? 1 : (r21 == r49 ? 0 : -1));
        if (r49 >= 0) goto L_0x00e6;
    L_0x00db:
        r0 = r34;
        r0 = (double) r0;
        r49 = r0;
        r49 = (r21 > r49 ? 1 : (r21 == r49 ? 0 : -1));
        if (r49 == 0) goto L_0x00e6;
    L_0x00e4:
        r34 = r34 + -1;
    L_0x00e6:
        r36 = 1;
        if (r34 < 0) goto L_0x00fe;
    L_0x00ea:
        r49 = 22;
        r0 = r34;
        r1 = r49;
        if (r0 > r1) goto L_0x00fe;
    L_0x00f2:
        r49 = tens;
        r49 = r49[r34];
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 >= 0) goto L_0x00fc;
    L_0x00fa:
        r34 = r34 + -1;
    L_0x00fc:
        r36 = 0;
    L_0x00fe:
        r49 = 0;
        r49 = r12[r49];
        r49 = r49 - r26;
        r32 = r49 + -1;
        if (r32 < 0) goto L_0x01dc;
    L_0x0108:
        r10 = 0;
        r43 = r32;
    L_0x010b:
        if (r34 < 0) goto L_0x01e3;
    L_0x010d:
        r11 = 0;
        r44 = r34;
        r43 = r43 + r34;
    L_0x0112:
        if (r57 < 0) goto L_0x011c;
    L_0x0114:
        r49 = 9;
        r0 = r57;
        r1 = r49;
        if (r0 <= r1) goto L_0x011e;
    L_0x011c:
        r57 = 0;
    L_0x011e:
        r46 = 1;
        r49 = 5;
        r0 = r57;
        r1 = r49;
        if (r0 <= r1) goto L_0x012c;
    L_0x0128:
        r57 = r57 + -4;
        r46 = 0;
    L_0x012c:
        r38 = 1;
        r31 = 0;
        r29 = r31;
        switch(r57) {
            case 0: goto L_0x01ec;
            case 1: goto L_0x01ec;
            case 2: goto L_0x01f6;
            case 3: goto L_0x0204;
            case 4: goto L_0x01f8;
            case 5: goto L_0x0206;
            default: goto L_0x0135;
        };
    L_0x0135:
        r25 = 0;
        if (r29 < 0) goto L_0x0346;
    L_0x0139:
        r49 = 14;
        r0 = r29;
        r1 = r49;
        if (r0 > r1) goto L_0x0346;
    L_0x0141:
        if (r46 == 0) goto L_0x0346;
    L_0x0143:
        r26 = 0;
        r14 = r55;
        r35 = r34;
        r30 = r29;
        r28 = 2;
        if (r34 <= 0) goto L_0x025c;
    L_0x014f:
        r49 = tens;
        r50 = r34 & 15;
        r21 = r49[r50];
        r32 = r34 >> 4;
        r49 = r32 & 16;
        if (r49 == 0) goto L_0x0167;
    L_0x015b:
        r32 = r32 & 15;
        r49 = bigtens;
        r50 = 4;
        r49 = r49[r50];
        r55 = r55 / r49;
        r28 = r28 + 1;
    L_0x0167:
        if (r32 == 0) goto L_0x0214;
    L_0x0169:
        r49 = r32 & 1;
        if (r49 == 0) goto L_0x0175;
    L_0x016d:
        r28 = r28 + 1;
        r49 = bigtens;
        r49 = r49[r26];
        r21 = r21 * r49;
    L_0x0175:
        r32 = r32 >> 1;
        r26 = r26 + 1;
        goto L_0x0167;
    L_0x017a:
        r49 = 0;
        r49 = r12[r49];
        r50 = 0;
        r50 = r13[r50];
        r49 = r49 + r50;
        r0 = r49;
        r0 = r0 + 1074;
        r26 = r0;
        r49 = 32;
        r0 = r26;
        r1 = r49;
        if (r0 <= r1) goto L_0x01ce;
    L_0x0192:
        r49 = word0(r55);
        r0 = r49;
        r0 = (long) r0;
        r49 = r0;
        r51 = 64 - r26;
        r49 = r49 << r51;
        r51 = word1(r55);
        r52 = r26 + -32;
        r51 = r51 >>> r52;
        r0 = r51;
        r0 = (long) r0;
        r51 = r0;
        r47 = r49 | r51;
    L_0x01ae:
        r0 = r47;
        r0 = (double) r0;
        r49 = r0;
        r0 = r47;
        r0 = (double) r0;
        r51 = r0;
        r51 = word0(r51);
        r52 = 32505856; // 0x1f00000 float:8.8162076E-38 double:1.60600267E-316;
        r51 = r51 - r52;
        r14 = setWord0(r49, r51);
        r0 = r26;
        r0 = r0 + -1075;
        r26 = r0;
        r17 = 1;
        goto L_0x00b0;
    L_0x01ce:
        r49 = word1(r55);
        r0 = r49;
        r0 = (long) r0;
        r49 = r0;
        r51 = 32 - r26;
        r47 = r49 << r51;
        goto L_0x01ae;
    L_0x01dc:
        r0 = r32;
        r10 = -r0;
        r43 = 0;
        goto L_0x010b;
    L_0x01e3:
        r10 = r10 - r34;
        r0 = r34;
        r11 = -r0;
        r44 = 0;
        goto L_0x0112;
    L_0x01ec:
        r31 = -1;
        r29 = r31;
        r26 = 18;
        r59 = 0;
        goto L_0x0135;
    L_0x01f6:
        r38 = 0;
    L_0x01f8:
        if (r59 > 0) goto L_0x01fc;
    L_0x01fa:
        r59 = 1;
    L_0x01fc:
        r26 = r59;
        r31 = r59;
        r29 = r59;
        goto L_0x0135;
    L_0x0204:
        r38 = 0;
    L_0x0206:
        r49 = r59 + r34;
        r26 = r49 + 1;
        r29 = r26;
        r31 = r26 + -1;
        if (r26 > 0) goto L_0x0135;
    L_0x0210:
        r26 = 1;
        goto L_0x0135;
    L_0x0214:
        r55 = r55 / r21;
    L_0x0216:
        if (r36 == 0) goto L_0x0224;
    L_0x0218:
        r49 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 >= 0) goto L_0x0224;
    L_0x021e:
        if (r29 <= 0) goto L_0x0224;
    L_0x0220:
        if (r31 > 0) goto L_0x0280;
    L_0x0222:
        r25 = 1;
    L_0x0224:
        r0 = r28;
        r0 = (double) r0;
        r49 = r0;
        r49 = r49 * r55;
        r51 = 4619567317775286272; // 0x401c000000000000 float:0.0 double:7.0;
        r23 = r49 + r51;
        r49 = word0(r23);
        r50 = 54525952; // 0x3400000 float:5.642373E-37 double:2.69393997E-316;
        r49 = r49 - r50;
        r0 = r23;
        r2 = r49;
        r23 = setWord0(r0, r2);
        if (r29 != 0) goto L_0x02ac;
    L_0x0241:
        r41 = 0;
        r5 = r41;
        r49 = 4617315517961601024; // 0x4014000000000000 float:0.0 double:5.0;
        r55 = r55 - r49;
        r49 = (r55 > r23 ? 1 : (r55 == r23 ? 0 : -1));
        if (r49 <= 0) goto L_0x028b;
    L_0x024d:
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r34 = r34 + 1;
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x025c:
        r0 = r34;
        r0 = -r0;
        r33 = r0;
        if (r33 == 0) goto L_0x0216;
    L_0x0263:
        r49 = tens;
        r50 = r33 & 15;
        r49 = r49[r50];
        r55 = r55 * r49;
        r32 = r33 >> 4;
    L_0x026d:
        if (r32 == 0) goto L_0x0216;
    L_0x026f:
        r49 = r32 & 1;
        if (r49 == 0) goto L_0x027b;
    L_0x0273:
        r28 = r28 + 1;
        r49 = bigtens;
        r49 = r49[r26];
        r55 = r55 * r49;
    L_0x027b:
        r32 = r32 >> 1;
        r26 = r26 + 1;
        goto L_0x026d;
    L_0x0280:
        r29 = r31;
        r34 = r34 + -1;
        r49 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r55 = r55 * r49;
        r28 = r28 + 1;
        goto L_0x0224;
    L_0x028b:
        r0 = r23;
        r0 = -r0;
        r49 = r0;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 >= 0) goto L_0x02aa;
    L_0x0294:
        r49 = 0;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 48;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = 1;
        goto L_0x0059;
    L_0x02aa:
        r25 = 1;
    L_0x02ac:
        if (r25 != 0) goto L_0x0335;
    L_0x02ae:
        r25 = 1;
        if (r38 == 0) goto L_0x0394;
    L_0x02b2:
        r49 = 4602678819172646912; // 0x3fe0000000000000 float:0.0 double:0.5;
        r51 = tens;
        r52 = r29 + -1;
        r51 = r51[r52];
        r49 = r49 / r51;
        r23 = r49 - r23;
        r26 = 0;
    L_0x02c0:
        r0 = r55;
        r3 = (long) r0;
        r0 = (double) r3;
        r49 = r0;
        r55 = r55 - r49;
        r49 = 48;
        r49 = r49 + r3;
        r0 = r49;
        r0 = (int) r0;
        r49 = r0;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = (r55 > r23 ? 1 : (r55 == r23 ? 0 : -1));
        if (r49 >= 0) goto L_0x02e5;
    L_0x02e1:
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x02e5:
        r49 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r49 = r49 - r55;
        r49 = (r49 > r23 ? 1 : (r49 == r23 ? 0 : -1));
        if (r49 >= 0) goto L_0x032d;
    L_0x02ed:
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r37 = r0.charAt(r1);
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 57;
        r0 = r37;
        r1 = r49;
        if (r0 == r1) goto L_0x0322;
    L_0x0310:
        r49 = r37 + 1;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x0322:
        r49 = r61.length();
        if (r49 != 0) goto L_0x02ed;
    L_0x0328:
        r34 = r34 + 1;
        r37 = 48;
        goto L_0x0310;
    L_0x032d:
        r26 = r26 + 1;
        r0 = r26;
        r1 = r29;
        if (r0 < r1) goto L_0x038a;
    L_0x0335:
        if (r25 == 0) goto L_0x0346;
    L_0x0337:
        r49 = 0;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r55 = r14;
        r34 = r35;
        r29 = r30;
    L_0x0346:
        r49 = 0;
        r49 = r13[r49];
        if (r49 < 0) goto L_0x04bb;
    L_0x034c:
        r49 = 14;
        r0 = r34;
        r1 = r49;
        if (r0 > r1) goto L_0x04bb;
    L_0x0354:
        r49 = tens;
        r21 = r49[r34];
        if (r59 >= 0) goto L_0x042e;
    L_0x035a:
        if (r29 > 0) goto L_0x042e;
    L_0x035c:
        r41 = 0;
        r5 = r41;
        if (r29 < 0) goto L_0x0374;
    L_0x0362:
        r49 = 4617315517961601024; // 0x4014000000000000 float:0.0 double:5.0;
        r49 = r49 * r21;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 < 0) goto L_0x0374;
    L_0x036a:
        if (r58 != 0) goto L_0x041f;
    L_0x036c:
        r49 = 4617315517961601024; // 0x4014000000000000 float:0.0 double:5.0;
        r49 = r49 * r21;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 != 0) goto L_0x041f;
    L_0x0374:
        r49 = 0;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 48;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = 1;
        goto L_0x0059;
    L_0x038a:
        r49 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r23 = r23 * r49;
        r49 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r55 = r55 * r49;
        goto L_0x02c0;
    L_0x0394:
        r49 = tens;
        r50 = r29 + -1;
        r49 = r49[r50];
        r23 = r23 * r49;
        r26 = 1;
    L_0x039e:
        r0 = r55;
        r3 = (long) r0;
        r0 = (double) r3;
        r49 = r0;
        r55 = r55 - r49;
        r49 = 48;
        r49 = r49 + r3;
        r0 = r49;
        r0 = (int) r0;
        r49 = r0;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r0 = r26;
        r1 = r29;
        if (r0 != r1) goto L_0x0418;
    L_0x03c1:
        r49 = 4602678819172646912; // 0x3fe0000000000000 float:0.0 double:0.5;
        r49 = r49 + r23;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 <= 0) goto L_0x0409;
    L_0x03c9:
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r37 = r0.charAt(r1);
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 57;
        r0 = r37;
        r1 = r49;
        if (r0 == r1) goto L_0x03fe;
    L_0x03ec:
        r49 = r37 + 1;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x03fe:
        r49 = r61.length();
        if (r49 != 0) goto L_0x03c9;
    L_0x0404:
        r34 = r34 + 1;
        r37 = 48;
        goto L_0x03ec;
    L_0x0409:
        r49 = 4602678819172646912; // 0x3fe0000000000000 float:0.0 double:0.5;
        r49 = r49 - r23;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 >= 0) goto L_0x0335;
    L_0x0411:
        stripTrailingZeroes(r61);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x0418:
        r26 = r26 + 1;
        r49 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r55 = r55 * r49;
        goto L_0x039e;
    L_0x041f:
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r34 = r34 + 1;
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x042e:
        r26 = 1;
    L_0x0430:
        r49 = r55 / r21;
        r0 = r49;
        r3 = (long) r0;
        r0 = (double) r3;
        r49 = r0;
        r49 = r49 * r21;
        r55 = r55 - r49;
        r49 = 48;
        r49 = r49 + r3;
        r0 = r49;
        r0 = (int) r0;
        r49 = r0;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r0 = r26;
        r1 = r29;
        if (r0 != r1) goto L_0x04ad;
    L_0x0457:
        r55 = r55 + r55;
        r49 = (r55 > r21 ? 1 : (r55 == r21 ? 0 : -1));
        if (r49 > 0) goto L_0x046d;
    L_0x045d:
        r49 = (r55 > r21 ? 1 : (r55 == r21 ? 0 : -1));
        if (r49 != 0) goto L_0x049e;
    L_0x0461:
        r49 = 1;
        r49 = r49 & r3;
        r51 = 0;
        r49 = (r49 > r51 ? 1 : (r49 == r51 ? 0 : -1));
        if (r49 != 0) goto L_0x046d;
    L_0x046b:
        if (r58 == 0) goto L_0x049e;
    L_0x046d:
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r37 = r0.charAt(r1);
        r49 = r61.length();
        r49 = r49 + -1;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 57;
        r0 = r37;
        r1 = r49;
        if (r0 == r1) goto L_0x04a2;
    L_0x0490:
        r49 = r37 + 1;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
    L_0x049e:
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x04a2:
        r49 = r61.length();
        if (r49 != 0) goto L_0x046d;
    L_0x04a8:
        r34 = r34 + 1;
        r37 = 48;
        goto L_0x0490;
    L_0x04ad:
        r49 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r55 = r55 * r49;
        r49 = 0;
        r49 = (r55 > r49 ? 1 : (r55 == r49 ? 0 : -1));
        if (r49 == 0) goto L_0x049e;
    L_0x04b7:
        r26 = r26 + 1;
        goto L_0x0430;
    L_0x04bb:
        r39 = r10;
        r40 = r11;
        r42 = 0;
        r41 = r42;
        if (r38 == 0) goto L_0x04e3;
    L_0x04c5:
        r49 = 2;
        r0 = r57;
        r1 = r49;
        if (r0 >= r1) goto L_0x057c;
    L_0x04cd:
        if (r17 == 0) goto L_0x0574;
    L_0x04cf:
        r49 = 0;
        r49 = r13[r49];
        r0 = r49;
        r0 = r0 + 1075;
        r26 = r0;
    L_0x04d9:
        r10 = r10 + r26;
        r43 = r43 + r26;
        r49 = 1;
        r41 = java.math.BigInteger.valueOf(r49);
    L_0x04e3:
        if (r39 <= 0) goto L_0x04f5;
    L_0x04e5:
        if (r43 <= 0) goto L_0x04f5;
    L_0x04e7:
        r0 = r39;
        r1 = r43;
        if (r0 >= r1) goto L_0x0599;
    L_0x04ed:
        r26 = r39;
    L_0x04ef:
        r10 = r10 - r26;
        r39 = r39 - r26;
        r43 = r43 - r26;
    L_0x04f5:
        if (r11 <= 0) goto L_0x0514;
    L_0x04f7:
        if (r38 == 0) goto L_0x059d;
    L_0x04f9:
        if (r40 <= 0) goto L_0x050a;
    L_0x04fb:
        r0 = r41;
        r1 = r40;
        r41 = pow5mult(r0, r1);
        r0 = r41;
        r9 = r0.multiply(r8);
        r8 = r9;
    L_0x050a:
        r32 = r11 - r40;
        if (r32 == 0) goto L_0x0514;
    L_0x050e:
        r0 = r32;
        r8 = pow5mult(r8, r0);
    L_0x0514:
        r49 = 1;
        r5 = java.math.BigInteger.valueOf(r49);
        if (r44 <= 0) goto L_0x0522;
    L_0x051c:
        r0 = r44;
        r5 = pow5mult(r5, r0);
    L_0x0522:
        r45 = 0;
        r49 = 2;
        r0 = r57;
        r1 = r49;
        if (r0 >= r1) goto L_0x054d;
    L_0x052c:
        r49 = word1(r55);
        if (r49 != 0) goto L_0x054d;
    L_0x0532:
        r49 = word0(r55);
        r50 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
        r49 = r49 & r50;
        if (r49 != 0) goto L_0x054d;
    L_0x053d:
        r49 = word0(r55);
        r50 = 2145386496; // 0x7fe00000 float:NaN double:1.0599617647E-314;
        r49 = r49 & r50;
        if (r49 == 0) goto L_0x054d;
    L_0x0547:
        r10 = r10 + 1;
        r43 = r43 + 1;
        r45 = 1;
    L_0x054d:
        r6 = r5.toByteArray();
        r7 = 0;
        r27 = 0;
    L_0x0554:
        r49 = 4;
        r0 = r27;
        r1 = r49;
        if (r0 >= r1) goto L_0x05a3;
    L_0x055c:
        r7 = r7 << 8;
        r0 = r6.length;
        r49 = r0;
        r0 = r27;
        r1 = r49;
        if (r0 >= r1) goto L_0x0571;
    L_0x0567:
        r49 = r6[r27];
        r0 = r49;
        r0 = r0 & 255;
        r49 = r0;
        r7 = r7 | r49;
    L_0x0571:
        r27 = r27 + 1;
        goto L_0x0554;
    L_0x0574:
        r49 = 0;
        r49 = r12[r49];
        r26 = 54 - r49;
        goto L_0x04d9;
    L_0x057c:
        r32 = r29 + -1;
        r0 = r40;
        r1 = r32;
        if (r0 < r1) goto L_0x0590;
    L_0x0584:
        r40 = r40 - r32;
    L_0x0586:
        r26 = r29;
        if (r29 >= 0) goto L_0x04d9;
    L_0x058a:
        r39 = r39 - r26;
        r26 = 0;
        goto L_0x04d9;
    L_0x0590:
        r32 = r32 - r40;
        r44 = r44 + r32;
        r11 = r11 + r32;
        r40 = 0;
        goto L_0x0586;
    L_0x0599:
        r26 = r43;
        goto L_0x04ef;
    L_0x059d:
        r8 = pow5mult(r8, r11);
        goto L_0x0514;
    L_0x05a3:
        if (r44 == 0) goto L_0x0631;
    L_0x05a5:
        r49 = hi0bits(r7);
        r49 = 32 - r49;
    L_0x05ab:
        r49 = r49 + r43;
        r26 = r49 & 31;
        if (r26 == 0) goto L_0x05b3;
    L_0x05b1:
        r26 = 32 - r26;
    L_0x05b3:
        r49 = 4;
        r0 = r26;
        r1 = r49;
        if (r0 <= r1) goto L_0x0635;
    L_0x05bb:
        r26 = r26 + -4;
        r10 = r10 + r26;
        r39 = r39 + r26;
        r43 = r43 + r26;
    L_0x05c3:
        if (r10 <= 0) goto L_0x05c9;
    L_0x05c5:
        r8 = r8.shiftLeft(r10);
    L_0x05c9:
        if (r43 <= 0) goto L_0x05d1;
    L_0x05cb:
        r0 = r43;
        r5 = r5.shiftLeft(r0);
    L_0x05d1:
        if (r36 == 0) goto L_0x05f9;
    L_0x05d3:
        r49 = r8.compareTo(r5);
        if (r49 >= 0) goto L_0x05f9;
    L_0x05d9:
        r34 = r34 + -1;
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r49;
        r8 = r8.multiply(r0);
        if (r38 == 0) goto L_0x05f7;
    L_0x05e9:
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r41;
        r1 = r49;
        r41 = r0.multiply(r1);
    L_0x05f7:
        r29 = r31;
    L_0x05f9:
        if (r29 > 0) goto L_0x0656;
    L_0x05fb:
        r49 = 2;
        r0 = r57;
        r1 = r49;
        if (r0 <= r1) goto L_0x0656;
    L_0x0603:
        if (r29 < 0) goto L_0x061b;
    L_0x0605:
        r49 = 5;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r49;
        r5 = r5.multiply(r0);
        r26 = r8.compareTo(r5);
        if (r26 < 0) goto L_0x061b;
    L_0x0617:
        if (r26 != 0) goto L_0x0647;
    L_0x0619:
        if (r58 != 0) goto L_0x0647;
    L_0x061b:
        r49 = 0;
        r0 = r61;
        r1 = r49;
        r0.setLength(r1);
        r49 = 48;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = 1;
        goto L_0x0059;
    L_0x0631:
        r49 = 1;
        goto L_0x05ab;
    L_0x0635:
        r49 = 4;
        r0 = r26;
        r1 = r49;
        if (r0 >= r1) goto L_0x05c3;
    L_0x063d:
        r26 = r26 + 28;
        r10 = r10 + r26;
        r39 = r39 + r26;
        r43 = r43 + r26;
        goto L_0x05c3;
    L_0x0647:
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r34 = r34 + 1;
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x0656:
        if (r38 == 0) goto L_0x080d;
    L_0x0658:
        if (r39 <= 0) goto L_0x0662;
    L_0x065a:
        r0 = r41;
        r1 = r39;
        r41 = r0.shiftLeft(r1);
    L_0x0662:
        r42 = r41;
        if (r45 == 0) goto L_0x0672;
    L_0x0666:
        r41 = r42;
        r49 = 1;
        r0 = r41;
        r1 = r49;
        r41 = r0.shiftLeft(r1);
    L_0x0672:
        r26 = 1;
    L_0x0674:
        r20 = r8.divideAndRemainder(r5);
        r49 = 1;
        r8 = r20[r49];
        r49 = 0;
        r49 = r20[r49];
        r49 = r49.intValue();
        r49 = r49 + 48;
        r0 = r49;
        r0 = (char) r0;
        r18 = r0;
        r0 = r42;
        r32 = r8.compareTo(r0);
        r0 = r41;
        r16 = r5.subtract(r0);
        r49 = r16.signum();
        if (r49 > 0) goto L_0x06d1;
    L_0x069d:
        r33 = 1;
    L_0x069f:
        if (r33 != 0) goto L_0x06ec;
    L_0x06a1:
        if (r57 != 0) goto L_0x06ec;
    L_0x06a3:
        r49 = word1(r55);
        r49 = r49 & 1;
        if (r49 != 0) goto L_0x06ec;
    L_0x06ab:
        r49 = 57;
        r0 = r18;
        r1 = r49;
        if (r0 != r1) goto L_0x06d8;
    L_0x06b3:
        r49 = 57;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = roundOff(r61);
        if (r49 == 0) goto L_0x06cd;
    L_0x06c2:
        r34 = r34 + 1;
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
    L_0x06cd:
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x06d1:
        r0 = r16;
        r33 = r8.compareTo(r0);
        goto L_0x069f;
    L_0x06d8:
        if (r32 <= 0) goto L_0x06e1;
    L_0x06da:
        r49 = r18 + 1;
        r0 = r49;
        r0 = (char) r0;
        r18 = r0;
    L_0x06e1:
        r0 = r61;
        r1 = r18;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x06ec:
        if (r32 < 0) goto L_0x06fa;
    L_0x06ee:
        if (r32 != 0) goto L_0x0752;
    L_0x06f0:
        if (r57 != 0) goto L_0x0752;
    L_0x06f2:
        r49 = word1(r55);
        r49 = r49 & 1;
        if (r49 != 0) goto L_0x0752;
    L_0x06fa:
        if (r33 <= 0) goto L_0x0747;
    L_0x06fc:
        r49 = 1;
        r0 = r49;
        r8 = r8.shiftLeft(r0);
        r33 = r8.compareTo(r5);
        if (r33 > 0) goto L_0x0718;
    L_0x070a:
        if (r33 != 0) goto L_0x0747;
    L_0x070c:
        r49 = r18 & 1;
        r50 = 1;
        r0 = r49;
        r1 = r50;
        if (r0 == r1) goto L_0x0718;
    L_0x0716:
        if (r58 == 0) goto L_0x0747;
    L_0x0718:
        r49 = r18 + 1;
        r0 = r49;
        r0 = (char) r0;
        r19 = r0;
        r49 = 57;
        r0 = r18;
        r1 = r49;
        if (r0 != r1) goto L_0x0745;
    L_0x0727:
        r49 = 57;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = roundOff(r61);
        if (r49 == 0) goto L_0x0741;
    L_0x0736:
        r34 = r34 + 1;
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
    L_0x0741:
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x0745:
        r18 = r19;
    L_0x0747:
        r0 = r61;
        r1 = r18;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x0752:
        if (r33 <= 0) goto L_0x078c;
    L_0x0754:
        r49 = 57;
        r0 = r18;
        r1 = r49;
        if (r0 != r1) goto L_0x077a;
    L_0x075c:
        r49 = 57;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = roundOff(r61);
        if (r49 == 0) goto L_0x0776;
    L_0x076b:
        r34 = r34 + 1;
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
    L_0x0776:
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x077a:
        r49 = r18 + 1;
        r0 = r49;
        r0 = (char) r0;
        r49 = r0;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x078c:
        r0 = r61;
        r1 = r18;
        r0.append(r1);
        r0 = r26;
        r1 = r29;
        if (r0 != r1) goto L_0x07ca;
    L_0x0799:
        r49 = 1;
        r0 = r49;
        r8 = r8.shiftLeft(r0);
        r32 = r8.compareTo(r5);
        if (r32 > 0) goto L_0x07b5;
    L_0x07a7:
        if (r32 != 0) goto L_0x0842;
    L_0x07a9:
        r49 = r18 & 1;
        r50 = 1;
        r0 = r49;
        r1 = r50;
        if (r0 == r1) goto L_0x07b5;
    L_0x07b3:
        if (r58 == 0) goto L_0x0842;
    L_0x07b5:
        r49 = roundOff(r61);
        if (r49 == 0) goto L_0x0845;
    L_0x07bb:
        r34 = r34 + 1;
        r49 = 49;
        r0 = r61;
        r1 = r49;
        r0.append(r1);
        r49 = r34 + 1;
        goto L_0x0059;
    L_0x07ca:
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r49;
        r8 = r8.multiply(r0);
        r0 = r42;
        r1 = r41;
        if (r0 != r1) goto L_0x07f0;
    L_0x07dc:
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r41;
        r1 = r49;
        r41 = r0.multiply(r1);
        r42 = r41;
    L_0x07ec:
        r26 = r26 + 1;
        goto L_0x0674;
    L_0x07f0:
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r42;
        r1 = r49;
        r42 = r0.multiply(r1);
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r41;
        r1 = r49;
        r41 = r0.multiply(r1);
        goto L_0x07ec;
    L_0x080d:
        r26 = 1;
    L_0x080f:
        r20 = r8.divideAndRemainder(r5);
        r49 = 1;
        r8 = r20[r49];
        r49 = 0;
        r49 = r20[r49];
        r49 = r49.intValue();
        r49 = r49 + 48;
        r0 = r49;
        r0 = (char) r0;
        r18 = r0;
        r0 = r61;
        r1 = r18;
        r0.append(r1);
        r0 = r26;
        r1 = r29;
        if (r0 >= r1) goto L_0x0799;
    L_0x0833:
        r49 = 10;
        r49 = java.math.BigInteger.valueOf(r49);
        r0 = r49;
        r8 = r8.multiply(r0);
        r26 = r26 + 1;
        goto L_0x080f;
    L_0x0842:
        stripTrailingZeroes(r61);
    L_0x0845:
        r49 = r34 + 1;
        goto L_0x0059;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.DToA.JS_dtoa(double, int, boolean, int, boolean[], java.lang.StringBuilder):int");
    }

    private static void stripTrailingZeroes(StringBuilder buf) {
        int bl = buf.length();
        while (true) {
            int bl2 = bl;
            bl = bl2 - 1;
            if (bl2 <= 0 || buf.charAt(bl) != '0') {
                buf.setLength(bl + 1);
            }
        }
        buf.setLength(bl + 1);
    }

    static void JS_dtostr(StringBuilder buffer, int mode, int precision, double d) {
        boolean[] sign = new boolean[1];
        if (mode == 2 && (d >= 1.0E21d || d <= -1.0E21d)) {
            mode = 0;
        }
        int decPt = JS_dtoa(d, dtoaModes[mode], mode >= 2, precision, sign, buffer);
        int nDigits = buffer.length();
        if (decPt != 9999) {
            boolean exponentialNotation = false;
            int minNDigits = 0;
            switch (mode) {
                case 0:
                    if (decPt >= -5 && decPt <= 21) {
                        minNDigits = decPt;
                        break;
                    } else {
                        exponentialNotation = true;
                        break;
                    }
                case 1:
                    break;
                case 2:
                    if (precision < 0) {
                        minNDigits = decPt;
                        break;
                    } else {
                        minNDigits = decPt + precision;
                        break;
                    }
                case 3:
                    minNDigits = precision;
                    break;
                case 4:
                    minNDigits = precision;
                    if (decPt < -5 || decPt > precision) {
                        exponentialNotation = true;
                        break;
                    }
            }
            exponentialNotation = true;
            if (nDigits < minNDigits) {
                int p = minNDigits;
                nDigits = minNDigits;
                do {
                    buffer.append('0');
                } while (buffer.length() != p);
            }
            if (exponentialNotation) {
                if (nDigits != 1) {
                    buffer.insert(1, '.');
                }
                buffer.append('e');
                if (decPt - 1 >= 0) {
                    buffer.append(SignatureVisitor.EXTENDS);
                }
                buffer.append(decPt - 1);
            } else if (decPt != nDigits) {
                if (decPt > 0) {
                    buffer.insert(decPt, '.');
                } else {
                    for (int i = 0; i < 1 - decPt; i++) {
                        buffer.insert(0, '0');
                    }
                    buffer.insert(1, '.');
                }
            }
        }
        if (!sign[0]) {
            return;
        }
        if (word0(d) != Sign_bit || word1(d) != 0) {
            if ((word0(d) & Exp_mask) != Exp_mask || (word1(d) == 0 && (word0(d) & 1048575) == 0)) {
                buffer.insert(0, SignatureVisitor.SUPER);
            }
        }
    }
}
