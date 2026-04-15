package com.mopub.nativeads;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.Preconditions;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import com.mopub.nativeads.PositioningSource.PositioningListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;

public class MoPubStreamAdPlacer {
    public static final int CONTENT_VIEW_TYPE = 0;
    private static final int DEFAULT_AD_VIEW_TYPE = -1;
    private static final MoPubNativeAdLoadedListener EMPTY_NATIVE_AD_LOADED_LISTENER = new MoPubNativeAdLoadedListener() {
        public final void onAdLoaded(int position) {
        }

        public final void onAdRemoved(int position) {
        }
    };
    private static final int MAX_VISIBLE_RANGE = 100;
    private static final int RANGE_BUFFER = 6;
    @NonNull
    private final Activity mActivity;
    @NonNull
    private MoPubNativeAdLoadedListener mAdLoadedListener;
    @NonNull
    private final NativeAdSource mAdSource;
    @Nullable
    private String mAdUnitId;
    private boolean mHasPlacedAds;
    public boolean mHasReceivedAds;
    private boolean mHasReceivedPositions;
    private int mItemCount;
    @NonNull
    private final WeakHashMap<View, NativeAd> mNativeAdMap;
    /* access modifiers changed from: private */
    public boolean mNeedsPlacement;
    @Nullable
    private PlacementData mPendingPlacementData;
    @NonNull
    private PlacementData mPlacementData;
    @NonNull
    private final Handler mPlacementHandler;
    @NonNull
    private final Runnable mPlacementRunnable;
    @NonNull
    private final PositioningSource mPositioningSource;
    @NonNull
    private final HashMap<NativeAd, WeakReference<View>> mViewMap;
    private int mVisibleRangeEnd;
    private int mVisibleRangeStart;

    public MoPubStreamAdPlacer(@NonNull Activity activity) {
        this(activity, MoPubNativeAdPositioning.serverPositioning());
    }

    public MoPubStreamAdPlacer(@NonNull Activity activity, @NonNull MoPubServerPositioning adPositioning) {
        this(activity, new NativeAdSource(), new ServerPositioningSource(activity));
    }

    public MoPubStreamAdPlacer(@NonNull Activity activity, @NonNull MoPubClientPositioning adPositioning) {
        this(activity, new NativeAdSource(), new ClientPositioningSource(adPositioning));
    }

    @VisibleForTesting
    MoPubStreamAdPlacer(@NonNull Activity activity, @NonNull NativeAdSource adSource, @NonNull PositioningSource positioningSource) {
        this.mAdLoadedListener = EMPTY_NATIVE_AD_LOADED_LISTENER;
        Preconditions.checkNotNull(activity, "activity is not allowed to be null");
        Preconditions.checkNotNull(adSource, "adSource is not allowed to be null");
        Preconditions.checkNotNull(positioningSource, "positioningSource is not allowed to be null");
        this.mActivity = activity;
        this.mPositioningSource = positioningSource;
        this.mAdSource = adSource;
        this.mPlacementData = PlacementData.empty();
        this.mNativeAdMap = new WeakHashMap();
        this.mViewMap = new HashMap();
        this.mPlacementHandler = new Handler();
        this.mPlacementRunnable = new Runnable() {
            public void run() {
                if (MoPubStreamAdPlacer.this.mNeedsPlacement) {
                    MoPubStreamAdPlacer.this.placeAds();
                    MoPubStreamAdPlacer.this.mNeedsPlacement = false;
                }
            }
        };
        this.mVisibleRangeStart = 0;
        this.mVisibleRangeEnd = 0;
    }

    public void setOuterNetworkListener(@NonNull MoPubNativeNetworkListener outerNetworkListener) {
        if (this.mAdSource != null) {
            this.mAdSource.setOuterNetworkListener(outerNetworkListener);
        }
    }

    public void registerAdRenderer(@NonNull MoPubAdRenderer adRenderer) {
        if (NoThrow.checkNotNull(adRenderer, "Cannot register a null adRenderer")) {
            this.mAdSource.registerAdRenderer(adRenderer);
        }
    }

    @Nullable
    public MoPubAdRenderer getAdRendererForViewType(int viewType) {
        return this.mAdSource.getAdRendererForViewType(viewType);
    }

