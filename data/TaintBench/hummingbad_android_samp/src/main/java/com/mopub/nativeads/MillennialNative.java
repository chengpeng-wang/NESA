package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.millennialmedia.AppInfo;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.millennialmedia.NativeAd;
import com.millennialmedia.NativeAd.ComponentName;
import com.millennialmedia.NativeAd.NativeErrorStatus;
import com.millennialmedia.NativeAd.NativeListener;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import com.mopub.nativeads.NativeImageHelper.ImageListener;
import java.util.ArrayList;
import java.util.Map;

public class MillennialNative extends CustomEventNative {
    public static final String APID_KEY = "adUnitID";
    public static final String DCN_KEY = "dcn";
    private static final String LOGCAT_TAG = "MoPub->MM-Native";
    /* access modifiers changed from: private|static|final */
    public static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    static class MillennialStaticNativeAd extends StaticNativeAd implements NativeListener {
        /* access modifiers changed from: private|final */
        public final Context mContext;
        private final ImpressionTracker mImpressionTracker;
        /* access modifiers changed from: private|final */
        public final CustomEventNativeListener mListener;
        /* access modifiers changed from: private|final */
        public final MillennialStaticNativeAd mMillennialStaticNativeAd = this;
        private NativeAd mNativeAd;
        private final NativeClickHandler mNativeClickHandler;

        public MillennialStaticNativeAd(Context context, NativeAd nativeAd, ImpressionTracker impressionTracker, NativeClickHandler nativeClickHandler, CustomEventNativeListener customEventNativeListener) {
            this.mContext = context.getApplicationContext();
            this.mNativeAd = nativeAd;
            this.mImpressionTracker = impressionTracker;
            this.mNativeClickHandler = nativeClickHandler;
            this.mListener = customEventNativeListener;
            nativeAd.setListener(this);
        }

        /* access modifiers changed from: 0000 */
        public void loadAd() {
            Log.i(MillennialNative.LOGCAT_TAG, "Loading native ad...");
            try {
                this.mNativeAd.load(this.mContext, null);
            } catch (MMException e) {
                Log.w(MillennialNative.LOGCAT_TAG, "Caught configuration error Exception.");
                e.printStackTrace();
                MillennialNative.UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialStaticNativeAd.this.mListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                    }
                });
            }
        }

        public void prepare(View view) {
            this.mNativeAd.getIconImage();
            this.mNativeAd.getDisclaimer();
            this.mImpressionTracker.addView(view, this);
            this.mNativeClickHandler.setOnClickListener(view, (ClickInterface) this);
        }

        public void clear(View view) {
            this.mImpressionTracker.removeView(view);
            this.mNativeClickHandler.clearOnClickListener(view);
        }

        public void destroy() {
            this.mImpressionTracker.destroy();
            this.mNativeAd.setListener(null);
            this.mNativeAd = null;
        }

        public void recordImpression(View view) {
            notifyAdImpressed();
            try {
                this.mNativeAd.fireImpression();
                Log.i(MillennialNative.LOGCAT_TAG, "Millennial native impression recorded.");
            } catch (MMException e) {
                Log.e(MillennialNative.LOGCAT_TAG, "Millennial native impression NOT tracked: " + e.getMessage());
            }
        }

        public void handleClick(View view) {
            notifyAdClicked();
            this.mNativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
            this.mNativeAd.fireClicked();
            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native ad clicked!");
        }

        public void onLoaded(NativeAd nativeAd) {
            String imageUrl = nativeAd.getImageUrl(ComponentName.ICON_IMAGE, 1);
            String imageUrl2 = nativeAd.getImageUrl(ComponentName.MAIN_IMAGE, 1);
            setTitle(nativeAd.getTitle().getText().toString());
            setText(nativeAd.getBody().getText().toString());
            setCallToAction(nativeAd.getCallToActionButton().getText().toString());
            String callToActionUrl = nativeAd.getCallToActionUrl();
            if (callToActionUrl == null) {
                MillennialNative.UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        Log.d(MillennialNative.LOGCAT_TAG, "Millennial native encountered null destination url. Failing over.");
                        MillennialStaticNativeAd.this.mListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                    }
                });
                return;
            }
            setClickDestinationUrl(callToActionUrl);
            setIconImageUrl(imageUrl);
            setMainImageUrl(imageUrl2);
            final ArrayList arrayList = new ArrayList();
            if (imageUrl != null) {
                arrayList.add(imageUrl);
            }
            if (imageUrl2 != null) {
                arrayList.add(imageUrl2);
            }
            MillennialNative.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    NativeImageHelper.preCacheImages(MillennialStaticNativeAd.this.mContext, arrayList, new ImageListener() {
                        public void onImagesCached() {
                            MillennialStaticNativeAd.this.mListener.onNativeAdLoaded(MillennialStaticNativeAd.this.mMillennialStaticNativeAd);
                            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native ad loaded");
                        }

                        public void onImagesFailedToCache(NativeErrorCode errorCode) {
                            MillennialStaticNativeAd.this.mListener.onNativeAdFailed(errorCode);
                        }
                    });
                }
            });
        }

        public void onLoadFailed(NativeAd nativeAd, NativeErrorStatus nativeErrorStatus) {
            NativeErrorCode nativeErrorCode;
            switch (nativeErrorStatus.getErrorCode()) {
                case 1:
                    nativeErrorCode = NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR;
                    break;
                case 2:
                    nativeErrorCode = NativeErrorCode.CONNECTION_ERROR;
                    break;
                case 3:
                case 5:
                    nativeErrorCode = NativeErrorCode.UNEXPECTED_RESPONSE_CODE;
                    break;
                case 4:
                case 301:
                    nativeErrorCode = NativeErrorCode.UNSPECIFIED;
                    break;
                case 6:
                    nativeErrorCode = NativeErrorCode.NETWORK_TIMEOUT;
                    break;
                case 7:
                    nativeErrorCode = NativeErrorCode.UNSPECIFIED;
                    break;
                default:
                    nativeErrorCode = NativeErrorCode.NETWORK_NO_FILL;
                    break;
            }
            MillennialNative.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialStaticNativeAd.this.mListener.onNativeAdFailed(nativeErrorCode);
                }
            });
            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native ad failed: " + nativeErrorStatus.getDescription());
        }

        public void onClicked(NativeAd nativeAd, ComponentName componentName, int i) {
            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native SDK's click tracker fired.");
        }

        public void onAdLeftApplication(NativeAd nativeAd) {
            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native SDK has left the application.");
        }

        public void onExpired(NativeAd nativeAd) {
            Log.i(MillennialNative.LOGCAT_TAG, "Millennial native ad has expired!");
        }
    }

    /* access modifiers changed from: protected */
    public void loadNativeAd(Activity activity, final CustomEventNativeListener listener, Map<String, Object> map, Map<String, String> serverExtras) {
        if (!MMSDK.isInitialized()) {
            try {
                MMSDK.initialize(activity);
            } catch (Exception e) {
                Log.e(LOGCAT_TAG, "Unable to initialize the Millennial SDK-- " + e.getMessage());
                e.printStackTrace();
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        listener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                    }
                });
                return;
            }
        }
        if (extrasAreValid(serverExtras)) {
            String str = (String) serverExtras.get("adUnitID");
            String str2 = (String) serverExtras.get("dcn");
            try {
                AppInfo siteId;
                AppInfo mediator = new AppInfo().setMediator("mopubsdk");
                if (str2 == null || str2.length() <= 0) {
                    siteId = mediator.setSiteId(null);
                } else {
                    siteId = mediator.setSiteId(str2);
                }
                MMSDK.setAppInfo(siteId);
                try {
                    new MillennialStaticNativeAd(activity, NativeAd.createInstance(str, "inline"), new ImpressionTracker(activity), new NativeClickHandler(activity), listener).loadAd();
                    return;
                } catch (MMException e2) {
                    UI_THREAD_HANDLER.post(new Runnable() {
                        public void run() {
                            listener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                        }
                    });
                    return;
                }
            } catch (IllegalStateException e3) {
                Log.w(LOGCAT_TAG, "Caught exception: " + e3.getMessage());
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        listener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                    }
                });
                return;
            }
        }
        UI_THREAD_HANDLER.post(new Runnable() {
            public void run() {
                listener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            }
        });
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        String str = (String) serverExtras.get("adUnitID");
        return serverExtras.containsKey("adUnitID") && str != null && str.length() > 0;
    }
}
