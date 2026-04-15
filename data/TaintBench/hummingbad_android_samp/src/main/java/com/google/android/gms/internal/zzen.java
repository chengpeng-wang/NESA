package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzei.zza;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

@zzgk
public final class zzen extends zza {
    private final MediationAdapter zzyW;
    private zzeo zzyX;

    public zzen(MediationAdapter mediationAdapter) {
        this.zzyW = mediationAdapter;
    }

    private Bundle zza(String str, int i, String str2) throws RemoteException {
        zzb.zzaE("Server parameters: " + str);
        try {
            Bundle bundle = new Bundle();
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str);
                Bundle bundle2 = new Bundle();
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str3 = (String) keys.next();
                    bundle2.putString(str3, jSONObject.getString(str3));
                }
                bundle = bundle2;
            }
            if (this.zzyW instanceof AdMobAdapter) {
                bundle.putString("adJson", str2);
                bundle.putInt("tagForChildDirectedTreatment", i);
            }
            return bundle;
        } catch (Throwable th) {
            zzb.zzd("Could not get Server Parameters Bundle.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void destroy() throws RemoteException {
        try {
            this.zzyW.onDestroy();
        } catch (Throwable th) {
            zzb.zzd("Could not destroy adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public zzd getView() throws RemoteException {
        if (this.zzyW instanceof MediationBannerAdapter) {
            try {
                return zze.zzx(((MediationBannerAdapter) this.zzyW).getBannerView());
            } catch (Throwable th) {
                zzb.zzd("Could not get banner view from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationBannerAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public boolean isInitialized() throws RemoteException {
        if (this.zzyW instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaC("Check if adapter is initialized.");
            try {
                return ((MediationRewardedVideoAdAdapter) this.zzyW).isInitialized();
            } catch (Throwable th) {
                zzb.zzd("Could not check if adapter is initialized.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void pause() throws RemoteException {
        try {
            this.zzyW.onPause();
        } catch (Throwable th) {
            zzb.zzd("Could not pause adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void resume() throws RemoteException {
        try {
            this.zzyW.onResume();
        } catch (Throwable th) {
            zzb.zzd("Could not resume adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzyW instanceof MediationInterstitialAdapter) {
            zzb.zzaC("Showing interstitial from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzyW).showInterstitial();
            } catch (Throwable th) {
                zzb.zzd("Could not show interstitial from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void showVideo() throws RemoteException {
        if (this.zzyW instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaC("Show rewarded video ad from adapter.");
            try {
                ((MediationRewardedVideoAdAdapter) this.zzyW).showVideo();
            } catch (Throwable th) {
                zzb.zzd("Could not show rewarded video ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(AdRequestParcel adRequestParcel, String str) throws RemoteException {
        if (this.zzyW instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaC("Requesting rewarded video ad from adapter.");
            try {
                MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzyW;
                mediationRewardedVideoAdAdapter.loadAd(new zzem(adRequestParcel.zzsq == -1 ? null : new Date(adRequestParcel.zzsq), adRequestParcel.zzsr, adRequestParcel.zzss != null ? new HashSet(adRequestParcel.zzss) : null, adRequestParcel.zzsy, adRequestParcel.zzst, adRequestParcel.zzsu), zza(str, adRequestParcel.zzsu, null), adRequestParcel.zzsA != null ? adRequestParcel.zzsA.getBundle(mediationRewardedVideoAdAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not load rewarded video ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza zza, String str2) throws RemoteException {
        Bundle bundle = null;
        if (this.zzyW instanceof MediationRewardedVideoAdAdapter) {
            zzb.zzaC("Initialize rewarded video adapter.");
            try {
                MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzyW;
                zzem zzem = new zzem(adRequestParcel.zzsq == -1 ? null : new Date(adRequestParcel.zzsq), adRequestParcel.zzsr, adRequestParcel.zzss != null ? new HashSet(adRequestParcel.zzss) : null, adRequestParcel.zzsy, adRequestParcel.zzst, adRequestParcel.zzsu);
                if (adRequestParcel.zzsA != null) {
                    bundle = adRequestParcel.zzsA.getBundle(mediationRewardedVideoAdAdapter.getClass().getName());
                }
                mediationRewardedVideoAdAdapter.initialize((Context) zze.zzp(zzd), zzem, str, new com.google.android.gms.ads.internal.reward.mediation.client.zzb(zza), zza(str2, adRequestParcel.zzsu, null), bundle);
            } catch (Throwable th) {
                zzb.zzd("Could not initialize rewarded video adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, zzej zzej) throws RemoteException {
        zza(zzd, adRequestParcel, str, null, zzej);
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej) throws RemoteException {
        if (this.zzyW instanceof MediationInterstitialAdapter) {
            zzb.zzaC("Requesting interstitial ad from adapter.");
            try {
                MediationInterstitialAdapter mediationInterstitialAdapter = (MediationInterstitialAdapter) this.zzyW;
                mediationInterstitialAdapter.requestInterstitialAd((Context) zze.zzp(zzd), new zzeo(zzej), zza(str, adRequestParcel.zzsu, str2), new zzem(adRequestParcel.zzsq == -1 ? null : new Date(adRequestParcel.zzsq), adRequestParcel.zzsr, adRequestParcel.zzss != null ? new HashSet(adRequestParcel.zzss) : null, adRequestParcel.zzsy, adRequestParcel.zzst, adRequestParcel.zzsu), adRequestParcel.zzsA != null ? adRequestParcel.zzsA.getBundle(mediationInterstitialAdapter.getClass().getName()) : null);
            } catch (Throwable th) {
                zzb.zzd("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) throws RemoteException {
        if (this.zzyW instanceof com.google.android.gms.ads.mediation.zza) {
            try {
                com.google.android.gms.ads.mediation.zza zza = (com.google.android.gms.ads.mediation.zza) this.zzyW;
                zzer zzer = new zzer(adRequestParcel.zzsq == -1 ? null : new Date(adRequestParcel.zzsq), adRequestParcel.zzsr, adRequestParcel.zzss != null ? new HashSet(adRequestParcel.zzss) : null, adRequestParcel.zzsy, adRequestParcel.zzst, adRequestParcel.zzsu, nativeAdOptionsParcel, list);
                Bundle bundle = adRequestParcel.zzsA != null ? adRequestParcel.zzsA.getBundle(zza.getClass().getName()) : null;
                this.zzyX = new zzeo(zzej);
                zza.requestNativeAd((Context) zze.zzp(zzd), this.zzyX, zza(str, adRequestParcel.zzsu, str2), zzer, bundle);
            } catch (Throwable th) {
                zzb.zzd("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationNativeAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzej zzej) throws RemoteException {
        zza(zzd, adSizeParcel, adRequestParcel, str, null, zzej);
    }

    public void zza(zzd zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej) throws RemoteException {
        Bundle bundle = null;
        if (this.zzyW instanceof MediationBannerAdapter) {
            zzb.zzaC("Requesting banner ad from adapter.");
            try {
                MediationBannerAdapter mediationBannerAdapter = (MediationBannerAdapter) this.zzyW;
                zzem zzem = new zzem(adRequestParcel.zzsq == -1 ? null : new Date(adRequestParcel.zzsq), adRequestParcel.zzsr, adRequestParcel.zzss != null ? new HashSet(adRequestParcel.zzss) : null, adRequestParcel.zzsy, adRequestParcel.zzst, adRequestParcel.zzsu);
                if (adRequestParcel.zzsA != null) {
                    bundle = adRequestParcel.zzsA.getBundle(mediationBannerAdapter.getClass().getName());
                }
                mediationBannerAdapter.requestBannerAd((Context) zze.zzp(zzd), new zzeo(zzej), zza(str, adRequestParcel.zzsu, str2), com.google.android.gms.ads.zza.zza(adSizeParcel.width, adSizeParcel.height, adSizeParcel.zzsG), zzem, bundle);
            } catch (Throwable th) {
                zzb.zzd("Could not request banner ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationBannerAdapter: " + this.zzyW.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public zzek zzdS() {
        NativeAdMapper zzdU = this.zzyX.zzdU();
        return zzdU instanceof NativeAppInstallAdMapper ? new zzep((NativeAppInstallAdMapper) zzdU) : null;
    }

    public zzel zzdT() {
        NativeAdMapper zzdU = this.zzyX.zzdU();
        return zzdU instanceof NativeContentAdMapper ? new zzeq((NativeContentAdMapper) zzdU) : null;
    }
}
