package com.duapps.ad.base;

import com.duapps.ad.entity.a;
import com.duapps.ad.entity.c;
import com.duapps.ad.stats.j;

/* compiled from: ToolCacheManager */
final class q implements Runnable {
    private /* synthetic */ c a;
    private /* synthetic */ j b;

    q(j jVar, c cVar) {
        this.b = jVar;
        this.a = cVar;
    }

    public final void run() {
        if (this.a == null || this.a.a == null || this.a.a.size() == 0) {
            f.c("ToolboxCacheManager", "ads == null || ads.list == null || ads.list.size() == 0");
            return;
        }
        this.b.a();
        try {
            for (a aVar : this.a.a) {
                f.c("ToolboxCacheManager", "wall---before insert: ad.package: " + aVar.c + ",ad.preParse:" + aVar.u);
                if (aVar.u == 1) {
                    this.b.b(new j(aVar));
                }
            }
        } catch (Exception e) {
            if (f.a()) {
                f.a("ToolboxCacheManager", "wall---batch update or insert triggerPreParse data error: ", e);
            }
        }
    }
}
