package com.duapps.ad.list;

import com.duapps.ad.AdError;
import com.duapps.ad.entity.strategy.NativeAd;
import java.util.List;

/* compiled from: DuNativeAdsManager */
final class a implements AdListArrivalListener {
    private /* synthetic */ DuNativeAdsManager a;

    a(DuNativeAdsManager duNativeAdsManager) {
        this.a = duNativeAdsManager;
    }

    public final void onAdLoaded(List<NativeAd> list) {
        if (this.a.c != null) {
            this.a.c.onAdLoaded(list);
        }
    }

    public final void onAdError(AdError adError) {
        if (this.a.c != null) {
            this.a.c.onAdError(adError);
        }
    }
}
