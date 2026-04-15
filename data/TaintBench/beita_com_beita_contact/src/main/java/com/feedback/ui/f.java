package com.feedback.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.feedback.b.c;

class f extends BroadcastReceiver {
    g a;
    final /* synthetic */ FeedbackConversations b;

    public f(FeedbackConversations feedbackConversations, g gVar) {
        this.b = feedbackConversations;
        this.a = gVar;
    }

    public void onReceive(Context context, Intent intent) {
        this.a.a(c.a(this.b));
        this.a.notifyDataSetChanged();
    }
}
