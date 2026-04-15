package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzhj.zza;
import java.util.HashMap;

@zzgk
public class zzgx extends zzb implements zzhb {
    private zzd zzGg;
    private String zzGh;
    private boolean zzGi;
    private HashMap<String, zzgy> zzGj = new HashMap();

    public zzgx(Context context, AdSizeParcel adSizeParcel, zzeh zzeh, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, null, zzeh, versionInfoParcel, null);
    }

    public void destroy() {
        zzx.zzch("destroy must be called on the main UI thread.");
        for (String str : this.zzGj.keySet()) {
            try {
                zzgy zzgy = (zzgy) this.zzGj.get(str);
                if (!(zzgy == null || zzgy.zzfQ() == null)) {
                    zzgy.zzfQ().destroy();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaE("Fail to destroy adapter: " + str);
            }
        }
    }

    public boolean isLoaded() {
        zzx.zzch("isLoaded must be called on the main UI thread.");
        return this.zzos.zzqd == null && this.zzos.zzqe == null && this.zzos.zzqg != null && !this.zzGi;
    }

    public void onRewardedVideoAdClosed() {
        if (this.zzGg != null) {
            try {
                this.zzGg.onRewardedVideoAdClosed();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onAdClosed().", e);
            }
        }
    }

    public void onRewardedVideoAdLeftApplication() {
        if (this.zzGg != null) {
            try {
                this.zzGg.onRewardedVideoAdLeftApplication();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onAdLeftApplication().", e);
            }
        }
    }

    public void onRewardedVideoAdOpened() {
        zza(this.zzos.zzqg, false);
        if (this.zzGg != null) {
            try {
                this.zzGg.onRewardedVideoAdOpened();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onAdOpened().", e);
            }
        }
    }

    public void onRewardedVideoStarted() {
        zzp.zzbJ().zza(this.zzos.context, this.zzos.zzqb.zzIz, this.zzos.zzqg, this.zzos.zzpZ, false, this.zzos.zzqg.zzyQ.zzys);
        if (this.zzGg != null) {
            try {
                this.zzGg.onRewardedVideoStarted();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onVideoStarted().", e);
            }
        }
    }

    public void pause() {
        zzx.zzch("pause must be called on the main UI thread.");
        for (String str : this.zzGj.keySet()) {
            try {
                zzgy zzgy = (zzgy) this.zzGj.get(str);
                if (!(zzgy == null || zzgy.zzfQ() == null)) {
                    zzgy.zzfQ().pause();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaE("Fail to pause adapter: " + str);
            }
        }
    }

    public void resume() {
        zzx.zzch("resume must be called on the main UI thread.");
        for (String str : this.zzGj.keySet()) {
            try {
                zzgy zzgy = (zzgy) this.zzGj.get(str);
                if (!(zzgy == null || zzgy.zzfQ() == null)) {
                    zzgy.zzfQ().resume();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaE("Fail to resume adapter: " + str);
            }
        }
    }

    public void setUserId(String userId) {
        zzx.zzch("setUserId must be called on the main UI thread.");
        this.zzGh = userId;
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) {
        zzx.zzch("loadAd must be called on the main UI thread.");
        if (TextUtils.isEmpty(rewardedVideoAdRequestParcel.zzpZ)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Invalid ad unit id. Aborting.");
            return;
        }
        this.zzGi = false;
        this.zzos.zzpZ = rewardedVideoAdRequestParcel.zzpZ;
        super.zza(rewardedVideoAdRequestParcel.zzDy);
    }

    public void zza(zzd zzd) {
        zzx.zzch("setRewardedVideoAdListener must be called on the main UI thread.");
        this.zzGg = zzd;
    }

    public void zza(RewardItemParcel rewardItemParcel) {
        zzp.zzbJ().zza(this.zzos.context, this.zzos.zzqb.zzIz, this.zzos.zzqg, this.zzos.zzpZ, false, this.zzos.zzqg.zzyQ.zzyt);
        if (this.zzGg != null) {
            try {
                if (this.zzos.zzqg == null || this.zzos.zzqg.zzGG == null || TextUtils.isEmpty(this.zzos.zzqg.zzGG.zzyB)) {
                    this.zzGg.zza(new zzgv(rewardItemParcel.type, rewardItemParcel.zzGE));
                } else {
                    this.zzGg.zza(new zzgv(this.zzos.zzqg.zzGG.zzyB, this.zzos.zzqg.zzGG.zzyC));
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onRewarded().", e);
            }
        }
    }

    public void zza(final zza zza, zzcd zzcd) {
        if (zza.errorCode != -2) {
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzgx.this.zzb(new zzhj(zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzos.zzqz = 0;
        this.zzos.zzqe = new zzhe(this.zzos.context, this.zzGh, zza, this);
        com.google.android.gms.ads.internal.util.client.zzb.zzaC("AdRenderer: " + this.zzos.zzqe.getClass().getName());
        this.zzos.zzqe.zzgn();
    }

    public boolean zza(zzhj zzhj, zzhj zzhj2) {
        if (this.zzGg != null) {
            try {
                this.zzGg.onRewardedVideoAdLoaded();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onAdLoaded().", e);
            }
        }
        return true;
    }

    public zzgy zzar(String str) {
        Throwable e;
        zzgy zzgy = (zzgy) this.zzGj.get(str);
        if (zzgy != null) {
            return zzgy;
        }
        try {
            zzgy zzgy2 = new zzgy(this.zzow.zzab(str), this);
            try {
                this.zzGj.put(str, zzgy2);
                return zzgy2;
            } catch (Exception e2) {
                Throwable th = e2;
                zzgy = zzgy2;
                e = th;
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, e);
                return zzgy;
            }
        } catch (Exception e3) {
            e = e3;
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, e);
            return zzgy;
        }
    }

    /* access modifiers changed from: protected */
    public boolean zze(int i) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaE("Failed to load ad: " + i);
        if (this.zzGg == null) {
            return false;
        }
        try {
            this.zzGg.onRewardedVideoAdFailedToLoad(i);
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call RewardedVideoAdListener.onAdFailedToLoad().", e);
            return false;
        }
    }

    public void zzfO() {
        zzx.zzch("showAd must be called on the main UI thread.");
        if (isLoaded()) {
            this.zzGi = true;
            zzgy zzar = zzar(this.zzos.zzqg.zzyS);
            if (zzar != null && zzar.zzfQ() != null) {
                try {
                    zzar.zzfQ().showVideo();
                    return;
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call showVideo.", e);
                    return;
                }
            }
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE("The reward video has not loaded.");
    }

    public void zzfP() {
        onAdClicked();
    }
}
