package com.duapps.ad.stats;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.duapps.ad.base.f;

public final class DuAdCacheProvider extends ContentProvider {
    private static Uri b;
    private static Uri c;
    private static Uri d;
    private static Uri e;
    private static Uri f;
    private static Uri g;
    private static final Object h = new Object();
    private static final Object j = new Object();
    private static final Object l = new Object();
    private static UriMatcher n;
    private String a = DuAdCacheProvider.class.getSimpleName();
    private o i;
    private p k;
    private m m;
    private Context o;

    public final boolean onCreate() {
        this.o = getContext();
        String str = this.o.getPackageName() + ".DuAdCacheProvider";
        Uri parse = Uri.parse("content://" + str);
        b = parse;
        c = Uri.withAppendedPath(parse, "parse");
        d = Uri.withAppendedPath(b, "click");
        e = Uri.withAppendedPath(b, "cache");
        f = Uri.withAppendedPath(b, "record");
        g = Uri.withAppendedPath(b, "preparse");
        UriMatcher uriMatcher = new UriMatcher(-1);
        uriMatcher.addURI(str, "parse", 1);
        uriMatcher.addURI(str, "click", 2);
        uriMatcher.addURI(str, "cache", 3);
        uriMatcher.addURI(str, "record", 4);
        uriMatcher.addURI(str, "preparse", 5);
        n = uriMatcher;
        return true;
    }

    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor cursor = null;
        int a = a(uri);
        if (a > 0 && a <= 5) {
            synchronized (b(a)) {
                cursor = b(getContext(), a).query(a(a), strArr, str, strArr2, null, null, str2);
            }
        }
        return cursor;
    }

    public final String getType(Uri uri) {
        switch (n.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/parse";
            case 2:
                return "vnd.android.cursor.dir/click";
            case 3:
                return "vnd.android.cursor.dir/cache";
            case 4:
                return "vnd.android.cursor.dir/record";
            case 5:
                return "vnd.android.cursor.dir/preparse";
            default:
                return "vnd.android.cursor.dir/unkown";
        }
    }

    public final Uri insert(Uri uri, ContentValues contentValues) {
        int a = a(uri);
        if (a <= 0 || a > 5) {
            return null;
        }
        synchronized (b(a)) {
            b(getContext(), a).insert(a(a), null, contentValues);
        }
        return uri;
    }

    public final int delete(Uri uri, String str, String[] strArr) {
        f.c(this.a, new StringBuilder("del selcetion  = ").append(str).append(" , selectionArgs = ").append(str).toString() != null ? str.toString() : null);
        int a = a(uri);
        if (a <= 0 || a > 5) {
            return -1;
        }
        synchronized (b(a)) {
            a = b(getContext(), a).delete(a(a), str, strArr);
        }
        return a;
    }

    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int a = a(uri);
        if (a <= 0 || a > 5) {
            return -1;
        }
        synchronized (b(a)) {
            a = b(getContext(), a).update(a(a), contentValues, str, strArr);
        }
        return a;
    }

    private int a(Uri uri) {
        if (uri == null || Uri.EMPTY == uri) {
            return -1;
        }
        int match = n.match(uri);
        f.c(this.a, "match code = " + match);
        return match;
    }

    private SQLiteDatabase b(Context context, int i) {
        switch (i) {
            case 1:
                if (this.k == null) {
                    this.k = new p(context);
                }
                return this.k.getWritableDatabase();
            case 2:
            case 3:
            case 5:
                if (this.i == null) {
                    this.i = new o(context);
                }
                return this.i.getWritableDatabase();
            case 4:
                if (this.m == null) {
                    this.m = new m(context);
                }
                return this.m.getWritableDatabase();
            default:
                return null;
        }
    }

    private static String a(int i) {
        switch (i) {
            case 1:
                return "ad_parse";
            case 2:
                return "tbvc";
            case 3:
                return "cache";
            case 4:
                return "srecord";
            case 5:
                return "appcache";
            default:
                return null;
        }
    }

    private static Object b(int i) {
        switch (i) {
            case 1:
                return j;
            case 2:
            case 3:
            case 5:
                return h;
            case 4:
                return l;
            default:
                return null;
        }
    }

    public static Uri a(Context context, int i) {
        Uri uri = Uri.EMPTY;
        if (b == null) {
            b = Uri.parse("content://" + (context.getPackageName() + ".DuAdCacheProvider"));
        }
        if (c == null) {
            c = Uri.withAppendedPath(b, "parse");
        }
        if (d == null) {
            d = Uri.withAppendedPath(b, "click");
        }
        if (e == null) {
            e = Uri.withAppendedPath(b, "cache");
        }
        if (f == null) {
            f = Uri.withAppendedPath(b, "record");
        }
        if (g == null) {
            g = Uri.withAppendedPath(b, "preparse");
        }
        switch (i) {
            case 1:
                return c;
            case 2:
                return d;
            case 3:
                return e;
            case 4:
                return f;
            case 5:
                return g;
            default:
                return uri;
        }
    }
}
