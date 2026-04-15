package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import java.util.concurrent.Callable;

@zzhb
public class zzbs {
    private final Object zzpV = new Object();
    private boolean zzqA = false;
    /* access modifiers changed from: private */
    public SharedPreferences zzvx = null;

    public void initialize(Context context) {
        synchronized (this.zzpV) {
            if (this.zzqA) {
                return;
            }
            Context remoteContext = zze.getRemoteContext(context);
            if (remoteContext == null) {
                return;
            }
            this.zzvx = zzr.zzbJ().zzw(remoteContext);
            this.zzqA = true;
        }
    }

    public <T> T zzd(final zzbp<T> zzbp) {
        synchronized (this.zzpV) {
            if (this.zzqA) {
                return zzjb.zzb(new Callable<T>() {
                    public T call() {
                        return zzbp.zza(zzbs.this.zzvx);
                    }
                });
            }
            Object zzdq = zzbp.zzdq();
            return zzdq;
        }
    }
}
