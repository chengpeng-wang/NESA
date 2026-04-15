package com.duapps.ad.stats;

import com.duapps.ad.base.d;
import com.duapps.ad.base.f;

/* compiled from: ToolClickHandler */
final class g implements Runnable {
    private /* synthetic */ b a;

    g(b bVar) {
        this.a = bVar;
    }

    public final void run() {
        if (f.a()) {
            f.c("ToolClickHandler", "[WebView] Timeout TIMEOUT_START.");
        }
        if (!this.a.f && !this.a.h) {
            this.a.f = true;
            if (b.c(this.a)) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] StartRunnable canceled.");
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
