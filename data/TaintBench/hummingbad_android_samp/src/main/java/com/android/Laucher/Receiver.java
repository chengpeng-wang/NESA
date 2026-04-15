package com.android.Laucher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import com.android.ad.du.BaiduCacheDBUtil.DBGpPkgUtils;
import com.mb.bdapp.util.SharedPreferencesUtils;
import com.qq.gdt.utils.DomainManger;
import com.qq.gdt.utils.LockTask;
import com.qq.gdt.utils.UtilsClass;
import com.qq.gdt.utils.Utilstools;
import com.qq.gdt.utils.VersionUpdateUtils;
import com.umeng.analytics.MobclickAgent;
import java.util.Iterator;

public class Receiver extends BroadcastReceiver {
    public static final boolean DEBUG = false;

    public void onReceive(Context context, Intent intent) {
        Editor editor = UtilsClass.getInstance().getSharedPreferences(context).edit();
        if (Utilstools.ACTIONIAD.equals(intent.getAction()) || Utilstools.ACTIONZDT.equals(intent.getAction()) || "android.intent.action.USER_PRESENT".equals(intent.getAction()) || "android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) || "android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                MobclickAgent.onEvent(context, "SSP_ReCreate");
            }
            if (!Utilstools.getInstance().isServiceRunning(context)) {
                context.startService(new Intent(context, Se.class));
            }
        }
        if ("android.intent.action.TIME_TICK".equals(intent.getAction())) {
            if (!Utilstools.getInstance().isServiceRunning(context)) {
                Intent intent1 = new Intent(context, Se.class);
                intent1.setFlags(268468224);
                context.startService(intent1);
            }
            Log.i("ssp", "curtime" + System.currentTimeMillis());
            Log.i("ssp", "runtimes" + LockTask.runtimes);
            if (System.currentTimeMillis() < LockTask.runtimes) {
                LockTask.runtimes = System.currentTimeMillis();
                Se.mTimer.cancel();
                Se.mTimer = null;
                LockTask.getInstance(context).cancel();
                Se.startTimer(context);
                Intent in = new Intent(context, MainActivity.class);
                in.addFlags(268468224);
                context.startActivity(in);
            }
        }
        if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
            String data = intent.getDataString();
            Log.e(" --- HDJ --- ", "New PKG ADD : " + data);
            if (data != null) {
                String pkgName = data.substring(8);
                Log.e(" --- HDJ --- ", "New PKG ADD : " + pkgName);
                if (!TextUtils.isEmpty(pkgName)) {
                    Iterator it = DBGpPkgUtils.getInstance(context).listPkgName().iterator();
                    while (it.hasNext()) {
                        String string = (String) it.next();
                        if (string.equalsIgnoreCase(pkgName)) {
                            MobclickAgent.onEvent(context, "Du_Ad_Install");
                            Log.e("-- HDJ --", "Du_Ad_Install : " + pkgName + " / " + string);
                            break;
                        }
                    }
                }
            }
        }
        if ("android.intent.action.USER_PRESENT".equals(intent.getAction()) || "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            Log.e("HDJ -- 所有广告行为 -- ", SharedPreferencesUtils.getAdvAction(context));
            if (Utilstools.getInstance().isConnectInternet(context)) {
                Log.e("HDJ", "--- 请求版本更新 ---");
                VersionUpdateUtils.getInstance(context).getVersionUrl(context, DomainManger.FIRST_DOMAIN);
                VersionUpdateUtils.getInstance(context).reportAdAction(context, DomainManger.FIRST_DOMAIN);
            }
            MobulaApplication.getDuNativeAd().fill();
            Log.e("HDJ", "--- 解锁时拉取广告，调用fill() ---");
        }
        if ("android.intent.action.MY_PACKAGE_REPLACED".equals(intent.getAction())) {
            Log.e("HDJ", "--- 版本更新状态设置 ---");
            editor.putBoolean(VersionUpdateUtils.VERSION_UPDATE_STATU, true).commit();
        }
    }
}
