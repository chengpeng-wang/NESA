package com.facebook.ads.internal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class e extends LinearLayout {
    private Bitmap a;
    private Bitmap b;
    private ImageView c;
    private ImageView d;
    private ImageView e;
    private Bitmap f;
    private int g;
    private int h;

    public e(Context context) {
        super(context);
        b();
    }

    private void a() {
        if (getHeight() > 0) {
            this.h = c();
            this.g = (int) Math.ceil((double) (((float) (getHeight() - this.h)) / 2.0f));
            Matrix matrix = new Matrix();
            matrix.preScale(1.0f, -1.0f);
            int floor = (int) Math.floor((double) (((float) (getHeight() - this.h)) / 2.0f));
            float height = ((float) this.a.getHeight()) / ((float) this.h);
            int round = Math.round(((float) this.g) * height);
            if (round > 0) {
                this.f = Bitmap.createBitmap(this.b, 0, 0, this.b.getWidth(), round, matrix, true);
                this.c.setImageBitmap(this.f);
            }
            round = Math.round(((float) floor) * height);
            if (round > 0) {
                this.e.setImageBitmap(Bitmap.createBitmap(this.b, 0, this.b.getHeight() - round, this.b.getWidth(), round, matrix, true));
            }
        }
    }

    private void b() {
        getContext().getResources().getDisplayMetrics();
        setOrientation(1);
        this.c = new ImageView(getContext());
        this.c.setScaleType(ScaleType.FIT_XY);
        addView(this.c);
        this.d = new ImageView(getContext());
        this.d.setLayoutParams(new LayoutParams(-1, -1));
        this.d.setScaleType(ScaleType.FIT_CENTER);
        addView(this.d);
        this.e = new ImageView(getContext());
        this.e.setScaleType(ScaleType.FIT_XY);
        addView(this.e);
    }

    private int c() {
        return (int) Math.round(((double) getWidth()) / 1.91d);
    }

    public void a(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            this.d.setImageDrawable(null);
            return;
        }
        this.d.setImageBitmap(Bitmap.createBitmap(bitmap));
        this.a = bitmap;
        this.b = bitmap2;
        bitmap.getHeight();
        bitmap.getWidth();
        a();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.a == null || this.b == null) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int c = c();
        if (this.f == null || this.h != c) {
            a();
        }
        this.c.layout(i, i2, i3, this.g);
        this.d.layout(i, this.g + i2, i3, this.g + this.h);
        this.e.layout(i, (this.g + i2) + this.h, i3, i4);
    }
}
