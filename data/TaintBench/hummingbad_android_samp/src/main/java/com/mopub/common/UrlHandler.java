package com.mopub.common;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.logging.MoPubLog;
import com.mopub.exceptions.IntentNotResolvableException;
import com.mopub.network.TrackingRequest;
import java.util.EnumSet;
import java.util.Iterator;

public class UrlHandler {
    /* access modifiers changed from: private|static|final */
    public static final ResultActions EMPTY_CLICK_LISTENER = new ResultActions() {
        public final void urlHandlingSucceeded(@NonNull String url, @NonNull UrlAction urlAction) {
        }

        public final void urlHandlingFailed(@NonNull String url, @NonNull UrlAction lastFailedUrlAction) {
        }
    };
    /* access modifiers changed from: private|static|final */
    public static final MoPubSchemeListener EMPTY_MOPUB_SCHEME_LISTENER = new MoPubSchemeListener() {
        public final void onFinishLoad() {
        }

        public final void onClose() {
        }

        public final void onFailLoad() {
        }
    };
    private boolean mAlreadySucceeded;
    @NonNull
    private MoPubSchemeListener mMoPubSchemeListener;
    @NonNull
    private ResultActions mResultActions;
    private boolean mSkipShowMoPubBrowser;
    @NonNull
    private EnumSet<UrlAction> mSupportedUrlActions;
    /* access modifiers changed from: private */
    public boolean mTaskPending;

    public static class Builder {
        @NonNull
        private MoPubSchemeListener moPubSchemeListener = UrlHandler.EMPTY_MOPUB_SCHEME_LISTENER;
        @NonNull
        private ResultActions resultActions = UrlHandler.EMPTY_CLICK_LISTENER;
        private boolean skipShowMoPubBrowser = false;
        @NonNull
        private EnumSet<UrlAction> supportedUrlActions = EnumSet.of(UrlAction.NOOP);

        public Builder withSupportedUrlActions(@NonNull UrlAction first, @Nullable UrlAction... others) {
            this.supportedUrlActions = EnumSet.of(first, others);
            return this;
        }

        public Builder withSupportedUrlActions(@NonNull EnumSet<UrlAction> supportedUrlActions) {
            this.supportedUrlActions = EnumSet.copyOf(supportedUrlActions);
            return this;
        }

        public Builder withResultActions(@NonNull ResultActions resultActions) {
            this.resultActions = resultActions;
            return this;
        }

        public Builder withMoPubSchemeListener(@NonNull MoPubSchemeListener moPubSchemeListener) {
            this.moPubSchemeListener = moPubSchemeListener;
            return this;
        }

        public Builder withoutMoPubBrowser() {
            this.skipShowMoPubBrowser = true;
            return this;
        }

        public UrlHandler build() {
            return new UrlHandler(this.supportedUrlActions, this.resultActions, this.moPubSchemeListener, this.skipShowMoPubBrowser, null);
        }
    }

    public interface MoPubSchemeListener {
        void onClose();

        void onFailLoad();

        void onFinishLoad();
    }

    public interface ResultActions {
        void urlHandlingFailed(@NonNull String str, @NonNull UrlAction urlAction);

        void urlHandlingSucceeded(@NonNull String str, @NonNull UrlAction urlAction);
    }

