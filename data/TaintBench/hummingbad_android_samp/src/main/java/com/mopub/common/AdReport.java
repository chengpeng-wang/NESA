package com.mopub.common;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.network.AdResponse;
import com.umeng.analytics.onlineconfig.a;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdReport implements Serializable {
    private static final String DATE_FORMAT_PATTERN = "M/d/yy hh:mm:ss a z";
    private static final long serialVersionUID = 1;
    private final AdResponse mAdResponse;
    private final String mAdUnitId;
    private final Locale mDeviceLocale;
    private final String mDeviceModel;
    private final String mSdkVersion;
    private final String mUdid;

    public AdReport(@NonNull String adUnitId, @NonNull ClientMetadata clientMetadata, @NonNull AdResponse adResponse) {
        this.mAdUnitId = adUnitId;
        this.mSdkVersion = clientMetadata.getSdkVersion();
        this.mDeviceModel = clientMetadata.getDeviceModel();
        this.mDeviceLocale = clientMetadata.getDeviceLocale();
        this.mUdid = clientMetadata.getDeviceId();
        this.mAdResponse = adResponse;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        appendKeyValue(stringBuilder, a.g, this.mSdkVersion);
        appendKeyValue(stringBuilder, "creative_id", this.mAdResponse.getDspCreativeId());
        appendKeyValue(stringBuilder, "platform_version", Integer.toString(VERSION.SDK_INT));
        appendKeyValue(stringBuilder, "device_model", this.mDeviceModel);
        appendKeyValue(stringBuilder, "ad_unit_id", this.mAdUnitId);
        appendKeyValue(stringBuilder, "device_locale", this.mDeviceLocale == null ? null : this.mDeviceLocale.toString());
        appendKeyValue(stringBuilder, "device_id", this.mUdid);
        appendKeyValue(stringBuilder, "network_type", this.mAdResponse.getNetworkType());
        appendKeyValue(stringBuilder, "platform", "android");
        appendKeyValue(stringBuilder, "timestamp", getFormattedTimeStamp(this.mAdResponse.getTimestamp()));
        appendKeyValue(stringBuilder, "ad_type", this.mAdResponse.getAdType());
        Object width = this.mAdResponse.getWidth();
        Integer height = this.mAdResponse.getHeight();
        String str = "ad_size";
        StringBuilder stringBuilder2 = new StringBuilder("{");
        if (width == null) {
            width = "0";
        }
        stringBuilder2 = stringBuilder2.append(width).append(", ");
        if (height == null) {
            width = "0";
        } else {
            Integer num = height;
        }
        appendKeyValue(stringBuilder, str, stringBuilder2.append(width).append("}").toString());
        return stringBuilder.toString();
    }

    public String getResponseString() {
        return this.mAdResponse.getStringBody();
    }

    private void appendKeyValue(StringBuilder parameters, String key, String value) {
        parameters.append(key);
        parameters.append(" : ");
        parameters.append(value);
        parameters.append(MASTNativeAdConstants.NEWLINE);
    }

    private String getFormattedTimeStamp(long timeStamp) {
        if (timeStamp != -1) {
            return new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US).format(new Date(timeStamp));
        }
        return null;
    }
}
