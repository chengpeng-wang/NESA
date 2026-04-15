package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzjr {
    public zzjp zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzan, VersionInfoParcel versionInfoParcel) {
        return zza(context, adSizeParcel, z, z2, zzan, versionInfoParcel, null, null);
    }

    public zzjp zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzan, VersionInfoParcel versionInfoParcel, zzcb zzcb, zzd zzd) {
        zzjp zzjs = new zzjs(zzjt.zzb(context, adSizeParcel, z, z2, zzan, versionInfoParcel, zzcb, zzd));
        zzjs.setWebViewClient(zzr.zzbE().zzb(zzjs, z2));
        zzjs.setWebChromeClient(zzr.zzbE().zzk(zzjs));
        return zzjs;
    }
}
