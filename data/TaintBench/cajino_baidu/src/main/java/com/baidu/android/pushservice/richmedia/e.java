package com.baidu.android.pushservice.richmedia;

import android.app.AlertDialog.Builder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

class e implements OnItemLongClickListener {
    final /* synthetic */ MediaListActivity a;

    e(MediaListActivity mediaListActivity) {
        this.a = mediaListActivity;
    }

    public boolean onItemLongClick(AdapterView adapterView, View view, int i, long j) {
        new Builder(this.a).setTitle("提示").setMessage("确定要删除该记录？").setPositiveButton("确定", new g(this, j)).setNegativeButton("取消", new f(this)).show();
        return true;
    }
}
