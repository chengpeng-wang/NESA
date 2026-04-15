package com.googleprojects.mm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class GJSMSUtil {
    public static final int FLAG_EXCLUDE_STOPPED_PACKAGES = 16;
    public static final int FLAG_INCLUDE_STOPPED_PACKAGES = 32;
    Context mContext = null;

    public GJSMSUtil(Context c) {
        this.mContext = c;
    }

    public SmsMessage[] getMessageListFromIntent(Intent intent) {
        SmsMessage[] msgs = null;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
        }
        return msgs;
    }

    public Uri addMessageToInbox(SmsMessage msg) {
        ContentValues cv = new ContentValues();
        cv.put("address", msg.getOriginatingAddress());
        cv.put("body", msg.getMessageBody().toString());
        return this.mContext.getContentResolver().insert(Uri.parse("content://sms/inbox"), cv);
    }

    public static boolean isHigher3dot1() {
        String version = VERSION.RELEASE;
        float fVersion = 0.0f;
        if (version == null || version.length() < 5) {
            try {
                fVersion = Float.parseFloat(version.substring(0, version.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                fVersion = Float.parseFloat(version.substring(0, 3));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (fVersion >= 3.1f) {
            return true;
        }
        return false;
    }
}
