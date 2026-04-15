package com.duapps.ad.base;

import android.content.Context;
import com.duapps.ad.entity.c;
import com.duapps.ad.entity.h;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* compiled from: ToolboxDLManager */
public class r extends a<NativeAd> {
    /* access modifiers changed from: private|static|final */
    public static final String h = r.class.getSimpleName();
    /* access modifiers changed from: private|final */
    public final List<com.duapps.ad.entity.a> i = Collections.synchronizedList(new LinkedList());
    private x<c> j = new s(this);

    static /* synthetic */ List a(r rVar, List list) {
        ArrayList arrayList = new ArrayList();
        for (com.duapps.ad.entity.a aVar : list) {
            if (!d.a(rVar.e, aVar.c)) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    public r(Context context, int i, long j) {
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
            f.c(h, "DL poll title-> " + (aVar != null ? aVar.b : "null"));
        }
        d.b(this.e, aVar == null ? "FAIL" : "OK", this.f);
        if (aVar == null) {
            return null;
        }
        return new h(this.e, aVar, this.g);
    }

    public final void a() {
        if (!d.b(this.e)) {
            f.c(h, "network error && sid = " + this.f);
        } else if (b() > 0) {
            f.c(h, "no need refresh");
        } else if (this.b) {
            f.c(h, "DL already refreshing && sid = " + this.f);
        } else {
            j.a(this.e).a(Integer.valueOf(this.f).intValue(), 1, this.j);
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
