package com.mopub.network;

import android.support.annotation.NonNull;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent;
import com.mopub.common.event.EventSerializer;
import com.mopub.network.RequestManager.RequestFactory;
import com.mopub.volley.DefaultRetryPolicy;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.Request;
import com.mopub.volley.Response;
import com.mopub.volley.Response.ErrorListener;
import com.mopub.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

public class ScribeRequest extends Request<Void> {
    @NonNull
    private final EventSerializer mEventSerializer;
    @NonNull
    private final List<BaseEvent> mEvents;
    @NonNull
    private final Listener mListener;

    public interface Listener extends ErrorListener {
        void onResponse();
    }

    public interface ScribeRequestFactory extends RequestFactory {
        ScribeRequest createRequest(Listener listener);
    }

    public ScribeRequest(@NonNull String url, @NonNull List<BaseEvent> events, @NonNull EventSerializer eventSerializer, @NonNull Listener listener) {
        super(1, url, listener);
        this.mEvents = events;
        this.mEventSerializer = eventSerializer;
        this.mListener = listener;
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy());
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getParams() {
        JSONArray serializeAsJson = this.mEventSerializer.serializeAsJson(this.mEvents);
        HashMap hashMap = new HashMap();
        hashMap.put("log", serializeAsJson.toString());
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(Void aVoid) {
        this.mListener.onResponse();
    }

    @NonNull
    @Deprecated
    @VisibleForTesting
    public List<BaseEvent> getEvents() {
        return this.mEvents;
    }
}
