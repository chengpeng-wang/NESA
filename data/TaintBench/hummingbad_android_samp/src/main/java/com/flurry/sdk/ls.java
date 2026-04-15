package com.flurry.sdk;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import java.io.File;

public final class ls {
    private static final String a = ls.class.getSimpleName();

    public static File a() {
        File file = null;
        Context context = hz.a.b;
        if ("mounted".equals(Environment.getExternalStorageState()) && (VERSION.SDK_INT >= 19 || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            file = context.getExternalFilesDir(null);
        }
        return file == null ? context.getFilesDir() : file;
    }

    public static boolean a(File file) {
        if (file == null) {
            return false;
        }
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return true;
        }
        if (parentFile.mkdirs() || parentFile.isDirectory()) {
            return true;
        }
        iw.a(6, a, "Unable to create persistent dir: " + parentFile);
        return false;
    }

    public static File b() {
        Context context = hz.a.b;
        File file = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && (VERSION.SDK_INT >= 19 || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            file = context.getExternalCacheDir();
        }
        return file == null ? context.getCacheDir() : file;
    }

    public static boolean b(File file) {
        if (file != null && file.isDirectory()) {
            for (String file2 : file.list()) {
                if (!b(new File(file, file2))) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
