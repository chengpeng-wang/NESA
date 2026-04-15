package com.picksbrowser;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: PicksBrowser */
final class f implements OnClickListener {
    final /* synthetic */ PicksBrowser a;

    f(PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onClick(View v) {
        this.a.d.reload();
    }
}
