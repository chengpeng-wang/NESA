package com.facebook.ads.internal.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class o extends View {
    private n a;

    public o(Context context, n nVar) {
        super(context);
        this.a = nVar;
        setLayoutParams(new LayoutParams(0, 0));
    }

    public void onWindowVisibilityChanged(int i) {
        if (this.a != null) {
            this.a.a(i);
        }
    }
}
