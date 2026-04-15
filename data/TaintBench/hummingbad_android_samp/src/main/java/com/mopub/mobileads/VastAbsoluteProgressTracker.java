package com.mopub.mobileads;

import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import java.io.Serializable;
import java.util.Locale;

public class VastAbsoluteProgressTracker extends VastTracker implements Serializable, Comparable<VastAbsoluteProgressTracker> {
    private static final long serialVersionUID = 0;
    private final int mTrackingMilliseconds;

    public VastAbsoluteProgressTracker(@NonNull String trackingUrl, int trackingMilliseconds) {
        super(trackingUrl);
        Preconditions.checkArgument(trackingMilliseconds >= 0);
        this.mTrackingMilliseconds = trackingMilliseconds;
    }

    public int getTrackingMilliseconds() {
        return this.mTrackingMilliseconds;
    }

    public int compareTo(@NonNull VastAbsoluteProgressTracker other) {
        return getTrackingMilliseconds() - other.getTrackingMilliseconds();
    }

    public String toString() {
        return String.format(Locale.US, "%dms: %s", new Object[]{Integer.valueOf(this.mTrackingMilliseconds), this.mTrackingUrl});
    }
}
