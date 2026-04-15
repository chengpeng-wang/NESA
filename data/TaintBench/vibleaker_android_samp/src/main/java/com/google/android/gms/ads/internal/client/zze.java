package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zze extends zzg<zzv> {
    public zze() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    private zzu zza(Context context, AdSizeParcel adSizeParcel, String str, zzew zzew, int i) {
        try {
            return zza.zzk(((zzv) zzaB(context)).zza(com.google.android.gms.dynamic.zze.zzC(context), adSizeParcel, str, zzew, 8487000, i));
        } catch (RemoteException | zzg.zza e) {
            zzb.zza("Could not create remote AdManager.", e);
            return null;
        }
    }

    public zzu zza(Context context, AdSizeParcel adSizeParcel, String str, zzew zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzu zza = zza(context, adSizeParcel, str, zzew, 1);
            if (zza != null) {
                return zza;
            }
        }
        zzb.zzaI("Using BannerAdManager from the client jar.");
        return zzn.zzcU().createBannerAdManager(context, adSizeParcel, str, zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    public zzu zzb(Context context, AdSizeParcel adSizeParcel, String str, zzew zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzu zza = zza(context, adSizeParcel, str, zzew, 2);
            if (zza != null) {
                return zza;
            }
        }
        zzb.zzaK("Using InterstitialAdManager from the client jar.");
        return zzn.zzcU().createInterstitialAdManager(context, adSizeParcel, str, zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    /* access modifiers changed from: protected */
    /* renamed from: zze */
    public zzv zzd(IBinder iBinder) {
        return zzv.zza.zzl(iBinder);
    }
}
