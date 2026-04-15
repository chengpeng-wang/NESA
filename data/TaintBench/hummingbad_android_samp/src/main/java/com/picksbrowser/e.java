package com.picksbrowser;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: PicksBrowser */
final class e implements OnClickListener {
    final /* synthetic */ PicksBrowser a;

    e(PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onClick(View v) {
        if (this.a.d.canGoForward()) {
            this.a.d.goForward();
        }
    }
}
