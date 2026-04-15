package com.google.android.gms.ads.internal.reward.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.reward.client.zzb.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzf extends zzg<zzc> {
    public zzf() {
        super("com.google.android.gms.ads.reward.RewardedVideoAdCreatorImpl");
    }

    private zzb zzb(Context context, zzew zzew) {
        try {
            return zza.zzaa(((zzc) zzaB(context)).zza(zze.zzC(context), zzew, 8487000));
        } catch (RemoteException | zzg.zza e) {
            zzb.zzd("Could not get remote RewardedVideoAd.", e);
            return null;
        }
    }

    public zzb zza(Context context, zzew zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzb zzb = zzb(context, zzew);
            if (zzb != null) {
                return zzb;
            }
        }
        zzb.zzaI("Using RewardedVideoAd from the client jar.");
        return zzn.zzcU().createRewardedVideoAd(context, zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzad */
    public zzc zzd(IBinder iBinder) {
        return zzc.zza.zzab(iBinder);
    }
}
