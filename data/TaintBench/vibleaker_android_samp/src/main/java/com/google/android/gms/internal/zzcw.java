package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.internal.zzcr.zza;

@zzhb
public class zzcw extends zza {
    private final OnAppInstallAdLoadedListener zzyS;

    public zzcw(OnAppInstallAdLoadedListener onAppInstallAdLoadedListener) {
        this.zzyS = onAppInstallAdLoadedListener;
    }

    public void zza(zzcl zzcl) {
        this.zzyS.onAppInstallAdLoaded(zzb(zzcl));
    }

    /* access modifiers changed from: 0000 */
    public zzcm zzb(zzcl zzcl) {
        return new zzcm(zzcl);
    }
}
