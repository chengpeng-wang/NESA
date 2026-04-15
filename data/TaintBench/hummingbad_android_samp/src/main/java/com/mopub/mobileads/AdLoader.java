package com.mopub.mobileads;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.AdResponse;
import java.lang.ref.WeakReference;
import java.util.Map;

abstract class AdLoader {
    WeakReference<AdViewController> mWeakAdViewController;

    static class CustomEventAdLoader extends AdLoader {
        private String mCustomEventClassName;
        private Map<String, String> mServerExtras;

        public CustomEventAdLoader(AdViewController adViewController, String customEventCLassName, Map<String, String> serverExtras) {
            super(adViewController);
            this.mCustomEventClassName = customEventCLassName;
            this.mServerExtras = serverExtras;
        }

        /* access modifiers changed from: 0000 */
        public void load() {
            AdViewController adViewController = (AdViewController) this.mWeakAdViewController.get();
            if (adViewController != null && !adViewController.isDestroyed() && !TextUtils.isEmpty(this.mCustomEventClassName)) {
                adViewController.setNotLoading();
                MoPubView moPubView = adViewController.getMoPubView();
                if (moPubView == null) {
                    MoPubLog.d("Can't load an ad in this ad view because it was destroyed.");
                } else {
                    moPubView.loadCustomEvent(this.mCustomEventClassName, this.mServerExtras);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public Map<String, String> getServerExtras() {
            return this.mServerExtras;
        }
    }

    public abstract void load();

    AdLoader(AdViewController adViewController) {
        this.mWeakAdViewController = new WeakReference(adViewController);
    }

    @Nullable
    static AdLoader fromAdResponse(AdResponse response, AdViewController adViewController) {
        MoPubLog.i("Performing custom event.");
        String customEventClassName = response.getCustomEventClassName();
        if (customEventClassName != null) {
            return new CustomEventAdLoader(adViewController, customEventClassName, response.getServerExtras());
        }
        MoPubLog.i("Failed to create custom event.");
        return null;
    }
}
