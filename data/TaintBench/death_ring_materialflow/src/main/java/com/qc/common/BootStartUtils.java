package com.qc.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BootStartUtils {
    private static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";

    public static List<Map<String, Object>> fetchInstalledApps(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
        List<Map<String, Object>> appList = new ArrayList(appInfo.size());
        for (ApplicationInfo app : appInfo) {
            if (pm.checkPermission(BOOT_START_PERMISSION, app.packageName) == 0) {
                Map<String, Object> appMap = new HashMap();
                String label = pm.getApplicationLabel(app).toString();
                Drawable icon = pm.getApplicationIcon(app);
                String desc = app.packageName;
                appMap.put("label", label);
                appMap.put("icon", icon);
                appMap.put("desc", desc);
                appList.add(appMap);
            }
        }
        return appList;
    }
}
