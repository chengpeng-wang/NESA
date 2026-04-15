package com.mopub.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.webkit.WebView;
import com.mopub.common.ClientMetadata;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.DeviceUtils;
import com.mopub.volley.toolbox.BasicNetwork;
import com.mopub.volley.toolbox.DiskBasedCache;
import com.mopub.volley.toolbox.ImageLoader;
import com.mopub.volley.toolbox.ImageLoader.ImageCache;
import java.io.File;

public class Networking {
    @VisibleForTesting
    static final String CACHE_DIRECTORY_NAME = "mopub-volley-cache";
    private static final String DEFAULT_USER_AGENT = System.getProperty("http.agent");
    private static volatile MaxWidthImageLoader sMaxWidthImageLoader;
    private static volatile MoPubRequestQueue sRequestQueue;
    public static boolean sUseHttps = false;
    private static volatile String sUserAgent;

    @Nullable
    public static MoPubRequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    @NonNull
    public static MoPubRequestQueue getRequestQueue(@NonNull Context context) {
        MoPubRequestQueue moPubRequestQueue = sRequestQueue;
        if (moPubRequestQueue == null) {
            synchronized (Networking.class) {
                moPubRequestQueue = sRequestQueue;
                if (moPubRequestQueue == null) {
                    BasicNetwork basicNetwork = new BasicNetwork(new RequestQueueHttpStack(getUserAgent(context.getApplicationContext()), new PlayServicesUrlRewriter(ClientMetadata.getInstance(context).getDeviceId(), context), CustomSSLSocketFactory.getDefault(10000)));
                    File file = new File(context.getCacheDir().getPath() + File.separator + CACHE_DIRECTORY_NAME);
                    moPubRequestQueue = new MoPubRequestQueue(new DiskBasedCache(file, (int) DeviceUtils.diskCacheSizeBytes(file, 10485760)), basicNetwork);
                    sRequestQueue = moPubRequestQueue;
                    moPubRequestQueue.start();
                }
            }
        }
        return moPubRequestQueue;
    }

    @NonNull
    public static ImageLoader getImageLoader(@NonNull Context context) {
        ImageLoader imageLoader = sMaxWidthImageLoader;
        if (imageLoader == null) {
            synchronized (Networking.class) {
                imageLoader = sMaxWidthImageLoader;
                if (imageLoader == null) {
                    MoPubRequestQueue requestQueue = getRequestQueue(context);
                    final AnonymousClass1 anonymousClass1 = new LruCache<String, Bitmap>(DeviceUtils.memoryCacheSizeBytes(context)) {
                        /* access modifiers changed from: protected|final */
                        public final int sizeOf(String key, Bitmap value) {
                            if (value != null) {
                                return value.getRowBytes() * value.getHeight();
                            }
                            return super.sizeOf(key, value);
                        }
                    };
                    imageLoader = new MaxWidthImageLoader(requestQueue, context, new ImageCache() {
                        public final Bitmap getBitmap(String key) {
                            return (Bitmap) anonymousClass1.get(key);
                        }

                        public final void putBitmap(String key, Bitmap bitmap) {
                            anonymousClass1.put(key, bitmap);
                        }
                    });
                    sMaxWidthImageLoader = imageLoader;
                }
            }
        }
        return imageLoader;
    }

    @NonNull
    public static String getUserAgent(@NonNull Context context) {
        Preconditions.checkNotNull(context);
        String str = sUserAgent;
        if (str == null) {
            synchronized (Networking.class) {
                str = sUserAgent;
                if (str == null) {
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        str = new WebView(context).getSettings().getUserAgentString();
                    } else {
                        str = DEFAULT_USER_AGENT;
                    }
                    sUserAgent = str;
                }
            }
        }
        return str;
    }

    @NonNull
    public static String getCachedUserAgent() {
        String str = sUserAgent;
        if (str == null) {
            return DEFAULT_USER_AGENT;
        }
        return str;
    }

    @VisibleForTesting
    public static synchronized void clearForTesting() {
        synchronized (Networking.class) {
            sRequestQueue = null;
            sMaxWidthImageLoader = null;
            sUserAgent = null;
        }
    }

    @VisibleForTesting
    public static synchronized void setRequestQueueForTesting(MoPubRequestQueue queue) {
        synchronized (Networking.class) {
            sRequestQueue = queue;
        }
    }

    @VisibleForTesting
    public static synchronized void setImageLoaderForTesting(MaxWidthImageLoader imageLoader) {
        synchronized (Networking.class) {
            sMaxWidthImageLoader = imageLoader;
        }
    }

    @VisibleForTesting
    public static synchronized void setUserAgentForTesting(String userAgent) {
        synchronized (Networking.class) {
            sUserAgent = userAgent;
        }
    }

    public static void useHttps(boolean useHttps) {
        sUseHttps = useHttps;
    }

    public static boolean useHttps() {
        return sUseHttps;
    }
}
