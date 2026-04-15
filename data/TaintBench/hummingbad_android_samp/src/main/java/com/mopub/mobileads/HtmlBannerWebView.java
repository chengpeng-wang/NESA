package com.mopub.mobileads;

import android.content.Context;
import com.mopub.common.AdReport;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;

public class HtmlBannerWebView extends BaseHtmlWebView {
    public static final String EXTRA_AD_CLICK_DATA = "com.mopub.intent.extra.AD_CLICK_DATA";
    public boolean isImgLoadSuccess = false;

    static class HtmlBannerWebViewListener implements HtmlWebViewListener {
        private final CustomEventBannerListener mCustomEventBannerListener;

        public HtmlBannerWebViewListener(CustomEventBannerListener customEventBannerListener) {
            this.mCustomEventBannerListener = customEventBannerListener;
        }

        public void onLoaded(BaseHtmlWebView htmlWebView) {
            this.mCustomEventBannerListener.onBannerLoaded(htmlWebView);
        }

        public void onFailed(MoPubErrorCode errorCode) {
            this.mCustomEventBannerListener.onBannerFailed(errorCode);
        }

        public void onClicked() {
            this.mCustomEventBannerListener.onBannerClicked();
        }

        public void onCollapsed() {
            this.mCustomEventBannerListener.onBannerCollapsed();
        }
    }

    public boolean isImgLoadSuccess() {
        return this.isImgLoadSuccess;
    }

    public void setIsImgLoadSuccess(boolean isImgLoadSuccess) {
        this.isImgLoadSuccess = isImgLoadSuccess;
    }

    public HtmlBannerWebView(Context context, AdReport adReport) {
        super(context, adReport);
    }

    public void init(CustomEventBannerListener customEventBannerListener, boolean isScrollable, String redirectUrl, String clickthroughUrl) {
        super.init(isScrollable);
        setWebViewClient(new HtmlWebViewClient(new HtmlBannerWebViewListener(customEventBannerListener), this, clickthroughUrl, redirectUrl));
    }
}
