package com.picksbrowser;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: PicksBrowser */
final class d implements OnClickListener {
    final /* synthetic */ PicksBrowser a;

    d(PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onClick(View v) {
        if (this.a.d.canGoBack()) {
            this.a.d.goBack();
        }
    }
}
