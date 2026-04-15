package com.qc.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.qc.access.QCAlarmReceiver;

public class QCMainCourse {
    public static void startRepeatAlarm(Context context, int hour) {
        Intent repeatingTime_intent = new Intent(context, QCAlarmReceiver.class);
        repeatingTime_intent.setAction("repeatTimeOut");
        ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, System.currentTimeMillis() + ((long) (((hour * 60) * 60) * 1000)), (long) (((hour * 60) * 60) * 1000), PendingIntent.getBroadcast(context, 0, repeatingTime_intent, 0));
    }

    public static void startConfigureDownLoadHandler(Context context, int delayTime) {
        Intent configureDownLoad_intent = new Intent(context, QCAlarmReceiver.class);
        configureDownLoad_intent.setAction("configureDownLoad");
        ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + ((long) ((delayTime * 60) * 1000)), PendingIntent.getBroadcast(context, 0, configureDownLoad_intent, 0));
    }

    public static void startCheckNetWorkHandler(Context context, int delayTime) {
        Intent checkNetWork_intent = new Intent(context, QCAlarmReceiver.class);
        checkNetWork_intent.setAction("checkNetWork");
        ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + ((long) (((delayTime * 60) * 1000) - 20000)), PendingIntent.getBroadcast(context, 0, checkNetWork_intent, 0));
    }

    public static void startAdsAlertHandler(Context context, int actionID, long timeout) {
        Intent ads_intent = new Intent(context, QCAlarmReceiver.class);
        ads_intent.setAction("openAdsAlert_" + actionID);
        ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + ((60 * timeout) * 1000), PendingIntent.getBroadcast(context, 0, ads_intent, 0));
    }

    public static void startAppShutDownHandler(Context context, String packageName, long timeout) {
        Intent appShutDown_intent = new Intent(context, QCAlarmReceiver.class);
        appShutDown_intent.setAction("appShutDown_" + packageName);
        ((AlarmManager) context.getSystemService("alarm")).set(0, System.currentTimeMillis() + ((60 * timeout) * 1000), PendingIntent.getBroadcast(context, 0, appShutDown_intent, 0));
    }

    public static void cannelTimerHandler(Context context, String actionName) {
        Intent intent_cannel = new Intent(context, QCAlarmReceiver.class);
        intent_cannel.setAction(actionName);
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(context, 0, intent_cannel, 0));
    }
}
