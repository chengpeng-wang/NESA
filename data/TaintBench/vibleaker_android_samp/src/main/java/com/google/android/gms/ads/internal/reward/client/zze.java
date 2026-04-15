package com.google.android.gms.ads.internal.reward.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zze implements RewardItem {
    private final zza zzKz;

    public zze(zza zza) {
        this.zzKz = zza;
    }

    public int getAmount() {
        int i = 0;
        if (this.zzKz == null) {
            return i;
        }
        try {
            return this.zzKz.getAmount();
        } catch (RemoteException e) {
            zzb.zzd("Could not forward getAmount to RewardItem", e);
            return i;
        }
    }

    public String getType() {
        String str = null;
        if (this.zzKz == null) {
            return str;
        }
        try {
            return this.zzKz.getType();
        } catch (RemoteException e) {
            zzb.zzd("Could not forward getType to RewardItem", e);
            return str;
        }
    }
}
