package com.mopub.mobileads;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.UrlHandler.MoPubSchemeListener;
import com.mopub.common.UrlHandler.ResultActions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Intents;
import com.mopub.exceptions.IntentNotResolvableException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;

class HtmlWebViewClient extends WebViewClient {
    static final String MOPUB_FAIL_LOAD = "mopub://failLoad";
    static final String MOPUB_FINISH_LOAD = "mopub://finishLoad";
    private final EnumSet<UrlAction> SUPPORTED_URL_ACTIONS = EnumSet.of(UrlAction.HANDLE_MOPUB_SCHEME, new UrlAction[]{UrlAction.IGNORE_ABOUT_SCHEME, UrlAction.HANDLE_PHONE_SCHEME, UrlAction.OPEN_APP_MARKET, UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK});
    private final String mClickthroughUrl;
    private final Context mContext;
    /* access modifiers changed from: private */
    public BaseHtmlWebView mHtmlWebView;
    /* access modifiers changed from: private */
    public HtmlWebViewListener mHtmlWebViewListener;
    private final String mRedirectUrl;

    HtmlWebViewClient(HtmlWebViewListener htmlWebViewListener, BaseHtmlWebView htmlWebView, String clickthrough, String redirect) {
        this.mHtmlWebViewListener = htmlWebViewListener;
        this.mHtmlWebView = htmlWebView;
        if (this.mHtmlWebView instanceof HtmlBannerWebView) {
            ((HtmlBannerWebView) this.mHtmlWebView).setIsImgLoadSuccess(false);
        }
        this.mClickthroughUrl = clickthrough;
        this.mRedirectUrl = redirect;
        this.mContext = htmlWebView.getContext();
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        new Builder().withSupportedUrlActions(this.SUPPORTED_URL_ACTIONS).withResultActions(new ResultActions() {
            public void urlHandlingSucceeded(@NonNull String url, @NonNull UrlAction urlAction) {
                if (HtmlWebViewClient.this.mHtmlWebView.wasClicked()) {
                    HtmlWebViewClient.this.mHtmlWebViewListener.onClicked();
                    HtmlWebViewClient.this.mHtmlWebView.onResetUserClick();
                }
            }

            public void urlHandlingFailed(@NonNull String url, @NonNull UrlAction lastFailedUrlAction) {
            }
        }).withMoPubSchemeListener(new MoPubSchemeListener() {
            public void onFinishLoad() {
                HtmlWebViewClient.this.mHtmlWebViewListener.onLoaded(HtmlWebViewClient.this.mHtmlWebView);
            }

            public void onClose() {
                HtmlWebViewClient.this.mHtmlWebViewListener.onCollapsed();
            }

            public void onFailLoad() {
                HtmlWebViewClient.this.mHtmlWebViewListener.onFailed(MoPubErrorCode.UNSPECIFIED);
            }
        }).build().handleUrl(this.mContext, url, this.mHtmlWebView.wasClicked());
        return true;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (this.mRedirectUrl != null && url.startsWith(this.mRedirectUrl)) {
            view.stopLoading();
            if (this.mHtmlWebView.wasClicked()) {
                try {
                    Intents.showMoPubBrowserForUrl(this.mContext, Uri.parse(url));
                    return;
                } catch (IntentNotResolvableException e) {
                    MoPubLog.d(e.getMessage());
                    return;
                }
            }
            MoPubLog.d("Attempted to redirect without user interaction");
        }
    }

