package com.mopub.nativeads;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.duapps.ad.AdError;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener;
import com.mopub.network.AdResponse;
import java.util.ArrayList;
import java.util.List;

class NativeAdSource {
    private static final int CACHE_LIMIT = 1;
    private static final int EXPIRATION_TIME_MILLISECONDS = 900000;
    private static final int MAXIMUM_RETRY_TIME_MILLISECONDS = 300000;
    @VisibleForTesting
    static final int[] RETRY_TIME_ARRAY_MILLISECONDS = new int[]{1000, AdError.TIME_OUT_CODE, 5000, 25000, 60000, MAXIMUM_RETRY_TIME_MILLISECONDS};
    @NonNull
    private final AdRendererRegistry mAdRendererRegistry;
    /* access modifiers changed from: private */
    @Nullable
    public AdSourceListener mAdSourceListener;
    @VisibleForTesting
    int mCurrentRetries;
    /* access modifiers changed from: private */
    @Nullable
    public MoPubNative mMoPubNative;
    @NonNull
    private final MoPubNativeNetworkListener mMoPubNativeNetworkListener;
    /* access modifiers changed from: private|final */
    @NonNull
    public final List<TimestampWrapper<NativeAd>> mNativeAdCache;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Handler mReplenishCacheHandler;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Runnable mReplenishCacheRunnable;
    @VisibleForTesting
    boolean mRequestInFlight;
    @Nullable
    private RequestParameters mRequestParameters;
    @VisibleForTesting
    boolean mRetryInFlight;
    @VisibleForTesting
    int mSequenceNumber;
    /* access modifiers changed from: private */
    public MoPubNativeNetworkListener outerNetworkListener;

    interface AdSourceListener {
        void onAdsAvailable();
    }

    public void setOuterNetworkListener(@NonNull MoPubNativeNetworkListener outerNetworkListener) {
        this.outerNetworkListener = outerNetworkListener;
    }

    NativeAdSource() {
        this(new ArrayList(1), new Handler(), new AdRendererRegistry());
    }

    @VisibleForTesting
    NativeAdSource(@NonNull List<TimestampWrapper<NativeAd>> nativeAdCache, @NonNull Handler replenishCacheHandler, @NonNull AdRendererRegistry adRendererRegistry) {
        this.mNativeAdCache = nativeAdCache;
        this.mReplenishCacheHandler = replenishCacheHandler;
        this.mReplenishCacheRunnable = new Runnable() {
            public void run() {
                NativeAdSource.this.mRetryInFlight = false;
                NativeAdSource.this.replenishCache();
            }
        };
        this.mAdRendererRegistry = adRendererRegistry;
        this.mMoPubNativeNetworkListener = new MoPubNativeNetworkListener() {
            public void onNativeLoad(@NonNull NativeAd nativeAd, AdResponse adResponse) {
                if (NativeAdSource.this.mMoPubNative != null) {
                    NativeAdSource.this.mRequestInFlight = false;
                    NativeAdSource nativeAdSource = NativeAdSource.this;
                    nativeAdSource.mSequenceNumber++;
                    NativeAdSource.this.resetRetryTime();
                    NativeAdSource.this.mNativeAdCache.add(new TimestampWrapper(nativeAd));
                    if (NativeAdSource.this.mNativeAdCache.size() == 1 && NativeAdSource.this.mAdSourceListener != null) {
                        NativeAdSource.this.mAdSourceListener.onAdsAvailable();
                    }
                    NativeAdSource.this.replenishCache();
                    if (NativeAdSource.this.outerNetworkListener != null) {
                        NativeAdSource.this.outerNetworkListener.onNativeLoad(nativeAd, adResponse);
                    }
                }
            }

            public void onNativeFail(NativeErrorCode errorCode) {
                NativeAdSource.this.mRequestInFlight = false;
                if (NativeAdSource.this.mCurrentRetries >= NativeAdSource.RETRY_TIME_ARRAY_MILLISECONDS.length - 1) {
                    NativeAdSource.this.resetRetryTime();
                    return;
                }
                NativeAdSource.this.updateRetryTime();
                NativeAdSource.this.mRetryInFlight = true;
                NativeAdSource.this.mReplenishCacheHandler.postDelayed(NativeAdSource.this.mReplenishCacheRunnable, (long) NativeAdSource.this.getRetryTime());
                if (NativeAdSource.this.outerNetworkListener != null) {
                    NativeAdSource.this.outerNetworkListener.onNativeFail(errorCode);
                }
            }
        };
        this.mSequenceNumber = 0;
        resetRetryTime();
    }

