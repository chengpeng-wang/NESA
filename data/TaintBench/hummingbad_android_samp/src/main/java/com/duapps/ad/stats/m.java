package com.duapps.ad.stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: ToolStatsDbHelper */
final class m extends SQLiteOpenHelper {
    public m(Context context) {
        super(context, "du_ad_ts.db", null, 1);
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS srecord(_id INTEGER primary key autoincrement,ts INTEGER,ls TEXT,child TEXT,stype TEXT,content BLOB);");
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS srecord(_id INTEGER primary key autoincrement,ts INTEGER,ls TEXT,child TEXT,stype TEXT,content BLOB);");
    }

    public final void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS srecord(_id INTEGER primary key autoincrement,ts INTEGER,ls TEXT,child TEXT,stype TEXT,content BLOB);");
    }
}
