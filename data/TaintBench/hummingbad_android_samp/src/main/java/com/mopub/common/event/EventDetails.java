package com.mopub.common.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.util.Json;
import java.util.HashMap;
import java.util.Map;

public class EventDetails {
    private static final String AD_HEIGHT_PX_KEY = "ad_height_px_key";
    private static final String AD_NETWORK_TYPE_KEY = "ad_network_type";
    private static final String AD_TYPE_KEY = "ad_type";
    private static final String AD_UNIT_ID_KEY = "ad_unit_id";
    private static final String AD_WIDTH_PX_KEY = "ad_width_px";
    private static final String DSP_CREATIVE_ID_KEY = "dsp_creative_id";
    private static final String GEO_ACCURACY_KEY = "geo_accuracy_key";
    private static final String GEO_LATITUDE_KEY = "geo_latitude";
    private static final String GEO_LONGITUDE_KEY = "geo_longitude";
    private static final String PERFORMANCE_DURATION_MS_KEY = "performance_duration_ms";
    private static final String REQUEST_ID_KEY = "request_id_key";
    private static final String REQUEST_STATUS_CODE_KEY = "request_status_code";
    private static final String REQUEST_URI_KEY = "request_uri_key";
    @NonNull
    private final Map<String, String> mEventDetailsMap;

    public static class Builder {
        @NonNull
        private final Map<String, String> eventDetailsMap = new HashMap();

        @NonNull
        public Builder adUnitId(@Nullable String adUnitId) {
            if (adUnitId != null) {
                this.eventDetailsMap.put(EventDetails.AD_UNIT_ID_KEY, adUnitId);
            }
            return this;
        }

        @NonNull
        public Builder dspCreativeId(@Nullable String dspCreativeId) {
            if (dspCreativeId != null) {
                this.eventDetailsMap.put(EventDetails.DSP_CREATIVE_ID_KEY, dspCreativeId);
            }
            return this;
        }

        @NonNull
        public Builder adType(@Nullable String adType) {
            if (adType != null) {
                this.eventDetailsMap.put(EventDetails.AD_TYPE_KEY, adType);
            }
            return this;
        }

        @NonNull
        public Builder adNetworkType(@Nullable String adNetworkType) {
            if (adNetworkType != null) {
                this.eventDetailsMap.put(EventDetails.AD_NETWORK_TYPE_KEY, adNetworkType);
            }
            return this;
        }

        @NonNull
        public Builder adWidthPx(@Nullable Integer adWidthPx) {
            if (adWidthPx != null) {
                this.eventDetailsMap.put(EventDetails.AD_WIDTH_PX_KEY, String.valueOf(adWidthPx));
            }
            return this;
        }

        @NonNull
        public Builder adHeightPx(@Nullable Integer adHeightPx) {
            if (adHeightPx != null) {
                this.eventDetailsMap.put(EventDetails.AD_HEIGHT_PX_KEY, String.valueOf(adHeightPx));
            }
            return this;
        }

        @NonNull
        public Builder geoLatitude(@Nullable Double geoLatitude) {
            if (geoLatitude != null) {
                this.eventDetailsMap.put(EventDetails.GEO_LATITUDE_KEY, String.valueOf(geoLatitude));
            }
            return this;
        }

        @NonNull
        public Builder geoLongitude(@Nullable Double geoLongitude) {
            if (geoLongitude != null) {
                this.eventDetailsMap.put(EventDetails.GEO_LONGITUDE_KEY, String.valueOf(geoLongitude));
            }
            return this;
        }

        @NonNull
        public Builder geoAccuracy(@Nullable Float geoAccuracy) {
            if (geoAccuracy != null) {
                this.eventDetailsMap.put(EventDetails.GEO_ACCURACY_KEY, String.valueOf((double) geoAccuracy.floatValue()));
            }
            return this;
        }

