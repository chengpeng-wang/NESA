package com.flurry.sdk;

import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;

public class cq extends bx {
    private static final String g = cq.class.getSimpleName();
    private final ck h;
    private final String i;
    private cp j;

    public cq(ck ckVar, String str) {
        this.h = ckVar;
        this.i = str;
    }

    /* access modifiers changed from: protected|final */
    public final OutputStream a() {
        if (this.j != null) {
            return this.j.a;
        }
        if (this.h == null) {
            throw new IOException("No cache specified");
        } else if (TextUtils.isEmpty(this.i)) {
            throw new IOException("No cache key specified");
        } else {
            this.j = this.h.b(this.i);
            if (this.j != null) {
                return this.j.a;
            }
            throw new IOException("Could not open writer for key: " + this.i);
        }
    }

    /* access modifiers changed from: protected|final */
    public final void b() {
        lt.a(this.j);
        this.j = null;
    }

    /* access modifiers changed from: protected|final */
    public final void c() {
        if (this.h != null && !TextUtils.isEmpty(this.i)) {
            try {
                this.h.c(this.i);
            } catch (Exception e) {
                iw.a(3, g, "Error removing result for key: " + this.i + " -- " + e);
            }
        }
    }
}
