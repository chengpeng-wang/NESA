package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

public class NotificationCompat {
    public static final int FLAG_HIGH_PRIORITY = 128;
    /* access modifiers changed from: private|static|final */
    public static final NotificationCompatImpl IMPL;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;

    public static class Action {
        public PendingIntent actionIntent;
        public int icon;
        public CharSequence title;

        public Action(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            CharSequence charSequence2 = charSequence;
            PendingIntent pendingIntent2 = pendingIntent;
            this.icon = i;
            this.title = charSequence2;
            this.actionIntent = pendingIntent2;
        }
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        public Style() {
        }

        public void setBuilder(Builder builder) {
            Builder builder2 = builder;
            if (this.mBuilder != builder2) {
                this.mBuilder = builder2;
                if (this.mBuilder != null) {
                    Builder style = this.mBuilder.setStyle(this);
                }
            }
        }

        public Notification build() {
            Notification notification = null;
            if (this.mBuilder != null) {
                notification = this.mBuilder.build();
            }
            return notification;
        }
    }

    public static class BigPictureStyle extends Style {
        Bitmap mBigLargeIcon;
        boolean mBigLargeIconSet;
        Bitmap mPicture;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigPictureStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap bitmap) {
            this.mPicture = bitmap;
            return this;
        }

