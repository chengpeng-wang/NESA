package com.google.tagmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;

class SharedPreferencesUtil {
    SharedPreferencesUtil() {
    }

    static void saveEditorAsync(final Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    editor.commit();
                }
            }).start();
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    static void saveAsync(Context context, String sharedPreferencesName, String key, String value) {
        Editor editor = context.getSharedPreferences(sharedPreferencesName, 0).edit();
        editor.putString(key, value);
        saveEditorAsync(editor);
    }
}
