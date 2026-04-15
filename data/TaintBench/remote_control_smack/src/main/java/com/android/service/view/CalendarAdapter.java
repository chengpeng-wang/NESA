package com.android.service.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.service.R;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private List<String> ID_list;
    /* access modifiers changed from: private */
    public List<String> beginString_list;
    Context context;
    /* access modifiers changed from: private */
    public Context ctx;
    /* access modifiers changed from: private */
    public List<String> description_list;
    /* access modifiers changed from: private */
    public List<String> endString_list;
    /* access modifiers changed from: private */
    public List<String> eventLocation_list;
    private List<String> items_list;
    /* access modifiers changed from: private */
    public List<String> title_list;

    public CalendarAdapter(Context ctxt, List<String> items_list, List<String> title_list, List<String> eventLocation_list, List<String> description_list, List<String> ID_list, List<String> beginString_list, List<String> endString_list) {
        this.items_list = items_list;
        this.title_list = title_list;
        this.eventLocation_list = eventLocation_list;
        this.description_list = description_list;
        this.ID_list = ID_list;
        this.beginString_list = beginString_list;
        this.endString_list = endString_list;
        this.ctx = ctxt;
    }

    public int getCount() {
        return this.title_list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.calendar_list_item, null);
        }
        LinearLayout callrecord_layout = (LinearLayout) convertView.findViewById(R.id.calendar_layout);
        ((TextView) convertView.findViewById(R.id.calendar_title)).setText((CharSequence) this.items_list.get(position));
        callrecord_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String EventLocation;
                String Description;
                int index = position;
                if (CalendarAdapter.this.eventLocation_list.get(index) == null) {
                    EventLocation = "無資料";
                } else {
                    EventLocation = (String) CalendarAdapter.this.eventLocation_list.get(index);
                }
                if (CalendarAdapter.this.description_list.get(index) == null) {
                    Description = "無資料";
                } else {
                    Description = (String) CalendarAdapter.this.description_list.get(index);
                }
                new Builder(CalendarAdapter.this.ctx).setTitle("詳細內容").setIcon(17301574).setMessage("主題:" + ((String) CalendarAdapter.this.title_list.get(index)) + "\n" + "內容:" + Description + "\n" + "地點:" + EventLocation + "\n" + "開始時間:" + ((String) CalendarAdapter.this.beginString_list.get(index)) + "\n" + "結束時間:" + ((String) CalendarAdapter.this.endString_list.get(index))).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
        return convertView;
    }
}
