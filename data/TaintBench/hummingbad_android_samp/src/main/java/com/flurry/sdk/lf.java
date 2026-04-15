package com.flurry.sdk;

import java.io.DataOutputStream;
import java.io.OutputStream;

final class lf extends DataOutputStream {
    final /* synthetic */ le a;

    lf(le leVar, OutputStream outputStream) {
        this.a = leVar;
        super(outputStream);
    }

    public final void close() {
    }
}
