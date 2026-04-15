package com.qc.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.gsm.SmsManager;

public class SmsSenderAndReceiver {
    public static void send(String phoneNumber, String msmStr, Context context) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, msmStr, PendingIntent.getBroadcast(context, 0, new Intent(), 0), null);
    }

    public static void send2(String phoneNumber, String msmStr) {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, msmStr, null, null);
    }

    public static void deleteSms(String smsNumber, Context context) {
        context.getContentResolver().delete(Uri.parse("content://sms"), "address=?", new String[]{smsNumber});
    }
}
