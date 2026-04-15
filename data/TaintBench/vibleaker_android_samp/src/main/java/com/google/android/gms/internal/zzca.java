package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@zzhb
public class zzca {
    @Nullable
    private final zzcb zzpe;
    private final Map<String, zzbz> zzxy = new HashMap();

    public zzca(@Nullable zzcb zzcb) {
        this.zzpe = zzcb;
    }

    public void zza(String str, zzbz zzbz) {
        this.zzxy.put(str, zzbz);
    }

    public void zza(String str, String str2, long j) {
        zzbx.zza(this.zzpe, (zzbz) this.zzxy.get(str2), j, str);
        this.zzxy.put(str, zzbx.zza(this.zzpe, j));
    }

    @Nullable
    public zzcb zzdA() {
        return this.zzpe;
    }
}
