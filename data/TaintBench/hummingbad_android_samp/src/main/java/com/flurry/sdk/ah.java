package com.flurry.sdk;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

final class ah extends SimpleOnGestureListener {
    final /* synthetic */ ag a;

    ah(ag agVar) {
        this.a = agVar;
    }

    public final boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public final boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        View view = (View) this.a.g.get();
        if (!(view == null || this.a.h)) {
            Log.i(ag.e, "On item clicked" + view.getClass());
            this.a.p();
            this.a.q();
        }
        return false;
    }
}
