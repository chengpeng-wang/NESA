package com.cleanmaster.ui.app.market.loader;

import android.os.Process;

/* compiled from: AsyncTaskEx */
class c extends h {
    final /* synthetic */ AsyncTaskEx a;

    c(AsyncTaskEx asyncTaskEx) {
        this.a = asyncTaskEx;
        super();
    }

    public Object call() {
        this.a.i.set(true);
        Process.setThreadPriority(10);
        return this.a.d(this.a.a(this.b));
    }
}