    public void setAdLoadedListener(@Nullable MoPubNativeAdLoadedListener listener) {
        if (listener == null) {
            listener = EMPTY_NATIVE_AD_LOADED_LISTENER;
        }
        this.mAdLoadedListener = listener;
    }

    public void loadAds(@NonNull String adUnitId) {
        loadAds(adUnitId, null);
    }

    public void loadAds(@NonNull String adUnitId, @Nullable RequestParameters requestParameters) {
        if (!NoThrow.checkNotNull(adUnitId, "Cannot load ads with a null ad unit ID")) {
            return;
        }
        if (this.mAdSource.getAdRendererCount() == 0) {
            MoPubLog.w("You must register at least 1 ad renderer by calling registerAdRenderer before loading ads");
            return;
        }
        this.mAdUnitId = adUnitId;
        this.mHasPlacedAds = false;
        this.mHasReceivedPositions = false;
        this.mHasReceivedAds = false;
        this.mPositioningSource.loadPositions(adUnitId, new PositioningListener() {
            public void onLoad(@NonNull MoPubClientPositioning positioning) {
                MoPubStreamAdPlacer.this.handlePositioningLoad(positioning);
            }

            public void onFailed() {
                MoPubLog.d("Unable to show ads because ad positions could not be loaded from the MoPub ad server.");
            }
        });
        this.mAdSource.setAdSourceListener(new AdSourceListener() {
            public void onAdsAvailable() {
                MoPubStreamAdPlacer.this.handleAdsAvailable();
            }
        });
        this.mAdSource.loadAds(this.mActivity, adUnitId, requestParameters);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handlePositioningLoad(@NonNull MoPubClientPositioning positioning) {
        PlacementData fromAdPositioning = PlacementData.fromAdPositioning(positioning);
        if (this.mHasReceivedAds) {
            placeInitialAds(fromAdPositioning);
        } else {
            this.mPendingPlacementData = fromAdPositioning;
        }
        this.mHasReceivedPositions = true;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleAdsAvailable() {
        if (this.mHasPlacedAds) {
            notifyNeedsPlacement();
            return;
        }
        if (this.mHasReceivedPositions) {
            placeInitialAds(this.mPendingPlacementData);
        }
        this.mHasReceivedAds = true;
    }

    private void placeInitialAds(PlacementData placementData) {
        removeAdsInRange(0, this.mItemCount);
        this.mPlacementData = placementData;
        placeAds();
        this.mHasPlacedAds = true;
    }

    public void placeAdsInRange(int startPosition, int endPosition) {
        this.mVisibleRangeStart = startPosition;
        this.mVisibleRangeEnd = Math.min(endPosition, startPosition + 100);
        notifyNeedsPlacement();
    }

    public boolean isAd(int position) {
        return this.mPlacementData.isPlacedAd(position);
    }

    public void clearAds() {
        removeAdsInRange(0, this.mItemCount);
        this.mAdSource.clear();
    }

    public void destroy() {
        this.mPlacementHandler.removeMessages(0);
        this.mAdSource.clear();
        this.mPlacementData.clearAds();
    }

    @Nullable
    public Object getAdData(int position) {
        return this.mPlacementData.getPlacedAd(position);
    }

    @Nullable
    public View getAdView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        NativeAd placedAd = this.mPlacementData.getPlacedAd(position);
        if (placedAd == null) {
            return null;
        }
        if (convertView == null) {
            convertView = placedAd.createAdView(this.mActivity, parent);
        }
        bindAdView(placedAd, convertView);
        return convertView;
    }

    public void bindAdView(@NonNull NativeAd nativeAd, @NonNull View adView) {
        Object obj;
        WeakReference weakReference = (WeakReference) this.mViewMap.get(nativeAd);
        if (weakReference != null) {
            obj = (View) weakReference.get();
        } else {
            obj = null;
        }
        if (!adView.equals(obj)) {
            clearNativeAd(obj);
            clearNativeAd(adView);
            prepareNativeAd(nativeAd, adView);
            nativeAd.renderAdView(adView);
        }
    }

    public int removeAdsInRange(int originalStartPosition, int originalEndPosition) {
        int[] placedAdPositions = this.mPlacementData.getPlacedAdPositions();
        int adjustedPosition = this.mPlacementData.getAdjustedPosition(originalStartPosition);
        int adjustedPosition2 = this.mPlacementData.getAdjustedPosition(originalEndPosition);
        ArrayList arrayList = new ArrayList();
        for (int length = placedAdPositions.length - 1; length >= 0; length--) {
            int i = placedAdPositions[length];
            if (i >= adjustedPosition && i < adjustedPosition2) {
                arrayList.add(Integer.valueOf(i));
                if (i < this.mVisibleRangeStart) {
                    this.mVisibleRangeStart--;
                }
                this.mItemCount--;
            }
        }
        int clearAdsInRange = this.mPlacementData.clearAdsInRange(adjustedPosition, adjustedPosition2);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.mAdLoadedListener.onAdRemoved(((Integer) it.next()).intValue());
        }
        return clearAdsInRange;
    }

