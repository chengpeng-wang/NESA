package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import com.cmcm.adsdk.Const;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.z;
import java.util.Locale;

/* compiled from: BUGLY */
public class b {
    public static String a() {
        try {
            return Build.MODEL;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static String b() {
        try {
            return VERSION.RELEASE;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static int c() {
        try {
            return VERSION.SDK_INT;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return -1;
        }
    }

    public static String a(Context context) {
        String str = "fail";
        if (context == null) {
            return str;
        }
        try {
            str = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (str == null) {
                return "null";
            }
            return str.toLowerCase();
        } catch (Throwable th) {
            Throwable th2 = th;
            String str2 = str;
            Throwable th3 = th2;
            if (z.a(th3)) {
                return str2;
            }
            th3.printStackTrace();
            return str2;
        }
    }

    public static String b(Context context) {
        String str = "fail";
        if (context == null) {
            return str;
        }
        try {
            str = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (str == null) {
                return "null";
            }
            return str.toLowerCase();
        } catch (Throwable th) {
            Throwable th2 = th;
            String str2 = str;
            Throwable th3 = th2;
            if (z.a(th3)) {
                return str2;
            }
            th3.printStackTrace();
            return str2;
        }
    }

    public static String c(Context context) {
        Throwable th;
        String str = "fail";
        if (context == null) {
            return str;
        }
        try {
            String string = Secure.getString(context.getContentResolver(), "android_id");
            if (string != null) {
                return string.toLowerCase();
            }
            try {
                return "null";
            } catch (Throwable th2) {
                Throwable th3 = th2;
                str = string;
                th = th3;
                if (z.a(th)) {
                    return str;
                }
                th.printStackTrace();
                return str;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public static String d(Context context) {
        String str = "fail";
        if (context == null) {
            return str;
        }
        try {
            str = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (str == null) {
                return "null";
            }
            return str.toLowerCase();
        } catch (Throwable th) {
            Throwable th2 = th;
            String str2 = str;
            Throwable th3 = th2;
            if (z.a(th3)) {
                return str2;
            }
            th3.printStackTrace();
            return str2;
        }
    }

    public static boolean d() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static String e() {
        try {
            return "" + System.getProperty("os.arch");
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static long f() {
        long j = -1;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
        } catch (Throwable th) {
            if (z.a(th)) {
                return j;
            }
            th.printStackTrace();
            return j;
        }
    }

    public static long g() {
        long j = -1;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        } catch (Throwable th) {
            if (z.a(th)) {
                return j;
            }
            th.printStackTrace();
            return j;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0088 A:{SYNTHETIC, Splitter:B:46:0x0088} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x008d A:{SYNTHETIC, Splitter:B:49:0x008d} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005d A:{Catch:{ all -> 0x00ac }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0062 A:{SYNTHETIC, Splitter:B:29:0x0062} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0067 A:{SYNTHETIC, Splitter:B:32:0x0067} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0088 A:{SYNTHETIC, Splitter:B:46:0x0088} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x008d A:{SYNTHETIC, Splitter:B:49:0x008d} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005d A:{Catch:{ all -> 0x00ac }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0062 A:{SYNTHETIC, Splitter:B:29:0x0062} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0067 A:{SYNTHETIC, Splitter:B:32:0x0067} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0088 A:{SYNTHETIC, Splitter:B:46:0x0088} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x008d A:{SYNTHETIC, Splitter:B:49:0x008d} */
    public static long h() {
        /*
        r1 = 0;
        r0 = "/proc/meminfo";
        r3 = new java.io.FileReader;	 Catch:{ Throwable -> 0x0055, all -> 0x0083 }
        r3.<init>(r0);	 Catch:{ Throwable -> 0x0055, all -> 0x0083 }
        r2 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x00b0, all -> 0x00a7 }
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r2.<init>(r3, r0);	 Catch:{ Throwable -> 0x00b0, all -> 0x00a7 }
        r0 = r2.readLine();	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r1 = ":\\s+";
        r4 = 2;
        r0 = r0.split(r1, r4);	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r1 = 1;
        r0 = r0[r1];	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r0 = r0.toLowerCase();	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r1 = "kb";
        r4 = "";
        r0 = r0.replace(r1, r4);	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r0 = r0.trim();	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r0 = java.lang.Long.parseLong(r0);	 Catch:{ Throwable -> 0x00b3, all -> 0x00aa }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r0 * r4;
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2.close();	 Catch:{ IOException -> 0x003f }
    L_0x0039:
        if (r3 == 0) goto L_0x003e;
    L_0x003b:
        r3.close();	 Catch:{ IOException -> 0x004a }
    L_0x003e:
        return r0;
    L_0x003f:
        r2 = move-exception;
        r4 = com.tencent.bugly.proguard.z.a(r2);
        if (r4 != 0) goto L_0x0039;
    L_0x0046:
        r2.printStackTrace();
        goto L_0x0039;
    L_0x004a:
        r2 = move-exception;
        r3 = com.tencent.bugly.proguard.z.a(r2);
        if (r3 != 0) goto L_0x003e;
    L_0x0051:
        r2.printStackTrace();
        goto L_0x003e;
    L_0x0055:
        r0 = move-exception;
        r2 = r1;
    L_0x0057:
        r3 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00ac }
        if (r3 != 0) goto L_0x0060;
    L_0x005d:
        r0.printStackTrace();	 Catch:{ all -> 0x00ac }
    L_0x0060:
        if (r1 == 0) goto L_0x0065;
    L_0x0062:
        r1.close();	 Catch:{ IOException -> 0x006d }
    L_0x0065:
        if (r2 == 0) goto L_0x006a;
    L_0x0067:
        r2.close();	 Catch:{ IOException -> 0x0078 }
    L_0x006a:
        r0 = -2;
        goto L_0x003e;
    L_0x006d:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x0065;
    L_0x0074:
        r0.printStackTrace();
        goto L_0x0065;
    L_0x0078:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x006a;
    L_0x007f:
        r0.printStackTrace();
        goto L_0x006a;
    L_0x0083:
        r0 = move-exception;
        r2 = r1;
        r3 = r1;
    L_0x0086:
        if (r2 == 0) goto L_0x008b;
    L_0x0088:
        r2.close();	 Catch:{ IOException -> 0x0091 }
    L_0x008b:
        if (r3 == 0) goto L_0x0090;
    L_0x008d:
        r3.close();	 Catch:{ IOException -> 0x009c }
    L_0x0090:
        throw r0;
    L_0x0091:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x008b;
    L_0x0098:
        r1.printStackTrace();
        goto L_0x008b;
    L_0x009c:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x0090;
    L_0x00a3:
        r1.printStackTrace();
        goto L_0x0090;
    L_0x00a7:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0086;
    L_0x00aa:
        r0 = move-exception;
        goto L_0x0086;
    L_0x00ac:
        r0 = move-exception;
        r3 = r2;
        r2 = r1;
        goto L_0x0086;
    L_0x00b0:
        r0 = move-exception;
        r2 = r3;
        goto L_0x0057;
    L_0x00b3:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x0057;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.h():long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d3 A:{SYNTHETIC, Splitter:B:46:0x00d3} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d8 A:{SYNTHETIC, Splitter:B:49:0x00d8} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a8 A:{Catch:{ all -> 0x00f7 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ad A:{SYNTHETIC, Splitter:B:29:0x00ad} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b2 A:{SYNTHETIC, Splitter:B:32:0x00b2} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d3 A:{SYNTHETIC, Splitter:B:46:0x00d3} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d8 A:{SYNTHETIC, Splitter:B:49:0x00d8} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a8 A:{Catch:{ all -> 0x00f7 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ad A:{SYNTHETIC, Splitter:B:29:0x00ad} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b2 A:{SYNTHETIC, Splitter:B:32:0x00b2} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d3 A:{SYNTHETIC, Splitter:B:46:0x00d3} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d8 A:{SYNTHETIC, Splitter:B:49:0x00d8} */
    public static long i() {
        /*
        r1 = 0;
        r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = "/proc/meminfo";
        r3 = new java.io.FileReader;	 Catch:{ Throwable -> 0x00a0, all -> 0x00ce }
        r3.<init>(r0);	 Catch:{ Throwable -> 0x00a0, all -> 0x00ce }
        r2 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x00fb, all -> 0x00f2 }
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r2.<init>(r3, r0);	 Catch:{ Throwable -> 0x00fb, all -> 0x00f2 }
        r2.readLine();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r0 = r2.readLine();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r1 = ":\\s+";
        r4 = 2;
        r0 = r0.split(r1, r4);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r1 = 1;
        r0 = r0[r1];	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r0 = r0.toLowerCase();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r1 = "kb";
        r4 = "";
        r0 = r0.replace(r1, r4);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r0 = r0.trim();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r0 = java.lang.Long.parseLong(r0);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r0 = r0 * r9;
        r4 = r2.readLine();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r5 = ":\\s+";
        r6 = 2;
        r4 = r4.split(r5, r6);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r5 = 1;
        r4 = r4[r5];	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r4 = r4.toLowerCase();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r5 = "kb";
        r6 = "";
        r4 = r4.replace(r5, r6);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r4 = r4.trim();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r4 = java.lang.Long.parseLong(r4);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r4 = r4 * r9;
        r6 = r2.readLine();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r7 = ":\\s+";
        r8 = 2;
        r6 = r6.split(r7, r8);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r7 = 1;
        r6 = r6[r7];	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r6 = r6.toLowerCase();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r7 = "kb";
        r8 = "";
        r6 = r6.replace(r7, r8);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r6 = r6.trim();	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r6 = java.lang.Long.parseLong(r6);	 Catch:{ Throwable -> 0x00fe, all -> 0x00f5 }
        r6 = r6 * r9;
        r0 = r0 + r4;
        r0 = r0 + r6;
        if (r2 == 0) goto L_0x0084;
    L_0x0081:
        r2.close();	 Catch:{ IOException -> 0x008a }
    L_0x0084:
        if (r3 == 0) goto L_0x0089;
    L_0x0086:
        r3.close();	 Catch:{ IOException -> 0x0095 }
    L_0x0089:
        return r0;
    L_0x008a:
        r2 = move-exception;
        r4 = com.tencent.bugly.proguard.z.a(r2);
        if (r4 != 0) goto L_0x0084;
    L_0x0091:
        r2.printStackTrace();
        goto L_0x0084;
    L_0x0095:
        r2 = move-exception;
        r3 = com.tencent.bugly.proguard.z.a(r2);
        if (r3 != 0) goto L_0x0089;
    L_0x009c:
        r2.printStackTrace();
        goto L_0x0089;
    L_0x00a0:
        r0 = move-exception;
        r2 = r1;
    L_0x00a2:
        r3 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00f7 }
        if (r3 != 0) goto L_0x00ab;
    L_0x00a8:
        r0.printStackTrace();	 Catch:{ all -> 0x00f7 }
    L_0x00ab:
        if (r1 == 0) goto L_0x00b0;
    L_0x00ad:
        r1.close();	 Catch:{ IOException -> 0x00b8 }
    L_0x00b0:
        if (r2 == 0) goto L_0x00b5;
    L_0x00b2:
        r2.close();	 Catch:{ IOException -> 0x00c3 }
    L_0x00b5:
        r0 = -2;
        goto L_0x0089;
    L_0x00b8:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x00b0;
    L_0x00bf:
        r0.printStackTrace();
        goto L_0x00b0;
    L_0x00c3:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x00b5;
    L_0x00ca:
        r0.printStackTrace();
        goto L_0x00b5;
    L_0x00ce:
        r0 = move-exception;
        r2 = r1;
        r3 = r1;
    L_0x00d1:
        if (r2 == 0) goto L_0x00d6;
    L_0x00d3:
        r2.close();	 Catch:{ IOException -> 0x00dc }
    L_0x00d6:
        if (r3 == 0) goto L_0x00db;
    L_0x00d8:
        r3.close();	 Catch:{ IOException -> 0x00e7 }
    L_0x00db:
        throw r0;
    L_0x00dc:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x00d6;
    L_0x00e3:
        r1.printStackTrace();
        goto L_0x00d6;
    L_0x00e7:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x00db;
    L_0x00ee:
        r1.printStackTrace();
        goto L_0x00db;
    L_0x00f2:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00d1;
    L_0x00f5:
        r0 = move-exception;
        goto L_0x00d1;
    L_0x00f7:
        r0 = move-exception;
        r3 = r2;
        r2 = r1;
        goto L_0x00d1;
    L_0x00fb:
        r0 = move-exception;
        r2 = r3;
        goto L_0x00a2;
    L_0x00fe:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x00a2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.i():long");
    }

    public static long j() {
        if (!d()) {
            return 0;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return -2;
        }
    }

    public static long k() {
        if (!d()) {
            return 0;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return -2;
        }
    }

    public static String l() {
        String str = "fail";
        try {
            return Locale.getDefault().getCountry();
        } catch (Throwable th) {
            if (z.a(th)) {
                return str;
            }
            th.printStackTrace();
            return str;
        }
    }

    public static String m() {
        String str = "fail";
        try {
            return Build.BRAND;
        } catch (Throwable th) {
            if (z.a(th)) {
                return str;
            }
            th.printStackTrace();
            return str;
        }
    }

    public static String e(Context context) {
        String str = EnvironmentCompat.MEDIA_UNKNOWN;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return null;
            }
            String str2;
            if (activeNetworkInfo.getType() == 1) {
                str2 = Const.CONNECTION_TYPE_WIFI;
            } else {
                if (activeNetworkInfo.getType() == 0) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    if (telephonyManager != null) {
                        int networkType = telephonyManager.getNetworkType();
                        switch (networkType) {
                            case 1:
                                str2 = Const.CONNECTION_TYPE_MOBILE_GPRS;
                                break;
                            case 2:
                                str2 = Const.CONNECTION_TYPE_MOBILE_EDGE;
                                break;
                            case 3:
                                str2 = Const.CONNECTION_TYPE_MOBILE_UMTS;
                                break;
                            case 4:
                                str2 = Const.CONNECTION_TYPE_MOBILE_CDMA;
                                break;
                            case 5:
                                str2 = Const.CONNECTION_TYPE_MOBILE_EVDO_0;
                                break;
                            case 6:
                                str2 = Const.CONNECTION_TYPE_MOBILE_EVDO_A;
                                break;
                            case 7:
                                str2 = Const.CONNECTION_TYPE_MOBILE_1xRTT;
                                break;
                            case 8:
                                str2 = Const.CONNECTION_TYPE_MOBILE_HSDPA;
                                break;
                            case 9:
                                str2 = Const.CONNECTION_TYPE_MOBILE_HSUPA;
                                break;
                            case 10:
                                str2 = Const.CONNECTION_TYPE_MOBILE_HSPA;
                                break;
                            case 11:
                                str2 = "iDen";
                                break;
                            case 12:
                                str2 = Const.CONNECTION_TYPE_MOBILE_EVDO_B;
                                break;
                            case 13:
                                str2 = Const.CONNECTION_TYPE_MOBILE_LTE;
                                break;
                            case 14:
                                str2 = "eHRPD";
                                break;
                            case 15:
                                str2 = "HSPA+";
                                break;
                            default:
                                str2 = "MOBILE(" + networkType + ")";
                                break;
                        }
                    }
                }
                str2 = str;
            }
            return str2;
        } catch (Exception e) {
            if (z.a(e)) {
                return str;
            }
            e.printStackTrace();
            return str;
        }
    }

    public static String f(Context context) {
        String a = ag.a(context, "ro.miui.ui.version.name");
        if (!ag.b(a) && !a.equals("fail")) {
            return "XiaoMi/MIUI/" + a;
        }
        a = ag.a(context, "ro.build.version.emui");
        if (!ag.b(a) && !a.equals("fail")) {
            return "HuaWei/EMOTION/" + a;
        }
        a = ag.a(context, "ro.lenovo.series");
        if (ag.b(a) || a.equals("fail")) {
            a = ag.a(context, "ro.build.nubia.rom.name");
            if (!ag.b(a) && !a.equals("fail")) {
                return "Zte/NUBIA/" + a + "_" + ag.a(context, "ro.build.nubia.rom.code");
            }
            a = ag.a(context, "ro.meizu.product.model");
            if (!ag.b(a) && !a.equals("fail")) {
                return "Meizu/FLYME/" + ag.a(context, "ro.build.display.id");
            }
            a = ag.a(context, "ro.build.version.opporom");
            if (!ag.b(a) && !a.equals("fail")) {
                return "Oppo/COLOROS/" + a;
            }
            a = ag.a(context, "ro.vivo.os.build.display.id");
            if (!ag.b(a) && !a.equals("fail")) {
                return "vivo/FUNTOUCH/" + a;
            }
            a = ag.a(context, "ro.aa.romver");
            if (!ag.b(a) && !a.equals("fail")) {
                return "htc/" + a + "/" + ag.a(context, "ro.build.description");
            }
            a = ag.a(context, "ro.lewa.version");
            if (!ag.b(a) && !a.equals("fail")) {
                return "tcl/" + a + "/" + ag.a(context, "ro.build.display.id");
            }
            a = ag.a(context, "ro.gn.gnromvernumber");
            if (!ag.b(a) && !a.equals("fail")) {
                return "amigo/" + a + "/" + ag.a(context, "ro.build.display.id");
            }
            a = ag.a(context, "ro.build.tyd.kbstyle_version");
            if (ag.b(a) || a.equals("fail")) {
                return ag.a(context, "ro.build.fingerprint") + "/" + ag.a(context, "ro.build.rom.id");
            }
            return "dido/" + a;
        }
        return "Lenovo/VIBE/" + ag.a(context, "ro.build.version.incremental");
    }

    public static String g(Context context) {
        return ag.a(context, "ro.board.platform");
    }
}
