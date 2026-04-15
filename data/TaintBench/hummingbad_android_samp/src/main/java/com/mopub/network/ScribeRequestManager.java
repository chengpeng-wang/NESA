package com.mopub.network;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.ScribeRequest.Listener;
import com.mopub.network.ScribeRequest.ScribeRequestFactory;
import com.mopub.volley.Request;
import com.mopub.volley.VolleyError;

public class ScribeRequestManager extends RequestManager<ScribeRequestFactory> implements Listener {
    public ScribeRequestManager(Looper looper) {
        super(looper);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Request<?> createRequest() {
        return ((ScribeRequestFactory) this.mRequestFactory).createRequest(this);
    }

    public void onResponse() {
        MoPubLog.d("Successfully scribed events");
        this.mHandler.post(new Runnable() {
            public void run() {
                ScribeRequestManager.this.clearRequest();
            }
        });
    }

    public void onErrorResponse(final VolleyError volleyError) {
        this.mHandler.post(new Runnable() {
            public void run() {
                try {
                    ScribeRequestManager.this.mBackoffPolicy.backoff(volleyError);
                    ScribeRequestManager.this.makeRequestInternal();
                } catch (VolleyError e) {
                    MoPubLog.d("Failed to Scribe events: " + volleyError);
                    ScribeRequestManager.this.clearRequest();
                }
            }
        });
    }
}
