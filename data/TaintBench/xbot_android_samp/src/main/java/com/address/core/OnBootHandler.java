package com.address.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootHandler extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            context.stopService(new Intent(context.getApplicationContext(), RunService.class));
            context.startService(new Intent(context.getApplicationContext(), RunService.class));
        } catch (Exception e) {
            Log.write("OnBootHandler exc: " + e.getMessage() + "onboothandlertrace: " + e.getStackTrace().toString());
        }
    }
}
