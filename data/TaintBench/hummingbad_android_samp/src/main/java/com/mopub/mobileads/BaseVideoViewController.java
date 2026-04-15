package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.mopub.common.logging.MoPubLog;

public abstract class BaseVideoViewController {
    private final BaseVideoViewControllerListener mBaseVideoViewControllerListener;
    @Nullable
    private Long mBroadcastIdentifier;
    private final Context mContext;
    private final RelativeLayout mLayout = new RelativeLayout(this.mContext);

    public interface BaseVideoViewControllerListener {
        void onFinish();

        void onSetContentView(View view);

        void onSetRequestedOrientation(int i);

        void onStartActivityForResult(Class<? extends Activity> cls, int i, Bundle bundle);
    }

    public abstract VideoView getVideoView();

    public abstract void onBackPressed();

    public abstract void onConfigurationChanged(Configuration configuration);

    public abstract void onDestroy();

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onSaveInstanceState(@NonNull Bundle bundle);

    protected BaseVideoViewController(Context context, @Nullable Long broadcastIdentifier, BaseVideoViewControllerListener baseVideoViewControllerListener) {
        this.mContext = context;
        this.mBroadcastIdentifier = broadcastIdentifier;
        this.mBaseVideoViewControllerListener = baseVideoViewControllerListener;
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(13);
        this.mLayout.addView(getVideoView(), 0, layoutParams);
        this.mBaseVideoViewControllerListener.onSetContentView(this.mLayout);
    }

    public boolean backButtonEnabled() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /* access modifiers changed from: protected */
    public BaseVideoViewControllerListener getBaseVideoViewControllerListener() {
        return this.mBaseVideoViewControllerListener;
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }

    public ViewGroup getLayout() {
        return this.mLayout;
    }

    /* access modifiers changed from: protected */
    public void videoError(boolean shouldFinish) {
        MoPubLog.e("Video cannot be played.");
        broadcastAction(EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_FAIL);
        if (shouldFinish) {
            this.mBaseVideoViewControllerListener.onFinish();
        }
    }

    /* access modifiers changed from: protected */
    public void videoCompleted(boolean shouldFinish) {
        if (shouldFinish) {
            this.mBaseVideoViewControllerListener.onFinish();
        }
    }

    /* access modifiers changed from: 0000 */
    public void broadcastAction(String action) {
        if (this.mBroadcastIdentifier != null) {
            EventForwardingBroadcastReceiver.broadcastAction(this.mContext, this.mBroadcastIdentifier.longValue(), action);
        } else {
            MoPubLog.w("Tried to broadcast a video event without a braodcast identifier to send to.");
        }
    }
}
