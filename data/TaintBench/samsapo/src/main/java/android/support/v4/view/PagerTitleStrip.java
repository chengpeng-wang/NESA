package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class PagerTitleStrip extends ViewGroup implements Decor {
    private static final int[] ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
    private static final PagerTitleStripImpl IMPL;
    private static final float SIDE_ALPHA = 0.6f;
    private static final String TAG = "PagerTitleStrip";
    private static final int[] TEXT_ATTRS;
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage;
    /* access modifiers changed from: private */
    public float mLastKnownPositionOffset;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener;
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;
    private WeakReference<PagerAdapter> mWatchingAdapter;

    private class PageListener extends DataSetObserver implements OnPageChangeListener, OnAdapterChangeListener {
        private int mScrollState;
        final /* synthetic */ PagerTitleStrip this$0;

        private PageListener(PagerTitleStrip pagerTitleStrip) {
            this.this$0 = pagerTitleStrip;
        }

        /* synthetic */ PageListener(PagerTitleStrip pagerTitleStrip, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(pagerTitleStrip);
        }

        public void onPageScrolled(int i, float f, int i2) {
            int i3 = i;
            float f2 = f;
            int i4 = i2;
            if (f2 > 0.5f) {
                i3++;
            }
            this.this$0.updateTextPositions(i3, f2, false);
        }

        public void onPageSelected(int i) {
            int i2 = i;
            if (this.mScrollState == 0) {
                this.this$0.updateText(this.this$0.mPager.getCurrentItem(), this.this$0.mPager.getAdapter());
                this.this$0.updateTextPositions(this.this$0.mPager.getCurrentItem(), this.this$0.mLastKnownPositionOffset >= 0.0f ? this.this$0.mLastKnownPositionOffset : 0.0f, true);
            }
        }

        public void onPageScrollStateChanged(int i) {
            this.mScrollState = i;
        }

        public void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            this.this$0.updateAdapter(pagerAdapter, pagerAdapter2);
        }

        public void onChanged() {
            this.this$0.updateText(this.this$0.mPager.getCurrentItem(), this.this$0.mPager.getAdapter());
            this.this$0.updateTextPositions(this.this$0.mPager.getCurrentItem(), this.this$0.mLastKnownPositionOffset >= 0.0f ? this.this$0.mLastKnownPositionOffset : 0.0f, true);
        }
    }

    interface PagerTitleStripImpl {
        void setSingleLineAllCaps(TextView textView);
    }

    static class PagerTitleStripImplBase implements PagerTitleStripImpl {
        PagerTitleStripImplBase() {
        }

        public void setSingleLineAllCaps(TextView textView) {
            textView.setSingleLine();
        }
    }

    static class PagerTitleStripImplIcs implements PagerTitleStripImpl {
        PagerTitleStripImplIcs() {
        }

        public void setSingleLineAllCaps(TextView textView) {
            PagerTitleStripIcs.setSingleLineAllCaps(textView);
        }
    }

    static {
        int[] iArr = new int[1];
        int[] iArr2 = iArr;
        iArr[0] = 16843660;
        TEXT_ATTRS = iArr2;
        if (VERSION.SDK_INT >= 14) {
            PagerTitleStripImplIcs pagerTitleStripImplIcs = r4;
            PagerTitleStripImplIcs pagerTitleStripImplIcs2 = new PagerTitleStripImplIcs();
            IMPL = pagerTitleStripImplIcs;
            return;
        }
        PagerTitleStripImplBase pagerTitleStripImplBase = r4;
        PagerTitleStripImplBase pagerTitleStripImplBase2 = new PagerTitleStripImplBase();
        IMPL = pagerTitleStripImplBase;
    }

    private static void setSingleLineAllCaps(TextView textView) {
        IMPL.setSingleLineAllCaps(textView);
    }

    public PagerTitleStrip(Context context) {
        this(context, null);
    }

    public PagerTitleStrip(Context context, AttributeSet attributeSet) {
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        super(context2, attributeSet2);
        this.mLastKnownCurrentPage = -1;
        this.mLastKnownPositionOffset = -1.0f;
        PageListener pageListener = r13;
        PageListener pageListener2 = new PageListener(this, null);
        this.mPageListener = pageListener;
        View view = r13;
        View textView = new TextView(context2);
        View view2 = view;
        View view3 = view2;
        this.mPrevText = view2;
        addView(view3);
        view = r13;
        textView = new TextView(context2);
        view2 = view;
        view3 = view2;
        this.mCurrText = view2;
        addView(view3);
        view = r13;
        textView = new TextView(context2);
        view2 = view;
        view3 = view2;
        this.mNextText = view2;
        addView(view3);
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, ATTRS);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId != 0) {
            this.mPrevText.setTextAppearance(context2, resourceId);
            this.mCurrText.setTextAppearance(context2, resourceId);
            this.mNextText.setTextAppearance(context2, resourceId);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        if (dimensionPixelSize != 0) {
            setTextSize(0, (float) dimensionPixelSize);
        }
        if (obtainStyledAttributes.hasValue(2)) {
            int color = obtainStyledAttributes.getColor(2, 0);
            this.mPrevText.setTextColor(color);
            this.mCurrText.setTextColor(color);
            this.mNextText.setTextColor(color);
        }
        this.mGravity = obtainStyledAttributes.getInteger(3, 80);
        obtainStyledAttributes.recycle();
        this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(SIDE_ALPHA);
        this.mPrevText.setEllipsize(TruncateAt.END);
        this.mCurrText.setEllipsize(TruncateAt.END);
        this.mNextText.setEllipsize(TruncateAt.END);
        boolean z = false;
        if (resourceId != 0) {
            TypedArray obtainStyledAttributes2 = context2.obtainStyledAttributes(resourceId, TEXT_ATTRS);
            z = obtainStyledAttributes2.getBoolean(0, false);
            obtainStyledAttributes2.recycle();
        }
        if (z) {
            setSingleLineAllCaps(this.mPrevText);
            setSingleLineAllCaps(this.mCurrText);
            setSingleLineAllCaps(this.mNextText);
        } else {
            this.mPrevText.setSingleLine();
            this.mCurrText.setSingleLine();
            this.mNextText.setSingleLine();
        }
        this.mScaledTextSpacing = (int) (16.0f * context2.getResources().getDisplayMetrics().density);
    }

    public void setTextSpacing(int i) {
        this.mScaledTextSpacing = i;
        requestLayout();
    }

    public int getTextSpacing() {
        return this.mScaledTextSpacing;
    }

    public void setNonPrimaryAlpha(float f) {
        this.mNonPrimaryAlpha = ((int) (f * 255.0f)) & MotionEventCompat.ACTION_MASK;
        int i = (this.mNonPrimaryAlpha << 24) | (this.mTextColor & ViewCompat.MEASURED_SIZE_MASK);
        this.mPrevText.setTextColor(i);
        this.mNextText.setTextColor(i);
    }

    public void setTextColor(int i) {
        int i2 = i;
        this.mTextColor = i2;
        this.mCurrText.setTextColor(i2);
        int i3 = (this.mNonPrimaryAlpha << 24) | (this.mTextColor & ViewCompat.MEASURED_SIZE_MASK);
        this.mPrevText.setTextColor(i3);
        this.mNextText.setTextColor(i3);
    }

    public void setTextSize(int i, float f) {
        int i2 = i;
        float f2 = f;
        this.mPrevText.setTextSize(i2, f2);
        this.mCurrText.setTextSize(i2, f2);
        this.mNextText.setTextSize(i2, f2);
    }

    public void setGravity(int i) {
        this.mGravity = i;
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) parent;
            PagerAdapter adapter = viewPager.getAdapter();
            OnPageChangeListener internalPageChangeListener = viewPager.setInternalPageChangeListener(this.mPageListener);
            viewPager.setOnAdapterChangeListener(this.mPageListener);
            this.mPager = viewPager;
            updateAdapter(this.mWatchingAdapter != null ? (PagerAdapter) this.mWatchingAdapter.get() : null, adapter);
            return;
        }
        IllegalStateException illegalStateException = r7;
        IllegalStateException illegalStateException2 = new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
        throw illegalStateException;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPager != null) {
            updateAdapter(this.mPager.getAdapter(), null);
            OnPageChangeListener internalPageChangeListener = this.mPager.setInternalPageChangeListener(null);
            this.mPager.setOnAdapterChangeListener(null);
            this.mPager = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateText(int i, PagerAdapter pagerAdapter) {
        int i2 = i;
        PagerAdapter pagerAdapter2 = pagerAdapter;
        int count = pagerAdapter2 != null ? pagerAdapter2.getCount() : 0;
        this.mUpdatingText = true;
        CharSequence charSequence = null;
        if (i2 >= 1 && pagerAdapter2 != null) {
            charSequence = pagerAdapter2.getPageTitle(i2 - 1);
        }
        this.mPrevText.setText(charSequence);
        TextView textView = this.mCurrText;
        CharSequence pageTitle = (pagerAdapter2 == null || i2 >= count) ? null : pagerAdapter2.getPageTitle(i2);
        textView.setText(pageTitle);
        charSequence = null;
        if (i2 + 1 < count && pagerAdapter2 != null) {
            charSequence = pagerAdapter2.getPageTitle(i2 + 1);
        }
        this.mNextText.setText(charSequence);
        int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f), ExploreByTouchHelper.INVALID_ID);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(height, ExploreByTouchHelper.INVALID_ID);
        this.mPrevText.measure(makeMeasureSpec, makeMeasureSpec2);
        this.mCurrText.measure(makeMeasureSpec, makeMeasureSpec2);
        this.mNextText.measure(makeMeasureSpec, makeMeasureSpec2);
        this.mLastKnownCurrentPage = i2;
        if (!this.mUpdatingPositions) {
            updateTextPositions(i2, this.mLastKnownPositionOffset, false);
        }
        this.mUpdatingText = false;
    }

    public void requestLayout() {
        if (!this.mUpdatingText) {
            super.requestLayout();
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateAdapter(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
        PagerAdapter pagerAdapter3 = pagerAdapter;
        PagerAdapter pagerAdapter4 = pagerAdapter2;
        if (pagerAdapter3 != null) {
            pagerAdapter3.unregisterDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = null;
        }
        if (pagerAdapter4 != null) {
            pagerAdapter4.registerDataSetObserver(this.mPageListener);
            WeakReference weakReference = r7;
            WeakReference weakReference2 = new WeakReference(pagerAdapter4);
            this.mWatchingAdapter = weakReference;
        }
        if (this.mPager != null) {
            this.mLastKnownCurrentPage = -1;
            this.mLastKnownPositionOffset = -1.0f;
            updateText(this.mPager.getCurrentItem(), pagerAdapter4);
            requestLayout();
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateTextPositions(int i, float f, boolean z) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = i;
        float f2 = f;
        boolean z2 = z;
        if (i6 != this.mLastKnownCurrentPage) {
            updateText(i6, this.mPager.getAdapter());
        } else if (!z2 && f2 == this.mLastKnownPositionOffset) {
            return;
        }
        this.mUpdatingPositions = true;
        int measuredWidth = this.mPrevText.getMeasuredWidth();
        int measuredWidth2 = this.mCurrText.getMeasuredWidth();
        int measuredWidth3 = this.mNextText.getMeasuredWidth();
        int i7 = measuredWidth2 / 2;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i8 = paddingRight + i7;
        int i9 = (width - (paddingLeft + i7)) - i8;
        float f3 = f2 + 0.5f;
        if (f3 > 1.0f) {
            f3 -= 1.0f;
        }
        int i10 = ((width - i8) - ((int) (((float) i9) * f3))) - (measuredWidth2 / 2);
        int i11 = i10 + measuredWidth2;
        int baseline = this.mPrevText.getBaseline();
        int baseline2 = this.mCurrText.getBaseline();
        int baseline3 = this.mNextText.getBaseline();
        int max = Math.max(Math.max(baseline, baseline2), baseline3);
        int i12 = max - baseline;
        int i13 = max - baseline2;
        int i14 = max - baseline3;
        int measuredHeight = i14 + this.mNextText.getMeasuredHeight();
        int max2 = Math.max(Math.max(i12 + this.mPrevText.getMeasuredHeight(), i13 + this.mCurrText.getMeasuredHeight()), measuredHeight);
        switch (this.mGravity & 112) {
            case 16:
                i2 = (((height - paddingTop) - paddingBottom) - max2) / 2;
                i3 = i2 + i12;
                i4 = i2 + i13;
                i5 = i2 + i14;
                break;
            case 80:
                int i15 = (height - paddingBottom) - max2;
                i3 = i15 + i12;
                i4 = i15 + i13;
                i5 = i15 + i14;
                break;
            default:
                i3 = paddingTop + i12;
                i4 = paddingTop + i13;
                i5 = paddingTop + i14;
                break;
        }
        this.mCurrText.layout(i10, i4, i11, i4 + this.mCurrText.getMeasuredHeight());
        int min = Math.min(paddingLeft, (i10 - this.mScaledTextSpacing) - measuredWidth);
        this.mPrevText.layout(min, i3, min + measuredWidth, i3 + this.mPrevText.getMeasuredHeight());
        i2 = Math.max((width - paddingRight) - measuredWidth3, i11 + this.mScaledTextSpacing);
        this.mNextText.layout(i2, i5, i2 + measuredWidth3, i5 + this.mNextText.getMeasuredHeight());
        this.mLastKnownPositionOffset = f2;
        this.mUpdatingPositions = false;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        int mode = MeasureSpec.getMode(i3);
        int mode2 = MeasureSpec.getMode(i4);
        int size = MeasureSpec.getSize(i3);
        int size2 = MeasureSpec.getSize(i4);
        if (mode != 1073741824) {
            IllegalStateException illegalStateException = r18;
            IllegalStateException illegalStateException2 = new IllegalStateException("Must measure with an exact width");
            throw illegalStateException;
        }
        int i5 = size2;
        int minHeight = getMinHeight();
        Object obj = null;
        int paddingTop = getPaddingTop() + getPaddingBottom();
        i5 -= paddingTop;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) size) * 0.8f), ExploreByTouchHelper.INVALID_ID);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(i5, ExploreByTouchHelper.INVALID_ID);
        this.mPrevText.measure(makeMeasureSpec, makeMeasureSpec2);
        this.mCurrText.measure(makeMeasureSpec, makeMeasureSpec2);
        this.mNextText.measure(makeMeasureSpec, makeMeasureSpec2);
        if (mode2 == 1073741824) {
            setMeasuredDimension(size, size2);
            return;
        }
        setMeasuredDimension(size, Math.max(minHeight, this.mCurrText.getMeasuredHeight() + paddingTop));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2 = z;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (this.mPager != null) {
            updateTextPositions(this.mLastKnownCurrentPage, this.mLastKnownPositionOffset >= 0.0f ? this.mLastKnownPositionOffset : 0.0f, true);
        }
    }

    /* access modifiers changed from: 0000 */
    public int getMinHeight() {
        int i = 0;
        Drawable background = getBackground();
        if (background != null) {
            i = background.getIntrinsicHeight();
        }
        return i;
    }
}
