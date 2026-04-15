package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

final class jd implements jl {
    final /* synthetic */ jc a;

    jd(jc jcVar) {
        this.a = jcVar;
    }

    public final void a(jg jgVar) {
        jc.d(this.a);
    }

    public final void a(jg jgVar, InputStream inputStream) {
        if (jgVar.c() && this.a.d != null) {
            this.a.k = this.a.d.a(inputStream);
        }
    }

    public final void a(OutputStream outputStream) {
        if (this.a.b != null && this.a.c != null) {
            this.a.c.a(outputStream, this.a.b);
        }
    }
}
