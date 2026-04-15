package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class ex extends DataInputStream {
    final /* synthetic */ ev a;

    ex(ev evVar, InputStream inputStream) {
        this.a = evVar;
        super(inputStream);
    }

    public final void close() {
    }
}
