package com.qc.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import com.qc.common.Funs;
import com.qc.util.HttpUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
public class ApkDownLoadManager {
    public static byte[] updateApkData_apache(Context context, String urlString, String apkName) {
        try {
            byte[] buffer = HttpUtil.queryAPKForGet(urlString).trim().getBytes();
            File sdCardFile = Funs.getSDCardFile(new StringBuilder(String.valueOf(apkName)).append(".apk").toString());
            if (sdCardFile != null) {
                try {
                    Funs.writeByteFile(new FileOutputStream(sdCardFile), buffer);
                    return buffer;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return buffer;
                }
            }
            OutputStream out = context.openFileOutput(new StringBuilder(String.valueOf(apkName)).append(".apk").toString(), 3);
            out.write(buffer);
            out.flush();
            out.close();
            return buffer;
        } catch (Exception e2) {
            return null;
        }
    }

    public static byte[] updateApkData(Context context, String urlString, String apkName) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            int length = conn.getContentLength();
            byte[] buffer = new byte[length];
            int offset = 0;
            int numread = 0;
            while (offset < length && numread >= 0) {
                numread = inputStream.read(buffer, offset, length - offset);
                offset += numread;
            }
            inputStream.read(buffer);
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    Funs.writeByteFile(new FileOutputStream(Funs.getSDCardFile(new StringBuilder(String.valueOf(apkName)).append(".apk").toString())), buffer);
                } catch (FileNotFoundException e) {
                }
            } else {
                OutputStream out = context.openFileOutput(new StringBuilder(String.valueOf(apkName)).append(".apk").toString(), 3);
                out.write(buffer);
                out.flush();
                out.close();
            }
            conn.disconnect();
            return buffer;
        } catch (MalformedURLException e2) {
            return null;
        } catch (IOException e3) {
            return null;
        } catch (Exception e4) {
            return null;
        }
    }
}
