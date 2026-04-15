package com.androidquery.callback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import com.androidquery.auth.AccountHandle;
import com.androidquery.util.AQUtility;
import com.androidquery.util.BitmapCache;
import com.androidquery.util.Common;
import com.androidquery.util.Constants;
import com.androidquery.util.RatioDrawable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.http.HttpHost;

public class BitmapAjaxCallback extends AbstractAjaxCallback<Bitmap, BitmapAjaxCallback> {
    private static int BIG_MAX = 20;
    private static int BIG_PIXELS = 160000;
    private static int BIG_TPIXELS = 1000000;
    private static boolean DELAY_WRITE = false;
    private static final int FADE_DUR = 300;
    private static int SMALL_MAX = 20;
    private static int SMALL_PIXELS = 2500;
    private static Map<String, Bitmap> bigCache;
    private static Bitmap dummy = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
    private static Bitmap empty = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
    private static Map<String, Bitmap> invalidCache;
    private static HashMap<String, WeakHashMap<ImageView, BitmapAjaxCallback>> queueMap = new HashMap();
    private static Map<String, Bitmap> smallCache;
    private float anchor = Float.MAX_VALUE;
    private int animation;
    private Bitmap bm;
    private int fallback;
    private File imageFile;
    private boolean invalid;
    private Bitmap preset;
    private float ratio;
    private int round;
    private boolean targetDim = true;
    private int targetWidth;
    private WeakReference<ImageView> v;

    public BitmapAjaxCallback() {
        ((BitmapAjaxCallback) ((BitmapAjaxCallback) ((BitmapAjaxCallback) type(Bitmap.class)).memCache(true)).fileCache(true)).url("");
    }

    public BitmapAjaxCallback imageView(ImageView view) {
        this.v = new WeakReference(view);
        return this;
    }

    public BitmapAjaxCallback targetWidth(int targetWidth) {
        this.targetWidth = targetWidth;
        return this;
    }

