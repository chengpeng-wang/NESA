package com.mopub.common.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.ClientMetadata;
import com.mopub.common.ClientMetadata.MoPubNetworkType;
import com.mopub.common.Preconditions;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseEvent {
    @Nullable
    private final String mAdCreativeId;
    @Nullable
    private final Double mAdHeightPx;
    @Nullable
    private final String mAdNetworkType;
    @Nullable
    private final String mAdType;
    @Nullable
    private final String mAdUnitId;
    @Nullable
    private final Double mAdWidthPx;
    @NonNull
    private final Category mCategory;
    @Nullable
    private ClientMetadata mClientMetaData = ClientMetadata.getInstance();
    @Nullable
    private final Integer mDeviceScreenHeightDip;
    @Nullable
    private final Integer mDeviceScreenWidthDip;
    @Nullable
    private final Double mGeoAccuracy;
    @Nullable
    private final Double mGeoLat;
    @Nullable
    private final Double mGeoLon;
    @Nullable
    private final String mIsoCountryCode;
    @NonNull
    private final Name mName;
    @Nullable
    private final String mNetworkOperator;
    @Nullable
    private final String mNetworkOperatorName;
    @Nullable
    private final MoPubNetworkType mNetworkType;
    @Nullable
    private final Double mPerformanceDurationMs;
    @Nullable
    private final String mRequestId;
    @Nullable
    private final Integer mRequestRetries;
    @Nullable
    private final Integer mRequestStatusCode;
    @Nullable
    private final String mRequestUri;
    private final double mSamplingRate;
    @NonNull
    private final ScribeCategory mScribeCategory;
    @Nullable
    private final SdkProduct mSdkProduct;
    @Nullable
    private final String mSimIsoCountryCode;
    @Nullable
    private final String mSimOperator;
    @Nullable
    private final String mSimOperatorName;
    private final long mTimestampUtcMs = System.currentTimeMillis();

    public enum AppPlatform {
        NONE(0),
        IOS(1),
        ANDROID(2),
        MOBILE_WEB(3);
        
        private final int mType;

        private AppPlatform(int type) {
            this.mType = type;
        }

        public final int getType() {
            return this.mType;
        }
    }

    public static abstract class Builder {
        /* access modifiers changed from: private */
        @Nullable
        public String mAdCreativeId;
        /* access modifiers changed from: private */
        @Nullable
        public Double mAdHeightPx;
        /* access modifiers changed from: private */
        @Nullable
        public String mAdNetworkType;
        /* access modifiers changed from: private */
        @Nullable
        public String mAdType;
        /* access modifiers changed from: private */
        @Nullable
        public String mAdUnitId;
        /* access modifiers changed from: private */
        @Nullable
        public Double mAdWidthPx;
        /* access modifiers changed from: private */
        @NonNull
        public Category mCategory;
        /* access modifiers changed from: private */
        @Nullable
        public Double mGeoAccuracy;
        /* access modifiers changed from: private */
        @Nullable
        public Double mGeoLat;
        /* access modifiers changed from: private */
        @Nullable
        public Double mGeoLon;
        /* access modifiers changed from: private */
        @NonNull
        public Name mName;
        /* access modifiers changed from: private */
        @Nullable
        public Double mPerformanceDurationMs;
        /* access modifiers changed from: private */
        @Nullable
        public String mRequestId;
        /* access modifiers changed from: private */
        @Nullable
        public Integer mRequestRetries;
        /* access modifiers changed from: private */
        @Nullable
        public Integer mRequestStatusCode;
        /* access modifiers changed from: private */
        @Nullable
        public String mRequestUri;
        /* access modifiers changed from: private */
        public double mSamplingRate;
        /* access modifiers changed from: private */
        @NonNull
        public ScribeCategory mScribeCategory;
        /* access modifiers changed from: private */
        @Nullable
        public SdkProduct mSdkProduct;

        public abstract BaseEvent build();

        public Builder(@NonNull ScribeCategory scribeCategory, @NonNull Name name, @NonNull Category category, double samplingRate) {
            Preconditions.checkNotNull(scribeCategory);
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(category);
            boolean z = samplingRate >= 0.0d && samplingRate <= 1.0d;
            Preconditions.checkArgument(z);
            this.mScribeCategory = scribeCategory;
            this.mName = name;
            this.mCategory = category;
            this.mSamplingRate = samplingRate;
        }

        @NonNull
        public Builder withSdkProduct(@Nullable SdkProduct sdkProduct) {
            this.mSdkProduct = sdkProduct;
            return this;
        }

        @NonNull
        public Builder withAdUnitId(@Nullable String adUnitId) {
            this.mAdUnitId = adUnitId;
            return this;
        }

        @NonNull
        public Builder withAdCreativeId(@Nullable String adCreativeId) {
            this.mAdCreativeId = adCreativeId;
            return this;
        }

        @NonNull
        public Builder withAdType(@Nullable String adType) {
            this.mAdType = adType;
            return this;
        }

        @NonNull
        public Builder withAdNetworkType(@Nullable String adNetworkType) {
            this.mAdNetworkType = adNetworkType;
            return this;
        }

        @NonNull
        public Builder withAdWidthPx(@Nullable Double adWidthPx) {
            this.mAdWidthPx = adWidthPx;
            return this;
        }

        @NonNull
        public Builder withAdHeightPx(@Nullable Double adHeightPx) {
            this.mAdHeightPx = adHeightPx;
            return this;
        }

        @NonNull
        public Builder withGeoLat(@Nullable Double geoLat) {
            this.mGeoLat = geoLat;
            return this;
        }

        @NonNull
        public Builder withGeoLon(@Nullable Double geoLon) {
            this.mGeoLon = geoLon;
            return this;
        }

        @NonNull
        public Builder withGeoAccuracy(@Nullable Double geoAccuracy) {
            this.mGeoAccuracy = geoAccuracy;
            return this;
        }

        @NonNull
        public Builder withPerformanceDurationMs(@Nullable Double performanceDurationMs) {
            this.mPerformanceDurationMs = performanceDurationMs;
            return this;
        }

        @NonNull
        public Builder withRequestId(@Nullable String requestId) {
            this.mRequestId = requestId;
            return this;
        }

        @NonNull
        public Builder withRequestStatusCode(@Nullable Integer requestStatusCode) {
            this.mRequestStatusCode = requestStatusCode;
            return this;
        }

        @NonNull
        public Builder withRequestUri(@Nullable String requestUri) {
            this.mRequestUri = requestUri;
            return this;
        }

        @NonNull
        public Builder withRequestRetries(@Nullable Integer requestRetries) {
            this.mRequestRetries = requestRetries;
            return this;
        }
    }

    public enum Category {
        REQUESTS("requests"),
        NATIVE_VIDEO("native_video");
        
        @NonNull
        private final String mCategory;

        private Category(String category) {
            this.mCategory = category;
        }

        @NonNull
        public final String getCategory() {
            return this.mCategory;
        }
    }

    public enum Name {
        AD_REQUEST("ad_request"),
        IMPRESSION_REQUEST("impression_request"),
        CLICK_REQUEST("click_request"),
        DOWNLOAD_START("download_start"),
        DOWNLOAD_VIDEO_READY("download_video_ready"),
        DOWNLOAD_BUFFERING("download_video_buffering"),
        DOWNLOAD_FINISHED("download_finished"),
        ERROR_DURING_PLAYBACK("error_during_playback"),
        ERROR_FAILED_TO_PLAY("error_failed_to_play");
        
        @NonNull
        private final String mName;

        private Name(String name) {
            this.mName = name;
        }

        @NonNull
        public final String getName() {
            return this.mName;
        }
    }

    public enum SamplingRate {
        AD_REQUEST(0.1d),
        NATIVE_VIDEO(0.1d);
        
        private final double mSamplingRate;

        private SamplingRate(double samplingRate) {
            this.mSamplingRate = samplingRate;
        }

        public final double getSamplingRate() {
            return this.mSamplingRate;
        }
    }

    public enum ScribeCategory {
        EXCHANGE_CLIENT_EVENT("exchange_client_event"),
        EXCHANGE_CLIENT_ERROR("exchange_client_error");
        
        @NonNull
        private final String mScribeCategory;

        private ScribeCategory(String scribeCategory) {
            this.mScribeCategory = scribeCategory;
        }

        @NonNull
        public final String getCategory() {
            return this.mScribeCategory;
        }
    }

    public enum SdkProduct {
        NONE(0),
        WEB_VIEW(1),
        NATIVE(2);
        
        private final int mType;

        private SdkProduct(int type) {
            this.mType = type;
        }

        public final int getType() {
            return this.mType;
        }
    }

    public BaseEvent(@NonNull Builder builder) {
        Preconditions.checkNotNull(builder);
        this.mScribeCategory = builder.mScribeCategory;
        this.mName = builder.mName;
        this.mCategory = builder.mCategory;
        this.mSdkProduct = builder.mSdkProduct;
        this.mAdUnitId = builder.mAdUnitId;
        this.mAdCreativeId = builder.mAdCreativeId;
        this.mAdType = builder.mAdType;
        this.mAdNetworkType = builder.mAdNetworkType;
        this.mAdWidthPx = builder.mAdWidthPx;
        this.mAdHeightPx = builder.mAdHeightPx;
        this.mGeoLat = builder.mGeoLat;
        this.mGeoLon = builder.mGeoLon;
        this.mGeoAccuracy = builder.mGeoAccuracy;
        this.mPerformanceDurationMs = builder.mPerformanceDurationMs;
        this.mRequestId = builder.mRequestId;
        this.mRequestStatusCode = builder.mRequestStatusCode;
        this.mRequestUri = builder.mRequestUri;
        this.mRequestRetries = builder.mRequestRetries;
        this.mSamplingRate = builder.mSamplingRate;
        if (this.mClientMetaData != null) {
            this.mDeviceScreenWidthDip = Integer.valueOf(this.mClientMetaData.getDeviceScreenWidthDip());
            this.mDeviceScreenHeightDip = Integer.valueOf(this.mClientMetaData.getDeviceScreenHeightDip());
            this.mNetworkType = this.mClientMetaData.getActiveNetworkType();
            this.mNetworkOperator = this.mClientMetaData.getNetworkOperator();
            this.mNetworkOperatorName = this.mClientMetaData.getNetworkOperatorName();
            this.mIsoCountryCode = this.mClientMetaData.getIsoCountryCode();
            this.mSimOperator = this.mClientMetaData.getSimOperator();
            this.mSimOperatorName = this.mClientMetaData.getSimOperatorName();
            this.mSimIsoCountryCode = this.mClientMetaData.getSimIsoCountryCode();
            return;
        }
        this.mDeviceScreenWidthDip = null;
        this.mDeviceScreenHeightDip = null;
        this.mNetworkType = null;
        this.mNetworkOperator = null;
        this.mNetworkOperatorName = null;
        this.mIsoCountryCode = null;
        this.mSimOperator = null;
        this.mSimOperatorName = null;
        this.mSimIsoCountryCode = null;
    }

    @NonNull
    public ScribeCategory getScribeCategory() {
        return this.mScribeCategory;
    }

    @NonNull
    public Name getName() {
        return this.mName;
    }

    @NonNull
    public Category getCategory() {
        return this.mCategory;
    }

    @Nullable
    public SdkProduct getSdkProduct() {
        return this.mSdkProduct;
    }

    @Nullable
    public String getSdkVersion() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getSdkVersion();
    }

    @Nullable
    public String getAdUnitId() {
        return this.mAdUnitId;
    }

    @Nullable
    public String getAdCreativeId() {
        return this.mAdCreativeId;
    }

    @Nullable
    public String getAdType() {
        return this.mAdType;
    }

    @Nullable
    public String getAdNetworkType() {
        return this.mAdNetworkType;
    }

    @Nullable
    public Double getAdWidthPx() {
        return this.mAdWidthPx;
    }

    @Nullable
    public Double getAdHeightPx() {
        return this.mAdHeightPx;
    }

    @Nullable
    public AppPlatform getAppPlatform() {
        return AppPlatform.ANDROID;
    }

    @Nullable
    public String getAppName() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getAppName();
    }

    @Nullable
    public String getAppPackageName() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getAppPackageName();
    }

    @Nullable
    public String getAppVersion() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getAppVersion();
    }

    @Nullable
    public String getClientAdvertisingId() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getDeviceId();
    }

    @NonNull
    public String getObfuscatedClientAdvertisingId() {
        return "ifa:XXXX";
    }

    @NonNull
    public Boolean getClientDoNotTrack() {
        boolean z = this.mClientMetaData == null || this.mClientMetaData.isDoNotTrackSet();
        return Boolean.valueOf(z);
    }

    @Nullable
    public String getDeviceManufacturer() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getDeviceManufacturer();
    }

    @Nullable
    public String getDeviceModel() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getDeviceModel();
    }

    @Nullable
    public String getDeviceProduct() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getDeviceProduct();
    }

    @Nullable
    public String getDeviceOsVersion() {
        return this.mClientMetaData == null ? null : this.mClientMetaData.getDeviceOsVersion();
    }

    @Nullable
    public Integer getDeviceScreenWidthDip() {
        return this.mDeviceScreenWidthDip;
    }

    @Nullable
    public Integer getDeviceScreenHeightDip() {
        return this.mDeviceScreenHeightDip;
    }

    @Nullable
    public Double getGeoLat() {
        return this.mGeoLat;
    }

    @Nullable
    public Double getGeoLon() {
        return this.mGeoLon;
    }

    @Nullable
    public Double getGeoAccuracy() {
        return this.mGeoAccuracy;
    }

    @Nullable
    public Double getPerformanceDurationMs() {
        return this.mPerformanceDurationMs;
    }

    @Nullable
    public MoPubNetworkType getNetworkType() {
        return this.mNetworkType;
    }

    @Nullable
    public String getNetworkOperatorCode() {
        return this.mNetworkOperator;
    }

    @Nullable
    public String getNetworkOperatorName() {
        return this.mNetworkOperatorName;
    }

    @Nullable
    public String getNetworkIsoCountryCode() {
        return this.mIsoCountryCode;
    }

    @Nullable
    public String getNetworkSimCode() {
        return this.mSimOperator;
    }

    @Nullable
    public String getNetworkSimOperatorName() {
        return this.mSimOperatorName;
    }

    @Nullable
    public String getNetworkSimIsoCountryCode() {
        return this.mSimIsoCountryCode;
    }

    @Nullable
    public String getRequestId() {
        return this.mRequestId;
    }

    @Nullable
    public Integer getRequestStatusCode() {
        return this.mRequestStatusCode;
    }

    @Nullable
    public String getRequestUri() {
        return this.mRequestUri;
    }

    @Nullable
    public Integer getRequestRetries() {
        return this.mRequestRetries;
    }

    public double getSamplingRate() {
        return this.mSamplingRate;
    }

    @NonNull
    public Long getTimestampUtcMs() {
        return Long.valueOf(this.mTimestampUtcMs);
    }

    public String toString() {
        return "BaseEvent\nScribeCategory: " + getScribeCategory() + "\nName: " + getName() + "\nCategory: " + getCategory() + "\nSdkProduct: " + getSdkProduct() + "\nSdkVersion: " + getSdkVersion() + "\nAdUnitId: " + getAdUnitId() + "\nAdCreativeId: " + getAdCreativeId() + "\nAdType: " + getAdType() + "\nAdNetworkType: " + getAdNetworkType() + "\nAdWidthPx: " + getAdWidthPx() + "\nAdHeightPx: " + getAdHeightPx() + "\nAppPlatform: " + getAppPlatform() + "\nAppName: " + getAppName() + "\nAppPackageName: " + getAppPackageName() + "\nAppVersion: " + getAppVersion() + "\nDeviceManufacturer: " + getDeviceManufacturer() + "\nDeviceModel: " + getDeviceModel() + "\nDeviceProduct: " + getDeviceProduct() + "\nDeviceOsVersion: " + getDeviceOsVersion() + "\nDeviceScreenWidth: " + getDeviceScreenWidthDip() + "\nDeviceScreenHeight: " + getDeviceScreenHeightDip() + "\nGeoLat: " + getGeoLat() + "\nGeoLon: " + getGeoLon() + "\nGeoAccuracy: " + getGeoAccuracy() + "\nPerformanceDurationMs: " + getPerformanceDurationMs() + "\nNetworkType: " + getNetworkType() + "\nNetworkOperatorCode: " + getNetworkOperatorCode() + "\nNetworkOperatorName: " + getNetworkOperatorName() + "\nNetworkIsoCountryCode: " + getNetworkIsoCountryCode() + "\nNetworkSimCode: " + getNetworkSimCode() + "\nNetworkSimOperatorName: " + getNetworkSimOperatorName() + "\nNetworkSimIsoCountryCode: " + getNetworkSimIsoCountryCode() + "\nRequestId: " + getRequestId() + "\nRequestStatusCode: " + getRequestStatusCode() + "\nRequestUri: " + getRequestUri() + "\nRequestRetries: " + getRequestRetries() + "\nSamplingRate: " + getSamplingRate() + "\nTimestampUtcMs: " + new SimpleDateFormat().format(new Date(getTimestampUtcMs().longValue())) + MASTNativeAdConstants.NEWLINE;
    }
}
