package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhq;

@zzgk
public class zza {

    public interface zza {
        void zza(com.google.android.gms.internal.zzhj.zza zza);
    }

    public zzhq zza(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, zzan zzan, zza zza2) {
        zzhq zzm = zza.zzDy.extras.getBundle("sdk_less_server_data") != null ? new zzm(context, zza, zza2) : new zzb(context, zza, zzan, zza2);
        zzm.zzgo();
        return zzm;
    }
}
