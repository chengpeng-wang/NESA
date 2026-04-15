package com.baidu.android.pushservice;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.LocalServerSocket;
import android.os.Handler;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.net.ConnectManager;
import com.baidu.android.pushservice.util.NoProGuard;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.m;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.varia.ExternallyRolledFileAppender;

public class PushSDK implements NoProGuard {
    private static int ALARM_TIMEOUT = 600000;
    private static int ALARM_TIMEOUT_BAD_NET = 300000;
    public static final String LOCAL_SOCKET_ADDRESS = "com.baidu.pushservice.singelinstance";
    private static String TAG = "PushSDK";
    private static Context mContext;
    private static Handler mHandler;
    private static Object mIsAlive_lock = new Object();
    private static LocalServerSocket mLocalSocket;
    /* access modifiers changed from: private|static */
    public static Object mPushConnLock = new Object();
    public static e mPushConnection;
    private static PushSDK mPushSDK = null;
    private int alarmTimeout;
    private Runnable mConnectRunnable = new m(this);
    private Boolean mIsAlive = Boolean.valueOf(false);
    private Runnable mRegisterRunnable = new l(this);
    private x mRegistrationService;
    private Runnable mStartRunnable = new k(this);

    private PushSDK(Context context) {
        mHandler = new Handler();
        mContext = context.getApplicationContext();
        PushSettings.a(context.getApplicationContext());
        this.alarmTimeout = ALARM_TIMEOUT;
    }

    private void cancelAlarmRepeat() {
        Intent intent = new Intent();
        intent.putExtra("AlarmAlert", ExternallyRolledFileAppender.OK);
        intent.setClass(mContext, PushService.class);
        ((AlarmManager) mContext.getSystemService("alarm")).cancel(PendingIntent.getService(mContext, 0, intent, 268435456));
    }

    public static void destory() {
        if (mPushSDK != null) {
            mPushSDK.doDestory();
        }
    }

