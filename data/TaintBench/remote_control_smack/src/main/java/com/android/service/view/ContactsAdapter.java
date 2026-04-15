package com.android.service.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.service.R;
import java.util.List;

public class ContactsAdapter extends BaseAdapter {
    private Context ctx;
    private List<String> list;
    private List<String> list2;

    public ContactsAdapter(Context ctxt, List<String> list, List<String> list2) {
        this.list = list;
        this.list2 = list2;
        this.ctx = ctxt;
    }

    public int getCount() {
        return this.list2.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.contact_list_item, null);
        }
        LinearLayout contact_layout = (LinearLayout) convertView.findViewById(R.id.contact_layout);
        TextView contact_phone = (TextView) convertView.findViewById(R.id.contact_phone);
        ((TextView) convertView.findViewById(R.id.contact_name)).setText((CharSequence) this.list.get(position));
        contact_phone.setText((CharSequence) this.list2.get(position));
        return convertView;
    }
}
