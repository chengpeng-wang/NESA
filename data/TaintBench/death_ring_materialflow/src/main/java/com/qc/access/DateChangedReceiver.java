package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.base.BitmapCache;

public class DateChangedReceiver extends BroadcastReceiver {
    private static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";

    public void onReceive(Context context, Intent intent) {
        if (ACTION_DATE_CHANGED.equals(intent.getAction())) {
            BitmapCache.getInstance().clearCache();
        }
    }
}
