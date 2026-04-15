package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator;
    private int mActivePointerId = -1;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public abstract boolean tryCaptureView(View view, int i);

        public Callback() {
        }

        public void onViewDragStateChanged(int i) {
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
        }

        public void onViewCaptured(View view, int i) {
        }

        public void onViewReleased(View view, float f, float f2) {
        }

        public void onEdgeTouched(int i, int i2) {
        }

        public boolean onEdgeLock(int i) {
            int i2 = i;
            return false;
        }

        public void onEdgeDragStarted(int i, int i2) {
        }

        public int getOrderedChildIndex(int i) {
            return i;
        }

        public int getViewHorizontalDragRange(View view) {
            View view2 = view;
            return 0;
        }

        public int getViewVerticalDragRange(View view) {
            View view2 = view;
            return 0;
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            View view2 = view;
            int i3 = i;
            int i4 = i2;
            return 0;
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            View view2 = view;
            int i3 = i;
            int i4 = i2;
            return 0;
        }
    }

    static {
        AnonymousClass1 anonymousClass1 = r2;
        AnonymousClass1 anonymousClass12 = new Interpolator() {
            public float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return ((((f2 * f2) * f2) * f2) * f2) + 1.0f;
            }
        };
        sInterpolator = anonymousClass1;
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        ViewGroup viewGroup2 = viewGroup;
        ViewDragHelper viewDragHelper = r7;
        ViewDragHelper viewDragHelper2 = new ViewDragHelper(viewGroup2.getContext(), viewGroup2, callback);
        return viewDragHelper;
    }

    public static ViewDragHelper create(ViewGroup viewGroup, float f, Callback callback) {
        float f2 = f;
        ViewDragHelper create = create(viewGroup, callback);
        create.mTouchSlop = (int) (((float) create.mTouchSlop) * (1.0f / f2));
        return create;
    }

    private ViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        Context context2 = context;
        ViewGroup viewGroup2 = viewGroup;
        Callback callback2 = callback;
        AnonymousClass2 anonymousClass2 = r10;
        AnonymousClass2 anonymousClass22 = new Runnable(this) {
            final /* synthetic */ ViewDragHelper this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                this.this$0.setDragState(0);
            }
        };
        this.mSetIdleRunnable = anonymousClass2;
        IllegalArgumentException illegalArgumentException;
        IllegalArgumentException illegalArgumentException2;
        if (viewGroup2 == null) {
            illegalArgumentException = r10;
            illegalArgumentException2 = new IllegalArgumentException("Parent view may not be null");
            throw illegalArgumentException;
        } else if (callback2 == null) {
            illegalArgumentException = r10;
            illegalArgumentException2 = new IllegalArgumentException("Callback may not be null");
            throw illegalArgumentException;
        } else {
            this.mParentView = viewGroup2;
            this.mCallback = callback2;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context2);
            this.mEdgeSize = (int) ((20.0f * context2.getResources().getDisplayMetrics().density) + 0.5f);
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
            this.mMaxVelocity = (float) viewConfiguration.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            this.mScroller = ScrollerCompat.create(context2, sInterpolator);
        }
    }

    public void setMinVelocity(float f) {
        this.mMinVelocity = f;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public void setEdgeTrackingEnabled(int i) {
        this.mTrackingEdges = i;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public void captureChildView(View view, int i) {
        View view2 = view;
        int i2 = i;
        if (view2.getParent() != this.mParentView) {
            IllegalArgumentException illegalArgumentException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (").append(this.mParentView).append(")").toString());
            throw illegalArgumentException;
        }
        this.mCapturedView = view2;
        this.mActivePointerId = i2;
        this.mCallback.onViewCaptured(view2, i2);
        setDragState(1);
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == 2) {
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int currX2 = this.mScroller.getCurrX();
            int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        setDragState(0);
    }

    public boolean smoothSlideViewTo(View view, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        return forceSettleCapturedViewAt(i3, i4, 0, 0);
    }

    public boolean settleCapturedViewAt(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(i3, i4, (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
        }
        IllegalStateException illegalStateException = r9;
        IllegalStateException illegalStateException2 = new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
        throw illegalStateException;
    }

    private boolean forceSettleCapturedViewAt(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        int i9 = i5 - left;
        int i10 = i6 - top;
        if (i9 == 0 && i10 == 0) {
            this.mScroller.abortAnimation();
            setDragState(0);
            return false;
        }
        this.mScroller.startScroll(left, top, i9, i10, computeSettleDuration(this.mCapturedView, i9, i10, i7, i8));
        setDragState(2);
        return true;
    }

    private int computeSettleDuration(View view, int i, int i2, int i3, int i4) {
        View view2 = view;
        int i5 = i;
        int i6 = i2;
        int i7 = i4;
        int clampMag = clampMag(i3, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        i7 = clampMag(i7, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int abs = Math.abs(i5);
        int abs2 = Math.abs(i6);
        int abs3 = Math.abs(clampMag);
        int abs4 = Math.abs(i7);
        int i8 = abs3 + abs4;
        int i9 = abs + abs2;
        return (int) ((((float) computeAxisDuration(i5, clampMag, this.mCallback.getViewHorizontalDragRange(view2))) * (clampMag != 0 ? ((float) abs3) / ((float) i8) : ((float) abs) / ((float) i9))) + (((float) computeAxisDuration(i6, i7, this.mCallback.getViewVerticalDragRange(view2))) * (i7 != 0 ? ((float) abs4) / ((float) i8) : ((float) abs2) / ((float) i9))));
    }

    private int computeAxisDuration(int i, int i2, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (i4 == 0) {
            return 0;
        }
        int round;
        int width = this.mParentView.getWidth();
        int i7 = width / 2;
        float distanceInfluenceForSnapDuration = ((float) i7) + (((float) i7) * distanceInfluenceForSnapDuration(Math.min(1.0f, ((float) Math.abs(i4)) / ((float) width))));
        i5 = Math.abs(i5);
        if (i5 > 0) {
            round = 4 * Math.round(1000.0f * Math.abs(distanceInfluenceForSnapDuration / ((float) i5)));
        } else {
            round = (int) (((((float) Math.abs(i4)) / ((float) i6)) + 1.0f) * 256.0f);
        }
        return Math.min(round, MAX_SETTLE_DURATION);
    }

    private int clampMag(int i, int i2, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        int abs = Math.abs(i4);
        if (abs < i5) {
            return 0;
        }
        if (abs <= i6) {
            return i4;
        }
        return i4 > 0 ? i6 : -i6;
    }

    private float clampMag(float f, float f2, float f3) {
        float f4 = f;
        float f5 = f2;
        float f6 = f3;
        float abs = Math.abs(f4);
        if (abs < f5) {
            return 0.0f;
        }
        if (abs <= f6) {
            return f4;
        }
        return f4 > 0.0f ? f6 : -f6;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    public void flingCapturedView(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), i5, i7, i6, i8);
            setDragState(2);
            return;
        }
        IllegalStateException illegalStateException = r14;
        IllegalStateException illegalStateException2 = new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        throw illegalStateException;
    }

    public boolean continueSettling(boolean z) {
        boolean post;
        boolean z2 = z;
        if (this.mDragState == 2) {
            boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (left != 0) {
                this.mCapturedView.offsetLeftAndRight(left);
            }
            if (top != 0) {
                this.mCapturedView.offsetTopAndBottom(top);
            }
            if (!(left == 0 && top == 0)) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                computeScrollOffset = this.mScroller.isFinished();
            }
            if (!computeScrollOffset) {
                if (z2) {
                    post = this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    setDragState(0);
                }
            }
        }
        if (this.mDragState == 2) {
            post = true;
        } else {
            post = false;
        }
        return post;
    }

    private void dispatchViewReleased(float f, float f2) {
        float f3 = f;
        float f4 = f2;
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f3, f4);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(0);
        }
    }

    private void clearMotionHistory() {
        if (this.mInitialMotionX != null) {
            Arrays.fill(this.mInitialMotionX, 0.0f);
            Arrays.fill(this.mInitialMotionY, 0.0f);
            Arrays.fill(this.mLastMotionX, 0.0f);
            Arrays.fill(this.mLastMotionY, 0.0f);
            Arrays.fill(this.mInitialEdgesTouched, 0);
            Arrays.fill(this.mEdgeDragsInProgress, 0);
            Arrays.fill(this.mEdgeDragsLocked, 0);
            this.mPointersDown = 0;
        }
    }

    private void clearMotionHistory(int i) {
        int i2 = i;
        if (this.mInitialMotionX != null) {
            this.mInitialMotionX[i2] = 0.0f;
            this.mInitialMotionY[i2] = 0.0f;
            this.mLastMotionX[i2] = 0.0f;
            this.mLastMotionY[i2] = 0.0f;
            this.mInitialEdgesTouched[i2] = 0;
            this.mEdgeDragsInProgress[i2] = 0;
            this.mEdgeDragsLocked[i2] = 0;
            this.mPointersDown &= (1 << i2) ^ -1;
        }
    }

    private void ensureMotionHistorySizeForId(int i) {
        int i2 = i;
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= i2) {
            Object obj = new float[(i2 + 1)];
            Object obj2 = new float[(i2 + 1)];
            Object obj3 = new float[(i2 + 1)];
            Object obj4 = new float[(i2 + 1)];
            Object obj5 = new int[(i2 + 1)];
            Object obj6 = new int[(i2 + 1)];
            Object obj7 = new int[(i2 + 1)];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, 0, obj, 0, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, 0, obj2, 0, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, 0, obj3, 0, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, 0, obj4, 0, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, 0, obj5, 0, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, 0, obj6, 0, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, 0, obj7, 0, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = obj;
            this.mInitialMotionY = obj2;
            this.mLastMotionX = obj3;
            this.mLastMotionY = obj4;
            this.mInitialEdgesTouched = obj5;
            this.mEdgeDragsInProgress = obj6;
            this.mEdgeDragsLocked = obj7;
        }
    }

    private void saveInitialMotion(float f, float f2, int i) {
        float f3 = f;
        float f4 = f2;
        int i2 = i;
        ensureMotionHistorySizeForId(i2);
        float[] fArr = this.mInitialMotionX;
        int i3 = i2;
        float f5 = f3;
        float f6 = f5;
        this.mLastMotionX[i2] = f5;
        fArr[i3] = f6;
        fArr = this.mInitialMotionY;
        i3 = i2;
        f5 = f4;
        f6 = f5;
        this.mLastMotionY[i2] = f5;
        fArr[i3] = f6;
        this.mInitialEdgesTouched[i2] = getEdgesTouched((int) f3, (int) f4);
        this.mPointersDown |= 1 << i2;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        int pointerCount = MotionEventCompat.getPointerCount(motionEvent2);
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = MotionEventCompat.getPointerId(motionEvent2, i);
            float x = MotionEventCompat.getX(motionEvent2, i);
            float y = MotionEventCompat.getY(motionEvent2, i);
            this.mLastMotionX[pointerId] = x;
            this.mLastMotionY[pointerId] = y;
        }
    }

    public boolean isPointerDown(int i) {
        return (this.mPointersDown & (1 << i)) != 0;
    }

    /* access modifiers changed from: 0000 */
    public void setDragState(int i) {
        int i2 = i;
        if (this.mDragState != i2) {
            this.mDragState = i2;
            this.mCallback.onViewDragStateChanged(i2);
            if (i2 == 0) {
                this.mCapturedView = null;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean tryCaptureViewForDrag(View view, int i) {
        View view2 = view;
        int i2 = i;
        if (view2 == this.mCapturedView && this.mActivePointerId == i2) {
            return true;
        }
        if (view2 == null || !this.mCallback.tryCaptureView(view2, i2)) {
            return false;
        }
        this.mActivePointerId = i2;
        captureChildView(view2, i2);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View view, boolean z, int i, int i2, int i3, int i4) {
        View view2 = view;
        boolean z2 = z;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int scrollX = view2.getScrollX();
            int scrollY = view2.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (i7 + scrollX >= childAt.getLeft() && i7 + scrollX < childAt.getRight() && i8 + scrollY >= childAt.getTop() && i8 + scrollY < childAt.getBottom() && canScroll(childAt, true, i5, i6, (i7 + scrollX) - childAt.getLeft(), (i8 + scrollY) - childAt.getTop())) {
                    return true;
                }
            }
        }
        boolean z3 = z2 && (ViewCompat.canScrollHorizontally(view2, -i5) || ViewCompat.canScrollVertically(view2, -i6));
        return z3;
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        boolean tryCaptureViewForDrag;
        MotionEvent motionEvent2 = motionEvent;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent2);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent2);
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent2);
        float y;
        int pointerId;
        View findTopChildUnder;
        int pointerCount;
        switch (actionMasked) {
            case 0:
                float x = motionEvent2.getX();
                y = motionEvent2.getY();
                pointerId = MotionEventCompat.getPointerId(motionEvent2, 0);
                saveInitialMotion(x, y, pointerId);
                findTopChildUnder = findTopChildUnder((int) x, (int) y);
                if (findTopChildUnder == this.mCapturedView && this.mDragState == 2) {
                    tryCaptureViewForDrag = tryCaptureViewForDrag(findTopChildUnder, pointerId);
                }
                int i = this.mInitialEdgesTouched[pointerId];
                if ((i & this.mTrackingEdges) != 0) {
                    this.mCallback.onEdgeTouched(i & this.mTrackingEdges, pointerId);
                    break;
                }
                break;
            case 1:
            case 3:
                cancel();
                break;
            case 2:
                pointerCount = MotionEventCompat.getPointerCount(motionEvent2);
                int i2 = 0;
                while (i2 < pointerCount) {
                    pointerId = MotionEventCompat.getPointerId(motionEvent2, i2);
                    float x2 = MotionEventCompat.getX(motionEvent2, i2);
                    float y2 = MotionEventCompat.getY(motionEvent2, i2);
                    float f = x2 - this.mInitialMotionX[pointerId];
                    float f2 = y2 - this.mInitialMotionY[pointerId];
                    reportNewEdgeDrags(f, f2, pointerId);
                    if (this.mDragState != 1) {
                        View findTopChildUnder2 = findTopChildUnder((int) x2, (int) y2);
                        if (findTopChildUnder2 == null || !checkTouchSlop(findTopChildUnder2, f, f2) || !tryCaptureViewForDrag(findTopChildUnder2, pointerId)) {
                            i2++;
                        }
                    }
                    saveLastMotion(motionEvent2);
                    break;
                }
                saveLastMotion(motionEvent2);
                break;
            case 5:
                pointerCount = MotionEventCompat.getPointerId(motionEvent2, actionIndex);
                y = MotionEventCompat.getX(motionEvent2, actionIndex);
                float y3 = MotionEventCompat.getY(motionEvent2, actionIndex);
                saveInitialMotion(y, y3, pointerCount);
                if (this.mDragState != 0) {
                    if (this.mDragState == 2) {
                        findTopChildUnder = findTopChildUnder((int) y, (int) y3);
                        if (findTopChildUnder == this.mCapturedView) {
                            tryCaptureViewForDrag = tryCaptureViewForDrag(findTopChildUnder, pointerCount);
                            break;
                        }
                    }
                }
                int i3 = this.mInitialEdgesTouched[pointerCount];
                if ((i3 & this.mTrackingEdges) != 0) {
                    this.mCallback.onEdgeTouched(i3 & this.mTrackingEdges, pointerCount);
                    break;
                }
                break;
            case 6:
                clearMotionHistory(MotionEventCompat.getPointerId(motionEvent2, actionIndex));
                break;
        }
        if (this.mDragState == 1) {
            tryCaptureViewForDrag = true;
        } else {
            tryCaptureViewForDrag = false;
        }
        return tryCaptureViewForDrag;
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x01f6  */
    public void processTouchEvent(android.view.MotionEvent r18) {
        /*
        r17 = this;
        r0 = r17;
        r1 = r18;
        r12 = r1;
        r12 = android.support.v4.view.MotionEventCompat.getActionMasked(r12);
        r2 = r12;
        r12 = r1;
        r12 = android.support.v4.view.MotionEventCompat.getActionIndex(r12);
        r3 = r12;
        r12 = r2;
        if (r12 != 0) goto L_0x0017;
    L_0x0013:
        r12 = r0;
        r12.cancel();
    L_0x0017:
        r12 = r0;
        r12 = r12.mVelocityTracker;
        if (r12 != 0) goto L_0x0023;
    L_0x001c:
        r12 = r0;
        r13 = android.view.VelocityTracker.obtain();
        r12.mVelocityTracker = r13;
    L_0x0023:
        r12 = r0;
        r12 = r12.mVelocityTracker;
        r13 = r1;
        r12.addMovement(r13);
        r12 = r2;
        switch(r12) {
            case 0: goto L_0x002f;
            case 1: goto L_0x0201;
            case 2: goto L_0x00d9;
            case 3: goto L_0x0211;
            case 4: goto L_0x002e;
            case 5: goto L_0x0075;
            case 6: goto L_0x0196;
            default: goto L_0x002e;
        };
    L_0x002e:
        return;
    L_0x002f:
        r12 = r1;
        r12 = r12.getX();
        r4 = r12;
        r12 = r1;
        r12 = r12.getY();
        r5 = r12;
        r12 = r1;
        r13 = 0;
        r12 = android.support.v4.view.MotionEventCompat.getPointerId(r12, r13);
        r6 = r12;
        r12 = r0;
        r13 = r4;
        r13 = (int) r13;
        r14 = r5;
        r14 = (int) r14;
        r12 = r12.findTopChildUnder(r13, r14);
        r7 = r12;
        r12 = r0;
        r13 = r4;
        r14 = r5;
        r15 = r6;
        r12.saveInitialMotion(r13, r14, r15);
        r12 = r0;
        r13 = r7;
        r14 = r6;
        r12 = r12.tryCaptureViewForDrag(r13, r14);
        r12 = r0;
        r12 = r12.mInitialEdgesTouched;
        r13 = r6;
        r12 = r12[r13];
        r8 = r12;
        r12 = r8;
        r13 = r0;
        r13 = r13.mTrackingEdges;
        r12 = r12 & r13;
        if (r12 == 0) goto L_0x002e;
    L_0x0068:
        r12 = r0;
        r12 = r12.mCallback;
        r13 = r8;
        r14 = r0;
        r14 = r14.mTrackingEdges;
        r13 = r13 & r14;
        r14 = r6;
        r12.onEdgeTouched(r13, r14);
        goto L_0x002e;
    L_0x0075:
        r12 = r1;
        r13 = r3;
        r12 = android.support.v4.view.MotionEventCompat.getPointerId(r12, r13);
        r4 = r12;
        r12 = r1;
        r13 = r3;
        r12 = android.support.v4.view.MotionEventCompat.getX(r12, r13);
        r5 = r12;
        r12 = r1;
        r13 = r3;
        r12 = android.support.v4.view.MotionEventCompat.getY(r12, r13);
        r6 = r12;
        r12 = r0;
        r13 = r5;
        r14 = r6;
        r15 = r4;
        r12.saveInitialMotion(r13, r14, r15);
        r12 = r0;
        r12 = r12.mDragState;
        if (r12 != 0) goto L_0x00c3;
    L_0x0096:
        r12 = r0;
        r13 = r5;
        r13 = (int) r13;
        r14 = r6;
        r14 = (int) r14;
        r12 = r12.findTopChildUnder(r13, r14);
        r7 = r12;
        r12 = r0;
        r13 = r7;
        r14 = r4;
        r12 = r12.tryCaptureViewForDrag(r13, r14);
        r12 = r0;
        r12 = r12.mInitialEdgesTouched;
        r13 = r4;
        r12 = r12[r13];
        r8 = r12;
        r12 = r8;
        r13 = r0;
        r13 = r13.mTrackingEdges;
        r12 = r12 & r13;
        if (r12 == 0) goto L_0x00c1;
    L_0x00b5:
        r12 = r0;
        r12 = r12.mCallback;
        r13 = r8;
        r14 = r0;
        r14 = r14.mTrackingEdges;
        r13 = r13 & r14;
        r14 = r4;
        r12.onEdgeTouched(r13, r14);
    L_0x00c1:
        goto L_0x002e;
    L_0x00c3:
        r12 = r0;
        r13 = r5;
        r13 = (int) r13;
        r14 = r6;
        r14 = (int) r14;
        r12 = r12.isCapturedViewUnder(r13, r14);
        if (r12 == 0) goto L_0x002e;
    L_0x00ce:
        r12 = r0;
        r13 = r0;
        r13 = r13.mCapturedView;
        r14 = r4;
        r12 = r12.tryCaptureViewForDrag(r13, r14);
        goto L_0x002e;
    L_0x00d9:
        r12 = r0;
        r12 = r12.mDragState;
        r13 = 1;
        if (r12 != r13) goto L_0x012e;
    L_0x00df:
        r12 = r1;
        r13 = r0;
        r13 = r13.mActivePointerId;
        r12 = android.support.v4.view.MotionEventCompat.findPointerIndex(r12, r13);
        r4 = r12;
        r12 = r1;
        r13 = r4;
        r12 = android.support.v4.view.MotionEventCompat.getX(r12, r13);
        r5 = r12;
        r12 = r1;
        r13 = r4;
        r12 = android.support.v4.view.MotionEventCompat.getY(r12, r13);
        r6 = r12;
        r12 = r5;
        r13 = r0;
        r13 = r13.mLastMotionX;
        r14 = r0;
        r14 = r14.mActivePointerId;
        r13 = r13[r14];
        r12 = r12 - r13;
        r12 = (int) r12;
        r7 = r12;
        r12 = r6;
        r13 = r0;
        r13 = r13.mLastMotionY;
        r14 = r0;
        r14 = r14.mActivePointerId;
        r13 = r13[r14];
        r12 = r12 - r13;
        r12 = (int) r12;
        r8 = r12;
        r12 = r0;
        r13 = r0;
        r13 = r13.mCapturedView;
        r13 = r13.getLeft();
        r14 = r7;
        r13 = r13 + r14;
        r14 = r0;
        r14 = r14.mCapturedView;
        r14 = r14.getTop();
        r15 = r8;
        r14 = r14 + r15;
        r15 = r7;
        r16 = r8;
        r12.dragTo(r13, r14, r15, r16);
        r12 = r0;
        r13 = r1;
        r12.saveLastMotion(r13);
        goto L_0x002e;
    L_0x012e:
        r12 = r1;
        r12 = android.support.v4.view.MotionEventCompat.getPointerCount(r12);
        r4 = r12;
        r12 = 0;
        r5 = r12;
    L_0x0136:
        r12 = r5;
        r13 = r4;
        if (r12 >= r13) goto L_0x016e;
    L_0x013a:
        r12 = r1;
        r13 = r5;
        r12 = android.support.v4.view.MotionEventCompat.getPointerId(r12, r13);
        r6 = r12;
        r12 = r1;
        r13 = r5;
        r12 = android.support.v4.view.MotionEventCompat.getX(r12, r13);
        r7 = r12;
        r12 = r1;
        r13 = r5;
        r12 = android.support.v4.view.MotionEventCompat.getY(r12, r13);
        r8 = r12;
        r12 = r7;
        r13 = r0;
        r13 = r13.mInitialMotionX;
        r14 = r6;
        r13 = r13[r14];
        r12 = r12 - r13;
        r9 = r12;
        r12 = r8;
        r13 = r0;
        r13 = r13.mInitialMotionY;
        r14 = r6;
        r13 = r13[r14];
        r12 = r12 - r13;
        r10 = r12;
        r12 = r0;
        r13 = r9;
        r14 = r10;
        r15 = r6;
        r12.reportNewEdgeDrags(r13, r14, r15);
        r12 = r0;
        r12 = r12.mDragState;
        r13 = 1;
        if (r12 != r13) goto L_0x0175;
    L_0x016e:
        r12 = r0;
        r13 = r1;
        r12.saveLastMotion(r13);
        goto L_0x002e;
    L_0x0175:
        r12 = r0;
        r13 = r7;
        r13 = (int) r13;
        r14 = r8;
        r14 = (int) r14;
        r12 = r12.findTopChildUnder(r13, r14);
        r11 = r12;
        r12 = r0;
        r13 = r11;
        r14 = r9;
        r15 = r10;
        r12 = r12.checkTouchSlop(r13, r14, r15);
        if (r12 == 0) goto L_0x0193;
    L_0x0189:
        r12 = r0;
        r13 = r11;
        r14 = r6;
        r12 = r12.tryCaptureViewForDrag(r13, r14);
        if (r12 == 0) goto L_0x0193;
    L_0x0192:
        goto L_0x016e;
    L_0x0193:
        r5 = r5 + 1;
        goto L_0x0136;
    L_0x0196:
        r12 = r1;
        r13 = r3;
        r12 = android.support.v4.view.MotionEventCompat.getPointerId(r12, r13);
        r4 = r12;
        r12 = r0;
        r12 = r12.mDragState;
        r13 = 1;
        if (r12 != r13) goto L_0x01fa;
    L_0x01a3:
        r12 = r4;
        r13 = r0;
        r13 = r13.mActivePointerId;
        if (r12 != r13) goto L_0x01fa;
    L_0x01a9:
        r12 = -1;
        r5 = r12;
        r12 = r1;
        r12 = android.support.v4.view.MotionEventCompat.getPointerCount(r12);
        r6 = r12;
        r12 = 0;
        r7 = r12;
    L_0x01b3:
        r12 = r7;
        r13 = r6;
        if (r12 >= r13) goto L_0x01f2;
    L_0x01b7:
        r12 = r1;
        r13 = r7;
        r12 = android.support.v4.view.MotionEventCompat.getPointerId(r12, r13);
        r8 = r12;
        r12 = r8;
        r13 = r0;
        r13 = r13.mActivePointerId;
        if (r12 != r13) goto L_0x01c7;
    L_0x01c4:
        r7 = r7 + 1;
        goto L_0x01b3;
    L_0x01c7:
        r12 = r1;
        r13 = r7;
        r12 = android.support.v4.view.MotionEventCompat.getX(r12, r13);
        r9 = r12;
        r12 = r1;
        r13 = r7;
        r12 = android.support.v4.view.MotionEventCompat.getY(r12, r13);
        r10 = r12;
        r12 = r0;
        r13 = r9;
        r13 = (int) r13;
        r14 = r10;
        r14 = (int) r14;
        r12 = r12.findTopChildUnder(r13, r14);
        r13 = r0;
        r13 = r13.mCapturedView;
        if (r12 != r13) goto L_0x01c4;
    L_0x01e3:
        r12 = r0;
        r13 = r0;
        r13 = r13.mCapturedView;
        r14 = r8;
        r12 = r12.tryCaptureViewForDrag(r13, r14);
        if (r12 == 0) goto L_0x01c4;
    L_0x01ee:
        r12 = r0;
        r12 = r12.mActivePointerId;
        r5 = r12;
    L_0x01f2:
        r12 = r5;
        r13 = -1;
        if (r12 != r13) goto L_0x01fa;
    L_0x01f6:
        r12 = r0;
        r12.releaseViewForPointerUp();
    L_0x01fa:
        r12 = r0;
        r13 = r4;
        r12.clearMotionHistory(r13);
        goto L_0x002e;
    L_0x0201:
        r12 = r0;
        r12 = r12.mDragState;
        r13 = 1;
        if (r12 != r13) goto L_0x020b;
    L_0x0207:
        r12 = r0;
        r12.releaseViewForPointerUp();
    L_0x020b:
        r12 = r0;
        r12.cancel();
        goto L_0x002e;
    L_0x0211:
        r12 = r0;
        r12 = r12.mDragState;
        r13 = 1;
        if (r12 != r13) goto L_0x021d;
    L_0x0217:
        r12 = r0;
        r13 = 0;
        r14 = 0;
        r12.dispatchViewReleased(r13, r14);
    L_0x021d:
        r12 = r0;
        r12.cancel();
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.ViewDragHelper.processTouchEvent(android.view.MotionEvent):void");
    }

    private void reportNewEdgeDrags(float f, float f2, int i) {
        float f3 = f;
        float f4 = f2;
        int i2 = i;
        int i3 = 0;
        if (checkNewEdgeDrag(f3, f4, i2, 1)) {
            i3 |= 1;
        }
        if (checkNewEdgeDrag(f4, f3, i2, 4)) {
            i3 |= 4;
        }
        if (checkNewEdgeDrag(f3, f4, i2, 2)) {
            i3 |= 2;
        }
        if (checkNewEdgeDrag(f4, f3, i2, 8)) {
            i3 |= 8;
        }
        if (i3 != 0) {
            int[] iArr = this.mEdgeDragsInProgress;
            int i4 = i2;
            iArr[i4] = iArr[i4] | i3;
            this.mCallback.onEdgeDragStarted(i3, i2);
        }
    }

    private boolean checkNewEdgeDrag(float f, float f2, int i, int i2) {
        float f3 = f2;
        int i3 = i;
        int i4 = i2;
        float abs = Math.abs(f);
        float abs2 = Math.abs(f3);
        if ((this.mInitialEdgesTouched[i3] & i4) != i4 || (this.mTrackingEdges & i4) == 0 || (this.mEdgeDragsLocked[i3] & i4) == i4 || (this.mEdgeDragsInProgress[i3] & i4) == i4 || (abs <= ((float) this.mTouchSlop) && abs2 <= ((float) this.mTouchSlop))) {
            return false;
        }
        if (abs >= abs2 * 0.5f || !this.mCallback.onEdgeLock(i4)) {
            boolean z = (this.mEdgeDragsInProgress[i3] & i4) == 0 && abs > ((float) this.mTouchSlop);
            return z;
        }
        int[] iArr = this.mEdgeDragsLocked;
        int i5 = i3;
        iArr[i5] = iArr[i5] | i4;
        return false;
    }

    private boolean checkTouchSlop(View view, float f, float f2) {
        View view2 = view;
        float f3 = f;
        float f4 = f2;
        if (view2 == null) {
            return false;
        }
        Object obj = this.mCallback.getViewHorizontalDragRange(view2) > 0 ? 1 : null;
        Object obj2 = this.mCallback.getViewVerticalDragRange(view2) > 0 ? 1 : null;
        if (obj != null && obj2 != null) {
            return (f3 * f3) + (f4 * f4) > ((float) (this.mTouchSlop * this.mTouchSlop));
        } else if (obj != null) {
            return Math.abs(f3) > ((float) this.mTouchSlop);
        } else if (obj2 == null) {
            return false;
        } else {
            return Math.abs(f4) > ((float) this.mTouchSlop);
        }
    }

    public boolean checkTouchSlop(int i) {
        int i2 = i;
        int length = this.mInitialMotionX.length;
        for (int i3 = 0; i3 < length; i3++) {
            if (checkTouchSlop(i2, i3)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (!isPointerDown(i4)) {
            return false;
        }
        Object obj = (i3 & 1) == 1 ? 1 : null;
        Object obj2 = (i3 & 2) == 2 ? 1 : null;
        float f = this.mLastMotionX[i4] - this.mInitialMotionX[i4];
        float f2 = this.mLastMotionY[i4] - this.mInitialMotionY[i4];
        if (obj != null && obj2 != null) {
            boolean z;
            if ((f * f) + (f2 * f2) > ((float) (this.mTouchSlop * this.mTouchSlop))) {
                z = true;
            } else {
                z = false;
            }
            return z;
        } else if (obj != null) {
            return Math.abs(f) > ((float) this.mTouchSlop);
        } else if (obj2 == null) {
            return false;
        } else {
            return Math.abs(f2) > ((float) this.mTouchSlop);
        }
    }

    public boolean isEdgeTouched(int i) {
        int i2 = i;
        int length = this.mInitialEdgesTouched.length;
        for (int i3 = 0; i3 < length; i3++) {
            if (isEdgeTouched(i2, i3)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeTouched(int i, int i2) {
        int i3 = i2;
        boolean z = isPointerDown(i3) && (this.mInitialEdgesTouched[i3] & i) != 0;
        return z;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void dragTo(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        int i9 = i5;
        int i10 = i6;
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        if (i7 != 0) {
            i9 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, i5, i7);
            this.mCapturedView.offsetLeftAndRight(i9 - left);
        }
        if (i8 != 0) {
            i10 = this.mCallback.clampViewPositionVertical(this.mCapturedView, i6, i8);
            this.mCapturedView.offsetTopAndBottom(i10 - top);
        }
        if (i7 != 0 || i8 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, i9, i10, i9 - left, i10 - top);
        }
    }

    public boolean isCapturedViewUnder(int i, int i2) {
        return isViewUnder(this.mCapturedView, i, i2);
    }

    public boolean isViewUnder(View view, int i, int i2) {
        View view2 = view;
        int i3 = i;
        int i4 = i2;
        if (view2 == null) {
            return false;
        }
        boolean z = i3 >= view2.getLeft() && i3 < view2.getRight() && i4 >= view2.getTop() && i4 < view2.getBottom();
        return z;
    }

    public View findTopChildUnder(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        for (int childCount = this.mParentView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(childCount));
            if (i3 >= childAt.getLeft() && i3 < childAt.getRight() && i4 >= childAt.getTop() && i4 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    private int getEdgesTouched(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        int i5 = 0;
        if (i3 < this.mParentView.getLeft() + this.mEdgeSize) {
            i5 |= 1;
        }
        if (i4 < this.mParentView.getTop() + this.mEdgeSize) {
            i5 |= 4;
        }
        if (i3 > this.mParentView.getRight() - this.mEdgeSize) {
            i5 |= 2;
        }
        if (i4 > this.mParentView.getBottom() - this.mEdgeSize) {
            i5 |= 8;
        }
        return i5;
    }
}
