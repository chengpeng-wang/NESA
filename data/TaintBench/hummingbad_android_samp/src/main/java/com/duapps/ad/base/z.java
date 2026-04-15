package com.duapps.ad.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: ToolboxThreadPool */
final class z implements ThreadFactory {
    private final AtomicInteger a = new AtomicInteger(1);

    z() {
    }

    public final Thread newThread(Runnable runnable) {
        return new Thread(runnable, "TooboxThread #" + this.a.getAndIncrement());
    }
}
