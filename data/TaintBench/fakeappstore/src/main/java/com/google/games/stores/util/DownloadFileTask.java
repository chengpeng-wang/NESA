package com.google.games.stores.util;

import android.app.ProgressDialog;
import android.util.Log;
import com.google.games.stores.config.Config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileTask {
    public static File getFile(String path, String filepath, ProgressDialog pd) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(Config.SHOW_UPDATE_TIME);
        if (conn.getResponseCode() == 200) {
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(filepath);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int process = 0;
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    fos.flush();
                    fos.close();
                    is.close();
                    return file;
                }
                fos.write(buffer, 0, len);
                process += len;
                pd.setProgress(process);
            }
        } else {
            Log.i("abc", "throw RuntimeException");
            throw new RuntimeException();
        }
    }
}
