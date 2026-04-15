package com.flurry.sdk;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

final class bz extends jk {
    final /* synthetic */ bx a;

    bz(bx bxVar) {
        this.a = bxVar;
    }

    public final void a(jg jgVar) {
        if (!this.a.m) {
            iw.a(3, bx.g, "Downloader: Download status code is:" + jgVar.i + " for url: " + this.a.b);
            this.a.f = jgVar.b();
            hz.a.b(new ca(this));
        }
    }

    public final void a(jg jgVar, InputStream inputStream) {
        Throwable th;
        if (this.a.m) {
            throw new IOException("Downloader: request cancelled");
        }
        this.a.e = this.a.a(jgVar);
        if (this.a.e > this.a.h) {
            throw new IOException("Downloader: content length: " + this.a.e + " exceeds size limit: " + this.a.h);
        }
        Closeable csVar;
        try {
            csVar = new cs(inputStream, this.a.h);
            try {
                lt.a(csVar, this.a.a());
                this.a.b();
                lt.a(csVar);
            } catch (Throwable th2) {
                th = th2;
                this.a.b();
                lt.a(csVar);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            csVar = null;
            this.a.b();
            lt.a(csVar);
            throw th;
        }
    }
}
