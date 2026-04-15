package com.google.android.gms.ads.internal.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.client.zzb.zza;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;

public class zzal extends zza {
    /* access modifiers changed from: private */
    public zzd zzvb;

    public void destroy() throws RemoteException {
    }

    public boolean isLoaded() throws RemoteException {
        return false;
    }

    public void pause() throws RemoteException {
    }

    public void resume() throws RemoteException {
    }

    public void setUserId(String userId) throws RemoteException {
    }

    public void show() throws RemoteException {
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) throws RemoteException {
        zzb.e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
            public void run() {
                if (zzal.this.zzvb != null) {
                    try {
                        zzal.this.zzvb.onRewardedVideoAdFailedToLoad(1);
                    } catch (RemoteException e) {
                        zzb.zzd("Could not notify onRewardedVideoAdFailedToLoad event.", e);
                    }
                }
            }
        });
    }

    public void zza(zzd zzd) throws RemoteException {
        this.zzvb = zzd;
    }
}
