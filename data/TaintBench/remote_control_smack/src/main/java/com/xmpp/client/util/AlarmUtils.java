package com.xmpp.client.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.android.service.AlarmReceiver;
import java.util.Calendar;

public class AlarmUtils {
    private Context Ctx;

    public AlarmUtils(Context context) {
        this.Ctx = context;
    }

    public void startAlatm() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        PendingIntent sender = PendingIntent.getBroadcast(this.Ctx, 1, new Intent(this.Ctx, AlarmReceiver.class), 0);
        AlarmManager am = (AlarmManager) this.Ctx.getSystemService("alarm");
        long firstime = SystemClock.elapsedRealtime();
        am.setRepeating(0, c.getTimeInMillis(), 60000, sender);
    }
}
