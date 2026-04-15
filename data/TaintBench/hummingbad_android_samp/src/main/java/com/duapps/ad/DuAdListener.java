package com.duapps.ad;

public interface DuAdListener {
    void onAdLoaded(DuNativeAd duNativeAd);

    void onClick(DuNativeAd duNativeAd);

    void onError(DuNativeAd duNativeAd, AdError adError);
}
