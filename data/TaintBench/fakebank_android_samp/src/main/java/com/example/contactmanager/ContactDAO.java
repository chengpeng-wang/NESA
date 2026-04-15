package com.example.contactmanager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    public static final int COL_HAS_PHONE = 1;
    public static final int COL_ID = 2;
    public static final int COL_NAME = 0;
    public static final int COL_PHONE_NAME = 1;
    public static final int COL_PHONE_NUMBER = 0;
    public static final int COL_PHONE_TYPE = 2;
    private static final String TAG = "ContactDAO";
    List<Contact> contactList;
    private Context context;
    String[] selPhoneCols = new String[]{"data1", "display_name", "data2"};
    String[] selectCol = new String[]{"display_name", "has_phone_number", "_id"};

    public ContactDAO(Context context) {
        this.context = context;
    }

    public List<Contact> getContactList() {
        String phoneNumber = ((TelephonyManager) this.context.getSystemService("phone")).getLine1Number();
        this.contactList = new ArrayList();
        Cursor cursor = this.context.getContentResolver().query(Contacts.CONTENT_URI, this.selectCol, "((display_name NOTNULL) AND (has_phone_number=1) AND (display_name != '' ))", null, "display_name COLLATE LOCALIZED ASC");
        if (cursor == null) {
            Toast.makeText(this.context, "cursor is null!", 1).show();
            return null;
        } else if (cursor.getCount() == 0) {
            Toast.makeText(this.context, "cursor count is zero!", 1).show();
            return null;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int contactId = cursor.getInt(cursor.getColumnIndex("_id"));
                if (cursor.getInt(1) > 0) {
                    String displayName = cursor.getString(0);
                    Cursor phoneCursor = this.context.getContentResolver().query(Phone.CONTENT_URI, this.selPhoneCols, "contact_id=" + contactId, null, null);
                    if (phoneCursor.moveToFirst()) {
                        do {
                            String contactNumber = phoneCursor.getString(0);
                            Contact contactData = new Contact();
                            contactData.setContactname(displayName);
                            contactData.setContactnumber(contactNumber);
                            contactData.setPhonenumber(phoneNumber);
                            this.contactList.add(contactData);
                        } while (phoneCursor.moveToNext());
                    }
                }
                cursor.moveToNext();
            }
            return this.contactList;
        }
    }

    public List<Contact> getContactList2() {
        String phoneNumber = ((TelephonyManager) this.context.getSystemService("phone")).getLine1Number();
        Cursor cursor = this.context.getContentResolver().query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex("_id");
            int displayNameColumn = cursor.getColumnIndex("display_name");
            do {
                String contactId = cursor.getString(idColumn);
                String disPlayName = cursor.getString(displayNameColumn);
                System.out.println(contactId);
                System.out.println(disPlayName);
                String contactNumber = "";
                if (cursor.getInt(cursor.getColumnIndex("has_phone_number")) > 0) {
                    Cursor phones = this.context.getContentResolver().query(Phone.CONTENT_URI, null, "contact_id = " + contactId, null, null);
                    if (phones.moveToFirst()) {
                        do {
                            contactNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println(contactNumber);
                            Contact contactData = new Contact();
                            contactData.setContactname(disPlayName);
                            contactData.setContactnumber(contactNumber);
                            contactData.setPhonenumber(phoneNumber);
                            this.contactList.add(contactData);
                        } while (phones.moveToNext());
                    }
                }
            } while (cursor.moveToNext());
        }
        return this.contactList;
    }
}
