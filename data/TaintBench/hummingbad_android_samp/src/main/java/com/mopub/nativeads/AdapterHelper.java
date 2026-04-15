package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import java.lang.ref.WeakReference;

@Deprecated
public final class AdapterHelper {
    @NonNull
    private final WeakReference<Activity> mActivity;
    @NonNull
    private final Context mApplicationContext;
    private final int mInterval;
    private final int mStart;

    @Deprecated
    public AdapterHelper(@NonNull Context context, int start, int interval) {
        boolean z = true;
        Preconditions.checkNotNull(context, "Context cannot be null.");
        Preconditions.checkArgument(context instanceof Activity, "Context must be an Activity.");
        Preconditions.checkArgument(start >= 0, "start position must be non-negative");
        if (interval < 2) {
            z = false;
        }
        Preconditions.checkArgument(z, "interval must be at least 2");
        this.mActivity = new WeakReference((Activity) context);
        this.mApplicationContext = context.getApplicationContext();
        this.mStart = start;
        this.mInterval = interval;
    }

    @Deprecated
    @NonNull
    public final View getAdView(@Nullable View convertView, @Nullable ViewGroup parent, @Nullable NativeAd nativeAd, @Nullable ViewBinder viewBinder) {
        Activity activity = (Activity) this.mActivity.get();
        if (activity != null) {
            return NativeAdViewHelper.getAdView(convertView, parent, activity, nativeAd, viewBinder);
        }
        MoPubLog.w("Weak reference to Activity Context in AdapterHelper became null. Returning empty view.");
        return new View(this.mApplicationContext);
    }

    @Deprecated
    public final int shiftedCount(int originalCount) {
        return numberOfAdsThatCouldFitWithContent(originalCount) + originalCount;
    }

    @Deprecated
    public final int shiftedPosition(int position) {
        return position - numberOfAdsSeenUpToPosition(position);
    }

    @Deprecated
    public final boolean isAdPosition(int position) {
        if (position >= this.mStart && (position - this.mStart) % this.mInterval == 0) {
            return true;
        }
        return false;
    }

    private int numberOfAdsSeenUpToPosition(int position) {
        if (position <= this.mStart) {
            return 0;
        }
        return ((int) Math.floor(((double) (position - this.mStart)) / ((double) this.mInterval))) + 1;
    }

    private int numberOfAdsThatCouldFitWithContent(int contentRowCount) {
        if (contentRowCount <= this.mStart) {
            return 0;
        }
        int i = this.mInterval - 1;
        if ((contentRowCount - this.mStart) % i == 0) {
            return (contentRowCount - this.mStart) / i;
        }
        return ((int) Math.floor(((double) (contentRowCount - this.mStart)) / ((double) i))) + 1;
    }

    /* access modifiers changed from: final */
    @Deprecated
    @VisibleForTesting
    public final void clearActivityContext() {
        this.mActivity.clear();
    }
}
