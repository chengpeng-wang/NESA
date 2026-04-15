package com.baidu.android.pushservice.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.LocalServerSocket;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.util.DeviceId;
import com.baidu.android.common.util.Util;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b.b;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.cookie.ClientCookie;

public final class m {
    private static final String[] a = new String[]{"android.permission.INTERNET", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.WRITE_SETTINGS", "android.permission.VIBRATE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.DISABLE_KEYGUARD", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_WIFI_STATE"};

    public static PackageInfo a(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 64);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static b a(b bVar, Context context, String str) {
        PackageInfo a = a(context, str);
        if (a != null) {
            bVar.e(a.applicationInfo.loadLabel(context.getPackageManager()).toString());
            bVar.g(a.versionName);
            bVar.a(a.versionCode);
            bVar.f(i(context, str));
            bVar.b(h(context, str));
        }
        return bVar;
    }

    public static String a(long j) {
        StringBuffer stringBuffer = new StringBuffer();
        long currentTimeMillis = System.currentTimeMillis() - j;
        long ceil = (long) Math.ceil((((double) currentTimeMillis) * 1.0d) / 1000.0d);
        long ceil2 = (long) Math.ceil((double) (((float) (currentTimeMillis / 60)) / 1000.0f));
        long ceil3 = (long) Math.ceil((double) (((float) ((currentTimeMillis / 60) / 60)) / 1000.0f));
        currentTimeMillis = (long) Math.ceil((double) (((float) (((currentTimeMillis / 24) / 60) / 60)) / 1000.0f));
        if (currentTimeMillis - 1 > 3) {
            stringBuffer.append(new SimpleDateFormat("MM月dd日").format(new Date(j)));
        } else if (currentTimeMillis - 1 > 0) {
            stringBuffer.append(currentTimeMillis + "天前");
        } else if (ceil3 - 1 > 0) {
            if (ceil3 >= 24) {
                stringBuffer.append("1天前");
            } else {
                stringBuffer.append(ceil3 + "小时前");
            }
        } else if (ceil2 - 1 > 0) {
            if (ceil2 == 60) {
                stringBuffer.append("1小时前");
            } else {
                stringBuffer.append(ceil2 + "分钟前");
            }
        } else if (ceil - 1 <= 0) {
            stringBuffer.append("刚刚");
        } else if (ceil == 60) {
            stringBuffer.append("1分钟前");
        } else {
            stringBuffer.append(ceil + "秒前");
        }
        return stringBuffer.toString();
    }

    public static String a(Context context, String str, String str2) {
        if (context == null) {
            Log.i("Utility", "getMetaData context == null");
            return null;
        }
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (NameNotFoundException e) {
            Log.e("getMetaDataString", "--- " + str + " GetMetaData Exception:\r\n", e);
            applicationInfo = null;
        }
        return (applicationInfo == null || applicationInfo.metaData == null) ? null : applicationInfo.metaData.getString(str2);
    }

    public static String a(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        th.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }

