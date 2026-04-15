package com.sun.mail.util;

import android.support.v4.view.MotionEventCompat;
import com.googleprojects.mm.MMMailContentUtil;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BASE64DecoderStream extends FilterInputStream {
    private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] pem_convert_array = new byte[256];
    private byte[] buffer;
    private int bufsize;
    private boolean ignoreErrors;
    private int index;
    private byte[] input_buffer;
    private int input_len;
    private int input_pos;

    public BASE64DecoderStream(InputStream in) {
        boolean z = false;
        super(in);
        this.buffer = new byte[3];
        this.bufsize = 0;
        this.index = 0;
        this.input_buffer = new byte[8190];
        this.input_pos = 0;
        this.input_len = 0;
        this.ignoreErrors = false;
        try {
            String s = System.getProperty("mail.mime.base64.ignoreerrors");
            if (!(s == null || s.equalsIgnoreCase("false"))) {
                z = true;
            }
            this.ignoreErrors = z;
        } catch (SecurityException e) {
        }
    }

    public BASE64DecoderStream(InputStream in, boolean ignoreErrors) {
        super(in);
        this.buffer = new byte[3];
        this.bufsize = 0;
        this.index = 0;
        this.input_buffer = new byte[8190];
        this.input_pos = 0;
        this.input_len = 0;
        this.ignoreErrors = false;
        this.ignoreErrors = ignoreErrors;
    }

    public int read() throws IOException {
        if (this.index >= this.bufsize) {
            this.bufsize = decode(this.buffer, 0, this.buffer.length);
            if (this.bufsize <= 0) {
                return -1;
            }
            this.index = 0;
        }
        byte[] bArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        return bArr[i] & MotionEventCompat.ACTION_MASK;
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        int off2;
        int off0 = off;
        while (this.index < this.bufsize && len > 0) {
            off2 = off + 1;
            byte[] bArr = this.buffer;
            int i = this.index;
            this.index = i + 1;
            buf[off] = bArr[i];
            len--;
            off = off2;
        }
        if (this.index >= this.bufsize) {
            this.index = 0;
            this.bufsize = 0;
        }
        int bsize = (len / 3) * 3;
        if (bsize > 0) {
            int size = decode(buf, off, bsize);
            off += size;
            len -= size;
            if (size != bsize) {
                if (off == off0) {
                    return -1;
                }
                return off - off0;
            }
        }
        off2 = off;
        while (len > 0) {
            int c = read();
            if (c == -1) {
                break;
            }
            off = off2 + 1;
            buf[off2] = (byte) c;
            len--;
            off2 = off;
        }
        if (off2 == off0) {
            off = off2;
            return -1;
        }
        off = off2;
        return off2 - off0;
    }

    public boolean markSupported() {
        return false;
    }

    public int available() throws IOException {
        return ((this.in.available() * 3) / 4) + (this.bufsize - this.index);
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

    private int decode(byte[] outbuf, int pos, int len) throws IOException {
        int pos0 = pos;
        while (len >= 3) {
            int got = 0;
            int val = 0;
            while (got < 4) {
                int i = getByte();
                if (i == -1 || i == -2) {
                    boolean atEOF;
                    if (i == -1) {
                        if (got == 0) {
                            return pos - pos0;
                        }
                        if (this.ignoreErrors) {
                            atEOF = true;
                        } else {
                            throw new IOException("Error in encoded stream: needed 4 valid base64 characters but only got " + got + " before EOF" + recentChars());
                        }
                    } else if (got < 2 && !this.ignoreErrors) {
                        throw new IOException("Error in encoded stream: needed at least 2 valid base64 characters, but only got " + got + " before padding character (=)" + recentChars());
                    } else if (got == 0) {
                        return pos - pos0;
                    } else {
                        atEOF = false;
                    }
                    int size = got - 1;
                    if (size == 0) {
                        size = 1;
                    }
                    val <<= 6;
                    for (got++; got < 4; got++) {
                        if (!atEOF) {
                            i = getByte();
                            if (i == -1) {
                                if (!this.ignoreErrors) {
                                    throw new IOException("Error in encoded stream: hit EOF while looking for padding characters (=)" + recentChars());
                                }
                            } else if (!(i == -2 || this.ignoreErrors)) {
                                throw new IOException("Error in encoded stream: found valid base64 character after a padding character (=)" + recentChars());
                            }
                        }
                        val <<= 6;
                    }
                    val >>= 8;
                    if (size == 2) {
                        outbuf[pos + 1] = (byte) (val & MotionEventCompat.ACTION_MASK);
                    }
                    outbuf[pos] = (byte) ((val >> 8) & MotionEventCompat.ACTION_MASK);
                    return (pos + size) - pos0;
                }
                got++;
                val = (val << 6) | i;
            }
            outbuf[pos + 2] = (byte) (val & MotionEventCompat.ACTION_MASK);
            val >>= 8;
            outbuf[pos + 1] = (byte) (val & MotionEventCompat.ACTION_MASK);
            outbuf[pos] = (byte) ((val >> 8) & MotionEventCompat.ACTION_MASK);
            len -= 3;
            pos += 3;
        }
        return pos - pos0;
    }

    private int getByte() throws IOException {
        int c;
        do {
            if (this.input_pos >= this.input_len) {
                try {
                    this.input_len = this.in.read(this.input_buffer);
                    if (this.input_len <= 0) {
                        return -1;
                    }
                    this.input_pos = 0;
                } catch (EOFException e) {
                    return -1;
                }
            }
            byte[] bArr = this.input_buffer;
            int i = this.input_pos;
            this.input_pos = i + 1;
            c = bArr[i] & MotionEventCompat.ACTION_MASK;
            if (c == 61) {
                return -2;
            }
            c = pem_convert_array[c];
        } while (c == -1);
        return c;
    }

    private String recentChars() {
        int nc = 10;
        String errstr = MMMailContentUtil.MM_MESSAGE_SUBJECT;
        if (this.input_pos <= 10) {
            nc = this.input_pos;
        }
        if (nc <= 0) {
            return errstr;
        }
        errstr = new StringBuilder(String.valueOf(errstr)).append(", the ").append(nc).append(" most recent characters were: \"").toString();
        for (int k = this.input_pos - nc; k < this.input_pos; k++) {
            char c = (char) (this.input_buffer[k] & MotionEventCompat.ACTION_MASK);
            switch (c) {
                case MotionEventCompat.ACTION_HOVER_ENTER /*9*/:
                    errstr = new StringBuilder(String.valueOf(errstr)).append("\\t").toString();
                    break;
                case MotionEventCompat.ACTION_HOVER_EXIT /*10*/:
                    errstr = new StringBuilder(String.valueOf(errstr)).append("\\n").toString();
                    break;
                case 13:
                    errstr = new StringBuilder(String.valueOf(errstr)).append("\\r").toString();
                    break;
                default:
                    if (c >= ' ' && c < 127) {
                        errstr = new StringBuilder(String.valueOf(errstr)).append(c).toString();
                        break;
                    }
                    errstr = new StringBuilder(String.valueOf(errstr)).append("\\").append(c).toString();
                    break;
                    break;
            }
        }
        return new StringBuilder(String.valueOf(errstr)).append("\"").toString();
    }

    public static byte[] decode(byte[] inbuf) {
        int size = (inbuf.length / 4) * 3;
        if (size == 0) {
            return inbuf;
        }
        if (inbuf[inbuf.length - 1] == (byte) 61) {
            size--;
            if (inbuf[inbuf.length - 2] == (byte) 61) {
                size--;
            }
        }
        byte[] outbuf = new byte[size];
        int outpos = 0;
        size = inbuf.length;
        int inpos = 0;
        while (size > 0) {
            int osize = 3;
            int inpos2 = inpos + 1;
            inpos = inpos2 + 1;
            int val = ((pem_convert_array[inbuf[inpos] & MotionEventCompat.ACTION_MASK] << 6) | pem_convert_array[inbuf[inpos2] & MotionEventCompat.ACTION_MASK]) << 6;
            if (inbuf[inpos] != (byte) 61) {
                inpos2 = inpos + 1;
                val |= pem_convert_array[inbuf[inpos] & MotionEventCompat.ACTION_MASK];
            } else {
                osize = 3 - 1;
                inpos2 = inpos;
            }
            val <<= 6;
            if (inbuf[inpos2] != (byte) 61) {
                val |= pem_convert_array[inbuf[inpos2] & MotionEventCompat.ACTION_MASK];
                inpos2++;
            } else {
                osize--;
            }
            if (osize > 2) {
                outbuf[outpos + 2] = (byte) (val & MotionEventCompat.ACTION_MASK);
            }
            val >>= 8;
            if (osize > 1) {
                outbuf[outpos + 1] = (byte) (val & MotionEventCompat.ACTION_MASK);
            }
            outbuf[outpos] = (byte) ((val >> 8) & MotionEventCompat.ACTION_MASK);
            outpos += osize;
            size -= 4;
            inpos = inpos2;
        }
        return outbuf;
    }
}
