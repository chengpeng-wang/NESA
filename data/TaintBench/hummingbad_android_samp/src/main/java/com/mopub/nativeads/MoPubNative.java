package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.AdFormat;
import com.mopub.common.Constants;
import com.mopub.common.GpsHelper;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.ManifestUtils;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import com.mopub.network.AdRequest;
import com.mopub.network.AdRequest.Listener;
import com.mopub.network.AdResponse;
import com.mopub.network.MoPubNetworkError;
import com.mopub.network.Networking;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.VolleyError;
import com.picksinit.ErrorInfo;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

public class MoPubNative {
    static final MoPubNativeNetworkListener EMPTY_NETWORK_LISTENER = new MoPubNativeNetworkListener() {
        public final void onNativeLoad(@NonNull NativeAd nativeAd, AdResponse adResponse) {
            nativeAd.destroy();
        }

        public final void onNativeFail(NativeErrorCode errorCode) {
        }
    };
    @NonNull
    private final WeakReference<Activity> mActivity;
    @NonNull
    AdRendererRegistry mAdRendererRegistry;
    /* access modifiers changed from: private|final */
    @NonNull
    public final String mAdUnitId;
    @NonNull
    private Map<String, Object> mLocalExtras;
    /* access modifiers changed from: private */
    @NonNull
    public MoPubNativeNetworkListener mMoPubNativeNetworkListener;
    @Nullable
    private AdRequest mNativeRequest;
    @NonNull
    private final Listener mVolleyListener;

    public interface MoPubNativeNetworkListener {
        void onNativeFail(NativeErrorCode nativeErrorCode);

        void onNativeLoad(NativeAd nativeAd, AdResponse adResponse);
    }

    public MoPubNative(@NonNull Activity activity, @NonNull String adUnitId, @NonNull MoPubNativeNetworkListener moPubNativeNetworkListener) {
        this(activity, adUnitId, new AdRendererRegistry(), moPubNativeNetworkListener);
    }

    @VisibleForTesting
    public MoPubNative(@NonNull Activity activity, @NonNull String adUnitId, @NonNull AdRendererRegistry adRendererRegistry, @NonNull MoPubNativeNetworkListener moPubNativeNetworkListener) {
        this.mLocalExtras = new TreeMap();
        Preconditions.checkNotNull(activity, "Activity may not be null.");
        Preconditions.checkNotNull(adUnitId, "AdUnitId may not be null.");
        Preconditions.checkNotNull(adRendererRegistry, "AdRendererRegistry may not be null.");
        Preconditions.checkNotNull(moPubNativeNetworkListener, "MoPubNativeNetworkListener may not be null.");
        ManifestUtils.checkNativeActivitiesDeclared(activity);
        this.mActivity = new WeakReference(activity);
        this.mAdUnitId = adUnitId;
        this.mMoPubNativeNetworkListener = moPubNativeNetworkListener;
        this.mAdRendererRegistry = adRendererRegistry;
        this.mVolleyListener = new Listener() {
            public void onSuccess(@NonNull AdResponse response) {
                MoPubNative.this.onAdLoad(response);
            }

            public void onErrorResponse(@NonNull VolleyError volleyError) {
                MoPubNative.this.onAdError(volleyError);
            }
        };
        GpsHelper.fetchAdvertisingInfoAsync(activity, null);
    }

    public void registerAdRenderer(MoPubAdRenderer moPubAdRenderer) {
        this.mAdRendererRegistry.registerAdRenderer(moPubAdRenderer);
    }

    public void destroy() {
        this.mActivity.clear();
        if (this.mNativeRequest != null) {
            this.mNativeRequest.cancel();
            this.mNativeRequest = null;
        }
        this.mMoPubNativeNetworkListener = EMPTY_NETWORK_LISTENER;
    }

    public void setLocalExtras(@Nullable Map<String, Object> localExtras) {
        if (localExtras == null) {
            this.mLocalExtras = new TreeMap();
        } else {
            this.mLocalExtras = new TreeMap(localExtras);
        }
    }

    public void makeRequest() {
        makeRequest(null);
    }

    public void makeRequest(@Nullable RequestParameters requestParameters) {
        makeRequest(requestParameters, null);
    }

