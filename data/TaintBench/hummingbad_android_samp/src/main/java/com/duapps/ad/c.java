package com.duapps.ad;

import com.duapps.ad.entity.strategy.NativeAd;

/* compiled from: ADRequestController */
final class c implements Runnable {
    private /* synthetic */ NativeAd a;
    private /* synthetic */ b b;

    c(b bVar, NativeAd nativeAd) {
        this.b = bVar;
        this.a = nativeAd;
    }

    public final void run() {
        DuAdDataCallBack a = this.b.a.e;
        if (a != null) {
            a.onAdLoaded(this.a);
        }
    }
}
