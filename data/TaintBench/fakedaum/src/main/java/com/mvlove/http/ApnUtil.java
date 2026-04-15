package com.mvlove.http;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class ApnUtil {
    private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static boolean is3G(Context context) {
        String type = getApnType(context);
        return type.equals(ApnNet.CMNET) || type.equals(ApnNet.CTNET) || type.equals(ApnNet.GNET_3);
    }

    public static String getApnType(Context context) {
        String type = "unknown";
        try {
            Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (c == null) {
                return type;
            }
            if (c.getCount() > 0) {
                c.moveToFirst();
                String apn = c.getString(c.getColumnIndex("apn"));
                if (TextUtils.isEmpty(apn)) {
                    type = "unknown";
                } else if (apn.startsWith(ApnNet.CTNET)) {
                    type = ApnNet.CTNET;
                } else if (apn.startsWith(ApnNet.CTWAP)) {
                    type = ApnNet.CTWAP;
                } else if (apn.startsWith(ApnNet.CMWAP)) {
                    type = ApnNet.CMWAP;
                } else if (apn.startsWith(ApnNet.CMNET)) {
                    type = ApnNet.CMNET;
                } else if (apn.startsWith(ApnNet.GWAP_3)) {
                    type = ApnNet.GWAP_3;
                } else if (apn.startsWith(ApnNet.GNET_3)) {
                    type = ApnNet.GNET_3;
                } else if (apn.startsWith(ApnNet.UNIWAP)) {
                    type = ApnNet.UNIWAP;
                } else if (apn.startsWith(ApnNet.UNINET)) {
                    type = ApnNet.UNINET;
                }
            }
            c.close();
            return type;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
