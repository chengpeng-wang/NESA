package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

class NotificationCompatHoneycomb {
    NotificationCompatHoneycomb() {
    }

    static Notification add(Context context, Notification notification, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int i, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap) {
        boolean z;
        boolean z2;
        Notification notification2 = notification;
        CharSequence charSequence4 = charSequence;
        CharSequence charSequence5 = charSequence2;
        CharSequence charSequence6 = charSequence3;
        RemoteViews remoteViews2 = remoteViews;
        int i2 = i;
        PendingIntent pendingIntent3 = pendingIntent;
        PendingIntent pendingIntent4 = pendingIntent2;
        Bitmap bitmap2 = bitmap;
        Builder builder = r15;
        Builder builder2 = new Builder(context);
        builder = builder.setWhen(notification2.when).setSmallIcon(notification2.icon, notification2.iconLevel).setContent(notification2.contentView).setTicker(notification2.tickerText, remoteViews2).setSound(notification2.sound, notification2.audioStreamType).setVibrate(notification2.vibrate).setLights(notification2.ledARGB, notification2.ledOnMS, notification2.ledOffMS);
        if ((notification2.flags & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        builder = builder.setOngoing(z);
        if ((notification2.flags & 8) != 0) {
            z = true;
        } else {
            z = false;
        }
        builder = builder.setOnlyAlertOnce(z);
        if ((notification2.flags & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        builder = builder.setAutoCancel(z).setDefaults(notification2.defaults).setContentTitle(charSequence4).setContentText(charSequence5).setContentInfo(charSequence6).setContentIntent(pendingIntent3).setDeleteIntent(notification2.deleteIntent);
        PendingIntent pendingIntent5 = pendingIntent4;
        if ((notification2.flags & 128) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        return builder.setFullScreenIntent(pendingIntent5, z2).setLargeIcon(bitmap2).setNumber(i2).getNotification();
    }
}
