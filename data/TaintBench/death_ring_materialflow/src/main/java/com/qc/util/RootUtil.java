package com.qc.util;

import java.io.File;

public class RootUtil {
    private static final int kSystemRootStateDisable = 0;
    private static final int kSystemRootStateEnable = 1;
    private static final int kSystemRootStateUnknow = -1;
    private static int systemRootState = kSystemRootStateUnknow;

    public static int isRootSystem() {
        File f;
        if (systemRootState == 1) {
            return 1;
        }
        if (systemRootState == 0) {
            return 0;
        }
        File f2 = null;
        String[] kSuSearchPaths = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        while (true) {
            try {
                f = f2;
                if (i >= kSuSearchPaths.length) {
                    systemRootState = 0;
                    return 0;
                }
                f2 = new File(kSuSearchPaths[i] + "su");
                if (f2 != null) {
                    try {
                        if (f2.exists()) {
                            systemRootState = 1;
                            return 1;
                        }
                    } catch (Exception e) {
                        return 0;
                    }
                }
                i++;
            } catch (Exception e2) {
                f2 = f;
                return 0;
            }
        }
    }
}
