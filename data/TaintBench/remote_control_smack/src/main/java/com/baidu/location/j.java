package com.baidu.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.map.MKEvent;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

class j {
    public static float A = 2.2f;
    public static long B = 20;
    public static float C = 3.8f;
    public static float D = 0.5f;
    public static int E = 0;
    public static int F = 16;
    public static boolean G = true;
    public static float H = 200.0f;
    public static boolean I = false;
    public static boolean J = true;
    public static int K = 120;
    public static int L = 100;
    public static boolean M = true;
    public static int N = 30;
    public static double O = 0.0d;
    public static float P = 0.1f;
    public static float Q = 10.0f;
    public static String R = "gcj02";
    public static float S = 0.0f;
    public static float T = 2.0f;
    public static int a = 10;
    public static boolean b = false;
    /* renamed from: byte */
    public static double f198byte = 0.0d;
    public static double c = 0.0d;
    /* renamed from: case */
    public static float f199case = 1.1f;
    /* renamed from: char */
    public static float f200char = 2.3f;
    public static float d = 50.0f;
    /* renamed from: do */
    public static String f201do = "http://loc.map.baidu.com/sdk_ep.php";
    public static boolean e = false;
    /* renamed from: else */
    public static boolean f202else = false;
    public static String f = null;
    /* renamed from: for */
    private static String f203for = f.v;
    public static int g = MKEvent.ERROR_PERMISSION_DENIED;
    /* renamed from: goto */
    public static int f204goto = 0;
    public static int h = 5000;
    public static int i = 20;
    /* renamed from: if */
    public static int f205if = 7;
    /* renamed from: int */
    public static int f206int = 20;
    public static String j = "no";
    public static int k = 70;
    public static int l = LocationClientOption.MIN_SCAN_SPAN;
    /* renamed from: long */
    public static int f207long = 0;
    public static boolean m = true;
    private static boolean n = false;
    /* renamed from: new */
    public static float f208new = 0.9f;
    public static byte[] o = null;
    private static boolean p = true;
    public static long q = 300000;
    private static boolean r = false;
    public static double s = 0.0d;
    public static int t = 3;
    /* renamed from: try */
    public static boolean f209try = false;
    public static int u = 2;
    public static boolean v = false;
    /* renamed from: void */
    public static int f210void = 0;
    private static String w = "[baidu_location_service]";
    private static String x = "http://loc.map.baidu.com/sdk.php";
    private static Process y = null;
    public static long z = 1200000;

    public static class a {
        private static final boolean a = false;
        /* renamed from: if */
        private static final String f194if = a.class.getSimpleName();

        private static String a(Context context) {
            return b.a(context);
        }

        /* renamed from: if */
        public static String m963if(Context context) {
            String a = a(context);
            String str = b.m965do(context);
            if (TextUtils.isEmpty(str)) {
                str = "0";
            }
            return a + "|" + new StringBuffer(str).reverse().toString();
        }
    }

    public static class b {
        private static final String a = "a";
        /* renamed from: do */
        private static final String f195do = "bids";
        /* renamed from: for */
        private static final String f196for = "i";
        /* renamed from: if */
        private static final String f197if = "DeviceId";

        private b() {
        }

        public static String a(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(f195do, 0);
            String string = sharedPreferences.getString(f196for, null);
            if (string == null) {
                string = m965do(context);
                Editor edit = sharedPreferences.edit();
                edit.putString(f196for, string);
                edit.commit();
            }
            String string2 = sharedPreferences.getString(a, null);
            if (string2 == null) {
                string2 = m966if(context);
                Editor edit2 = sharedPreferences.edit();
                edit2.putString(a, string2);
                edit2.commit();
            }
            String str = "";
            str = j.a(("com.baidu" + string + string2).getBytes(), true);
            String string3 = System.getString(context.getContentResolver(), str);
            if (!TextUtils.isEmpty(string3)) {
                return string3;
            }
            string = j.a((string + string2 + UUID.randomUUID().toString()).getBytes(), true);
            System.putString(context.getContentResolver(), str, string);
            return !string.equals(System.getString(context.getContentResolver(), str)) ? str : string;
        }

        /* renamed from: do */
        public static String m965do(Context context) {
            String str = "";
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return str;
            }
            String deviceId = telephonyManager.getDeviceId();
            return TextUtils.isEmpty(deviceId) ? "" : deviceId;
        }

