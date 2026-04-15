package com.mopub.mobileads;

import android.content.Context;
import com.mopub.common.AdReport;
import com.mopub.common.DataKeys;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import com.mopub.mobileads.factories.HtmlBannerWebViewFactory;
import java.util.Map;

public class HtmlBanner extends CustomEventBanner {
    private HtmlBannerWebView mHtmlBannerWebView;

    /* access modifiers changed from: protected */
    public void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (extrasAreValid(serverExtras)) {
            String str = (String) serverExtras.get(DataKeys.HTML_RESPONSE_BODY_KEY);
            String str2 = (String) serverExtras.get(DataKeys.REDIRECT_URL_KEY);
            String str3 = (String) serverExtras.get(DataKeys.CLICKTHROUGH_URL_KEY);
            try {
                AdReport adReport = (AdReport) localExtras.get(DataKeys.AD_REPORT_KEY);
                this.mHtmlBannerWebView = HtmlBannerWebViewFactory.create(context, adReport, customEventBannerListener, Boolean.valueOf((String) serverExtras.get(DataKeys.SCROLLABLE_KEY)).booleanValue(), str2, str3);
                AdViewController.setShouldHonorServerDimensions(this.mHtmlBannerWebView);
                this.mHtmlBannerWebView.loadHtmlResponse(str);
                return;
            } catch (ClassCastException e) {
                MoPubLog.e("LocalExtras contained an incorrect type.");
                customEventBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
                return;
            }
        }
        customEventBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
    }

    /* access modifiers changed from: protected */
    public void onInvalidate() {
        if (this.mHtmlBannerWebView != null) {
            this.mHtmlBannerWebView.destroy();
        }
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey(DataKeys.HTML_RESPONSE_BODY_KEY);
    }
}
