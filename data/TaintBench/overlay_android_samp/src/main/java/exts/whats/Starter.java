package exts.whats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Starter extends BroadcastReceiver {
    public static final String ACTION = "exts.whats.wakeup";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ((action.equals("android.intent.action.BOOT_COMPLETED") || action.equals(ACTION)) && !MainService.isRunning) {
            Intent i = new Intent();
            i.setClass(context, MainService.class);
            context.startService(i);
        }
    }
}
