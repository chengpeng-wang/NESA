package com.flurry.sdk;

import java.util.List;

final class cb implements je {
    final /* synthetic */ bx a;

    cb(bx bxVar) {
        this.a = bxVar;
    }

    public final /* synthetic */ void a(jc jcVar, Object obj) {
        if (!this.a.m) {
            iw.a(3, bx.g, "Downloader: HTTP HEAD status code is:" + jcVar.i + " for url: " + this.a.b);
            if (jcVar.b()) {
                this.a.e = this.a.a((jg) jcVar);
                List a = jcVar.a("Accept-Ranges");
                if (this.a.e <= 0 || a == null || a.isEmpty()) {
                    this.a.j = 1;
                } else {
                    this.a.i = "bytes".equals(((String) a.get(0)).trim());
                    this.a.j = (int) (((long) (this.a.e % this.a.k > 0 ? 1 : 0)) + (this.a.e / this.a.k));
                }
                if (this.a.h <= 0 || this.a.e <= this.a.h) {
                    hz.a.b(new cd(this));
                    return;
                }
                iw.a(3, bx.g, "Downloader: Size limit exceeded -- limit: " + this.a.h + ", content-length: " + this.a.e + " bytes!");
                hz.a.b(new cc(this));
                return;
            }
            hz.a.b(new ce(this));
        }
    }
}
