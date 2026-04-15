package com.beita.contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CONTACTS_TABLE = "contacts";
    private static final String DATABASE_CREATE = "CREATE TABLE contacts (_id integer primary key autoincrement,name text,mobileNumber text,homeNumber text,address text,email text,blog text);";
    public static final String DATABASE_NAME = "mycontacts.db";
    public static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}
