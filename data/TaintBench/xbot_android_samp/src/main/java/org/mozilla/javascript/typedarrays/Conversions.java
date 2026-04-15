package org.mozilla.javascript.typedarrays;

import org.mozilla.classfile.ByteCode;
import org.mozilla.javascript.ScriptRuntime;

public class Conversions {
    public static final int EIGHT_BIT = 256;
    public static final int SIXTEEN_BIT = 65536;
    public static final long THIRTYTWO_BIT = 4294967296L;

    public static int toInt8(Object arg) {
        int iv;
        if (arg instanceof Integer) {
            iv = ((Integer) arg).intValue();
        } else {
            iv = ScriptRuntime.toInt32(arg);
        }
        int int8Bit = iv % 256;
        return int8Bit >= 128 ? int8Bit - 256 : int8Bit;
    }

    public static int toUint8(Object arg) {
        int iv;
        if (arg instanceof Integer) {
            iv = ((Integer) arg).intValue();
        } else {
            iv = ScriptRuntime.toInt32(arg);
        }
        return iv % 256;
    }

    public static int toUint8Clamp(Object arg) {
        double d = ScriptRuntime.toNumber(arg);
        if (d <= 0.0d) {
            return 0;
        }
        if (d >= 255.0d) {
            return ByteCode.IMPDEP2;
        }
        double f = Math.floor(d);
        if (f + 0.5d < d) {
            return (int) (1.0d + f);
        }
        if (d < f + 0.5d) {
            return (int) f;
        }
        if (((int) f) % 2 != 0) {
            return ((int) f) + 1;
        }
        return (int) f;
    }

    public static int toInt16(Object arg) {
        int iv;
        if (arg instanceof Integer) {
            iv = ((Integer) arg).intValue();
        } else {
            iv = ScriptRuntime.toInt32(arg);
        }
        int int16Bit = iv % 65536;
        return int16Bit >= 32768 ? int16Bit - 65536 : int16Bit;
    }

    public static int toUint16(Object arg) {
        int iv;
        if (arg instanceof Integer) {
            iv = ((Integer) arg).intValue();
        } else {
            iv = ScriptRuntime.toInt32(arg);
        }
        return iv % 65536;
    }

    public static int toInt32(Object arg) {
        long int32Bit = ((long) ScriptRuntime.toNumber(arg)) % THIRTYTWO_BIT;
        if (int32Bit >= 2147483648L) {
            int32Bit -= THIRTYTWO_BIT;
        }
        return (int) int32Bit;
    }

    public static long toUint32(Object arg) {
        return ((long) ScriptRuntime.toNumber(arg)) % THIRTYTWO_BIT;
    }
}
