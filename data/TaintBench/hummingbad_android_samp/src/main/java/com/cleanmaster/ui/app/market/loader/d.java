package com.cleanmaster.ui.app.market.loader;

import android.util.Log;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* compiled from: AsyncTaskEx */
class d extends FutureTask {
    final /* synthetic */ AsyncTaskEx a;

    d(AsyncTaskEx asyncTaskEx, Callable callable) {
        this.a = asyncTaskEx;
        super(callable);
    }

    /* access modifiers changed from: protected */
    public void done() {
        try {
            this.a.c(get());
        } catch (InterruptedException e) {
            Log.w("AsyncTask", e);
        } catch (ExecutionException e2) {
            throw new RuntimeException("An error occured while executing doInBackground()", e2.getCause());
        } catch (CancellationException e3) {
            this.a.c(null);
        } catch (Throwable th) {
            RuntimeException runtimeException = new RuntimeException("An error occured while executing doInBackground()", th);
        }
    }
}
