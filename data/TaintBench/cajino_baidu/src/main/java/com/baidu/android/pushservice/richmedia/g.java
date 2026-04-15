package com.baidu.android.pushservice.richmedia;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import java.io.File;
import java.util.Map;

class g implements OnClickListener {
    final /* synthetic */ long a;
    final /* synthetic */ e b;

    g(e eVar, long j) {
        this.b = eVar;
        this.a = j;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        String str = (String) ((Map) this.b.a.a.get((int) this.a)).get(MediaListActivity.r);
        j fileDownloadingInfo = PushDatabase.getFileDownloadingInfo(PushDatabase.getDb(this.b.a), str);
        if (fileDownloadingInfo != null) {
            new File(fileDownloadingInfo.e).delete();
        }
        PushDatabase.deleteFileDownloadingInfo(PushDatabase.getDb(this.b.a), str);
        Intent intent = new Intent();
        intent.setClass(this.b.a, MediaListActivity.class);
        this.b.a.startActivity(intent);
    }
}
