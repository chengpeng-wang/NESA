package com.android.tools.system;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.android.tools.system.EternalService.Alarm;

public class OnBootReceiver extends BroadcastReceiver {
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Context context2 = context;
        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED") && !EternalService.isRunning(context2)) {
            Alarm.setAlarm(context2);
            Context context3 = context2;
            Intent intent2 = r8;
            Intent intent3 = intent3;
            try {
                intent3 = new Intent(context2, Class.forName("com.android.tools.system.EternalService"));
                ComponentName startService = context3.startService(intent2);
            } catch (ClassNotFoundException e) {
                Throwable th = e;
                NoClassDefFoundError noClassDefFoundError = r14;
                NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
                throw noClassDefFoundError;
            }
        }
    }

    public OnBootReceiver() {
    }
}
