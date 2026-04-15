package com.mopub.mobileads;

import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import java.io.Serializable;

public class VastTracker implements Serializable {
    private static final long serialVersionUID = 0;
    private boolean mCalled;
    private boolean mIsRepeatable;
    @NonNull
    protected final String mTrackingUrl;

    public VastTracker(@NonNull String trackingUrl) {
        Preconditions.checkNotNull(trackingUrl);
        this.mTrackingUrl = trackingUrl;
    }

    public VastTracker(@NonNull String trackingUrl, boolean isRepeatable) {
        this(trackingUrl);
        this.mIsRepeatable = isRepeatable;
    }

    @NonNull
    public String getTrackingUrl() {
        return this.mTrackingUrl;
    }

    public void setTracked() {
        this.mCalled = true;
    }

    public boolean isTracked() {
        return this.mCalled;
    }

    public boolean isRepeatable() {
        return this.mIsRepeatable;
    }
}
