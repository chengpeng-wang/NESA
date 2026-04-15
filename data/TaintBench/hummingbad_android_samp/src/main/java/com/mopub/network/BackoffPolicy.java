package com.mopub.network;

import com.mopub.volley.VolleyError;

public abstract class BackoffPolicy {
    protected int mBackoffMs;
    protected int mBackoffMultiplier;
    protected int mDefaultBackoffTimeMs;
    protected int mMaxBackoffTimeMs;
    protected int mMaxRetries;
    protected int mRetryCount;

    public abstract void backoff(VolleyError volleyError) throws VolleyError;

    public int getBackoffMs() {
        return this.mBackoffMs;
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public boolean hasAttemptRemaining() {
        return this.mRetryCount < this.mMaxRetries;
    }
}
