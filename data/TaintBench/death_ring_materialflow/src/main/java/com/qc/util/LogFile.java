package com.qc.util;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {
    private static boolean LogSwitch = false;

    private static File getLogFile() {
        String path = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/mnkp/debuglog").toString();
        } else {
            path = "debuglog";
        }
        File ret = new File(path);
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(new StringBuilder(String.valueOf(path)).append("/debuglogMsg.log").toString());
        if (!ret.exists()) {
            try {
                ret.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static void WriteLogFile(String logStr) {
        if (LogSwitch) {
            File file = getLogFile();
            if (file != null) {
                logStr = new StringBuilder(String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()))).append("    ").append(logStr).append("\r\n").toString();
                try {
                    FileWriter fw = new FileWriter(file, true);
                    fw.write(logStr);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
