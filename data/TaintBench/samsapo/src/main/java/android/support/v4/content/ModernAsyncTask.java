package android.support.v4.content;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor = THREAD_POOL_EXECUTOR;
    private static final InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus = Status.PENDING;
    /* access modifiers changed from: private|final */
    public final AtomicBoolean mTaskInvoked;
    private final WorkerRunnable<Params, Result> mWorker;

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        private WorkerRunnable() {
        }

        /* synthetic */ WorkerRunnable(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }
    }

    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ModernAsyncTask mTask;

        AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data... dataArr) {
            Data[] dataArr2 = dataArr;
            this.mTask = modernAsyncTask;
            this.mData = dataArr2;
        }
    }

    private static class InternalHandler extends Handler {
        private InternalHandler() {
        }

        /* synthetic */ InternalHandler(AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this();
        }

        public void handleMessage(Message message) {
            Message message2 = message;
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult) message2.obj;
            switch (message2.what) {
                case 1:
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
                    return;
                case 2:
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                    return;
                default:
                    return;
            }
        }
    }

    public enum Status {
    }

    public abstract Result doInBackground(Params... paramsArr);

    static {
        AnonymousClass1 anonymousClass1 = r9;
        AnonymousClass1 anonymousClass12 = new ThreadFactory() {
            private final AtomicInteger mCount;

            {
                AtomicInteger atomicInteger = r5;
                AtomicInteger atomicInteger2 = new AtomicInteger(1);
                this.mCount = atomicInteger;
            }

            public Thread newThread(Runnable runnable) {
                Thread thread = r7;
                Runnable runnable2 = runnable;
                StringBuilder stringBuilder = r7;
                StringBuilder stringBuilder2 = new StringBuilder();
                Thread thread2 = new Thread(runnable2, stringBuilder.append("ModernAsyncTask #").append(this.mCount.getAndIncrement()).toString());
                return thread;
            }
        };
        sThreadFactory = anonymousClass1;
        LinkedBlockingQueue linkedBlockingQueue = r9;
        LinkedBlockingQueue linkedBlockingQueue2 = new LinkedBlockingQueue(10);
        sPoolWorkQueue = linkedBlockingQueue;
        ThreadPoolExecutor threadPoolExecutor = r9;
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
        InternalHandler internalHandler = r9;
        InternalHandler internalHandler2 = new InternalHandler();
        sHandler = internalHandler;
    }

    public static void init() {
        Looper looper = sHandler.getLooper();
    }

    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    public ModernAsyncTask() {
        AtomicBoolean atomicBoolean = r6;
        AtomicBoolean atomicBoolean2 = new AtomicBoolean();
        this.mTaskInvoked = atomicBoolean;
        WorkerRunnable workerRunnable = r6;
        WorkerRunnable anonymousClass2 = new WorkerRunnable<Params, Result>(this) {
            final /* synthetic */ ModernAsyncTask this$0;

            {
                this.this$0 = r5;
            }

            public Result call() throws Exception {
                this.this$0.mTaskInvoked.set(true);
                Process.setThreadPriority(10);
                return this.this$0.postResult(this.this$0.doInBackground(this.mParams));
            }
        };
        this.mWorker = workerRunnable;
        FutureTask futureTask = r6;
        FutureTask anonymousClass3 = new FutureTask<Result>(this, this.mWorker) {
            final /* synthetic */ ModernAsyncTask this$0;

            /* access modifiers changed from: protected */
            public void done() {
                RuntimeException runtimeException;
                RuntimeException runtimeException2;
                try {
                    this.this$0.postResultIfNotInvoked(get());
                } catch (InterruptedException e) {
                    int w = Log.w(ModernAsyncTask.LOG_TAG, e);
                } catch (ExecutionException e2) {
                    ExecutionException executionException = e2;
                    runtimeException = r6;
                    runtimeException2 = new RuntimeException("An error occured while executing doInBackground()", executionException.getCause());
                    throw runtimeException;
                } catch (CancellationException e3) {
                    CancellationException cancellationException = e3;
                    this.this$0.postResultIfNotInvoked(null);
                } catch (Throwable e4) {
                    Throwable th = e4;
                    runtimeException = r6;
                    runtimeException2 = new RuntimeException("An error occured while executing doInBackground()", th);
                }
            }
        };
        this.mFuture = futureTask;
    }

    /* access modifiers changed from: private */
    public void postResultIfNotInvoked(Result result) {
        Result result2 = result;
        if (!this.mTaskInvoked.get()) {
            Object postResult = postResult(result2);
        }
    }

    /* access modifiers changed from: private */
    public Result postResult(Result result) {
        Result result2 = result;
        InternalHandler internalHandler = sHandler;
        AsyncTaskResult asyncTaskResult = r12;
        Object[] objArr = new Object[1];
        Object[] objArr2 = objArr;
        objArr[0] = result2;
        AsyncTaskResult asyncTaskResult2 = new AsyncTaskResult(this, objArr2);
        internalHandler.obtainMessage(1, asyncTaskResult).sendToTarget();
        return result2;
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
    }

    /* access modifiers changed from: protected|varargs */
    public void onProgressUpdate(Progress... progressArr) {
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
        Result result2 = result;
        onCancelled();
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
    }

    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }

    public final boolean cancel(boolean z) {
        return this.mFuture.cancel(z);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(j, timeUnit);
    }

    public final ModernAsyncTask<Params, Progress, Result> execute(Params... paramsArr) {
        return executeOnExecutor(sDefaultExecutor, paramsArr);
    }

    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params... paramsArr) {
        Executor executor2 = executor;
        Params[] paramsArr2 = paramsArr;
        if (this.mStatus != Status.PENDING) {
            IllegalStateException illegalStateException;
            IllegalStateException illegalStateException2;
            switch (this.mStatus) {
                case RUNNING:
                    illegalStateException = r6;
                    illegalStateException2 = new IllegalStateException("Cannot execute task: the task is already running.");
                    throw illegalStateException;
                case FINISHED:
                    illegalStateException = r6;
                    illegalStateException2 = new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                    throw illegalStateException;
            }
        }
        this.mStatus = Status.RUNNING;
        onPreExecute();
        this.mWorker.mParams = paramsArr2;
        executor2.execute(this.mFuture);
        return this;
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    /* access modifiers changed from: protected|final|varargs */
    public final void publishProgress(Progress... progressArr) {
        Progress[] progressArr2 = progressArr;
        if (!isCancelled()) {
            InternalHandler internalHandler = sHandler;
            AsyncTaskResult asyncTaskResult = r8;
            AsyncTaskResult asyncTaskResult2 = new AsyncTaskResult(this, progressArr2);
            internalHandler.obtainMessage(2, asyncTaskResult).sendToTarget();
        }
    }

    /* access modifiers changed from: private */
    public void finish(Result result) {
        Result result2 = result;
        if (isCancelled()) {
            onCancelled(result2);
        } else {
            onPostExecute(result2);
        }
        this.mStatus = Status.FINISHED;
    }
}
