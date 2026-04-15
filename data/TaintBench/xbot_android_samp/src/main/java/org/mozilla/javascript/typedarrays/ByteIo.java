package org.mozilla.javascript.typedarrays;

import org.mozilla.classfile.ByteCode;

public class ByteIo {
    public static Object readInt8(byte[] buf, int offset) {
        return Byte.valueOf(buf[offset]);
    }

    public static void writeInt8(byte[] buf, int offset, int val) {
        buf[offset] = (byte) val;
    }

    public static Object readUint8(byte[] buf, int offset) {
        return Integer.valueOf(buf[offset] & ByteCode.IMPDEP2);
    }

    public static void writeUint8(byte[] buf, int offset, int val) {
        buf[offset] = (byte) (val & ByteCode.IMPDEP2);
    }

    private static short doReadInt16(byte[] buf, int offset, boolean littleEndian) {
        if (littleEndian) {
            return (short) ((buf[offset] & ByteCode.IMPDEP2) | ((buf[offset + 1] & ByteCode.IMPDEP2) << 8));
        }
        return (short) (((buf[offset] & ByteCode.IMPDEP2) << 8) | (buf[offset + 1] & ByteCode.IMPDEP2));
    }

    private static void doWriteInt16(byte[] buf, int offset, int val, boolean littleEndian) {
        if (littleEndian) {
            buf[offset] = (byte) (val & ByteCode.IMPDEP2);
            buf[offset + 1] = (byte) ((val >>> 8) & ByteCode.IMPDEP2);
            return;
        }
        buf[offset] = (byte) ((val >>> 8) & ByteCode.IMPDEP2);
        buf[offset + 1] = (byte) (val & ByteCode.IMPDEP2);
    }

    public static Object readInt16(byte[] buf, int offset, boolean littleEndian) {
        return Short.valueOf(doReadInt16(buf, offset, littleEndian));
    }

    public static void writeInt16(byte[] buf, int offset, int val, boolean littleEndian) {
        doWriteInt16(buf, offset, val, littleEndian);
    }

    public static Object readUint16(byte[] buf, int offset, boolean littleEndian) {
        return Integer.valueOf(doReadInt16(buf, offset, littleEndian) & 65535);
    }

    public static void writeUint16(byte[] buf, int offset, int val, boolean littleEndian) {
        doWriteInt16(buf, offset, 65535 & val, littleEndian);
    }

    public static Object readInt32(byte[] buf, int offset, boolean littleEndian) {
        if (littleEndian) {
            return Integer.valueOf((((buf[offset] & ByteCode.IMPDEP2) | ((buf[offset + 1] & ByteCode.IMPDEP2) << 8)) | ((buf[offset + 2] & ByteCode.IMPDEP2) << 16)) | ((buf[offset + 3] & ByteCode.IMPDEP2) << 24));
        }
        return Integer.valueOf(((((buf[offset] & ByteCode.IMPDEP2) << 24) | ((buf[offset + 1] & ByteCode.IMPDEP2) << 16)) | ((buf[offset + 2] & ByteCode.IMPDEP2) << 8)) | (buf[offset + 3] & ByteCode.IMPDEP2));
    }

    public static void writeInt32(byte[] buf, int offset, int val, boolean littleEndian) {
        if (littleEndian) {
            buf[offset] = (byte) (val & ByteCode.IMPDEP2);
            buf[offset + 1] = (byte) ((val >>> 8) & ByteCode.IMPDEP2);
            buf[offset + 2] = (byte) ((val >>> 16) & ByteCode.IMPDEP2);
            buf[offset + 3] = (byte) ((val >>> 24) & ByteCode.IMPDEP2);
            return;
        }
        buf[offset] = (byte) ((val >>> 24) & ByteCode.IMPDEP2);
        buf[offset + 1] = (byte) ((val >>> 16) & ByteCode.IMPDEP2);
        buf[offset + 2] = (byte) ((val >>> 8) & ByteCode.IMPDEP2);
        buf[offset + 3] = (byte) (val & ByteCode.IMPDEP2);
    }

    public static long readUint32Primitive(byte[] buf, int offset, boolean littleEndian) {
        if (littleEndian) {
            return ((((((long) buf[offset]) & 255) | ((((long) buf[offset + 1]) & 255) << 8)) | ((((long) buf[offset + 2]) & 255) << 16)) | ((((long) buf[offset + 3]) & 255) << 24)) & 4294967295L;
        }
        return (((((((long) buf[offset]) & 255) << 24) | ((((long) buf[offset + 1]) & 255) << 16)) | ((((long) buf[offset + 2]) & 255) << 8)) | (((long) buf[offset + 3]) & 255)) & 4294967295L;
    }

