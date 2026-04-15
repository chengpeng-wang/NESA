package com.baidu.android.pushservice;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.android.common.security.AESUtil;
import com.baidu.android.common.security.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class PushSettings {
    private static Context a;

    public static String a() {
        if (a != null) {
            return System.getString(a.getContentResolver(), "com.baidu.pushservice.channel_id");
        }
        Log.e("PushSettings", "mContext == null");
        return "";
    }

    public static void a(int i) {
        if (a == null) {
            Log.w("PushSettings", "setCurPeriod mContext == null");
        } else {
            System.putInt(a.getContentResolver(), "com.baidu.pushservice.cur_period", i);
        }
    }

    public static void a(long j) {
        if (a == null) {
            Log.w("PushSettings", "setLastSendStatisticTime mContext == null");
        } else {
            System.putLong(a.getContentResolver(), "com.baidu.pushservice.cst", j);
        }
    }

    public static void a(Context context) {
        a = context;
    }

    public static void a(Context context, String str) {
        if (context == null) {
            Log.w("PushSettings", "removeUninstalledAppLbsSwitch mContext == null");
        } else if (!TextUtils.isEmpty(str)) {
            String string = System.getString(context.getContentResolver(), "com.baidu.pushservice.le");
            if (!TextUtils.isEmpty(string)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String str2 : string.trim().split(",")) {
                    if (!str2.equals(str)) {
                        stringBuilder.append(str2 + ",");
                    }
                }
                System.putString(context.getContentResolver(), "com.baidu.pushservice.le", stringBuilder.toString());
            }
        }
    }

    public static void a(Context context, boolean z) {
        Object obj = null;
        if (context == null) {
            Log.w("PushSettings", "setLbsEnabled mContext == null");
        } else if (TextUtils.isEmpty(context.getPackageName())) {
            Log.w("PushSettings", "mContext.getPackageName() == null");
        } else {
            String string = System.getString(context.getContentResolver(), "com.baidu.pushservice.le");
            if (!TextUtils.isEmpty(string)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : string.trim().split(",")) {
                    if (str.equals(context.getPackageName())) {
                        obj = 1;
                        if (!z) {
                        }
                    }
                    stringBuilder.append(str + ",");
                }
                if (obj == null) {
                    stringBuilder.append(context.getPackageName() + ",");
                }
                System.putString(context.getContentResolver(), "com.baidu.pushservice.le", stringBuilder.toString());
            } else if (z) {
                System.putString(context.getContentResolver(), "com.baidu.pushservice.le", context.getPackageName() + ",");
            }
        }
    }

    public static void a(String str) {
        if (a == null) {
            Log.e("PushSettings", "setChannelId mContext == null");
        } else {
            System.putString(a.getContentResolver(), "com.baidu.pushservice.channel_id", str);
        }
    }

    public static void a(String str, int i, String str2) {
        if (a == null) {
            Log.e("PushSettings", "setApiInfo mContext == null");
        } else if (i == 9) {
            try {
                HashMap g = g();
                if (g != null && g.containsKey("com.baidu.pushservice" + str)) {
                    g.remove("com.baidu.pushservice" + str);
                    a(g);
                    System.putString(a.getContentResolver(), "com.baidu.pushservice" + str, "");
                }
            } catch (Exception e) {
                Log.d("PushSettings", "set appInfo exception");
            }
        } else {
            CharSequence encode;
            try {
                encode = Base64.encode(AESUtil.encrypt("2011121211143000", "9876543210123456", (i + str2).getBytes()), "utf-8");
            } catch (Exception e2) {
                encode = "";
                Log.i("PushSettings", "setAppInfo exception");
            }
            if (!TextUtils.isEmpty(encode)) {
                HashMap hashMap = null;
                try {
                    hashMap = g();
                } catch (Exception e3) {
                    Log.i("PushSettings", "set AppInfo exception" + e3.toString());
                }
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                if (!hashMap.containsKey("com.baidu.pushservice" + str)) {
                    hashMap.put("com.baidu.pushservice" + str, encode);
                    a(hashMap);
                }
                System.putString(a.getContentResolver(), "com.baidu.pushservice" + str, encode);
            }
        }
    }

    private static void a(HashMap hashMap) {
        try {
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(absolutePath, "baidu/pushservice/files");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(absolutePath, "baidu/pushservice/files/apps"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.i("PushSettings", "setAppInfo read file exception");
        }
    }

    public static String b() {
        if (a != null) {
            return System.getString(a.getContentResolver(), "com.baidu.pushservice.channel_token_rsa");
        }
        Log.e("PushSettings", "getChannelToken mContext == null");
        return "";
    }

    public static String b(String str) {
        if (a == null) {
            Log.e("PushSettings", "setApiInfo mContext == null");
            return "";
        }
        CharSequence string = System.getString(a.getContentResolver(), "com.baidu.pushservice" + str);
        if (TextUtils.isEmpty(string)) {
            try {
                string = (String) g().get("com.baidu.pushservice" + str);
            } catch (Exception e) {
                return "";
            }
        }
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            return new String(AESUtil.decrypt("2011121211143000", "9876543210123456", Base64.decode(string.getBytes())));
        } catch (Exception e2) {
            return "";
        }
    }

    public static void b(int i) {
        if (a == null) {
            Log.w("PushSettings", "setStatisticSendDisabled mContext == null");
        } else {
            System.putInt(a.getContentResolver(), "com.baidu.pushservice.sd", i);
        }
    }

    public static void b(long j) {
        if (a == null) {
            Log.w("PushSettings", "setLastSendLbsTime mContext == null");
        } else {
            System.putLong(a.getContentResolver(), "com.baidu.pushservice.clt", j);
        }
    }

    public static boolean b(Context context) {
        if (context == null) {
            Log.e("PushSettings", "getConnectState, context == null");
            return true;
        }
        try {
            return System.getInt(context.getContentResolver(), "com.baidu.pushservice.PushSettings.connect_state") == 1;
        } catch (SettingNotFoundException e) {
            Log.w("PushSettings", "com.baidu.pushservice.PushSettings.connect_state setting is not set.");
            return true;
        }
    }

    public static long c(Context context) {
        long j = 0;
        if (context == null) {
            Log.e("PushSettings", "getLastSendStatisticTime mContext == null");
            return j;
        }
        try {
            return System.getLong(context.getContentResolver(), "com.baidu.pushservice.cst");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return j;
        }
    }

    public static void c(long j) {
        if (a == null) {
            Log.w("PushSettings", "setLastSendStatisticTime mContext == null");
        } else {
            System.putLong(a.getContentResolver(), "com.baidu.pushservice.st", j);
        }
    }

    public static void c(String str) {
        if (a == null) {
            Log.e("PushSettings", "setChannelToken mContext == null");
        } else {
            System.putString(a.getContentResolver(), "com.baidu.pushservice.channel_token_rsa", str);
        }
    }

    public static boolean c() {
        if (a == null) {
            return false;
        }
        try {
            return System.getInt(a.getContentResolver(), "com.baidu.android.pushservice.PushSettings.debug_mode") == 1;
        } catch (SettingNotFoundException e) {
            return false;
        }
    }

    public static int d() {
        int i = 0;
        if (a == null) {
            Log.e("PushSettings", "getCurPeriod mContext == null");
            return i;
        }
        try {
            return System.getInt(a.getContentResolver(), "com.baidu.pushservice.cur_period");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static long d(Context context) {
        long j = 0;
        if (context == null) {
            Log.e("PushSettings", "getLastSendLbsTime mContext == null");
            return j;
        }
        try {
            return System.getLong(context.getContentResolver(), "com.baidu.pushservice.clt");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return j;
        }
    }

    public static int e() {
        return System.getInt(a.getContentResolver(), "com.baidu.pushservice.sd", 0);
    }

    public static void e(Context context) {
        if (context == null) {
            Log.w("PushSettings", "refreshLbsSwitchInfo mContext == null");
            return;
        }
        String string = System.getString(context.getContentResolver(), "com.baidu.pushservice.le");
        if (!TextUtils.isEmpty(string)) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] split = string.trim().split(",");
            PackageManager packageManager = context.getPackageManager();
            for (String str : split) {
                PackageInfo packageInfo = null;
                try {
                    packageInfo = packageManager.getPackageInfo(str, 0);
                } catch (NameNotFoundException e) {
                    Log.w("PushSettings", Log.getStackTraceString(e));
                }
                if (packageInfo != null) {
                    stringBuilder.append(str + ",");
                }
            }
            System.putString(a.getContentResolver(), "com.baidu.pushservice.le", stringBuilder.toString());
        }
    }

    public static void enableDebugMode(Context context, boolean z) {
        if (context == null) {
            Log.e("PushSettings", "enableDebugMode context == null");
        } else if (z) {
            System.putInt(context.getContentResolver(), "com.baidu.android.pushservice.PushSettings.debug_mode", 1);
        } else {
            System.putInt(context.getContentResolver(), "com.baidu.android.pushservice.PushSettings.debug_mode", 0);
        }
    }

    public static boolean f() {
        return !TextUtils.isEmpty(System.getString(a.getContentResolver(), "com.baidu.pushservice.le"));
    }

    private static HashMap g() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "baidu/pushservice/files");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "apps");
        if (!file2.exists()) {
            return null;
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file2));
        HashMap hashMap = new HashMap();
        hashMap = (HashMap) objectInputStream.readObject();
        objectInputStream.close();
        return hashMap;
    }
}
