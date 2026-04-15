package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.internal.client.zzz;

public final class PublisherInterstitialAd {
    private final zzz zznT;

    public PublisherInterstitialAd(Context context) {
        this.zznT = new zzz(context, this);
    }

    public AdListener getAdListener() {
        return this.zznT.getAdListener();
    }

    public String getAdUnitId() {
        return this.zznT.getAdUnitId();
    }

    public AppEventListener getAppEventListener() {
        return this.zznT.getAppEventListener();
    }

    public String getMediationAdapterClassName() {
        return this.zznT.getMediationAdapterClassName();
    }

    public OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zznT.getOnCustomRenderedAdLoadedListener();
    }

    public boolean isLoaded() {
        return this.zznT.isLoaded();
    }

    public void loadAd(PublisherAdRequest publisherAdRequest) {
        this.zznT.zza(publisherAdRequest.zzaF());
    }

    public void setAdListener(AdListener adListener) {
        this.zznT.setAdListener(adListener);
    }

    public void setAdUnitId(String adUnitId) {
        this.zznT.setAdUnitId(adUnitId);
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        this.zznT.setAppEventListener(appEventListener);
    }

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zznT.setOnCustomRenderedAdLoadedListener(onCustomRenderedAdLoadedListener);
    }

    public void show() {
        this.zznT.show();
    }
}