    public static void writeUint32(byte[] buf, int offset, long val, boolean littleEndian) {
        if (littleEndian) {
            buf[offset] = (byte) ((int) (val & 255));
            buf[offset + 1] = (byte) ((int) ((val >>> 8) & 255));
            buf[offset + 2] = (byte) ((int) ((val >>> 16) & 255));
            buf[offset + 3] = (byte) ((int) ((val >>> 24) & 255));
            return;
        }
        buf[offset] = (byte) ((int) ((val >>> 24) & 255));
        buf[offset + 1] = (byte) ((int) ((val >>> 16) & 255));
        buf[offset + 2] = (byte) ((int) ((val >>> 8) & 255));
        buf[offset + 3] = (byte) ((int) (val & 255));
    }

    public static Object readUint32(byte[] buf, int offset, boolean littleEndian) {
        return Long.valueOf(readUint32Primitive(buf, offset, littleEndian));
    }

    public static long readUint64Primitive(byte[] buf, int offset, boolean littleEndian) {
        if (littleEndian) {
            return (((((((((long) buf[offset]) & 255) | ((((long) buf[offset + 1]) & 255) << 8)) | ((((long) buf[offset + 2]) & 255) << 16)) | ((((long) buf[offset + 3]) & 255) << 24)) | ((((long) buf[offset + 4]) & 255) << 32)) | ((((long) buf[offset + 5]) & 255) << 40)) | ((((long) buf[offset + 6]) & 255) << 48)) | ((((long) buf[offset + 7]) & 255) << 56);
        }
        return ((((((((((long) buf[offset]) & 255) << 56) | ((((long) buf[offset + 1]) & 255) << 48)) | ((((long) buf[offset + 2]) & 255) << 40)) | ((((long) buf[offset + 3]) & 255) << 32)) | ((((long) buf[offset + 4]) & 255) << 24)) | ((((long) buf[offset + 5]) & 255) << 16)) | ((((long) buf[offset + 6]) & 255) << 8)) | ((((long) buf[offset + 7]) & 255) << null);
    }

    public static void writeUint64(byte[] buf, int offset, long val, boolean littleEndian) {
        if (littleEndian) {
            buf[offset] = (byte) ((int) (val & 255));
            buf[offset + 1] = (byte) ((int) ((val >>> 8) & 255));
            buf[offset + 2] = (byte) ((int) ((val >>> 16) & 255));
            buf[offset + 3] = (byte) ((int) ((val >>> 24) & 255));
            buf[offset + 4] = (byte) ((int) ((val >>> 32) & 255));
            buf[offset + 5] = (byte) ((int) ((val >>> 40) & 255));
            buf[offset + 6] = (byte) ((int) ((val >>> 48) & 255));
            buf[offset + 7] = (byte) ((int) ((val >>> 56) & 255));
            return;
        }
        buf[offset] = (byte) ((int) ((val >>> 56) & 255));
        buf[offset + 1] = (byte) ((int) ((val >>> 48) & 255));
        buf[offset + 2] = (byte) ((int) ((val >>> 40) & 255));
        buf[offset + 3] = (byte) ((int) ((val >>> 32) & 255));
        buf[offset + 4] = (byte) ((int) ((val >>> 24) & 255));
        buf[offset + 5] = (byte) ((int) ((val >>> 16) & 255));
        buf[offset + 6] = (byte) ((int) ((val >>> 8) & 255));
        buf[offset + 7] = (byte) ((int) (val & 255));
    }

    public static Object readFloat32(byte[] buf, int offset, boolean littleEndian) {
        return Float.valueOf(Float.intBitsToFloat((int) readUint32Primitive(buf, offset, littleEndian)));
    }

    public static void writeFloat32(byte[] buf, int offset, double val, boolean littleEndian) {
        writeUint32(buf, offset, (long) Float.floatToIntBits((float) val), littleEndian);
    }

    public static Object readFloat64(byte[] buf, int offset, boolean littleEndian) {
        return Double.valueOf(Double.longBitsToDouble(readUint64Primitive(buf, offset, littleEndian)));
    }

    public static void writeFloat64(byte[] buf, int offset, double val, boolean littleEndian) {
        writeUint64(buf, offset, Double.doubleToLongBits(val), littleEndian);
    }
}
