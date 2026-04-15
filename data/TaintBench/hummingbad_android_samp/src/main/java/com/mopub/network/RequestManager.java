package com.mopub.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.volley.Request;

public abstract class RequestManager<T extends RequestFactory> {
    @Nullable
    protected BackoffPolicy mBackoffPolicy;
    @Nullable
    protected Request<?> mCurrentRequest;
    @NonNull
    protected Handler mHandler;
    @Nullable
    protected T mRequestFactory;

    public interface RequestFactory {
    }

    @NonNull
    public abstract Request<?> createRequest();

    public RequestManager(@NonNull Looper looper) {
        this.mHandler = new Handler(looper);
    }

    public boolean isAtCapacity() {
        return this.mCurrentRequest != null;
    }

    public void makeRequest(@NonNull T requestFactory, @NonNull BackoffPolicy backoffPolicy) {
        Preconditions.checkNotNull(requestFactory);
        Preconditions.checkNotNull(backoffPolicy);
        cancelRequest();
        this.mRequestFactory = requestFactory;
        this.mBackoffPolicy = backoffPolicy;
        makeRequestInternal();
    }

    public void cancelRequest() {
        MoPubRequestQueue requestQueue = Networking.getRequestQueue();
        if (!(requestQueue == null || this.mCurrentRequest == null)) {
            requestQueue.cancel(this.mCurrentRequest);
        }
        clearRequest();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void makeRequestInternal() {
        this.mCurrentRequest = createRequest();
        MoPubRequestQueue requestQueue = Networking.getRequestQueue();
        if (requestQueue == null) {
            MoPubLog.d("MoPubRequest queue is null. Clearing request.");
            clearRequest();
        } else if (this.mBackoffPolicy.getRetryCount() == 0) {
            requestQueue.add(this.mCurrentRequest);
        } else {
            requestQueue.addDelayedRequest(this.mCurrentRequest, this.mBackoffPolicy.getBackoffMs());
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void clearRequest() {
        this.mCurrentRequest = null;
        this.mRequestFactory = null;
        this.mBackoffPolicy = null;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public Request<?> getCurrentRequest() {
        return this.mCurrentRequest;
    }
}
