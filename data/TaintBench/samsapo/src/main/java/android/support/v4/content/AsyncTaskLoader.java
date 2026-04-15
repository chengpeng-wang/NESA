package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

public abstract class AsyncTaskLoader<D> extends Loader<D> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncTaskLoader";
    volatile LoadTask mCancellingTask;
    Handler mHandler;
    long mLastLoadCompleteTime = -10000;
    volatile LoadTask mTask;
    long mUpdateThrottle;

    final class LoadTask extends ModernAsyncTask<Void, Void, D> implements Runnable {
        /* access modifiers changed from: private */
        public CountDownLatch done;
        D result;
        final /* synthetic */ AsyncTaskLoader this$0;
        boolean waiting;

        LoadTask(AsyncTaskLoader asyncTaskLoader) {
            this.this$0 = asyncTaskLoader;
            CountDownLatch countDownLatch = r6;
            CountDownLatch countDownLatch2 = new CountDownLatch(1);
            this.done = countDownLatch;
        }

        /* access modifiers changed from: protected|varargs */
        public D doInBackground(Void... voidArr) {
            Void[] voidArr2 = voidArr;
            this.result = this.this$0.onLoadInBackground();
            return this.result;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(D d) {
            try {
                this.this$0.dispatchOnLoadComplete(this, d);
                this.done.countDown();
            } catch (Throwable th) {
                Throwable th2 = th;
                this.done.countDown();
                Throwable th3 = th2;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            try {
                this.this$0.dispatchOnCancelled(this, this.result);
                this.done.countDown();
            } catch (Throwable th) {
                Throwable th2 = th;
                this.done.countDown();
                Throwable th3 = th2;
            }
        }

        public void run() {
            this.waiting = AsyncTaskLoader.DEBUG;
            this.this$0.executePendingTask();
        }
    }

    public abstract D loadInBackground();

    public AsyncTaskLoader(Context context) {
        super(context);
    }

    public void setUpdateThrottle(long j) {
        long j2 = j;
        this.mUpdateThrottle = j2;
        if (j2 != 0) {
            Handler handler = r7;
            Handler handler2 = new Handler();
            this.mHandler = handler;
        }
    }

    /* access modifiers changed from: protected */
    public void onForceLoad() {
        super.onForceLoad();
        boolean cancelLoad = cancelLoad();
        LoadTask loadTask = r5;
        LoadTask loadTask2 = new LoadTask(this);
        this.mTask = loadTask;
        executePendingTask();
    }

    public boolean cancelLoad() {
        if (this.mTask == null) {
            return DEBUG;
        }
        if (this.mCancellingTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = DEBUG;
                this.mHandler.removeCallbacks(this.mTask);
            }
            this.mTask = null;
            return DEBUG;
        } else if (this.mTask.waiting) {
            this.mTask.waiting = DEBUG;
            this.mHandler.removeCallbacks(this.mTask);
            this.mTask = null;
            return DEBUG;
        } else {
            boolean cancel = this.mTask.cancel(DEBUG);
            if (cancel) {
                this.mCancellingTask = this.mTask;
            }
            this.mTask = null;
            return cancel;
        }
    }

    public void onCanceled(D d) {
    }

    /* access modifiers changed from: 0000 */
    public void executePendingTask() {
        if (this.mCancellingTask == null && this.mTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = DEBUG;
                this.mHandler.removeCallbacks(this.mTask);
            }
            if (this.mUpdateThrottle <= 0 || SystemClock.uptimeMillis() >= this.mLastLoadCompleteTime + this.mUpdateThrottle) {
                ModernAsyncTask executeOnExecutor = this.mTask.executeOnExecutor(ModernAsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
                return;
            }
            this.mTask.waiting = true;
            boolean postAtTime = this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnCancelled(LoadTask loadTask, D d) {
        LoadTask loadTask2 = loadTask;
        onCanceled(d);
        if (this.mCancellingTask == loadTask2) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            executePendingTask();
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnLoadComplete(LoadTask loadTask, D d) {
        LoadTask loadTask2 = loadTask;
        D d2 = d;
        if (this.mTask != loadTask2) {
            dispatchOnCancelled(loadTask2, d2);
        } else if (isAbandoned()) {
            onCanceled(d2);
        } else {
            commitContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mTask = null;
            deliverResult(d2);
        }
    }

    /* access modifiers changed from: protected */
    public D onLoadInBackground() {
        return loadInBackground();
    }

    public void waitForLoader() {
        LoadTask loadTask = this.mTask;
        if (loadTask != null) {
            try {
                loadTask.done.await();
            } catch (InterruptedException e) {
                InterruptedException interruptedException = e;
            }
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str;
        PrintWriter printWriter2 = printWriter;
        super.dump(str2, fileDescriptor, printWriter2, strArr);
        if (this.mTask != null) {
            printWriter2.print(str2);
            printWriter2.print("mTask=");
            printWriter2.print(this.mTask);
            printWriter2.print(" waiting=");
            printWriter2.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter2.print(str2);
            printWriter2.print("mCancellingTask=");
            printWriter2.print(this.mCancellingTask);
            printWriter2.print(" waiting=");
            printWriter2.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            printWriter2.print(str2);
            printWriter2.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, printWriter2);
            printWriter2.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), printWriter2);
            printWriter2.println();
        }
    }
}
