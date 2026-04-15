package com.cmcm.adsdk.bannerad;

public interface CMBannerAdListener {
    void adFailedToLoad();

    void onAdClicked(CMAdView cMAdView);

    void onAdLoaded(CMAdView cMAdView);
}
