package com.mopub.mobileads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.AdType;
import com.mopub.common.DataKeys;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Intents;
import com.mopub.mobileads.BaseVideoViewController.BaseVideoViewControllerListener;
import com.mopub.mraid.MraidVideoViewController;
import com.mopub.nativeads.NativeVideoViewController;

public class MraidVideoPlayerActivity extends BaseVideoPlayerActivity implements BaseVideoViewControllerListener {
    @Nullable
    private BaseVideoViewController mBaseVideoController;
    private long mBroadcastIdentifier;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        this.mBroadcastIdentifier = getBroadcastIdentifierFromIntent(getIntent());
        try {
            this.mBaseVideoController = createVideoViewController(savedInstanceState);
            this.mBaseVideoController.onCreate();
        } catch (IllegalStateException e) {
            EventForwardingBroadcastReceiver.broadcastAction(this, this.mBroadcastIdentifier, EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_FAIL);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onResume();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onDestroy();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onSaveInstanceState(outState);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onConfigurationChanged(newConfig);
        }
    }

    public void onBackPressed() {
        if (this.mBaseVideoController != null && this.mBaseVideoController.backButtonEnabled()) {
            super.onBackPressed();
            this.mBaseVideoController.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mBaseVideoController != null) {
            this.mBaseVideoController.onActivityResult(requestCode, resultCode, data);
        }
    }

    private BaseVideoViewController createVideoViewController(Bundle savedInstanceState) throws IllegalStateException {
        String stringExtra = getIntent().getStringExtra(BaseVideoPlayerActivity.VIDEO_CLASS_EXTRAS_KEY);
        if ("vast".equals(stringExtra)) {
            return new VastVideoViewController(this, getIntent().getExtras(), savedInstanceState, this.mBroadcastIdentifier, this);
        } else if (AdType.MRAID.equals(stringExtra)) {
            return new MraidVideoViewController(this, getIntent().getExtras(), savedInstanceState, this);
        } else {
            if (MASTNativeAdConstants.RESPONSE_NATIVE_STRING.equals(stringExtra)) {
                return new NativeVideoViewController(this, getIntent().getExtras(), savedInstanceState, this);
            }
            throw new IllegalStateException("Unsupported video type: " + stringExtra);
        }
    }

    public void onSetContentView(View view) {
        setContentView(view);
    }

    public void onSetRequestedOrientation(int requestedOrientation) {
        setRequestedOrientation(requestedOrientation);
    }

    public void onFinish() {
        finish();
    }

    public void onStartActivityForResult(Class<? extends Activity> clazz, int requestCode, Bundle extras) {
        if (clazz != null) {
            try {
                startActivityForResult(Intents.getStartActivityIntent(this, clazz, extras), requestCode);
            } catch (ActivityNotFoundException e) {
                MoPubLog.d("Activity " + clazz.getName() + " not found. Did you declare it in your AndroidManifest.xml?");
            }
        }
    }

    protected static long getBroadcastIdentifierFromIntent(Intent intent) {
        return intent.getLongExtra(DataKeys.BROADCAST_IDENTIFIER_KEY, -1);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public BaseVideoViewController getBaseVideoViewController() {
        return this.mBaseVideoController;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public void setBaseVideoViewController(BaseVideoViewController baseVideoViewController) {
        this.mBaseVideoController = baseVideoViewController;
    }
}
