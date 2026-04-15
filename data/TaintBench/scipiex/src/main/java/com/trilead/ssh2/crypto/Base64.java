package com.trilead.ssh2.crypto;

import java.io.CharArrayWriter;
import java.io.IOException;

public class Base64 {
    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static char[] encode(byte[] content) {
        CharArrayWriter cw = new CharArrayWriter((content.length * 4) / 3);
        int idx = 0;
        int x = 0;
        for (int i = 0; i < content.length; i++) {
            if (idx == 0) {
                x = (content[i] & 255) << 16;
            } else if (idx == 1) {
                x |= (content[i] & 255) << 8;
            } else {
                x |= content[i] & 255;
            }
            idx++;
            if (idx == 3) {
                cw.write(alphabet[x >> 18]);
                cw.write(alphabet[(x >> 12) & 63]);
                cw.write(alphabet[(x >> 6) & 63]);
                cw.write(alphabet[x & 63]);
                idx = 0;
            }
        }
        if (idx == 1) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[(x >> 12) & 63]);
            cw.write(61);
            cw.write(61);
        }
        if (idx == 2) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[(x >> 12) & 63]);
            cw.write(alphabet[(x >> 6) & 63]);
            cw.write(61);
        }
        return cw.toCharArray();
    }

    public static byte[] decode(char[] message) throws IOException {
        byte[] buff = new byte[4];
        byte[] dest = new byte[message.length];
        int bpos = 0;
        int destpos = 0;
        for (int c : message) {
            if (!(c == 10 || c == 13 || c == 32 || c == 9)) {
                int bpos2;
                if (c >= 65 && c <= 90) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) (c - 65);
                    bpos = bpos2;
                } else if (c >= 97 && c <= 122) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) ((c - 97) + 26);
                    bpos = bpos2;
                } else if (c >= 48 && c <= 57) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) ((c - 48) + 52);
                    bpos = bpos2;
                } else if (c == 43) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) 62;
                    bpos = bpos2;
                } else if (c == 47) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) 63;
                    bpos = bpos2;
                } else if (c == 61) {
                    bpos2 = bpos + 1;
                    buff[bpos] = (byte) 64;
                    bpos = bpos2;
                } else {
                    throw new IOException("Illegal char in base64 code.");
                }
                if (bpos == 4) {
                    bpos = 0;
                    int destpos2;
                    int v;
                    if (buff[0] == (byte) 64) {
                        break;
                    } else if (buff[1] == (byte) 64) {
                        throw new IOException("Unexpected '=' in base64 code.");
                    } else if (buff[2] == (byte) 64) {
                        destpos2 = destpos + 1;
                        dest[destpos] = (byte) ((((buff[0] & 63) << 6) | (buff[1] & 63)) >> 4);
                        destpos = destpos2;
                        break;
                    } else if (buff[3] == (byte) 64) {
                        v = (((buff[0] & 63) << 12) | ((buff[1] & 63) << 6)) | (buff[2] & 63);
                        destpos2 = destpos + 1;
                        dest[destpos] = (byte) (v >> 10);
                        destpos = destpos2 + 1;
                        dest[destpos2] = (byte) (v >> 2);
                        break;
                    } else {
                        v = ((((buff[0] & 63) << 18) | ((buff[1] & 63) << 12)) | ((buff[2] & 63) << 6)) | (buff[3] & 63);
                        destpos2 = destpos + 1;
                        dest[destpos] = (byte) (v >> 16);
                        destpos = destpos2 + 1;
                        dest[destpos2] = (byte) (v >> 8);
                        destpos2 = destpos + 1;
                        dest[destpos] = (byte) v;
                        destpos = destpos2;
                    }
                } else {
                    continue;
                }
            }
        }
        byte[] res = new byte[destpos];
        System.arraycopy(dest, 0, res, 0, destpos);
        return res;
    }
}
