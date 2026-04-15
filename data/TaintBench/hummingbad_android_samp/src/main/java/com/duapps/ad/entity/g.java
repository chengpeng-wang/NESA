package com.duapps.ad.entity;

import android.os.SystemClock;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;

/* compiled from: FacebookCacheManager */
final class g implements e {
    private /* synthetic */ String a;
    private /* synthetic */ l b;
    private /* synthetic */ long c;
    private /* synthetic */ int d;
    private /* synthetic */ f e;

    g(f fVar, String str, l lVar, long j, int i) {
        this.e = fVar;
        this.a = str;
        this.b = lVar;
        this.c = j;
        this.d = i;
    }

    public final void a(int i, String str) {
        f.c("FbCache", "onError: code=" + i + "; msg=" + str);
        this.e.a = true;
        a(i);
    }

    public final void a() {
        f.c("FbCache", "onAdLoaded: id=" + this.a);
        i.a(this.e.m);
        i.b(this.e.m);
        synchronized (this.e.k) {
            this.e.k.add(this.b);
        }
        a(CtaButton.WIDTH_DIPS);
    }

    public final void b() {
        if (this.e.g != null) {
            this.e.g.onAdClick();
        }
    }

    private void a(int i) {
        d.a(this.e.m, this.e.f, i, SystemClock.elapsedRealtime() - this.c);
        f.c("FbCache", "Refresh result: id = " + this.b.b() + "; code = " + i);
        if (this.d > 0) {
            this.e.l.obtainMessage(2, this.d - 1, 0).sendToTarget();
            return;
        }
        this.e.b = false;
        f.c("FbCache", "Refresh result: DONE for geeen count");
    }
}
