package com.flurry.sdk;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import u.aly.bf;

final class bt implements Closeable {
    final Charset a;
    private final InputStream b;
    private byte[] c;
    private int d;
    private int e;

    bt(InputStream inputStream) {
        if (bv.a == null) {
            throw new NullPointerException();
        }
        this.b = inputStream;
        this.a = bv.a;
        this.c = new byte[8192];
    }

    private void b() {
        int read = this.b.read(this.c, 0, this.c.length);
        if (read == -1) {
            throw new EOFException();
        }
        this.d = 0;
        this.e = read;
    }

    public final String a() {
        String str;
        synchronized (this.b) {
            if (this.c == null) {
                throw new IOException("LineReader is closed");
            }
            int i;
            if (this.d >= this.e) {
                b();
            }
            int i2 = this.d;
            while (i2 != this.e) {
                if (this.c[i2] == (byte) 10) {
                    int i3 = (i2 == this.d || this.c[i2 - 1] != bf.k) ? i2 : i2 - 1;
                    str = new String(this.c, this.d, i3 - this.d, this.a.name());
                    this.d = i2 + 1;
                } else {
                    i2++;
                }
            }
            bu buVar = new bu(this, (this.e - this.d) + 80);
            loop1:
            while (true) {
                buVar.write(this.c, this.d, this.e - this.d);
                this.e = -1;
                b();
                i = this.d;
                while (i != this.e) {
                    if (this.c[i] == (byte) 10) {
                        break loop1;
                    }
                    i++;
                }
            }
            if (i != this.d) {
                buVar.write(this.c, this.d, i - this.d);
            }
            this.d = i + 1;
            str = buVar.toString();
        }
        return str;
    }

    public final void close() {
        synchronized (this.b) {
            if (this.c != null) {
                this.c = null;
                this.b.close();
            }
        }
    }
}
