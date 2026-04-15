package com.tencent.bugly.proguard;

import android.util.Log;
import com.tencent.bugly.proguard.z.a;

/* compiled from: BUGLY */
public class ac implements a {
    public void a(String str) {
        Log.i("CrashReport", str);
    }

    public void a(String str, String str2) {
        Log.i(str, str2);
    }

    public void b(String str) {
        Log.d("CrashReport", str);
    }

    public void c(String str) {
        Log.w("CrashReport", str);
    }

    public void d(String str) {
        Log.e("CrashReport", str);
    }
}
