package com.baidu.android.pushservice.b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.m;

public class s {
    private Context a;
    private e b;
    private h c;
    private long d;
    private o e;

    public s(Context context) {
        this.a = context;
        this.b = e.a(context);
        this.e = new o(context);
        this.c = new h(context);
        this.d = m.s(context);
    }

    public static long a(Context context, a aVar) {
        SQLiteDatabase db = PushDatabase.getDb(context);
        return db != null ? PushDatabase.insertApiBehavior(db, aVar) : -1;
    }

    public static long a(Context context, b bVar) {
        SQLiteDatabase db = PushDatabase.getDb(context);
        return db != null ? PushDatabase.insertAppInfo(db, bVar) : -1;
    }

    public static long a(Context context, i iVar) {
        SQLiteDatabase db = PushDatabase.getDb(context);
        return db != null ? PushDatabase.insertPromptBehavior(db, iVar) : -1;
    }

    public static long a(Context context, j jVar) {
        SQLiteDatabase db = PushDatabase.getDb(context);
        return db != null ? PushDatabase.insertPushBehavior(db, jVar) : -1;
    }

    private boolean c() {
        return PushSettings.f() && System.currentTimeMillis() - PushSettings.d(this.a) > 1800000;
    }

    public void a() {
        this.e.b();
    }

    public void b() {
        if (this.c == null) {
            this.c = h.a(this.a);
        }
        if (c()) {
            this.c.g();
        }
    }
}
