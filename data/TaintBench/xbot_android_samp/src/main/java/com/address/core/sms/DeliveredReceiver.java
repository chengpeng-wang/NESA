package com.address.core.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.address.core.RunService;

public class DeliveredReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String number = intent.getStringExtra("number");
        String sms = intent.getStringExtra("message");
        RunService.getService().getScriptLoader().call("onSMSStatus", Integer.valueOf(id), number, sms, Integer.valueOf(2));
    }
}
