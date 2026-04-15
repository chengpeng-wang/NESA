package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;

@zzhb
public class zza {

    public interface zza {
        void zza(com.google.android.gms.internal.zzif.zza zza);
    }

    public zzim zza(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, zzan zzan, zza zza2) {
        zzim zzm = zza.zzHt.extras.getBundle("sdk_less_server_data") != null ? new zzm(context, zza, zza2) : new zzb(context, zza, zzan, zza2);
        zzm.zzgd();
        return zzm;
    }
}
