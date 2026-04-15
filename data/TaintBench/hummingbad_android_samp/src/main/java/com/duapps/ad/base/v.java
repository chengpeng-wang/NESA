package com.duapps.ad.base;

import com.duapps.ad.entity.c;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.List;

/* compiled from: ToolboxOLManager */
final class v implements x<c> {
    private /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        c cVar = (c) obj;
        this.a.b = false;
        if (i == CtaButton.WIDTH_DIPS && cVar != null) {
            List a = g.a(this.a.e, u.a(this.a, cVar.a));
            synchronized (this.a.j) {
                if (a.size() <= 0) {
                    d.c(this.a.e, this.a.f);
                    return;
                }
                this.a.j.addAll(a);
                f.a(u.h, "store data into cache list -- list.size = " + this.a.j.size());
            }
        }
    }

    public final void a() {
        f.a(u.h, "start load cache data--");
        this.a.b = true;
        this.a.c = true;
    }

    public final void a(String str) {
        f.a(u.h, "fail to get cache -" + str);
        this.a.a = true;
        this.a.b = false;
    }
}