        /* renamed from: if */
        public static String m966if(Context context) {
            String str = "";
            str = Secure.getString(context.getContentResolver(), "android_id");
            return TextUtils.isEmpty(str) ? "" : str;
        }
    }

    j() {
    }

    static float a(String str, String str2, String str3) {
        float f = Float.MIN_VALUE;
        if (str == null || str.equals("")) {
            return f;
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return f;
        }
        indexOf += str2.length();
        int indexOf2 = str.indexOf(str3, indexOf);
        if (indexOf2 == -1) {
            return f;
        }
        String substring = str.substring(indexOf, indexOf2);
        if (substring == null || substring.equals("")) {
            return f;
        }
        try {
            return Float.parseFloat(substring);
        } catch (NumberFormatException e) {
            a(f203for, "util numberFormatException, intStr : " + substring);
            e.printStackTrace();
            return f;
        }
    }

    static String a() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        int i2 = instance.get(1);
        int i3 = instance.get(2) + 1;
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format("%d-%d-%d %d:%d:%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
    }

    public static String a(com.baidu.location.c.a aVar, c cVar, Location location, String str, int i) {
        String aVar2;
        StringBuffer stringBuffer = new StringBuffer();
        if (i != 0) {
            stringBuffer.append("&q=");
            stringBuffer.append(i);
        }
        if (aVar != null) {
            aVar2 = aVar.toString();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (cVar != null) {
            aVar2 = i == 0 ? cVar.m822byte() : cVar.m832try();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (location != null) {
            aVar2 = (f210void == 0 || i == 0) ? b.m777if(location) : b.k();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        boolean z = false;
        if (i == 0) {
            z = true;
        }
        aVar2 = c.a(z);
        if (aVar2 != null) {
            stringBuffer.append(aVar2);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        if (aVar != null) {
            aVar2 = aVar.m800int();
            if (aVar2 != null && aVar2.length() + stringBuffer.length() < 750) {
                stringBuffer.append(aVar2);
            }
        }
        aVar2 = stringBuffer.toString();
        a(f203for, "util format : " + aVar2);
        return aVar2;
    }

    static String a(String str, String str2, String str3, double d) {
        if (str == null || str.equals("")) {
            return null;
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return null;
        }
        indexOf += str2.length();
        int indexOf2 = str.indexOf(str3, indexOf);
        if (indexOf2 == -1) {
            return null;
        }
        String substring = str.substring(0, indexOf);
        substring = substring + String.format("%.7f", new Object[]{Double.valueOf(d)}) + str.substring(indexOf2);
        a(f203for, "NEW:" + substring);
        return substring;
    }

    public static String a(byte[] bArr, String str, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (z) {
                toHexString = toHexString.toUpperCase();
            }
            if (toHexString.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(toHexString).append(str);
        }
        return stringBuilder.toString();
    }

    public static String a(byte[] bArr, boolean z) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bArr);
            return a(instance.digest(), "", z);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void a(String str, String str2) {
        if (r) {
            Log.d(str, str2);
        }
    }

    public static boolean a(String str) {
        int i = m973if(str, "error\":\"", "\"");
        return i > 100 && i < 200;
    }

    /* renamed from: do */
    static double m969do(String str, String str2, String str3) {
        double d = Double.MIN_VALUE;
        if (str == null || str.equals("")) {
            return d;
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return d;
        }
        indexOf += str2.length();
        int indexOf2 = str.indexOf(str3, indexOf);
        if (indexOf2 == -1) {
            return d;
        }
        String substring = str.substring(indexOf, indexOf2);
        if (substring == null || substring.equals("")) {
            return d;
        }
        try {
            return Double.parseDouble(substring);
        } catch (NumberFormatException e) {
            a(f203for, "util numberFormatException, doubleStr : " + substring);
            e.printStackTrace();
            return d;
        }
    }

    /* renamed from: do */
    public static String m970do() {
        return x;
    }

    /* renamed from: do */
    public static void m971do(String str) {
        if ((r || n) && str != null) {
            x = str;
        }
    }

    /* renamed from: for */
    public static void m972for() {
        if (r || n) {
            try {
                if (y != null) {
                    y.destroy();
                    y = null;
                }
                File file = new File(f.aa);
                if (file.exists()) {
                    a("sdkdemo_applocation", "directory already exists...");
                } else {
                    file.mkdirs();
                    a("sdkdemo_applocation", "directory not exists, make dirs...");
                }
                a(f203for, "logcat start ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: if */
    static int m973if(String str, String str2, String str3) {
        int i = ExploreByTouchHelper.INVALID_ID;
        if (str == null || str.equals("")) {
            return i;
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return i;
        }
        indexOf += str2.length();
        int indexOf2 = str.indexOf(str3, indexOf);
        if (indexOf2 == -1) {
            return i;
        }
        String substring = str.substring(indexOf, indexOf2);
        if (substring == null || substring.equals("")) {
            return i;
        }
        try {
            return Integer.parseInt(substring);
        } catch (NumberFormatException e) {
            a(f203for, "util numberFormatException, intStr : " + substring);
            e.printStackTrace();
            return i;
        }
    }

    /* renamed from: if */
    static String m974if() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        int i2 = instance.get(1);
        int i3 = instance.get(2) + 1;
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format("%d_%d_%d_%d_%d_%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
    }

    /* renamed from: if */
    public static void m975if(String str) {
        if (p) {
            Log.d(w, str);
        }
    }

    /* renamed from: if */
    public static void m976if(String str, String str2) {
        if (n) {
            Log.d(str, str2);
        }
    }

    /* renamed from: int */
    public static void m977int() {
        if (y != null) {
            try {
                a(f203for, "logcat stop...");
                y.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
