package com.cleanmaster.ui.app.utils;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.picksinit.c;
import java.util.List;
import java.util.Locale;

public class MarketContext extends ContextWrapper {
    public MarketContext(Context base) {
        super(base);
    }

    private List a(Intent intent) {
        PackageManager packageManager = getPackageManager();
        if (packageManager == null) {
            return null;
        }
        List queryIntentActivities;
        try {
            queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
            if (queryIntentActivities == null) {
                return null;
            }
        } catch (Exception e) {
            queryIntentActivities = null;
        }
        return queryIntentActivities;
    }

    private ResolveInfo a(List list) {
        if (list == null) {
            return null;
        }
        for (ResolveInfo resolveInfo : list) {
            if (resolveInfo != null && resolveInfo.isDefault) {
                return resolveInfo;
            }
        }
        return null;
    }

    private void a(Intent intent, String str) {
        List<ResolveInfo> a = a(intent);
        if (a != null) {
            ResolveInfo a2 = a((List) a);
            if (a2 != null) {
                intent.setClassName(a2.activityInfo.packageName, a2.activityInfo.name);
                return;
            }
            int i;
            for (ResolveInfo a22 : a) {
                if (a22.activityInfo != null && a22.activityInfo.packageName.equalsIgnoreCase(str)) {
                    intent.setClassName(a22.activityInfo.packageName, a22.activityInfo.name);
                    i = 1;
                    break;
                }
            }
            i = 0;
            if (i == 0) {
                a22 = (ResolveInfo) a.get(0);
                intent.setClassName(a22.activityInfo.packageName, a22.activityInfo.name);
            }
        }
    }

    public void startActivity(Intent intent) {
        if (intent == null) {
            super.startActivity(intent);
            return;
        }
        Uri data = intent.getData();
        intent.addFlags(268435456);
        intent.addFlags(65536);
        Intent intent2 = new Intent(intent);
        if (data != null) {
            try {
                if (data.toString() != null) {
                    String toLowerCase = data.toString().toLowerCase(Locale.getDefault());
                    if (intent.getAction().equals("android.intent.action.VIEW")) {
                        if (toLowerCase.startsWith("http://") || toLowerCase.startsWith("https://")) {
                            if (toLowerCase.startsWith("https://play.google.com/store/apps/details") || toLowerCase.startsWith("http://play.google.com/store/apps/details")) {
                                a(intent, "com.android.vending");
                            } else {
                                a(intent, "com.android.browser");
                            }
                        } else if (toLowerCase.startsWith("market://")) {
                            a(intent, "com.android.vending");
                        }
                        super.startActivity(intent);
                        return;
                    }
                    throw new Exception("not view action");
                }
            } catch (Exception e) {
                super.startActivity(intent2);
                return;
            }
        }
        throw new Exception("null uri");
    }

    public void registerComponentCallbacks(ComponentCallbacks callback) {
        c.getInstance().getContext().registerComponentCallbacks(callback);
    }

    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        c.getInstance().getContext().unregisterComponentCallbacks(callback);
    }

    public Context getApplicationContext() {
        return this;
    }
}
