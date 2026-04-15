package com.qc.base;

import android.app.Application;
import com.qc.common.QuietInstallEngine;
import com.qc.util.SystemUtil;

public class QCApplication extends Application {
    public void onCreate() {
        super.onCreate();
        QCExceptionHandler.getInstance().init(getApplicationContext());
        if (SystemUtil.checkAppType(this, getPackageName()) == 0) {
            QuietInstallEngine.unInstall_saveData(getPackageName());
        }
    }

    public void onTerminate() {
        super.onTerminate();
        QCCache.getInstance().clearCache();
        QCCache.getInstance().clearQueue();
    }
}
