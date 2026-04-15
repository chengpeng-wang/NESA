package com.splunk.mint;

import java.io.File;
import java.io.FileFilter;

class SplunkFileFilter implements FileFilter {
    private static final String POSTFIX = ".json";
    private static final String PREFIX = "MintSavedData-1-";
    private static final String VERSION = "1";
    private static SplunkFileFilter fileFilterSingleton = null;

    SplunkFileFilter() {
    }

    public boolean accept(File filename) {
        if (filename.getName().startsWith(PREFIX) && filename.getName().endsWith(POSTFIX)) {
            return true;
        }
        return false;
    }

    public static String createNewFile() {
        return Properties.FILES_PATH + "/" + PREFIX + String.valueOf(System.currentTimeMillis()) + POSTFIX;
    }

    public static SplunkFileFilter getInstance() {
        if (fileFilterSingleton == null) {
            fileFilterSingleton = new SplunkFileFilter();
        }
        return fileFilterSingleton;
    }
}
