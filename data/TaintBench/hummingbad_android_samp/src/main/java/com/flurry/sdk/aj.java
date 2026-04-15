package com.flurry.sdk;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

final class aj implements OnClickListener {
    final /* synthetic */ ag a;

    aj(ag agVar) {
        this.a = agVar;
    }

    public final void onClick(View view) {
        View view2 = (View) this.a.g.get();
        if (view2 != null && !this.a.h) {
            Log.i(ag.e, "On item clicked" + view2.getClass());
            this.a.p();
            this.a.q();
        }
    }
}
