package com.feedback.ui;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.feedback.b.b;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;

class j implements OnClickListener {
    final /* synthetic */ SendFeedback a;

    private j(SendFeedback sendFeedback) {
        this.a = sendFeedback;
    }

    /* synthetic */ j(SendFeedback sendFeedback, j jVar) {
        this(sendFeedback);
    }

    public void onClick(View view) {
        String str = null;
        if (this.a.d != null) {
            str = this.a.d.getText().toString();
        }
        if (d.a(str)) {
            Toast.makeText(this.a, this.a.getString(l.a(this.a, "string", "UMEmptyFbNotAllowed")), 0).show();
        } else if (str.length() > 140) {
            Toast.makeText(this.a, this.a.getString(l.a(this.a, "string", "UMContentTooLong")), 0).show();
        } else {
            int selectedItemPosition = this.a.b != null ? this.a.b.getSelectedItemPosition() : -1;
            int selectedItemPosition2 = this.a.c != null ? this.a.c.getSelectedItemPosition() : -1;
            Editor edit = this.a.getSharedPreferences(UmengConstants.PreName_Trivial, 0).edit();
            edit.putInt(UmengConstants.TrivialPreKey_AgeGroup, selectedItemPosition);
            edit.putInt(UmengConstants.TrivialPreKey_Sex, selectedItemPosition2);
            edit.commit();
            try {
                this.a.h = b.a(this.a, str, selectedItemPosition, selectedItemPosition2);
                c.b(this.a, this.a.h);
                new com.feedback.c.b(this.a.h, this.a).start();
                this.a.startActivity(new Intent(this.a, FeedbackConversations.class).setFlags(131072));
                ((InputMethodManager) this.a.getSystemService("input_method")).hideSoftInputFromWindow(this.a.d.getWindowToken(), 0);
                this.a.finish();
            } catch (Exception e) {
                e.printStackTrace();
                c.c(this.a, this.a.h);
            }
        }
    }
}
