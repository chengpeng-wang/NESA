package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static final SparseArray<WakeLock> mActiveWakeLocks;
    private static int mNextId = 1;

    public WakefulBroadcastReceiver() {
    }

    static {
        SparseArray sparseArray = r2;
        SparseArray sparseArray2 = new SparseArray();
        mActiveWakeLocks = sparseArray;
    }

    public static ComponentName startWakefulService(Context context, Intent intent) {
        Context context2 = context;
        Intent intent2 = intent;
        SparseArray sparseArray = mActiveWakeLocks;
        SparseArray sparseArray2 = sparseArray;
        synchronized (sparseArray) {
            try {
                int i = mNextId;
                mNextId++;
                if (mNextId <= 0) {
                    mNextId = 1;
                }
                Intent putExtra = intent2.putExtra(EXTRA_WAKE_LOCK_ID, i);
                ComponentName startService = context2.startService(intent2);
                if (startService == null) {
                    return null;
                }
                PowerManager powerManager = (PowerManager) context2.getSystemService("power");
                StringBuilder stringBuilder = r12;
                StringBuilder stringBuilder2 = new StringBuilder();
                WakeLock newWakeLock = powerManager.newWakeLock(1, stringBuilder.append("wake:").append(startService.flattenToShortString()).toString());
                newWakeLock.setReferenceCounted(false);
                newWakeLock.acquire(60000);
                mActiveWakeLocks.put(i, newWakeLock);
                ComponentName componentName = startService;
                return componentName;
            } catch (Throwable th) {
                Throwable th2 = th;
                SparseArray sparseArray3 = sparseArray2;
                Throwable th3 = th2;
            }
        }
    }

    /* JADX WARNING: Missing block: B:16:0x0059, code skipped:
            r5 = r4;
     */
    public static boolean completeWakefulIntent(android.content.Intent r9) {
        /*
        r0 = r9;
        r5 = r0;
        r6 = "android.support.content.wakelockid";
        r7 = 0;
        r5 = r5.getIntExtra(r6, r7);
        r1 = r5;
        r5 = r1;
        if (r5 != 0) goto L_0x0010;
    L_0x000d:
        r5 = 0;
        r0 = r5;
    L_0x000f:
        return r0;
    L_0x0010:
        r5 = mActiveWakeLocks;
        r8 = r5;
        r5 = r8;
        r6 = r8;
        r2 = r6;
        monitor-enter(r5);
        r5 = mActiveWakeLocks;	 Catch:{ all -> 0x0055 }
        r6 = r1;
        r5 = r5.get(r6);	 Catch:{ all -> 0x0055 }
        r5 = (android.os.PowerManager.WakeLock) r5;	 Catch:{ all -> 0x0055 }
        r3 = r5;
        r5 = r3;
        if (r5 == 0) goto L_0x0033;
    L_0x0024:
        r5 = r3;
        r5.release();	 Catch:{ all -> 0x0055 }
        r5 = mActiveWakeLocks;	 Catch:{ all -> 0x0055 }
        r6 = r1;
        r5.remove(r6);	 Catch:{ all -> 0x0055 }
        r5 = 1;
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x0055 }
        r0 = r5;
        goto L_0x000f;
    L_0x0033:
        r5 = "WakefulBroadcastReceiver";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r8 = r6;
        r6 = r8;
        r7 = r8;
        r7.<init>();	 Catch:{ all -> 0x0055 }
        r7 = "No active wake lock id #";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0055 }
        r7 = r1;
        r6 = r6.append(r7);	 Catch:{ all -> 0x0055 }
        r6 = r6.toString();	 Catch:{ all -> 0x0055 }
        r5 = android.util.Log.w(r5, r6);	 Catch:{ all -> 0x0055 }
        r5 = 1;
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x0055 }
        r0 = r5;
        goto L_0x000f;
    L_0x0055:
        r5 = move-exception;
        r4 = r5;
        r5 = r2;
        monitor-exit(r5);	 Catch:{ all -> 0x0055 }
        r5 = r4;
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent(android.content.Intent):boolean");
    }
}
