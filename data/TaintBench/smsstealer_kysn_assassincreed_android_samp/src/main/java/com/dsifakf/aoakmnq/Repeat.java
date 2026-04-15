package com.dsifakf.aoakmnq;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Repeat extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ((AlarmManager) getSystemService("alarm")).setRepeating(0, System.currentTimeMillis(), 11002, PendingIntent.getBroadcast(getBaseContext(), 0, new Intent("Check"), 0));
    }
}
