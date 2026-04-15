package com.cleanmaster.functionevent.report;

import android.os.Build;
import android.text.TextUtils;
import com.cleanmaster.util.u;

/* compiled from: BaseTracer */
public abstract class a {
    public static int a(long j) {
        return Math.round(((float) j) / 1048576.0f);
    }

    public static String a(String str, String str2) {
        CharSequence a = u.a(str);
        if (TextUtils.isEmpty(a)) {
            a = Build.MODEL;
        }
        return !TextUtils.isEmpty(a) ? a : str2;
    }
}
