package com.duapps.ad.list;

import android.content.Context;
import com.duapps.ad.list.cache.DuNativeAdsCache;
import com.duapps.ad.list.cache.INativeListRequest;

public class DuNativeAdsManager {
    private static AdListArrivalListener g = new b();
    private Context a;
    private INativeListRequest b;
    /* access modifiers changed from: private */
    public AdListArrivalListener c;
    private DuNativeAdsCache d;
    private int e;
    private AdListArrivalListener f = new a(this);

    public DuNativeAdsManager(Context context, int i, int i2) {
        this.a = context;
        this.e = i;
        this.d = DuNativeAdsCache.getInstance(this.a.getApplicationContext());
        this.b = this.d.getCachePool(i, i2);
        this.b.setListener(this.f);
    }

    public void load() {
        this.b.loadList();
    }

    public void fill() {
        this.b.fillList();
    }

    public void clearCache() {
        this.d.destroy(this.e);
    }

    public void setListener(AdListArrivalListener adListArrivalListener) {
        this.c = adListArrivalListener;
    }

    public void destroy() {
        this.c = g;
    }
}
