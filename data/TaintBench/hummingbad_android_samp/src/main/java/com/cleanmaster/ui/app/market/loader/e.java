package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.loader.AsyncTaskEx.Status;

/* compiled from: AsyncTaskEx */
/* synthetic */ class e {
    static final /* synthetic */ int[] a = new int[Status.values().length];

    static {
        try {
            a[Status.RUNNING.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[Status.FINISHED.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
    }
}
