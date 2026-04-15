package com.qc.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import java.io.File;

public class InstallEngine {
    public static boolean installApp(String path, Context context) {
        try {
            Funs.ec("chmod 666 " + path);
        } catch (InterruptedException e) {
        }
        if (!new File(path).exists() || !path.endsWith(".apk")) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        context.startActivity(intent);
        return true;
    }

    public static boolean removeApp(String pkgName, Context context) {
        if (!isApkInstalled(context, pkgName)) {
            return false;
        }
        context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + pkgName)));
        return true;
    }

    public static boolean isApkInstalled(Context context, String pkgName) {
        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
            return true;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
