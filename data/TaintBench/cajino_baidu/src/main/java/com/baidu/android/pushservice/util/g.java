package com.baidu.android.pushservice.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import java.io.File;

class g extends ContextWrapper {
    public g(Context context) {
        super(context);
    }

    public File getDatabasePath(String str) {
        String str2 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "baidu/pushservice/database" + File.separator + str;
        if (!str2.endsWith(".db")) {
            str2 = str2 + ".db";
        }
        File file = new File(str2);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public SQLiteDatabase openOrCreateDatabase(String str, int i, CursorFactory cursorFactory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(str), null);
    }
}
