package com.duapps.ad;

import com.duapps.ad.c.b.a;
import com.duapps.ad.entity.strategy.NativeAd;

/* compiled from: ADRequestController */
final class b implements DuAdDataCallBack {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public final void onAdLoaded(NativeAd nativeAd) {
        this.a.m = false;
        if (a.a()) {
            DuAdDataCallBack a = this.a.e;
            if (a != null) {
                a.onAdLoaded(nativeAd);
                return;
            }
            return;
        }
        a.a(new c(this, nativeAd));
    }

    public final void onAdError(AdError adError) {
        this.a.m = false;
        if (a.a()) {
            DuAdDataCallBack a = this.a.e;
            if (a != null) {
                a.onAdError(adError);
                return;
            }
            return;
        }
        a.a(new d(this, adError));
    }

    public final void onAdClick() {
        a.a(new e(this));
    }
}
