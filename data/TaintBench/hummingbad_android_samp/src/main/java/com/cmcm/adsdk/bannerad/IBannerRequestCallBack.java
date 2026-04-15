package com.cmcm.adsdk.bannerad;

public interface IBannerRequestCallBack {
    void onAdClicked();

    void onAdLoadFailed();

    void onAdLoaded(c cVar);
}
