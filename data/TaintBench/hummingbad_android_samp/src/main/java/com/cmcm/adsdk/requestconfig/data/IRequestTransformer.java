package com.cmcm.adsdk.requestconfig.data;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONObject;

public interface IRequestTransformer {
    Object fromCursor(Cursor cursor);

    Object fromJSONObject(JSONObject jSONObject);

    ContentValues toContentValues();
}
