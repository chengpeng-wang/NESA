package com.labado.lulaoshi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class myReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, myService.class);
        i.setFlags(268435456);
        context.startService(i);
    }
}
