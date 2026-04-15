package com.feedback.b;

import android.content.Context;
import android.content.Intent;
import com.feedback.a.d;
import com.feedback.a.e;
import com.feedback.ui.FeedbackConversation;
import com.feedback.ui.FeedbackConversations;
import com.feedback.ui.SendFeedback;
import com.mobclick.android.UmengConstants;

public class a {
    public static void a(Context context) {
        Intent intent = new Intent(context, SendFeedback.class);
        intent.setFlags(131072);
        context.startActivity(intent);
    }

    public static void a(Context context, d dVar) {
        Intent intent = new Intent(context, SendFeedback.class);
        intent.setFlags(131072);
        if (dVar != null && dVar.b == e.PureFail) {
            intent.putExtra(UmengConstants.AtomKey_SequenceNum, dVar.c);
        }
        context.startActivity(intent);
    }

    public static void b(Context context) {
        Intent intent = new Intent(context, FeedbackConversations.class);
        intent.setFlags(131072);
        context.startActivity(intent);
    }

    public static void b(Context context, d dVar) {
        FeedbackConversation.setUserContext(context);
        Intent intent = new Intent(context, FeedbackConversation.class);
        intent.setFlags(131072);
        if (dVar.b == e.PureSending) {
            intent.putExtra(UmengConstants.AtomKey_SequenceNum, dVar.c);
        } else {
            intent.putExtra(UmengConstants.AtomKey_FeedbackID, dVar.c);
        }
        context.startActivity(intent);
    }

    public static void c(Context context) {
        if (c.a(context).size() == 0) {
            a(context);
        } else {
            b(context);
        }
    }
}
