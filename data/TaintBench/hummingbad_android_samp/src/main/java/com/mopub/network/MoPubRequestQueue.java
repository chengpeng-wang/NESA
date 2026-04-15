package com.mopub.network;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.volley.Cache;
import com.mopub.volley.Network;
import com.mopub.volley.Request;
import com.mopub.volley.RequestQueue;
import com.mopub.volley.RequestQueue.RequestFilter;
import com.mopub.volley.ResponseDelivery;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MoPubRequestQueue extends RequestQueue {
    private static final int CAPACITY = 10;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Map<Request<?>, DelayedRequestHelper> mDelayedRequests = new HashMap(10);

    class DelayedRequestHelper {
        final int mDelayMs;
        @NonNull
        final Runnable mDelayedRunnable;
        @NonNull
        final Handler mHandler;

        DelayedRequestHelper(MoPubRequestQueue moPubRequestQueue, @NonNull Request<?> request, int delayMs) {
            this(request, delayMs, new Handler());
        }

        @VisibleForTesting
        DelayedRequestHelper(final Request<?> request, @NonNull int delayMs, Handler handler) {
            this.mDelayMs = delayMs;
            this.mHandler = handler;
            this.mDelayedRunnable = new Runnable(MoPubRequestQueue.this) {
                public void run() {
                    MoPubRequestQueue.this.mDelayedRequests.remove(request);
                    MoPubRequestQueue.this.add(request);
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public void start() {
            this.mHandler.postDelayed(this.mDelayedRunnable, (long) this.mDelayMs);
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            this.mHandler.removeCallbacks(this.mDelayedRunnable);
        }
    }

    MoPubRequestQueue(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
        super(cache, network, threadPoolSize, delivery);
    }

    MoPubRequestQueue(Cache cache, Network network, int threadPoolSize) {
        super(cache, network, threadPoolSize);
    }

    MoPubRequestQueue(Cache cache, Network network) {
        super(cache, network);
    }

    public void addDelayedRequest(@NonNull Request<?> request, int delayMs) {
        Preconditions.checkNotNull(request);
        addDelayedRequest((Request) request, new DelayedRequestHelper(this, request, delayMs));
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void addDelayedRequest(@NonNull Request<?> request, @NonNull DelayedRequestHelper delayedRequestHelper) {
        Preconditions.checkNotNull(delayedRequestHelper);
        if (this.mDelayedRequests.containsKey(request)) {
            cancel(request);
        }
        delayedRequestHelper.start();
        this.mDelayedRequests.put(request, delayedRequestHelper);
    }

    public void cancelAll(@NonNull RequestFilter filter) {
        Preconditions.checkNotNull(filter);
        super.cancelAll(filter);
        Iterator it = this.mDelayedRequests.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (filter.apply((Request) entry.getKey())) {
                ((Request) entry.getKey()).cancel();
                ((DelayedRequestHelper) entry.getValue()).cancel();
                it.remove();
            }
        }
    }

    public void cancelAll(@NonNull final Object tag) {
        Preconditions.checkNotNull(tag);
        super.cancelAll(tag);
        cancelAll(new RequestFilter() {
            public boolean apply(Request<?> request) {
                return request.getTag() == tag;
            }
        });
    }

    public void cancel(@NonNull final Request<?> request) {
        Preconditions.checkNotNull(request);
        cancelAll(new RequestFilter() {
            public boolean apply(Request<?> _request) {
                return request == _request;
            }
        });
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @Deprecated
    @VisibleForTesting
    public Map<Request<?>, DelayedRequestHelper> getDelayedRequests() {
        return this.mDelayedRequests;
    }
}
