package org.mozilla.javascript.v8dtoa;

import org.java_websocket.framing.CloseFrame;
import org.objectweb.asm.signature.SignatureVisitor;

public class FastDtoa {
    static final /* synthetic */ boolean $assertionsDisabled = (!FastDtoa.class.desiredAssertionStatus());
    static final int kFastDtoaMaximalLength = 17;
    static final int kTen4 = 10000;
    static final int kTen5 = 100000;
    static final int kTen6 = 1000000;
    static final int kTen7 = 10000000;
    static final int kTen8 = 100000000;
    static final int kTen9 = 1000000000;
    static final int maximal_target_exponent = -32;
    static final int minimal_target_exponent = -60;

    static boolean roundWeed(FastDtoaBuilder buffer, long distance_too_high_w, long unsafe_interval, long rest, long ten_kappa, long unit) {
        long small_distance = distance_too_high_w - unit;
        long big_distance = distance_too_high_w + unit;
        while (rest < small_distance && unsafe_interval - rest >= ten_kappa && (rest + ten_kappa < small_distance || small_distance - rest >= (rest + ten_kappa) - small_distance)) {
            buffer.decreaseLast();
            rest += ten_kappa;
        }
        if (rest >= big_distance || unsafe_interval - rest < ten_kappa || (rest + ten_kappa >= big_distance && big_distance - rest <= (rest + ten_kappa) - big_distance)) {
            return 2 * unit <= rest && rest <= unsafe_interval - (4 * unit);
        } else {
            return false;
        }
    }

    static long biggestPowerTen(int number, int number_bits) {
        int power;
        int exponent;
        switch (number_bits) {
            case 30:
            case 31:
            case 32:
                if (kTen9 <= number) {
                    power = kTen9;
                    exponent = 9;
                    break;
                }
            case 27:
            case 28:
            case 29:
                if (kTen8 <= number) {
                    power = kTen8;
                    exponent = 8;
                    break;
                }
            case 24:
            case 25:
            case 26:
                if (kTen7 <= number) {
                    power = kTen7;
                    exponent = 7;
                    break;
                }
            case 20:
            case 21:
            case 22:
            case 23:
                if (kTen6 <= number) {
                    power = kTen6;
                    exponent = 6;
                    break;
                }
            case 17:
            case 18:
            case 19:
                if (kTen5 <= number) {
                    power = kTen5;
                    exponent = 5;
                    break;
                }
            case 14:
            case 15:
            case 16:
                if (kTen4 <= number) {
                    power = kTen4;
                    exponent = 4;
                    break;
                }
            case 10:
            case 11:
            case 12:
            case 13:
                if (CloseFrame.NORMAL <= number) {
                    power = CloseFrame.NORMAL;
                    exponent = 3;
                    break;
                }
            case 7:
            case 8:
            case 9:
                if (100 <= number) {
                    power = 100;
                    exponent = 2;
                    break;
                }
            case 4:
            case 5:
            case 6:
                if (10 <= number) {
                    power = 10;
                    exponent = 1;
                    break;
                }
            case 1:
            case 2:
            case 3:
                if (1 <= number) {
                    power = 1;
                    exponent = 0;
                    break;
                }
            case 0:
                power = 0;
                exponent = -1;
                break;
            default:
                power = 0;
                exponent = 0;
                break;
        }
        return (((long) power) << 32) | (4294967295L & ((long) exponent));
    }

    private static boolean uint64_lte(long a, long b) {
        if (a != b) {
            if (((b < 0 ? 1 : 0) ^ ((a < b ? 1 : 0) ^ (a < 0 ? 1 : 0))) == 0) {
                return false;
            }
        }
        return true;
    }

