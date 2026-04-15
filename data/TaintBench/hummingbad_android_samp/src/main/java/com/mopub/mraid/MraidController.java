package com.mopub.mraid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.mopub.common.AdReport;
import com.mopub.common.CloseableLayout;
import com.mopub.common.CloseableLayout.ClosePosition;
import com.mopub.common.CloseableLayout.OnCloseListener;
import com.mopub.common.Preconditions;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.common.util.Views;
import com.mopub.mobileads.BaseVideoPlayerActivity;
import com.mopub.mobileads.util.WebViews;
import com.mopub.mraid.MraidBridge.MraidBridgeListener;
import com.mopub.mraid.MraidBridge.MraidWebView;
import java.lang.ref.WeakReference;
import java.net.URI;

public class MraidController {
    private final AdReport mAdReport;
    private boolean mAllowOrientationChange;
    @NonNull
    private final CloseableLayout mCloseableAdContainer;
    /* access modifiers changed from: private|final */
    @NonNull
    public final Context mContext;
    @Nullable
    private MraidWebViewDebugListener mDebugListener;
    /* access modifiers changed from: private|final */
    @NonNull
    public final FrameLayout mDefaultAdContainer;
    private MraidOrientation mForceOrientation;
    private boolean mIsPaused;
    /* access modifiers changed from: private|final */
    @NonNull
    public final MraidBridge mMraidBridge;
    private final MraidBridgeListener mMraidBridgeListener;
    /* access modifiers changed from: private */
    @Nullable
    public MraidListener mMraidListener;
    /* access modifiers changed from: private|final */
    public final MraidNativeCommandHandler mMraidNativeCommandHandler;
    @Nullable
    private MraidWebView mMraidWebView;
    @Nullable
    private UseCustomCloseListener mOnCloseButtonListener;
    @NonNull
    private OrientationBroadcastReceiver mOrientationBroadcastReceiver;
    @Nullable
    private Integer mOriginalActivityOrientation;
    /* access modifiers changed from: private|final */
    @NonNull
    public final PlacementType mPlacementType;
    @Nullable
    private ViewGroup mRootView;
    /* access modifiers changed from: private|final */
    @NonNull
    public final MraidScreenMetrics mScreenMetrics;
    @NonNull
    private final ScreenMetricsWaiter mScreenMetricsWaiter;
    /* access modifiers changed from: private|final */
    @NonNull
    public final MraidBridge mTwoPartBridge;
    private final MraidBridgeListener mTwoPartBridgeListener;
    @Nullable
    private MraidWebView mTwoPartWebView;
    /* access modifiers changed from: private */
    @NonNull
    public ViewState mViewState;
    @NonNull
    private final WeakReference<Activity> mWeakActivity;

    public interface MraidListener {
        void onClose();

        void onExpand();

        void onFailedToLoad();

        void onLoaded(View view);

        void onOpen();
    }

    @VisibleForTesting
    class OrientationBroadcastReceiver extends BroadcastReceiver {
        @Nullable
        private Context mContext;
        private int mLastRotation = -1;

        OrientationBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (this.mContext != null && "android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
                int access$1400 = MraidController.this.getDisplayRotation();
                if (access$1400 != this.mLastRotation) {
                    this.mLastRotation = access$1400;
                    MraidController.this.handleOrientationChange(this.mLastRotation);
                }
            }
        }

        public void register(@NonNull Context context) {
            Preconditions.checkNotNull(context);
            this.mContext = context.getApplicationContext();
            if (this.mContext != null) {
                this.mContext.registerReceiver(this, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
            }
        }

        public void unregister() {
            if (this.mContext != null) {
                this.mContext.unregisterReceiver(this);
                this.mContext = null;
            }
        }
    }

    @VisibleForTesting
    static class ScreenMetricsWaiter {
        @NonNull
        private final Handler mHandler = new Handler();
        @Nullable
        private WaitRequest mLastWaitRequest;

        static class WaitRequest {
            @NonNull
            private final Handler mHandler;
            @Nullable
            private Runnable mSuccessRunnable;
            /* access modifiers changed from: private|final */
            @NonNull
            public final View[] mViews;
            int mWaitCount;
            private final Runnable mWaitingRunnable;

