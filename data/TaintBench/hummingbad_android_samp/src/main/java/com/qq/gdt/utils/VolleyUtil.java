package com.qq.gdt.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.picksinit.c;
import java.io.File;

public class VolleyUtil {
    private static ImageListener preloadImageListener;
    public static ImageLoader sImageLoader = new ImageLoader(Volley.newRequestQueue(c.getInstance().getContext()), new BitmapLruCache());

    public static class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
        public static int getDefaultLruCacheSize() {
            return ((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8;
        }

        public BitmapLruCache() {
            this(getDefaultLruCacheSize());
        }

        public BitmapLruCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        /* access modifiers changed from: protected */
        public int sizeOf(String key, Bitmap bitmap) {
            if (bitmap == null) {
                return 0;
            }
            return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
        }

        public Bitmap getBitmap(String url) {
            return (Bitmap) get(url);
        }

        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }

    public static void loadImage(final ImageView view, String url) {
        doCleanAllCache();
        sImageLoader.get(url, new ImageListener() {
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                }
            }

            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    public static void loadImage(String url, ImageListener listener) {
        doCleanAllCache();
        sImageLoader.get(url, listener);
    }

    private static void doCleanAllCache() {
        String DEFAULT_CACHE_DIR = "volley";
        File[] files = new File(c.getInstance().getContext().getCacheDir(), "volley").listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static void preloadImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            doCleanAllCache();
            if (preloadImageListener == null) {
                preloadImageListener = new ImageListener() {
                    public void onResponse(ImageContainer response, boolean isImmediate) {
                    }

                    public void onErrorResponse(VolleyError error) {
                    }
                };
            }
            sImageLoader.get(imageUrl, preloadImageListener);
        }
    }
}
