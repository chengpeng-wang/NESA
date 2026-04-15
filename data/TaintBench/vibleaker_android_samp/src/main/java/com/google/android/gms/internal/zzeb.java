package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzeb extends zza {
    private zzk zzAD;
    private zzdx zzAJ;
    private zzgh zzAK;
    private String zzAL;
    private zzdv zzAz;
    private String zzpS;

    public zzeb(Context context, String str, zzex zzex, VersionInfoParcel versionInfoParcel, zzd zzd) {
        this(str, new zzdv(context.getApplicationContext(), zzex, versionInfoParcel, zzd));
    }

    public zzeb(String str, zzdv zzdv) {
        this.zzpS = str;
        this.zzAz = zzdv;
        this.zzAJ = new zzdx();
        zzr.zzbN().zza(zzdv);
    }

    private void zzel() {
        if (this.zzAD != null && this.zzAK != null) {
            this.zzAD.zza(this.zzAK, this.zzAL);
        }
    }

    /* access modifiers changed from: 0000 */
    public void abort() {
        if (this.zzAD == null) {
            this.zzAD = this.zzAz.zzX(this.zzpS);
            this.zzAJ.zzc(this.zzAD);
            zzel();
        }
    }

    public void destroy() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.destroy();
        }
    }

    public String getMediationAdapterClassName() throws RemoteException {
        return this.zzAD != null ? this.zzAD.getMediationAdapterClassName() : null;
    }

    public boolean isLoading() throws RemoteException {
        return this.zzAD != null && this.zzAD.isLoading();
    }

    public boolean isReady() throws RemoteException {
        return this.zzAD != null && this.zzAD.isReady();
    }

    public void pause() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.pause();
        }
    }

    public void resume() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.resume();
        }
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) throws RemoteException {
        abort();
        if (this.zzAD != null) {
            this.zzAD.setManualImpressionsEnabled(manualImpressionsEnabled);
        }
    }

    public void setUserId(String useId) {
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.showInterstitial();
        } else {
            zzb.zzaK("Interstitial ad must be loaded before showInterstitial().");
        }
    }

    public void stopLoading() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.stopLoading();
        }
    }

    public void zza(AdSizeParcel adSizeParcel) throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.zza(adSizeParcel);
        }
    }

    public void zza(zzp zzp) throws RemoteException {
        this.zzAJ.zzAt = zzp;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzq zzq) throws RemoteException {
        this.zzAJ.zzpK = zzq;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzw zzw) throws RemoteException {
        this.zzAJ.zzAq = zzw;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzx zzx) throws RemoteException {
        abort();
        if (this.zzAD != null) {
            this.zzAD.zza(zzx);
        }
    }

    public void zza(com.google.android.gms.ads.internal.reward.client.zzd zzd) {
        this.zzAJ.zzAu = zzd;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzcf zzcf) throws RemoteException {
        this.zzAJ.zzAs = zzcf;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzgd zzgd) throws RemoteException {
        this.zzAJ.zzAr = zzgd;
        if (this.zzAD != null) {
            this.zzAJ.zzc(this.zzAD);
        }
    }

    public void zza(zzgh zzgh, String str) throws RemoteException {
        this.zzAK = zzgh;
        this.zzAL = str;
        zzel();
    }

    public com.google.android.gms.dynamic.zzd zzaM() throws RemoteException {
        return this.zzAD != null ? this.zzAD.zzaM() : null;
    }

    public AdSizeParcel zzaN() throws RemoteException {
        return this.zzAD != null ? this.zzAD.zzaN() : null;
    }

    public void zzaP() throws RemoteException {
        if (this.zzAD != null) {
            this.zzAD.zzaP();
        } else {
            zzb.zzaK("Interstitial ad must be loaded before pingManualTrackingUrl().");
        }
    }

    public boolean zzb(AdRequestParcel adRequestParcel) throws RemoteException {
        if (zzi(adRequestParcel)) {
            abort();
        }
        if (adRequestParcel.zztJ != null) {
            abort();
        }
        if (this.zzAD != null) {
            return this.zzAD.zzb(adRequestParcel);
        }
        zza zza = zzr.zzbN().zza(adRequestParcel, this.zzpS);
        if (zza != null) {
            if (!zza.zzAG) {
                zza.zzh(adRequestParcel);
            }
            this.zzAD = zza.zzAD;
            zza.zzc(this.zzAz);
            zza.zzAE.zza(this.zzAJ);
            this.zzAJ.zzc(this.zzAD);
            zzel();
            return zza.zzAH;
        }
        this.zzAD = this.zzAz.zzX(this.zzpS);
        this.zzAJ.zzc(this.zzAD);
        zzel();
        return this.zzAD.zzb(adRequestParcel);
    }

    /* access modifiers changed from: 0000 */
    public boolean zzi(AdRequestParcel adRequestParcel) {
        Bundle bundle = adRequestParcel.zztM;
        if (bundle == null) {
            return false;
        }
        bundle = bundle.getBundle(AdMobAdapter.class.getCanonicalName());
        if (bundle == null) {
            return false;
        }
        return bundle.keySet().contains("gw");
    }
}
