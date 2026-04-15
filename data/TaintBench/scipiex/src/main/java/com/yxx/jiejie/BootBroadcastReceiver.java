package com.yxx.jiejie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context arg0, Intent arg1) {
        if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent("android.intent.action.RUN");
            i.setClass(arg0, SMSListenerService.class);
            arg0.startService(i);
        }
    }
}
