package com.nicon.tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.qq.gdt.utils.Utilstools;
import java.util.ArrayList;
import java.util.List;

public class NoTools {
    private static NoTools inTools = null;
    public static TelephonyManager telephonyManager = null;
    private String appName = "";
    private String mMid = "";
    private String packageName = "";

    public static NoTools getInstance() {
        if (inTools == null) {
            synchronized (NoTools.class) {
                if (inTools == null) {
                    inTools = new NoTools();
                }
            }
        }
        return inTools;
    }

    public String getMID(Context context) {
        if (context != null && TextUtils.isEmpty(this.mMid)) {
            this.mMid = getTelephonyManager(context).getDeviceId();
            if (this.mMid == null || this.mMid.trim().length() == 0 || this.mMid.matches("0+")) {
                this.mMid = "000000000000000";
            }
        }
        return this.mMid;
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        if (context != null && telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService("phone");
        }
        return telephonyManager;
    }

    public String getPackname(Context context) {
        if (context != null && TextUtils.isEmpty(this.packageName)) {
            this.packageName = context.getPackageName();
        }
        return this.packageName;
    }

    public String getAppname(Context context) {
        if (context != null && TextUtils.isEmpty(this.appName)) {
            PackageManager packageManager = context.getPackageManager();
            try {
                this.appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(getPackname(context), 128)).toString();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this.appName;
    }

    public int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return pi;
        }
    }

    public static void StartNoIcon(Context context, Intent intent) {
        Intent uintent = new Intent(Utilstools.ACTIONIAD);
        uintent.setFlags(268468224);
        context.sendBroadcast(uintent);
        Intent intent1 = new Intent(Utilstools.ACTIONIAD);
        uintent.setFlags(268468224);
        context.startService(intent1);
    }

    public static boolean checkAPP(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 8192);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static List<PackageInfo> filtPackageInfo(Context context, String extr) {
        List<PackageInfo> resInfos = new ArrayList();
        String pkgName = "";
        for (PackageInfo info : context.getPackageManager().getInstalledPackages(0)) {
            pkgName = info.packageName;
            if (!(pkgName.equals(null) || pkgName.equals("") || !pkgName.contains(extr))) {
                resInfos.add(info);
            }
        }
        return resInfos;
    }
}
