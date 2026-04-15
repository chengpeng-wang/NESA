package com.google.android.gms.internal;

import com.google.android.gms.internal.zzji.zzc;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@zzhb
public class zzjj<T> implements zzji<T> {
    protected int zzBc = 0;
    protected final BlockingQueue<zza> zzNq = new LinkedBlockingQueue();
    protected T zzNr;
    private final Object zzpV = new Object();

    class zza {
        public final zzc<T> zzNs;
        public final com.google.android.gms.internal.zzji.zza zzNt;

        public zza(zzc<T> zzc, com.google.android.gms.internal.zzji.zza zza) {
            this.zzNs = zzc;
            this.zzNt = zza;
        }
    }

    public int getStatus() {
        return this.zzBc;
    }

    public void reject() {
        synchronized (this.zzpV) {
            if (this.zzBc != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzBc = -1;
            for (zza zza : this.zzNq) {
                zza.zzNt.run();
            }
            this.zzNq.clear();
        }
    }

    public void zza(zzc<T> zzc, com.google.android.gms.internal.zzji.zza zza) {
        synchronized (this.zzpV) {
            if (this.zzBc == 1) {
                zzc.zze(this.zzNr);
            } else if (this.zzBc == -1) {
                zza.run();
            } else if (this.zzBc == 0) {
                this.zzNq.add(new zza(zzc, zza));
            }
        }
    }

    public void zzh(T t) {
        synchronized (this.zzpV) {
            if (this.zzBc != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzNr = t;
            this.zzBc = 1;
            for (zza zza : this.zzNq) {
                zza.zzNs.zze(t);
            }
            this.zzNq.clear();
        }
    }
}
