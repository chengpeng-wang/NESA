package com.mopub.nativeads;

import android.app.Activity;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import java.util.List;
import java.util.WeakHashMap;

public class MoPubAdAdapter extends BaseAdapter {
    @Nullable
    private MoPubNativeAdLoadedListener mAdLoadedListener;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Adapter mOriginalAdapter;
    /* access modifiers changed from: private|final */
    @NonNull
    public final MoPubStreamAdPlacer mStreamAdPlacer;
    @NonNull
    private final WeakHashMap<View, Integer> mViewPositionMap;
    @NonNull
    private final VisibilityTracker mVisibilityTracker;

    public MoPubAdAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter) {
        this(activity, originalAdapter, MoPubNativeAdPositioning.serverPositioning());
    }

    public MoPubAdAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter, @NonNull MoPubServerPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(activity, adPositioning), originalAdapter, new VisibilityTracker(activity));
    }

    public MoPubAdAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter, @NonNull MoPubClientPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(activity, adPositioning), originalAdapter, new VisibilityTracker(activity));
    }

    @VisibleForTesting
    MoPubAdAdapter(@NonNull MoPubStreamAdPlacer streamAdPlacer, @NonNull Adapter originalAdapter, @NonNull VisibilityTracker visibilityTracker) {
        this.mOriginalAdapter = originalAdapter;
        this.mStreamAdPlacer = streamAdPlacer;
        this.mViewPositionMap = new WeakHashMap();
        this.mVisibilityTracker = visibilityTracker;
        this.mVisibilityTracker.setVisibilityTrackerListener(new VisibilityTrackerListener() {
            public void onVisibilityChanged(@NonNull List<View> visibleViews, List<View> list) {
                MoPubAdAdapter.this.handleVisibilityChange(visibleViews);
            }
        });
        this.mOriginalAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                MoPubAdAdapter.this.mStreamAdPlacer.setItemCount(MoPubAdAdapter.this.mOriginalAdapter.getCount());
                MoPubAdAdapter.this.notifyDataSetChanged();
            }

            public void onInvalidated() {
                MoPubAdAdapter.this.notifyDataSetInvalidated();
            }
        });
        this.mStreamAdPlacer.setAdLoadedListener(new MoPubNativeAdLoadedListener() {
            public void onAdLoaded(int position) {
                MoPubAdAdapter.this.handleAdLoaded(position);
            }

            public void onAdRemoved(int position) {
                MoPubAdAdapter.this.handleAdRemoved(position);
            }
        });
        this.mStreamAdPlacer.setItemCount(this.mOriginalAdapter.getCount());
    }

    public void setOuterNetworkListener(@NonNull MoPubNativeNetworkListener outerNetworkListener) {
        if (this.mStreamAdPlacer != null) {
            this.mStreamAdPlacer.setOuterNetworkListener(outerNetworkListener);
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleAdLoaded(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdLoaded(position);
        }
        notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleAdRemoved(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdRemoved(position);
        }
        notifyDataSetChanged();
    }

    public final void registerAdRenderer(@NonNull MoPubAdRenderer adRenderer) {
        if (NoThrow.checkNotNull(adRenderer, "Tried to set a null ad renderer on the placer.")) {
            this.mStreamAdPlacer.registerAdRenderer(adRenderer);
        }
    }

    public final void setAdLoadedListener(@Nullable MoPubNativeAdLoadedListener listener) {
        this.mAdLoadedListener = listener;
    }

    public void loadAds(@NonNull String adUnitId) {
        this.mStreamAdPlacer.loadAds(adUnitId);
    }

    public void loadAds(@NonNull String adUnitId, @Nullable RequestParameters requestParameters) {
        this.mStreamAdPlacer.loadAds(adUnitId, requestParameters);
    }

    public boolean isAd(int position) {
        return this.mStreamAdPlacer.isAd(position);
    }

    public void clearAds() {
        this.mStreamAdPlacer.clearAds();
    }

    public void destroy() {
        this.mStreamAdPlacer.destroy();
        this.mVisibilityTracker.destroy();
    }

    public boolean areAllItemsEnabled() {
        return (this.mOriginalAdapter instanceof ListAdapter) && ((ListAdapter) this.mOriginalAdapter).areAllItemsEnabled();
    }

    public boolean isEnabled(int position) {
        return isAd(position) || ((this.mOriginalAdapter instanceof ListAdapter) && ((ListAdapter) this.mOriginalAdapter).isEnabled(this.mStreamAdPlacer.getOriginalPosition(position)));
    }

    public int getCount() {
        return this.mStreamAdPlacer.getAdjustedCount(this.mOriginalAdapter.getCount());
    }

    @Nullable
    public Object getItem(int position) {
        Object adData = this.mStreamAdPlacer.getAdData(position);
        return adData != null ? adData : this.mOriginalAdapter.getItem(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public long getItemId(int position) {
        Object adData = this.mStreamAdPlacer.getAdData(position);
        if (adData != null) {
            return (long) (-System.identityHashCode(adData));
        }
        return this.mOriginalAdapter.getItemId(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public boolean hasStableIds() {
        return this.mOriginalAdapter.hasStableIds();
    }

    @Nullable
    public View getView(int position, View view, ViewGroup viewGroup) {
        Object adView = this.mStreamAdPlacer.getAdView(position, view, viewGroup);
        if (adView == null) {
            adView = this.mOriginalAdapter.getView(this.mStreamAdPlacer.getOriginalPosition(position), view, viewGroup);
        }
        this.mViewPositionMap.put(adView, Integer.valueOf(position));
        this.mVisibilityTracker.addView(adView, 0);
        return adView;
    }

    public int getItemViewType(int position) {
        int adViewType = this.mStreamAdPlacer.getAdViewType(position);
        if (adViewType != 0) {
            return (adViewType + this.mOriginalAdapter.getViewTypeCount()) - 1;
        }
        return this.mOriginalAdapter.getItemViewType(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public int getViewTypeCount() {
        return this.mOriginalAdapter.getViewTypeCount() + this.mStreamAdPlacer.getAdViewTypeCount();
    }

    public boolean isEmpty() {
        return this.mOriginalAdapter.isEmpty() && this.mStreamAdPlacer.getAdjustedCount(0) == 0;
    }

    /* access modifiers changed from: private */
    public void handleVisibilityChange(@NonNull List<View> visibleViews) {
        int i = MoPubClientPositioning.NO_REPEAT;
        int i2 = 0;
        for (View view : visibleViews) {
            Integer num = (Integer) this.mViewPositionMap.get(view);
            if (num != null) {
                i = Math.min(num.intValue(), i);
                i2 = Math.max(num.intValue(), i2);
            }
        }
        this.mStreamAdPlacer.placeAdsInRange(i, i2 + 1);
    }

    public int getOriginalPosition(int position) {
        return this.mStreamAdPlacer.getOriginalPosition(position);
    }

    public int getAdjustedPosition(int originalPosition) {
        return this.mStreamAdPlacer.getAdjustedPosition(originalPosition);
    }

    public void insertItem(int originalPosition) {
        this.mStreamAdPlacer.insertItem(originalPosition);
    }

    public void removeItem(int originalPosition) {
        this.mStreamAdPlacer.removeItem(originalPosition);
    }

    public void setOnClickListener(@NonNull ListView listView, @Nullable final OnItemClickListener listener) {
        if (!NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.setOnClickListener with a null ListView")) {
            return;
        }
        if (listener == null) {
            listView.setOnItemClickListener(null);
        } else {
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (!MoPubAdAdapter.this.mStreamAdPlacer.isAd(position)) {
                        listener.onItemClick(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id);
                    }
                }
            });
        }
    }

    public void setOnItemLongClickListener(@NonNull ListView listView, @Nullable final OnItemLongClickListener listener) {
        if (!NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.setOnItemLongClickListener with a null ListView")) {
            return;
        }
        if (listener == null) {
            listView.setOnItemLongClickListener(null);
        } else {
            listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (!MoPubAdAdapter.this.isAd(position)) {
                        if (!listener.onItemLongClick(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id)) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
    }

    public void setOnItemSelectedListener(@NonNull ListView listView, @Nullable final OnItemSelectedListener listener) {
        if (!NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.setOnItemSelectedListener with a null ListView")) {
            return;
        }
        if (listener == null) {
            listView.setOnItemSelectedListener(null);
        } else {
            listView.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (!MoPubAdAdapter.this.isAd(position)) {
                        listener.onItemSelected(adapterView, view, MoPubAdAdapter.this.mStreamAdPlacer.getOriginalPosition(position), id);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    listener.onNothingSelected(adapterView);
                }
            });
        }
    }

    public void setSelection(@NonNull ListView listView, int originalPosition) {
        if (NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.setSelection with a null ListView")) {
            listView.setSelection(this.mStreamAdPlacer.getAdjustedPosition(originalPosition));
        }
    }

    public void smoothScrollToPosition(@NonNull ListView listView, int originalPosition) {
        if (NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.smoothScrollToPosition with a null ListView")) {
            listView.smoothScrollToPosition(this.mStreamAdPlacer.getAdjustedPosition(originalPosition));
        }
    }

    public void refreshAds(@NonNull ListView listView, @NonNull String adUnitId) {
        refreshAds(listView, adUnitId, null);
    }

    public void refreshAds(@NonNull ListView listView, @NonNull String adUnitId, @Nullable RequestParameters requestParameters) {
        if (NoThrow.checkNotNull(listView, "You called MoPubAdAdapter.refreshAds with a null ListView")) {
            View childAt = listView.getChildAt(0);
            int top = childAt == null ? 0 : childAt.getTop();
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            int max = Math.max(firstVisiblePosition - 1, 0);
            while (this.mStreamAdPlacer.isAd(max) && max > 0) {
                max--;
            }
            int lastVisiblePosition = listView.getLastVisiblePosition();
            while (this.mStreamAdPlacer.isAd(lastVisiblePosition) && lastVisiblePosition < getCount() - 1) {
                lastVisiblePosition++;
            }
            max = this.mStreamAdPlacer.getOriginalPosition(max);
            this.mStreamAdPlacer.removeAdsInRange(this.mStreamAdPlacer.getOriginalCount(lastVisiblePosition + 1), this.mStreamAdPlacer.getOriginalCount(getCount()));
            int removeAdsInRange = this.mStreamAdPlacer.removeAdsInRange(0, max);
            if (removeAdsInRange > 0) {
                listView.setSelectionFromTop(firstVisiblePosition - removeAdsInRange, top);
            }
            loadAds(adUnitId, requestParameters);
        }
    }

    public boolean hasReceivedAds() {
        return this.mStreamAdPlacer.hasReceivedAds();
    }
}
