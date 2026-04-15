package com.duapps.ad.base;

import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: ToolboxThreadPool */
public final class y {
    private static final ThreadFactory a = new z();
    private static final BlockingQueue<Runnable> b = new LinkedBlockingQueue(CtaButton.WIDTH_DIPS);
    private static y c;
    private ThreadPoolExecutor d = new ThreadPoolExecutor(5, 50, 1, TimeUnit.SECONDS, b, a);

    private y() {
    }

    public static synchronized y a() {
        y yVar;
        synchronized (y.class) {
            if (c == null) {
                c = new y();
            }
            yVar = c;
        }
        return yVar;
    }

    public final void a(Runnable runnable) {
        this.d.execute(runnable);
    }

    public static void b(Runnable runnable) {
        a().a(runnable);
    }
}
