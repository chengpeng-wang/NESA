package com.google.elements;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceAdmin extends DeviceAdminReceiver {
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }

    public void onDisabled(Context context, Intent intent) {
        Intent service = new Intent(context, AdminService.class);
        service.setFlags(268435456);
        context.startService(service);
    }
}
