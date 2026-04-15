package com.facebook.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.g.a;
import com.facebook.ads.internal.util.r;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class AdSettings {
    public static final boolean DEBUG = false;
    static volatile boolean a = false;
    private static final String b = AdSettings.class.getSimpleName();
    private static final Collection<String> c = new HashSet();
    private static final Collection<String> d;
    private static String e = null;
    private static boolean f = false;
    private static String g = null;

    static {
        HashSet hashSet = new HashSet();
        d = hashSet;
        hashSet.add("sdk");
        d.add("google_sdk");
        d.add("vbox86p");
        d.add("vbox86tp");
    }

    private static void a(String str) {
        if (!a) {
            a = true;
            Log.d(b, "Test mode device hash: " + str);
            Log.d(b, "When testing your app with Facebook's ad units you must specify the device hashed ID to ensure the delivery of test ads, add the following code before loading an ad: AdSettings.addTestDevice(\"" + str + "\");");
        }
    }

    public static void addTestDevice(String str) {
        c.add(str);
    }

    public static void addTestDevices(Collection<String> collection) {
        c.addAll(collection);
    }

    public static void clearTestDevices() {
        c.clear();
    }

    public static String getUrlPrefix() {
        return e;
    }

    public static boolean isChildDirected() {
        return f;
    }

    public static boolean isTestMode(Context context) {
        if (d.contains(Build.PRODUCT)) {
            return true;
        }
        if (g == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("FBAdPrefs", 0);
            String string = sharedPreferences.getString("deviceIdHash", null);
            g = string;
            if (r.a(string)) {
                a a = g.a(context.getContentResolver());
                if (!r.a(a.b)) {
                    g = r.b(a.b);
                } else if (r.a(a.a)) {
                    g = r.b(UUID.randomUUID().toString());
                } else {
                    g = r.b(a.a);
                }
                sharedPreferences.edit().putString("deviceIdHash", g).apply();
            }
        }
        if (c.contains(g)) {
            return true;
        }
        a(g);
        return false;
    }

    public static void setIsChildDirected(boolean z) {
        f = z;
    }

    public static void setUrlPrefix(String str) {
        e = str;
    }
}
