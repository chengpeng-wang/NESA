package com.baidu.android.pushservice.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class d implements ThreadFactory {
    private final ThreadFactory a;
    private final String b;
    private final AtomicInteger c;

    public d(String str) {
        this(str, Executors.defaultThreadFactory());
    }

    public d(String str, ThreadFactory threadFactory) {
        this.c = new AtomicInteger(0);
        this.b = str;
        this.a = threadFactory;
    }

    private String a(int i) {
        return String.format("%s-%d", new Object[]{this.b, Integer.valueOf(i)});
    }

    public Thread newThread(Runnable runnable) {
        Thread newThread = this.a.newThread(runnable);
        newThread.setName(a(this.c.getAndIncrement()));
        return newThread;
    }
}
