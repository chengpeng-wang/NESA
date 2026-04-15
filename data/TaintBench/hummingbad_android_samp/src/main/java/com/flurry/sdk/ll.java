package com.flurry.sdk;

import android.support.annotation.NonNull;
import java.util.concurrent.ThreadFactory;

public final class ll implements ThreadFactory {
    private final ThreadGroup a;
    private final int b = 1;

    public ll(String str) {
        this.a = new ThreadGroup(str);
    }

    public final Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(this.a, runnable);
        thread.setName(this.a.getName() + ":" + thread.getId());
        thread.setPriority(this.b);
        return thread;
    }
}
