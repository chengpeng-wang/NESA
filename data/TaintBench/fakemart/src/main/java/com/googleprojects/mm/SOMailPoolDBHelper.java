package com.googleprojects.mm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SOMailPoolDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "so_mailpool.db";
    public static final String TABLE_NAME = "so_mailpool";

    public SOMailPoolDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE so_mailpool(_id INTEGER PRIMARY KEY AUTOINCREMENT,sender_addr TEXT,receiver_addr TEXT,mail_body TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS so_mailpool");
        onCreate(db);
    }
}