    @TargetApi(21)
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return interceptRequest(view, request.getUrl().toString());
    }

    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return interceptRequest(view, url);
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0156  */
    @android.annotation.TargetApi(11)
    public android.webkit.WebResourceResponse interceptRequest(android.webkit.WebView r12, java.lang.String r13) {
        /*
        r11 = this;
        r0 = 0;
        r1 = com.mopub.mobileads.MoPubView.getLoadImageSwitch();
        if (r1 != 0) goto L_0x000d;
    L_0x0007:
        r1 = "HtmlWebViewClient interceptRequest isOpen:false";
        com.mopub.common.logging.MoPubLog.d(r1);
    L_0x000c:
        return r0;
    L_0x000d:
        r2 = com.mopub.common.HttpClient.getHttpClient();	 Catch:{ Exception -> 0x0132, all -> 0x0151 }
        r1 = com.mopub.common.HttpClient.initializeHttpGet(r13);	 Catch:{ Exception -> 0x015c }
        r1 = r2.execute(r1);	 Catch:{ Exception -> 0x015c }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r4 = "HtmlWebViewClient shouldInterceptRequest response:";
        r3.<init>(r4);	 Catch:{ Exception -> 0x015c }
        r3 = r3.append(r1);	 Catch:{ Exception -> 0x015c }
        r3 = r3.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r3);	 Catch:{ Exception -> 0x015c }
        if (r1 != 0) goto L_0x0033;
    L_0x002d:
        if (r2 == 0) goto L_0x000c;
    L_0x002f:
        r2.close();
        goto L_0x000c;
    L_0x0033:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r4 = "HtmlWebViewClient shouldInterceptRequest url: ";
        r3.<init>(r4);	 Catch:{ Exception -> 0x015c }
        r3 = r3.append(r13);	 Catch:{ Exception -> 0x015c }
        r4 = ",status code:";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x015c }
        r4 = r1.getStatusLine();	 Catch:{ Exception -> 0x015c }
        r4 = r4.getStatusCode();	 Catch:{ Exception -> 0x015c }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x015c }
        r3 = r3.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r3);	 Catch:{ Exception -> 0x015c }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r4 = r1.getStatusLine();	 Catch:{ Exception -> 0x015c }
        r4 = r4.getStatusCode();	 Catch:{ Exception -> 0x015c }
        if (r3 != r4) goto L_0x012b;
    L_0x0063:
        r4 = r1.getEntity();	 Catch:{ Exception -> 0x015c }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r3 = "HtmlWebViewClient shouldInterceptRequest entity.getContentLength(): ";
        r1.<init>(r3);	 Catch:{ Exception -> 0x015c }
        r5 = r4.getContentLength();	 Catch:{ Exception -> 0x015c }
        r1 = r1.append(r5);	 Catch:{ Exception -> 0x015c }
        r1 = r1.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r1);	 Catch:{ Exception -> 0x015c }
        r5 = r4.getContent();	 Catch:{ Exception -> 0x015c }
        r6 = org.apache.http.util.EntityUtils.getContentCharSet(r4);	 Catch:{ Exception -> 0x015c }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r3 = "HtmlWebViewClient shouldInterceptRequest encoding: ";
        r1.<init>(r3);	 Catch:{ Exception -> 0x015c }
        r1 = r1.append(r6);	 Catch:{ Exception -> 0x015c }
        r1 = r1.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r1);	 Catch:{ Exception -> 0x015c }
        r1 = r4.getContentType();	 Catch:{ Exception -> 0x015c }
        r7 = r1.getElements();	 Catch:{ Exception -> 0x015c }
        r1 = 0;
        r3 = r0;
    L_0x00a1:
        r8 = r7.length;	 Catch:{ Exception -> 0x015c }
        if (r1 >= r8) goto L_0x00ed;
    L_0x00a4:
        r3 = r7[r1];	 Catch:{ Exception -> 0x015c }
        r3 = r3.getName();	 Catch:{ Exception -> 0x015c }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r9 = "HtmlWebViewClient shouldInterceptRequest headerElement[i]: ";
        r8.<init>(r9);	 Catch:{ Exception -> 0x015c }
        r9 = r7[r1];	 Catch:{ Exception -> 0x015c }
        r9 = r9.toString();	 Catch:{ Exception -> 0x015c }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x015c }
        r9 = " and mimeType = ";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x015c }
        r8 = r8.append(r3);	 Catch:{ Exception -> 0x015c }
        r8 = r8.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r8);	 Catch:{ Exception -> 0x015c }
        r8 = "image";
        r8 = r3.contains(r8);	 Catch:{ Exception -> 0x015c }
        if (r8 != 0) goto L_0x00ea;
    L_0x00d4:
        r1 = new android.webkit.WebResourceResponse;	 Catch:{ Exception -> 0x015c }
        r4 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x015c }
        r5 = r11.toByteArray(r5);	 Catch:{ Exception -> 0x015c }
        r4.<init>(r5);	 Catch:{ Exception -> 0x015c }
        r1.<init>(r3, r6, r4);	 Catch:{ Exception -> 0x015c }
        if (r2 == 0) goto L_0x00e7;
    L_0x00e4:
        r2.close();
    L_0x00e7:
        r0 = r1;
        goto L_0x000c;
    L_0x00ea:
        r1 = r1 + 1;
        goto L_0x00a1;
    L_0x00ed:
        r7 = r4.getContentLength();	 Catch:{ Exception -> 0x015c }
        r9 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r1 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r1 <= 0) goto L_0x0115;
    L_0x00f7:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r4 = "HtmlWebViewClient shouldInterceptRequest contentLength: ";
        r1.<init>(r4);	 Catch:{ Exception -> 0x015c }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x015c }
        r1 = r1.toString();	 Catch:{ Exception -> 0x015c }
        com.mopub.common.logging.MoPubLog.d(r1);	 Catch:{ Exception -> 0x015c }
        if (r12 == 0) goto L_0x0115;
    L_0x010b:
        r1 = r12 instanceof com.mopub.mobileads.HtmlBannerWebView;	 Catch:{ Exception -> 0x015c }
        if (r1 == 0) goto L_0x0115;
    L_0x010f:
        r12 = (com.mopub.mobileads.HtmlBannerWebView) r12;	 Catch:{ Exception -> 0x015c }
        r1 = 1;
        r12.setIsImgLoadSuccess(r1);	 Catch:{ Exception -> 0x015c }
    L_0x0115:
        r1 = new android.webkit.WebResourceResponse;	 Catch:{ Exception -> 0x015c }
        r4 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x015c }
        r5 = r11.toByteArray(r5);	 Catch:{ Exception -> 0x015c }
        r4.<init>(r5);	 Catch:{ Exception -> 0x015c }
        r1.<init>(r3, r6, r4);	 Catch:{ Exception -> 0x015c }
        if (r2 == 0) goto L_0x0128;
    L_0x0125:
        r2.close();
    L_0x0128:
        r0 = r1;
        goto L_0x000c;
    L_0x012b:
        if (r2 == 0) goto L_0x000c;
    L_0x012d:
        r2.close();
        goto L_0x000c;
    L_0x0132:
        r1 = move-exception;
        r2 = r0;
    L_0x0134:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x015a }
        r4 = "HtmlWebViewClient Failed to get url : ";
        r3.<init>(r4);	 Catch:{ all -> 0x015a }
        r1 = r1.getMessage();	 Catch:{ all -> 0x015a }
        r1 = r3.append(r1);	 Catch:{ all -> 0x015a }
        r1 = r1.toString();	 Catch:{ all -> 0x015a }
        com.mopub.common.logging.MoPubLog.d(r1);	 Catch:{ all -> 0x015a }
        if (r2 == 0) goto L_0x000c;
    L_0x014c:
        r2.close();
        goto L_0x000c;
    L_0x0151:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0154:
        if (r2 == 0) goto L_0x0159;
    L_0x0156:
        r2.close();
    L_0x0159:
        throw r0;
    L_0x015a:
        r0 = move-exception;
        goto L_0x0154;
    L_0x015c:
        r1 = move-exception;
        goto L_0x0134;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.HtmlWebViewClient.interceptRequest(android.webkit.WebView, java.lang.String):android.webkit.WebResourceResponse");
    }

    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = input.read(bArr);
            if (-1 == read) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }
}
