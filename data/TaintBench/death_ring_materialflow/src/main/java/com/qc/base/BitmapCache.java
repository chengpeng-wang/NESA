package com.qc.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.qc.common.Funs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
public class BitmapCache {
    private static BitmapCache cache;
    private Hashtable<Integer, MySoftRef> hashRefs = new Hashtable();
    private ReferenceQueue<Bitmap> q = new ReferenceQueue();

    private class MySoftRef extends SoftReference<Bitmap> {
        /* access modifiers changed from: private */
        public Integer _key = Integer.valueOf(0);

        public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, int key) {
            super(bmp, q);
            this._key = Integer.valueOf(key);
        }
    }

    private BitmapCache() {
    }

    public static BitmapCache getInstance() {
        if (cache == null) {
            cache = new BitmapCache();
        }
        return cache;
    }

    private void addCacheBitmap(Bitmap bmp, Integer key) {
        cleanCache();
        this.hashRefs.put(key, new MySoftRef(bmp, this.q, key.intValue()));
    }

    public Bitmap getBitmap(Context context, Integer key) {
        Bitmap bmp = null;
        if (this.hashRefs.containsKey(key)) {
            bmp = (Bitmap) ((MySoftRef) this.hashRefs.get(key)).get();
        }
        if (bmp == null) {
            if (Environment.getExternalStorageState().equals("mounted")) {
                File imageFile = new File(Funs.getSDCardImagesURL() + "/" + key + ".png");
                if (imageFile.exists()) {
                    try {
                        FileInputStream fs = new FileInputStream(imageFile);
                        BufferedInputStream bs = new BufferedInputStream(fs);
                        bmp = BitmapFactory.decodeStream(bs);
                        fs.close();
                        bs.close();
                    } catch (FileNotFoundException | IOException e) {
                    }
                }
                imageFile.delete();
            } else {
                try {
                    bmp = BitmapFactory.decodeStream(context.openFileInput(key + ".png"));
                } catch (FileNotFoundException e2) {
                }
                context.deleteFile(key + ".png");
            }
        }
        return bmp;
    }

    public boolean downLoadImage(Context context, String urlStr, int id) {
        if (urlStr == null || "".equals(urlStr)) {
            return false;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            if (bitmap == null) {
                return false;
            }
            addCacheBitmap(bitmap, Integer.valueOf(id));
            File sdCardFile = Funs.getSDCardImage(new StringBuilder(String.valueOf(id)).append(".png").toString());
            if (sdCardFile != null) {
                try {
                    Funs.writeByteFile(new FileOutputStream(sdCardFile), buffer);
                } catch (FileNotFoundException e) {
                }
            } else {
                OutputStream out = context.openFileOutput(new StringBuilder(String.valueOf(id)).append(".png").toString(), 3);
                out.write(buffer);
                out.flush();
                out.close();
            }
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    private void cleanCache() {
        while (true) {
            MySoftRef ref = (MySoftRef) this.q.poll();
            if (ref != null) {
                this.hashRefs.remove(ref._key);
            } else {
                return;
            }
        }
    }

    public void clearCache() {
        cleanCache();
        this.hashRefs.clear();
        System.gc();
        System.runFinalization();
    }
}
