package com.mb.bdapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.Locale;

public class MobileInfoUtils {
    private static final String NETWORKTYPE_2G = "2G";
    private static final String NETWORKTYPE_3G = "3G";
    private static final String NETWORKTYPE_4G = "4G";
    private static final String NETWORKTYPE_WIFI = "WIFI";
    private static String mNetName;
    private static String mNetType;
    private static String mOperatorsName;

    public static String getOperatorsName(Context context) {
        if (context != null && TextUtils.isEmpty(mOperatorsName)) {
            String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperatorName();
            if (!TextUtils.isEmpty(operator)) {
                mOperatorsName = operator;
            }
        }
        return mOperatorsName;
    }

    public static String getNetName(Context context) {
        if (context != null && TextUtils.isEmpty(mNetName)) {
            String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
            if (!TextUtils.isEmpty(operator)) {
                mNetName = operator;
            }
        }
        return mNetName;
    }

    public static String getNetType(Context context) {
        if (context != null && TextUtils.isEmpty(mNetType)) {
            switch (((TelephonyManager) context.getSystemService("phone")).getPhoneType()) {
                case 1:
                    mNetType = NETWORKTYPE_2G;
                    break;
                case 2:
                    mNetType = NETWORKTYPE_3G;
                    break;
                default:
                    mNetType = "NONE";
                    break;
            }
        }
        return mNetType;
    }

    public static String getCurrentNetType(Context context) {
        String type = "";
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return "null";
        }
        if (info.getType() == 1) {
            return "WIFI";
        }
        if (info.getType() != 0) {
            return type;
        }
        int subType = info.getSubtype();
        if (subType == 4 || subType == 1 || subType == 2) {
            return NETWORKTYPE_2G;
        }
        if (subType == 3 || subType == 8 || subType == 6 || subType == 5 || subType == 12) {
            return NETWORKTYPE_3G;
        }
        if (subType == 13) {
            return NETWORKTYPE_4G;
        }
        return type;
    }

    public static int getAndroidVersion() {
        int versionCode = 0;
        try {
            return VERSION.SDK_INT;
        } catch (Exception e) {
            return versionCode;
        }
    }

    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getLanguage(Context context) {
        return Locale.getDefault().getLanguage();
    }

    public static String getSubscriberId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static boolean isConnectInternet(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
}
