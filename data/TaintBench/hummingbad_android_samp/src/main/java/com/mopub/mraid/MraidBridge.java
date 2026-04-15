package com.mopub.mraid;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.AdReport;
import com.mopub.common.AdType;
import com.mopub.common.CloseableLayout.ClosePosition;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.BaseWebView;
import com.mopub.mobileads.VastIconXmlManager;
import com.mopub.mobileads.ViewGestureDetector;
import com.mopub.mobileads.ViewGestureDetector.UserClickListener;
import com.mopub.mobileads.resource.MraidJavascript;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

public class MraidBridge {
    private final String FILTERED_JAVASCRIPT_SOURCE;
    private final AdReport mAdReport;
    private boolean mHasLoaded;
    /* access modifiers changed from: private */
    public boolean mIsClicked;
    /* access modifiers changed from: private */
    @Nullable
    public MraidBridgeListener mMraidBridgeListener;
    @NonNull
    private final MraidNativeCommandHandler mMraidNativeCommandHandler;
    @Nullable
    private MraidWebView mMraidWebView;
    private final WebViewClient mMraidWebViewClient;
    @NonNull
    private final PlacementType mPlacementType;

    public interface MraidBridgeListener {
        void onClose();

        boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage);

        void onExpand(URI uri, boolean z) throws MraidCommandException;

        boolean onJsAlert(@NonNull String str, @NonNull JsResult jsResult);

        void onOpen(URI uri);

        void onPageFailedToLoad();

        void onPageLoaded();

        void onPlayVideo(URI uri);

        void onResize(int i, int i2, int i3, int i4, @NonNull ClosePosition closePosition, boolean z) throws MraidCommandException;

        void onSetOrientationProperties(boolean z, MraidOrientation mraidOrientation) throws MraidCommandException;

        void onUseCustomClose(boolean z);

