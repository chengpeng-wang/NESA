package com.flurry.sdk;

import android.support.annotation.NonNull;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class bq extends FilterOutputStream {
    final /* synthetic */ bp a;

    private bq(bp bpVar, OutputStream outputStream) {
        this.a = bpVar;
        super(outputStream);
    }

    /* synthetic */ bq(bp bpVar, OutputStream outputStream, byte b) {
        this(bpVar, outputStream);
    }

    public final void close() {
        try {
            this.out.close();
        } catch (IOException e) {
            this.a.c = true;
        }
    }

    public final void flush() {
        try {
            this.out.flush();
        } catch (IOException e) {
            this.a.c = true;
        }
    }

    public final void write(int i) {
        try {
            this.out.write(i);
        } catch (IOException e) {
            this.a.c = true;
        }
    }

    public final void write(@NonNull byte[] bArr, int i, int i2) {
        try {
            this.out.write(bArr, i, i2);
        } catch (IOException e) {
            this.a.c = true;
        }
    }
}
