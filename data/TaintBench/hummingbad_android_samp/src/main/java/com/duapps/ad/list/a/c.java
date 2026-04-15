package com.duapps.ad.list.a;

import android.content.Context;
import android.os.SystemClock;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.t;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.b;
import com.facebook.ads.NativeAdsManager;
import com.facebook.ads.NativeAdsManager.Listener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: FacebookAdsMananger */
public final class c extends b<List<NativeAd>> {
    NativeAdsManager h;
    /* access modifiers changed from: private */
    public int i;
    /* access modifiers changed from: private */
    public List<NativeAd> j;
    private String k;
    /* access modifiers changed from: private */
    public long l;
    /* access modifiers changed from: private */
    public int m;
    private Listener n;

    private c(Context context, int i, long j, int i2, DuAdDataCallBack duAdDataCallBack) {
        super(context, i, j);
        this.j = Collections.synchronizedList(new ArrayList());
        this.n = new d(this);
        this.g = null;
        if (i2 > 10 || i2 <= 0) {
            this.i = 10;
        } else {
            this.i = i2;
        }
        this.k = t.a(this.e).b(i);
    }

    public c(Context context, int i, long j, int i2) {
        this(context, i, j, i2, null);
    }

    public final void a() {
        if (d.b(this.e)) {
            if (this.h == null) {
                this.h = new NativeAdsManager(this.e, this.k, this.i);
                this.h.setListener(this.n);
            }
            if (!this.b) {
                if (b() > 0) {
                    f.c("FacebookAdsMananger", "Do not need to load");
                    return;
                }
                this.m++;
                f.c("FacebookAdsMananger", "refresh in FB :" + this.m);
                this.a = false;
                this.l = SystemClock.elapsedRealtime();
                this.h.loadAds();
                this.b = true;
                this.c = true;
                return;
            }
            return;
        }
        f.c("FacebookAdsMananger", "network error && sid = " + this.f);
    }

    public final int b() {
        int i = 0;
        synchronized (this.j) {
            if (this.j != null && this.j.size() > 0) {
                Iterator it = this.j.iterator();
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
        synchronized (this.j) {
            Iterator it = this.j.iterator();
            while (it.hasNext()) {
                NativeAd nativeAd = (NativeAd) it.next();
                if (nativeAd == null) {
                    it.remove();
                } else if (nativeAd.isValid()) {
                    arrayList.add(nativeAd);
                }
            }
            this.j.clear();
        }
        return arrayList;
    }

    public final void c() {
        synchronized (this.j) {
            this.j.clear();
        }
    }
}
