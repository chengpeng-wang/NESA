package com.baidu.android.pushservice.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b.a;
import com.baidu.android.pushservice.b.b;
import com.baidu.android.pushservice.b.f;
import com.baidu.android.pushservice.b.i;
import com.baidu.android.pushservice.b.j;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class PushDatabase implements NoProGuard {
    private static final String DB_DIR = "baidu/pushservice/database";
    private static final String DB_NAME = "pushstat.db";
    private static final int DB_VERSION = 2;
    private static final String TAG = "PushDatabase";
    private static h mDbHelper;

    public static long clearAppInfo(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase == null ? 0 : (long) sQLiteDatabase.delete("AppInfo", null, null);
    }

    public static long clearBehaviorInfo(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase == null ? 0 : (long) sQLiteDatabase.delete("PushBehavior", null, null);
    }

    public static long clearFileDownloadingInfo(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase == null ? 0 : (long) sQLiteDatabase.delete("FileDownloadingInfo", null, null);
    }

    public static long clearStatisticsInfo(SQLiteDatabase sQLiteDatabase) {
        return sQLiteDatabase == null ? 0 : (long) sQLiteDatabase.delete("StatisticsInfo", null, null);
    }

    public static void close() {
        if (mDbHelper != null && mDbHelper.getWritableDatabase() != null && mDbHelper.getWritableDatabase().isOpen()) {
            mDbHelper.close();
            mDbHelper = null;
        }
    }

    public static int deleteBehavior(SQLiteDatabase sQLiteDatabase, int i) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        return sQLiteDatabase.delete("PushBehavior", f.actionId.name() + "=?", new String[]{"" + i});
    }

    public static int deleteFileDownloadingInfo(SQLiteDatabase sQLiteDatabase, String str) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        return sQLiteDatabase.delete("FileDownloadingInfo", i.downloadUrl.name() + "=?", new String[]{str});
    }

    public static void deleteOldBehaviors(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase != null) {
            long currentTimeMillis = System.currentTimeMillis() - 604800000;
            sQLiteDatabase.delete("PushBehavior", f.timeStamp + "<?", new String[]{currentTimeMillis + ""});
        }
    }

    public static int deleteStatisticsInfo(SQLiteDatabase sQLiteDatabase, int i) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        return sQLiteDatabase.delete("StatisticsInfo", l.info_id.name() + "=?", new String[]{"" + i});
    }

    public static List getApiBehaviorInfoByAppId(SQLiteDatabase sQLiteDatabase, String str) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str2 = f.appid.name() + " = " + str + ";";
        Cursor query = sQLiteDatabase.query("PushBehavior", null, str2, null, null, null, null);
        while (query.moveToNext()) {
            f fVar = new f(query.getString(query.getColumnIndex(f.actionName.name())), query.getLong(query.getColumnIndex(f.timeStamp.name())), query.getString(query.getColumnIndex(f.networkStatus.name())), query.getInt(query.getColumnIndex(f.errorCode.name())), query.getString(query.getColumnIndex(f.appid.name())));
            if (fVar.i() == f.c) {
                a aVar = new a(fVar);
                aVar.a(query.getString(query.getColumnIndex(f.errorMsg.name())));
                aVar.b(query.getString(query.getColumnIndex(f.requestId.name())));
                arrayList.add(aVar);
            }
        }
        query.close();
        return arrayList;
    }

    public static List getAppInfo(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM AppInfo;", null);
        while (rawQuery.moveToNext()) {
            b bVar = new b();
            bVar.a(rawQuery.getString(rawQuery.getColumnIndex(e.appid.name())));
            bVar.b(rawQuery.getString(rawQuery.getColumnIndex(e.rsaUserId.name())));
            bVar.c(rawQuery.getString(rawQuery.getColumnIndex(e.userId.name())));
            bVar.d(rawQuery.getString(rawQuery.getColumnIndex(e.packageName.name())));
            bVar.e(rawQuery.getString(rawQuery.getColumnIndex(e.appName.name())));
            bVar.f(rawQuery.getString(rawQuery.getColumnIndex(e.cFrom.name())));
            bVar.a(rawQuery.getInt(rawQuery.getColumnIndex(e.versionCode.name())));
            bVar.g(rawQuery.getString(rawQuery.getColumnIndex(e.versionName.name())));
            bVar.b(rawQuery.getInt(rawQuery.getColumnIndex(e.intergratedPushVersion.name())));
            arrayList.add(bVar);
        }
        rawQuery.close();
        return arrayList;
    }

    public static int getBehaviorInfoCounts(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            return 0;
        }
        Cursor query = sQLiteDatabase.query("PushBehavior", null, null, null, null, null, null);
        int count = query.getCount();
        query.close();
        return count;
    }

    public static SQLiteDatabase getDb(Context context) {
        SQLiteDatabase sQLiteDatabase = null;
        h dbOpenHelper = getDbOpenHelper(context);
        if (dbOpenHelper == null) {
            return sQLiteDatabase;
        }
        try {
            return dbOpenHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return sQLiteDatabase;
        }
    }

    private static h getDbOpenHelper(Context context) {
        if (mDbHelper == null) {
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return null;
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DB_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            mDbHelper = new h(context, file.getAbsolutePath() + File.separator + DB_NAME, 2);
        }
        return mDbHelper;
    }

    public static j getFileDownloadingInfo(SQLiteDatabase sQLiteDatabase, String str) {
        j jVar = null;
        SQLiteDatabase sQLiteDatabase2 = sQLiteDatabase;
        Cursor query = sQLiteDatabase2.query("FileDownloadingInfo", null, "(" + i.downloadUrl.name() + "==?" + ")", new String[]{str}, null, null, null);
        if (query.moveToFirst()) {
            jVar = new j();
            jVar.a = query.getString(query.getColumnIndex(i.belongTo.name()));
            jVar.b = query.getString(query.getColumnIndex(i.downloadUrl.name()));
            jVar.c = query.getString(query.getColumnIndex(i.title.name()));
            jVar.d = query.getString(query.getColumnIndex(i.description.name()));
            jVar.e = query.getString(query.getColumnIndex(i.savePath.name()));
            jVar.f = query.getString(query.getColumnIndex(i.fileName.name()));
            jVar.g = query.getInt(query.getColumnIndex(i.downloadBytes.name()));
            jVar.h = query.getInt(query.getColumnIndex(i.totalBytes.name()));
            jVar.i = query.getInt(query.getColumnIndex(i.downloadStatus.name()));
            jVar.j = query.getLong(query.getColumnIndex(i.timeStamp.name()));
        }
        query.close();
        return jVar;
    }

    public static int getFileDownloadingInfoCounts(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        Cursor query = sQLiteDatabase.query("FileDownloadingInfo", null, null, null, null, null, null);
        int count = query.getCount();
        query.close();
        return count;
    }

    public static List getPromptBehaviorInfo(SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str = f.actionName.name() + " = " + "030101" + ";";
        Cursor query = sQLiteDatabase.query("PushBehavior", null, str, null, null, null, null);
        while (query.moveToNext()) {
            f fVar = new f(query.getString(query.getColumnIndex(f.actionName.name())), query.getLong(query.getColumnIndex(f.timeStamp.name())), query.getString(query.getColumnIndex(f.networkStatus.name())), query.getInt(query.getColumnIndex(f.errorCode.name())), query.getString(query.getColumnIndex(f.appid.name())));
            if (fVar.i() == f.d) {
                i iVar = new i(fVar);
                iVar.b(query.getInt(query.getColumnIndex(f.stableHeartInterval.name())));
                arrayList.add(iVar);
            }
        }
        query.close();
        return arrayList;
    }

    public static List getPushBehaviorInfo(SQLiteDatabase sQLiteDatabase, int i) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str = "" + i;
        Cursor query = sQLiteDatabase.query("PushBehavior", null, null, null, null, null, null, str);
        while (query.moveToNext()) {
            f fVar = new f(query.getString(query.getColumnIndex(f.actionName.name())), query.getLong(query.getColumnIndex(f.timeStamp.name())), query.getString(query.getColumnIndex(f.networkStatus.name())), query.getInt(query.getColumnIndex(f.errorCode.name())), query.getString(query.getColumnIndex(f.appid.name())));
            fVar.e(query.getString(query.getColumnIndex(f.appid.name())));
            if (fVar.i() == f.b) {
                j jVar = new j(fVar);
                jVar.c(query.getInt(query.getColumnIndex(f.msgType.name())));
                jVar.a(query.getString(query.getColumnIndex(f.msgId.name())));
                jVar.b(query.getInt(query.getColumnIndex(f.msgLen.name())));
                arrayList.add(jVar);
            } else if (fVar.i() == f.c) {
                a aVar = new a(fVar);
                aVar.a(query.getString(query.getColumnIndex(f.errorMsg.name())));
                aVar.b(query.getString(query.getColumnIndex(f.requestId.name())));
                arrayList.add(aVar);
            } else if (fVar.i() == f.d) {
                i iVar = new i(fVar);
                iVar.b(query.getInt(query.getColumnIndex(f.stableHeartInterval.name())));
                arrayList.add(iVar);
            }
        }
        query.close();
        return arrayList;
    }

    public static List getPushBehaviorInfoByAppId(SQLiteDatabase sQLiteDatabase, String str) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str2 = f.appid.name() + " = " + str + ";";
        Cursor query = sQLiteDatabase.query("PushBehavior", null, str2, null, null, null, null);
        while (query.moveToNext()) {
            f fVar = new f(query.getString(query.getColumnIndex(f.actionName.name())), query.getLong(query.getColumnIndex(f.timeStamp.name())), query.getString(query.getColumnIndex(f.networkStatus.name())), query.getInt(query.getColumnIndex(f.errorCode.name())), query.getString(query.getColumnIndex(f.appid.name())));
            if (fVar.i() == f.b) {
                j jVar = new j(fVar);
                jVar.c(query.getInt(query.getColumnIndex(f.msgType.name())));
                jVar.a(query.getString(query.getColumnIndex(f.msgId.name())));
                jVar.b(query.getInt(query.getColumnIndex(f.msgLen.name())));
                arrayList.add(jVar);
            }
        }
        query.close();
        return arrayList;
    }

    public static int getStatisticsInfoCounts(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            return 0;
        }
        Cursor query = sQLiteDatabase.query("StatisticsInfo", null, null, null, null, null, null);
        int count = query.getCount();
        query.close();
        return count;
    }

    public static long insertApiBehavior(SQLiteDatabase sQLiteDatabase, a aVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(f.actionName.name(), aVar.d());
        contentValues.put(f.timeStamp.name(), Long.valueOf(aVar.e()));
        contentValues.put(f.networkStatus.name(), aVar.f());
        contentValues.put(f.appid.name(), aVar.h());
        contentValues.put(f.errorMsg.name(), aVar.a());
        contentValues.put(f.requestId.name(), aVar.b());
        contentValues.put(f.errorCode.name(), Integer.valueOf(aVar.g()));
        return sQLiteDatabase.insert("PushBehavior", null, contentValues);
    }

    public static long insertAppInfo(SQLiteDatabase sQLiteDatabase, b bVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        if (needToInsertUpdate(sQLiteDatabase, bVar) != null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(e.appid.name(), bVar.a());
        contentValues.put(e.rsaUserId.name(), bVar.b());
        contentValues.put(e.userId.name(), bVar.c());
        contentValues.put(e.packageName.name(), bVar.d());
        contentValues.put(e.appName.name(), bVar.e());
        contentValues.put(e.cFrom.name(), bVar.f());
        contentValues.put(e.versionCode.name(), Integer.valueOf(bVar.g()));
        contentValues.put(e.versionName.name(), bVar.h());
        contentValues.put(e.intergratedPushVersion.name(), Integer.valueOf(bVar.i()));
        return sQLiteDatabase.insert("AppInfo", null, contentValues);
    }

    public static long insertFileDownloadingInfo(SQLiteDatabase sQLiteDatabase, j jVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(i.belongTo.name(), jVar.a);
        contentValues.put(i.downloadUrl.name(), jVar.b);
        contentValues.put(i.title.name(), jVar.c);
        contentValues.put(i.description.name(), jVar.d);
        contentValues.put(i.savePath.name(), jVar.e);
        contentValues.put(i.fileName.name(), jVar.f);
        contentValues.put(i.downloadBytes.name(), Integer.valueOf(jVar.g));
        contentValues.put(i.totalBytes.name(), Integer.valueOf(jVar.h));
        contentValues.put(i.downloadStatus.name(), Integer.valueOf(jVar.i));
        contentValues.put(i.timeStamp.name(), Long.valueOf(System.currentTimeMillis()));
        return sQLiteDatabase.insert("FileDownloadingInfo", null, contentValues);
    }

    public static long insertPromptBehavior(SQLiteDatabase sQLiteDatabase, i iVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(f.actionName.name(), iVar.d());
        contentValues.put(f.timeStamp.name(), Long.valueOf(iVar.e()));
        contentValues.put(f.networkStatus.name(), iVar.f());
        contentValues.put(f.stableHeartInterval.name(), Integer.valueOf(iVar.a()));
        contentValues.put(f.errorCode.name(), Integer.valueOf(iVar.g()));
        contentValues.put(f.appid.name(), iVar.h());
        return sQLiteDatabase.insert("PushBehavior", null, contentValues);
    }

    public static long insertPushBehavior(SQLiteDatabase sQLiteDatabase, j jVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(f.actionName.name(), jVar.d());
        contentValues.put(f.timeStamp.name(), Long.valueOf(jVar.e()));
        contentValues.put(f.networkStatus.name(), jVar.f());
        if (jVar.h() != null) {
            contentValues.put(f.appid.name(), jVar.h());
        }
        contentValues.put(f.msgType.name(), Integer.valueOf(jVar.j()));
        contentValues.put(f.msgId.name(), jVar.a());
        contentValues.put(f.msgLen.name(), Integer.valueOf(jVar.b()));
        contentValues.put(f.errorCode.name(), Integer.valueOf(jVar.g()));
        return sQLiteDatabase.insert("PushBehavior", null, contentValues);
    }

    public static long insertStatisticsInfo(SQLiteDatabase sQLiteDatabase, k kVar) {
        if (sQLiteDatabase == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(l.packageName.name(), kVar.b);
        contentValues.put(l.msgid.name(), kVar.d);
        contentValues.put(l.open_type.name(), kVar.c);
        contentValues.put(l.app_open_time.name(), kVar.e);
        contentValues.put(l.app_close_time.name(), kVar.f);
        contentValues.put(l.use_duration.name(), kVar.g);
        if (kVar.h != null) {
            contentValues.put(l.extra.name(), kVar.h.toString());
        }
        return sQLiteDatabase.insert("StatisticsInfo", null, contentValues);
    }

    public static b needToInsertUpdate(SQLiteDatabase sQLiteDatabase, b bVar) {
        if (sQLiteDatabase == null) {
            return null;
        }
        b bVar2 = new b(bVar.a());
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM AppInfo WHERE " + e.appid.name() + " = " + bVar.a() + ";", null);
        Object obj = null;
        if (rawQuery.moveToNext()) {
            obj = 1;
            int i = rawQuery.getInt(rawQuery.getColumnIndex(e.appInfoId.name()));
            if (!(TextUtils.equals(bVar.c(), rawQuery.getString(rawQuery.getColumnIndex(e.userId.name()))) && TextUtils.equals(bVar.f(), rawQuery.getString(rawQuery.getColumnIndex(e.cFrom.name()))) && TextUtils.equals(bVar.g() + "", rawQuery.getInt(rawQuery.getColumnIndex(e.versionCode.name())) + ""))) {
                updateAppInfo(sQLiteDatabase, i, bVar);
            }
        }
        rawQuery.close();
        return obj != null ? bVar2 : null;
    }

    public static List selectFileDownloadingInfo(SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str = i.timeStamp.name() + " DESC";
        Cursor query = sQLiteDatabase.query("FileDownloadingInfo", null, null, null, null, null, str);
        while (query.moveToNext()) {
            j jVar = new j();
            jVar.a = query.getString(query.getColumnIndex(i.belongTo.name()));
            jVar.b = query.getString(query.getColumnIndex(i.downloadUrl.name()));
            jVar.c = query.getString(query.getColumnIndex(i.title.name()));
            jVar.d = query.getString(query.getColumnIndex(i.description.name()));
            jVar.e = query.getString(query.getColumnIndex(i.savePath.name()));
            jVar.f = query.getString(query.getColumnIndex(i.fileName.name()));
            jVar.g = query.getInt(query.getColumnIndex(i.downloadBytes.name()));
            jVar.h = query.getInt(query.getColumnIndex(i.totalBytes.name()));
            jVar.i = query.getInt(query.getColumnIndex(i.downloadStatus.name()));
            jVar.j = query.getLong(query.getColumnIndex(i.timeStamp.name()));
            arrayList.add(jVar);
        }
        query.close();
        return arrayList;
    }

    public static List selectStatisticsInfo(SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        Cursor query = sQLiteDatabase.query("StatisticsInfo", null, null, null, null, null, null);
        while (query.moveToNext()) {
            k kVar = new k();
            kVar.a = query.getInt(query.getColumnIndex(l.info_id.name()));
            kVar.b = query.getString(query.getColumnIndex(l.packageName.name()));
            kVar.c = query.getString(query.getColumnIndex(l.open_type.name()));
            kVar.d = query.getString(query.getColumnIndex(l.msgid.name()));
            kVar.e = query.getString(query.getColumnIndex(l.app_open_time.name()));
            kVar.f = query.getString(query.getColumnIndex(l.app_close_time.name()));
            kVar.g = query.getString(query.getColumnIndex(l.use_duration.name()));
            String string = query.getString(query.getColumnIndex(l.extra.name()));
            if (string != null) {
                try {
                    kVar.h = new JSONObject(string);
                } catch (JSONException e) {
                    Log.w(TAG, "new json object error !");
                }
            }
            arrayList.add(kVar);
        }
        query.close();
        return arrayList;
    }

    public static List selectStatisticsInfo(SQLiteDatabase sQLiteDatabase, int i) {
        ArrayList arrayList = new ArrayList();
        if (sQLiteDatabase == null) {
            return arrayList;
        }
        String str = "" + i;
        Cursor query = sQLiteDatabase.query("StatisticsInfo", null, null, null, null, null, null, str);
        while (query.moveToNext()) {
            k kVar = new k();
            kVar.a = query.getInt(query.getColumnIndex(l.info_id.name()));
            kVar.b = query.getString(query.getColumnIndex(l.packageName.name()));
            kVar.c = query.getString(query.getColumnIndex(l.open_type.name()));
            kVar.d = query.getString(query.getColumnIndex(l.msgid.name()));
            kVar.e = query.getString(query.getColumnIndex(l.app_open_time.name()));
            kVar.f = query.getString(query.getColumnIndex(l.app_close_time.name()));
            kVar.g = query.getString(query.getColumnIndex(l.use_duration.name()));
            String string = query.getString(query.getColumnIndex(l.extra.name()));
            if (string != null) {
                try {
                    kVar.h = new JSONObject(string);
                } catch (JSONException e) {
                    Log.w(TAG, "new json object error !");
                }
            }
            arrayList.add(kVar);
        }
        query.close();
        return arrayList;
    }

    public static int updateAppInfo(SQLiteDatabase sQLiteDatabase, int i, b bVar) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        String[] strArr = new String[]{"" + i};
        ContentValues contentValues = new ContentValues();
        contentValues.put(e.appInfoId.name(), Integer.valueOf(i));
        contentValues.put(e.appid.name(), bVar.a());
        contentValues.put(e.rsaUserId.name(), bVar.b());
        contentValues.put(e.userId.name(), bVar.c());
        contentValues.put(e.packageName.name(), bVar.d());
        contentValues.put(e.appName.name(), bVar.e());
        if (!TextUtils.isEmpty(bVar.f())) {
            contentValues.put(e.cFrom.name(), bVar.f());
        }
        contentValues.put(e.versionCode.name(), Integer.valueOf(bVar.g()));
        contentValues.put(e.versionName.name(), bVar.h());
        contentValues.put(e.intergratedPushVersion.name(), Integer.valueOf(bVar.i()));
        return sQLiteDatabase.update("AppInfo", contentValues, e.appInfoId.name() + "=?", strArr);
    }

    public static int updateBehavior(SQLiteDatabase sQLiteDatabase, int i, f fVar) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        String[] strArr = new String[]{"" + i};
        ContentValues contentValues = new ContentValues();
        contentValues.put(f.actionId.name(), Integer.valueOf(fVar.c()));
        contentValues.put(f.actionName.name(), fVar.d());
        contentValues.put(f.timeStamp.name(), Long.valueOf(fVar.e()));
        contentValues.put(f.networkStatus.name(), fVar.f());
        if (fVar.h() != null) {
            contentValues.put(f.appid.name(), fVar.h());
        }
        if (fVar.i() == f.b) {
            j jVar = new j(fVar);
            contentValues.put(f.msgType.name(), Integer.valueOf(jVar.j()));
            contentValues.put(f.msgId.name(), jVar.a());
            contentValues.put(f.msgLen.name(), Integer.valueOf(jVar.b()));
        } else if (fVar.i() == f.c) {
            a aVar = new a(fVar);
            contentValues.put(f.errorMsg.name(), aVar.a());
            contentValues.put(f.requestId.name(), aVar.b());
        } else if (fVar.i() == f.d) {
            contentValues.put(f.stableHeartInterval.name(), Integer.valueOf(new i(fVar).a()));
        }
        contentValues.put(f.errorCode.name(), Integer.valueOf(fVar.g()));
        return sQLiteDatabase.update("PushBehavior", contentValues, f.actionId.name() + "=?", strArr);
    }

    public static int updateFileDownloadingInfo(SQLiteDatabase sQLiteDatabase, String str, j jVar) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        String[] strArr = new String[]{str};
        ContentValues contentValues = new ContentValues();
        contentValues.put(i.belongTo.name(), jVar.a);
        contentValues.put(i.downloadUrl.name(), jVar.b);
        contentValues.put(i.title.name(), jVar.c);
        contentValues.put(i.description.name(), jVar.d);
        contentValues.put(i.savePath.name(), jVar.e);
        contentValues.put(i.fileName.name(), jVar.f);
        contentValues.put(i.downloadBytes.name(), Integer.valueOf(jVar.g));
        contentValues.put(i.totalBytes.name(), Integer.valueOf(jVar.h));
        contentValues.put(i.downloadStatus.name(), Integer.valueOf(jVar.i));
        contentValues.put(i.timeStamp.name(), Long.valueOf(System.currentTimeMillis()));
        return sQLiteDatabase.update("FileDownloadingInfo", contentValues, i.downloadUrl.name() + "=?", strArr);
    }

    public static int updateStatisticsInfo(SQLiteDatabase sQLiteDatabase, int i, k kVar) {
        if (sQLiteDatabase == null) {
            return 0;
        }
        String[] strArr = new String[]{"" + i};
        ContentValues contentValues = new ContentValues();
        contentValues.put(l.info_id.name(), Integer.valueOf(kVar.a));
        contentValues.put(l.packageName.name(), kVar.b);
        contentValues.put(l.msgid.name(), kVar.d);
        contentValues.put(l.open_type.name(), kVar.c);
        contentValues.put(l.app_open_time.name(), kVar.e);
        contentValues.put(l.app_close_time.name(), kVar.f);
        contentValues.put(l.use_duration.name(), kVar.g);
        contentValues.put(l.extra.name(), kVar.h.toString());
        return sQLiteDatabase.update("StatisticsInfo", contentValues, l.info_id.name() + "=?", strArr);
    }
}
