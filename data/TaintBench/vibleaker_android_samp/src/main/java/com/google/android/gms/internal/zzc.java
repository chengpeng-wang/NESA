package com.google.android.gms.internal;

import android.os.Process;
import com.google.android.gms.internal.zzb.zza;
import java.util.concurrent.BlockingQueue;

public class zzc extends Thread {
    private static final boolean DEBUG = zzs.DEBUG;
    private final BlockingQueue<zzk<?>> zzh;
    /* access modifiers changed from: private|final */
    public final BlockingQueue<zzk<?>> zzi;
    private final zzb zzj;
    private final zzn zzk;
    private volatile boolean zzl = false;

    public zzc(BlockingQueue<zzk<?>> blockingQueue, BlockingQueue<zzk<?>> blockingQueue2, zzb zzb, zzn zzn) {
        this.zzh = blockingQueue;
        this.zzi = blockingQueue2;
        this.zzj = zzb;
        this.zzk = zzn;
    }

    public void quit() {
        this.zzl = true;
        interrupt();
    }

    public void run() {
        if (DEBUG) {
            zzs.zza("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.zzj.zza();
        while (true) {
            try {
                final zzk zzk = (zzk) this.zzh.take();
                zzk.zzc("cache-queue-take");
                if (zzk.isCanceled()) {
                    zzk.zzd("cache-discard-canceled");
                } else {
                    zza zza = this.zzj.zza(zzk.zzh());
                    if (zza == null) {
                        zzk.zzc("cache-miss");
                        this.zzi.put(zzk);
                    } else if (zza.zzb()) {
                        zzk.zzc("cache-hit-expired");
                        zzk.zza(zza);
                        this.zzi.put(zzk);
                    } else {
                        zzk.zzc("cache-hit");
                        zzm zza2 = zzk.zza(new zzi(zza.data, zza.zzg));
                        zzk.zzc("cache-hit-parsed");
                        if (zza.zzc()) {
                            zzk.zzc("cache-hit-refresh-needed");
                            zzk.zza(zza);
                            zza2.zzai = true;
                            this.zzk.zza(zzk, zza2, new Runnable() {
                                public void run() {
                                    try {
                                        zzc.this.zzi.put(zzk);
                                    } catch (InterruptedException e) {
                                    }
                                }
                            });
                        } else {
                            this.zzk.zza(zzk, zza2);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