    public int getAdViewTypeCount() {
        return this.mAdSource.getAdRendererCount();
    }

    public int getAdViewType(int position) {
        NativeAd placedAd = this.mPlacementData.getPlacedAd(position);
        if (placedAd == null) {
            return 0;
        }
        return this.mAdSource.getViewTypeForAd(placedAd);
    }

    public int getOriginalPosition(int position) {
        return this.mPlacementData.getOriginalPosition(position);
    }

    public int getAdjustedPosition(int originalPosition) {
        return this.mPlacementData.getAdjustedPosition(originalPosition);
    }

    public int getOriginalCount(int count) {
        return this.mPlacementData.getOriginalCount(count);
    }

    public int getAdjustedCount(int originalCount) {
        return this.mPlacementData.getAdjustedCount(originalCount);
    }

    public void setItemCount(int originalCount) {
        this.mItemCount = this.mPlacementData.getAdjustedCount(originalCount);
        if (this.mHasPlacedAds) {
            notifyNeedsPlacement();
        }
    }

    public void insertItem(int originalPosition) {
        this.mPlacementData.insertItem(originalPosition);
    }

    public void removeItem(int originalPosition) {
        this.mPlacementData.removeItem(originalPosition);
    }

    public void moveItem(int originalPosition, int newPosition) {
        this.mPlacementData.moveItem(originalPosition, newPosition);
    }

    private void notifyNeedsPlacement() {
        if (!this.mNeedsPlacement) {
            this.mNeedsPlacement = true;
            this.mPlacementHandler.post(this.mPlacementRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void placeAds() {
        if (tryPlaceAdsInRange(this.mVisibleRangeStart, this.mVisibleRangeEnd)) {
            tryPlaceAdsInRange(this.mVisibleRangeEnd, this.mVisibleRangeEnd + 6);
        }
    }

    private boolean tryPlaceAdsInRange(int start, int end) {
        int i = end - 1;
        while (start <= i && start != -1 && start < this.mItemCount) {
            if (this.mPlacementData.shouldPlaceAd(start)) {
                if (!tryPlaceAd(start)) {
                    return false;
                }
                i++;
            }
            start = this.mPlacementData.nextInsertionPosition(start);
        }
        return true;
    }

    private boolean tryPlaceAd(int position) {
        NativeAd dequeueAd = this.mAdSource.dequeueAd();
        if (dequeueAd == null) {
            return false;
        }
        this.mPlacementData.placeAd(position, dequeueAd);
        this.mItemCount++;
        this.mAdLoadedListener.onAdLoaded(position);
        return true;
    }

    private void clearNativeAd(@Nullable View view) {
        if (view != null) {
            NativeAd nativeAd = (NativeAd) this.mNativeAdMap.get(view);
            if (nativeAd != null) {
                nativeAd.clear(view);
                this.mNativeAdMap.remove(view);
                this.mViewMap.remove(nativeAd);
            }
        }
    }

    private void prepareNativeAd(@NonNull NativeAd nativeAd, @NonNull View view) {
        this.mViewMap.put(nativeAd, new WeakReference(view));
        this.mNativeAdMap.put(view, nativeAd);
        nativeAd.prepare(view);
    }

    public boolean hasReceivedAds() {
        return this.mHasReceivedAds;
    }
}
