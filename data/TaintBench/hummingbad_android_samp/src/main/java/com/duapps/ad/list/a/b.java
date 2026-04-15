package com.duapps.ad.list.a;

import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.g;
import com.duapps.ad.base.x;
import com.duapps.ad.entity.a;
import com.duapps.ad.entity.c;
import com.duapps.ad.entity.h;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.List;

/* compiled from: DownloadAdsManager */
final class b implements x<c> {
    private /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        c cVar = (c) obj;
        if (i != CtaButton.WIDTH_DIPS || cVar == null) {
            this.a.b = false;
            return;
        }
        List a = g.a(this.a.e, a.a(this.a, cVar.a));
        synchronized (this.a.h) {
            if (a.size() <= 0) {
                d.a(this.a.e, this.a.f);
                this.a.a = true;
                this.a.b = false;
                return;
            }
            int e = this.a.j - this.a.b();
            int size = a.size() > e ? e : a.size();
            for (int i2 = 0; i2 < size; i2++) {
                a aVar = (a) a.get(i2);
                if (aVar != null && aVar.a()) {
                    this.a.i = this.a.i + 1;
                    this.a.h.add(new h(this.a.e, (a) a.get(i2), null));
                }
            }
            d.b(this.a.e, this.a.i == 0 ? "FAIL" : "OK", this.a.f);
            f.a("DownloadAdsManager", "store data into cache list -- list.size = " + this.a.h.size());
            this.a.b = false;
        }
    }

    public final void a() {
        f.a("DownloadAdsManager", "start load cache data--");
        this.a.b = true;
    }

    public final void a(String str) {
        this.a.a = true;
        this.a.b = false;
        f.a("DownloadAdsManager", "fail to get cache -" + str);
    }
}
