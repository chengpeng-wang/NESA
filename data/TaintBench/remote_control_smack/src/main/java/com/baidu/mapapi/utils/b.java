package com.baidu.mapapi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class b {
    public static final String a = System.getProperty("file.separator");
    static String b;
    static String c;
    static String d;
    static String e;
    static int f;
    static int g;
    static int h;
    static String[] i;
    static String[] j;
    static String[] k;
    private static String l;

    public static String a() {
        String str = b + "/BaiduMapSDK";
        if (str.length() != 0) {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return str;
    }

    public static void a(Context context) {
        c = context.getFilesDir().getAbsolutePath();
        b(context);
        if (com.baidu.platform.comapi.d.b.a() == 0) {
            d = j();
            e = context.getCacheDir().getAbsolutePath();
            f = 20971520;
            g = 52428800;
            h = 5242880;
        } else {
            d = context.getCacheDir().getAbsolutePath();
            e = "";
            f = 10485760;
            g = 10485760;
            h = 5242880;
        }
        c(context);
    }

    public static boolean a(String str) {
        boolean createNewFile;
        IOException e;
        try {
            File file = new File(str + "/test.0");
            if (file.exists()) {
                file.delete();
            }
            createNewFile = file.createNewFile();
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                return createNewFile;
            }
        } catch (IOException e3) {
            IOException iOException = e3;
            createNewFile = false;
            e = iOException;
            e.printStackTrace();
            return createNewFile;
        }
        return createNewFile;
    }

    public static String b() {
        return d;
    }

    private static void b(Context context) {
        i.a(context);
        i = i.b;
        j = i.a;
        k = i.c;
        SharedPreferences sharedPreferences = context.getSharedPreferences("map_pref", 0);
        if (sharedPreferences.contains("selected_sdcard")) {
            b = sharedPreferences.getString("selected_sdcard", "/sdcard/");
            if (a(b)) {
                return;
            }
        }
        try {
            if (i == null || i.length <= 0) {
                b = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (b != null || b.trim().length() < 1) {
                    b = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                return;
            }
            b = i[0];
            if (b != null) {
            }
            b = Environment.getExternalStorageDirectory().getAbsolutePath();
        } catch (Exception e) {
            b = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static String c() {
        return e;
    }

    private static void c(Context context) {
        l = "";
    }

    public static int d() {
        return f;
    }

    public static int e() {
        return g;
    }

    public static int f() {
        return h;
    }

    public static File g() {
        return new File(b);
    }

    public static String h() {
        try {
            return g().getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean i() {
        return com.baidu.platform.comapi.d.b.a() != 0 ? false : a(h());
    }

    private static String j() {
        String str = "";
        if (i()) {
            str = h() + "/BaiduMapSDK/cache";
        }
        if (str.length() != 0) {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return str;
    }
}
