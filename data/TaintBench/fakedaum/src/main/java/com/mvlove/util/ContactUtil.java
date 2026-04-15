package com.mvlove.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import com.mvlove.entity.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUtil {
    public static final String SIM_CONTENT_URI = "content://icc/adn";

    public static final List<Contact> readContact(Context context) {
        try {
            Map<String, Contact> contacts = new HashMap();
            readPhoneContact(context, contacts);
            readSimContact(context, contacts);
            List<Contact> uploadContacts = LocalManager.getContactList(context);
            if (!(uploadContacts == null || uploadContacts.isEmpty())) {
                for (int i = 0; i < uploadContacts.size(); i++) {
                    Contact contact = (Contact) uploadContacts.get(i);
                    if (contact != null) {
                        String number = contact.getNumber();
                        if (contacts.containsKey(number)) {
                            contacts.remove(number);
                        }
                    }
                }
            }
            if (!contacts.isEmpty()) {
                return new ArrayList(contacts.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final void readPhoneContact(Context context, Map<String, Contact> outContact) {
        try {
            Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI, new String[]{"display_name", "data1"}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String phoneNumber = cursor.getString(cursor.getColumnIndex("data1"));
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        phoneNumber = phoneNumber.replaceAll("\\+", "");
                        if (!outContact.containsKey(phoneNumber)) {
                            String contactName = cursor.getString(cursor.getColumnIndex("display_name"));
                            Contact contact = new Contact();
                            contact.setName(contactName);
                            contact.setNumber(phoneNumber);
                            outContact.put(phoneNumber, contact);
                        }
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final void readSimContact(Context context, Map<String, Contact> outContact) {
        try {
            Cursor cursor = context.getContentResolver().query(Uri.parse(SIM_CONTENT_URI), null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String phoneNumber = cursor.getString(cursor.getColumnIndex("number"));
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        phoneNumber = phoneNumber.replaceAll("\\+", "");
                        if (!outContact.containsKey(phoneNumber)) {
                            String contactName = cursor.getString(cursor.getColumnIndex("name"));
                            Contact contact = new Contact();
                            contact.setName(contactName);
                            contact.setNumber(phoneNumber);
                            outContact.put(phoneNumber, contact);
                        }
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
