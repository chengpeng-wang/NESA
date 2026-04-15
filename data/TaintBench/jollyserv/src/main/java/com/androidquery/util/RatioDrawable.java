package com.androidquery.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.lang.ref.WeakReference;

public class RatioDrawable extends BitmapDrawable {
    private boolean adjusted;
    private float anchor;
    private Matrix m;
    private float ratio;
    private WeakReference<ImageView> ref;
    private int w;

    public RatioDrawable(Resources res, Bitmap bm, ImageView iv, float ratio, float anchor) {
        super(res, bm);
        this.ref = new WeakReference(iv);
        this.ratio = ratio;
        this.anchor = anchor;
        iv.setScaleType(ScaleType.MATRIX);
        iv.setImageMatrix(new Matrix());
        adjust(iv, bm, false);
    }

    private int getWidth(ImageView iv) {
        int width = 0;
        LayoutParams lp = iv.getLayoutParams();
        if (lp != null) {
            width = lp.width;
        }
        if (width <= 0) {
            width = iv.getWidth();
        }
        if (width > 0) {
            return (width - iv.getPaddingLeft()) - iv.getPaddingRight();
        }
        return width;
    }

    public void draw(Canvas canvas) {
        ImageView iv = null;
        if (this.ref != null) {
            iv = (ImageView) this.ref.get();
        }
        if (this.ratio == 0.0f || iv == null) {
            super.draw(canvas);
        } else {
            draw(canvas, iv, getBitmap());
        }
    }

    private void draw(Canvas canvas, ImageView iv, Bitmap bm) {
        Matrix m = getMatrix(iv, bm);
        if (m != null) {
            int vpad = iv.getPaddingTop() + iv.getPaddingBottom();
            int hpad = iv.getPaddingLeft() + iv.getPaddingRight();
            if (vpad > 0 || hpad > 0) {
                canvas.clipRect(0, 0, iv.getWidth() - hpad, iv.getHeight() - vpad);
            }
            canvas.drawBitmap(bm, m, getPaint());
        }
        if (!this.adjusted) {
            adjust(iv, bm, true);
        }
    }

    private void adjust(ImageView iv, Bitmap bm, boolean done) {
        int vw = getWidth(iv);
        if (vw > 0) {
            int th = (targetHeight(bm.getWidth(), bm.getHeight(), vw) + iv.getPaddingTop()) + iv.getPaddingBottom();
            LayoutParams lp = iv.getLayoutParams();
            if (lp != null) {
                if (th != lp.height) {
                    lp.height = th;
                    iv.setLayoutParams(lp);
                }
                if (done) {
                    this.adjusted = true;
                }
            }
        }
    }

    private int targetHeight(int dw, int dh, int vw) {
        float r = this.ratio;
        if (this.ratio == Float.MAX_VALUE) {
            r = ((float) dh) / ((float) dw);
        }
        return (int) (((float) vw) * r);
    }

    private Matrix getMatrix(ImageView iv, Bitmap bm) {
        int dw = bm.getWidth();
        if (this.m != null && dw == this.w) {
            return this.m;
        }
        int dh = bm.getHeight();
        int vw = getWidth(iv);
        int vh = targetHeight(dw, dh, vw);
        if (dw <= 0 || dh <= 0 || vw <= 0 || vh <= 0) {
            return null;
        }
        if (this.m == null || dw != this.w) {
            float scale;
            float dx = 0.0f;
            float dy = 0.0f;
            this.m = new Matrix();
            if (dw * vh >= vw * dh) {
                scale = ((float) vh) / ((float) dh);
                dx = (((float) vw) - (((float) dw) * scale)) * 0.5f;
            } else {
                scale = ((float) vw) / ((float) dw);
                dy = (((float) vh) - (((float) dh) * scale)) * getYOffset(dw, dh);
            }
            this.m.setScale(scale, scale);
            this.m.postTranslate(dx, dy);
            this.w = dw;
        }
        return this.m;
    }

    private float getYOffset(int vwidth, int vheight) {
        if (this.anchor != Float.MAX_VALUE) {
            return (1.0f - this.anchor) / 2.0f;
        }
        return 0.25f + ((1.5f - Math.max(1.0f, Math.min(1.5f, ((float) vheight) / ((float) vwidth)))) / 2.0f);
    }
}