    public static void a(Context context, long j) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("Utility", ">>> setAlarmForRestart");
        }
        Context applicationContext = context.getApplicationContext();
        a(applicationContext, PushConstants.createMethodIntent(applicationContext), j);
    }

    public static void a(Context context, Intent intent, long j) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("Utility", ">>> setAlarmForSendInent : \r\n" + intent);
        }
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 268435456);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        alarmManager.cancel(broadcast);
        alarmManager.set(3, SystemClock.elapsedRealtime() + j, broadcast);
    }

    public static void a(Context context, boolean z) {
        if (f(context, context.getPackageName())) {
            com.baidu.android.pushservice.b.b(context, z);
            c(context, true);
            g(context, context.getPackageName());
        }
    }

    public static synchronized void a(String str) {
        synchronized (m.class) {
            String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            String concat = format.substring(0, 4).concat(format.substring(5, 7)).concat(format.substring(8, 10));
            format = format + " " + str + "\n\r";
            try {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(absolutePath, "baidu/pushservice/files");
                if (file.exists()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                    for (File file2 : file.listFiles()) {
                        if (file2.getName().startsWith("msg") && Integer.parseInt(concat) - Integer.parseInt(simpleDateFormat.format(Long.valueOf(file2.lastModified()))) >= 7) {
                            file2.delete();
                        }
                    }
                } else {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(absolutePath, "baidu/pushservice/files/msg" + concat + ".log"), true);
                fileOutputStream.write(format.getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public static boolean a(Context context) {
        File file = new File("/data/data/root");
        try {
            file.createNewFile();
            if (!file.exists()) {
                return true;
            }
            file.delete();
            return true;
        } catch (IOException e) {
            return (a(context, "com.noshufou.android.su") == null && a(context, "com.miui.uac") == null) ? false : true;
        }
    }

    static boolean a(String str, List list) {
        int i = 0;
        while (i < list.size()) {
            if (str != null && str.equals(((ResolveInfo) list.get(i)).activityInfo.name)) {
                return ((ResolveInfo) list.get(i)).activityInfo.enabled;
            } else {
                i++;
            }
        }
        return false;
    }

    static boolean a(String str, String[] strArr) {
        for (Object equals : strArr) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static Intent b(Context context, String str) {
        n nVar = new n();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(str);
        Intent registerReceiver = context.registerReceiver(nVar, intentFilter);
        context.unregisterReceiver(nVar);
        return registerReceiver;
    }

    public static String b(String str) {
        if (!TextUtils.isDigitsOnly(str)) {
            return "0";
        }
        BigInteger bigInteger = new BigInteger(str);
        if (bigInteger.and(new BigInteger("0800000000000000", 16)).equals(BigInteger.ZERO)) {
            bigInteger = bigInteger.xor(new BigInteger("282335"));
            bigInteger = bigInteger.and(new BigInteger("00ff0000", 16)).shiftLeft(8).add(bigInteger.and(new BigInteger("000000ff", 16)).shiftLeft(16)).add(bigInteger.and(new BigInteger("ff000000", 16)).shiftRight(16).and(new BigInteger("0000ff00", 16))).add(bigInteger.and(new BigInteger("0000ff00", 16)).shiftRight(8));
        } else {
            System.out.println("encode =  1");
            bigInteger = bigInteger.xor(new BigInteger("22727017042830095"));
            bigInteger = bigInteger.and(new BigInteger("000000ff00000000", 16)).shiftLeft(16).add(bigInteger.and(new BigInteger("000000000000ffff", 16)).shiftLeft(32)).add(bigInteger.and(new BigInteger("00ffff0000000000", 16)).shiftRight(24).and(new BigInteger("00000000ffff0000", 16))).add(bigInteger.and(new BigInteger("00000000ffff0000", 16)).shiftRight(16)).add(bigInteger.and(new BigInteger("ff00000000000000", 16)));
        }
        return bigInteger.toString();
    }

    public static void b(Context context, boolean z) {
        if (f(context, context.getPackageName())) {
            com.baidu.android.pushservice.b.c(context, z);
        }
    }

    public static boolean b(Context context, String str, String str2) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (NameNotFoundException e) {
            Log.e("getMetaDataBoolean", "--- " + str + " GetMetaData Exception:\r\n", e);
            applicationInfo = null;
        }
        return (applicationInfo == null || applicationInfo.metaData == null) ? false : applicationInfo.metaData.getBoolean(str2);
    }

    public static int[] b(Context context) {
        int[] iArr = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        iArr[0] = displayMetrics.heightPixels;
        iArr[1] = displayMetrics.widthPixels;
        return iArr;
    }

    public static void c(Context context, boolean z) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("Utility", "updateServiceInfo2  isForce =" + z);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("pst", 0);
        int d = d(context, context.getPackageName());
        if (sharedPreferences.getInt("pr_app_v", 0) < d || z) {
            Editor edit = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1).edit();
            if (c(context)) {
                edit.putLong("priority2", 0);
            } else if (m(context)) {
                edit.putLong("priority2", k(context));
            } else {
                edit.putLong("priority2", 0);
            }
            edit.putInt("version2", 13);
            edit.commit();
            Editor edit2 = sharedPreferences.edit();
            edit2.putInt("pr_app_v", d);
            edit2.commit();
        }
    }

    public static boolean c(Context context) {
        String c = com.baidu.android.pushservice.b.c(context);
        boolean b = "enabled".equals(c) ? false : "disabled".equals(c) ? true : b(context, context.getPackageName(), "DisableService");
        if (com.baidu.android.pushservice.b.a()) {
            Log.i("Utility", "--- isDisableService : " + b);
        }
        return b;
    }

    public static boolean c(Context context, String str) {
        try {
            return (context.getPackageManager().getApplicationInfo(str, 0).flags & 1) != 0;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean c(Context context, String str, String str2) {
        Intent intent = new Intent(str);
        intent.setPackage(context.getPackageName());
        return a(str2, context.getPackageManager().queryBroadcastReceivers(intent, 0));
    }

    public static int d(Context context, String str) {
        PackageInfo a = a(context, str);
        return a != null ? a.versionCode : 0;
    }

    public static Intent d(Context context) {
        Intent intent = new Intent(PushConstants.ACTION_METHOD);
        intent.addFlags(32);
        intent.putExtra(PushConstants.EXTRA_APP, PendingIntent.getBroadcast(context, 0, new Intent(), 0));
        intent.putExtra("method_version", "V2");
        return intent;
    }

    public static Intent e(Context context) {
        return d(context);
    }

    public static boolean e(Context context, String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (NameNotFoundException e) {
            Log.e("isBaiduApp", "--- " + str + " GetMetaData Exception:\r\n", e);
            applicationInfo = null;
        }
        return (applicationInfo == null || applicationInfo.metaData == null) ? false : applicationInfo.metaData.getBoolean("IsBaiduApp");
    }

    public static void f(Context context) {
        c(context, false);
    }

    public static boolean f(Context context, String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (NameNotFoundException e) {
            Log.e("isEnableInternal", "--- " + str + " GetMetaData Exception:\r\n", e);
            applicationInfo = null;
        }
        return (applicationInfo == null || applicationInfo.metaData == null) ? false : applicationInfo.metaData.getBoolean("EnablePrivate");
    }

    public static void g(Context context, String str) {
        if (f(context, context.getPackageName())) {
            Intent createMethodIntent = PushConstants.createMethodIntent(context);
            createMethodIntent.putExtra("method", "pushservice_restart");
            createMethodIntent.setPackage(str);
            context.sendBroadcast(createMethodIntent);
            com.baidu.android.pushservice.b.a(context, false);
            return;
        }
        j(context);
    }

    static boolean g(Context context) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr == null) {
                Log.e("Utility", "Permissions Push-SDK need are not exist !");
                return false;
            }
            int i = 0;
            while (i < a.length) {
                if (a(a[i], strArr)) {
                    i++;
                } else {
                    Log.e("Utility", a[i] + " permission Push-SDK need is not exist !");
                    return false;
                }
            }
            return true;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int h(Context context, String str) {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = context.createPackageContext(str, 2).getSharedPreferences(str + ".push_sync", 1);
        } catch (NameNotFoundException e) {
            Log.e("Utility", e.getMessage());
        }
        if (sharedPreferences == null) {
            Log.w("Utility", "App:" + str + " doesn't init Version!");
            return 0;
        }
        int i = sharedPreferences.getInt("version2", 0);
        return i > 0 ? i : sharedPreferences.getInt(ClientCookie.VERSION_ATTR, 0);
    }

    static boolean h(Context context) {
        if (!c(context, "android.intent.action.BOOT_COMPLETED", "com.baidu.android.pushservice.PushServiceReceiver")) {
            Log.e("Utility", "com.baidu.android.pushservice.PushServiceReceiverdid not declaredandroid.intent.action.BOOT_COMPLETED");
            return false;
        } else if (!c(context, "android.net.conn.CONNECTIVITY_CHANGE", "com.baidu.android.pushservice.PushServiceReceiver")) {
            Log.e("Utility", "com.baidu.android.pushservice.PushServiceReceiverdid not declaredandroid.net.conn.CONNECTIVITY_CHANGE");
            return false;
        } else if (!c(context, "com.baidu.android.pushservice.action.notification.SHOW", "com.baidu.android.pushservice.PushServiceReceiver")) {
            Log.e("Utility", "com.baidu.android.pushservice.PushServiceReceiverdid not declaredcom.baidu.android.pushservice.action.notification.SHOW");
            return false;
        } else if (!c(context, PushConstants.ACTION_METHOD, "com.baidu.android.pushservice.RegistrationReceiver")) {
            Log.e("Utility", "com.baidu.android.pushservice.RegistrationReceiverdid not declaredcom.baidu.android.pushservice.action.METHOD");
            return false;
        } else if (c(context, "com.baidu.android.pushservice.action.BIND_SYNC", "com.baidu.android.pushservice.RegistrationReceiver")) {
            return true;
        } else {
            Log.e("Utility", "com.baidu.android.pushservice.RegistrationReceiverdid not declaredcom.baidu.android.pushservice.action.BIND_SYNC");
            return false;
        }
    }

    public static String i(Context context, String str) {
        return TextUtils.isEmpty(str) ? "" : a(context, str, "BaiduPush_CHANNEL");
    }

    public static void i(Context context) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("Utility", ">>> setAlarmForPeriodRestart");
        }
        a(context, 300000);
    }

    public static void j(Context context) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.i("Utility", "--- Start Service from" + context.getPackageName());
        }
        if (com.baidu.android.pushservice.b.b(context)) {
            Intent d = d(context);
            d.putExtra("method", "pushservice_restart");
            d.putExtra(PushConstants.PACKAGE_NAME, context.getPackageName());
            d.setPackage(null);
            context.sendBroadcast(d);
            d = d(context);
            d.putExtra("type", "service_restart");
            d.setPackage(null);
            context.sendBroadcast(d);
            com.baidu.android.pushservice.b.a(context, false);
            return;
        }
        String packageName = context.getPackageName();
        Intent d2 = d(context);
        d2.setPackage(packageName);
        context.sendBroadcast(d2);
    }

    public static long k(Context context) {
        long j = 13 << 1;
        if (e(context, context.getPackageName())) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("Utility", "--- get " + context + " PriorityVersion, baidu app");
            }
            j++;
        }
        j <<= 1;
        if (c(context, context.getPackageName())) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("Utility", "--- get " + context + " PriorityVersion, system app");
            }
            j++;
        }
        return j << 2;
    }

    static boolean l(Context context) {
        ServiceInfo[] serviceInfoArr = null;
        try {
            serviceInfoArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4).services;
        } catch (NameNotFoundException e) {
            Log.e("Utility", "Permissions Push-SDK package name not found !");
            e.printStackTrace();
        }
        if (serviceInfoArr == null) {
            Log.e("Utility", "Push-SDK PushService or MoPlusService need are not exist !");
            return false;
        }
        int i = 0;
        while (i < serviceInfoArr.length) {
            if ("com.baidu.android.pushservice.PushService".equals(serviceInfoArr[i].name)) {
                return serviceInfoArr[i].exported && serviceInfoArr[i].enabled;
            } else {
                i++;
            }
        }
        return false;
    }

    static boolean m(Context context) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("Utility", "check PushService AndroidManifest declearation !");
        }
        return g(context) && h(context) && l(context);
    }

    public static List n(Context context) {
        return context.getPackageManager().queryBroadcastReceivers(new Intent("com.baidu.android.pushservice.action.BIND_SYNC"), 0);
    }

    public static boolean o(Context context) {
        LocalServerSocket localServerSocket;
        boolean z = true;
        try {
            localServerSocket = new LocalServerSocket(p(context));
        } catch (Exception e) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("Utility", "--- Socket Adress (" + p(context) + ") in use --- @ " + context.getPackageName());
            }
            localServerSocket = null;
        }
        if (localServerSocket != null) {
            z = false;
            try {
                localServerSocket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return z;
    }

    public static String p(Context context) {
        return Util.toMd5(("com.baidu.pushservice.singelinstancev2" + DeviceId.getDeviceID(context)).getBytes(), false);
    }

    public static ArrayList q(Context context) {
        ArrayList t = t(context);
        List<RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(1000);
        ArrayList arrayList = new ArrayList();
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            String packageName = runningServiceInfo.service.getPackageName();
            if (!arrayList.contains(packageName) && t.contains(packageName)) {
                if (runningServiceInfo.service.getClassName().contains("PushService") || runningServiceInfo.service.getClassName().contains("MoPlusService")) {
                    SharedPreferences sharedPreferences;
                    try {
                        sharedPreferences = context.createPackageContext(packageName, 2).getSharedPreferences(packageName + ".push_sync", 1);
                    } catch (NameNotFoundException e) {
                        Log.e("Utility", e.getMessage());
                        sharedPreferences = null;
                    }
                    if (sharedPreferences == null) {
                        Log.w("Utility", "App:" + packageName + " doesn't init Version!");
                    } else if (sharedPreferences.getInt(ClientCookie.VERSION_ATTR, 0) > 0) {
                        arrayList.add(runningServiceInfo.service.getPackageName());
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0037  */
    public static int r(android.content.Context r4) {
        /*
        r2 = 0;
        r1 = "";
        if (r4 == 0) goto L_0x0084;
    L_0x0005:
        r0 = r4.getApplicationContext();
        r3 = "connectivity";
        r0 = r0.getSystemService(r3);
        r0 = (android.net.ConnectivityManager) r0;
        r3 = r0.getActiveNetworkInfo();
        if (r3 == 0) goto L_0x0084;
    L_0x0017:
        r0 = r3.isConnectedOrConnecting();
        if (r0 == 0) goto L_0x0084;
    L_0x001d:
        r0 = r3.getTypeName();
        r0 = r0.toLowerCase();
        r1 = "wifi";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0039;
    L_0x002d:
        r0 = "WF";
    L_0x002f:
        r1 = "WF";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0064;
    L_0x0037:
        r0 = 1;
    L_0x0038:
        return r0;
    L_0x0039:
        r0 = "2G";
        r1 = r3.getSubtype();
        switch(r1) {
            case 1: goto L_0x002f;
            case 2: goto L_0x002f;
            case 3: goto L_0x0043;
            case 4: goto L_0x002f;
            case 5: goto L_0x0049;
            case 6: goto L_0x004c;
            case 7: goto L_0x0046;
            case 8: goto L_0x004f;
            case 9: goto L_0x0055;
            case 10: goto L_0x0052;
            case 11: goto L_0x002f;
            case 12: goto L_0x005b;
            case 13: goto L_0x0061;
            case 14: goto L_0x0058;
            case 15: goto L_0x005e;
            default: goto L_0x0042;
        };
    L_0x0042:
        goto L_0x002f;
    L_0x0043:
        r0 = "3G";
        goto L_0x002f;
    L_0x0046:
        r0 = "3G";
        goto L_0x002f;
    L_0x0049:
        r0 = "3G";
        goto L_0x002f;
    L_0x004c:
        r0 = "3G";
        goto L_0x002f;
    L_0x004f:
        r0 = "3G";
        goto L_0x002f;
    L_0x0052:
        r0 = "3G";
        goto L_0x002f;
    L_0x0055:
        r0 = "3G";
        goto L_0x002f;
    L_0x0058:
        r0 = "3G";
        goto L_0x002f;
    L_0x005b:
        r0 = "3G";
        goto L_0x002f;
    L_0x005e:
        r0 = "3G";
        goto L_0x002f;
    L_0x0061:
        r0 = "4G";
        goto L_0x002f;
    L_0x0064:
        r1 = "2G";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x006e;
    L_0x006c:
        r0 = 2;
        goto L_0x0038;
    L_0x006e:
        r1 = "3G";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0078;
    L_0x0076:
        r0 = 3;
        goto L_0x0038;
    L_0x0078:
        r1 = "4G";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0082;
    L_0x0080:
        r0 = 4;
        goto L_0x0038;
    L_0x0082:
        r0 = r2;
        goto L_0x0038;
    L_0x0084:
        r0 = r1;
        goto L_0x002f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.util.m.r(android.content.Context):int");
    }

    public static long s(Context context) {
        return r(context) == 1 ? 600000 : 3600000;
    }

    private static ArrayList t(Context context) {
        List<ResolveInfo> n = n(context.getApplicationContext());
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : n) {
            arrayList.add(resolveInfo.activityInfo.packageName);
        }
        return arrayList;
    }
}
