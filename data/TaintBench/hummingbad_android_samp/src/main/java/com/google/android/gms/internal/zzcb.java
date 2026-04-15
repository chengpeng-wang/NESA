package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;

public class zzcb {
    public zzca zza(zzbz zzbz) {
        if (zzbz == null) {
            throw new IllegalArgumentException("CSI configuration can't be null. ");
        } else if (!zzbz.zzdf()) {
            zzb.v("CsiReporterFactory: CSI is not enabled. No CSI reporter created.");
            return null;
        } else if (zzbz.getContext() == null) {
            throw new IllegalArgumentException("Context can't be null. Please set up context in CsiConfiguration.");
        } else if (!TextUtils.isEmpty(zzbz.zzbV())) {
            return new zzca(zzbz.getContext(), zzbz.zzbV(), zzbz.zzdg(), zzbz.zzdh());
        } else {
            throw new IllegalArgumentException("AfmaVersion can't be null or empty. Please set up afmaVersion in CsiConfiguration.");
        }
    }
}
