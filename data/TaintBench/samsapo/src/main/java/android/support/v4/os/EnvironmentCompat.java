package android.support.v4.os;

import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class EnvironmentCompat {
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";

    public EnvironmentCompat() {
    }

    public static String getStorageState(File file) {
        File file2 = file;
        if (VERSION.SDK_INT >= 19) {
            return EnvironmentCompatKitKat.getStorageState(file2);
        }
        try {
            if (file2.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        } catch (IOException e) {
            IOException iOException = e;
            String str = TAG;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            int w = Log.w(str, stringBuilder.append("Failed to resolve canonical path: ").append(iOException).toString());
        }
        return MEDIA_UNKNOWN;
    }
}
