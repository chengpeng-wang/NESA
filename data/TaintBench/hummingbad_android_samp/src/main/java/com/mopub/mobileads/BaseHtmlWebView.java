package com.mopub.mobileads;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.mopub.common.AdReport;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.VersionCode;
import com.mopub.mobileads.ViewGestureDetector.UserClickListener;

public class BaseHtmlWebView extends BaseWebView implements UserClickListener {
    private boolean mClicked;
    /* access modifiers changed from: private|final */
    public final ViewGestureDetector mViewGestureDetector;

    public BaseHtmlWebView(Context context, AdReport adReport) {
        super(context);
        disableScrollingAndZoom();
        getSettings().setJavaScriptEnabled(true);
        this.mViewGestureDetector = new ViewGestureDetector(context, (View) this, adReport);
        this.mViewGestureDetector.setUserClickListener(this);
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.ICE_CREAM_SANDWICH)) {
            enablePlugins(true);
        }
        setBackgroundColor(0);
    }

    public void init(boolean isScrollable) {
        initializeOnTouchListener(isScrollable);
    }

    public void loadUrl(String url) {
        if (url != null) {
            MoPubLog.d("Loading url: " + url);
            if (url.startsWith("javascript:")) {
                super.loadUrl(url);
            }
        }
    }

    private void disableScrollingAndZoom() {
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        getSettings().setSupportZoom(false);
    }

    /* access modifiers changed from: 0000 */
    public void loadHtmlResponse(String htmlResponse) {
        loadDataWithBaseURL("http://ads.mopub.com/", htmlResponse, "text/html", "utf-8", null);
    }

    /* access modifiers changed from: 0000 */
    public void initializeOnTouchListener(final boolean isScrollable) {
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                BaseHtmlWebView.this.mViewGestureDetector.sendTouchEvent(event);
                return event.getAction() == 2 && !isScrollable;
            }
        });
    }

    public void onUserClick() {
        this.mClicked = true;
    }

    public void onResetUserClick() {
        this.mClicked = false;
    }

    public boolean wasClicked() {
        return this.mClicked;
    }
}
