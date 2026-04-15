package com.baidu.mapapi.utils;

import java.io.File;

public class c {
    public static boolean a(String str) {
        File file = new File(str + "/BaiduMap/vmp/l");
        if (com.baidu.platform.comapi.d.c.m() > 180) {
            file = new File(str + "/BaiduMap/vmp/h");
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] list = file.list(new d());
        return list != null && list.length > 0;
    }
}
