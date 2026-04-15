package com.baidu.android.pushservice;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BasicPushNotificationBuilder extends PushNotificationBuilder {
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
        notification.setLatestEventInfo(context, this.mNotificationTitle, this.mNotificationText, null);
        return notification;
    }
}
