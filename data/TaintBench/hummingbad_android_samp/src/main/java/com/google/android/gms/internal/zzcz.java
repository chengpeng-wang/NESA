package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.internal.zzcu.zza;

@zzgk
public class zzcz extends zza {
    private final OnContentAdLoadedListener zzwE;

    public zzcz(OnContentAdLoadedListener onContentAdLoadedListener) {
        this.zzwE = onContentAdLoadedListener;
    }

    public void zza(zzcp zzcp) {
        this.zzwE.onContentAdLoaded(zzb(zzcp));
    }

    /* access modifiers changed from: 0000 */
    public zzcq zzb(zzcp zzcp) {
        return new zzcq(zzcp);
    }
}
