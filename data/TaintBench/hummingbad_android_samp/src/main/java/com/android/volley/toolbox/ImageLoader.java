package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ImageLoader {
    private int mBatchResponseDelayMs = 100;
    /* access modifiers changed from: private|final */
    public final HashMap<String, BatchedImageRequest> mBatchedResponses = new HashMap();
    private final ImageCache mCache;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private|final */
    public final HashMap<String, BatchedImageRequest> mInFlightRequests = new HashMap();
    private final RequestQueue mRequestQueue;
    /* access modifiers changed from: private */
    public Runnable mRunnable;

    private class BatchedImageRequest {
        /* access modifiers changed from: private|final */
        public final LinkedList<ImageContainer> mContainers = new LinkedList();
        private VolleyError mError;
        private final Request<?> mRequest;
        /* access modifiers changed from: private */
        public Bitmap mResponseBitmap;

        public BatchedImageRequest(Request<?> request, ImageContainer container) {
            this.mRequest = request;
            this.mContainers.add(container);
        }

        public void setError(VolleyError error) {
            this.mError = error;
        }

        public VolleyError getError() {
            return this.mError;
        }

        public void addContainer(ImageContainer container) {
            this.mContainers.add(container);
        }

        public boolean removeContainerAndCancelIfNecessary(ImageContainer container) {
            this.mContainers.remove(container);
            if (this.mContainers.size() != 0) {
                return false;
            }
            this.mRequest.cancel();
            return true;
        }
    }

    public interface ImageCache {
        Bitmap getBitmap(String str);

        void putBitmap(String str, Bitmap bitmap);
    }

    public class ImageContainer {
        /* access modifiers changed from: private */
        public Bitmap mBitmap;
        private final String mCacheKey;
        /* access modifiers changed from: private|final */
        public final ImageListener mListener;
        private final String mRequestUrl;

        public ImageContainer(Bitmap bitmap, String requestUrl, String cacheKey, ImageListener listener) {
            this.mBitmap = bitmap;
            this.mRequestUrl = requestUrl;
            this.mCacheKey = cacheKey;
            this.mListener = listener;
        }

        public void cancelRequest() {
            if (this.mListener != null) {
                BatchedImageRequest request = (BatchedImageRequest) ImageLoader.this.mInFlightRequests.get(this.mCacheKey);
                if (request == null) {
                    request = (BatchedImageRequest) ImageLoader.this.mBatchedResponses.get(this.mCacheKey);
                    if (request != null) {
                        request.removeContainerAndCancelIfNecessary(this);
                        if (request.mContainers.size() == 0) {
                            ImageLoader.this.mBatchedResponses.remove(this.mCacheKey);
                        }
                    }
                } else if (request.removeContainerAndCancelIfNecessary(this)) {
                    ImageLoader.this.mInFlightRequests.remove(this.mCacheKey);
                }
            }
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public String getRequestUrl() {
            return this.mRequestUrl;
        }
    }

    public interface ImageListener extends ErrorListener {
        void onResponse(ImageContainer imageContainer, boolean z);
    }

    class MyImageRequest extends ImageRequest {
        public MyImageRequest(String url, Listener<Bitmap> listener, int maxWidth, int maxHeight, ScaleType scaleType, Config decodeConfig, ErrorListener errorListener) {
            super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        }

        /* access modifiers changed from: protected */
        public Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
            Response<Bitmap> ret = super.parseNetworkResponse(response);
            if (!(ret == null || ret.cacheEntry == null)) {
                ret.cacheEntry.softTtl = Long.MAX_VALUE;
                ret.cacheEntry.ttl = Long.MAX_VALUE;
            }
            return ret;
        }
    }

    public ImageLoader(RequestQueue queue, ImageCache imageCache) {
        this.mRequestQueue = queue;
        this.mCache = imageCache;
    }

    public ImageCache getImageCache() {
        return this.mCache;
    }

    public static ImageListener getImageListener(final ImageView view, final int defaultImageResId, final int errorImageResId) {
        return new ImageListener() {
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }

    public boolean isCached(String requestUrl, int maxWidth, int maxHeight) {
        return isCached(requestUrl, maxWidth, maxHeight, ScaleType.CENTER_INSIDE);
    }

    public boolean isCached(String requestUrl, int maxWidth, int maxHeight, ScaleType scaleType) {
        throwIfNotOnMainThread();
        return this.mCache.getBitmap(getCacheKey(requestUrl, maxWidth, maxHeight, scaleType)) != null;
    }

    public ImageContainer get(String requestUrl, ImageListener listener) {
        return get(requestUrl, listener, 0, 0);
    }

    public ImageContainer get(String requestUrl, ImageListener imageListener, int maxWidth, int maxHeight) {
        return get(requestUrl, imageListener, maxWidth, maxHeight, ScaleType.CENTER_INSIDE);
    }

    public ImageContainer get(String requestUrl, ImageListener imageListener, int maxWidth, int maxHeight, ScaleType scaleType) {
        throwIfNotOnMainThread();
        String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight, scaleType);
        Bitmap cachedBitmap = this.mCache.getBitmap(cacheKey);
        if (cachedBitmap != null) {
            ImageContainer container = new ImageContainer(cachedBitmap, requestUrl, null, null);
            imageListener.onResponse(container, true);
            return container;
        }
        ImageContainer imageContainer = new ImageContainer(null, requestUrl, cacheKey, imageListener);
        imageListener.onResponse(imageContainer, true);
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(imageContainer);
            return imageContainer;
        }
        Request<Bitmap> newRequest = makeImageRequest(requestUrl, maxWidth, maxHeight, scaleType, cacheKey);
        this.mRequestQueue.add(newRequest);
        this.mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest, imageContainer));
        return imageContainer;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x005b A:{SYNTHETIC, Splitter:B:32:0x005b} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004f A:{SYNTHETIC, Splitter:B:26:0x004f} */
    public java.lang.String getProcessName() {
        /*
        r7 = this;
        r0 = new java.io.File;
        r5 = "/proc/self/cmdline";
        r0.<init>(r5);
        r5 = r0.exists();
        if (r5 == 0) goto L_0x0041;
    L_0x000d:
        r5 = r0.isDirectory();
        if (r5 != 0) goto L_0x0041;
    L_0x0013:
        r3 = 0;
        r4 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0049 }
        r5 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0049 }
        r6 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0049 }
        r6.<init>(r0);	 Catch:{ Exception -> 0x0049 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x0049 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0049 }
        r2 = r4.readLine();	 Catch:{ Exception -> 0x0067, all -> 0x0064 }
        r5 = android.text.TextUtils.isEmpty(r2);	 Catch:{ Exception -> 0x0067, all -> 0x0064 }
        if (r5 != 0) goto L_0x003c;
    L_0x002d:
        r5 = r2.trim();	 Catch:{ Exception -> 0x0067, all -> 0x0064 }
        if (r4 == 0) goto L_0x0036;
    L_0x0033:
        r4.close();	 Catch:{ Exception -> 0x0037 }
    L_0x0036:
        return r5;
    L_0x0037:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0036;
    L_0x003c:
        if (r4 == 0) goto L_0x0041;
    L_0x003e:
        r4.close();	 Catch:{ Exception -> 0x0044 }
    L_0x0041:
        r5 = "cccc";
        goto L_0x0036;
    L_0x0044:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0041;
    L_0x0049:
        r1 = move-exception;
    L_0x004a:
        r1.printStackTrace();	 Catch:{ all -> 0x0058 }
        if (r3 == 0) goto L_0x0041;
    L_0x004f:
        r3.close();	 Catch:{ Exception -> 0x0053 }
        goto L_0x0041;
    L_0x0053:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0041;
    L_0x0058:
        r5 = move-exception;
    L_0x0059:
        if (r3 == 0) goto L_0x005e;
    L_0x005b:
        r3.close();	 Catch:{ Exception -> 0x005f }
    L_0x005e:
        throw r5;
    L_0x005f:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x005e;
    L_0x0064:
        r5 = move-exception;
        r3 = r4;
        goto L_0x0059;
    L_0x0067:
        r1 = move-exception;
        r3 = r4;
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.ImageLoader.getProcessName():java.lang.String");
    }

    /* access modifiers changed from: protected */
    public Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ScaleType scaleType, final String cacheKey) {
        return new MyImageRequest(requestUrl, new Listener<Bitmap>() {
            public void onResponse(Bitmap response) {
                ImageLoader.this.onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight, scaleType, Config.RGB_565, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                ImageLoader.this.onGetImageError(cacheKey, error);
            }
        });
    }

    public void setBatchedResponseDelay(int newBatchedResponseDelayMs) {
        this.mBatchResponseDelayMs = newBatchedResponseDelayMs;
    }

    /* access modifiers changed from: protected */
    public void onGetImageSuccess(String cacheKey, Bitmap response) {
        this.mCache.putBitmap(cacheKey, response);
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        if (request != null) {
            request.mResponseBitmap = response;
            batchResponse(cacheKey, request);
        }
    }

    /* access modifiers changed from: protected */
    public void onGetImageError(String cacheKey, VolleyError error) {
        BatchedImageRequest request = (BatchedImageRequest) this.mInFlightRequests.remove(cacheKey);
        if (request != null) {
            request.setError(error);
            batchResponse(cacheKey, request);
        }
    }

    private void batchResponse(String cacheKey, BatchedImageRequest request) {
        this.mBatchedResponses.put(cacheKey, request);
        if (this.mRunnable == null) {
            this.mRunnable = new Runnable() {
                public void run() {
                    for (BatchedImageRequest bir : ImageLoader.this.mBatchedResponses.values()) {
                        Iterator i$ = bir.mContainers.iterator();
                        while (i$.hasNext()) {
                            ImageContainer container = (ImageContainer) i$.next();
                            if (container.mListener != null) {
                                if (bir.getError() == null) {
                                    container.mBitmap = bir.mResponseBitmap;
                                    container.mListener.onResponse(container, false);
                                } else {
                                    container.mListener.onErrorResponse(bir.getError());
                                }
                            }
                        }
                    }
                    ImageLoader.this.mBatchedResponses.clear();
                    ImageLoader.this.mRunnable = null;
                }
            };
            this.mHandler.postDelayed(this.mRunnable, (long) this.mBatchResponseDelayMs);
        }
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }
    }

    public static String getCacheKey(String url, int maxWidth, int maxHeight, ScaleType scaleType) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight).append("#S").append(scaleType.ordinal()).append(url).toString();
    }
}
