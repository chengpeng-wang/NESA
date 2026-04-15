package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.millennialmedia.AppInfo;
import com.millennialmedia.InlineAd;
import com.millennialmedia.InlineAd.AdSize;
import com.millennialmedia.InlineAd.InlineAdMetadata;
import com.millennialmedia.InlineAd.InlineErrorStatus;
import com.millennialmedia.InlineAd.InlineListener;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.mopub.mobileads.CustomEventBanner.CustomEventBannerListener;
import java.util.Map;

class MillennialBanner extends CustomEventBanner {
    public static final String AD_HEIGHT_KEY = "adHeight";
    public static final String AD_WIDTH_KEY = "adWidth";
    public static final String APID_KEY = "adUnitID";
    public static final String DCN_KEY = "dcn";
    public static final String LOGCAT_TAG = "MP->MM Inline";
    /* access modifiers changed from: private|static|final */
    public static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public CustomEventBannerListener mBannerListener;
    private InlineAd mInlineAd;
    /* access modifiers changed from: private */
    public LinearLayout mInternalView;

    class MillennialInlineListener implements InlineListener {
        MillennialInlineListener() {
        }

        public void onAdLeftApplication(InlineAd arg0) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Leaving application");
        }

        public void onClicked(InlineAd arg0) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Ad clicked");
            MillennialBanner.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialBanner.this.mBannerListener.onBannerClicked();
                }
            });
        }

        public void onCollapsed(InlineAd arg0) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner collapsed");
            MillennialBanner.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialBanner.this.mBannerListener.onBannerCollapsed();
                }
            });
        }

        public void onExpanded(InlineAd arg0) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner expanded");
            MillennialBanner.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialBanner.this.mBannerListener.onBannerExpanded();
                }
            });
        }

        public void onRequestFailed(InlineAd arg0, InlineErrorStatus err) {
            MoPubErrorCode moPubErrorCode;
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner failed (" + err.getErrorCode() + "): " + err.getDescription());
            switch (err.getErrorCode()) {
                case 1:
                    moPubErrorCode = MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                    break;
                case 2:
                    moPubErrorCode = MoPubErrorCode.NO_CONNECTION;
                    break;
                case 3:
                    moPubErrorCode = MoPubErrorCode.WARMUP;
                    break;
                case 4:
                    moPubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
                case 7:
                    moPubErrorCode = MoPubErrorCode.UNSPECIFIED;
                    break;
                default:
                    moPubErrorCode = MoPubErrorCode.NETWORK_NO_FILL;
                    break;
            }
            MillennialBanner.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialBanner.this.mBannerListener.onBannerFailed(moPubErrorCode);
                }
            });
        }

        public void onRequestSucceeded(InlineAd arg0) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner request succeeded");
            MillennialBanner.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialBanner.this.mBannerListener.onBannerLoaded(MillennialBanner.this.mInternalView);
                }
            });
        }

        public void onResize(InlineAd arg0, int w, int h) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner about to resize (width: " + w + ", height: " + h + ")");
        }

        public void onResized(InlineAd arg0, int w, int h, boolean isClosed) {
            Log.d(MillennialBanner.LOGCAT_TAG, "Millennial Inline Ad - Banner resized (width: " + w + ", height: " + h + "). " + (isClosed ? "Returned to original placement." : "Got a fresh, new place."));
        }
    }

    MillennialBanner() {
    }

    /* access modifiers changed from: protected */
    public void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mBannerListener = customEventBannerListener;
        if (!MMSDK.isInitialized()) {
            try {
                MMSDK.initialize((Activity) context);
            } catch (Exception e) {
                Log.e(LOGCAT_TAG, "Unable to initialize the Millennial SDK-- " + e.getMessage());
                e.printStackTrace();
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialBanner.this.mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
                    }
                });
                return;
            }
        }
        if (extrasAreValid(serverExtras)) {
            String dcn = (String) serverExtras.get("dcn");
            String apid = (String) serverExtras.get("adUnitID");
            int parseInt = Integer.parseInt((String) serverExtras.get(AD_WIDTH_KEY));
            int parseInt2 = Integer.parseInt((String) serverExtras.get(AD_HEIGHT_KEY));
            try {
                AppInfo mediator = new AppInfo().setMediator("mopubsdk");
                if (dcn == null || dcn.length() <= 0) {
                    mediator = mediator.setSiteId(null);
                } else {
                    mediator = mediator.setSiteId(dcn);
                }
                MMSDK.setAppInfo(mediator);
                this.mInternalView = new LinearLayout(context);
                LayoutParams layoutParams = new LayoutParams(-1, -2);
                layoutParams.gravity = 1;
                this.mInternalView.setLayoutParams(layoutParams);
                try {
                    this.mInlineAd = InlineAd.createInstance(apid, this.mInternalView);
                    InlineAdMetadata adSize = new InlineAdMetadata().setAdSize(new AdSize(parseInt, parseInt2));
                    this.mInlineAd.setListener(new MillennialInlineListener());
                    MMSDK.setLocationEnabled(localExtras.get("location") != null);
                    AdViewController.setShouldHonorServerDimensions(this.mInternalView);
                    this.mInlineAd.request(adSize);
                    return;
                } catch (MMException e2) {
                    e2.printStackTrace();
                    UI_THREAD_HANDLER.post(new Runnable() {
                        public void run() {
                            MillennialBanner.this.mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
                        }
                    });
                    return;
                }
            } catch (IllegalStateException e3) {
                Log.i(LOGCAT_TAG, "Caught exception " + e3.getMessage());
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialBanner.this.mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
                    }
                });
                return;
            }
        }
        Log.e(LOGCAT_TAG, "We were given invalid extras! Make sure placement ID, width, and height are specified.");
        UI_THREAD_HANDLER.post(new Runnable() {
            public void run() {
                MillennialBanner.this.mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onInvalidate() {
        if (this.mInlineAd != null) {
            this.mInlineAd.setListener(null);
            this.mInlineAd = null;
        }
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        try {
            int parseInt = Integer.parseInt((String) serverExtras.get(AD_WIDTH_KEY));
            if (Integer.parseInt((String) serverExtras.get(AD_HEIGHT_KEY)) >= 0 && parseInt >= 0) {
                return serverExtras.containsKey("adUnitID");
            }
            throw new NumberFormatException();
        } catch (Exception e) {
            Log.e(LOGCAT_TAG, "Width and height must exist and contain positive integers!");
            e.printStackTrace();
            return false;
        }
    }
}
