package com.baidu.android.pushservice.richmedia;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import java.io.File;
import java.util.HashMap;

class d implements OnItemClickListener {
    final /* synthetic */ MediaListActivity a;

    d(MediaListActivity mediaListActivity) {
        this.a = mediaListActivity;
    }

    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        j fileDownloadingInfo = PushDatabase.getFileDownloadingInfo(PushDatabase.getDb(this.a), (String) ((HashMap) adapterView.getItemAtPosition(i)).get(MediaListActivity.r));
        if (fileDownloadingInfo == null) {
            return;
        }
        if (fileDownloadingInfo.i == b.f) {
            String str = fileDownloadingInfo.e;
            String str2 = fileDownloadingInfo.f;
            str2 = str + "/" + str2.substring(0, str2.lastIndexOf(".")) + "/index.html";
            Intent intent = new Intent();
            intent.setClass(this.a, MediaViewActivity.class);
            intent.setData(Uri.fromFile(new File(str2)));
            intent.addFlags(268435456);
            this.a.startActivity(intent);
            return;
        }
        this.a.a(fileDownloadingInfo.b, fileDownloadingInfo.c, fileDownloadingInfo.d);
    }
}
