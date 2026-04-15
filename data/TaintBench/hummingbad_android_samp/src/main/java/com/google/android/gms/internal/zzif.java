package com.google.android.gms.internal;

import java.util.concurrent.TimeUnit;

@zzgk
public class zzif<T> implements zzih<T> {
    private final T zzID;
    private final zzii zzIF = new zzii();

    public zzif(T t) {
        this.zzID = t;
        this.zzIF.zzgK();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public T get() {
        return this.zzID;
    }

    public T get(long timeout, TimeUnit unit) {
        return this.zzID;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

    public void zzc(Runnable runnable) {
        this.zzIF.zzc(runnable);
    }
}
