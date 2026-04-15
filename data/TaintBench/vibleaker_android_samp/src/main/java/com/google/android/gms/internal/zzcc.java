package com.google.android.gms.internal;

import android.view.View;
import com.google.android.gms.ads.internal.zzg;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzce.zza;

@zzhb
public final class zzcc extends zza {
    private final zzg zzxE;
    private final String zzxF;
    private final String zzxG;

    public zzcc(zzg zzg, String str, String str2) {
        this.zzxE = zzg;
        this.zzxF = str;
        this.zzxG = str2;
    }

    public String getContent() {
        return this.zzxG;
    }

    public void recordClick() {
        this.zzxE.zzbd();
    }

    public void recordImpression() {
        this.zzxE.zzbe();
    }

    public void zzb(zzd zzd) {
        if (zzd != null) {
            this.zzxE.zzc((View) zze.zzp(zzd));
        }
    }

    public String zzdF() {
        return this.zzxF;
    }
}
