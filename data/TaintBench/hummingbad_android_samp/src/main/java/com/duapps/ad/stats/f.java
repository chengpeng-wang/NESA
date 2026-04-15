package com.duapps.ad.stats;

import com.duapps.ad.base.d;

/* compiled from: ToolClickHandler */
final class f implements Runnable {
    private /* synthetic */ b a;

    f(b bVar) {
        this.a = bVar;
    }

    public final void run() {
        if (com.duapps.ad.base.f.a()) {
            com.duapps.ad.base.f.c("ToolClickHandler", "[WebView] timeout TIMEOUT_FINISH.");
        }
        if (!this.a.f && !this.a.h) {
            this.a.f = true;
            if (b.c(this.a)) {
                if (com.duapps.ad.base.f.a()) {
                    com.duapps.ad.base.f.c("ToolClickHandler", "[WebView]FinishRunnable canceled.");
                }
                d.g(b.this.c, this.a.a);
                b.this.f();
                return;
            }
            if (this.a.b != null) {
                this.a.b.stopLoading();
            }
            b.this.b();
            b.this.g(this.a.a, this.a.a.h());
            b.this.f();
        }
    }
}
