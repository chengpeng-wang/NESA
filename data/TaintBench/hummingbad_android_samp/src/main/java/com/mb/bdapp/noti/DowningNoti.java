package com.mb.bdapp.noti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import com.mb.bdapp.db.DuAd;

public class DowningNoti extends BaseNoti {
    private int progress;
    private int type;

    public DowningNoti(Context context, DuAd item, int progress, int type) throws Exception {
        super(context, item);
        this.progress = progress;
        this.type = type;
        reSetData();
    }

    /* access modifiers changed from: protected */
    public void reSetData() {
        this.flags = 32;
        if (this.type == 0) {
            this.content = this.progress + "%" + "\t\t" + this.item.getContent();
            setFlags(32);
        } else if (this.type == -1) {
            this.content = "Failed to download";
            setFlags(16);
        }
        this.tickerText = this.item.getContent();
        this.notiDefaults = 4;
        this.defPendingIntentType = 1;
    }

    @SuppressLint({"NewApi"})
    public Notification createNotifiSupportLater() {
        if (this.type == -1) {
            return super.createNotifiSupportLater();
        }
        Builder mBuilder = new Builder(this.context);
        mBuilder.setSmallIcon(17301659);
        mBuilder.setDefaults(this.notiDefaults);
        PendingIntent contentIntent = getPendingIntent();
        Long when = (Long) WHENS.get(this.gid);
        if (when == null) {
            when = Long.valueOf(System.currentTimeMillis());
            WHENS.put(this.gid, when);
        }
        mBuilder.setWhen(when.longValue());
        mBuilder.setTicker(this.tickerText);
        mBuilder.setContentTitle(this.title);
        mBuilder.setContentText(this.content);
        mBuilder.setProgress(100, this.progress, false);
        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.getNotification();
        notification.flags = this.flags;
        return notification;
    }
}
