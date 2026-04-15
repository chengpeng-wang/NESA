package com.mobclick.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.Deflater;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONObject;

public class l {
    static String a = "utf-8";
    public static int b;

    public static int a(Context context, String str, String str2) {
        try {
            Field field = Class.forName(context.getPackageName() + ".R$" + str).getField(str2);
            return Integer.parseInt(field.get(field.getName()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int a(Date date, Date date2) {
        Date date3;
        Date date4;
        if (date.after(date2)) {
            date3 = date;
            date4 = date2;
        } else {
            date3 = date2;
            date4 = date;
        }
        return (int) ((date3.getTime() - date4.getTime()) / 1000);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b  */
    public static java.lang.String a() {
        /*
        r0 = 0;
        r1 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x0039 }
        r2 = "/proc/cpuinfo";
        r1.<init>(r2);	 Catch:{ FileNotFoundException -> 0x0039 }
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x002c }
        r3 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2.<init>(r1, r3);	 Catch:{ IOException -> 0x002c }
        r0 = r2.readLine();	 Catch:{ IOException -> 0x002c }
        r2.close();	 Catch:{ IOException -> 0x004d, FileNotFoundException -> 0x0046 }
        r1.close();	 Catch:{ IOException -> 0x004d, FileNotFoundException -> 0x0046 }
    L_0x0019:
        if (r0 == 0) goto L_0x0027;
    L_0x001b:
        r1 = 58;
        r1 = r0.indexOf(r1);
        r1 = r1 + 1;
        r0 = r0.substring(r1);
    L_0x0027:
        r0 = r0.trim();
        return r0;
    L_0x002c:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0030:
        r2 = "MobclickAgent";
        r3 = "Could not read from file /proc/cpuinfo";
        android.util.Log.e(r2, r3, r0);	 Catch:{ FileNotFoundException -> 0x004b }
        r0 = r1;
        goto L_0x0019;
    L_0x0039:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x003d:
        r2 = "MobclickAgent";
        r3 = "Could not open file /proc/cpuinfo";
        android.util.Log.e(r2, r3, r0);
        r0 = r1;
        goto L_0x0019;
    L_0x0046:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x003d;
    L_0x004b:
        r0 = move-exception;
        goto L_0x003d;
    L_0x004d:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.l.a():java.lang.String");
    }

    public static String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toHexString(b & 255));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String a(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static void a(Context context) {
        Toast.makeText(context, context.getString(a(context, "string", "UMToast_IsUpdating")), 0).show();
    }

    public static void a(Context context, Date date) {
        Editor edit = context.getSharedPreferences("exchange_last_request_time", 0).edit();
        edit.putString("last_request_time", a(date));
        edit.commit();
    }

    public static boolean a(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public static String[] a(GL10 gl10) {
        try {
            String[] strArr = new String[2];
            String glGetString = gl10.glGetString(7936);
            String glGetString2 = gl10.glGetString(7937);
            strArr[0] = glGetString;
            strArr[1] = glGetString2;
            return strArr;
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, "Could not read gpu infor:", e);
            return new String[0];
        }
    }

    public static String b() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String b(Context context) {
        String str = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                String string = applicationInfo.metaData.getString("UMENG_APPKEY");
                if (string != null) {
                    str = string;
                } else {
                    Log.i(UmengConstants.LOG_TAG, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.trim();
    }

    public static byte[] b(String str) {
        b = 0;
        Deflater deflater = new Deflater();
        deflater.setInput(str.getBytes(a));
        deflater.finish();
        byte[] bArr = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (!deflater.finished()) {
            int deflate = deflater.deflate(bArr);
            b += deflate;
            byteArrayOutputStream.write(bArr, 0, deflate);
        }
        deflater.end();
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0025  */
    public static java.lang.String c(android.content.Context r3) {
        /*
        r0 = "phone";
        r0 = r3.getSystemService(r0);
        r0 = (android.telephony.TelephonyManager) r0;
        if (r0 != 0) goto L_0x0011;
    L_0x000a:
        r1 = "MobclickAgent";
        r2 = "No IMEI.";
        android.util.Log.w(r1, r2);
    L_0x0011:
        r1 = "";
        r2 = "android.permission.READ_PHONE_STATE";
        r2 = a(r3, r2);	 Catch:{ Exception -> 0x003b }
        if (r2 == 0) goto L_0x003f;
    L_0x001b:
        r0 = r0.getDeviceId();	 Catch:{ Exception -> 0x003b }
    L_0x001f:
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 == 0) goto L_0x0041;
    L_0x0025:
        r0 = "MobclickAgent";
        r1 = "No IMEI.";
        android.util.Log.w(r0, r1);
        r0 = i(r3);
        if (r0 != 0) goto L_0x0041;
    L_0x0032:
        r0 = "MobclickAgent";
        r1 = "Failed to take mac as IMEI.";
        android.util.Log.w(r0, r1);
        r0 = 0;
    L_0x003a:
        return r0;
    L_0x003b:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x003f:
        r0 = r1;
        goto L_0x001f;
    L_0x0041:
        r0 = a(r0);
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.l.c(android.content.Context):java.lang.String");
    }

    public static Date c(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date d(Context context) {
        return c(context.getSharedPreferences("exchange_last_request_time", 0).getString("last_request_time", "1900-01-01 00:00:00"));
    }

    public static JSONObject e(Context context) {
        JSONObject jSONObject = new JSONObject();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String c = c(context);
        if (c == null || c.equals("")) {
            Log.e(UmengConstants.LOG_TAG, "No device id");
            return null;
        }
        jSONObject.put("idmd5", c);
        jSONObject.put(UmengConstants.AtomKey_DeviceModel, Build.MODEL);
        c = b(context);
        if (c == null) {
            Log.e(UmengConstants.LOG_TAG, "No appkey");
            return null;
        }
        int i;
        jSONObject.put(UmengConstants.AtomKey_AppKey, c);
        jSONObject.put("channel", g(context));
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String str = packageInfo.versionName;
            i = packageInfo.versionCode;
            jSONObject.put(UmengConstants.AtomKey_AppVersion, str);
            jSONObject.put("version_code", i);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            jSONObject.put(UmengConstants.AtomKey_AppVersion, "unknown");
            jSONObject.put("version_code", "unknown");
        }
        try {
            jSONObject.put("sdk_type", "Android");
            jSONObject.put(UmengConstants.AtomKey_SDK_Version, UmengConstants.SDK_VERSION);
            jSONObject.put("os", "Android");
            jSONObject.put(UmengConstants.AtomKey_OSVersion, VERSION.RELEASE);
            Configuration configuration = new Configuration();
            System.getConfiguration(context.getContentResolver(), configuration);
            if (configuration == null || configuration.locale == null) {
                jSONObject.put("country", "Unknown");
                jSONObject.put("language", "Unknown");
                jSONObject.put("timezone", 8);
            } else {
                jSONObject.put("country", configuration.locale.getCountry());
                jSONObject.put("language", configuration.locale.toString());
                Calendar instance = Calendar.getInstance(configuration.locale);
                if (instance != null) {
                    TimeZone timeZone = instance.getTimeZone();
                    if (timeZone != null) {
                        jSONObject.put("timezone", timeZone.getRawOffset() / 3600000);
                    } else {
                        jSONObject.put("timezone", 8);
                    }
                } else {
                    jSONObject.put("timezone", 8);
                }
            }
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
                i = displayMetrics.widthPixels;
                jSONObject.put("resolution", new StringBuilder(String.valueOf(String.valueOf(displayMetrics.heightPixels))).append("*").append(String.valueOf(i)).toString());
            } catch (Exception e2) {
                e2.printStackTrace();
                jSONObject.put("resolution", "Unknown");
            }
            try {
                String[] f = f(context);
                jSONObject.put("access", f[0]);
                if (f[0].equals("2G/3G")) {
                    jSONObject.put("access_subtype", f[1]);
                }
            } catch (Exception e22) {
                e22.printStackTrace();
                jSONObject.put("access", "Unknown");
            }
            try {
                jSONObject.put("carrier", telephonyManager.getNetworkOperatorName());
            } catch (Exception e3) {
                e3.printStackTrace();
                jSONObject.put("carrier", "Unknown");
            }
            if (UmengConstants.LOCATION_OPEN) {
                Location j = j(context);
                if (j != null) {
                    jSONObject.put(UmengConstants.AtomKey_Lat, String.valueOf(j.getLatitude()));
                    jSONObject.put(UmengConstants.AtomKey_Lng, String.valueOf(j.getLongitude()));
                } else {
                    jSONObject.put(UmengConstants.AtomKey_Lat, 0.0d);
                    jSONObject.put(UmengConstants.AtomKey_Lng, 0.0d);
                }
            }
            jSONObject.put("cpu", a());
            if (!MobclickAgent.GPU_VENDER.equals("")) {
                jSONObject.put("gpu_vender", MobclickAgent.GPU_VENDER);
            }
            if (!MobclickAgent.GPU_RENDERER.equals("")) {
                jSONObject.put("gpu_renderer", MobclickAgent.GPU_RENDERER);
            }
            return jSONObject;
        } catch (Exception e32) {
            e32.printStackTrace();
            return null;
        }
    }

    public static String[] f(Context context) {
        String[] strArr = new String[]{"Unknown", "Unknown"};
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            strArr[0] = "Unknown";
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                strArr[0] = "Unknown";
            } else if (connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED) {
                strArr[0] = "Wi-Fi";
            } else {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
                if (networkInfo.getState() == State.CONNECTED) {
                    strArr[0] = "2G/3G";
                    strArr[1] = networkInfo.getSubtypeName();
                }
            }
        }
        return strArr;
    }

    public static String g(Context context) {
        String str = "Unknown";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return str;
            }
            Object obj = applicationInfo.metaData.get("UMENG_CHANNEL");
            if (obj == null) {
                return str;
            }
            String obj2 = obj.toString();
            if (obj2 != null) {
                return obj2;
            }
            Log.i(UmengConstants.LOG_TAG, "Could not read UMENG_CHANNEL meta-data from AndroidManifest.xml.");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean h(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager == null ? false : connectivityManager.getNetworkInfo(0).getState() == State.CONNECTED ? true : connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED;
    }

    private static String i(Context context) {
        String str = null;
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            Log.i(UmengConstants.LOG_TAG, "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?", e);
            return str;
        }
    }

    private static Location j(Context context) {
        return new e(context).a();
    }
}
