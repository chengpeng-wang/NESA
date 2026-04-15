package com.flurry.android.ads;

public interface FlurryAdNativeListener {
    void onAppExit(FlurryAdNative flurryAdNative);

    void onClicked(FlurryAdNative flurryAdNative);

    void onCloseFullscreen(FlurryAdNative flurryAdNative);

    void onError(FlurryAdNative flurryAdNative, FlurryAdErrorType flurryAdErrorType, int i);

    void onFetched(FlurryAdNative flurryAdNative);

    void onImpressionLogged(FlurryAdNative flurryAdNative);

    void onShowFullscreen(FlurryAdNative flurryAdNative);
}
