package com.mb.bdapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {
    private static String db_name = "db_ad_du";
    private static int version = 1;

    public Db(Context context) {
        super(context, db_name, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AdvActionDao.TB_NAME + "(" + AdvActionDao.COL_ACT_REQ + " int default 0," + AdvActionDao.COL_ACT_SHOW + " int default 0," + AdvActionDao.COL_ACT_CLICK + " int default 0," + AdvActionDao.COL_ACT_ADV_DB + " int default 0," + AdvActionDao.COL_ACT_GP_DB + " int default 0," + AdvActionDao.COL_ACT_GET_REF + " int default 0," + AdvActionDao.COL_GP + " int default 0," + AdvActionDao.COL_ACT_DOWN_START + " int default 0," + AdvActionDao.COL_ACT_DOWN_SUCCESS + " int default 0," + AdvActionDao.COL_ACT_INSTALL + " int default 0," + AdvActionDao.COL_ACT_INSTALL_SUCC + " int default 0," + AdvActionDao.COL_ACT_SEND_REF + " int default 0," + AdvActionDao.COL_ACT_START + " int default 0," + AdvActionDao.COL_PKG + " string" + ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
