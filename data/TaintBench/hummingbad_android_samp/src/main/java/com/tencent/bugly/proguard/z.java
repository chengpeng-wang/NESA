package com.tencent.bugly.proguard;

import java.util.Locale;

/* compiled from: BUGLY */
public class z {
    private static a a = null;

    /* compiled from: BUGLY */
    public interface a {
        void a(String str);

        void a(String str, String str2);

        void b(String str);

        void c(String str);

        void d(String str);
    }

    public static synchronized a a() {
        a aVar;
        synchronized (z.class) {
            aVar = a;
        }
        return aVar;
    }

    public static synchronized void a(a aVar) {
        synchronized (z.class) {
            a = aVar;
        }
    }

    private static String f(String str, Object... objArr) {
        if (str == null) {
            return "null";
        }
        return (objArr == null || objArr.length == 0) ? str : String.format(Locale.US, str, objArr);
    }

    private static boolean a(int i, String str, Object... objArr) {
        a a = a();
        if (a == null) {
            return false;
        }
        String f = f(str, objArr);
        switch (i) {
            case 0:
                a.a(f);
                return true;
            case 1:
                a.b(f);
                return true;
            case 2:
                a.c(f);
                ab.a("W", "CrashReport", f);
                return true;
            case 3:
                a.d(f);
                ab.a("E", "CrashReport", f);
                return true;
            case 5:
                a.a("CrashReportInfo", f);
                ab.a("I", "CrashReportInfo", f);
                return true;
            default:
                return false;
        }
    }

    private static boolean a(int i, Throwable th) {
        a a = a();
        if (a == null) {
            return false;
        }
        String a2 = ag.a(th);
        switch (i) {
            case 0:
                a.a(a2);
                return true;
            case 1:
                a.b(a2);
                return true;
            case 2:
                a.c(a2);
                ab.a("W", "CrashReport", a2);
                return true;
            case 3:
                a.d(a2);
                ab.a("E", "CrashReport", a2);
                return true;
            default:
                return false;
        }
    }

    public static boolean a(String str, Object... objArr) {
        return a(0, str, objArr);
    }

    public static boolean b(String str, Object... objArr) {
        return a(5, str, objArr);
    }

    public static boolean c(String str, Object... objArr) {
        return a(1, str, objArr);
    }

    public static boolean d(String str, Object... objArr) {
        return a(2, str, objArr);
    }

    public static boolean a(Throwable th) {
        return a(2, th);
    }

    public static boolean e(String str, Object... objArr) {
        return a(3, str, objArr);
    }

    public static boolean b(Throwable th) {
        return a(3, th);
    }
}
