package com.duapps.ad.b;

import android.content.Context;
import android.text.TextUtils;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.j;
import com.duapps.ad.base.t;
import com.duapps.ad.base.x;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* compiled from: InMobiCacheManager */
public final class e extends a<NativeAd> {
    private int h;
    /* access modifiers changed from: private|final */
    public final List<a> i = Collections.synchronizedList(new LinkedList());
    private x<c> j = new f(this);

    static /* synthetic */ List a(e eVar, List list) {
        ArrayList arrayList = new ArrayList();
        for (a aVar : list) {
            if (a(eVar.e, aVar.h)) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    public e(Context context, int i, long j, int i2) {
        super(context, i, j);
        this.h = i2;
    }

    public final void a() {
        if (!d.b(this.e)) {
            return;
        }
        if (this.h == 0) {
            f.c("InMobiCacheManager", "cacheSize is zero");
            return;
        }
        String c = t.a(this.e).c();
        f.c("InMobiCacheManager", "ImCache inId = " + c);
        int b = this.h - b();
        if (b > 0 && !this.b) {
            this.b = true;
            this.c = true;
            j.a(this.e).a(Integer.valueOf(this.f).intValue(), c, String.valueOf(b), this.j);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: e */
    public NativeAd d() {
        a aVar;
        synchronized (this.i) {
            a aVar2 = null;
            while (this.i.size() > 0) {
                aVar2 = (a) this.i.remove(0);
                if (aVar2 != null && aVar2.a()) {
                    break;
                }
            }
            aVar = aVar2;
        }
        d.d(this.e, aVar == null ? "FAIL" : "OK", this.f);
        if (aVar == null) {
            return null;
        }
        return new m(this.e, aVar, this.g);
    }

    public final int b() {
        int i;
        synchronized (this.i) {
            Iterator it = this.i.iterator();
            i = 0;
            while (it.hasNext()) {
                a aVar = (a) it.next();
                if (aVar == null) {
                    it.remove();
                } else if (aVar.a() && a(this.e, aVar.h)) {
                    i++;
                } else {
                    it.remove();
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

    private static boolean a(Context context, String str) {
        if (!TextUtils.isEmpty(str) && d.a(context, str)) {
            return false;
        }
        return true;
    }
}
