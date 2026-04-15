package com.flurry.sdk;

import java.lang.ref.WeakReference;
import java.util.concurrent.FutureTask;

final class io extends FutureTask {
    private final WeakReference a;

    public io(Runnable runnable, Object obj) {
        super(runnable, obj);
        this.a = new WeakReference(runnable);
    }

    public final Runnable a() {
        return (Runnable) this.a.get();
    }
}
