package com.flurry.sdk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ip {
    private static final String a = ip.class.getSimpleName();
    private final ig b = new ig();
    private final HashMap c = new HashMap();
    /* access modifiers changed from: private|final */
    public final HashMap d = new HashMap();
    private final ThreadPoolExecutor e;

    public ip(String str, BlockingQueue blockingQueue) {
        this.e = new iq(this, TimeUnit.MILLISECONDS, blockingQueue);
        this.e.setRejectedExecutionHandler(new it(this));
        this.e.setThreadFactory(new ll(str));
    }

    static /* synthetic */ ma a(Runnable runnable) {
        if (runnable instanceof io) {
            return (ma) ((io) runnable).a();
        }
        if (runnable instanceof ma) {
            return (ma) runnable;
        }
        iw.a(6, a, "Unknown runnable class: " + runnable.getClass().getName());
        return null;
    }

    private synchronized void a(ma maVar) {
        if (maVar != null) {
            Future future;
            synchronized (this.d) {
                future = (Future) this.d.remove(maVar);
            }
            b(maVar);
            if (future != null) {
                future.cancel(true);
            }
            new iv(this, maVar).run();
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void b(ma maVar) {
        c(this.c.get(maVar), maVar);
    }

    private synchronized void b(Object obj, ma maVar) {
        this.b.a(obj, (Object) maVar);
        this.c.put(maVar, obj);
    }

    private synchronized void c(Object obj, ma maVar) {
        this.b.b(obj, maVar);
        this.c.remove(maVar);
    }

    public final synchronized void a(Object obj) {
        if (obj != null) {
            HashSet<ma> hashSet = new HashSet();
            hashSet.addAll(this.b.a(obj));
            for (ma a : hashSet) {
                a(a);
            }
        }
    }

    public final synchronized void a(Object obj, ma maVar) {
        if (obj != null) {
            b(obj, maVar);
            this.e.submit(maVar);
        }
    }

    public final synchronized long b(Object obj) {
        return obj == null ? 0 : (long) this.b.a(obj).size();
    }
}
