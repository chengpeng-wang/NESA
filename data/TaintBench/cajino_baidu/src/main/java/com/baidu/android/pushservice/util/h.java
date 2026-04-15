package com.baidu.android.pushservice.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class h extends SQLiteOpenHelper {
    public h(Context context, String str, int i) {
        super(new g(context), str, null, i);
    }

    private void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS MsgResultInfo");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS StatisticsInfo");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS FileDownloadingInfo");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS PushBehavior");
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS MsgResultInfo");
        sQLiteDatabase.execSQL("CREATE TABLE StatisticsInfo (" + l.info_id.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " + l.packageName.name() + " TEXT NOT NULL, " + l.open_type.name() + " TEXT NOT NULL, " + l.msgid.name() + " TEXT, " + l.app_open_time.name() + " TEXT NOT NULL, " + l.app_close_time.name() + " TEXT NOT NULL, " + l.use_duration.name() + " TEXT NOT NULL, " + l.extra.name() + " TEXT" + ");");
        sQLiteDatabase.execSQL("CREATE TABLE PushBehavior (" + f.actionId.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " + f.actionName.name() + " TEXT NOT NULL, " + f.timeStamp.name() + " TEXT NOT NULL, " + f.networkStatus.name() + " TEXT, " + f.msgType.name() + " INTEGER, " + f.msgId.name() + " TEXT, " + f.msgLen.name() + " TEXT, " + f.errorMsg.name() + " TEXT, " + f.requestId.name() + " TEXT, " + f.stableHeartInterval.name() + " TEXT, " + f.errorCode.name() + " TEXT, " + f.appid.name() + " TEXT" + ");");
        sQLiteDatabase.execSQL("CREATE TABLE AppInfo (" + e.appInfoId.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, " + e.appid.name() + " TEXT UNIQUE, " + e.rsaUserId.name() + " TEXT, " + e.userId.name() + " TEXT, " + e.packageName.name() + " TEXT, " + e.appName.name() + " TEXT, " + e.cFrom.name() + " TEXT, " + e.versionCode.name() + " TEXT, " + e.versionName.name() + " TEXT, " + e.intergratedPushVersion.name() + " TEXT" + ");");
        sQLiteDatabase.execSQL("CREATE TABLE FileDownloadingInfo (" + i.belongTo.name() + " TEXT, " + i.downloadUrl.name() + " TEXT PRIMARY KEY, " + i.savePath.name() + " TEXT NOT NULL, " + i.title.name() + " TEXT, " + i.description.name() + " TEXT, " + i.fileName.name() + " TEXT NOT NULL, " + i.downloadBytes.name() + " INTEGER NOT NULL, " + i.totalBytes.name() + " INTEGER NOT NULL, " + i.downloadStatus.name() + " INTEGER NOT NULL," + i.timeStamp.name() + " INTEGER NOT NULL" + ");");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        a(sQLiteDatabase);
        onCreate(sQLiteDatabase);
    }
}
