package com.flurry.sdk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class iq extends ThreadPoolExecutor {
    final /* synthetic */ ip a;

    iq(ip ipVar, TimeUnit timeUnit, BlockingQueue blockingQueue) {
        this.a = ipVar;
        super(0, 5, 5000, timeUnit, blockingQueue);
    }

    /* access modifiers changed from: protected|final */
    public final void afterExecute(Runnable runnable, Throwable th) {
        super.afterExecute(runnable, th);
        ma a = ip.a(runnable);
        if (a != null) {
            synchronized (this.a.d) {
                this.a.d.remove(a);
            }
            this.a.b(a);
            new is(this, a).run();
        }
    }

    /* access modifiers changed from: protected|final */
    public final void beforeExecute(Thread thread, Runnable runnable) {
        super.beforeExecute(thread, runnable);
        ma a = ip.a(runnable);
        if (a != null) {
            new ir(this, a).run();
        }
    }

    /* access modifiers changed from: protected|final */
    public final RunnableFuture newTaskFor(Runnable runnable, Object obj) {
        io ioVar = new io(runnable, obj);
        synchronized (this.a.d) {
            this.a.d.put((ma) runnable, ioVar);
        }
        return ioVar;
    }

    /* access modifiers changed from: protected|final */
    public final RunnableFuture newTaskFor(Callable callable) {
        throw new UnsupportedOperationException("Callable not supported");
    }
}
