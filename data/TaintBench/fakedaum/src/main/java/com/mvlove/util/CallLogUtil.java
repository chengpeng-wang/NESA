package com.mvlove.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.text.TextUtils;

public class CallLogUtil {
    public static synchronized void deleteRecentLog(Context context, String number) {
        synchronized (CallLogUtil.class) {
            if (!TextUtils.isEmpty(number)) {
                try {
                    Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, "number=?", new String[]{number}, "date desc");
                    if (cursor != null) {
                        if (cursor.moveToNext()) {
                            if (System.currentTimeMillis() - cursor.getLong(cursor.getColumnIndex("date")) < 10000) {
                                long _id = cursor.getLong(cursor.getColumnIndex("_id"));
                                context.getContentResolver().delete(Calls.CONTENT_URI, "_id=?", new String[]{String.valueOf(_id)});
                            }
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    public static synchronized void deleteCalllogByPhone(Context context, String number) {
        synchronized (CallLogUtil.class) {
            if (!TextUtils.isEmpty(number)) {
                try {
                    Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, "number=?", new String[]{number}, "date desc");
                    if (cursor != null) {
                        if (cursor.moveToNext()) {
                            long _id = cursor.getLong(cursor.getColumnIndex("_id"));
                            context.getContentResolver().delete(Calls.CONTENT_URI, "_id=?", new String[]{String.valueOf(_id)});
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
}
