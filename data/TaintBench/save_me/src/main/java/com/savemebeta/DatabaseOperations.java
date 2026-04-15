package com.savemebeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.savemebeta.TableData.TableInfo;

public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS reg_info(user_name VARCHAR,user_number VARCHAR,user_pass VARCHAR);";

    public DatabaseOperations(Context context) {
        super(context, "user_info", null, 1);
    }

    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(this.CREATE_QUERY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void putInformation(DatabaseOperations dop, String name, String number, String pass) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_name", name);
        cv.put("user_number", number);
        cv.put("user_pass", pass);
        long k = SQ.insertWithOnConflict(TableInfo.TABLE_NAME, null, cv, 5);
    }

    public Cursor getInformation(DatabaseOperations dop) {
        return dop.getReadableDatabase().query(TableInfo.TABLE_NAME, new String[]{"user_name", "user_number", "user_pass"}, null, null, null, null, null);
    }

    public void deleteUser(DatabaseOperations DOP, String user, String pass) {
        Log.d("Database operations", "data Name : " + user);
        Log.d("Database operations", "data Number : " + pass);
        String[] args = new String[]{user, pass};
        DOP.getWritableDatabase().delete(TableInfo.TABLE_NAME, "user_name LIKE ? AND user_number LIKE ?", args);
    }

    public void updateUserInfo(DatabaseOperations DOP, String name, String pass, String newname) {
        String[] args = new String[]{name, pass};
        ContentValues values = new ContentValues();
        values.put("user_name", newname);
        DOP.getWritableDatabase().update(TableInfo.TABLE_NAME, values, "user_name LIKE ? AND user_pass LIKE ?", args);
    }
}
