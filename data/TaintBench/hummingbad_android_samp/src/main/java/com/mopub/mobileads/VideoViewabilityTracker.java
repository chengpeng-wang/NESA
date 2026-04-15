package com.mopub.mobileads;

import android.support.annotation.NonNull;

public class VideoViewabilityTracker extends VastTracker {
    private final int mPercentViewable;
    private final int mViewablePlaytimeMS;

    public VideoViewabilityTracker(int viewablePlaytimeMS, int percentViewable, @NonNull String trackerUrl) {
        super(trackerUrl);
        this.mViewablePlaytimeMS = viewablePlaytimeMS;
        this.mPercentViewable = percentViewable;
    }

    public int getViewablePlaytimeMS() {
        return this.mViewablePlaytimeMS;
    }

    public int getPercentViewable() {
        return this.mPercentViewable;
    }
}
