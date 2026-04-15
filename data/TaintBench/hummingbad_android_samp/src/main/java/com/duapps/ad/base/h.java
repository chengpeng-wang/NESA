package com.duapps.ad.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: ParseExecutor */
final class h implements ThreadFactory {
    private final AtomicInteger a = new AtomicInteger(1);

    h() {
    }

    public final Thread newThread(Runnable runnable) {
        return new Thread(runnable, "parse #" + this.a.getAndIncrement());
    }
}
