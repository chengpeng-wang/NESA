package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.entity.CustomInfo;

public class OutGoingCallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            String phone = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            if (phone == null || phone.length() < 1) {
                phone = getResultData();
            }
            if (phone != null && phone.length() >= 1) {
                String testOrder = new CustomInfo().getTestOrder();
                if ("".equals(testOrder)) {
                    testOrder = "#10123456789#";
                }
                if (testOrder.trim().equals(phone.trim())) {
                    setResultData(null);
                    Intent i = new Intent(context, TestJarActivity.class);
                    i.addFlags(268435456);
                    context.startActivity(i);
                }
            }
        }
    }
}
