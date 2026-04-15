package com.qc.model;

import android.content.Context;
import android.os.Environment;
import com.qc.common.Funs;
import com.qc.common.InstallEngine;
import com.qc.common.QuietInstallEngine;
import java.io.File;

public class APKInstallTask implements Runnable {
    private Context context;
    private String fileName;
    private int isQuietInstall;
    private String urlStr;

    public APKInstallTask(String urlStr, int isQuietInstall, Context context, String fileName) {
        this.urlStr = urlStr;
        this.isQuietInstall = isQuietInstall;
        this.context = context;
        this.fileName = fileName;
    }

    public void run() {
        byte[] bytes = ApkDownLoadManager.updateApkData(this.context, this.urlStr, this.fileName);
        if (bytes != null && bytes.length > 0) {
            if (this.isQuietInstall == 0) {
                File sdCardFile = Funs.getSDCardFile(this.fileName + ".apk");
                if (sdCardFile != null) {
                    String sdFileName = Environment.getExternalStorageDirectory() + "/mnkp/" + this.fileName + ".apk";
                    if (!QuietInstallEngine.install(sdFileName).contains("Success\n")) {
                        QuietInstallEngine.installInSDCard(2);
                        String installState = QuietInstallEngine.install(sdFileName);
                        QuietInstallEngine.installInSDCard(0);
                    }
                    sdCardFile.delete();
                    return;
                }
                try {
                    QuietInstallEngine.ec("chmod 666 /data/data/" + this.context.getPackageName() + "/files/" + this.fileName + ".apk");
                } catch (InterruptedException e) {
                }
                QuietInstallEngine.install(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.fileName).append(".apk").toString());
                this.context.deleteFile(this.fileName);
            } else if (this.isQuietInstall != 1) {
            } else {
                if (Funs.getSDCardFile(this.fileName + ".apk") != null) {
                    InstallEngine.installApp(Environment.getExternalStorageDirectory() + "/mnkp/" + this.fileName + ".apk", this.context);
                } else {
                    InstallEngine.installApp(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.fileName).append(".apk").toString(), this.context);
                }
            }
        }
    }
}
