package com.sun.mail.util;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ASCIIUtility {
    private ASCIIUtility() {
    }

    public static int parseInt(byte[] b, int start, int end, int radix) throws NumberFormatException {
        if (b == null) {
            throw new NumberFormatException("null");
        }
        int result = 0;
        boolean negative = false;
        int i = start;
        if (end > start) {
            int limit;
            int i2;
            int digit;
            if (b[i] == (byte) 45) {
                negative = true;
                limit = Integer.MIN_VALUE;
                i++;
            } else {
                limit = -2147483647;
            }
            int multmin = limit / radix;
            if (i < end) {
                i2 = i + 1;
                digit = Character.digit((char) b[i], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + toString(b, start, end));
                }
                result = -digit;
            } else {
                i2 = i;
            }
            while (i2 < end) {
                i = i2 + 1;
                digit = Character.digit((char) b[i2], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                } else if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                } else {
                    result *= radix;
                    if (result < limit + digit) {
                        throw new NumberFormatException("illegal number");
                    }
                    result -= digit;
                    i2 = i;
                }
            }
            if (!negative) {
                return -result;
            }
            if (i2 > start + 1) {
                return result;
            }
            throw new NumberFormatException("illegal number");
        }
        throw new NumberFormatException("illegal number");
    }

    public static int parseInt(byte[] b, int start, int end) throws NumberFormatException {
        return parseInt(b, start, end, 10);
    }

    public static long parseLong(byte[] b, int start, int end, int radix) throws NumberFormatException {
        if (b == null) {
            throw new NumberFormatException("null");
        }
        long result = 0;
        boolean negative = false;
        int i = start;
        if (end > start) {
            long limit;
            int i2;
            int digit;
            if (b[i] == (byte) 45) {
                negative = true;
                limit = Long.MIN_VALUE;
                i++;
            } else {
                limit = -9223372036854775807L;
            }
            long multmin = limit / ((long) radix);
            if (i < end) {
                i2 = i + 1;
                digit = Character.digit((char) b[i], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + toString(b, start, end));
                }
                result = (long) (-digit);
            } else {
                i2 = i;
            }
            while (i2 < end) {
                i = i2 + 1;
                digit = Character.digit((char) b[i2], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                } else if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                } else {
                    result *= (long) radix;
                    if (result < ((long) digit) + limit) {
                        throw new NumberFormatException("illegal number");
                    }
                    result -= (long) digit;
                    i2 = i;
                }
            }
            if (!negative) {
                return -result;
            }
            if (i2 > start + 1) {
                return result;
            }
            throw new NumberFormatException("illegal number");
        }
        throw new NumberFormatException("illegal number");
    }

    public static long parseLong(byte[] b, int start, int end) throws NumberFormatException {
        return parseLong(b, start, end, 10);
    }

    public static String toString(byte[] b, int start, int end) {
        int size = end - start;
        char[] theChars = new char[size];
        int j = start;
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            int j2 = j + 1;
            theChars[i] = (char) (b[j] & MotionEventCompat.ACTION_MASK);
            j = j2;
            i = i2;
        }
        return new String(theChars);
    }

    public static String toString(ByteArrayInputStream is) {
        int size = is.available();
        char[] theChars = new char[size];
        byte[] bytes = new byte[size];
        is.read(bytes, 0, size);
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            theChars[i] = (char) (bytes[i] & MotionEventCompat.ACTION_MASK);
            i = i2;
        }
        return new String(theChars);
    }

    public static byte[] getBytes(String s) {
        char[] chars = s.toCharArray();
        int size = chars.length;
        byte[] bytes = new byte[size];
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            bytes[i] = (byte) chars[i];
            i = i2;
        }
        return bytes;
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        byte[] buf;
        int len;
        if (is instanceof ByteArrayInputStream) {
            int size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
            return buf;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[1024];
        while (true) {
            len = is.read(buf, 0, 1024);
            if (len == -1) {
                return bos.toByteArray();
            }
            bos.write(buf, 0, len);
        }
    }
}
