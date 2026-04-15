package com.mb.bdapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "flow_duapp.db";
    private static final int VERSION = 1;
    private static DBOpenHelper instance;

    DBOpenHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    public static synchronized DBOpenHelper getInstance(Context context) {
        DBOpenHelper dBOpenHelper;
        synchronized (DBOpenHelper.class) {
            if (instance == null) {
                instance = new DBOpenHelper(context);
            }
            dBOpenHelper = instance;
        }
        return dBOpenHelper;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_dubai_adv(_ID INTEGER PRIMARY KEY,gid TEXT,title TEXT,content TEXT,icon TEXT,du_url TEXT,referrer TEXT,pname TEXT,down_url TEXT,down_retry INTEGER,install_retry INTEGER,status INTEGER,modify_time TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version < 1) {
            db.beginTransaction();
            try {
                db.execSQL("DROP TABLE IF EXISTS tb_dubai_adv");
                onCreate(db);
                version = 1;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
        if (version < 1) {
            db.beginTransaction();
            try {
                db.execSQL("DROP TABLE IF EXISTS tb_dubai_adv");
                onCreate(db);
                onCreate(db);
            } catch (SQLException e2) {
                e2.printStackTrace();
            } finally {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
    }
}
