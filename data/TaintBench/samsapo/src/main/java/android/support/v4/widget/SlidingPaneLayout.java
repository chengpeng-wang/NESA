package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    /* access modifiers changed from: private|final */
    public final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    /* access modifiers changed from: private */
    public boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    /* access modifiers changed from: private|final */
    public final ArrayList<DisableLayerRunnable> mPostedRunnables;
    private boolean mPreservedOpenState;
    private Drawable mShadowDrawable;
    /* access modifiers changed from: private */
    public float mSlideOffset;
    /* access modifiers changed from: private */
    public int mSlideRange;
    /* access modifiers changed from: private */
    public View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;
        final /* synthetic */ SlidingPaneLayout this$0;

        AccessibilityDelegate(SlidingPaneLayout slidingPaneLayout) {
            this.this$0 = slidingPaneLayout;
            Rect rect = r5;
            Rect rect2 = new Rect();
            this.mTmpRect = rect;
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            View view2 = view;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
            AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat2);
            super.onInitializeAccessibilityNodeInfo(view2, obtain);
            copyNodeInfoNoChildren(accessibilityNodeInfoCompat2, obtain);
            obtain.recycle();
            accessibilityNodeInfoCompat2.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat2.setSource(view2);
            ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(view2);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat2.setParent((View) parentForAccessibility);
            }
            int childCount = this.this$0.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.this$0.getChildAt(i);
                if (!filter(childAt) && childAt.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(childAt, 1);
                    accessibilityNodeInfoCompat2.addChild(childAt);
                }
            }
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            super.onInitializeAccessibilityEvent(view, accessibilityEvent2);
            accessibilityEvent2.setClassName(SlidingPaneLayout.class.getName());
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            ViewGroup viewGroup2 = viewGroup;
            View view2 = view;
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            if (filter(view2)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(viewGroup2, view2, accessibilityEvent2);
        }

        public boolean filter(View view) {
            return this.this$0.isDimmed(view);
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
            accessibilityNodeInfoCompat3.setMovementGranularities(accessibilityNodeInfoCompat4.getMovementGranularities());
        }
    }

    private class DisableLayerRunnable implements Runnable {
        final View mChildView;
        final /* synthetic */ SlidingPaneLayout this$0;

        DisableLayerRunnable(SlidingPaneLayout slidingPaneLayout, View view) {
            View view2 = view;
            this.this$0 = slidingPaneLayout;
            this.mChildView = view2;
        }

        public void run() {
            if (this.mChildView.getParent() == this.this$0) {
                ViewCompat.setLayerType(this.mChildView, 0, null);
                this.this$0.invalidateChildRegion(this.mChildView);
            }
            boolean remove = this.this$0.mPostedRunnables.remove(this);
        }
    }

    private class DragHelperCallback extends Callback {
        final /* synthetic */ SlidingPaneLayout this$0;

        private DragHelperCallback(SlidingPaneLayout slidingPaneLayout) {
            this.this$0 = slidingPaneLayout;
        }

        /* synthetic */ DragHelperCallback(SlidingPaneLayout slidingPaneLayout, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(slidingPaneLayout);
        }

        public boolean tryCaptureView(View view, int i) {
            View view2 = view;
            int i2 = i;
            if (this.this$0.mIsUnableToDrag) {
                return false;
            }
            return ((LayoutParams) view2.getLayoutParams()).slideable;
        }

        public void onViewDragStateChanged(int i) {
            int i2 = i;
            if (this.this$0.mDragHelper.getViewDragState() != 0) {
                return;
            }
            boolean access$502;
            if (this.this$0.mSlideOffset == 0.0f) {
                this.this$0.updateObscuredViewsVisibility(this.this$0.mSlideableView);
                this.this$0.dispatchOnPanelClosed(this.this$0.mSlideableView);
                access$502 = SlidingPaneLayout.access$502(this.this$0, false);
                return;
            }
            this.this$0.dispatchOnPanelOpened(this.this$0.mSlideableView);
            access$502 = SlidingPaneLayout.access$502(this.this$0, true);
        }

        public void onViewCaptured(View view, int i) {
            View view2 = view;
            int i2 = i;
            this.this$0.setAllChildrenVisible();
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            View view2 = view;
            int i5 = i2;
            int i6 = i3;
            int i7 = i4;
            this.this$0.onPanelDragged(i);
            this.this$0.invalidate();
        }

        public void onViewReleased(View view, float f, float f2) {
            View view2 = view;
            float f3 = f;
            float f4 = f2;
            int paddingLeft = this.this$0.getPaddingLeft() + ((LayoutParams) view2.getLayoutParams()).leftMargin;
            if (f3 > 0.0f || (f3 == 0.0f && this.this$0.mSlideOffset > 0.5f)) {
                paddingLeft += this.this$0.mSlideRange;
            }
            boolean z = this.this$0.mDragHelper.settleCapturedViewAt(paddingLeft, view2.getTop());
            this.this$0.invalidate();
        }

        public int getViewHorizontalDragRange(View view) {
            View view2 = view;
            return this.this$0.mSlideRange;
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            View view2 = view;
            int i3 = i2;
            int paddingLeft = this.this$0.getPaddingLeft() + ((LayoutParams) this.this$0.mSlideableView.getLayoutParams()).leftMargin;
            return Math.min(Math.max(i, paddingLeft), paddingLeft + this.this$0.mSlideRange);
        }

        public void onEdgeDragStarted(int i, int i2) {
            int i3 = i;
            this.this$0.mDragHelper.captureChildView(this.this$0.mSlideableView, i2);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private static final int[] ATTRS;
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight = 0.0f;

        static {
            int[] iArr = new int[1];
            int[] iArr2 = iArr;
            iArr[0] = 16843137;
            ATTRS = iArr2;
        }

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            MarginLayoutParams marginLayoutParams = layoutParams;
            super(marginLayoutParams);
            this.weight = marginLayoutParams.weight;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            Context context2 = context;
            AttributeSet attributeSet2 = attributeSet;
            super(context2, attributeSet2);
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, ATTRS);
            this.weight = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }
    }

    public interface PanelSlideListener {
        void onPanelClosed(View view);

        void onPanelOpened(View view);

        void onPanelSlide(View view, float f);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        boolean isOpen;

        /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            Parcel parcel2 = parcel;
            super(parcel2);
            this.isOpen = parcel2.readInt() != 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            Parcel parcel2 = parcel;
            super.writeToParcel(parcel2, i);
            parcel2.writeInt(this.isOpen ? 1 : 0);
        }

        static {
            AnonymousClass1 anonymousClass1 = r2;
            AnonymousClass1 anonymousClass12 = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel parcel) {
                    SavedState savedState = r6;
                    SavedState savedState2 = new SavedState(parcel, null);
                    return savedState;
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            CREATOR = anonymousClass1;
        }
    }

    public static class SimplePanelSlideListener implements PanelSlideListener {
        public SimplePanelSlideListener() {
        }

        public void onPanelSlide(View view, float f) {
        }

        public void onPanelOpened(View view) {
        }

        public void onPanelClosed(View view) {
        }
    }

    interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view);
    }

    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
        SlidingPanelLayoutImplBase() {
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            View view2 = view;
            ViewCompat.postInvalidateOnAnimation(slidingPaneLayout, view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            int e;
            try {
                this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
            } catch (NoSuchMethodException e2) {
                e = Log.e(SlidingPaneLayout.TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e2);
            }
            try {
                this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                this.mRecreateDisplayList.setAccessible(true);
            } catch (NoSuchFieldException e22) {
                e = Log.e(SlidingPaneLayout.TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e22);
            }
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            SlidingPaneLayout slidingPaneLayout2 = slidingPaneLayout;
            View view2 = view;
            if (this.mGetDisplayList == null || this.mRecreateDisplayList == null) {
                view2.invalidate();
                return;
            }
            try {
                this.mRecreateDisplayList.setBoolean(view2, true);
                Object invoke = this.mGetDisplayList.invoke(view2, (Object[]) null);
            } catch (Exception e) {
                int e2 = Log.e(SlidingPaneLayout.TAG, "Error refreshing display list state", e);
            }
            super.invalidateChildRegion(slidingPaneLayout2, view2);
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
        SlidingPanelLayoutImplJBMR1() {
        }

        public void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view) {
            SlidingPaneLayout slidingPaneLayout2 = slidingPaneLayout;
            View view2 = view;
            ViewCompat.setLayerPaint(view2, ((LayoutParams) view2.getLayoutParams()).dimPaint);
        }
    }

    static /* synthetic */ boolean access$502(SlidingPaneLayout slidingPaneLayout, boolean z) {
        boolean z2 = z;
        boolean z3 = z2;
        boolean z4 = z2;
        slidingPaneLayout.mPreservedOpenState = z4;
        return z3;
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 17) {
            SlidingPanelLayoutImplJBMR1 slidingPanelLayoutImplJBMR1 = r3;
            SlidingPanelLayoutImplJBMR1 slidingPanelLayoutImplJBMR12 = new SlidingPanelLayoutImplJBMR1();
            IMPL = slidingPanelLayoutImplJBMR1;
        } else if (i >= 16) {
            SlidingPanelLayoutImplJB slidingPanelLayoutImplJB = r3;
            SlidingPanelLayoutImplJB slidingPanelLayoutImplJB2 = new SlidingPanelLayoutImplJB();
            IMPL = slidingPanelLayoutImplJB;
        } else {
            SlidingPanelLayoutImplBase slidingPanelLayoutImplBase = r3;
            SlidingPanelLayoutImplBase slidingPanelLayoutImplBase2 = new SlidingPanelLayoutImplBase();
            IMPL = slidingPanelLayoutImplBase;
        }
    }

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet, int i) {
        Context context2 = context;
        super(context2, attributeSet, i);
        this.mSliderFadeColor = DEFAULT_FADE_COLOR;
        this.mFirstLayout = true;
        Rect rect = r13;
        Rect rect2 = new Rect();
        this.mTmpRect = rect;
        ArrayList arrayList = r13;
        ArrayList arrayList2 = new ArrayList();
        this.mPostedRunnables = arrayList;
        float f = context2.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int) ((32.0f * f) + 0.5f);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context2);
        setWillNotDraw(false);
        AccessibilityDelegateCompat accessibilityDelegateCompat = r13;
        AccessibilityDelegateCompat accessibilityDelegate = new AccessibilityDelegate(this);
        ViewCompat.setAccessibilityDelegate(this, accessibilityDelegateCompat);
        ViewCompat.setImportantForAccessibility(this, 1);
        Callback callback = r13;
        Callback dragHelperCallback = new DragHelperCallback(this, null);
        this.mDragHelper = ViewDragHelper.create(this, 0.5f, callback);
        this.mDragHelper.setEdgeTrackingEnabled(1);
        this.mDragHelper.setMinVelocity(400.0f * f);
    }

    public void setParallaxDistance(int i) {
        this.mParallaxBy = i;
        requestLayout();
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public void setSliderFadeColor(int i) {
        this.mSliderFadeColor = i;
    }

    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    public void setCoveredFadeColor(int i) {
        this.mCoveredFadeColor = i;
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public void setPanelSlideListener(PanelSlideListener panelSlideListener) {
        this.mPanelSlideListener = panelSlideListener;
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnPanelSlide(View view) {
        View view2 = view;
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelSlide(view2, this.mSlideOffset);
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnPanelOpened(View view) {
        View view2 = view;
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelOpened(view2);
        }
        sendAccessibilityEvent(32);
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnPanelClosed(View view) {
        View view2 = view;
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelClosed(view2);
        }
        sendAccessibilityEvent(32);
    }

    /* access modifiers changed from: 0000 */
    public void updateObscuredViewsVisibility(View view) {
        int i;
        View view2 = view;
        int paddingLeft = getPaddingLeft();
        int width = getWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int height = getHeight() - getPaddingBottom();
        int i2;
        int i3;
        int i4;
        if (view2 == null || !viewIsOpaque(view2)) {
            int i5 = 0;
            i2 = i5;
            i5 = i5;
            i3 = i5;
            i5 = i5;
            i4 = i5;
            i = i5;
        } else {
            i = view2.getLeft();
            i4 = view2.getRight();
            i3 = view2.getTop();
            i2 = view2.getBottom();
        }
        int i6 = 0;
        int childCount = getChildCount();
        while (i6 < childCount) {
            View childAt = getChildAt(i6);
            if (childAt != view2) {
                int i7;
                int max = Math.max(paddingLeft, childAt.getLeft());
                int max2 = Math.max(paddingTop, childAt.getTop());
                int min = Math.min(width, childAt.getRight());
                int min2 = Math.min(height, childAt.getBottom());
                if (max < i || max2 < i3 || min > i4 || min2 > i2) {
                    i7 = 0;
                } else {
                    i7 = 4;
                }
                childAt.setVisibility(i7);
                i6++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 4) {
                childAt.setVisibility(0);
            }
        }
    }

    private static boolean viewIsOpaque(View view) {
        View view2 = view;
        if (ViewCompat.isOpaque(view2)) {
            return true;
        }
        if (VERSION.SDK_INT >= 18) {
            return false;
        }
        Drawable background = view2.getBackground();
        if (background == null) {
            return false;
        }
        return background.getOpacity() == -1;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        int size = this.mPostedRunnables.size();
        for (int i = 0; i < size; i++) {
            ((DisableLayerRunnable) this.mPostedRunnables.get(i)).run();
        }
        this.mPostedRunnables.clear();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int e;
        int i3;
        int makeMeasureSpec;
        int measuredWidth;
        int i4 = i;
        int i5 = i2;
        int mode = MeasureSpec.getMode(i4);
        int size = MeasureSpec.getSize(i4);
        int mode2 = MeasureSpec.getMode(i5);
        int size2 = MeasureSpec.getSize(i5);
        IllegalStateException illegalStateException;
        IllegalStateException illegalStateException2;
        if (mode != 1073741824) {
            if (!isInEditMode()) {
                illegalStateException = r29;
                illegalStateException2 = new IllegalStateException("Width must have an exact value or MATCH_PARENT");
                throw illegalStateException;
            } else if (mode == Integer.MIN_VALUE) {
                mode = 1073741824;
            } else if (mode == 0) {
                mode = 1073741824;
                size = 300;
            }
        } else if (mode2 == 0) {
            if (!isInEditMode()) {
                illegalStateException = r29;
                illegalStateException2 = new IllegalStateException("Height must not be UNSPECIFIED");
                throw illegalStateException;
            } else if (mode2 == 0) {
                mode2 = Integer.MIN_VALUE;
                size2 = 300;
            }
        }
        int i6 = 0;
        int i7 = -1;
        switch (mode2) {
            case ExploreByTouchHelper.INVALID_ID /*-2147483648*/:
                i7 = (size2 - getPaddingTop()) - getPaddingBottom();
                break;
            case 1073741824:
                int paddingTop = (size2 - getPaddingTop()) - getPaddingBottom();
                i7 = paddingTop;
                i6 = paddingTop;
                break;
        }
        float f = 0.0f;
        boolean z = false;
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int childCount = getChildCount();
        if (childCount > 2) {
            e = Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        for (i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() == 8) {
                layoutParams.dimWhenOffset = false;
            } else {
                int makeMeasureSpec2;
                if (layoutParams.weight > 0.0f) {
                    f += layoutParams.weight;
                    if (layoutParams.width == 0) {
                    }
                }
                int i8 = layoutParams.leftMargin + layoutParams.rightMargin;
                if (layoutParams.width == -2) {
                    makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(size - i8, ExploreByTouchHelper.INVALID_ID);
                } else if (layoutParams.width == -1) {
                    makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(size - i8, 1073741824);
                } else {
                    makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                }
                if (layoutParams.height == -2) {
                    makeMeasureSpec = MeasureSpec.makeMeasureSpec(i7, ExploreByTouchHelper.INVALID_ID);
                } else if (layoutParams.height == -1) {
                    makeMeasureSpec = MeasureSpec.makeMeasureSpec(i7, 1073741824);
                } else {
                    makeMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                }
                childAt.measure(makeMeasureSpec2, makeMeasureSpec);
                measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (mode2 == Integer.MIN_VALUE && measuredHeight > i6) {
                    i6 = Math.min(measuredHeight, i7);
                }
                paddingLeft -= measuredWidth;
                boolean z2 = z;
                LayoutParams layoutParams2 = layoutParams;
                boolean z3 = paddingLeft < 0;
                boolean z4 = z3;
                layoutParams2.slideable = z3;
                z = z2 | z4;
                if (layoutParams.slideable) {
                    this.mSlideableView = childAt;
                }
            }
        }
        if (z || f > 0.0f) {
            i3 = size - this.mOverhangSize;
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt2 = getChildAt(i9);
                if (childAt2.getVisibility() != 8) {
                    LayoutParams layoutParams3 = (LayoutParams) childAt2.getLayoutParams();
                    if (childAt2.getVisibility() != 8) {
                        Object obj = (layoutParams3.width != 0 || layoutParams3.weight <= 0.0f) ? null : 1;
                        Object obj2 = obj;
                        if (obj2 != null) {
                            e = 0;
                        } else {
                            e = childAt2.getMeasuredWidth();
                        }
                        makeMeasureSpec = e;
                        if (!z || childAt2 == this.mSlideableView) {
                            if (layoutParams3.weight > 0.0f) {
                                if (layoutParams3.width != 0) {
                                    measuredWidth = MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824);
                                } else if (layoutParams3.height == -2) {
                                    measuredWidth = MeasureSpec.makeMeasureSpec(i7, ExploreByTouchHelper.INVALID_ID);
                                } else if (layoutParams3.height == -1) {
                                    measuredWidth = MeasureSpec.makeMeasureSpec(i7, 1073741824);
                                } else {
                                    measuredWidth = MeasureSpec.makeMeasureSpec(layoutParams3.height, 1073741824);
                                }
                                if (z) {
                                    int i10 = size - (layoutParams3.leftMargin + layoutParams3.rightMargin);
                                    int makeMeasureSpec3 = MeasureSpec.makeMeasureSpec(i10, 1073741824);
                                    if (makeMeasureSpec != i10) {
                                        childAt2.measure(makeMeasureSpec3, measuredWidth);
                                    }
                                } else {
                                    childAt2.measure(MeasureSpec.makeMeasureSpec(makeMeasureSpec + ((int) ((layoutParams3.weight * ((float) Math.max(0, paddingLeft))) / f)), 1073741824), measuredWidth);
                                }
                            }
                        } else if (layoutParams3.width < 0 && (makeMeasureSpec > i3 || layoutParams3.weight > 0.0f)) {
                            if (obj2 == null) {
                                measuredWidth = MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824);
                            } else if (layoutParams3.height == -2) {
                                measuredWidth = MeasureSpec.makeMeasureSpec(i7, ExploreByTouchHelper.INVALID_ID);
                            } else if (layoutParams3.height == -1) {
                                measuredWidth = MeasureSpec.makeMeasureSpec(i7, 1073741824);
                            } else {
                                measuredWidth = MeasureSpec.makeMeasureSpec(layoutParams3.height, 1073741824);
                            }
                            childAt2.measure(MeasureSpec.makeMeasureSpec(i3, 1073741824), measuredWidth);
                        }
                    }
                }
            }
        }
        setMeasuredDimension(size, i6);
        this.mCanSlide = z;
        if (this.mDragHelper.getViewDragState() != 0 && !z) {
            this.mDragHelper.abort();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        boolean z2 = z;
        int i6 = i2;
        int i7 = i4;
        int i8 = i3 - i;
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int i9 = paddingLeft;
        int i10 = i9;
        if (this.mFirstLayout) {
            float f = (this.mCanSlide && this.mPreservedOpenState) ? 1.0f : 0.0f;
            this.mSlideOffset = f;
        }
        for (i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int i11 = 0;
                if (layoutParams.slideable) {
                    int min = (Math.min(i10, (i8 - paddingRight) - this.mOverhangSize) - i9) - (layoutParams.leftMargin + layoutParams.rightMargin);
                    this.mSlideRange = min;
                    layoutParams.dimWhenOffset = ((i9 + layoutParams.leftMargin) + min) + (measuredWidth / 2) > i8 - paddingRight;
                    i9 += ((int) (((float) min) * this.mSlideOffset)) + layoutParams.leftMargin;
                } else if (!this.mCanSlide || this.mParallaxBy == 0) {
                    i9 = i10;
                } else {
                    i11 = (int) ((1.0f - this.mSlideOffset) * ((float) this.mParallaxBy));
                    i9 = i10;
                }
                int i12 = i9 - i11;
                childAt.layout(i12, paddingTop, i12 + measuredWidth, paddingTop + childAt.getMeasuredHeight());
                i10 += childAt.getWidth();
            }
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams) this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            } else {
                for (i5 = 0; i5 < childCount; i5++) {
                    dimChildView(getChildAt(i5), 0.0f, this.mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i3;
        super.onSizeChanged(i5, i2, i6, i4);
        if (i5 != i6) {
            this.mFirstLayout = true;
        }
    }

    public void requestChildFocus(View view, View view2) {
        View view3 = view;
        super.requestChildFocus(view3, view2);
        if (!isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = view3 == this.mSlideableView;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent2);
        if (!this.mCanSlide && actionMasked == 0 && getChildCount() > 1) {
            View childAt = getChildAt(1);
            if (childAt != null) {
                this.mPreservedOpenState = !this.mDragHelper.isViewUnder(childAt, (int) motionEvent2.getX(), (int) motionEvent2.getY());
            }
        }
        if (!this.mCanSlide || (this.mIsUnableToDrag && actionMasked != 0)) {
            this.mDragHelper.cancel();
            return super.onInterceptTouchEvent(motionEvent2);
        } else if (actionMasked == 3 || actionMasked == 1) {
            this.mDragHelper.cancel();
            return false;
        } else {
            boolean z;
            Object obj = null;
            float x;
            float y;
            switch (actionMasked) {
                case 0:
                    this.mIsUnableToDrag = false;
                    x = motionEvent2.getX();
                    y = motionEvent2.getY();
                    this.mInitialMotionX = x;
                    this.mInitialMotionY = y;
                    if (this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y) && isDimmed(this.mSlideableView)) {
                        int obj2 = 1;
                        break;
                    }
                case 2:
                    x = motionEvent2.getX();
                    y = motionEvent2.getY();
                    float abs = Math.abs(x - this.mInitialMotionX);
                    float abs2 = Math.abs(y - this.mInitialMotionY);
                    if (abs > ((float) this.mDragHelper.getTouchSlop()) && abs2 > abs) {
                        this.mDragHelper.cancel();
                        this.mIsUnableToDrag = true;
                        return false;
                    }
            }
            if (this.mDragHelper.shouldInterceptTouchEvent(motionEvent2) || obj2 != null) {
                z = true;
            } else {
                z = false;
            }
            return z;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        if (!this.mCanSlide) {
            return super.onTouchEvent(motionEvent2);
        }
        this.mDragHelper.processTouchEvent(motionEvent2);
        boolean z = true;
        float x;
        float y;
        switch (motionEvent2.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                x = motionEvent2.getX();
                y = motionEvent2.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                break;
            case 1:
                if (isDimmed(this.mSlideableView)) {
                    x = motionEvent2.getX();
                    y = motionEvent2.getY();
                    float f = x - this.mInitialMotionX;
                    float f2 = y - this.mInitialMotionY;
                    int touchSlop = this.mDragHelper.getTouchSlop();
                    if ((f * f) + (f2 * f2) < ((float) (touchSlop * touchSlop)) && this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y)) {
                        boolean closePane = closePane(this.mSlideableView, 0);
                        break;
                    }
                }
                break;
        }
        return z;
    }

    private boolean closePane(View view, int i) {
        View view2 = view;
        int i2 = i;
        if (!this.mFirstLayout && !smoothSlideTo(0.0f, i2)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private boolean openPane(View view, int i) {
        View view2 = view;
        int i2 = i;
        if (!this.mFirstLayout && !smoothSlideTo(1.0f, i2)) {
            return false;
        }
        this.mPreservedOpenState = true;
        return true;
    }

    @Deprecated
    public void smoothSlideOpen() {
        boolean openPane = openPane();
    }

    public boolean openPane() {
        return openPane(this.mSlideableView, 0);
    }

    @Deprecated
    public void smoothSlideClosed() {
        boolean closePane = closePane();
    }

    public boolean closePane() {
        return closePane(this.mSlideableView, 0);
    }

    public boolean isOpen() {
        boolean z = !this.mCanSlide || this.mSlideOffset == 1.0f;
        return z;
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    /* access modifiers changed from: private */
    public void onPanelDragged(int i) {
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        this.mSlideOffset = ((float) (i - (getPaddingLeft() + layoutParams.leftMargin))) / ((float) this.mSlideRange);
        if (this.mParallaxBy != 0) {
            parallaxOtherViews(this.mSlideOffset);
        }
        if (layoutParams.dimWhenOffset) {
            dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        dispatchOnPanelSlide(this.mSlideableView);
    }

    private void dimChildView(View view, float f, int i) {
        View view2 = view;
        float f2 = f;
        int i2 = i;
        LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
        ColorFilter colorFilter;
        if (f2 > 0.0f && i2 != 0) {
            int i3 = (((int) (((float) ((i2 & ViewCompat.MEASURED_STATE_MASK) >>> 24)) * f2)) << 24) | (i2 & ViewCompat.MEASURED_SIZE_MASK);
            if (layoutParams.dimPaint == null) {
                LayoutParams layoutParams2 = layoutParams;
                Paint paint = r13;
                Paint paint2 = new Paint();
                layoutParams2.dimPaint = paint;
            }
            Paint paint3 = layoutParams.dimPaint;
            ColorFilter colorFilter2 = r13;
            ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(i3, Mode.SRC_OVER);
            colorFilter = paint3.setColorFilter(colorFilter2);
            if (ViewCompat.getLayerType(view2) != 2) {
                ViewCompat.setLayerType(view2, 2, layoutParams.dimPaint);
            }
            invalidateChildRegion(view2);
        } else if (ViewCompat.getLayerType(view2) != 0) {
            if (layoutParams.dimPaint != null) {
                colorFilter = layoutParams.dimPaint.setColorFilter(null);
            }
            DisableLayerRunnable disableLayerRunnable = r13;
            DisableLayerRunnable disableLayerRunnable2 = new DisableLayerRunnable(this, view2);
            DisableLayerRunnable disableLayerRunnable3 = disableLayerRunnable;
            boolean add = this.mPostedRunnables.add(disableLayerRunnable3);
            ViewCompat.postOnAnimation(this, disableLayerRunnable3);
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild;
        Canvas canvas2 = canvas;
        View view2 = view;
        long j2 = j;
        LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
        int save = canvas2.save(2);
        if (!(!this.mCanSlide || layoutParams.slideable || this.mSlideableView == null)) {
            boolean clipBounds = canvas2.getClipBounds(this.mTmpRect);
            this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
            clipBounds = canvas2.clipRect(this.mTmpRect);
        }
        if (VERSION.SDK_INT >= 11) {
            drawChild = super.drawChild(canvas2, view2, j2);
        } else if (!layoutParams.dimWhenOffset || this.mSlideOffset <= 0.0f) {
            if (view2.isDrawingCacheEnabled()) {
                view2.setDrawingCacheEnabled(false);
            }
            drawChild = super.drawChild(canvas2, view2, j2);
        } else {
            if (!view2.isDrawingCacheEnabled()) {
                view2.setDrawingCacheEnabled(true);
            }
            Bitmap drawingCache = view2.getDrawingCache();
            if (drawingCache != null) {
                canvas2.drawBitmap(drawingCache, (float) view2.getLeft(), (float) view2.getTop(), layoutParams.dimPaint);
                drawChild = false;
            } else {
                String str = TAG;
                StringBuilder stringBuilder = r14;
                StringBuilder stringBuilder2 = new StringBuilder();
                int e = Log.e(str, stringBuilder.append("drawChild: child view ").append(view2).append(" returned null drawing cache").toString());
                drawChild = super.drawChild(canvas2, view2, j2);
            }
        }
        canvas2.restoreToCount(save);
        return drawChild;
    }

    /* access modifiers changed from: private */
    public void invalidateChildRegion(View view) {
        IMPL.invalidateChildRegion(this, view);
    }

    /* access modifiers changed from: 0000 */
    public boolean smoothSlideTo(float f, int i) {
        float f2 = f;
        int i2 = i;
        if (!this.mCanSlide) {
            return false;
        }
        if (!this.mDragHelper.smoothSlideViewTo(this.mSlideableView, (int) (((float) (getPaddingLeft() + ((LayoutParams) this.mSlideableView.getLayoutParams()).leftMargin)) + (f2 * ((float) this.mSlideRange))), this.mSlideableView.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    public void computeScroll() {
        if (!this.mDragHelper.continueSettling(true)) {
            return;
        }
        if (this.mCanSlide) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.mDragHelper.abort();
        }
    }

    public void setShadowDrawable(Drawable drawable) {
        this.mShadowDrawable = drawable;
    }

    public void setShadowResource(int i) {
        setShadowDrawable(getResources().getDrawable(i));
    }

    public void draw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.draw(canvas2);
        View childAt = getChildCount() > 1 ? getChildAt(1) : null;
        if (childAt != null && this.mShadowDrawable != null) {
            int intrinsicWidth = this.mShadowDrawable.getIntrinsicWidth();
            int left = childAt.getLeft();
            this.mShadowDrawable.setBounds(left - intrinsicWidth, childAt.getTop(), left, childAt.getBottom());
            this.mShadowDrawable.draw(canvas2);
        }
    }

    private void parallaxOtherViews(float f) {
        float f2 = f;
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        Object obj = (!layoutParams.dimWhenOffset || layoutParams.leftMargin > 0) ? null : 1;
        Object obj2 = obj;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != this.mSlideableView) {
                int i2 = (int) ((1.0f - this.mParallaxOffset) * ((float) this.mParallaxBy));
                this.mParallaxOffset = f2;
                childAt.offsetLeftAndRight(i2 - ((int) ((1.0f - f2) * ((float) this.mParallaxBy))));
                if (obj2 != null) {
                    dimChildView(childAt, 1.0f - this.mParallaxOffset, this.mCoveredFadeColor);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        View view2 = view;
        boolean z2 = z;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int scrollX = view2.getScrollX();
            int scrollY = view2.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (i5 + scrollX >= childAt.getLeft() && i5 + scrollX < childAt.getRight() && i6 + scrollY >= childAt.getTop() && i6 + scrollY < childAt.getBottom() && canScroll(childAt, true, i4, (i5 + scrollX) - childAt.getLeft(), (i6 + scrollY) - childAt.getTop())) {
                    return true;
                }
            }
        }
        boolean z3 = z2 && ViewCompat.canScrollHorizontally(view2, -i4);
        return z3;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDimmed(View view) {
        View view2 = view;
        if (view2 == null) {
            return false;
        }
        boolean z = this.mCanSlide && ((LayoutParams) view2.getLayoutParams()).dimWhenOffset && this.mSlideOffset > 0.0f;
        return z;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        android.view.ViewGroup.LayoutParams layoutParams = r3;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams();
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams layoutParams2;
        android.view.ViewGroup.LayoutParams layoutParams3 = layoutParams;
        android.view.ViewGroup.LayoutParams layoutParams4;
        if (layoutParams3 instanceof MarginLayoutParams) {
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
        boolean z = (layoutParams2 instanceof LayoutParams) && super.checkLayoutParams(layoutParams2);
        return z;
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        android.view.ViewGroup.LayoutParams layoutParams = r6;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(getContext(), attributeSet);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = r6;
        SavedState savedState2 = new SavedState(super.onSaveInstanceState());
        SavedState savedState3 = savedState;
        savedState3.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        return savedState3;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        boolean openPane;
        if (savedState.isOpen) {
            openPane = openPane();
        } else {
            openPane = closePane();
        }
        this.mPreservedOpenState = savedState.isOpen;
    }
}
