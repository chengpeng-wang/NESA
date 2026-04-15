package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.support.v4.util.SimpleArrayMap;
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
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzfb;
import com.google.android.gms.internal.zzfc;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzif.zza;
import com.google.android.gms.internal.zzir;
import java.util.List;

@zzhb
public class zzp extends zzb {
    public zzp(Context context, zzd zzd, AdSizeParcel adSizeParcel, String str, zzex zzex, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, str, zzex, versionInfoParcel, zzd);
    }

    private static zzd zza(zzfb zzfb) throws RemoteException {
        return new zzd(zzfb.getHeadline(), zzfb.getImages(), zzfb.getBody(), zzfb.zzdK() != null ? zzfb.zzdK() : null, zzfb.getCallToAction(), zzfb.getStarRating(), zzfb.getStore(), zzfb.getPrice(), null, zzfb.getExtras());
    }

    private static zze zza(zzfc zzfc) throws RemoteException {
        return new zze(zzfc.getHeadline(), zzfc.getImages(), zzfc.getBody(), zzfc.zzdO() != null ? zzfc.zzdO() : null, zzfc.getCallToAction(), zzfc.getAdvertiser(), null, zzfc.getExtras());
    }

    private void zza(final zzd zzd) {
        zzir.zzMc.post(new Runnable() {
            public void run() {
                try {
                    zzp.this.zzpj.zzrz.zza(zzd);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call OnAppInstallAdLoadedListener.onAppInstallAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zze zze) {
        zzir.zzMc.post(new Runnable() {
            public void run() {
                try {
                    zzp.this.zzpj.zzrA.zza(zze);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call OnContentAdLoadedListener.onContentAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zzif zzif, final String str) {
        zzir.zzMc.post(new Runnable() {
            public void run() {
                try {
                    ((zzcu) zzp.this.zzpj.zzrC.get(str)).zza((zzf) zzif.zzLa);
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onCustomTemplateAdLoadedListener.onCustomTemplateAdLoaded().", e);
                }
            }
        });
    }

    public void pause() {
        throw new IllegalStateException("Native Ad DOES NOT support pause().");
    }

    public void resume() {
        throw new IllegalStateException("Native Ad DOES NOT support resume().");
    }

    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by NativeAdManager.");
    }

    public void zza(SimpleArrayMap<String, zzcu> simpleArrayMap) {
        zzx.zzcD("setOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        this.zzpj.zzrC = simpleArrayMap;
    }

    public void zza(zzh zzh) {
        if (this.zzpj.zzrq.zzKT != null) {
            zzr.zzbF().zzhh().zza(this.zzpj.zzrp, this.zzpj.zzrq, zzh);
        }
    }

    public void zza(zzcf zzcf) {
        throw new IllegalStateException("CustomRendering is NOT supported by NativeAdManager.");
    }

    public void zza(zzgd zzgd) {
        throw new IllegalStateException("In App Purchase is NOT supported by NativeAdManager.");
    }

    public void zza(final zza zza, zzcb zzcb) {
        if (zza.zzrp != null) {
            this.zzpj.zzrp = zza.zzrp;
        }
        if (zza.errorCode != -2) {
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzp.this.zzb(new zzif(zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzpj.zzrL = 0;
        this.zzpj.zzro = zzr.zzbB().zza(this.zzpj.context, this, zza, this.zzpj.zzrk, null, this.zzpn, this, zzcb);
        zzb.zzaI("AdRenderer: " + this.zzpj.zzro.getClass().getName());
    }

    public void zza(List<String> list) {
        zzx.zzcD("setNativeTemplates must be called on the main UI thread.");
        this.zzpj.zzrH = list;
    }

    /* access modifiers changed from: protected */
    public boolean zza(AdRequestParcel adRequestParcel, zzif zzif, boolean z) {
        return this.zzpi.zzbw();
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzif zzif, zzif zzif2) {
        zza(null);
        if (this.zzpj.zzbW()) {
            if (zzif2.zzHT) {
                try {
                    zzfb zzeF = zzif2.zzCq.zzeF();
                    zzfc zzeG = zzif2.zzCq.zzeG();
                    if (zzeF != null) {
                        zzd zza = zza(zzeF);
                        zza.zzb(new zzg(this.zzpj.context, this, this.zzpj.zzrk, zzeF));
                        zza(zza);
                    } else if (zzeG != null) {
                        zze zza2 = zza(zzeG);
                        zza2.zzb(new zzg(this.zzpj.context, this, this.zzpj.zzrk, zzeG));
                        zza(zza2);
                    } else {
                        zzb.zzaK("No matching mapper for retrieved native ad template.");
                        zzf(0);
                        return false;
                    }
                } catch (RemoteException e) {
                    zzb.zzd("Failed to get native ad mapper", e);
                }
            } else {
                zzh.zza zza3 = zzif2.zzLa;
                if ((zza3 instanceof zze) && this.zzpj.zzrA != null) {
                    zza((zze) zzif2.zzLa);
                } else if ((zza3 instanceof zzd) && this.zzpj.zzrz != null) {
                    zza((zzd) zzif2.zzLa);
                } else if (!(zza3 instanceof zzf) || this.zzpj.zzrC == null || this.zzpj.zzrC.get(((zzf) zza3).getCustomTemplateId()) == null) {
                    zzb.zzaK("No matching listener for retrieved native ad template.");
                    zzf(0);
                    return false;
                } else {
                    zza(zzif2, ((zzf) zza3).getCustomTemplateId());
                }
            }
            return super.zza(zzif, zzif2);
        }
        throw new IllegalStateException("Native ad DOES NOT have custom rendering mode.");
    }

    public void zzb(SimpleArrayMap<String, zzct> simpleArrayMap) {
        zzx.zzcD("setOnCustomClickListener must be called on the main UI thread.");
        this.zzpj.zzrB = simpleArrayMap;
    }

    public void zzb(NativeAdOptionsParcel nativeAdOptionsParcel) {
        zzx.zzcD("setNativeAdOptions must be called on the main UI thread.");
        this.zzpj.zzrD = nativeAdOptionsParcel;
    }

    public void zzb(zzcr zzcr) {
        zzx.zzcD("setOnAppInstallAdLoadedListener must be called on the main UI thread.");
        this.zzpj.zzrz = zzcr;
    }

    public void zzb(zzcs zzcs) {
        zzx.zzcD("setOnContentAdLoadedListener must be called on the main UI thread.");
        this.zzpj.zzrA = zzcs;
    }

    public SimpleArrayMap<String, zzcu> zzbv() {
        zzx.zzcD("getOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        return this.zzpj.zzrC;
    }

    public zzct zzs(String str) {
        zzx.zzcD("getOnCustomClickListener must be called on the main UI thread.");
        return (zzct) this.zzpj.zzrB.get(str);
    }
}
