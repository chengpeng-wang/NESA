package com.duapps.ad.base;

import com.duapps.ad.entity.c;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.List;

/* compiled from: ToolboxDLManager */
final class s implements x<c> {
    private /* synthetic */ r a;

    s(r rVar) {
        this.a = rVar;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        int i2 = 0;
        c cVar = (c) obj;
        this.a.b = false;
        if (i == CtaButton.WIDTH_DIPS && cVar != null) {
            List a = g.a(this.a.e, r.a(this.a, cVar.a));
            synchronized (this.a.i) {
                if (a.size() <= 0) {
                    d.a(this.a.e, this.a.f);
                    return;
                }
                this.a.i.clear();
                while (i2 < a.size() && i2 <= 0) {
                    this.a.i.add(a.get(0));
                    i2++;
                }
                f.a(r.h, "store data into cache list -- list.size = " + this.a.i.size());
            }
        }
    }

    public final void a() {
        f.a(r.h, "start load cache data--");
        this.a.b = true;
        this.a.c = true;
    }

    public final void a(String str) {
        f.a(r.h, "fail to get cache -" + str);
        this.a.a = true;
        this.a.b = false;
    }
}
