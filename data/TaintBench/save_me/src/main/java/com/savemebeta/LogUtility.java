package com.savemebeta;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CallLog.Calls;

public class LogUtility {
    public void AddNumToCallLog(ContentResolver resolver, String strNum, int type, long timeInMiliSecond) {
        while (strNum.contains("-")) {
            strNum = strNum.substring(0, strNum.indexOf(45)) + strNum.substring(strNum.indexOf(45) + 1, strNum.length());
        }
        ContentValues values = new ContentValues();
        values.put("number", strNum);
        values.put("date", Long.valueOf(timeInMiliSecond));
        values.put("duration", Integer.valueOf(0));
        values.put("type", Integer.valueOf(type));
        values.put("new", Integer.valueOf(1));
        values.put("name", "");
        values.put("numbertype", Integer.valueOf(0));
        values.put("numberlabel", "");
        if (resolver != null) {
            resolver.insert(Calls.CONTENT_URI, values);
        }
    }

    public void DeleteNumFromCallLog(ContentResolver resolver, String strNum) {
        try {
            Uri UriCalls = Uri.parse("content://call_log/calls");
            if (resolver != null) {
                resolver.delete(UriCalls, "number=?", new String[]{strNum});
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
