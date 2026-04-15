package com.mopub.common.util;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.io.File;
import org.xbill.DNS.TTL;

public class Files {
    public static File createDirectory(String absolutePath) {
        if (absolutePath == null) {
            return null;
        }
        File file = new File(absolutePath);
        if ((file.exists() && file.isDirectory()) || (file.mkdirs() && file.isDirectory())) {
            return file;
        }
        return null;
    }

    public static int intLength(File file) {
        if (file == null) {
            return 0;
        }
        long length = file.length();
        if (length < TTL.MAX_VALUE) {
            return (int) length;
        }
        return MoPubClientPositioning.NO_REPEAT;
    }
}
