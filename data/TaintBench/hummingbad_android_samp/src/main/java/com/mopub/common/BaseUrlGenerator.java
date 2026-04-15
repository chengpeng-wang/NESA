package com.mopub.common;

import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.network.Networking;
import com.mopub.network.PlayServicesUrlRewriter;

public abstract class BaseUrlGenerator {
    private static final String HEIGHT_KEY = "h";
    private static final String WIDTH_KEY = "w";
    private boolean mFirstParam;
    private StringBuilder mStringBuilder;

    public abstract String generateUrlString(String str);

    /* access modifiers changed from: protected */
    public void initUrlString(String serverHostname, String handlerType) {
        this.mStringBuilder = new StringBuilder(Networking.useHttps() ? Constants.HTTPS : Constants.HTTP).append("://").append(serverHostname).append(handlerType);
        this.mFirstParam = true;
    }

    /* access modifiers changed from: protected */
    public String getFinalUrlString() {
        return this.mStringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public void addParam(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            this.mStringBuilder.append(getParamDelimiter());
            this.mStringBuilder.append(key);
            this.mStringBuilder.append(MASTNativeAdConstants.EQUAL);
            this.mStringBuilder.append(Uri.encode(value));
        }
    }

    private String getParamDelimiter() {
        if (!this.mFirstParam) {
            return MASTNativeAdConstants.AMPERSAND;
        }
        this.mFirstParam = false;
        return MASTNativeAdConstants.QUESTIONMARK;
    }

    /* access modifiers changed from: protected */
    public void setApiVersion(String apiVersion) {
        addParam("v", apiVersion);
    }

    /* access modifiers changed from: protected */
    public void setAppVersion(String appVersion) {
        addParam("av", appVersion);
    }

    /* access modifiers changed from: protected */
    public void setExternalStoragePermission(boolean isExternalStoragePermissionGranted) {
        addParam("android_perms_ext_storage", isExternalStoragePermissionGranted ? "1" : "0");
    }

    /* access modifiers changed from: protected|varargs */
    public void setDeviceInfo(String... info) {
        StringBuilder stringBuilder = new StringBuilder();
        if (info != null && info.length > 0) {
            for (int i = 0; i < info.length - 1; i++) {
                stringBuilder.append(info[i]).append(",");
            }
            stringBuilder.append(info[info.length - 1]);
            addParam("dn", stringBuilder.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void setDoNotTrack(boolean dnt) {
        if (dnt) {
            addParam("dnt", "1");
        }
    }

    /* access modifiers changed from: protected */
    public void setUdid(String udid) {
        addParam("udid", udid);
    }

    /* access modifiers changed from: protected */
    public void appendAdvertisingInfoTemplates() {
        addParam("udid", PlayServicesUrlRewriter.UDID_TEMPLATE);
        addParam("dnt", PlayServicesUrlRewriter.DO_NOT_TRACK_TEMPLATE);
    }

    /* access modifiers changed from: protected */
    public void setDeviceDimensions(@NonNull Point dimensions) {
        addParam("w", dimensions.x);
        addParam("h", dimensions.y);
    }
}
