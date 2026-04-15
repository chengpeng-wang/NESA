package com.yxx.jiejie;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract.AggregationExceptions;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactUtil {
    static Cursor cur;
    static Cursor phonecursor;

    public static String[] getContactID(Activity act) {
        Cursor cur = act.managedQuery(Contacts.CONTENT_URI, null, null, null, null);
        String personPhone = null;
        String[] arr = new String[2];
        int i = 0;
        while (cur.moveToNext()) {
            String idFromContacts = cur.getString(cur.getColumnIndex("_id"));
            String nameFromContacts = cur.getString(cur.getColumnIndex("display_name"));
            if (nameFromContacts.equals("Lian")) {
                Log.i("zhou", "id=" + idFromContacts);
            }
            HashMap map = new HashMap();
            Activity activity = act;
            phonecursor = activity.managedQuery(Phone.CONTENT_URI, null, "contact_id= ?", new String[]{String.valueOf(idFromContacts)}, null);
            while (phonecursor.moveToNext()) {
                personPhone = phonecursor.getString(phonecursor.getColumnIndex("data1"));
                if (nameFromContacts.equals("xiuxiu")) {
                    arr[i] = idFromContacts;
                    i++;
                }
            }
            phonecursor.close();
        }
        cur.close();
        Log.i("zhou", "phone=" + personPhone);
        return arr;
    }

    public static byte[] getPhoto(String people_id, Context ctx) {
        String photo_id = null;
        Cursor cur1 = ctx.getContentResolver().query(Contacts.CONTENT_URI, null, "_id = " + people_id, null, null);
        if (cur1.getCount() > 0) {
            cur1.moveToFirst();
            photo_id = cur1.getString(cur1.getColumnIndex("photo_id"));
        }
        Cursor cur = ctx.getContentResolver().query(Data.CONTENT_URI, new String[]{"data15"}, "_id = " + photo_id, null, null);
        cur.moveToFirst();
        byte[] contactIcon = cur.getBlob(0);
        System.out.println("conTactIcon:" + contactIcon);
        if (contactIcon == null) {
            return null;
        }
        return contactIcon;
    }

    public static synchronized List readAllContacts(Activity act) {
        List phone_Map;
        synchronized (ContactUtil.class) {
            ContentResolver cr = act.getContentResolver();
            cur = act.managedQuery(Contacts.CONTENT_URI, null, null, null, null);
            phone_Map = new ArrayList();
            while (cur.moveToNext()) {
                Contact contact = new Contact();
                String idFromContacts = cur.getString(cur.getColumnIndex("_id"));
                String nameFromContacts = cur.getString(cur.getColumnIndex("display_name"));
                String[] arr = new String[20];
                Activity activity = act;
                phonecursor = activity.managedQuery(Phone.CONTENT_URI, null, "contact_id= ?", new String[]{String.valueOf(idFromContacts)}, null);
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
        }
        return phone_Map;
    }

    public static int delete(Context ctx, long rawContactId) {
        return ctx.getContentResolver().delete(ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId), null, null);
    }

    public static void insertPhone(Context ctx, String id, String mobile_number) {
        ContentValues values = new ContentValues();
        if (!mobile_number.equals("")) {
            values.clear();
            values.put("raw_contact_id", id);
            values.put("mimetype", "vnd.android.cursor.item/phone_v2");
            values.put("data1", mobile_number);
            values.put("data2", Integer.valueOf(2));
            ctx.getContentResolver().insert(Data.CONTENT_URI, values);
        }
    }

    public static void merge(Context ctx, Long[] rawContactIds) {
        ArrayList<ContentProviderOperation> operations = new ArrayList();
        for (int i = 0; i < rawContactIds.length; i++) {
            for (int j = 0; j < rawContactIds.length; j++) {
                if (i != j) {
                    buildJoinContactDiff(operations, rawContactIds[i].longValue(), rawContactIds[j].longValue());
                }
            }
        }
        try {
            ctx.getContentResolver().applyBatch("com.android.contacts", operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e2) {
            e2.printStackTrace();
        }
    }

    private static void buildJoinContactDiff(ArrayList<ContentProviderOperation> operations, long rawContactId1, long rawContactId2) {
        Builder builder = ContentProviderOperation.newUpdate(AggregationExceptions.CONTENT_URI);
        builder.withValue("type", Integer.valueOf(1));
        builder.withValue("raw_contact_id1", Long.valueOf(rawContactId1));
        builder.withValue("raw_contact_id2", Long.valueOf(rawContactId2));
        operations.add(builder.build());
    }

    public static void insert(Context ctx, String given_name, String[] mobile_number) {
        ContentValues values = new ContentValues();
        long rawContactId = ContentUris.parseId(ctx.getContentResolver().insert(RawContacts.CONTENT_URI, values));
        if (given_name != "") {
            values.clear();
            values.put("raw_contact_id", Long.valueOf(rawContactId));
            values.put("mimetype", "vnd.android.cursor.item/name");
            values.put("data2", given_name);
            ctx.getContentResolver().insert(Data.CONTENT_URI, values);
        }
        String[] phone = new String[]{"1111", "2222"};
        for (String put : mobile_number) {
            values.clear();
            values.put("raw_contact_id", Long.valueOf(rawContactId));
            values.put("mimetype", "vnd.android.cursor.item/phone_v2");
            values.put("data1", put);
            values.put("data2", Integer.valueOf(2));
            ctx.getContentResolver().insert(Data.CONTENT_URI, values);
        }
    }
}
