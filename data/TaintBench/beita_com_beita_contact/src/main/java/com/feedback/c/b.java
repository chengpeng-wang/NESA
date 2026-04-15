package com.feedback.c;

import android.content.Context;
import android.content.Intent;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import org.json.JSONObject;

public class b extends Thread {
    static String a = "PostFeedbackThread";
    JSONObject b;
    Context c;
    String d;
    String e;
    int f;
    int g;

    public b(JSONObject jSONObject, Context context) {
        com.feedback.b.b.c(jSONObject);
        this.b = jSONObject;
        this.c = context;
    }

    public void run() {
        String a;
        JSONObject jSONObject = null;
        Intent action = new Intent().setAction(UmengConstants.PostFeedbackBroadcastAction);
        try {
            if (UmengConstants.Atom_Type_NewFeedback.equals(this.b.optString(UmengConstants.AtomKey_Type))) {
                a = d.a(this.b, UmengConstants.FEEDBACK_NewFeedback_URL, UmengConstants.FeedbackPreName);
                action.putExtra(UmengConstants.AtomKey_Type, UmengConstants.FeedbackPreName);
                jSONObject = new JSONObject(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        a = com.feedback.b.b.a(this.b, UmengConstants.AtomKey_SequenceNum);
        c.a(this.c, UmengConstants.TempPreName, a);
        if (com.feedback.b.b.b(jSONObject)) {
            String a2 = com.feedback.b.b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
            com.feedback.b.b.f(this.b);
            com.feedback.b.b.a(this.b, UmengConstants.AtomKey_FeedbackID, a2);
            c.a(this.c, this.b);
            action.putExtra(UmengConstants.PostFeedbackBroadcast, UmengConstants.PostFeedbackBroadcast_Succeed);
            action.putExtra(UmengConstants.AtomKey_FeedbackID, a2);
        } else {
            com.feedback.b.b.d(this.b);
            c.c(this.c, this.b);
            action.putExtra(UmengConstants.AtomKey_SequenceNum, a);
            action.putExtra(UmengConstants.PostFeedbackBroadcast, "fail");
        }
        this.c.sendBroadcast(action);
    }
}
