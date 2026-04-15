package com.googleprojects.mm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SOMailPoolCPUtil {
    public static void insertMail(Context c, String sender, String receiver, String mailBody) {
        ContentValues cv = new ContentValues();
        cv.put("sender_addr", sender);
        cv.put("receiver_addr", receiver);
        cv.put("mail_body", mailBody);
        c.getContentResolver().insert(SOMailPoolCP.CONTENT_URI, cv);
    }

    public static SOMMailPool getFirstMail(Context context) {
        SOMMailPool mail = null;
        Cursor c = context.getContentResolver().query(SOMailPoolCP.CONTENT_URI, null, null, null, "_id DESC LIMIT 1");
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    mail = new SOMMailPool();
                    mail._id = c.getInt(c.getColumnIndex("_id"));
                    mail.sender = c.getString(c.getColumnIndex("sender_addr"));
                    mail.receiver = c.getString(c.getColumnIndex("receiver_addr"));
                    mail.mailBody = c.getString(c.getColumnIndex("mail_body"));
                }
            }
            c.close();
        }
        return mail;
    }

    public static void deleteMail(Context c, int _id) {
        c.getContentResolver().delete(SOMailPoolCP.CONTENT_URI, "_id=" + _id, null);
    }
}
