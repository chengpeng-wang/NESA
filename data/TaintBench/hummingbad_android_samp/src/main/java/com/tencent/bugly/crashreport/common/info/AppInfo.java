package com.tencent.bugly.crashreport.common.info;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: BUGLY */
public class AppInfo {
    public static final String[] a = ",msdk,imsdk,tbscore,lejiagu,opengame".split(",");
    public static final String[] b = "0,1,2,3,4,5".split(",");
    private static String c = "BUGLY_DISABLE";
    private static ActivityManager d;

    public static String a(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageName();
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static synchronized String b(Context context) {
        String str;
        synchronized (AppInfo.class) {
            try {
                str = context.getPackageManager().getPackageInfo(a(context), 0).versionName;
                if (str == null) {
                    throw new Exception("Can not get version name in AndroidManifest.xml.");
                }
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
                str = "fail";
            }
        }
        return str;
    }

    public static boolean a(Context context, String str) {
        if (context == null || str == null || str.trim().length() <= 0) {
            return false;
        }
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr == null) {
                return false;
            }
            for (Object equals : strArr) {
                if (str.equals(equals)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            if (z.a(th)) {
                return false;
            }
            th.printStackTrace();
            return false;
        }
    }

    public static synchronized boolean c(Context context) {
        boolean a;
        synchronized (AppInfo.class) {
            a = a(context, "android.permission.READ_LOGS");
        }
        return a;
    }

    public static Boolean d(Context context) {
        if (context == null) {
            return null;
        }
        try {
            List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses == null || runningAppProcesses.size() == 0) {
                return Boolean.valueOf(false);
            }
            int myPid = Process.myPid();
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == myPid) {
                    return Boolean.valueOf(runningAppProcessInfo.importance == 100);
                }
            }
            return Boolean.valueOf(false);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0049 A:{Catch:{ all -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0052 A:{SYNTHETIC, Splitter:B:24:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005c A:{SYNTHETIC, Splitter:B:30:0x005c} */
    public static java.lang.String a(android.content.Context r5, int r6) {
        /*
        r0 = 0;
        r2 = 0;
        r1 = new java.io.FileReader;	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r3.<init>();	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r4 = "/proc/";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r3 = r3.append(r6);	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r4 = "/cmdline";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r1.<init>(r3);	 Catch:{ Throwable -> 0x0041, all -> 0x0058 }
        r2 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r2 = new char[r2];	 Catch:{ Throwable -> 0x0066 }
        r1.read(r2);	 Catch:{ Throwable -> 0x0066 }
    L_0x0027:
        r3 = r2.length;	 Catch:{ Throwable -> 0x0066 }
        if (r0 >= r3) goto L_0x002e;
    L_0x002a:
        r3 = r2[r0];	 Catch:{ Throwable -> 0x0066 }
        if (r3 != 0) goto L_0x003e;
    L_0x002e:
        r3 = new java.lang.String;	 Catch:{ Throwable -> 0x0066 }
        r3.<init>(r2);	 Catch:{ Throwable -> 0x0066 }
        r2 = 0;
        r0 = r3.substring(r2, r0);	 Catch:{ Throwable -> 0x0066 }
        if (r1 == 0) goto L_0x003d;
    L_0x003a:
        r1.close();	 Catch:{ Throwable -> 0x0060 }
    L_0x003d:
        return r0;
    L_0x003e:
        r0 = r0 + 1;
        goto L_0x0027;
    L_0x0041:
        r0 = move-exception;
        r1 = r2;
    L_0x0043:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x0064 }
        if (r2 != 0) goto L_0x004c;
    L_0x0049:
        r0.printStackTrace();	 Catch:{ all -> 0x0064 }
    L_0x004c:
        r0 = java.lang.String.valueOf(r6);	 Catch:{ all -> 0x0064 }
        if (r1 == 0) goto L_0x003d;
    L_0x0052:
        r1.close();	 Catch:{ Throwable -> 0x0056 }
        goto L_0x003d;
    L_0x0056:
        r1 = move-exception;
        goto L_0x003d;
    L_0x0058:
        r0 = move-exception;
        r1 = r2;
    L_0x005a:
        if (r1 == 0) goto L_0x005f;
    L_0x005c:
        r1.close();	 Catch:{ Throwable -> 0x0062 }
    L_0x005f:
        throw r0;
    L_0x0060:
        r1 = move-exception;
        goto L_0x003d;
    L_0x0062:
        r1 = move-exception;
        goto L_0x005f;
    L_0x0064:
        r0 = move-exception;
        goto L_0x005a;
    L_0x0066:
        r0 = move-exception;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.AppInfo.a(android.content.Context, int):java.lang.String");
    }

    public static String e(Context context) {
        try {
            return a(context, Process.myPid());
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static List<String> f(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData == null) {
                return null;
            }
            String string = applicationInfo.metaData.getString(c);
            if (string == null || string.length() == 0) {
                return null;
            }
            String[] split = string.split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].trim();
            }
            return Arrays.asList(split);
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static boolean g(Context context) {
        if (context == null) {
            return false;
        }
        if (d == null) {
            d = (ActivityManager) context.getSystemService("activity");
        }
        try {
            MemoryInfo memoryInfo = new MemoryInfo();
            d.getMemoryInfo(memoryInfo);
            return memoryInfo.lowMemory;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public static List<String> h(Context context) {
        int i;
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < b.length - 1; i2++) {
            for (i = i2 + 1; i < b.length; i++) {
                if (Integer.valueOf(b[i2]).intValue() > Integer.valueOf(b[i]).intValue()) {
                    String str = a[i2];
                    a[i2] = a[i];
                    a[i] = str;
                    str = b[i2];
                    b[i2] = b[i];
                    b[i] = str;
                }
            }
        }
        for (String str2 : a) {
            String str3;
            if (str2.equals("")) {
                str3 = "com.tencent.bugly." + ".crashreport.CrashReport".substring(1);
            } else {
                str3 = "com.tencent.bugly." + str2 + ".crashreport.CrashReport";
            }
            try {
                Class.forName(str3);
                if (str2.equals("")) {
                    arrayList.add("bugly");
                } else {
                    z.a("[init] find bugly channel: %s", str2);
                    arrayList.add(str2);
                }
            } catch (Throwable th) {
            }
        }
        return arrayList;
    }
}
