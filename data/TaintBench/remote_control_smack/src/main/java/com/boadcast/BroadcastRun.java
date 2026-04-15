package com.boadcast;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import com.example.monkeydiy.XmppService;

public class BroadcastRun extends Activity {
    public static void BroadcastIntent(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.TIME_TICK")) {
            boolean isServiceRunning = false;
            for (RunningServiceInfo service : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
                if ("com.example.monkeydiy.XmppService".equals(service.service.getClassName())) {
                    isServiceRunning = true;
                }
            }
            if (!isServiceRunning) {
                Intent xmpp_service = new Intent(context, XmppService.class);
                xmpp_service.setFlags(268435456);
                context.startService(xmpp_service);
            }
        }
    }
}
