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
import java.util.StringTokenizer;

public class CallRecordAdapter extends BaseAdapter {
    private LayoutInflater _myInflater;
    String[] callDate = null;
    Context context;
    private Context ctx;
    LinearLayout layout;
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    String[] numberName = null;
    String[] numbertype = null;
    TextView tvDate;
    TextView tvName;

    public CallRecordAdapter(Context ctxt, List<String> list, List<String> list2, List<String> listtime) {
        this.list = list;
        this.list2 = list2;
        this.listtime = listtime;
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
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.callrecord_list_item, null);
        }
        LinearLayout callrecord_layout = (LinearLayout) convertView.findViewById(R.id.callrecord_layout);
        TextView callrecord_Name = (TextView) convertView.findViewById(R.id.callrecord_Name);
        TextView callrecord_phone = (TextView) convertView.findViewById(R.id.callrecord_phone);
        TextView textView_Call = (TextView) convertView.findViewById(R.id.text_call);
        ((TextView) convertView.findViewById(R.id.callrecord_Date)).setText((CharSequence) this.listtime.get(position));
        StringTokenizer strtoken = new StringTokenizer((String) this.list2.get(position), ":");
        int n = 0;
        while (strtoken.hasMoreElements()) {
            if (n == 0) {
                String strName = strtoken.nextToken();
                if (strName == null || strName.length() <= 0) {
                    callrecord_Name.setText("");
                } else {
                    callrecord_Name.setText(strName);
                }
            } else if (n == 1) {
                String strPhone = strtoken.nextToken();
                if (strPhone == null || strPhone.length() <= 5) {
                    callrecord_phone.setText("");
                } else {
                    callrecord_phone.setText(strPhone);
                }
            }
            n++;
        }
        if (((String) this.list.get(position)).contains("in")) {
            textView_Call.setText("接聽");
        } else if (((String) this.list.get(position)).equalsIgnoreCase("out")) {
            textView_Call.setText("撥出");
        } else if (((String) this.list.get(position)).contains("not")) {
            textView_Call.setText("未接");
        }
        return convertView;
    }
}
