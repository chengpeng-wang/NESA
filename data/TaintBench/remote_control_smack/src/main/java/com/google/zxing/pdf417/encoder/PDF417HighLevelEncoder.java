package com.google.zxing.pdf417.encoder;

import android.support.v4.view.MotionEventCompat;
import com.google.zxing.WriterException;
import java.math.BigInteger;
import java.util.Arrays;

final class PDF417HighLevelEncoder {
    private static final int BYTE_COMPACTION = 1;
    private static final int LATCH_TO_BYTE = 924;
    private static final int LATCH_TO_BYTE_PADDED = 901;
    private static final int LATCH_TO_NUMERIC = 902;
    private static final int LATCH_TO_TEXT = 900;
    private static final byte[] MIXED = new byte[128];
    private static final int NUMERIC_COMPACTION = 2;
    private static final byte[] PUNCTUATION = new byte[128];
    private static final int SHIFT_TO_BYTE = 913;
    private static final int SUBMODE_ALPHA = 0;
    private static final int SUBMODE_LOWER = 1;
    private static final int SUBMODE_MIXED = 2;
    private static final int SUBMODE_PUNCTUATION = 3;
    private static final int TEXT_COMPACTION = 0;
    private static final byte[] TEXT_MIXED_RAW;
    private static final byte[] TEXT_PUNCTUATION_RAW;

    static {
        byte i;
        byte b;
        r2 = new byte[30];
        TEXT_MIXED_RAW = r2;
        r2 = new byte[30];
        r2[0] = (byte) 59;
        r2[1] = (byte) 60;
        r2[2] = (byte) 62;
        r2[3] = (byte) 64;
        r2[4] = (byte) 91;
        r2[5] = (byte) 92;
        r2[6] = (byte) 93;
        r2[7] = (byte) 95;
        r2[8] = (byte) 96;
        r2[9] = (byte) 126;
        r2[10] = (byte) 33;
        r2[11] = (byte) 13;
        r2[12] = (byte) 9;
        r2[13] = (byte) 44;
        r2[14] = (byte) 58;
        r2[15] = (byte) 10;
        r2[16] = (byte) 45;
        r2[17] = (byte) 46;
        r2[18] = (byte) 36;
        r2[19] = (byte) 47;
        r2[20] = (byte) 34;
        r2[21] = (byte) 124;
        r2[22] = (byte) 42;
        r2[23] = (byte) 40;
        r2[24] = (byte) 41;
        r2[25] = (byte) 63;
        r2[26] = (byte) 123;
        r2[27] = (byte) 125;
        r2[28] = (byte) 39;
        TEXT_PUNCTUATION_RAW = r2;
        Arrays.fill(MIXED, (byte) -1);
        for (i = (byte) 0; i < TEXT_MIXED_RAW.length; i = (byte) (i + 1)) {
            b = TEXT_MIXED_RAW[i];
            if (b > (byte) 0) {
                MIXED[b] = i;
            }
        }
        Arrays.fill(PUNCTUATION, (byte) -1);
        for (i = (byte) 0; i < TEXT_PUNCTUATION_RAW.length; i = (byte) (i + 1)) {
            b = TEXT_PUNCTUATION_RAW[i];
            if (b > (byte) 0) {
                PUNCTUATION[b] = i;
            }
        }
    }

    private PDF417HighLevelEncoder() {
    }

    private static byte[] getBytesForMessage(String msg) {
        return msg.getBytes();
    }

    static String encodeHighLevel(String msg, Compaction compaction) throws WriterException {
        byte[] bytes = null;
        StringBuilder sb = new StringBuilder(msg.length());
        int len = msg.length();
        int p = 0;
        int encodingMode = 0;
        int textSubMode = 0;
        if (compaction == Compaction.TEXT) {
            encodeText(msg, 0, len, sb, 0);
        } else if (compaction == Compaction.BYTE) {
            bytes = getBytesForMessage(msg);
            encodeBinary(bytes, 0, bytes.length, 1, sb);
        } else if (compaction == Compaction.NUMERIC) {
            sb.append(902);
            encodeNumeric(msg, 0, len, sb);
        } else {
            while (p < len) {
                int n = determineConsecutiveDigitCount(msg, p);
                if (n >= 13) {
                    sb.append(902);
                    encodingMode = 2;
                    textSubMode = 0;
                    encodeNumeric(msg, p, n, sb);
                    p += n;
                } else {
                    int t = determineConsecutiveTextCount(msg, p);
                    if (t >= 5 || n == len) {
                        if (encodingMode != 0) {
                            sb.append(900);
                            encodingMode = 0;
                            textSubMode = 0;
                        }
                        textSubMode = encodeText(msg, p, t, sb, textSubMode);
                        p += t;
                    } else {
                        if (bytes == null) {
                            bytes = getBytesForMessage(msg);
                        }
                        int b = determineConsecutiveBinaryCount(msg, bytes, p);
                        if (b == 0) {
                            b = 1;
                        }
                        if (b == 1 && encodingMode == 0) {
                            encodeBinary(bytes, p, 1, 0, sb);
                        } else {
                            encodeBinary(bytes, p, b, encodingMode, sb);
                            encodingMode = 1;
                            textSubMode = 0;
                        }
                        p += b;
                    }
                }
            }
        }
        return sb.toString();
    }

