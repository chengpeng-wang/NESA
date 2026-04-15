package com.baidu.inf.iis.bcs.utils;

public class StringUtils {
    public static String trimSlash(String str) {
        String replaceAll = str.replaceAll("//", "/");
        if (str.equals(replaceAll)) {
            return replaceAll;
        }
        return trimSlash(replaceAll);
    }
}
