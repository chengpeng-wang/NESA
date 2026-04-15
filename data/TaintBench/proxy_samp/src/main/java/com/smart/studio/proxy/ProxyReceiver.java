package com.smart.studio.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;

public class ProxyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("proxy", intent.getAction());
            if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                ProxyService.userPresent(context);
            } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                try {
                    NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    String typeName = info.getTypeName();
                    String subtypeName = info.getSubtypeName();
                    Log.i("proxy", "Network Type: " + typeName + ", subtype: " + subtypeName + ", available: " + info.isAvailable() + ", state: " + info.getState());
                } catch (Exception e) {
                }
                ProxyService.connectionChange(context);
            }
        } catch (Exception e2) {
        }
    }
}
