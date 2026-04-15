package com.mopub.nativeads;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.mopub.common.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class ImpressionTracker {
    private static final int PERIOD = 250;
    @NonNull
    private final Handler mPollHandler;
    @NonNull
    private final PollingRunnable mPollingRunnable;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Map<View, TimestampWrapper<ImpressionInterface>> mPollingViews;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Map<View, ImpressionInterface> mTrackedViews;
    /* access modifiers changed from: private|final */
    @NonNull
    public final VisibilityChecker mVisibilityChecker;
    @NonNull
    private final VisibilityTracker mVisibilityTracker;
    @Nullable
    private VisibilityTrackerListener mVisibilityTrackerListener;

    @VisibleForTesting
    class PollingRunnable implements Runnable {
        @NonNull
        private final ArrayList<View> mRemovedViews = new ArrayList();

        PollingRunnable() {
        }

        public void run() {
            for (Entry entry : ImpressionTracker.this.mPollingViews.entrySet()) {
                View view = (View) entry.getKey();
                TimestampWrapper timestampWrapper = (TimestampWrapper) entry.getValue();
                if (ImpressionTracker.this.mVisibilityChecker.hasRequiredTimeElapsed(timestampWrapper.mCreatedTimestamp, ((ImpressionInterface) timestampWrapper.mInstance).getImpressionMinTimeViewed())) {
                    ((ImpressionInterface) timestampWrapper.mInstance).recordImpression(view);
                    ((ImpressionInterface) timestampWrapper.mInstance).setImpressionRecorded();
                    this.mRemovedViews.add(view);
                }
            }
            Iterator it = this.mRemovedViews.iterator();
            while (it.hasNext()) {
                ImpressionTracker.this.removeView((View) it.next());
            }
            this.mRemovedViews.clear();
            if (!ImpressionTracker.this.mPollingViews.isEmpty()) {
                ImpressionTracker.this.scheduleNextPoll();
            }
        }
    }

    public ImpressionTracker(@NonNull Activity activity) {
        this(new WeakHashMap(), new WeakHashMap(), new VisibilityChecker(), new VisibilityTracker(activity), new Handler(Looper.getMainLooper()));
    }

    public ImpressionTracker(@NonNull View rootView) {
        this(new WeakHashMap(), new WeakHashMap(), new VisibilityChecker(), new VisibilityTracker(rootView), new Handler(Looper.getMainLooper()));
    }

    @VisibleForTesting
    ImpressionTracker(@NonNull Map<View, ImpressionInterface> trackedViews, @NonNull Map<View, TimestampWrapper<ImpressionInterface>> pollingViews, @NonNull VisibilityChecker visibilityChecker, @NonNull VisibilityTracker visibilityTracker, @NonNull Handler handler) {
        this.mTrackedViews = trackedViews;
        this.mPollingViews = pollingViews;
        this.mVisibilityChecker = visibilityChecker;
        this.mVisibilityTracker = visibilityTracker;
        this.mVisibilityTrackerListener = new VisibilityTrackerListener() {
            public void onVisibilityChanged(@NonNull List<View> visibleViews, @NonNull List<View> invisibleViews) {
                for (View view : visibleViews) {
                    ImpressionInterface impressionInterface = (ImpressionInterface) ImpressionTracker.this.mTrackedViews.get(view);
                    if (impressionInterface == null) {
                        ImpressionTracker.this.removeView(view);
                    } else {
                        TimestampWrapper timestampWrapper = (TimestampWrapper) ImpressionTracker.this.mPollingViews.get(view);
                        if (timestampWrapper == null || !impressionInterface.equals(timestampWrapper.mInstance)) {
                            ImpressionTracker.this.mPollingViews.put(view, new TimestampWrapper(impressionInterface));
                        }
                    }
                }
                for (View view2 : invisibleViews) {
                    ImpressionTracker.this.mPollingViews.remove(view2);
                }
                ImpressionTracker.this.scheduleNextPoll();
            }
        };
        this.mVisibilityTracker.setVisibilityTrackerListener(this.mVisibilityTrackerListener);
        this.mPollHandler = handler;
        this.mPollingRunnable = new PollingRunnable();
    }

    public void addView(View view, @NonNull ImpressionInterface impressionInterface) {
        if (this.mTrackedViews.get(view) != impressionInterface) {
            removeView(view);
            if (!impressionInterface.isImpressionRecorded()) {
                this.mTrackedViews.put(view, impressionInterface);
                this.mVisibilityTracker.addView(view, impressionInterface.getImpressionMinPercentageViewed());
            }
        }
    }

    public void removeView(View view) {
        this.mTrackedViews.remove(view);
        removePollingView(view);
        this.mVisibilityTracker.removeView(view);
    }

    public void clear() {
        this.mTrackedViews.clear();
        this.mPollingViews.clear();
        this.mVisibilityTracker.clear();
        this.mPollHandler.removeMessages(0);
    }

    public void destroy() {
        clear();
        this.mVisibilityTracker.destroy();
        this.mVisibilityTrackerListener = null;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void scheduleNextPoll() {
        if (!this.mPollHandler.hasMessages(0)) {
            this.mPollHandler.postDelayed(this.mPollingRunnable, 250);
        }
    }

    private void removePollingView(View view) {
        this.mPollingViews.remove(view);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @Deprecated
    @VisibleForTesting
    public VisibilityTrackerListener getVisibilityTrackerListener() {
        return this.mVisibilityTrackerListener;
    }
}
