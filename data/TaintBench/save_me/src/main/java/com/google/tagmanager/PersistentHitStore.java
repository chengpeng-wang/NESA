package com.google.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.SimpleNetworkDispatcher.DispatchListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.impl.client.DefaultHttpClient;

class PersistentHitStore implements HitStore {
    /* access modifiers changed from: private|static|final */
    public static final String CREATE_HITS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", new Object[]{HITS_TABLE, HIT_ID, HIT_TIME, HIT_URL, HIT_FIRST_DISPATCH_TIME});
    private static final String DATABASE_FILENAME = "gtm_urls.db";
    @VisibleForTesting
    static final String HITS_TABLE = "gtm_hits";
    static final long HIT_DISPATCH_RETRY_WINDOW = 14400000;
    @VisibleForTesting
    static final String HIT_FIRST_DISPATCH_TIME = "hit_first_send_time";
    @VisibleForTesting
    static final String HIT_ID = "hit_id";
    private static final String HIT_ID_WHERE_CLAUSE = "hit_id=?";
    @VisibleForTesting
    static final String HIT_TIME = "hit_time";
    @VisibleForTesting
    static final String HIT_URL = "hit_url";
    /* access modifiers changed from: private */
    public Clock mClock;
    /* access modifiers changed from: private|final */
    public final Context mContext;
    /* access modifiers changed from: private|final */
    public final String mDatabaseName;
    private final UrlDatabaseHelper mDbHelper;
    private volatile Dispatcher mDispatcher;
    private long mLastDeleteStaleHitsTime;
    private final HitStoreStateListener mListener;

    @VisibleForTesting
    class UrlDatabaseHelper extends SQLiteOpenHelper {
        private boolean mBadDatabase;
        private long mLastDatabaseCheckTime = 0;

        /* access modifiers changed from: 0000 */
        public boolean isBadDatabase() {
            return this.mBadDatabase;
        }

        /* access modifiers changed from: 0000 */
        public void setBadDatabase(boolean badDatabase) {
            this.mBadDatabase = badDatabase;
        }

        UrlDatabaseHelper(Context context, String databaseName) {
            super(context, databaseName, null, 1);
        }

        private boolean tablePresent(String table, SQLiteDatabase db) {
            Cursor cursor = null;
            try {
                SQLiteDatabase sQLiteDatabase = db;
                cursor = sQLiteDatabase.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{table}, null, null, null);
                boolean moveToFirst = cursor.moveToFirst();
                if (cursor == null) {
                    return moveToFirst;
                }
                cursor.close();
                return moveToFirst;
            } catch (SQLiteException e) {
                Log.w("Error querying for table " + table);
                if (cursor != null) {
                    cursor.close();
                }
                return false;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            if (!this.mBadDatabase || this.mLastDatabaseCheckTime + 3600000 <= PersistentHitStore.this.mClock.currentTimeMillis()) {
                SQLiteDatabase db = null;
                this.mBadDatabase = true;
                this.mLastDatabaseCheckTime = PersistentHitStore.this.mClock.currentTimeMillis();
                try {
                    db = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    PersistentHitStore.this.mContext.getDatabasePath(PersistentHitStore.this.mDatabaseName).delete();
                }
                if (db == null) {
                    db = super.getWritableDatabase();
                }
                this.mBadDatabase = false;
                return db;
            }
            throw new SQLiteException("Database creation failed");
        }

        public void onOpen(SQLiteDatabase db) {
            if (VERSION.SDK_INT < 15) {
                Cursor cursor = db.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    cursor.moveToFirst();
                } finally {
                    cursor.close();
                }
            }
            if (tablePresent(PersistentHitStore.HITS_TABLE, db)) {
                validateColumnsPresent(db);
            } else {
                db.execSQL(PersistentHitStore.CREATE_HITS_TABLE);
            }
        }

