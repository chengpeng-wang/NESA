package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class dm extends DataInputStream {
    final /* synthetic */ dk a;

    dm(dk dkVar, InputStream inputStream) {
        this.a = dkVar;
        super(inputStream);
    }

    public final void close() {
    }
}
