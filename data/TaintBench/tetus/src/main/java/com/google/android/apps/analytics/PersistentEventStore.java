package com.google.android.apps.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.security.SecureRandom;
import java.util.ArrayList;

class PersistentEventStore implements EventStore {
    private static final String ACCOUNT_ID = "account_id";
    private static final String ACTION = "action";
    private static final String CATEGORY = "category";
    private static final String DATABASE_NAME = "google_analytics.db";
    private static final int DATABASE_VERSION = 1;
    private static final String EVENT_ID = "event_id";
    private static final String LABEL = "label";
    private static final int MAX_EVENTS = 1000;
    private static final String RANDOM_VAL = "random_val";
    private static final String REFERRER = "referrer";
    private static final String SCREEN_HEIGHT = "screen_height";
    private static final String SCREEN_WIDTH = "screen_width";
    private static final String STORE_ID = "store_id";
    private static final String TIMESTAMP_CURRENT = "timestamp_current";
    private static final String TIMESTAMP_FIRST = "timestamp_first";
    private static final String TIMESTAMP_PREVIOUS = "timestamp_previous";
    private static final String USER_ID = "user_id";
    private static final String VALUE = "value";
    private static final String VISITS = "visits";
    private SQLiteStatement compiledCountStatement;
    private DataBaseHelper databaseHelper;
    private int numStoredEvents;
    private boolean sessionUpdated;
    private int storeId;
    private long timestampCurrent;
    private long timestampFirst;
    private long timestampPrevious;
    private int visits;

    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, PersistentEventStore.DATABASE_NAME, null, PersistentEventStore.DATABASE_VERSION);
        }

        public DataBaseHelper(Context context, String str) {
            super(context, str, null, PersistentEventStore.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            String str = " '%s' INTEGER,";
            String str2 = " '%s' CHAR(256) NOT NULL,";
            String str3 = " '%s' INTEGER NOT NULL,";
            StringBuilder append = new StringBuilder().append("CREATE TABLE events (");
            Object[] objArr = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr[0] = PersistentEventStore.EVENT_ID;
            append = append.append(String.format(" '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,", objArr));
            String str4 = " '%s' INTEGER NOT NULL,";
            Object[] objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.USER_ID;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' CHAR(256) NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.ACCOUNT_ID;
            append = append.append(String.format(str2, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.RANDOM_VAL;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.TIMESTAMP_FIRST;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.TIMESTAMP_PREVIOUS;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.TIMESTAMP_CURRENT;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.VISITS;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' CHAR(256) NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.CATEGORY;
            append = append.append(String.format(str2, objArr2));
            str4 = " '%s' CHAR(256) NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.ACTION;
            append = append.append(String.format(str2, objArr2));
            objArr = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr[0] = PersistentEventStore.LABEL;
            append = append.append(String.format(" '%s' CHAR(256), ", objArr));
            str4 = " '%s' INTEGER,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.VALUE;
            append = append.append(String.format(str, objArr2));
            str4 = " '%s' INTEGER,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.SCREEN_WIDTH;
            append = append.append(String.format(str, objArr2));
            objArr = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr[0] = PersistentEventStore.SCREEN_HEIGHT;
            sQLiteDatabase.execSQL(append.append(String.format(" '%s' INTEGER);", objArr)).toString());
            append = new StringBuilder().append("CREATE TABLE session (");
            objArr = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr[0] = PersistentEventStore.TIMESTAMP_FIRST;
            append = append.append(String.format(" '%s' INTEGER PRIMARY KEY,", objArr));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.TIMESTAMP_PREVIOUS;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.TIMESTAMP_CURRENT;
            append = append.append(String.format(str3, objArr2));
            str4 = " '%s' INTEGER NOT NULL,";
            objArr2 = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr2[0] = PersistentEventStore.VISITS;
            append = append.append(String.format(str3, objArr2));
            objArr = new Object[PersistentEventStore.DATABASE_VERSION];
            objArr[0] = PersistentEventStore.STORE_ID;
            sQLiteDatabase.execSQL(append.append(String.format(" '%s' INTEGER NOT NULL);", objArr)).toString());
            sQLiteDatabase.execSQL("CREATE TABLE install_referrer (referrer TEXT PRIMARY KEY NOT NULL);");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.w(GoogleAnalyticsTracker.TRACKER_TAG, "Database upgrade attempted, with no upgrade method available");
        }
    }

    public PersistentEventStore(Context context) {
        this(context, null);
    }

    public PersistentEventStore(Context context, String str) {
        this.compiledCountStatement = null;
        if (str != null) {
            this.databaseHelper = new DataBaseHelper(context, str);
        } else {
            this.databaseHelper = new DataBaseHelper(context);
        }
    }

    private void storeUpdatedSession() {
        SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMESTAMP_PREVIOUS, Long.valueOf(this.timestampPrevious));
        contentValues.put(TIMESTAMP_CURRENT, Long.valueOf(this.timestampCurrent));
        contentValues.put(VISITS, Integer.valueOf(this.visits));
        String[] strArr = new String[DATABASE_VERSION];
        strArr[0] = Long.toString(this.timestampFirst);
        writableDatabase.update("session", contentValues, "timestamp_first=?", strArr);
        this.sessionUpdated = true;
    }

    public void deleteEvent(long j) {
        if (this.databaseHelper.getWritableDatabase().delete("events", "event_id=" + j, null) != 0) {
            this.numStoredEvents -= DATABASE_VERSION;
        }
    }

    public int getNumStoredEvents() {
        if (this.compiledCountStatement == null) {
            this.compiledCountStatement = this.databaseHelper.getReadableDatabase().compileStatement("SELECT COUNT(*) from events");
        }
        return (int) this.compiledCountStatement.simpleQueryForLong();
    }

    public String getReferrer() {
        String[] strArr = new String[DATABASE_VERSION];
        strArr[0] = REFERRER;
        Cursor query = this.databaseHelper.getReadableDatabase().query("install_referrer", strArr, null, null, null, null, null);
        String string = query.moveToFirst() ? query.getString(0) : null;
        query.close();
        return string;
    }

    public int getStoreId() {
        return this.storeId;
    }

    public Event[] peekEvents() {
        return peekEvents(MAX_EVENTS);
    }

    public Event[] peekEvents(int i) {
        Cursor query = this.databaseHelper.getReadableDatabase().query("events", null, null, null, null, null, EVENT_ID, Integer.toString(i));
        ArrayList arrayList = new ArrayList();
        while (query.moveToNext()) {
            arrayList.add(new Event(query.getLong(0), query.getInt(DATABASE_VERSION), query.getString(2), query.getInt(3), query.getInt(4), query.getInt(5), query.getInt(6), query.getInt(7), query.getString(8), query.getString(9), query.getString(10), query.getInt(11), query.getInt(12), query.getInt(13)));
        }
        query.close();
        return (Event[]) arrayList.toArray(new Event[arrayList.size()]);
    }

    public void putEvent(Event event) {
        if (this.numStoredEvents >= MAX_EVENTS) {
            Log.w(GoogleAnalyticsTracker.TRACKER_TAG, "Store full. Not storing last event.");
            return;
        }
        if (!this.sessionUpdated) {
            storeUpdatedSession();
        }
        SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, Integer.valueOf(event.userId));
        contentValues.put(ACCOUNT_ID, event.accountId);
        contentValues.put(RANDOM_VAL, Integer.valueOf((int) (Math.random() * 2.147483647E9d)));
        contentValues.put(TIMESTAMP_FIRST, Long.valueOf(this.timestampFirst));
        contentValues.put(TIMESTAMP_PREVIOUS, Long.valueOf(this.timestampPrevious));
        contentValues.put(TIMESTAMP_CURRENT, Long.valueOf(this.timestampCurrent));
        contentValues.put(VISITS, Integer.valueOf(this.visits));
        contentValues.put(CATEGORY, event.category);
        contentValues.put(ACTION, event.action);
        contentValues.put(LABEL, event.label);
        contentValues.put(VALUE, Integer.valueOf(event.value));
        contentValues.put(SCREEN_WIDTH, Integer.valueOf(event.screenWidth));
        contentValues.put(SCREEN_HEIGHT, Integer.valueOf(event.screenHeight));
        if (writableDatabase.insert("events", EVENT_ID, contentValues) != -1) {
            this.numStoredEvents += DATABASE_VERSION;
        }
    }

    public void setReferrer(String str) {
        SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REFERRER, str);
        writableDatabase.insert("install_referrer", null, contentValues);
    }

    public void startNewVisit() {
        String str = TIMESTAMP_FIRST;
        String str2 = "session";
        this.sessionUpdated = false;
        this.numStoredEvents = getNumStoredEvents();
        SQLiteDatabase writableDatabase = this.databaseHelper.getWritableDatabase();
        String str3 = "session";
        Cursor query = writableDatabase.query(str2, null, null, null, null, null, null);
        if (query.moveToFirst()) {
            this.timestampFirst = query.getLong(0);
            this.timestampPrevious = query.getLong(2);
            this.timestampCurrent = System.currentTimeMillis() / 1000;
            this.visits = query.getInt(3) + DATABASE_VERSION;
            this.storeId = query.getInt(4);
        } else {
            long currentTimeMillis = System.currentTimeMillis() / 1000;
            this.timestampFirst = currentTimeMillis;
            this.timestampPrevious = currentTimeMillis;
            this.timestampCurrent = currentTimeMillis;
            this.visits = DATABASE_VERSION;
            this.storeId = new SecureRandom().nextInt() & Integer.MAX_VALUE;
            ContentValues contentValues = new ContentValues();
            String str4 = TIMESTAMP_FIRST;
            contentValues.put(str, Long.valueOf(this.timestampFirst));
            contentValues.put(TIMESTAMP_PREVIOUS, Long.valueOf(this.timestampPrevious));
            contentValues.put(TIMESTAMP_CURRENT, Long.valueOf(this.timestampCurrent));
            contentValues.put(VISITS, Integer.valueOf(this.visits));
            contentValues.put(STORE_ID, Integer.valueOf(this.storeId));
            str4 = "session";
            str4 = TIMESTAMP_FIRST;
            writableDatabase.insert(str2, str, contentValues);
        }
        query.close();
    }
}
