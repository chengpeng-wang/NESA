package com.facebook.ads.internal.dto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.facebook.ads.internal.f.c;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.g.a;
import com.mopub.common.GpsHelper;

public class f {
    public static final String a = VERSION.RELEASE;
    public static String b = "";
    public static String c = "";
    public static String d = "";
    public static String e = "";
    public static String f = "";
    public static int g = 0;
    public static String h = "";
    public static String i = "";
    public static int j = 0;
    public static String k = "";
    public static int l = 0;
    public static String m = "";
    public static String n = "";
    public static String o = "";
    public static boolean p = false;
    public static String q = "";
    private static boolean r = false;

    public static synchronized void a(Context context) {
        synchronized (f.class) {
            if (!r) {
                r = true;
                c(context);
            }
            d(context);
        }
    }

    public static void b(Context context) {
        if (r) {
            try {
                a a;
                com.facebook.ads.internal.f a2;
                SharedPreferences sharedPreferences = context.getSharedPreferences("SDKIDFA", 0);
                if (sharedPreferences.contains("attributionId")) {
                    n = sharedPreferences.getString("attributionId", "");
                }
                if (sharedPreferences.contains(GpsHelper.ADVERTISING_ID_KEY)) {
                    o = sharedPreferences.getString(GpsHelper.ADVERTISING_ID_KEY, "");
                    p = sharedPreferences.getBoolean("limitAdTracking", p);
                    q = c.SHARED_PREFS.name();
                }
                try {
                    a = g.a(context.getContentResolver());
                } catch (Exception e) {
                    com.facebook.ads.internal.util.c.a(b.a(e, "Error retrieving attribution id from fb4a"));
                    a = null;
                }
                if (a != null) {
                    String str = a.a;
                    if (str != null) {
                        n = str;
                    }
                }
                try {
                    a2 = com.facebook.ads.internal.f.a(context, a);
                } catch (Exception e2) {
                    com.facebook.ads.internal.util.c.a(b.a(e2, "Error retrieving advertising id from Google Play Services"));
                    a2 = null;
                }
                if (a2 != null) {
                    String a3 = a2.a();
                    Boolean valueOf = Boolean.valueOf(a2.b());
                    if (a3 != null) {
                        o = a3;
                        p = valueOf.booleanValue();
                        q = a2.c().name();
                    }
                }
                Editor edit = sharedPreferences.edit();
                edit.putString("attributionId", n);
                edit.putString(GpsHelper.ADVERTISING_ID_KEY, o);
                edit.putBoolean("limitAdTracking", p);
                edit.apply();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }
    }

    private static void c(Context context) {
        String networkOperatorName;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            d = packageInfo.packageName;
            f = packageInfo.versionName;
            g = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        try {
            if (d != null && d.length() >= 0) {
                String installerPackageName = packageManager.getInstallerPackageName(d);
                if (installerPackageName != null && installerPackageName.length() > 0) {
                    h = installerPackageName;
                }
            }
        } catch (Exception e2) {
        }
        try {
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
            if (applicationLabel != null && applicationLabel.length() > 0) {
                e = applicationLabel.toString();
            }
        } catch (NameNotFoundException e3) {
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            networkOperatorName = telephonyManager.getNetworkOperatorName();
            if (networkOperatorName != null && networkOperatorName.length() > 0) {
                i = networkOperatorName;
            }
        }
        networkOperatorName = Build.MANUFACTURER;
        if (networkOperatorName != null && networkOperatorName.length() > 0) {
            b = networkOperatorName;
        }
        networkOperatorName = Build.MODEL;
        if (networkOperatorName != null && networkOperatorName.length() > 0) {
            c = Build.MODEL;
        }
    }

    private static void d(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                j = activeNetworkInfo.getType();
                k = activeNetworkInfo.getTypeName();
                l = activeNetworkInfo.getSubtype();
                m = activeNetworkInfo.getSubtypeName();
            }
        } catch (Exception e) {
        }
    }
}
