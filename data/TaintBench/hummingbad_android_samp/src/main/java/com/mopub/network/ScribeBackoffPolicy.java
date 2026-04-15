package com.mopub.network;

import com.mopub.common.VisibleForTesting;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.NoConnectionError;
import com.mopub.volley.VolleyError;

public class ScribeBackoffPolicy extends BackoffPolicy {
    private static final int BACKOFF_MULTIPLIER = 2;
    private static final int DEFAULT_BACKOFF_TIME_MS = 60000;
    private static final int MAX_RETRIES = 5;

    public ScribeBackoffPolicy() {
        this(DEFAULT_BACKOFF_TIME_MS, 5, 2);
    }

    @VisibleForTesting
    ScribeBackoffPolicy(int defaultBackoffTimeMs, int maxRetries, int backoffMultiplier) {
        this.mDefaultBackoffTimeMs = defaultBackoffTimeMs;
        this.mMaxRetries = maxRetries;
        this.mBackoffMultiplier = backoffMultiplier;
    }

    public void backoff(VolleyError volleyError) throws VolleyError {
        if (!hasAttemptRemaining()) {
            throw volleyError;
        } else if (volleyError instanceof NoConnectionError) {
            updateBackoffTime();
        } else {
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse == null || !(networkResponse.statusCode == 503 || networkResponse.statusCode == 504)) {
                throw volleyError;
            }
            updateBackoffTime();
        }
    }

    private void updateBackoffTime() {
        this.mBackoffMs = (int) (Math.pow((double) this.mBackoffMultiplier, (double) this.mRetryCount) * ((double) this.mDefaultBackoffTimeMs));
        this.mRetryCount++;
    }
}
