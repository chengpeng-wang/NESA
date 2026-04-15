package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Window;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzdj;
import com.google.android.gms.internal.zzdn;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjp;

@zzhb
public class zzk extends zzc implements zzdj, com.google.android.gms.internal.zzdn.zza {
    protected transient boolean zzqc = false;
    /* access modifiers changed from: private */
    public boolean zzqd;
    /* access modifiers changed from: private */
    public float zzqe;
    private String zzqf = ("background" + hashCode() + "." + "png");

    @zzhb
    private class zza extends zzim {
        private final String zzqg;

        public zza(String str) {
            this.zzqg = str;
        }

        public void onStop() {
        }

        public void zzbr() {
            zzr.zzbC().zzg(zzk.this.zzpj.context, this.zzqg);
        }
    }

    @zzhb
    private class zzb extends zzim {
        private final String zzqg;
        private final Bitmap zzqi;

        public zzb(Bitmap bitmap, String str) {
            this.zzqi = bitmap;
            this.zzqg = str;
        }

        public void onStop() {
        }

        public void zzbr() {
            InterstitialAdParameterParcel interstitialAdParameterParcel = new InterstitialAdParameterParcel(zzk.this.zzpj.zzql, zzk.this.zzbo(), zzk.this.zzpj.zzql ? zzr.zzbC().zza(zzk.this.zzpj.context, this.zzqi, this.zzqg) : false ? this.zzqg : null, zzk.this.zzqd, zzk.this.zzqe);
            int requestedOrientation = zzk.this.zzpj.zzrq.zzED.getRequestedOrientation();
            if (requestedOrientation == -1) {
                requestedOrientation = zzk.this.zzpj.zzrq.orientation;
            }
            final AdOverlayInfoParcel adOverlayInfoParcel = new AdOverlayInfoParcel(zzk.this, zzk.this, zzk.this, zzk.this.zzpj.zzrq.zzED, requestedOrientation, zzk.this.zzpj.zzrl, zzk.this.zzpj.zzrq.zzHY, interstitialAdParameterParcel);
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzr.zzbA().zza(zzk.this.zzpj.context, adOverlayInfoParcel);
                }
            });
        }
    }

    public zzk(Context context, AdSizeParcel adSizeParcel, String str, zzex zzex, VersionInfoParcel versionInfoParcel, zzd zzd) {
        super(context, adSizeParcel, str, zzex, versionInfoParcel, zzd);
    }

    private void zzb(Bundle bundle) {
        zzr.zzbC().zzb(this.zzpj.context, this.zzpj.zzrl.afmaVersion, "gmob-apps", bundle, false);
    }

    public void showInterstitial() {
        zzx.zzcD("showInterstitial must be called on the main UI thread.");
        if (this.zzpj.zzrq == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("The interstitial has not loaded.");
            return;
        }
        if (((Boolean) zzbt.zzwN.get()).booleanValue()) {
            Bundle bundle;
            String packageName = this.zzpj.context.getApplicationContext() != null ? this.zzpj.context.getApplicationContext().getPackageName() : this.zzpj.context.getPackageName();
            if (!this.zzqc) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("It is not recommended to show an interstitial before onAdLoaded completes.");
                bundle = new Bundle();
                bundle.putString("appid", packageName);
                bundle.putString("action", "show_interstitial_before_load_finish");
                zzb(bundle);
            }
            if (!zzr.zzbC().zzO(this.zzpj.context)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("It is not recommended to show an interstitial when app is not in foreground.");
                bundle = new Bundle();
                bundle.putString("appid", packageName);
                bundle.putString("action", "show_interstitial_app_not_in_foreground");
                zzb(bundle);
            }
        }
        if (!this.zzpj.zzbX()) {
            if (this.zzpj.zzrq.zzHT) {
                try {
                    this.zzpj.zzrq.zzCq.showInterstitial();
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not show interstitial.", e);
                    zzbp();
                }
            } else if (this.zzpj.zzrq.zzED == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("The interstitial failed to load.");
            } else if (this.zzpj.zzrq.zzED.zzhY()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("The interstitial is already showing.");
            } else {
                this.zzpj.zzrq.zzED.zzD(true);
                if (this.zzpj.zzrq.zzKT != null) {
                    this.zzpl.zza(this.zzpj.zzrp, this.zzpj.zzrq);
                }
                Bitmap zzP = this.zzpj.zzql ? zzr.zzbC().zzP(this.zzpj.context) : null;
                if (!((Boolean) zzbt.zzxe.get()).booleanValue() || zzP == null) {
                    InterstitialAdParameterParcel interstitialAdParameterParcel = new InterstitialAdParameterParcel(this.zzpj.zzql, zzbo(), null, false, 0.0f);
                    int requestedOrientation = this.zzpj.zzrq.zzED.getRequestedOrientation();
                    if (requestedOrientation == -1) {
                        requestedOrientation = this.zzpj.zzrq.orientation;
                    }
                    zzr.zzbA().zza(this.zzpj.context, new AdOverlayInfoParcel(this, this, this, this.zzpj.zzrq.zzED, requestedOrientation, this.zzpj.zzrl, this.zzpj.zzrq.zzHY, interstitialAdParameterParcel));
                    return;
                }
                new zzb(zzP, this.zzqf).zzgd();
            }
        }
    }

    /* access modifiers changed from: protected */
    public zzjp zza(com.google.android.gms.internal.zzif.zza zza, zze zze) {
        zzjp zza2 = zzr.zzbD().zza(this.zzpj.context, this.zzpj.zzrp, false, false, this.zzpj.zzrk, this.zzpj.zzrl, this.zzpe, this.zzpm);
        zza2.zzhU().zzb(this, null, this, this, ((Boolean) zzbt.zzwv.get()).booleanValue(), this, this, zze, null);
        zza((zzeh) zza2);
        zza2.zzaM(zza.zzLd.zzHI);
        zzdn.zza(zza2, (com.google.android.gms.internal.zzdn.zza) this);
        return zza2;
    }

    public void zza(boolean z, float f) {
        this.zzqd = z;
        this.zzqe = f;
    }

    public boolean zza(AdRequestParcel adRequestParcel, zzcb zzcb) {
        if (this.zzpj.zzrq == null) {
            return super.zza(adRequestParcel, zzcb);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK("An interstitial is already loading. Aborting.");
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean zza(AdRequestParcel adRequestParcel, zzif zzif, boolean z) {
        if (this.zzpj.zzbW() && zzif.zzED != null) {
            zzr.zzbE().zzi(zzif.zzED);
        }
        return this.zzpi.zzbw();
    }

    public boolean zza(zzif zzif, zzif zzif2) {
        if (!super.zza(zzif, zzif2)) {
            return false;
        }
        if (!(this.zzpj.zzbW() || this.zzpj.zzrK == null || zzif2.zzKT == null)) {
            this.zzpl.zza(this.zzpj.zzrp, zzif2, this.zzpj.zzrK);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void zzaQ() {
        zzbp();
        super.zzaQ();
    }

    /* access modifiers changed from: protected */
    public void zzaT() {
        super.zzaT();
        this.zzqc = true;
    }

    public void zzaX() {
        recordImpression();
        super.zzaX();
    }

    public void zzb(RewardItemParcel rewardItemParcel) {
        if (this.zzpj.zzrq != null) {
            if (this.zzpj.zzrq.zzIl != null) {
                zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq.zzIl);
            }
            if (this.zzpj.zzrq.zzIj != null) {
                rewardItemParcel = this.zzpj.zzrq.zzIj;
            }
        }
        zza(rewardItemParcel);
    }

    /* access modifiers changed from: protected */
    public boolean zzbo() {
        if (!(this.zzpj.context instanceof Activity)) {
            return false;
        }
        Window window = ((Activity) this.zzpj.context).getWindow();
        if (window == null || window.getDecorView() == null) {
            return false;
        }
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        window.getDecorView().getGlobalVisibleRect(rect, null);
        window.getDecorView().getWindowVisibleDisplayFrame(rect2);
        boolean z = (rect.bottom == 0 || rect2.bottom == 0 || rect.top != rect2.top) ? false : true;
        return z;
    }

    public void zzbp() {
        new zza(this.zzqf).zzgd();
        if (this.zzpj.zzbW()) {
            this.zzpj.zzbT();
            this.zzpj.zzrq = null;
            this.zzpj.zzql = false;
            this.zzqc = false;
        }
    }

    public void zzbq() {
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzKX == null)) {
            zzr.zzbC().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq.zzKX);
        }
        zzaU();
    }

    public void zzd(boolean z) {
        this.zzpj.zzql = z;
    }
}
