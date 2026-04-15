package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.internal.client.zzt.zza;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class zzi extends zza {
    private final AppEventListener zzsK;

    public zzi(AppEventListener appEventListener) {
        this.zzsK = appEventListener;
    }

    public void onAppEvent(String name, String info) {
        this.zzsK.onAppEvent(name, info);
    }
}
