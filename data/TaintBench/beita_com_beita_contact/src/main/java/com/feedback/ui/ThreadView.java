package com.feedback.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ThreadView extends ListView {
    public ThreadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i4 > i2) {
            setSelection(getAdapter().getCount() - 1);
        }
    }
}
