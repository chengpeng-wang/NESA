package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import java.util.WeakHashMap;

@Deprecated
class NativeAdViewHelper {
    private static final WeakHashMap<View, NativeAd> sNativeAdMap = new WeakHashMap();

    @VisibleForTesting
    enum ViewType {
        EMPTY,
        AD
    }

    private NativeAdViewHelper() {
    }

    @Deprecated
    @NonNull
    static View getAdView(@Nullable View convertView, @Nullable ViewGroup parent, @NonNull Activity activity, @Nullable NativeAd nativeAd, @Nullable ViewBinder viewBinder) {
        NoThrow.checkNotNull(viewBinder, "ViewBinder is null.");
        if (convertView != null) {
            clearNativeAd(activity, convertView);
        }
        if (nativeAd == null || nativeAd.isDestroyed() || viewBinder == null) {
            MoPubLog.d("NativeAd or viewBinder null or invalid. Returning empty view");
            if (convertView != null && ViewType.EMPTY.equals(convertView.getTag())) {
                return convertView;
            }
            convertView = new View(activity);
            convertView.setTag(ViewType.EMPTY);
            convertView.setVisibility(8);
            return convertView;
        }
        if (convertView == null || !ViewType.AD.equals(convertView.getTag())) {
            convertView = nativeAd.createAdView(activity, parent);
            convertView.setTag(ViewType.AD);
        }
        prepareNativeAd(activity, convertView, nativeAd);
        nativeAd.renderAdView(convertView);
        return convertView;
    }

    private static void clearNativeAd(@NonNull Context context, @NonNull View view) {
        NativeAd nativeAd = (NativeAd) sNativeAdMap.get(view);
        if (nativeAd != null) {
            nativeAd.clear(view);
        }
    }

    private static void prepareNativeAd(@NonNull Context context, @NonNull View view, @NonNull NativeAd nativeAd) {
        sNativeAdMap.put(view, nativeAd);
        nativeAd.prepare(view);
    }
}
