package com.moceanmobile.mast;

import com.moceanmobile.mast.bean.AssetResponse;
import java.util.Arrays;
import java.util.List;

public final class NativeAdDescriptor extends AdDescriptor {
    private String adUnitId = null;
    private String click = null;
    private String[] clickTrackers = null;
    private String creativeId = null;
    private String fallbackUrl = null;
    private String feedId = null;
    private String[] impressionTrackers = null;
    private String jsTracker = null;
    private String mediation = null;
    private String mediationId = null;
    private String nativeAdJSON = null;
    private List<AssetResponse> nativeAssetList = null;
    private int nativeVersion = 0;
    private String source = null;
    private String subtype = "";
    private boolean typeMediation = false;

    NativeAdDescriptor(String subtype, int nativeVersion2, String clickUrl, String fallbackUrl, String[] impressionTrackers, String[] clickTrackers, String jsTrackerString, List<AssetResponse> nativeAssetList) {
        this.subtype = subtype;
        this.click = clickUrl;
        this.impressionTrackers = impressionTrackers;
        this.clickTrackers = clickTrackers;
        this.jsTracker = jsTrackerString;
        this.fallbackUrl = fallbackUrl;
        this.nativeAssetList = nativeAssetList;
        this.nativeVersion = nativeVersion2;
        this.typeMediation = false;
    }

    NativeAdDescriptor(String subtype, String creativeId, String mediation, String mediationId, String adUnitId, String source, String[] impressionTrackers, String[] clickTrackers, String jsTrackerString, String feedId) {
        this.subtype = subtype;
        this.creativeId = creativeId;
        this.mediation = mediation;
        this.mediationId = mediationId;
        this.adUnitId = adUnitId;
        this.source = source;
        this.impressionTrackers = impressionTrackers;
        this.clickTrackers = clickTrackers;
        this.jsTracker = jsTrackerString;
        this.feedId = feedId;
        this.typeMediation = true;
    }

    public final String getClick() {
        return this.click;
    }

    public final void setClick(String click) {
        this.click = click;
    }

    public final void setFallbackUrl(String fallbackUrl) {
        this.fallbackUrl = fallbackUrl;
    }

    public final String getFallbackUrl() {
        return this.fallbackUrl;
    }

    public final String[] getNativeAdImpressionTrackers() {
        return this.impressionTrackers;
    }

    public final String[] getNativeAdClickTrackers() {
        return this.clickTrackers;
    }

    public final String getJsTracker() {
        return this.jsTracker;
    }

    public final void setJsTracker(String jsTracker) {
        this.jsTracker = jsTracker;
    }

    public final String getNativeAdJSON() {
        return this.nativeAdJSON;
    }

    /* access modifiers changed from: final */
    public final void setNativeAdJSON(String nativeAdJSON) {
        this.nativeAdJSON = nativeAdJSON;
    }

    /* access modifiers changed from: final */
    public final String getSubtype() {
        return this.subtype;
    }

    /* access modifiers changed from: final */
    public final void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /* access modifiers changed from: final */
    public final String getCreativeId() {
        return this.creativeId;
    }

    /* access modifiers changed from: final */
    public final void setCreativeId(String creativeId) {
        this.creativeId = creativeId;
    }

    public final void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public final String getFeedId() {
        return this.feedId;
    }

    /* access modifiers changed from: final */
    public final String getMediation() {
        return this.mediation;
    }

    /* access modifiers changed from: final */
    public final void setMediation(String mediation) {
        this.mediation = mediation;
    }

    /* access modifiers changed from: final */
    public final String getMediationId() {
        return this.mediationId;
    }

    /* access modifiers changed from: final */
    public final void setMediationId(String mediationId) {
        this.mediationId = mediationId;
    }

    /* access modifiers changed from: final */
    public final String getSource() {
        return this.source;
    }

    /* access modifiers changed from: final */
    public final void setSource(String source) {
        this.source = source;
    }

    /* access modifiers changed from: final */
    public final String getAdUnitId() {
        return this.adUnitId;
    }

    /* access modifiers changed from: final */
    public final void setMediationData(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    /* access modifiers changed from: final */
    public final boolean isTypeMediation() {
        return this.typeMediation;
    }

    public final List<AssetResponse> getNativeAssetList() {
        return this.nativeAssetList;
    }

    public final void setNativeAssetList(List<AssetResponse> nativeAssetList) {
        this.nativeAssetList = nativeAssetList;
    }

    public final int getNativeVersion() {
        return this.nativeVersion;
    }

    public final void setNativeVersion(int nativeVersion) {
        this.nativeVersion = nativeVersion;
    }

    public final String toString() {
        return "NativeAdDescriptor [click=" + this.click + ", impressionTrackers=" + Arrays.toString(this.impressionTrackers) + ", clickTrackers=" + Arrays.toString(this.clickTrackers) + ", nativeAdJSON=" + this.nativeAdJSON + ", subtype=" + this.subtype + ", mediation=" + this.mediation + ", mediationId=" + this.mediationId + ", adUnitId=" + this.adUnitId + " nativeVersion=" + this.nativeVersion + " ]";
    }
}
