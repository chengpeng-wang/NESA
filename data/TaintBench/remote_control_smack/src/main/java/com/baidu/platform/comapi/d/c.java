package com.baidu.platform.comapi.d;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.baidu.mapapi.VersionInfo;
import com.baidu.platform.comjni.map.commonmemcache.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class c {
    public static Context A;
    public static final int B = Integer.parseInt(VERSION.SDK);
    public static float C = 1.0f;
    private static boolean D = true;
    private static int E = 0;
    private static int F = 0;
    static a a = new a();
    static String b = "02";
    static String c;
    static String d;
    static String e;
    static String f;
    static int g;
    static int h;
    static int i;
    static int j;
    static int k;
    static int l;
    static String m;
    static String n;
    static String o;
    static String p;
    static String q;
    static String r;
    static String s = "baidu";
    static String t = "baidu";
    static String u = "";
    static String v = "";
    static String w = "";
    static String x;
    static String y = "-1";
    static String z = "-1";

    public static String a() {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer(10);
        for (int i = 0; i < 10; i++) {
            stringBuffer.append(random.nextInt(10));
        }
        return stringBuffer.toString();
    }

    public static String a(Context context) {
        Exception e;
        String str = "imei.dat";
        String str2;
        try {
            if (D) {
                FileInputStream openFileInput = context.openFileInput(str);
                byte[] bArr = new byte[openFileInput.available()];
                openFileInput.read(bArr);
                openFileInput.close();
                str = new String(bArr);
                try {
                    return str.substring(str.indexOf(124) + 1);
                } catch (Exception e2) {
                    e = e2;
                    str2 = str;
                }
            } else {
                str2 = a();
                try {
                    FileOutputStream openFileOutput = context.openFileOutput(str, 32768);
                    openFileOutput.write(("|" + str2).getBytes());
                    openFileOutput.close();
                    return str2;
                } catch (Exception e3) {
                    e = e3;
                }
            }
        } catch (Exception e22) {
            Exception exception = e22;
            str2 = null;
            e = exception;
            e.printStackTrace();
            return str2;
        }
    }

    private static void a(String str) {
        v = str;
    }

    public static void a(String str, String str2) {
        b(str);
        a(str2);
        e();
    }

    public static void b() {
        d();
    }

    private static void b(String str) {
        w = str;
    }

    public static void b(String str, String str2) {
        y = str2;
        z = str;
        e();
    }

    public static byte[] b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toByteArray();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle c() {
        Bundle bundle = new Bundle();
        bundle.putString("cpu", u);
        bundle.putString("resid", b);
        bundle.putString("channel", s);
        bundle.putString("glr", v);
        bundle.putString("glv", w);
        bundle.putString("mb", f());
        bundle.putString("sv", h());
        bundle.putString("os", j());
        bundle.putInt("dpi_x", m());
        bundle.putInt("dpi_y", m());
        bundle.putString("net", p);
        bundle.putString("im", j(A));
        bundle.putString("imrand", a(A));
        bundle.putByteArray("signature", b(A));
        bundle.putString("pcn", A.getPackageName());
        bundle.putInt("screen_x", g());
        bundle.putInt("screen_y", i());
        return bundle;
    }

    public static void c(Context context) {
        A = context;
        x = context.getFilesDir().getAbsolutePath();
        d = Build.MODEL;
        e = "Android" + VERSION.SDK;
        c = context.getPackageName();
        e(context);
        f(context);
        g(context);
        h(context);
        i(context);
        l(context);
        t();
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            E = locationManager.isProviderEnabled("gps") ? 1 : 0;
            F = locationManager.isProviderEnabled("network") ? 1 : 0;
        } catch (Exception e) {
        }
    }

    public static void d() {
        if (a != null) {
            a.b();
        }
    }

    public static void d(Context context) {
        int i = 1;
        int i2 = 0;
        try {
            byte[] bArr;
            FileInputStream fileInputStream;
            InputStream open;
            byte[] bArr2;
            FileOutputStream fileOutputStream;
            File file = new File(x);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(x + "/ver.dat");
            byte[] bArr3 = new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 0, (byte) 0, (byte) 0};
            if (file.exists()) {
                FileInputStream fileInputStream2 = new FileInputStream(file);
                bArr = new byte[fileInputStream2.available()];
                fileInputStream2.read(bArr);
                fileInputStream2.close();
                if (Arrays.equals(bArr, bArr3)) {
                    i = 0;
                }
            }
            AssetManager assets = context.getAssets();
            File file2 = new File(x + "/channel");
            if (file2.exists()) {
                fileInputStream = new FileInputStream(file2);
                bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                s = new String(bArr).trim();
                fileInputStream.close();
            } else {
                open = assets.open("channel");
                bArr2 = new byte[open.available()];
                open.read(bArr2);
                s = new String(bArr2).trim();
                bArr2 = new String(bArr2).trim().getBytes();
                open.close();
                file2.createNewFile();
                fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr2);
                fileOutputStream.close();
            }
            file2 = new File(x + "/oem");
            if (file2.exists()) {
                fileInputStream = new FileInputStream(file2);
                bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                t = new String(bArr).trim();
                fileInputStream.close();
            } else {
                open = assets.open("oem");
                bArr2 = new byte[open.available()];
                open.read(bArr2);
                t = new String(bArr2).trim();
                bArr2 = new String(bArr2).trim().getBytes();
                open.close();
                file2.createNewFile();
                fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(bArr2);
                fileOutputStream.close();
            }
            if (i != 0) {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                fileOutputStream2.write(bArr3);
                fileOutputStream2.close();
                File file3 = new File(x + "/cfg/h");
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                file3 = new File(x + "/cfg/l");
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                String[] strArr = new String[]{"CMRequire.dat", "VerDatset.dat", "cfg/h/ResPack.cfg", "cfg/l/ResPack.cfg", "cfg/h/DVHotcity.cfg", "cfg/l/DVHotcity.cfg", "cfg/l/mapstyle.sty", "cfg/l/satellitestyle.sty", "cfg/l/trafficstyle.sty", "cfg/l/DVDirectory.cfg", "cfg/l/DVVersion.cfg", "cfg/h/mapstyle.sty", "cfg/h/satellitestyle.sty", "cfg/h/trafficstyle.sty", "cfg/h/DVDirectory.cfg", "cfg/h/DVVersion.cfg"};
                String[] strArr2 = new String[]{"CMRequire.dat", "VerDatset.dat", "cfg/h/ResPack.rs", "cfg/l/ResPack.rs", "cfg/h/DVHotcity.cfg", "cfg/l/DVHotcity.cfg", "cfg/l/mapstyle.sty", "cfg/l/satellitestyle.sty", "cfg/l/trafficstyle.sty", "cfg/l/DVDirectory.cfg", "cfg/l/DVVersion.cfg", "cfg/h/mapstyle.sty", "cfg/h/satellitestyle.sty", "cfg/h/trafficstyle.sty", "cfg/h/DVDirectory.cfg", "cfg/h/DVVersion.cfg"};
                while (i2 < strArr2.length) {
                    InputStream open2 = assets.open(strArr[i2]);
                    bArr = new byte[open2.available()];
                    open2.read(bArr);
                    open2.close();
                    File file4 = new File(x + "/" + strArr2[i2]);
                    if (file4.exists()) {
                        file4.delete();
                    }
                    file4.createNewFile();
                    fileOutputStream = new FileOutputStream(file4);
                    fileOutputStream.write(bArr);
                    fileOutputStream.close();
                    i2++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void e() {
        Bundle bundle = new Bundle();
        bundle.putString("cpu", u);
        bundle.putString("resid", b);
        bundle.putString("channel", s);
        bundle.putString("glr", v);
        bundle.putString("glv", w);
        bundle.putString("mb", f());
        bundle.putString("sv", h());
        bundle.putString("os", j());
        bundle.putInt("dpi_x", m());
        bundle.putInt("dpi_y", m());
        bundle.putString("net", p);
        bundle.putString("im", j(A));
        bundle.putString("imrand", a(A));
        bundle.putString("pcn", A.getPackageName());
        bundle.putInt("screen_x", g());
        bundle.putInt("screen_y", i());
        bundle.putString("appid", y);
        bundle.putString("uid", z);
        if (a != null) {
            a.a(bundle);
        }
    }

    private static void e(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            f = VersionInfo.getApiVersion();
            g = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            f = "1.0.0";
            g = 1;
        }
    }

    public static String f() {
        return d;
    }

    private static void f(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = windowManager != null ? windowManager.getDefaultDisplay() : null;
        if (defaultDisplay != null) {
            h = defaultDisplay.getWidth();
            i = defaultDisplay.getHeight();
            defaultDisplay.getMetrics(displayMetrics);
        }
        C = displayMetrics.density;
        j = (int) displayMetrics.xdpi;
        k = (int) displayMetrics.ydpi;
        if (B > 3) {
            l = displayMetrics.densityDpi;
        } else {
            l = 160;
        }
        if (l == 0) {
            l = 160;
        }
    }

    public static int g() {
        return h;
    }

    private static void g(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            m = j(context);
            n = telephonyManager.getSubscriberId();
            o = a(context);
        }
    }

    public static String h() {
        return f;
    }

    private static void h(Context context) {
        q = Secure.getString(context.getContentResolver(), "android_id");
    }

    public static int i() {
        return i;
    }

    private static void i(Context context) {
        if (((TelephonyManager) context.getSystemService("phone")) != null) {
            r = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        }
    }

    public static String j() {
        return e;
    }

    private static String j(Context context) {
        Exception e;
        String str = "imei.dat";
        String str2;
        try {
            if (new File(context.getFilesDir().getAbsolutePath() + "/" + str).exists()) {
                D = true;
                FileInputStream openFileInput = context.openFileInput(str);
                byte[] bArr = new byte[openFileInput.available()];
                openFileInput.read(bArr);
                openFileInput.close();
                str = new String(bArr);
                try {
                    return str.substring(0, str.indexOf(124));
                } catch (Exception e2) {
                    e = e2;
                    str2 = str;
                }
            } else {
                D = false;
                str2 = k(context);
                try {
                    FileOutputStream openFileOutput = context.openFileOutput(str, 32768);
                    openFileOutput.write(str2.getBytes());
                    openFileOutput.close();
                    return str2;
                } catch (Exception e3) {
                    e = e3;
                }
            }
        } catch (Exception e22) {
            Exception exception = e22;
            str2 = null;
            e = exception;
            e.printStackTrace();
            return str2;
        }
    }

    public static int k() {
        return j;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0015  */
    private static java.lang.String k(android.content.Context r2) {
        /*
        r1 = 0;
        r0 = "phone";
        r0 = r2.getSystemService(r0);	 Catch:{ Exception -> 0x0018 }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x0018 }
        if (r0 == 0) goto L_0x001c;
    L_0x000b:
        r0 = r0.getDeviceId();	 Catch:{ Exception -> 0x0018 }
    L_0x000f:
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 == 0) goto L_0x0017;
    L_0x0015:
        r0 = "000000000000000";
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x001c:
        r0 = r1;
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.d.c.k(android.content.Context):java.lang.String");
    }

    public static int l() {
        return k;
    }

    private static void l(Context context) {
        p = a.a(context);
    }

    public static int m() {
        return l;
    }

    public static String n() {
        return m;
    }

    public static String o() {
        return s;
    }

    public static String p() {
        return c;
    }

    public static String q() {
        return x;
    }

    public static String r() {
        return v;
    }

    public static String s() {
        return w;
    }

    private static void t() {
        if (a != null) {
            a.a();
        }
    }
}
