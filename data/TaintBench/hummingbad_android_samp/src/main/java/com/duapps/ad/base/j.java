package com.duapps.ad.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.duapps.ad.AdError;
import com.duapps.ad.entity.c;
import com.duapps.ad.stats.DuAdCacheProvider;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONObject;

/* compiled from: ToolCacheManager */
public final class j {
    private static String a = "http://api.mobula.sdk.duapps.com/adunion/slot/getDlAd?";
    /* access modifiers changed from: private|static */
    public static String b = "http://api.mobula.sdk.duapps.com/adunion/rtb/getInmobiAd?";
    private static String c = "http://api.mobula.sdk.duapps.com/adunion/rtb/fetchAd?";
    private static j e;
    /* access modifiers changed from: private */
    public Context d;

    /* compiled from: ToolCacheManager */
    static class a {
        public String a;
        public String b;
        public long c;

        a() {
        }
    }

    public static synchronized j a(Context context) {
        j jVar;
        synchronized (j.class) {
            if (e == null) {
                e = new j(context.getApplicationContext());
            }
            jVar = e;
        }
        return jVar;
    }

    private j(Context context) {
        this.d = context;
        try {
            this.d.getContentResolver().delete(DuAdCacheProvider.a(this.d, 3), "ts<?", new String[]{String.valueOf(System.currentTimeMillis() - 7200000)});
        } catch (Exception e) {
            f.a("ToolboxCacheManager", "mDatabase initCacheDatabase() del exception: ", e);
        }
    }

    private void a(int i, String str, int i2, String str2, String str3, x<c> xVar, String str4, int i3) {
        xVar.a();
        Context context = this.d;
        String a = t.a();
        String str5 = str3 + a + "_" + str + "_" + i + "_" + i2;
        if (d.b(this.d)) {
            y.a().a(new k(this, a, i3, i2, i, str, str4, str2, str5, xVar));
            return;
        }
        xVar.a(AdError.NETWORK_ERROR.getErrorMessage());
    }

    public final void a(int i, int i2, x<c> xVar) {
        a(i, MASTNativeAdConstants.RESPONSE_NATIVE_STRING, 1, a, "native_", xVar, "normal", 20);
    }

    public final void a(int i, int i2, x<c> xVar, int i3) {
        a(i, MASTNativeAdConstants.RESPONSE_NATIVE_STRING, 1, a, "native_", xVar, "normal", 10);
    }

    public final void b(int i, int i2, x<c> xVar) {
        a(i, MASTNativeAdConstants.RESPONSE_NATIVE_STRING, 1, a, "native_", xVar, "high", 20);
    }

    public final void b(int i, int i2, x<c> xVar, int i3) {
        String str = "online";
        String str2 = c;
        xVar.a();
        Context context = this.d;
        String a = t.a();
        DisplayMetrics displayMetrics = this.d.getResources().getDisplayMetrics();
        String str3 = "online_" + a + "_" + str + "_" + i + "_" + 1;
        if (d.b(this.d)) {
            y.a().a(new m(this, a, displayMetrics, 1, i, str, i3, str2, str3, xVar));
        } else {
            xVar.a(AdError.NETWORK_ERROR.getErrorMessage());
        }
    }

    public final void a(int i, String str, String str2, x<com.duapps.ad.b.c> xVar) {
        xVar.a();
        if (i.a(b + i, this.d)) {
            Context context = this.d;
            String a = t.a();
            if (d.b(this.d)) {
                y.a().a(new o(this, a, i, str, str2, xVar));
                return;
            } else {
                xVar.a(AdError.NETWORK_ERROR.getErrorMessage());
                return;
            }
        }
        xVar.a("This url is request too frequently.");
        f.c("ToolboxCacheManager", "This url is request too frequently.");
    }

