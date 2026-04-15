package com.mopub.nativeads;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

public interface MoPubAdRenderer<T extends BaseNativeAd> {
    @NonNull
    View createAdView(@NonNull Activity activity, @Nullable ViewGroup viewGroup);

    void renderAdView(@NonNull View view, @NonNull T t);

    boolean supports(@NonNull BaseNativeAd baseNativeAd);
}
