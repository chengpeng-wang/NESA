package com.android.ad.du;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduCacheDBUtil {

    private static class DBGpPkgName extends SQLiteOpenHelper {
        private static final String COL = "pkg";
        private static final String DATABASE_NAME = "db_gp_pkg_name";
        private static final String TB = "tb_gp_pkg_name";

        public DBGpPkgName(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table tb_gp_pkg_name(pkg string)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static class DBGpPkgUtils {
        private static DBGpPkgUtils instance;
        private DBGpPkgName db;
        private Context mCtx;
        private SQLiteDatabase mReadDb;
        private SQLiteDatabase mWriteDb;

        private DBGpPkgUtils(Context ctx) {
            this.mCtx = ctx;
            init();
        }

        public static DBGpPkgUtils getInstance(Context ctx) {
            if (instance == null) {
                instance = new DBGpPkgUtils(ctx);
            }
            return instance;
        }

        private void init() {
            this.db = new DBGpPkgName(this.mCtx);
            this.mReadDb = this.db.getReadableDatabase();
            this.mWriteDb = this.db.getWritableDatabase();
        }

        public void addPkgName(String pkgname) {
            if (!exist(pkgname)) {
                this.mWriteDb.execSQL("insert into tb_gp_pkg_name values('" + pkgname + "')");
            }
        }

        public boolean exist(String pkgname) {
            if (this.mReadDb.query("tb_gp_pkg_name", new String[]{"pkg"}, "pkg=?", new String[]{pkgname}, null, null, null).moveToNext()) {
                return true;
            }
            return false;
        }

        public ArrayList<String> listPkgName() {
            Cursor c = this.mReadDb.query("tb_gp_pkg_name", null, null, null, null, null, null);
            ArrayList<String> pkgnames = new ArrayList();
            while (c.moveToNext()) {
                pkgnames.add(c.getString(c.getColumnIndex("pkg")));
            }
            return pkgnames;
        }
    }

    public static String getBaiDuAdPackage(String title, Context context) {
        try {
            JSONArray list = new JSONObject(getData(context)).getJSONObject("datas").getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject note = list.getJSONObject(i);
                if (title.equals(note.getString("title"))) {
                    return note.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<DuAd> readAllDuAd(Context context) {
        String data = getData(context);
        ArrayList<DuAd> duAds = new ArrayList();
        try {
            JSONArray list = new JSONObject(data).getJSONObject("datas").getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                DuAd ad = parseJson(list.getJSONObject(i));
                if (ad != null) {
                    duAds.add(ad);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return duAds;
    }

    private static String getData(Context context) {
        String data = "";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(context.getDatabasePath("du_ad_cache.db")), null);
            if (db != null) {
                Cursor cursor = db.query("cache", new String[]{"data"}, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    data = cursor.getString(cursor.getColumnIndex("data"));
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static DuAdData findDbDuAdData(Context context, String appName) {
        Iterator it = listDuAdDatasFromAppCache(context).iterator();
        while (it.hasNext()) {
            DuAdData duAdData = (DuAdData) it.next();
            if (duAdData.appname.equals(appName)) {
                return duAdData;
            }
        }
        return null;
    }

    private static ArrayList<DuAdData> listDuAdDatasFromAppCache(Context context) {
        ArrayList<DuAdData> duAdDatas = new ArrayList();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(context.getDatabasePath("du_ad_cache.db")), null);
            if (db != null) {
                Cursor cursor = db.query("appcache", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String adId = cursor.getString(cursor.getColumnIndex("ad_id"));
                    String pkg = cursor.getString(cursor.getColumnIndex("pkgName"));
                    JSONObject joData = new JSONObject(cursor.getString(cursor.getColumnIndex("data"))).getJSONObject("data");
                    duAdDatas.add(new DuAdData(adId, pkg, joData.getString("name"), joData.getString("playurl"), joData.getString("sdesc")));
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duAdDatas;
    }

    public static DuAd jsonToDuAd(String adString) {
        if (!TextUtils.isEmpty(adString)) {
            try {
                return parseJson(new JSONObject(adString));
            } catch (JSONException e) {
            }
        }
        return null;
    }

    public static String getAdPackageName(Context context, String title) {
        String str = null;
        String pkgData = getBaiDuAdPackage(title, context);
        if (TextUtils.isEmpty(pkgData)) {
            return str;
        }
        try {
            return new JSONObject(pkgData).getString("pkg");
        } catch (JSONException e) {
            e.printStackTrace();
            return str;
        }
    }

    private static DuAd parseJson(JSONObject note) {
        JSONException e;
        DuAd ad = null;
        try {
            DuAd ad2 = new DuAd();
            try {
                ad2.setGid(new StringBuilder(String.valueOf(note.getInt(MASTNativeAdConstants.ID_STRING))).toString());
                ad2.setContent(note.getString("shortDesc"));
                ad2.setIcon(note.getJSONArray("images").getJSONObject(0).getString(MASTNativeAdConstants.RESPONSE_URL));
                ad2.setTitle(note.getString("title"));
                ad2.setPname(note.getString("pkg"));
                ad2.setDuUrl(note.getString("adUrl"));
                return ad2;
            } catch (JSONException e2) {
                e = e2;
                ad = ad2;
                e.printStackTrace();
                return ad;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return ad;
        }
    }
}
