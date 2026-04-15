package com.cleanmaster.ui.app.market.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.umeng.analytics.onlineconfig.a;
import java.io.Serializable;
import org.json.JSONObject;

public class PackageStatInfo implements Serializable {
    public static final int VERSION = 2;
    private static final long serialVersionUID = 7094307304673091847L;
    private boolean mIsExpended = false;
    private String mPackageName = null;
    private int mVersionCode = 0;

    public PackageStatInfo(String packageName) {
        this.mPackageName = packageName;
    }

    public void setVersionCode(int versioncode) {
        this.mVersionCode = versioncode;
    }

    public void setExpended(boolean b) {
        this.mIsExpended = b;
    }

    public boolean isExpended() {
        return this.mIsExpended;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public static PackageStatInfo fromJSONObject(JSONObject o) {
        Exception e;
        if (o == null) {
            return null;
        }
        PackageStatInfo packageStatInfo;
        try {
            String string = o.getString("pkg_name");
            String string2 = o.getString(a.e);
            packageStatInfo = new PackageStatInfo(string);
            try {
                packageStatInfo.setVersionCode(Integer.valueOf(string2).intValue());
                return packageStatInfo;
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            Exception exception = e3;
            packageStatInfo = null;
            e = exception;
            e.printStackTrace();
            return packageStatInfo;
        }
    }

    public String toString() {
        return String.format("%s : %d", new Object[]{getPackageName(), Integer.valueOf(getVersionCode())});
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pkg_name", getPackageName());
        contentValues.put(a.e, Integer.valueOf(getVersionCode()));
        return contentValues;
    }

    public static void createTable(SQLiteDatabase db, String tname) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tname + "(_id" + " INTEGER PRIMARY KEY," + "pkg_name TEXT" + ",version_code" + " INTEGER);");
    }
}
