package com.baidu.android.pushservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.util.Util;

public class PushService extends Service {
    private boolean a = false;
    private Handler b = new Handler();
    private Runnable c = new n(this);

    private void a(boolean z, boolean z2) {
        this.a = z;
        if (b.a()) {
            Log.d("PushService", "stopSelf : exitOnDestroy=" + z + " --- immediate=" + z2);
        }
        if (z2) {
            this.c.run();
            return;
        }
        this.b.removeCallbacks(this.c);
        this.b.postDelayed(this.c, 1000);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        PushSettings.a(getApplicationContext());
        if (b.a()) {
            Log.d("PushService", "onCreate from : " + getPackageName());
        }
        if (!PushSDK.getInstantce(this).initPushSDK()) {
            a(true, true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (b.a()) {
            Log.i("PushService", "onDestroy");
        }
        PushSDK.destory();
        if (this.a && !Util.hasOtherServiceRuninMyPid(this, getClass().getName())) {
            Process.killProcess(Process.myPid());
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (b.a()) {
            Log.d("PushService", "-- onStartCommand -- " + intent);
        }
        if (intent == null) {
            intent = new Intent();
            if (b.a()) {
                Log.i("PushService", "--- onStart by null intent!");
            }
        }
        this.b.removeCallbacks(this.c);
        if (!PushSDK.getInstantce(this).handleOnStart(intent)) {
            a(true, true);
        }
        return 1;
    }
}
