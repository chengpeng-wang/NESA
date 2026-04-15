package com.mopub.mraid;

import android.support.annotation.NonNull;
import com.mopub.common.DataKeys;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.mopub.mobileads.MraidActivity;
import com.mopub.mobileads.ResponseBodyInterstitial;
import java.util.Map;

class MraidInterstitial extends ResponseBodyInterstitial {
    private String mHtmlData;

    MraidInterstitial() {
    }

    /* access modifiers changed from: protected */
    public void extractExtras(Map<String, String> serverExtras) {
        this.mHtmlData = (String) serverExtras.get(DataKeys.HTML_RESPONSE_BODY_KEY);
    }

    /* access modifiers changed from: protected */
    public void preRenderHtml(@NonNull CustomEventInterstitialListener customEventInterstitialListener) {
        MraidActivity.preRenderHtml(this.mContext, customEventInterstitialListener, this.mHtmlData);
    }

    public void showInterstitial() {
        MraidActivity.start(this.mContext, this.mAdReport, this.mHtmlData, this.mBroadcastIdentifier);
    }
}
