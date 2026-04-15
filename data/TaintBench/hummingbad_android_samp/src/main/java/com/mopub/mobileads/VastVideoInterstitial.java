package com.mopub.mobileads;

import com.mopub.common.CacheService;
import com.mopub.common.DataKeys;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.mopub.mobileads.VastManager.VastManagerListener;
import com.mopub.mobileads.factories.VastManagerFactory;
import java.util.Map;

class VastVideoInterstitial extends ResponseBodyInterstitial implements VastManagerListener {
    private CustomEventInterstitialListener mCustomEventInterstitialListener;
    private VastManager mVastManager;
    private String mVastResponse;
    private VastVideoConfig mVastVideoConfig;

    VastVideoInterstitial() {
    }

    /* access modifiers changed from: protected */
    public void extractExtras(Map<String, String> serverExtras) {
        this.mVastResponse = (String) serverExtras.get(DataKeys.HTML_RESPONSE_BODY_KEY);
    }

    /* access modifiers changed from: protected */
    public void preRenderHtml(CustomEventInterstitialListener customEventInterstitialListener) {
        this.mCustomEventInterstitialListener = customEventInterstitialListener;
        if (CacheService.initializeDiskCache(this.mContext)) {
            this.mVastManager = VastManagerFactory.create(this.mContext);
            this.mVastManager.prepareVastVideoConfiguration(this.mVastResponse, this, this.mContext);
            return;
        }
        this.mCustomEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.VIDEO_CACHE_ERROR);
    }

    public void showInterstitial() {
        BaseVideoPlayerActivity.startVast(this.mContext, this.mVastVideoConfig, this.mBroadcastIdentifier);
    }

    public void onInvalidate() {
        if (this.mVastManager != null) {
            this.mVastManager.cancel();
        }
        super.onInvalidate();
    }

    public void onVastVideoConfigurationPrepared(VastVideoConfig vastVideoConfig) {
        if (vastVideoConfig == null) {
            this.mCustomEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.VIDEO_DOWNLOAD_ERROR);
            return;
        }
        this.mVastVideoConfig = vastVideoConfig;
        this.mCustomEventInterstitialListener.onInterstitialLoaded();
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public String getVastResponse() {
        return this.mVastResponse;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public void setVastManager(VastManager vastManager) {
        this.mVastManager = vastManager;
    }
}
