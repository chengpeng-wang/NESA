package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zza;

public class zzg implements zzk {
    /* access modifiers changed from: private|final */
    public final zzl zzahj;
    private boolean zzahk = false;

    public zzg(zzl zzl) {
        this.zzahj = zzl;
    }

    private <A extends zzb> void zza(zze<A> zze) throws DeadObjectException {
        this.zzahj.zzagW.zzb((zze) zze);
        zzb zza = this.zzahj.zzagW.zza(zze.zzoR());
        if (zza.isConnected() || !this.zzahj.zzaio.containsKey(zze.zzoR())) {
            zze.zzb(zza);
        } else {
            zze.zzw(new Status(17));
        }
    }

    public void begin() {
    }

    public void connect() {
        if (this.zzahk) {
            this.zzahk = false;
            this.zzahj.zza(new zza(this) {
                public void zzpt() {
                    zzg.this.zzahj.zzais.zzi(null);
                }
            });
        }
    }

    public boolean disconnect() {
        if (this.zzahk) {
            return false;
        }
        if (this.zzahj.zzagW.zzpG()) {
            this.zzahk = true;
            for (zzx zzpU : this.zzahj.zzagW.zzaia) {
                zzpU.zzpU();
            }
            return false;
        }
        this.zzahj.zzh(null);
        return true;
    }

    public void onConnected(Bundle connectionHint) {
    }

    public void onConnectionSuspended(int cause) {
        this.zzahj.zzh(null);
        this.zzahj.zzais.zzc(cause, this.zzahk);
    }

    public <A extends zzb, R extends Result, T extends zza<R, A>> T zza(T t) {
        return zzb(t);
    }

    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    public <A extends zzb, T extends zza<? extends Result, A>> T zzb(T t) {
        try {
            zza((zze) t);
        } catch (DeadObjectException e) {
            this.zzahj.zza(new zza(this) {
                public void zzpt() {
                    zzg.this.onConnectionSuspended(1);
                }
            });
        }
        return t;
    }

    /* access modifiers changed from: 0000 */
    public void zzps() {
        if (this.zzahk) {
            this.zzahk = false;
            this.zzahj.zzagW.zzaa(false);
            disconnect();
        }
    }
}
