package com.mopub.common;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

class DiskLruCacheStrictLineReader implements Closeable {
    private static final byte CR = (byte) 13;
    private static final byte LF = (byte) 10;
    private byte[] buf;
    /* access modifiers changed from: private|final */
    public final Charset charset;
    private int end;
    private final InputStream in;
    private int pos;

    public DiskLruCacheStrictLineReader(InputStream in, Charset charset) {
        this(in, 8192, charset);
    }

    public DiskLruCacheStrictLineReader(InputStream in, int capacity, Charset charset) {
        if (in == null || charset == null) {
            throw new NullPointerException();
        } else if (capacity < 0) {
            throw new IllegalArgumentException("capacity <= 0");
        } else if (charset.equals(DiskLruCacheUtil.US_ASCII)) {
            this.in = in;
            this.charset = charset;
            this.buf = new byte[capacity];
        } else {
            throw new IllegalArgumentException("Unsupported encoding");
        }
    }

    public void close() throws IOException {
        synchronized (this.in) {
            if (this.buf != null) {
                this.buf = null;
                this.in.close();
            }
        }
    }

    public String readLine() throws IOException {
        String str;
        synchronized (this.in) {
            if (this.buf == null) {
                throw new IOException("LineReader is closed");
            }
            int i;
            if (this.pos >= this.end) {
                fillBuf();
            }
            int i2 = this.pos;
            while (i2 != this.end) {
                if (this.buf[i2] == (byte) 10) {
                    int i3 = (i2 == this.pos || this.buf[i2 - 1] != (byte) 13) ? i2 : i2 - 1;
                    str = new String(this.buf, this.pos, i3 - this.pos, this.charset.name());
                    this.pos = i2 + 1;
                } else {
                    i2++;
                }
            }
            AnonymousClass1 anonymousClass1 = new ByteArrayOutputStream((this.end - this.pos) + 80) {
                public String toString() {
                    int i = (this.count <= 0 || this.buf[this.count - 1] != (byte) 13) ? this.count : this.count - 1;
                    try {
                        return new String(this.buf, 0, i, DiskLruCacheStrictLineReader.this.charset.name());
                    } catch (UnsupportedEncodingException e) {
                        throw new AssertionError(e);
                    }
                }
            };
            loop1:
            while (true) {
                anonymousClass1.write(this.buf, this.pos, this.end - this.pos);
                this.end = -1;
                fillBuf();
                i = this.pos;
                while (i != this.end) {
                    if (this.buf[i] == (byte) 10) {
                        break loop1;
                    }
                    i++;
                }
            }
            if (i != this.pos) {
                anonymousClass1.write(this.buf, this.pos, i - this.pos);
            }
            this.pos = i + 1;
            str = anonymousClass1.toString();
        }
        return str;
    }

    private void fillBuf() throws IOException {
        int read = this.in.read(this.buf, 0, this.buf.length);
        if (read == -1) {
            throw new EOFException();
        }
        this.pos = 0;
        this.end = read;
    }
}
