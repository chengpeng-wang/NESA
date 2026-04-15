package com.qq.gdt.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class SystemServiceUtils {
    public static ConnectivityManager connectivityManager = null;
    public static NotificationManager notificationManager = null;
    public static TelephonyManager telephonyManager = null;
    public static WindowManager windowManager = null;

    public static NotificationManager getNotificationManager(Context context) {
        if (context != null && notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService("notification");
        }
        return notificationManager;
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        if (context != null && telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService("phone");
        }
        return telephonyManager;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        if (context != null && connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        }
        return connectivityManager;
    }

    public static WindowManager getWindowManager(Context context) {
        if (context != null && windowManager == null) {
            windowManager = (WindowManager) context.getSystemService("window");
        }
        return windowManager;
    }
}
