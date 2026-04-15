package com.flurry.sdk;

import android.text.TextUtils;
import java.io.File;

public final class hc {
    public static File a(int i) {
        return new File(ls.a().getPath() + File.separator + ".fcaches" + File.separator + "fileStreamCacheDownloaderTmp" + File.separator + i);
    }

    public static File a(String str) {
        return new File(ls.b().getPath() + File.separator + ".fcaches" + File.separator + str);
    }

    public static String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return String.format("%016x", new Object[]{Long.valueOf(lt.f(str))}).trim();
    }
}
