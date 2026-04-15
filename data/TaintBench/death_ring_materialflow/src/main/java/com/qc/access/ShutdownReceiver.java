package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;

public class ShutdownReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Funs.isInstallApk(context, Constant.BaseSite_pkgName)) {
            Funs.forceStopProcess(context, Constant.BaseSite_pkgName);
            QuietInstallEngine.unInstall(Constant.BaseSite_pkgName);
        }
    }
}
