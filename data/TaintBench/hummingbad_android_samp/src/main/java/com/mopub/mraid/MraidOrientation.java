package com.mopub.mraid;

enum MraidOrientation {
    PORTRAIT(1),
    LANDSCAPE(0),
    NONE(-1);
    
    private final int mActivityInfoOrientation;

    private MraidOrientation(int activityInfoOrientation) {
        this.mActivityInfoOrientation = activityInfoOrientation;
    }

    /* access modifiers changed from: final */
    public final int getActivityInfoOrientation() {
        return this.mActivityInfoOrientation;
    }
}
