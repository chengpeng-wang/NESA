package com.mopub.mobileads;

import android.support.annotation.NonNull;
import com.mopub.common.MoPubReward;
import java.util.Set;

public interface MoPubRewardedVideoListener {
    void onRewardedVideoClosed(@NonNull String str);

    void onRewardedVideoCompleted(@NonNull Set<String> set, @NonNull MoPubReward moPubReward);

    void onRewardedVideoLoadFailure(@NonNull String str, @NonNull MoPubErrorCode moPubErrorCode);

    void onRewardedVideoLoadSuccess(@NonNull String str);

    void onRewardedVideoPlaybackError(@NonNull String str, @NonNull MoPubErrorCode moPubErrorCode);

    void onRewardedVideoStarted(@NonNull String str);
}
