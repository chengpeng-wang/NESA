package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class kv extends DataInputStream {
    final /* synthetic */ kt a;

    kv(kt ktVar, InputStream inputStream) {
        this.a = ktVar;
        super(inputStream);
    }

    public final void close() {
    }
}
