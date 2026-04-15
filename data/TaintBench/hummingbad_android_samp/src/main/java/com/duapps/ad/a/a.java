package com.duapps.ad.a;

import android.content.Context;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.j;
import com.duapps.ad.base.x;
import com.duapps.ad.entity.c;
import com.duapps.ad.entity.h;
import com.duapps.ad.entity.strategy.NativeAd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* compiled from: DLHighCacheManager */
public class a extends com.duapps.ad.entity.strategy.a<NativeAd> {
    /* access modifiers changed from: private|static|final */
    public static final String h = a.class.getSimpleName();
    /* access modifiers changed from: private|final */
    public final List<com.duapps.ad.entity.a> i = Collections.synchronizedList(new LinkedList());
    /* access modifiers changed from: private */
    public boolean j = true;
    private x<c> k = new b(this);

    static /* synthetic */ List a(a aVar, List list) {
        ArrayList arrayList = new ArrayList();
        for (com.duapps.ad.entity.a aVar2 : list) {
            if (!d.a(aVar.e, aVar2.c)) {
                arrayList.add(aVar2);
            }
        }
        return arrayList;
    }

    public a(Context context, int i, long j) {
        super(context, i, j);
    }

    /* access modifiers changed from: private */
    /* renamed from: f */
    public h d() {
        com.duapps.ad.entity.a aVar;
        synchronized (this.i) {
            com.duapps.ad.entity.a aVar2 = null;
            while (this.i.size() > 0) {
                aVar2 = (com.duapps.ad.entity.a) this.i.remove(0);
                if (aVar2 != null && aVar2.a()) {
                    break;
                }
            }
            aVar = aVar2;
            f.c(h, "DLH poll title-> " + (aVar != null ? aVar.b : "null"));
        }
        d.c(this.e, aVar == null ? "FAIL" : "OK", this.f);
        if (aVar == null) {
            return null;
        }
        return new h(this.e, aVar, this.g);
    }

    public final void a() {
        if (!d.b(this.e)) {
            f.c(h, "no net");
        } else if (b() > 0) {
            f.c(h, "DLH validAdCount is" + b());
        } else if (this.b) {
            f.c(h, "DLH is refreshing!");
        } else {
            j a = j.a(this.e);
            int intValue = Integer.valueOf(this.f).intValue();
            x xVar = this.k;
            boolean z = this.j;
            a.b(intValue, 1, xVar);
        }
    }

    public final int b() {
        int i;
        synchronized (this.i) {
            Iterator it = this.i.iterator();
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

    public final void c() {
        synchronized (this.i) {
            this.i.clear();
        }
    }
}
