package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhb;
import java.util.Locale;

@zzhb
public class zzh {
    /* access modifiers changed from: private|static|final */
    public static final String zzFV = String.format(Locale.US, "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER)", new Object[]{"InAppPurchase", "purchase_id", "product_id", "developer_payload", "record_time"});
    private static zzh zzFX;
    private static final Object zzpV = new Object();
    private final zza zzFW;

    public class zza extends SQLiteOpenHelper {
        public zza(Context context, String str) {
            super(context, str, null, 4);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(zzh.zzFV);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            zzb.zzaJ("Database updated from version " + oldVersion + " to version " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS InAppPurchase");
            onCreate(db);
        }
    }

    zzh(Context context) {
        this.zzFW = new zza(context, "google_inapp_purchase.db");
    }

    public static zzh zzy(Context context) {
        zzh zzh;
        synchronized (zzpV) {
            if (zzFX == null) {
                zzFX = new zzh(context);
            }
            zzh = zzFX;
        }
        return zzh;
    }

    public int getRecordCount() {
        Cursor cursor = null;
        int i = 0;
        synchronized (zzpV) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase == null) {
            } else {
                try {
                    cursor = writableDatabase.rawQuery("select count(*) from InAppPurchase", null);
                    if (cursor.moveToFirst()) {
                        i = cursor.getInt(0);
                        if (cursor != null) {
                            cursor.close();
                        }
                    } else {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } catch (SQLiteException e) {
                    zzb.zzaK("Error getting record count" + e.getMessage());
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
        return i;
    }

    public SQLiteDatabase getWritableDatabase() {
        try {
            return this.zzFW.getWritableDatabase();
        } catch (SQLiteException e) {
            zzb.zzaK("Error opening writable conversion tracking database");
            return null;
        }
    }

    public zzf zza(Cursor cursor) {
        return cursor == null ? null : new zzf(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }

    public void zza(zzf zzf) {
        if (zzf != null) {
            synchronized (zzpV) {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                if (writableDatabase == null) {
                    return;
                }
                writableDatabase.delete("InAppPurchase", String.format(Locale.US, "%s = %d", new Object[]{"purchase_id", Long.valueOf(zzf.zzFP)}), null);
            }
        }
    }

    /* JADX WARNING: Missing block: B:18:?, code skipped:
            return;
     */
    public void zzb(com.google.android.gms.ads.internal.purchase.zzf r7) {
        /*
        r6 = this;
        if (r7 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r1 = zzpV;
        monitor-enter(r1);
        r0 = r6.getWritableDatabase();	 Catch:{ all -> 0x000e }
        if (r0 != 0) goto L_0x0011;
    L_0x000c:
        monitor-exit(r1);	 Catch:{ all -> 0x000e }
        goto L_0x0002;
    L_0x000e:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x000e }
        throw r0;
    L_0x0011:
        r2 = new android.content.ContentValues;	 Catch:{ all -> 0x000e }
        r2.<init>();	 Catch:{ all -> 0x000e }
        r3 = "product_id";
        r4 = r7.zzFR;	 Catch:{ all -> 0x000e }
        r2.put(r3, r4);	 Catch:{ all -> 0x000e }
        r3 = "developer_payload";
        r4 = r7.zzFQ;	 Catch:{ all -> 0x000e }
        r2.put(r3, r4);	 Catch:{ all -> 0x000e }
        r3 = "record_time";
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x000e }
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x000e }
        r2.put(r3, r4);	 Catch:{ all -> 0x000e }
        r3 = "InAppPurchase";
        r4 = 0;
        r2 = r0.insert(r3, r4, r2);	 Catch:{ all -> 0x000e }
        r7.zzFP = r2;	 Catch:{ all -> 0x000e }
        r0 = r6.getRecordCount();	 Catch:{ all -> 0x000e }
        r2 = (long) r0;	 Catch:{ all -> 0x000e }
        r4 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 <= 0) goto L_0x0048;
    L_0x0045:
        r6.zzfY();	 Catch:{ all -> 0x000e }
    L_0x0048:
        monitor-exit(r1);	 Catch:{ all -> 0x000e }
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.purchase.zzh.zzb(com.google.android.gms.ads.internal.purchase.zzf):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x005a A:{Catch:{ SQLiteException -> 0x0034, all -> 0x0056 }} */
    public void zzfY() {
        /*
        r11 = this;
        r9 = 0;
        r10 = zzpV;
        monitor-enter(r10);
        r0 = r11.getWritableDatabase();	 Catch:{ all -> 0x0031 }
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r10);	 Catch:{ all -> 0x0031 }
    L_0x000b:
        return;
    L_0x000c:
        r7 = "record_time ASC";
        r1 = "InAppPurchase";
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = "1";
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x0034, all -> 0x0056 }
        if (r1 == 0) goto L_0x002a;
    L_0x001d:
        r0 = r1.moveToFirst();	 Catch:{ SQLiteException -> 0x0060 }
        if (r0 == 0) goto L_0x002a;
    L_0x0023:
        r0 = r11.zza(r1);	 Catch:{ SQLiteException -> 0x0060 }
        r11.zza(r0);	 Catch:{ SQLiteException -> 0x0060 }
    L_0x002a:
        if (r1 == 0) goto L_0x002f;
    L_0x002c:
        r1.close();	 Catch:{ all -> 0x0031 }
    L_0x002f:
        monitor-exit(r10);	 Catch:{ all -> 0x0031 }
        goto L_0x000b;
    L_0x0031:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0031 }
        throw r0;
    L_0x0034:
        r0 = move-exception;
        r1 = r9;
    L_0x0036:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005e }
        r2.<init>();	 Catch:{ all -> 0x005e }
        r3 = "Error remove oldest record";
        r2 = r2.append(r3);	 Catch:{ all -> 0x005e }
        r0 = r0.getMessage();	 Catch:{ all -> 0x005e }
        r0 = r2.append(r0);	 Catch:{ all -> 0x005e }
        r0 = r0.toString();	 Catch:{ all -> 0x005e }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x005e }
        if (r1 == 0) goto L_0x002f;
    L_0x0052:
        r1.close();	 Catch:{ all -> 0x0031 }
        goto L_0x002f;
    L_0x0056:
        r0 = move-exception;
        r1 = r9;
    L_0x0058:
        if (r1 == 0) goto L_0x005d;
    L_0x005a:
        r1.close();	 Catch:{ all -> 0x0031 }
    L_0x005d:
        throw r0;	 Catch:{ all -> 0x0031 }
    L_0x005e:
        r0 = move-exception;
        goto L_0x0058;
    L_0x0060:
        r0 = move-exception;
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.purchase.zzh.zzfY():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x0070 A:{SYNTHETIC, Splitter:B:36:0x0070} */
    public java.util.List<com.google.android.gms.ads.internal.purchase.zzf> zzg(long r14) {
        /*
        r13 = this;
        r10 = 0;
        r11 = zzpV;
        monitor-enter(r11);
        r9 = new java.util.LinkedList;	 Catch:{ all -> 0x0069 }
        r9.<init>();	 Catch:{ all -> 0x0069 }
        r0 = 0;
        r0 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1));
        if (r0 > 0) goto L_0x0012;
    L_0x000f:
        monitor-exit(r11);	 Catch:{ all -> 0x0069 }
        r0 = r9;
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = r13.getWritableDatabase();	 Catch:{ all -> 0x0069 }
        if (r0 != 0) goto L_0x001b;
    L_0x0018:
        monitor-exit(r11);	 Catch:{ all -> 0x0069 }
        r0 = r9;
        goto L_0x0011;
    L_0x001b:
        r7 = "record_time ASC";
        r1 = "InAppPurchase";
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = java.lang.String.valueOf(r14);	 Catch:{ SQLiteException -> 0x0047, all -> 0x006c }
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x0047, all -> 0x006c }
        r0 = r1.moveToFirst();	 Catch:{ SQLiteException -> 0x0076 }
        if (r0 == 0) goto L_0x003f;
    L_0x0032:
        r0 = r13.zza(r1);	 Catch:{ SQLiteException -> 0x0076 }
        r9.add(r0);	 Catch:{ SQLiteException -> 0x0076 }
        r0 = r1.moveToNext();	 Catch:{ SQLiteException -> 0x0076 }
        if (r0 != 0) goto L_0x0032;
    L_0x003f:
        if (r1 == 0) goto L_0x0044;
    L_0x0041:
        r1.close();	 Catch:{ all -> 0x0069 }
    L_0x0044:
        monitor-exit(r11);	 Catch:{ all -> 0x0069 }
        r0 = r9;
        goto L_0x0011;
    L_0x0047:
        r0 = move-exception;
        r1 = r10;
    L_0x0049:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0074 }
        r2.<init>();	 Catch:{ all -> 0x0074 }
        r3 = "Error extracing purchase info: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0074 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x0074 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0074 }
        r0 = r0.toString();	 Catch:{ all -> 0x0074 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x0074 }
        if (r1 == 0) goto L_0x0044;
    L_0x0065:
        r1.close();	 Catch:{ all -> 0x0069 }
        goto L_0x0044;
    L_0x0069:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0069 }
        throw r0;
    L_0x006c:
        r0 = move-exception;
        r1 = r10;
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
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.purchase.zzh.zzg(long):java.util.List");
    }
}
