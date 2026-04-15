package com.savemebeta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStartUpReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, GTSTSR.class));
        Intent App = new Intent(context, TaskBarView.class);
        App.addFlags(268435456);
        context.startActivity(App);
    }
}
