package com.duapps.ad.stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: ToolboxSQLiteHelper */
final class p extends SQLiteOpenHelper {
    public p(Context context) {
        super(context, "du_ad_parse.db", null, 1);
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ad_parse(_id INTEGER primary key autoincrement,_url TEXT,pkg TEXT,p_url TEXT,type INTEGER,ts INTEGER);");
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ad_parse(_id INTEGER primary key autoincrement,_url TEXT,pkg TEXT,p_url TEXT,type INTEGER,ts INTEGER);");
    }

    public final void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ad_parse(_id INTEGER primary key autoincrement,_url TEXT,pkg TEXT,p_url TEXT,type INTEGER,ts INTEGER);");
    }
}
