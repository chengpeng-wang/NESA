package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.internal.zzcs.zza;

@zzhb
public class zzcx extends zza {
    private final OnContentAdLoadedListener zzyT;

    public zzcx(OnContentAdLoadedListener onContentAdLoadedListener) {
        this.zzyT = onContentAdLoadedListener;
    }

    public void zza(zzcn zzcn) {
        this.zzyT.onContentAdLoaded(zzb(zzcn));
    }

    /* access modifiers changed from: 0000 */
    public zzco zzb(zzcn zzcn) {
        return new zzco(zzcn);
    }
}
