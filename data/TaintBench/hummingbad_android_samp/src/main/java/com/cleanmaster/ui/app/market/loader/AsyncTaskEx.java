package com.cleanmaster.ui.app.market.loader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AsyncTaskEx {
    private static final ThreadFactory a = new b();
    private static final BlockingQueue b = new LinkedBlockingQueue(10);
    private static final g c = new g();
    private static volatile Executor d = e;
    public static final Executor e = new ThreadPoolExecutor(3, 128, 1, TimeUnit.SECONDS, b, a);
    private final h f = new c(this);
    private final FutureTask g = new d(this, this.f);
    private volatile Status h = Status.PENDING;
    /* access modifiers changed from: private|final */
    public final AtomicBoolean i = new AtomicBoolean();

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    public abstract Object a(Object... objArr);

    /* access modifiers changed from: private */
    public void c(Object obj) {
        if (!this.i.get()) {
            d(obj);
        }
    }

    /* access modifiers changed from: private */
    public Object d(Object obj) {
        c.obtainMessage(1, new f(this, obj)).sendToTarget();
        return obj;
    }

    public final Status c() {
        return this.h;
    }

    /* access modifiers changed from: protected */
    public void d() {
    }

    /* access modifiers changed from: protected */
    public void a(Object obj) {
    }

    /* access modifiers changed from: protected|varargs */
    public void b(Object... objArr) {
    }

    /* access modifiers changed from: protected */
    public void b(Object obj) {
        e();
    }

    /* access modifiers changed from: protected */
    public void e() {
    }

    public final boolean f() {
        return this.g.isCancelled();
    }

    public final boolean a(boolean z) {
        return this.g.cancel(z);
    }

    public final AsyncTaskEx c(Object... objArr) {
        return a(d, objArr);
    }

    public final AsyncTaskEx a(Executor executor, Object... objArr) {
        if (this.h != Status.PENDING) {
            switch (e.a[this.h.ordinal()]) {
                case 1:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case 2:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }
        this.h = Status.RUNNING;
        d();
        this.f.b = objArr;
        executor.execute(this.g);
        return this;
    }

    /* access modifiers changed from: private */
    public void e(Object obj) {
        if (f()) {
            b(obj);
        } else {
            a(obj);
        }
        this.h = Status.FINISHED;
    }
}
