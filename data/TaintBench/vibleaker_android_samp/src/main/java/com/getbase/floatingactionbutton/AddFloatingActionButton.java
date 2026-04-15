package com.getbase.floatingactionbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

public class AddFloatingActionButton extends FloatingActionButton {
    int mPlusColor;

    public AddFloatingActionButton(Context context) {
        this(context, null);
    }

    public AddFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: 0000 */
    public void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.AddFloatingActionButton, 0, 0);
        this.mPlusColor = attr.getColor(R.styleable.AddFloatingActionButton_fab_plusIconColor, getColor(17170443));
        attr.recycle();
        super.init(context, attributeSet);
    }

    public int getPlusColor() {
        return this.mPlusColor;
    }

    public void setPlusColorResId(@ColorRes int plusColor) {
        setPlusColor(getColor(plusColor));
    }

    public void setPlusColor(int color) {
        if (this.mPlusColor != color) {
            this.mPlusColor = color;
            updateBackground();
        }
    }

    public void setIcon(@DrawableRes int icon) {
        throw new UnsupportedOperationException("Use FloatingActionButton if you want to use custom icon");
    }

    /* access modifiers changed from: 0000 */
    public Drawable getIconDrawable() {
        final float iconSize = getDimension(R.dimen.fab_icon_size);
        final float iconHalfSize = iconSize / 2.0f;
        final float plusHalfStroke = getDimension(R.dimen.fab_plus_icon_stroke) / 2.0f;
        final float plusOffset = (iconSize - getDimension(R.dimen.fab_plus_icon_size)) / 2.0f;
        ShapeDrawable drawable = new ShapeDrawable(new Shape() {
            public void draw(Canvas canvas, Paint paint) {
                canvas.drawRect(plusOffset, iconHalfSize - plusHalfStroke, iconSize - plusOffset, plusHalfStroke + iconHalfSize, paint);
                canvas.drawRect(iconHalfSize - plusHalfStroke, plusOffset, plusHalfStroke + iconHalfSize, iconSize - plusOffset, paint);
            }
        });
        Paint paint = drawable.getPaint();
        paint.setColor(this.mPlusColor);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        return drawable;
    }
}
