package com.example.smsmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.smsmanager.tools.ParamsInfo;
import java.util.Timer;
import java.util.TimerTask;

public final class SmsSystemManageService extends Service {
    private static final String TAG = "SmsSystemManageService";
    static String s1 = "111111";
    static String s2 = "22222";
    private boolean isDoing;
    TelephonyManager telManager = null;
    private Thread thread;
    Timer timer = null;
    TimerTask ttTask = null;
    WakeLock wakeLock;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.i(TAG, "oncreat");
        ParamsInfo.context = getApplicationContext();
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, SmsSystemManageService.class.getName());
        this.wakeLock.acquire();
        this.isDoing = false;
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onstart");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Log.i(TAG, "on destroy");
        super.onDestroy();
        if (this.wakeLock != null) {
            this.wakeLock.release();
            this.wakeLock = null;
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, SmsSystemManageService.class);
        startService(localIntent);
    }
}
