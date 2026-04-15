package com.google.android.gms.ads.mediation.customevent;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;

public final class CustomEventAdapter implements MediationBannerAdapter, MediationInterstitialAdapter, com.google.android.gms.ads.mediation.zza {
    CustomEventBanner zzJG;
    CustomEventInterstitial zzJH;
    CustomEventNative zzJI;
    private View zzaY;

    static final class zza implements CustomEventBannerListener {
        private final CustomEventAdapter zzJJ;
        private final MediationBannerListener zzaQ;

        public zza(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.zzJJ = customEventAdapter;
            this.zzaQ = mediationBannerListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClicked.");
            this.zzaQ.onAdClicked(this.zzJJ);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClosed.");
            this.zzaQ.onAdClosed(this.zzJJ);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdFailedToLoad.");
            this.zzaQ.onAdFailedToLoad(this.zzJJ, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdLeftApplication.");
            this.zzaQ.onAdLeftApplication(this.zzJJ);
        }

        public void onAdLoaded(View view) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdLoaded.");
            this.zzJJ.zza(view);
            this.zzaQ.onAdLoaded(this.zzJJ);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdOpened.");
            this.zzaQ.onAdOpened(this.zzJJ);
        }
    }

    class zzb implements CustomEventInterstitialListener {
        private final CustomEventAdapter zzJJ;
        private final MediationInterstitialListener zzaR;

        public zzb(CustomEventAdapter customEventAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.zzJJ = customEventAdapter;
            this.zzaR = mediationInterstitialListener;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClicked.");
            this.zzaR.onAdClicked(this.zzJJ);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClosed.");
            this.zzaR.onAdClosed(this.zzJJ);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onFailedToReceiveAd.");
            this.zzaR.onAdFailedToLoad(this.zzJJ, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdLeftApplication.");
            this.zzaR.onAdLeftApplication(this.zzJJ);
        }

        public void onAdLoaded() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onReceivedAd.");
            this.zzaR.onAdLoaded(CustomEventAdapter.this);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdOpened.");
            this.zzaR.onAdOpened(this.zzJJ);
        }
    }

    static class zzc implements CustomEventNativeListener {
        private final CustomEventAdapter zzJJ;
        private final com.google.android.gms.ads.mediation.zzb zzaS;

        public zzc(CustomEventAdapter customEventAdapter, com.google.android.gms.ads.mediation.zzb zzb) {
            this.zzJJ = customEventAdapter;
            this.zzaS = zzb;
        }

        public void onAdClicked() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClicked.");
            this.zzaS.zzd(this.zzJJ);
        }

        public void onAdClosed() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdClosed.");
            this.zzaS.zzb(this.zzJJ);
        }

        public void onAdFailedToLoad(int errorCode) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdFailedToLoad.");
            this.zzaS.zza(this.zzJJ, errorCode);
        }

        public void onAdLeftApplication() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdLeftApplication.");
            this.zzaS.zzc(this.zzJJ);
        }

        public void onAdLoaded(NativeAdMapper ad) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdLoaded.");
            this.zzaS.zza(this.zzJJ, ad);
        }

        public void onAdOpened() {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Custom event adapter called onAdOpened.");
            this.zzaS.zza(this.zzJJ);
        }
    }

    /* access modifiers changed from: private */
    public void zza(View view) {
        this.zzaY = view;
    }

    private static <T> T zzj(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Could not instantiate custom event adapter: " + str + ". " + th.getMessage());
            return null;
        }
    }

    public View getBannerView() {
        return this.zzaY;
    }

    public void onDestroy() {
        if (this.zzJG != null) {
            this.zzJG.onDestroy();
        }
        if (this.zzJH != null) {
            this.zzJH.onDestroy();
        }
        if (this.zzJI != null) {
            this.zzJI.onDestroy();
        }
    }

    public void onPause() {
        if (this.zzJG != null) {
            this.zzJG.onPause();
        }
        if (this.zzJH != null) {
            this.zzJH.onPause();
        }
        if (this.zzJI != null) {
            this.zzJI.onPause();
        }
    }

    public void onResume() {
        if (this.zzJG != null) {
            this.zzJG.onResume();
        }
        if (this.zzJH != null) {
            this.zzJH.onResume();
        }
        if (this.zzJI != null) {
            this.zzJI.onResume();
        }
    }

    public void requestBannerAd(Context context, MediationBannerListener listener, Bundle serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzJG = (CustomEventBanner) zzj(serverParameters.getString("class_name"));
        if (this.zzJG == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzJG.requestBannerAd(context, new zza(this, listener), serverParameters.getString("parameter"), adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestInterstitialAd(Context context, MediationInterstitialListener listener, Bundle serverParameters, MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzJH = (CustomEventInterstitial) zzj(serverParameters.getString("class_name"));
        if (this.zzJH == null) {
            listener.onAdFailedToLoad(this, 0);
            return;
        }
        this.zzJH.requestInterstitialAd(context, zza(listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void requestNativeAd(Context context, com.google.android.gms.ads.mediation.zzb listener, Bundle serverParameters, NativeMediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.zzJI = (CustomEventNative) zzj(serverParameters.getString("class_name"));
        if (this.zzJI == null) {
            listener.zza((com.google.android.gms.ads.mediation.zza) this, 0);
            return;
        }
        this.zzJI.requestNativeAd(context, new zzc(this, listener), serverParameters.getString("parameter"), mediationAdRequest, customEventExtras == null ? null : customEventExtras.getBundle(serverParameters.getString("class_name")));
    }

    public void showInterstitial() {
        this.zzJH.showInterstitial();
    }

    /* access modifiers changed from: 0000 */
    public zzb zza(MediationInterstitialListener mediationInterstitialListener) {
        return new zzb(this, mediationInterstitialListener);
    }
}