        @NonNull
        public Builder performanceDurationMs(@Nullable Long performanceDurationMs) {
            if (performanceDurationMs != null) {
                this.eventDetailsMap.put(EventDetails.PERFORMANCE_DURATION_MS_KEY, String.valueOf((double) performanceDurationMs.longValue()));
            }
            return this;
        }

        @NonNull
        public Builder requestId(@Nullable String requestId) {
            if (requestId != null) {
                this.eventDetailsMap.put(EventDetails.REQUEST_ID_KEY, requestId);
            }
            return this;
        }

        @NonNull
        public Builder requestStatusCode(@Nullable Integer requestStatusCode) {
            if (requestStatusCode != null) {
                this.eventDetailsMap.put(EventDetails.REQUEST_STATUS_CODE_KEY, String.valueOf(requestStatusCode));
            }
            return this;
        }

        @NonNull
        public Builder requestUri(@Nullable String requestUri) {
            if (requestUri != null) {
                this.eventDetailsMap.put(EventDetails.REQUEST_URI_KEY, requestUri);
            }
            return this;
        }

        @NonNull
        public EventDetails build() {
            return new EventDetails(this.eventDetailsMap);
        }
    }

    private EventDetails(@NonNull Map<String, String> eventDetailsMap) {
        Preconditions.checkNotNull(eventDetailsMap);
        this.mEventDetailsMap = eventDetailsMap;
    }

    @Nullable
    public String getAdUnitId() {
        return (String) this.mEventDetailsMap.get(AD_UNIT_ID_KEY);
    }

    @Nullable
    public String getDspCreativeId() {
        return (String) this.mEventDetailsMap.get(DSP_CREATIVE_ID_KEY);
    }

    @Nullable
    public String getAdType() {
        return (String) this.mEventDetailsMap.get(AD_TYPE_KEY);
    }

    @Nullable
    public String getAdNetworkType() {
        return (String) this.mEventDetailsMap.get(AD_NETWORK_TYPE_KEY);
    }

    @Nullable
    public Double getAdWidthPx() {
        return getNullableDoubleValue(this.mEventDetailsMap, AD_WIDTH_PX_KEY);
    }

    @Nullable
    public Double getAdHeightPx() {
        return getNullableDoubleValue(this.mEventDetailsMap, AD_HEIGHT_PX_KEY);
    }

    @Nullable
    public Double getGeoLatitude() {
        return getNullableDoubleValue(this.mEventDetailsMap, GEO_LATITUDE_KEY);
    }

    @Nullable
    public Double getGeoLongitude() {
        return getNullableDoubleValue(this.mEventDetailsMap, GEO_LONGITUDE_KEY);
    }

    @Nullable
    public Double getGeoAccuracy() {
        return getNullableDoubleValue(this.mEventDetailsMap, GEO_ACCURACY_KEY);
    }

    @Nullable
    public Double getPerformanceDurationMs() {
        return getNullableDoubleValue(this.mEventDetailsMap, PERFORMANCE_DURATION_MS_KEY);
    }

    @Nullable
    public String getRequestId() {
        return (String) this.mEventDetailsMap.get(REQUEST_ID_KEY);
    }

    @Nullable
    public Integer getRequestStatusCode() {
        return getNullableIntegerValue(this.mEventDetailsMap, REQUEST_STATUS_CODE_KEY);
    }

    @Nullable
    public String getRequestUri() {
        return (String) this.mEventDetailsMap.get(REQUEST_URI_KEY);
    }

    public String toJsonString() {
        return Json.mapToJsonString(this.mEventDetailsMap);
    }

    public String toString() {
        return toJsonString();
    }

    @Nullable
    private static Double getNullableDoubleValue(@NonNull Map<String, String> map, @NonNull String key) {
        String str = (String) map.get(key);
        if (str == null) {
            return null;
        }
        try {
            return Double.valueOf(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    private static Integer getNullableIntegerValue(@NonNull Map<String, String> map, @NonNull String key) {
        String str = (String) map.get(key);
        if (str == null) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
