package com.duapps.ad.stats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.umeng.analytics.a;

/* compiled from: ToolboxCacheManager */
public final class n {
    private static n b;
    private Context a;

    public static synchronized n a(Context context) {
        n nVar;
        synchronized (n.class) {
            if (b == null) {
                b = new n(context.getApplicationContext());
            }
            nVar = b;
        }
        return nVar;
    }

    private n(Context context) {
        this.a = context;
    }

    public final void a(d dVar) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_url", dVar.a);
        contentValues.put("pkg", dVar.b);
        contentValues.put("p_url", dVar.d);
        contentValues.put("type", Integer.valueOf(dVar.c));
        contentValues.put("ts", Long.valueOf(dVar.e));
        try {
            if (this.a.getContentResolver().update(DuAdCacheProvider.a(this.a, 1), contentValues, "_url = ?", new String[]{dVar.a}) <= 0) {
                this.a.getContentResolver().insert(DuAdCacheProvider.a(this.a, 1), contentValues);
            }
            try {
                this.a.getContentResolver().delete(DuAdCacheProvider.a(this.a, 1), "ts<?", new String[]{String.valueOf(System.currentTimeMillis() - a.m)});
            } catch (Exception e) {
                f.a("ToolboxCacheMgr", "dumpTimeOutDatas() exception: ", e);
            } catch (Throwable th) {
                f.a("ToolboxCacheMgr", "dumpTimeOutDatas() exception: ", th);
            }
        } catch (Exception e2) {
            f.a("ToolboxCacheMgr", "saveParseResult() exception: ", e2);
        } catch (Throwable th2) {
            f.a("ToolboxCacheMgr", "saveParseResult() exception: ", th2);
        }
    }

    public final d a(String str) {
        Throwable e;
        d dVar = new d();
        dVar.a = str;
        dVar.c = 0;
        Cursor query;
        try {
            query = this.a.getContentResolver().query(DuAdCacheProvider.a(this.a, 1), new String[]{"_url", "pkg", "p_url", "type"}, "_url=? AND ts >?", new String[]{str, String.valueOf(System.currentTimeMillis() - a.m)}, "ts DESC");
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        dVar.a = query.getString(0);
                        dVar.b = query.getString(1);
                        dVar.d = query.getString(2);
                        dVar.c = query.getInt(3);
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        f.a("ToolboxCacheMgr", "getParseResult() Exception: ", e);
                        if (!(query == null || query.isClosed())) {
                            query.close();
                        }
                        return dVar;
                    } catch (Throwable th) {
                        e = th;
                        query.close();
                        throw e;
                    }
                }
            }
            if (!(query == null || query.isClosed())) {
                query.close();
            }
        } catch (Exception e3) {
            e = e3;
            query = null;
        } catch (Throwable th2) {
            e = th2;
            query = null;
            if (!(query == null || query.isClosed())) {
                query.close();
            }
            throw e;
        }
        return dVar;
    }

    public final int b(String str) {
        Throwable e;
        Cursor query;
        try {
            int i;
            query = this.a.getContentResolver().query(DuAdCacheProvider.a(this.a, 1), new String[]{"type"}, "_url=? AND ts >?", new String[]{str, String.valueOf(System.currentTimeMillis() - a.m)}, "ts DESC");
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        i = query.getInt(0);
                        if (query == null && !query.isClosed()) {
                            query.close();
                            return i;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        f.a("ToolboxCacheMgr", "getParseResultType() exception: ", e);
                        if (query == null || query.isClosed()) {
                            return 0;
                        }
                        query.close();
                        return 0;
                    } catch (Throwable th) {
                        e = th;
                        query.close();
                        throw e;
                    }
                }
            }
            i = 0;
            return query == null ? i : i;
        } catch (Exception e3) {
            e = e3;
            query = null;
        } catch (Throwable th2) {
            e = th2;
            query = null;
            if (!(query == null || query.isClosed())) {
                query.close();
            }
            throw e;
        }
    }
}
