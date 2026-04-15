package com.cmcm.adsdk.requestconfig.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.cmcm.adsdk.requestconfig.log.a;
import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONObject;

public final class ConfigResponseHeader implements IRequestTransformer {
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;

    public interface Colums extends BaseColumns {
        public static final String REQUEST_MID_COLUMN = "mid";
        public static final String REQUEST_MT_TYPE_COLUMN = "mt_type";
        public static final String REQUEST_POSID_COLUMN = "pos_id";
        public static final String REQUEST_SHOW_TYPE_COLUMN = "show_type";
    }

    public final Object fromJSONObject(JSONObject o) {
        try {
            this.a = o.getInt(MASTNativeAdConstants.RESPONSE_ERROR_CODE);
            this.b = o.getInt("mid");
            this.c = o.getInt(Colums.REQUEST_MT_TYPE_COLUMN);
            this.d = o.getInt("posid");
            this.e = o.getInt(Colums.REQUEST_SHOW_TYPE_COLUMN);
        } catch (Exception e) {
            a.d("ConfigResponseHeader", "parse json error..." + e.getMessage());
        }
        return this;
    }

    public final ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Colums.REQUEST_POSID_COLUMN, Integer.valueOf(this.d));
        contentValues.put("mid", Integer.valueOf(this.b));
        contentValues.put(Colums.REQUEST_MT_TYPE_COLUMN, Integer.valueOf(this.c));
        contentValues.put(Colums.REQUEST_SHOW_TYPE_COLUMN, Integer.valueOf(this.e));
        return contentValues;
    }

    public final String toString() {
        return String.format("(:pos_id %s :code %d :mid %d :mt_type %d :show_type %d", new Object[]{Integer.valueOf(this.d), Integer.valueOf(this.a), Integer.valueOf(this.b), Integer.valueOf(this.e), Integer.valueOf(this.c), Integer.valueOf(this.e)});
    }

    public final /* synthetic */ Object fromCursor(Cursor x0) {
        ConfigResponseHeader configResponseHeader = new ConfigResponseHeader();
        configResponseHeader.d = x0.getInt(x0.getColumnIndex(Colums.REQUEST_POSID_COLUMN));
        configResponseHeader.b = x0.getInt(x0.getColumnIndex("mid"));
        configResponseHeader.c = x0.getInt(x0.getColumnIndex(Colums.REQUEST_MT_TYPE_COLUMN));
        configResponseHeader.e = x0.getInt(x0.getColumnIndex(Colums.REQUEST_SHOW_TYPE_COLUMN));
        return configResponseHeader;
    }
}
