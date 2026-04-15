package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.internal.zzct.zza;

@zzgk
public class zzcy extends zza {
    private final OnAppInstallAdLoadedListener zzwD;

    public zzcy(OnAppInstallAdLoadedListener onAppInstallAdLoadedListener) {
        this.zzwD = onAppInstallAdLoadedListener;
    }

    public void zza(zzcn zzcn) {
        this.zzwD.onAppInstallAdLoaded(zzb(zzcn));
    }

    /* access modifiers changed from: 0000 */
    public zzco zzb(zzcn zzcn) {
        return new zzco(zzcn);
    }
}
