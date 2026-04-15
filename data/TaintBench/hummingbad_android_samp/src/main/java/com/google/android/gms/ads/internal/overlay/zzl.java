package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import com.google.android.gms.internal.zzcc;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzip;

public class zzl implements zzj {
    public zzi zza(Context context, zzip zzip, int i, zzcd zzcd, zzcc zzcc) {
        return new zzc(context, new zzp(context, zzip.zzgV(), zzip.getRequestId(), zzcd, zzcc));
    }
}
