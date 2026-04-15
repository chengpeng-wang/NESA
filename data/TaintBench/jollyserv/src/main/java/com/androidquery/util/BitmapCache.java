package com.androidquery.util;

import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class BitmapCache extends LinkedHashMap<String, Bitmap> {
    private static final long serialVersionUID = 1;
    private int maxCount;
    private int maxPixels;
    private int maxTotalPixels;
    private int pixels;

    public BitmapCache(int mc, int mp, int mtp) {
        super(8, 0.75f, true);
        this.maxCount = mc;
        this.maxPixels = mp;
        this.maxTotalPixels = mtp;
    }

    public Bitmap put(String key, Bitmap bm) {
        Bitmap old = null;
        int px = pixels(bm);
        if (px <= this.maxPixels) {
            this.pixels += px;
            old = (Bitmap) super.put(key, bm);
            if (old != null) {
                this.pixels -= pixels(old);
            }
        }
        return old;
    }

    public Bitmap remove(Object key) {
        Bitmap old = (Bitmap) super.remove(key);
        if (old != null) {
            this.pixels -= pixels(old);
        }
        return old;
    }

    public void clear() {
        super.clear();
        this.pixels = 0;
    }

    private int pixels(Bitmap bm) {
        if (bm == null) {
            return 0;
        }
        return bm.getWidth() * bm.getHeight();
    }

    private void shrink() {
        if (this.pixels > this.maxTotalPixels) {
            Iterator<String> keys = keySet().iterator();
            while (keys.hasNext()) {
                keys.next();
                keys.remove();
                if (this.pixels <= this.maxTotalPixels) {
                    return;
                }
            }
        }
    }

    public boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
        if (this.pixels > this.maxTotalPixels || size() > this.maxCount) {
            remove(eldest.getKey());
        }
        shrink();
        return false;
    }
}