    private static int encodeText(java.lang.CharSequence r11, int r12, int r13, java.lang.StringBuilder r14, int r15) {
        /*
        r8 = new java.lang.StringBuilder;
        r8.<init>(r13);
        r7 = r15;
        r3 = 0;
    L_0x0007:
        r9 = r12 + r3;
        r0 = r11.charAt(r9);
        switch(r7) {
            case 0: goto L_0x0037;
            case 1: goto L_0x0076;
            case 2: goto L_0x00bc;
            default: goto L_0x0010;
        };
    L_0x0010:
        r9 = isPunctuation(r0);
        if (r9 == 0) goto L_0x0113;
    L_0x0016:
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
    L_0x001e:
        r3 = r3 + 1;
        if (r3 < r13) goto L_0x0007;
    L_0x0022:
        r1 = 0;
        r4 = r8.length();
        r2 = 0;
    L_0x0028:
        if (r2 < r4) goto L_0x011b;
    L_0x002a:
        r9 = r4 % 2;
        if (r9 == 0) goto L_0x0036;
    L_0x002e:
        r9 = r1 * 30;
        r9 = r9 + 29;
        r9 = (char) r9;
        r14.append(r9);
    L_0x0036:
        return r7;
    L_0x0037:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x004e;
    L_0x003d:
        r9 = 32;
        if (r0 != r9) goto L_0x0047;
    L_0x0041:
        r9 = 26;
        r8.append(r9);
        goto L_0x001e;
    L_0x0047:
        r9 = r0 + -65;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x004e:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x005b;
    L_0x0054:
        r7 = 1;
        r9 = 27;
        r8.append(r9);
        goto L_0x0007;
    L_0x005b:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x0068;
    L_0x0061:
        r7 = 2;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x0068:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x0076:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x008d;
    L_0x007c:
        r9 = 32;
        if (r0 != r9) goto L_0x0086;
    L_0x0080:
        r9 = 26;
        r8.append(r9);
        goto L_0x001e;
    L_0x0086:
        r9 = r0 + -97;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x008d:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x009f;
    L_0x0093:
        r9 = 27;
        r8.append(r9);
        r9 = r0 + -65;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x009f:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x00ad;
    L_0x00a5:
        r7 = 2;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x00ad:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x00bc:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x00cc;
    L_0x00c2:
        r9 = MIXED;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x00cc:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x00da;
    L_0x00d2:
        r7 = 0;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x00da:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x00e8;
    L_0x00e0:
        r7 = 1;
        r9 = 27;
        r8.append(r9);
        goto L_0x0007;
    L_0x00e8:
        r9 = r12 + r3;
        r9 = r9 + 1;
        if (r9 >= r13) goto L_0x0104;
    L_0x00ee:
        r9 = r12 + r3;
        r9 = r9 + 1;
        r5 = r11.charAt(r9);
        r9 = isPunctuation(r5);
        if (r9 == 0) goto L_0x0104;
    L_0x00fc:
        r7 = 3;
        r9 = 25;
        r8.append(r9);
        goto L_0x0007;
    L_0x0104:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x0113:
        r7 = 0;
        r9 = 29;
        r8.append(r9);
        goto L_0x0007;
    L_0x011b:
        r9 = r2 % 2;
        if (r9 == 0) goto L_0x0131;
    L_0x011f:
        r6 = 1;
    L_0x0120:
        if (r6 == 0) goto L_0x0133;
    L_0x0122:
        r9 = r1 * 30;
        r10 = r8.charAt(r2);
        r9 = r9 + r10;
        r1 = (char) r9;
        r14.append(r1);
    L_0x012d:
        r2 = r2 + 1;
        goto L_0x0028;
    L_0x0131:
        r6 = 0;
        goto L_0x0120;
    L_0x0133:
        r1 = r8.charAt(r2);
        goto L_0x012d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.encoder.PDF417HighLevelEncoder.encodeText(java.lang.CharSequence, int, int, java.lang.StringBuilder, int):int");
    }

    private static void encodeBinary(byte[] bytes, int startpos, int count, int startmode, StringBuilder sb) {
        int i;
        if (count == 1 && startmode == 0) {
            sb.append(913);
        }
        int idx = startpos;
        if (count >= 6) {
            sb.append(924);
            char[] chars = new char[5];
            while ((startpos + count) - idx >= 6) {
                long t = 0;
                for (i = 0; i < 6; i++) {
                    t = (t << 8) + ((long) (bytes[idx + i] & MotionEventCompat.ACTION_MASK));
                }
                for (i = 0; i < 5; i++) {
                    chars[i] = (char) ((int) (t % 900));
                    t /= 900;
                }
                for (i = chars.length - 1; i >= 0; i--) {
                    sb.append(chars[i]);
                }
                idx += 6;
            }
        }
        if (idx < startpos + count) {
            sb.append(901);
        }
        for (i = idx; i < startpos + count; i++) {
            sb.append((char) (bytes[i] & MotionEventCompat.ACTION_MASK));
        }
    }

    private static void encodeNumeric(String msg, int startpos, int count, StringBuilder sb) {
        int idx = 0;
        StringBuilder tmp = new StringBuilder((count / 3) + 1);
        BigInteger num900 = BigInteger.valueOf(900);
        BigInteger num0 = BigInteger.valueOf(0);
        while (idx < count - 1) {
            tmp.setLength(0);
            int len = Math.min(44, count - idx);
            BigInteger bigint = new BigInteger(new StringBuilder(String.valueOf('1')).append(msg.substring(startpos + idx, (startpos + idx) + len)).toString());
            do {
                tmp.append((char) bigint.mod(num900).intValue());
                bigint = bigint.divide(num900);
            } while (!bigint.equals(num0));
            for (int i = tmp.length() - 1; i >= 0; i--) {
                sb.append(tmp.charAt(i));
            }
            idx += len;
        }
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isAlphaUpper(char ch) {
        return ch == ' ' || (ch >= 'A' && ch <= 'Z');
    }

    private static boolean isAlphaLower(char ch) {
        return ch == ' ' || (ch >= 'a' && ch <= 'z');
    }

    private static boolean isMixed(char ch) {
        return MIXED[ch] != (byte) -1;
    }

    private static boolean isPunctuation(char ch) {
        return PUNCTUATION[ch] != (byte) -1;
    }

    private static boolean isText(char ch) {
        return ch == 9 || ch == 10 || ch == 13 || (ch >= ' ' && ch <= '~');
    }

    private static int determineConsecutiveDigitCount(CharSequence msg, int startpos) {
        int count = 0;
        int len = msg.length();
        int idx = startpos;
        if (idx < len) {
            char ch = msg.charAt(idx);
            while (isDigit(ch) && idx < len) {
                count++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
        }
        return count;
    }

    private static int determineConsecutiveTextCount(CharSequence msg, int startpos) {
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch) && idx < len) {
                numericCount++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
            if (numericCount < 13) {
                if (numericCount <= 0) {
                    if (!isText(msg.charAt(idx))) {
                        break;
                    }
                    idx++;
                }
            } else {
                return (idx - startpos) - numericCount;
            }
        }
        return idx - startpos;
    }

    private static int determineConsecutiveBinaryCount(CharSequence msg, byte[] bytes, int startpos) throws WriterException {
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            int i;
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch)) {
                numericCount++;
                i = idx + numericCount;
                if (i >= len) {
                    break;
                }
                ch = msg.charAt(i);
            }
            if (numericCount >= 13) {
                return idx - startpos;
            }
            int textCount = 0;
            while (textCount < 5 && isText(ch)) {
                textCount++;
                i = idx + textCount;
                if (i >= len) {
                    break;
                }
                ch = msg.charAt(i);
            }
            if (textCount >= 5) {
                return idx - startpos;
            }
            ch = msg.charAt(idx);
            if (bytes[idx] != (byte) 63 || ch == '?') {
                idx++;
            } else {
                throw new WriterException("Non-encodable character detected: " + ch + " (Unicode: " + ch + ')');
            }
        }
        return idx - startpos;
    }
}