            /* synthetic */ WaitRequest(Handler x0, View[] x1, AnonymousClass1 x2) {
                this(x0, x1);
            }

            private WaitRequest(@NonNull Handler handler, @NonNull View[] views) {
                this.mWaitingRunnable = new Runnable() {
                    public void run() {
                        for (final View view : WaitRequest.this.mViews) {
                            if (view.getHeight() > 0 || view.getWidth() > 0) {
                                WaitRequest.this.countDown();
                            } else {
                                view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                                    public boolean onPreDraw() {
                                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                                        WaitRequest.this.countDown();
                                        return true;
                                    }
                                });
                            }
                        }
                    }
                };
                this.mHandler = handler;
                this.mViews = views;
            }

            /* access modifiers changed from: private */
            public void countDown() {
                this.mWaitCount--;
                if (this.mWaitCount == 0 && this.mSuccessRunnable != null) {
                    this.mSuccessRunnable.run();
                    this.mSuccessRunnable = null;
                }
            }

            /* access modifiers changed from: 0000 */
            public void start(@NonNull Runnable successRunnable) {
                this.mSuccessRunnable = successRunnable;
                this.mWaitCount = this.mViews.length;
                this.mHandler.post(this.mWaitingRunnable);
            }

            /* access modifiers changed from: 0000 */
            public void cancel() {
                this.mHandler.removeCallbacks(this.mWaitingRunnable);
                this.mSuccessRunnable = null;
            }
        }

        ScreenMetricsWaiter() {
        }

        /* access modifiers changed from: varargs */
        public WaitRequest waitFor(@NonNull View... views) {
            this.mLastWaitRequest = new WaitRequest(this.mHandler, views, null);
            return this.mLastWaitRequest;
        }

        /* access modifiers changed from: 0000 */
        public void cancelLastRequest() {
            if (this.mLastWaitRequest != null) {
                this.mLastWaitRequest.cancel();
                this.mLastWaitRequest = null;
            }
        }
    }

    public interface UseCustomCloseListener {
        void useCustomCloseChanged(boolean z);
    }

    public MraidController(@NonNull Context context, @Nullable AdReport adReport, @NonNull PlacementType placementType) {
        this(context, adReport, placementType, new MraidBridge(adReport, placementType), new MraidBridge(adReport, PlacementType.INTERSTITIAL), new ScreenMetricsWaiter());
    }

    @VisibleForTesting
    MraidController(@NonNull Context context, @Nullable AdReport adReport, @NonNull PlacementType placementType, @NonNull MraidBridge bridge, @NonNull MraidBridge twoPartBridge, @NonNull ScreenMetricsWaiter screenMetricsWaiter) {
        this.mViewState = ViewState.LOADING;
        this.mOrientationBroadcastReceiver = new OrientationBroadcastReceiver();
        this.mAllowOrientationChange = true;
        this.mForceOrientation = MraidOrientation.NONE;
        this.mMraidBridgeListener = new MraidBridgeListener() {
            public void onPageLoaded() {
                MraidController.this.handlePageLoad();
            }

            public void onPageFailedToLoad() {
                if (MraidController.this.mMraidListener != null) {
                    MraidController.this.mMraidListener.onFailedToLoad();
                }
            }

            public void onVisibilityChanged(boolean isVisible) {
                if (!MraidController.this.mTwoPartBridge.isAttached()) {
                    MraidController.this.mMraidBridge.notifyViewability(isVisible);
                }
            }

            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                return MraidController.this.handleJsAlert(message, result);
            }

            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                return MraidController.this.handleConsoleMessage(consoleMessage);
            }

            public void onClose() {
                MraidController.this.handleClose();
            }

            public void onResize(int width, int height, int offsetX, int offsetY, @NonNull ClosePosition closePosition, boolean allowOffscreen) throws MraidCommandException {
                MraidController.this.handleResize(width, height, offsetX, offsetY, closePosition, allowOffscreen);
            }

            public void onExpand(@Nullable URI uri, boolean shouldUseCustomClose) throws MraidCommandException {
                MraidController.this.handleExpand(uri, shouldUseCustomClose);
            }

            public void onUseCustomClose(boolean shouldUseCustomClose) {
                MraidController.this.handleCustomClose(shouldUseCustomClose);
            }

            public void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidCommandException {
                MraidController.this.handleSetOrientationProperties(allowOrientationChange, forceOrientation);
            }

            public void onOpen(@NonNull URI uri) {
                MraidController.this.handleOpen(uri.toString());
            }

            public void onPlayVideo(@NonNull URI uri) {
                MraidController.this.handleShowVideo(uri.toString());
            }
        };
        this.mTwoPartBridgeListener = new MraidBridgeListener() {
            public void onPageLoaded() {
                MraidController.this.handleTwoPartPageLoad();
            }

            public void onPageFailedToLoad() {
            }

            public void onVisibilityChanged(boolean isVisible) {
                MraidController.this.mMraidBridge.notifyViewability(isVisible);
                MraidController.this.mTwoPartBridge.notifyViewability(isVisible);
            }

            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                return MraidController.this.handleJsAlert(message, result);
            }

            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                return MraidController.this.handleConsoleMessage(consoleMessage);
            }

            public void onResize(int width, int height, int offsetX, int offsetY, @NonNull ClosePosition closePosition, boolean allowOffscreen) throws MraidCommandException {
                throw new MraidCommandException("Not allowed to resize from an expanded state");
            }

            public void onExpand(@Nullable URI uri, boolean shouldUseCustomClose) {
            }

            public void onClose() {
                MraidController.this.handleClose();
            }

            public void onUseCustomClose(boolean shouldUseCustomClose) {
                MraidController.this.handleCustomClose(shouldUseCustomClose);
            }

            public void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidCommandException {
                MraidController.this.handleSetOrientationProperties(allowOrientationChange, forceOrientation);
            }

            public void onOpen(URI uri) {
                MraidController.this.handleOpen(uri.toString());
            }

            public void onPlayVideo(@NonNull URI uri) {
                MraidController.this.handleShowVideo(uri.toString());
            }
        };
        this.mContext = context.getApplicationContext();
        Preconditions.checkNotNull(this.mContext);
        this.mAdReport = adReport;
        if (context instanceof Activity) {
            this.mWeakActivity = new WeakReference((Activity) context);
        } else {
            this.mWeakActivity = new WeakReference(null);
        }
        this.mPlacementType = placementType;
        this.mMraidBridge = bridge;
        this.mTwoPartBridge = twoPartBridge;
        this.mScreenMetricsWaiter = screenMetricsWaiter;
        this.mViewState = ViewState.LOADING;
        this.mScreenMetrics = new MraidScreenMetrics(this.mContext, this.mContext.getResources().getDisplayMetrics().density);
        this.mDefaultAdContainer = new FrameLayout(this.mContext);
        this.mCloseableAdContainer = new CloseableLayout(this.mContext);
        this.mCloseableAdContainer.setOnCloseListener(new OnCloseListener() {
            public void onClose() {
                MraidController.this.handleClose();
            }
        });
        View view = new View(this.mContext);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mCloseableAdContainer.addView(view, new LayoutParams(-1, -1));
        this.mOrientationBroadcastReceiver.register(this.mContext);
        this.mMraidBridge.setMraidBridgeListener(this.mMraidBridgeListener);
        this.mTwoPartBridge.setMraidBridgeListener(this.mTwoPartBridgeListener);
        this.mMraidNativeCommandHandler = new MraidNativeCommandHandler();
    }

    public void setMraidListener(@Nullable MraidListener mraidListener) {
        this.mMraidListener = mraidListener;
    }

    public void setUseCustomCloseListener(@Nullable UseCustomCloseListener listener) {
        this.mOnCloseButtonListener = listener;
    }

    public void setDebugListener(@Nullable MraidWebViewDebugListener debugListener) {
        this.mDebugListener = debugListener;
    }

    public void loadContent(@NonNull String htmlData) {
        Preconditions.checkState(this.mMraidWebView == null, "loadContent should only be called once");
        this.mMraidWebView = new MraidWebView(this.mContext);
        this.mMraidBridge.attachView(this.mMraidWebView);
        this.mDefaultAdContainer.addView(this.mMraidWebView, new LayoutParams(-1, -1));
        this.mMraidBridge.setContentHtml(htmlData);
    }

    /* access modifiers changed from: private */
    public int getDisplayRotation() {
        return ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean handleConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
        if (this.mDebugListener != null) {
            return this.mDebugListener.onConsoleMessage(consoleMessage);
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean handleJsAlert(@NonNull String message, @NonNull JsResult result) {
        if (this.mDebugListener != null) {
            return this.mDebugListener.onJsAlert(message, result);
        }
        result.confirm();
        return true;
    }

    @Nullable
    private View getCurrentWebView() {
        return this.mTwoPartBridge.isAttached() ? this.mTwoPartWebView : this.mMraidWebView;
    }

    /* access modifiers changed from: private */
    public boolean isInlineVideoAvailable() {
        Activity activity = (Activity) this.mWeakActivity.get();
        if (activity == null || getCurrentWebView() == null) {
            return false;
        }
        return this.mMraidNativeCommandHandler.isInlineVideoAvailable(activity, getCurrentWebView());
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handlePageLoad() {
        setViewState(ViewState.DEFAULT, new Runnable() {
            public void run() {
                MraidController.this.mMraidBridge.notifySupports(MraidController.this.mMraidNativeCommandHandler.isSmsAvailable(MraidController.this.mContext), MraidController.this.mMraidNativeCommandHandler.isTelAvailable(MraidController.this.mContext), MraidNativeCommandHandler.isCalendarAvailable(MraidController.this.mContext), MraidNativeCommandHandler.isStorePictureSupported(MraidController.this.mContext), MraidController.this.isInlineVideoAvailable());
                MraidController.this.mMraidBridge.notifyPlacementType(MraidController.this.mPlacementType);
                MraidController.this.mMraidBridge.notifyViewability(MraidController.this.mMraidBridge.isVisible());
                MraidController.this.mMraidBridge.notifyReady();
            }
        });
        if (this.mMraidListener != null) {
            this.mMraidListener.onLoaded(this.mDefaultAdContainer);
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleTwoPartPageLoad() {
        updateScreenMetricsAsync(new Runnable() {
            public void run() {
                MraidBridge access$100 = MraidController.this.mTwoPartBridge;
                boolean isSmsAvailable = MraidController.this.mMraidNativeCommandHandler.isSmsAvailable(MraidController.this.mContext);
                boolean isTelAvailable = MraidController.this.mMraidNativeCommandHandler.isTelAvailable(MraidController.this.mContext);
                MraidController.this.mMraidNativeCommandHandler;
                boolean isCalendarAvailable = MraidNativeCommandHandler.isCalendarAvailable(MraidController.this.mContext);
                MraidController.this.mMraidNativeCommandHandler;
                access$100.notifySupports(isSmsAvailable, isTelAvailable, isCalendarAvailable, MraidNativeCommandHandler.isStorePictureSupported(MraidController.this.mContext), MraidController.this.isInlineVideoAvailable());
                MraidController.this.mTwoPartBridge.notifyViewState(MraidController.this.mViewState);
                MraidController.this.mTwoPartBridge.notifyPlacementType(MraidController.this.mPlacementType);
                MraidController.this.mTwoPartBridge.notifyViewability(MraidController.this.mTwoPartBridge.isVisible());
                MraidController.this.mTwoPartBridge.notifyReady();
            }
        });
    }

    private void updateScreenMetricsAsync(@Nullable final Runnable successRunnable) {
        this.mScreenMetricsWaiter.cancelLastRequest();
        final View currentWebView = getCurrentWebView();
        if (currentWebView != null) {
            this.mScreenMetricsWaiter.waitFor(this.mDefaultAdContainer, currentWebView).start(new Runnable() {
                public void run() {
                    DisplayMetrics displayMetrics = MraidController.this.mContext.getResources().getDisplayMetrics();
                    MraidController.this.mScreenMetrics.setScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
                    int[] iArr = new int[2];
                    ViewGroup access$1200 = MraidController.this.getRootView();
                    if (access$1200 != null) {
                        access$1200.getLocationOnScreen(iArr);
                        MraidController.this.mScreenMetrics.setRootViewPosition(iArr[0], iArr[1], access$1200.getWidth(), access$1200.getHeight());
                        MraidController.this.mDefaultAdContainer.getLocationOnScreen(iArr);
                        MraidController.this.mScreenMetrics.setDefaultAdPosition(iArr[0], iArr[1], MraidController.this.mDefaultAdContainer.getWidth(), MraidController.this.mDefaultAdContainer.getHeight());
                        currentWebView.getLocationOnScreen(iArr);
                        MraidController.this.mScreenMetrics.setCurrentAdPosition(iArr[0], iArr[1], currentWebView.getWidth(), currentWebView.getHeight());
                        MraidController.this.mMraidBridge.notifyScreenMetrics(MraidController.this.mScreenMetrics);
                        if (MraidController.this.mTwoPartBridge.isAttached()) {
                            MraidController.this.mTwoPartBridge.notifyScreenMetrics(MraidController.this.mScreenMetrics);
                        }
                        if (successRunnable != null) {
                            successRunnable.run();
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleOrientationChange(int currentRotation) {
        updateScreenMetricsAsync(null);
    }

    public void pause(boolean isFinishing) {
        this.mIsPaused = true;
        if (this.mMraidWebView != null) {
            WebViews.onPause(this.mMraidWebView, isFinishing);
        }
        if (this.mTwoPartWebView != null) {
            WebViews.onPause(this.mTwoPartWebView, isFinishing);
        }
    }

    public void resume() {
        this.mIsPaused = false;
        if (this.mMraidWebView != null) {
            WebViews.onResume(this.mMraidWebView);
        }
        if (this.mTwoPartWebView != null) {
            WebViews.onResume(this.mTwoPartWebView);
        }
    }

    public void destroy() {
        this.mScreenMetricsWaiter.cancelLastRequest();
        try {
            this.mOrientationBroadcastReceiver.unregister();
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("Receiver not registered")) {
                throw e;
            }
        }
        if (!this.mIsPaused) {
            pause(true);
        }
        Views.removeFromParent(this.mCloseableAdContainer);
        this.mMraidBridge.detach();
        if (this.mMraidWebView != null) {
            this.mMraidWebView.destroy();
            this.mMraidWebView = null;
        }
        this.mTwoPartBridge.detach();
        if (this.mTwoPartWebView != null) {
            this.mTwoPartWebView.destroy();
            this.mTwoPartWebView = null;
        }
    }

    private void setViewState(@NonNull ViewState viewState) {
        setViewState(viewState, null);
    }

    private void setViewState(@NonNull ViewState viewState, @Nullable Runnable successRunnable) {
        MoPubLog.d("MRAID state set to " + viewState);
        this.mViewState = viewState;
        this.mMraidBridge.notifyViewState(viewState);
        if (this.mTwoPartBridge.isLoaded()) {
            this.mTwoPartBridge.notifyViewState(viewState);
        }
        if (this.mMraidListener != null) {
            if (viewState == ViewState.EXPANDED) {
                this.mMraidListener.onExpand();
            } else if (viewState == ViewState.HIDDEN) {
                this.mMraidListener.onClose();
            }
        }
        updateScreenMetricsAsync(successRunnable);
    }

    /* access modifiers changed from: 0000 */
    public int clampInt(int min, int target, int max) {
        return Math.max(min, Math.min(target, max));
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleResize(int widthDips, int heightDips, int offsetXDips, int offsetYDips, @NonNull ClosePosition closePosition, boolean allowOffscreen) throws MraidCommandException {
        if (this.mMraidWebView == null) {
            throw new MraidCommandException("Unable to resize after the WebView is destroyed");
        } else if (this.mViewState != ViewState.LOADING && this.mViewState != ViewState.HIDDEN) {
            if (this.mViewState == ViewState.EXPANDED) {
                throw new MraidCommandException("Not allowed to resize from an already expanded ad");
            } else if (this.mPlacementType == PlacementType.INTERSTITIAL) {
                throw new MraidCommandException("Not allowed to resize from an interstitial ad");
            } else {
                Rect rootViewRect;
                int dipsToIntPixels = Dips.dipsToIntPixels((float) widthDips, this.mContext);
                int dipsToIntPixels2 = Dips.dipsToIntPixels((float) heightDips, this.mContext);
                int dipsToIntPixels3 = Dips.dipsToIntPixels((float) offsetXDips, this.mContext);
                dipsToIntPixels3 += this.mScreenMetrics.getDefaultAdRect().left;
                int dipsToIntPixels4 = Dips.dipsToIntPixels((float) offsetYDips, this.mContext) + this.mScreenMetrics.getDefaultAdRect().top;
                Rect rect = new Rect(dipsToIntPixels3, dipsToIntPixels4, dipsToIntPixels + dipsToIntPixels3, dipsToIntPixels4 + dipsToIntPixels2);
                if (!allowOffscreen) {
                    rootViewRect = this.mScreenMetrics.getRootViewRect();
                    if (rect.width() > rootViewRect.width() || rect.height() > rootViewRect.height()) {
                        throw new MraidCommandException("resizeProperties specified a size (" + widthDips + ", " + heightDips + ") and offset (" + offsetXDips + ", " + offsetYDips + ") that doesn't allow the ad to appear within the max allowed size (" + this.mScreenMetrics.getRootViewRectDips().width() + ", " + this.mScreenMetrics.getRootViewRectDips().height() + ")");
                    }
                    rect.offsetTo(clampInt(rootViewRect.left, rect.left, rootViewRect.right - rect.width()), clampInt(rootViewRect.top, rect.top, rootViewRect.bottom - rect.height()));
                }
                rootViewRect = new Rect();
                this.mCloseableAdContainer.applyCloseRegionBounds(closePosition, rect, rootViewRect);
                if (!this.mScreenMetrics.getRootViewRect().contains(rootViewRect)) {
                    throw new MraidCommandException("resizeProperties specified a size (" + widthDips + ", " + heightDips + ") and offset (" + offsetXDips + ", " + offsetYDips + ") that doesn't allow the close region to appear within the max allowed size (" + this.mScreenMetrics.getRootViewRectDips().width() + ", " + this.mScreenMetrics.getRootViewRectDips().height() + ")");
                } else if (rect.contains(rootViewRect)) {
                    this.mCloseableAdContainer.setCloseVisible(false);
                    this.mCloseableAdContainer.setClosePosition(closePosition);
                    LayoutParams layoutParams = new LayoutParams(rect.width(), rect.height());
                    layoutParams.leftMargin = rect.left - this.mScreenMetrics.getRootViewRect().left;
                    layoutParams.topMargin = rect.top - this.mScreenMetrics.getRootViewRect().top;
                    if (this.mViewState == ViewState.DEFAULT) {
                        this.mDefaultAdContainer.removeView(this.mMraidWebView);
                        this.mDefaultAdContainer.setVisibility(4);
                        this.mCloseableAdContainer.addView(this.mMraidWebView, new LayoutParams(-1, -1));
                        getRootView().addView(this.mCloseableAdContainer, layoutParams);
                    } else if (this.mViewState == ViewState.RESIZED) {
                        this.mCloseableAdContainer.setLayoutParams(layoutParams);
                    }
                    this.mCloseableAdContainer.setClosePosition(closePosition);
                    setViewState(ViewState.RESIZED);
                } else {
                    throw new MraidCommandException("resizeProperties specified a size (" + widthDips + ", " + dipsToIntPixels2 + ") and offset (" + offsetXDips + ", " + offsetYDips + ") that don't allow the close region to appear within the resized ad.");
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleExpand(@Nullable URI uri, boolean shouldUseCustomClose) throws MraidCommandException {
        if (this.mMraidWebView == null) {
            throw new MraidCommandException("Unable to expand after the WebView is destroyed");
        } else if (this.mPlacementType != PlacementType.INTERSTITIAL) {
            if (this.mViewState == ViewState.DEFAULT || this.mViewState == ViewState.RESIZED) {
                applyOrientation();
                Object obj = uri != null ? 1 : null;
                if (obj != null) {
                    this.mTwoPartWebView = new MraidWebView(this.mContext);
                    this.mTwoPartBridge.attachView(this.mTwoPartWebView);
                    this.mTwoPartBridge.setContentUrl(uri.toString());
                }
                LayoutParams layoutParams = new LayoutParams(-1, -1);
                if (this.mViewState == ViewState.DEFAULT) {
                    if (obj != null) {
                        this.mCloseableAdContainer.addView(this.mTwoPartWebView, layoutParams);
                    } else {
                        this.mDefaultAdContainer.removeView(this.mMraidWebView);
                        this.mDefaultAdContainer.setVisibility(4);
                        this.mCloseableAdContainer.addView(this.mMraidWebView, layoutParams);
                    }
                    getRootView().addView(this.mCloseableAdContainer, new LayoutParams(-1, -1));
                } else if (this.mViewState == ViewState.RESIZED && obj != null) {
                    this.mCloseableAdContainer.removeView(this.mMraidWebView);
                    this.mDefaultAdContainer.addView(this.mMraidWebView, layoutParams);
                    this.mDefaultAdContainer.setVisibility(4);
                    this.mCloseableAdContainer.addView(this.mTwoPartWebView, layoutParams);
                }
                this.mCloseableAdContainer.setLayoutParams(layoutParams);
                handleCustomClose(shouldUseCustomClose);
                setViewState(ViewState.EXPANDED);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleClose() {
        if (this.mMraidWebView != null && this.mViewState != ViewState.LOADING && this.mViewState != ViewState.HIDDEN) {
            if (this.mViewState == ViewState.EXPANDED || this.mPlacementType == PlacementType.INTERSTITIAL) {
                unApplyOrientation();
            }
            if (this.mViewState == ViewState.RESIZED || this.mViewState == ViewState.EXPANDED) {
                if (!this.mTwoPartBridge.isAttached() || this.mTwoPartWebView == null) {
                    this.mCloseableAdContainer.removeView(this.mMraidWebView);
                    this.mDefaultAdContainer.addView(this.mMraidWebView, new LayoutParams(-1, -1));
                    this.mDefaultAdContainer.setVisibility(0);
                } else {
                    this.mCloseableAdContainer.removeView(this.mTwoPartWebView);
                    this.mTwoPartBridge.detach();
                }
                getRootView().removeView(this.mCloseableAdContainer);
                setViewState(ViewState.DEFAULT);
            } else if (this.mViewState == ViewState.DEFAULT) {
                this.mDefaultAdContainer.setVisibility(4);
                setViewState(ViewState.HIDDEN);
            }
        }
    }

    /* access modifiers changed from: private */
    @TargetApi(19)
    @NonNull
    public ViewGroup getRootView() {
        if (this.mRootView == null) {
            if (VERSION.SDK_INT >= 19) {
                try {
                    Preconditions.checkState(this.mDefaultAdContainer.isAttachedToWindow());
                } catch (Exception e) {
                    return null;
                }
            }
            this.mRootView = (ViewGroup) this.mDefaultAdContainer.getRootView().findViewById(16908290);
        }
        return this.mRootView;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleShowVideo(@NonNull String videoUrl) {
        BaseVideoPlayerActivity.startMraid(this.mContext, videoUrl);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void lockOrientation(int screenOrientation) throws MraidCommandException {
        Activity activity = (Activity) this.mWeakActivity.get();
        if (activity == null || !shouldAllowForceOrientation(this.mForceOrientation)) {
            throw new MraidCommandException("Attempted to lock orientation to unsupported value: " + this.mForceOrientation.name());
        }
        if (this.mOriginalActivityOrientation == null) {
            this.mOriginalActivityOrientation = Integer.valueOf(activity.getRequestedOrientation());
        }
        activity.setRequestedOrientation(screenOrientation);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void applyOrientation() throws MraidCommandException {
        if (this.mForceOrientation != MraidOrientation.NONE) {
            lockOrientation(this.mForceOrientation.getActivityInfoOrientation());
        } else if (this.mAllowOrientationChange) {
            unApplyOrientation();
        } else {
            Activity activity = (Activity) this.mWeakActivity.get();
            if (activity == null) {
                throw new MraidCommandException("Unable to set MRAID expand orientation to 'none'; expected passed in Activity Context.");
            }
            lockOrientation(DeviceUtils.getScreenOrientation(activity));
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void unApplyOrientation() {
        Activity activity = (Activity) this.mWeakActivity.get();
        if (!(activity == null || this.mOriginalActivityOrientation == null)) {
            activity.setRequestedOrientation(this.mOriginalActivityOrientation.intValue());
        }
        this.mOriginalActivityOrientation = null;
    }

    /* access modifiers changed from: 0000 */
    @TargetApi(13)
    @VisibleForTesting
    public boolean shouldAllowForceOrientation(MraidOrientation newOrientation) {
        if (newOrientation == MraidOrientation.NONE) {
            return true;
        }
        Activity activity = (Activity) this.mWeakActivity.get();
        if (activity == null) {
            return false;
        }
        try {
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(new ComponentName(activity, activity.getClass()), 0);
            int i = activityInfo.screenOrientation;
            if (i == -1) {
                boolean bitMaskContainsFlag = Utils.bitMaskContainsFlag(activityInfo.configChanges, 128);
                if (VERSION.SDK_INT >= 13) {
                    if (bitMaskContainsFlag && Utils.bitMaskContainsFlag(activityInfo.configChanges, 1024)) {
                        bitMaskContainsFlag = true;
                    } else {
                        bitMaskContainsFlag = false;
                    }
                }
                return bitMaskContainsFlag;
            } else if (i == newOrientation.getActivityInfoOrientation()) {
                return true;
            } else {
                return false;
            }
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleCustomClose(boolean useCustomClose) {
        boolean z = true;
        if (useCustomClose != (!this.mCloseableAdContainer.isCloseVisible())) {
            CloseableLayout closeableLayout = this.mCloseableAdContainer;
            if (useCustomClose) {
                z = false;
            }
            closeableLayout.setCloseVisible(z);
            if (this.mOnCloseButtonListener != null) {
                this.mOnCloseButtonListener.useCustomCloseChanged(useCustomClose);
            }
        }
    }

    @NonNull
    public FrameLayout getAdContainer() {
        return this.mDefaultAdContainer;
    }

    public void loadJavascript(@NonNull String javascript) {
        this.mMraidBridge.injectJavaScript(javascript);
    }

    @NonNull
    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidCommandException {
        if (shouldAllowForceOrientation(forceOrientation)) {
            this.mAllowOrientationChange = allowOrientationChange;
            this.mForceOrientation = forceOrientation;
            if (this.mViewState == ViewState.EXPANDED || this.mPlacementType == PlacementType.INTERSTITIAL) {
                applyOrientation();
                return;
            }
            return;
        }
        throw new MraidCommandException("Unable to force orientation to " + forceOrientation);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void handleOpen(@NonNull String url) {
        if (this.mMraidListener != null) {
            this.mMraidListener.onOpen();
        }
        new Builder().withSupportedUrlActions(UrlAction.IGNORE_ABOUT_SCHEME, UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK).build().handleUrl(this.mContext, url);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @NonNull
    @VisibleForTesting
    public ViewState getViewState() {
        return this.mViewState;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setViewStateForTesting(@NonNull ViewState viewState) {
        this.mViewState = viewState;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @NonNull
    @VisibleForTesting
    public CloseableLayout getExpandedAdContainer() {
        return this.mCloseableAdContainer;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setRootView(FrameLayout rootView) {
        this.mRootView = rootView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setRootViewSize(int width, int height) {
        this.mScreenMetrics.setRootViewPosition(0, 0, width, height);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public Integer getOriginalActivityOrientation() {
        return this.mOriginalActivityOrientation;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean getAllowOrientationChange() {
        return this.mAllowOrientationChange;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public MraidOrientation getForceOrientation() {
        return this.mForceOrientation;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setOrientationBroadcastReceiver(OrientationBroadcastReceiver receiver) {
        this.mOrientationBroadcastReceiver = receiver;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public MraidWebView getMraidWebView() {
        return this.mMraidWebView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public MraidWebView getTwoPartWebView() {
        return this.mTwoPartWebView;
    }
}
