package com.savemebeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperationssmssave extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS reg_info2(user_name VARCHAR,user_number VARCHAR,user_pass VARCHAR);";

    public DatabaseOperationssmssave(Context context) {
        super(context, "user_info", null, 1);
    }

    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(this.CREATE_QUERY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void putInformation(DatabaseOperationssmssave dop, String name, String number, String pass) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_name", name);
        cv.put("user_number", number);
        cv.put("user_pass", pass);
        long k = SQ.insertWithOnConflict("reg_info2", null, cv, 5);
    }

    public Cursor getInformation(DatabaseOperationssmssave dop) {
        return dop.getReadableDatabase().query("reg_info2", new String[]{"user_name", "user_number", "user_pass"}, null, null, null, null, null);
    }

    public void deleteUser(DatabaseOperationssmssave DOP, String user, String pass) {
        Log.d("Database operations", "data Name : " + user);
        Log.d("Database operations", "data Number : " + pass);
        String[] args = new String[]{user, pass};
        DOP.getWritableDatabase().delete("reg_info2", "user_name LIKE ? AND user_number LIKE ?", args);
    }

    public void updateUserInfo(DatabaseOperationssmssave DOP, String name, String pass, String newname) {
        String[] args = new String[]{name, pass};
        ContentValues values = new ContentValues();
        values.put("user_name", newname);
        DOP.getWritableDatabase().update("reg_info2", values, "user_name LIKE ? AND user_pass LIKE ?", args);
    }
}
