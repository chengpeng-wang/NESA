package com.tencent.bugly.proguard;

import android.util.Log;
import com.tencent.bugly.crashreport.CrashReport;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: BUGLY */
public class y {
    static y a;
    ScheduledExecutorService b = null;
    ThreadPoolExecutor c = null;

    protected y() {
        AnonymousClass1 anonymousClass1 = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("BUGLY_THREAD");
                return thread;
            }
        };
        AnonymousClass2 anonymousClass2 = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("BUGLY_QUEUE_THREAD");
                return thread;
            }
        };
        this.b = Executors.newScheduledThreadPool(3, anonymousClass1);
        this.c = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue(100));
        this.c.setThreadFactory(anonymousClass2);
        if (this.b == null || this.b.isShutdown()) {
            throw new IllegalArgumentException("ScheduledExecutorService is not valiable!");
        } else if (this.c == null || this.c.isShutdown()) {
            throw new IllegalArgumentException("queueExecutorService is not valiable!");
        }
    }

    public static synchronized y a() {
        y yVar;
        synchronized (y.class) {
            if (a == null) {
                a = new y();
            }
            yVar = a;
        }
        return yVar;
    }

    public synchronized boolean a(Runnable runnable) {
        boolean z = false;
        synchronized (this) {
            if (b()) {
                if (runnable != null) {
                    try {
                        this.c.submit(runnable);
                    } catch (Throwable th) {
                        if (CrashReport.isDebug) {
                            th.printStackTrace();
                        }
                    }
                    z = true;
                } else if (CrashReport.isDebug) {
                    Log.w("CrashReport", "queue task is null");
                }
            } else if (CrashReport.isDebug) {
                Log.w("CrashReport", "queue handler was closed , should not post task!");
            }
        }
        return z;
    }

    public synchronized boolean a(Runnable runnable, long j) {
        boolean z = false;
        synchronized (this) {
            if (!b()) {
                z.d("async handler was closed , should not post task!", new Object[0]);
            } else if (runnable == null) {
                z.d("async task == null", new Object[0]);
            } else {
                if (j <= 0) {
                    j = 0;
                }
                z.c("delay %d task %s", Long.valueOf(j), runnable.getClass().getName());
                this.b.schedule(runnable, j, TimeUnit.MILLISECONDS);
                z = true;
            }
        }
        return z;
    }

    public synchronized boolean b(Runnable runnable) {
        boolean z = false;
        synchronized (this) {
            if (!b()) {
                z.d("async handler was closed , should not post task!", new Object[0]);
            } else if (runnable == null) {
                z.d("async task == null", new Object[0]);
            } else {
                z.c("normal task %s", runnable.getClass().getName());
                this.b.execute(runnable);
                z = true;
            }
        }
        return z;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized boolean b() {
        boolean z;
        z = (this.b == null || this.b.isShutdown() || this.c == null || this.c.isShutdown()) ? false : true;
        return z;
    }
}
