package com.feedback.a;

import com.feedback.b.b;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.util.Date;
import org.json.JSONObject;

public class a implements Comparable {
    String a;
    String b;
    public String c;
    public String d;
    public Date e;
    public c f;
    public b g;

    public a(JSONObject jSONObject) {
        if (jSONObject == null) {
            throw new Exception("invalid atom");
        }
        String optString = jSONObject.optString(UmengConstants.AtomKey_Type);
        if (UmengConstants.Atom_Type_NewFeedback.equals(optString)) {
            this.f = c.Starting;
        } else if (UmengConstants.Atom_Type_DevReply.equals(optString)) {
            this.f = c.DevReply;
        } else if (UmengConstants.Atom_Type_UserReply.equals(optString)) {
            this.f = c.UserReply;
        }
        optString = b.a(jSONObject, UmengConstants.AtomKey_State);
        if (UmengConstants.TempState.equalsIgnoreCase(optString)) {
            this.g = b.Sending;
        } else if ("fail".equalsIgnoreCase(optString)) {
            this.g = b.Fail;
        } else if ("ok".equalsIgnoreCase(optString)) {
            this.g = b.OK;
        } else {
            this.g = b.OK;
        }
        if (this.f == c.Starting) {
            this.a = b.a(jSONObject, UmengConstants.AtomKey_Thread_Title);
        }
        this.b = b.a(jSONObject, UmengConstants.AtomKey_Content);
        this.c = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
        this.d = b.a(jSONObject, UmengConstants.AtomKey_SequenceNum);
        this.e = l.c(b.a(jSONObject, UmengConstants.AtomKey_Date));
    }

    /* renamed from: a */
    public int compareTo(a aVar) {
        Date date = aVar.e;
        return (this.e == null || date == null || date.equals(this.e)) ? 0 : date.after(this.e) ? -1 : 1;
    }

    public String a() {
        return this.f == c.Starting ? this.a : this.b;
    }
}
