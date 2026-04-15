package com.mb.bdapp.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;

public class AppInfoUtils {
    private static final String TAG = AppInfoUtils.class.getSimpleName();

    public static String getPackageName(Context context) {
        return context.getApplicationInfo().packageName;
    }

    public static String getAppName(Context context) {
        return (String) context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getVersion(Context context) {
        String version = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
            return version;
        }
    }
}
