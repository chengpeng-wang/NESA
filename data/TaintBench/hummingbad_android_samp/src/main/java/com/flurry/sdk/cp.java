package com.flurry.sdk;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public final class cp implements Closeable {
    final cn a;
    final /* synthetic */ ck b;
    private final bp c;
    private final OutputStream d;
    private final GZIPOutputStream e;
    private boolean f;

    private cp(ck ckVar, bp bpVar, boolean z) {
        this.b = ckVar;
        if (bpVar == null) {
            throw new IllegalArgumentException("Editor cannot be null");
        }
        this.c = bpVar;
        this.d = this.c.a();
        if (this.d == null) {
            throw new IOException("Editor outputstream is null");
        } else if (z) {
            this.e = new GZIPOutputStream(this.d);
            this.a = new cn(this.e, (byte) 0);
        } else {
            this.e = null;
            this.a = new cn(this.d, (byte) 0);
        }
    }

    /* synthetic */ cp(ck ckVar, bp bpVar, boolean z, byte b) {
        this(ckVar, bpVar, z);
    }

    public final void close() {
        if (!this.f) {
            this.f = true;
            lt.a(this.a);
            lt.a(this.e);
            lt.a(this.d);
            if (this.c == null) {
                return;
            }
            if (this.a.a) {
                try {
                    this.c.b();
                    return;
                } catch (IOException e) {
                    iw.a(3, ck.a, "Exception closing editor for cache: " + this.b.b, e);
                    return;
                }
            }
            bp bpVar = this.c;
            if (bpVar.c) {
                bpVar.d.a(bpVar, false);
                bpVar.d.c(bpVar.a.a);
                return;
            }
            bpVar.d.a(bpVar, true);
        }
    }

    /* access modifiers changed from: protected|final */
    public final void finalize() {
        super.finalize();
        close();
    }
}
