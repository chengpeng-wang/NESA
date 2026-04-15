package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.support.annotation.Nullable;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;

@zzhb
public class zzn extends zzj {
    @Nullable
    public zzi zza(Context context, zzjp zzjp, int i, zzcb zzcb, zzbz zzbz) {
        if (!zzx(context)) {
            return null;
        }
        return new zzc(context, new zzt(context, zzjp.zzhX(), zzjp.getRequestId(), zzcb, zzbz));
    }
}
