package com.android.locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.locker.MainActivity.mainActivity;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            Intent intentStarter = new Intent(context, mainActivity.class);
            intentStarter.addFlags(268435456);
            context.startActivity(intentStarter);
        } catch (Throwable e) {
            Toast.makeText(context, e.toString(), 1).show();
        }
    }
}