    /* access modifiers changed from: 0000 */
    public int getAdRendererCount() {
        return this.mAdRendererRegistry.getAdRendererCount();
    }

    public int getViewTypeForAd(@NonNull NativeAd nativeAd) {
        return this.mAdRendererRegistry.getViewTypeForAd(nativeAd);
    }

    /* access modifiers changed from: 0000 */
    public void registerAdRenderer(@NonNull MoPubAdRenderer moPubNativeAdRenderer) {
        this.mAdRendererRegistry.registerAdRenderer(moPubNativeAdRenderer);
        if (this.mMoPubNative != null) {
            this.mMoPubNative.registerAdRenderer(moPubNativeAdRenderer);
        }
    }

    @Nullable
    public MoPubAdRenderer getAdRendererForViewType(int viewType) {
        return this.mAdRendererRegistry.getRendererForViewType(viewType);
    }

    /* access modifiers changed from: 0000 */
    public void setAdSourceListener(@Nullable AdSourceListener adSourceListener) {
        this.mAdSourceListener = adSourceListener;
    }

    /* access modifiers changed from: 0000 */
    public void loadAds(@NonNull Activity activity, @NonNull String adUnitId, RequestParameters requestParameters) {
        loadAds(requestParameters, new MoPubNative(activity, adUnitId, this.mMoPubNativeNetworkListener));
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void loadAds(RequestParameters requestParameters, MoPubNative moPubNative) {
        clear();
        for (MoPubAdRenderer registerAdRenderer : this.mAdRendererRegistry.getRendererIterable()) {
            moPubNative.registerAdRenderer(registerAdRenderer);
        }
        this.mRequestParameters = requestParameters;
        this.mMoPubNative = moPubNative;
        replenishCache();
    }

    /* access modifiers changed from: 0000 */
    public void clear() {
        if (this.mMoPubNative != null) {
            this.mMoPubNative.destroy();
            this.mMoPubNative = null;
        }
        this.mRequestParameters = null;
        for (TimestampWrapper timestampWrapper : this.mNativeAdCache) {
            ((NativeAd) timestampWrapper.mInstance).destroy();
        }
        this.mNativeAdCache.clear();
        this.mReplenishCacheHandler.removeMessages(0);
        this.mRequestInFlight = false;
        this.mSequenceNumber = 0;
        resetRetryTime();
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public NativeAd dequeueAd() {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (!(this.mRequestInFlight || this.mRetryInFlight)) {
            this.mReplenishCacheHandler.post(this.mReplenishCacheRunnable);
        }
        while (!this.mNativeAdCache.isEmpty()) {
            TimestampWrapper timestampWrapper = (TimestampWrapper) this.mNativeAdCache.remove(0);
            if (uptimeMillis - timestampWrapper.mCreatedTimestamp < 900000) {
                return (NativeAd) timestampWrapper.mInstance;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void updateRetryTime() {
        if (this.mCurrentRetries < RETRY_TIME_ARRAY_MILLISECONDS.length - 1) {
            this.mCurrentRetries++;
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void resetRetryTime() {
        this.mCurrentRetries = 0;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public int getRetryTime() {
        if (this.mCurrentRetries >= RETRY_TIME_ARRAY_MILLISECONDS.length) {
            this.mCurrentRetries = RETRY_TIME_ARRAY_MILLISECONDS.length - 1;
        }
        return RETRY_TIME_ARRAY_MILLISECONDS[this.mCurrentRetries];
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void replenishCache() {
        if (!this.mRequestInFlight && this.mMoPubNative != null && this.mNativeAdCache.size() <= 0) {
            this.mRequestInFlight = true;
            this.mMoPubNative.makeRequest(this.mRequestParameters, Integer.valueOf(this.mSequenceNumber));
        }
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setMoPubNative(MoPubNative moPubNative) {
        this.mMoPubNative = moPubNative;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @NonNull
    @VisibleForTesting
    public MoPubNativeNetworkListener getMoPubNativeNetworkListener() {
        return this.mMoPubNativeNetworkListener;
    }
}
