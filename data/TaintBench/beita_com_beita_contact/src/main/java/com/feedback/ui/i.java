package com.feedback.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import com.feedback.b.a;

class i implements OnClickListener {
    final /* synthetic */ SendFeedback a;

    i(SendFeedback sendFeedback) {
        this.a = sendFeedback;
    }

    public void onClick(View view) {
        a.b(this.a);
        this.a.finish();
        ((InputMethodManager) this.a.getSystemService("input_method")).hideSoftInputFromWindow(this.a.d.getWindowToken(), 0);
    }
}
