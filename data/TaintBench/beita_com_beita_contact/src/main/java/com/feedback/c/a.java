package com.feedback.c;

import android.content.Context;
import java.util.concurrent.Callable;
import org.json.JSONObject;

public class a implements Callable {
    static String a = "MsgWorker";
    JSONObject b;
    Context c;

    public a(JSONObject jSONObject, Context context) {
        this.b = jSONObject;
        this.c = context;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x003a  */
    /* renamed from: a */
    public java.lang.Boolean call() {
        /*
        r6 = this;
        r5 = 0;
        r0 = r6.b;
        r1 = "feedback_id";
        r0 = com.feedback.b.b.a(r0, r1);
        r1 = new android.content.Intent;
        r1.<init>();
        r2 = "postFeedbackFinished";
        r1 = r1.setAction(r2);
        r2 = "type";
        r3 = "user_reply";
        r1 = r1.putExtra(r2, r3);
        r2 = "feedback_id";
        r0 = r1.putExtra(r2, r0);
        r1 = r6.b;	 Catch:{ Exception -> 0x0060 }
        r2 = "http://feedback.whalecloud.com/feedback/reply";
        r3 = "reply";
        r1 = com.feedback.b.d.a(r1, r2, r3);	 Catch:{ Exception -> 0x0060 }
        if (r1 == 0) goto L_0x0064;
    L_0x002e:
        r2 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0060 }
        r2.<init>(r1);	 Catch:{ Exception -> 0x0060 }
        r1 = r2;
    L_0x0034:
        r2 = com.feedback.b.b.b(r1);
        if (r2 == 0) goto L_0x006b;
    L_0x003a:
        r2 = r6.b;
        com.feedback.b.b.f(r2);
        r2 = r6.b;	 Catch:{ JSONException -> 0x0066 }
        r3 = "reply_id";
        r4 = "reply_id";
        r1 = r1.getString(r4);	 Catch:{ JSONException -> 0x0066 }
        com.feedback.b.b.a(r2, r3, r1);	 Catch:{ JSONException -> 0x0066 }
    L_0x004c:
        r1 = "PostFeedbackBroadcast";
        r2 = "succeed";
        r0.putExtra(r1, r2);
    L_0x0053:
        r1 = r6.c;
        r2 = r6.b;
        com.feedback.b.c.a(r1, r2);
        r1 = r6.c;
        r1.sendBroadcast(r0);
        return r5;
    L_0x0060:
        r1 = move-exception;
        r1.printStackTrace();
    L_0x0064:
        r1 = r5;
        goto L_0x0034;
    L_0x0066:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x004c;
    L_0x006b:
        r1 = r6.b;
        com.feedback.b.b.d(r1);
        r1 = "PostFeedbackBroadcast";
        r2 = "fail";
        r0.putExtra(r1, r2);
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.feedback.c.a.call():java.lang.Boolean");
    }
}
