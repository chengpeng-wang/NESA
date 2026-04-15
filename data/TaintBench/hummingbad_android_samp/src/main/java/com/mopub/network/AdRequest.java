package com.mopub.network;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.AdFormat;
import com.mopub.common.AdType;
import com.mopub.common.DataKeys;
import com.mopub.common.LocationService;
import com.mopub.common.MoPub;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.SamplingRate;
import com.mopub.common.event.Event;
import com.mopub.common.event.EventDetails;
import com.mopub.common.event.MoPubEvents;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Json;
import com.mopub.common.util.ResponseHeader;
import com.mopub.mobileads.AdTypeTranslator;
import com.mopub.network.AdResponse.Builder;
import com.mopub.network.MoPubNetworkError.Reason;
import com.mopub.volley.DefaultRetryPolicy;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.Request;
import com.mopub.volley.Response;
import com.mopub.volley.Response.ErrorListener;
import com.mopub.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;

public class AdRequest extends Request<AdResponse> {
    @NonNull
    private final AdFormat mAdFormat;
    @Nullable
    private final String mAdUnitId;
    @NonNull
    private final Context mContext;
    @NonNull
    private final Listener mListener;

    public interface Listener extends ErrorListener {
        void onSuccess(AdResponse adResponse);
    }

    public AdRequest(@NonNull String url, @NonNull AdFormat adFormat, @Nullable String adUnitId, @NonNull Context context, @NonNull Listener listener) {
        super(0, url, listener);
        Preconditions.checkNotNull(adFormat);
        Preconditions.checkNotNull(listener);
        this.mAdUnitId = adUnitId;
        this.mListener = listener;
        this.mAdFormat = adFormat;
        this.mContext = context.getApplicationContext();
        setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1.0f));
        setShouldCache(false);
    }

    @NonNull
    public Listener getListener() {
        return this.mListener;
    }

    public Map<String, String> getHeaders() {
        TreeMap treeMap = new TreeMap();
        String language = Locale.getDefault().getLanguage();
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (!(locale == null || locale.getLanguage().trim().isEmpty())) {
            language = locale.getLanguage().trim();
        }
        if (!language.isEmpty()) {
            treeMap.put(ResponseHeader.ACCEPT_LANGUAGE.getKey(), language);
        }
        return treeMap;
    }

    /* access modifiers changed from: protected */
    public Response<AdResponse> parseNetworkResponse(NetworkResponse networkResponse) {
        Map map = networkResponse.headers;
        if (HeaderUtils.extractBooleanHeader(map, ResponseHeader.WARMUP, false)) {
            return Response.error(new MoPubNetworkError("Ad Unit is warming up.", Reason.WARMING_UP));
        }
        Location lastKnownLocation = LocationService.getLastKnownLocation(this.mContext, MoPub.getLocationPrecision(), MoPub.getLocationAwareness());
        Builder builder = new Builder();
        builder.setAdUnitId(this.mAdUnitId);
        String extractHeader = HeaderUtils.extractHeader(map, ResponseHeader.AD_TYPE);
        String extractHeader2 = HeaderUtils.extractHeader(map, ResponseHeader.FULL_AD_TYPE);
        builder.setAdType(extractHeader);
        builder.setFullAdType(extractHeader2);
        Integer extractIntegerHeader = HeaderUtils.extractIntegerHeader(map, ResponseHeader.REFRESH_TIME);
        if (extractIntegerHeader == null) {
            extractIntegerHeader = null;
        } else {
            extractIntegerHeader = Integer.valueOf(extractIntegerHeader.intValue() * 1000);
        }
        builder.setRefreshTimeMilliseconds(extractIntegerHeader);
        if (AdType.CLEAR.equals(extractHeader)) {
            logScribeEvent(builder.build(), networkResponse, lastKnownLocation);
            return Response.error(new MoPubNetworkError("No ads found for ad unit.", Reason.NO_FILL, extractIntegerHeader));
        }
        String extractHeader3 = HeaderUtils.extractHeader(map, ResponseHeader.NETWORK_TYPE);
        builder.setNetworkType(extractHeader3);
        String extractHeader4 = HeaderUtils.extractHeader(map, ResponseHeader.REDIRECT_URL);
        builder.setRedirectUrl(extractHeader4);
        String extractHeader5 = HeaderUtils.extractHeader(map, ResponseHeader.CLICK_TRACKING_URL);
        builder.setClickTrackingUrl(extractHeader5);
        builder.setImpressionTrackingUrl(HeaderUtils.extractHeader(map, ResponseHeader.IMPRESSION_URL));
        String extractHeader6 = HeaderUtils.extractHeader(map, ResponseHeader.FAIL_URL);
        builder.setFailoverUrl(extractHeader6);
        String requestId = getRequestId(extractHeader6);
        builder.setRequestId(requestId);
        boolean extractBooleanHeader = HeaderUtils.extractBooleanHeader(map, ResponseHeader.SCROLLABLE, false);
        builder.setScrollable(Boolean.valueOf(extractBooleanHeader));
        Integer extractIntegerHeader2 = HeaderUtils.extractIntegerHeader(map, ResponseHeader.WIDTH);
        Integer extractIntegerHeader3 = HeaderUtils.extractIntegerHeader(map, ResponseHeader.HEIGHT);
        builder.setDimensions(extractIntegerHeader2, extractIntegerHeader3);
        extractIntegerHeader = HeaderUtils.extractIntegerHeader(map, ResponseHeader.AD_TIMEOUT);
        if (extractIntegerHeader == null) {
            extractIntegerHeader = null;
        } else {
            extractIntegerHeader = Integer.valueOf(extractIntegerHeader.intValue() * 1000);
        }
        builder.setAdTimeoutDelayMilliseconds(extractIntegerHeader);
        String parseStringBody = parseStringBody(networkResponse);
        builder.setResponseBody(parseStringBody);
        if (AdType.STATIC_NATIVE.equals(extractHeader) || AdType.VIDEO_NATIVE.equals(extractHeader)) {
            try {
                builder.setJsonBody(new JSONObject(parseStringBody));
            } catch (JSONException e) {
                return Response.error(new MoPubNetworkError("Failed to decode body JSON for native ad format", e, Reason.BAD_BODY));
            }
        }
        builder.setCustomEventClassName(AdTypeTranslator.getCustomEventName(this.mAdFormat, extractHeader, extractHeader2, map));
        extractHeader6 = HeaderUtils.extractHeader(map, ResponseHeader.CUSTOM_EVENT_DATA);
        if (TextUtils.isEmpty(extractHeader6)) {
            extractHeader6 = HeaderUtils.extractHeader(map, ResponseHeader.NATIVE_PARAMS);
        }
        try {
            Map jsonStringToMap = Json.jsonStringToMap(extractHeader6);
            if (extractHeader4 != null) {
                jsonStringToMap.put(DataKeys.REDIRECT_URL_KEY, extractHeader4);
            }
            if (extractHeader5 != null) {
                jsonStringToMap.put(DataKeys.CLICKTHROUGH_URL_KEY, extractHeader5);
            }
            if (eventDataIsInResponseBody(extractHeader, extractHeader2)) {
                jsonStringToMap.put(DataKeys.HTML_RESPONSE_BODY_KEY, parseStringBody);
                jsonStringToMap.put(DataKeys.SCROLLABLE_KEY, Boolean.toString(extractBooleanHeader));
                jsonStringToMap.put(DataKeys.CREATIVE_ORIENTATION_KEY, HeaderUtils.extractHeader(map, ResponseHeader.ORIENTATION));
            }
            if (AdType.VIDEO_NATIVE.equals(extractHeader)) {
                if (VERSION.SDK_INT < 16) {
                    return Response.error(new MoPubNetworkError("Native Video ads are only supported for Android API Level 16 (JellyBean) and above.", Reason.UNSPECIFIED));
                }
                Double d;
                Float f;
                jsonStringToMap.put(DataKeys.PLAY_VISIBLE_PERCENT, HeaderUtils.extractPercentHeaderString(map, ResponseHeader.PLAY_VISIBLE_PERCENT));
                jsonStringToMap.put(DataKeys.PAUSE_VISIBLE_PERCENT, HeaderUtils.extractPercentHeaderString(map, ResponseHeader.PAUSE_VISIBLE_PERCENT));
                jsonStringToMap.put(DataKeys.IMPRESSION_MIN_VISIBLE_PERCENT, HeaderUtils.extractPercentHeaderString(map, ResponseHeader.IMPRESSION_MIN_VISIBLE_PERCENT));
                jsonStringToMap.put(DataKeys.IMPRESSION_VISIBLE_MS, HeaderUtils.extractHeader(map, ResponseHeader.IMPRESSION_VISIBLE_MS));
                jsonStringToMap.put(DataKeys.MAX_BUFFER_MS, HeaderUtils.extractHeader(map, ResponseHeader.MAX_BUFFER_MS));
                EventDetails.Builder adHeightPx = new EventDetails.Builder().adUnitId(this.mAdUnitId).adType(extractHeader).adNetworkType(extractHeader3).adWidthPx(extractIntegerHeader2).adHeightPx(extractIntegerHeader3);
                if (lastKnownLocation == null) {
                    d = null;
                } else {
                    d = Double.valueOf(lastKnownLocation.getLatitude());
                }
                adHeightPx = adHeightPx.geoLatitude(d);
                if (lastKnownLocation == null) {
                    d = null;
                } else {
                    d = Double.valueOf(lastKnownLocation.getLongitude());
                }
                adHeightPx = adHeightPx.geoLongitude(d);
                if (lastKnownLocation == null) {
                    f = null;
                } else {
                    f = Float.valueOf(lastKnownLocation.getAccuracy());
                }
                builder.setEventDetails(adHeightPx.geoAccuracy(f).performanceDurationMs(Long.valueOf(networkResponse.networkTimeMs)).requestId(requestId).requestStatusCode(Integer.valueOf(networkResponse.statusCode)).requestUri(getUrl()).build());
            }
            builder.setServerExtras(jsonStringToMap);
            logScribeEvent(builder.build(), networkResponse, lastKnownLocation);
            return Response.success(builder.build(), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e2) {
            return Response.error(new MoPubNetworkError("Failed to decode server extras for custom event data.", e2, Reason.BAD_HEADER_DATA));
        }
    }

    private boolean eventDataIsInResponseBody(@Nullable String adType, @Nullable String fullAdType) {
        return AdType.MRAID.equals(adType) || AdType.HTML.equals(adType) || (AdType.INTERSTITIAL.equals(adType) && "vast".equals(fullAdType));
    }

    /* access modifiers changed from: protected */
    public String parseStringBody(NetworkResponse response) {
        try {
            return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return new String(response.data);
        }
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(AdResponse adResponse) {
        this.mListener.onSuccess(adResponse);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public String getRequestId(@Nullable String failUrl) {
        String str = null;
        if (failUrl == null) {
            return str;
        }
        try {
            return Uri.parse(failUrl).getQueryParameter("request_id");
        } catch (UnsupportedOperationException e) {
            MoPubLog.d("Unable to obtain request id from fail url.");
            return str;
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void logScribeEvent(@NonNull AdResponse adResponse, @NonNull NetworkResponse networkResponse, @Nullable Location location) {
        Double valueOf;
        Double d = null;
        Preconditions.checkNotNull(adResponse);
        Preconditions.checkNotNull(networkResponse);
        BaseEvent.Builder withAdNetworkType = new Event.Builder(Name.AD_REQUEST, Category.REQUESTS, SamplingRate.AD_REQUEST.getSamplingRate()).withAdUnitId(this.mAdUnitId).withAdCreativeId(adResponse.getDspCreativeId()).withAdType(adResponse.getAdType()).withAdNetworkType(adResponse.getNetworkType());
        if (adResponse.getWidth() != null) {
            valueOf = Double.valueOf(adResponse.getWidth().doubleValue());
        } else {
            valueOf = null;
        }
        withAdNetworkType = withAdNetworkType.withAdWidthPx(valueOf);
        if (adResponse.getHeight() != null) {
            valueOf = Double.valueOf(adResponse.getHeight().doubleValue());
        } else {
            valueOf = null;
        }
        withAdNetworkType = withAdNetworkType.withAdHeightPx(valueOf);
        if (location != null) {
            valueOf = Double.valueOf(location.getLatitude());
        } else {
            valueOf = null;
        }
        withAdNetworkType = withAdNetworkType.withGeoLat(valueOf);
        if (location != null) {
            valueOf = Double.valueOf(location.getLongitude());
        } else {
            valueOf = null;
        }
        BaseEvent.Builder withGeoLon = withAdNetworkType.withGeoLon(valueOf);
        if (location != null) {
            d = Double.valueOf((double) location.getAccuracy());
        }
        MoPubEvents.log(withGeoLon.withGeoAccuracy(d).withPerformanceDurationMs(Double.valueOf((double) networkResponse.networkTimeMs)).withRequestId(adResponse.getRequestId()).withRequestStatusCode(Integer.valueOf(networkResponse.statusCode)).withRequestUri(getUrl()).build());
    }
}
