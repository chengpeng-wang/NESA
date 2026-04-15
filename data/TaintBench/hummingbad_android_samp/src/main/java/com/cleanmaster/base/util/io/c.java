package com.cleanmaster.base.util.io;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/* compiled from: StorageInfoUtils */
public class c {
    public static b a() {
        return a(Environment.getDataDirectory());
    }

    public static b a(File file) {
        b bVar = null;
        if (file != null) {
            try {
                StatFs statFs = new StatFs(file.getPath());
                long a = a.a(statFs);
                long b = a.b(statFs);
                long c = a.c(statFs);
                bVar = new b();
                bVar.a = b * c;
                bVar.b = a * c;
                if (bVar.a < bVar.b) {
                    bVar.b = bVar.a;
                }
            } catch (Exception e) {
            }
        }
        return bVar;
    }
}
