package com.mopub.common.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.SamplingRate;
import com.mopub.common.event.BaseEvent.ScribeCategory;
import com.mopub.common.logging.MoPubLog;

public class Event extends BaseEvent {

    public static class Builder extends com.mopub.common.event.BaseEvent.Builder {
        public Builder(@NonNull Name name, @NonNull Category category, double samplingRate) {
            super(ScribeCategory.EXCHANGE_CLIENT_EVENT, name, category, samplingRate);
        }

        @NonNull
        public Event build() {
            return new Event(this);
        }
    }

    private Event(@NonNull Builder builder) {
        super(builder);
    }

    @Nullable
    public static BaseEvent createEventFromDetails(@NonNull Name name, @NonNull Category category, @NonNull SamplingRate samplingRate, @Nullable EventDetails eventDetails) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(category);
        Preconditions.checkNotNull(samplingRate);
        if (eventDetails != null) {
            return new Builder(name, category, samplingRate.getSamplingRate()).withAdUnitId(eventDetails.getAdUnitId()).withAdCreativeId(eventDetails.getDspCreativeId()).withAdType(eventDetails.getAdType()).withAdNetworkType(eventDetails.getAdNetworkType()).withAdWidthPx(eventDetails.getAdWidthPx()).withAdHeightPx(eventDetails.getAdHeightPx()).withGeoLat(eventDetails.getGeoLatitude()).withGeoLon(eventDetails.getGeoLongitude()).withGeoAccuracy(eventDetails.getGeoAccuracy()).withPerformanceDurationMs(eventDetails.getPerformanceDurationMs()).withRequestId(eventDetails.getRequestId()).withRequestStatusCode(eventDetails.getRequestStatusCode()).withRequestUri(eventDetails.getRequestUri()).build();
        }
        MoPubLog.d("Unable to log event due to no details present");
        return null;
    }
}
