package android.support.v4.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class PagerTabStrip extends PagerTitleStrip {
    private static final int FULL_UNDERLINE_HEIGHT = 1;
    private static final int INDICATOR_HEIGHT = 3;
    private static final int MIN_PADDING_BOTTOM = 6;
    private static final int MIN_STRIP_HEIGHT = 32;
    private static final int MIN_TEXT_SPACING = 64;
    private static final int TAB_PADDING = 16;
    private static final int TAB_SPACING = 32;
    private static final String TAG = "PagerTabStrip";
    private boolean mDrawFullUnderline;
    private boolean mDrawFullUnderlineSet;
    private int mFullUnderlineHeight;
    private boolean mIgnoreTap;
    private int mIndicatorColor;
    private int mIndicatorHeight;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mMinPaddingBottom;
    private int mMinStripHeight;
    private int mMinTextSpacing;
    private int mTabAlpha;
    private int mTabPadding;
    private final Paint mTabPaint;
    private final Rect mTempRect;
    private int mTouchSlop;

    public PagerTabStrip(Context context) {
        this(context, null);
    }

    public PagerTabStrip(Context context, AttributeSet attributeSet) {
        Context context2 = context;
        super(context2, attributeSet);
        Paint paint = r9;
        Paint paint2 = new Paint();
        this.mTabPaint = paint;
        Rect rect = r9;
        Rect rect2 = new Rect();
        this.mTempRect = rect;
        this.mTabAlpha = MotionEventCompat.ACTION_MASK;
        this.mDrawFullUnderline = false;
        this.mDrawFullUnderlineSet = false;
        this.mIndicatorColor = this.mTextColor;
        this.mTabPaint.setColor(this.mIndicatorColor);
        float f = context2.getResources().getDisplayMetrics().density;
        this.mIndicatorHeight = (int) ((3.0f * f) + 0.5f);
        this.mMinPaddingBottom = (int) ((6.0f * f) + 0.5f);
        this.mMinTextSpacing = (int) (64.0f * f);
        this.mTabPadding = (int) ((16.0f * f) + 0.5f);
        this.mFullUnderlineHeight = (int) ((1.0f * f) + 0.5f);
        this.mMinStripHeight = (int) ((32.0f * f) + 0.5f);
        this.mTouchSlop = ViewConfiguration.get(context2).getScaledTouchSlop();
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setTextSpacing(getTextSpacing());
        setWillNotDraw(false);
        this.mPrevText.setFocusable(true);
        TextView textView = this.mPrevText;
        AnonymousClass1 anonymousClass1 = r9;
        AnonymousClass1 anonymousClass12 = new OnClickListener(this) {
            final /* synthetic */ PagerTabStrip this$0;

            {
                this.this$0 = r5;
            }

            public void onClick(View view) {
                View view2 = view;
                this.this$0.mPager.setCurrentItem(this.this$0.mPager.getCurrentItem() - 1);
            }
        };
        textView.setOnClickListener(anonymousClass1);
        this.mNextText.setFocusable(true);
        textView = this.mNextText;
        AnonymousClass2 anonymousClass2 = r9;
        AnonymousClass2 anonymousClass22 = new OnClickListener(this) {
            final /* synthetic */ PagerTabStrip this$0;

            {
                this.this$0 = r5;
            }

            public void onClick(View view) {
                View view2 = view;
                this.this$0.mPager.setCurrentItem(this.this$0.mPager.getCurrentItem() + 1);
            }
        };
        textView.setOnClickListener(anonymousClass2);
        if (getBackground() == null) {
            this.mDrawFullUnderline = true;
        }
    }

    public void setTabIndicatorColor(int i) {
        this.mIndicatorColor = i;
        this.mTabPaint.setColor(this.mIndicatorColor);
        invalidate();
    }

    public void setTabIndicatorColorResource(int i) {
        setTabIndicatorColor(getContext().getResources().getColor(i));
    }

    public int getTabIndicatorColor() {
        return this.mIndicatorColor;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (i8 < this.mMinPaddingBottom) {
            i8 = this.mMinPaddingBottom;
        }
        super.setPadding(i5, i6, i7, i8);
    }

    public void setTextSpacing(int i) {
        int i2 = i;
        if (i2 < this.mMinTextSpacing) {
            i2 = this.mMinTextSpacing;
        }
        super.setTextSpacing(i2);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        Drawable drawable2 = drawable;
        super.setBackgroundDrawable(drawable2);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = drawable2 == null;
        }
    }

    public void setBackgroundColor(int i) {
        int i2 = i;
        super.setBackgroundColor(i2);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = (i2 & ViewCompat.MEASURED_STATE_MASK) == 0;
        }
    }

    public void setBackgroundResource(int i) {
        int i2 = i;
        super.setBackgroundResource(i2);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = i2 == 0;
        }
    }

    public void setDrawFullUnderline(boolean z) {
        this.mDrawFullUnderline = z;
        this.mDrawFullUnderlineSet = true;
        invalidate();
    }

    public boolean getDrawFullUnderline() {
        return this.mDrawFullUnderline;
    }

    /* access modifiers changed from: 0000 */
    public int getMinHeight() {
        return Math.max(super.getMinHeight(), this.mMinStripHeight);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        int action = motionEvent2.getAction();
        if (action != 0 && this.mIgnoreTap) {
            return false;
        }
        float x = motionEvent2.getX();
        float y = motionEvent2.getY();
        switch (action) {
            case 0:
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                this.mIgnoreTap = false;
                break;
            case 1:
                if (x >= ((float) (this.mCurrText.getLeft() - this.mTabPadding))) {
                    if (x > ((float) (this.mCurrText.getRight() + this.mTabPadding))) {
                        this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
                        break;
                    }
                }
                this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
                break;
                break;
            case 2:
                if (Math.abs(x - this.mInitialMotionX) > ((float) this.mTouchSlop) || Math.abs(y - this.mInitialMotionY) > ((float) this.mTouchSlop)) {
                    this.mIgnoreTap = true;
                    break;
                }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas2);
        int height = getHeight();
        int i = height;
        int left = this.mCurrText.getLeft() - this.mTabPadding;
        int right = this.mCurrText.getRight() + this.mTabPadding;
        int i2 = i - this.mIndicatorHeight;
        this.mTabPaint.setColor((this.mTabAlpha << 24) | (this.mIndicatorColor & ViewCompat.MEASURED_SIZE_MASK));
        canvas2.drawRect((float) left, (float) i2, (float) right, (float) i, this.mTabPaint);
        if (this.mDrawFullUnderline) {
            this.mTabPaint.setColor(ViewCompat.MEASURED_STATE_MASK | (this.mIndicatorColor & ViewCompat.MEASURED_SIZE_MASK));
            canvas2.drawRect((float) getPaddingLeft(), (float) (height - this.mFullUnderlineHeight), (float) (getWidth() - getPaddingRight()), (float) height, this.mTabPaint);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateTextPositions(int i, float f, boolean z) {
        int i2 = i;
        float f2 = f;
        boolean z2 = z;
        Rect rect = this.mTempRect;
        int height = getHeight();
        int i3 = height - this.mIndicatorHeight;
        rect.set(this.mCurrText.getLeft() - this.mTabPadding, i3, this.mCurrText.getRight() + this.mTabPadding, height);
        super.updateTextPositions(i2, f2, z2);
        this.mTabAlpha = (int) ((Math.abs(f2 - 0.5f) * 2.0f) * 255.0f);
        rect.union(this.mCurrText.getLeft() - this.mTabPadding, i3, this.mCurrText.getRight() + this.mTabPadding, height);
        invalidate(rect);
    }
}
