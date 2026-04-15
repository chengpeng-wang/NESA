package com.qc.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.MotionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Funs {
    public static int getIndex(int flag) {
        int result;
        do {
            result = (int) (Math.random() * 10.0d);
            if (result <= 8) {
                break;
            }
        } while (flag == 1);
        return result;
    }

    public static String getStrByNotNull(String str) {
        return str.replaceAll(" ", "_");
    }

    public static boolean isHasConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.isAvailable() && info.getState() == State.CONNECTED) {
            return true;
        }
        return false;
    }

    public static boolean isWapconnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected() || info.getState() != State.CONNECTED) {
            return false;
        }
        String currentAPN = info.getExtraInfo();
        if (currentAPN == null || "".equals(currentAPN) || !currentAPN.contains("wap")) {
            return false;
        }
        return true;
    }

    public static int getRandom(int max) {
        if (max == 1) {
            return 0;
        }
        int result;
        do {
            result = (int) (Math.random() * 10.0d);
        } while (result >= max);
        return result;
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(2);
    }

    public static String date2String() {
        return new SimpleDateFormat("M月dd日").format(new Date());
    }

    public static double getRandomDouble(int min, int max) {
        double d1 = Double.parseDouble(new DecimalFormat("##.#").format(Math.random()));
        while (true) {
            int result = (int) (Math.random() * 10.0d);
            if (result < max && result >= min) {
                return ((double) result) + d1;
            }
        }
    }

    public static double getRandomDouble2(int value) {
        return ((double) value) + Double.parseDouble(new DecimalFormat("##.#").format(Math.random()));
    }

    public static String getImsi(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService("phone");
        if (telManager == null) {
            return null;
        }
        String imsi = telManager.getSubscriberId();
        if (imsi == null) {
            return telManager.getSimOperator();
        }
        return imsi;
    }

    public static String getAndroidID(Context context) {
        String androidId = System.getString(context.getContentResolver(), "android_id");
        if (androidId == null) {
            return "000000";
        }
        return androidId;
    }

    public static int getARS(Context context) {
        String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return 1;
            }
            if (operator.equals("46001")) {
                return 2;
            }
            if (operator.equals("46003")) {
                return 3;
            }
        }
        return 0;
    }

    public static int getARS2(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return 1;
            }
            if (imsi.startsWith("46001")) {
                return 2;
            }
            if (imsi.startsWith("46003")) {
                return 3;
            }
        }
        return 0;
    }

    public static String date2String2() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static boolean isConnection(HttpURLConnection conn) {
        try {
            conn.setConnectTimeout(5000);
            conn.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String updateTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    public static String getRandomCode() {
        String time = new SimpleDateFormat("MMddmmss").format(new Date());
        String tick = new StringBuilder(String.valueOf(System.currentTimeMillis())).toString();
        return time.trim() + tick.trim().substring(tick.length() - 6);
    }

    public static boolean isSDCardExise() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean sdCardIsExsit(String sdCardPath) {
        return new File(sdCardPath).exists();
    }

    public static String getImei(Context context) {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm != null) {
            imei = tm.getDeviceId();
        }
        if (imei == null) {
            return "000000";
        }
        return imei;
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static File downLoadFile(String fileName, String httpUrl) {
        File file = getSDCardFile(new StringBuilder(String.valueOf(fileName)).append(".apk").toString());
        try {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(httpUrl).openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                if (conn.getResponseCode() < 400) {
                    while (0.0d <= 100.0d && is != null) {
                        int numRead = is.read(buf);
                        if (numRead <= 0) {
                            break;
                        }
                        fos.write(buf, 0, numRead);
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        return file;
    }

    public static int isWifi(Context context) {
        if (State.CONNECTED == ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).getState()) {
            return 1;
        }
        return 0;
    }

    public static File getSDCardFile(String fileName) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        String path = "";
        path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/mnkp").toString();
        File ret = new File(path);
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString());
        if (ret.exists()) {
            return ret;
        }
        try {
            ret.createNewFile();
            return ret;
        } catch (IOException e) {
            return ret;
        }
    }

    public static String getSDCardImagesURL() {
        return new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/mnkp/images").toString();
    }

    public static File getSDCardImage(String fileName) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        String path = "";
        path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/mnkp/images").toString();
        File ret = new File(path);
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString());
        if (ret.exists()) {
            return ret;
        }
        try {
            ret.createNewFile();
            return ret;
        } catch (IOException e) {
            return ret;
        }
    }

    public static File getSDCardFile(String folder, String fileName) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        String path = "";
        path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(folder).toString();
        File ret = new File(path);
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString());
        if (ret.exists()) {
            return ret;
        }
        try {
            ret.createNewFile();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public static void writeByteFile(OutputStream os, byte[] b) {
        try {
            os.write(b);
            os.flush();
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            try {
                os.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (IOException e32) {
            e32.printStackTrace();
            try {
                os.close();
            } catch (IOException e322) {
                e322.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                os.close();
            } catch (IOException e3222) {
                e3222.printStackTrace();
            }
            throw th;
        }
    }

    public static boolean isInstallApk(Context context, String packageName) {
        for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(0)) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRuningAPK(Context context, String packageName) throws NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        for (RunningAppProcessInfo rapInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (rapInfo.processName.equals(packageInfo.applicationInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getAssetsAPK(Context context, String folder) {
        String[] files = null;
        try {
            return context.getResources().getAssets().list(folder);
        } catch (IOException e) {
            return files;
        }
    }

    public static int getAssetsAPKCount(Context context, String folder) {
        try {
            String[] files = context.getResources().getAssets().list(folder);
            if (files != null) {
                return files.length;
            }
            return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public static byte[] getByteFromAssets(Context context, String filename) {
        try {
            InputStream in = context.getResources().getAssets().open(filename);
            int length = in.available();
            byte[] buffer = new byte[length];
            int offset = 0;
            int numread = 0;
            while (offset < length && numread >= 0) {
                numread = in.read(buffer, offset, length - offset);
                offset += numread;
            }
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] getByteFromAssets(Context context, String folder, String filename) {
        try {
            InputStream in = context.getResources().getAssets().open(new StringBuilder(String.valueOf(folder)).append("/").append(filename).toString());
            int length = in.available();
            byte[] buffer = new byte[length];
            int offset = 0;
            int numread = 0;
            while (offset < length && numread >= 0) {
                numread = in.read(buffer, offset, length - offset);
                offset += numread;
            }
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean addShortcut(Context context, String pkg) {
        String title = "unknown";
        String mainAct = null;
        int iconIdentifier = 0;
        PackageManager pkgMag = context.getPackageManager();
        Intent queryIntent = new Intent("android.intent.action.MAIN", null);
        queryIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> list = pkgMag.queryIntentActivities(queryIntent, 1);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = (ResolveInfo) list.get(i);
            if (info.activityInfo.packageName.equals(pkg)) {
                title = info.loadLabel(pkgMag).toString();
                mainAct = info.activityInfo.name;
                iconIdentifier = info.activityInfo.applicationInfo.icon;
                break;
            }
        }
        if (mainAct == null) {
            return false;
        }
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", title);
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", new Intent("android.intent.action.MAIN").setComponent(new ComponentName(pkg, mainAct)));
        Context pkgContext = null;
        if (context.getPackageName().equals(pkg)) {
            pkgContext = context;
        } else {
            try {
                pkgContext = context.createPackageContext(pkg, 3);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (pkgContext != null) {
            shortcut.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(pkgContext, iconIdentifier));
        }
        context.sendBroadcast(shortcut);
        return true;
    }

    public static void forceStopProcess(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        try {
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", new Class[]{String.class});
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, new Object[]{packageName});
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
    }

    public boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        List<RunningServiceInfo> serviceList = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE);
        if (serviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (((RunningServiceInfo) serviceList.get(i)).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static Intent startAppByPackageName(Context context, String packageName) {
        PackageInfo pi = null;
        boolean flag = true;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            flag = false;
        }
        if (!flag) {
            return null;
        }
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri == null) {
            return null;
        }
        String packageName1 = ri.activityInfo.packageName;
        String className = ri.activityInfo.name;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(packageName1, className));
        return intent;
    }

    public static int randromInt(int startNum, int endNum) {
        if (startNum >= endNum) {
            return 1;
        }
        return (int) ((Math.random() * ((double) (endNum - startNum))) + ((double) startNum));
    }

    public static String ec(String command) throws InterruptedException {
        String returnString = "";
        try {
            Process pro = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                returnString = new StringBuilder(String.valueOf(returnString)).append(line).append("\n").toString();
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException e) {
        }
        return returnString;
    }

    public static void startAPKByPackageName2(Context context, String packageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    public static void startAPKByPackageName(Context context, String packageName) {
        if (packageName != null && packageName.length() >= 1) {
            PackageInfo pi = null;
            boolean flag = true;
            try {
                pi = context.getPackageManager().getPackageInfo(packageName, 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                flag = false;
            }
            if (flag) {
                Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
                resolveIntent.addCategory("android.intent.category.LAUNCHER");
                resolveIntent.setPackage(pi.packageName);
                ResolveInfo ri = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
                if (ri != null) {
                    String packageName1 = ri.activityInfo.packageName;
                    String className = ri.activityInfo.name;
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.setComponent(new ComponentName(packageName1, className));
                    intent.addFlags(268435456);
                    context.startActivity(intent);
                }
            }
        }
    }

    public boolean isAppOnForeground(Context context, String packageName) {
        List<RunningAppProcessInfo> appProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == 100) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0044 A:{SYNTHETIC, Splitter:B:12:0x0044} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004f A:{SYNTHETIC, Splitter:B:18:0x004f} */
    public static boolean RootCommand(java.lang.String r6) {
        /*
        r3 = 0;
        r1 = 0;
        r4 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x0041, all -> 0x004c }
        r5 = "su";
        r3 = r4.exec(r5);	 Catch:{ Exception -> 0x0041, all -> 0x004c }
        r2 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x0041, all -> 0x004c }
        r4 = r3.getOutputStream();	 Catch:{ Exception -> 0x0041, all -> 0x004c }
        r2.<init>(r4);	 Catch:{ Exception -> 0x0041, all -> 0x004c }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r5 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r4.<init>(r5);	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r5 = "\n";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r2.writeBytes(r4);	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r4 = "exit\n";
        r2.writeBytes(r4);	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r2.flush();	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        r3.waitFor();	 Catch:{ Exception -> 0x005f, all -> 0x005a }
        if (r2 == 0) goto L_0x003b;
    L_0x0038:
        r2.close();	 Catch:{ Exception -> 0x0056 }
    L_0x003b:
        r3.destroy();	 Catch:{ Exception -> 0x0056 }
    L_0x003e:
        r4 = 1;
        r1 = r2;
    L_0x0040:
        return r4;
    L_0x0041:
        r0 = move-exception;
    L_0x0042:
        if (r1 == 0) goto L_0x0047;
    L_0x0044:
        r1.close();	 Catch:{ Exception -> 0x005d }
    L_0x0047:
        r3.destroy();	 Catch:{ Exception -> 0x005d }
    L_0x004a:
        r4 = 0;
        goto L_0x0040;
    L_0x004c:
        r4 = move-exception;
    L_0x004d:
        if (r1 == 0) goto L_0x0052;
    L_0x004f:
        r1.close();	 Catch:{ Exception -> 0x0058 }
    L_0x0052:
        r3.destroy();	 Catch:{ Exception -> 0x0058 }
    L_0x0055:
        throw r4;
    L_0x0056:
        r4 = move-exception;
        goto L_0x003e;
    L_0x0058:
        r5 = move-exception;
        goto L_0x0055;
    L_0x005a:
        r4 = move-exception;
        r1 = r2;
        goto L_0x004d;
    L_0x005d:
        r4 = move-exception;
        goto L_0x004a;
    L_0x005f:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.Funs.RootCommand(java.lang.String):boolean");
    }

    public static boolean isScreenLocked(Context context) {
        return !((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    public static boolean isCreenOn(Context context) {
        return ((PowerManager) context.getSystemService("power")).isScreenOn();
    }

    public static int getSystemVersion() {
        return VERSION.SDK_INT;
    }

    public static void createShortcut(Context context, int drawable, int label, Class<Activity> clzz) {
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Parcelable icon = ShortcutIconResource.fromContext(context, drawable);
        Intent myIntent = new Intent(context, clzz);
        myIntent.setAction("android.intent.action.MAIN");
        myIntent.addCategory("android.intent.category.LAUNCHER");
        addIntent.putExtra("android.intent.extra.shortcut.NAME", context.getString(label));
        addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", icon);
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", myIntent);
        context.sendBroadcast(addIntent);
    }

    public static String getTopPkg(Context context) {
        return ((RunningTaskInfo) ((ActivityManager) context.getApplicationContext().getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
    }

    public static String getTopActivity(Context context) {
        return ((RunningTaskInfo) ((ActivityManager) context.getApplicationContext().getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
    }

    public static int getAppProcessNum(Context mContext, String pacName) {
        int _num = 0;
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) mContext.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.equals(pacName)) {
                _num++;
            }
        }
        return _num == 0 ? 1 : _num;
    }

    public static int getAppServiceNum(Context mContext, String pacName) {
        int _num = 0;
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) mContext.getSystemService("activity")).getRunningServices(30)) {
            if (runningServiceInfo.service.getPackageName().equals(pacName)) {
                _num++;
            }
        }
        return _num;
    }

    public Set<String> getAllBrowser(Context context) {
        Set<String> lisapps = new HashSet();
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://www.qincai123.com"));
        List<ResolveInfo> lists = context.getPackageManager().queryIntentActivities(intent, 1);
        if (lists.size() != 0) {
            for (int i = 0; i < lists.size(); i++) {
                lisapps.add(((ResolveInfo) lists.get(i)).activityInfo.packageName);
            }
        }
        return lisapps;
    }

    public static void setDefaultBrowser(String pkg, String actvity, Context context) {
        PackageManager packageManager = context.getPackageManager();
        String str1 = "android.intent.category.DEFAULT";
        String str2 = "android.intent.category.BROWSABLE";
        String str3 = "android.intent.action.VIEW";
        IntentFilter filter = new IntentFilter(str3);
        filter.addCategory(str1);
        filter.addCategory(str2);
        filter.addDataScheme("http");
        ComponentName component = new ComponentName(pkg, actvity);
        Intent intent = new Intent(str3);
        intent.addCategory(str2);
        intent.addCategory(str1);
        intent.setDataAndType(Uri.parse("http://"), null);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 32);
        int size = resolveInfoList.size();
        ComponentName[] arrayOfComponentName = new ComponentName[size];
        for (int i = 0; i < size; i++) {
            ActivityInfo activityInfo = ((ResolveInfo) resolveInfoList.get(i)).activityInfo;
            String packageName = activityInfo.packageName;
            String className = activityInfo.name;
            packageManager.clearPackagePreferredActivities(packageName);
            arrayOfComponentName[i] = new ComponentName(packageName, className);
        }
        packageManager.addPreferredActivity(filter, 2097152, arrayOfComponentName, component);
    }

    public static String openWebByConfirmBrawable(Context context, String urlStr) {
        String pkgName = "";
        String MainActivity = "";
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.parse("http://"), null);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 32);
        if (resolveInfoList == null || resolveInfoList.size() <= 0) {
            return pkgName;
        }
        ActivityInfo activityInfo = ((ResolveInfo) resolveInfoList.get(0)).activityInfo;
        pkgName = activityInfo.packageName;
        MainActivity = activityInfo.name;
        Intent startIntent = new Intent("android.intent.action.VIEW", Uri.parse(urlStr));
        startIntent.addFlags(268435456);
        startIntent.setClassName(pkgName, MainActivity);
        context.startActivity(startIntent);
        return pkgName;
    }

    public static void openAppByPkg(Context context, String packageName) {
        Intent intent;
        String url = "http://www.baidu.com";
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        boolean isPresence = false;
        for (int i = 0; i < packageInfoList.size(); i++) {
            if (((PackageInfo) packageInfoList.get(i)).applicationInfo.packageName.equals(packageName)) {
                isPresence = true;
                break;
            }
        }
        if (isPresence) {
            intent = packageManager.getLaunchIntentForPackage(packageName);
        } else {
            intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        }
        context.startActivity(intent);
    }

    public static boolean interflateNumber(List<String> listenContents, String currentContent) {
        if ((listenContents == null || listenContents.size() < 1) && (currentContent == null || currentContent.length() < 1)) {
            return false;
        }
        for (String prefixContent : listenContents) {
            if (currentContent.contains(prefixContent)) {
                return true;
            }
        }
        return false;
    }

    public static boolean interflateContent(List<List<String>> listenContents, String currentContent) {
        if ((listenContents == null || listenContents.size() < 1) && (currentContent == null || currentContent.length() < 1)) {
            return false;
        }
        boolean isConcait = false;
        for (List<String> inStrList : listenContents) {
            if (inStrList.size() == 1) {
                if (currentContent.contains((CharSequence) inStrList.get(0))) {
                    return true;
                }
            } else if (inStrList.size() > 1) {
                int offSet = 0;
                for (String str : inStrList) {
                    offSet++;
                    if (!currentContent.contains(str)) {
                        isConcait = false;
                        break;
                    } else if (offSet == inStrList.size() && currentContent.contains(str)) {
                        isConcait = true;
                    }
                }
            }
        }
        return isConcait;
    }

    public static void startAppByPkgName(Context context, String packageName, String url) {
        PackageInfo pi = null;
        boolean flag = true;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            flag = false;
        }
        if (flag) {
            Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
            resolveIntent.addCategory("android.intent.category.LAUNCHER");
            resolveIntent.setPackage(pi.packageName);
            ResolveInfo ri = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setComponent(new ComponentName(packageName1, className));
                intent.addFlags(268435456);
                context.startActivity(intent);
                return;
            }
            return;
        }
        Intent myIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        myIntent.addFlags(268435456);
        context.startActivity(myIntent);
    }

    public static boolean isSmsContentHas(String smsContent, List<String> keys) {
        boolean flag = false;
        if (smsContent == null || smsContent.length() < 1 || keys == null || keys.size() < 1) {
            return false;
        }
        for (String key : keys) {
            if (smsContent.contains(key)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String getReplySmsContent(HashMap<String, String> replyes, String smsContent, List<String> keys) {
        String replyContent = "";
        if (smsContent == null || smsContent.length() < 1) {
            return "";
        }
        if (keys == null || keys.size() < 1) {
            return "";
        }
        if (replyes == null || replyes.size() < 1) {
            return "";
        }
        for (String key : keys) {
            if (smsContent.contains(key)) {
                replyContent = (String) replyes.get(key);
            }
        }
        return replyContent;
    }

    public static boolean isSmsContentHas(String smsContent, HashMap<String, String> keys) {
        boolean flag = false;
        if (smsContent == null || smsContent.length() < 1 || keys == null || keys.size() < 1) {
            return false;
        }
        for (Entry entry : keys.entrySet()) {
            String val = (String) entry.getValue();
            if (smsContent.contains((String) entry.getKey()) && smsContent.contains(val)) {
                flag = true;
            }
        }
        return flag;
    }

    public static void silent(Context context) {
        AudioManager audio = (AudioManager) context.getSystemService("audio");
        audio.setRingerMode(0);
        audio.setVibrateSetting(0, 0);
        audio.setVibrateSetting(1, 0);
    }

    public static void ring_open(Context context) {
        AudioManager audio = (AudioManager) context.getSystemService("audio");
        audio.setRingerMode(2);
        audio.setVibrateSetting(0, 0);
        audio.setVibrateSetting(1, 0);
    }

    public static boolean isSilentMode(Context context) {
        return ((AudioManager) context.getSystemService("audio")).getRingerMode() != 2;
    }

    public static String isSmsContentRules(String smsContent, HashMap<String, String> keys) {
        String keyvalue = "";
        if (smsContent == null || smsContent.length() < 1) {
            return "";
        }
        if (keys == null || keys.size() < 1) {
            return "";
        }
        for (Entry entry : keys.entrySet()) {
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            if (smsContent.contains(key) && smsContent.contains(val)) {
                keyvalue = key;
            }
        }
        return keyvalue;
    }

    public static void MotionScreen(int motionType, int x, int y) {
        try {
            Class ServiceManager = Class.forName("android.os.ServiceManager");
            Class IWindowManagerClass = Class.forName("android.view.IWindowManager");
            IWindowManager OIWindowManager = Stub.asInterface((IBinder) ServiceManager.getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{new String("window")}));
            MotionEvent event_down = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), motionType, (float) x, (float) y, 0);
            IWindowManagerClass.getMethod("injectPointerEvent", new Class[]{MotionEvent.class, Boolean.TYPE}).invoke(OIWindowManager, new Object[]{event_down, Boolean.valueOf(true)});
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
    }

    public static boolean queryServices(Context mContext, String ActionName) {
        List<ResolveInfo> resolves = mContext.getPackageManager().queryIntentServices(new Intent(ActionName), 4);
        if (resolves == null || resolves.size() <= 1) {
            return false;
        }
        return true;
    }

    public static void returnDesk(Context mContext) {
        Intent i = new Intent("android.intent.action.MAIN");
        i.setFlags(268435456);
        i.addCategory("android.intent.category.HOME");
        mContext.startActivity(i);
    }
}
