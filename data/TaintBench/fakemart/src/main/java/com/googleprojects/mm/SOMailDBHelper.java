package com.googleprojects.mm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SOMailDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "so_mail.db";
    public static final String TABLE_NAME = "so_mail";

    public SOMailDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE so_mail(_id INTEGER PRIMARY KEY AUTOINCREMENT,sender_addr TEXT,receiver_addr TEXT,smtp_port TEXT,smtp_addr TEXT)");
        ContentValues cv = new ContentValues();
        cv.put("sender_addr", SOMailCPUtil.default_sender_addr);
        cv.put("receiver_addr", SOMailCPUtil.default_receiver_addr);
        cv.put("smtp_port", SOMailCPUtil.default_smtp_port);
        cv.put("smtp_addr", SOMailCPUtil.default_smtp_addr);
        db.insert(TABLE_NAME, null, cv);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS so_mail");
        onCreate(db);
    }
}
