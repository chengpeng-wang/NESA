package com.feedback;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

class a implements OnClickListener {
    private final /* synthetic */ AlertDialog a;

    a(AlertDialog alertDialog) {
        this.a = alertDialog;
    }

    public void onClick(View view) {
        this.a.dismiss();
    }
}
