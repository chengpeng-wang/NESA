package com.google.android.gms.internal;

import android.app.Activity;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzei.zza;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@zzgk
public final class zzes<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends zza {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> zzzc;
    private final NETWORK_EXTRAS zzzd;

    public zzes(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.zzzc = mediationAdapter;
        this.zzzd = network_extras;
    }

    private SERVER_PARAMETERS zzb(String str, int i, String str2) throws RemoteException {
        Map hashMap;
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                hashMap = new HashMap(jSONObject.length());
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str3 = (String) keys.next();
                    hashMap.put(str3, jSONObject.getString(str3));
                }
            } catch (Throwable th) {
                zzb.zzd("Could not get MediationServerParameters.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            hashMap = new HashMap(0);
        }
        Class serverParametersType = this.zzzc.getServerParametersType();
        if (serverParametersType == null) {
            return null;
        }
        MediationServerParameters mediationServerParameters = (MediationServerParameters) serverParametersType.newInstance();
        mediationServerParameters.load(hashMap);
        return mediationServerParameters;
    }

    public void destroy() throws RemoteException {
        try {
            this.zzzc.destroy();
        } catch (Throwable th) {
            zzb.zzd("Could not destroy adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public zzd getView() throws RemoteException {
        if (this.zzzc instanceof MediationBannerAdapter) {
            try {
                return zze.zzx(((MediationBannerAdapter) this.zzzc).getBannerView());
            } catch (Throwable th) {
                zzb.zzd("Could not get banner view from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationBannerAdapter: " + this.zzzc.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public boolean isInitialized() {
        return true;
    }

    public void pause() throws RemoteException {
        throw new RemoteException();
    }

    public void resume() throws RemoteException {
        throw new RemoteException();
    }

    public void showInterstitial() throws RemoteException {
        if (this.zzzc instanceof MediationInterstitialAdapter) {
            zzb.zzaC("Showing interstitial from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzzc).showInterstitial();
            } catch (Throwable th) {
                zzb.zzd("Could not show interstitial from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzzc.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void showVideo() {
    }

    public void zza(AdRequestParcel adRequestParcel, String str) {
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza zza, String str2) throws RemoteException {
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, zzej zzej) throws RemoteException {
        zza(zzd, adRequestParcel, str, null, zzej);
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej) throws RemoteException {
        if (this.zzzc instanceof MediationInterstitialAdapter) {
            zzb.zzaC("Requesting interstitial ad from adapter.");
            try {
                ((MediationInterstitialAdapter) this.zzzc).requestInterstitialAd(new zzet(zzej), (Activity) zze.zzp(zzd), zzb(str, adRequestParcel.zzsu, str2), zzeu.zzg(adRequestParcel), this.zzzd);
            } catch (Throwable th) {
                zzb.zzd("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzzc.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void zza(zzd zzd, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
    }

    public void zza(zzd zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzej zzej) throws RemoteException {
        zza(zzd, adSizeParcel, adRequestParcel, str, null, zzej);
    }

    public void zza(zzd zzd, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzej zzej) throws RemoteException {
        if (this.zzzc instanceof MediationBannerAdapter) {
            zzb.zzaC("Requesting banner ad from adapter.");
            try {
                ((MediationBannerAdapter) this.zzzc).requestBannerAd(new zzet(zzej), (Activity) zze.zzp(zzd), zzb(str, adRequestParcel.zzsu, str2), zzeu.zzb(adSizeParcel), zzeu.zzg(adRequestParcel), this.zzzd);
            } catch (Throwable th) {
                zzb.zzd("Could not request banner ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            zzb.zzaE("MediationAdapter is not a MediationBannerAdapter: " + this.zzzc.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public zzek zzdS() {
        return null;
    }

    public zzel zzdT() {
        return null;
    }
}
