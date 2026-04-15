package com.google.android.gms.ads.internal.reward.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.reward.RewardItem;

public class zze implements RewardItem {
    private final zza zzGl;

    public zze(zza zza) {
        this.zzGl = zza;
    }

    public int getAmount() {
        int i = 0;
        if (this.zzGl == null) {
            return i;
        }
        try {
            return this.zzGl.getAmount();
        } catch (RemoteException e) {
            zzb.zzd("Could not forward getAmount to RewardItem", e);
            return i;
        }
    }

    public String getType() {
        String str = null;
        if (this.zzGl == null) {
            return str;
        }
        try {
            return this.zzGl.getType();
        } catch (RemoteException e) {
            zzb.zzd("Could not forward getType to RewardItem", e);
            return str;
        }
    }
}
