package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LinearLayoutICS extends LinearLayout {
    private static final int SHOW_DIVIDER_BEGINNING = 1;
    private static final int SHOW_DIVIDER_END = 4;
    private static final int SHOW_DIVIDER_MIDDLE = 2;
    private static final int SHOW_DIVIDER_NONE = 0;
    private final Drawable mDivider;
    private final int mDividerHeight;
    private final int mDividerPadding;
    private final int mDividerWidth;
    private final int mShowDividers;

    public LinearLayoutICS(Context context, AttributeSet attrs) {
        boolean z = true;
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutICS);
        this.mDivider = a.getDrawable(0);
        if (this.mDivider != null) {
            this.mDividerWidth = this.mDivider.getIntrinsicWidth();
            this.mDividerHeight = this.mDivider.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        this.mShowDividers = a.getInt(1, 0);
        this.mDividerPadding = a.getDimensionPixelSize(2, 0);
        a.recycle();
        if (this.mDivider != null) {
            z = false;
        }
        setWillNotDraw(z);
    }

    public int getSupportDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (getOrientation() == 1) {
                drawSupportDividersVertical(canvas);
            } else {
                drawSupportDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        if (this.mDivider != null) {
            int childIndex = indexOfChild(child);
            int count = getChildCount();
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (getOrientation() == 1) {
                if (hasSupportDividerBeforeChildAt(childIndex)) {
                    params.topMargin = this.mDividerHeight;
                } else if (childIndex == count - 1 && hasSupportDividerBeforeChildAt(count)) {
                    params.bottomMargin = this.mDividerHeight;
                }
            } else if (hasSupportDividerBeforeChildAt(childIndex)) {
                params.leftMargin = this.mDividerWidth;
            } else if (childIndex == count - 1 && hasSupportDividerBeforeChildAt(count)) {
                params.rightMargin = this.mDividerWidth;
            }
        }
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    /* access modifiers changed from: 0000 */
    public void drawSupportDividersVertical(Canvas canvas) {
        View child;
        int count = getChildCount();
        int i = 0;
        while (i < count) {
            child = getChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasSupportDividerBeforeChildAt(i))) {
                drawSupportHorizontalDivider(canvas, child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin);
            }
            i++;
        }
        if (hasSupportDividerBeforeChildAt(count)) {
            int bottom;
            child = getChildAt(count - 1);
            if (child == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child.getBottom();
            }
            drawSupportHorizontalDivider(canvas, bottom);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawSupportDividersHorizontal(Canvas canvas) {
        View child;
        int count = getChildCount();
        int i = 0;
        while (i < count) {
            child = getChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasSupportDividerBeforeChildAt(i))) {
                drawSupportVerticalDivider(canvas, child.getLeft() - ((LayoutParams) child.getLayoutParams()).leftMargin);
            }
            i++;
        }
        if (hasSupportDividerBeforeChildAt(count)) {
            int right;
            child = getChildAt(count - 1);
            if (child == null) {
                right = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            } else {
                right = child.getRight();
            }
            drawSupportVerticalDivider(canvas, right);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawSupportHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, top, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + top);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawSupportVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public boolean hasSupportDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            if ((this.mShowDividers & 1) != 0) {
                return true;
            }
            return false;
        } else if (childIndex == getChildCount()) {
            if ((this.mShowDividers & 4) == 0) {
                return false;
            }
            return true;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            boolean hasVisibleViewBefore = false;
            for (int i = childIndex - 1; i >= 0; i--) {
                if (getChildAt(i).getVisibility() != 8) {
                    hasVisibleViewBefore = true;
                    break;
                }
            }
            return hasVisibleViewBefore;
        }
    }
}
