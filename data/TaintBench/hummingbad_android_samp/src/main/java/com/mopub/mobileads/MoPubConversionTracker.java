package com.mopub.mobileads;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.BaseUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.Constants;
import com.mopub.common.SharedPreferencesHelper;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.TrackingRequest;
import com.mopub.network.TrackingRequest.Listener;
import com.mopub.volley.VolleyError;

public class MoPubConversionTracker {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public String mIsTrackedKey;
    /* access modifiers changed from: private */
    public String mPackageName;
    /* access modifiers changed from: private */
    public SharedPreferences mSharedPreferences;

    private class ConversionUrlGenerator extends BaseUrlGenerator {
        private ConversionUrlGenerator() {
        }

        /* synthetic */ ConversionUrlGenerator(MoPubConversionTracker x0, AnonymousClass1 x1) {
            this();
        }

        public String generateUrlString(String serverHostname) {
            initUrlString(serverHostname, Constants.CONVERSION_TRACKING_HANDLER);
            setApiVersion(MarketRequestBuilder.REQUEST_HOT_APPS_FROM_GIFTBOX);
            setPackageId(MoPubConversionTracker.this.mPackageName);
            setAppVersion(ClientMetadata.getInstance(MoPubConversionTracker.this.mContext).getAppVersion());
            appendAdvertisingInfoTemplates();
            return getFinalUrlString();
        }

        private void setPackageId(String packageName) {
            addParam(MASTNativeAdConstants.ID_STRING, packageName);
        }
    }

    public void reportAppOpen(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mPackageName = this.mContext.getPackageName();
            this.mIsTrackedKey = this.mPackageName + " tracked";
            this.mSharedPreferences = SharedPreferencesHelper.getSharedPreferences(this.mContext);
            if (isAlreadyTracked()) {
                MoPubLog.d("Conversion already tracked");
            } else {
                TrackingRequest.makeTrackingHttpRequest(new ConversionUrlGenerator(this, null).generateUrlString(Constants.HOST), this.mContext, new Listener() {
                    public void onResponse(@NonNull String url) {
                        MoPubConversionTracker.this.mSharedPreferences.edit().putBoolean(MoPubConversionTracker.this.mIsTrackedKey, true).commit();
                    }

                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
            }
        }
    }

    private boolean isAlreadyTracked() {
        return this.mSharedPreferences.getBoolean(this.mIsTrackedKey, false);
    }
}
