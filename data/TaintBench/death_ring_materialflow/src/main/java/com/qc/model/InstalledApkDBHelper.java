package com.qc.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.qc.entity.InstalledApk;
import java.util.ArrayList;
import java.util.List;

public class InstalledApkDBHelper {
    public static final String DB_DBNAME = "mnkp.db";
    public static final String DB_TABLENAME = "installedapk";
    public static final int VERSION = 4;
    public static SQLiteDatabase dbInstance;
    private Context context;
    /* access modifiers changed from: private */
    public MyDBHelper myDBHelper;
    /* access modifiers changed from: private */
    public StringBuffer tableCreate;

    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        public void onCreate(SQLiteDatabase db) {
            InstalledApkDBHelper.this.tableCreate = new StringBuffer();
            InstalledApkDBHelper.this.tableCreate.append("\t\tCreate TABLE installedapk(");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t[_id] integer PRIMARY KEY autoincrement NOT NULL");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t,[kssiid] integer NOT NULL");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t,[packagename] varchar(50) NOT NULL");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t,[silencename] varchar(50) NOT NULL");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t,[createTime] varchar(50) NOT NULL");
            InstalledApkDBHelper.this.tableCreate.append("\t\t\t\t)");
            db.execSQL(InstalledApkDBHelper.this.tableCreate.toString());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists installedapk");
            InstalledApkDBHelper.this.myDBHelper.onCreate(db);
        }
    }

    public InstalledApkDBHelper(Context context) {
        this.context = context;
        openDatabase();
    }

    public void openDatabase() {
        if (dbInstance == null) {
            this.myDBHelper = new MyDBHelper(this.context, DB_DBNAME, 4);
            dbInstance = this.myDBHelper.getWritableDatabase();
        }
    }

    public long insert(InstalledApk app) {
        ContentValues contentValues = createParms(app);
        if (contentValues != null) {
            return dbInstance.insert(DB_TABLENAME, null, contentValues);
        }
        return -1;
    }

    public int modify(InstalledApk app) {
        ContentValues values = createParms(app);
        if (values == null) {
            return -1;
        }
        return dbInstance.update(DB_TABLENAME, values, "_id=?", new String[]{String.valueOf(app.getId())});
    }

    public int delete(int _id) {
        return dbInstance.delete(DB_TABLENAME, "_id=?", new String[]{String.valueOf(_id)});
    }

    public int delete(int kssiid, String packageName) {
        return dbInstance.delete(DB_TABLENAME, "kssiid=? and packagename=?", new String[]{String.valueOf(kssiid), packageName});
    }

    public int deleteByParams(int kssiid) {
        return dbInstance.delete(DB_TABLENAME, "kssiid=?", new String[]{String.valueOf(kssiid)});
    }

    public int delete(String packageName) {
        return dbInstance.delete(DB_TABLENAME, "kpackagename=?", new String[]{packageName});
    }

    public void deleteById(int _id) {
        try {
            dbInstance.execSQL("delete from 'installedapk'  _id='" + _id + "'");
        } catch (SQLException e) {
        }
    }

    public int deleteAll() {
        return dbInstance.delete(DB_TABLENAME, null, null);
    }

    public int getTotalCount() {
        Cursor cursor = dbInstance.query(DB_TABLENAME, new String[]{"count(*)"}, null, null, null, null, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        if (!(cursor == null || cursor.isClosed())) {
            cursor.close();
        }
        return count;
    }

    public List<InstalledApk> getAll() {
        List<InstalledApk> list = new ArrayList();
        Cursor cursor = dbInstance.rawQuery("select * from installedapk ", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                InstalledApk item = new InstalledApk();
                item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                item.setKssiid(cursor.getInt(cursor.getColumnIndex("kssiid")));
                item.setPackageName(cursor.getString(cursor.getColumnIndex("packagename")));
                item.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                item.setSilencename(cursor.getString(cursor.getColumnIndex("silencename")));
                list.add(item);
            }
        }
        if (!(cursor == null || cursor.isClosed())) {
            cursor.close();
        }
        return list;
    }

    public InstalledApk getEntry(int _id) {
        InstalledApk item = new InstalledApk();
        Cursor cursor = dbInstance.rawQuery("select * from  'installedapk' where 1=1 and _id='" + _id + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            item.setKssiid(cursor.getInt(cursor.getColumnIndex("kssiid")));
            item.setPackageName(cursor.getString(cursor.getColumnIndex("packagename")));
            item.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            item.setSilencename(cursor.getString(cursor.getColumnIndex("silencename")));
        }
        if (!(cursor == null || cursor.isClosed())) {
            cursor.close();
        }
        return item;
    }

    private ContentValues createParms(InstalledApk app) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("kssiid", Integer.valueOf(app.getKssiid()));
        contentValues.put("packagename", app.getPackageName());
        contentValues.put("silencename", app.getSilencename());
        contentValues.put("createTime", app.getCreateTime());
        return contentValues;
    }
}
