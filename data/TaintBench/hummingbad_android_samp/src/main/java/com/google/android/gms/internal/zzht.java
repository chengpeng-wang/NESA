package com.google.android.gms.internal;

import android.os.Process;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@zzgk
public final class zzht {
    private static final ExecutorService zzHy = Executors.newFixedThreadPool(10, zzav("Default"));
    private static final ExecutorService zzHz = Executors.newFixedThreadPool(5, zzav("Loader"));

    public static zzih<Void> zza(int i, final Runnable runnable) {
        return i == 1 ? zza(zzHz, new Callable<Void>() {
            /* renamed from: zzgp */
            public Void call() {
                runnable.run();
                return null;
            }
        }) : zza(zzHy, new Callable<Void>() {
            /* renamed from: zzgp */
            public Void call() {
                runnable.run();
                return null;
            }
        });
    }

    public static zzih<Void> zza(Runnable runnable) {
        return zza(0, runnable);
    }

    public static <T> zzih<T> zza(Callable<T> callable) {
        return zza(zzHy, (Callable) callable);
    }

    public static <T> zzih<T> zza(ExecutorService executorService, final Callable<T> callable) {
        final zzie zzie = new zzie();
        try {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        Process.setThreadPriority(10);
                        zzie.zzf(callable.call());
                    } catch (Exception e) {
                        zzp.zzbA().zzc(e, true);
                        zzie.cancel(true);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            zzb.zzd("Thread execution is rejected.", e);
            zzie.cancel(true);
        }
        return zzie;
    }

    private static ThreadFactory zzav(final String str) {
        return new ThreadFactory() {
            private final AtomicInteger zzHC = new AtomicInteger(1);

            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AdWorker(" + str + ") #" + this.zzHC.getAndIncrement());
            }
        };
    }
}
