package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.zza.zza;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;

@zzgk
public class zzhc extends zza {
    private zzhd zzGq;
    private zzha zzGw;
    private zzhb zzGx;

    public zzhc(zzhb zzhb) {
        this.zzGx = zzhb;
    }

    public void zza(zzd zzd, RewardItemParcel rewardItemParcel) {
        if (this.zzGx != null) {
            this.zzGx.zza(rewardItemParcel);
        }
    }

    public void zza(zzha zzha) {
        this.zzGw = zzha;
    }

    public void zza(zzhd zzhd) {
        this.zzGq = zzhd;
    }

    public void zzb(zzd zzd, int i) {
        if (this.zzGw != null) {
            this.zzGw.zzJ(i);
        }
    }

    public void zzc(zzd zzd, int i) {
        if (this.zzGq != null) {
            this.zzGq.zzb(zze.zzp(zzd).getClass().getName(), i);
        }
    }

    public void zzg(zzd zzd) {
        if (this.zzGw != null) {
            this.zzGw.zzfS();
        }
    }

    public void zzh(zzd zzd) {
        if (this.zzGq != null) {
            this.zzGq.zzas(zze.zzp(zzd).getClass().getName());
        }
    }

    public void zzi(zzd zzd) {
        if (this.zzGx != null) {
            this.zzGx.onRewardedVideoAdOpened();
        }
    }

    public void zzj(zzd zzd) {
        if (this.zzGx != null) {
            this.zzGx.onRewardedVideoStarted();
        }
    }

    public void zzk(zzd zzd) {
        if (this.zzGx != null) {
            this.zzGx.onRewardedVideoAdClosed();
        }
    }

    public void zzl(zzd zzd) {
        if (this.zzGx != null) {
            this.zzGx.zzfP();
        }
    }

    public void zzm(zzd zzd) {
        if (this.zzGx != null) {
            this.zzGx.onRewardedVideoAdLeftApplication();
        }
    }
}
