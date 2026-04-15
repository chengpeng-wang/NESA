package com.facebook.ads.internal.view;

import android.content.Context;
import android.widget.RelativeLayout;

public class l extends RelativeLayout {
    private int a = 0;
    private int b = 0;

    public l(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.b > 0 && getMeasuredWidth() > this.b) {
            setMeasuredDimension(this.b, getMeasuredHeight());
        } else if (getMeasuredWidth() < this.a) {
            setMeasuredDimension(this.a, getMeasuredHeight());
        }
    }

    /* access modifiers changed from: protected */
    public void setMaxWidth(int i) {
        this.b = i;
    }

    public void setMinWidth(int i) {
        this.a = i;
    }
}
