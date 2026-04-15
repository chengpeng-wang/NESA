package com.smart.studio.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            ProxyService.alarmProcess(context);
        } catch (Exception e) {
        }
    }
}
