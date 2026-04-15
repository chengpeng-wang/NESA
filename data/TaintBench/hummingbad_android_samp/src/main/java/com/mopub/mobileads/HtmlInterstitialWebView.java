package com.mopub.mobileads;

import android.content.Context;
import android.os.Handler;
import com.mopub.common.AdReport;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;

public class HtmlInterstitialWebView extends BaseHtmlWebView {
    private Handler mHandler = new Handler();

    static class HtmlInterstitialWebViewListener implements HtmlWebViewListener {
        private final CustomEventInterstitialListener mCustomEventInterstitialListener;

        public HtmlInterstitialWebViewListener(CustomEventInterstitialListener customEventInterstitialListener) {
            this.mCustomEventInterstitialListener = customEventInterstitialListener;
        }

        public void onLoaded(BaseHtmlWebView mHtmlWebView) {
            this.mCustomEventInterstitialListener.onInterstitialLoaded();
        }

        public void onFailed(MoPubErrorCode errorCode) {
            this.mCustomEventInterstitialListener.onInterstitialFailed(errorCode);
        }

        public void onClicked() {
            this.mCustomEventInterstitialListener.onInterstitialClicked();
        }

        public void onCollapsed() {
        }
    }

    public HtmlInterstitialWebView(Context context, AdReport adReport) {
        super(context, adReport);
    }

    public void init(CustomEventInterstitialListener customEventInterstitialListener, boolean isScrollable, String redirectUrl, String clickthroughUrl) {
        super.init(isScrollable);
        setWebViewClient(new HtmlWebViewClient(new HtmlInterstitialWebViewListener(customEventInterstitialListener), this, clickthroughUrl, redirectUrl));
    }

    private void postHandlerRunnable(Runnable r) {
        this.mHandler.post(r);
    }
}
