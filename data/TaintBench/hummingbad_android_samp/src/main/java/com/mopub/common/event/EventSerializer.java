package com.mopub.common.event;

import android.support.annotation.NonNull;
import com.mopub.common.ClientMetadata.MoPubNetworkType;
import com.mopub.common.Preconditions;
import com.mopub.common.event.BaseEvent.AppPlatform;
import com.mopub.common.event.BaseEvent.SdkProduct;
import com.mopub.common.logging.MoPubLog;
import com.umeng.analytics.onlineconfig.a;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventSerializer {
    @NonNull
    public JSONArray serializeAsJson(@NonNull List<BaseEvent> events) {
        Preconditions.checkNotNull(events);
        JSONArray jSONArray = new JSONArray();
        for (BaseEvent baseEvent : events) {
            try {
                jSONArray.put(serializeAsJson(baseEvent));
            } catch (JSONException e) {
                MoPubLog.d("Failed to serialize event \"" + baseEvent.getName() + "\" to JSON: ", e);
            }
        }
        return jSONArray;
    }

    @NonNull
    public JSONObject serializeAsJson(@NonNull BaseEvent event) throws JSONException {
        Object obj;
        Object obj2 = null;
        Preconditions.checkNotNull(event);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("_category_", event.getScribeCategory().getCategory());
        jSONObject.put("ts", event.getTimestampUtcMs());
        jSONObject.put("name", event.getName().getName());
        jSONObject.put("name_category", event.getCategory().getCategory());
        SdkProduct sdkProduct = event.getSdkProduct();
        String str = "sdk_product";
        if (sdkProduct == null) {
            obj = null;
        } else {
            obj = Integer.valueOf(sdkProduct.getType());
        }
        jSONObject.put(str, obj);
        jSONObject.put(a.g, event.getSdkVersion());
        jSONObject.put("ad_unit_id", event.getAdUnitId());
        jSONObject.put("ad_creative_id", event.getAdCreativeId());
        jSONObject.put("ad_type", event.getAdType());
        jSONObject.put("ad_network_type", event.getAdNetworkType());
        jSONObject.put("ad_width_px", event.getAdWidthPx());
        jSONObject.put("ad_height_px", event.getAdHeightPx());
        AppPlatform appPlatform = event.getAppPlatform();
        str = "app_platform";
        if (appPlatform == null) {
            obj = null;
        } else {
            obj = Integer.valueOf(appPlatform.getType());
        }
        jSONObject.put(str, obj);
        jSONObject.put("app_name", event.getAppName());
        jSONObject.put("app_package_name", event.getAppPackageName());
        jSONObject.put("app_version", event.getAppVersion());
        jSONObject.put("client_advertising_id", event.getObfuscatedClientAdvertisingId());
        jSONObject.put("client_do_not_track", event.getClientDoNotTrack());
        jSONObject.put("device_manufacturer", event.getDeviceManufacturer());
        jSONObject.put("device_model", event.getDeviceModel());
        jSONObject.put("device_product", event.getDeviceProduct());
        jSONObject.put("device_os_version", event.getDeviceOsVersion());
        jSONObject.put("device_screen_width_px", event.getDeviceScreenWidthDip());
        jSONObject.put("device_screen_height_px", event.getDeviceScreenHeightDip());
        jSONObject.put("geo_lat", event.getGeoLat());
        jSONObject.put("geo_lon", event.getGeoLon());
        jSONObject.put("geo_accuracy_radius_meters", event.getGeoAccuracy());
        jSONObject.put("perf_duration_ms", event.getPerformanceDurationMs());
        MoPubNetworkType networkType = event.getNetworkType();
        str = "network_type";
        if (networkType != null) {
            obj2 = Integer.valueOf(networkType.getId());
        }
        jSONObject.put(str, obj2);
        jSONObject.put("network_operator_code", event.getNetworkOperatorCode());
        jSONObject.put("network_operator_name", event.getNetworkOperatorName());
        jSONObject.put("network_iso_country_code", event.getNetworkIsoCountryCode());
        jSONObject.put("network_sim_code", event.getNetworkSimCode());
        jSONObject.put("network_sim_operator_name", event.getNetworkSimOperatorName());
        jSONObject.put("network_sim_iso_country_code", event.getNetworkSimIsoCountryCode());
        jSONObject.put("req_id", event.getRequestId());
        jSONObject.put("req_status_code", event.getRequestStatusCode());
        jSONObject.put("req_uri", event.getRequestUri());
        jSONObject.put("req_retries", event.getRequestRetries());
        jSONObject.put("timestamp_client", event.getTimestampUtcMs());
        if (event instanceof ErrorEvent) {
            ErrorEvent errorEvent = (ErrorEvent) event;
            jSONObject.put("error_exception_class_name", errorEvent.getErrorExceptionClassName());
            jSONObject.put("error_message", errorEvent.getErrorMessage());
            jSONObject.put("error_stack_trace", errorEvent.getErrorStackTrace());
            jSONObject.put("error_file_name", errorEvent.getErrorFileName());
            jSONObject.put("error_class_name", errorEvent.getErrorClassName());
            jSONObject.put("error_method_name", errorEvent.getErrorMethodName());
            jSONObject.put("error_line_number", errorEvent.getErrorLineNumber());
        }
        return jSONObject;
    }
}
