package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomClickListener;
import com.google.android.gms.internal.zzcv.zza;

@zzgk
public class zzda extends zza {
    private final OnCustomClickListener zzwF;

    public zzda(OnCustomClickListener onCustomClickListener) {
        this.zzwF = onCustomClickListener;
    }

    public void zza(zzcr zzcr, String str) {
        this.zzwF.onCustomClick(new zzcs(zzcr), str);
    }
}
