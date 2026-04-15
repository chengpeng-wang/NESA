package com.mb.bdapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mb.bdapp.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private static final String TAG = DBService.class.getSimpleName();
    private static DBService dbService;
    private SQLiteDatabase db;
    private Context mContext;
    private DBOpenHelper openHelper;

    private DBService(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
            try {
                initDB(this.mContext);
            } catch (Exception e) {
            }
        }
    }

    public static DBService getInstance(Context context) {
        if (dbService == null) {
            dbService = new DBService(context);
        }
        return dbService;
    }

    private SQLiteDatabase initDB(Context context) {
        if (this.openHelper == null) {
            this.openHelper = DBOpenHelper.getInstance(context);
        }
        if (!(this.db != null && this.openHelper.getReadableDatabase().isDbLockedByOtherThreads() && this.db.isOpen())) {
            this.db = this.openHelper.getReadableDatabase();
        }
        return this.db;
    }

    public void close() {
        try {
            if (this.openHelper != null) {
                this.openHelper.close();
                this.openHelper = null;
            }
            if (this.db != null) {
                this.db.close();
                this.db = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        close();
    }

    private boolean checkDB() {
        int count = 0;
        while (true) {
            try {
                if ((this.db != null && this.db.isOpen()) || count >= 3) {
                    break;
                }
                count++;
                initDB(this.mContext);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
            }
        }
        if (this.db == null || !this.db.isOpen()) {
            return false;
        }
        return true;
    }

    public long insertAd(DuAd ad) throws Exception {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put("gid", ad.getGid());
            values.put("title", ad.getTitle());
            values.put("content", ad.getContent());
            values.put(DuAd.DB_ICON, ad.getIcon());
            values.put(DuAd.DB_DU_URL, ad.getDuUrl());
            values.put("pname", ad.getPname());
            values.put(DuAd.DB_REFERRER, ad.getReferrer());
            values.put(DuAd.DB_DOWN_URL, ad.getDownUrl());
            values.put(DuAd.DB_STATUS, Integer.valueOf(ad.getStatus()));
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(ad.getDownRetry()));
            values.put(DuAd.DB_MODIFY_TIME, Long.valueOf(System.currentTimeMillis()));
            return this.db.insert(DuAd.DB_TB, null, values);
        }
        throw new Exception("数据库连接失败！");
    }

    public DuAd queryAdByGid(String gid) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "gid=? ", new String[]{gid}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    public DuAd queryAdByPname(String pname) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "pname=? ", new String[]{pname}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    public DuAd queryAdById(int id) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "_ID=? ", new String[]{new StringBuilder(String.valueOf(id)).toString()}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    private static DuAd parseCursorToAd(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        DuAd ad = new DuAd();
        ad.set_ID(cursor.getInt(cursor.getColumnIndex(DuAd.DB_ID)));
        ad.setGid(cursor.getString(cursor.getColumnIndex("gid")));
        ad.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        ad.setContent(cursor.getString(cursor.getColumnIndex("content")));
        ad.setIcon(cursor.getString(cursor.getColumnIndex(DuAd.DB_ICON)));
        ad.setPname(cursor.getString(cursor.getColumnIndex("pname")));
        ad.setDuUrl(cursor.getString(cursor.getColumnIndex(DuAd.DB_DU_URL)));
        ad.setReferrer(cursor.getString(cursor.getColumnIndex(DuAd.DB_REFERRER)));
        ad.setDownUrl(cursor.getString(cursor.getColumnIndex(DuAd.DB_DOWN_URL)));
        ad.setStatus(cursor.getInt(cursor.getColumnIndex(DuAd.DB_STATUS)));
        ad.setDownRetry(cursor.getInt(cursor.getColumnIndex(DuAd.DB_DOWN_RETRY)));
        ad.setInstallRetry(cursor.getInt(cursor.getColumnIndex(DuAd.DB_INSTALL_RETRY)));
        ad.setModifyTime(cursor.getLong(cursor.getColumnIndex(DuAd.DB_MODIFY_TIME)));
        return ad;
    }

    public void deleteAdById(long _ID) {
        if (checkDB()) {
            this.db.delete(DuAd.DB_TB, "_ID =?", new String[]{Long.toString(_ID)});
        }
    }

    public void updateAdDownRetryById(long id, int retry) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(retry));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void updateAdInstallRetryById(long id, int retry) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_INSTALL_RETRY, Integer.valueOf(retry));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void updateAdById(long id, DuAd ad) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put("gid", ad.getGid());
            values.put("title", ad.getTitle());
            values.put("content", ad.getContent());
            values.put(DuAd.DB_ICON, ad.getIcon());
            values.put(DuAd.DB_DU_URL, ad.getDuUrl());
            values.put("pname", ad.getPname());
            values.put(DuAd.DB_REFERRER, ad.getReferrer());
            values.put(DuAd.DB_DOWN_URL, ad.getDownUrl());
            values.put(DuAd.DB_STATUS, Integer.valueOf(ad.getStatus()));
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(ad.getDownRetry()));
            values.put(DuAd.DB_INSTALL_RETRY, Integer.valueOf(ad.getInstallRetry()));
            values.put(DuAd.DB_MODIFY_TIME, Long.valueOf(System.currentTimeMillis()));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public void updateAdStatusById(long id, int status) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_STATUS, Integer.valueOf(status));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public List<DuAd> queryAdByStatus(int status) {
        Exception e;
        Throwable th;
        List<DuAd> result = null;
        if (checkDB()) {
            result = null;
            Cursor cursor = null;
            try {
                cursor = this.db.query(DuAd.DB_TB, null, "status=? ", new String[]{Integer.toString(status)}, null, null, "_ID desc");
                if (cursor != null && cursor.getCount() > 0) {
                    List<DuAd> result2 = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            DuAd ad = parseCursorToAd(cursor);
                            if (ad == null) {
                                result = result2;
                                break;
                            }
                            result2.add(ad);
                        } catch (Exception e2) {
                            e = e2;
                            result = result2;
                        } catch (Throwable th2) {
                            th = th2;
                            result = result2;
                            closeCursor(cursor);
                            throw th;
                        }
                    }
                    result = result2;
                }
                closeCursor(cursor);
            } catch (Exception e3) {
                e = e3;
                try {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    closeCursor(cursor);
                    return result;
                } catch (Throwable th3) {
                    th = th3;
                    closeCursor(cursor);
                    throw th;
                }
            }
        }
        return result;
    }

    public boolean updateReferrerById(String referrer, int id) {
        if (!checkDB()) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(DuAd.DB_REFERRER, referrer);
        this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString((long) id)});
        return true;
    }
}
