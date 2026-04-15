package com.sun.mail.util;

import android.support.v4.view.MotionEventCompat;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BASE64EncoderStream extends FilterOutputStream {
    private static byte[] newline = new byte[]{(byte) 13, (byte) 10};
    private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private byte[] buffer;
    private int bufsize;
    private int bytesPerLine;
    private int count;
    private int lineLimit;
    private boolean noCRLF;
    private byte[] outbuf;

    public BASE64EncoderStream(OutputStream out, int bytesPerLine) {
        super(out);
        this.bufsize = 0;
        this.count = 0;
        this.noCRLF = false;
        this.buffer = new byte[3];
        if (bytesPerLine == Integer.MAX_VALUE || bytesPerLine < 4) {
            this.noCRLF = true;
            bytesPerLine = 76;
        }
        bytesPerLine = (bytesPerLine / 4) * 4;
        this.bytesPerLine = bytesPerLine;
        this.lineLimit = (bytesPerLine / 4) * 3;
        if (this.noCRLF) {
            this.outbuf = new byte[bytesPerLine];
            return;
        }
        this.outbuf = new byte[(bytesPerLine + 2)];
        this.outbuf[bytesPerLine] = (byte) 13;
        this.outbuf[bytesPerLine + 1] = (byte) 10;
    }

    public BASE64EncoderStream(OutputStream out) {
        this(out, 76);
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        Throwable th;
        int outlen;
        int end = off + len;
        int off2 = off;
        while (this.bufsize != 0 && off2 < end) {
            try {
                off = off2 + 1;
                write(b[off2]);
                off2 = off;
            } catch (Throwable th2) {
                th = th2;
                off = off2;
                throw th;
            }
        }
        int blen = ((this.bytesPerLine - this.count) / 4) * 3;
        if (off2 + blen < end) {
            outlen = encodedSize(blen);
            if (!this.noCRLF) {
                int i = outlen + 1;
                this.outbuf[outlen] = (byte) 13;
                outlen = i + 1;
                this.outbuf[i] = (byte) 10;
            }
            this.out.write(encode(b, off2, blen, this.outbuf), 0, outlen);
            off = off2 + blen;
            try {
                this.count = 0;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
        off = off2;
        while (this.lineLimit + off < end) {
            this.out.write(encode(b, off, this.lineLimit, this.outbuf));
            off += this.lineLimit;
        }
        if (off + 3 < end) {
            blen = ((end - off) / 3) * 3;
            outlen = encodedSize(blen);
            this.out.write(encode(b, off, blen, this.outbuf), 0, outlen);
            off += blen;
            this.count += outlen;
        }
        while (off < end) {
            write(b[off]);
            off++;
        }
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public synchronized void write(int c) throws IOException {
        byte[] bArr = this.buffer;
        int i = this.bufsize;
        this.bufsize = i + 1;
        bArr[i] = (byte) c;
        if (this.bufsize == 3) {
            encode();
            this.bufsize = 0;
        }
    }

    public synchronized void flush() throws IOException {
        if (this.bufsize > 0) {
            encode();
            this.bufsize = 0;
        }
        this.out.flush();
    }

    public synchronized void close() throws IOException {
        flush();
        if (this.count > 0 && !this.noCRLF) {
            this.out.write(newline);
            this.out.flush();
        }
        this.out.close();
    }

    private void encode() throws IOException {
        int osize = encodedSize(this.bufsize);
        this.out.write(encode(this.buffer, 0, this.bufsize, this.outbuf), 0, osize);
        this.count += osize;
        if (this.count >= this.bytesPerLine) {
            if (!this.noCRLF) {
                this.out.write(newline);
            }
            this.count = 0;
        }
    }

    public static byte[] encode(byte[] inbuf) {
        return inbuf.length == 0 ? inbuf : encode(inbuf, 0, inbuf.length, null);
    }

    private static byte[] encode(byte[] inbuf, int off, int size, byte[] outbuf) {
        int inpos;
        int val;
        if (outbuf == null) {
            outbuf = new byte[encodedSize(size)];
        }
        int inpos2 = off;
        int outpos = 0;
        while (true) {
            inpos = inpos2;
            if (size < 3) {
                break;
            }
            inpos2 = inpos + 1;
            inpos = inpos2 + 1;
            inpos2 = inpos + 1;
            val = ((((inbuf[inpos] & MotionEventCompat.ACTION_MASK) << 8) | (inbuf[inpos2] & MotionEventCompat.ACTION_MASK)) << 8) | (inbuf[inpos] & MotionEventCompat.ACTION_MASK);
            outbuf[outpos + 3] = (byte) pem_array[val & 63];
            val >>= 6;
            outbuf[outpos + 2] = (byte) pem_array[val & 63];
            val >>= 6;
            outbuf[outpos + 1] = (byte) pem_array[val & 63];
            outbuf[outpos + 0] = (byte) pem_array[(val >> 6) & 63];
            size -= 3;
            outpos += 4;
        }
        if (size == 1) {
            inpos2 = inpos + 1;
            val = (inbuf[inpos] & MotionEventCompat.ACTION_MASK) << 4;
            outbuf[outpos + 3] = (byte) 61;
            outbuf[outpos + 2] = (byte) 61;
            outbuf[outpos + 1] = (byte) pem_array[val & 63];
            outbuf[outpos + 0] = (byte) pem_array[(val >> 6) & 63];
        } else {
            if (size == 2) {
                inpos2 = inpos + 1;
                inpos = inpos2 + 1;
                val = (((inbuf[inpos] & MotionEventCompat.ACTION_MASK) << 8) | (inbuf[inpos2] & MotionEventCompat.ACTION_MASK)) << 2;
                outbuf[outpos + 3] = (byte) 61;
                outbuf[outpos + 2] = (byte) pem_array[val & 63];
                val >>= 6;
                outbuf[outpos + 1] = (byte) pem_array[val & 63];
                outbuf[outpos + 0] = (byte) pem_array[(val >> 6) & 63];
            }
            inpos2 = inpos;
        }
        return outbuf;
    }

    private static int encodedSize(int size) {
        return ((size + 2) / 3) * 4;
    }
}
