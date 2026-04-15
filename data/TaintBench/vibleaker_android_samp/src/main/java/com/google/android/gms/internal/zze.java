package com.google.android.gms.internal;

import android.os.Handler;
import java.util.concurrent.Executor;

public class zze implements zzn {
    private final Executor zzs;

    private class zza implements Runnable {
        private final zzk zzv;
        private final zzm zzw;
        private final Runnable zzx;

        public zza(zzk zzk, zzm zzm, Runnable runnable) {
            this.zzv = zzk;
            this.zzw = zzm;
            this.zzx = runnable;
        }

        public void run() {
            if (this.zzv.isCanceled()) {
                this.zzv.zzd("canceled-at-delivery");
                return;
            }
            if (this.zzw.isSuccess()) {
                this.zzv.zza(this.zzw.result);
            } else {
                this.zzv.zzc(this.zzw.zzah);
            }
            if (this.zzw.zzai) {
                this.zzv.zzc("intermediate-response");
            } else {
                this.zzv.zzd("done");
            }
            if (this.zzx != null) {
                this.zzx.run();
            }
        }
    }

    public zze(final Handler handler) {
        this.zzs = new Executor() {
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public void zza(zzk<?> zzk, zzm<?> zzm) {
        zza(zzk, zzm, null);
    }

    public void zza(zzk<?> zzk, zzm<?> zzm, Runnable runnable) {
        zzk.zzv();
        zzk.zzc("post-response");
        this.zzs.execute(new zza(zzk, zzm, runnable));
    }

    public void zza(zzk<?> zzk, zzr zzr) {
        zzk.zzc("post-error");
        this.zzs.execute(new zza(zzk, zzm.zzd(zzr), null));
    }
}
