package com.qc.util;

import android.os.Environment;
import android.os.StatFs;
import java.text.DecimalFormat;

public class FileUtil {
    public static String sdcard_left() {
        String sizeString = "0.00";
        if (Environment.getExternalStorageState().equals("mounted")) {
            return convert(used(Environment.getExternalStorageDirectory().getPath()));
        }
        return sizeString;
    }

    public static String mobile_left() {
        String sizeString = "0.00";
        try {
            return convert(used("/data/data"));
        } catch (Exception e) {
            return sizeString;
        }
    }

    public static double used(String DirPath) {
        StatFs statFs = new StatFs(DirPath);
        return ((((double) ((long) statFs.getBlockSize())) * ((double) statFs.getAvailableBlocks())) / 1024.0d) / 1024.0d;
    }

    public static String convert(double d) {
        return new DecimalFormat("#0.00").format(d);
    }
}
