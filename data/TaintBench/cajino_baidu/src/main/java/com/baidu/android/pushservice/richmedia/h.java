package com.baidu.android.pushservice.richmedia;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import java.io.File;

class h implements s {
    final /* synthetic */ MediaListActivity a;

    h(MediaListActivity mediaListActivity) {
        this.a = mediaListActivity;
    }

    public void a(b bVar) {
    }

    public void a(b bVar, m mVar) {
        String d = bVar.d.d();
        if (mVar.a != mVar.b && this.a.m != null) {
            int i = (int) ((((double) mVar.a) * 100.0d) / ((double) mVar.b));
            this.a.m.setTextViewText(this.a.o, i + "%");
            this.a.m.setTextViewText(this.a.p, d);
            this.a.m.setProgressBar(this.a.n, 100, i, false);
            this.a.m.setImageViewResource(this.a.q, 17301633);
            Notification notification = new Notification(17301633, null, System.currentTimeMillis());
            notification.contentView = this.a.m;
            notification.contentIntent = PendingIntent.getActivity(this.a, 0, new Intent(), 0);
            notification.flags |= 32;
            notification.flags |= 2;
            this.a.b.notify(d, 0, notification);
        }
    }

    public void a(b bVar, r rVar) {
        String d = bVar.d.d();
        this.a.b.cancel(d, 0);
        j fileDownloadingInfo = PushDatabase.getFileDownloadingInfo(PushDatabase.getDb(this.a), d);
        if (fileDownloadingInfo != null && fileDownloadingInfo.i == b.f) {
            String str = fileDownloadingInfo.e;
            d = fileDownloadingInfo.f;
            d = str + "/" + d.substring(0, d.lastIndexOf(".")) + "/index.html";
            Intent intent = new Intent();
            intent.setClass(this.a, MediaViewActivity.class);
            intent.setData(Uri.fromFile(new File(d)));
            intent.addFlags(268435456);
            this.a.startActivity(intent);
        }
    }

    public void a(b bVar, Throwable th) {
        String d = bVar.d.d();
        this.a.b.cancel(d, 0);
        Toast makeText = Toast.makeText(this.a, "下载富媒体" + Uri.parse(d).getAuthority() + "失败", 1);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public void b(b bVar) {
        this.a.b.cancel(bVar.d.d(), 0);
    }
}
