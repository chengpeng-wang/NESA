package com.flurry.sdk;

import java.util.concurrent.PriorityBlockingQueue;

public final class hy extends ip {
    private static hy a = null;

    protected hy() {
        super(hy.class.getName(), new PriorityBlockingQueue(11, new in()));
    }

    public static synchronized hy a() {
        hy hyVar;
        synchronized (hy.class) {
            if (a == null) {
                a = new hy();
            }
            hyVar = a;
        }
        return hyVar;
    }
}
