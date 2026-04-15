package com.baidu.vi;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StatFs;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class VDeviceAPI {
    private static WakeLock a = null;
    private static BroadcastReceiver b = null;

    public static String getAppVersion() {
        try {
            return c.a().getPackageManager().getPackageInfo(c.a().getApplicationInfo().packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static long getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) c.a().getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static String getCachePath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    public static String getCellId() {
        TelephonyManager telephonyManager = (TelephonyManager) c.a().getSystemService("phone");
        if (telephonyManager == null) {
            return null;
        }
        CellLocation cellLocation = telephonyManager.getCellLocation();
        return cellLocation instanceof GsmCellLocation ? " " + ((GsmCellLocation) cellLocation).getCid() : " ";
    }

    public static int getCurrentNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.a().getSystemService("connectivity");
        if (connectivityManager.getActiveNetworkInfo() == null) {
            return 0;
        }
        switch (connectivityManager.getActiveNetworkInfo().getType()) {
            case 0:
                return 3;
            case 1:
                return 2;
            default:
                return 1;
        }
    }

    public static long getFreeSpace() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
        return (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) c.a().getSystemService("phone");
        return telephonyManager != null ? telephonyManager.getDeviceId() : null;
    }

    public static String getImsi() {
        TelephonyManager telephonyManager = (TelephonyManager) c.a().getSystemService("phone");
        return telephonyManager != null ? telephonyManager.getSubscriberId() : null;
    }

    public static String getLac() {
        TelephonyManager telephonyManager = (TelephonyManager) c.a().getSystemService("phone");
        if (telephonyManager == null) {
            return null;
        }
        CellLocation cellLocation = telephonyManager.getCellLocation();
        return cellLocation instanceof GsmCellLocation ? "" + ((GsmCellLocation) cellLocation).getLac() : "";
    }

    public static String getModuleFileName() {
        return c.a().getFilesDir().getAbsolutePath();
    }

    public static d getNetworkInfo(int i) {
        NetworkInfo networkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) c.a().getSystemService("connectivity");
        switch (i) {
            case 2:
                networkInfo = connectivityManager.getNetworkInfo(1);
                break;
            case 3:
                networkInfo = connectivityManager.getNetworkInfo(0);
                break;
            default:
                networkInfo = null;
                break;
        }
        return networkInfo != null ? new d(networkInfo) : null;
    }

    public static String getOsVersion() {
        return "android";
    }

    public static int getScreenBrightness() {
        int i = -1;
        ContentResolver contentResolver = c.a().getContentResolver();
        int i2 = 0;
        try {
            i2 = System.getInt(contentResolver, "screen_brightness_mode");
        } catch (SettingNotFoundException e) {
        }
        if (i2 == 1) {
            return i;
        }
        try {
            return System.getInt(contentResolver, "screen_brightness");
        } catch (SettingNotFoundException e2) {
            return i;
        }
    }

    public static float getScreenDensity() {
        if (b.a() == null) {
            return 0.0f;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        b.a().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    public static int getScreenDensityDpi() {
        if (b.a() == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        b.a().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    public static long getSdcardFreeSpace() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static long getSdcardTotalSpace() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static float getSystemMetricsX() {
        if (b.a() == null) {
            return 0.0f;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        b.a().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (float) displayMetrics.widthPixels;
    }

    public static float getSystemMetricsY() {
        if (b.a() == null) {
            return 0.0f;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        b.a().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (float) displayMetrics.heightPixels;
    }

    public static long getTotalMemory() {
        long j = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                j = (long) Integer.valueOf(readLine.split("\\s+")[1]).intValue();
            }
            bufferedReader.close();
        } catch (IOException e) {
        }
        return j;
    }

    public static long getTotalSpace() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
        return (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static ScanResult[] getWifiHotpot() {
        List scanResults = ((WifiManager) c.a().getSystemService("wifi")).getScanResults();
        return (ScanResult[]) scanResults.toArray(new ScanResult[scanResults.size()]);
    }

    public static boolean isWifiConnected() {
        return ((ConnectivityManager) c.a().getSystemService("connectivity")).getNetworkInfo(1).isConnected();
    }

    public static void makeCall(String str) {
        c.a().startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + str)));
    }

    public static native void onNetworkStateChanged();

    public static void openUrl(String str) {
        c.a().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    public static int sendMMS(String str, String str2, String str3, String str4) {
        if (!PhoneNumberUtils.isWellFormedSmsAddress(str)) {
            return 1;
        }
        try {
            String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(str4)).toString()));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("address", str);
            intent.putExtra("subject", str2);
            intent.putExtra("sms_body", str3);
            intent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + str4));
            intent.setType(mimeTypeFromExtension);
            c.a().startActivity(intent);
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    public static void sendSMS(String str, String str2) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        intent.putExtra("sms_body", str2);
        c.a().startActivity(intent);
    }

    public static void setNetworkChangedCallback() {
        unsetNetworkChangedCallback();
        b = new a();
        c.a().registerReceiver(b, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static void setScreenAlwaysOn(boolean z) {
        if (z) {
            if (a == null) {
                a = ((PowerManager) c.a().getSystemService("power")).newWakeLock(10, "VDeviceAPI");
            }
            a.acquire();
        } else if (a != null && a.isHeld()) {
            a.release();
            a = null;
        }
    }

    public static void setupSoftware(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        c.a().startActivity(intent);
    }

    public static void unsetNetworkChangedCallback() {
        if (b != null) {
            c.a().unregisterReceiver(b);
            b = null;
        }
    }
}
