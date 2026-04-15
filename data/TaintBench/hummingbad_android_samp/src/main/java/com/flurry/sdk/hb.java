package com.flurry.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import java.util.List;

public class hb {
    private static final String a = hb.class.getSimpleName();

    public static Intent a(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                Object obj;
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                String str2 = activityInfo.packageName;
                if (TextUtils.isEmpty(str2) || !("com.android.vending".equalsIgnoreCase(str2) || "com.google.market".equalsIgnoreCase(str2))) {
                    obj = null;
                    continue;
                } else {
                    obj = 1;
                    continue;
                }
                if (obj != null) {
                    iw.a(3, a, "Launching App in package: " + activityInfo.packageName);
                    ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.setComponent(componentName);
                    return intent;
                }
            }
        }
        return null;
    }
}
