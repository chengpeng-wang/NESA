package com.baidu.android.pushservice.richmedia;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class i extends BaseAdapter {
    final /* synthetic */ MediaListActivity a;
    private Context b;
    private ArrayList c;

    public i(MediaListActivity mediaListActivity, Context context, ArrayList arrayList) {
        this.a = mediaListActivity;
        this.b = context;
        this.c = arrayList;
    }

    public int getCount() {
        return this.c.size();
    }

    public Object getItem(int i) {
        return this.c.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(this.b.getApplicationContext()).inflate(this.a.e, null);
        inflate.setBackgroundColor(-7829368);
        ImageView imageView = (ImageView) inflate.findViewById(this.a.g);
        TextView textView = (TextView) inflate.findViewById(this.a.i);
        TextView textView2 = (TextView) inflate.findViewById(this.a.j);
        ((TextView) inflate.findViewById(this.a.h)).setText(((HashMap) this.c.get(i)).get("title").toString());
        textView.setText(((HashMap) this.c.get(i)).get("fromtext").toString());
        textView2.setText(((HashMap) this.c.get(i)).get("timetext").toString());
        imageView.setImageDrawable((Drawable) ((HashMap) this.c.get(i)).get("img"));
        return inflate;
    }
}
