package com.cmcm.adsdk.splashad;

public interface SplashAdListener {
    void onAdClicked(String str);

    void onAdDismissed();

    void onAdFailed(String str);

    void onAdPresent(String str);

    void onAdSkiped();
}
