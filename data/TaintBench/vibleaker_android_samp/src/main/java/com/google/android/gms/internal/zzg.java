package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

public class zzg extends Thread {
    private final zzb zzj;
    private final zzn zzk;
    private volatile boolean zzl = false;
    private final BlockingQueue<zzk<?>> zzy;
    private final zzf zzz;

    public zzg(BlockingQueue<zzk<?>> blockingQueue, zzf zzf, zzb zzb, zzn zzn) {
        this.zzy = blockingQueue;
        this.zzz = zzf;
        this.zzj = zzb;
        this.zzk = zzn;
    }

    @TargetApi(14)
    private void zzb(zzk<?> zzk) {
        if (VERSION.SDK_INT >= 14) {
            TrafficStats.setThreadStatsTag(zzk.zzg());
        }
    }

    private void zzb(zzk<?> zzk, zzr zzr) {
        this.zzk.zza((zzk) zzk, zzk.zzb(zzr));
    }

    public void quit() {
        this.zzl = true;
        interrupt();
    }

    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            try {
                zzk zzk = (zzk) this.zzy.take();
                try {
                    zzk.zzc("network-queue-take");
                    if (zzk.isCanceled()) {
                        zzk.zzd("network-discard-cancelled");
                    } else {
                        zzb(zzk);
                        zzi zza = this.zzz.zza(zzk);
                        zzk.zzc("network-http-complete");
                        if (zza.zzB && zzk.zzw()) {
                            zzk.zzd("not-modified");
                        } else {
                            zzm zza2 = zzk.zza(zza);
                            zzk.zzc("network-parse-complete");
                            if (zzk.zzr() && zza2.zzag != null) {
                                this.zzj.zza(zzk.zzh(), zza2.zzag);
                                zzk.zzc("network-cache-written");
                            }
                            zzk.zzv();
                            this.zzk.zza(zzk, zza2);
                        }
                    }
                } catch (zzr e) {
                    e.zza(SystemClock.elapsedRealtime() - elapsedRealtime);
                    zzb(zzk, e);
                } catch (Exception e2) {
                    zzs.zza(e2, "Unhandled exception %s", e2.toString());
                    zzr zzr = new zzr(e2);
                    zzr.zza(SystemClock.elapsedRealtime() - elapsedRealtime);
                    this.zzk.zza(zzk, zzr);
                }
            } catch (InterruptedException e3) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
