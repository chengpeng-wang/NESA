package com.mb.bdapp.down.callback;

import android.text.TextUtils;
import com.mb.bdapp.down.IOUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;

public class FileDownloadHandler {
    private static final String TAG = FileDownloadHandler.class.getSimpleName();

    public File handleEntity(HttpEntity entity, RequestCallBackHandler callBackHandler, String target, boolean isResume, String responseFileName) throws IOException {
        Throwable th;
        if (entity == null || TextUtils.isEmpty(target)) {
            return null;
        }
        FileOutputStream fileOutputStream;
        File file = new File(target);
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (dir.exists() || dir.mkdirs()) {
                file.createNewFile();
            }
        }
        long current = 0;
        Closeable bis = null;
        Closeable bos = null;
        if (isResume) {
            try {
                current = file.length();
                fileOutputStream = new FileOutputStream(target, true);
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly(bis);
                IOUtils.closeQuietly(bos);
                throw th;
            }
        }
        fileOutputStream = new FileOutputStream(target);
        long total = entity.getContentLength() + current;
        Closeable bis2 = new BufferedInputStream(entity.getContent());
        try {
            Closeable bos2 = new BufferedOutputStream(fileOutputStream);
            if (callBackHandler != null) {
                try {
                    if (!callBackHandler.updateProgress(total, current, true)) {
                        IOUtils.closeQuietly(bis2);
                        IOUtils.closeQuietly(bos2);
                        return file;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bos = bos2;
                    bis = bis2;
                }
            }
            byte[] tmp = new byte[4096];
            while (true) {
                int len = bis2.read(tmp);
                if (len == -1) {
                    bos2.flush();
                    if (callBackHandler != null) {
                        callBackHandler.updateProgress(total, current, true);
                    }
                    IOUtils.closeQuietly(bis2);
                    IOUtils.closeQuietly(bos2);
                    if (!file.exists() || TextUtils.isEmpty(responseFileName)) {
                        return file;
                    }
                    File newFile;
                    file = new File(file.getParent(), responseFileName);
                    while (newFile.exists()) {
                        file = new File(file.getParent(), System.currentTimeMillis() + responseFileName);
                    }
                    if (!file.renameTo(newFile)) {
                        newFile = file;
                    }
                    return newFile;
                }
                bos2.write(tmp, 0, len);
                current += (long) len;
                if (callBackHandler != null && !callBackHandler.updateProgress(total, current, false)) {
                    IOUtils.closeQuietly(bis2);
                    IOUtils.closeQuietly(bos2);
                    return file;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            bis = bis2;
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            throw th;
        }
    }
}
