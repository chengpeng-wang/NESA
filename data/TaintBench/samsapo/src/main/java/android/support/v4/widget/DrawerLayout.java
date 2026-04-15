package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public class DrawerLayout extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    /* access modifiers changed from: private|static|final */
    public static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mMinDrawerMargin;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;
        final /* synthetic */ DrawerLayout this$0;

        AccessibilityDelegate(DrawerLayout drawerLayout) {
            this.this$0 = drawerLayout;
            Rect rect = r5;
            Rect rect2 = new Rect();
            this.mTmpRect = rect;
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            View view2 = view;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
            AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat2);
            super.onInitializeAccessibilityNodeInfo(view2, obtain);
            accessibilityNodeInfoCompat2.setSource(view2);
            ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(view2);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat2.setParent((View) parentForAccessibility);
            }
            copyNodeInfoNoChildren(accessibilityNodeInfoCompat2, obtain);
            obtain.recycle();
            addChildrenForAccessibility(accessibilityNodeInfoCompat2, (ViewGroup) view2);
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
            ViewGroup viewGroup2 = viewGroup;
            int childCount = viewGroup2.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup2.getChildAt(i);
                if (!filter(childAt)) {
                    switch (ViewCompat.getImportantForAccessibility(childAt)) {
                        case 0:
                            ViewCompat.setImportantForAccessibility(childAt, 1);
                            break;
                        case 1:
                            break;
                        case 2:
                            if (childAt instanceof ViewGroup) {
                                addChildrenForAccessibility(accessibilityNodeInfoCompat2, (ViewGroup) childAt);
                                break;
                            }
                            continue;
                        default:
                            break;
                    }
                    accessibilityNodeInfoCompat2.addChild(childAt);
                }
            }
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            View view2 = view;
            return !filter(view2) ? super.onRequestSendAccessibilityEvent(viewGroup, view2, accessibilityEvent) : DrawerLayout.ALLOW_EDGE_LOCK;
        }

        public boolean filter(View view) {
            View view2 = view;
            View findOpenDrawer = this.this$0.findOpenDrawer();
            boolean z = (findOpenDrawer == null || findOpenDrawer == view2) ? DrawerLayout.ALLOW_EDGE_LOCK : true;
            return z;
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat3 = accessibilityNodeInfoCompat;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat4 = accessibilityNodeInfoCompat2;
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat4.getBoundsInParent(rect);
            accessibilityNodeInfoCompat3.setBoundsInParent(rect);
            accessibilityNodeInfoCompat4.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat3.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat3.setVisibleToUser(accessibilityNodeInfoCompat4.isVisibleToUser());
            accessibilityNodeInfoCompat3.setPackageName(accessibilityNodeInfoCompat4.getPackageName());
            accessibilityNodeInfoCompat3.setClassName(accessibilityNodeInfoCompat4.getClassName());
            accessibilityNodeInfoCompat3.setContentDescription(accessibilityNodeInfoCompat4.getContentDescription());
            accessibilityNodeInfoCompat3.setEnabled(accessibilityNodeInfoCompat4.isEnabled());
            accessibilityNodeInfoCompat3.setClickable(accessibilityNodeInfoCompat4.isClickable());
            accessibilityNodeInfoCompat3.setFocusable(accessibilityNodeInfoCompat4.isFocusable());
            accessibilityNodeInfoCompat3.setFocused(accessibilityNodeInfoCompat4.isFocused());
            accessibilityNodeInfoCompat3.setAccessibilityFocused(accessibilityNodeInfoCompat4.isAccessibilityFocused());
            accessibilityNodeInfoCompat3.setSelected(accessibilityNodeInfoCompat4.isSelected());
            accessibilityNodeInfoCompat3.setLongClickable(accessibilityNodeInfoCompat4.isLongClickable());
            accessibilityNodeInfoCompat3.addAction(accessibilityNodeInfoCompat4.getActions());
        }
    }

    public interface DrawerListener {
        void onDrawerClosed(View view);

        void onDrawerOpened(View view);

        void onDrawerSlide(View view, float f);

        void onDrawerStateChanged(int i);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        boolean isPeeking;
        boolean knownOpen;
        float onScreen;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            Context context2 = context;
            AttributeSet attributeSet2 = attributeSet;
            super(context2, attributeSet2);
            this.gravity = 0;
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = 0;
        }

        public LayoutParams(int i, int i2, int i3) {
            int i4 = i3;
            this(i, i2);
            this.gravity = i4;
        }

        public LayoutParams(LayoutParams layoutParams) {
            MarginLayoutParams marginLayoutParams = layoutParams;
            super(marginLayoutParams);
            this.gravity = 0;
            this.gravity = marginLayoutParams.gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = 0;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = 0;
        }
    }

    protected static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        int lockModeLeft = 0;
        int lockModeRight = 0;
        int openDrawerGravity = 0;

        public SavedState(Parcel parcel) {
            Parcel parcel2 = parcel;
            super(parcel2);
            this.openDrawerGravity = parcel2.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            Parcel parcel2 = parcel;
            super.writeToParcel(parcel2, i);
            parcel2.writeInt(this.openDrawerGravity);
        }

        static {
            AnonymousClass1 anonymousClass1 = r2;
            AnonymousClass1 anonymousClass12 = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel parcel) {
                    SavedState savedState = r5;
                    SavedState savedState2 = new SavedState(parcel);
                    return savedState;
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            CREATOR = anonymousClass1;
        }
    }

    public static abstract class SimpleDrawerListener implements DrawerListener {
        public SimpleDrawerListener() {
        }

        public void onDrawerSlide(View view, float f) {
        }

        public void onDrawerOpened(View view) {
        }

        public void onDrawerClosed(View view) {
        }

        public void onDrawerStateChanged(int i) {
        }
    }

    private class ViewDragCallback extends Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;
        final /* synthetic */ DrawerLayout this$0;

        public ViewDragCallback(DrawerLayout drawerLayout, int i) {
            int i2 = i;
            this.this$0 = drawerLayout;
            AnonymousClass1 anonymousClass1 = r7;
            AnonymousClass1 anonymousClass12 = new Runnable(this) {
                final /* synthetic */ ViewDragCallback this$1;

                {
                    this.this$1 = r5;
                }

                public void run() {
                    this.this$1.peekDrawer();
                }
            };
            this.mPeekRunnable = anonymousClass1;
            this.mAbsGravity = i2;
        }

        public void setDragger(ViewDragHelper viewDragHelper) {
            this.mDragger = viewDragHelper;
        }

        public void removeCallbacks() {
            boolean removeCallbacks = this.this$0.removeCallbacks(this.mPeekRunnable);
        }

        public boolean tryCaptureView(View view, int i) {
            View view2 = view;
            int i2 = i;
            boolean z = (this.this$0.isDrawerView(view2) && this.this$0.checkDrawerViewAbsoluteGravity(view2, this.mAbsGravity) && this.this$0.getDrawerLockMode(view2) == 0) ? true : DrawerLayout.ALLOW_EDGE_LOCK;
            return z;
        }

        public void onViewDragStateChanged(int i) {
            this.this$0.updateDrawerState(this.mAbsGravity, i, this.mDragger.getCapturedView());
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            float f;
            View view2 = view;
            int i5 = i;
            int i6 = i2;
            int i7 = i3;
            int i8 = i4;
            int width = view2.getWidth();
            if (this.this$0.checkDrawerViewAbsoluteGravity(view2, 3)) {
                f = ((float) (width + i5)) / ((float) width);
            } else {
                f = ((float) (this.this$0.getWidth() - i5)) / ((float) width);
            }
            this.this$0.setDrawerViewOffset(view2, f);
            view2.setVisibility(f == 0.0f ? 4 : 0);
            this.this$0.invalidate();
        }

        public void onViewCaptured(View view, int i) {
            int i2 = i;
            ((LayoutParams) view.getLayoutParams()).isPeeking = DrawerLayout.ALLOW_EDGE_LOCK;
            closeOtherDrawer();
        }

        private void closeOtherDrawer() {
            View findDrawerWithGravity = this.this$0.findDrawerWithGravity(this.mAbsGravity == 3 ? 5 : 3);
            if (findDrawerWithGravity != null) {
                this.this$0.closeDrawer(findDrawerWithGravity);
            }
        }

        public void onViewReleased(View view, float f, float f2) {
            int i;
            View view2 = view;
            float f3 = f;
            float f4 = f2;
            float drawerViewOffset = this.this$0.getDrawerViewOffset(view2);
            int width = view2.getWidth();
            int i2;
            if (this.this$0.checkDrawerViewAbsoluteGravity(view2, 3)) {
                i2 = (f3 > 0.0f || (f3 == 0.0f && drawerViewOffset > 0.5f)) ? 0 : -width;
                i = i2;
            } else {
                int width2 = this.this$0.getWidth();
                i2 = (f3 < 0.0f || (f3 == 0.0f && drawerViewOffset > 0.5f)) ? width2 - width : width2;
                i = i2;
            }
            boolean z = this.mDragger.settleCapturedViewAt(i, view2.getTop());
            this.this$0.invalidate();
        }

        public void onEdgeTouched(int i, int i2) {
            int i3 = i;
            int i4 = i2;
            boolean postDelayed = this.this$0.postDelayed(this.mPeekRunnable, 160);
        }

        /* access modifiers changed from: private */
        public void peekDrawer() {
            View findDrawerWithGravity;
            int i;
            int edgeSize = this.mDragger.getEdgeSize();
            Object obj = this.mAbsGravity == 3 ? 1 : null;
            if (obj != null) {
                findDrawerWithGravity = this.this$0.findDrawerWithGravity(3);
                i = (findDrawerWithGravity != null ? -findDrawerWithGravity.getWidth() : 0) + edgeSize;
            } else {
                findDrawerWithGravity = this.this$0.findDrawerWithGravity(5);
                i = this.this$0.getWidth() - edgeSize;
            }
            if (findDrawerWithGravity == null) {
                return;
            }
            if (((obj != null && findDrawerWithGravity.getLeft() < i) || (obj == null && findDrawerWithGravity.getLeft() > i)) && this.this$0.getDrawerLockMode(findDrawerWithGravity) == 0) {
                LayoutParams layoutParams = (LayoutParams) findDrawerWithGravity.getLayoutParams();
                boolean smoothSlideViewTo = this.mDragger.smoothSlideViewTo(findDrawerWithGravity, i, findDrawerWithGravity.getTop());
                layoutParams.isPeeking = true;
                this.this$0.invalidate();
                closeOtherDrawer();
                this.this$0.cancelChildViewTouch();
            }
        }

        public boolean onEdgeLock(int i) {
            int i2 = i;
            return DrawerLayout.ALLOW_EDGE_LOCK;
        }

        public void onEdgeDragStarted(int i, int i2) {
            View findDrawerWithGravity;
            int i3 = i2;
            if ((i & 1) == 1) {
                findDrawerWithGravity = this.this$0.findDrawerWithGravity(3);
            } else {
                findDrawerWithGravity = this.this$0.findDrawerWithGravity(5);
            }
            if (findDrawerWithGravity != null && this.this$0.getDrawerLockMode(findDrawerWithGravity) == 0) {
                this.mDragger.captureChildView(findDrawerWithGravity, i3);
            }
        }

        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            View view2 = view;
            int i3 = i;
            int i4 = i2;
            if (this.this$0.checkDrawerViewAbsoluteGravity(view2, 3)) {
                return Math.max(-view2.getWidth(), Math.min(i3, 0));
            }
            int width = this.this$0.getWidth();
            return Math.max(width - view2.getWidth(), Math.min(i3, width));
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            int i3 = i;
            int i4 = i2;
            return view.getTop();
        }
    }

    static {
        int[] iArr = new int[1];
        int[] iArr2 = iArr;
        iArr[0] = 16842931;
        LAYOUT_ATTRS = iArr2;
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        Paint paint = r11;
        Paint paint2 = new Paint();
        this.mScrimPaint = paint;
        this.mFirstLayout = true;
        float f = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) ((64.0f * f) + 0.5f);
        float f2 = 400.0f * f;
        ViewDragCallback viewDragCallback = r11;
        ViewDragCallback viewDragCallback2 = new ViewDragCallback(this, 3);
        this.mLeftCallback = viewDragCallback;
        viewDragCallback = r11;
        viewDragCallback2 = new ViewDragCallback(this, 5);
        this.mRightCallback = viewDragCallback;
        this.mLeftDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(f2);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(f2);
        this.mRightCallback.setDragger(this.mRightDragger);
        setFocusableInTouchMode(true);
        AccessibilityDelegateCompat accessibilityDelegateCompat = r11;
        AccessibilityDelegateCompat accessibilityDelegate = new AccessibilityDelegate(this);
        ViewCompat.setAccessibilityDelegate(this, accessibilityDelegateCompat);
        ViewGroupCompat.setMotionEventSplittingEnabled(this, ALLOW_EDGE_LOCK);
    }

    public void setDrawerShadow(Drawable drawable, int i) {
        Drawable drawable2 = drawable;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        if ((absoluteGravity & 3) == 3) {
            this.mShadowLeft = drawable2;
            invalidate();
        }
        if ((absoluteGravity & 5) == 5) {
            this.mShadowRight = drawable2;
            invalidate();
        }
    }

    public void setDrawerShadow(int i, int i2) {
        int i3 = i2;
        setDrawerShadow(getResources().getDrawable(i), i3);
    }

    public void setScrimColor(int i) {
        this.mScrimColor = i;
        invalidate();
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.mListener = drawerListener;
    }

    public void setDrawerLockMode(int i) {
        int i2 = i;
        setDrawerLockMode(i2, 3);
        setDrawerLockMode(i2, 5);
    }

    public void setDrawerLockMode(int i, int i2) {
        int i3 = i;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i2, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 3) {
            this.mLockModeLeft = i3;
        } else if (absoluteGravity == 5) {
            this.mLockModeRight = i3;
        }
        if (i3 != 0) {
            (absoluteGravity == 3 ? this.mLeftDragger : this.mRightDragger).cancel();
        }
        switch (i3) {
            case 1:
                View findDrawerWithGravity = findDrawerWithGravity(absoluteGravity);
                if (findDrawerWithGravity != null) {
                    closeDrawer(findDrawerWithGravity);
                    return;
                }
                return;
            case 2:
                View findDrawerWithGravity2 = findDrawerWithGravity(absoluteGravity);
                if (findDrawerWithGravity2 != null) {
                    openDrawer(findDrawerWithGravity2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setDrawerLockMode(int i, View view) {
        int i2 = i;
        View view2 = view;
        if (isDrawerView(view2)) {
            setDrawerLockMode(i2, ((LayoutParams) view2.getLayoutParams()).gravity);
            return;
        }
        IllegalArgumentException illegalArgumentException = r8;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("View ").append(view2).append(" is not a ").append("drawer with appropriate layout_gravity").toString());
        throw illegalArgumentException;
    }

    public int getDrawerLockMode(int i) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 3) {
            return this.mLockModeLeft;
        }
        if (absoluteGravity == 5) {
            return this.mLockModeRight;
        }
        return 0;
    }

    public int getDrawerLockMode(View view) {
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(view);
        if (drawerViewAbsoluteGravity == 3) {
            return this.mLockModeLeft;
        }
        if (drawerViewAbsoluteGravity == 5) {
            return this.mLockModeRight;
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public void updateDrawerState(int i, int i2, View view) {
        int i3;
        int i4 = i;
        int i5 = i2;
        View view2 = view;
        int viewDragState = this.mLeftDragger.getViewDragState();
        int viewDragState2 = this.mRightDragger.getViewDragState();
        if (viewDragState == 1 || viewDragState2 == 1) {
            i3 = 1;
        } else if (viewDragState == 2 || viewDragState2 == 2) {
            i3 = 2;
        } else {
            i3 = 0;
        }
        if (view2 != null && i5 == 0) {
            LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                dispatchOnDrawerClosed(view2);
            } else if (layoutParams.onScreen == TOUCH_SLOP_SENSITIVITY) {
                dispatchOnDrawerOpened(view2);
            }
        }
        if (i3 != this.mDrawerState) {
            this.mDrawerState = i3;
            if (this.mListener != null) {
                this.mListener.onDrawerStateChanged(i3);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerClosed(View view) {
        View view2 = view;
        LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
        if (layoutParams.knownOpen) {
            layoutParams.knownOpen = ALLOW_EDGE_LOCK;
            if (this.mListener != null) {
                this.mListener.onDrawerClosed(view2);
            }
            sendAccessibilityEvent(32);
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerOpened(View view) {
        View view2 = view;
        LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
        if (!layoutParams.knownOpen) {
            layoutParams.knownOpen = true;
            if (this.mListener != null) {
                this.mListener.onDrawerOpened(view2);
            }
            view2.sendAccessibilityEvent(32);
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerSlide(View view, float f) {
        View view2 = view;
        float f2 = f;
        if (this.mListener != null) {
            this.mListener.onDrawerSlide(view2, f2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setDrawerViewOffset(View view, float f) {
        View view2 = view;
        float f2 = f;
        LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
        if (f2 != layoutParams.onScreen) {
            layoutParams.onScreen = f2;
            dispatchOnDrawerSlide(view2, f2);
        }
    }

    /* access modifiers changed from: 0000 */
    public float getDrawerViewOffset(View view) {
        return ((LayoutParams) view.getLayoutParams()).onScreen;
    }

    /* access modifiers changed from: 0000 */
    public int getDrawerViewAbsoluteGravity(View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams) view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
    }

    /* access modifiers changed from: 0000 */
    public boolean checkDrawerViewAbsoluteGravity(View view, int i) {
        int i2 = i;
        return (getDrawerViewAbsoluteGravity(view) & i2) == i2 ? true : ALLOW_EDGE_LOCK;
    }

    /* access modifiers changed from: 0000 */
    public View findOpenDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (((LayoutParams) childAt.getLayoutParams()).knownOpen) {
                return childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void moveDrawerToOffset(View view, float f) {
        View view2 = view;
        float f2 = f;
        float drawerViewOffset = getDrawerViewOffset(view2);
        int width = view2.getWidth();
        int i = ((int) (((float) width) * f2)) - ((int) (((float) width) * drawerViewOffset));
        view2.offsetLeftAndRight(checkDrawerViewAbsoluteGravity(view2, 3) ? i : -i);
        setDrawerViewOffset(view2, f2);
    }

    /* access modifiers changed from: 0000 */
    public View findDrawerWithGravity(int i) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if ((getDrawerViewAbsoluteGravity(childAt) & 7) == absoluteGravity) {
                return childAt;
            }
        }
        return null;
    }

    static String gravityToString(int i) {
        int i2 = i;
        if ((i2 & 3) == 3) {
            return "LEFT";
        }
        if ((i2 & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(i2);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        int mode = MeasureSpec.getMode(i3);
        int mode2 = MeasureSpec.getMode(i4);
        int size = MeasureSpec.getSize(i3);
        int size2 = MeasureSpec.getSize(i4);
        if (!(mode == 1073741824 && mode2 == 1073741824)) {
            if (isInEditMode()) {
                if (mode == Integer.MIN_VALUE) {
                    mode = 1073741824;
                } else if (mode == 0) {
                    mode = 1073741824;
                    size = 300;
                }
                if (mode2 == Integer.MIN_VALUE) {
                    mode2 = 1073741824;
                } else if (mode2 == 0) {
                    mode2 = 1073741824;
                    size2 = 300;
                }
            } else {
                IllegalArgumentException illegalArgumentException = r21;
                IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
                throw illegalArgumentException;
            }
        }
        setMeasuredDimension(size, size2);
        int i5 = 0;
        int childCount = getChildCount();
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                IllegalStateException illegalStateException;
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                IllegalStateException illegalStateException2;
                if (isContentView(childAt)) {
                    childAt.measure(MeasureSpec.makeMeasureSpec((size - layoutParams.leftMargin) - layoutParams.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec((size2 - layoutParams.topMargin) - layoutParams.bottomMargin, 1073741824));
                } else if (isDrawerView(childAt)) {
                    int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(childAt) & 7;
                    if ((i5 & drawerViewAbsoluteGravity) != 0) {
                        illegalStateException = r21;
                        stringBuilder = r21;
                        stringBuilder2 = new StringBuilder();
                        illegalStateException2 = new IllegalStateException(stringBuilder.append("Child drawer has absolute gravity ").append(gravityToString(drawerViewAbsoluteGravity)).append(" but this ").append(TAG).append(" already has a ").append("drawer view along that edge").toString());
                        throw illegalStateException;
                    }
                    childAt.measure(getChildMeasureSpec(i3, (this.mMinDrawerMargin + layoutParams.leftMargin) + layoutParams.rightMargin, layoutParams.width), getChildMeasureSpec(i4, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                } else {
                    illegalStateException = r21;
                    stringBuilder = r21;
                    stringBuilder2 = new StringBuilder();
                    illegalStateException2 = new IllegalStateException(stringBuilder.append("Child ").append(childAt).append(" at index ").append(i6).append(" does not have a valid layout_gravity - must be Gravity.LEFT, ").append("Gravity.RIGHT or Gravity.NO_GRAVITY").toString());
                    throw illegalStateException;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2 = z;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        this.mInLayout = true;
        int i9 = i7 - i5;
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (isContentView(childAt)) {
                    childAt.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + childAt.getMeasuredWidth(), layoutParams.topMargin + childAt.getMeasuredHeight());
                } else {
                    int i11;
                    float f;
                    int i12;
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                        i11 = (-measuredWidth) + ((int) (((float) measuredWidth) * layoutParams.onScreen));
                        f = ((float) (measuredWidth + i11)) / ((float) measuredWidth);
                    } else {
                        i11 = i9 - ((int) (((float) measuredWidth) * layoutParams.onScreen));
                        f = ((float) (i9 - i11)) / ((float) measuredWidth);
                    }
                    Object obj = f != layoutParams.onScreen ? 1 : null;
                    switch (layoutParams.gravity & 112) {
                        case 16:
                            i12 = i8 - i6;
                            int i13 = (i12 - measuredHeight) / 2;
                            if (i13 < layoutParams.topMargin) {
                                i13 = layoutParams.topMargin;
                            } else {
                                if (i13 + measuredHeight > i12 - layoutParams.bottomMargin) {
                                    i13 = (i12 - layoutParams.bottomMargin) - measuredHeight;
                                }
                            }
                            childAt.layout(i11, i13, i11 + measuredWidth, i13 + measuredHeight);
                            break;
                        case 80:
                            i12 = i8 - i6;
                            childAt.layout(i11, (i12 - layoutParams.bottomMargin) - childAt.getMeasuredHeight(), i11 + measuredWidth, i12 - layoutParams.bottomMargin);
                            break;
                        default:
                            childAt.layout(i11, layoutParams.topMargin, i11 + measuredWidth, layoutParams.topMargin + measuredHeight);
                            break;
                    }
                    if (obj != null) {
                        setDrawerViewOffset(childAt, f);
                    }
                    i12 = layoutParams.onScreen > 0.0f ? 0 : 4;
                    if (childAt.getVisibility() != i12) {
                        childAt.setVisibility(i12);
                    }
                }
            }
        }
        this.mInLayout = ALLOW_EDGE_LOCK;
        this.mFirstLayout = ALLOW_EDGE_LOCK;
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void computeScroll() {
        int childCount = getChildCount();
        float f = 0.0f;
        for (int i = 0; i < childCount; i++) {
            f = Math.max(f, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        if ((this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private static boolean hasOpaqueBackground(View view) {
        Drawable background = view.getBackground();
        if (background == null) {
            return ALLOW_EDGE_LOCK;
        }
        return background.getOpacity() == -1 ? true : ALLOW_EDGE_LOCK;
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        int i;
        Canvas canvas2 = canvas;
        View view2 = view;
        long j2 = j;
        int height = getHeight();
        boolean isContentView = isContentView(view2);
        int i2 = 0;
        int width = getWidth();
        int save = canvas2.save();
        if (isContentView) {
            int childCount = getChildCount();
            for (i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt != view2 && childAt.getVisibility() == 0 && hasOpaqueBackground(childAt) && isDrawerView(childAt) && childAt.getHeight() >= height) {
                    int right;
                    if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                        right = childAt.getRight();
                        if (right > i2) {
                            i2 = right;
                        }
                    } else {
                        right = childAt.getLeft();
                        if (right < width) {
                            width = right;
                        }
                    }
                }
            }
            boolean clipRect = canvas2.clipRect(i2, 0, width, getHeight());
        }
        boolean drawChild = super.drawChild(canvas2, view2, j2);
        canvas2.restoreToCount(save);
        int right2;
        if (this.mScrimOpacity > 0.0f && isContentView) {
            this.mScrimPaint.setColor((((int) (((float) ((this.mScrimColor & ViewCompat.MEASURED_STATE_MASK) >>> 24)) * this.mScrimOpacity)) << 24) | (this.mScrimColor & ViewCompat.MEASURED_SIZE_MASK));
            canvas2.drawRect((float) i2, 0.0f, (float) width, (float) getHeight(), this.mScrimPaint);
        } else if (this.mShadowLeft != null && checkDrawerViewAbsoluteGravity(view2, 3)) {
            i = this.mShadowLeft.getIntrinsicWidth();
            right2 = view2.getRight();
            float max = Math.max(0.0f, Math.min(((float) right2) / ((float) this.mLeftDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            this.mShadowLeft.setBounds(right2, view2.getTop(), right2 + i, view2.getBottom());
            this.mShadowLeft.setAlpha((int) (255.0f * max));
            this.mShadowLeft.draw(canvas2);
        } else if (this.mShadowRight != null && checkDrawerViewAbsoluteGravity(view2, 5)) {
            i = this.mShadowRight.getIntrinsicWidth();
            right2 = view2.getLeft();
            float max2 = Math.max(0.0f, Math.min(((float) (getWidth() - right2)) / ((float) this.mRightDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            this.mShadowRight.setBounds(right2 - i, view2.getTop(), right2, view2.getBottom());
            this.mShadowRight.setAlpha((int) (255.0f * max2));
            this.mShadowRight.draw(canvas2);
        }
        return drawChild;
    }

    /* access modifiers changed from: 0000 */
    public boolean isContentView(View view) {
        return ((LayoutParams) view.getLayoutParams()).gravity == 0 ? true : ALLOW_EDGE_LOCK;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDrawerView(View view) {
        View view2 = view;
        return (GravityCompat.getAbsoluteGravity(((LayoutParams) view2.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view2)) & 7) != 0 ? true : ALLOW_EDGE_LOCK;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        MotionEvent motionEvent2 = motionEvent;
        int shouldInterceptTouchEvent = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent2) | this.mRightDragger.shouldInterceptTouchEvent(motionEvent2);
        Object obj = null;
        switch (MotionEventCompat.getActionMasked(motionEvent2)) {
            case 0:
                float x = motionEvent2.getX();
                float y = motionEvent2.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mScrimOpacity > 0.0f && isContentView(this.mLeftDragger.findTopChildUnder((int) x, (int) y))) {
                    obj = 1;
                }
                this.mDisallowInterceptRequested = ALLOW_EDGE_LOCK;
                this.mChildrenCanceledTouch = ALLOW_EDGE_LOCK;
                break;
            case 1:
            case 3:
                closeDrawers(true);
                this.mDisallowInterceptRequested = ALLOW_EDGE_LOCK;
                this.mChildrenCanceledTouch = ALLOW_EDGE_LOCK;
                break;
            case 2:
                if (this.mLeftDragger.checkTouchSlop(3)) {
                    this.mLeftCallback.removeCallbacks();
                    this.mRightCallback.removeCallbacks();
                    break;
                }
                break;
        }
        if (shouldInterceptTouchEvent != 0 || obj != null || hasPeekingDrawer() || this.mChildrenCanceledTouch) {
            z = true;
        } else {
            z = ALLOW_EDGE_LOCK;
        }
        return z;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        this.mLeftDragger.processTouchEvent(motionEvent2);
        this.mRightDragger.processTouchEvent(motionEvent2);
        boolean z = true;
        float x;
        float y;
        switch (motionEvent2.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                x = motionEvent2.getX();
                y = motionEvent2.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                this.mDisallowInterceptRequested = ALLOW_EDGE_LOCK;
                this.mChildrenCanceledTouch = ALLOW_EDGE_LOCK;
                break;
            case 1:
                x = motionEvent2.getX();
                y = motionEvent2.getY();
                boolean z2 = true;
                View findTopChildUnder = this.mLeftDragger.findTopChildUnder((int) x, (int) y);
                if (findTopChildUnder != null && isContentView(findTopChildUnder)) {
                    float f = x - this.mInitialMotionX;
                    float f2 = y - this.mInitialMotionY;
                    int touchSlop = this.mLeftDragger.getTouchSlop();
                    if ((f * f) + (f2 * f2) < ((float) (touchSlop * touchSlop))) {
                        View findOpenDrawer = findOpenDrawer();
                        if (findOpenDrawer != null) {
                            z2 = getDrawerLockMode(findOpenDrawer) == 2 ? true : ALLOW_EDGE_LOCK;
                        }
                    }
                }
                closeDrawers(z2);
                this.mDisallowInterceptRequested = ALLOW_EDGE_LOCK;
                break;
            case 3:
                closeDrawers(true);
                this.mDisallowInterceptRequested = ALLOW_EDGE_LOCK;
                this.mChildrenCanceledTouch = ALLOW_EDGE_LOCK;
                break;
        }
        return z;
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        boolean z2 = z;
        super.requestDisallowInterceptTouchEvent(z2);
        this.mDisallowInterceptRequested = z2;
        if (z2) {
            closeDrawers(true);
        }
    }

    public void closeDrawers() {
        closeDrawers(ALLOW_EDGE_LOCK);
    }

    /* access modifiers changed from: 0000 */
    public void closeDrawers(boolean z) {
        boolean z2 = z;
        int i = 0;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (isDrawerView(childAt) && (!z2 || layoutParams.isPeeking)) {
                int width = childAt.getWidth();
                if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                    i |= this.mLeftDragger.smoothSlideViewTo(childAt, -width, childAt.getTop());
                } else {
                    i |= this.mRightDragger.smoothSlideViewTo(childAt, getWidth(), childAt.getTop());
                }
                layoutParams.isPeeking = ALLOW_EDGE_LOCK;
            }
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (i != 0) {
            invalidate();
        }
    }

    public void openDrawer(View view) {
        View view2 = view;
        if (isDrawerView(view2)) {
            boolean smoothSlideViewTo;
            if (this.mFirstLayout) {
                LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
                layoutParams.onScreen = TOUCH_SLOP_SENSITIVITY;
                layoutParams.knownOpen = true;
            } else if (checkDrawerViewAbsoluteGravity(view2, 3)) {
                smoothSlideViewTo = this.mLeftDragger.smoothSlideViewTo(view2, 0, view2.getTop());
            } else {
                smoothSlideViewTo = this.mRightDragger.smoothSlideViewTo(view2, getWidth() - view2.getWidth(), view2.getTop());
            }
            invalidate();
            return;
        }
        IllegalArgumentException illegalArgumentException = r7;
        StringBuilder stringBuilder = r7;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("View ").append(view2).append(" is not a sliding drawer").toString());
        throw illegalArgumentException;
    }

    public void openDrawer(int i) {
        int i2 = i;
        View findDrawerWithGravity = findDrawerWithGravity(i2);
        if (findDrawerWithGravity == null) {
            IllegalArgumentException illegalArgumentException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("No drawer view found with gravity ").append(gravityToString(i2)).toString());
            throw illegalArgumentException;
        }
        openDrawer(findDrawerWithGravity);
    }

    public void closeDrawer(View view) {
        View view2 = view;
        if (isDrawerView(view2)) {
            boolean smoothSlideViewTo;
            if (this.mFirstLayout) {
                LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
                layoutParams.onScreen = 0.0f;
                layoutParams.knownOpen = ALLOW_EDGE_LOCK;
            } else if (checkDrawerViewAbsoluteGravity(view2, 3)) {
                smoothSlideViewTo = this.mLeftDragger.smoothSlideViewTo(view2, -view2.getWidth(), view2.getTop());
            } else {
                smoothSlideViewTo = this.mRightDragger.smoothSlideViewTo(view2, getWidth(), view2.getTop());
            }
            invalidate();
            return;
        }
        IllegalArgumentException illegalArgumentException = r7;
        StringBuilder stringBuilder = r7;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("View ").append(view2).append(" is not a sliding drawer").toString());
        throw illegalArgumentException;
    }

    public void closeDrawer(int i) {
        int i2 = i;
        View findDrawerWithGravity = findDrawerWithGravity(i2);
        if (findDrawerWithGravity == null) {
            IllegalArgumentException illegalArgumentException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("No drawer view found with gravity ").append(gravityToString(i2)).toString());
            throw illegalArgumentException;
        }
        closeDrawer(findDrawerWithGravity);
    }

    public boolean isDrawerOpen(View view) {
        View view2 = view;
        if (isDrawerView(view2)) {
            return ((LayoutParams) view2.getLayoutParams()).knownOpen;
        }
        IllegalArgumentException illegalArgumentException = r6;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("View ").append(view2).append(" is not a drawer").toString());
        throw illegalArgumentException;
    }

    public boolean isDrawerOpen(int i) {
        View findDrawerWithGravity = findDrawerWithGravity(i);
        return findDrawerWithGravity != null ? isDrawerOpen(findDrawerWithGravity) : ALLOW_EDGE_LOCK;
    }

    public boolean isDrawerVisible(View view) {
        View view2 = view;
        if (isDrawerView(view2)) {
            return ((LayoutParams) view2.getLayoutParams()).onScreen > 0.0f ? true : ALLOW_EDGE_LOCK;
        }
        IllegalArgumentException illegalArgumentException = r6;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder();
        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("View ").append(view2).append(" is not a drawer").toString());
        throw illegalArgumentException;
    }

    public boolean isDrawerVisible(int i) {
        View findDrawerWithGravity = findDrawerWithGravity(i);
        return findDrawerWithGravity != null ? isDrawerVisible(findDrawerWithGravity) : ALLOW_EDGE_LOCK;
    }

    private boolean hasPeekingDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isPeeking) {
                return true;
            }
        }
        return ALLOW_EDGE_LOCK;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        android.view.ViewGroup.LayoutParams layoutParams = r5;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams layoutParams2;
        android.view.ViewGroup.LayoutParams layoutParams3 = layoutParams;
        android.view.ViewGroup.LayoutParams layoutParams4;
        if (layoutParams3 instanceof LayoutParams) {
            layoutParams2 = r5;
            layoutParams4 = new LayoutParams((LayoutParams) layoutParams3);
        } else if (layoutParams3 instanceof MarginLayoutParams) {
            layoutParams2 = r5;
            layoutParams4 = new LayoutParams((MarginLayoutParams) layoutParams3);
        } else {
            layoutParams2 = r5;
            layoutParams4 = new LayoutParams(layoutParams3);
        }
        return layoutParams2;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams layoutParams2 = layoutParams;
        boolean z = ((layoutParams2 instanceof LayoutParams) && super.checkLayoutParams(layoutParams2)) ? true : ALLOW_EDGE_LOCK;
        return z;
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        android.view.ViewGroup.LayoutParams layoutParams = r6;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(getContext(), attributeSet);
        return layoutParams;
    }

    private boolean hasVisibleDrawer() {
        return findVisibleDrawer() != null ? true : ALLOW_EDGE_LOCK;
    }

    private View findVisibleDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (isDrawerView(childAt) && isDrawerVisible(childAt)) {
                return childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                boolean dispatchTouchEvent = getChildAt(i).dispatchTouchEvent(obtain);
            }
            obtain.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2 = i;
        KeyEvent keyEvent2 = keyEvent;
        if (i2 != 4 || !hasVisibleDrawer()) {
            return super.onKeyDown(i2, keyEvent2);
        }
        KeyEventCompat.startTracking(keyEvent2);
        return true;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        int i2 = i;
        KeyEvent keyEvent2 = keyEvent;
        if (i2 != 4) {
            return super.onKeyUp(i2, keyEvent2);
        }
        View findVisibleDrawer = findVisibleDrawer();
        if (findVisibleDrawer != null && getDrawerLockMode(findVisibleDrawer) == 0) {
            closeDrawers();
        }
        return findVisibleDrawer != null ? true : ALLOW_EDGE_LOCK;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.openDrawerGravity != 0) {
            View findDrawerWithGravity = findDrawerWithGravity(savedState.openDrawerGravity);
            if (findDrawerWithGravity != null) {
                openDrawer(findDrawerWithGravity);
            }
        }
        setDrawerLockMode(savedState.lockModeLeft, 3);
        setDrawerLockMode(savedState.lockModeRight, 5);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = r10;
        SavedState savedState2 = new SavedState(super.onSaveInstanceState());
        SavedState savedState3 = savedState;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (isDrawerView(childAt)) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.knownOpen) {
                    savedState3.openDrawerGravity = layoutParams.gravity;
                    break;
                }
            }
        }
        savedState3.lockModeLeft = this.mLockModeLeft;
        savedState3.lockModeRight = this.mLockModeRight;
        return savedState3;
    }
}
