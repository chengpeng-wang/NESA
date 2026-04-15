package com.google.android.gms.ads.internal.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;

public class zzag extends com.google.android.gms.ads.internal.client.zzs.zza {
    /* access modifiers changed from: private */
    public zzq zzpK;

    private class zza extends com.google.android.gms.ads.internal.client.zzr.zza {
        private zza() {
        }

        public String getMediationAdapterClassName() throws RemoteException {
            return null;
        }

        public boolean isLoading() throws RemoteException {
            return false;
        }

        public void zzf(AdRequestParcel adRequestParcel) throws RemoteException {
            zzb.e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
            com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
                public void run() {
                    if (zzag.this.zzpK != null) {
                        try {
                            zzag.this.zzpK.onAdFailedToLoad(1);
                        } catch (RemoteException e) {
                            zzb.zzd("Could not notify onAdFailedToLoad event.", e);
                        }
                    }
                }
            });
        }
    }

    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) throws RemoteException {
    }

    public void zza(zzcr zzcr) throws RemoteException {
    }

    public void zza(zzcs zzcs) throws RemoteException {
    }

    public void zza(String str, zzcu zzcu, zzct zzct) throws RemoteException {
    }

    public void zzb(zzq zzq) throws RemoteException {
        this.zzpK = zzq;
    }

    public void zzb(zzx zzx) throws RemoteException {
    }

    public zzr zzbn() throws RemoteException {
        return new zza();
    }
}
