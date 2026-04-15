package com.beita.contact;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ContactsProvider extends ContentProvider {
    public static final String AUTHORITY = "com.beita.contact.provider.ContactsProvider";
    public static final int CONTACTS = 1;
    public static final String CONTACTS_TABLE = "contacts";
    public static final int CONTACT_ID = 2;
    public static final Uri CONTENT_URI = Uri.parse("content://com.beita.contact.provider.ContactsProvider/contacts");
    private static final String TAG = "ContactsProvider";
    private static final UriMatcher uriMatcher = new UriMatcher(-1);
    private SQLiteDatabase contactsDB;
    private DBHelper dbHelper;

    static {
        uriMatcher.addURI(AUTHORITY, "contacts", 1);
        uriMatcher.addURI(AUTHORITY, "contacts/#", 2);
    }

    public boolean onCreate() {
        this.dbHelper = new DBHelper(getContext());
        this.contactsDB = this.dbHelper.getWritableDatabase();
        return this.contactsDB != null;
    }

    public int delete(Uri uri, String where, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = this.contactsDB.delete("contacts", where, selectionArgs);
                break;
            case 2:
                count = this.contactsDB.delete("contacts", "_id=" + ((String) uri.getPathSegments().get(1)) + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd.beita.contact.mycontacts";
            case 2:
                return "vnd.android.cursor.item/vnd.beita.contact.mycontacts";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues initialValues) {
        if (uriMatcher.match(uri) != 1) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
            Log.e("ContactsProviderinsert", "initialValues is not null");
        } else {
            values = new ContentValues();
        }
        if (!values.containsKey(ContactColumn.NAME)) {
            values.put(ContactColumn.NAME, "");
        }
        if (!values.containsKey(ContactColumn.MOBILENUM)) {
            values.put(ContactColumn.MOBILENUM, "");
        }
        if (!values.containsKey(ContactColumn.HOMENUM)) {
            values.put(ContactColumn.HOMENUM, "");
        }
        if (!values.containsKey(ContactColumn.ADDRESS)) {
            values.put(ContactColumn.ADDRESS, "");
        }
        if (!values.containsKey("email")) {
            values.put("email", "");
        }
        if (!values.containsKey(ContactColumn.BLOG)) {
            values.put(ContactColumn.BLOG, "");
        }
        Log.e("ContactsProviderinsert", values.toString());
        long rowId = this.contactsDB.insert("contacts", null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            Log.e("ContactsProviderinsert", noteUri.toString());
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        Log.e("ContactsProvider:query", " in Query");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("contacts");
        switch (uriMatcher.match(uri)) {
            case 2:
                qb.appendWhere("_id=" + ((String) uri.getPathSegments().get(1)));
                break;
        }
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = "_id";
        } else {
            orderBy = sortOrder;
        }
        Cursor c = qb.query(this.contactsDB, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        int count;
        Log.e("ContactsProviderupdate", values.toString());
        Log.e("ContactsProviderupdate", uri.toString());
        Log.e("ContactsProviderupdate :match", uriMatcher.match(uri));
        switch (uriMatcher.match(uri)) {
            case 1:
                Log.e("ContactsProviderupdate", "1");
                count = this.contactsDB.update("contacts", values, where, selectionArgs);
                break;
            case 2:
                String contactID = (String) uri.getPathSegments().get(1);
                Log.e("ContactsProviderupdate", new StringBuilder(String.valueOf(contactID)).toString());
                count = this.contactsDB.update("contacts", values, "_id=" + contactID + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
