package com.duapps.ad.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.duapps.ad.stats.ToolStatsCore;
import com.duapps.ad.stats.b;
import com.duapps.ad.stats.j;
import java.net.URI;

public class DuAdNetwork {
    private static a a = a.OVERSEA;
    private static DuAdNetwork b;
    private String c;
    private Context d;

    public enum a {
        CHINA,
        OVERSEA
    }

    public static String a() {
        return null;
    }

    private DuAdNetwork(Context context) {
        this.d = context;
        try {
            this.c = this.d.getPackageManager().getApplicationInfo(this.d.getPackageName(), 128).metaData.getString("app_license");
            if (TextUtils.isEmpty(this.c)) {
                throw new IllegalArgumentException("license should not null");
            }
        } catch (NameNotFoundException e) {
        }
        ToolStatsCore.a(context);
    }

    public static void init(Context context, String str) {
        t.a(context).a(str);
        synchronized (DuAdNetwork.class) {
            if (b == null) {
                b = new DuAdNetwork(context.getApplicationContext());
            }
        }
    }

    public final String b() {
        return this.c;
    }

    public static DuAdNetwork c() {
        if (b != null) {
            return b;
        }
        throw new IllegalStateException("Please call init first.");
    }

    public static boolean d() {
        return a == a.OVERSEA;
    }

    public static void onPackageAddReceived(Context context, Intent intent) {
        if (context != null && intent != null) {
            String action = intent.getAction();
            if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (f.a()) {
                    f.c("ToolboxManager", "ACTION_PACKAGE_ADDED, replaceing? " + booleanExtra);
                }
                if (!booleanExtra) {
                    action = URI.create(intent.getDataString()).getSchemeSpecificPart();
                    j a = j.a(context);
                    j b = a.b(action);
                    if (b == null) {
                        if (f.a()) {
                            f.c("ToolboxManager", "Non-click item, skip.");
                        }
                        j d = j.a(context).d(action);
                        if (d != null) {
                            f.c("ToolboxManager", "TiggerPreParse:packageName:" + action + ";id=" + d.b() + ";preParse=" + d.j());
                            if (d.j() == 1) {
                                d.a(true);
                                new b(context).c(d, d.h());
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    d.f(context, b);
                    a.c(action);
                }
            } else if (f.a()) {
                f.c("ToolboxManager", "Not ACTION_PACKAGE_ADDED: " + action);
            }
        } else if (f.a()) {
            f.c("ToolboxManager", "Params error.");
        }
    }
}