    public void makeRequest(@Nullable RequestParameters requestParameters, @Nullable Integer sequenceNumber) {
        Activity activityOrDestroy = getActivityOrDestroy();
        if (activityOrDestroy != null) {
            if (DeviceUtils.isNetworkAvailable(activityOrDestroy)) {
                loadNativeAd(requestParameters, sequenceNumber);
            } else {
                this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.CONNECTION_ERROR);
            }
        }
    }

    private void loadNativeAd(@Nullable RequestParameters requestParameters, @Nullable Integer sequenceNumber) {
        Activity activityOrDestroy = getActivityOrDestroy();
        if (activityOrDestroy != null) {
            NativeUrlGenerator withRequest = new NativeUrlGenerator(activityOrDestroy).withAdUnitId(this.mAdUnitId).withRequest(requestParameters);
            if (sequenceNumber != null) {
                withRequest.withSequenceNumber(sequenceNumber.intValue());
            }
            String generateUrlString = withRequest.generateUrlString(Constants.HOST);
            if (generateUrlString != null) {
                MoPubLog.d("Loading ad from: " + generateUrlString);
            }
            requestNativeAd(generateUrlString);
        }
    }

    /* access modifiers changed from: 0000 */
    public void requestNativeAd(@Nullable String endpointUrl) {
        Activity activityOrDestroy = getActivityOrDestroy();
        if (activityOrDestroy != null) {
            if (endpointUrl == null) {
                this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.INVALID_REQUEST_URL);
                return;
            }
            this.mNativeRequest = new AdRequest(endpointUrl, AdFormat.NATIVE, this.mAdUnitId, activityOrDestroy, this.mVolleyListener);
            Networking.getRequestQueue(activityOrDestroy).add(this.mNativeRequest);
        }
    }

    /* access modifiers changed from: private */
    public void onAdLoad(@NonNull final AdResponse response) {
        Activity activityOrDestroy = getActivityOrDestroy();
        if (activityOrDestroy != null) {
            CustomEventNativeAdapter.loadNativeAd(activityOrDestroy, this.mLocalExtras, response, new CustomEventNativeListener() {
                public void onNativeAdLoaded(@NonNull BaseNativeAd nativeAd) {
                    Activity activityOrDestroy = MoPubNative.this.getActivityOrDestroy();
                    if (activityOrDestroy != null) {
                        MoPubNative.this.mMoPubNativeNetworkListener.onNativeLoad(new NativeAd(activityOrDestroy, response.getImpressionTrackingUrl(), response.getClickTrackingUrl(), MoPubNative.this.mAdUnitId, nativeAd, MoPubNative.this.mAdRendererRegistry.getRendererForAd(nativeAd)), response);
                    }
                }

                public void onNativeAdFailed(NativeErrorCode errorCode) {
                    MoPubNative.this.requestNativeAd(response.getFailoverUrl());
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void onAdError(@NonNull VolleyError volleyError) {
        MoPubLog.d("Native ad request failed.", volleyError);
        if (volleyError instanceof MoPubNetworkError) {
            switch (((MoPubNetworkError) volleyError).getReason()) {
                case BAD_BODY:
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.INVALID_RESPONSE);
                    return;
                case BAD_HEADER_DATA:
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.INVALID_RESPONSE);
                    return;
                case WARMING_UP:
                    MoPubLog.c(MoPubErrorCode.WARMUP.toString());
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.EMPTY_AD_RESPONSE);
                    return;
                case NO_FILL:
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.EMPTY_AD_RESPONSE);
                    return;
                default:
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.UNSPECIFIED);
                    return;
            }
        }
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null && networkResponse.statusCode >= ErrorInfo.ERROR_CODE_PARAMS && networkResponse.statusCode < 600) {
            this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
        } else if (networkResponse != null || DeviceUtils.isNetworkAvailable((Context) this.mActivity.get())) {
            this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.UNSPECIFIED);
        } else {
            MoPubLog.c(String.valueOf(MoPubErrorCode.NO_CONNECTION.toString()));
            this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.CONNECTION_ERROR);
        }
    }

    /* access modifiers changed from: 0000 */
    public Activity getActivityOrDestroy() {
        Activity activity = (Activity) this.mActivity.get();
        if (activity == null) {
            destroy();
            MoPubLog.d("Weak reference to Activity in MoPubNative became null. This instance of MoPubNative is destroyed and No more requests will be processed.");
        }
        return activity;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @NonNull
    @VisibleForTesting
    public MoPubNativeNetworkListener getMoPubNativeNetworkListener() {
        return this.mMoPubNativeNetworkListener;
    }
}
