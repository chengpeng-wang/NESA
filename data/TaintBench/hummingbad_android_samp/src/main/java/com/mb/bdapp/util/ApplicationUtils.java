package com.mb.bdapp.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationUtils {
    public static boolean isNoSystemApp(Context context) {
        String topActivityName = getForegroundPackageName(context);
        if (TextUtils.isEmpty(topActivityName) || !getAllPackageName(context).contains(topActivityName)) {
            return false;
        }
        return true;
    }

    private static Set<String> getAllPackageName(Context context) {
        Set<String> names = new HashSet();
        List<ApplicationInfo> list = context.getPackageManager().getInstalledApplications(8192);
        for (int i = 0; i < list.size(); i++) {
            ApplicationInfo ai = (ApplicationInfo) list.get(i);
            if ((ai.flags & 1) <= 0) {
                String packageName = ai.packageName;
                if (!packageName.equals(context.getPackageName())) {
                    names.add(packageName);
                }
            }
        }
        return names;
    }

    private static String getForegroundPackageName(Context context) {
        String foregroundPackageName = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT < 21) {
            List<RunningTaskInfo> rti = activityManager.getRunningTasks(1);
            if (!(rti == null || rti.isEmpty())) {
                foregroundPackageName = ((RunningTaskInfo) rti.get(0)).topActivity.getPackageName();
            }
        } else {
            for (RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                if (appProcess.importance == 100) {
                    return appProcess.processName;
                }
            }
        }
        return foregroundPackageName;
    }
}
