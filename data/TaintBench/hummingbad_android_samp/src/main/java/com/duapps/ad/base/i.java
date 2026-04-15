package com.duapps.ad.base;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

/* compiled from: SharedPrefsUtils */
public class i {
    private static final String a = i.class.getSimpleName();
    private static final Object b = new Object();
    private static final byte[] c = new byte[]{(byte) -53, (byte) -49, (byte) 125, (byte) 31, (byte) 17, (byte) 26, (byte) 81, (byte) 36, (byte) -53, (byte) 17, (byte) 39, (byte) 43, (byte) -64, (byte) 79, (byte) 48, (byte) -9, (byte) 32, (byte) -60, (byte) -21, (byte) -92, (byte) -48, (byte) 58, (byte) -59, (byte) -73, (byte) -36, (byte) -121, (byte) -71, (byte) -92, (byte) -87, (byte) 87, (byte) -121, (byte) 19, (byte) -92, (byte) -96, (byte) 67, (byte) 53, (byte) 51, (byte) 99, (byte) 53, (byte) 59, (byte) 57, (byte) 33, (byte) 121, (byte) -22, (byte) 31, (byte) -80, (byte) 118, (byte) -69};

    static {
        Object obj = new Object();
    }

    private i() {
    }

    public static void a(Context context, List<String> list, int i) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        try {
            JSONStringer array = new JSONStringer().array();
            for (String value : list) {
                array.value(value);
            }
            array.endArray();
            edit.putString("n_pid" + i, array.toString());
            d.a(edit);
        } catch (JSONException e) {
        }
    }

    public static List<String> a(Context context, int i) {
        int i2 = 0;
        String string = context.getSharedPreferences("_toolbox_prefs", 0).getString("n_pid" + i, "");
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray jSONArray = new JSONArray(string);
                int length = jSONArray.length();
                while (i2 < length) {
                    String optString = jSONArray.optString(i2);
                    if (!TextUtils.isEmpty(optString)) {
                        arrayList.add(optString);
                    }
                    i2++;
                }
            } catch (JSONException e) {
            }
        }
        return arrayList;
    }

    public static void a(Context context, String str, long j) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit.putLong("last_modified_" + str, j);
        d.a(edit);
    }

    public static long a(Context context, String str) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getLong("last_modified_" + str, 0);
    }

    public static void a(Context context) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        synchronized (b) {
            edit.putInt("fb_no_fill_c", 0);
            edit.putLong("fb_no_fill_t", 0);
            d.a(edit);
        }
    }

    public static void b(Context context) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit.putLong("fb_success_t", System.currentTimeMillis());
        d.a(edit);
    }

    public static void c(Context context) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit.putLong("ls_priotity_client", System.currentTimeMillis());
        d.a(edit);
    }

    public static long d(Context context) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getLong("ls_priotity_client", 0);
    }

    public static void a(Context context, long j) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit.putLong("ls_priotity_server", j);
        d.a(edit);
    }

    public static long b(Context context, int i) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getLong("priotity_server" + i, 0);
    }

    public static void a(Context context, int i, long j) {
        context.getSharedPreferences("_toolbox_prefs", 0).edit().putLong("priotity_server" + i, j).commit();
    }

    public static long e(Context context) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getLong("ls_priotity_server", 0);
    }

    public static boolean a(String str, Context context) {
        long d = d(str, context);
        if (d == 0) {
            return true;
        }
        d = System.currentTimeMillis() - d;
        if (d > 300000) {
            return true;
        }
        if (d > 300000 || c(str, context) >= 60) {
            return false;
        }
        return true;
    }

    public static boolean f(Context context) {
        return a("load_frequently_times", context);
    }

    public static boolean g(Context context) {
        return a("ads_load_frequently_times", context);
    }

    public static void h(Context context) {
        b("ads_load_frequently_times", context);
    }

    public static boolean i(Context context) {
        return a("fill_frequently_times", context);
    }

    public static void j(Context context) {
        b("fill_frequently_times", context);
    }

    public static void k(Context context) {
        b("load_frequently_times", context);
    }

    public static void b(String str, Context context) {
        int i;
        if (System.currentTimeMillis() - d(str, context) >= 300000) {
            Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
            edit.putLong(str + "_pull_time", System.currentTimeMillis());
            d.a(edit);
            i = 1;
        } else {
            i = c(str, context) + 1;
        }
        Editor edit2 = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit2.putInt(str, i);
        d.a(edit2);
    }

    private static int c(String str, Context context) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getInt(str, 0);
    }

    private static long d(String str, Context context) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getLong(str + "_pull_time", 0);
    }

    public static void c(Context context, int i) {
        Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
        edit.putInt("log_priotity", i);
        d.a(edit);
    }

    public static int l(Context context) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getInt("log_priotity", 3);
    }

    public static void a(Context context, int i, String str) {
        if (!TextUtils.isEmpty(str)) {
            Editor edit = context.getSharedPreferences("_toolbox_prefs", 0).edit();
            edit.putString("priority_policy_" + i, str);
            d.a(edit);
        }
    }

    public static String d(Context context, int i) {
        return context.getSharedPreferences("_toolbox_prefs", 0).getString("priority_policy_" + i, "");
    }

    public static void b(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            context.getSharedPreferences("_toolbox_prefs", 0).edit().putString("imid", str).apply();
        }
    }

    public static String m(Context context) {
        try {
            String str = new String(a.a("8a1n9d0i3c1y0c2f", "8a1n9d0i3c1y0c2f", c));
            f.c(a, "getInID(): " + str);
            return context.getSharedPreferences("_toolbox_prefs", 0).getString("imid", str);
        } catch (Exception e) {
            f.d(a, "getInID Exception :" + e.toString());
            return null;
        }
    }
}
