package com.google.android.gms.ads.internal.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzgh;

public class zzah extends zza {
    /* access modifiers changed from: private */
    public zzq zzpK;

    public void destroy() {
    }

    public String getMediationAdapterClassName() {
        return null;
    }

    public boolean isLoading() {
        return false;
    }

    public boolean isReady() {
        return false;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void setManualImpressionsEnabled(boolean enabled) {
    }

    public void setUserId(String userId) {
    }

    public void showInterstitial() {
    }

    public void stopLoading() {
    }

    public void zza(AdSizeParcel adSizeParcel) {
    }

    public void zza(zzp zzp) {
    }

    public void zza(zzq zzq) {
        this.zzpK = zzq;
    }

    public void zza(zzw zzw) {
    }

    public void zza(zzx zzx) {
    }

    public void zza(zzd zzd) {
    }

    public void zza(zzcf zzcf) {
    }

    public void zza(zzgd zzgd) {
    }

    public void zza(zzgh zzgh, String str) {
    }

    public com.google.android.gms.dynamic.zzd zzaM() {
        return null;
    }

    public AdSizeParcel zzaN() {
        return null;
    }

    public void zzaP() {
    }

    public boolean zzb(AdRequestParcel adRequestParcel) {
        zzb.e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
            public void run() {
                if (zzah.this.zzpK != null) {
                    try {
                        zzah.this.zzpK.onAdFailedToLoad(1);
                    } catch (RemoteException e) {
                        zzb.zzd("Could not notify onAdFailedToLoad event.", e);
                    }
                }
            }
        });
        return false;
    }
}
