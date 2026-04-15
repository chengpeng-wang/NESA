package com.mopub.mobileads;

import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import java.io.Serializable;
import java.util.Locale;

public class VastFractionalProgressTracker extends VastTracker implements Serializable, Comparable<VastFractionalProgressTracker> {
    private static final long serialVersionUID = 0;
    private final float mFraction;

    public VastFractionalProgressTracker(@NonNull String trackingUrl, float trackingFraction) {
        super(trackingUrl);
        Preconditions.checkArgument(trackingFraction >= 0.0f);
        this.mFraction = trackingFraction;
    }

    public float trackingFraction() {
        return this.mFraction;
    }

    public int compareTo(@NonNull VastFractionalProgressTracker other) {
        return Double.compare((double) trackingFraction(), (double) other.trackingFraction());
    }

    public String toString() {
        return String.format(Locale.US, "%2f: %s", new Object[]{Float.valueOf(this.mFraction), this.mTrackingUrl});
    }
}
