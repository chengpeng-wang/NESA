package com.duapps.ad.b;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;

/* compiled from: InMobiDataExecutor */
public final class g implements Runnable {
    public a a;
    public com.duapps.ad.b.a.a b;
    public n c;
    private Context d;
    private a e;
    /* access modifiers changed from: private|volatile */
    public volatile boolean f = false;
    /* access modifiers changed from: private */
    public String g;
    private Handler h = new h(this, Looper.getMainLooper());

    /* compiled from: InMobiDataExecutor */
    public interface a {
        void a(g gVar);
    }

    static /* synthetic */ void a(g gVar) {
        if (gVar.c == null) {
            gVar.f = false;
        } else {
            gVar.c.a.setWebViewClient(new i(gVar));
        }
    }

    static /* synthetic */ void c(g gVar) {
        if (gVar.e != null) {
            gVar.e.a(gVar);
        }
    }

    public g(Context context, n nVar, a aVar, com.duapps.ad.b.a.a aVar2, a aVar3) {
        this.d = context;
        this.c = nVar;
        this.a = aVar;
        this.b = aVar2;
        if (nVar != null) {
            nVar.c = false;
        }
        this.e = aVar3;
    }

    private void a(String str) {
        if (d.b(this.d)) {
            this.f = true;
            this.g = str;
            this.h.sendEmptyMessage(100);
            return;
        }
        this.f = false;
        if (this.c != null) {
            this.c.c = false;
        }
    }

    public final void run() {
        boolean z = true;
        f.c("InMobiDataExecutor", " started");
        if (!this.f) {
            this.f = true;
            if (this.b != com.duapps.ad.b.a.a.Impression || this.a.s) {
                if (this.b == com.duapps.ad.b.a.a.Click && !this.a.t) {
                    if (!this.a.s) {
                        a(this.a.b());
                    }
                }
                z = false;
            }
            if (z) {
                a(this.a.a(this.b));
            }
        }
    }
}
