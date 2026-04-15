package com.xmpp.client.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {
    private String _location;
    private String _zipFile;

    public UnzipUtil(String zipFile, String location) {
        this._zipFile = zipFile;
        this._location = location;
        _dirChecker("");
    }

    public void unzip() {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(this._zipFile));
            while (true) {
                ZipEntry ze = zin.getNextEntry();
                if (ze == null) {
                    zin.close();
                    return;
                }
                Log.v("Decompress", "Unzipping " + ze.getName());
                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(this._location + ze.getName());
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int len = zin.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        fout.write(buffer, 0, len);
                    }
                    fout.close();
                    zin.closeEntry();
                }
            }
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(this._location + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