    /* synthetic */ UrlHandler(EnumSet x0, ResultActions x1, MoPubSchemeListener x2, boolean x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    private UrlHandler(@NonNull EnumSet<UrlAction> supportedUrlActions, @NonNull ResultActions resultActions, @NonNull MoPubSchemeListener moPubSchemeListener, boolean skipShowMoPubBrowser) {
        this.mSupportedUrlActions = EnumSet.copyOf(supportedUrlActions);
        this.mResultActions = resultActions;
        this.mMoPubSchemeListener = moPubSchemeListener;
        this.mSkipShowMoPubBrowser = skipShowMoPubBrowser;
        this.mAlreadySucceeded = false;
        this.mTaskPending = false;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public EnumSet<UrlAction> getSupportedUrlActions() {
        return EnumSet.copyOf(this.mSupportedUrlActions);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public ResultActions getResultActions() {
        return this.mResultActions;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public MoPubSchemeListener getMoPubSchemeListener() {
        return this.mMoPubSchemeListener;
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldSkipShowMoPubBrowser() {
        return this.mSkipShowMoPubBrowser;
    }

    public void handleUrl(@NonNull Context context, @NonNull String destinationUrl) {
        Preconditions.checkNotNull(context);
        handleUrl(context, destinationUrl, true);
    }

    public void handleUrl(@NonNull Context context, @NonNull String destinationUrl, boolean fromUserInteraction) {
        Preconditions.checkNotNull(context);
        handleUrl(context, destinationUrl, fromUserInteraction, null);
    }

    public void handleUrl(@NonNull Context context, @NonNull String destinationUrl, boolean fromUserInteraction, @Nullable Iterable<String> trackingUrls) {
        Preconditions.checkNotNull(context);
        if (TextUtils.isEmpty(destinationUrl)) {
            failUrlHandling(destinationUrl, null, "Attempted to handle empty url.", null);
            return;
        }
        final Context context2 = context;
        final boolean z = fromUserInteraction;
        final Iterable<String> iterable = trackingUrls;
        final String str = destinationUrl;
        UrlResolutionTask.getResolvedUrl(destinationUrl, new UrlResolutionListener() {
            public void onSuccess(@NonNull String resolvedUrl) {
                UrlHandler.this.mTaskPending = false;
                UrlHandler.this.handleResolvedUrl(context2, resolvedUrl, z, iterable);
            }

            public void onFailure(@NonNull String message, @Nullable Throwable throwable) {
                UrlHandler.this.mTaskPending = false;
                UrlHandler.this.failUrlHandling(str, null, message, throwable);
            }
        });
        this.mTaskPending = true;
    }

    public boolean handleResolvedUrl(@NonNull Context context, @NonNull String url, boolean fromUserInteraction, @Nullable Iterable<String> trackingUrls) {
        if (TextUtils.isEmpty(url)) {
            failUrlHandling(url, null, "Attempted to handle empty url.", null);
            return false;
        }
        UrlAction urlAction = UrlAction.NOOP;
        Uri parse = Uri.parse(url);
        Iterator it = this.mSupportedUrlActions.iterator();
        while (true) {
            UrlAction urlAction2 = urlAction;
            if (it.hasNext()) {
                urlAction = (UrlAction) it.next();
                if (urlAction.shouldTryHandlingUrl(parse)) {
                    try {
                        urlAction.handleUrl(this, context, parse, fromUserInteraction);
                        if (!(this.mAlreadySucceeded || this.mTaskPending || UrlAction.IGNORE_ABOUT_SCHEME.equals(urlAction) || UrlAction.HANDLE_MOPUB_SCHEME.equals(urlAction))) {
                            TrackingRequest.makeTrackingHttpRequest((Iterable) trackingUrls, context, Name.CLICK_REQUEST);
                            this.mResultActions.urlHandlingSucceeded(parse.toString(), urlAction);
                            this.mAlreadySucceeded = true;
                        }
                        return true;
                    } catch (IntentNotResolvableException e) {
                        MoPubLog.d(e.getMessage(), e);
                    }
                } else {
                    urlAction = urlAction2;
                }
            } else {
                failUrlHandling(url, urlAction2, "Link ignored. Unable to handle url: " + url, null);
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void failUrlHandling(@Nullable String url, @Nullable UrlAction urlAction, @NonNull String message, @Nullable Throwable throwable) {
        Preconditions.checkNotNull(message);
        if (urlAction == null) {
            urlAction = UrlAction.NOOP;
        }
        MoPubLog.d(message, throwable);
        this.mResultActions.urlHandlingFailed(url, urlAction);
    }
}