        private void validateColumnsPresent(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT * FROM gtm_hits WHERE 0", null);
            Set<String> columns = new HashSet();
            try {
                String[] columnNames = c.getColumnNames();
                for (Object add : columnNames) {
                    columns.add(add);
                }
                if (!columns.remove(PersistentHitStore.HIT_ID) || !columns.remove(PersistentHitStore.HIT_URL) || !columns.remove(PersistentHitStore.HIT_TIME) || !columns.remove(PersistentHitStore.HIT_FIRST_DISPATCH_TIME)) {
                    throw new SQLiteException("Database column missing");
                } else if (!columns.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } finally {
                c.close();
            }
        }

        public void onCreate(SQLiteDatabase db) {
            FutureApis.setOwnerOnlyReadWrite(db.getPath());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    @VisibleForTesting
    class StoreDispatchListener implements DispatchListener {
        StoreDispatchListener() {
        }

        public void onHitDispatched(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
        }

        public void onHitPermanentDispatchFailure(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
            Log.v("Permanent failure dispatching hitId: " + hit.getHitId());
        }

        public void onHitTransientDispatchFailure(Hit hit) {
            long firstDispatchTime = hit.getHitFirstDispatchTime();
            if (firstDispatchTime == 0) {
                PersistentHitStore.this.setHitFirstDispatchTime(hit.getHitId(), PersistentHitStore.this.mClock.currentTimeMillis());
            } else if (PersistentHitStore.HIT_DISPATCH_RETRY_WINDOW + firstDispatchTime < PersistentHitStore.this.mClock.currentTimeMillis()) {
                PersistentHitStore.this.deleteHit(hit.getHitId());
                Log.v("Giving up on failed hitId: " + hit.getHitId());
            }
        }
    }

    PersistentHitStore(HitStoreStateListener listener, Context ctx) {
        this(listener, ctx, DATABASE_FILENAME);
    }

    @VisibleForTesting
    PersistentHitStore(HitStoreStateListener listener, Context ctx, String databaseName) {
        this.mContext = ctx.getApplicationContext();
        this.mDatabaseName = databaseName;
        this.mListener = listener;
        this.mClock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.mDbHelper = new UrlDatabaseHelper(this.mContext, this.mDatabaseName);
        this.mDispatcher = new SimpleNetworkDispatcher(new DefaultHttpClient(), this.mContext, new StoreDispatchListener());
        this.mLastDeleteStaleHitsTime = 0;
    }

    @VisibleForTesting
    public void setClock(Clock clock) {
        this.mClock = clock;
    }

    @VisibleForTesting
    public UrlDatabaseHelper getDbHelper() {
        return this.mDbHelper;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setDispatcher(Dispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    public void putHit(long hitTimeInMilliseconds, String path) {
        deleteStaleHits();
        removeOldHitIfFull();
        writeHitToDatabase(hitTimeInMilliseconds, path);
    }

    private void removeOldHitIfFull() {
        int hitsOverLimit = (getNumStoredHits() - 2000) + 1;
        if (hitsOverLimit > 0) {
            List<String> hitsToDelete = peekHitIds(hitsOverLimit);
            Log.v("Store full, deleting " + hitsToDelete.size() + " hits to make room.");
            deleteHits((String[]) hitsToDelete.toArray(new String[0]));
        }
    }

    private void writeHitToDatabase(long hitTimeInMilliseconds, String path) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for putHit");
        if (db != null) {
            ContentValues content = new ContentValues();
            content.put(HIT_TIME, Long.valueOf(hitTimeInMilliseconds));
            content.put(HIT_URL, path);
            content.put(HIT_FIRST_DISPATCH_TIME, Integer.valueOf(0));
            try {
                db.insert(HITS_TABLE, null, content);
                this.mListener.reportStoreIsEmpty(false);
            } catch (SQLiteException e) {
                Log.w("Error storing hit");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public List<String> peekHitIds(int maxHits) {
        List<String> hitIds = new ArrayList();
        if (maxHits <= 0) {
            Log.w("Invalid maxHits specified. Skipping");
        } else {
            SQLiteDatabase db = getWritableDatabase("Error opening database for peekHitIds.");
            if (db != null) {
                Cursor cursor = null;
                try {
                    cursor = db.query(HITS_TABLE, new String[]{HIT_ID}, null, null, null, null, String.format("%s ASC", new Object[]{HIT_ID}), Integer.toString(maxHits));
                    if (cursor.moveToFirst()) {
                        do {
                            hitIds.add(String.valueOf(cursor.getLong(0)));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e) {
                    Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
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
        return hitIds;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00f7  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ed  */
    public java.util.List<com.google.tagmanager.Hit> peekHits(int r23) {
        /*
        r22 = this;
        r17 = new java.util.ArrayList;
        r17.<init>();
        r3 = "Error opening database for peekHits";
        r0 = r22;
        r1 = r0.getWritableDatabase(r3);
        if (r1 != 0) goto L_0x0012;
    L_0x000f:
        r18 = r17;
    L_0x0011:
        return r18;
    L_0x0012:
        r13 = 0;
        r2 = "gtm_hits";
        r3 = 3;
        r3 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x00d0 }
        r4 = 0;
        r5 = "hit_id";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00d0 }
        r4 = 1;
        r5 = "hit_time";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00d0 }
        r4 = 2;
        r5 = "hit_first_send_time";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00d0 }
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = "%s ASC";
        r9 = 1;
        r9 = new java.lang.Object[r9];	 Catch:{ SQLiteException -> 0x00d0 }
        r10 = 0;
        r11 = "hit_id";
        r9[r10] = r11;	 Catch:{ SQLiteException -> 0x00d0 }
        r8 = java.lang.String.format(r8, r9);	 Catch:{ SQLiteException -> 0x00d0 }
        r9 = java.lang.Integer.toString(r23);	 Catch:{ SQLiteException -> 0x00d0 }
        r13 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ SQLiteException -> 0x00d0 }
        r18 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x00d0 }
        r18.<init>();	 Catch:{ SQLiteException -> 0x00d0 }
        r3 = r13.moveToFirst();	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        if (r3 == 0) goto L_0x006b;
    L_0x004c:
        r2 = new com.google.tagmanager.Hit;	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r3 = 0;
        r3 = r13.getLong(r3);	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r5 = 1;
        r5 = r13.getLong(r5);	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r7 = 2;
        r7 = r13.getLong(r7);	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r2.m664init(r3, r5, r7);	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r0 = r18;
        r0.add(r2);	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        r3 = r13.moveToNext();	 Catch:{ SQLiteException -> 0x0178, all -> 0x0173 }
        if (r3 != 0) goto L_0x004c;
    L_0x006b:
        if (r13 == 0) goto L_0x0070;
    L_0x006d:
        r13.close();
    L_0x0070:
        r12 = 0;
        r4 = "gtm_hits";
        r3 = 2;
        r5 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x011b }
        r3 = 0;
        r6 = "hit_id";
        r5[r3] = r6;	 Catch:{ SQLiteException -> 0x011b }
        r3 = 1;
        r6 = "hit_url";
        r5[r3] = r6;	 Catch:{ SQLiteException -> 0x011b }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r3 = "%s ASC";
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ SQLiteException -> 0x011b }
        r11 = 0;
        r21 = "hit_id";
        r10[r11] = r21;	 Catch:{ SQLiteException -> 0x011b }
        r10 = java.lang.String.format(r3, r10);	 Catch:{ SQLiteException -> 0x011b }
        r11 = java.lang.Integer.toString(r23);	 Catch:{ SQLiteException -> 0x011b }
        r3 = r1;
        r13 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ SQLiteException -> 0x011b }
        r3 = r13.moveToFirst();	 Catch:{ SQLiteException -> 0x011b }
        if (r3 == 0) goto L_0x00c7;
    L_0x00a1:
        r0 = r13;
        r0 = (android.database.sqlite.SQLiteCursor) r0;	 Catch:{ SQLiteException -> 0x011b }
        r3 = r0;
        r14 = r3.getWindow();	 Catch:{ SQLiteException -> 0x011b }
        r3 = r14.getNumRows();	 Catch:{ SQLiteException -> 0x011b }
        if (r3 <= 0) goto L_0x00fb;
    L_0x00af:
        r0 = r18;
        r3 = r0.get(r12);	 Catch:{ SQLiteException -> 0x011b }
        r3 = (com.google.tagmanager.Hit) r3;	 Catch:{ SQLiteException -> 0x011b }
        r4 = 1;
        r4 = r13.getString(r4);	 Catch:{ SQLiteException -> 0x011b }
        r3.setHitUrl(r4);	 Catch:{ SQLiteException -> 0x011b }
    L_0x00bf:
        r12 = r12 + 1;
        r3 = r13.moveToNext();	 Catch:{ SQLiteException -> 0x011b }
        if (r3 != 0) goto L_0x00a1;
    L_0x00c7:
        if (r13 == 0) goto L_0x00cc;
    L_0x00c9:
        r13.close();
    L_0x00cc:
        r17 = r18;
        goto L_0x0011;
    L_0x00d0:
        r15 = move-exception;
    L_0x00d1:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f4 }
        r3.<init>();	 Catch:{ all -> 0x00f4 }
        r4 = "Error in peekHits fetching hitIds: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00f4 }
        r4 = r15.getMessage();	 Catch:{ all -> 0x00f4 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x00f4 }
        r3 = r3.toString();	 Catch:{ all -> 0x00f4 }
        com.google.tagmanager.Log.w(r3);	 Catch:{ all -> 0x00f4 }
        if (r13 == 0) goto L_0x00f0;
    L_0x00ed:
        r13.close();
    L_0x00f0:
        r18 = r17;
        goto L_0x0011;
    L_0x00f4:
        r3 = move-exception;
    L_0x00f5:
        if (r13 == 0) goto L_0x00fa;
    L_0x00f7:
        r13.close();
    L_0x00fa:
        throw r3;
    L_0x00fb:
        r4 = "HitString for hitId %d too large.  Hit will be deleted.";
        r3 = 1;
        r5 = new java.lang.Object[r3];	 Catch:{ SQLiteException -> 0x011b }
        r6 = 0;
        r0 = r18;
        r3 = r0.get(r12);	 Catch:{ SQLiteException -> 0x011b }
        r3 = (com.google.tagmanager.Hit) r3;	 Catch:{ SQLiteException -> 0x011b }
        r7 = r3.getHitId();	 Catch:{ SQLiteException -> 0x011b }
        r3 = java.lang.Long.valueOf(r7);	 Catch:{ SQLiteException -> 0x011b }
        r5[r6] = r3;	 Catch:{ SQLiteException -> 0x011b }
        r3 = java.lang.String.format(r4, r5);	 Catch:{ SQLiteException -> 0x011b }
        com.google.tagmanager.Log.w(r3);	 Catch:{ SQLiteException -> 0x011b }
        goto L_0x00bf;
    L_0x011b:
        r15 = move-exception;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016c }
        r3.<init>();	 Catch:{ all -> 0x016c }
        r4 = "Error in peekHits fetching hit url: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x016c }
        r4 = r15.getMessage();	 Catch:{ all -> 0x016c }
        r3 = r3.append(r4);	 Catch:{ all -> 0x016c }
        r3 = r3.toString();	 Catch:{ all -> 0x016c }
        com.google.tagmanager.Log.w(r3);	 Catch:{ all -> 0x016c }
        r20 = new java.util.ArrayList;	 Catch:{ all -> 0x016c }
        r20.<init>();	 Catch:{ all -> 0x016c }
        r16 = 0;
        r19 = r18.iterator();	 Catch:{ all -> 0x016c }
    L_0x0141:
        r3 = r19.hasNext();	 Catch:{ all -> 0x016c }
        if (r3 == 0) goto L_0x0159;
    L_0x0147:
        r2 = r19.next();	 Catch:{ all -> 0x016c }
        r2 = (com.google.tagmanager.Hit) r2;	 Catch:{ all -> 0x016c }
        r3 = r2.getHitUrl();	 Catch:{ all -> 0x016c }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x016c }
        if (r3 == 0) goto L_0x0166;
    L_0x0157:
        if (r16 == 0) goto L_0x0164;
    L_0x0159:
        if (r13 == 0) goto L_0x015e;
    L_0x015b:
        r13.close();
    L_0x015e:
        r17 = r18;
        r18 = r20;
        goto L_0x0011;
    L_0x0164:
        r16 = 1;
    L_0x0166:
        r0 = r20;
        r0.add(r2);	 Catch:{ all -> 0x016c }
        goto L_0x0141;
    L_0x016c:
        r3 = move-exception;
        if (r13 == 0) goto L_0x0172;
    L_0x016f:
        r13.close();
    L_0x0172:
        throw r3;
    L_0x0173:
        r3 = move-exception;
        r17 = r18;
        goto L_0x00f5;
    L_0x0178:
        r15 = move-exception;
        r17 = r18;
        goto L_0x00d1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.PersistentHitStore.peekHits(int):java.util.List");
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setLastDeleteStaleHitsTime(long timeInMilliseconds) {
        this.mLastDeleteStaleHitsTime = timeInMilliseconds;
    }

    /* access modifiers changed from: 0000 */
    public int deleteStaleHits() {
        boolean z = true;
        long now = this.mClock.currentTimeMillis();
        if (now <= this.mLastDeleteStaleHitsTime + 86400000) {
            return 0;
        }
        this.mLastDeleteStaleHitsTime = now;
        SQLiteDatabase db = getWritableDatabase("Error opening database for deleteStaleHits.");
        if (db == null) {
            return 0;
        }
        long lastGoodTime = this.mClock.currentTimeMillis() - 2592000000L;
        int rslt = db.delete(HITS_TABLE, "HIT_TIME < ?", new String[]{Long.toString(lastGoodTime)});
        HitStoreStateListener hitStoreStateListener = this.mListener;
        if (getNumStoredHits() != 0) {
            z = false;
        }
        hitStoreStateListener.reportStoreIsEmpty(z);
        return rslt;
    }

    /* access modifiers changed from: 0000 */
    public void deleteHits(String[] hitIds) {
        boolean z = true;
        if (hitIds != null && hitIds.length != 0) {
            SQLiteDatabase db = getWritableDatabase("Error opening database for deleteHits.");
            if (db != null) {
                try {
                    db.delete(HITS_TABLE, String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(",", Collections.nCopies(hitIds.length, "?"))}), hitIds);
                    HitStoreStateListener hitStoreStateListener = this.mListener;
                    if (getNumStoredHits() != 0) {
                        z = false;
                    }
                    hitStoreStateListener.reportStoreIsEmpty(z);
                } catch (SQLiteException e) {
                    Log.w("Error deleting hits");
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void deleteHit(long hitId) {
        deleteHits(new String[]{String.valueOf(hitId)});
    }

    /* access modifiers changed from: private */
    public void setHitFirstDispatchTime(long hitId, long firstDispatchTime) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db != null) {
            ContentValues cv = new ContentValues();
            cv.put(HIT_FIRST_DISPATCH_TIME, Long.valueOf(firstDispatchTime));
            try {
                db.update(HITS_TABLE, cv, HIT_ID_WHERE_CLAUSE, new String[]{String.valueOf(hitId)});
            } catch (SQLiteException e) {
                Log.w("Error setting HIT_FIRST_DISPATCH_TIME for hitId: " + hitId);
                deleteHit(hitId);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int getNumStoredHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) from gtm_hits", null);
            if (cursor.moveToFirst()) {
                numStoredHits = (int) cursor.getLong(0);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLiteException e) {
            Log.w("Error getting numStoredHits");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return numStoredHits;
    }

    /* access modifiers changed from: 0000 */
    public int getNumStoredUntriedHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return numStoredHits;
        }
        Cursor cursor = null;
        try {
            cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_FIRST_DISPATCH_TIME}, "hit_first_send_time=0", null, null, null, null);
            numStoredHits = cursor.getCount();
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLiteException e) {
            Log.w("Error getting num untried hits");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return numStoredHits;
    }

    public void dispatch() {
        Log.v("GTM Dispatch running...");
        if (this.mDispatcher.okToDispatch()) {
            List<Hit> hits = peekHits(40);
            if (hits.isEmpty()) {
                Log.v("...nothing to dispatch");
                this.mListener.reportStoreIsEmpty(true);
                return;
            }
            this.mDispatcher.dispatchHits(hits);
            if (getNumStoredUntriedHits() > 0) {
                ServiceManagerImpl.getInstance().dispatch();
            }
        }
    }

    public Dispatcher getDispatcher() {
        return this.mDispatcher;
    }

    public void close() {
        try {
            this.mDbHelper.getWritableDatabase().close();
            this.mDispatcher.close();
        } catch (SQLiteException e) {
            Log.w("Error opening database for close");
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public UrlDatabaseHelper getHelper() {
        return this.mDbHelper;
    }

    private SQLiteDatabase getWritableDatabase(String errorMessage) {
        try {
            return this.mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.w(errorMessage);
            return null;
        }
    }
}
