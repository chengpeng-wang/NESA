package com.mopub.nativeads;

import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import com.mopub.common.Preconditions.NoThrow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MoPubNativeAdPositioning {

    public static class MoPubClientPositioning {
        public static final int NO_REPEAT = Integer.MAX_VALUE;
        /* access modifiers changed from: private|final */
        @NonNull
        public final ArrayList<Integer> mFixedPositions = new ArrayList();
        /* access modifiers changed from: private */
        public int mRepeatInterval = NO_REPEAT;

        @NonNull
        public MoPubClientPositioning addFixedPosition(int position) {
            if (NoThrow.checkArgument(position >= 0)) {
                int binarySearch = Collections.binarySearch(this.mFixedPositions, Integer.valueOf(position));
                if (binarySearch < 0) {
                    this.mFixedPositions.add(binarySearch ^ -1, Integer.valueOf(position));
                }
            }
            return this;
        }

        /* access modifiers changed from: 0000 */
        @NonNull
        public List<Integer> getFixedPositions() {
            return this.mFixedPositions;
        }

        @NonNull
        public MoPubClientPositioning enableRepeatingPositions(int interval) {
            boolean z = true;
            if (interval <= 1) {
                z = false;
            }
            if (NoThrow.checkArgument(z, "Repeating interval must be greater than 1")) {
                this.mRepeatInterval = interval;
            } else {
                this.mRepeatInterval = NO_REPEAT;
            }
            return this;
        }

        /* access modifiers changed from: 0000 */
        public int getRepeatingInterval() {
            return this.mRepeatInterval;
        }
    }

    public static class MoPubServerPositioning {
    }

    @NonNull
    static MoPubClientPositioning clone(@NonNull MoPubClientPositioning positioning) {
        Preconditions.checkNotNull(positioning);
        MoPubClientPositioning moPubClientPositioning = new MoPubClientPositioning();
        moPubClientPositioning.mFixedPositions.addAll(positioning.mFixedPositions);
        moPubClientPositioning.mRepeatInterval = positioning.mRepeatInterval;
        return moPubClientPositioning;
    }

    @NonNull
    public static MoPubClientPositioning clientPositioning() {
        return new MoPubClientPositioning();
    }

    @NonNull
    public static MoPubServerPositioning serverPositioning() {
        return new MoPubServerPositioning();
    }
}
