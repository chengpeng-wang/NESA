package com.mopub.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.mopub.common.UrlHandler.MoPubSchemeListener;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Intents;
import com.mopub.exceptions.IntentNotResolvableException;
import com.mopub.exceptions.UrlParseException;
import com.mopub.network.TrackingRequest;
import java.util.List;

public enum UrlAction {
    HANDLE_MOPUB_SCHEME(false) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return "mopub".equals(uri.getScheme());
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            String host = uri.getHost();
            MoPubSchemeListener moPubSchemeListener = urlHandler.getMoPubSchemeListener();
            if ("finishLoad".equals(host)) {
                moPubSchemeListener.onFinishLoad();
            } else if ("close".equals(host)) {
                moPubSchemeListener.onClose();
            } else if ("failLoad".equals(host)) {
                moPubSchemeListener.onFailLoad();
            } else {
                throw new IntentNotResolvableException("Could not handle MoPub Scheme url: " + uri);
            }
        }
    },
    IGNORE_ABOUT_SCHEME(false) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return "about".equals(uri.getScheme());
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            MoPubLog.d("Link to about page ignored.");
        }
    },
    HANDLE_PHONE_SCHEME(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            String scheme = uri.getScheme();
            return "tel".equals(scheme) || "voicemail".equals(scheme) || "sms".equals(scheme) || "mailto".equals(scheme) || "geo".equals(scheme) || "google.streetview".equals(scheme);
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            Intents.launchActionViewIntent(context, uri, "Could not handle intent with URI: " + uri + "\n\tIs this intent supported on your phone?");
        }
    },
    OPEN_NATIVE_BROWSER(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return "mopubnativebrowser".equals(uri.getScheme());
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            String str = "Unable to load mopub native browser url: " + uri;
            try {
                Intents.launchIntentForUserClick(context, Intents.intentForNativeBrowserScheme(uri), str);
            } catch (UrlParseException e) {
                throw new IntentNotResolvableException(str + "\n\t" + e.getMessage());
            }
        }
    },
    OPEN_APP_MARKET(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            String scheme = uri.getScheme();
            String host = uri.getHost();
            return "play.google.com".equals(host) || "market.android.com".equals(host) || "market".equals(scheme) || uri.toString().startsWith("play.google.com/") || uri.toString().startsWith("market.android.com/");
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            Intents.launchApplicationUrl(context, uri);
        }
    },
    OPEN_IN_APP_BROWSER(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            String scheme = uri.getScheme();
            return Constants.HTTP.equals(scheme) || Constants.HTTPS.equals(scheme);
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            if (!urlHandler.shouldSkipShowMoPubBrowser()) {
                Intents.showMoPubBrowserForUrl(context, uri);
            }
        }
    },
    HANDLE_SHARE_TWEET(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            Preconditions.checkNotNull(uri);
            return "mopubshare".equals(uri.getScheme()) && "tweet".equals(uri.getHost());
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(uri);
            String str = "Could not handle share tweet intent with URI " + uri;
            try {
                Intents.launchIntentForUserClick(context, Intent.createChooser(Intents.intentForShareTweet(uri), "Share via"), str);
            } catch (UrlParseException e) {
                throw new IntentNotResolvableException(str + "\n\t" + e.getMessage());
            }
        }
    },
    FOLLOW_DEEP_LINK_WITH_FALLBACK(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return "deeplink+".equalsIgnoreCase(uri.getScheme());
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            if ("navigate".equalsIgnoreCase(uri.getHost())) {
                try {
                    String queryParameter = uri.getQueryParameter("primaryUrl");
                    Iterable queryParameters = uri.getQueryParameters("primaryTrackingUrl");
                    String queryParameter2 = uri.getQueryParameter("fallbackUrl");
                    List queryParameters2 = uri.getQueryParameters("fallbackTrackingUrl");
                    if (queryParameter == null) {
                        throw new IntentNotResolvableException("Deeplink+ did not have 'primaryUrl' query param.");
                    }
                    Uri parse = Uri.parse(queryParameter);
                    if (shouldTryHandlingUrl(parse)) {
                        throw new IntentNotResolvableException("Deeplink+ had another Deeplink+ as the 'primaryUrl'.");
                    }
                    try {
                        Intents.launchApplicationUrl(context, parse);
                        TrackingRequest.makeTrackingHttpRequest(queryParameters, context, Name.CLICK_REQUEST);
                        return;
                    } catch (IntentNotResolvableException e) {
                        if (queryParameter2 == null) {
                            throw new IntentNotResolvableException("Unable to handle 'primaryUrl' for Deeplink+ and 'fallbackUrl' was missing.");
                        } else if (shouldTryHandlingUrl(Uri.parse(queryParameter2))) {
                            throw new IntentNotResolvableException("Deeplink+ URL had another Deeplink+ URL as the 'fallbackUrl'.");
                        } else {
                            urlHandler.handleUrl(context, queryParameter2, true, queryParameters2);
                            return;
                        }
                    }
                } catch (UnsupportedOperationException e2) {
                    throw new IntentNotResolvableException("Deeplink+ URL was not a hierarchical URI.");
                }
            }
            throw new IntentNotResolvableException("Deeplink+ URL did not have 'navigate' as the host.");
        }
    },
    FOLLOW_DEEP_LINK(true) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return (TextUtils.isEmpty(uri.getScheme()) || TextUtils.isEmpty(uri.getHost())) ? false : true;
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
            Intents.launchApplicationUrl(context, uri);
        }
    },
    NOOP(false) {
        public final boolean shouldTryHandlingUrl(@NonNull Uri uri) {
            return false;
        }

        /* access modifiers changed from: protected|final */
        public final void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException {
        }
    };
    
    private final boolean mRequiresUserInteraction;

    public abstract void performAction(@NonNull Context context, @NonNull Uri uri, @NonNull UrlHandler urlHandler) throws IntentNotResolvableException;

    public abstract boolean shouldTryHandlingUrl(@NonNull Uri uri);

    public void handleUrl(UrlHandler urlHandler, @NonNull Context context, @NonNull Uri destinationUri, boolean fromUserInteraction) throws IntentNotResolvableException {
        MoPubLog.d("Ad event URL: " + destinationUri);
        if (!this.mRequiresUserInteraction || fromUserInteraction) {
            performAction(context, destinationUri, urlHandler);
            return;
        }
        throw new IntentNotResolvableException("Attempted to handle action without user interaction.");
    }

    private UrlAction(boolean requiresUserInteraction) {
        this.mRequiresUserInteraction = requiresUserInteraction;
    }
}
