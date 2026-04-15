package com.cmcm.adsdk.utils;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.cmcm.adsdk.Const;

/* compiled from: Util */
public final class c {
    private static String a = "";
    private static Object b = new Object();

    public static boolean a() {
        if (a("ro.miui.ui.version.name", Const.CONNECTION_TYPE_UNKNOWN).equals("V5")) {
            return true;
        }
        return false;
    }

    public static boolean b() {
        if (a("ro.miui.ui.version.name", Const.CONNECTION_TYPE_UNKNOWN).equalsIgnoreCase("V6")) {
            return true;
        }
        return false;
    }

    public static boolean c() {
        if (a("ro.miui.ui.version.name", Const.CONNECTION_TYPE_UNKNOWN).equalsIgnoreCase("V7")) {
            return true;
        }
        return false;
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        return powerManager != null ? powerManager.isScreenOn() : false;
    }

    private static String a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{str, str2});
        } catch (Exception e) {
            return str2;
        }
    }

    public static String b(Context context) {
        if (TextUtils.isEmpty(a)) {
            synchronized (b) {
                if (TextUtils.isEmpty(a)) {
                    try {
                        String string = System.getString(context.getContentResolver(), "android_id");
                        if (!TextUtils.isEmpty(string)) {
                            a = string;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return a;
    }

    public static String c(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.widthPixels;
            return String.valueOf(displayMetrics.heightPixels) + "*" + String.valueOf(i);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static String d(Context context) {
        return context.getResources().getConfiguration().locale.toString();
    }

    public static String e(Context context) {
        if (context == null) {
            return "";
        }
        String simOperator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (simOperator == null || simOperator.length() < 3) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(simOperator, 0, 3);
        return stringBuilder.toString();
    }

    public static int f(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return -1;
        }
    }
}
