package com.sun.mail.imap.protocol;

import android.support.v4.view.MotionEventCompat;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class BASE64MailboxDecoder {
    static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', ','};
    private static final byte[] pem_convert_array = new byte[256];

    public static String decode(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        int copyTo;
        boolean changedString = false;
        int copyTo2 = 0;
        char[] chars = new char[original.length()];
        StringCharacterIterator iter = new StringCharacterIterator(original);
        char c = iter.first();
        while (true) {
            copyTo = copyTo2;
            if (c == 65535) {
                break;
            }
            if (c == '&') {
                changedString = true;
                copyTo2 = base64decode(chars, copyTo, iter);
            } else {
                copyTo2 = copyTo + 1;
                chars[copyTo] = c;
            }
            c = iter.next();
        }
        if (changedString) {
            return new String(chars, 0, copyTo);
        }
        return original;
    }

    protected static int base64decode(char[] buffer, int offset, CharacterIterator iter) {
        boolean firsttime = true;
        int leftover = -1;
        while (true) {
            byte orig_0 = (byte) iter.next();
            if (orig_0 == (byte) -1) {
                return offset;
            }
            int offset2;
            if (orig_0 != (byte) 45) {
                firsttime = false;
                byte orig_1 = (byte) iter.next();
                if (orig_1 == (byte) -1 || orig_1 == (byte) 45) {
                    return offset;
                }
                byte a = pem_convert_array[orig_0 & MotionEventCompat.ACTION_MASK];
                byte b = pem_convert_array[orig_1 & MotionEventCompat.ACTION_MASK];
                byte current = (byte) (((a << 2) & 252) | ((b >>> 4) & 3));
                if (leftover != -1) {
                    offset2 = offset + 1;
                    buffer[offset] = (char) ((leftover << 8) | (current & MotionEventCompat.ACTION_MASK));
                    leftover = -1;
                    offset = offset2;
                } else {
                    leftover = current & MotionEventCompat.ACTION_MASK;
                }
                byte orig_2 = (byte) iter.next();
                if (orig_2 != (byte) 61) {
                    if (orig_2 == (byte) -1 || orig_2 == (byte) 45) {
                        return offset;
                    }
                    a = b;
                    b = pem_convert_array[orig_2 & MotionEventCompat.ACTION_MASK];
                    current = (byte) (((a << 4) & 240) | ((b >>> 2) & 15));
                    if (leftover != -1) {
                        offset2 = offset + 1;
                        buffer[offset] = (char) ((leftover << 8) | (current & MotionEventCompat.ACTION_MASK));
                        leftover = -1;
                        offset = offset2;
                    } else {
                        leftover = current & MotionEventCompat.ACTION_MASK;
                    }
                    byte orig_3 = (byte) iter.next();
                    if (orig_3 == (byte) 61) {
                        continue;
                    } else if (orig_3 == (byte) -1 || orig_3 == (byte) 45) {
                        return offset;
                    } else {
                        current = (byte) (((b << 6) & 192) | (pem_convert_array[orig_3 & MotionEventCompat.ACTION_MASK] & 63));
                        if (leftover != -1) {
                            char testing = (char) ((leftover << 8) | (current & MotionEventCompat.ACTION_MASK));
                            offset2 = offset + 1;
                            buffer[offset] = (char) ((leftover << 8) | (current & MotionEventCompat.ACTION_MASK));
                            leftover = -1;
                            offset = offset2;
                        } else {
                            leftover = current & MotionEventCompat.ACTION_MASK;
                        }
                    }
                }
            } else if (!firsttime) {
                return offset;
            } else {
                offset2 = offset + 1;
                buffer[offset] = '&';
                return offset2;
            }
        }
    }

    static {
        int i;
        for (i = 0; i < MotionEventCompat.ACTION_MASK; i++) {
            pem_convert_array[i] = (byte) -1;
        }
        for (i = 0; i < pem_array.length; i++) {
            pem_convert_array[pem_array[i]] = (byte) i;
        }
    }
}
