package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Utils;
import com.mopub.common.util.VersionCode;

class VastWebView extends BaseWebView {
    @Nullable
    VastWebViewClickListener mVastWebViewClickListener;

    interface VastWebViewClickListener {
        void onVastWebViewClick();
    }

    class VastWebViewOnTouchListener implements OnTouchListener {
        private boolean mClickStarted;

        VastWebViewOnTouchListener() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.mClickStarted = true;
                    break;
                case 1:
                    if (this.mClickStarted) {
                        this.mClickStarted = false;
                        if (VastWebView.this.mVastWebViewClickListener != null) {
                            VastWebView.this.mVastWebViewClickListener.onVastWebViewClick();
                            break;
                        }
                    }
                    break;
            }
            return false;
        }
    }

    VastWebView(Context context) {
        super(context);
        disableScrollingAndZoom();
        getSettings().setJavaScriptEnabled(true);
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.ICE_CREAM_SANDWICH)) {
            enablePlugins(true);
        }
        setBackgroundColor(0);
        setOnTouchListener(new VastWebViewOnTouchListener());
        setId((int) Utils.generateUniqueId());
    }

    /* access modifiers changed from: 0000 */
    public void loadData(String data) {
        loadDataWithBaseURL("http://ads.mopub.com/", data, "text/html", "utf-8", null);
    }

    /* access modifiers changed from: 0000 */
    public void setVastWebViewClickListener(@NonNull VastWebViewClickListener vastWebViewClickListener) {
        this.mVastWebViewClickListener = vastWebViewClickListener;
    }

    private void disableScrollingAndZoom() {
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        getSettings().setSupportZoom(false);
        setScrollBarStyle(0);
    }

    @NonNull
    static VastWebView createView(@NonNull Context context, @NonNull VastResource vastResource) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(vastResource);
        VastWebView vastWebView = new VastWebView(context);
        vastResource.initializeWebView(vastWebView);
        return vastWebView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @NonNull
    @VisibleForTesting
    public VastWebViewClickListener getVastWebViewClickListener() {
        return this.mVastWebViewClickListener;
    }
}
