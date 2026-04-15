package com.adobe.flashplayer_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemSWF extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, AdobeFlashCore.class));
        }
    }
}