        public BigPictureStyle bigLargeIcon(Bitmap bitmap) {
            this.mBigLargeIcon = bitmap;
            this.mBigLargeIconSet = true;
            return this;
        }
    }

    public static class BigTextStyle extends Style {
        CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }

        public BigTextStyle bigText(CharSequence charSequence) {
            this.mBigText = charSequence;
            return this;
        }
    }

    public static class Builder {
        ArrayList<Action> mActions;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        Context mContext;
        PendingIntent mFullScreenIntent;
        Bitmap mLargeIcon;
        Notification mNotification;
        int mNumber;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        boolean mUseChronometer;

        public Builder(Context context) {
            Context context2 = context;
            ArrayList arrayList = r5;
            ArrayList arrayList2 = new ArrayList();
            this.mActions = arrayList;
            Notification notification = r5;
            Notification notification2 = new Notification();
            this.mNotification = notification;
            this.mContext = context2;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
        }

        public Builder setWhen(long j) {
            this.mNotification.when = j;
            return this;
        }

        public Builder setUsesChronometer(boolean z) {
            this.mUseChronometer = z;
            return this;
        }

        public Builder setSmallIcon(int i) {
            this.mNotification.icon = i;
            return this;
        }

        public Builder setSmallIcon(int i, int i2) {
            int i3 = i2;
            this.mNotification.icon = i;
            this.mNotification.iconLevel = i3;
            return this;
        }

        public Builder setContentTitle(CharSequence charSequence) {
            this.mContentTitle = charSequence;
            return this;
        }

        public Builder setContentText(CharSequence charSequence) {
            this.mContentText = charSequence;
            return this;
        }

        public Builder setSubText(CharSequence charSequence) {
            this.mSubText = charSequence;
            return this;
        }

        public Builder setNumber(int i) {
            this.mNumber = i;
            return this;
        }

        public Builder setContentInfo(CharSequence charSequence) {
            this.mContentInfo = charSequence;
            return this;
        }

        public Builder setProgress(int i, int i2, boolean z) {
            int i3 = i2;
            boolean z2 = z;
            this.mProgressMax = i;
            this.mProgress = i3;
            this.mProgressIndeterminate = z2;
            return this;
        }

        public Builder setContent(RemoteViews remoteViews) {
            this.mNotification.contentView = remoteViews;
            return this;
        }

        public Builder setContentIntent(PendingIntent pendingIntent) {
            this.mContentIntent = pendingIntent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent pendingIntent) {
            this.mNotification.deleteIntent = pendingIntent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent pendingIntent, boolean z) {
            boolean z2 = z;
            this.mFullScreenIntent = pendingIntent;
            setFlag(128, z2);
            return this;
        }

        public Builder setTicker(CharSequence charSequence) {
            this.mNotification.tickerText = charSequence;
            return this;
        }

        public Builder setTicker(CharSequence charSequence, RemoteViews remoteViews) {
            RemoteViews remoteViews2 = remoteViews;
            this.mNotification.tickerText = charSequence;
            this.mTickerView = remoteViews2;
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            this.mLargeIcon = bitmap;
            return this;
        }

        public Builder setSound(Uri uri) {
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = -1;
            return this;
        }

        public Builder setSound(Uri uri, int i) {
            int i2 = i;
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = i2;
            return this;
        }

        public Builder setVibrate(long[] jArr) {
            this.mNotification.vibrate = jArr;
            return this;
        }

        public Builder setLights(int i, int i2, int i3) {
            int i4 = i2;
            int i5 = i3;
            this.mNotification.ledARGB = i;
            this.mNotification.ledOnMS = i4;
            this.mNotification.ledOffMS = i5;
            Object obj = (this.mNotification.ledOnMS == 0 || this.mNotification.ledOffMS == 0) ? null : 1;
            this.mNotification.flags = (this.mNotification.flags & -2) | (obj != null ? 1 : 0);
            return this;
        }

        public Builder setOngoing(boolean z) {
            setFlag(2, z);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean z) {
            setFlag(8, z);
            return this;
        }

        public Builder setAutoCancel(boolean z) {
            setFlag(16, z);
            return this;
        }

        public Builder setDefaults(int i) {
            int i2 = i;
            this.mNotification.defaults = i2;
            if ((i2 & 4) != 0) {
                Notification notification = this.mNotification;
                notification.flags |= 1;
            }
            return this;
        }

        private void setFlag(int i, boolean z) {
            int i2 = i;
            Notification notification;
            if (z) {
                notification = this.mNotification;
                notification.flags |= i2;
                return;
            }
            notification = this.mNotification;
            notification.flags &= i2 ^ -1;
        }

        public Builder setPriority(int i) {
            this.mPriority = i;
            return this;
        }

        public Builder addAction(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            int i2 = i;
            CharSequence charSequence2 = charSequence;
            PendingIntent pendingIntent2 = pendingIntent;
            ArrayList arrayList = this.mActions;
            Action action = r10;
            Action action2 = new Action(i2, charSequence2, pendingIntent2);
            boolean add = arrayList.add(action);
            return this;
        }

        public Builder setStyle(Style style) {
            Style style2 = style;
            if (this.mStyle != style2) {
                this.mStyle = style2;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }

        @Deprecated
        public Notification getNotification() {
            return NotificationCompat.IMPL.build(this);
        }

        public Notification build() {
            return NotificationCompat.IMPL.build(this);
        }
    }

    public static class InboxStyle extends Style {
        ArrayList<CharSequence> mTexts;

        public InboxStyle() {
            ArrayList arrayList = r4;
            ArrayList arrayList2 = new ArrayList();
            this.mTexts = arrayList;
        }

        public InboxStyle(Builder builder) {
            Builder builder2 = builder;
            ArrayList arrayList = r5;
            ArrayList arrayList2 = new ArrayList();
            this.mTexts = arrayList;
            setBuilder(builder2);
        }

        public InboxStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public InboxStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }

        public InboxStyle addLine(CharSequence charSequence) {
            boolean add = this.mTexts.add(charSequence);
            return this;
        }
    }

    interface NotificationCompatImpl {
        Notification build(Builder builder);
    }

    static class NotificationCompatImplBase implements NotificationCompatImpl {
        NotificationCompatImplBase() {
        }

        public Notification build(Builder builder) {
            Builder builder2 = builder;
            Notification notification = builder2.mNotification;
            notification.setLatestEventInfo(builder2.mContext, builder2.mContentTitle, builder2.mContentText, builder2.mContentIntent);
            if (builder2.mPriority > 0) {
                Notification notification2 = notification;
                notification2.flags |= 128;
            }
            return notification;
        }
    }

    static class NotificationCompatImplHoneycomb implements NotificationCompatImpl {
        NotificationCompatImplHoneycomb() {
        }

        public Notification build(Builder builder) {
            Builder builder2 = builder;
            return NotificationCompatHoneycomb.add(builder2.mContext, builder2.mNotification, builder2.mContentTitle, builder2.mContentText, builder2.mContentInfo, builder2.mTickerView, builder2.mNumber, builder2.mContentIntent, builder2.mFullScreenIntent, builder2.mLargeIcon);
        }
    }

    static class NotificationCompatImplIceCreamSandwich implements NotificationCompatImpl {
        NotificationCompatImplIceCreamSandwich() {
        }

        public Notification build(Builder builder) {
            Builder builder2 = builder;
            return NotificationCompatIceCreamSandwich.add(builder2.mContext, builder2.mNotification, builder2.mContentTitle, builder2.mContentText, builder2.mContentInfo, builder2.mTickerView, builder2.mNumber, builder2.mContentIntent, builder2.mFullScreenIntent, builder2.mLargeIcon, builder2.mProgressMax, builder2.mProgress, builder2.mProgressIndeterminate);
        }
    }

    static class NotificationCompatImplJellybean implements NotificationCompatImpl {
        NotificationCompatImplJellybean() {
        }

        public Notification build(Builder builder) {
            Builder builder2 = builder;
            NotificationCompatJellybean notificationCompatJellybean = r24;
            NotificationCompatJellybean notificationCompatJellybean2 = new NotificationCompatJellybean(builder2.mContext, builder2.mNotification, builder2.mContentTitle, builder2.mContentText, builder2.mContentInfo, builder2.mTickerView, builder2.mNumber, builder2.mContentIntent, builder2.mFullScreenIntent, builder2.mLargeIcon, builder2.mProgressMax, builder2.mProgress, builder2.mProgressIndeterminate, builder2.mUseChronometer, builder2.mPriority, builder2.mSubText);
            NotificationCompatJellybean notificationCompatJellybean3 = notificationCompatJellybean;
            Iterator it = builder2.mActions.iterator();
            while (it.hasNext()) {
                Action action = (Action) it.next();
                notificationCompatJellybean3.addAction(action.icon, action.title, action.actionIntent);
            }
            if (builder2.mStyle != null) {
                if (builder2.mStyle instanceof BigTextStyle) {
                    BigTextStyle bigTextStyle = (BigTextStyle) builder2.mStyle;
                    notificationCompatJellybean3.addBigTextStyle(bigTextStyle.mBigContentTitle, bigTextStyle.mSummaryTextSet, bigTextStyle.mSummaryText, bigTextStyle.mBigText);
                } else if (builder2.mStyle instanceof InboxStyle) {
                    InboxStyle inboxStyle = (InboxStyle) builder2.mStyle;
                    notificationCompatJellybean3.addInboxStyle(inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
                } else if (builder2.mStyle instanceof BigPictureStyle) {
                    BigPictureStyle bigPictureStyle = (BigPictureStyle) builder2.mStyle;
                    notificationCompatJellybean3.addBigPictureStyle(bigPictureStyle.mBigContentTitle, bigPictureStyle.mSummaryTextSet, bigPictureStyle.mSummaryText, bigPictureStyle.mPicture, bigPictureStyle.mBigLargeIcon, bigPictureStyle.mBigLargeIconSet);
                }
            }
            return notificationCompatJellybean3.build();
        }
    }

    public NotificationCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            NotificationCompatImplJellybean notificationCompatImplJellybean = r2;
            NotificationCompatImplJellybean notificationCompatImplJellybean2 = new NotificationCompatImplJellybean();
            IMPL = notificationCompatImplJellybean;
        } else if (VERSION.SDK_INT >= 14) {
            NotificationCompatImplIceCreamSandwich notificationCompatImplIceCreamSandwich = r2;
            NotificationCompatImplIceCreamSandwich notificationCompatImplIceCreamSandwich2 = new NotificationCompatImplIceCreamSandwich();
            IMPL = notificationCompatImplIceCreamSandwich;
        } else if (VERSION.SDK_INT >= 11) {
            NotificationCompatImplHoneycomb notificationCompatImplHoneycomb = r2;
            NotificationCompatImplHoneycomb notificationCompatImplHoneycomb2 = new NotificationCompatImplHoneycomb();
            IMPL = notificationCompatImplHoneycomb;
        } else {
            NotificationCompatImplBase notificationCompatImplBase = r2;
            NotificationCompatImplBase notificationCompatImplBase2 = new NotificationCompatImplBase();
            IMPL = notificationCompatImplBase;
        }
    }
}
