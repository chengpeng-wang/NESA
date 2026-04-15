package com.labado.lulaoshi;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AsyncImageLoader {
    /* access modifiers changed from: private */
    public HashMap<String, WeakReference<Drawable>> imageCache = new HashMap();

    public interface ImageCallback {
        void imageLoaded(Drawable drawable, String str);
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
        if (this.imageCache.containsKey(imageUrl)) {
            Drawable drawable = (Drawable) ((WeakReference) this.imageCache.get(imageUrl)).get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
            }
        };
        new Thread() {
            public void run() {
                Drawable drawable = AsyncImageLoader.loadImageFromUrl(imageUrl);
                AsyncImageLoader.this.imageCache.put(imageUrl, new WeakReference(drawable));
                handler.sendMessage(handler.obtainMessage(0, drawable));
            }
        }.start();
        return null;
    }

    public static Drawable loadImageFromUrl(String url) {
        InputStream i = null;
        try {
            i = (InputStream) new URL(url).getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Drawable.createFromStream(i, "src");
    }
}
