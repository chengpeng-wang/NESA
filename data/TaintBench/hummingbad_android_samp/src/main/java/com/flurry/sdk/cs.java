package com.flurry.sdk;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

final class cs extends FilterInputStream {
    private final long a;
    private long b;

    public cs(InputStream inputStream, long j) {
        super(inputStream);
        this.a = j;
    }

    private int a(int i) {
        this.b += (long) i;
        if (this.b <= this.a) {
            return i;
        }
        throw new IOException("Size limit exceeded: " + this.a + " bytes, read " + this.b + " bytes!");
    }

    public final void close() {
        this.in = null;
    }

    public final int read() {
        return a(super.read());
    }

    public final int read(byte[] bArr) {
        return a(super.read(bArr));
    }

    public final int read(byte[] bArr, int i, int i2) {
        return a(super.read(bArr, i, i2));
    }
}
