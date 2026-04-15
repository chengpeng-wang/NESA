package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    interface GestureDetectorCompatImpl {
        boolean isLongpressEnabled();

        boolean onTouchEvent(MotionEvent motionEvent);

        void setIsLongpressEnabled(boolean z);

        void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener);
    }

    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        /* access modifiers changed from: private */
        public MotionEvent mCurrentDownEvent;
        private boolean mDeferConfirmSingleTap;
        /* access modifiers changed from: private */
        public OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        /* access modifiers changed from: private|final */
        public final OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        /* access modifiers changed from: private */
        public boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        private class GestureHandler extends Handler {
            final /* synthetic */ GestureDetectorCompatImplBase this$0;

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase) {
                this.this$0 = gestureDetectorCompatImplBase;
            }

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase, Handler handler) {
                Handler handler2 = handler;
                this.this$0 = gestureDetectorCompatImplBase;
                super(handler2.getLooper());
            }

            public void handleMessage(Message message) {
                Message message2 = message;
                switch (message2.what) {
                    case 1:
                        this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
                        return;
                    case 2:
                        this.this$0.dispatchLongPress();
                        return;
                    case 3:
                        if (this.this$0.mDoubleTapListener == null) {
                            return;
                        }
                        boolean access$502;
                        if (this.this$0.mStillDown) {
                            access$502 = GestureDetectorCompatImplBase.access$502(this.this$0, true);
                            return;
                        } else {
                            access$502 = this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                            return;
                        }
                    default:
                        RuntimeException runtimeException = r6;
                        StringBuilder stringBuilder = r6;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        RuntimeException runtimeException2 = new RuntimeException(stringBuilder.append("Unknown message ").append(message2).toString());
                        throw runtimeException;
                }
            }
        }

        static /* synthetic */ boolean access$502(GestureDetectorCompatImplBase gestureDetectorCompatImplBase, boolean z) {
            boolean z2 = z;
            boolean z3 = z2;
            boolean z4 = z2;
            gestureDetectorCompatImplBase.mDeferConfirmSingleTap = z4;
            return z3;
        }

        public GestureDetectorCompatImplBase(Context context, OnGestureListener onGestureListener, Handler handler) {
            Context context2 = context;
            OnGestureListener onGestureListener2 = onGestureListener;
            Handler handler2 = handler;
            Handler handler3;
            Handler gestureHandler;
            if (handler2 != null) {
                handler3 = r9;
                gestureHandler = new GestureHandler(this, handler2);
                this.mHandler = handler3;
            } else {
                handler3 = r9;
                gestureHandler = new GestureHandler(this);
                this.mHandler = handler3;
            }
            this.mListener = onGestureListener2;
            if (onGestureListener2 instanceof OnDoubleTapListener) {
                setOnDoubleTapListener((OnDoubleTapListener) onGestureListener2);
            }
            init(context2);
        }

        private void init(Context context) {
            Context context2 = context;
            IllegalArgumentException illegalArgumentException;
            IllegalArgumentException illegalArgumentException2;
            if (context2 == null) {
                illegalArgumentException = r8;
                illegalArgumentException2 = new IllegalArgumentException("Context must not be null");
                throw illegalArgumentException;
            } else if (this.mListener == null) {
                illegalArgumentException = r8;
                illegalArgumentException2 = new IllegalArgumentException("OnGestureListener must not be null");
                throw illegalArgumentException;
            } else {
                this.mIsLongpressEnabled = true;
                ViewConfiguration viewConfiguration = ViewConfiguration.get(context2);
                int scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
                int scaledDoubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
                this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
                this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
                this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
                this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
            }
        }

        public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        public void setIsLongpressEnabled(boolean z) {
            this.mIsLongpressEnabled = z;
        }

        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            int i;
            MotionEvent motionEvent2 = motionEvent;
            int action = motionEvent2.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent2);
            Object obj = (action & MotionEventCompat.ACTION_MASK) == 6 ? 1 : null;
            int actionIndex = obj != null ? MotionEventCompat.getActionIndex(motionEvent2) : -1;
            float f = 0.0f;
            float f2 = 0.0f;
            int pointerCount = MotionEventCompat.getPointerCount(motionEvent2);
            for (i = 0; i < pointerCount; i++) {
                if (actionIndex != i) {
                    f += MotionEventCompat.getX(motionEvent2, i);
                    f2 += MotionEventCompat.getY(motionEvent2, i);
                }
            }
            i = obj != null ? pointerCount - 1 : pointerCount;
            float f3 = f / ((float) i);
            float f4 = f2 / ((float) i);
            boolean z = false;
            boolean sendEmptyMessageDelayed;
            float f5;
            float f6;
            int pointerId;
            switch (action & MotionEventCompat.ACTION_MASK) {
                case 0:
                    int onDoubleTap;
                    if (this.mDoubleTapListener != null) {
                        boolean hasMessages = this.mHandler.hasMessages(3);
                        if (hasMessages) {
                            this.mHandler.removeMessages(3);
                        }
                        if (this.mCurrentDownEvent == null || this.mPreviousUpEvent == null || !hasMessages || !isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, motionEvent2)) {
                            sendEmptyMessageDelayed = this.mHandler.sendEmptyMessageDelayed(3, (long) DOUBLE_TAP_TIMEOUT);
                        } else {
                            this.mIsDoubleTapping = true;
                            onDoubleTap = (z | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent)) | this.mDoubleTapListener.onDoubleTapEvent(motionEvent2);
                        }
                    }
                    f5 = f3;
                    f6 = f5;
                    this.mLastFocusX = f5;
                    this.mDownFocusX = f6;
                    f5 = f4;
                    f6 = f5;
                    this.mLastFocusY = f5;
                    this.mDownFocusY = f6;
                    if (this.mCurrentDownEvent != null) {
                        this.mCurrentDownEvent.recycle();
                    }
                    this.mCurrentDownEvent = MotionEvent.obtain(motionEvent2);
                    this.mAlwaysInTapRegion = true;
                    this.mAlwaysInBiggerTapRegion = true;
                    this.mStillDown = true;
                    this.mInLongPress = false;
                    this.mDeferConfirmSingleTap = false;
                    if (this.mIsLongpressEnabled) {
                        this.mHandler.removeMessages(2);
                        sendEmptyMessageDelayed = this.mHandler.sendEmptyMessageAtTime(2, (this.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT)) + ((long) LONGPRESS_TIMEOUT));
                    }
                    sendEmptyMessageDelayed = this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT));
                    z = onDoubleTap | this.mListener.onDown(motionEvent2);
                    break;
                case 1:
                    this.mStillDown = false;
                    MotionEvent obtain = MotionEvent.obtain(motionEvent2);
                    if (this.mIsDoubleTapping) {
                        z |= this.mDoubleTapListener.onDoubleTapEvent(motionEvent2);
                    } else if (this.mInLongPress) {
                        this.mHandler.removeMessages(3);
                        this.mInLongPress = false;
                    } else if (this.mAlwaysInTapRegion) {
                        z = this.mListener.onSingleTapUp(motionEvent2);
                        if (this.mDeferConfirmSingleTap && this.mDoubleTapListener != null) {
                            sendEmptyMessageDelayed = this.mDoubleTapListener.onSingleTapConfirmed(motionEvent2);
                        }
                    } else {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        pointerId = MotionEventCompat.getPointerId(motionEvent2, 0);
                        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
                        float yVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);
                        float xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
                        if (Math.abs(yVelocity) > ((float) this.mMinimumFlingVelocity) || Math.abs(xVelocity) > ((float) this.mMinimumFlingVelocity)) {
                            z = this.mListener.onFling(this.mCurrentDownEvent, motionEvent2, xVelocity, yVelocity);
                        }
                    }
                    if (this.mPreviousUpEvent != null) {
                        this.mPreviousUpEvent.recycle();
                    }
                    this.mPreviousUpEvent = obtain;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mIsDoubleTapping = false;
                    this.mDeferConfirmSingleTap = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    break;
                case 2:
                    if (!this.mInLongPress) {
                        float f7 = this.mLastFocusX - f3;
                        float f8 = this.mLastFocusY - f4;
                        if (!this.mIsDoubleTapping) {
                            if (!this.mAlwaysInTapRegion) {
                                if (Math.abs(f7) >= 1.0f || Math.abs(f8) >= 1.0f) {
                                    z = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent2, f7, f8);
                                    this.mLastFocusX = f3;
                                    this.mLastFocusY = f4;
                                    break;
                                }
                            }
                            int i2 = (int) (f3 - this.mDownFocusX);
                            int i3 = (int) (f4 - this.mDownFocusY);
                            pointerId = (i2 * i2) + (i3 * i3);
                            if (pointerId > this.mTouchSlopSquare) {
                                z = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent2, f7, f8);
                                this.mLastFocusX = f3;
                                this.mLastFocusY = f4;
                                this.mAlwaysInTapRegion = false;
                                this.mHandler.removeMessages(3);
                                this.mHandler.removeMessages(1);
                                this.mHandler.removeMessages(2);
                            }
                            if (pointerId > this.mTouchSlopSquare) {
                                this.mAlwaysInBiggerTapRegion = false;
                                break;
                            }
                        }
                        z |= this.mDoubleTapListener.onDoubleTapEvent(motionEvent2);
                        break;
                    }
                    break;
                case 3:
                    cancel();
                    break;
                case 5:
                    f5 = f3;
                    f6 = f5;
                    this.mLastFocusX = f5;
                    this.mDownFocusX = f6;
                    f5 = f4;
                    f6 = f5;
                    this.mLastFocusY = f5;
                    this.mDownFocusY = f6;
                    cancelTaps();
                    break;
                case 6:
                    f5 = f3;
                    f6 = f5;
                    this.mLastFocusX = f5;
                    this.mDownFocusX = f6;
                    f5 = f4;
                    f6 = f5;
                    this.mLastFocusY = f5;
                    this.mDownFocusY = f6;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
                    int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent2);
                    int pointerId2 = MotionEventCompat.getPointerId(motionEvent2, actionIndex2);
                    float xVelocity2 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId2);
                    float yVelocity2 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId2);
                    for (int i4 = 0; i4 < pointerCount; i4++) {
                        if (i4 != actionIndex2) {
                            int pointerId3 = MotionEventCompat.getPointerId(motionEvent2, i4);
                            if ((xVelocity2 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId3)) + (yVelocity2 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId3)) < 0.0f) {
                                this.mVelocityTracker.clear();
                                break;
                            }
                        }
                    }
                    break;
            }
            return z;
        }

        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
            MotionEvent motionEvent4 = motionEvent;
            MotionEvent motionEvent5 = motionEvent2;
            MotionEvent motionEvent6 = motionEvent3;
            if (!this.mAlwaysInBiggerTapRegion) {
                return false;
            }
            if (motionEvent6.getEventTime() - motionEvent5.getEventTime() > ((long) DOUBLE_TAP_TIMEOUT)) {
                return false;
            }
            int x = ((int) motionEvent4.getX()) - ((int) motionEvent6.getX());
            int y = ((int) motionEvent4.getY()) - ((int) motionEvent6.getY());
            return (x * x) + (y * y) < this.mDoubleTapSlopSquare;
        }

        /* access modifiers changed from: private */
        public void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        public GestureDetectorCompatImplJellybeanMr2(Context context, OnGestureListener onGestureListener, Handler handler) {
            GestureDetector gestureDetector = r10;
            GestureDetector gestureDetector2 = new GestureDetector(context, onGestureListener, handler);
            this.mDetector = gestureDetector;
        }

        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }

        public void setIsLongpressEnabled(boolean z) {
            this.mDetector.setIsLongpressEnabled(z);
        }

        public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }

    public GestureDetectorCompat(Context context, OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetectorCompat(Context context, OnGestureListener onGestureListener, Handler handler) {
        Context context2 = context;
        OnGestureListener onGestureListener2 = onGestureListener;
        Handler handler2 = handler;
        if (VERSION.SDK_INT > 17) {
            GestureDetectorCompatImplJellybeanMr2 gestureDetectorCompatImplJellybeanMr2 = r10;
            GestureDetectorCompatImplJellybeanMr2 gestureDetectorCompatImplJellybeanMr22 = new GestureDetectorCompatImplJellybeanMr2(context2, onGestureListener2, handler2);
            this.mImpl = gestureDetectorCompatImplJellybeanMr2;
            return;
        }
        GestureDetectorCompatImplBase gestureDetectorCompatImplBase = r10;
        GestureDetectorCompatImplBase gestureDetectorCompatImplBase2 = new GestureDetectorCompatImplBase(context2, onGestureListener2, handler2);
        this.mImpl = gestureDetectorCompatImplBase;
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }

    public void setIsLongpressEnabled(boolean z) {
        this.mImpl.setIsLongpressEnabled(z);
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }
}
