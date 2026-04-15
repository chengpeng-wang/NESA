package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_CACHE = "cache";
    private static final String DIR_DATA = "data";
    private static final String DIR_FILES = "files";
    private static final String DIR_OBB = "obb";

    public ContextCompat() {
    }

    public static boolean startActivities(Context context, Intent[] intentArr) {
        return startActivities(context, intentArr, null);
    }

    public static boolean startActivities(Context context, Intent[] intentArr, Bundle bundle) {
        Context context2 = context;
        Intent[] intentArr2 = intentArr;
        Bundle bundle2 = bundle;
        int i = VERSION.SDK_INT;
        if (i >= 16) {
            ContextCompatJellybean.startActivities(context2, intentArr2, bundle2);
            return true;
        } else if (i < 11) {
            return false;
        } else {
            ContextCompatHoneycomb.startActivities(context2, intentArr2);
            return true;
        }
    }

    public static File[] getObbDirs(Context context) {
        Context context2 = context;
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getObbDirs(context2);
        }
        File obbDir;
        if (i >= 11) {
            obbDir = ContextCompatHoneycomb.getObbDir(context2);
        } else {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            String[] strArr = new String[3];
            String[] strArr2 = strArr;
            strArr[0] = DIR_ANDROID;
            strArr = strArr2;
            strArr2 = strArr;
            strArr[1] = DIR_OBB;
            strArr = strArr2;
            strArr2 = strArr;
            strArr[2] = context2.getPackageName();
            obbDir = buildPath(externalStorageDirectory, strArr2);
        }
        File[] fileArr = new File[1];
        File[] fileArr2 = fileArr;
        fileArr[0] = obbDir;
        return fileArr2;
    }

    public static File[] getExternalFilesDirs(Context context, String str) {
        Context context2 = context;
        String str2 = str;
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context2, str2);
        }
        File externalFilesDir;
        if (i >= 8) {
            externalFilesDir = ContextCompatFroyo.getExternalFilesDir(context2, str2);
        } else {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            r9 = new String[5];
            String[] strArr = r9;
            r9[0] = DIR_ANDROID;
            r9 = strArr;
            strArr = r9;
            r9[1] = DIR_DATA;
            r9 = strArr;
            strArr = r9;
            r9[2] = context2.getPackageName();
            r9 = strArr;
            strArr = r9;
            r9[3] = DIR_FILES;
            r9 = strArr;
            strArr = r9;
            r9[4] = str2;
            externalFilesDir = buildPath(externalStorageDirectory, strArr);
        }
        File[] fileArr = new File[1];
        File[] fileArr2 = fileArr;
        fileArr[0] = externalFilesDir;
        return fileArr2;
    }

    public static File[] getExternalCacheDirs(Context context) {
        Context context2 = context;
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context2);
        }
        File externalCacheDir;
        if (i >= 8) {
            externalCacheDir = ContextCompatFroyo.getExternalCacheDir(context2);
        } else {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            String[] strArr = new String[4];
            String[] strArr2 = strArr;
            strArr[0] = DIR_ANDROID;
            strArr = strArr2;
            strArr2 = strArr;
            strArr[1] = DIR_DATA;
            strArr = strArr2;
            strArr2 = strArr;
            strArr[2] = context2.getPackageName();
            strArr = strArr2;
            strArr2 = strArr;
            strArr[3] = DIR_CACHE;
            externalCacheDir = buildPath(externalStorageDirectory, strArr2);
        }
        File[] fileArr = new File[1];
        File[] fileArr2 = fileArr;
        fileArr[0] = externalCacheDir;
        return fileArr2;
    }

    private static File buildPath(File file, String... strArr) {
        File file2 = file;
        for (String str : strArr) {
            File file3;
            File file4;
            if (file2 == null) {
                file3 = r11;
                file4 = new File(str);
                file2 = file3;
            } else if (str != null) {
                file3 = r11;
                file4 = new File(file2, str);
                file2 = file3;
            }
        }
        return file2;
    }
}
