package com.monkey.photo.ilb;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.baidu.location.LocationClientOption;

public abstract class VersionedGestureDetector {
    static final String LOG_TAG = "VersionedGestureDetector";
    OnGestureListener mListener;

    public interface OnGestureListener {
        void onDrag(float f, float f2);

        void onFling(float f, float f2, float f3, float f4);

        void onScale(float f, float f2, float f3);
    }

    private static class CupcakeDetector extends VersionedGestureDetector {
        private boolean mIsDragging;
        float mLastTouchX;
        float mLastTouchY;
        final float mMinimumVelocity;
        final float mTouchSlop;
        private VelocityTracker mVelocityTracker;

        public CupcakeDetector(Context context) {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            this.mMinimumVelocity = (float) configuration.getScaledMinimumFlingVelocity();
            this.mTouchSlop = (float) configuration.getScaledTouchSlop();
        }

        /* access modifiers changed from: 0000 */
        public float getActiveX(MotionEvent ev) {
            return ev.getX();
        }

        /* access modifiers changed from: 0000 */
        public float getActiveY(MotionEvent ev) {
            return ev.getY();
        }

        public boolean isScaling() {
            return false;
        }

        public boolean onTouchEvent(MotionEvent ev) {
            boolean z = false;
            switch (ev.getAction()) {
                case 0:
                    this.mVelocityTracker = VelocityTracker.obtain();
                    this.mVelocityTracker.addMovement(ev);
                    this.mLastTouchX = getActiveX(ev);
                    this.mLastTouchY = getActiveY(ev);
                    this.mIsDragging = false;
                    break;
                case 1:
                    if (this.mIsDragging && this.mVelocityTracker != null) {
                        this.mLastTouchX = getActiveX(ev);
                        this.mLastTouchY = getActiveY(ev);
                        this.mVelocityTracker.addMovement(ev);
                        this.mVelocityTracker.computeCurrentVelocity(LocationClientOption.MIN_SCAN_SPAN);
                        float vX = this.mVelocityTracker.getXVelocity();
                        float vY = this.mVelocityTracker.getYVelocity();
                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= this.mMinimumVelocity) {
                            this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -vX, -vY);
                        }
                    }
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
                case 2:
                    float x = getActiveX(ev);
                    float y = getActiveY(ev);
                    float dx = x - this.mLastTouchX;
                    float dy = y - this.mLastTouchY;
                    if (!this.mIsDragging) {
                        if (FloatMath.sqrt((dx * dx) + (dy * dy)) >= this.mTouchSlop) {
                            z = true;
                        }
                        this.mIsDragging = z;
                    }
                    if (this.mIsDragging) {
                        this.mListener.onDrag(dx, dy);
                        this.mLastTouchX = x;
                        this.mLastTouchY = y;
                        if (this.mVelocityTracker != null) {
                            this.mVelocityTracker.addMovement(ev);
                            break;
                        }
                    }
                    break;
                case 3:
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    @TargetApi(5)
    private static class EclairDetector extends CupcakeDetector {
        private static final int INVALID_POINTER_ID = -1;
        private int mActivePointerId = -1;
        private int mActivePointerIndex = 0;

        public EclairDetector(Context context) {
            super(context);
        }

        /* access modifiers changed from: 0000 */
        public float getActiveX(MotionEvent ev) {
            try {
                return ev.getX(this.mActivePointerIndex);
            } catch (Exception e) {
                return ev.getX();
            }
        }

        /* access modifiers changed from: 0000 */
        public float getActiveY(MotionEvent ev) {
            try {
                return ev.getY(this.mActivePointerIndex);
            } catch (Exception e) {
                return ev.getY();
            }
        }

        public boolean onTouchEvent(MotionEvent ev) {
            int i = 0;
            switch (ev.getAction() & MotionEventCompat.ACTION_MASK) {
                case 0:
                    this.mActivePointerId = ev.getPointerId(0);
                    break;
                case 1:
                case 3:
                    this.mActivePointerId = -1;
                    break;
                case 6:
                    int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                    if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
                        int newPointerIndex;
                        if (pointerIndex == 0) {
                            newPointerIndex = 1;
                        } else {
                            newPointerIndex = 0;
                        }
                        this.mActivePointerId = ev.getPointerId(newPointerIndex);
                        this.mLastTouchX = ev.getX(newPointerIndex);
                        this.mLastTouchY = ev.getY(newPointerIndex);
                        break;
                    }
                    break;
            }
            if (this.mActivePointerId != -1) {
                i = this.mActivePointerId;
            }
            this.mActivePointerIndex = ev.findPointerIndex(i);
            return super.onTouchEvent(ev);
        }
    }

    @TargetApi(8)
    private static class FroyoDetector extends EclairDetector {
        private final ScaleGestureDetector mDetector;
        private final OnScaleGestureListener mScaleListener = new OnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                FroyoDetector.this.mListener.onScale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
                return true;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        };

        public FroyoDetector(Context context) {
            super(context);
            this.mDetector = new ScaleGestureDetector(context, this.mScaleListener);
        }

        public boolean isScaling() {
            return this.mDetector.isInProgress();
        }

        public boolean onTouchEvent(MotionEvent ev) {
            this.mDetector.onTouchEvent(ev);
            return super.onTouchEvent(ev);
        }
    }

    public abstract boolean isScaling();

    public abstract boolean onTouchEvent(MotionEvent motionEvent);

    public static VersionedGestureDetector newInstance(Context context, OnGestureListener listener) {
        VersionedGestureDetector detector;
        int sdkVersion = VERSION.SDK_INT;
        if (sdkVersion < 5) {
            detector = new CupcakeDetector(context);
        } else if (sdkVersion < 8) {
            detector = new EclairDetector(context);
        } else {
            detector = new FroyoDetector(context);
        }
        detector.mListener = listener;
        return detector;
    }
}