    /* access modifiers changed from: final */
    public final a a(String str) {
        Throwable e;
        String[] strArr = new String[]{"data", "ts"};
        String str2 = "key=?";
        String[] strArr2 = new String[]{str};
        a aVar = new a();
        aVar.a = str;
        Cursor query;
        try {
            query = this.d.getContentResolver().query(DuAdCacheProvider.a(this.d, 3), strArr, str2, strArr2, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        aVar.b = query.getString(0);
                        aVar.c = query.getLong(1);
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        f.a("ToolboxCacheManager", "getCacheEntry() exception: ", e);
                        if (!(query == null || query.isClosed())) {
                            query.close();
                        }
                        return aVar;
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
        return aVar;
    }

    /* access modifiers changed from: final */
    public final void a(a aVar) {
        ContentValues contentValues = new ContentValues(4);
        contentValues.put(MASTNativeAdConstants.REQUESTPARAM_KEY, aVar.a);
        contentValues.put("data", aVar.b);
        contentValues.put("ts", Long.valueOf(aVar.c));
        try {
            if (this.d.getContentResolver().update(DuAdCacheProvider.a(this.d, 3), contentValues, "key=?", new String[]{aVar.a}) <= 0) {
                this.d.getContentResolver().insert(DuAdCacheProvider.a(this.d, 3), contentValues);
            }
        } catch (Exception e) {
            f.a("ToolboxCacheManager", "cacheDabase saveCacheEntry() exception: ", e);
        } catch (Throwable th) {
            f.a("ToolboxCacheManager", "cacheDabase saveCacheEntry() exception: ", th);
        }
    }

    public final void a(com.duapps.ad.stats.j jVar) {
        if (jVar != null && !TextUtils.isEmpty(jVar.a())) {
            try {
                ContentValues contentValues = new ContentValues(5);
                contentValues.put("pkgName", jVar.a());
                contentValues.put("ctime", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("cdata", com.duapps.ad.stats.j.a(jVar).toString());
                String[] strArr = new String[]{jVar.a()};
                if (this.d.getContentResolver().update(DuAdCacheProvider.a(this.d, 2), contentValues, "pkgName=?", strArr) == 0) {
                    contentValues.put(DuAd.DB_STATUS, Integer.valueOf(0));
                    this.d.getContentResolver().insert(DuAdCacheProvider.a(this.d, 2), contentValues);
                }
            } catch (Exception e) {
                f.a("ToolboxCacheManager", "updateOrInsertValidClickTime() exception: ", e);
            } catch (Throwable th) {
                f.a("ToolboxCacheManager", "updateOrInsertValidClickTime() exception: ", th);
            }
        }
    }

    public final com.duapps.ad.stats.j b(String str) {
        Throwable e;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        com.duapps.ad.stats.j a;
        long currentTimeMillis = System.currentTimeMillis() - com.umeng.analytics.a.m;
        Cursor query;
        try {
            query = this.d.getContentResolver().query(DuAdCacheProvider.a(this.d, 2), new String[]{"cdata", "ctime"}, "pkgName=? AND ctime>= ?", new String[]{str, String.valueOf(currentTimeMillis)}, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        a = com.duapps.ad.stats.j.a(new JSONObject(query.getString(0)));
                        if (!(query == null || query.isClosed())) {
                            query.close();
                        }
                        return a;
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        f.a("ToolboxCacheManager", "getValidClickTimeRecord exception: ", e);
                        if (query == null || query.isClosed()) {
                            a = null;
                        } else {
                            query.close();
                            a = null;
                        }
                        return a;
                    } catch (Throwable th) {
                        e = th;
                        query.close();
                        throw e;
                    }
                }
            }
            a = null;
            query.close();
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
        return a;
    }

    public final void a(c cVar) {
        y.a().a(new q(this, cVar));
    }

    public final void c(String str) {
        try {
            this.d.getContentResolver().delete(DuAdCacheProvider.a(this.d, 2), "pkgName=?", new String[]{str});
        } catch (Exception e) {
            f.a("ToolboxCacheManager", "clearValidClickTimeRecord exception: ", e);
        } catch (Throwable th) {
            f.a("ToolboxCacheManager", "clearValidClickTimeRecord exception: ", th);
        }
    }

    public final void b(com.duapps.ad.stats.j jVar) {
        if (jVar != null && !TextUtils.isEmpty(jVar.a())) {
            try {
                ContentValues contentValues = new ContentValues(5);
                contentValues.put("ad_id", Long.valueOf(jVar.c.a));
                contentValues.put("pkgName", jVar.a());
                contentValues.put("data", com.duapps.ad.stats.j.a(jVar).toString());
                contentValues.put("ctime", Long.valueOf(System.currentTimeMillis()));
                String[] strArr = new String[]{jVar.a()};
                if (this.d.getContentResolver().update(DuAdCacheProvider.a(this.d, 5), contentValues, "pkgName=?", strArr) == 0) {
                    this.d.getContentResolver().insert(DuAdCacheProvider.a(this.d, 5), contentValues);
                    f.c("ToolboxCacheManager", "update or insert triggerPreParse data success");
                }
            } catch (Exception e) {
                f.a("ToolboxCacheManager", "update or insert triggerPreParse data error: ", e);
            } catch (Throwable th) {
                f.a("ToolboxCacheManager", "update or insert triggerPreParse data error: ", th);
            }
        }
    }

    public final com.duapps.ad.stats.j d(String str) {
        Cursor cursor;
        Throwable th;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        com.duapps.ad.stats.j a;
        Cursor query;
        try {
            query = this.d.getContentResolver().query(DuAdCacheProvider.a(this.d, 5), new String[]{"pkgName", "data"}, "pkgName=? ", new String[]{str}, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        a = com.duapps.ad.stats.j.a(new JSONObject(query.getString(1)));
                        if (!(query == null || query.isClosed())) {
                            query.close();
                        }
                        return a;
                    }
                } catch (Exception e) {
                    cursor = query;
                    try {
                        if (f.a()) {
                            f.c("ToolboxCacheManager", "fetch triggerPreParse data error");
                        }
                        if (cursor == null || cursor.isClosed()) {
                            a = null;
                        } else {
                            cursor.close();
                            a = null;
                        }
                        return a;
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        query = cursor;
                        th = th3;
                        query.close();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    if (!(query == null || query.isClosed())) {
                        query.close();
                    }
                    throw th;
                }
            }
            a = null;
            query.close();
        } catch (Exception e2) {
            cursor = null;
        } catch (Throwable th5) {
            th = th5;
            query = null;
            query.close();
            throw th;
        }
        return a;
    }

    public final void a() {
        try {
            String[] strArr = new String[]{Long.toString(System.currentTimeMillis() - com.umeng.analytics.a.m)};
            this.d.getContentResolver().delete(DuAdCacheProvider.a(this.d, 5), "ctime<?", strArr);
        } catch (Exception e) {
            f.a("ToolboxCacheManager", "clearTriggerPreParseData error: ", e);
        } catch (Throwable th) {
            f.a("ToolboxCacheManager", "clearTriggerPreParseData error: ", th);
        }
    }
}
