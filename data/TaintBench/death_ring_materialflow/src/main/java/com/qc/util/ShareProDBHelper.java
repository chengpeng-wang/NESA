package com.qc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareProDBHelper {
    public static final int BOOLEANVALUE = 1;
    public static final int FLOATVALUE = 5;
    public static final int INTEGERVALUE = 3;
    public static final int LONGVALUE = 4;
    public static final int STRINGVALUE = 2;

    public static Editor getEditor(Context context, String dbName) {
        return context.getSharedPreferences(dbName, 0).edit();
    }

    public static SharedPreferences getSharedPreferences(Context context, String dbName) {
        return context.getSharedPreferences(dbName, 0);
    }

    public static boolean write(Context context, String dbName, String key, Object value) {
        Editor editor = context.getSharedPreferences(dbName, 0).edit();
        if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
            editor.commit();
            return true;
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
            editor.commit();
            return true;
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
            editor.commit();
            return true;
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
            editor.commit();
            return true;
        } else if (!(value instanceof Float)) {
            return false;
        } else {
            editor.putFloat(key, ((Float) value).floatValue());
            editor.commit();
            return true;
        }
    }

    public static Object read(Context context, String dbName, String key, int mode) {
        SharedPreferences settings = context.getSharedPreferences(dbName, 0);
        if (mode == 1) {
            return Boolean.valueOf(settings.getBoolean(key, false));
        }
        if (mode == 2) {
            return settings.getString(key, "");
        }
        if (mode == 3) {
            return Integer.valueOf(settings.getInt(key, 0));
        }
        if (mode == 4) {
            return Long.valueOf(settings.getLong(key, 0));
        }
        if (mode == 5) {
            return Float.valueOf(settings.getFloat(key, 0.0f));
        }
        return false;
    }
}
