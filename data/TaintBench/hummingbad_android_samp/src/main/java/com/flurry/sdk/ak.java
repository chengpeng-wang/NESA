package com.flurry.sdk;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

final class ak implements OnTouchListener {
    final /* synthetic */ ag a;

    ak(ag agVar) {
        this.a = agVar;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        this.a.i.onTouchEvent(motionEvent);
        return true;
    }
}
