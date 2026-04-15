package com.android.ad.du;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import org.json.JSONObject;

public class CheetahCacheDBUtil {
    public static String getAdPackage(Context context, String title) {
        String data = "";
        Log.i("ssp", "---------1---------");
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(context.getDatabasePath("market.db")), null);
            Log.i("ssp", "---------2---------");
            if (db != null) {
                Cursor cursor = db.query("tbl_" + ShowDuAd.mAdPosid, new String[]{"_id", "desc", "pic_url", "title", "pkg", "pkg_url"}, "title=?", new String[]{title}, null, null, null);
                Log.i("ssp", "---------3---------");
                JSONObject json = new JSONObject();
                if (cursor.moveToFirst()) {
                    try {
                        json.put(MASTNativeAdConstants.ID_STRING, cursor.getInt(cursor.getColumnIndex("_id")));
                        json.put("shortDesc", cursor.getString(cursor.getColumnIndex("desc")));
                        json.put(MASTNativeAdConstants.RESPONSE_URL, cursor.getString(cursor.getColumnIndex("pic_url")));
                        json.put("title", cursor.getString(cursor.getColumnIndex("title")));
                        json.put("pkg", cursor.getString(cursor.getColumnIndex("pkg")));
                        json.put("adUrl", cursor.getString(cursor.getColumnIndex("pkg_url")));
                        data = json.toString();
                        Log.i("ssp", "---------data---------" + data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return data;
    }
}
