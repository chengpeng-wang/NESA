package com.flurry.sdk;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class cn extends BufferedOutputStream {
    /* access modifiers changed from: private */
    public boolean a;

    private cn(OutputStream outputStream) {
        super(outputStream);
        this.a = false;
    }

    /* synthetic */ cn(OutputStream outputStream, byte b) {
        this(outputStream);
    }

    public final void close() {
        try {
            super.close();
        } catch (IOException e) {
            this.a = true;
            throw e;
        }
    }

    public final void flush() {
        try {
            super.flush();
        } catch (IOException e) {
            this.a = true;
            throw e;
        }
    }

    public final void write(int i) {
        try {
            super.write(i);
        } catch (IOException e) {
            this.a = true;
            throw e;
        }
    }

    public final void write(byte[] bArr) {
        try {
            super.write(bArr);
        } catch (IOException e) {
            this.a = true;
            throw e;
        }
    }

    public final void write(byte[] bArr, int i, int i2) {
        try {
            super.write(bArr, i, i2);
        } catch (IOException e) {
            this.a = true;
            throw e;
        }
    }
}