    static boolean digitGen(DiyFp low, DiyFp w, DiyFp high, FastDtoaBuilder buffer, int mk) {
        if (!$assertionsDisabled && (low.e() != w.e() || w.e() != high.e())) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && !uint64_lte(low.f() + 1, high.f() - 1)) {
            throw new AssertionError();
        } else if ($assertionsDisabled || (minimal_target_exponent <= w.e() && w.e() <= maximal_target_exponent)) {
            long unit = 1;
            DiyFp diyFp = new DiyFp(low.f() - 1, low.e());
            diyFp = new DiyFp(high.f() + 1, high.e());
            DiyFp unsafe_interval = DiyFp.minus(diyFp, diyFp);
            diyFp = new DiyFp(1 << (-w.e()), w.e());
            int integrals = (int) ((diyFp.f() >>> (-diyFp.e())) & 4294967295L);
            long fractionals = diyFp.f() & (diyFp.f() - 1);
            long result = biggestPowerTen(integrals, 64 - (-diyFp.e()));
            int divider = (int) ((result >>> 32) & 4294967295L);
            int kappa = ((int) (4294967295L & result)) + 1;
            while (kappa > 0) {
                buffer.append((char) ((integrals / divider) + 48));
                integrals %= divider;
                kappa--;
                long rest = (((long) integrals) << (-diyFp.e())) + fractionals;
                if (rest < unsafe_interval.f()) {
                    buffer.point = (buffer.end - mk) + kappa;
                    return roundWeed(buffer, DiyFp.minus(diyFp, w).f(), unsafe_interval.f(), rest, ((long) divider) << (-diyFp.e()), 1);
                }
                divider /= 10;
            }
            do {
                fractionals *= 5;
                unit *= 5;
                unsafe_interval.setF(unsafe_interval.f() * 5);
                unsafe_interval.setE(unsafe_interval.e() + 1);
                diyFp.setF(diyFp.f() >>> 1);
                diyFp.setE(diyFp.e() + 1);
                buffer.append((char) (((int) ((fractionals >>> (-diyFp.e())) & 4294967295L)) + 48));
                fractionals &= diyFp.f() - 1;
                kappa--;
            } while (fractionals >= unsafe_interval.f());
            buffer.point = (buffer.end - mk) + kappa;
            return roundWeed(buffer, DiyFp.minus(diyFp, w).f() * unit, unsafe_interval.f(), fractionals, diyFp.f(), unit);
        } else {
            throw new AssertionError();
        }
    }

    static boolean grisu3(double v, FastDtoaBuilder buffer) {
        long bits = Double.doubleToLongBits(v);
        DiyFp w = DoubleHelper.asNormalizedDiyFp(bits);
        DiyFp boundary_minus = new DiyFp();
        DiyFp boundary_plus = new DiyFp();
        DoubleHelper.normalizedBoundaries(bits, boundary_minus, boundary_plus);
        if ($assertionsDisabled || boundary_plus.e() == w.e()) {
            DiyFp ten_mk = new DiyFp();
            int mk = CachedPowers.getCachedPower(w.e() + 64, minimal_target_exponent, maximal_target_exponent, ten_mk);
            if ($assertionsDisabled || (minimal_target_exponent <= (w.e() + ten_mk.e()) + 64 && maximal_target_exponent >= (w.e() + ten_mk.e()) + 64)) {
                DiyFp scaled_w = DiyFp.times(w, ten_mk);
                if ($assertionsDisabled || scaled_w.e() == (boundary_plus.e() + ten_mk.e()) + 64) {
                    return digitGen(DiyFp.times(boundary_minus, ten_mk), scaled_w, DiyFp.times(boundary_plus, ten_mk), buffer, mk);
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static boolean dtoa(double v, FastDtoaBuilder buffer) {
        if (!$assertionsDisabled && v <= 0.0d) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && Double.isNaN(v)) {
            throw new AssertionError();
        } else if ($assertionsDisabled || !Double.isInfinite(v)) {
            return grisu3(v, buffer);
        } else {
            throw new AssertionError();
        }
    }

    public static String numberToString(double v) {
        FastDtoaBuilder buffer = new FastDtoaBuilder();
        return numberToString(v, buffer) ? buffer.format() : null;
    }

    public static boolean numberToString(double v, FastDtoaBuilder buffer) {
        buffer.reset();
        if (v < 0.0d) {
            buffer.append(SignatureVisitor.SUPER);
            v = -v;
        }
        return dtoa(v, buffer);
    }
}
