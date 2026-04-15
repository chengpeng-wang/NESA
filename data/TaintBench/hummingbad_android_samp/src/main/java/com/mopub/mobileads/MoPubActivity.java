package com.mopub.mobileads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mopub.common.AdReport;
import com.mopub.common.CreativeOrientation;
import com.mopub.common.DataKeys;
import com.mopub.common.util.DeviceUtils;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.mopub.mobileads.factories.HtmlInterstitialWebViewFactory;
import java.io.Serializable;

public class MoPubActivity extends BaseInterstitialActivity {
    /* access modifiers changed from: private */
    public HtmlInterstitialWebView mHtmlInterstitialWebView;

    class BroadcastingInterstitialListener implements CustomEventInterstitialListener {
        BroadcastingInterstitialListener() {
        }

        public void onInterstitialLoaded() {
            MoPubActivity.this.mHtmlInterstitialWebView.loadUrl(JavaScriptWebViewCallbacks.WEB_VIEW_DID_APPEAR.getUrl());
        }

        public void onInterstitialFailed(MoPubErrorCode errorCode) {
            EventForwardingBroadcastReceiver.broadcastAction(MoPubActivity.this, MoPubActivity.this.getBroadcastIdentifier().longValue(), EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_FAIL);
            MoPubActivity.this.finish();
        }

        public void onInterstitialShown() {
        }

        public void onInterstitialClicked() {
            EventForwardingBroadcastReceiver.broadcastAction(MoPubActivity.this, MoPubActivity.this.getBroadcastIdentifier().longValue(), EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_CLICK);
        }

        public void onLeaveApplication() {
        }

        public void onInterstitialDismissed() {
        }
    }

    public static void start(Context context, String htmlData, AdReport adReport, boolean isScrollable, String redirectUrl, String clickthroughUrl, CreativeOrientation creativeOrientation, long broadcastIdentifier) {
        try {
            context.startActivity(createIntent(context, htmlData, adReport, isScrollable, redirectUrl, clickthroughUrl, creativeOrientation, broadcastIdentifier));
        } catch (ActivityNotFoundException e) {
            Log.d("MoPubActivity", "MoPubActivity not found - did you declare it in AndroidManifest.xml?");
        }
    }

    static Intent createIntent(Context context, String htmlData, AdReport adReport, boolean isScrollable, String redirectUrl, String clickthroughUrl, CreativeOrientation orientation, long broadcastIdentifier) {
        Intent intent = new Intent(context, MoPubActivity.class);
        intent.putExtra(DataKeys.HTML_RESPONSE_BODY_KEY, htmlData);
        intent.putExtra(DataKeys.SCROLLABLE_KEY, isScrollable);
        intent.putExtra(DataKeys.CLICKTHROUGH_URL_KEY, clickthroughUrl);
        intent.putExtra(DataKeys.REDIRECT_URL_KEY, redirectUrl);
        intent.putExtra(DataKeys.BROADCAST_IDENTIFIER_KEY, broadcastIdentifier);
        intent.putExtra(DataKeys.AD_REPORT_KEY, adReport);
        intent.putExtra(DataKeys.CREATIVE_ORIENTATION_KEY, orientation);
        intent.addFlags(268435456);
        return intent;
    }

    static void preRenderHtml(Context context, AdReport adReport, final CustomEventInterstitialListener customEventInterstitialListener, String htmlData) {
        HtmlInterstitialWebView create = HtmlInterstitialWebViewFactory.create(context, adReport, customEventInterstitialListener, false, null, null);
        create.enablePlugins(false);
        create.setWebViewClient(new WebViewClient() {
            public final boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("mopub://finishLoad")) {
                    customEventInterstitialListener.onInterstitialLoaded();
                } else if (url.equals("mopub://failLoad")) {
                    customEventInterstitialListener.onInterstitialFailed(null);
                }
                return true;
            }
        });
        create.loadHtmlResponse(htmlData);
    }

    public View getAdView() {
        Intent intent = getIntent();
        boolean booleanExtra = intent.getBooleanExtra(DataKeys.SCROLLABLE_KEY, false);
        String stringExtra = intent.getStringExtra(DataKeys.REDIRECT_URL_KEY);
        String stringExtra2 = intent.getStringExtra(DataKeys.CLICKTHROUGH_URL_KEY);
        String stringExtra3 = intent.getStringExtra(DataKeys.HTML_RESPONSE_BODY_KEY);
        this.mHtmlInterstitialWebView = HtmlInterstitialWebViewFactory.create(getApplicationContext(), this.mAdReport, new BroadcastingInterstitialListener(), booleanExtra, stringExtra, stringExtra2);
        this.mHtmlInterstitialWebView.loadHtmlResponse(stringExtra3);
        return this.mHtmlInterstitialWebView;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        CreativeOrientation creativeOrientation;
        super.onCreate(savedInstanceState);
        Serializable serializableExtra = getIntent().getSerializableExtra(DataKeys.CREATIVE_ORIENTATION_KEY);
        if (serializableExtra == null || !(serializableExtra instanceof CreativeOrientation)) {
            creativeOrientation = CreativeOrientation.UNDEFINED;
        } else {
            creativeOrientation = (CreativeOrientation) serializableExtra;
        }
        DeviceUtils.lockOrientation(this, creativeOrientation);
        EventForwardingBroadcastReceiver.broadcastAction(this, getBroadcastIdentifier().longValue(), EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_SHOW);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mHtmlInterstitialWebView.loadUrl(JavaScriptWebViewCallbacks.WEB_VIEW_DID_CLOSE.getUrl());
        this.mHtmlInterstitialWebView.destroy();
        EventForwardingBroadcastReceiver.broadcastAction(this, getBroadcastIdentifier().longValue(), EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_DISMISS);
        super.onDestroy();
    }
}
