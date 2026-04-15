package sx.jolly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import sx.jolly.core.JollyService;

public class RebootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, JollyService.class));
        }
    }
}
