package com.cleanmaster.ui.app.market.loader;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: AsyncTaskEx */
final class b implements ThreadFactory {
    private final AtomicInteger a = new AtomicInteger(1);

    b() {
    }

    public final Thread newThread(Runnable r) {
        return new Thread(r, "AsyncTask #" + this.a.getAndIncrement());
    }
}