    public BitmapAjaxCallback file(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public BitmapAjaxCallback preset(Bitmap preset) {
        this.preset = preset;
        return this;
    }

    public BitmapAjaxCallback bitmap(Bitmap bm) {
        this.bm = bm;
        return this;
    }

    public BitmapAjaxCallback fallback(int resId) {
        this.fallback = resId;
        return this;
    }

    public BitmapAjaxCallback animation(int animation) {
        this.animation = animation;
        return this;
    }

    public BitmapAjaxCallback ratio(float ratio) {
        this.ratio = ratio;
        return this;
    }

    public BitmapAjaxCallback anchor(float anchor) {
        this.anchor = anchor;
        return this;
    }

    public BitmapAjaxCallback round(int radius) {
        this.round = radius;
        return this;
    }

    private static Bitmap decode(String path, byte[] data, Options options) {
        Bitmap result = null;
        if (path != null) {
            result = decodeFile(path, options);
        } else if (data != null) {
            result = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }
        if (!(result != null || options == null || options.inJustDecodeBounds)) {
            AQUtility.debug("decode image failed", path);
        }
        return result;
    }

    private static Bitmap decodeFile(String path, Options options) {
        IOException e;
        Throwable th;
        Bitmap result = null;
        if (options == null) {
            options = new Options();
        }
        options.inInputShareable = true;
        options.inPurgeable = true;
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(path);
            try {
                result = BitmapFactory.decodeFileDescriptor(fis2.getFD(), null, options);
                AQUtility.close(fis2);
                fis = fis2;
            } catch (IOException e2) {
                e = e2;
                fis = fis2;
                try {
                    AQUtility.report(e);
                    AQUtility.close(fis);
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    AQUtility.close(fis);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                AQUtility.close(fis);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            AQUtility.report(e);
            AQUtility.close(fis);
            return result;
        }
        return result;
    }

    public static Bitmap getResizedImage(String path, byte[] data, int target, boolean width, int round) {
        Options options = null;
        if (target > 0) {
            Options info = new Options();
            info.inJustDecodeBounds = true;
            decode(path, data, info);
            int dim = info.outWidth;
            if (!width) {
                dim = Math.max(dim, info.outHeight);
            }
            int ssize = sampleSize(dim, target);
            options = new Options();
            options.inSampleSize = ssize;
        }
        Bitmap bm = null;
        try {
            bm = decode(path, data, options);
        } catch (OutOfMemoryError e) {
            clearCache();
            AQUtility.report(e);
        }
        if (round > 0) {
            return getRoundedCornerBitmap(bm, round);
        }
        return bm;
    }

    private static int sampleSize(int width, int target) {
        int result = 1;
        for (int i = 0; i < 10 && width >= target * 2; i++) {
            width /= 2;
            result *= 2;
        }
        return result;
    }

    private Bitmap bmGet(String path, byte[] data) {
        return getResizedImage(path, data, this.targetWidth, this.targetDim, this.round);
    }

    /* access modifiers changed from: protected */
    public File accessFile(File cacheDir, String url) {
        if (this.imageFile == null || !this.imageFile.exists()) {
            return super.accessFile(cacheDir, url);
        }
        return this.imageFile;
    }

    /* access modifiers changed from: protected */
    public Bitmap fileGet(String url, File file, AjaxStatus status) {
        return bmGet(file.getAbsolutePath(), null);
    }

    public Bitmap transform(String url, byte[] data, AjaxStatus status) {
        String path = null;
        File file = status.getFile();
        if (file != null) {
            path = file.getAbsolutePath();
        }
        Bitmap bm = bmGet(path, data);
        if (bm == null) {
            if (this.fallback > 0) {
                bm = getFallback();
            } else if (this.fallback == -2 || this.fallback == -1) {
                bm = dummy;
            } else if (this.fallback == -3) {
                bm = this.preset;
            }
            if (status.getCode() != 200) {
                this.invalid = true;
            }
        }
        return bm;
    }

    private Bitmap getFallback() {
        Bitmap bm = null;
        View view = (View) this.v.get();
        if (view != null) {
            String key = Integer.toString(this.fallback);
            bm = memGet(key);
            if (bm == null) {
                bm = BitmapFactory.decodeResource(view.getResources(), this.fallback);
                if (bm != null) {
                    memPut(key, bm);
                }
            }
        }
        return bm;
    }

    public static Bitmap getMemoryCached(Context context, int resId) {
        String key = Integer.toString(resId);
        Bitmap bm = memGet(key, 0, 0);
        if (bm == null) {
            bm = BitmapFactory.decodeResource(context.getResources(), resId);
            if (bm != null) {
                memPut(key, 0, 0, bm, false);
            }
        }
        return bm;
    }

    public static Bitmap getEmptyBitmap() {
        return empty;
    }

    public final void callback(String url, Bitmap bm, AjaxStatus status) {
        ImageView firstView = (ImageView) this.v.get();
        WeakHashMap<ImageView, BitmapAjaxCallback> ivs = (WeakHashMap) queueMap.remove(url);
        if (ivs == null || !ivs.containsKey(firstView)) {
            checkCb(this, url, firstView, bm, status);
        }
        if (ivs != null) {
            for (ImageView view : ivs.keySet()) {
                BitmapAjaxCallback cb = (BitmapAjaxCallback) ivs.get(view);
                cb.status = status;
                checkCb(cb, url, view, bm, status);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void skip(String url, Bitmap bm, AjaxStatus status) {
        queueMap.remove(url);
    }

    private void checkCb(BitmapAjaxCallback cb, String url, ImageView v, Bitmap bm, AjaxStatus status) {
        if (v != null && cb != null) {
            if (url.equals(v.getTag(Constants.TAG_URL))) {
                if (v instanceof ImageView) {
                    cb.callback(url, v, bm, status);
                } else {
                    setBitmap(url, v, bm, false);
                }
            }
            showProgress(false);
        }
    }

    /* access modifiers changed from: protected */
    public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
        setBitmap(url, iv, bm, false);
    }

    public static void setIconCacheLimit(int limit) {
        SMALL_MAX = limit;
        clearCache();
    }

    public static void setCacheLimit(int limit) {
        BIG_MAX = limit;
        clearCache();
    }

    public static void setDelayWrite(boolean delay) {
        DELAY_WRITE = delay;
    }

    public static void setPixelLimit(int pixels) {
        BIG_PIXELS = pixels;
        clearCache();
    }

    public static void setSmallPixel(int pixels) {
        SMALL_PIXELS = pixels;
        clearCache();
    }

    public static void setMaxPixelLimit(int pixels) {
        BIG_TPIXELS = pixels;
        clearCache();
    }

    public static void clearCache() {
        bigCache = null;
        smallCache = null;
        invalidCache = null;
    }

    protected static void clearTasks() {
        queueMap.clear();
    }

    private static Map<String, Bitmap> getBCache() {
        if (bigCache == null) {
            bigCache = Collections.synchronizedMap(new BitmapCache(BIG_MAX, BIG_PIXELS, BIG_TPIXELS));
        }
        return bigCache;
    }

    private static Map<String, Bitmap> getSCache() {
        if (smallCache == null) {
            smallCache = Collections.synchronizedMap(new BitmapCache(SMALL_MAX, SMALL_PIXELS, 250000));
        }
        return smallCache;
    }

    private static Map<String, Bitmap> getICache() {
        if (invalidCache == null) {
            invalidCache = Collections.synchronizedMap(new BitmapCache(100, BIG_PIXELS, 250000));
        }
        return invalidCache;
    }

    /* access modifiers changed from: protected */
    public Bitmap memGet(String url) {
        if (this.bm != null) {
            return this.bm;
        }
        if (this.memCache) {
            return memGet(url, this.targetWidth, this.round);
        }
        return null;
    }

    public static boolean isMemoryCached(String url) {
        return getBCache().containsKey(url) || getSCache().containsKey(url) || getICache().containsKey(url);
    }

    public static Bitmap getMemoryCached(String url, int targetWidth) {
        return memGet(url, targetWidth, 0);
    }

    private static Bitmap memGet(String url, int targetWidth, int round) {
        url = getKey(url, targetWidth, round);
        Bitmap result = (Bitmap) getBCache().get(url);
        if (result == null) {
            result = (Bitmap) getSCache().get(url);
        }
        if (result != null) {
            return result;
        }
        result = (Bitmap) getICache().get(url);
        if (result == null || AbstractAjaxCallback.getLastStatus() != 200) {
            return result;
        }
        invalidCache = null;
        return null;
    }

    private static String getKey(String url, int targetWidth, int round) {
        if (targetWidth > 0) {
            url = new StringBuilder(String.valueOf(url)).append("#").append(targetWidth).toString();
        }
        if (round > 0) {
            return new StringBuilder(String.valueOf(url)).append("#").append(round).toString();
        }
        return url;
    }

    private static void memPut(String url, int targetWidth, int round, Bitmap bm, boolean invalid) {
        if (bm != null) {
            Map<String, Bitmap> cache;
            int pixels = bm.getWidth() * bm.getHeight();
            if (invalid) {
                cache = getICache();
            } else if (pixels <= SMALL_PIXELS) {
                cache = getSCache();
            } else {
                cache = getBCache();
            }
            if (targetWidth > 0 || round > 0) {
                cache.put(getKey(url, targetWidth, round), bm);
                if (!cache.containsKey(url)) {
                    cache.put(url, null);
                    return;
                }
                return;
            }
            cache.put(url, bm);
        }
    }

    /* access modifiers changed from: protected */
    public void memPut(String url, Bitmap bm) {
        memPut(url, this.targetWidth, this.round, bm, this.invalid);
    }

    private static Bitmap filter(View iv, Bitmap bm, int fallback) {
        if (bm != null && bm.getWidth() == 1 && bm.getHeight() == 1 && bm != empty) {
            bm = null;
        }
        if (bm != null) {
            iv.setVisibility(0);
        } else if (fallback == -2) {
            iv.setVisibility(8);
        } else if (fallback == -1) {
            iv.setVisibility(4);
        }
        return bm;
    }

    private void presetBitmap(String url, ImageView v) {
        if (!url.equals(v.getTag(Constants.TAG_URL)) || this.preset != null) {
            v.setTag(Constants.TAG_URL, url);
            if (this.preset == null || cacheAvailable(v.getContext())) {
                setBitmap(url, v, null, true);
            } else {
                setBitmap(url, v, this.preset, true);
            }
        }
    }

    private void setBitmap(String url, ImageView iv, Bitmap bm, boolean isPreset) {
        if (bm == null) {
            iv.setImageDrawable(null);
        } else if (isPreset) {
            iv.setImageDrawable(makeDrawable(iv, bm, this.ratio, this.anchor));
        } else if (this.status != null) {
            setBmAnimate(iv, bm, this.preset, this.fallback, this.animation, this.ratio, this.anchor, this.status.getSource());
        }
    }

    private static Drawable makeDrawable(ImageView iv, Bitmap bm, float ratio, float anchor) {
        if (ratio > 0.0f) {
            return new RatioDrawable(iv.getResources(), bm, iv, ratio, anchor);
        }
        return new BitmapDrawable(iv.getResources(), bm);
    }

    private static void setBmAnimate(ImageView iv, Bitmap bm, Bitmap preset, int fallback, int animation, float ratio, float anchor, int source) {
        bm = filter(iv, bm, fallback);
        if (bm == null) {
            iv.setImageBitmap(null);
            return;
        }
        Drawable d = makeDrawable(iv, bm, ratio, anchor);
        Animation anim = null;
        if (fadeIn(animation, source)) {
            if (preset == null) {
                anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.setDuration(300);
            } else {
                Drawable td = new TransitionDrawable(new Drawable[]{makeDrawable(iv, preset, ratio, anchor), d});
                td.setCrossFadeEnabled(true);
                td.startTransition(FADE_DUR);
                d = td;
            }
        } else if (animation > 0) {
            anim = AnimationUtils.loadAnimation(iv.getContext(), animation);
        }
        iv.setImageDrawable(d);
        if (anim != null) {
            anim.setStartTime(AnimationUtils.currentAnimationTimeMillis());
            iv.startAnimation(anim);
            return;
        }
        iv.setAnimation(null);
    }

    private static boolean fadeIn(int animation, int source) {
        switch (animation) {
            case -3:
                if (source == 3) {
                    return true;
                }
                break;
            case -2:
                break;
            case -1:
                return true;
        }
        if (source == 1) {
            return true;
        }
        return false;
    }

    public static void async(Activity act, Context context, ImageView iv, String url, Object progress, AccountHandle ah, ImageOptions options, HttpHost proxy, String networkUrl) {
        async(act, context, iv, url, options.memCache, options.fileCache, options.targetWidth, options.fallback, options.preset, options.animation, options.ratio, options.anchor, progress, ah, options.policy, options.round, proxy, networkUrl);
    }

    public static void async(Activity act, Context context, ImageView iv, String url, boolean memCache, boolean fileCache, int targetWidth, int fallbackId, Bitmap preset, int animation, float ratio, float anchor, Object progress, AccountHandle ah, int policy, int round, HttpHost proxy, String networkUrl) {
        Bitmap bm = null;
        if (memCache) {
            bm = memGet(url, targetWidth, round);
        }
        if (bm != null) {
            iv.setTag(Constants.TAG_URL, url);
            Common.showProgress(progress, url, false);
            setBmAnimate(iv, bm, preset, fallbackId, animation, ratio, anchor, 4);
            return;
        }
        BitmapAjaxCallback cb = new BitmapAjaxCallback();
        ((BitmapAjaxCallback) ((BitmapAjaxCallback) ((BitmapAjaxCallback) ((BitmapAjaxCallback) ((BitmapAjaxCallback) ((BitmapAjaxCallback) cb.url(url)).imageView(iv).memCache(memCache)).fileCache(fileCache)).targetWidth(targetWidth).fallback(fallbackId).preset(preset).animation(animation).ratio(ratio).anchor(anchor).progress(progress)).auth(ah)).policy(policy)).round(round).networkUrl(networkUrl);
        if (proxy != null) {
            cb.proxy(proxy.getHostName(), proxy.getPort());
        }
        if (act != null) {
            cb.async(act);
        } else {
            cb.async(context);
        }
    }

    public void async(Context context) {
        String url = getUrl();
        ImageView v = (ImageView) this.v.get();
        if (url == null) {
            showProgress(false);
            setBitmap(url, v, null, false);
            return;
        }
        Bitmap bm = memGet(url);
        if (bm != null) {
            v.setTag(Constants.TAG_URL, url);
            this.status = new AjaxStatus().source(4).done();
            callback(url, bm, this.status);
            return;
        }
        presetBitmap(url, v);
        if (queueMap.containsKey(url)) {
            showProgress(true);
            addQueue(url, v);
            return;
        }
        addQueue(url, v);
        super.async(v.getContext());
    }

    /* access modifiers changed from: protected */
    public boolean isStreamingContent() {
        return !DELAY_WRITE;
    }

    private void addQueue(String url, ImageView iv) {
        WeakHashMap<ImageView, BitmapAjaxCallback> ivs = (WeakHashMap) queueMap.get(url);
        if (ivs != null) {
            ivs.put(iv, this);
        } else if (queueMap.containsKey(url)) {
            ivs = new WeakHashMap();
            ivs.put(iv, this);
            queueMap.put(url, ivs);
        } else {
            queueMap.put(url, null);
        }
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
