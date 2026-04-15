package com.feedback.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.feedback.a.b;
import com.feedback.a.c;
import com.feedback.a.d;
import com.feedback.a.e;
import com.mobclick.android.l;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;

public class g extends BaseAdapter {
    public static int h = -9;
    LayoutInflater a;
    Context b;
    List c;
    JSONArray d;
    String[] e;
    String f = "";
    String g = "FeedbackListAdapter";

    public g(Context context, List list) {
        this.b = context;
        this.a = LayoutInflater.from(context);
        Collections.sort(list);
        this.c = list;
    }

    private String a(d dVar) {
        return dVar.d.a();
    }

    private String b(d dVar) {
        if (dVar.b == e.Other) {
            for (int size = dVar.f.size() - 1; size >= 0; size--) {
                b bVar = dVar.a(size).g;
                if (bVar == b.Sending) {
                    return this.b.getString(l.a(this.b, "string", "UMFbList_ListItem_State_Sending"));
                }
                if (bVar == b.Fail) {
                    return this.b.getString(l.a(this.b, "string", "UMFbList_ListItem_State_Fail"));
                }
            }
        } else if (dVar.b == e.PureFail) {
            return this.b.getString(l.a(this.b, "string", "UMFbList_ListItem_State_ReSend"));
        } else {
            if (dVar.b == e.PureSending) {
                return this.b.getString(l.a(this.b, "string", "UMFbList_ListItem_State_Sending"));
            }
        }
        return "";
    }

    private String c(d dVar) {
        return (dVar.f.size() == 1 || dVar.e.f != c.DevReply) ? null : dVar.e.a();
    }

    private String d(d dVar) {
        return com.feedback.b.d.a(dVar.e.e, this.b);
    }

    public e a(int i) {
        return ((d) this.c.get(i)).b;
    }

    public void a(List list) {
        Collections.sort(list);
        this.c = list;
    }

    public d b(int i) {
        return (d) this.c.get(i);
    }

    public int getCount() {
        return this.c.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        h hVar;
        if (view == null || view.getTag() == null) {
            View inflate = this.a.inflate(l.a(this.b, "layout", "umeng_analyse_feedback_conversations_item"), null);
            h hVar2 = new h(this);
            hVar2.a = (ImageView) inflate.findViewById(l.a(this.b, "id", "umeng_analyse_new_reply_notifier"));
            hVar2.b = (TextView) inflate.findViewById(l.a(this.b, "id", "umeng_analyse_feedbackpreview"));
            hVar2.c = (TextView) inflate.findViewById(l.a(this.b, "id", "umeng_analyse_dev_reply"));
            hVar2.d = (TextView) inflate.findViewById(l.a(this.b, "id", "umeng_analyse_state_or_date"));
            inflate.setTag(hVar2);
            h hVar3 = hVar2;
            view2 = inflate;
            hVar = hVar3;
        } else {
            hVar = (h) view.getTag();
            view2 = view;
        }
        d dVar = (d) this.c.get(i);
        String a = a(dVar);
        String c = c(dVar);
        String b = b(dVar);
        String d = d(dVar);
        hVar.b.setText(a);
        if (c == null) {
            hVar.c.setVisibility(8);
        } else {
            hVar.c.setVisibility(0);
            hVar.c.setText(c);
        }
        if (com.feedback.b.d.a(b)) {
            hVar.d.setText(d);
        } else {
            hVar.d.setText(b);
        }
        if (com.feedback.b.c.a(this.b, dVar)) {
            hVar.a.setVisibility(0);
            hVar.a.setBackgroundResource(l.a(this.b, "drawable", "umeng_analyse_point_new"));
        } else {
            hVar.a.setVisibility(4);
        }
        return view2;
    }
}
