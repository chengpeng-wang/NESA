package com.duapps.ad;

import com.duapps.ad.entity.strategy.NativeAd;

/* compiled from: DuNativeAd */
final class f implements DuAdDataCallBack {
    private /* synthetic */ DuNativeAd a;

    f(DuNativeAd duNativeAd) {
        this.a = duNativeAd;
    }

    public final void onAdLoaded(NativeAd nativeAd) {
        this.a.b = nativeAd;
        DuAdListener a = this.a.c;
        if (a != null) {
            a.onAdLoaded(this.a);
        }
        if (this.a.g != null) {
            this.a.b.setProcessClickUrlCallback(this.a.g);
        }
    }

    public final void onAdError(AdError adError) {
        DuAdListener a = this.a.c;
        if (a != null) {
            a.onError(this.a, adError);
        }
    }

    public final void onAdClick() {
        DuAdListener a = this.a.c;
        if (a != null) {
            a.onClick(this.a);
        }
    }
}
