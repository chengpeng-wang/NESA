package cn.smsmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME = "phonelistener.db";
    private static final int DATABASEVERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DATABASENAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sms_email (_id integer primary key autoincrement, subject varchar(100),  content varchar(300),  addtime varchar(20)) ");
        db.execSQL("CREATE TABLE phone_email (_id integer primary key autoincrement, subject varchar(100),  content varchar(300),  file_path varchar(100),  addtime varchar(20)) ");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sms_email");
        db.execSQL("DROP TABLE IF EXISTS phone_email");
        onCreate(db);
    }
}
