package android.support.v4.os;

import android.os.Build.VERSION;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    /* JADX WARNING: Missing block: B:7:0x0012, code skipped:
            if (r0 == null) goto L_0x0017;
     */
    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r0.onCancel();
     */
    /* JADX WARNING: Missing block: B:10:0x0017, code skipped:
            if (r1 == null) goto L_0x001c;
     */
    /* JADX WARNING: Missing block: B:11:0x0019, code skipped:
            android.support.v4.os.CancellationSignalCompatJellybean.cancel(r1);
     */
    /* JADX WARNING: Missing block: B:12:0x001c, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:15:?, code skipped:
            r4.mCancelInProgress = false;
            notifyAll();
     */
    /* JADX WARNING: Missing block: B:16:0x0023, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:26:0x002c, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:29:?, code skipped:
            r4.mCancelInProgress = false;
            notifyAll();
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            return;
     */
    public void cancel() {
        /*
        r4 = this;
        monitor-enter(r4);
        r2 = r4.mIsCanceled;	 Catch:{ all -> 0x0028 }
        if (r2 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0028 }
    L_0x0006:
        return;
    L_0x0007:
        r2 = 1;
        r4.mIsCanceled = r2;	 Catch:{ all -> 0x0028 }
        r2 = 1;
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x0028 }
        r0 = r4.mOnCancelListener;	 Catch:{ all -> 0x0028 }
        r1 = r4.mCancellationSignalObj;	 Catch:{ all -> 0x0028 }
        monitor-exit(r4);	 Catch:{ all -> 0x0028 }
        if (r0 == 0) goto L_0x0017;
    L_0x0014:
        r0.onCancel();	 Catch:{ all -> 0x002b }
    L_0x0017:
        if (r1 == 0) goto L_0x001c;
    L_0x0019:
        android.support.v4.os.CancellationSignalCompatJellybean.cancel(r1);	 Catch:{ all -> 0x002b }
    L_0x001c:
        monitor-enter(r4);
        r2 = 0;
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x0025 }
        r4.notifyAll();	 Catch:{ all -> 0x0025 }
        monitor-exit(r4);	 Catch:{ all -> 0x0025 }
        goto L_0x0006;
    L_0x0025:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0025 }
        throw r2;
    L_0x0028:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0028 }
        throw r2;
    L_0x002b:
        r2 = move-exception;
        monitor-enter(r4);
        r3 = 0;
        r4.mCancelInProgress = r3;	 Catch:{ all -> 0x0035 }
        r4.notifyAll();	 Catch:{ all -> 0x0035 }
        monitor-exit(r4);	 Catch:{ all -> 0x0035 }
        throw r2;
    L_0x0035:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0035 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.CancellationSignal.cancel():void");
    }

    /* JADX WARNING: Missing block: B:17:?, code skipped:
            return;
     */
    public void setOnCancelListener(android.support.v4.os.CancellationSignal.OnCancelListener r2) {
        /*
        r1 = this;
        monitor-enter(r1);
        r1.waitForCancelFinishedLocked();	 Catch:{ all -> 0x0014 }
        r0 = r1.mOnCancelListener;	 Catch:{ all -> 0x0014 }
        if (r0 != r2) goto L_0x000a;
    L_0x0008:
        monitor-exit(r1);	 Catch:{ all -> 0x0014 }
    L_0x0009:
        return;
    L_0x000a:
        r1.mOnCancelListener = r2;	 Catch:{ all -> 0x0014 }
        r0 = r1.mIsCanceled;	 Catch:{ all -> 0x0014 }
        if (r0 == 0) goto L_0x0012;
    L_0x0010:
        if (r2 != 0) goto L_0x0017;
    L_0x0012:
        monitor-exit(r1);	 Catch:{ all -> 0x0014 }
        goto L_0x0009;
    L_0x0014:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0014 }
        throw r0;
    L_0x0017:
        monitor-exit(r1);	 Catch:{ all -> 0x0014 }
        r2.onCancel();
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.CancellationSignal.setOnCancelListener(android.support.v4.os.CancellationSignal$OnCancelListener):void");
    }

    public Object getCancellationSignalObject() {
        if (VERSION.SDK_INT < 16) {
            return null;
        }
        Object obj;
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = CancellationSignalCompatJellybean.create();
                if (this.mIsCanceled) {
                    CancellationSignalCompatJellybean.cancel(this.mCancellationSignalObj);
                }
            }
            obj = this.mCancellationSignalObj;
        }
        return obj;
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
