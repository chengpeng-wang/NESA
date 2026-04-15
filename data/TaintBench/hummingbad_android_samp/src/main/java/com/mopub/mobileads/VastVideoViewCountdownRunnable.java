package com.mopub.mobileads;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;

public class VastVideoViewCountdownRunnable extends RepeatingHandlerRunnable {
    @NonNull
    private final VastVideoViewController mVideoViewController;

    public VastVideoViewCountdownRunnable(@NonNull VastVideoViewController videoViewController, @NonNull Handler handler) {
        super(handler);
        Preconditions.checkNotNull(handler);
        Preconditions.checkNotNull(videoViewController);
        this.mVideoViewController = videoViewController;
    }

    public void doWork() {
        this.mVideoViewController.updateCountdown();
        if (this.mVideoViewController.shouldBeInteractable()) {
            this.mVideoViewController.makeVideoInteractable();
        }
    }
}
