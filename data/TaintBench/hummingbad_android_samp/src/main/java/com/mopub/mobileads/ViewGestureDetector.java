package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.mopub.common.AdReport;
import com.mopub.common.logging.MoPubLog;

public class ViewGestureDetector extends GestureDetector {
    private AdAlertGestureListener mAdAlertGestureListener;
    private UserClickListener mUserClickListener;
    private final View mView;

    public interface UserClickListener {
        void onResetUserClick();

        void onUserClick();

        boolean wasClicked();
    }

    public ViewGestureDetector(@NonNull Context context, @NonNull View view, @Nullable AdReport adReport) {
        this(context, view, new AdAlertGestureListener(view, adReport));
    }

    private ViewGestureDetector(Context context, View view, AdAlertGestureListener adAlertGestureListener) {
        super(context, adAlertGestureListener);
        this.mAdAlertGestureListener = adAlertGestureListener;
        this.mView = view;
        setIsLongpressEnabled(false);
    }

    public void sendTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                onTouchEvent(motionEvent);
                return;
            case 1:
                if (this.mUserClickListener != null) {
                    this.mUserClickListener.onUserClick();
                } else {
                    MoPubLog.d("View's onUserClick() is not registered.");
                }
                this.mAdAlertGestureListener.finishGestureDetection();
                return;
            case 2:
                if (isMotionEventInView(motionEvent, this.mView)) {
                    onTouchEvent(motionEvent);
                    return;
                } else {
                    resetAdFlaggingGesture();
                    return;
                }
            default:
                return;
        }
    }

    public void setUserClickListener(UserClickListener listener) {
        this.mUserClickListener = listener;
    }

    /* access modifiers changed from: 0000 */
    public void resetAdFlaggingGesture() {
        this.mAdAlertGestureListener.reset();
    }

    private boolean isMotionEventInView(MotionEvent motionEvent, View view) {
        if (motionEvent == null || view == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x < 0.0f || x > ((float) view.getWidth()) || y < 0.0f || y > ((float) view.getHeight())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public void setAdAlertGestureListener(AdAlertGestureListener adAlertGestureListener) {
        this.mAdAlertGestureListener = adAlertGestureListener;
    }
}
