package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.Notification.InboxStyle;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

class NotificationCompatJellybean {
    private Builder b;

    public NotificationCompatJellybean(Context context, Notification notification, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int i, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int i2, int i3, boolean z, boolean z2, int i4, CharSequence charSequence4) {
        boolean z3;
        boolean z4;
        Notification notification2 = notification;
        CharSequence charSequence5 = charSequence;
        CharSequence charSequence6 = charSequence2;
        CharSequence charSequence7 = charSequence3;
        RemoteViews remoteViews2 = remoteViews;
        int i5 = i;
        PendingIntent pendingIntent3 = pendingIntent;
        PendingIntent pendingIntent4 = pendingIntent2;
        Bitmap bitmap2 = bitmap;
        int i6 = i2;
        int i7 = i3;
        boolean z5 = z;
        boolean z6 = z2;
        int i8 = i4;
        CharSequence charSequence8 = charSequence4;
        Builder builder = r24;
        Builder builder2 = new Builder(context);
        builder = builder.setWhen(notification2.when).setSmallIcon(notification2.icon, notification2.iconLevel).setContent(notification2.contentView).setTicker(notification2.tickerText, remoteViews2).setSound(notification2.sound, notification2.audioStreamType).setVibrate(notification2.vibrate).setLights(notification2.ledARGB, notification2.ledOnMS, notification2.ledOffMS);
        if ((notification2.flags & 2) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        builder = builder.setOngoing(z3);
        if ((notification2.flags & 8) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        builder = builder.setOnlyAlertOnce(z3);
        if ((notification2.flags & 16) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        builder = builder.setAutoCancel(z3).setDefaults(notification2.defaults).setContentTitle(charSequence5).setContentText(charSequence6).setSubText(charSequence8).setContentInfo(charSequence7).setContentIntent(pendingIntent3).setDeleteIntent(notification2.deleteIntent);
        PendingIntent pendingIntent5 = pendingIntent4;
        if ((notification2.flags & 128) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.b = builder.setFullScreenIntent(pendingIntent5, z4).setLargeIcon(bitmap2).setNumber(i5).setUsesChronometer(z6).setPriority(i8).setProgress(i6, i7, z5);
    }

    public void addAction(int i, CharSequence charSequence, PendingIntent pendingIntent) {
        Builder addAction = this.b.addAction(i, charSequence, pendingIntent);
    }

    public void addBigTextStyle(CharSequence charSequence, boolean z, CharSequence charSequence2, CharSequence charSequence3) {
        CharSequence charSequence4 = charSequence;
        boolean z2 = z;
        CharSequence charSequence5 = charSequence2;
        CharSequence charSequence6 = charSequence3;
        BigTextStyle bigTextStyle = r9;
        BigTextStyle bigTextStyle2 = new BigTextStyle(this.b);
        BigTextStyle bigText = bigTextStyle.setBigContentTitle(charSequence4).bigText(charSequence6);
        if (z2) {
            bigTextStyle = bigText.setSummaryText(charSequence5);
        }
    }

    public void addBigPictureStyle(CharSequence charSequence, boolean z, CharSequence charSequence2, Bitmap bitmap, Bitmap bitmap2, boolean z2) {
        CharSequence charSequence3 = charSequence;
        boolean z3 = z;
        CharSequence charSequence4 = charSequence2;
        Bitmap bitmap3 = bitmap;
        Bitmap bitmap4 = bitmap2;
        boolean z4 = z2;
        BigPictureStyle bigPictureStyle = r11;
        BigPictureStyle bigPictureStyle2 = new BigPictureStyle(this.b);
        BigPictureStyle bigPicture = bigPictureStyle.setBigContentTitle(charSequence3).bigPicture(bitmap3);
        if (z4) {
            bigPictureStyle = bigPicture.bigLargeIcon(bitmap4);
        }
        if (z3) {
            bigPictureStyle = bigPicture.setSummaryText(charSequence4);
        }
    }

    public void addInboxStyle(CharSequence charSequence, boolean z, CharSequence charSequence2, ArrayList<CharSequence> arrayList) {
        CharSequence charSequence3 = charSequence;
        boolean z2 = z;
        CharSequence charSequence4 = charSequence2;
        ArrayList<CharSequence> arrayList2 = arrayList;
        InboxStyle inboxStyle = r11;
        InboxStyle inboxStyle2 = new InboxStyle(this.b);
        InboxStyle bigContentTitle = inboxStyle.setBigContentTitle(charSequence3);
        if (z2) {
            inboxStyle = bigContentTitle.setSummaryText(charSequence4);
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            inboxStyle = bigContentTitle.addLine((CharSequence) it.next());
        }
    }

    public Notification build() {
        return this.b.build();
    }
}
