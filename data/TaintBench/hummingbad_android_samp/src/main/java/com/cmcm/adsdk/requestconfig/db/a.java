package com.cmcm.adsdk.requestconfig.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.cmcm.adsdk.db.b;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import java.util.List;

/* compiled from: PosInfoStorage */
public final class a {
    public static a a;
    private Context b;
    private boolean c = false;

    private a(Context context) {
        this.b = context;
    }

    public static a a(Context context) {
        if (a == null) {
            a = new a(context);
        }
        return a;
    }

    private SQLiteDatabase b() {
        try {
            return b.a(this.b).a().getWritableDatabase();
        } catch (Exception e) {
            return null;
        }
    }

    public final boolean a(List<PosBean> list) {
        SQLiteDatabase b = b();
        if (b == null || !com.cmcm.adsdk.requestconfig.util.a.a(list)) {
            return false;
        }
        boolean z;
        if (this.c) {
            b.beginTransaction();
        }
        try {
            for (PosBean posBean : list) {
                if (!(posBean == null || b == null || a(b, posBean))) {
                    b(b, posBean);
                }
            }
            if (this.c) {
                b.setTransactionSuccessful();
            }
            z = true;
            if (this.c) {
                b.endTransaction();
            }
        } catch (Exception e) {
            com.cmcm.adsdk.requestconfig.log.a.d("PosInfoStorage", "operate db is error..." + e.getMessage());
            if (this.c) {
                b.endTransaction();
                z = false;
            } else {
                z = false;
            }
        } catch (Throwable th) {
            if (this.c) {
                b.endTransaction();
            }
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005b A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x000e} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:20:0x005b, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:21:0x005c, code skipped:
            r2 = null;
     */
    /* JADX WARNING: Missing block: B:30:0x006e, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:31:0x006f, code skipped:
            r1 = r0;
            r2 = null;
            r0 = r8;
     */
    public final java.util.List<com.cmcm.adsdk.requestconfig.data.PosBean> a() {
        /*
        r11 = this;
        r9 = 0;
        r0 = r11.b();
        r1 = new java.util.ArrayList;
        r1.<init>();
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        r0 = r1;
    L_0x000d:
        return r0;
    L_0x000e:
        r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0068, all -> 0x005b }
        r8.<init>();	 Catch:{ Exception -> 0x0068, all -> 0x005b }
        r1 = "posinfo";
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x006e, all -> 0x005b }
    L_0x001f:
        r0 = r1.moveToNext();	 Catch:{ Exception -> 0x0032, all -> 0x0063 }
        if (r0 == 0) goto L_0x0054;
    L_0x0025:
        r0 = new com.cmcm.adsdk.requestconfig.data.PosBean;	 Catch:{ Exception -> 0x0032, all -> 0x0063 }
        r0.m3199init();	 Catch:{ Exception -> 0x0032, all -> 0x0063 }
        r0 = com.cmcm.adsdk.requestconfig.data.PosBean.a(r1);	 Catch:{ Exception -> 0x0032, all -> 0x0063 }
        r8.add(r0);	 Catch:{ Exception -> 0x0032, all -> 0x0063 }
        goto L_0x001f;
    L_0x0032:
        r0 = move-exception;
        r2 = r1;
        r1 = r0;
        r0 = r8;
    L_0x0036:
        r3 = "PosInfoStorage";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0066 }
        r5 = "operate db is error...";
        r4.<init>(r5);	 Catch:{ all -> 0x0066 }
        r1 = r1.getMessage();	 Catch:{ all -> 0x0066 }
        r1 = r4.append(r1);	 Catch:{ all -> 0x0066 }
        r1 = r1.toString();	 Catch:{ all -> 0x0066 }
        com.cmcm.adsdk.requestconfig.log.a.d(r3, r1);	 Catch:{ all -> 0x0066 }
        if (r2 == 0) goto L_0x000d;
    L_0x0050:
        r2.close();
        goto L_0x000d;
    L_0x0054:
        if (r1 == 0) goto L_0x0073;
    L_0x0056:
        r1.close();
        r0 = r8;
        goto L_0x000d;
    L_0x005b:
        r0 = move-exception;
        r2 = r9;
    L_0x005d:
        if (r2 == 0) goto L_0x0062;
    L_0x005f:
        r2.close();
    L_0x0062:
        throw r0;
    L_0x0063:
        r0 = move-exception;
        r2 = r1;
        goto L_0x005d;
    L_0x0066:
        r0 = move-exception;
        goto L_0x005d;
    L_0x0068:
        r0 = move-exception;
        r2 = r9;
        r10 = r1;
        r1 = r0;
        r0 = r10;
        goto L_0x0036;
    L_0x006e:
        r0 = move-exception;
        r1 = r0;
        r2 = r9;
        r0 = r8;
        goto L_0x0036;
    L_0x0073:
        r0 = r8;
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.requestconfig.db.a.a():java.util.List");
    }

    public final boolean b(List<Integer> list) {
        SQLiteDatabase b = b();
        if (b == null) {
            return false;
        }
        if (!com.cmcm.adsdk.requestconfig.util.a.a(list)) {
            return false;
        }
        try {
            for (Integer num : list) {
                b.delete(PosBean.TABLE_NAME, "placeid!=?", new String[]{num.intValue()});
            }
            return true;
        } catch (Exception e) {
            com.cmcm.adsdk.requestconfig.log.a.d("PosInfoStorage", "operate db is error..." + e.getMessage());
            return false;
        }
    }

    private static boolean a(SQLiteDatabase sQLiteDatabase, PosBean posBean) {
        if (posBean == null || sQLiteDatabase == null) {
            return false;
        }
        try {
            if (sQLiteDatabase.update(PosBean.TABLE_NAME, posBean.toContentValues(), "placeid=? and name=?", new String[]{posBean.placeid, posBean.name}) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            com.cmcm.adsdk.requestconfig.log.a.d("PosInfoStorage", "operate db is error..." + e.getMessage());
            return false;
        }
    }

    private static boolean b(SQLiteDatabase sQLiteDatabase, PosBean posBean) {
        if (posBean == null || sQLiteDatabase == null) {
            return false;
        }
        try {
            com.cmcm.adsdk.requestconfig.log.a.a("PosInfoStorage", "data:" + posBean);
            if (sQLiteDatabase.insert(PosBean.TABLE_NAME, null, posBean.toContentValues()) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            com.cmcm.adsdk.requestconfig.log.a.d("PosInfoStorage", "operate db is error..." + e.getMessage());
            return false;
        }
    }
}
