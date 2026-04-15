package com.google.android.gms.internal;

import android.support.annotation.Nullable;

@zzhb
public class zzbx {
    @Nullable
    public static zzbz zza(@Nullable zzcb zzcb, long j) {
        return zzcb == null ? null : zzcb.zzb(j);
    }

    public static boolean zza(@Nullable zzcb zzcb, @Nullable zzbz zzbz, long j, String... strArr) {
        return (zzcb == null || zzbz == null) ? false : zzcb.zza(zzbz, j, strArr);
    }

    public static boolean zza(@Nullable zzcb zzcb, @Nullable zzbz zzbz, String... strArr) {
        return (zzcb == null || zzbz == null) ? false : zzcb.zza(zzbz, strArr);
    }

    @Nullable
    public static zzbz zzb(@Nullable zzcb zzcb) {
        return zzcb == null ? null : zzcb.zzdB();
    }
}
