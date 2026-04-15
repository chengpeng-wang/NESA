package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.zze;

public abstract class zzg<T> {
    private final String zzavI;
    private T zzavJ;

    public static class zza extends Exception {
        public zza(String str) {
            super(str);
        }

        public zza(String str, Throwable th) {
            super(str, th);
        }
    }

    protected zzg(String str) {
        this.zzavI = str;
    }

    /* access modifiers changed from: protected|final */
    public final T zzaB(Context context) throws zza {
        if (this.zzavJ == null) {
            zzx.zzz(context);
            Context remoteContext = zze.getRemoteContext(context);
            if (remoteContext == null) {
                throw new zza("Could not get remote context.");
            }
            try {
                this.zzavJ = zzd((IBinder) remoteContext.getClassLoader().loadClass(this.zzavI).newInstance());
            } catch (ClassNotFoundException e) {
                throw new zza("Could not load creator class.", e);
            } catch (InstantiationException e2) {
                throw new zza("Could not instantiate creator.", e2);
            } catch (IllegalAccessException e3) {
                throw new zza("Could not access creator.", e3);
            }
        }
        return this.zzavJ;
    }

    public abstract T zzd(IBinder iBinder);
}
