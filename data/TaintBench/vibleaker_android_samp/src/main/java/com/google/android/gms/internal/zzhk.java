package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.zzr;
import java.util.WeakHashMap;

@zzhb
public final class zzhk {
    private WeakHashMap<Context, zza> zzKm = new WeakHashMap();

    private class zza {
        public final long zzKn = zzr.zzbG().currentTimeMillis();
        public final zzhj zzKo;

        public zza(zzhj zzhj) {
            this.zzKo = zzhj;
        }

        public boolean hasExpired() {
            return ((Long) zzbt.zzwM.get()).longValue() + this.zzKn < zzr.zzbG().currentTimeMillis();
        }
    }

    public zzhj zzE(Context context) {
        zza zza = (zza) this.zzKm.get(context);
        zzhj zzgI = (zza == null || zza.hasExpired() || !((Boolean) zzbt.zzwL.get()).booleanValue()) ? new com.google.android.gms.internal.zzhj.zza(context).zzgI() : new com.google.android.gms.internal.zzhj.zza(context, zza.zzKo).zzgI();
        this.zzKm.put(context, new zza(zzgI));
        return zzgI;
    }
}
