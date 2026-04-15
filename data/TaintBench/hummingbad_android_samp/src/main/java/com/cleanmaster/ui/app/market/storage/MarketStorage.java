package com.cleanmaster.ui.app.market.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.ui.app.market.data.PackageStatInfo;
import com.cleanmaster.ui.app.market.data.a;
import com.picksinit.c;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarketStorage {
    private static boolean a = false;
    private static MarketStorage b = new MarketStorage();
    private DatabaseHelper c = new DatabaseHelper(c.getInstance().getContext());

    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, "market.db", null, 45);
        }

        public void onCreate(SQLiteDatabase db) {
            PackageStatInfo.createTable(db, "tbl_apps_update");
            Ad.createTable(db, "tbl_41");
            a.a(db, "tbl_market_globel_property");
            Ad.createTable(db, "tbl_downloading_apps");
        }

        public void a(SQLiteDatabase sQLiteDatabase) {
            for (String str : b(sQLiteDatabase)) {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + str);
            }
        }

        public static List b(SQLiteDatabase sQLiteDatabase) {
            List arrayList = new ArrayList();
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
            try {
                rawQuery.moveToFirst();
                while (!rawQuery.isAfterLast()) {
                    String string = rawQuery.getString(1);
                    if (!(string.equals("android_metadata") || string.equals("sqlite_sequence"))) {
                        arrayList.add(string);
                    }
                    rawQuery.moveToNext();
                }
                return arrayList;
            } finally {
                if (rawQuery != null) {
                    rawQuery.close();
                }
            }
        }

        public void a(SQLiteDatabase sQLiteDatabase, String str) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + str);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
            a(db);
            onCreate(db);
        }

        public void b(SQLiteDatabase sQLiteDatabase, String str) {
            a(sQLiteDatabase, "tbl_" + str);
        }
    }

    private MarketStorage() {
    }

    public static MarketStorage a() {
        return b;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0003} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:6:0x001a, code skipped:
            if (r2.getCount() > 0) goto L_0x001c;
     */
    /* JADX WARNING: Missing block: B:11:0x0025, code skipped:
            r0 = r2;
     */
    /* JADX WARNING: Missing block: B:16:0x002e, code skipped:
            if (r2 != null) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:17:0x0030, code skipped:
            r2.close();
     */
    public static boolean a(android.database.sqlite.SQLiteDatabase r6, java.lang.String r7) {
        /*
        r2 = 0;
        r0 = 1;
        r1 = 0;
        r3 = "SELECT * FROM sqlite_master WHERE type='table' AND name='%s';";
        r4 = 1;
        r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0024, all -> 0x002d }
        r5 = 0;
        r4[r5] = r7;	 Catch:{ Exception -> 0x0024, all -> 0x002d }
        r3 = java.lang.String.format(r3, r4);	 Catch:{ Exception -> 0x0024, all -> 0x002d }
        r4 = 0;
        r2 = r6.rawQuery(r3, r4);	 Catch:{ Exception -> 0x0024, all -> 0x002d }
        if (r2 == 0) goto L_0x0022;
    L_0x0016:
        r3 = r2.getCount();	 Catch:{ Exception -> 0x0034, all -> 0x002d }
        if (r3 <= 0) goto L_0x0022;
    L_0x001c:
        if (r2 == 0) goto L_0x0021;
    L_0x001e:
        r2.close();
    L_0x0021:
        return r0;
    L_0x0022:
        r0 = r1;
        goto L_0x001c;
    L_0x0024:
        r0 = move-exception;
        r0 = r2;
    L_0x0026:
        if (r0 == 0) goto L_0x002b;
    L_0x0028:
        r0.close();
    L_0x002b:
        r0 = r1;
        goto L_0x0021;
    L_0x002d:
        r0 = move-exception;
        if (r2 == 0) goto L_0x0033;
    L_0x0030:
        r2.close();
    L_0x0033:
        throw r0;
    L_0x0034:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.storage.MarketStorage.a(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    public synchronized long a(String str, MarketResponse marketResponse) {
        long j = -1;
        synchronized (this) {
            SQLiteDatabase c = c();
            if (c != null) {
                try {
                    marketResponse.getHeader().h = str;
                    ContentValues a = marketResponse.getHeader().a(str);
                    j = (long) c.update("tbl_market_globel_property", a, "pos_id = ?", new String[]{str});
                    if (j == 0) {
                        j = c.insert("tbl_market_globel_property", null, a);
                    }
                } catch (Exception e) {
                    a(e);
                }
            }
        }
        return j;
    }

    public synchronized void a(String str) {
        SQLiteDatabase c = c();
        if (c != null) {
            try {
                c.delete("tbl_market_globel_property", "pos_id = ?", new String[]{str});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public synchronized int a(String str, List list) {
        int i;
        try {
            SQLiteDatabase c = c();
            if (c == null) {
                i = -1;
            } else {
                String str2 = "tbl_" + str;
                i = 0;
                try {
                    c.beginTransaction();
                    Ad.createTable(c, str2);
                    for (Ad toContentValues : list) {
                        int i2;
                        if (c.insert(str2, "", toContentValues.toContentValues(str)) > 0) {
                            i2 = i + 1;
                        } else {
                            i2 = i;
                        }
                        i = i2;
                    }
                    c.setTransactionSuccessful();
                    c.endTransaction();
                } catch (Exception e) {
                    a(e);
                    try {
                        c.endTransaction();
                    } catch (Exception e2) {
                        a(e2);
                    }
                } catch (Throwable th) {
                    try {
                        c.endTransaction();
                    } catch (Exception e3) {
                        a(e3);
                    }
                    throw th;
                }
            }
        } catch (Exception e22) {
            a(e22);
        } catch (Throwable th2) {
            throw th2;
        }
        return i;
    }

    public synchronized void a(String str, Ad ad) {
        SQLiteDatabase c = c();
        if (c == null) {
            c.endTransaction();
        } else {
            try {
                String str2 = "tbl_" + str;
                ad.setShowed(true);
                c.beginTransaction();
                c.update(str2, ad.toContentValues(str), "pkg = ?", new String[]{ad.getPkg()});
                c.setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                c.endTransaction();
            }
        }
    }

    public synchronized void b(String str) {
        String str2 = "tbl_" + str;
        SQLiteDatabase c = c();
        if (c != null) {
            this.c.a(c, str2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0060 A:{Catch:{ Exception -> 0x0051, all -> 0x005c }} */
    public synchronized int c(java.lang.String r11) {
        /*
        r10 = this;
        r8 = 0;
        r9 = 0;
        monitor-enter(r10);
        r0 = r10.c();	 Catch:{ all -> 0x0047 }
        if (r0 != 0) goto L_0x000c;
    L_0x0009:
        r0 = r8;
    L_0x000a:
        monitor-exit(r10);
        return r0;
    L_0x000c:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r2 = "tbl_";
        r1.<init>(r2);	 Catch:{ all -> 0x0047 }
        r1 = r1.append(r11);	 Catch:{ all -> 0x0047 }
        r1 = r1.toString();	 Catch:{ all -> 0x0047 }
        r2 = a(r0, r1);	 Catch:{ all -> 0x0047 }
        if (r2 != 0) goto L_0x0023;
    L_0x0021:
        r0 = r8;
        goto L_0x000a;
    L_0x0023:
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ all -> 0x0047 }
        r3 = 0;
        r4 = "_id";
        r2[r3] = r4;	 Catch:{ all -> 0x0047 }
        r3 = "_id";
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0051, all -> 0x005c }
        if (r1 == 0) goto L_0x004a;
    L_0x0037:
        r0 = r1.getCount();	 Catch:{ Exception -> 0x0066 }
        if (r0 <= 0) goto L_0x004a;
    L_0x003d:
        r0 = r1.getCount();	 Catch:{ Exception -> 0x0066 }
        if (r1 == 0) goto L_0x000a;
    L_0x0043:
        r1.close();	 Catch:{ all -> 0x0047 }
        goto L_0x000a;
    L_0x0047:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x004a:
        if (r1 == 0) goto L_0x004f;
    L_0x004c:
        r1.close();	 Catch:{ all -> 0x0047 }
    L_0x004f:
        r0 = r8;
        goto L_0x000a;
    L_0x0051:
        r0 = move-exception;
        r1 = r9;
    L_0x0053:
        r10.a(r0);	 Catch:{ all -> 0x0064 }
        if (r1 == 0) goto L_0x004f;
    L_0x0058:
        r1.close();	 Catch:{ all -> 0x0047 }
        goto L_0x004f;
    L_0x005c:
        r0 = move-exception;
        r1 = r9;
    L_0x005e:
        if (r1 == 0) goto L_0x0063;
    L_0x0060:
        r1.close();	 Catch:{ all -> 0x0047 }
    L_0x0063:
        throw r0;	 Catch:{ all -> 0x0047 }
    L_0x0064:
        r0 = move-exception;
        goto L_0x005e;
    L_0x0066:
        r0 = move-exception;
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.storage.MarketStorage.c(java.lang.String):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070 A:{SYNTHETIC, Splitter:B:42:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0059  */
    public synchronized java.util.List a(java.lang.String r11, java.lang.String[] r12, java.lang.String r13, java.lang.String[] r14) {
        /*
        r10 = this;
        r9 = 0;
        monitor-enter(r10);
        r8 = new java.util.ArrayList;	 Catch:{ all -> 0x0069 }
        r8.<init>();	 Catch:{ all -> 0x0069 }
        r0 = r10.c();	 Catch:{ all -> 0x0069 }
        if (r0 != 0) goto L_0x0010;
    L_0x000d:
        r0 = r8;
    L_0x000e:
        monitor-exit(r10);
        return r0;
    L_0x0010:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0069 }
        r2 = "tbl_";
        r1.<init>(r2);	 Catch:{ all -> 0x0069 }
        r1 = r1.append(r11);	 Catch:{ all -> 0x0069 }
        r1 = r1.toString();	 Catch:{ all -> 0x0069 }
        r2 = a(r0, r1);	 Catch:{ all -> 0x0069 }
        if (r2 != 0) goto L_0x0027;
    L_0x0025:
        r0 = r8;
        goto L_0x000e;
    L_0x0027:
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r2 = r12;
        r3 = r13;
        r4 = r14;
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x005e, all -> 0x006c }
        if (r1 == 0) goto L_0x004e;
    L_0x0033:
        r0 = r1.getCount();	 Catch:{ Exception -> 0x0076 }
        if (r0 <= 0) goto L_0x004e;
    L_0x0039:
        r1.moveToFirst();	 Catch:{ Exception -> 0x0076 }
    L_0x003c:
        r0 = new com.cleanmaster.ui.app.market.Ad;	 Catch:{ Exception -> 0x0076 }
        r0.m3153init();	 Catch:{ Exception -> 0x0076 }
        r0 = r0.fromCursor(r1);	 Catch:{ Exception -> 0x0076 }
        r8.add(r0);	 Catch:{ Exception -> 0x0076 }
        r0 = r1.moveToNext();	 Catch:{ Exception -> 0x0076 }
        if (r0 != 0) goto L_0x003c;
    L_0x004e:
        if (r1 == 0) goto L_0x0053;
    L_0x0050:
        r1.close();	 Catch:{ all -> 0x0069 }
    L_0x0053:
        r0 = com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder.isAppRequest(r11);	 Catch:{ all -> 0x0069 }
        if (r0 == 0) goto L_0x005c;
    L_0x0059:
        com.cleanmaster.ui.app.market.c.a(r8);	 Catch:{ all -> 0x0069 }
    L_0x005c:
        r0 = r8;
        goto L_0x000e;
    L_0x005e:
        r0 = move-exception;
        r1 = r9;
    L_0x0060:
        r10.a(r0);	 Catch:{ all -> 0x0074 }
        if (r1 == 0) goto L_0x0053;
    L_0x0065:
        r1.close();	 Catch:{ all -> 0x0069 }
        goto L_0x0053;
    L_0x0069:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x006c:
        r0 = move-exception;
        r1 = r9;
    L_0x006e:
        if (r1 == 0) goto L_0x0073;
    L_0x0070:
        r1.close();	 Catch:{ all -> 0x0069 }
    L_0x0073:
        throw r0;	 Catch:{ all -> 0x0069 }
    L_0x0074:
        r0 = move-exception;
        goto L_0x006e;
    L_0x0076:
        r0 = move-exception;
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.storage.MarketStorage.a(java.lang.String, java.lang.String[], java.lang.String, java.lang.String[]):java.util.List");
    }

    private SQLiteDatabase c() {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            return this.c.getWritableDatabase();
        } catch (Exception e) {
            a(e);
            return sQLiteDatabase;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0052 A:{SYNTHETIC, Splitter:B:34:0x0052} */
    public synchronized java.util.ArrayList b() {
        /*
        r10 = this;
        r9 = 0;
        monitor-enter(r10);
        r8 = new java.util.ArrayList;	 Catch:{ all -> 0x004b }
        r8.<init>();	 Catch:{ all -> 0x004b }
        r0 = r10.c();	 Catch:{ all -> 0x004b }
        if (r0 != 0) goto L_0x0010;
    L_0x000d:
        r0 = r8;
    L_0x000e:
        monitor-exit(r10);
        return r0;
    L_0x0010:
        r1 = "tbl_market_globel_property";
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0040, all -> 0x004e }
        if (r1 == 0) goto L_0x0039;
    L_0x001e:
        r0 = r1.getCount();	 Catch:{ Exception -> 0x0058 }
        if (r0 <= 0) goto L_0x0039;
    L_0x0024:
        r1.moveToFirst();	 Catch:{ Exception -> 0x0058 }
    L_0x0027:
        r0 = new com.cleanmaster.ui.app.market.data.a;	 Catch:{ Exception -> 0x0058 }
        r0.m503init();	 Catch:{ Exception -> 0x0058 }
        r0 = r0.a(r1);	 Catch:{ Exception -> 0x0058 }
        r8.add(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r1.moveToNext();	 Catch:{ Exception -> 0x0058 }
        if (r0 != 0) goto L_0x0027;
    L_0x0039:
        if (r1 == 0) goto L_0x003e;
    L_0x003b:
        r1.close();	 Catch:{ all -> 0x004b }
    L_0x003e:
        r0 = r8;
        goto L_0x000e;
    L_0x0040:
        r0 = move-exception;
        r1 = r9;
    L_0x0042:
        r10.a(r0);	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x003e;
    L_0x0047:
        r1.close();	 Catch:{ all -> 0x004b }
        goto L_0x003e;
    L_0x004b:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x004e:
        r0 = move-exception;
        r1 = r9;
    L_0x0050:
        if (r1 == 0) goto L_0x0055;
    L_0x0052:
        r1.close();	 Catch:{ all -> 0x004b }
    L_0x0055:
        throw r0;	 Catch:{ all -> 0x004b }
    L_0x0056:
        r0 = move-exception;
        goto L_0x0050;
    L_0x0058:
        r0 = move-exception;
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.storage.MarketStorage.b():java.util.ArrayList");
    }

    public synchronized boolean a(long j) {
        boolean z = false;
        synchronized (this) {
            ArrayList b = b();
            SQLiteDatabase c = c();
            if (c != null) {
                client.core.model.a aVar = new client.core.model.a();
                long currentTimeMillis = System.currentTimeMillis();
                Iterator it = b.iterator();
                while (it.hasNext()) {
                    a aVar2 = (a) it.next();
                    if (currentTimeMillis - aVar2.g > j) {
                        aVar.a().a("removeRecentCache");
                        this.c.b(c, aVar2.h);
                        aVar.a(String.format("START DROP '%s' (EXPIRED=%d)", new Object[]{aVar2.h, Long.valueOf(currentTimeMillis - aVar2.g)}));
                        a(aVar2.h);
                        aVar.a("deleteMarketResponseHeader(" + aVar2.h + ")");
                    }
                }
                z = true;
            }
        }
        return z;
    }

    public void a(Exception exception) {
        if (a) {
            throw new RuntimeException(exception);
        }
        exception.printStackTrace();
    }
}
