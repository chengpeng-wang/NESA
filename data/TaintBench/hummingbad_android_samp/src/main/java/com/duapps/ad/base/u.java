package com.duapps.ad.base;

import android.content.Context;
import com.duapps.ad.entity.c;
import com.duapps.ad.entity.h;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: ToolboxOLManager */
public class u extends a<NativeAd> {
    /* access modifiers changed from: private|static|final */
    public static final String h = u.class.getSimpleName();
    private int i;
    /* access modifiers changed from: private|final */
    public final List<com.duapps.ad.entity.a> j = Collections.synchronizedList(new ArrayList());
    private x<c> k = new v(this);

    static /* synthetic */ List a(u uVar, List list) {
        ArrayList arrayList = new ArrayList();
        for (com.duapps.ad.entity.a aVar : list) {
            if (!d.a(uVar.e, aVar.c)) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    public u(Context context, int i, long j, int i2) {
        super(context, i, j);
        this.i = i2;
    }

    public final void a() {
        if (!d.b(this.e)) {
            return;
        }
        if (this.b) {
            f.c(h, "isRefreshing ...");
            return;
        }
        int b = b();
        if (this.i - b <= 0) {
            f.c(h, "no need refresh");
        } else {
            j.a(this.e).b(Integer.valueOf(this.f).intValue(), 1, this.k, this.i - b);
        }
    }

    public final int b() {
        int i;
        synchronized (this.j) {
            Iterator it = this.j.iterator();
            i = 0;
            while (it.hasNext()) {
                com.duapps.ad.entity.a aVar = (com.duapps.ad.entity.a) it.next();
                if (aVar == null) {
                    it.remove();
                } else {
                    if (d.a(this.e, aVar.c) || !aVar.a()) {
                        it.remove();
                    } else {
                        i++;
                    }
                }
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    /* renamed from: f */
    public h d() {
        com.duapps.ad.entity.a aVar;
        synchronized (this.j) {
            com.duapps.ad.entity.a aVar2 = null;
            while (this.j.size() > 0) {
                aVar2 = (com.duapps.ad.entity.a) this.j.remove(0);
                if (aVar2 != null && aVar2.a()) {
                    break;
                }
            }
            aVar = aVar2;
            f.c(h, "OL poll title-> " + (aVar != null ? aVar.b : "null"));
        }
        d.e(this.e, aVar == null ? "FAIL" : "OK", this.f);
        if (aVar == null) {
            return null;
        }
        return new h(this.e, aVar, this.g);
    }

    public final void c() {
        synchronized (this.j) {
            this.j.clear();
        }
    }
}
