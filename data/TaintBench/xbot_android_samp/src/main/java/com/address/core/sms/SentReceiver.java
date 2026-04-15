package com.address.core.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.address.core.RunService;

public class SentReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int result;
        int id = intent.getIntExtra("id", 0);
        String number = intent.getStringExtra("number");
        String sms = intent.getStringExtra("message");
        switch (getResultCode()) {
            case -1:
                result = 1;
                break;
            default:
                result = 3;
                break;
        }
        RunService.getService().getScriptLoader().call("onSMSStatus", Integer.valueOf(id), number, sms, Integer.valueOf(result));
    }
}
