package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;

@zzhb
public class zzgr {

    public interface zza {
        void zzb(zzif zzif);
    }

    public zzit zza(Context context, com.google.android.gms.ads.internal.zza zza, com.google.android.gms.internal.zzif.zza zza2, zzan zzan, zzjp zzjp, zzex zzex, zza zza3, zzcb zzcb) {
        Object zzgu;
        AdResponseParcel adResponseParcel = zza2.zzLe;
        if (adResponseParcel.zzHT) {
            zzgu = new zzgu(context, zza2, zzex, zza3, zzcb, zzjp);
        } else if (!adResponseParcel.zzuk) {
            zzgu = adResponseParcel.zzHZ ? new zzgp(context, zza2, zzjp, zza3) : (((Boolean) zzbt.zzwu.get()).booleanValue() && zzne.zzsk() && !zzne.isAtLeastL() && zzjp.zzaN().zzui) ? new zzgt(context, zza2, zzjp, zza3) : new zzgs(context, zza2, zzjp, zza3);
        } else if (zza instanceof zzp) {
            zzgu = new zzgv(context, (zzp) zza, new zzee(), zza2, zzan, zza3);
        } else {
            throw new IllegalArgumentException("Invalid NativeAdManager type. Found: " + (zza != null ? zza.getClass().getName() : "null") + "; Required: NativeAdManager.");
        }
        zzb.zzaI("AdRenderer: " + zzgu.getClass().getName());
        zzgu.zzgd();
        return zzgu;
    }

    public zzit zza(Context context, String str, com.google.android.gms.internal.zzif.zza zza, zzht zzht) {
        zzhz zzhz = new zzhz(context, str, zza, zzht);
        zzb.zzaI("AdRenderer: " + zzhz.getClass().getName());
        zzhz.zzgd();
        return zzhz;
    }
}
