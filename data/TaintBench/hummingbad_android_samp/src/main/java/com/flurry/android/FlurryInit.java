package com.flurry.android;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import com.flurry.sdk.gq;
import com.flurry.sdk.hg;
import com.flurry.sdk.hz;
import com.flurry.sdk.iw;
import com.flurry.sdk.iy;
import com.flurry.sdk.n;

public class FlurryInit {
    private static final String kLogTag = FlurryInit.class.getSimpleName();

    public static synchronized void init(Context context, String str) {
        synchronized (FlurryInit.class) {
            if (VERSION.SDK_INT >= 10) {
                if (context == null) {
                    throw new NullPointerException("Null context");
                }
                if (str != null) {
                    if (str.length() != 0) {
                        try {
                            Log.i("Flurry", "init");
                            iy.a(hg.class);
                            iy.a(n.class);
                            hz.a(context, str);
                            n.a().i = new gq(context);
                        } catch (Throwable th) {
                            iw.a(kLogTag, "", th);
                        }
                    }
                }
                throw new IllegalArgumentException("apiKey not specified");
            }
        }
        return;
    }

    public static void setLogEnabled(boolean z) {
        if (VERSION.SDK_INT < 10) {
            iw.b(kLogTag, "Device SDK Version older than 10");
            return;
        }
        iw.a(z);
        if (z) {
            iw.a(0);
        } else {
            iw.a(5);
        }
    }
}
