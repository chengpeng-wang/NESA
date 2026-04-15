package fm.xtube.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public enum BitmapManager {
    INSTANCE;
    
    private final Map<String, SoftReference<Bitmap>> cache;
    /* access modifiers changed from: private */
    public Map<ImageView, String> imageViews;
    /* access modifiers changed from: private */
    public Bitmap placeholder;
    private final ExecutorService pool;

    public void setPlaceholder(Bitmap bmp) {
        this.placeholder = bmp;
    }

    public Bitmap getBitmapFromCache(String url) {
        if (this.cache.containsKey(url)) {
            return (Bitmap) ((SoftReference) this.cache.get(url)).get();
        }
        return null;
    }

    public void queueJob(final String url, final ImageView imageView) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String tag = (String) BitmapManager.this.imageViews.get(imageView);
                if (tag != null && tag.equals(url)) {
                    if (msg.obj != null) {
                        imageView.setImageBitmap((Bitmap) msg.obj);
                        return;
                    }
                    imageView.setImageBitmap(BitmapManager.this.placeholder);
                    Log.d(null, "fail " + url);
                }
            }
        };
        this.pool.submit(new Runnable() {
            public void run() {
                Bitmap bmp = BitmapManager.this.downloadBitmap(url);
                Message message = Message.obtain();
                message.obj = bmp;
                Log.d(null, "Item downloaded: " + url);
                handler.sendMessage(message);
            }
        });
    }

    public void loadBitmap(String url, ImageView imageView) {
        this.imageViews.put(imageView, url);
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            Log.d(null, "Item loaded from cache: " + url);
            imageView.setImageBitmap(bitmap);
            return;
        }
        imageView.setImageBitmap(this.placeholder);
        queueJob(url, imageView);
    }

    /* access modifiers changed from: private */
    public Bitmap downloadBitmap(String url) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            this.cache.put(url, new SoftReference(bitmap));
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
