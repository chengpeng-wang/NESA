package com.duapps.ad.b;

import com.duapps.ad.base.d;
import com.duapps.ad.base.x;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.List;

/* compiled from: InMobiCacheManager */
final class f implements x<c> {
    private /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        c cVar = (c) obj;
        this.a.b = false;
        if (i == CtaButton.WIDTH_DIPS && cVar != null) {
            com.duapps.ad.base.f.c("InMobiCacheManager", i);
            List a = e.a(this.a, cVar.a);
            synchronized (this.a.i) {
                if (a.size() <= 0) {
                    d.b(this.a.e, this.a.f);
                    return;
                }
                this.a.i.addAll(a);
                com.duapps.ad.base.f.a("InMobiCacheManager", "store data into cache list -- list.size = " + this.a.i.size());
            }
        }
    }

    public final void a() {
        com.duapps.ad.base.f.a("InMobiCacheManager", "start load cache data--");
        this.a.b = true;
        this.a.c = true;
    }

    public final void a(String str) {
        com.duapps.ad.base.f.a("InMobiCacheManager", "fail to get cache -" + str);
        this.a.a = true;
        this.a.b = false;
    }
}
