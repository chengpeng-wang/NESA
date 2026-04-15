package com.cleanmaster.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import com.cleanmaster.func.cache.a;

public class ReceiverUtils extends BroadcastReceiver {
    private static ReceiverUtils a;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Uri data;
        if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
            data = intent.getData();
            a.a().a(data != null ? data.getSchemeSpecificPart() : "", context);
        } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
            data = intent.getData();
            a.a().a(data != null ? data.getSchemeSpecificPart() : "");
        }
    }

    public static void a(Context context) {
        if (context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme(com.umeng.analytics.onlineconfig.a.b);
            if (a == null) {
                a = new ReceiverUtils();
            }
            context.registerReceiver(a, intentFilter);
        }
    }
}
