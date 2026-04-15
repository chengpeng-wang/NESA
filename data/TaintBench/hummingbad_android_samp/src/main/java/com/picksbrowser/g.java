package com.picksbrowser;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: PicksBrowser */
final class g implements OnClickListener {
    final /* synthetic */ PicksBrowser a;

    g(PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onClick(View v) {
        this.a.finish();
    }
}
