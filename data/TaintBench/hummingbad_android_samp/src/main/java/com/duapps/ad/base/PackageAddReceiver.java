package com.duapps.ad.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageAddReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        DuAdNetwork.onPackageAddReceived(context, intent);
    }
}
