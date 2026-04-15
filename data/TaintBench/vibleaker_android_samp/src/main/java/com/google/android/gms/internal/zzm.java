package com.google.android.gms.internal;

public class zzm<T> {
    public final T result;
    public final com.google.android.gms.internal.zzb.zza zzag;
    public final zzr zzah;
    public boolean zzai;

    public interface zza {
        void zze(zzr zzr);
    }

    public interface zzb<T> {
        void zzb(T t);
    }

    private zzm(zzr zzr) {
        this.zzai = false;
        this.result = null;
        this.zzag = null;
        this.zzah = zzr;
    }

    private zzm(T t, com.google.android.gms.internal.zzb.zza zza) {
        this.zzai = false;
        this.result = t;
        this.zzag = zza;
        this.zzah = null;
    }

    public static <T> zzm<T> zza(T t, com.google.android.gms.internal.zzb.zza zza) {
        return new zzm(t, zza);
    }

    public static <T> zzm<T> zzd(zzr zzr) {
        return new zzm(zzr);
    }

    public boolean isSuccess() {
        return this.zzah == null;
    }
}
