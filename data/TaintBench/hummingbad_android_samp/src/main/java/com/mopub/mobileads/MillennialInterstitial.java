package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.millennialmedia.AppInfo;
import com.millennialmedia.InterstitialAd;
import com.millennialmedia.InterstitialAd.InterstitialErrorStatus;
import com.millennialmedia.InterstitialAd.InterstitialListener;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;
import java.util.Map;

class MillennialInterstitial extends CustomEventInterstitial {
    public static final String APID_KEY = "adUnitID";
    public static final String DCN_KEY = "dcn";
    public static final String LOGCAT_TAG = "MP->MM Int.";
    /* access modifiers changed from: private|static|final */
    public static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());
    private Context mContext;
    /* access modifiers changed from: private */
    public CustomEventInterstitialListener mInterstitialListener;
    private InterstitialAd mMillennialInterstitial;

    class MillennialInterstitialListener implements InterstitialListener {
        MillennialInterstitialListener() {
        }

        public void onAdLeftApplication(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Leaving application");
        }

        public void onClicked(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Ad was clicked");
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialClicked();
                }
            });
        }

        public void onClosed(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Ad was closed");
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialDismissed();
                }
            });
        }

        public void onExpired(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Ad expired");
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NO_FILL);
                }
            });
        }

        public void onLoadFailed(InterstitialAd arg0, InterstitialErrorStatus err) {
            MoPubErrorCode moPubErrorCode;
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - load failed (" + err.getErrorCode() + "): " + err.getDescription());
            switch (err.getErrorCode()) {
                case 1:
                case 3:
                case 4:
                case 201:
                    moPubErrorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
                case 2:
                    moPubErrorCode = MoPubErrorCode.NO_CONNECTION;
                    break;
                case 7:
                    moPubErrorCode = MoPubErrorCode.UNSPECIFIED;
                    break;
                case 203:
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialLoaded();
                    Log.w(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Attempted to load ads when ads are already loaded.");
                    return;
                default:
                    moPubErrorCode = MoPubErrorCode.NETWORK_NO_FILL;
                    break;
            }
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(moPubErrorCode);
                }
            });
        }

        public void onLoaded(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Ad loaded splendidly");
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialLoaded();
                }
            });
        }

        public void onShowFailed(InterstitialAd arg0, InterstitialErrorStatus arg1) {
            Log.e(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Show failed (" + arg1.getErrorCode() + "): " + arg1.getDescription());
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                }
            });
        }

        public void onShown(InterstitialAd arg0) {
            Log.d(MillennialInterstitial.LOGCAT_TAG, "Millennial Interstitial Ad - Ad shown");
            MillennialInterstitial.UI_THREAD_HANDLER.post(new Runnable() {
                public void run() {
                    MillennialInterstitial.this.mInterstitialListener.onInterstitialShown();
                }
            });
        }
    }

    MillennialInterstitial() {
    }

    /* access modifiers changed from: protected */
    public void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mInterstitialListener = customEventInterstitialListener;
        this.mContext = context;
        if (!MMSDK.isInitialized()) {
            try {
                MMSDK.initialize((Activity) context);
            } catch (Exception e) {
                Log.e(LOGCAT_TAG, "Unable to initialize the Millennial SDK-- " + e.getMessage());
                e.printStackTrace();
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                    }
                });
                return;
            }
        }
        if (extrasAreValid(serverExtras)) {
            String dcn = (String) serverExtras.get("dcn");
            String str = (String) serverExtras.get("adUnitID");
            try {
                AppInfo mediator = new AppInfo().setMediator("mopubsdk");
                if (dcn == null || dcn.length() <= 0) {
                    mediator.setSiteId(null);
                } else {
                    mediator = mediator.setSiteId(dcn);
                }
                MMSDK.setAppInfo(mediator);
            } catch (IllegalStateException e2) {
                Log.i(LOGCAT_TAG, "SDK not finished initializing-- " + e2.getMessage());
            }
            MMSDK.setLocationEnabled(localExtras.get("location") != null);
            try {
                this.mMillennialInterstitial = InterstitialAd.createInstance(str);
                this.mMillennialInterstitial.setListener(new MillennialInterstitialListener());
                this.mMillennialInterstitial.load(context, null);
                return;
            } catch (MMException e3) {
                e3.printStackTrace();
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                    }
                });
                return;
            }
        }
        Log.e(LOGCAT_TAG, "Invalid extras-- Be sure you have an placement ID specified.");
        UI_THREAD_HANDLER.post(new Runnable() {
            public void run() {
                MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void showInterstitial() {
        if (this.mMillennialInterstitial.isReady()) {
            try {
                this.mMillennialInterstitial.show(this.mContext);
                return;
            } catch (MMException e) {
                e.printStackTrace();
                UI_THREAD_HANDLER.post(new Runnable() {
                    public void run() {
                        MillennialInterstitial.this.mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                    }
                });
                return;
            }
        }
        Log.w(LOGCAT_TAG, "showInterstitial called before Millennial's ad was loaded.");
    }

    /* access modifiers changed from: protected */
    public void onInvalidate() {
        if (this.mMillennialInterstitial != null) {
            this.mMillennialInterstitial.setListener(null);
            this.mMillennialInterstitial = null;
        }
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        return serverExtras.containsKey("adUnitID");
    }
}
