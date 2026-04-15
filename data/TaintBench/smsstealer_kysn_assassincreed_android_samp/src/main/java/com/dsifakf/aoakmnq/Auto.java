package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Auto extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, Repeat.class);
        serviceIntent.addFlags(268435456);
        context.startService(serviceIntent);
    }
}
