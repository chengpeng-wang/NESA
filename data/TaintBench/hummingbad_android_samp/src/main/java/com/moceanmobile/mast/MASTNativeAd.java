package com.moceanmobile.mast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import com.moceanmobile.mast.AdRequest.Handler;
import com.moceanmobile.mast.Defaults.MediationNetwork;
import com.moceanmobile.mast.MASTAdView.LogLevel;
import com.moceanmobile.mast.bean.AssetRequest;
import com.moceanmobile.mast.bean.AssetResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public final class MASTNativeAd implements Handler {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$CallbackType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$TrackerType;
    private boolean isAndroidaidEnabled;
    private LocationListener locationListener = null;
    private LocationManager locationManager = null;
    private AdRequest mAdRequest = null;
    /* access modifiers changed from: private|final */
    public final HashMap<String, String> mAdRequestParameters;
    private BrowserDialog mBrowserDialog = null;
    private boolean mClickTrackerSent = false;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private boolean mImpressionTrackerSent = false;
    private boolean mIsSubViewsClickable = true;
    private NativeRequestListener mListener = null;
    private LogLevel mLogLevel = LogLevel.None;
    private LogListener mLogListener = null;
    private Map<MediationNetwork, String> mMapMediationNetworkTestDeviceIds = null;
    private MediationData mMediationData = null;
    private NativeAdDescriptor mNativeAdDescriptor = null;
    private String mNetworkUrl = Defaults.AD_NETWORK_URL;
    private boolean mOverrideAdapter = true;
    private List<AssetRequest> mRequestedNativeAssets;
    private boolean mThirdPartyClickTrackerSent = false;
    private boolean mThirdPartyImpTrackerSent = false;
    private boolean mUseInternalBrowser = false;
    private String mUserAgent = null;
    private int mZone = 0;
    private boolean test = false;

    private enum CallbackType {
        NativeReceived,
        NativeFailed,
        ThirdPartyReceived,
        NativeAdClicked
    }

    public static class Image {
        int height = 0;
        String url = null;
        int width = 0;

        public Image(String url) {
            this.url = url;
        }

        Image(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        public String getUrl() {
            return this.url;
        }

        /* access modifiers changed from: 0000 */
        public int getWidth() {
            return this.width;
        }

        /* access modifiers changed from: 0000 */
        public int getHeight() {
            return this.height;
        }

        static Image getImage(JSONObject jsonImage) {
            if (jsonImage == null || jsonImage.optString(MASTNativeAdConstants.RESPONSE_URL) == null) {
                return null;
            }
            return new Image(jsonImage.optString(MASTNativeAdConstants.RESPONSE_URL), jsonImage.optInt(MASTNativeAdConstants.NATIVE_IMAGE_W, 0), jsonImage.optInt(MASTNativeAdConstants.NATIVE_IMAGE_H, 0));
        }
    }

    private class LocationListener implements android.location.LocationListener {
        private LocationListener() {
        }

        /* synthetic */ LocationListener(MASTNativeAd mASTNativeAd, LocationListener locationListener) {
            this();
        }

        public void onLocationChanged(Location location) {
            MASTNativeAd.this.logEvent("LocationListener.onLocationChanged location:" + location.toString(), LogLevel.Debug);
            String valueOf = String.valueOf(location.getLatitude());
            String valueOf2 = String.valueOf(location.getLongitude());
            MASTNativeAd.this.mAdRequestParameters.put(MASTNativeAdConstants.REQUESTPARAM_LATITUDE, valueOf);
            MASTNativeAd.this.mAdRequestParameters.put(MASTNativeAdConstants.REQUESTPARAM_LONGITUDE, valueOf2);
        }

        public void onProviderDisabled(String provider) {
            MASTNativeAd.this.logEvent("LocationListener.onProviderDisabled provider:" + provider, LogLevel.Debug);
        }

        public void onProviderEnabled(String provider) {
            MASTNativeAd.this.logEvent("LocationListener.onProviderEnabled provider:" + provider, LogLevel.Debug);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            MASTNativeAd.this.logEvent("LocationListener.onStatusChanged provider:" + provider + " status:" + String.valueOf(status), LogLevel.Debug);
            if (status != 2) {
                MASTNativeAd.this.mAdRequestParameters.remove(MASTNativeAdConstants.REQUESTPARAM_LATITUDE);
                MASTNativeAd.this.mAdRequestParameters.remove(MASTNativeAdConstants.REQUESTPARAM_LONGITUDE);
            }
        }
    }

    public interface LogListener {
        boolean onLogEvent(MASTNativeAd mASTNativeAd, String str, LogLevel logLevel);
    }

    public interface NativeRequestListener {
        void onNativeAdClicked(MASTNativeAd mASTNativeAd);

        void onNativeAdFailed(MASTNativeAd mASTNativeAd, Exception exception);

        void onNativeAdReceived(MASTNativeAd mASTNativeAd);

        void onReceivedThirdPartyRequest(MASTNativeAd mASTNativeAd, Map<String, String> map, Map<String, String> map2);
    }

    private enum TrackerType {
        IMPRESSION_TRACKER,
        CLICK_TRACKER
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$CallbackType() {
        int[] iArr = $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$CallbackType;
        if (iArr == null) {
            iArr = new int[CallbackType.values().length];
            try {
                iArr[CallbackType.NativeAdClicked.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CallbackType.NativeFailed.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CallbackType.NativeReceived.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CallbackType.ThirdPartyReceived.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$CallbackType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$TrackerType() {
        int[] iArr = $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$TrackerType;
        if (iArr == null) {
            iArr = new int[TrackerType.values().length];
            try {
                iArr[TrackerType.CLICK_TRACKER.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TrackerType.IMPRESSION_TRACKER.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$TrackerType = iArr;
        }
        return iArr;
    }

    public MASTNativeAd(Context context) {
        this.mContext = context;
        this.mAdRequestParameters = new HashMap();
        this.mRequestedNativeAssets = new ArrayList();
    }

    public final void setZone(int zone) {
        this.mZone = zone;
    }

    public final int getZone() {
        return this.mZone;
    }

    public final void setTest(boolean test) {
        this.test = test;
    }

    public final boolean isTest() {
        return this.test;
    }

    public final void setRequestListener(NativeRequestListener requestListener) {
        if (requestListener == null || !(requestListener instanceof NativeRequestListener)) {
            throw new IllegalArgumentException("Kindly pass the object of type NativeRequestListener in case you want to use NativeAdView. Implement NativeRequestListener in your activity instead of RequestListener. ");
        }
        this.mListener = requestListener;
    }

    public final void setLogListener(LogListener logListener) {
        this.mLogListener = logListener;
    }

    public final LogListener getLogListener() {
        return this.mLogListener;
    }

    public final void setLogLevel(LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }

    public final LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    public final List<AssetRequest> getRequestedNativeAssetsList() {
        return this.mRequestedNativeAssets;
    }

    public final void addNativeAssetRequest(AssetRequest asset) {
        if (this.mRequestedNativeAssets != null && asset != null) {
            this.mRequestedNativeAssets.add(asset);
        }
    }

    public final void addNativeAssetRequestList(List<AssetRequest> assetList) {
        if (this.mRequestedNativeAssets != null && assetList != null) {
            this.mRequestedNativeAssets.addAll(assetList);
        }
    }

    public final Map<String, String> getAdRequestParameters() {
        return this.mAdRequestParameters;
    }

    public final void addCustomParameter(String customParamName, String value) {
        if (this.mAdRequestParameters != null && customParamName != null) {
            this.mAdRequestParameters.put(customParamName, value);
        }
    }

    public final void addCustomParametersMap(HashMap<String, String> customParamMap) {
        if (this.mAdRequestParameters != null && customParamMap != null) {
            this.mAdRequestParameters.putAll(customParamMap);
        }
    }

    public final void setAdNetworkURL(String adNetworkURL) {
        this.mNetworkUrl = adNetworkURL;
    }

    public final String getAdNetworkURL() {
        return this.mNetworkUrl;
    }

    @Deprecated
    private void addTestDeviceIdForNetwork(MediationNetwork network, String deviceId) {
        if (this.mMapMediationNetworkTestDeviceIds == null) {
            this.mMapMediationNetworkTestDeviceIds = new HashMap();
        }
        this.mMapMediationNetworkTestDeviceIds.put(network, deviceId);
    }

    public final void setAndroidaidEnabled(boolean isAndroidaidEnabled) {
        this.isAndroidaidEnabled = isAndroidaidEnabled;
    }

    public final boolean isAndoridaidEnabled() {
        return this.isAndroidaidEnabled;
    }

    public final void removeTestDeviceIdForNetwork(MediationNetwork network) {
        if (this.mMapMediationNetworkTestDeviceIds != null) {
            this.mMapMediationNetworkTestDeviceIds.remove(network);
        }
    }

    public final void loadImage(ImageView imageView, String url) {
        ImageDownloader.loadImage(imageView, url);
    }

    public final void update() {
        try {
            validateAssetRequest();
            internalUpdate(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logEvent("ERROR: Native asset validation failed. Ad requested interrupted.", LogLevel.Error);
        }
    }

    private void validateAssetRequest() {
        HashSet hashSet = new HashSet();
        for (AssetRequest assetRequest : this.mRequestedNativeAssets) {
            if (assetRequest.assetId <= 0) {
                throw new IllegalArgumentException("ERROR: Missing/Invalid assetId.\nNote: Asset id is mandatory for each requested asset. Each assetId should be unique and should be > 0");
            } else if (!hashSet.add(Integer.valueOf(assetRequest.assetId))) {
                throw new IllegalArgumentException("ERROR: Duplicate assetId.\nNote: Unique assetId is mandatory for each requested asset. Each assetId should be > 0");
            }
        }
    }

    private void internalUpdate(boolean defaulted) {
        HashMap hashMap = new HashMap();
        if (defaulted && this.mNativeAdDescriptor != null) {
            if (MASTNativeAdConstants.RESPONSE_DIRECT_STRING.equals(this.mNativeAdDescriptor.getSource())) {
                hashMap.put("excreatives", this.mNativeAdDescriptor.getCreativeId());
                hashMap.remove("pubmatic_exfeeds");
            } else if (MASTNativeAdConstants.RESPONSE_MEDIATION.equals(this.mNativeAdDescriptor.getSource())) {
                hashMap.put("pubmatic_exfeeds", this.mNativeAdDescriptor.getMediationId());
                hashMap.remove("excreatives");
            }
        }
        reset();
        initUserAgent();
        try {
            String networkOperator = ((TelephonyManager) this.mContext.getSystemService("phone")).getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 3) {
                String substring = networkOperator.substring(0, 3);
                networkOperator = networkOperator.substring(3);
                hashMap.put(MASTNativeAdConstants.TELEPHONY_MCC, String.valueOf(substring));
                hashMap.put(MASTNativeAdConstants.TELEPHONY_MNC, String.valueOf(networkOperator));
            }
        } catch (Exception e) {
            logEvent("Unable to obtain mcc and mnc. Exception:" + e, LogLevel.Debug);
        }
        if (isAndoridaidEnabled()) {
            hashMap.put(MASTNativeAdConstants.REQUESTPARAM_ANDROID_ID_SHA1, getUdidFromContext(this.mContext));
        }
        hashMap.putAll(this.mAdRequestParameters);
        hashMap.put(MASTNativeAdConstants.REQUESTPARAM_UA, this.mUserAgent);
        hashMap.put("version", Defaults.SDK_VERSION);
        hashMap.put(MASTNativeAdConstants.REQUESTPARAM_COUNT, "1");
        hashMap.put(MASTNativeAdConstants.REQUESTPARAM_KEY, "8");
        hashMap.put("type", "8");
        hashMap.put("zone", String.valueOf(this.mZone));
        if (this.test) {
            hashMap.put(MASTNativeAdConstants.REQUESTPARAM_TEST, "1");
        }
        try {
            if (this.mAdRequest != null) {
                this.mAdRequest.cancel();
            }
            this.mAdRequest = AdRequest.create(5, this.mNetworkUrl, this.mUserAgent, hashMap, this.mRequestedNativeAssets, this, true);
            logEvent("Ad request:" + this.mAdRequest.getRequestUrl(), LogLevel.Debug);
        } catch (UnsupportedEncodingException e2) {
            logEvent("Exception encountered while generating ad request URL:" + e2, LogLevel.Error);
            fireCallback(CallbackType.NativeFailed, e2, null);
        }
    }

    public final void trackViewForInteractions(View view) {
        if (view != null) {
            if (!this.mIsSubViewsClickable) {
                removeListenerFromChildViews(view);
            }
            view.setClickable(true);
            if (!this.mImpressionTrackerSent) {
                sendImpressions(TrackerType.IMPRESSION_TRACKER);
                this.mImpressionTrackerSent = true;
            }
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MASTNativeAd.this.adClicked();
                    MASTNativeAd.this.openClickUrlInBrowser();
                    MASTNativeAd.this.fireCallback(CallbackType.NativeAdClicked, null, null);
                }
            });
        }
    }

    private void removeListenerFromChildViews(View view) {
        int i = 0;
        if (view != null) {
            view.setOnClickListener(null);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                while (i < viewGroup.getChildCount()) {
                    removeListenerFromChildViews(viewGroup.getChildAt(i));
                    i++;
                }
                return;
            }
            System.out.println("View=" + view.toString() + ", ClickState=" + view.isClickable());
            view.setClickable(false);
        }
    }

    @Deprecated
    private void overrideAdapterLoading(boolean override) {
        this.mOverrideAdapter = override;
    }

    public final void thirdpartyPartnerDefaulted() {
        internalUpdate(true);
    }

    public final boolean isLocationDetectionEnabled() {
        if (this.locationManager != null) {
            return true;
        }
        return false;
    }

    public final void setLocationDetectionEnabled(boolean locationDetectionEnabled) {
        if (locationDetectionEnabled) {
            Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(0);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setAltitudeRequired(false);
            criteria.setAccuracy(2);
            enableLocationDetection(600000, 20.0f, criteria, null);
        } else if (this.locationManager != null) {
            this.locationManager.removeUpdates(this.locationListener);
            this.locationManager = null;
            this.locationListener = null;
        }
    }

    public final void enableLocationDetection(long minTime, float minDistance, Criteria criteria, String provider) {
        if (provider == null && criteria == null) {
            throw new IllegalArgumentException("Criteria or Provider required");
        }
        this.locationManager = (LocationManager) this.mContext.getSystemService("location");
        if (this.locationManager != null) {
            if (provider == null) {
                try {
                    List providers = this.locationManager.getProviders(criteria, true);
                    if (providers != null && providers.size() > 0) {
                        provider = (String) providers.get(0);
                    }
                } catch (Exception e) {
                    logEvent("Error requesting location updates.  Exception:" + e, LogLevel.Error);
                    this.locationManager.removeUpdates(this.locationListener);
                    this.locationManager = null;
                    this.locationListener = null;
                    return;
                }
            }
            if (provider != null) {
                this.locationListener = new LocationListener(this, null);
                this.locationManager.requestLocationUpdates(provider, minTime, minDistance, this.locationListener);
            }
        }
    }

    public final void reset() {
        if (this.mAdRequest != null) {
            this.mAdRequest.cancel();
        }
        this.mNativeAdDescriptor = null;
        this.mMediationData = null;
        this.mImpressionTrackerSent = false;
        this.mThirdPartyImpTrackerSent = false;
        this.mThirdPartyClickTrackerSent = false;
        this.mClickTrackerSent = false;
    }

    public final void destroy() {
        reset();
        this.mOverrideAdapter = true;
        this.mListener = null;
    }

    public final List<AssetResponse> getNativeAssets() {
        if (this.mNativeAdDescriptor == null || this.mNativeAdDescriptor.getNativeAssetList() == null || this.mNativeAdDescriptor.getNativeAssetList().size() <= 0) {
            return null;
        }
        return this.mNativeAdDescriptor.getNativeAssetList();
    }

    public final String getJsTracker() {
        if (this.mNativeAdDescriptor == null || this.mNativeAdDescriptor.getJsTracker() == null) {
            return null;
        }
        return this.mNativeAdDescriptor.getJsTracker();
    }

    public final String getClick() {
        if (this.mNativeAdDescriptor != null) {
            return this.mNativeAdDescriptor.getClick();
        }
        return null;
    }

    public final String getAdResponse() {
        if (this.mNativeAdDescriptor != null) {
            return this.mNativeAdDescriptor.getNativeAdJSON();
        }
        return null;
    }

    public final boolean isMediationResponse() {
        return this.mNativeAdDescriptor.isTypeMediation();
    }

    public final MediationData getMediationData() {
        return this.mMediationData;
    }

    private void initUserAgent() {
        if (TextUtils.isEmpty(this.mUserAgent)) {
            this.mUserAgent = new WebView(this.mContext).getSettings().getUserAgentString();
            if (TextUtils.isEmpty(this.mUserAgent)) {
                this.mUserAgent = Defaults.USER_AGENT;
            }
        }
    }

    /* access modifiers changed from: private */
    public void logEvent(String event, LogLevel eventLevel) {
        if (eventLevel.ordinal() <= this.mLogLevel.ordinal() && this.mLogListener != null && this.mLogListener.onLogEvent(this, event, eventLevel)) {
        }
    }

    public final void sendImpression() {
        if (!this.mThirdPartyImpTrackerSent && this.mNativeAdDescriptor != null && this.mNativeAdDescriptor.isTypeMediation()) {
            sendImpressions(TrackerType.IMPRESSION_TRACKER);
            this.mThirdPartyImpTrackerSent = true;
        }
    }

    public final void sendClickTracker() {
        if (!this.mThirdPartyClickTrackerSent && this.mNativeAdDescriptor != null && this.mNativeAdDescriptor.isTypeMediation()) {
            sendImpressions(TrackerType.CLICK_TRACKER);
            this.mThirdPartyClickTrackerSent = true;
        }
    }

    private void sendImpressions(TrackerType trackerType) {
        if (this.mNativeAdDescriptor != null) {
            switch ($SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$TrackerType()[trackerType.ordinal()]) {
                case 1:
                    AdTracking.invokeTrackingUrl(5, this.mNativeAdDescriptor.getNativeAdImpressionTrackers(), this.mUserAgent);
                    return;
                case 2:
                    AdTracking.invokeTrackingUrl(5, this.mNativeAdDescriptor.getNativeAdClickTrackers(), this.mUserAgent);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void adClicked() {
        if (!this.mClickTrackerSent) {
            sendImpressions(TrackerType.CLICK_TRACKER);
            this.mClickTrackerSent = true;
        }
    }

    public final void setUseInternalBrowser(boolean useInternalBrowser) {
        this.mUseInternalBrowser = useInternalBrowser;
    }

    public final boolean getUseInternalBrowser() {
        return this.mUseInternalBrowser;
    }

    /* access modifiers changed from: private */
    public void openClickUrlInBrowser() {
        if (this.mNativeAdDescriptor != null) {
            String click = this.mNativeAdDescriptor.getClick();
            if (click == null) {
                return;
            }
            if (this.mUseInternalBrowser) {
                if (this.mBrowserDialog == null) {
                    this.mBrowserDialog = new BrowserDialog(this.mContext, click, new BrowserDialog.Handler() {
                        public void browserDialogDismissed(BrowserDialog browserDialog) {
                        }

                        public void browserDialogOpenUrl(BrowserDialog browserDialog, String url, boolean dismiss) {
                            MASTNativeAd.this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                            if (dismiss) {
                                browserDialog.dismiss();
                            }
                        }
                    });
                } else {
                    this.mBrowserDialog.loadUrl(click);
                }
                if (!this.mBrowserDialog.isShowing()) {
                    this.mBrowserDialog.show();
                    return;
                }
                return;
            }
            this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(click)));
        }
    }

    @Deprecated
    public final void adRequestFailed(AdRequest request, Exception exception) {
        if (request == this.mAdRequest) {
            logEvent("Ad request failed: " + exception, LogLevel.Error);
            fireCallback(CallbackType.NativeFailed, exception, null);
        }
    }

    @Deprecated
    public final void adRequestError(AdRequest request, String errorCode, String errorMessage) {
        if (request == this.mAdRequest) {
            fireCallback(CallbackType.NativeFailed, new Exception(errorMessage), null);
            LogLevel logLevel = LogLevel.Error;
            if ("404".equals(errorCode)) {
                logLevel = LogLevel.Debug;
            }
            logEvent("Error response from server.  Error code: " + errorCode + ". Error message: " + errorMessage, logLevel);
        }
    }

    @Deprecated
    public final void adRequestCompleted(AdRequest request, AdDescriptor adDescriptor) {
        if (request == this.mAdRequest) {
            if (adDescriptor == null || !(adDescriptor instanceof NativeAdDescriptor)) {
                fireCallback(CallbackType.NativeFailed, new Exception("Incorrect response received"), null);
                return;
            }
            this.mNativeAdDescriptor = (NativeAdDescriptor) adDescriptor;
            this.mMediationData = null;
            if (this.mNativeAdDescriptor.isTypeMediation()) {
                try {
                    if (this.mOverrideAdapter) {
                        ThirdPartyDescriptor descriptor = ThirdPartyDescriptor.getDescriptor(this.mNativeAdDescriptor);
                        if (MASTNativeAdConstants.RESPONSE_MEDIATION.equalsIgnoreCase(this.mNativeAdDescriptor.getSource())) {
                            this.mMediationData = new MediationData();
                            this.mMediationData.setMediationNetworkId(this.mNativeAdDescriptor.getMediationId());
                            this.mMediationData.setMediationNetworkName(this.mNativeAdDescriptor.getMediation());
                            this.mMediationData.setMediationSource(this.mNativeAdDescriptor.getSource());
                            this.mMediationData.setMediationAdId(this.mNativeAdDescriptor.getAdUnitId());
                        }
                        fireCallback(CallbackType.ThirdPartyReceived, null, descriptor);
                        return;
                    }
                    logEvent("Error parsing third party content descriptor. No Inbuilt adapter available.", LogLevel.Error);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    logEvent("Error parsing third party content descriptor.  Exception:" + e, LogLevel.Error);
                    return;
                }
            }
            fireCallback(CallbackType.NativeReceived, null, null);
        }
    }

    /* access modifiers changed from: private */
    public void fireCallback(CallbackType callbackType, Exception ex, ThirdPartyDescriptor thirdPartyDescriptor) {
        if (this.mListener != null) {
            switch ($SWITCH_TABLE$com$moceanmobile$mast$MASTNativeAd$CallbackType()[callbackType.ordinal()]) {
                case 1:
                    this.mListener.onNativeAdReceived(this);
                    return;
                case 2:
                    this.mListener.onNativeAdFailed(this, ex);
                    return;
                case 3:
                    if (thirdPartyDescriptor != null) {
                        this.mListener.onReceivedThirdPartyRequest(this, thirdPartyDescriptor.getProperties(), thirdPartyDescriptor.getParams());
                        return;
                    } else {
                        fireCallback(CallbackType.NativeFailed, new Exception("Third party response is invalid"), thirdPartyDescriptor);
                        return;
                    }
                case 4:
                    this.mListener.onNativeAdClicked(this);
                    return;
                default:
                    return;
            }
        }
    }

    private static String getUdidFromContext(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        return string == null ? "" : sha1(string);
    }

    @SuppressLint({"DefaultLocale"})
    public static String sha1(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            byte[] bytes = string.getBytes(Defaults.ENCODING_UTF_8);
            instance.update(bytes, 0, bytes.length);
            int length = instance.digest().length;
            for (int i = 0; i < length; i++) {
                stringBuilder.append(String.format("%02X", new Object[]{Byte.valueOf(r2[i])}));
            }
            return stringBuilder.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    public final boolean isSubViewsClickable() {
        return this.mIsSubViewsClickable;
    }

    public final void setSubViewsClickable(boolean state) {
        this.mIsSubViewsClickable = state;
    }
}
