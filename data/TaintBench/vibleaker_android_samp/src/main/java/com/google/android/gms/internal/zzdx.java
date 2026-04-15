package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;

@zzhb
class zzdx {
    zzw zzAq;
    zzgd zzAr;
    zzcf zzAs;
    zzp zzAt;
    zzd zzAu;
    zzq zzpK;

    private class zza extends com.google.android.gms.ads.internal.client.zzq.zza {
        zzq zzAv;

        zza(zzq zzq) {
            this.zzAv = zzq;
        }

        public void onAdClosed() throws RemoteException {
            this.zzAv.onAdClosed();
            zzr.zzbN().zzee();
        }

        public void onAdFailedToLoad(int errorCode) throws RemoteException {
            this.zzAv.onAdFailedToLoad(errorCode);
        }

        public void onAdLeftApplication() throws RemoteException {
            this.zzAv.onAdLeftApplication();
        }

        public void onAdLoaded() throws RemoteException {
            this.zzAv.onAdLoaded();
        }

        public void onAdOpened() throws RemoteException {
            this.zzAv.onAdOpened();
        }
    }

    zzdx() {
    }

    /* access modifiers changed from: 0000 */
    public void zzc(zzk zzk) {
        if (this.zzpK != null) {
            zzk.zza((zzq) new zza(this.zzpK));
        }
        if (this.zzAq != null) {
            zzk.zza(this.zzAq);
        }
        if (this.zzAr != null) {
            zzk.zza(this.zzAr);
        }
        if (this.zzAs != null) {
            zzk.zza(this.zzAs);
        }
        if (this.zzAt != null) {
            zzk.zza(this.zzAt);
        }
        if (this.zzAu != null) {
            zzk.zza(this.zzAu);
        }
    }
}
