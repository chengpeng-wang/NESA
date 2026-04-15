package com.android.tools.system;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.view.MotionEventCompat;
import java.util.ArrayList;

public class PhoneBook {
    public Context context;

    public PhoneBook(Context context) {
        this.context = context;
    }

    public ArrayList<String> getNumbers() {
        ArrayList<String> arrayList = r17;
        ArrayList<String> arrayList2 = new ArrayList();
        ArrayList<String> arrayList3 = arrayList;
        ContentResolver contentResolver = this.context.getContentResolver();
        Cursor query = contentResolver.query(Contacts.CONTENT_URI, null, null, null, null);
        if (query.getCount() > 0) {
            while (query.moveToNext()) {
                if (Integer.parseInt(query.getString(query.getColumnIndex("has_phone_number"))) > 0) {
                    String string = query.getString(query.getColumnIndex("_id"));
                    ContentResolver contentResolver2 = contentResolver;
                    Uri uri = Phone.CONTENT_URI;
                    StringBuffer stringBuffer = r17;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    String stringBuffer3 = stringBuffer.append("contact_id").append(" = ?").toString();
                    String[] strArr = new String[1];
                    String[] strArr2 = strArr;
                    strArr[0] = string;
                    Cursor query2 = contentResolver2.query(uri, null, stringBuffer3, strArr2, null);
                    while (query2.moveToNext()) {
                        int i = query2.getInt(query2.getColumnIndex("data2"));
                        String replaceAll = query2.getString(query2.getColumnIndex("data1")).replaceAll("[^\\d\\+]", "");
                        boolean add;
                        switch (i) {
                            case 2:
                                if (!(arrayList3.contains(replaceAll) || replaceAll.equals(""))) {
                                    add = arrayList3.add(replaceAll);
                                }
                                break;
                            case MotionEventCompat.ACTION_HOVER_MOVE /*7*/:
                                if (!(arrayList3.contains(replaceAll) || replaceAll.equals(""))) {
                                    add = arrayList3.add(replaceAll);
                                }
                                break;
                            case 17:
                                if (!(arrayList3.contains(replaceAll) || replaceAll.equals(""))) {
                                    add = arrayList3.add(replaceAll);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    query2.close();
                }
            }
        }
        return arrayList3;
    }
}
