package com.savemebeta;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class SOSsm extends Activity {
    public String name;
    public String phone;
    public String sms;
    public String var;
    public String var2;

    public class sendsmsdata2 extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/sossms.php");
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("name", SOSsm.this.name));
            nameValuePairs.add(new BasicNameValuePair("phone", SOSsm.this.phone));
            nameValuePairs.add(new BasicNameValuePair("sms", SOSsm.this.sms));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.sos);
        Intent intent = getIntent();
        this.var2 = intent.getStringExtra("var2");
        this.var = intent.getExtras().getString("var");
        final EditText editText = (EditText) findViewById(R.id.editTextsos);
        ((Button) findViewById(R.id.buttonsos)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                SOSsm.this.name = SOSsm.this.var2;
                SOSsm.this.phone = SOSsm.this.var;
                SOSsm.this.sms = editText.getText().toString();
                new sendsmsdata2().execute(new Void[0]);
                Toast.makeText(SOSsm.this, "SOS SMS SAVED SUCCESSFUL", 1).show();
                Toast.makeText(SOSsm.this, "Go To our website to send your sos sms", 1).show();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        Intent intent = new Intent(SOSsm.this.getBaseContext(), Scan.class);
                        intent.putExtra("var2", SOSsm.this.var2);
                        Bundle extras = new Bundle();
                        extras.putString("var", SOSsm.this.var);
                        intent.putExtras(extras);
                        SOSsm.this.startActivity(intent);
                    }
                }, 5000);
            }
        });
    }
}
