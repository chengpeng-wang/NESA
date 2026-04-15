package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public abstract class AutoScrollHelper implements OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    /* access modifiers changed from: private */
    public boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMinimumVelocity = new float[]{0.0f, 0.0f};
    /* access modifiers changed from: private */
    public boolean mNeedsCancel;
    /* access modifiers changed from: private */
    public boolean mNeedsReset;
    private float[] mRelativeEdges = new float[]{0.0f, 0.0f};
    private float[] mRelativeVelocity = new float[]{0.0f, 0.0f};
    private Runnable mRunnable;
    /* access modifiers changed from: private|final */
    public final ClampedScroller mScroller;
    /* access modifiers changed from: private|final */
    public final View mTarget;

    private static class ClampedScroller {
        private long mDeltaTime = 0;
        private int mDeltaX = 0;
        private int mDeltaY = 0;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        public ClampedScroller() {
        }

        public void setRampUpDuration(int i) {
            this.mRampUpDuration = i;
        }

        public void setRampDownDuration(int i) {
            this.mRampDownDuration = i;
        }

        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }

        public void requestStop() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentAnimationTimeMillis - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = getValueAt(currentAnimationTimeMillis);
            this.mStopTime = currentAnimationTimeMillis;
        }

        public boolean isFinished() {
            boolean z = this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
            return z;
        }

        private float getValueAt(long j) {
            long j2 = j;
            if (j2 < this.mStartTime) {
                return 0.0f;
            }
            if (this.mStopTime < 0 || j2 < this.mStopTime) {
                return 0.5f * AutoScrollHelper.constrain(((float) (j2 - this.mStartTime)) / ((float) this.mRampUpDuration), 0.0f, (float) AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY);
            }
            return (AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY - this.mStopValue) + (this.mStopValue * AutoScrollHelper.constrain(((float) (j2 - this.mStopTime)) / ((float) this.mEffectiveRampDown), 0.0f, (float) AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY));
        }

        private float interpolateValue(float f) {
            float f2 = f;
            return ((-4.0f * f2) * f2) + (4.0f * f2);
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) {
                RuntimeException runtimeException = r11;
                RuntimeException runtimeException2 = new RuntimeException("Cannot compute scroll delta before calling start()");
                throw runtimeException;
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float interpolateValue = interpolateValue(getValueAt(currentAnimationTimeMillis));
            long j = currentAnimationTimeMillis - this.mDeltaTime;
            this.mDeltaTime = currentAnimationTimeMillis;
            this.mDeltaX = (int) ((((float) j) * interpolateValue) * this.mTargetVelocityX);
            this.mDeltaY = (int) ((((float) j) * interpolateValue) * this.mTargetVelocityY);
        }

        public void setTargetVelocity(float f, float f2) {
            float f3 = f2;
            this.mTargetVelocityX = f;
            this.mTargetVelocityY = f3;
        }

        public int getHorizontalDirection() {
            return (int) (this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
        }

        public int getVerticalDirection() {
            return (int) (this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }
    }

    private class ScrollAnimationRunnable implements Runnable {
        final /* synthetic */ AutoScrollHelper this$0;

        private ScrollAnimationRunnable(AutoScrollHelper autoScrollHelper) {
            this.this$0 = autoScrollHelper;
        }

        /* synthetic */ ScrollAnimationRunnable(AutoScrollHelper autoScrollHelper, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(autoScrollHelper);
        }

        public void run() {
            if (this.this$0.mAnimating) {
                boolean access$202;
                if (this.this$0.mNeedsReset) {
                    access$202 = AutoScrollHelper.access$202(this.this$0, false);
                    this.this$0.mScroller.start();
                }
                ClampedScroller access$300 = this.this$0.mScroller;
                if (access$300.isFinished() || !this.this$0.shouldAnimate()) {
                    access$202 = AutoScrollHelper.access$102(this.this$0, false);
                    return;
                }
                if (this.this$0.mNeedsCancel) {
                    access$202 = AutoScrollHelper.access$502(this.this$0, false);
                    this.this$0.cancelTargetTouch();
                }
                access$300.computeScrollDelta();
                this.this$0.scrollTargetBy(access$300.getDeltaX(), access$300.getDeltaY());
                ViewCompat.postOnAnimation(this.this$0.mTarget, this);
            }
        }
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    public abstract void scrollTargetBy(int i, int i2);

    static /* synthetic */ boolean access$102(AutoScrollHelper autoScrollHelper, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        autoScrollHelper.mAnimating = z4;
        return z3;
    }

    static /* synthetic */ boolean access$202(AutoScrollHelper autoScrollHelper, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        autoScrollHelper.mNeedsReset = z4;
        return z3;
    }

    static /* synthetic */ boolean access$502(AutoScrollHelper autoScrollHelper, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        autoScrollHelper.mNeedsCancel = z4;
        return z3;
    }

    public AutoScrollHelper(View view) {
        View view2 = view;
        ClampedScroller clampedScroller = r10;
        ClampedScroller clampedScroller2 = new ClampedScroller();
        this.mScroller = clampedScroller;
        AccelerateInterpolator accelerateInterpolator = r10;
        AccelerateInterpolator accelerateInterpolator2 = new AccelerateInterpolator();
        this.mEdgeInterpolator = accelerateInterpolator;
        this.mTarget = view2;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int i = (int) ((1575.0f * displayMetrics.density) + 0.5f);
        int i2 = (int) ((315.0f * displayMetrics.density) + 0.5f);
        AutoScrollHelper thisR = setMaximumVelocity((float) i, (float) i);
        thisR = setMinimumVelocity((float) i2, (float) i2);
        thisR = setEdgeType(1);
        thisR = setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        thisR = setRelativeEdges(DEFAULT_RELATIVE_EDGE, DEFAULT_RELATIVE_EDGE);
        thisR = setRelativeVelocity(DEFAULT_RELATIVE_VELOCITY, DEFAULT_RELATIVE_VELOCITY);
        thisR = setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        thisR = setRampUpDuration(500);
        thisR = setRampDownDuration(500);
    }

    public AutoScrollHelper setEnabled(boolean z) {
        boolean z2 = z;
        if (this.mEnabled && !z2) {
            requestStop();
        }
        this.mEnabled = z2;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public AutoScrollHelper setExclusive(boolean z) {
        this.mExclusive = z;
        return this;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f2) {
        float f3 = f2;
        this.mMaximumVelocity[0] = f / 1000.0f;
        this.mMaximumVelocity[1] = f3 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f2) {
        float f3 = f2;
        this.mMinimumVelocity[0] = f / 1000.0f;
        this.mMinimumVelocity[1] = f3 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f2) {
        float f3 = f2;
        this.mRelativeVelocity[0] = f / 1000.0f;
        this.mRelativeVelocity[1] = f3 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setEdgeType(int i) {
        this.mEdgeType = i;
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f2) {
        float f3 = f2;
        this.mRelativeEdges[0] = f;
        this.mRelativeEdges[1] = f3;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f2) {
        float f3 = f2;
        this.mMaximumEdges[0] = f;
        this.mMaximumEdges[1] = f3;
        return this;
    }

    public AutoScrollHelper setActivationDelay(int i) {
        this.mActivationDelay = i;
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int i) {
        this.mScroller.setRampUpDuration(i);
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int i) {
        this.mScroller.setRampDownDuration(i);
        return this;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        View view2 = view;
        MotionEvent motionEvent2 = motionEvent;
        if (!this.mEnabled) {
            return false;
        }
        boolean z;
        switch (MotionEventCompat.getActionMasked(motionEvent2)) {
            case 0:
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
                break;
            case 1:
            case 3:
                requestStop();
                break;
            case 2:
                break;
        }
        this.mScroller.setTargetVelocity(computeTargetVelocity(0, motionEvent2.getX(), (float) view2.getWidth(), (float) this.mTarget.getWidth()), computeTargetVelocity(1, motionEvent2.getY(), (float) view2.getHeight(), (float) this.mTarget.getHeight()));
        if (!this.mAnimating && shouldAnimate()) {
            startAnimating();
        }
        if (this.mExclusive && this.mAnimating) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int verticalDirection = clampedScroller.getVerticalDirection();
        int horizontalDirection = clampedScroller.getHorizontalDirection();
        boolean z = (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
        return z;
    }

    private void startAnimating() {
        if (this.mRunnable == null) {
            ScrollAnimationRunnable scrollAnimationRunnable = r6;
            ScrollAnimationRunnable scrollAnimationRunnable2 = new ScrollAnimationRunnable(this, null);
            this.mRunnable = scrollAnimationRunnable;
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (this.mAlreadyDelayed || this.mActivationDelay <= 0) {
            this.mRunnable.run();
        } else {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, (long) this.mActivationDelay);
        }
        this.mAlreadyDelayed = true;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private float computeTargetVelocity(int i, float f, float f2, float f3) {
        int i2 = i;
        float f4 = f;
        float f5 = f2;
        float f6 = f3;
        float edgeValue = getEdgeValue(this.mRelativeEdges[i2], f5, this.mMaximumEdges[i2], f4);
        if (edgeValue == 0.0f) {
            return 0.0f;
        }
        float f7 = this.mRelativeVelocity[i2];
        float f8 = this.mMinimumVelocity[i2];
        float f9 = this.mMaximumVelocity[i2];
        float f10 = f7 * f6;
        if (edgeValue > 0.0f) {
            return constrain(edgeValue * f10, f8, f9);
        }
        return -constrain((-edgeValue) * f10, f8, f9);
    }

    private float getEdgeValue(float f, float f2, float f3, float f4) {
        float f5;
        float f6 = f2;
        float f7 = f4;
        float constrain = constrain(f * f6, 0.0f, f3);
        float constrainEdgeValue = constrainEdgeValue(f6 - f7, constrain) - constrainEdgeValue(f7, constrain);
        if (constrainEdgeValue < 0.0f) {
            f5 = -this.mEdgeInterpolator.getInterpolation(-constrainEdgeValue);
        } else if (constrainEdgeValue <= 0.0f) {
            return 0.0f;
        } else {
            f5 = this.mEdgeInterpolator.getInterpolation(constrainEdgeValue);
        }
        return constrain(f5, -1.0f, (float) DEFAULT_RELATIVE_VELOCITY);
    }

    private float constrainEdgeValue(float f, float f2) {
        float f3 = f;
        float f4 = f2;
        if (f4 == 0.0f) {
            return 0.0f;
        }
        switch (this.mEdgeType) {
            case 0:
            case 1:
                if (f3 < f4) {
                    if (f3 >= 0.0f) {
                        return DEFAULT_RELATIVE_VELOCITY - (f3 / f4);
                    }
                    if (this.mAnimating && this.mEdgeType == 1) {
                        return DEFAULT_RELATIVE_VELOCITY;
                    }
                }
                break;
            case 2:
                if (f3 < 0.0f) {
                    return f3 / (-f4);
                }
                break;
        }
        return 0.0f;
    }

    /* access modifiers changed from: private|static */
    public static int constrain(int i, int i2, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (i4 > i6) {
            return i6;
        }
        if (i4 < i5) {
            return i5;
        }
        return i4;
    }

    /* access modifiers changed from: private|static */
    public static float constrain(float f, float f2, float f3) {
        float f4 = f;
        float f5 = f2;
        float f6 = f3;
        if (f4 > f6) {
            return f6;
        }
        if (f4 < f5) {
            return f5;
        }
        return f4;
    }

    /* access modifiers changed from: private */
    public void cancelTargetTouch() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        boolean onTouchEvent = this.mTarget.onTouchEvent(obtain);
        obtain.recycle();
    }
}
