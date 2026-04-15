package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.mopub.common.AdReport;
import com.mopub.common.ClientMetadata;
import com.mopub.common.Constants;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mraid.MraidNativeCommandHandler;
import com.mopub.network.AdRequest;
import com.mopub.network.AdRequest.Listener;
import com.mopub.network.AdResponse;
import com.mopub.network.MoPubNetworkError;
import com.mopub.network.Networking;
import com.mopub.network.TrackingRequest;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.VolleyError;
import com.picksinit.ErrorInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class AdViewController {
    static final double BACKOFF_FACTOR = 1.5d;
    static final int DEFAULT_REFRESH_TIME_MILLISECONDS = 60000;
    static final int MAX_REFRESH_TIME_MILLISECONDS = 600000;
    private static final LayoutParams WRAP_AND_CENTER_LAYOUT_PARAMS = new LayoutParams(-2, -2, 17);
    private static final WeakHashMap<View, Boolean> sViewShouldHonorServerDimensions = new WeakHashMap();
    @Nullable
    private AdRequest mActiveRequest;
    @NonNull
    private final Listener mAdListener;
    @Nullable
    private AdResponse mAdResponse;
    @Nullable
    private String mAdUnitId;
    private boolean mAdWasLoaded;
    private boolean mAutoRefreshEnabled = true;
    @VisibleForTesting
    int mBackoffPower = 1;
    private final long mBroadcastIdentifier;
    @Nullable
    private Context mContext;
    private Handler mHandler;
    private boolean mIsDestroyed;
    private boolean mIsImageLoadSucc = false;
    private boolean mIsLoading;
    private boolean mIsTesting;
    private String mKeywords;
    private Map<String, Object> mLocalExtras = new HashMap();
    private Location mLocation;
    @Nullable
    private MoPubView mMoPubView;
    private boolean mPreviousAutoRefreshSetting = true;
    private final Runnable mRefreshRunnable;
    @Nullable
    private Integer mRefreshTimeMillis;
    private int mTimeoutMilliseconds;
    private String mUrl;
    @Nullable
    private WebViewAdUrlGenerator mUrlGenerator;

    public static void setShouldHonorServerDimensions(View view) {
        sViewShouldHonorServerDimensions.put(view, Boolean.valueOf(true));
    }

    private static boolean getShouldHonorServerDimensions(View view) {
        return sViewShouldHonorServerDimensions.get(view) != null;
    }

    public AdViewController(@NonNull Context context, @NonNull MoPubView view) {
        this.mContext = context;
        this.mMoPubView = view;
        this.mTimeoutMilliseconds = -1;
        this.mBroadcastIdentifier = Utils.generateUniqueId();
        this.mUrlGenerator = new WebViewAdUrlGenerator(this.mContext.getApplicationContext(), MraidNativeCommandHandler.isStorePictureSupported(this.mContext));
        this.mAdListener = new Listener() {
            public void onSuccess(AdResponse response) {
                AdViewController.this.onAdLoadSuccess(response);
            }

            public void onErrorResponse(VolleyError volleyError) {
                AdViewController.this.onAdLoadError(volleyError);
            }
        };
        this.mRefreshRunnable = new Runnable() {
            public void run() {
                AdViewController.this.internalLoadAd();
            }
        };
        this.mRefreshTimeMillis = Integer.valueOf(DEFAULT_REFRESH_TIME_MILLISECONDS);
        this.mHandler = new Handler();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void onAdLoadSuccess(@NonNull AdResponse adResponse) {
        this.mBackoffPower = 1;
        this.mAdResponse = adResponse;
        this.mTimeoutMilliseconds = this.mAdResponse.getAdTimeoutMillis() == null ? this.mTimeoutMilliseconds : this.mAdResponse.getAdTimeoutMillis().intValue();
        this.mRefreshTimeMillis = this.mAdResponse.getRefreshTimeMillis();
        setNotLoading();
        AdLoader fromAdResponse = AdLoader.fromAdResponse(this.mAdResponse, this);
        if (fromAdResponse != null) {
            fromAdResponse.load();
        }
        scheduleRefreshTimerIfEnabled();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void onAdLoadError(VolleyError error) {
        if (error instanceof MoPubNetworkError) {
            MoPubNetworkError moPubNetworkError = (MoPubNetworkError) error;
            if (moPubNetworkError.getRefreshTimeMillis() != null) {
                this.mRefreshTimeMillis = moPubNetworkError.getRefreshTimeMillis();
            }
        }
        MoPubErrorCode errorCodeFromVolleyError = getErrorCodeFromVolleyError(error, this.mContext);
        if (errorCodeFromVolleyError == MoPubErrorCode.SERVER_ERROR) {
            this.mBackoffPower++;
        }
        setNotLoading();
        adDidFail(errorCodeFromVolleyError);
    }

    @NonNull
    @VisibleForTesting
    static MoPubErrorCode getErrorCodeFromVolleyError(@NonNull VolleyError error, @Nullable Context context) {
        NetworkResponse networkResponse = error.networkResponse;
        if (error instanceof MoPubNetworkError) {
            switch (((MoPubNetworkError) error).getReason()) {
                case WARMING_UP:
                    return MoPubErrorCode.WARMUP;
                case NO_FILL:
                    return MoPubErrorCode.NO_FILL;
                default:
                    return MoPubErrorCode.UNSPECIFIED;
            }
        } else if (networkResponse == null) {
            if (DeviceUtils.isNetworkAvailable(context)) {
                return MoPubErrorCode.UNSPECIFIED;
            }
            return MoPubErrorCode.NO_CONNECTION;
        } else if (error.networkResponse.statusCode >= ErrorInfo.ERROR_CODE_NO_FILL) {
            return MoPubErrorCode.SERVER_ERROR;
        } else {
            return MoPubErrorCode.UNSPECIFIED;
        }
    }

    @Nullable
    public MoPubView getMoPubView() {
        return this.mMoPubView;
    }

    public void loadAd() {
        this.mBackoffPower = 1;
        this.mIsImageLoadSucc = false;
        internalLoadAd();
    }

    /* access modifiers changed from: private */
    public void internalLoadAd() {
        this.mAdWasLoaded = true;
        if (TextUtils.isEmpty(this.mAdUnitId)) {
            MoPubLog.d("Can't load an ad in this ad view because the ad unit ID is not set. Did you forget to call setAdUnitId()?");
        } else if (isNetworkAvailable()) {
            loadNonJavascript(generateAdUrl());
        } else {
            MoPubLog.d("Can't load an ad because there is no network connectivity.");
            scheduleRefreshTimerIfEnabled();
        }
    }

    /* access modifiers changed from: 0000 */
    public void loadNonJavascript(String url) {
        if (url != null) {
            MoPubLog.d("Loading url: " + url);
            if (!this.mIsLoading) {
                this.mUrl = url;
                this.mIsLoading = true;
                fetchAd(this.mUrl);
            } else if (!TextUtils.isEmpty(this.mAdUnitId)) {
                MoPubLog.i("Already loading an ad for " + this.mAdUnitId + ", wait to finish.");
            }
        }
    }

    public void reload() {
        MoPubLog.d("Reload ad: " + this.mUrl);
        loadNonJavascript(this.mUrl);
    }

    /* access modifiers changed from: 0000 */
    public void loadFailUrl(MoPubErrorCode errorCode) {
        this.mIsLoading = false;
        Log.v("MoPub", "MoPubErrorCode: " + (errorCode == null ? "" : errorCode.toString()));
        CharSequence failoverUrl = this.mAdResponse == null ? "" : this.mAdResponse.getFailoverUrl();
        if (TextUtils.isEmpty(failoverUrl)) {
            adDidFail(MoPubErrorCode.NO_FILL);
            return;
        }
        MoPubLog.d("Loading failover url: " + failoverUrl);
        loadNonJavascript(failoverUrl);
    }

    /* access modifiers changed from: 0000 */
    public void setNotLoading() {
        this.mIsLoading = false;
        if (this.mActiveRequest != null) {
            if (!this.mActiveRequest.isCanceled()) {
                this.mActiveRequest.cancel();
            }
            this.mActiveRequest = null;
        }
    }

    public String getKeywords() {
        return this.mKeywords;
    }

    public void setKeywords(String keywords) {
        this.mKeywords = keywords;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public String getAdUnitId() {
        return this.mAdUnitId;
    }

    public void setAdUnitId(@NonNull String adUnitId) {
        this.mAdUnitId = adUnitId;
    }

    public long getBroadcastIdentifier() {
        return this.mBroadcastIdentifier;
    }

    public void setTimeout(int milliseconds) {
        this.mTimeoutMilliseconds = milliseconds;
    }

    public int getAdWidth() {
        if (this.mAdResponse == null || this.mAdResponse.getWidth() == null) {
            return 0;
        }
        return this.mAdResponse.getWidth().intValue();
    }

    public int getAdHeight() {
        if (this.mAdResponse == null || this.mAdResponse.getHeight() == null) {
            return 0;
        }
        return this.mAdResponse.getHeight().intValue();
    }

    public boolean getAutorefreshEnabled() {
        return this.mAutoRefreshEnabled;
    }

    /* access modifiers changed from: 0000 */
    public void pauseRefresh() {
        this.mPreviousAutoRefreshSetting = this.mAutoRefreshEnabled;
        setAutorefreshEnabled(false);
    }

    /* access modifiers changed from: 0000 */
    public void unpauseRefresh() {
        setAutorefreshEnabled(this.mPreviousAutoRefreshSetting);
    }

    /* access modifiers changed from: 0000 */
    public void forceSetAutorefreshEnabled(boolean enabled) {
        this.mPreviousAutoRefreshSetting = enabled;
        setAutorefreshEnabled(enabled);
    }

    private void setAutorefreshEnabled(boolean enabled) {
        Object obj = (!this.mAdWasLoaded || this.mAutoRefreshEnabled == enabled) ? null : 1;
        if (obj != null) {
            MoPubLog.d("Refresh " + (enabled ? "enabled" : "disabled") + " for ad unit (" + this.mAdUnitId + ").");
        }
        this.mAutoRefreshEnabled = enabled;
        if (this.mAdWasLoaded && this.mAutoRefreshEnabled) {
            scheduleRefreshTimerIfEnabled();
        } else if (!this.mAutoRefreshEnabled) {
            cancelRefreshTimer();
        }
    }

    @Nullable
    public AdReport getAdReport() {
        if (this.mAdUnitId == null || this.mAdResponse == null) {
            return null;
        }
        return new AdReport(this.mAdUnitId, ClientMetadata.getInstance(this.mContext), this.mAdResponse);
    }

    public boolean getTesting() {
        return this.mIsTesting;
    }

    public void setTesting(boolean enabled) {
        this.mIsTesting = enabled;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDestroyed() {
        return this.mIsDestroyed;
    }

    /* access modifiers changed from: 0000 */
    public void cleanup() {
        if (!this.mIsDestroyed) {
            if (this.mActiveRequest != null) {
                this.mActiveRequest.cancel();
                this.mActiveRequest = null;
            }
            setAutorefreshEnabled(false);
            cancelRefreshTimer();
            this.mMoPubView = null;
            this.mContext = null;
            this.mUrlGenerator = null;
            this.mIsDestroyed = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public Integer getAdTimeoutDelay() {
        return this.mAdResponse == null ? null : this.mAdResponse.getAdTimeoutMillis();
    }

    /* access modifiers changed from: 0000 */
    public void trackImpression() {
        if (this.mAdResponse != null) {
            TrackingRequest.makeTrackingHttpRequest(this.mAdResponse.getImpressionTrackingUrl(), this.mContext, Name.IMPRESSION_REQUEST);
        }
    }

    /* access modifiers changed from: 0000 */
    public void registerClick() {
        if (this.mAdResponse != null) {
            TrackingRequest.makeTrackingHttpRequest(this.mAdResponse.getClickTrackingUrl(), this.mContext, Name.CLICK_REQUEST);
        }
    }

    /* access modifiers changed from: 0000 */
    public void fetchAd(String url) {
        MoPubView moPubView = getMoPubView();
        if (moPubView == null || this.mContext == null) {
            MoPubLog.d("Can't load an ad in this ad view because it was destroyed.");
            setNotLoading();
            return;
        }
        AdRequest adRequest = new AdRequest(url, moPubView.getAdFormat(), this.mAdUnitId, this.mContext, this.mAdListener);
        Networking.getRequestQueue(this.mContext).add(adRequest);
        this.mActiveRequest = adRequest;
    }

    /* access modifiers changed from: 0000 */
    public void forceRefresh() {
        setNotLoading();
        loadAd();
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String generateAdUrl() {
        return this.mUrlGenerator == null ? null : this.mUrlGenerator.withAdUnitId(this.mAdUnitId).withKeywords(this.mKeywords).withLocation(this.mLocation).generateUrlString(Constants.HOST);
    }

    /* access modifiers changed from: 0000 */
    public void adDidFail(MoPubErrorCode errorCode) {
        MoPubLog.i("Ad failed to load.");
        setNotLoading();
        MoPubView moPubView = getMoPubView();
        if (moPubView != null) {
            scheduleRefreshTimerIfEnabled();
            moPubView.adFailed(errorCode);
        }
    }

    /* access modifiers changed from: 0000 */
    public void scheduleRefreshTimerIfEnabled() {
        cancelRefreshTimer();
        if (this.mAutoRefreshEnabled && this.mRefreshTimeMillis != null && this.mRefreshTimeMillis.intValue() > 0) {
            this.mHandler.postDelayed(this.mRefreshRunnable, Math.min(600000, ((long) this.mRefreshTimeMillis.intValue()) * ((long) Math.pow(BACKOFF_FACTOR, (double) this.mBackoffPower))));
        }
    }

    /* access modifiers changed from: 0000 */
    public void setLocalExtras(Map<String, Object> localExtras) {
        this.mLocalExtras = localExtras != null ? new TreeMap(localExtras) : new TreeMap();
    }

    /* access modifiers changed from: 0000 */
    public Map<String, Object> getLocalExtras() {
        return this.mLocalExtras != null ? new TreeMap(this.mLocalExtras) : new TreeMap();
    }

    private void cancelRefreshTimer() {
        this.mHandler.removeCallbacks(this.mRefreshRunnable);
    }

    private boolean isNetworkAvailable() {
        if (this.mContext == null) {
            return false;
        }
        if (this.mContext.checkCallingPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* access modifiers changed from: 0000 */
    public void setAdContentView(final View view) {
        if (view != null && (view instanceof HtmlBannerWebView)) {
            this.mIsImageLoadSucc = ((HtmlBannerWebView) view).isImgLoadSuccess;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                MoPubView moPubView = AdViewController.this.getMoPubView();
                if (moPubView != null) {
                    moPubView.removeAllViews();
                    moPubView.addView(view, AdViewController.this.getAdLayoutParams(view));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public LayoutParams getAdLayoutParams(View view) {
        Integer width;
        Integer num = null;
        if (this.mAdResponse != null) {
            width = this.mAdResponse.getWidth();
            num = this.mAdResponse.getHeight();
        } else {
            width = null;
        }
        if (width == null || num == null || !getShouldHonorServerDimensions(view) || width.intValue() <= 0 || num.intValue() <= 0) {
            return WRAP_AND_CENTER_LAYOUT_PARAMS;
        }
        return new LayoutParams(Dips.asIntPixels((float) width.intValue(), this.mContext), Dips.asIntPixels((float) num.intValue(), this.mContext), 17);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public Integer getRefreshTimeMillis() {
        return this.mRefreshTimeMillis;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setRefreshTimeMillis(@Nullable Integer refreshTimeMillis) {
        this.mRefreshTimeMillis = refreshTimeMillis;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public boolean isImageLoadSuccess() {
        return this.mIsImageLoadSucc;
    }
}
