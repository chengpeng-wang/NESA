package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import java.util.Map;
import java.util.TreeMap;

public class AdRequestStatusMapping {
    @NonNull
    private final Map<String, AdRequestStatus> mAdUnitToAdRequestStatus = new TreeMap();

    private static class AdRequestStatus {
        @Nullable
        private String mClickUrl;
        @Nullable
        private String mFailUrl;
        @Nullable
        private String mImpressionUrl;
        @NonNull
        private LoadingStatus mLoadingStatus;

        public AdRequestStatus(@NonNull LoadingStatus loadingStatus) {
            this(loadingStatus, null, null, null);
        }

        public AdRequestStatus(@NonNull LoadingStatus loadingStatus, @Nullable String failUrl, @Nullable String impressionUrl, @Nullable String clickUrl) {
            Preconditions.checkNotNull(loadingStatus);
            this.mLoadingStatus = loadingStatus;
            this.mFailUrl = failUrl;
            this.mImpressionUrl = impressionUrl;
            this.mClickUrl = clickUrl;
        }

        /* access modifiers changed from: private */
        @NonNull
        public LoadingStatus getStatus() {
            return this.mLoadingStatus;
        }

        /* access modifiers changed from: private */
        public void setStatus(@NonNull LoadingStatus loadingStatus) {
            this.mLoadingStatus = loadingStatus;
        }

        /* access modifiers changed from: private */
        @Nullable
        public String getFailurl() {
            return this.mFailUrl;
        }

        /* access modifiers changed from: private */
        @Nullable
        public String getImpressionUrl() {
            return this.mImpressionUrl;
        }

        /* access modifiers changed from: private */
        public void setImpressionUrl(@Nullable String impressionUrl) {
            this.mImpressionUrl = impressionUrl;
        }

        /* access modifiers changed from: private */
        @Nullable
        public String getClickUrl() {
            return this.mClickUrl;
        }

        /* access modifiers changed from: private */
        public void setClickUrl(@Nullable String clickUrl) {
            this.mClickUrl = clickUrl;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (!(o instanceof AdRequestStatus)) {
                return false;
            }
            AdRequestStatus adRequestStatus = (AdRequestStatus) o;
            if (this.mLoadingStatus.equals(adRequestStatus.mLoadingStatus) && TextUtils.equals(this.mFailUrl, adRequestStatus.mFailUrl) && TextUtils.equals(this.mImpressionUrl, adRequestStatus.mImpressionUrl) && TextUtils.equals(this.mClickUrl, adRequestStatus.mClickUrl)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hashCode;
            int i = 0;
            int hashCode2 = ((this.mFailUrl != null ? this.mFailUrl.hashCode() : 0) + ((this.mLoadingStatus.ordinal() + 899) * 31)) * 31;
            if (this.mImpressionUrl != null) {
                hashCode = this.mImpressionUrl.hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (hashCode + hashCode2) * 31;
            if (this.mClickUrl != null) {
                i = this.mClickUrl.hashCode();
            }
            return hashCode + i;
        }
    }

    private enum LoadingStatus {
        LOADING,
        LOADED,
        PLAYED
    }

    /* access modifiers changed from: 0000 */
    public void markFail(@NonNull String adUnitId) {
        this.mAdUnitToAdRequestStatus.remove(adUnitId);
    }

    /* access modifiers changed from: 0000 */
    public void markLoading(@NonNull String adUnitId) {
        this.mAdUnitToAdRequestStatus.put(adUnitId, new AdRequestStatus(LoadingStatus.LOADING));
    }

    /* access modifiers changed from: 0000 */
    public void markLoaded(@NonNull String adUnitId, @Nullable String failUrlString, @Nullable String impressionTrackerUrlString, @Nullable String clickTrackerUrlString) {
        this.mAdUnitToAdRequestStatus.put(adUnitId, new AdRequestStatus(LoadingStatus.LOADED, failUrlString, impressionTrackerUrlString, clickTrackerUrlString));
    }

    /* access modifiers changed from: 0000 */
    public void markPlayed(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).setStatus(LoadingStatus.PLAYED);
        } else {
            this.mAdUnitToAdRequestStatus.put(adUnitId, new AdRequestStatus(LoadingStatus.PLAYED));
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean canPlay(@NonNull String adUnitId) {
        AdRequestStatus adRequestStatus = (AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId);
        return adRequestStatus != null && LoadingStatus.LOADED.equals(adRequestStatus.getStatus());
    }

    /* access modifiers changed from: 0000 */
    public boolean isLoading(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            return ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).getStatus() == LoadingStatus.LOADING;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getFailoverUrl(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            return ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).getFailurl();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getImpressionTrackerUrlString(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            return ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).getImpressionUrl();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getClickTrackerUrlString(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            return ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).getClickUrl();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void clearImpressionUrl(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).setImpressionUrl(null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearClickUrl(@NonNull String adUnitId) {
        if (this.mAdUnitToAdRequestStatus.containsKey(adUnitId)) {
            ((AdRequestStatus) this.mAdUnitToAdRequestStatus.get(adUnitId)).setClickUrl(null);
        }
    }
}
