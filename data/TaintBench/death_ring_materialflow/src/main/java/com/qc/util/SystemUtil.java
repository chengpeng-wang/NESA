package com.qc.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemUtil {
    public static int checkAppType(Context context, String pname) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pname, 0);
            if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {
                return 1;
            }
            return 0;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList();
        List<PackageInfo> paklist = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            int i2 = pak.applicationInfo.flags;
            ApplicationInfo applicationInfo = pak.applicationInfo;
            if ((i2 & 1) <= 0) {
                apps.add(pak);
            }
        }
        return apps;
    }

    public static boolean isSystemApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 1) != 0;
    }

    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 128) != 0;
    }

    public static boolean isUserApp(PackageInfo pInfo) {
        return (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) ? false : true;
    }

    public static HashMap<String, Integer> getScreenByDis(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        HashMap<String, Integer> hm = new HashMap();
        hm.put("width", Integer.valueOf(w));
        hm.put("height", Integer.valueOf(h));
        return hm;
    }

    public static String getScreenByDis2(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        return new StringBuilder(String.valueOf(w)).append("*").append(display.getHeight()).toString();
    }

    public static HashMap<String, Integer> getScreenByMetric(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        HashMap<String, Integer> hm = new HashMap();
        hm.put("width", Integer.valueOf(w));
        hm.put("height", Integer.valueOf(h));
        return hm;
    }

    public static String getMac() {
        String str = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    return str.trim();
                }
            }
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getCpu() {
        String str = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("adb shell cat /proc/cpuinfo ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    return str.trim();
                }
            }
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String[] getCpuInfo() {
        String str2 = "";
        String[] cpuInfo = new String[]{"", ""};
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"), 8192);
            String[] arrayOfString = localBufferedReader.readLine().split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            cpuInfo[1] = cpuInfo[1] + localBufferedReader.readLine().split("\\s+")[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    public static String[] getVersion() {
        String[] version = new String[]{"null", "null", "null", "null"};
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/version"), 8192);
            version[0] = localBufferedReader.readLine().split("\\s+")[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = VERSION.RELEASE;
        version[2] = Build.MODEL;
        version[3] = Build.DISPLAY;
        return version;
    }

    public static String getTelNumber(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
    }

    public static String getBasicVersion() {
        try {
            Class<?> cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            return (String) cl.getMethod("get", new Class[]{String.class, String.class}).invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
        } catch (Exception e) {
            return "获取失败";
        }
    }

    public static String[] getMacAndTime(Context mContext) {
        String[] other = new String[]{"null", "null"};
        WifiInfo wifiInfo = ((WifiManager) mContext.getSystemService("wifi")).getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            other[0] = wifiInfo.getMacAddress();
        } else {
            other[0] = "Fail";
        }
        other[1] = getTimes();
        return other;
    }

    private static String getTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        return new StringBuilder(String.valueOf((int) (ut / 3600))).append("时").append((int) ((ut / 60) % 60)).append("分").toString();
    }

    public static List<PackageInfo> getAllApp(Context context) {
        List<PackageInfo> apps = new ArrayList();
        for (PackageInfo packageInfo : getInstalledPackages(context)) {
            if ((packageInfo.applicationInfo.flags & 1) <= 0) {
                apps.add(packageInfo);
            } else if ((packageInfo.applicationInfo.flags & 128) != 0) {
                apps.add(packageInfo);
            }
        }
        return apps;
    }

    public static List<PackageInfo> getAllROMApp(Context context) {
        List<PackageInfo> apps = new ArrayList();
        for (PackageInfo packageInfo : getInstalledPackages(context)) {
            if ((packageInfo.applicationInfo.flags & 1) <= 0 && (packageInfo.applicationInfo.flags & 262144) == 0) {
                apps.add(packageInfo);
            } else if ((packageInfo.applicationInfo.flags & 128) != 0 && (packageInfo.applicationInfo.flags & 262144) == 0) {
                apps.add(packageInfo);
            }
        }
        return apps;
    }

    public static List<PackageInfo> getAllSDCARDApp(Context context) {
        List<PackageInfo> apps = new ArrayList();
        for (PackageInfo packageInfo : getInstalledPackages(context)) {
            if ((packageInfo.applicationInfo.flags & 1) <= 0 && (packageInfo.applicationInfo.flags & 262144) != 0) {
                apps.add(packageInfo);
            } else if (!((packageInfo.applicationInfo.flags & 128) == 0 || (packageInfo.applicationInfo.flags & 262144) == 0)) {
                apps.add(packageInfo);
            }
        }
        return apps;
    }

    public static List<PackageInfo> getInstalledPackages(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    public static List<ApplicationInfo> getApps(Context context) {
        return context.getPackageManager().getInstalledApplications(8192);
    }

    public static PackageInfo getMaxApp(Context context) {
        List<PackageInfo> apps = getAllApp(context);
        if (apps == null || apps.size() <= 0) {
            return null;
        }
        PackageInfo app = (PackageInfo) apps.get(0);
        long maxSize = new File(app.applicationInfo.publicSourceDir).length();
        for (PackageInfo packageInfo : apps) {
            if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                app = packageInfo;
                maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
            }
        }
        return app;
    }

    public static PackageInfo getMaxRomApp(Context context) {
        List<PackageInfo> apps = getAllROMApp(context);
        if (apps == null || apps.size() <= 0) {
            return null;
        }
        PackageInfo app = (PackageInfo) apps.get(0);
        long maxSize = new File(app.applicationInfo.publicSourceDir).length();
        for (PackageInfo packageInfo : apps) {
            if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                app = packageInfo;
                maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
            }
        }
        return app;
    }

    public static PackageInfo getMaxSdCardApp(Context context) {
        List<PackageInfo> apps = getAllSDCARDApp(context);
        if (apps == null || apps.size() <= 0) {
            return null;
        }
        PackageInfo app = (PackageInfo) apps.get(0);
        long maxSize = new File(app.applicationInfo.publicSourceDir).length();
        for (PackageInfo packageInfo : apps) {
            if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                app = packageInfo;
                maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
            }
        }
        return app;
    }

    public static PackageInfo getMaxApp(Context context, List<String> depart) {
        PackageInfo app = null;
        List<PackageInfo> apps = getAllApp(context);
        if (apps != null && apps.size() >= 1 && apps != null && apps.size() > 0) {
            if (depart != null && depart.size() > 0) {
                for (int i = 0; i < depart.size(); i++) {
                    for (int j = 0; j < apps.size(); j++) {
                        if (((PackageInfo) apps.get(j)).packageName.equals(depart.get(i))) {
                            apps.remove(j);
                        }
                    }
                }
            }
            app = (PackageInfo) apps.get(0);
            long maxSize = new File(app.applicationInfo.publicSourceDir).length();
            for (PackageInfo packageInfo : apps) {
                if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                    app = packageInfo;
                    maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
                }
            }
        }
        return app;
    }

    public static PackageInfo getMaxRomApp(Context context, List<String> depart) {
        PackageInfo app = null;
        List<PackageInfo> apps = getAllROMApp(context);
        if (apps != null && apps.size() >= 1 && apps != null && apps.size() > 0) {
            if (depart != null && depart.size() > 0) {
                for (int i = 0; i < depart.size(); i++) {
                    for (int j = 0; j < apps.size(); j++) {
                        if (((PackageInfo) apps.get(j)).packageName.equals(depart.get(i))) {
                            apps.remove(j);
                        }
                    }
                }
            }
            app = (PackageInfo) apps.get(0);
            long maxSize = new File(app.applicationInfo.publicSourceDir).length();
            for (PackageInfo packageInfo : apps) {
                if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                    app = packageInfo;
                    maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
                }
            }
        }
        return app;
    }

    public static PackageInfo getMaxSdCardApp(Context context, List<String> depart) {
        PackageInfo app = null;
        List<PackageInfo> apps = getAllSDCARDApp(context);
        if (apps != null && apps.size() >= 1 && apps != null && apps.size() > 0) {
            if (depart != null && depart.size() > 0) {
                for (int i = 0; i < depart.size(); i++) {
                    for (int j = 0; j < apps.size(); j++) {
                        if (((PackageInfo) apps.get(j)).packageName.equals(depart.get(i))) {
                            apps.remove(j);
                        }
                    }
                }
            }
            app = (PackageInfo) apps.get(0);
            long maxSize = new File(app.applicationInfo.publicSourceDir).length();
            for (PackageInfo packageInfo : apps) {
                if (maxSize < new File(packageInfo.applicationInfo.publicSourceDir).length()) {
                    app = packageInfo;
                    maxSize = new File(packageInfo.applicationInfo.publicSourceDir).length();
                }
            }
        }
        return app;
    }

    public static boolean getSimState(Context context) {
        if (((TelephonyManager) context.getSystemService("phone")).getSubscriberId() != null) {
            return true;
        }
        return false;
    }

    public static boolean getWifiState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null && info.length > 0) {
                int i = 0;
                while (i < info.length) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;
    }
}
