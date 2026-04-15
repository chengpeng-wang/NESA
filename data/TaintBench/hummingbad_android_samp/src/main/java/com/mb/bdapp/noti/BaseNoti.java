package com.mb.bdapp.noti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.util.ClsUtils;
import com.mb.bdapp.util.FileUtils;
import com.mb.bdapp.util.LogUtil;
import java.io.File;
import java.util.HashMap;

public class BaseNoti {
    protected static final String ICON_FMAT = ".png";
    protected static final String ICON_NOTE_BASNAME = "noti_";
    protected static final int ICON_SIZE = 5;
    protected static final String IMG_CACHE_SUFFIX = ".png";
    public static final String INSTALL_CONTENT = "100%\t\tClick Install";
    protected static final int PENDING_INTENT_GET_ACTIVITY = 1;
    protected static final int PENDING_INTENT_GET_BROADCAST = 0;
    protected static final int PENDING_INTENT_GET_SERVICE = 2;
    private static final String TAG = BaseNoti.class.getSimpleName();
    protected static HashMap<String, Long> WHENS = new HashMap();
    private static NotificationManager mNotificationManager;
    protected String content = null;
    protected Context context;
    protected int defPendingIntentType = 0;
    protected int flags = 32;
    protected String gid;
    protected Intent intent;
    protected DuAd item;
    protected int notiDefaults = 1;
    protected String tickerText = null;
    protected String title = null;

    public static NotificationManager getNotificationManager(Context context) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService("notification");
        }
        return mNotificationManager;
    }

    public BaseNoti(Context context, DuAd item) throws Exception {
        this.context = context;
        this.item = item;
        setData();
    }

    /* access modifiers changed from: protected */
    public void setData() throws Exception {
        if (this.item == null) {
            throw new Exception("item==null");
        }
        this.gid = this.item.getGid();
        this.title = this.item.getTitle();
        this.content = this.item.getContent();
    }

    public void showNotify() {
        Notification notification;
        if (VERSION.SDK_INT >= 15) {
            notification = createNotifiSupportLater();
        } else {
            notification = createNotifi();
        }
        setLogoImage(this.context, notification);
    }

    public void setLogoImage(Context context, Notification notification) {
        Bitmap paramBitmap = getDiscCacheBitmap(this.item.getIcon());
        if (paramBitmap != null) {
            setLogo(context, notification, paramBitmap);
        }
        show(notification);
    }

    public static Bitmap getDiscCacheBitmap(String imageUri) {
        try {
            File file = FileUtils.getFile(getImageFileNameByUri(imageUri));
            if (file != null && file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    public static String getImageFileNameByUri(String imageUri) {
        return FileUtils.generateFileName(imageUri) + ".png";
    }

    public void show(Notification notification) {
        getNotificationManager(this.context).notify(getNotiID(this.gid), notification);
    }

    public Notification createNotifi() {
        Notification notification = new Notification();
        notification.icon = 17301659;
        notification.defaults |= this.notiDefaults;
        PendingIntent contentIntent = getPendingIntent();
        notification.flags = this.flags;
        notification.when = getNotificationWhen(this.gid);
        notification.tickerText = this.tickerText;
        notification.setLatestEventInfo(this.context, this.title, this.content, contentIntent);
        return notification;
    }

    /* access modifiers changed from: protected */
    public PendingIntent getPendingIntent() {
        if (this.defPendingIntentType == 0) {
            return PendingIntent.getBroadcast(this.context, Integer.parseInt(this.gid), getIntent(), 134217728);
        }
        if (this.defPendingIntentType == 2) {
            return PendingIntent.getService(this.context, Integer.parseInt(this.gid), getIntent(), 134217728);
        }
        return PendingIntent.getActivity(this.context, Integer.parseInt(this.gid), getIntent(), 134217728);
    }

    public void setNotiDefaults(int notiDefaults) {
        this.notiDefaults = notiDefaults;
    }

    @SuppressLint({"NewApi"})
    public Notification createNotifiSupportLater() {
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
        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.getNotification();
        notification.flags = this.flags;
        return notification;
    }

    private Intent getIntent() {
        if (this.intent == null) {
            this.intent = new Intent();
        }
        return this.intent;
    }

    /* access modifiers changed from: protected */
    public void setFlagsByLock(String lock) {
        if ("0".equals(lock)) {
            this.flags |= 1;
        } else {
            this.flags |= 32;
        }
    }

    public void setFlags(int flags) {
        this.flags |= flags;
    }

    protected static long getNotificationWhen(String gid) {
        Long when = (Long) WHENS.get(gid);
        if (when == null) {
            when = Long.valueOf(System.currentTimeMillis());
            WHENS.put(gid, when);
        }
        return when.longValue();
    }

    public static void setLogo(Context context, Notification notification, Bitmap data) {
        if (data != null) {
            try {
                RemoteViews contentView = notification.contentView;
                View view = LayoutInflater.from(context).inflate(contentView.getLayoutId(), null);
                if (view != null) {
                    ImageView logo = getNotiImageView(view);
                    if (logo != null) {
                        contentView.setImageViewBitmap(logo.getId(), data);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static ImageView getNotiImageView(View paramView) {
        if (paramView instanceof ImageView) {
            return (ImageView) paramView;
        }
        if (paramView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) paramView).getChildCount(); i++) {
                View localView = ((ViewGroup) paramView).getChildAt(i);
                if (localView instanceof ImageView) {
                    return (ImageView) localView;
                }
                if (localView instanceof ViewGroup) {
                    return getNotiImageView(localView);
                }
            }
        }
        return null;
    }

    private static int getNotiID(String gid) {
        int notiID = 0;
        try {
            return Integer.parseInt(gid);
        } catch (Exception e) {
            return notiID;
        }
    }

    /* access modifiers changed from: protected */
    public Class<?> getClasses(String name) {
        return ClsUtils.generateClasses(this.context, name);
    }
}
