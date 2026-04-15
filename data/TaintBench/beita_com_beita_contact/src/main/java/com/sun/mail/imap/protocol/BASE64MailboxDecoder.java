package com.sun.mail.imap.protocol;

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
        char testing = 0;
        int leftover = -1;
        while (true) {
            byte orig_0 = (byte) iter.next();
            if (orig_0 != (byte) -1) {
                if (orig_0 != (byte) 45) {
                    byte orig_1 = (byte) iter.next();
                    if (orig_1 != (byte) -1) {
                        if (orig_1 != (byte) 45) {
                            int offset2;
                            byte a = pem_convert_array[orig_0 & 255];
                            orig_0 = pem_convert_array[orig_1 & 255];
                            int leftover2 = (byte) (((a << 2) & 252) | ((orig_0 >>> 4) & 3));
                            if (leftover != -1) {
                                offset2 = offset + 1;
                                buffer[offset] = (char) ((leftover2 & 255) | (leftover << 8));
                                leftover = -1;
                                offset = offset2;
                            } else {
                                leftover = leftover2 & 255;
                            }
                            orig_1 = (byte) iter.next();
                            if (orig_1 != (byte) 61) {
                                if (orig_1 != (byte) -1) {
                                    if (orig_1 != (byte) 45) {
                                        a = orig_0;
                                        orig_0 = pem_convert_array[orig_1 & 255];
                                        leftover2 = (byte) (((a << 4) & 240) | ((orig_0 >>> 2) & 15));
                                        if (leftover != -1) {
                                            offset2 = offset + 1;
                                            buffer[offset] = (char) ((leftover2 & 255) | (leftover << 8));
                                            leftover = (byte) -1;
                                            offset = offset2;
                                        } else {
                                            byte leftover3 = leftover2 & 255;
                                        }
                                        orig_1 = (byte) iter.next();
                                        if (orig_1 != (byte) 61) {
                                            if (orig_1 == (byte) -1) {
                                                break;
                                            } else if (orig_1 == (byte) 45) {
                                                iter = leftover;
                                                buffer = null;
                                                return offset;
                                            } else {
                                                leftover2 = (byte) (((orig_0 << 6) & 192) | (pem_convert_array[orig_1 & 255] & 63));
                                                if (leftover != (byte) -1) {
                                                    char testing2 = (char) ((leftover << 8) | (leftover2 & 255));
                                                    orig_0 = offset + 1;
                                                    buffer[offset] = (char) ((leftover2 & 255) | (leftover << 8));
                                                    testing = testing2;
                                                    leftover = -1;
                                                    firsttime = false;
                                                    offset = orig_0;
                                                } else {
                                                    leftover = leftover2 & 255;
                                                    firsttime = false;
                                                }
                                            }
                                        } else {
                                            firsttime = false;
                                        }
                                    } else {
                                        iter = leftover;
                                        buffer = null;
                                        return offset;
                                    }
                                }
                                break;
                            }
                            firsttime = false;
                        } else {
                            iter = leftover;
                            buffer = null;
                            return offset;
                        }
                    }
                    break;
                } else if (firsttime) {
                    int offset3 = offset + 1;
                    buffer[offset] = (byte) 38;
                    buffer = firsttime;
                    offset = offset3;
                    iter = leftover;
                    return offset;
                } else {
                    return offset;
                }
            }
            iter = leftover;
            buffer = firsttime;
            return offset;
        }
        buffer = null;
        return offset;
    }

    static {
        int i;
        for (i = 0; i < 255; i++) {
            pem_convert_array[i] = (byte) -1;
        }
        for (i = 0; i < pem_array.length; i++) {
            pem_convert_array[pem_array[i]] = (byte) i;
        }
    }
}
