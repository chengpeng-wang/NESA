package com.baidu.android.pushservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.baidu.android.pushservice.message.PublicMsg;
import com.baidu.android.pushservice.richmedia.MediaViewActivity;
import com.baidu.android.pushservice.richmedia.b;
import com.baidu.android.pushservice.richmedia.m;
import com.baidu.android.pushservice.richmedia.r;
import com.baidu.android.pushservice.richmedia.s;
import com.baidu.android.pushservice.util.PushDatabase;
import com.baidu.android.pushservice.util.j;
import java.io.File;

class o implements s {
    public Context a = null;
    public RemoteViews b = null;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    NotificationManager g;
    PublicMsg h;
    final /* synthetic */ PushServiceReceiver i;

    o(PushServiceReceiver pushServiceReceiver, Context context, PublicMsg publicMsg) {
        this.i = pushServiceReceiver;
        this.a = context;
        this.g = (NotificationManager) context.getSystemService("notification");
        this.h = publicMsg;
    }

    public void a(b bVar) {
        Resources resources = this.a.getResources();
        String packageName = this.a.getPackageName();
        if (resources != null) {
            int identifier = resources.getIdentifier("bpush_download_progress", "layout", packageName);
            this.b = new RemoteViews(this.a.getPackageName(), identifier);
            if (identifier != 0) {
                this.c = resources.getIdentifier("bpush_download_progress", "id", packageName);
                this.d = resources.getIdentifier("bpush_progress_percent", "id", packageName);
                this.e = resources.getIdentifier("bpush_progress_text", "id", packageName);
                this.f = resources.getIdentifier("bpush_download_icon", "id", packageName);
                this.b.setImageViewResource(this.f, this.a.getApplicationInfo().icon);
            }
        }
    }

    public void a(b bVar, m mVar) {
        String d = bVar.d.d();
        if (mVar.a != mVar.b && this.b != null) {
            int i = (int) ((((double) mVar.a) * 100.0d) / ((double) mVar.b));
            this.b.setTextViewText(this.d, i + "%");
            this.b.setTextViewText(this.e, "正在下载富媒体:" + d);
            this.b.setProgressBar(this.c, 100, i, false);
            Notification notification = new Notification(17301633, null, System.currentTimeMillis());
            notification.contentView = this.b;
            notification.contentIntent = PendingIntent.getActivity(this.a, 0, new Intent(), 0);
            notification.flags |= 32;
            notification.flags |= 2;
            this.g.notify(d, 0, notification);
        }
    }

    public void a(b bVar, r rVar) {
        String d = bVar.d.d();
        this.g.cancel(d, 0);
        j fileDownloadingInfo = PushDatabase.getFileDownloadingInfo(PushDatabase.getDb(this.a), d);
        if (fileDownloadingInfo != null && fileDownloadingInfo.i == b.f) {
            String str = fileDownloadingInfo.e;
            d = fileDownloadingInfo.f;
            d = str + "/" + d.substring(0, d.lastIndexOf(".")) + "/index.html";
            com.baidu.android.pushservice.b.j jVar = new com.baidu.android.pushservice.b.j();
            jVar.c("010401");
            jVar.a(this.h.a);
            jVar.c(7);
            jVar.e(this.h.b);
            jVar.a(System.currentTimeMillis());
            jVar.d(com.baidu.android.pushservice.b.m.d(this.a));
            jVar.a(0);
            com.baidu.android.pushservice.b.s.a(this.a, jVar);
            Intent intent = new Intent();
            intent.setClass(this.a, MediaViewActivity.class);
            intent.setData(Uri.fromFile(new File(d)));
            intent.addFlags(268435456);
            this.a.startActivity(intent);
        }
    }

    public void a(b bVar, Throwable th) {
        String d = bVar.d.d();
        this.g.cancel(d, 0);
        Toast makeText = Toast.makeText(this.a, "下载富媒体" + Uri.parse(d).getAuthority() + "失败", 1);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public void b(b bVar) {
        this.g.cancel(bVar.d.d(), 0);
    }
}
