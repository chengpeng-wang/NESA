package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.InputStream;

final class lb extends DataInputStream {
    final /* synthetic */ kz a;

    lb(kz kzVar, InputStream inputStream) {
        this.a = kzVar;
        super(inputStream);
    }

    public final void close() {
    }
}
