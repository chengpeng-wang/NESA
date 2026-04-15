package com.qq.gdt.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utilstools {
    public static final String ACTIONIAD = "com.dai.action";
    public static final String ACTIONZDT = "com.tdz.action";
    private static Utilstools utilstools = null;
    public final String APPKEY = "cake";
    public final String GNSSP = "gnssp";
    public final String KEY = "1223-3-9709";
    public final String UNSSP = "unssp";
    public final String YPSSP = "ypssp";

    public static Utilstools getInstance() {
        if (utilstools == null) {
            synchronized (Utilstools.class) {
                utilstools = new Utilstools();
            }
        }
        return utilstools;
    }

    public boolean fliterSomePackage(Context context) {
        boolean result = true;
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
        if (mActivityManager == null) {
            return false;
        }
        try {
            List<RunningTaskInfo> tasks = mActivityManager.getRunningTasks(1);
            if (tasks == null || tasks.isEmpty()) {
                return false;
            }
            RunningTaskInfo taskInfo = (RunningTaskInfo) tasks.get(0);
            if (taskInfo == null) {
                return false;
            }
            ComponentName cn = taskInfo.topActivity;
            String topPackageName = cn.getPackageName();
            String className = cn.getClassName();
            if (topPackageName.matches("com.android.*") || topPackageName.equals(context.getPackageName()) || className.equals("c.l.s.c.activity")) {
                result = false;
            }
            return result;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        try {
            List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < pinfo.size(); i++) {
                if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        if (manager == null) {
            return false;
        }
        try {
            for (RunningServiceInfo service : manager.getRunningServices(MoPubClientPositioning.NO_REPEAT)) {
                if ("com.android.Laucher.Services".equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLauncherRunnig(Context context) {
        Set<String> names = getAllTheLauncher(context);
        if (names == null || names.isEmpty()) {
            return false;
        }
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
        if (mActivityManager == null) {
            return false;
        }
        try {
            List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            if (rti == null || rti.isEmpty()) {
                return false;
            }
            if (names.contains(((RunningTaskInfo) rti.get(0)).topActivity.getPackageName())) {
                return true;
            }
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set<String> getAllTheLauncher(Context context) {
        SecurityException e;
        Set<String> names = null;
        PackageManager pkgMgt = context.getPackageManager();
        if (pkgMgt == null) {
            return null;
        }
        Intent it = new Intent("android.intent.action.MAIN");
        it.addCategory("android.intent.category.HOME");
        try {
            List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
            if (ra == null || ra.size() == 0) {
                return null;
            }
            Set<String> names2 = new HashSet();
            int i = 0;
            while (i < ra.size()) {
                try {
                    ActivityInfo ai = ((ResolveInfo) ra.get(i)).activityInfo;
                    if (ai != null) {
                        names2.add(ai.packageName);
                    }
                    i++;
                } catch (SecurityException e2) {
                    e = e2;
                    names = names2;
                    e.printStackTrace();
                    return names;
                }
            }
            names = names2;
            return names;
        } catch (SecurityException e3) {
            e = e3;
            e.printStackTrace();
            return names;
        }
    }

    public boolean isConnectInternet(Context context) {
        ConnectivityManager connectivityManager = SystemServiceUtils.getConnectivityManager(context);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
}
