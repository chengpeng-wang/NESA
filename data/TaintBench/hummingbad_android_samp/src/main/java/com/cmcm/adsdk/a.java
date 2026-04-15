package com.cmcm.adsdk;

import android.util.Log;

/* compiled from: Log */
public final class a {
    public static boolean a = true;
    public static boolean b = true;

    public static void a(String str, String str2) {
        if (a) {
            Log.i(str, str2, null);
        }
    }

    public static void a(String str, Throwable th) {
        if (a) {
            Log.i(str, " " + th);
        }
    }
}
