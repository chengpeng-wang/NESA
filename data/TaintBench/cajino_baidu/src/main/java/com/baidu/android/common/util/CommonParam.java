package com.baidu.android.common.util;

import android.content.Context;
import android.text.TextUtils;

public class CommonParam {
    private static final boolean DEBUG = false;
    private static final String TAG = CommonParam.class.getSimpleName();

    public static String getCUID(Context context) {
        String deviceId = getDeviceId(context);
        String imei = DeviceId.getIMEI(context);
        if (TextUtils.isEmpty(imei)) {
            imei = "0";
        }
        return deviceId + "|" + new StringBuffer(imei).reverse().toString();
    }

    private static String getDeviceId(Context context) {
        return DeviceId.getDeviceID(context);
    }
}
