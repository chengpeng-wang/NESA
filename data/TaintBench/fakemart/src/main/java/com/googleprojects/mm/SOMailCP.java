package com.googleprojects.mm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class SOMailCP extends ContentProvider {
    static final int ALL_ITEMS = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://jh.so.sp.mail/mail");
    static final UriMatcher Matcher = new UriMatcher(-1);
    static final int ONE_ITEM = 2;
    SQLiteDatabase mDatabase;

    static {
        Matcher.addURI("jh.so.sp.mail", "mail", 1);
        Matcher.addURI("jh.so.sp.mail", "mail/#", 2);
    }

    public String getType(Uri uri) {
        if (Matcher.match(uri) == 1) {
            return "vnd.jhsosp.Data.cursor.item/mail";
        }
        if (Matcher.match(uri) == 2) {
            return "vnd.jhsosp.Data.cursor.dir/mails";
        }
        return null;
    }

    public String getTableName() {
        return SOMailDBHelper.TABLE_NAME;
    }

    public boolean onCreate() {
        this.mDatabase = new SOMailDBHelper(getContext()).getWritableDatabase();
        return true;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (Matcher.match(uri)) {
            case 1:
                count = this.mDatabase.delete(getTableName(), selection, selectionArgs);
                break;
            case 2:
                String where = "_id=" + ((String) uri.getPathSegments().get(1));
                if (!TextUtils.isEmpty(selection)) {
                    where = new StringBuilder(String.valueOf(where)).append(" AND ").append(selection).toString();
                }
                count = this.mDatabase.delete(getTableName(), where, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public Uri insert(Uri uri, ContentValues values) {
        long row = this.mDatabase.insert(getTableName(), null, values);
        if (row <= 0) {
            return null;
        }
        Uri notiUri = ContentUris.withAppendedId(CONTENT_URI, row);
        getContext().getContentResolver().notifyChange(notiUri, null);
        return notiUri;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.mDatabase.query(getTableName(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (Matcher.match(uri)) {
            case 1:
                count = this.mDatabase.update(getTableName(), values, selection, selectionArgs);
                break;
            case 2:
                String where = "_id=" + ((String) uri.getPathSegments().get(1));
                if (!TextUtils.isEmpty(selection)) {
                    where = new StringBuilder(String.valueOf(where)).append(" AND ").append(selection).toString();
                }
                count = this.mDatabase.update(getTableName(), values, where, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
