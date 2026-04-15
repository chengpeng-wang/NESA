package com.feedback.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.feedback.a.a;
import com.feedback.a.b;
import com.feedback.a.d;
import com.mobclick.android.l;

public class c extends BaseAdapter {
    private static /* synthetic */ int[] f;
    Context a;
    LayoutInflater b;
    String c;
    String d = "FeedbackAdapter";
    d e;

    public c(Context context, d dVar) {
        this.a = context;
        this.e = dVar;
        this.b = LayoutInflater.from(context);
    }

    private void a(a aVar, TextView textView) {
        switch (a()[aVar.g.ordinal()]) {
            case 1:
                textView.setText(this.a.getString(l.a(this.a, "string", "UMFb_Atom_State_Sending")));
                textView.setTextColor(-7829368);
                return;
            case 2:
                textView.setText(this.a.getString(l.a(this.a, "string", "UMFb_Atom_State_Resend")));
                textView.setTextColor(-65536);
                return;
            default:
                String b = com.feedback.b.d.b(aVar.e, this.a);
                if ("".equals(b)) {
                    textView.setText("");
                    return;
                }
                textView.setText(b);
                textView.setTextColor(-7829368);
                return;
        }
    }

    static /* synthetic */ int[] a() {
        int[] iArr = f;
        if (iArr == null) {
            iArr = new int[b.values().length];
            try {
                iArr[b.Fail.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[b.OK.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[b.Sending.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            f = iArr;
        }
        return iArr;
    }

    public void a(d dVar) {
        this.e = dVar;
    }

    public int getCount() {
        return this.e == null ? 0 : this.e.f.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate;
        d dVar;
        if (view == null) {
            inflate = this.b.inflate(l.a(this.a, "layout", "umeng_analyse_feedback_conversation_item"), null);
            d dVar2 = new d(this);
            dVar2.a = (LinearLayout) inflate.findViewById(l.a(this.a, "id", "umeng_analyse_atomLinearLayout"));
            dVar2.b = (RelativeLayout) dVar2.a.findViewById(l.a(this.a, "id", "umeng_analyse_bubble"));
            dVar2.c = (TextView) dVar2.a.findViewById(l.a(this.a, "id", "umeng_analyse_atomtxt"));
            dVar2.d = (TextView) dVar2.a.findViewById(l.a(this.a, "id", "umeng_analyse_stateOrTime"));
            dVar2.e = inflate.findViewById(l.a(this.a, "id", "umeng_analyse_atom_left_margin"));
            dVar2.f = inflate.findViewById(l.a(this.a, "id", "umeng_analyse_atom_right_margin"));
            inflate.setTag(dVar2);
            dVar = dVar2;
        } else {
            dVar = (d) view.getTag();
            inflate = view;
        }
        a a = this.e.a(i);
        a(a, dVar.d);
        dVar.c.setText(a.a());
        if (a.f == com.feedback.a.c.DevReply) {
            dVar.a.setGravity(5);
            dVar.b.setBackgroundResource(l.a(this.a, "drawable", "umeng_analyse_dev_bubble"));
            dVar.f.setVisibility(8);
            dVar.e.setVisibility(0);
        } else {
            dVar.a.setGravity(3);
            dVar.b.setBackgroundResource(l.a(this.a, "drawable", "umeng_analyse_user_bubble"));
            dVar.f.setVisibility(0);
            dVar.e.setVisibility(8);
        }
        return inflate;
    }
}
