package com.flurry.sdk;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class co implements Closeable {
    final BufferedInputStream a;
    final /* synthetic */ ck b;
    private final bs c;
    private final InputStream d;
    private final GZIPInputStream e;
    private boolean f;

    private co(ck ckVar, bs bsVar, boolean z) {
        this.b = ckVar;
        if (bsVar == null) {
            throw new IllegalArgumentException("Snapshot cannot be null");
        }
        this.c = bsVar;
        this.d = this.c.a[0];
        if (this.d == null) {
            throw new IOException("Snapshot inputstream is null");
        } else if (z) {
            this.e = new GZIPInputStream(this.d);
            this.a = new BufferedInputStream(this.e);
        } else {
            this.e = null;
            this.a = new BufferedInputStream(this.d);
        }
    }

    /* synthetic */ co(ck ckVar, bs bsVar, boolean z, byte b) {
        this(ckVar, bsVar, z);
    }

    public final void close() {
        if (!this.f) {
            this.f = true;
            lt.a(this.a);
            lt.a(this.e);
            lt.a(this.d);
            lt.a(this.c);
        }
    }

    /* access modifiers changed from: protected|final */
    public final void finalize() {
        super.finalize();
        close();
    }
}
