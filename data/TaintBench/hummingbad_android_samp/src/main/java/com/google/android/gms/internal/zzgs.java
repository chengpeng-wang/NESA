package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.zzp;
import java.util.WeakHashMap;

@zzgk
public final class zzgs {
    private WeakHashMap<Context, zza> zzFX = new WeakHashMap();

    private class zza {
        public final long zzFY = zzp.zzbB().currentTimeMillis();
        public final zzgr zzFZ;

        public zza(zzgr zzgr) {
            this.zzFZ = zzgr;
        }

        public boolean hasExpired() {
            return this.zzFY + ((Long) zzby.zzuY.get()).longValue() < zzp.zzbB().currentTimeMillis();
        }
    }

    public zzgr zzD(Context context) {
        zza zza = (zza) this.zzFX.get(context);
        zzgr zzfN = (zza == null || zza.hasExpired() || !((Boolean) zzby.zzuX.get()).booleanValue()) ? new com.google.android.gms.internal.zzgr.zza(context).zzfN() : new com.google.android.gms.internal.zzgr.zza(context, zza.zzFZ).zzfN();
        this.zzFX.put(context, new zza(zzfN));
        return zzfN;
    }
}
