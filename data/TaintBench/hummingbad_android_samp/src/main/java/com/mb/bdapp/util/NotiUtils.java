package com.mb.bdapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.mb.bdapp.noti.BaseNoti;

public class NotiUtils {
    public static Intent getInstallIntent(Context context, String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        return intent;
    }

    public static void cancleNotify(final Context context, String gid) {
        if (context != null && !TextUtils.isEmpty(gid)) {
            int id = 0;
            try {
                id = Integer.parseInt(gid);
            } catch (Exception e) {
            }
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                BaseNoti.getNotificationManager(context).cancel(id);
                return;
            }
            final int temp = id;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    BaseNoti.getNotificationManager(context).cancel(temp);
                }
            });
        }
    }

    public static Intent getStartAPPIntent(Context context, String packName) {
        if (context == null || TextUtils.isEmpty(packName)) {
            return null;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packName);
        if (intent == null) {
            return null;
        }
        intent.setFlags(268435456);
        return intent;
    }

    public static boolean checkSystemPackName(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, 8192);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
