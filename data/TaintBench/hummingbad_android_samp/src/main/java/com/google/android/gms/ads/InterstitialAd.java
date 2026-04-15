package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzz;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;

public final class InterstitialAd {
    private final zzz zznT;

    public InterstitialAd(Context context) {
        this.zznT = new zzz(context);
    }

    public AdListener getAdListener() {
        return this.zznT.getAdListener();
    }

    public String getAdUnitId() {
        return this.zznT.getAdUnitId();
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zznT.getInAppPurchaseListener();
    }

    public String getMediationAdapterClassName() {
        return this.zznT.getMediationAdapterClassName();
    }

    public boolean isLoaded() {
        return this.zznT.isLoaded();
    }

    public boolean isLoading() {
        return this.zznT.isLoading();
    }

    public void loadAd(AdRequest adRequest) {
        this.zznT.zza(adRequest.zzaF());
    }

    public void setAdListener(AdListener adListener) {
        this.zznT.setAdListener(adListener);
        if (adListener != null && (adListener instanceof zza)) {
            this.zznT.zza((zza) adListener);
        } else if (adListener == null) {
            this.zznT.zza(null);
        }
    }

    public void setAdUnitId(String adUnitId) {
        this.zznT.setAdUnitId(adUnitId);
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        this.zznT.setInAppPurchaseListener(inAppPurchaseListener);
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        this.zznT.setPlayStorePurchaseParams(playStorePurchaseListener, publicKey);
    }

    public void show() {
        this.zznT.show();
    }
}
