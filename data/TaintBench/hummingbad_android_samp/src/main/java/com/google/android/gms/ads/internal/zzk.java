package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Window;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzdk;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhj;
import com.google.android.gms.internal.zzhj.zza;
import com.google.android.gms.internal.zzip;

@zzgk
public class zzk extends zzc implements zzdk {
    protected transient boolean zzpj = false;

    public zzk(Context context, AdSizeParcel adSizeParcel, String str, zzeh zzeh, VersionInfoParcel versionInfoParcel, zzd zzd) {
        super(context, adSizeParcel, str, zzeh, versionInfoParcel, zzd);
    }

    private void zzb(Bundle bundle) {
        zzp.zzbx().zzb(this.zzos.context, this.zzos.zzqb.zzIz, "gmob-apps", bundle, false);
    }

    private void zzbm() {
        if (this.zzos.zzbP()) {
            this.zzos.zzbM();
            this.zzos.zzqg = null;
            this.zzos.zzpk = false;
            this.zzpj = false;
        }
    }

    public void showInterstitial() {
        zzx.zzch("showInterstitial must be called on the main UI thread.");
        if (this.zzos.zzqg == null) {
            zzb.zzaE("The interstitial has not loaded.");
            return;
        }
        if (((Boolean) zzby.zzuZ.get()).booleanValue()) {
            Bundle bundle;
            String packageName = this.zzos.context.getApplicationContext() != null ? this.zzos.context.getApplicationContext().getPackageName() : this.zzos.context.getPackageName();
            if (!this.zzpj) {
                zzb.zzaE("It is not recommended to show an interstitial before onAdLoaded completes.");
                bundle = new Bundle();
                bundle.putString(CMNativeAd.KEY_APP_ID, packageName);
                bundle.putString("action", "show_interstitial_before_load_finish");
                zzb(bundle);
            }
            if (!zzp.zzbx().zzO(this.zzos.context)) {
                zzb.zzaE("It is not recommended to show an interstitial when app is not in foreground.");
                bundle = new Bundle();
                bundle.putString(CMNativeAd.KEY_APP_ID, packageName);
                bundle.putString("action", "show_interstitial_app_not_in_foreground");
                zzb(bundle);
            }
        }
        if (!this.zzos.zzbQ()) {
            if (this.zzos.zzqg.zzDX) {
                try {
                    this.zzos.zzqg.zzyR.showInterstitial();
                } catch (RemoteException e) {
                    zzb.zzd("Could not show interstitial.", e);
                    zzbm();
                }
            } else if (this.zzos.zzqg.zzAR == null) {
                zzb.zzaE("The interstitial failed to load.");
            } else if (this.zzos.zzqg.zzAR.zzgW()) {
                zzb.zzaE("The interstitial is already showing.");
            } else {
                this.zzos.zzqg.zzAR.zzC(true);
                if (this.zzos.zzqg.zzGF != null) {
                    this.zzou.zza(this.zzos.zzqf, this.zzos.zzqg);
                }
                InterstitialAdParameterParcel interstitialAdParameterParcel = new InterstitialAdParameterParcel(this.zzos.zzpk, zzbl());
                int requestedOrientation = this.zzos.zzqg.zzAR.getRequestedOrientation();
                if (requestedOrientation == -1) {
                    requestedOrientation = this.zzos.zzqg.orientation;
                }
                zzp.zzbv().zza(this.zzos.context, new AdOverlayInfoParcel(this, this, this, this.zzos.zzqg.zzAR, requestedOrientation, this.zzos.zzqb, this.zzos.zzqg.zzEc, interstitialAdParameterParcel));
            }
        }
    }

    /* access modifiers changed from: protected */
    public zzip zza(zza zza, zze zze) {
        zzip zza2 = zzp.zzby().zza(this.zzos.context, this.zzos.zzqf, false, false, this.zzos.zzqa, this.zzos.zzqb, this.zzov);
        zza2.zzgS().zzb(this, null, this, this, ((Boolean) zzby.zzuN.get()).booleanValue(), this, this, zze, null);
        zza2.zzaG(zza.zzGL.zzDQ);
        return zza2;
    }

    public boolean zza(AdRequestParcel adRequestParcel, zzcd zzcd) {
        if (this.zzos.zzqg == null) {
            return super.zza(adRequestParcel, zzcd);
        }
        zzb.zzaE("An interstitial is already loading. Aborting.");
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean zza(AdRequestParcel adRequestParcel, zzhj zzhj, boolean z) {
        if (this.zzos.zzbP() && zzhj.zzAR != null) {
            zzp.zzbz().zza(zzhj.zzAR.getWebView());
        }
        return this.zzor.zzbr();
    }

    public boolean zza(zzhj zzhj, zzhj zzhj2) {
        if (!super.zza(zzhj, zzhj2)) {
            return false;
        }
        if (!(this.zzos.zzbP() || this.zzos.zzqy == null || zzhj2.zzGF == null)) {
            this.zzou.zza(this.zzos.zzqf, zzhj2, this.zzos.zzqy);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean zzaT() {
        if (!super.zzaT()) {
            return false;
        }
        this.zzpj = true;
        return true;
    }

    public void zzaV() {
        zzbm();
        super.zzaV();
    }

    public void zzaW() {
        recordImpression();
        super.zzaW();
    }

    /* access modifiers changed from: protected */
    public boolean zzbl() {
        if (!(this.zzos.context instanceof Activity)) {
            return false;
        }
        Window window = ((Activity) this.zzos.context).getWindow();
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

    public void zzd(boolean z) {
        this.zzos.zzpk = z;
    }
}
