package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    /* access modifiers changed from: private|static|final */
    public static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId = -1;
    /* access modifiers changed from: private */
    public PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    /* access modifiers changed from: private */
    public int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = DEBUG;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            Context context2 = context;
            AttributeSet attributeSet2 = attributeSet;
            super(context2, attributeSet2);
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        final /* synthetic */ ViewPager this$0;

        MyAccessibilityDelegate(ViewPager viewPager) {
            this.this$0 = viewPager;
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
            super.onInitializeAccessibilityEvent(view, accessibilityEvent2);
            accessibilityEvent2.setClassName(ViewPager.class.getName());
            AccessibilityRecordCompat obtain = AccessibilityRecordCompat.obtain();
            obtain.setScrollable(canScroll());
            if (accessibilityEvent2.getEventType() == 4096 && this.this$0.mAdapter != null) {
                obtain.setItemCount(this.this$0.mAdapter.getCount());
                obtain.setFromIndex(this.this$0.mCurItem);
                obtain.setToIndex(this.this$0.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
            accessibilityNodeInfoCompat2.setClassName(ViewPager.class.getName());
            accessibilityNodeInfoCompat2.setScrollable(canScroll());
            if (this.this$0.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat2.addAction(4096);
            }
            if (this.this$0.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat2.addAction(8192);
            }
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            int i2 = i;
            if (super.performAccessibilityAction(view, i2, bundle)) {
                return true;
            }
            switch (i2) {
                case 4096:
                    if (!this.this$0.canScrollHorizontally(1)) {
                        return ViewPager.DEBUG;
                    }
                    this.this$0.setCurrentItem(this.this$0.mCurItem + 1);
                    return true;
                case 8192:
                    if (!this.this$0.canScrollHorizontally(-1)) {
                        return ViewPager.DEBUG;
                    }
                    this.this$0.setCurrentItem(this.this$0.mCurItem - 1);
                    return true;
                default:
                    return ViewPager.DEBUG;
            }
        }

        private boolean canScroll() {
            boolean z = (this.this$0.mAdapter == null || this.this$0.mAdapter.getCount() <= 1) ? ViewPager.DEBUG : true;
            return z;
        }
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        final /* synthetic */ ViewPager this$0;

        private PagerObserver(ViewPager viewPager) {
            this.this$0 = viewPager;
        }

        /* synthetic */ PagerObserver(ViewPager viewPager, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(viewPager);
        }

        public void onChanged() {
            this.this$0.dataSetChanged();
        }

        public void onInvalidated() {
            this.this$0.dataSetChanged();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            Parcel parcel2 = parcel;
            int i2 = i;
            super.writeToParcel(parcel2, i2);
            parcel2.writeInt(this.position);
            parcel2.writeParcelable(this.adapterState, i2);
        }

        public String toString() {
            StringBuilder stringBuilder = r3;
            StringBuilder stringBuilder2 = new StringBuilder();
            return stringBuilder.append("FragmentPager.SavedState{").append(Integer.toHexString(System.identityHashCode(this))).append(" position=").append(this.position).append("}").toString();
        }

        static {
            AnonymousClass1 anonymousClass1 = r2;
            AnonymousClass1 anonymousClass12 = new ParcelableCompatCreatorCallbacks<SavedState>() {
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    SavedState savedState = r7;
                    SavedState savedState2 = new SavedState(parcel, classLoader);
                    return savedState;
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            CREATOR = ParcelableCompat.newCreator(anonymousClass1);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            Parcel parcel2 = parcel;
            ClassLoader classLoader2 = classLoader;
            super(parcel2);
            if (classLoader2 == null) {
                classLoader2 = getClass().getClassLoader();
            }
            this.position = parcel2.readInt();
            this.adapterState = parcel2.readParcelable(classLoader2);
            this.loader = classLoader2;
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public SimpleOnPageChangeListener() {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }

        public void onPageScrollStateChanged(int i) {
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View view, View view2) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
            if (layoutParams.isDecor == layoutParams2.isDecor) {
                return layoutParams.position - layoutParams2.position;
            }
            return layoutParams.isDecor ? 1 : -1;
        }
    }

    static {
        int[] iArr = new int[1];
        int[] iArr2 = iArr;
        iArr[0] = 16842931;
        LAYOUT_ATTRS = iArr2;
        AnonymousClass1 anonymousClass1 = r4;
        AnonymousClass1 anonymousClass12 = new Comparator<ItemInfo>() {
            public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        COMPARATOR = anonymousClass1;
        AnonymousClass2 anonymousClass2 = r4;
        AnonymousClass2 anonymousClass22 = new Interpolator() {
            public float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return ((((f2 * f2) * f2) * f2) * f2) + 1.0f;
            }
        };
        sInterpolator = anonymousClass2;
        ViewPositionComparator viewPositionComparator = r4;
        ViewPositionComparator viewPositionComparator2 = new ViewPositionComparator();
        sPositionComparator = viewPositionComparator;
    }

    public ViewPager(Context context) {
        super(context);
        ArrayList arrayList = r6;
        ArrayList arrayList2 = new ArrayList();
        this.mItems = arrayList;
        ItemInfo itemInfo = r6;
        ItemInfo itemInfo2 = new ItemInfo();
        this.mTempItem = itemInfo;
        Rect rect = r6;
        Rect rect2 = new Rect();
        this.mTempRect = rect;
        AnonymousClass3 anonymousClass3 = r6;
        AnonymousClass3 anonymousClass32 = new Runnable(this) {
            final /* synthetic */ ViewPager this$0;

            {
                this.this$0 = r5;
            }

            public void run() {
                this.this$0.setScrollState(0);
                this.this$0.populate();
            }
        };
        this.mEndScrollRunnable = anonymousClass3;
        this.mScrollState = 0;
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ArrayList arrayList = r7;
        ArrayList arrayList2 = new ArrayList();
        this.mItems = arrayList;
        ItemInfo itemInfo = r7;
        ItemInfo itemInfo2 = new ItemInfo();
        this.mTempItem = itemInfo;
        Rect rect = r7;
        Rect rect2 = new Rect();
        this.mTempRect = rect;
        AnonymousClass3 anonymousClass3 = r7;
        AnonymousClass3 anonymousClass32 = /* anonymous class already generated */;
        this.mEndScrollRunnable = anonymousClass3;
        this.mScrollState = 0;
        initViewPager();
    }

    /* access modifiers changed from: 0000 */
    public void initViewPager() {
        setWillNotDraw(DEBUG);
        setDescendantFocusability(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START);
        setFocusable(true);
        Context context = getContext();
        Scroller scroller = r9;
        Scroller scroller2 = new Scroller(context, sInterpolator);
        this.mScroller = scroller;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
        this.mMinimumVelocity = (int) (400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        EdgeEffectCompat edgeEffectCompat = r9;
        EdgeEffectCompat edgeEffectCompat2 = new EdgeEffectCompat(context);
        this.mLeftEdge = edgeEffectCompat;
        edgeEffectCompat = r9;
        edgeEffectCompat2 = new EdgeEffectCompat(context);
        this.mRightEdge = edgeEffectCompat;
        this.mFlingDistance = (int) (25.0f * f);
        this.mCloseEnough = (int) (2.0f * f);
        this.mDefaultGutterSize = (int) (16.0f * f);
        AccessibilityDelegateCompat accessibilityDelegateCompat = r9;
        AccessibilityDelegateCompat myAccessibilityDelegate = new MyAccessibilityDelegate(this);
        ViewCompat.setAccessibilityDelegate(this, accessibilityDelegateCompat);
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        boolean removeCallbacks = removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: private */
    public void setScrollState(int i) {
        int i2 = i;
        if (this.mScrollState != i2) {
            this.mScrollState = i2;
            if (this.mPageTransformer != null) {
                enableLayers(i2 != 0 ? true : DEBUG);
            }
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(i2);
            }
        }
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        PagerAdapter pagerAdapter2 = pagerAdapter;
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo itemInfo = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter pagerAdapter3 = this.mAdapter;
        this.mAdapter = pagerAdapter2;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                PagerObserver pagerObserver = r9;
                PagerObserver pagerObserver2 = new PagerObserver(this, null);
                this.mObserver = pagerObserver;
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = DEBUG;
            boolean z = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, DEBUG, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (z) {
                requestLayout();
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListener != null && pagerAdapter3 != pagerAdapter2) {
            this.mAdapterChangeListener.onAdapterChanged(pagerAdapter3, pagerAdapter2);
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    /* access modifiers changed from: 0000 */
    public void setOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        this.mAdapterChangeListener = onAdapterChangeListener;
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void setCurrentItem(int i) {
        int i2 = i;
        this.mPopulatePending = DEBUG;
        setCurrentItemInternal(i2, !this.mFirstLayout ? true : DEBUG, DEBUG);
    }

    public void setCurrentItem(int i, boolean z) {
        int i2 = i;
        boolean z2 = z;
        this.mPopulatePending = DEBUG;
        setCurrentItemInternal(i2, z2, DEBUG);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        int i3 = i;
        boolean z3 = z;
        boolean z4 = z2;
        int i4 = i2;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(DEBUG);
        } else if (z4 || this.mCurItem != i3 || this.mItems.size() == 0) {
            if (i3 < 0) {
                i3 = 0;
            } else if (i3 >= this.mAdapter.getCount()) {
                i3 = this.mAdapter.getCount() - 1;
            }
            int i5 = this.mOffscreenPageLimit;
            if (i3 > this.mCurItem + i5 || i3 < this.mCurItem - i5) {
                for (int i6 = 0; i6 < this.mItems.size(); i6++) {
                    ((ItemInfo) this.mItems.get(i6)).scrolling = true;
                }
            }
            boolean z5 = this.mCurItem != i3 ? true : DEBUG;
            if (this.mFirstLayout) {
                this.mCurItem = i3;
                if (z5 && this.mOnPageChangeListener != null) {
                    this.mOnPageChangeListener.onPageSelected(i3);
                }
                if (z5 && this.mInternalPageChangeListener != null) {
                    this.mInternalPageChangeListener.onPageSelected(i3);
                }
                requestLayout();
                return;
            }
            populate(i3);
            scrollToItem(i3, z3, i4, z5);
        } else {
            setScrollingCacheEnabled(DEBUG);
        }
    }

    private void scrollToItem(int i, boolean z, int i2, boolean z2) {
        int i3 = i;
        boolean z3 = z;
        int i4 = i2;
        boolean z4 = z2;
        ItemInfo infoForPosition = infoForPosition(i3);
        int i5 = 0;
        if (infoForPosition != null) {
            i5 = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)));
        }
        if (z3) {
            smoothScrollTo(i5, 0, i4);
            if (z4 && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(i3);
            }
            if (z4 && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(i3);
                return;
            }
            return;
        }
        if (z4 && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(i3);
        }
        if (z4 && this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(i3);
        }
        completeScroll(DEBUG);
        scrollTo(i5, 0);
        boolean pageScrolled = pageScrolled(i5);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageTransformer(boolean z, PageTransformer pageTransformer) {
        boolean z2 = z;
        PageTransformer pageTransformer2 = pageTransformer;
        if (VERSION.SDK_INT >= 11) {
            boolean z3 = pageTransformer2 != null ? true : DEBUG;
            Object obj = z3 != (this.mPageTransformer != null ? true : DEBUG) ? 1 : null;
            this.mPageTransformer = pageTransformer2;
            setChildrenDrawingOrderEnabledCompat(z3);
            if (z3) {
                this.mDrawingOrder = z2 ? 2 : 1;
            } else {
                this.mDrawingOrder = 0;
            }
            if (obj != null) {
                populate();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setChildrenDrawingOrderEnabledCompat(boolean z) {
        int e;
        boolean z2 = z;
        if (VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    Class[] clsArr = new Class[1];
                    Class[] clsArr2 = clsArr;
                    clsArr[0] = Boolean.TYPE;
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", clsArr2);
                } catch (NoSuchMethodException e2) {
                    e = Log.e(TAG, "Can't find setChildrenDrawingOrderEnabled", e2);
                }
            }
            try {
                Method method = this.mSetChildrenDrawingOrderEnabled;
                Boolean[] boolArr = new Object[1];
                Boolean[] boolArr2 = boolArr;
                boolArr[0] = Boolean.valueOf(z2);
                Object invoke = method.invoke(this, boolArr2);
            } catch (Exception e22) {
                e = Log.e(TAG, "Error changing children drawing order", e22);
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int i, int i2) {
        int i3 = i2;
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(this.mDrawingOrder == 2 ? (i - 1) - i3 : i3)).getLayoutParams()).childIndex;
    }

    /* access modifiers changed from: 0000 */
    public OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = onPageChangeListener;
        OnPageChangeListener onPageChangeListener3 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener2;
        return onPageChangeListener3;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int i) {
        int i2 = i;
        if (i2 < 1) {
            String str = TAG;
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder();
            int w = Log.w(str, stringBuilder.append("Requested offscreen page limit ").append(i2).append(" too small; defaulting to ").append(1).toString());
            i2 = 1;
        }
        if (i2 != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = i2;
            populate();
        }
    }

    public void setPageMargin(int i) {
        int i2 = i;
        int i3 = this.mPageMargin;
        this.mPageMargin = i2;
        int width = getWidth();
        recomputeScrollPosition(width, width, i2, i3);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable drawable) {
        Drawable drawable2 = drawable;
        this.mMarginDrawable = drawable2;
        if (drawable2 != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable2 == null ? true : DEBUG);
        invalidate();
    }

    public void setPageMarginDrawable(int i) {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        Drawable drawable2 = drawable;
        boolean z = (super.verifyDrawable(drawable2) || drawable2 == this.mMarginDrawable) ? true : DEBUG;
        return z;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable != null && drawable.isStateful()) {
            boolean state = drawable.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: 0000 */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* access modifiers changed from: 0000 */
    public void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    /* access modifiers changed from: 0000 */
    public void smoothScrollTo(int i, int i2, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(DEBUG);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i7 = i4 - scrollX;
        int i8 = i5 - scrollY;
        if (i7 == 0 && i8 == 0) {
            completeScroll(DEBUG);
            populate();
            setScrollState(0);
            return;
        }
        int round;
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int clientWidth = getClientWidth();
        int i9 = clientWidth / 2;
        float distanceInfluenceForSnapDuration = ((float) i9) + (((float) i9) * distanceInfluenceForSnapDuration(Math.min(1.0f, (1.0f * ((float) Math.abs(i7))) / ((float) clientWidth))));
        Object obj = null;
        i6 = Math.abs(i6);
        if (i6 > 0) {
            round = 4 * Math.round(1000.0f * Math.abs(distanceInfluenceForSnapDuration / ((float) i6)));
        } else {
            round = (int) (((((float) Math.abs(i7)) / ((((float) clientWidth) * this.mAdapter.getPageWidth(this.mCurItem)) + ((float) this.mPageMargin))) + 1.0f) * 100.0f);
        }
        this.mScroller.startScroll(scrollX, scrollY, i7, i8, Math.min(round, MAX_SETTLE_DURATION));
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo addNewItem(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        ItemInfo itemInfo = r8;
        ItemInfo itemInfo2 = new ItemInfo();
        ItemInfo itemInfo3 = itemInfo;
        itemInfo3.position = i3;
        itemInfo3.object = this.mAdapter.instantiateItem(this, i3);
        itemInfo3.widthFactor = this.mAdapter.getPageWidth(i3);
        if (i4 < 0 || i4 >= this.mItems.size()) {
            boolean add = this.mItems.add(itemInfo3);
        } else {
            this.mItems.add(i4, itemInfo3);
        }
        return itemInfo3;
    }

    /* access modifiers changed from: 0000 */
    public void dataSetChanged() {
        int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        Object obj = (this.mItems.size() >= (this.mOffscreenPageLimit * 2) + 1 || this.mItems.size() >= count) ? null : 1;
        Object obj2 = obj;
        int i = this.mCurItem;
        Object obj3 = null;
        int i2 = 0;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i2);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition != -1) {
                int obj22;
                if (itemPosition == -2) {
                    obj = this.mItems.remove(i2);
                    i2--;
                    if (obj3 == null) {
                        this.mAdapter.startUpdate(this);
                        obj3 = 1;
                    }
                    this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
                    obj22 = 1;
                    if (this.mCurItem == itemInfo.position) {
                        i = Math.max(0, Math.min(this.mCurItem, count - 1));
                        obj22 = 1;
                    }
                } else if (itemInfo.position != itemPosition) {
                    if (itemInfo.position == this.mCurItem) {
                        i = itemPosition;
                    }
                    itemInfo.position = itemPosition;
                    obj22 = 1;
                }
            }
            i2++;
        }
        if (obj3 != null) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (obj22 != null) {
            i2 = getChildCount();
            for (int i3 = 0; i3 < i2; i3++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i3).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(i, DEBUG, true);
            requestLayout();
        }
    }

    /* access modifiers changed from: 0000 */
    public void populate() {
        populate(this.mCurItem);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x01e4  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x058d  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x02d5  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0303  */
    /* JADX WARNING: Removed duplicated region for block: B:190:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x059e  */
    public void populate(int r26) {
        /*
        r25 = this;
        r2 = r25;
        r3 = r26;
        r20 = 0;
        r4 = r20;
        r20 = 2;
        r5 = r20;
        r20 = r2;
        r0 = r20;
        r0 = r0.mCurItem;
        r20 = r0;
        r21 = r3;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x004a;
    L_0x001c:
        r20 = r2;
        r0 = r20;
        r0 = r0.mCurItem;
        r20 = r0;
        r21 = r3;
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x005a;
    L_0x002c:
        r20 = 66;
    L_0x002e:
        r5 = r20;
        r20 = r2;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r20 = r20.infoForPosition(r21);
        r4 = r20;
        r20 = r2;
        r21 = r3;
        r0 = r21;
        r1 = r20;
        r1.mCurItem = r0;
    L_0x004a:
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        if (r20 != 0) goto L_0x005d;
    L_0x0054:
        r20 = r2;
        r20.sortChildDrawingOrder();
    L_0x0059:
        return;
    L_0x005a:
        r20 = 17;
        goto L_0x002e;
    L_0x005d:
        r20 = r2;
        r0 = r20;
        r0 = r0.mPopulatePending;
        r20 = r0;
        if (r20 == 0) goto L_0x006d;
    L_0x0067:
        r20 = r2;
        r20.sortChildDrawingOrder();
        goto L_0x0059;
    L_0x006d:
        r20 = r2;
        r20 = r20.getWindowToken();
        if (r20 != 0) goto L_0x0076;
    L_0x0075:
        goto L_0x0059;
    L_0x0076:
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r21 = r2;
        r20.startUpdate(r21);
        r20 = r2;
        r0 = r20;
        r0 = r0.mOffscreenPageLimit;
        r20 = r0;
        r6 = r20;
        r20 = 0;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r22 = r6;
        r21 = r21 - r22;
        r20 = java.lang.Math.max(r20, r21);
        r7 = r20;
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r20 = r20.getCount();
        r8 = r20;
        r20 = r8;
        r21 = 1;
        r20 = r20 + -1;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r22 = r6;
        r21 = r21 + r22;
        r20 = java.lang.Math.min(r20, r21);
        r9 = r20;
        r20 = r8;
        r21 = r2;
        r0 = r21;
        r0 = r0.mExpectedAdapterCount;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x0164;
    L_0x00d7:
        r20 = r2;
        r20 = r20.getResources();	 Catch:{ NotFoundException -> 0x0154 }
        r21 = r2;
        r21 = r21.getId();	 Catch:{ NotFoundException -> 0x0154 }
        r20 = r20.getResourceName(r21);	 Catch:{ NotFoundException -> 0x0154 }
        r10 = r20;
    L_0x00e9:
        r20 = new java.lang.IllegalStateException;
        r24 = r20;
        r20 = r24;
        r21 = r24;
        r22 = new java.lang.StringBuilder;
        r24 = r22;
        r22 = r24;
        r23 = r24;
        r23.<init>();
        r23 = "The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ";
        r22 = r22.append(r23);
        r23 = r2;
        r0 = r23;
        r0 = r0.mExpectedAdapterCount;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = ", found: ";
        r22 = r22.append(r23);
        r23 = r8;
        r22 = r22.append(r23);
        r23 = " Pager id: ";
        r22 = r22.append(r23);
        r23 = r10;
        r22 = r22.append(r23);
        r23 = " Pager class: ";
        r22 = r22.append(r23);
        r23 = r2;
        r23 = r23.getClass();
        r22 = r22.append(r23);
        r23 = " Problematic adapter: ";
        r22 = r22.append(r23);
        r23 = r2;
        r0 = r23;
        r0 = r0.mAdapter;
        r23 = r0;
        r23 = r23.getClass();
        r22 = r22.append(r23);
        r22 = r22.toString();
        r21.<init>(r22);
        throw r20;
    L_0x0154:
        r20 = move-exception;
        r11 = r20;
        r20 = r2;
        r20 = r20.getId();
        r20 = java.lang.Integer.toHexString(r20);
        r10 = r20;
        goto L_0x00e9;
    L_0x0164:
        r20 = -1;
        r10 = r20;
        r20 = 0;
        r11 = r20;
        r20 = 0;
        r10 = r20;
    L_0x0170:
        r20 = r10;
        r21 = r2;
        r0 = r21;
        r0 = r0.mItems;
        r21 = r0;
        r21 = r21.size();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x01c6;
    L_0x0184:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r10;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
        r12 = r20;
        r20 = r12;
        r0 = r20;
        r0 = r0.position;
        r20 = r0;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 < r1) goto L_0x036a;
    L_0x01ac:
        r20 = r12;
        r0 = r20;
        r0 = r0.position;
        r20 = r0;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x01c6;
    L_0x01c2:
        r20 = r12;
        r11 = r20;
    L_0x01c6:
        r20 = r11;
        if (r20 != 0) goto L_0x01e0;
    L_0x01ca:
        r20 = r8;
        if (r20 <= 0) goto L_0x01e0;
    L_0x01ce:
        r20 = r2;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r22 = r10;
        r20 = r20.addNewItem(r21, r22);
        r11 = r20;
    L_0x01e0:
        r20 = r11;
        if (r20 == 0) goto L_0x02bf;
    L_0x01e4:
        r20 = 0;
        r12 = r20;
        r20 = r10;
        r21 = 1;
        r20 = r20 + -1;
        r13 = r20;
        r20 = r13;
        if (r20 < 0) goto L_0x036e;
    L_0x01f4:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x0204:
        r14 = r20;
        r20 = r2;
        r20 = r20.getClientWidth();
        r15 = r20;
        r20 = r15;
        if (r20 > 0) goto L_0x0372;
    L_0x0212:
        r20 = 0;
    L_0x0214:
        r16 = r20;
        r20 = r2;
        r0 = r20;
        r0 = r0.mCurItem;
        r20 = r0;
        r21 = 1;
        r20 = r20 + -1;
        r17 = r20;
    L_0x0224:
        r20 = r17;
        if (r20 < 0) goto L_0x023e;
    L_0x0228:
        r20 = r12;
        r21 = r16;
        r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
        if (r20 < 0) goto L_0x03f6;
    L_0x0230:
        r20 = r17;
        r21 = r7;
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x03f6;
    L_0x023a:
        r20 = r14;
        if (r20 != 0) goto L_0x0396;
    L_0x023e:
        r20 = r11;
        r0 = r20;
        r0 = r0.widthFactor;
        r20 = r0;
        r17 = r20;
        r20 = r10;
        r21 = 1;
        r20 = r20 + 1;
        r13 = r20;
        r20 = r17;
        r21 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
        if (r20 >= 0) goto L_0x02b4;
    L_0x0258:
        r20 = r13;
        r21 = r2;
        r0 = r21;
        r0 = r0.mItems;
        r21 = r0;
        r21 = r21.size();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x046e;
    L_0x026c:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x027c:
        r14 = r20;
        r20 = r15;
        if (r20 > 0) goto L_0x0472;
    L_0x0282:
        r20 = 0;
    L_0x0284:
        r18 = r20;
        r20 = r2;
        r0 = r20;
        r0 = r0.mCurItem;
        r20 = r0;
        r21 = 1;
        r20 = r20 + 1;
        r19 = r20;
    L_0x0294:
        r20 = r19;
        r21 = r8;
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x02b4;
    L_0x029e:
        r20 = r17;
        r21 = r18;
        r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
        if (r20 < 0) goto L_0x04f8;
    L_0x02a6:
        r20 = r19;
        r21 = r9;
        r0 = r20;
        r1 = r21;
        if (r0 <= r1) goto L_0x04f8;
    L_0x02b0:
        r20 = r14;
        if (r20 != 0) goto L_0x048c;
    L_0x02b4:
        r20 = r2;
        r21 = r11;
        r22 = r10;
        r23 = r4;
        r20.calculatePageOffsets(r21, r22, r23);
    L_0x02bf:
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r21 = r2;
        r22 = r2;
        r0 = r22;
        r0 = r0.mCurItem;
        r22 = r0;
        r23 = r11;
        if (r23 == 0) goto L_0x058d;
    L_0x02d5:
        r23 = r11;
        r0 = r23;
        r0 = r0.object;
        r23 = r0;
    L_0x02dd:
        r20.setPrimaryItem(r21, r22, r23);
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r21 = r2;
        r20.finishUpdate(r21);
        r20 = r2;
        r20 = r20.getChildCount();
        r12 = r20;
        r20 = 0;
        r13 = r20;
    L_0x02f9:
        r20 = r13;
        r21 = r12;
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x0591;
    L_0x0303:
        r20 = r2;
        r21 = r13;
        r20 = r20.getChildAt(r21);
        r14 = r20;
        r20 = r14;
        r20 = r20.getLayoutParams();
        r20 = (android.support.v4.view.ViewPager.LayoutParams) r20;
        r15 = r20;
        r20 = r15;
        r21 = r13;
        r0 = r21;
        r1 = r20;
        r1.childIndex = r0;
        r20 = r15;
        r0 = r20;
        r0 = r0.isDecor;
        r20 = r0;
        if (r20 != 0) goto L_0x0367;
    L_0x032b:
        r20 = r15;
        r0 = r20;
        r0 = r0.widthFactor;
        r20 = r0;
        r21 = 0;
        r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
        if (r20 != 0) goto L_0x0367;
    L_0x0339:
        r20 = r2;
        r21 = r14;
        r20 = r20.infoForChild(r21);
        r16 = r20;
        r20 = r16;
        if (r20 == 0) goto L_0x0367;
    L_0x0347:
        r20 = r15;
        r21 = r16;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r0 = r21;
        r1 = r20;
        r1.widthFactor = r0;
        r20 = r15;
        r21 = r16;
        r0 = r21;
        r0 = r0.position;
        r21 = r0;
        r0 = r21;
        r1 = r20;
        r1.position = r0;
    L_0x0367:
        r13 = r13 + 1;
        goto L_0x02f9;
    L_0x036a:
        r10 = r10 + 1;
        goto L_0x0170;
    L_0x036e:
        r20 = 0;
        goto L_0x0204;
    L_0x0372:
        r20 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r21 = r11;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r20 = r20 - r21;
        r21 = r2;
        r21 = r21.getPaddingLeft();
        r0 = r21;
        r0 = (float) r0;
        r21 = r0;
        r22 = r15;
        r0 = r22;
        r0 = (float) r0;
        r22 = r0;
        r21 = r21 / r22;
        r20 = r20 + r21;
        goto L_0x0214;
    L_0x0396:
        r20 = r17;
        r21 = r14;
        r0 = r21;
        r0 = r0.position;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x03ef;
    L_0x03a6:
        r20 = r14;
        r0 = r20;
        r0 = r0.scrolling;
        r20 = r0;
        if (r20 != 0) goto L_0x03ef;
    L_0x03b0:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.remove(r21);
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r21 = r2;
        r22 = r17;
        r23 = r14;
        r0 = r23;
        r0 = r0.object;
        r23 = r0;
        r20.destroyItem(r21, r22, r23);
        r13 = r13 + -1;
        r10 = r10 + -1;
        r20 = r13;
        if (r20 < 0) goto L_0x03f3;
    L_0x03dd:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x03ed:
        r14 = r20;
    L_0x03ef:
        r17 = r17 + -1;
        goto L_0x0224;
    L_0x03f3:
        r20 = 0;
        goto L_0x03ed;
    L_0x03f6:
        r20 = r14;
        if (r20 == 0) goto L_0x0434;
    L_0x03fa:
        r20 = r17;
        r21 = r14;
        r0 = r21;
        r0 = r0.position;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0434;
    L_0x040a:
        r20 = r12;
        r21 = r14;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r20 = r20 + r21;
        r12 = r20;
        r13 = r13 + -1;
        r20 = r13;
        if (r20 < 0) goto L_0x0431;
    L_0x041e:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x042e:
        r14 = r20;
        goto L_0x03ef;
    L_0x0431:
        r20 = 0;
        goto L_0x042e;
    L_0x0434:
        r20 = r2;
        r21 = r17;
        r22 = r13;
        r23 = 1;
        r22 = r22 + 1;
        r20 = r20.addNewItem(r21, r22);
        r14 = r20;
        r20 = r12;
        r21 = r14;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r20 = r20 + r21;
        r12 = r20;
        r10 = r10 + 1;
        r20 = r13;
        if (r20 < 0) goto L_0x046b;
    L_0x0458:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x0468:
        r14 = r20;
        goto L_0x03ef;
    L_0x046b:
        r20 = 0;
        goto L_0x0468;
    L_0x046e:
        r20 = 0;
        goto L_0x027c;
    L_0x0472:
        r20 = r2;
        r20 = r20.getPaddingRight();
        r0 = r20;
        r0 = (float) r0;
        r20 = r0;
        r21 = r15;
        r0 = r21;
        r0 = (float) r0;
        r21 = r0;
        r20 = r20 / r21;
        r21 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r20 = r20 + r21;
        goto L_0x0284;
    L_0x048c:
        r20 = r19;
        r21 = r14;
        r0 = r21;
        r0 = r0.position;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x04f1;
    L_0x049c:
        r20 = r14;
        r0 = r20;
        r0 = r0.scrolling;
        r20 = r0;
        if (r20 != 0) goto L_0x04f1;
    L_0x04a6:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.remove(r21);
        r20 = r2;
        r0 = r20;
        r0 = r0.mAdapter;
        r20 = r0;
        r21 = r2;
        r22 = r19;
        r23 = r14;
        r0 = r23;
        r0 = r0.object;
        r23 = r0;
        r20.destroyItem(r21, r22, r23);
        r20 = r13;
        r21 = r2;
        r0 = r21;
        r0 = r0.mItems;
        r21 = r0;
        r21 = r21.size();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x04f5;
    L_0x04df:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x04ef:
        r14 = r20;
    L_0x04f1:
        r19 = r19 + 1;
        goto L_0x0294;
    L_0x04f5:
        r20 = 0;
        goto L_0x04ef;
    L_0x04f8:
        r20 = r14;
        if (r20 == 0) goto L_0x0546;
    L_0x04fc:
        r20 = r19;
        r21 = r14;
        r0 = r21;
        r0 = r0.position;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0546;
    L_0x050c:
        r20 = r17;
        r21 = r14;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r20 = r20 + r21;
        r17 = r20;
        r13 = r13 + 1;
        r20 = r13;
        r21 = r2;
        r0 = r21;
        r0 = r0.mItems;
        r21 = r0;
        r21 = r21.size();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x0543;
    L_0x0530:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x0540:
        r14 = r20;
        goto L_0x04f1;
    L_0x0543:
        r20 = 0;
        goto L_0x0540;
    L_0x0546:
        r20 = r2;
        r21 = r19;
        r22 = r13;
        r20 = r20.addNewItem(r21, r22);
        r14 = r20;
        r13 = r13 + 1;
        r20 = r17;
        r21 = r14;
        r0 = r21;
        r0 = r0.widthFactor;
        r21 = r0;
        r20 = r20 + r21;
        r17 = r20;
        r20 = r13;
        r21 = r2;
        r0 = r21;
        r0 = r0.mItems;
        r21 = r0;
        r21 = r21.size();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x058a;
    L_0x0576:
        r20 = r2;
        r0 = r20;
        r0 = r0.mItems;
        r20 = r0;
        r21 = r13;
        r20 = r20.get(r21);
        r20 = (android.support.v4.view.ViewPager.ItemInfo) r20;
    L_0x0586:
        r14 = r20;
        goto L_0x04f1;
    L_0x058a:
        r20 = 0;
        goto L_0x0586;
    L_0x058d:
        r23 = 0;
        goto L_0x02dd;
    L_0x0591:
        r20 = r2;
        r20.sortChildDrawingOrder();
        r20 = r2;
        r20 = r20.hasFocus();
        if (r20 == 0) goto L_0x0618;
    L_0x059e:
        r20 = r2;
        r20 = r20.findFocus();
        r13 = r20;
        r20 = r13;
        if (r20 == 0) goto L_0x061a;
    L_0x05aa:
        r20 = r2;
        r21 = r13;
        r20 = r20.infoForAnyChild(r21);
    L_0x05b2:
        r14 = r20;
        r20 = r14;
        if (r20 == 0) goto L_0x05ce;
    L_0x05b8:
        r20 = r14;
        r0 = r20;
        r0 = r0.position;
        r20 = r0;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x0618;
    L_0x05ce:
        r20 = 0;
        r15 = r20;
    L_0x05d2:
        r20 = r15;
        r21 = r2;
        r21 = r21.getChildCount();
        r0 = r20;
        r1 = r21;
        if (r0 >= r1) goto L_0x0618;
    L_0x05e0:
        r20 = r2;
        r21 = r15;
        r20 = r20.getChildAt(r21);
        r16 = r20;
        r20 = r2;
        r21 = r16;
        r20 = r20.infoForChild(r21);
        r14 = r20;
        r20 = r14;
        if (r20 == 0) goto L_0x061d;
    L_0x05f8:
        r20 = r14;
        r0 = r20;
        r0 = r0.position;
        r20 = r0;
        r21 = r2;
        r0 = r21;
        r0 = r0.mCurItem;
        r21 = r0;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x061d;
    L_0x060e:
        r20 = r16;
        r21 = r5;
        r20 = r20.requestFocus(r21);
        if (r20 == 0) goto L_0x061d;
    L_0x0618:
        goto L_0x0059;
    L_0x061a:
        r20 = 0;
        goto L_0x05b2;
    L_0x061d:
        r15 = r15 + 1;
        goto L_0x05d2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.populate(int):void");
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                ArrayList arrayList = r7;
                ArrayList arrayList2 = new ArrayList();
                this.mDrawingOrderedChildren = arrayList;
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                boolean add = this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0085 A:{LOOP_END, LOOP:2: B:19:0x007f->B:21:0x0085} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x014e A:{LOOP_END, LOOP:7: B:49:0x0148->B:51:0x014e} */
    private void calculatePageOffsets(android.support.v4.view.ViewPager.ItemInfo r17, int r18, android.support.v4.view.ViewPager.ItemInfo r19) {
        /*
        r16 = this;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r3 = r19;
        r12 = r0;
        r12 = r12.mAdapter;
        r12 = r12.getCount();
        r4 = r12;
        r12 = r0;
        r12 = r12.getClientWidth();
        r5 = r12;
        r12 = r5;
        if (r12 <= 0) goto L_0x007d;
    L_0x0019:
        r12 = r0;
        r12 = r12.mPageMargin;
        r12 = (float) r12;
        r13 = r5;
        r13 = (float) r13;
        r12 = r12 / r13;
    L_0x0020:
        r6 = r12;
        r12 = r3;
        if (r12 == 0) goto L_0x00a4;
    L_0x0024:
        r12 = r3;
        r12 = r12.position;
        r7 = r12;
        r12 = r7;
        r13 = r1;
        r13 = r13.position;
        if (r12 >= r13) goto L_0x0101;
    L_0x002e:
        r12 = 0;
        r8 = r12;
        r12 = 0;
        r9 = r12;
        r12 = r3;
        r12 = r12.offset;
        r13 = r3;
        r13 = r13.widthFactor;
        r12 = r12 + r13;
        r13 = r6;
        r12 = r12 + r13;
        r10 = r12;
        r12 = r7;
        r13 = 1;
        r12 = r12 + 1;
        r11 = r12;
    L_0x0041:
        r12 = r11;
        r13 = r1;
        r13 = r13.position;
        if (r12 > r13) goto L_0x00a4;
    L_0x0047:
        r12 = r8;
        r13 = r0;
        r13 = r13.mItems;
        r13 = r13.size();
        if (r12 >= r13) goto L_0x00a4;
    L_0x0051:
        r12 = r0;
        r12 = r12.mItems;
        r13 = r8;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r9 = r12;
    L_0x005c:
        r12 = r11;
        r13 = r9;
        r13 = r13.position;
        if (r12 <= r13) goto L_0x007f;
    L_0x0062:
        r12 = r8;
        r13 = r0;
        r13 = r13.mItems;
        r13 = r13.size();
        r14 = 1;
        r13 = r13 + -1;
        if (r12 >= r13) goto L_0x007f;
    L_0x006f:
        r8 = r8 + 1;
        r12 = r0;
        r12 = r12.mItems;
        r13 = r8;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r9 = r12;
        goto L_0x005c;
    L_0x007d:
        r12 = 0;
        goto L_0x0020;
    L_0x007f:
        r12 = r11;
        r13 = r9;
        r13 = r13.position;
        if (r12 >= r13) goto L_0x0095;
    L_0x0085:
        r12 = r10;
        r13 = r0;
        r13 = r13.mAdapter;
        r14 = r11;
        r13 = r13.getPageWidth(r14);
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 + r13;
        r10 = r12;
        r11 = r11 + 1;
        goto L_0x007f;
    L_0x0095:
        r12 = r9;
        r13 = r10;
        r12.offset = r13;
        r12 = r10;
        r13 = r9;
        r13 = r13.widthFactor;
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 + r13;
        r10 = r12;
        r11 = r11 + 1;
        goto L_0x0041;
    L_0x00a4:
        r12 = r0;
        r12 = r12.mItems;
        r12 = r12.size();
        r7 = r12;
        r12 = r1;
        r12 = r12.offset;
        r8 = r12;
        r12 = r1;
        r12 = r12.position;
        r13 = 1;
        r12 = r12 + -1;
        r9 = r12;
        r12 = r0;
        r13 = r1;
        r13 = r13.position;
        if (r13 != 0) goto L_0x016d;
    L_0x00bd:
        r13 = r1;
        r13 = r13.offset;
    L_0x00c0:
        r12.mFirstOffset = r13;
        r12 = r0;
        r13 = r1;
        r13 = r13.position;
        r14 = r4;
        r15 = 1;
        r14 = r14 + -1;
        if (r13 != r14) goto L_0x0172;
    L_0x00cc:
        r13 = r1;
        r13 = r13.offset;
        r14 = r1;
        r14 = r14.widthFactor;
        r13 = r13 + r14;
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = r13 - r14;
    L_0x00d6:
        r12.mLastOffset = r13;
        r12 = r2;
        r13 = 1;
        r12 = r12 + -1;
        r10 = r12;
    L_0x00dd:
        r12 = r10;
        if (r12 < 0) goto L_0x0192;
    L_0x00e0:
        r12 = r0;
        r12 = r12.mItems;
        r13 = r10;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r11 = r12;
    L_0x00eb:
        r12 = r9;
        r13 = r11;
        r13 = r13.position;
        if (r12 <= r13) goto L_0x0177;
    L_0x00f1:
        r12 = r8;
        r13 = r0;
        r13 = r13.mAdapter;
        r14 = r9;
        r9 = r9 + -1;
        r13 = r13.getPageWidth(r14);
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 - r13;
        r8 = r12;
        goto L_0x00eb;
    L_0x0101:
        r12 = r7;
        r13 = r1;
        r13 = r13.position;
        if (r12 <= r13) goto L_0x00a4;
    L_0x0107:
        r12 = r0;
        r12 = r12.mItems;
        r12 = r12.size();
        r13 = 1;
        r12 = r12 + -1;
        r8 = r12;
        r12 = 0;
        r9 = r12;
        r12 = r3;
        r12 = r12.offset;
        r10 = r12;
        r12 = r7;
        r13 = 1;
        r12 = r12 + -1;
        r11 = r12;
    L_0x011d:
        r12 = r11;
        r13 = r1;
        r13 = r13.position;
        if (r12 < r13) goto L_0x00a4;
    L_0x0123:
        r12 = r8;
        if (r12 < 0) goto L_0x00a4;
    L_0x0126:
        r12 = r0;
        r12 = r12.mItems;
        r13 = r8;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r9 = r12;
    L_0x0131:
        r12 = r11;
        r13 = r9;
        r13 = r13.position;
        if (r12 >= r13) goto L_0x0148;
    L_0x0137:
        r12 = r8;
        if (r12 <= 0) goto L_0x0148;
    L_0x013a:
        r8 = r8 + -1;
        r12 = r0;
        r12 = r12.mItems;
        r13 = r8;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r9 = r12;
        goto L_0x0131;
    L_0x0148:
        r12 = r11;
        r13 = r9;
        r13 = r13.position;
        if (r12 <= r13) goto L_0x015e;
    L_0x014e:
        r12 = r10;
        r13 = r0;
        r13 = r13.mAdapter;
        r14 = r11;
        r13 = r13.getPageWidth(r14);
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 - r13;
        r10 = r12;
        r11 = r11 + -1;
        goto L_0x0148;
    L_0x015e:
        r12 = r10;
        r13 = r9;
        r13 = r13.widthFactor;
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 - r13;
        r10 = r12;
        r12 = r9;
        r13 = r10;
        r12.offset = r13;
        r11 = r11 + -1;
        goto L_0x011d;
    L_0x016d:
        r13 = -8388609; // 0xffffffffff7fffff float:-3.4028235E38 double:NaN;
        goto L_0x00c0;
    L_0x0172:
        r13 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        goto L_0x00d6;
    L_0x0177:
        r12 = r8;
        r13 = r11;
        r13 = r13.widthFactor;
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 - r13;
        r8 = r12;
        r12 = r11;
        r13 = r8;
        r12.offset = r13;
        r12 = r11;
        r12 = r12.position;
        if (r12 != 0) goto L_0x018c;
    L_0x0188:
        r12 = r0;
        r13 = r8;
        r12.mFirstOffset = r13;
    L_0x018c:
        r10 = r10 + -1;
        r9 = r9 + -1;
        goto L_0x00dd;
    L_0x0192:
        r12 = r1;
        r12 = r12.offset;
        r13 = r1;
        r13 = r13.widthFactor;
        r12 = r12 + r13;
        r13 = r6;
        r12 = r12 + r13;
        r8 = r12;
        r12 = r1;
        r12 = r12.position;
        r13 = 1;
        r12 = r12 + 1;
        r9 = r12;
        r12 = r2;
        r13 = 1;
        r12 = r12 + 1;
        r10 = r12;
    L_0x01a8:
        r12 = r10;
        r13 = r7;
        if (r12 >= r13) goto L_0x01f2;
    L_0x01ac:
        r12 = r0;
        r12 = r12.mItems;
        r13 = r10;
        r12 = r12.get(r13);
        r12 = (android.support.v4.view.ViewPager.ItemInfo) r12;
        r11 = r12;
    L_0x01b7:
        r12 = r9;
        r13 = r11;
        r13 = r13.position;
        if (r12 >= r13) goto L_0x01cd;
    L_0x01bd:
        r12 = r8;
        r13 = r0;
        r13 = r13.mAdapter;
        r14 = r9;
        r9 = r9 + 1;
        r13 = r13.getPageWidth(r14);
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 + r13;
        r8 = r12;
        goto L_0x01b7;
    L_0x01cd:
        r12 = r11;
        r12 = r12.position;
        r13 = r4;
        r14 = 1;
        r13 = r13 + -1;
        if (r12 != r13) goto L_0x01e1;
    L_0x01d6:
        r12 = r0;
        r13 = r8;
        r14 = r11;
        r14 = r14.widthFactor;
        r13 = r13 + r14;
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = r13 - r14;
        r12.mLastOffset = r13;
    L_0x01e1:
        r12 = r11;
        r13 = r8;
        r12.offset = r13;
        r12 = r8;
        r13 = r11;
        r13 = r13.widthFactor;
        r14 = r6;
        r13 = r13 + r14;
        r12 = r12 + r13;
        r8 = r12;
        r10 = r10 + 1;
        r9 = r9 + 1;
        goto L_0x01a8;
    L_0x01f2:
        r12 = r0;
        r13 = 0;
        r12.mNeedCalculatePageOffsets = r13;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.calculatePageOffsets(android.support.v4.view.ViewPager$ItemInfo, int, android.support.v4.view.ViewPager$ItemInfo):void");
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = r6;
        SavedState savedState2 = new SavedState(super.onSaveInstanceState());
        SavedState savedState3 = savedState;
        savedState3.position = this.mCurItem;
        if (this.mAdapter != null) {
            savedState3.adapterState = this.mAdapter.saveState();
        }
        return savedState3;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        Parcelable parcelable2 = parcelable;
        if (parcelable2 instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable2;
            super.onRestoreInstanceState(savedState.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(savedState.adapterState, savedState.loader);
                setCurrentItemInternal(savedState.position, DEBUG, true);
                return;
            }
            this.mRestoredCurItem = savedState.position;
            this.mRestoredAdapterState = savedState.adapterState;
            this.mRestoredClassLoader = savedState.loader;
            return;
        }
        super.onRestoreInstanceState(parcelable2);
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutParams) {
        View view2 = view;
        int i2 = i;
        android.view.ViewGroup.LayoutParams layoutParams2 = layoutParams;
        if (!checkLayoutParams(layoutParams2)) {
            layoutParams2 = generateLayoutParams(layoutParams2);
        }
        LayoutParams layoutParams3 = (LayoutParams) layoutParams2;
        LayoutParams layoutParams4 = layoutParams3;
        layoutParams4.isDecor |= view2 instanceof Decor;
        if (!this.mInLayout) {
            super.addView(view2, i2, layoutParams2);
        } else if (layoutParams3 == null || !layoutParams3.isDecor) {
            layoutParams3.needsMeasure = true;
            boolean addViewInLayout = addViewInLayout(view2, i2, layoutParams2);
        } else {
            IllegalStateException illegalStateException = r9;
            IllegalStateException illegalStateException2 = new IllegalStateException("Cannot add pager decor view during layout");
            throw illegalStateException;
        }
    }

    public void removeView(View view) {
        View view2 = view;
        if (this.mInLayout) {
            removeViewInLayout(view2);
        } else {
            super.removeView(view2);
        }
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForChild(View view) {
        View view2 = view;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view2, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForAnyChild(View view) {
        View view2 = view;
        while (true) {
            ViewParent parent = view2.getParent();
            ViewParent viewParent = parent;
            if (parent == this) {
                return infoForChild(view2);
            }
            if (viewParent != null && (viewParent instanceof View)) {
                view2 = (View) viewParent;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForPosition(int i) {
        int i2 = i;
        for (int i3 = 0; i3 < this.mItems.size(); i3++) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i3);
            if (itemInfo.position == i2) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        View childAt;
        LayoutParams layoutParams;
        setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, i2));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        for (i3 = 0; i3 < childCount; i3++) {
            childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    int i4 = layoutParams.gravity & 7;
                    int i5 = layoutParams.gravity & 112;
                    int i6 = Integer.MIN_VALUE;
                    int i7 = Integer.MIN_VALUE;
                    Object obj = (i5 == 48 || i5 == 80) ? 1 : null;
                    Object obj2 = obj;
                    obj = (i4 == 3 || i4 == 5) ? 1 : null;
                    Object obj3 = obj;
                    if (obj2 != null) {
                        i6 = 1073741824;
                    } else if (obj3 != null) {
                        i7 = 1073741824;
                    }
                    int i8 = paddingLeft;
                    int i9 = measuredHeight;
                    if (layoutParams.width != -2) {
                        i6 = 1073741824;
                        if (layoutParams.width != -1) {
                            i8 = layoutParams.width;
                        }
                    }
                    if (layoutParams.height != -2) {
                        i7 = 1073741824;
                        if (layoutParams.height != -1) {
                            i9 = layoutParams.height;
                        }
                    }
                    childAt.measure(MeasureSpec.makeMeasureSpec(i8, i6), MeasureSpec.makeMeasureSpec(i9, i7));
                    if (obj2 != null) {
                        measuredHeight -= childAt.getMeasuredHeight();
                    } else if (obj3 != null) {
                        paddingLeft -= childAt.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = DEBUG;
        childCount = getChildCount();
        for (i3 = 0; i3 < childCount; i3++) {
            childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams == null || !layoutParams.isDecor) {
                    childAt.measure(MeasureSpec.makeMeasureSpec((int) (((float) paddingLeft) * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i3;
        super.onSizeChanged(i5, i2, i6, i4);
        if (i5 != i6) {
            recomputeScrollPosition(i5, i6, this.mPageMargin, this.mPageMargin);
        }
    }

    private void recomputeScrollPosition(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (i6 <= 0 || this.mItems.isEmpty()) {
            ItemInfo infoForPosition = infoForPosition(this.mCurItem);
            int min = (int) ((infoForPosition != null ? Math.min(infoForPosition.offset, this.mLastOffset) : 0.0f) * ((float) ((i5 - getPaddingLeft()) - getPaddingRight())));
            if (min != getScrollX()) {
                completeScroll(DEBUG);
                scrollTo(min, getScrollY());
                return;
            }
            return;
        }
        int scrollX = (int) ((((float) getScrollX()) / ((float) (((i6 - getPaddingLeft()) - getPaddingRight()) + i8))) * ((float) (((i5 - getPaddingLeft()) - getPaddingRight()) + i7)));
        scrollTo(scrollX, getScrollY());
        if (!this.mScroller.isFinished()) {
            this.mScroller.startScroll(scrollX, 0, (int) (infoForPosition(this.mCurItem).offset * ((float) i5)), 0, this.mScroller.getDuration() - this.mScroller.timePassed());
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        boolean z2 = z;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        int childCount = getChildCount();
        int i11 = i9 - i7;
        int i12 = i10 - i8;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int i13 = 0;
        for (i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Object obj = null;
                Object obj2 = null;
                if (layoutParams.isDecor) {
                    int max;
                    int max2;
                    i6 = layoutParams.gravity & 112;
                    switch (layoutParams.gravity & 7) {
                        case 1:
                            max = Math.max((i11 - childAt.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 3:
                            max = paddingLeft;
                            paddingLeft += childAt.getMeasuredWidth();
                            break;
                        case 5:
                            max = (i11 - paddingRight) - childAt.getMeasuredWidth();
                            paddingRight += childAt.getMeasuredWidth();
                            break;
                        default:
                            max = paddingLeft;
                            break;
                    }
                    switch (i6) {
                        case 16:
                            max2 = Math.max((i12 - childAt.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            max2 = paddingTop;
                            paddingTop += childAt.getMeasuredHeight();
                            break;
                        case 80:
                            max2 = (i12 - paddingBottom) - childAt.getMeasuredHeight();
                            paddingBottom += childAt.getMeasuredHeight();
                            break;
                        default:
                            max2 = paddingTop;
                            break;
                    }
                    max += scrollX;
                    childAt.layout(max, max2, max + childAt.getMeasuredWidth(), max2 + childAt.getMeasuredHeight());
                    i13++;
                }
            }
        }
        i5 = (i11 - paddingLeft) - paddingRight;
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt2 = getChildAt(i14);
            if (childAt2.getVisibility() != 8) {
                LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    ItemInfo infoForChild = infoForChild(childAt2);
                    ItemInfo itemInfo = infoForChild;
                    if (infoForChild != null) {
                        i6 = paddingLeft + ((int) (((float) i5) * itemInfo.offset));
                        int i15 = paddingTop;
                        if (layoutParams2.needsMeasure) {
                            layoutParams2.needsMeasure = DEBUG;
                            childAt2.measure(MeasureSpec.makeMeasureSpec((int) (((float) i5) * layoutParams2.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((i12 - paddingTop) - paddingBottom, 1073741824));
                        }
                        childAt2.layout(i6, i15, i6 + childAt2.getMeasuredWidth(), i15 + childAt2.getMeasuredHeight());
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = i12 - paddingBottom;
        this.mDecorChildCount = i13;
        if (this.mFirstLayout) {
            scrollToItem(this.mCurItem, DEBUG, 0, DEBUG);
        }
        this.mFirstLayout = DEBUG;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
            if (!pageScrolled(currX)) {
                this.mScroller.abortAnimation();
                scrollTo(0, currY);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean pageScrolled(int i) {
        int i2 = i;
        IllegalStateException illegalStateException;
        IllegalStateException illegalStateException2;
        if (this.mItems.size() == 0) {
            this.mCalledSuper = DEBUG;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return DEBUG;
            }
            illegalStateException = r13;
            illegalStateException2 = new IllegalStateException("onPageScrolled did not call superclass implementation");
            throw illegalStateException;
        }
        ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
        int clientWidth = getClientWidth();
        int i3 = clientWidth + this.mPageMargin;
        float f = ((float) this.mPageMargin) / ((float) clientWidth);
        int i4 = infoForCurrentScrollPosition.position;
        float f2 = ((((float) i2) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + f);
        int i5 = (int) (f2 * ((float) i3));
        this.mCalledSuper = DEBUG;
        onPageScrolled(i4, f2, i5);
        if (this.mCalledSuper) {
            return true;
        }
        illegalStateException = r13;
        illegalStateException2 = new IllegalStateException("onPageScrolled did not call superclass implementation");
        throw illegalStateException;
    }

    /* access modifiers changed from: protected */
    public void onPageScrolled(int i, float f, int i2) {
        int scrollX;
        int paddingLeft;
        int paddingRight;
        int i3 = i;
        float f2 = f;
        int i4 = i2;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            paddingLeft = getPaddingLeft();
            paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    int max;
                    Object obj = null;
                    switch (layoutParams.gravity & 7) {
                        case 1:
                            max = Math.max((width - childAt.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 3:
                            max = paddingLeft;
                            paddingLeft += childAt.getWidth();
                            break;
                        case 5:
                            max = (width - paddingRight) - childAt.getMeasuredWidth();
                            paddingRight += childAt.getMeasuredWidth();
                            break;
                        default:
                            max = paddingLeft;
                            break;
                    }
                    int left = (max + scrollX) - childAt.getLeft();
                    if (left != 0) {
                        childAt.offsetLeftAndRight(left);
                    }
                }
            }
        }
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(i3, f2, i4);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(i3, f2, i4);
        }
        if (this.mPageTransformer != null) {
            scrollX = getScrollX();
            paddingLeft = getChildCount();
            for (paddingRight = 0; paddingRight < paddingLeft; paddingRight++) {
                View childAt2 = getChildAt(paddingRight);
                if (!((LayoutParams) childAt2.getLayoutParams()).isDecor) {
                    this.mPageTransformer.transformPage(childAt2, ((float) (childAt2.getLeft() - scrollX)) / ((float) getClientWidth()));
                }
            }
        }
        this.mCalledSuper = true;
    }

    private void completeScroll(boolean z) {
        int scrollX;
        boolean z2 = z;
        Object obj = this.mScrollState == 2 ? 1 : null;
        if (obj != null) {
            setScrollingCacheEnabled(DEBUG);
            this.mScroller.abortAnimation();
            scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (!(scrollX == currX && scrollY == currY)) {
                scrollTo(currX, currY);
            }
        }
        this.mPopulatePending = DEBUG;
        for (scrollX = 0; scrollX < this.mItems.size(); scrollX++) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(scrollX);
            if (itemInfo.scrolling) {
                obj = 1;
                itemInfo.scrolling = DEBUG;
            }
        }
        if (obj == null) {
            return;
        }
        if (z2) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private boolean isGutterDrag(float f, float f2) {
        float f3 = f;
        float f4 = f2;
        boolean z = ((f3 >= ((float) this.mGutterSize) || f4 <= 0.0f) && (f3 <= ((float) (getWidth() - this.mGutterSize)) || f4 >= 0.0f)) ? DEBUG : true;
        return z;
    }

    private void enableLayers(boolean z) {
        boolean z2 = z;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewCompat.setLayerType(getChildAt(i), z2 ? 2 : 0, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        int action = motionEvent2.getAction() & MotionEventCompat.ACTION_MASK;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = DEBUG;
            this.mIsUnableToDrag = DEBUG;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return DEBUG;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return DEBUG;
            }
        }
        switch (action) {
            case 0:
                float x = motionEvent2.getX();
                float f = x;
                this.mInitialMotionX = x;
                this.mLastMotionX = f;
                x = motionEvent2.getY();
                f = x;
                this.mInitialMotionY = x;
                this.mLastMotionY = f;
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent2, 0);
                this.mIsUnableToDrag = DEBUG;
                boolean computeScrollOffset = this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = DEBUG;
                    populate();
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    break;
                }
                completeScroll(DEBUG);
                this.mIsBeingDragged = DEBUG;
                break;
            case 2:
                int i = this.mActivePointerId;
                if (i != -1) {
                    int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent2, i);
                    float x2 = MotionEventCompat.getX(motionEvent2, findPointerIndex);
                    float f2 = x2 - this.mLastMotionX;
                    float abs = Math.abs(f2);
                    float y = MotionEventCompat.getY(motionEvent2, findPointerIndex);
                    float abs2 = Math.abs(y - this.mInitialMotionY);
                    if (f2 == 0.0f || isGutterDrag(this.mLastMotionX, f2) || !canScroll(this, DEBUG, (int) f2, (int) x2, (int) y)) {
                        if (abs > ((float) this.mTouchSlop) && abs * 0.5f > abs2) {
                            this.mIsBeingDragged = true;
                            requestParentDisallowInterceptTouchEvent(true);
                            setScrollState(1);
                            this.mLastMotionX = f2 > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                            this.mLastMotionY = y;
                            setScrollingCacheEnabled(true);
                        } else if (abs2 > ((float) this.mTouchSlop)) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && performDrag(x2)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                            break;
                        }
                    }
                    this.mLastMotionX = x2;
                    this.mLastMotionY = y;
                    this.mIsUnableToDrag = true;
                    return DEBUG;
                }
                break;
            case 6:
                onSecondaryPointerUp(motionEvent2);
                break;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent2);
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent2.getAction() == 0 && motionEvent2.getEdgeFlags() != 0) {
            return DEBUG;
        }
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return DEBUG;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent2);
        int i = 0;
        float f;
        int findPointerIndex;
        switch (motionEvent2.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                this.mScroller.abortAnimation();
                this.mPopulatePending = DEBUG;
                populate();
                float x = motionEvent2.getX();
                f = x;
                this.mInitialMotionX = x;
                this.mLastMotionX = f;
                x = motionEvent2.getY();
                f = x;
                this.mInitialMotionY = x;
                this.mLastMotionY = f;
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent2, 0);
                break;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    this.mPopulatePending = true;
                    int clientWidth = getClientWidth();
                    int scrollX = getScrollX();
                    ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
                    setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (MotionEventCompat.getX(motionEvent2, MotionEventCompat.findPointerIndex(motionEvent2, this.mActivePointerId)) - this.mInitialMotionX)), true, true, xVelocity);
                    this.mActivePointerId = -1;
                    endDrag();
                    i = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    break;
                }
                break;
            case 2:
                if (!this.mIsBeingDragged) {
                    findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent2, this.mActivePointerId);
                    float x2 = MotionEventCompat.getX(motionEvent2, findPointerIndex);
                    float abs = Math.abs(x2 - this.mLastMotionX);
                    float y = MotionEventCompat.getY(motionEvent2, findPointerIndex);
                    float abs2 = Math.abs(y - this.mLastMotionY);
                    if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        if (x2 - this.mInitialMotionX > 0.0f) {
                            f = this.mInitialMotionX + ((float) this.mTouchSlop);
                        } else {
                            f = this.mInitialMotionX - ((float) this.mTouchSlop);
                        }
                        this.mLastMotionX = f;
                        this.mLastMotionY = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    i |= performDrag(MotionEventCompat.getX(motionEvent2, MotionEventCompat.findPointerIndex(motionEvent2, this.mActivePointerId)));
                    break;
                }
                break;
            case 3:
                if (this.mIsBeingDragged) {
                    scrollToItem(this.mCurItem, true, 0, DEBUG);
                    this.mActivePointerId = -1;
                    endDrag();
                    i = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    break;
                }
                break;
            case 5:
                findPointerIndex = MotionEventCompat.getActionIndex(motionEvent2);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent2, findPointerIndex);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent2, findPointerIndex);
                break;
            case 6:
                onSecondaryPointerUp(motionEvent2);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent2, MotionEventCompat.findPointerIndex(motionEvent2, this.mActivePointerId));
                break;
        }
        if (i != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean z) {
        boolean z2 = z;
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z2);
        }
    }

    private boolean performDrag(float f) {
        float f2 = f;
        boolean z = false;
        float f3 = this.mLastMotionX - f2;
        this.mLastMotionX = f2;
        float scrollX = ((float) getScrollX()) + f3;
        int clientWidth = getClientWidth();
        float f4 = ((float) clientWidth) * this.mFirstOffset;
        float f5 = ((float) clientWidth) * this.mLastOffset;
        Object obj = 1;
        Object obj2 = 1;
        ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
        ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            obj = null;
            f4 = itemInfo.offset * ((float) clientWidth);
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            obj2 = null;
            f5 = itemInfo2.offset * ((float) clientWidth);
        }
        if (scrollX < f4) {
            if (obj != null) {
                z = this.mLeftEdge.onPull(Math.abs(f4 - scrollX) / ((float) clientWidth));
            }
            scrollX = f4;
        } else if (scrollX > f5) {
            if (obj2 != null) {
                z = this.mRightEdge.onPull(Math.abs(scrollX - f5) / ((float) clientWidth));
            }
            scrollX = f5;
        }
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        boolean pageScrolled = pageScrolled((int) scrollX);
        return z;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int clientWidth = getClientWidth();
        float scrollX = clientWidth > 0 ? ((float) getScrollX()) / ((float) clientWidth) : 0.0f;
        float f = clientWidth > 0 ? ((float) this.mPageMargin) / ((float) clientWidth) : 0.0f;
        int i = -1;
        float f2 = 0.0f;
        float f3 = 0.0f;
        Object obj = 1;
        ItemInfo itemInfo = null;
        int i2 = 0;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(i2);
            if (obj == null && itemInfo2.position != i + 1) {
                itemInfo2 = this.mTempItem;
                itemInfo2.offset = (f2 + f3) + f;
                itemInfo2.position = i + 1;
                itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
                i2--;
            }
            float f4 = itemInfo2.offset;
            float f5 = f4;
            float f6 = (f4 + itemInfo2.widthFactor) + f;
            if (obj == null && scrollX < f5) {
                return itemInfo;
            }
            if (scrollX < f6 || i2 == this.mItems.size() - 1) {
                return itemInfo2;
            }
            obj = null;
            i = itemInfo2.position;
            f2 = f4;
            f3 = itemInfo2.widthFactor;
            itemInfo = itemInfo2;
            i2++;
        }
        return itemInfo;
    }

    private int determineTargetPage(int i, float f, int i2, int i3) {
        int i4;
        int i5 = i;
        float f2 = f;
        int i6 = i2;
        if (Math.abs(i3) <= this.mFlingDistance || Math.abs(i6) <= this.mMinimumVelocity) {
            i4 = (int) ((((float) i5) + f2) + (i5 >= this.mCurItem ? 0.4f : 0.6f));
        } else {
            i4 = i6 > 0 ? i5 : i5 + 1;
        }
        if (this.mItems.size() > 0) {
            i4 = Math.max(((ItemInfo) this.mItems.get(0)).position, Math.min(i4, ((ItemInfo) this.mItems.get(this.mItems.size() - 1)).position));
        }
        return i4;
    }

    public void draw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.draw(canvas2);
        int i = 0;
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            int save;
            int height;
            int width;
            if (!this.mLeftEdge.isFinished()) {
                save = canvas2.save();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                width = getWidth();
                canvas2.rotate(270.0f);
                canvas2.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(height, width);
                i |= this.mLeftEdge.draw(canvas2);
                canvas2.restoreToCount(save);
            }
            if (!this.mRightEdge.isFinished()) {
                save = canvas2.save();
                height = getWidth();
                width = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas2.rotate(90.0f);
                canvas2.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) height));
                this.mRightEdge.setSize(width, height);
                i |= this.mRightEdge.draw(canvas2);
                canvas2.restoreToCount(save);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (i != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas2);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float f = ((float) this.mPageMargin) / ((float) width);
            int i = 0;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            float f2 = itemInfo.offset;
            int size = this.mItems.size();
            int i2 = itemInfo.position;
            int i3 = ((ItemInfo) this.mItems.get(size - 1)).position;
            int i4 = i2;
            while (i4 < i3) {
                float f3;
                while (i4 > itemInfo.position && i < size) {
                    i++;
                    itemInfo = (ItemInfo) this.mItems.get(i);
                }
                if (i4 == itemInfo.position) {
                    f3 = (itemInfo.offset + itemInfo.widthFactor) * ((float) width);
                    f2 = (itemInfo.offset + itemInfo.widthFactor) + f;
                } else {
                    float pageWidth = this.mAdapter.getPageWidth(i4);
                    f3 = (f2 + pageWidth) * ((float) width);
                    f2 += pageWidth + f;
                }
                if (f3 + ((float) this.mPageMargin) > ((float) scrollX)) {
                    this.mMarginDrawable.setBounds((int) f3, this.mTopPageBounds, (int) ((f3 + ((float) this.mPageMargin)) + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas2);
                }
                if (f3 <= ((float) (scrollX + width))) {
                    i4++;
                } else {
                    return;
                }
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return DEBUG;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        float f = 0.0f;
        float f2 = f;
        float f3 = f;
        this.mLastMotionX = f3;
        this.mInitialMotionX = f2;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int clientWidth = getClientWidth();
            int scrollX = getScrollX();
            ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, xVelocity);
            endDrag();
            this.mFakeDragging = DEBUG;
            return;
        }
        IllegalStateException illegalStateException = r15;
        IllegalStateException illegalStateException2 = new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        throw illegalStateException;
    }

    public void fakeDragBy(float f) {
        float f2 = f;
        if (this.mFakeDragging) {
            this.mLastMotionX += f2;
            float scrollX = ((float) getScrollX()) - f2;
            int clientWidth = getClientWidth();
            float f3 = ((float) clientWidth) * this.mFirstOffset;
            float f4 = ((float) clientWidth) * this.mLastOffset;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
            if (itemInfo.position != 0) {
                f3 = itemInfo.offset * ((float) clientWidth);
            }
            if (itemInfo2.position != this.mAdapter.getCount() - 1) {
                f4 = itemInfo2.offset * ((float) clientWidth);
            }
            if (scrollX < f3) {
                scrollX = f3;
            } else if (scrollX > f4) {
                scrollX = f4;
            }
            this.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            boolean pageScrolled = pageScrolled((int) scrollX);
            MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
            return;
        }
        IllegalStateException illegalStateException = r21;
        IllegalStateException illegalStateException2 = new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        throw illegalStateException;
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent2);
        if (MotionEventCompat.getPointerId(motionEvent2, actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(motionEvent2, i);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent2, i);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = DEBUG;
        this.mIsUnableToDrag = DEBUG;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        boolean z2 = z;
        if (this.mScrollingCacheEnabled != z2) {
            this.mScrollingCacheEnabled = z2;
        }
    }

    public boolean canScrollHorizontally(int i) {
        int i2 = i;
        if (this.mAdapter == null) {
            return DEBUG;
        }
        int clientWidth = getClientWidth();
        int scrollX = getScrollX();
        if (i2 < 0) {
            return scrollX > ((int) (((float) clientWidth) * this.mFirstOffset)) ? true : DEBUG;
        } else if (i2 <= 0) {
            return DEBUG;
        } else {
            return scrollX < ((int) (((float) clientWidth) * this.mLastOffset)) ? true : DEBUG;
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
        boolean z3 = (z2 && ViewCompat.canScrollHorizontally(view2, -i4)) ? true : DEBUG;
        return z3;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        KeyEvent keyEvent2 = keyEvent;
        boolean z = (super.dispatchKeyEvent(keyEvent2) || executeKeyEvent(keyEvent2)) ? true : DEBUG;
        return z;
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        KeyEvent keyEvent2 = keyEvent;
        boolean z = false;
        if (keyEvent2.getAction() == 0) {
            switch (keyEvent2.getKeyCode()) {
                case 21:
                    z = arrowScroll(17);
                    break;
                case 22:
                    z = arrowScroll(66);
                    break;
                case 61:
                    if (VERSION.SDK_INT >= 11) {
                        if (!KeyEventCompat.hasNoModifiers(keyEvent2)) {
                            if (KeyEventCompat.hasModifiers(keyEvent2, 1)) {
                                z = arrowScroll(1);
                                break;
                            }
                        }
                        z = arrowScroll(2);
                        break;
                    }
                    break;
            }
        }
        return z;
    }

    public boolean arrowScroll(int i) {
        int i2 = i;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        } else if (findFocus != null) {
            Object obj = null;
            ViewParent parent = findFocus.getParent();
            while (true) {
                ViewParent viewParent = parent;
                if (!(viewParent instanceof ViewGroup)) {
                    break;
                } else if (viewParent == this) {
                    obj = 1;
                    break;
                } else {
                    parent = viewParent.getParent();
                }
            }
            if (obj == null) {
                StringBuilder stringBuilder = r11;
                StringBuilder stringBuilder2 = new StringBuilder();
                StringBuilder stringBuilder3 = stringBuilder;
                stringBuilder = stringBuilder3.append(findFocus.getClass().getSimpleName());
                parent = findFocus.getParent();
                while (true) {
                    ViewParent viewParent2 = parent;
                    if (!(viewParent2 instanceof ViewGroup)) {
                        break;
                    }
                    stringBuilder = stringBuilder3.append(" => ").append(viewParent2.getClass().getSimpleName());
                    parent = viewParent2.getParent();
                }
                String str = TAG;
                stringBuilder2 = r11;
                StringBuilder stringBuilder4 = new StringBuilder();
                int e = Log.e(str, stringBuilder2.append("arrowScroll tried to find focus based on non-child current focused view ").append(stringBuilder3.toString()).toString());
                findFocus = null;
            }
        }
        boolean z = false;
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i2);
        if (findNextFocus == null || findNextFocus == findFocus) {
            if (i2 == 17 || i2 == 1) {
                z = pageLeft();
            } else if (i2 == 66 || i2 == 2) {
                z = pageRight();
            }
        } else if (i2 == 17) {
            z = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left < getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageLeft();
        } else if (i2 == 66) {
            z = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left > getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageRight();
        }
        if (z) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i2));
        }
        return z;
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        Rect rect2;
        Rect rect3 = rect;
        View view2 = view;
        if (rect3 == null) {
            Rect rect4 = rect2;
            Rect rect5 = new Rect();
            rect3 = rect4;
        }
        if (view2 == null) {
            rect3.set(0, 0, 0, 0);
            return rect3;
        }
        rect3.left = view2.getLeft();
        rect3.right = view2.getRight();
        rect3.top = view2.getTop();
        rect3.bottom = view2.getBottom();
        ViewParent parent = view2.getParent();
        while (true) {
            ViewParent viewParent = parent;
            if ((viewParent instanceof ViewGroup) && viewParent != this) {
                ViewGroup viewGroup = (ViewGroup) viewParent;
                rect2 = rect3;
                rect2.left += viewGroup.getLeft();
                rect2 = rect3;
                rect2.right += viewGroup.getRight();
                rect2 = rect3;
                rect2.top += viewGroup.getTop();
                rect2 = rect3;
                rect2.bottom += viewGroup.getBottom();
                parent = viewGroup.getParent();
            }
        }
        return rect3;
    }

    /* access modifiers changed from: 0000 */
    public boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        ArrayList<View> arrayList2 = arrayList;
        int i3 = i;
        int i4 = i2;
        int size = arrayList2.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i5 = 0; i5 < getChildCount(); i5++) {
                View childAt = getChildAt(i5);
                if (childAt.getVisibility() == 0) {
                    ItemInfo infoForChild = infoForChild(childAt);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        childAt.addFocusables(arrayList2, i3, i4);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && size != arrayList2.size()) || !isFocusable()) {
            return;
        }
        if (((i4 & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arrayList2 != null) {
            boolean add = arrayList2.add(this);
        }
    }

    public void addTouchables(ArrayList<View> arrayList) {
        ArrayList<View> arrayList2 = arrayList;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    childAt.addTouchables(arrayList2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        int i3;
        int i4;
        int i5 = i;
        Rect rect2 = rect;
        int childCount = getChildCount();
        if ((i5 & 2) != 0) {
            i2 = 0;
            i3 = 1;
            i4 = childCount;
        } else {
            i2 = childCount - 1;
            i3 = -1;
            i4 = -1;
        }
        int i6 = i2;
        while (true) {
            int i7 = i6;
            if (i7 == i4) {
                return DEBUG;
            }
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.requestFocus(i5, rect2)) {
                    return true;
                }
            }
            i6 = i7 + i3;
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityEvent accessibilityEvent2 = accessibilityEvent;
        if (accessibilityEvent2.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent2);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent2)) {
                    return true;
                }
            }
        }
        return DEBUG;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        android.view.ViewGroup.LayoutParams layoutParams = r3;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams();
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams layoutParams2 = layoutParams;
        return generateDefaultLayoutParams();
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams layoutParams2 = layoutParams;
        boolean z = ((layoutParams2 instanceof LayoutParams) && super.checkLayoutParams(layoutParams2)) ? true : DEBUG;
        return z;
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        android.view.ViewGroup.LayoutParams layoutParams = r6;
        android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(getContext(), attributeSet);
        return layoutParams;
    }
}
