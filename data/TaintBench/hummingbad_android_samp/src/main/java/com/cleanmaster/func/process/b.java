package com.cleanmaster.func.process;

import android.content.Intent;
import com.picksinit.c;
import java.lang.reflect.Method;

/* compiled from: ProcessInfoHelper */
public class b {
    private static long a = -1;
    private static Method b = null;
    private static Method c = null;
    private static boolean d = false;

    public static long a() {
        if (a > 1) {
            return a;
        }
        long a = b().a();
        a = a;
        return a;
    }

    public static IPhoneMemoryInfo b() {
        PhoneMemoryInfo phoneMemoryInfo = new PhoneMemoryInfo(c.a(), c.b());
        if (a < phoneMemoryInfo.a() && 0 < phoneMemoryInfo.a()) {
            a = phoneMemoryInfo.a();
        }
        return phoneMemoryInfo;
    }

    public static Intent a(String str) {
        Intent intent = null;
        try {
            return c.getInstance().getContext().getPackageManager().getLaunchIntentForPackage(str);
        } catch (Exception e) {
            e.printStackTrace();
            return intent;
        }
    }
}
