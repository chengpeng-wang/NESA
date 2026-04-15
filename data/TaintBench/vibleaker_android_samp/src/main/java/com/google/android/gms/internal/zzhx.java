package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.zza.zza;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;

@zzhb
public class zzhx extends zza {
    private zzhy zzKC;
    private zzhv zzKJ;
    private zzhw zzKK;

    public zzhx(zzhw zzhw) {
        this.zzKK = zzhw;
    }

    public void zza(zzd zzd, RewardItemParcel rewardItemParcel) {
        if (this.zzKK != null) {
            this.zzKK.zzc(rewardItemParcel);
        }
    }

    public void zza(zzhv zzhv) {
        this.zzKJ = zzhv;
    }

    public void zza(zzhy zzhy) {
        this.zzKC = zzhy;
    }

    public void zzb(zzd zzd, int i) {
        if (this.zzKJ != null) {
            this.zzKJ.zzN(i);
        }
    }

    public void zzc(zzd zzd, int i) {
        if (this.zzKC != null) {
            this.zzKC.zza(zze.zzp(zzd).getClass().getName(), i);
        }
    }

    public void zzg(zzd zzd) {
        if (this.zzKJ != null) {
            this.zzKJ.zzgN();
        }
    }

    public void zzh(zzd zzd) {
        if (this.zzKC != null) {
            this.zzKC.zzax(zze.zzp(zzd).getClass().getName());
        }
    }

    public void zzi(zzd zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdOpened();
        }
    }

    public void zzj(zzd zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoStarted();
        }
    }

    public void zzk(zzd zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdClosed();
        }
    }

    public void zzl(zzd zzd) {
        if (this.zzKK != null) {
            this.zzKK.zzgM();
        }
    }

    public void zzm(zzd zzd) {
        if (this.zzKK != null) {
            this.zzKK.onRewardedVideoAdLeftApplication();
        }
    }
}
