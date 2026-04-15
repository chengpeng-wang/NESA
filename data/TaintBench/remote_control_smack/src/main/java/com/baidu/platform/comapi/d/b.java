package com.baidu.platform.comapi.d;

import android.os.Environment;

public class b {
    public static int a() {
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals("bad_removal")) {
            return 2;
        }
        if (!externalStorageState.equals("checking")) {
            if (externalStorageState.equals("mounted")) {
                return 0;
            }
            if (externalStorageState.equals("mounted_ro") || externalStorageState.equals("nofs")) {
                return 2;
            }
            if (externalStorageState.equals("removed")) {
                return 3;
            }
            if (externalStorageState.equals("shared")) {
                return 3;
            }
            if (externalStorageState.equals("unmountable")) {
                return 2;
            }
            if (externalStorageState.equals("unmounted")) {
                return 3;
            }
        }
        return 0;
    }
}
