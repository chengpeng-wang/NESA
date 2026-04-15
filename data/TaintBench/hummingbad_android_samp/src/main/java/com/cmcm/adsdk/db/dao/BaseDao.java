package com.cmcm.adsdk.db.dao;

import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
    void delete(int i);

    void delete(String... strArr);

    void execSql(String str, Object[] objArr);

    List<T> find();

    List<T> find(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, String str5);

    T get(int i);

    SQLiteOpenHelper getDbHelper();

    long insert(T t);

    boolean isExist(String str, String[] strArr);

    List<Map<String, String>> query2MapList(String str, String[] strArr);

    List<T> rawQuery(String str, String[] strArr);

    void update(T t);
}
