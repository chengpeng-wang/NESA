package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Drawables;
import java.util.Map;

public class NativeRendererHelper {
    public static void addTextView(@Nullable TextView textView, @Nullable String contents) {
        if (textView == null) {
            MoPubLog.d("Attempted to add text (" + contents + ") to null TextView.");
            return;
        }
        textView.setText(null);
        if (contents == null) {
            MoPubLog.d("Attempted to set TextView contents to null.");
        } else {
            textView.setText(contents);
        }
    }

    public static void addPrivacyInformationIcon(@Nullable ImageView privacyInformationIconImageView, @Nullable final String privacyInformationClickthroughUrl) {
        if (privacyInformationIconImageView != null) {
            if (privacyInformationClickthroughUrl == null) {
                privacyInformationIconImageView.setImageDrawable(null);
                privacyInformationIconImageView.setOnClickListener(null);
                privacyInformationIconImageView.setVisibility(4);
                return;
            }
            final Context context = privacyInformationIconImageView.getContext();
            if (context != null) {
                privacyInformationIconImageView.setImageDrawable(Drawables.NATIVE_PRIVACY_INFORMATION_ICON.createDrawable(context));
                privacyInformationIconImageView.setOnClickListener(new OnClickListener() {
                    public final void onClick(View v) {
                        new Builder().withSupportedUrlActions(UrlAction.IGNORE_ABOUT_SCHEME, UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK).build().handleUrl(context, privacyInformationClickthroughUrl);
                    }
                });
                privacyInformationIconImageView.setVisibility(0);
            }
        }
    }

    public static void addCtaButton(@Nullable TextView ctaTextView, @Nullable final View rootView, @Nullable String contents) {
        addTextView(ctaTextView, contents);
        if (ctaTextView != null && rootView != null) {
            ctaTextView.setOnClickListener(new OnClickListener() {
                public final void onClick(View v) {
                    rootView.performClick();
                }
            });
        }
    }

    public static void updateExtras(@Nullable View mainView, @NonNull Map<String, Integer> extrasIds, @NonNull Map<String, Object> extras) {
        if (mainView == null) {
            MoPubLog.w("Attempted to bind extras on a null main view.");
            return;
        }
        for (String str : extrasIds.keySet()) {
            View findViewById = mainView.findViewById(((Integer) extrasIds.get(str)).intValue());
            Object obj = extras.get(str);
            if (findViewById instanceof ImageView) {
                ((ImageView) findViewById).setImageDrawable(null);
                Object obj2 = extras.get(str);
                if (obj2 != null && (obj2 instanceof String)) {
                    NativeImageHelper.loadImageView((String) obj2, (ImageView) findViewById);
                }
            } else if (findViewById instanceof TextView) {
                ((TextView) findViewById).setText(null);
                if (obj instanceof String) {
                    addTextView((TextView) findViewById, (String) obj);
                }
            } else {
                MoPubLog.d("View bound to " + str + " should be an instance of TextView or ImageView.");
            }
        }
    }
}
