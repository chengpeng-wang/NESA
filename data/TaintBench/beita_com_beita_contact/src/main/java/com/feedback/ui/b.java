package com.feedback.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.feedback.a.e;
import com.feedback.b.c;
import com.mobclick.android.UmengConstants;

class b extends BroadcastReceiver {
    final /* synthetic */ FeedbackConversation a;

    private b(FeedbackConversation feedbackConversation) {
        this.a = feedbackConversation;
    }

    /* synthetic */ b(FeedbackConversation feedbackConversation, b bVar) {
        this(feedbackConversation);
    }

    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(UmengConstants.AtomKey_FeedbackID);
        String stringExtra2 = intent.getStringExtra(UmengConstants.AtomKey_SequenceNum);
        if (this.a.e.b == e.Other && this.a.e.c.equalsIgnoreCase(stringExtra)) {
            this.a.e = c.b(this.a, stringExtra);
            this.a.f.a(this.a.e);
            this.a.f.notifyDataSetChanged();
        } else if (this.a.e.b == e.PureSending && this.a.e.c.equalsIgnoreCase(stringExtra2)) {
            this.a.e = c.b(this.a, stringExtra2, "fail");
            this.a.f.a(this.a.e);
            this.a.f.notifyDataSetChanged();
        }
    }
}
