package com.flurry.sdk;

import java.io.Closeable;
import java.io.InputStream;

public final class bs implements Closeable {
    final InputStream[] a;
    final /* synthetic */ bm b;

    private bs(bm bmVar, InputStream[] inputStreamArr) {
        this.b = bmVar;
        this.a = inputStreamArr;
    }

    /* synthetic */ bs(bm bmVar, InputStream[] inputStreamArr, byte b) {
        this(bmVar, inputStreamArr);
    }

    public final void close() {
        for (Closeable a : this.a) {
            bv.a(a);
        }
    }
}
