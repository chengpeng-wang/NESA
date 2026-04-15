package com.mopub.mobileads;

public interface HtmlWebViewListener {
    void onClicked();

    void onCollapsed();

    void onFailed(MoPubErrorCode moPubErrorCode);

    void onLoaded(BaseHtmlWebView baseHtmlWebView);
}
