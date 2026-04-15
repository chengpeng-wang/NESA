package com.mopub.nativeads;

import android.view.View;

public interface ImpressionInterface {
    int getImpressionMinPercentageViewed();

    int getImpressionMinTimeViewed();

    boolean isImpressionRecorded();

    void recordImpression(View view);

    void setImpressionRecorded();
}
