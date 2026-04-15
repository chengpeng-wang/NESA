package com.cmcm.adsdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cmcm.adsdk.requestconfig.data.PosBean;

/* compiled from: DatabaseHelper */
public final class a extends SQLiteOpenHelper {
    public a(Context context) {
        super(context, "ad_sdk.db", null, 1);
    }

    public final void onCreate(SQLiteDatabase db) {
        PosBean.a(db, PosBean.TABLE_NAME);
    }

    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PosBean.b(db, PosBean.TABLE_NAME);
        onCreate(db);
    }
}
