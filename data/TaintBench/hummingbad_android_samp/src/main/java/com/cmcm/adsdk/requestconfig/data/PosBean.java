package com.cmcm.adsdk.requestconfig.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.cmcm.adsdk.requestconfig.log.a;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class PosBean extends a implements IRequestTransformer, Comparable<PosBean> {
    public static final String TABLE_NAME = "posinfo";
    private static final String TAG = "PosBean";
    public int adtype;
    public List<InfoBean> infoList = new ArrayList();
    public boolean mIsLoadList = false;
    public String name;
    public String parameter;
    public int placeid;
    public Integer weight;

    public interface Colums extends BaseColumns {
        public static final String REQUEST_ADTYPE_COLUMN = "adtype";
        public static final String REQUEST_NAME_COLUMN = "name";
        public static final String REQUEST_PARAMETER_COLUMN = "parameter";
        public static final String REQUEST_PLACEID_COLUMN = "placeid";
        public static final String REQUEST_WEIGHT_COLUMN = "weight";
    }

    public /* bridge */ /* synthetic */ int compareTo(Object x0) {
        return ((PosBean) x0).weight.compareTo(this.weight);
    }

    public /* synthetic */ Object fromCursor(Cursor x0) {
        return a(x0);
    }

    public PosBean(String name, int placeid, Integer weight, int adType, String parameter) {
        this.adtype = adType;
        this.name = name;
        this.placeid = placeid;
        this.weight = weight;
        this.parameter = parameter;
    }

    public Object fromJSONObject(JSONObject o) {
        try {
            this.adtype = o.getInt(Colums.REQUEST_ADTYPE_COLUMN);
            this.placeid = o.getInt(Colums.REQUEST_PLACEID_COLUMN);
        } catch (Exception e) {
            a.d(TAG, "parse json error..." + e.getMessage());
        }
        return this;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Colums.REQUEST_ADTYPE_COLUMN, Integer.valueOf(this.adtype));
        contentValues.put(Colums.REQUEST_PLACEID_COLUMN, Integer.valueOf(this.placeid));
        contentValues.put("name", this.name);
        contentValues.put("parameter", this.parameter);
        contentValues.put("weight", this.weight);
        return contentValues;
    }

    public static PosBean a(Cursor cursor) {
        PosBean posBean = new PosBean();
        posBean.adtype = cursor.getInt(cursor.getColumnIndex(Colums.REQUEST_ADTYPE_COLUMN));
        posBean.placeid = cursor.getInt(cursor.getColumnIndex(Colums.REQUEST_PLACEID_COLUMN));
        posBean.name = cursor.getString(cursor.getColumnIndex("name"));
        posBean.parameter = cursor.getString(cursor.getColumnIndex("parameter"));
        posBean.weight = Integer.valueOf(cursor.getInt(cursor.getColumnIndex("weight")));
        return posBean;
    }

    public static void a(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + str + "(_id" + " INTEGER PRIMARY KEY," + "adtype INTEGER" + ",placeid" + " INTEGER ," + "weight INTEGER" + ",name" + " TEXT," + "parameter INTEGER " + ");");
    }

    public static void b(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + str);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" adtype:" + this.adtype);
        stringBuilder.append(" placeid:" + this.placeid);
        stringBuilder.append(" name:" + this.name);
        stringBuilder.append(" parameter:" + this.parameter);
        stringBuilder.append(" weight:" + this.weight);
        stringBuilder.append(" info:");
        for (InfoBean infoBean : this.infoList) {
            stringBuilder.append("[");
            stringBuilder.append(" name:" + infoBean.a);
            stringBuilder.append(" parameter:" + infoBean.b);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
