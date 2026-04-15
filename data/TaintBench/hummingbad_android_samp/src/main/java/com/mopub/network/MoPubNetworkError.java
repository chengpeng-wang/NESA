package com.mopub.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.VolleyError;

public class MoPubNetworkError extends VolleyError {
    @NonNull
    private final Reason mReason;
    @Nullable
    private final Integer mRefreshTimeMillis;

    public enum Reason {
        WARMING_UP,
        NO_FILL,
        BAD_HEADER_DATA,
        BAD_BODY,
        TRACKING_FAILURE,
        UNSPECIFIED
    }

    public MoPubNetworkError(@NonNull Reason reason) {
        this.mReason = reason;
        this.mRefreshTimeMillis = null;
    }

    public MoPubNetworkError(@NonNull NetworkResponse networkResponse, @NonNull Reason reason) {
        super(networkResponse);
        this.mReason = reason;
        this.mRefreshTimeMillis = null;
    }

    public MoPubNetworkError(@NonNull Throwable cause, @NonNull Reason reason) {
        super(cause);
        this.mReason = reason;
        this.mRefreshTimeMillis = null;
    }

    public MoPubNetworkError(@NonNull String message, @NonNull Reason reason) {
        this(message, reason, null);
    }

    public MoPubNetworkError(@NonNull String message, @NonNull Throwable cause, @NonNull Reason reason) {
        super(message, cause);
        this.mReason = reason;
        this.mRefreshTimeMillis = null;
    }

    public MoPubNetworkError(@NonNull String message, @NonNull Reason reason, @Nullable Integer refreshTimeMillis) {
        super(message);
        this.mReason = reason;
        this.mRefreshTimeMillis = refreshTimeMillis;
    }

    @NonNull
    public Reason getReason() {
        return this.mReason;
    }

    @Nullable
    public Integer getRefreshTimeMillis() {
        return this.mRefreshTimeMillis;
    }
}
