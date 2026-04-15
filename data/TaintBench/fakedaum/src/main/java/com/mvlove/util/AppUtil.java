package com.mvlove.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtil {
    private static final String MAIN_APK_PKG_NAME = "com.tmvlove";

    public static final boolean isMainApkInstalled(Context context) {
        if (MAIN_APK_PKG_NAME.equalsIgnoreCase(context.getPackageName())) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(MAIN_APK_PKG_NAME, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }
}
