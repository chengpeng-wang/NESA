package com.duapps.ad.list.a;

import android.content.Context;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.j;
import com.duapps.ad.base.x;
import com.duapps.ad.entity.c;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.b;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: DownloadAdsManager */
public final class a extends b<List<NativeAd>> {
    /* access modifiers changed from: private */
    public List<NativeAd> h;
    /* access modifiers changed from: private */
    public int i;
    /* access modifiers changed from: private */
    public int j;
    private x<c> k;

    static /* synthetic */ List a(a aVar, List list) {
        ArrayList arrayList = new ArrayList();
        for (com.duapps.ad.entity.a aVar2 : list) {
            if (!d.a(aVar.e, aVar2.c) && aVar2.a()) {
                arrayList.add(aVar2);
            }
        }
        return arrayList;
    }

    private a(Context context, int i, long j, int i2, DuAdDataCallBack duAdDataCallBack) {
        super(context, i, j);
        this.h = Collections.synchronizedList(new ArrayList());
        this.k = new b(this);
        if (i2 > 10 || i2 <= 0) {
            this.j = 10;
        } else {
            this.j = i2;
        }
        this.g = null;
    }

    public a(Context context, int i, long j, int i2) {
        this(context, i, j, i2, null);
    }

    public final void a() {
        if (!d.b(this.e)) {
            f.c("DownloadAdsManager", "network error && sid = " + this.f);
        } else if (!this.b) {
            if (b() > 0) {
                f.c("DownloadAdsManager", "Do not need to load");
                return;
            }
            this.a = false;
            this.i = 0;
            j.a(this.e).a(Integer.valueOf(this.f).intValue(), 1, this.k, 10);
            this.b = true;
            this.c = true;
        }
    }

    public final int b() {
        int i = 0;
        synchronized (this.h) {
            if (this.h != null && this.h.size() > 0) {
                Iterator it = this.h.iterator();
                while (it.hasNext()) {
                    NativeAd nativeAd = (NativeAd) it.next();
                    if (nativeAd == null) {
                        it.remove();
                    } else {
                        int i2;
                        if (nativeAd.isValid()) {
                            i2 = i + 1;
                        } else {
                            i2 = i;
                        }
                        i = i2;
                    }
                }
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    /* renamed from: e */
    public List<NativeAd> d() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.h) {
            Iterator it = this.h.iterator();
            while (it.hasNext()) {
                NativeAd nativeAd = (NativeAd) it.next();
                if (nativeAd == null) {
                    it.remove();
                } else if (nativeAd.isValid()) {
                    arrayList.add(nativeAd);
                }
            }
            this.h.clear();
        }
        return arrayList;
    }

    public final void c() {
        this.i = 0;
        synchronized (this.h) {
            this.h.clear();
        }
    }
}