    private void doDestory() {
        if (b.a()) {
            Log.d(TAG, "destory");
        }
        synchronized (mIsAlive_lock) {
            try {
                if (mLocalSocket != null) {
                    mLocalSocket.close();
                    mLocalSocket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPushConnection != null) {
                synchronized (mPushConnLock) {
                    mPushConnection.c();
                    mPushConnection = null;
                }
            }
            PushDatabase.close();
            this.mIsAlive = Boolean.valueOf(false);
            mPushSDK = null;
        }
    }

    public static synchronized PushSDK getInstantce(Context context) {
        PushSDK pushSDK;
        synchronized (PushSDK.class) {
            if (mPushSDK == null) {
                mPushSDK = new PushSDK(context);
            }
            pushSDK = mPushSDK;
        }
        return pushSDK;
    }

    private boolean heartbeat() {
        boolean isNetworkConnected = ConnectManager.isNetworkConnected(mContext);
        if (b.a()) {
            Log.d(TAG, "heartbeat networkConnected :" + isNetworkConnected);
        }
        if (!isNetworkConnected) {
            cancelAlarmRepeat();
            return false;
        } else if (mPushConnection == null) {
            return false;
        } else {
            if (mPushConnection.a()) {
                mPushConnection.d();
                Intent intent = new Intent(PushConstants.ACTION_METHOD);
                intent.putExtra("method", "com.baidu.android.pushservice.action.SEND_APPSTAT");
                intent.setClass(mContext, PushService.class);
                this.mRegistrationService.a(intent);
            } else if (y.a().e()) {
                scheduleConnect();
            } else {
                if (b.a()) {
                    Log.i(TAG, "Channel token is not available, start NETWORK REGISTER SERVICE .");
                }
                scheduleRegister();
            }
            return true;
        }
    }

    public static boolean isAlive() {
        return mPushSDK != null ? mPushSDK.mIsAlive.booleanValue() : false;
    }

    private void newPushConnection() {
        synchronized (mPushConnLock) {
            mPushConnection = e.a(mContext);
        }
    }

    private void scheduleConnect() {
        mHandler.removeCallbacks(this.mConnectRunnable);
        mHandler.postDelayed(this.mConnectRunnable, 1000);
    }

    private void scheduleRegister() {
        mHandler.removeCallbacks(this.mRegisterRunnable);
        mHandler.postDelayed(this.mRegisterRunnable, 500);
    }

    private void setAlarmRepeat() {
        cancelAlarmRepeat();
        Intent intent = new Intent();
        intent.putExtra("AlarmAlert", ExternallyRolledFileAppender.OK);
        intent.setClass(mContext, PushService.class);
        PendingIntent service = PendingIntent.getService(mContext.getApplicationContext(), 0, intent, 268435456);
        long currentTimeMillis = System.currentTimeMillis() + ((long) ALARM_TIMEOUT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService("alarm");
        alarmManager.cancel(service);
        alarmManager.setRepeating(0, currentTimeMillis, (long) this.alarmTimeout, service);
    }

    private boolean shouldReConnect(Context context) {
        if (m.n(context.getApplicationContext()).size() <= 1) {
            if (b.a()) {
                Log.i(TAG, "Only one push app : " + context.getPackageName());
            }
            return false;
        }
        List<RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(1000);
        ArrayList arrayList = new ArrayList();
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            if (PushService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                arrayList.add(runningServiceInfo.service.getPackageName());
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!context.getPackageName().equals(str)) {
                SharedPreferences sharedPreferences;
                try {
                    sharedPreferences = context.createPackageContext(str, 2).getSharedPreferences(str + ".push_sync", 1);
                } catch (NameNotFoundException e) {
                    if (b.a()) {
                        Log.e(TAG, e.getMessage());
                    }
                    sharedPreferences = null;
                }
                if (sharedPreferences == null) {
                    if (b.a()) {
                        Log.w(TAG, "App:" + str + " doesn't init Version!");
                    }
                } else if (sharedPreferences.getInt("version2", 0) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean shouldStopSelf(Context context) {
        List<ResolveInfo> n = m.n(context.getApplicationContext());
        if (n.size() > 1) {
            boolean z;
            long j = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1).getLong("priority2", 0);
            for (ResolveInfo resolveInfo : n) {
                String str = resolveInfo.activityInfo.packageName;
                if (!context.getPackageName().equals(str)) {
                    SharedPreferences sharedPreferences;
                    try {
                        sharedPreferences = context.createPackageContext(str, 2).getSharedPreferences(str + ".push_sync", 1);
                    } catch (NameNotFoundException e) {
                        if (b.a()) {
                            Log.e(TAG, e.getMessage());
                        }
                        sharedPreferences = null;
                    }
                    if (sharedPreferences != null) {
                        long j2 = sharedPreferences.getLong("priority2", 0);
                        if (j2 > j) {
                            if (b.a()) {
                                Log.d(TAG, "shouldStopSelf-------localPriority = " + j + ";  other packageName = " + str + "--priority =" + j2);
                            }
                            z = true;
                            return z;
                        }
                    } else if (b.a()) {
                        Log.w(TAG, "App:" + str + " doesn't init Version!");
                    }
                }
            }
            z = false;
            return z;
        } else if (!b.a()) {
            return false;
        } else {
            Log.i(TAG, "Only one push app : " + context.getPackageName());
            return false;
        }
    }

    private boolean tryConnect() {
        boolean isNetworkConnected = ConnectManager.isNetworkConnected(mContext);
        if (b.a()) {
            Log.d(TAG, "tryConnect networkConnected :" + isNetworkConnected);
        }
        if (!isNetworkConnected || mPushConnection == null) {
            return false;
        }
        if (!mPushConnection.a()) {
            if (y.a().e()) {
                scheduleConnect();
            } else {
                if (b.a()) {
                    Log.i(TAG, "Channel token is not available, start NETWORK REGISTER SERVICE .");
                }
                scheduleRegister();
            }
        }
        return true;
    }

    public x getRegistrationService() {
        return this.mRegistrationService;
    }

    /* JADX WARNING: Missing block: B:64:?, code skipped:
            return true;
     */
    public boolean handleOnStart(android.content.Intent r7) {
        /*
        r6 = this;
        r2 = 1;
        r1 = 0;
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x0026;
    L_0x0008:
        r3 = TAG;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "handleOnStart intent action = ";
        r4 = r0.append(r4);
        if (r7 == 0) goto L_0x0048;
    L_0x0017:
        r0 = r7.getAction();
    L_0x001b:
        r0 = r4.append(r0);
        r0 = r0.toString();
        com.baidu.android.common.logging.Log.d(r3, r0);
    L_0x0026:
        if (r7 != 0) goto L_0x003a;
    L_0x0028:
        r7 = new android.content.Intent;
        r7.<init>();
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x003a;
    L_0x0033:
        r0 = TAG;
        r3 = "--- handleOnStart by null intent!";
        com.baidu.android.common.logging.Log.i(r0, r3);
    L_0x003a:
        r3 = mIsAlive_lock;
        monitor-enter(r3);
        r0 = r6.mIsAlive;	 Catch:{ all -> 0x0085 }
        r0 = r0.booleanValue();	 Catch:{ all -> 0x0085 }
        if (r0 != 0) goto L_0x004b;
    L_0x0045:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r1;
    L_0x0047:
        return r0;
    L_0x0048:
        r0 = "";
        goto L_0x001b;
    L_0x004b:
        r0 = mHandler;	 Catch:{ all -> 0x0085 }
        r4 = r6.mStartRunnable;	 Catch:{ all -> 0x0085 }
        r0.removeCallbacks(r4);	 Catch:{ all -> 0x0085 }
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x0070;
    L_0x0058:
        r0 = TAG;	 Catch:{ all -> 0x0085 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0085 }
        r4.<init>();	 Catch:{ all -> 0x0085 }
        r5 = "-- handleOnStart -- ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0085 }
        r4 = r4.append(r7);	 Catch:{ all -> 0x0085 }
        r4 = r4.toString();	 Catch:{ all -> 0x0085 }
        com.baidu.android.common.logging.Log.i(r0, r4);	 Catch:{ all -> 0x0085 }
    L_0x0070:
        r0 = mLocalSocket;	 Catch:{ all -> 0x0085 }
        if (r0 != 0) goto L_0x0077;
    L_0x0074:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r1;
        goto L_0x0047;
    L_0x0077:
        r0 = "AlarmAlert";
        r0 = r7.getStringExtra(r0);	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x0088;
    L_0x007f:
        r0 = r6.heartbeat();	 Catch:{ all -> 0x0085 }
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        goto L_0x0047;
    L_0x0085:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        throw r0;
    L_0x0088:
        r0 = "com.baidu.pushservice.action.STOP";
        r4 = r7.getAction();	 Catch:{ all -> 0x0085 }
        r0 = r0.equals(r4);	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x0097;
    L_0x0094:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r1;
        goto L_0x0047;
    L_0x0097:
        if (r7 == 0) goto L_0x00df;
    L_0x0099:
        r0 = "pushservice_restart";
        r4 = "method";
        r4 = r7.getStringExtra(r4);	 Catch:{ all -> 0x0085 }
        r0 = r0.equals(r4);	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x00c6;
    L_0x00a7:
        r0 = mContext;	 Catch:{ all -> 0x0085 }
        r0 = r0.getPackageName();	 Catch:{ all -> 0x0085 }
        r4 = "pkg_name";
        r4 = r7.getStringExtra(r4);	 Catch:{ all -> 0x0085 }
        r0 = android.text.TextUtils.equals(r0, r4);	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x00bc;
    L_0x00b9:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r2;
        goto L_0x0047;
    L_0x00bc:
        r0 = mContext;	 Catch:{ all -> 0x0085 }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        com.baidu.android.pushservice.util.m.a(r0, r4);	 Catch:{ all -> 0x0085 }
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r1;
        goto L_0x0047;
    L_0x00c6:
        r0 = r6.mRegistrationService;	 Catch:{ all -> 0x0085 }
        r0 = r0.a(r7);	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x00df;
    L_0x00ce:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x00db;
    L_0x00d4:
        r0 = TAG;	 Catch:{ all -> 0x0085 }
        r1 = "-- handleOnStart -- intent handled  by mRegistrationService ";
        com.baidu.android.common.logging.Log.i(r0, r1);	 Catch:{ all -> 0x0085 }
    L_0x00db:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        r0 = r2;
        goto L_0x0047;
    L_0x00df:
        r0 = r6.tryConnect();	 Catch:{ all -> 0x0085 }
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        goto L_0x0047;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.PushSDK.handleOnStart(android.content.Intent):boolean");
    }

    public boolean initPushSDK() {
        if (b.a()) {
            Log.d(TAG, "Create PushSDK from : " + mContext.getPackageName());
        }
        m.f(mContext.getApplicationContext());
        if (!m.c(mContext.getApplicationContext()) && !shouldStopSelf(mContext)) {
            synchronized (mIsAlive_lock) {
                if (mLocalSocket == null) {
                    try {
                        mLocalSocket = new LocalServerSocket(m.p(mContext));
                    } catch (Exception e) {
                        if (b.a()) {
                            Log.d(TAG, "--- Socket Adress (" + m.p(mContext) + ") in use --- @ " + mContext.getPackageName());
                        }
                    }
                }
                if (mLocalSocket == null) {
                    return false;
                }
                newPushConnection();
                this.mRegistrationService = new x(mContext);
                PushSettings.e(mContext);
                mHandler.postDelayed(this.mStartRunnable, 500);
                this.mIsAlive = Boolean.valueOf(true);
                return true;
            }
        } else if (!b.a()) {
            return false;
        } else {
            Log.d(TAG, "onCreate shouldStopSelf");
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void sendRequestTokenIntent() {
        if (b.a()) {
            Log.d(TAG, ">> sendRequestTokenIntent");
        }
        b.a(mContext, new Intent("com.baidu.pushservice.action.TOKEN"));
    }

    public void setAlarmTimeout(int i) {
        if (i > 0) {
            this.alarmTimeout = i * 1000;
        }
        setAlarmRepeat();
    }
}