        void onVisibilityChanged(boolean z);
    }

    public static class MraidWebView extends BaseWebView {
        private boolean mIsVisible;
        @Nullable
        private OnVisibilityChangedListener mOnVisibilityChangedListener;

        public interface OnVisibilityChangedListener {
            void onVisibilityChanged(boolean z);
        }

        public MraidWebView(Context context) {
            super(context);
            this.mIsVisible = getVisibility() == 0;
        }

        /* access modifiers changed from: 0000 */
        public void setVisibilityChangedListener(@Nullable OnVisibilityChangedListener listener) {
            this.mOnVisibilityChangedListener = listener;
        }

        /* access modifiers changed from: protected */
        public void onVisibilityChanged(@NonNull View changedView, int visibility) {
            super.onVisibilityChanged(changedView, visibility);
            boolean z = visibility == 0;
            if (z != this.mIsVisible) {
                this.mIsVisible = z;
                if (this.mOnVisibilityChangedListener != null) {
                    this.mOnVisibilityChangedListener.onVisibilityChanged(this.mIsVisible);
                }
            }
        }

        public boolean isVisible() {
            return this.mIsVisible;
        }
    }

    MraidBridge(@Nullable AdReport adReport, @NonNull PlacementType placementType) {
        this(adReport, placementType, new MraidNativeCommandHandler());
    }

    @VisibleForTesting
    MraidBridge(@Nullable AdReport adReport, @NonNull PlacementType placementType, @NonNull MraidNativeCommandHandler mraidNativeCommandHandler) {
        this.FILTERED_JAVASCRIPT_SOURCE = MraidJavascript.JAVASCRIPT_SOURCE.replaceAll("(?m)^\\s+", "").replaceAll("(?m)^//.*(?=\\n)", "");
        this.mMraidWebViewClient = new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                MoPubLog.d("Error: " + description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull String url) {
                return MraidBridge.this.handleShouldOverrideUrl(url);
            }

            public void onPageFinished(@NonNull WebView view, @NonNull String url) {
                MraidBridge.this.handlePageFinished();
            }
        };
        this.mAdReport = adReport;
        this.mPlacementType = placementType;
        this.mMraidNativeCommandHandler = mraidNativeCommandHandler;
    }

    /* access modifiers changed from: 0000 */
    public void setMraidBridgeListener(@Nullable MraidBridgeListener listener) {
        this.mMraidBridgeListener = listener;
    }

    /* access modifiers changed from: 0000 */
    public void attachView(@NonNull MraidWebView mraidWebView) {
        this.mMraidWebView = mraidWebView;
        this.mMraidWebView.getSettings().setJavaScriptEnabled(true);
        if (VERSION.SDK_INT >= 17 && this.mPlacementType == PlacementType.INTERSTITIAL) {
            mraidWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        this.mMraidWebView.loadUrl("javascript:" + this.FILTERED_JAVASCRIPT_SOURCE);
        this.mMraidWebView.setScrollContainer(false);
        this.mMraidWebView.setVerticalScrollBarEnabled(false);
        this.mMraidWebView.setHorizontalScrollBarEnabled(false);
        this.mMraidWebView.setBackgroundColor(-16777216);
        this.mMraidWebView.setWebViewClient(this.mMraidWebViewClient);
        this.mMraidWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (MraidBridge.this.mMraidBridgeListener != null) {
                    return MraidBridge.this.mMraidBridgeListener.onJsAlert(message, result);
                }
                return super.onJsAlert(view, url, message, result);
            }

            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                if (MraidBridge.this.mMraidBridgeListener != null) {
                    return MraidBridge.this.mMraidBridgeListener.onConsoleMessage(consoleMessage);
                }
                return super.onConsoleMessage(consoleMessage);
            }

            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
        final ViewGestureDetector viewGestureDetector = new ViewGestureDetector(this.mMraidWebView.getContext(), this.mMraidWebView, this.mAdReport);
        viewGestureDetector.setUserClickListener(new UserClickListener() {
            public void onUserClick() {
                MraidBridge.this.mIsClicked = true;
            }

            public void onResetUserClick() {
                MraidBridge.this.mIsClicked = false;
            }

            public boolean wasClicked() {
                return MraidBridge.this.mIsClicked;
            }
        });
        this.mMraidWebView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                viewGestureDetector.sendTouchEvent(event);
                switch (event.getAction()) {
                    case 0:
                    case 1:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                            break;
                        }
                        break;
                }
                return false;
            }
        });
        this.mMraidWebView.setVisibilityChangedListener(new OnVisibilityChangedListener() {
            public void onVisibilityChanged(boolean isVisible) {
                if (MraidBridge.this.mMraidBridgeListener != null) {
                    MraidBridge.this.mMraidBridgeListener.onVisibilityChanged(isVisible);
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void detach() {
        this.mMraidWebView = null;
    }

    public void setContentHtml(@NonNull String htmlData) {
        if (this.mMraidWebView == null) {
            MoPubLog.d("MRAID bridge called setContentHtml before WebView was attached");
            return;
        }
        this.mHasLoaded = false;
        this.mMraidWebView.loadDataWithBaseURL("http://ads.mopub.com/", htmlData, "text/html", Defaults.ENCODING_UTF_8, null);
    }

    public void setContentUrl(String url) {
        if (this.mMraidWebView == null) {
            MoPubLog.d("MRAID bridge called setContentHtml while WebView was not attached");
            return;
        }
        this.mHasLoaded = false;
        this.mMraidWebView.loadUrl(url);
    }

    /* access modifiers changed from: 0000 */
    public void injectJavaScript(@NonNull String javascript) {
        if (this.mMraidWebView == null) {
            MoPubLog.d("Attempted to inject Javascript into MRAID WebView while was not attached:\n\t" + javascript);
            return;
        }
        MoPubLog.v("Injecting Javascript into MRAID WebView:\n\t" + javascript);
        this.mMraidWebView.loadUrl("javascript:" + javascript);
    }

    /* access modifiers changed from: private */
    public void fireErrorEvent(@NonNull MraidJavascriptCommand command, @NonNull String message) {
        injectJavaScript("window.mraidbridge.notifyErrorEvent(" + JSONObject.quote(command.toJavascriptString()) + ", " + JSONObject.quote(message) + ")");
    }

    private void fireNativeCommandCompleteEvent(@NonNull MraidJavascriptCommand command) {
        injectJavaScript("window.mraidbridge.nativeCallComplete(" + JSONObject.quote(command.toJavascriptString()) + ")");
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public boolean handleShouldOverrideUrl(@NonNull String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if ("mopub".equals(scheme)) {
                if ("failLoad".equals(host) && this.mPlacementType == PlacementType.INLINE && this.mMraidBridgeListener != null) {
                    this.mMraidBridgeListener.onPageFailedToLoad();
                }
                return true;
            } else if (AdType.MRAID.equals(scheme)) {
                HashMap hashMap = new HashMap();
                for (NameValuePair nameValuePair : URLEncodedUtils.parse(uri, Defaults.ENCODING_UTF_8)) {
                    hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
                }
                MraidJavascriptCommand fromJavascriptString = MraidJavascriptCommand.fromJavascriptString(host);
                try {
                    runCommand(fromJavascriptString, hashMap);
                } catch (MraidCommandException e) {
                    fireErrorEvent(fromJavascriptString, e.getMessage());
                }
                fireNativeCommandCompleteEvent(fromJavascriptString);
                return true;
            } else if (!this.mIsClicked) {
                return false;
            } else {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.addFlags(268435456);
                try {
                    if (this.mMraidWebView == null) {
                        MoPubLog.d("WebView was detached. Unable to load a URL");
                        return true;
                    }
                    this.mMraidWebView.getContext().startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e2) {
                    MoPubLog.d("No activity found to handle this URL " + url);
                    return false;
                }
            }
        } catch (URISyntaxException e3) {
            MoPubLog.w("Invalid MRAID URL: " + url);
            fireErrorEvent(MraidJavascriptCommand.UNSPECIFIED, "Mraid command sent an invalid URL");
            return true;
        }
    }

    /* access modifiers changed from: private */
    @VisibleForTesting
    public void handlePageFinished() {
        if (!this.mHasLoaded) {
            this.mHasLoaded = true;
            if (this.mMraidBridgeListener != null) {
                this.mMraidBridgeListener.onPageLoaded();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void runCommand(@NonNull final MraidJavascriptCommand command, @NonNull Map<String, String> params) throws MraidCommandException {
        if (command.requiresClick(this.mPlacementType) && !this.mIsClicked) {
            throw new MraidCommandException("Cannot execute this command unless the user clicks");
        } else if (this.mMraidBridgeListener == null) {
            throw new MraidCommandException("Invalid state to execute this command");
        } else if (this.mMraidWebView == null) {
            throw new MraidCommandException("The current WebView is being destroyed");
        } else {
            switch (command) {
                case CLOSE:
                    this.mMraidBridgeListener.onClose();
                    return;
                case RESIZE:
                    this.mMraidBridgeListener.onResize(checkRange(parseSize((String) params.get(VastIconXmlManager.WIDTH)), 0, 100000), checkRange(parseSize((String) params.get(VastIconXmlManager.HEIGHT)), 0, 100000), checkRange(parseSize((String) params.get("offsetX")), -100000, 100000), checkRange(parseSize((String) params.get("offsetY")), -100000, 100000), parseClosePosition((String) params.get("customClosePosition"), ClosePosition.TOP_RIGHT), parseBoolean((String) params.get("allowOffscreen"), true));
                    return;
                case EXPAND:
                    this.mMraidBridgeListener.onExpand(parseURI((String) params.get(MASTNativeAdConstants.RESPONSE_URL), null), parseBoolean((String) params.get("shouldUseCustomClose"), false));
                    return;
                case USE_CUSTOM_CLOSE:
                    this.mMraidBridgeListener.onUseCustomClose(parseBoolean((String) params.get("shouldUseCustomClose"), false));
                    return;
                case OPEN:
                    this.mMraidBridgeListener.onOpen(parseURI((String) params.get(MASTNativeAdConstants.RESPONSE_URL)));
                    return;
                case SET_ORIENTATION_PROPERTIES:
                    this.mMraidBridgeListener.onSetOrientationProperties(parseBoolean((String) params.get("allowOrientationChange")), parseOrientation((String) params.get("forceOrientation")));
                    return;
                case PLAY_VIDEO:
                    this.mMraidBridgeListener.onPlayVideo(parseURI((String) params.get("uri")));
                    return;
                case STORE_PICTURE:
                    this.mMraidNativeCommandHandler.storePicture(this.mMraidWebView.getContext(), parseURI((String) params.get("uri")).toString(), new MraidCommandFailureListener() {
                        public void onFailure(MraidCommandException exception) {
                            MraidBridge.this.fireErrorEvent(command, exception.getMessage());
                        }
                    });
                    return;
                case CREATE_CALENDAR_EVENT:
                    this.mMraidNativeCommandHandler.createCalendarEvent(this.mMraidWebView.getContext(), params);
                    return;
                case UNSPECIFIED:
                    throw new MraidCommandException("Unspecified MRAID Javascript command");
                default:
                    return;
            }
        }
    }

    private ClosePosition parseClosePosition(@NonNull String text, @NonNull ClosePosition defaultValue) throws MraidCommandException {
        if (TextUtils.isEmpty(text)) {
            return defaultValue;
        }
        if (text.equals("top-left")) {
            return ClosePosition.TOP_LEFT;
        }
        if (text.equals("top-right")) {
            return ClosePosition.TOP_RIGHT;
        }
        if (text.equals("center")) {
            return ClosePosition.CENTER;
        }
        if (text.equals("bottom-left")) {
            return ClosePosition.BOTTOM_LEFT;
        }
        if (text.equals("bottom-right")) {
            return ClosePosition.BOTTOM_RIGHT;
        }
        if (text.equals("top-center")) {
            return ClosePosition.TOP_CENTER;
        }
        if (text.equals("bottom-center")) {
            return ClosePosition.BOTTOM_CENTER;
        }
        throw new MraidCommandException("Invalid close position: " + text);
    }

    private int parseSize(@NonNull String text) throws MraidCommandException {
        try {
            return Integer.parseInt(text, 10);
        } catch (NumberFormatException e) {
            throw new MraidCommandException("Invalid numeric parameter: " + text);
        }
    }

    private MraidOrientation parseOrientation(String text) throws MraidCommandException {
        if ("portrait".equals(text)) {
            return MraidOrientation.PORTRAIT;
        }
        if ("landscape".equals(text)) {
            return MraidOrientation.LANDSCAPE;
        }
        if ("none".equals(text)) {
            return MraidOrientation.NONE;
        }
        throw new MraidCommandException("Invalid orientation: " + text);
    }

    private int checkRange(int value, int min, int max) throws MraidCommandException {
        if (value >= min && value <= max) {
            return value;
        }
        throw new MraidCommandException("Integer parameter out of range: " + value);
    }

    private boolean parseBoolean(@Nullable String text, boolean defaultValue) throws MraidCommandException {
        return text == null ? defaultValue : parseBoolean(text);
    }

    private boolean parseBoolean(String text) throws MraidCommandException {
        if ("true".equals(text)) {
            return true;
        }
        if ("false".equals(text)) {
            return false;
        }
        throw new MraidCommandException("Invalid boolean parameter: " + text);
    }

    @NonNull
    private URI parseURI(@Nullable String encodedText, URI defaultValue) throws MraidCommandException {
        return encodedText == null ? defaultValue : parseURI(encodedText);
    }

    @NonNull
    private URI parseURI(@Nullable String encodedText) throws MraidCommandException {
        if (encodedText == null) {
            throw new MraidCommandException("Parameter cannot be null");
        }
        try {
            return new URI(encodedText);
        } catch (URISyntaxException e) {
            throw new MraidCommandException("Invalid URL parameter: " + encodedText);
        }
    }

    /* access modifiers changed from: 0000 */
    public void notifyViewability(boolean isViewable) {
        injectJavaScript("mraidbridge.setIsViewable(" + isViewable + ")");
    }

    /* access modifiers changed from: 0000 */
    public void notifyPlacementType(PlacementType placementType) {
        injectJavaScript("mraidbridge.setPlacementType(" + JSONObject.quote(placementType.toJavascriptString()) + ")");
    }

    /* access modifiers changed from: 0000 */
    public void notifyViewState(ViewState state) {
        injectJavaScript("mraidbridge.setState(" + JSONObject.quote(state.toJavascriptString()) + ")");
    }

    /* access modifiers changed from: 0000 */
    public void notifySupports(boolean sms, boolean telephone, boolean calendar, boolean storePicture, boolean inlineVideo) {
        injectJavaScript("mraidbridge.setSupports(" + sms + "," + telephone + "," + calendar + "," + storePicture + "," + inlineVideo + ")");
    }

    @NonNull
    private String stringifyRect(Rect rect) {
        return rect.left + "," + rect.top + "," + rect.width() + "," + rect.height();
    }

    @NonNull
    private String stringifySize(Rect rect) {
        return rect.width() + "," + rect.height();
    }

    public void notifyScreenMetrics(@NonNull MraidScreenMetrics screenMetrics) {
        injectJavaScript("mraidbridge.setScreenSize(" + stringifySize(screenMetrics.getScreenRectDips()) + ");mraidbridge.setMaxSize(" + stringifySize(screenMetrics.getRootViewRectDips()) + ");mraidbridge.setCurrentPosition(" + stringifyRect(screenMetrics.getCurrentAdRectDips()) + ");mraidbridge.setDefaultPosition(" + stringifyRect(screenMetrics.getDefaultAdRectDips()) + ")");
        injectJavaScript("mraidbridge.notifySizeChangeEvent(" + stringifySize(screenMetrics.getCurrentAdRect()) + ")");
    }

    /* access modifiers changed from: 0000 */
    public void notifyReady() {
        injectJavaScript("mraidbridge.notifyReadyEvent();");
    }

    /* access modifiers changed from: 0000 */
    public boolean isClicked() {
        return this.mIsClicked;
    }

    /* access modifiers changed from: 0000 */
    public boolean isVisible() {
        return this.mMraidWebView != null && this.mMraidWebView.isVisible();
    }

    /* access modifiers changed from: 0000 */
    public boolean isAttached() {
        return this.mMraidWebView != null;
    }

    /* access modifiers changed from: 0000 */
    public boolean isLoaded() {
        return this.mHasLoaded;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public MraidWebView getMraidWebView() {
        return this.mMraidWebView;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setClicked(boolean clicked) {
        this.mIsClicked = clicked;
    }
}
