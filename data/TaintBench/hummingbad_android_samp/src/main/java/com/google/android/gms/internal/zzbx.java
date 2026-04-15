package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesUtil;

@zzgk
public class zzbx {
    private final Object zzpc = new Object();
    private boolean zzpr = false;
    private SharedPreferences zztU = null;

    public <T> T zzd(zzbu<T> zzbu) {
        synchronized (this.zzpc) {
            if (this.zzpr) {
                return zzbu.zza(this.zztU);
            }
            Object zzdd = zzbu.zzdd();
            return zzdd;
        }
    }

    public void zzw(Context context) {
        synchronized (this.zzpc) {
            if (this.zzpr) {
                return;
            }
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                return;
            }
            this.zztU = zzp.zzbE().zzv(remoteContext);
            this.zzpr = true;
        }
    }
}
