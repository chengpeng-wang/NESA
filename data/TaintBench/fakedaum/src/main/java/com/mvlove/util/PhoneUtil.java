package com.mvlove.util;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PhoneUtil {
    public static final String getPhone(Context context) {
        String phone = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
        if (TextUtils.isEmpty(phone)) {
            phone = LocalManager.getPhone(context);
        }
        if (TextUtils.isEmpty(phone)) {
            return phone;
        }
        return phone.replaceAll("\\+", "");
    }

    public static final String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
    }

    public static final String getModel() {
        return Build.MODEL;
    }

    public static final String getVersion() {
        return VERSION.RELEASE;
    }
}
