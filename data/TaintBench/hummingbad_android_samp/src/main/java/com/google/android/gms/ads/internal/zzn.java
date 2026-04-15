package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.formats.zzd;
import com.google.android.gms.ads.internal.formats.zze;
import com.google.android.gms.ads.internal.formats.zzf;
import com.google.android.gms.ads.internal.formats.zzg;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzcw;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzek;
import com.google.android.gms.internal.zzel;
import com.google.android.gms.internal.zzfm;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhj;
import com.google.android.gms.internal.zzhj.zza;
import com.google.android.gms.internal.zzhu;
import com.google.android.gms.internal.zzlh;
import java.util.List;

@zzgk
public class zzn extends zzb {
    public zzn(Context context, AdSizeParcel adSizeParcel, String str, zzeh zzeh, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, str, zzeh, versionInfoParcel, null);
    }

    private static zzd zza(zzek zzek) throws RemoteException {
        return new zzd(zzek.getHeadline(), zzek.getImages(), zzek.getBody(), zzek.zzds() != null ? zzek.zzds() : null, zzek.getCallToAction(), zzek.getStarRating(), zzek.getStore(), zzek.getPrice(), null, zzek.getExtras());
    }

    private static zze zza(zzel zzel) throws RemoteException {
        return new zze(zzel.getHeadline(), zzel.getImages(), zzel.getBody(), zzel.zzdw() != null ? zzel.zzdw() : null, zzel.getCallToAction(), zzel.getAdvertiser(), null, zzel.getExtras());
    }

    private void zza(final zzd zzd) {
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                try {
                    zzn.this.zzos.zzqp.zza(zzd);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call OnAppInstallAdLoadedListener.onAppInstallAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zze zze) {
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                try {
                    zzn.this.zzos.zzqq.zza(zze);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call OnContentAdLoadedListener.onContentAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zzhj zzhj, final String str) {
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                try {
                    ((zzcw) zzn.this.zzos.zzqs.get(str)).zza((zzf) zzhj.zzGK);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onCustomTemplateAdLoadedListener.onCustomTemplateAdLoaded().", e);
                }
            }
        });
    }

    public void pause() {
        throw new IllegalStateException("Native Ad DOES NOT support pause().");
    }

    public void recordImpression() {
        zza(this.zzos.zzqg, false);
    }

    public void resume() {
        throw new IllegalStateException("Native Ad DOES NOT support resume().");
    }

    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by NativeAdManager.");
    }

    public void zza(zzch zzch) {
        throw new IllegalStateException("CustomRendering is NOT supported by NativeAdManager.");
    }

    public void zza(zzfm zzfm) {
        throw new IllegalStateException("In App Purchase is NOT supported by NativeAdManager.");
    }

    public void zza(final zza zza, zzcd zzcd) {
        if (zza.zzqf != null) {
            this.zzos.zzqf = zza.zzqf;
        }
        if (zza.errorCode != -2) {
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzn.this.zzb(new zzhj(zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzos.zzqz = 0;
        this.zzos.zzqe = zzp.zzbw().zza(this.zzos.context, this, zza, this.zzos.zzqa, null, this.zzow, this, zzcd);
        zzb.zzaC("AdRenderer: " + this.zzos.zzqe.getClass().getName());
    }

    public void zza(zzlh<String, zzcw> zzlh) {
        zzx.zzch("setOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        this.zzos.zzqs = zzlh;
    }

    public void zza(List<String> list) {
        zzx.zzch("setNativeTemplates must be called on the main UI thread.");
        this.zzos.zzqv = list;
    }

    /* access modifiers changed from: protected */
    public boolean zza(AdRequestParcel adRequestParcel, zzhj zzhj, boolean z) {
        return this.zzor.zzbr();
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzhj zzhj, zzhj zzhj2) {
        zza(null);
        if (this.zzos.zzbP()) {
            if (zzhj2.zzDX) {
                try {
                    zzek zzdS = zzhj2.zzyR.zzdS();
                    zzel zzdT = zzhj2.zzyR.zzdT();
                    if (zzdS != null) {
                        zzd zza = zza(zzdS);
                        zza.zza(new zzg(this.zzos.context, this, this.zzos.zzqa, zzdS));
                        zza(zza);
                    } else if (zzdT != null) {
                        zze zza2 = zza(zzdT);
                        zza2.zza(new zzg(this.zzos.context, this, this.zzos.zzqa, zzdT));
                        zza(zza2);
                    } else {
                        zzb.zzaE("No matching mapper for retrieved native ad template.");
                        zze(0);
                        return false;
                    }
                } catch (RemoteException e) {
                    zzb.zzd("Failed to get native ad mapper", e);
                }
            } else {
                zzh.zza zza3 = zzhj2.zzGK;
                if ((zza3 instanceof zze) && this.zzos.zzqq != null) {
                    zza((zze) zzhj2.zzGK);
                } else if ((zza3 instanceof zzd) && this.zzos.zzqp != null) {
                    zza((zzd) zzhj2.zzGK);
                } else if (!(zza3 instanceof zzf) || this.zzos.zzqs == null || this.zzos.zzqs.get(((zzf) zza3).getCustomTemplateId()) == null) {
                    zzb.zzaE("No matching listener for retrieved native ad template.");
                    zze(0);
                    return false;
                } else {
                    zza(zzhj2, ((zzf) zza3).getCustomTemplateId());
                }
            }
            return super.zza(zzhj, zzhj2);
        }
        throw new IllegalStateException("Native ad DOES NOT have custom rendering mode.");
    }

    public void zzb(NativeAdOptionsParcel nativeAdOptionsParcel) {
        zzx.zzch("setNativeAdOptions must be called on the main UI thread.");
        this.zzos.zzqt = nativeAdOptionsParcel;
    }

    public void zzb(zzct zzct) {
        zzx.zzch("setOnAppInstallAdLoadedListener must be called on the main UI thread.");
        this.zzos.zzqp = zzct;
    }

    public void zzb(zzcu zzcu) {
        zzx.zzch("setOnContentAdLoadedListener must be called on the main UI thread.");
        this.zzos.zzqq = zzcu;
    }

    public void zzb(zzlh<String, zzcv> zzlh) {
        zzx.zzch("setOnCustomClickListener must be called on the main UI thread.");
        this.zzos.zzqr = zzlh;
    }

    public zzlh<String, zzcw> zzbq() {
        zzx.zzch("getOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        return this.zzos.zzqs;
    }

    public zzcv zzr(String str) {
        zzx.zzch("getOnCustomClickListener must be called on the main UI thread.");
        return (zzcv) this.zzos.zzqr.get(str);
    }
}
