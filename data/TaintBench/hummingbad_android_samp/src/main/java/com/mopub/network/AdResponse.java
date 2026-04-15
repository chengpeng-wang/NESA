package com.mopub.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.event.EventDetails;
import com.mopub.common.util.DateAndTime;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;

public class AdResponse implements Serializable {
    private static final long serialVersionUID = 1;
    @Nullable
    private final Integer mAdTimeoutDelayMillis;
    @Nullable
    private final String mAdType;
    @Nullable
    private final String mAdUnitId;
    @Nullable
    private final String mClickTrackingUrl;
    @Nullable
    private final String mCustomEventClassName;
    @Nullable
    private final String mDspCreativeId;
    @Nullable
    private final EventDetails mEventDetails;
    @Nullable
    private final String mFailoverUrl;
    @Nullable
    private final String mFullAdType;
    @Nullable
    private final Integer mHeight;
    @Nullable
    private final String mImpressionTrackingUrl;
    @Nullable
    private final JSONObject mJsonBody;
    @Nullable
    private final String mNetworkType;
    @Nullable
    private final String mRedirectUrl;
    @Nullable
    private final Integer mRefreshTimeMillis;
    @Nullable
    private final String mRequestId;
    @Nullable
    private final String mResponseBody;
    private final boolean mScrollable;
    @NonNull
    private final Map<String, String> mServerExtras;
    private final long mTimestamp;
    @Nullable
    private final Integer mWidth;

    public static class Builder {
        /* access modifiers changed from: private */
        public Integer adTimeoutDelayMillis;
        /* access modifiers changed from: private */
        public String adType;
        /* access modifiers changed from: private */
        public String adUnitId;
        /* access modifiers changed from: private */
        public String clickTrackingUrl;
        /* access modifiers changed from: private */
        public String customEventClassName;
        /* access modifiers changed from: private */
        public String dspCreativeId;
        /* access modifiers changed from: private */
        public EventDetails eventDetails;
        /* access modifiers changed from: private */
        public String failoverUrl;
        /* access modifiers changed from: private */
        public String fullAdType;
        /* access modifiers changed from: private */
        public Integer height;
        /* access modifiers changed from: private */
        public String impressionTrackingUrl;
        /* access modifiers changed from: private */
        public JSONObject jsonBody;
        /* access modifiers changed from: private */
        public String networkType;
        /* access modifiers changed from: private */
        public String redirectUrl;
        /* access modifiers changed from: private */
        public Integer refreshTimeMillis;
        /* access modifiers changed from: private */
        public String requestId;
        /* access modifiers changed from: private */
        public String responseBody;
        /* access modifiers changed from: private */
        public boolean scrollable = false;
        /* access modifiers changed from: private */
        public Map<String, String> serverExtras = new TreeMap();
        /* access modifiers changed from: private */
        public Integer width;

        public Builder setAdType(@Nullable String adType) {
            this.adType = adType;
            return this;
        }

        public Builder setAdUnitId(@Nullable String adUnitId) {
            this.adUnitId = adUnitId;
            return this;
        }

        public Builder setFullAdType(@Nullable String fullAdType) {
            this.fullAdType = fullAdType;
            return this;
        }

        public Builder setNetworkType(@Nullable String networkType) {
            this.networkType = networkType;
            return this;
        }

