package com.cleanmaster.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.cleanmaster.func.process.b;
import com.cmcm.adsdk.Const;
import com.picksinit.c;
import java.io.File;
import java.util.List;
import java.util.Locale;

/* compiled from: Commons */
public final class a {
    private static int a = 0;
    private static boolean b = true;
    private static long c = 0;
    private static Locale d = null;
    private static boolean e = false;
    private static boolean f = false;

    public static int a(Context context, String str) {
        int i = -1;
        if (context == null || str == null || str.length() <= 0) {
            return i;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    public static boolean b(Context context, String str) {
        if (context == null || str == null) {
            return false;
        }
        List c = com.cleanmaster.func.cache.a.a().c();
        if (c != null && c.size() > 0) {
            return c.contains(str);
        }
        try {
            context.getPackageManager().getPackageInfo(str, 256);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean a(Context context) {
        if (!b(context, "com.android.vending")) {
            return false;
        }
        PackageInfo c = c(context, "com.google.android.gsf");
        if (c == null || (c.applicationInfo.flags & 1) != 1) {
            return false;
        }
        return true;
    }

    public static PackageInfo c(Context context, String str) {
        PackageInfo packageInfo = null;
        if (context == null || str == null) {
            return packageInfo;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 256);
        } catch (Exception e) {
            return packageInfo;
        }
    }

    public static String a() {
        try {
            return System.getString(c.getInstance().getContext().getContentResolver(), "android_id");
        } catch (Exception e) {
            return "";
        }
    }

    public static String b() {
        return c.getInstance().getChannelId();
    }

    public static boolean a(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 1) == 0 && (applicationInfo.flags & 128) == 0;
    }

    public static final boolean b(Context context) {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            if (new File("/data/data/" + context.getPackageName() + "/databases/webviewCache.db").exists()) {
                return false;
            }
            sQLiteDatabase = context.openOrCreateDatabase("webviewCache.db", 0, null);
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
                return false;
            }
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
            }
        }
    }

    public static boolean a(Context context, Intent intent) {
        try {
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        } catch (SecurityException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    public static boolean c(Context context) {
        for (NetworkInfo networkInfo : ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo()) {
            if (networkInfo.getTypeName().equalsIgnoreCase(Const.CONNECTION_TYPE_WIFI)) {
                if (networkInfo.isConnected()) {
                    return true;
                }
            } else if (networkInfo.getTypeName().equalsIgnoreCase(Const.CONNECTION_TYPE_MOBILE_UNKNOWN) && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean a(String str, Context context) {
        boolean z = false;
        if (str == null || str.length() == 0) {
            return z;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName("com.android.vending", "com.android.vending.AssetBrowserActivity");
            intent.setFlags(268435456);
            intent.setData(Uri.parse(str));
            return a(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
            return z;
        }
    }

    public static void b(String str, Context context) {
        boolean z = false;
        if (a(context) && !TextUtils.isEmpty(str)) {
            z = a(str, context);
        }
        if (!z && !TextUtils.isEmpty(str)) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setFlags(268435456);
            a(context, intent);
        }
    }

    public static int d(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void d(Context context, String str) {
        Intent a = b.a(str);
        if (a != null) {
            if (!(context instanceof Activity)) {
                a.addFlags(268435456);
            }
            a(context, a);
        }
    }

    public static String e(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return String.format(Locale.US, "%d*%d", new Object[]{Integer.valueOf(displayMetrics.heightPixels), Integer.valueOf(displayMetrics.widthPixels)});
        } catch (Exception e) {
            return "";
        }
    }

    public static float f(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static String g(Context context) {
        if (context == null) {
            return null;
        }
        String simOperator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (simOperator == null || simOperator.length() < 3) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(simOperator, 0, 3);
        return stringBuilder.toString();
    }

    public static String h(Context context) {
        if (context == null) {
            return null;
        }
        String simOperator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (simOperator == null || simOperator.length() < 5) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(simOperator, 3, 5);
        return stringBuilder.toString();
    }

    public static String i(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static String j(Context context) {
        Locale locale = null;
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                Configuration configuration = resources.getConfiguration();
                if (configuration != null) {
                    locale = configuration.locale;
                }
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                return locale.getLanguage();
            }
        }
        locale = Locale.getDefault();
        return locale.getLanguage();
    }

    public static String k(Context context) {
        Locale locale = null;
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                Configuration configuration = resources.getConfiguration();
                if (configuration != null) {
                    locale = configuration.locale;
                }
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                return locale.getCountry();
            }
        }
        locale = Locale.getDefault();
        return locale.getCountry();
    }

    public static String c() {
        return c.getInstance().getContext().getPackageName();
    }

    public static String d() {
        return "3";
    }
}
