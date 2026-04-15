package com.duapps.ad;

import com.duapps.ad.entity.strategy.NativeAd;

public interface DuAdDataCallBack {
    void onAdClick();

    void onAdError(AdError adError);

    void onAdLoaded(NativeAd nativeAd);
}