        public Builder setRedirectUrl(@Nullable String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public Builder setClickTrackingUrl(@Nullable String clickTrackingUrl) {
            this.clickTrackingUrl = clickTrackingUrl;
            return this;
        }

        public Builder setImpressionTrackingUrl(@Nullable String impressionTrackingUrl) {
            this.impressionTrackingUrl = impressionTrackingUrl;
            return this;
        }

        public Builder setFailoverUrl(@Nullable String failoverUrl) {
            this.failoverUrl = failoverUrl;
            return this;
        }

        public Builder setRequestId(@Nullable String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setDimensions(@Nullable Integer width, @Nullable Integer height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setAdTimeoutDelayMilliseconds(@Nullable Integer adTimeoutDelayMilliseconds) {
            this.adTimeoutDelayMillis = adTimeoutDelayMilliseconds;
            return this;
        }

        public Builder setRefreshTimeMilliseconds(@Nullable Integer refreshTimeMilliseconds) {
            this.refreshTimeMillis = refreshTimeMilliseconds;
            return this;
        }

        public Builder setScrollable(@Nullable Boolean scrollable) {
            this.scrollable = scrollable == null ? this.scrollable : scrollable.booleanValue();
            return this;
        }

        public Builder setDspCreativeId(@Nullable String dspCreativeId) {
            this.dspCreativeId = dspCreativeId;
            return this;
        }

        public Builder setResponseBody(@Nullable String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public Builder setJsonBody(@Nullable JSONObject jsonBody) {
            this.jsonBody = jsonBody;
            return this;
        }

        public Builder setEventDetails(@Nullable EventDetails eventDetails) {
            this.eventDetails = eventDetails;
            return this;
        }

        public Builder setCustomEventClassName(@Nullable String customEventClassName) {
            this.customEventClassName = customEventClassName;
            return this;
        }

        public Builder setServerExtras(@Nullable Map<String, String> serverExtras) {
            if (serverExtras == null) {
                this.serverExtras = new TreeMap();
            } else {
                this.serverExtras = new TreeMap(serverExtras);
            }
            return this;
        }

        public AdResponse build() {
            return new AdResponse(this);
        }
    }

    private AdResponse(@NonNull Builder builder) {
        this.mAdType = builder.adType;
        this.mAdUnitId = builder.adUnitId;
        this.mFullAdType = builder.fullAdType;
        this.mNetworkType = builder.networkType;
        this.mRedirectUrl = builder.redirectUrl;
        this.mClickTrackingUrl = builder.clickTrackingUrl;
        this.mImpressionTrackingUrl = builder.impressionTrackingUrl;
        this.mFailoverUrl = builder.failoverUrl;
        this.mRequestId = builder.requestId;
        this.mWidth = builder.width;
        this.mHeight = builder.height;
        this.mAdTimeoutDelayMillis = builder.adTimeoutDelayMillis;
        this.mRefreshTimeMillis = builder.refreshTimeMillis;
        this.mDspCreativeId = builder.dspCreativeId;
        this.mScrollable = builder.scrollable;
        this.mResponseBody = builder.responseBody;
        this.mJsonBody = builder.jsonBody;
        this.mEventDetails = builder.eventDetails;
        this.mCustomEventClassName = builder.customEventClassName;
        this.mServerExtras = builder.serverExtras;
        this.mTimestamp = DateAndTime.now().getTime();
    }

    public boolean hasJson() {
        return this.mJsonBody != null;
    }

    @Nullable
    public JSONObject getJsonBody() {
        return this.mJsonBody;
    }

    @Nullable
    public EventDetails getEventDetails() {
        return this.mEventDetails;
    }

    @Nullable
    public String getStringBody() {
        return this.mResponseBody;
    }

    @Nullable
    public String getAdType() {
        return this.mAdType;
    }

    @Nullable
    public String getFullAdType() {
        return this.mFullAdType;
    }

    @Nullable
    public String getAdUnitId() {
        return this.mAdUnitId;
    }

    @Nullable
    public String getNetworkType() {
        return this.mNetworkType;
    }

    @Nullable
    public String getRedirectUrl() {
        return this.mRedirectUrl;
    }

    @Nullable
    public String getClickTrackingUrl() {
        return this.mClickTrackingUrl;
    }

    @Nullable
    public String getImpressionTrackingUrl() {
        return this.mImpressionTrackingUrl;
    }

    @Nullable
    public String getFailoverUrl() {
        return this.mFailoverUrl;
    }

    @Nullable
    public String getRequestId() {
        return this.mRequestId;
    }

    public boolean isScrollable() {
        return this.mScrollable;
    }

    @Nullable
    public Integer getWidth() {
        return this.mWidth;
    }

    @Nullable
    public Integer getHeight() {
        return this.mHeight;
    }

    @Nullable
    public Integer getAdTimeoutMillis() {
        return this.mAdTimeoutDelayMillis;
    }

    @Nullable
    public Integer getRefreshTimeMillis() {
        return this.mRefreshTimeMillis;
    }

    @Nullable
    public String getDspCreativeId() {
        return this.mDspCreativeId;
    }

    @Nullable
    public String getCustomEventClassName() {
        return this.mCustomEventClassName;
    }

    @NonNull
    public Map<String, String> getServerExtras() {
        return new TreeMap(this.mServerExtras);
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public Builder toBuilder() {
        return new Builder().setAdType(this.mAdType).setNetworkType(this.mNetworkType).setRedirectUrl(this.mRedirectUrl).setClickTrackingUrl(this.mClickTrackingUrl).setImpressionTrackingUrl(this.mImpressionTrackingUrl).setFailoverUrl(this.mFailoverUrl).setDimensions(this.mWidth, this.mHeight).setAdTimeoutDelayMilliseconds(this.mAdTimeoutDelayMillis).setRefreshTimeMilliseconds(this.mRefreshTimeMillis).setDspCreativeId(this.mDspCreativeId).setScrollable(Boolean.valueOf(this.mScrollable)).setResponseBody(this.mResponseBody).setJsonBody(this.mJsonBody).setEventDetails(this.mEventDetails).setCustomEventClassName(this.mCustomEventClassName).setServerExtras(this.mServerExtras);
    }
}
