package com.baidu.mapapi.utils;

import java.io.File;
import java.io.FilenameFilter;

final class d implements FilenameFilter {
    d() {
    }

    public boolean accept(File file, String str) {
        return str.endsWith(".dat") || str.endsWith(".dat_svc") || str.endsWith(".dat_seg") || str.equalsIgnoreCase("DVWifilog.cfg") || str.equalsIgnoreCase("DVUserdat.cfg");
    }
}
