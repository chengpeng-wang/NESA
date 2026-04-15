package com.feedback.a;

import android.util.Log;
import com.feedback.b.b;
import com.mobclick.android.UmengConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class d implements Comparable {
    public String a;
    public e b;
    public String c;
    public a d;
    public a e;
    public List f;

    public d(JSONArray jSONArray) {
        this.a = d.class.getSimpleName();
        this.f = new ArrayList();
        this.b = e.Other;
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                this.f.add(new a(jSONArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!this.f.isEmpty()) {
            this.d = (a) this.f.get(0);
            this.e = (a) this.f.get(this.f.size() - 1);
            this.c = this.d.c;
        }
    }

    public d(JSONObject jSONObject) {
        this.a = d.class.getSimpleName();
        this.f = new ArrayList();
        String a = b.a(jSONObject, UmengConstants.AtomKey_State);
        if (UmengConstants.TempState.equalsIgnoreCase(a)) {
            this.b = e.PureSending;
        } else if ("fail".equalsIgnoreCase(a)) {
            this.b = e.PureFail;
        } else {
            Log.e(this.a, "Code should not get here");
        }
        this.d = new a(jSONObject);
        this.e = this.d;
        this.f.add(this.d);
        this.c = b.a(jSONObject, UmengConstants.AtomKey_SequenceNum);
    }

    /* renamed from: a */
    public int compareTo(d dVar) {
        Date date = this.e.e;
        Date date2 = dVar.e.e;
        return (date2 == null || date == null || date.equals(date2)) ? 0 : date.after(date2) ? -1 : 1;
    }

    public a a(int i) {
        return (i < 0 || i > this.f.size() - 1) ? null : (a) this.f.get(i);
    }

    public d a(a aVar) {
        this.f.add(aVar);
        return this;
    }

    public void b(int i) {
        if (i >= 0 && i <= this.f.size() - 1) {
            this.f.remove(i);
        }
    }
}
