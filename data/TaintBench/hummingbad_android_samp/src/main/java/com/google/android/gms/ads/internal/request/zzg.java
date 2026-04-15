package com.google.android.gms.ads.internal.request;

import com.google.android.gms.ads.internal.request.zzk.zza;
import java.lang.ref.WeakReference;

public final class zzg extends zza {
    private final WeakReference<zzc.zza> zzDV;

    public zzg(zzc.zza zza) {
        this.zzDV = new WeakReference(zza);
    }

    public void zzb(AdResponseParcel adResponseParcel) {
        zzc.zza zza = (zzc.zza) this.zzDV.get();
        if (zza != null) {
            zza.zzb(adResponseParcel);
        }
    }
}
