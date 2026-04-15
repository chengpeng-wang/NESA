package com.google.analytics.tracking.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

class PersistentAnalyticsStore implements AnalyticsStore {
    @VisibleForTesting
    static final String BACKEND_LIBRARY_VERSION = "";
    /* access modifiers changed from: private|static|final */
    public static final String CREATE_HITS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL, '%s' TEXT NOT NULL, '%s' INTEGER);", new Object[]{HITS_TABLE, HIT_ID, HIT_TIME, HIT_URL, HIT_STRING, HIT_APP_ID});
    private static final String DATABASE_FILENAME = "google_analytics_v2.db";
    @VisibleForTesting
    static final String HITS_TABLE = "hits2";
    @VisibleForTesting
    static final String HIT_APP_ID = "hit_app_id";
    @VisibleForTesting
    static final String HIT_ID = "hit_id";
    @VisibleForTesting
    static final String HIT_STRING = "hit_string";
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
    private final AnalyticsDatabaseHelper mDbHelper;
    private volatile Dispatcher mDispatcher;
    private long mLastDeleteStaleHitsTime;
    private final AnalyticsStoreStateListener mListener;

    @VisibleForTesting
    class AnalyticsDatabaseHelper extends SQLiteOpenHelper {
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

        AnalyticsDatabaseHelper(Context context, String databaseName) {
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
                Log.w("error querying for table " + table);
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
            if (!this.mBadDatabase || this.mLastDatabaseCheckTime + 3600000 <= PersistentAnalyticsStore.this.mClock.currentTimeMillis()) {
                SQLiteDatabase db = null;
                this.mBadDatabase = true;
                this.mLastDatabaseCheckTime = PersistentAnalyticsStore.this.mClock.currentTimeMillis();
                try {
                    db = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    PersistentAnalyticsStore.this.mContext.getDatabasePath(PersistentAnalyticsStore.this.mDatabaseName).delete();
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
            if (tablePresent(PersistentAnalyticsStore.HITS_TABLE, db)) {
                validateColumnsPresent(db);
            } else {
                db.execSQL(PersistentAnalyticsStore.CREATE_HITS_TABLE);
            }
        }

        private void validateColumnsPresent(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT * FROM hits2 WHERE 0", null);
            Set<String> columns = new HashSet();
            try {
                String[] columnNames = c.getColumnNames();
                for (Object add : columnNames) {
                    columns.add(add);
                }
                if (columns.remove(PersistentAnalyticsStore.HIT_ID) && columns.remove(PersistentAnalyticsStore.HIT_URL) && columns.remove(PersistentAnalyticsStore.HIT_STRING) && columns.remove(PersistentAnalyticsStore.HIT_TIME)) {
                    boolean needsAppId = !columns.remove(PersistentAnalyticsStore.HIT_APP_ID);
                    if (!columns.isEmpty()) {
                        throw new SQLiteException("Database has extra columns");
                    } else if (needsAppId) {
                        db.execSQL("ALTER TABLE hits2 ADD COLUMN hit_app_id");
                        return;
                    } else {
                        return;
                    }
                }
                throw new SQLiteException("Database column missing");
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

    PersistentAnalyticsStore(AnalyticsStoreStateListener listener, Context ctx) {
        this(listener, ctx, DATABASE_FILENAME);
    }

    @VisibleForTesting
    PersistentAnalyticsStore(AnalyticsStoreStateListener listener, Context ctx, String databaseName) {
        this.mContext = ctx.getApplicationContext();
        this.mDatabaseName = databaseName;
        this.mListener = listener;
        this.mClock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.mDbHelper = new AnalyticsDatabaseHelper(this.mContext, this.mDatabaseName);
        this.mDispatcher = new SimpleNetworkDispatcher(this, createDefaultHttpClientFactory(), this.mContext);
        this.mLastDeleteStaleHitsTime = 0;
    }

    @VisibleForTesting
    public void setClock(Clock clock) {
        this.mClock = clock;
    }

    @VisibleForTesting
    public AnalyticsDatabaseHelper getDbHelper() {
        return this.mDbHelper;
    }

    private HttpClientFactory createDefaultHttpClientFactory() {
        return new HttpClientFactory() {
            public HttpClient newInstance() {
                return new DefaultHttpClient();
            }
        };
    }

    public void setDispatch(boolean dispatch) {
        this.mDispatcher = dispatch ? new SimpleNetworkDispatcher(this, createDefaultHttpClientFactory(), this.mContext) : new NoopDispatcher();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setDispatcher(Dispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    public void clearHits(long appId) {
        boolean z = true;
        SQLiteDatabase db = getWritableDatabase("Error opening database for clearHits");
        if (db != null) {
            if (appId == 0) {
                db.delete(HITS_TABLE, null, null);
            } else {
                db.delete(HITS_TABLE, "hit_app_id = ?", new String[]{Long.valueOf(appId).toString()});
            }
            AnalyticsStoreStateListener analyticsStoreStateListener = this.mListener;
            if (getNumStoredHits() != 0) {
                z = false;
            }
            analyticsStoreStateListener.reportStoreIsEmpty(z);
        }
    }

    public void putHit(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, Collection<Command> commands) {
        deleteStaleHits();
        fillVersionParametersIfNecessary(wireFormatParams, commands);
        removeOldHitIfFull();
        writeHitToDatabase(wireFormatParams, hitTimeInMilliseconds, path);
    }

    private void fillVersionParametersIfNecessary(Map<String, String> wireFormatParams, Collection<Command> commands) {
        for (Command command : commands) {
            if (command.getId().equals(Command.APPEND_VERSION)) {
                storeVersion(wireFormatParams, command.getUrlParam(), command.getValue());
                return;
            }
        }
    }

    private void storeVersion(Map<String, String> wireFormatParams, String versionUrlParam, String clientVersion) {
        String version = clientVersion;
        if (clientVersion == null) {
            version = "";
        } else {
            version = clientVersion + "";
        }
        if (versionUrlParam != null) {
            wireFormatParams.put(versionUrlParam, version);
        }
    }

    private void removeOldHitIfFull() {
        int hitsOverLimit = (getNumStoredHits() - 2000) + 1;
        if (hitsOverLimit > 0) {
            List<Hit> hitsToDelete = peekHits(hitsOverLimit);
            Log.wDebug("Store full, deleting " + hitsToDelete.size() + " hits to make room");
            deleteHits(hitsToDelete);
        }
    }

    private void writeHitToDatabase(Map<String, String> hit, long hitTimeInMilliseconds, String path) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for putHit");
        if (db != null) {
            ContentValues content = new ContentValues();
            content.put(HIT_STRING, generateHitString(hit));
            content.put(HIT_TIME, Long.valueOf(hitTimeInMilliseconds));
            long appSystemId = 0;
            if (hit.containsKey(ModelFields.ANDROID_APP_UID)) {
                try {
                    appSystemId = Long.parseLong((String) hit.get(ModelFields.ANDROID_APP_UID));
                } catch (NumberFormatException e) {
                }
            }
            content.put(HIT_APP_ID, Long.valueOf(appSystemId));
            if (path == null) {
                path = "http://www.google-analytics.com/collect";
            }
            if (path.length() == 0) {
                Log.w("empty path: not sending hit");
                return;
            }
            content.put(HIT_URL, path);
            try {
                db.insert(HITS_TABLE, null, content);
                this.mListener.reportStoreIsEmpty(false);
            } catch (SQLiteException e2) {
                Log.w("Error storing hit");
            }
        }
    }

    public static String generateHitString(Map<String, String> urlParams) {
        List<String> keyAndValues = new ArrayList(urlParams.size());
        for (Entry<String, String> entry : urlParams.entrySet()) {
            keyAndValues.add(((String) entry.getKey()) + "=" + HitBuilder.encode((String) entry.getValue()));
        }
        return TextUtils.join("&", keyAndValues);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0100  */
    /* JADX WARNING: Missing block: B:68:?, code skipped:
            return r18;
     */
    public java.util.List<com.google.analytics.tracking.android.Hit> peekHits(int r23) {
        /*
        r22 = this;
        r3 = "Error opening database for peekHits";
        r0 = r22;
        r1 = r0.getWritableDatabase(r3);
        if (r1 != 0) goto L_0x0010;
    L_0x000a:
        r18 = new java.util.ArrayList;
        r18.<init>();
    L_0x000f:
        return r18;
    L_0x0010:
        r13 = 0;
        r17 = new java.util.ArrayList;
        r17.<init>();
        r2 = "hits2";
        r3 = 3;
        r3 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x00de }
        r4 = 0;
        r5 = "hit_id";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00de }
        r4 = 1;
        r5 = "hit_time";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00de }
        r4 = 2;
        r5 = "hit_url";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00de }
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = "%s ASC, %s ASC";
        r9 = 2;
        r9 = new java.lang.Object[r9];	 Catch:{ SQLiteException -> 0x00de }
        r10 = 0;
        r11 = "hit_url";
        r9[r10] = r11;	 Catch:{ SQLiteException -> 0x00de }
        r10 = 1;
        r11 = "hit_id";
        r9[r10] = r11;	 Catch:{ SQLiteException -> 0x00de }
        r8 = java.lang.String.format(r8, r9);	 Catch:{ SQLiteException -> 0x00de }
        r9 = java.lang.Integer.toString(r23);	 Catch:{ SQLiteException -> 0x00de }
        r13 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ SQLiteException -> 0x00de }
        r18 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x00de }
        r18.<init>();	 Catch:{ SQLiteException -> 0x00de }
        r3 = r13.moveToFirst();	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        if (r3 == 0) goto L_0x0077;
    L_0x0054:
        r2 = new com.google.analytics.tracking.android.Hit;	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r3 = 0;
        r4 = 0;
        r4 = r13.getLong(r4);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r6 = 1;
        r6 = r13.getLong(r6);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r2.m280init(r3, r4, r6);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r3 = 2;
        r3 = r13.getString(r3);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r2.setHitUrl(r3);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r0 = r18;
        r0.add(r2);	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        r3 = r13.moveToNext();	 Catch:{ SQLiteException -> 0x01a2, all -> 0x019d }
        if (r3 != 0) goto L_0x0054;
    L_0x0077:
        if (r13 == 0) goto L_0x007c;
    L_0x0079:
        r13.close();
    L_0x007c:
        r12 = 0;
        r4 = "hits2";
        r3 = 2;
        r5 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x0135 }
        r3 = 0;
        r6 = "hit_id";
        r5[r3] = r6;	 Catch:{ SQLiteException -> 0x0135 }
        r3 = 1;
        r6 = "hit_string";
        r5[r3] = r6;	 Catch:{ SQLiteException -> 0x0135 }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r3 = "%s ASC";
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ SQLiteException -> 0x0135 }
        r11 = 0;
        r21 = "hit_id";
        r10[r11] = r21;	 Catch:{ SQLiteException -> 0x0135 }
        r10 = java.lang.String.format(r3, r10);	 Catch:{ SQLiteException -> 0x0135 }
        r11 = java.lang.Integer.toString(r23);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r1;
        r13 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r13.moveToFirst();	 Catch:{ SQLiteException -> 0x0135 }
        if (r3 == 0) goto L_0x00d7;
    L_0x00ad:
        r3 = r13 instanceof android.database.sqlite.SQLiteCursor;	 Catch:{ SQLiteException -> 0x0135 }
        if (r3 == 0) goto L_0x017c;
    L_0x00b1:
        r0 = r13;
        r0 = (android.database.sqlite.SQLiteCursor) r0;	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r0;
        r14 = r3.getWindow();	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r14.getNumRows();	 Catch:{ SQLiteException -> 0x0135 }
        if (r3 <= 0) goto L_0x010c;
    L_0x00bf:
        r0 = r18;
        r3 = r0.get(r12);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = (com.google.analytics.tracking.android.Hit) r3;	 Catch:{ SQLiteException -> 0x0135 }
        r4 = 1;
        r4 = r13.getString(r4);	 Catch:{ SQLiteException -> 0x0135 }
        r3.setHitString(r4);	 Catch:{ SQLiteException -> 0x0135 }
    L_0x00cf:
        r12 = r12 + 1;
        r3 = r13.moveToNext();	 Catch:{ SQLiteException -> 0x0135 }
        if (r3 != 0) goto L_0x00ad;
    L_0x00d7:
        if (r13 == 0) goto L_0x000f;
    L_0x00d9:
        r13.close();
        goto L_0x000f;
    L_0x00de:
        r15 = move-exception;
    L_0x00df:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0105 }
        r3.<init>();	 Catch:{ all -> 0x0105 }
        r4 = "error in peekHits fetching hitIds: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0105 }
        r4 = r15.getMessage();	 Catch:{ all -> 0x0105 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0105 }
        r3 = r3.toString();	 Catch:{ all -> 0x0105 }
        com.google.analytics.tracking.android.Log.w(r3);	 Catch:{ all -> 0x0105 }
        r18 = new java.util.ArrayList;	 Catch:{ all -> 0x0105 }
        r18.<init>();	 Catch:{ all -> 0x0105 }
        if (r13 == 0) goto L_0x000f;
    L_0x0100:
        r13.close();
        goto L_0x000f;
    L_0x0105:
        r3 = move-exception;
    L_0x0106:
        if (r13 == 0) goto L_0x010b;
    L_0x0108:
        r13.close();
    L_0x010b:
        throw r3;
    L_0x010c:
        r3 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0135 }
        r3.<init>();	 Catch:{ SQLiteException -> 0x0135 }
        r4 = "hitString for hitId ";
        r4 = r3.append(r4);	 Catch:{ SQLiteException -> 0x0135 }
        r0 = r18;
        r3 = r0.get(r12);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = (com.google.analytics.tracking.android.Hit) r3;	 Catch:{ SQLiteException -> 0x0135 }
        r5 = r3.getHitId();	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r4.append(r5);	 Catch:{ SQLiteException -> 0x0135 }
        r4 = " too large.  Hit will be deleted.";
        r3 = r3.append(r4);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = r3.toString();	 Catch:{ SQLiteException -> 0x0135 }
        com.google.analytics.tracking.android.Log.w(r3);	 Catch:{ SQLiteException -> 0x0135 }
        goto L_0x00cf;
    L_0x0135:
        r15 = move-exception;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x018e }
        r3.<init>();	 Catch:{ all -> 0x018e }
        r4 = "error in peekHits fetching hitString: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x018e }
        r4 = r15.getMessage();	 Catch:{ all -> 0x018e }
        r3 = r3.append(r4);	 Catch:{ all -> 0x018e }
        r3 = r3.toString();	 Catch:{ all -> 0x018e }
        com.google.analytics.tracking.android.Log.w(r3);	 Catch:{ all -> 0x018e }
        r20 = new java.util.ArrayList;	 Catch:{ all -> 0x018e }
        r20.<init>();	 Catch:{ all -> 0x018e }
        r16 = 0;
        r19 = r18.iterator();	 Catch:{ all -> 0x018e }
    L_0x015b:
        r3 = r19.hasNext();	 Catch:{ all -> 0x018e }
        if (r3 == 0) goto L_0x0173;
    L_0x0161:
        r2 = r19.next();	 Catch:{ all -> 0x018e }
        r2 = (com.google.analytics.tracking.android.Hit) r2;	 Catch:{ all -> 0x018e }
        r3 = r2.getHitParams();	 Catch:{ all -> 0x018e }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x018e }
        if (r3 == 0) goto L_0x0197;
    L_0x0171:
        if (r16 == 0) goto L_0x0195;
    L_0x0173:
        if (r13 == 0) goto L_0x0178;
    L_0x0175:
        r13.close();
    L_0x0178:
        r18 = r20;
        goto L_0x000f;
    L_0x017c:
        r0 = r18;
        r3 = r0.get(r12);	 Catch:{ SQLiteException -> 0x0135 }
        r3 = (com.google.analytics.tracking.android.Hit) r3;	 Catch:{ SQLiteException -> 0x0135 }
        r4 = 1;
        r4 = r13.getString(r4);	 Catch:{ SQLiteException -> 0x0135 }
        r3.setHitString(r4);	 Catch:{ SQLiteException -> 0x0135 }
        goto L_0x00cf;
    L_0x018e:
        r3 = move-exception;
        if (r13 == 0) goto L_0x0194;
    L_0x0191:
        r13.close();
    L_0x0194:
        throw r3;
    L_0x0195:
        r16 = 1;
    L_0x0197:
        r0 = r20;
        r0.add(r2);	 Catch:{ all -> 0x018e }
        goto L_0x015b;
    L_0x019d:
        r3 = move-exception;
        r17 = r18;
        goto L_0x0106;
    L_0x01a2:
        r15 = move-exception;
        r17 = r18;
        goto L_0x00df;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.analytics.tracking.android.PersistentAnalyticsStore.peekHits(int):java.util.List");
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
        SQLiteDatabase db = getWritableDatabase("Error opening database for deleteStaleHits");
        if (db == null) {
            return 0;
        }
        long lastGoodTime = this.mClock.currentTimeMillis() - 2592000000L;
        int rslt = db.delete(HITS_TABLE, "HIT_TIME < ?", new String[]{Long.toString(lastGoodTime)});
        AnalyticsStoreStateListener analyticsStoreStateListener = this.mListener;
        if (getNumStoredHits() != 0) {
            z = false;
        }
        analyticsStoreStateListener.reportStoreIsEmpty(z);
        return rslt;
    }

    public void deleteHits(Collection<Hit> hits) {
        if (hits == null) {
            throw new NullPointerException("hits cannot be null");
        } else if (!hits.isEmpty()) {
            SQLiteDatabase db = getWritableDatabase("Error opening database for deleteHit");
            if (db != null) {
                String[] ids = new String[hits.size()];
                String whereClause = String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(",", Collections.nCopies(ids.length, "?"))});
                int i = 0;
                for (Hit hit : hits) {
                    int i2 = i + 1;
                    ids[i] = Long.toString(hit.getHitId());
                    i = i2;
                }
                try {
                    db.delete(HITS_TABLE, whereClause, ids);
                    this.mListener.reportStoreIsEmpty(getNumStoredHits() == 0);
                } catch (SQLiteException e) {
                    Log.w("Error deleting hit " + hits);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int getNumStoredHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for requestNumHitsPending");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) from hits2", null);
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

    public void dispatch() {
        Log.vDebug("dispatch running...");
        if (this.mDispatcher.okToDispatch()) {
            List<Hit> hits = peekHits(40);
            if (hits.isEmpty()) {
                Log.vDebug("...nothing to dispatch");
                this.mListener.reportStoreIsEmpty(true);
                return;
            }
            int hitsDispatched = this.mDispatcher.dispatchHits(hits);
            Log.vDebug("sent " + hitsDispatched + " of " + hits.size() + " hits");
            deleteHits(hits.subList(0, Math.min(hitsDispatched, hits.size())));
            if (hitsDispatched == hits.size() && getNumStoredHits() > 0) {
                GAServiceManager.getInstance().dispatch();
            }
        }
    }

    public void close() {
        try {
            this.mDbHelper.getWritableDatabase().close();
        } catch (SQLiteException e) {
            Log.w("Error opening database for close");
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public AnalyticsDatabaseHelper getHelper() {
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
