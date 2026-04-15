package com.baidu.android.pushservice;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.widget.RemoteViews;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CustomPushNotificationBuilder extends PushNotificationBuilder {
    private int mLayoutIconDrawable;
    private int mLayoutIconId;
    private int mLayoutId;
    private int mLayoutTextId;
    private int mLayoutTitleId;

    public CustomPushNotificationBuilder(int i, int i2, int i3, int i4) {
        this.mLayoutId = i;
        this.mLayoutIconId = i2;
        this.mLayoutTitleId = i3;
        this.mLayoutTextId = i4;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.mStatusbarIcon = objectInputStream.readInt();
        this.mNotificationFlags = objectInputStream.readInt();
        this.mNotificationDefaults = objectInputStream.readInt();
        if (objectInputStream.readBoolean()) {
            this.mNotificationsound = Uri.parse((String) objectInputStream.readObject());
        }
        int readInt = objectInputStream.readInt();
        this.mVibratePattern = new long[readInt];
        for (int i = 0; i < readInt; i++) {
            this.mVibratePattern[i] = objectInputStream.readLong();
        }
        this.mNotificationTitle = (String) objectInputStream.readObject();
        this.mNotificationText = (String) objectInputStream.readObject();
        this.mLayoutId = objectInputStream.readInt();
        this.mLayoutIconId = objectInputStream.readInt();
        this.mLayoutTitleId = objectInputStream.readInt();
        this.mLayoutTextId = objectInputStream.readInt();
        this.mLayoutIconDrawable = objectInputStream.readInt();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        int i = 0;
        objectOutputStream.writeInt(this.mStatusbarIcon);
        objectOutputStream.writeInt(this.mNotificationFlags);
        objectOutputStream.writeInt(this.mNotificationDefaults);
        if (this.mNotificationsound != null) {
            objectOutputStream.writeBoolean(true);
            objectOutputStream.writeObject(this.mNotificationsound.toString());
        } else {
            objectOutputStream.writeBoolean(false);
        }
        if (this.mVibratePattern != null) {
            objectOutputStream.writeInt(this.mVibratePattern.length);
            while (i < this.mVibratePattern.length) {
                objectOutputStream.writeLong(this.mVibratePattern[i]);
                i++;
            }
        } else {
            objectOutputStream.writeInt(0);
        }
        objectOutputStream.writeObject(this.mNotificationTitle);
        objectOutputStream.writeObject(this.mNotificationText);
        objectOutputStream.writeInt(this.mLayoutId);
        objectOutputStream.writeInt(this.mLayoutIconId);
        objectOutputStream.writeInt(this.mLayoutTitleId);
        objectOutputStream.writeInt(this.mLayoutTextId);
        objectOutputStream.writeInt(this.mLayoutIconDrawable);
    }

    public Notification construct(Context context) {
        Notification notification = new Notification();
        if (this.mNotificationDefaults != 0) {
            notification.defaults = this.mNotificationDefaults;
        }
        if (this.mNotificationsound != null) {
            notification.sound = this.mNotificationsound;
        }
        if (this.mVibratePattern != null) {
            notification.vibrate = this.mVibratePattern;
        }
        if (this.mStatusbarIcon != 0) {
            notification.icon = this.mStatusbarIcon;
        }
        if (this.mNotificationFlags != 0) {
            notification.flags = this.mNotificationFlags;
        }
        if (this.mLayoutId != 0) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), this.mLayoutId);
            if (this.mLayoutIconDrawable != 0) {
                remoteViews.setImageViewResource(this.mLayoutIconId, this.mLayoutIconDrawable);
            }
            if (this.mNotificationTitle != null) {
                remoteViews.setTextViewText(this.mLayoutTitleId, this.mNotificationTitle);
            }
            if (this.mNotificationText != null) {
                remoteViews.setTextViewText(this.mLayoutTextId, this.mNotificationText);
            }
            notification.contentView = remoteViews;
        } else {
            notification.setLatestEventInfo(context, this.mNotificationTitle, this.mNotificationTitle, null);
        }
        return notification;
    }

    public void setLayoutDrawable(int i) {
        this.mLayoutIconDrawable = i;
    }
}
