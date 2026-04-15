package com.google.android.gms.internal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class zzig {

    public interface zza<D, R> {
        R zze(D d);
    }

    public static <A, B> zzih<B> zza(final zzih<A> zzih, final zza<A, B> zza) {
        final zzie zzie = new zzie();
        zzih.zzc(new Runnable() {
            public void run() {
                try {
                    zzie.zzf(zza.zze(zzih.get()));
                } catch (InterruptedException | CancellationException | ExecutionException e) {
                    zzie.cancel(true);
                }
            }
        });
        return zzie;
    }
}
