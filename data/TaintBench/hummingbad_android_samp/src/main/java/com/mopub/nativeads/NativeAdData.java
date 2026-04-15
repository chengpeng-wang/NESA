package com.mopub.nativeads;

import android.support.annotation.NonNull;

class NativeAdData {
    @NonNull
    private final MoPubAdRenderer adRenderer;
    @NonNull
    private final NativeAd adResponse;
    @NonNull
    private final String adUnitId;

    NativeAdData(@NonNull String adUnitId, @NonNull MoPubAdRenderer adRenderer, @NonNull NativeAd adResponse) {
        this.adUnitId = adUnitId;
        this.adRenderer = adRenderer;
        this.adResponse = adResponse;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public String getAdUnitId() {
        return this.adUnitId;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public MoPubAdRenderer getAdRenderer() {
        return this.adRenderer;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public NativeAd getAd() {
        return this.adResponse;
    }
}
