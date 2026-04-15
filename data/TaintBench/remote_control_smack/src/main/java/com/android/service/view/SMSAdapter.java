package com.android.service.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.service.R;
import java.util.List;

public class SMSAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public Context ctx;
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    private List<String> listtype;

    public SMSAdapter(Context ctxt, List<String> list, List<String> list2, List<String> listtime, List<String> listtype) {
        this.list = list;
        this.list2 = list2;
        this.list2 = list2;
        this.listtime = listtime;
        this.listtype = listtype;
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
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.sms_list_item, null);
        }
        LinearLayout sms_layout = (LinearLayout) convertView.findViewById(R.id.sms_layout);
        TextView tvDate = (TextView) convertView.findViewById(R.id.text2);
        ImageView sms_img = (ImageView) convertView.findViewById(R.id.sms_img);
        ((TextView) convertView.findViewById(R.id.text1)).setText((CharSequence) this.list.get(position));
        tvDate.setText((CharSequence) this.listtime.get(position));
        if (((String) this.listtype.get(position)).contains("in")) {
            sms_img.setImageResource(R.drawable.sms_in);
        } else if (((String) this.listtype.get(position)).contains("out")) {
            sms_img.setImageResource(R.drawable.sms_send);
        }
        final String msg = (String) this.list2.get(position);
        sms_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                new Builder(SMSAdapter.this.ctx).setTitle("簡訊內容").setIcon(17301545).setMessage(msg).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        return convertView;
    }
}
