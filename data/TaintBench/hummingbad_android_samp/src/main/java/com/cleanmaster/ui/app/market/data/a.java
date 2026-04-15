package com.cleanmaster.ui.app.market.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.cmcm.adsdk.requestconfig.data.ConfigResponseHeader.Colums;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
import org.json.JSONObject;

/* compiled from: MarketResponseHeader */
public class a {
    public int a = -1;
    public int b = 0;
    public int c = -1;
    public int d = -1;
    public int e = -1;
    public int f = -1;
    public long g = 0;
    public String h = "";

    public Object a(JSONObject jSONObject) {
        try {
            this.a = jSONObject.getInt(MASTNativeAdConstants.RESPONSE_ERROR_CODE);
            this.b = jSONObject.getInt("adn");
            this.c = jSONObject.getInt(VastIconXmlManager.OFFSET);
            this.d = jSONObject.getInt("show_rating");
            this.e = jSONObject.getInt(Colums.REQUEST_SHOW_TYPE_COLUMN);
            this.f = jSONObject.getInt("total_ads");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ContentValues a() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Colums.REQUEST_POSID_COLUMN, this.h);
        contentValues.put("total_ads", Integer.valueOf(this.f));
        contentValues.put(Colums.REQUEST_SHOW_TYPE_COLUMN, Integer.valueOf(this.e));
        contentValues.put("x_mtime", Long.valueOf(System.currentTimeMillis()));
        return contentValues;
    }

    public ContentValues a(String str) {
        return a();
    }

    public a a(Cursor cursor) {
        a aVar = new a();
        aVar.h = cursor.getString(cursor.getColumnIndex(Colums.REQUEST_POSID_COLUMN));
        aVar.f = cursor.getInt(cursor.getColumnIndex("total_ads"));
        aVar.e = cursor.getInt(cursor.getColumnIndex(Colums.REQUEST_SHOW_TYPE_COLUMN));
        aVar.g = cursor.getLong(cursor.getColumnIndex("x_mtime"));
        return aVar;
    }

    public static void a(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + str + "(_id" + " INTEGER PRIMARY KEY," + "pos_id TEXT" + ",show_type" + " TEXT," + "total_ads INTEGER " + ",x_mtime" + " INTEGER );");
    }

    public String toString() {
        return String.format("(:pos_id %s :code %d :total_ads %d :show_type %d :show_rating %d :x_mtime %d)", new Object[]{this.h, Integer.valueOf(this.a), Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(this.d), Long.valueOf(this.g)});
    }
}
