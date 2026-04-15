package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.base.QCCache;
import com.qc.entity.InstalledApk;
import com.qc.model.InstalledApkDBHelper;
import java.util.List;

public class ApkUninstallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString().trim();
            if (packageName != null && !"".equals(packageName)) {
                InstalledApkDBHelper dbHelper = new InstalledApkDBHelper(context);
                List<InstalledApk> installedApks = dbHelper.getAll();
                if (installedApks != null && installedApks.size() > 0) {
                    for (InstalledApk installedApk : installedApks) {
                        if (packageName.equals("package:" + installedApk.getPackageName().trim())) {
                            int deleteFlag = dbHelper.delete(installedApk.getKssiid(), installedApk.getPackageName());
                            QCCache.getInstance().init(context);
                            return;
                        }
                    }
                }
            }
        }
    }
}
