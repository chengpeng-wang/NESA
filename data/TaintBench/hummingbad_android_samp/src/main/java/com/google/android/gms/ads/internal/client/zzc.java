package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.internal.client.zzn.zza;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class zzc extends zza {
    private final AdListener zzso;

    public zzc(AdListener adListener) {
        this.zzso = adListener;
    }

    public void onAdClosed() {
        this.zzso.onAdClosed();
    }

    public void onAdFailedToLoad(int errorCode) {
        this.zzso.onAdFailedToLoad(errorCode);
    }

    public void onAdLeftApplication() {
        this.zzso.onAdLeftApplication();
    }

    public void onAdLoaded() {
        this.zzso.onAdLoaded();
    }

    public void onAdOpened() {
        this.zzso.onAdOpened();
    }
}
