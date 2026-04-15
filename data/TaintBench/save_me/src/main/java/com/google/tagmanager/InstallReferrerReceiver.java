package com.google.tagmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class InstallReferrerReceiver extends BroadcastReceiver {
    static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        if (INSTALL_ACTION.equals(intent.getAction()) && referrer != null) {
            InstallReferrerUtil.cacheInstallReferrer(referrer);
            Intent serviceIntent = new Intent(context, InstallReferrerService.class);
            serviceIntent.putExtra("referrer", referrer);
            context.startService(serviceIntent);
        }
    }
}
