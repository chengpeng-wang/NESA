package com.cmcm.adsdk.requestconfig.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.cmcm.adsdk.requestconfig.log.a;
import org.json.JSONObject;

public final class InfoBean implements IRequestTransformer {
    public String a;
    public String b;
    public int c;

    public interface Colums extends BaseColumns {
        public static final String REQUEST_NAME_COLUMN = "name";
        public static final String REQUEST_PARAMETER_COLUMN = "parameter";
        public static final String REQUEST_WEIGHT_COLUMN = "weight";
    }

    public final Object fromJSONObject(JSONObject o) {
        try {
            this.a = o.getString("name");
            this.b = o.getString("parameter");
            this.c = o.getInt("weight");
        } catch (Exception e) {
            a.d("InfoBean", "parse json error..." + e.getMessage());
        }
        return this;
    }

    public final ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", this.a);
        contentValues.put("parameter", this.b);
        contentValues.put("weight", Integer.valueOf(this.c));
        return contentValues;
    }

    public final Object fromCursor(Cursor cursor) {
        InfoBean infoBean = new InfoBean();
        infoBean.a = cursor.getString(cursor.getColumnIndex("name"));
        infoBean.b = cursor.getString(cursor.getColumnIndex("parameter"));
        infoBean.c = cursor.getInt(cursor.getColumnIndex("weight"));
        return infoBean;
    }

    public final String toString() {
        return String.format("(name %s :parameter %s :weight %d)", new Object[]{this.a, this.b, Integer.valueOf(this.c)});
    }
}
