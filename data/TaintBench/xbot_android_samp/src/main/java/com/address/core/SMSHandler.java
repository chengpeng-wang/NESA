package com.address.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSHandler extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        Log.write("SMSHandler:onReceive");
        try {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            String num = "";
            String msg = "";
            SmsMessage[] messages = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(messages[i].getMessageBody());
            }
            if (RunService.getService().onSMSReceived(messages[0].getOriginatingAddress(), sb.toString()).booleanValue()) {
                abortBroadcast();
            }
        } catch (Exception e) {
            Log.write("SMSHandler exception: " + e.getMessage() + " stacktrace: " + e.getStackTrace().toString());
        }
    }
}
