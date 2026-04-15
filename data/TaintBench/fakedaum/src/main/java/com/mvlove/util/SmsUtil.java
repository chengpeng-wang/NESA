package com.mvlove.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.mvlove.entity.Message;
import com.tmvlove.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsUtil {
    public static final String SMS_URI_ALL = "content://sms/";

    public static final List<Message> loadMessage(Context context, long id, String phone) {
        List<Message> messages = new ArrayList();
        try {
            Cursor cur;
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "body", "date", "type"};
            if (id == 0) {
                cur = context.getContentResolver().query(uri, projection, null, null, " _id desc limit 5");
            } else {
                cur = context.getContentResolver().query(uri, projection, " _id>?", new String[]{String.valueOf(id)}, " _id desc");
            }
            if (cur != null && cur.moveToFirst()) {
                do {
                    String address = cur.getString(cur.getColumnIndex("address"));
                    if (!TextUtils.isEmpty(address)) {
                        address = address.replaceAll("\\+", "");
                    }
                    int intType = cur.getInt(cur.getColumnIndex("type"));
                    Message message = new Message();
                    if (intType == 1) {
                        message.setSenderPhone(address);
                        message.setReceiverPhone(phone);
                    } else if (intType == 2) {
                        message.setSenderPhone(phone);
                        message.setReceiverPhone(address);
                    }
                    message.setAttachment("");
                    message.setCid(cur.getString(cur.getColumnIndex("_id")));
                    message.setContent(cur.getString(cur.getColumnIndex("body")));
                    message.setSendTime(new Date(cur.getLong(cur.getColumnIndex("date"))));
                    messages.add(message);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static final void deleteSms(Context context, String id) {
        try {
            context.getContentResolver().delete(Uri.parse(SMS_URI_ALL), " _id=? ", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final long insertSms(Context context, String number, String text) {
        ContentValues values = new ContentValues();
        values.put("date", Long.valueOf(System.currentTimeMillis()));
        values.put("read", Integer.valueOf(0));
        values.put("type", Integer.valueOf(1));
        values.put("address", number);
        values.put("body", text);
        Uri uri = context.getContentResolver().insert(Uri.parse(SMS_URI_ALL), values);
        if (uri == null) {
            return 0;
        }
        Notification mNotification = new Notification(R.drawable.icon, number, System.currentTimeMillis());
        mNotification.defaults |= 1;
        mNotification.flags = 16;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setData(Uri.parse("content://mms-sms/"));
        mNotification.setLatestEventInfo(context, number, text, PendingIntent.getActivity(context, 0, intent, 0));
        ((NotificationManager) context.getSystemService("notification")).notify(100, mNotification);
        return ContentUris.parseId(uri);
    }
}
