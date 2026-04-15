package com.google.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.List;

public class ScreenReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (isServiceRunning(context, "com.google.services.PhoneService")) {
            Log.i("新的intent", "主程序正在运行，这次不启动");
        } else {
            context.startService(new Intent(context, PhoneService.class));
        }
    }

    public boolean isServiceRunning(Context context, String str) {
        List runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(30);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (((RunningServiceInfo) runningServices.get(i)).service.getClassName().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
