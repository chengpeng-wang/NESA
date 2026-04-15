package cn.smsmanager.tools;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

public class FileLog {
    public static void LogString(String logString) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/log.txt"), true);
            outputStream.write(logString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
