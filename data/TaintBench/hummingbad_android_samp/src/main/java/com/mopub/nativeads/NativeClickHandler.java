package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.mopub.common.Preconditions;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.UrlHandler.ResultActions;
import com.mopub.common.VisibleForTesting;

public class NativeClickHandler {
    /* access modifiers changed from: private */
    public boolean mClickInProgress;
    @NonNull
    private final Context mContext;

    public NativeClickHandler(@NonNull Context context) {
        Preconditions.checkNotNull(context);
        this.mContext = context.getApplicationContext();
    }

    public void setOnClickListener(@NonNull View view, @NonNull final ClickInterface clickInterface) {
        if (NoThrow.checkNotNull(view, "Cannot set click listener on a null view") && NoThrow.checkNotNull(clickInterface, "Cannot set click listener with a null ClickInterface")) {
            setOnClickListener(view, new OnClickListener() {
                public void onClick(View v) {
                    clickInterface.handleClick(v);
                }
            });
        }
    }

    private void setOnClickListener(@NonNull View view, @Nullable OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setOnClickListener(viewGroup.getChildAt(i), onClickListener);
            }
        }
    }

    public void clearOnClickListener(@NonNull View view) {
        if (NoThrow.checkNotNull(view, "Cannot clear click listener from a null view")) {
            setOnClickListener(view, null);
        }
    }

    public void openClickDestinationUrl(@NonNull String clickDestinationUrl, @Nullable View view) {
        openClickDestinationUrl(clickDestinationUrl, view, new SpinningProgressView(this.mContext));
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void openClickDestinationUrl(@NonNull String clickDestinationUrl, @Nullable final View view, @NonNull final SpinningProgressView spinningProgressView) {
        if (NoThrow.checkNotNull(clickDestinationUrl, "Cannot open a null click destination url")) {
            Preconditions.checkNotNull(spinningProgressView);
            if (!this.mClickInProgress) {
                this.mClickInProgress = true;
                if (view != null) {
                    spinningProgressView.addToRoot(view);
                }
                new Builder().withSupportedUrlActions(UrlAction.IGNORE_ABOUT_SCHEME, UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_APP_MARKET, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK).withResultActions(new ResultActions() {
                    public void urlHandlingSucceeded(@NonNull String url, @NonNull UrlAction urlAction) {
                        removeSpinningProgressView();
                        NativeClickHandler.this.mClickInProgress = false;
                    }

                    public void urlHandlingFailed(@NonNull String url, @NonNull UrlAction lastFailedUrlAction) {
                        removeSpinningProgressView();
                        NativeClickHandler.this.mClickInProgress = false;
                    }

                    private void removeSpinningProgressView() {
                        if (view != null) {
                            spinningProgressView.removeFromRoot();
                        }
                    }
                }).build().handleUrl(this.mContext, clickDestinationUrl);
            }
        }
    }
}
