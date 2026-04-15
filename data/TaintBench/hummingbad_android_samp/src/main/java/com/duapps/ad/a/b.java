package com.duapps.ad.a;

import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.g;
import com.duapps.ad.base.x;
import com.duapps.ad.entity.c;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.List;

/* compiled from: DLHighCacheManager */
final class b implements x<c> {
    private /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        c cVar = (c) obj;
        if (i == CtaButton.WIDTH_DIPS && cVar != null) {
            this.a.b = false;
            List a = g.a(this.a.e, a.a(this.a, cVar.a));
            synchronized (this.a.i) {
                if (a.size() <= 0) {
                    d.d(this.a.e, this.a.f);
                    return;
                }
                this.a.i.clear();
                int i2 = 0;
                while (i2 < a.size() && i2 < 5) {
                    this.a.i.add(a.get(i2));
                    i2++;
                }
                f.a(a.h, "store data into cache list -- list.size = " + this.a.i.size());
                this.a.j = false;
            }
        }
    }

    public final void a() {
        f.a(a.h, "start load cache data--");
        this.a.b = true;
        this.a.c = true;
    }

    public final void a(String str) {
        f.a(a.h, "fail to get cache -" + str);
        this.a.a = true;
        this.a.b = false;
    }
}
