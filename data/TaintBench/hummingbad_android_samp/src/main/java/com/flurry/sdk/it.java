package com.flurry.sdk;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

final class it extends DiscardPolicy {
    final /* synthetic */ ip a;

    it(ip ipVar) {
        this.a = ipVar;
    }

    public final void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        super.rejectedExecution(runnable, threadPoolExecutor);
        ma a = ip.a(runnable);
        if (a != null) {
            synchronized (this.a.d) {
                this.a.d.remove(a);
            }
            this.a.b(a);
            new iu(this, a).run();
        }
    }
}
