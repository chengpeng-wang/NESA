package com.smart.studio.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            ProxyService.LogLocation(context, intent);
        } catch (Exception e) {
        }
    }
}
