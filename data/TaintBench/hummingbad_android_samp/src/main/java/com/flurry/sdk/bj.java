package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class bj extends DataInputStream {
    final /* synthetic */ bh a;

    bj(bh bhVar, InputStream inputStream) {
        this.a = bhVar;
        super(inputStream);
    }

    public final void close() {
    }
}
