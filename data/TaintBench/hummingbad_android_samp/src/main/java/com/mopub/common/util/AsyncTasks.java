package com.mopub.common.util;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTasks {
    private static Executor sExecutor;

    static {
        init();
    }

    @TargetApi(11)
    private static void init() {
        if (VERSION.SDK_INT >= 11) {
            sExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        } else {
            sExecutor = Executors.newSingleThreadExecutor();
        }
    }

    @VisibleForTesting
    public static void setExecutor(Executor executor) {
        sExecutor = executor;
    }

    @TargetApi(11)
    public static <P> void safeExecuteOnExecutor(AsyncTask<P, ?, ?> asyncTask, P... params) {
        Preconditions.checkNotNull(asyncTask, "Unable to execute null AsyncTask.");
        Preconditions.checkUiThread("AsyncTask must be executed on the main thread");
        if (VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(sExecutor, params);
        } else {
            asyncTask.execute(params);
        }
    }
}
