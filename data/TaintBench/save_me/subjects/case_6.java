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
