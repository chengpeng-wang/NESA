package com.flurry.sdk;

final class fq implements je {
    final /* synthetic */ String a;
    final /* synthetic */ ct b;
    final /* synthetic */ fl c;

    fq(fl flVar, String str, ct ctVar) {
        this.c = flVar;
        this.a = str;
        this.b = ctVar;
    }

    public final /* synthetic */ void a(jc jcVar, Object obj) {
        iw.a(3, fl.a, "Prerender: HTTP status code is:" + jcVar.i + " for url: " + this.a);
        if (jcVar.b()) {
            gz.a(this.c.f);
            this.c.c();
            return;
        }
        this.c.a(this.b, df.kPrerenderDownloadFailed);
        this.c.c();
    }
}
