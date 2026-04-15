package com.mopub.nativeads;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubServerPositioning;
import java.util.List;
import java.util.WeakHashMap;

public final class MoPubRecyclerAdapter extends Adapter<ViewHolder> {
    static final int NATIVE_AD_VIEW_TYPE_BASE = -56;
    @Nullable
    private MoPubNativeAdLoadedListener mAdLoadedListener;
    @NonNull
    private final AdapterDataObserver mAdapterDataObserver;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Adapter mOriginalAdapter;
    @Nullable
    private RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    @NonNull
    public ContentChangeStrategy mStrategy;
    /* access modifiers changed from: private|final */
    @NonNull
    public final MoPubStreamAdPlacer mStreamAdPlacer;
    @NonNull
    private final WeakHashMap<View, Integer> mViewPositionMap;
    @NonNull
    private final VisibilityTracker mVisibilityTracker;

    public enum ContentChangeStrategy {
        INSERT_AT_END,
        MOVE_ALL_ADS_WITH_CONTENT,
        KEEP_ADS_FIXED
    }

    public MoPubRecyclerAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter) {
        this(activity, originalAdapter, MoPubNativeAdPositioning.serverPositioning());
    }

    public MoPubRecyclerAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter, @NonNull MoPubServerPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(activity, adPositioning), originalAdapter, new VisibilityTracker(activity));
    }

    public MoPubRecyclerAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter, @NonNull MoPubClientPositioning adPositioning) {
        this(new MoPubStreamAdPlacer(activity, adPositioning), originalAdapter, new VisibilityTracker(activity));
    }

    @VisibleForTesting
    MoPubRecyclerAdapter(@NonNull MoPubStreamAdPlacer streamAdPlacer, @NonNull Adapter originalAdapter, @NonNull VisibilityTracker visibilityTracker) {
        this.mStrategy = ContentChangeStrategy.INSERT_AT_END;
        this.mViewPositionMap = new WeakHashMap();
        this.mOriginalAdapter = originalAdapter;
        this.mVisibilityTracker = visibilityTracker;
        this.mVisibilityTracker.setVisibilityTrackerListener(new VisibilityTrackerListener() {
            public void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews) {
                MoPubRecyclerAdapter.this.handleVisibilityChanged(visibleViews, invisibleViews);
            }
        });
        setHasStableIdsInternal(this.mOriginalAdapter.hasStableIds());
        this.mStreamAdPlacer = streamAdPlacer;
        this.mStreamAdPlacer.setAdLoadedListener(new MoPubNativeAdLoadedListener() {
            public void onAdLoaded(int position) {
                MoPubRecyclerAdapter.this.handleAdLoaded(position);
            }

            public void onAdRemoved(int position) {
                MoPubRecyclerAdapter.this.handleAdRemoved(position);
            }
        });
        this.mStreamAdPlacer.setItemCount(this.mOriginalAdapter.getItemCount());
        this.mAdapterDataObserver = new AdapterDataObserver() {
            public void onChanged() {
                MoPubRecyclerAdapter.this.mStreamAdPlacer.setItemCount(MoPubRecyclerAdapter.this.mOriginalAdapter.getItemCount());
                MoPubRecyclerAdapter.this.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                int adjustedPosition = MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedPosition((positionStart + itemCount) - 1);
                int adjustedPosition2 = MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedPosition(positionStart);
                MoPubRecyclerAdapter.this.notifyItemRangeChanged(adjustedPosition2, (adjustedPosition - adjustedPosition2) + 1);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                int i = 0;
                int adjustedPosition = MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedPosition(positionStart);
                int itemCount2 = MoPubRecyclerAdapter.this.mOriginalAdapter.getItemCount();
                MoPubRecyclerAdapter.this.mStreamAdPlacer.setItemCount(itemCount2);
                itemCount2 = positionStart + itemCount >= itemCount2 ? 1 : 0;
                if (ContentChangeStrategy.KEEP_ADS_FIXED == MoPubRecyclerAdapter.this.mStrategy || (ContentChangeStrategy.INSERT_AT_END == MoPubRecyclerAdapter.this.mStrategy && itemCount2 != 0)) {
                    MoPubRecyclerAdapter.this.notifyDataSetChanged();
                    return;
                }
                while (i < itemCount) {
                    MoPubRecyclerAdapter.this.mStreamAdPlacer.insertItem(positionStart);
                    i++;
                }
                MoPubRecyclerAdapter.this.notifyItemRangeInserted(adjustedPosition, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemsRemoved) {
                int i = 0;
                int adjustedPosition = MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedPosition(positionStart);
                int itemCount = MoPubRecyclerAdapter.this.mOriginalAdapter.getItemCount();
                MoPubRecyclerAdapter.this.mStreamAdPlacer.setItemCount(itemCount);
                int i2 = positionStart + itemsRemoved >= itemCount ? 1 : 0;
                if (ContentChangeStrategy.KEEP_ADS_FIXED == MoPubRecyclerAdapter.this.mStrategy || (ContentChangeStrategy.INSERT_AT_END == MoPubRecyclerAdapter.this.mStrategy && i2 != 0)) {
                    MoPubRecyclerAdapter.this.notifyDataSetChanged();
                    return;
                }
                i2 = MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedCount(itemCount + itemsRemoved);
                while (i < itemsRemoved) {
                    MoPubRecyclerAdapter.this.mStreamAdPlacer.removeItem(positionStart);
                    i++;
                }
                i2 -= MoPubRecyclerAdapter.this.mStreamAdPlacer.getAdjustedCount(itemCount);
                MoPubRecyclerAdapter.this.notifyItemRangeRemoved(adjustedPosition - (i2 - itemsRemoved), i2);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                MoPubRecyclerAdapter.this.notifyDataSetChanged();
            }
        };
        this.mOriginalAdapter.registerAdapterDataObserver(this.mAdapterDataObserver);
    }

    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        MoPubRecyclerAdapter.super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        MoPubRecyclerAdapter.super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
    }

    public final void setAdLoadedListener(@Nullable MoPubNativeAdLoadedListener listener) {
        this.mAdLoadedListener = listener;
    }

    public final void registerAdRenderer(@NonNull MoPubAdRenderer adRenderer) {
        if (NoThrow.checkNotNull(adRenderer, "Cannot register a null adRenderer")) {
            this.mStreamAdPlacer.registerAdRenderer(adRenderer);
        }
    }

    public final void loadAds(@NonNull String adUnitId) {
        this.mStreamAdPlacer.loadAds(adUnitId);
    }

    public final void loadAds(@NonNull String adUnitId, @Nullable RequestParameters requestParameters) {
        this.mStreamAdPlacer.loadAds(adUnitId, requestParameters);
    }

    public static int computeScrollOffset(@NonNull LinearLayoutManager linearLayoutManager, @Nullable ViewHolder holder) {
        if (holder == null) {
            return 0;
        }
        View view = holder.itemView;
        if (linearLayoutManager.canScrollVertically()) {
            if (linearLayoutManager.getStackFromEnd()) {
                return view.getBottom();
            }
            return view.getTop();
        } else if (!linearLayoutManager.canScrollHorizontally()) {
            return 0;
        } else {
            if (linearLayoutManager.getStackFromEnd()) {
                return view.getRight();
            }
            return view.getLeft();
        }
    }

    public final void refreshAds(@NonNull String adUnitId) {
        refreshAds(adUnitId, null);
    }

    public final void refreshAds(@NonNull String adUnitId, @Nullable RequestParameters requestParameters) {
        if (this.mRecyclerView == null) {
            MoPubLog.w("This adapter is not attached to a RecyclerView and cannot be refreshed.");
            return;
        }
        LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            MoPubLog.w("Can't refresh ads when there is no layout manager on a RecyclerView.");
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int computeScrollOffset = computeScrollOffset(linearLayoutManager, this.mRecyclerView.findViewHolderForLayoutPosition(findFirstVisibleItemPosition));
            int max = Math.max(0, findFirstVisibleItemPosition - 1);
            while (this.mStreamAdPlacer.isAd(max) && max > 0) {
                max--;
            }
            int itemCount = getItemCount();
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            while (this.mStreamAdPlacer.isAd(findLastVisibleItemPosition) && findLastVisibleItemPosition < itemCount - 1) {
                findLastVisibleItemPosition++;
            }
            max = this.mStreamAdPlacer.getOriginalPosition(max);
            this.mStreamAdPlacer.removeAdsInRange(this.mStreamAdPlacer.getOriginalPosition(findLastVisibleItemPosition), this.mOriginalAdapter.getItemCount());
            max = this.mStreamAdPlacer.removeAdsInRange(0, max);
            if (max > 0) {
                linearLayoutManager.scrollToPositionWithOffset(findFirstVisibleItemPosition - max, computeScrollOffset);
            }
            loadAds(adUnitId, requestParameters);
        } else {
            MoPubLog.w("This LayoutManager can't be refreshed.");
        }
    }

    public final void clearAds() {
        this.mStreamAdPlacer.clearAds();
    }

    public final boolean isAd(int position) {
        return this.mStreamAdPlacer.isAd(position);
    }

    public final int getAdjustedPosition(int originalPosition) {
        return this.mStreamAdPlacer.getAdjustedPosition(originalPosition);
    }

    public final int getOriginalPosition(int position) {
        return this.mStreamAdPlacer.getOriginalPosition(position);
    }

    public final void setContentChangeStrategy(@NonNull ContentChangeStrategy strategy) {
        if (NoThrow.checkNotNull(strategy)) {
            this.mStrategy = strategy;
        }
    }

    public final int getItemCount() {
        return this.mStreamAdPlacer.getAdjustedCount(this.mOriginalAdapter.getItemCount());
    }

    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < NATIVE_AD_VIEW_TYPE_BASE || viewType > this.mStreamAdPlacer.getAdViewTypeCount() + NATIVE_AD_VIEW_TYPE_BASE) {
            return this.mOriginalAdapter.onCreateViewHolder(parent, viewType);
        }
        MoPubAdRenderer adRendererForViewType = this.mStreamAdPlacer.getAdRendererForViewType(viewType + 56);
        if (adRendererForViewType != null) {
            return new MoPubRecyclerViewHolder(adRendererForViewType.createAdView((Activity) parent.getContext(), parent));
        }
        MoPubLog.w("No view binder was registered for ads in MoPubRecyclerAdapter.");
        return null;
    }

    public final void onBindViewHolder(ViewHolder holder, int position) {
        Object adData = this.mStreamAdPlacer.getAdData(position);
        if (adData != null) {
            this.mStreamAdPlacer.bindAdView((NativeAd) adData, holder.itemView);
            return;
        }
        this.mViewPositionMap.put(holder.itemView, Integer.valueOf(position));
        this.mVisibilityTracker.addView(holder.itemView, 0);
        this.mOriginalAdapter.onBindViewHolder(holder, this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public final int getItemViewType(int position) {
        int adViewType = this.mStreamAdPlacer.getAdViewType(position);
        if (adViewType != 0) {
            return adViewType + NATIVE_AD_VIEW_TYPE_BASE;
        }
        return this.mOriginalAdapter.getItemViewType(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public final void setHasStableIds(boolean hasStableIds) {
        setHasStableIdsInternal(hasStableIds);
        this.mOriginalAdapter.unregisterAdapterDataObserver(this.mAdapterDataObserver);
        this.mOriginalAdapter.setHasStableIds(hasStableIds);
        this.mOriginalAdapter.registerAdapterDataObserver(this.mAdapterDataObserver);
    }

    public final void destroy() {
        this.mOriginalAdapter.unregisterAdapterDataObserver(this.mAdapterDataObserver);
        this.mStreamAdPlacer.destroy();
        this.mVisibilityTracker.destroy();
    }

    public final long getItemId(int position) {
        if (!this.mOriginalAdapter.hasStableIds()) {
            return -1;
        }
        Object adData = this.mStreamAdPlacer.getAdData(position);
        if (adData != null) {
            return (long) (-System.identityHashCode(adData));
        }
        return this.mOriginalAdapter.getItemId(this.mStreamAdPlacer.getOriginalPosition(position));
    }

    public final boolean onFailedToRecycleView(ViewHolder holder) {
        if (holder instanceof MoPubRecyclerViewHolder) {
            return MoPubRecyclerAdapter.super.onFailedToRecycleView(holder);
        }
        return this.mOriginalAdapter.onFailedToRecycleView(holder);
    }

    public final void onViewAttachedToWindow(ViewHolder holder) {
        if (holder instanceof MoPubRecyclerViewHolder) {
            MoPubRecyclerAdapter.super.onViewAttachedToWindow(holder);
        } else {
            this.mOriginalAdapter.onViewAttachedToWindow(holder);
        }
    }

    public final void onViewDetachedFromWindow(ViewHolder holder) {
        if (holder instanceof MoPubRecyclerViewHolder) {
            MoPubRecyclerAdapter.super.onViewDetachedFromWindow(holder);
        } else {
            this.mOriginalAdapter.onViewDetachedFromWindow(holder);
        }
    }

    public final void onViewRecycled(ViewHolder holder) {
        if (holder instanceof MoPubRecyclerViewHolder) {
            MoPubRecyclerAdapter.super.onViewRecycled(holder);
        } else {
            this.mOriginalAdapter.onViewRecycled(holder);
        }
    }

    /* access modifiers changed from: final */
    @VisibleForTesting
    public final void handleAdLoaded(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdLoaded(position);
        }
        notifyItemInserted(position);
    }

    /* access modifiers changed from: final */
    @VisibleForTesting
    public final void handleAdRemoved(int position) {
        if (this.mAdLoadedListener != null) {
            this.mAdLoadedListener.onAdRemoved(position);
        }
        notifyItemRemoved(position);
    }

    /* access modifiers changed from: private */
    public void handleVisibilityChanged(List<View> visibleViews, List<View> list) {
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

    private void setHasStableIdsInternal(boolean hasStableIds) {
        MoPubRecyclerAdapter.super.setHasStableIds(hasStableIds);
    }
}
