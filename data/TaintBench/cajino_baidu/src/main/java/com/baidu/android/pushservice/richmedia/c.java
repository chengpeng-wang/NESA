package com.baidu.android.pushservice.richmedia;

import android.view.View;
import android.view.View.OnClickListener;

class c implements OnClickListener {
    final /* synthetic */ MediaListActivity a;

    c(MediaListActivity mediaListActivity) {
        this.a = mediaListActivity;
    }

    public void onClick(View view) {
        this.a.finish();
    }
}
