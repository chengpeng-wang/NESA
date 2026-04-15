package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class dc extends DataInputStream {
    final /* synthetic */ da a;

    dc(da daVar, InputStream inputStream) {
        this.a = daVar;
        super(inputStream);
    }

    public final void close() {
    }
}
