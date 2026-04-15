package com.getbase.floatingactionbutton;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import java.util.ArrayList;

public class TouchDelegateGroup extends TouchDelegate {
    private static final Rect USELESS_HACKY_RECT = new Rect();
    private TouchDelegate mCurrentTouchDelegate;
    private boolean mEnabled;
    private final ArrayList<TouchDelegate> mTouchDelegates = new ArrayList();

    public TouchDelegateGroup(View uselessHackyView) {
        super(USELESS_HACKY_RECT, uselessHackyView);
    }

    public void addTouchDelegate(@NonNull TouchDelegate touchDelegate) {
        this.mTouchDelegates.add(touchDelegate);
    }

    public void removeTouchDelegate(TouchDelegate touchDelegate) {
        this.mTouchDelegates.remove(touchDelegate);
        if (this.mCurrentTouchDelegate == touchDelegate) {
            this.mCurrentTouchDelegate = null;
        }
    }

    public void clearTouchDelegates() {
        this.mTouchDelegates.clear();
        this.mCurrentTouchDelegate = null;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!this.mEnabled) {
            return false;
        }
        TouchDelegate delegate = null;
        switch (event.getAction()) {
            case 0:
                for (int i = 0; i < this.mTouchDelegates.size(); i++) {
                    TouchDelegate touchDelegate = (TouchDelegate) this.mTouchDelegates.get(i);
                    if (touchDelegate.onTouchEvent(event)) {
                        this.mCurrentTouchDelegate = touchDelegate;
                        return true;
                    }
                }
                break;
            case 1:
            case 3:
                delegate = this.mCurrentTouchDelegate;
                this.mCurrentTouchDelegate = null;
                break;
            case 2:
                delegate = this.mCurrentTouchDelegate;
                break;
        }
        if (delegate == null || !delegate.onTouchEvent(event)) {
            return false;
        }
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }
}
