package com.qc.common;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog.Calls;

public class ContactUtil {
    public static int getCallLogSum(Context context) {
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, null, null, null);
        int sum = cursor.getCount();
        cursor.close();
        return sum;
    }

    public static int getCalloutPhoneSum(Context context) {
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, "type = 1", null, null);
        int sum = cursor.getCount();
        cursor.close();
        return sum;
    }

    public static int getCallinPhoneSum(Context context) {
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, "type = 2", null, null);
        int sum = cursor.getCount();
        cursor.close();
        return sum;
    }

    public static int getCallnullPhoneSum(Context context) {
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, "type = 3", null, null);
        int sum = cursor.getCount();
        cursor.close();
        return sum;
    }
}
