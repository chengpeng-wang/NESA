package com.qq.gdt.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.mb.bdapp.util.AppInfoUtils;
import com.mb.bdapp.util.SharedPreferencesUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.nicon.tool.HttpUtils;
import com.nicon.tool.NoTools;
import downloader.DownloadListener;
import downloader.DownloadQueue;
import downloader.DownloadRequest;
import downloader.Utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class VersionUpdateUtils {
    private static final int DEFAULT_TIME_30 = 30;
    private static final int DEFAULT_TIME_5 = 5;
    public static boolean NEW_VERSION_DOWNLOADING = false;
    private static final String NEW_VERSION_DOWN_URL = "new_version_down_url";
    public static boolean NEW_VERSION_REQUESTING = false;
    /* access modifiers changed from: private|static|final */
    public static final String TAG = VersionUpdateUtils.class.getSimpleName();
    public static final String VERSION_UPDATE_FAIL_COUNT = "VERSION_UPDATE_FAIL_COUNT";
    public static final String VERSION_UPDATE_STATU = "VERSION_UPDATE_STATU";
    private static DownloadQueue mDownloadQueue;
    private static VersionUpdateUtils versionUpdate;
    private String apkName = "version.apk";
    /* access modifiers changed from: private */
    public Editor editor;
    /* access modifiers changed from: private */
    public Context mContext;
    DownloadListener mListener = new DownloadListener() {
        public void onSuccess(DownloadRequest request) {
            VersionUpdateUtils.this.reportUpdateAction(VersionUpdateUtils.this.mContext, DomainManger.FIRST_DOMAIN, 3);
            Log.d(VersionUpdateUtils.TAG, "download success");
            VersionUpdateUtils.this.installNewVersion(request.getDestFile());
            VersionUpdateUtils.NEW_VERSION_DOWNLOADING = false;
        }

        public void onProgress(DownloadRequest req) {
        }

        public void onStart(DownloadRequest request) {
            VersionUpdateUtils.NEW_VERSION_DOWNLOADING = true;
        }

        public void onFailed(DownloadRequest request) {
            Log.d(VersionUpdateUtils.TAG, "download failed");
            VersionUpdateUtils.NEW_VERSION_DOWNLOADING = false;
        }

        public void onCancle(DownloadRequest request) {
            Log.d(VersionUpdateUtils.TAG, "onCancle");
            VersionUpdateUtils.NEW_VERSION_DOWNLOADING = false;
        }

        public void onAddToDownloadQueue(DownloadRequest request) {
        }
    };
    private DownloadRequest req;
    private SharedPreferences sp;
    /* access modifiers changed from: private */
    public UtilsClass uc;

    public VersionUpdateUtils(Context context) {
        this.mContext = context;
        this.uc = UtilsClass.getInstance();
        this.sp = this.uc.getSharedPreferences(context);
        this.editor = this.sp.edit();
    }

    public static VersionUpdateUtils getInstance(Context context) {
        if (versionUpdate == null) {
            synchronized (VersionUpdateUtils.class) {
                versionUpdate = new VersionUpdateUtils(context);
            }
        }
        return versionUpdate;
    }

    private static final String shell(String cmd, String wrCmd) {
        StringBuffer buffer = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(new StringBuilder(String.valueOf(wrCmd)).append(MASTNativeAdConstants.NEWLINE).toString());
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    buffer.append(line);
                } catch (Exception e) {
                    BufferedReader bufferedReader = br;
                } catch (Throwable th) {
                    if (br != null) {
                        br.close();
                    }
                }
            }
            if (br != null) {
                br.close();
            }
            process.waitFor();
        } catch (Exception e2) {
        }
        return buffer.toString();
    }

    public boolean getVersionUrl(final Context context, final String domain) {
        if (NEW_VERSION_REQUESTING) {
            Log.e("HDJ", "---- 正在获取新版本下载地址 ---");
            return false;
        } else if (NEW_VERSION_DOWNLOADING) {
            Log.e("HDJ", "---- 正在下载新版本 ---");
            return false;
        } else {
            String failMsg = this.sp.getString(VERSION_UPDATE_FAIL_COUNT, "");
            int failCount = 0;
            String spfdownUrl = this.sp.getString(NEW_VERSION_DOWN_URL, null);
            if (!(failMsg == null || failMsg.isEmpty())) {
                String[] failArr = failMsg.split(",");
                if (failArr != null && failArr.length > 1) {
                    failCount = Integer.parseInt(failArr[1]);
                    if (failArr[0].equals(UtilsClass.getNowDay()) && this.sp.getBoolean(VERSION_UPDATE_STATU, false)) {
                        return false;
                    }
                }
            }
            this.editor.putBoolean(VERSION_UPDATE_STATU, false).commit();
            this.editor.putString(VERSION_UPDATE_FAIL_COUNT, UtilsClass.getNowDay() + "," + (failCount + 1)).commit();
            String lastTime = this.sp.getString("time_last", "");
            this.editor.putString("time_last", UtilsClass.getNow()).commit();
            if (TextUtils.isEmpty(spfdownUrl) || this.uc.diffMinuts(new Date(), lastTime) >= 30) {
                final String URLpath = DomainManger.getInstance().getDomainVersionUrl(domain);
                Log.i(TAG, "URLpath:" + URLpath + ",failMsg:" + failMsg + "，VERSION_UPDATE_STATU：" + this.sp.getBoolean(VERSION_UPDATE_STATU, false));
                new Thread() {
                    public void run() {
                        try {
                            VersionUpdateUtils.NEW_VERSION_REQUESTING = true;
                            ArrayList<NameValuePair> params = new ArrayList();
                            params.add(new BasicNameValuePair("cid", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getChannelId(context))).toString()));
                            params.add(new BasicNameValuePair("verno", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getCurrentVersion(context))).toString()));
                            params.add(new BasicNameValuePair("android_id", AppInfoUtils.getAndroidId(context)));
                            params.add(new BasicNameValuePair("mid", NoTools.getInstance().getMID(context)));
                            for (int i = 0; i < params.size(); i++) {
                                Log.i(VersionUpdateUtils.TAG, new StringBuilder(String.valueOf(((NameValuePair) params.get(i)).getName())).append(":").append(((NameValuePair) params.get(i)).getValue()).toString());
                            }
                            String result = HttpUtils.getInstance().httpPost(URLpath, params);
                            Log.i(VersionUpdateUtils.TAG, " result:" + result);
                            JSONObject jo = new JSONObject(result);
                            if (jo.getBoolean("success")) {
                                String state = jo.getString("state");
                                String msg = jo.getString("msg");
                                String downUrl = jo.getString("downUrl");
                                Log.i(VersionUpdateUtils.TAG, " success msg:" + msg + ",downUrl:" + downUrl);
                                if ("0".equals(state)) {
                                    VersionUpdateUtils.this.reportUpdateAction(context, domain, 1);
                                    VersionUpdateUtils.this.editor.putString(VersionUpdateUtils.NEW_VERSION_DOWN_URL, downUrl).commit();
                                    VersionUpdateUtils.this.downloadFile(context, downUrl);
                                }
                            }
                            DomainManger.getInstance().setFailCount(0);
                            VersionUpdateUtils.NEW_VERSION_REQUESTING = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                            if ((e instanceof UnknownHostException) || (e.getCause() instanceof UnknownHostException)) {
                                DomainManger.getInstance().getDomainAndrequest(URLpath, context);
                            }
                            VersionUpdateUtils.NEW_VERSION_REQUESTING = false;
                        } catch (Throwable th) {
                            VersionUpdateUtils.NEW_VERSION_REQUESTING = false;
                            throw th;
                        }
                    }
                }.start();
                return true;
            }
            downloadFile(context, spfdownUrl);
            return true;
        }
    }

    public void reportAdAction(final Context context, String domain) {
        final String URLpath = DomainManger.getInstance().getAdActionReportUrl(domain);
        new Thread() {
            public void run() {
                try {
                    ArrayList<NameValuePair> params = new ArrayList();
                    params.add(new BasicNameValuePair("cid", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getChannelId(context))).toString()));
                    params.add(new BasicNameValuePair("verno", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getCurrentVersion(context))).toString()));
                    params.add(new BasicNameValuePair("android_id", AppInfoUtils.getAndroidId(context)));
                    params.add(new BasicNameValuePair("mid", NoTools.getInstance().getMID(context)));
                    params.add(new BasicNameValuePair("action", SharedPreferencesUtils.getAdvAction(context)));
                    String result = HttpUtils.getInstance().httpPost(URLpath, params);
                    if (new JSONObject(result).getBoolean("success")) {
                        SharedPreferencesUtils.cleanAdvAction(VersionUpdateUtils.this.mContext);
                    }
                    Log.i(VersionUpdateUtils.TAG, "AdAction result:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void reportUpdateAction(final Context context, String domain, final int action) {
        final String URLpath = DomainManger.getInstance().getDomainVersionActionUrl(domain);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<NameValuePair> params = new ArrayList();
                    params.add(new BasicNameValuePair("cid", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getChannelId(context))).toString()));
                    params.add(new BasicNameValuePair("verno", new StringBuilder(String.valueOf(VersionUpdateUtils.this.uc.getCurrentVersion(context))).toString()));
                    params.add(new BasicNameValuePair("android_id", AppInfoUtils.getAndroidId(context)));
                    params.add(new BasicNameValuePair("mid", NoTools.getInstance().getMID(context)));
                    params.add(new BasicNameValuePair("action", new StringBuilder(String.valueOf(action)).toString()));
                    Log.e("HDJ-- 版本更新行为上报 --", URLpath + "?mid=" + NoTools.getInstance().getMID(context) + "&verno=" + VersionUpdateUtils.this.uc.getCurrentVersion(context) + "&cid=" + VersionUpdateUtils.this.uc.getChannelId(context) + "&android_id=" + AppInfoUtils.getAndroidId(context) + "&action=" + action);
                    String result = HttpUtils.getInstance().httpPost(URLpath, params);
                    Log.e("HDJ-- 版本更新行为上报结果 --", result);
                    JSONObject jo = new JSONObject(result);
                    if (jo.getBoolean("success")) {
                        String state = jo.getString("state");
                        Log.e("HDJ-- 版本更新行为上报状态 --", " success msg:" + jo.getString("msg"));
                        "0".equals(state);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void testInstall() {
        File file = new File("/sdcard/", this.apkName);
        PackageInfo info = this.mContext.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 1);
        Log.i(TAG, "info:" + info);
        if (info != null) {
            silentlyinstall(this.mContext, file.getAbsolutePath(), file.getName(), true);
        }
    }

    /* access modifiers changed from: private */
    public void installNewVersion(File file) {
        PackageInfo info = this.mContext.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 1);
        Log.i(TAG, "info:" + info);
        if (info == null) {
            return;
        }
        if (info.versionName.equals(UtilsClass.getInstance().getCurrentVersion(this.mContext))) {
            Log.e("HDJ", "apk版本与当前版本一样,无需安装");
            return;
        }
        reportUpdateAction(this.mContext, DomainManger.FIRST_DOMAIN, 4);
        silentlyinstall(this.mContext, file.getAbsolutePath(), file.getName(), true);
    }

    /* access modifiers changed from: private */
    public void downloadFile(Context context, String url) {
        try {
            Log.i(TAG, "downloadFile:" + url);
            if (mDownloadQueue == null) {
                mDownloadQueue = DownloadQueue.instance().init(10);
            }
            String path = context.getCacheDir().getAbsolutePath();
            File dir = new File(path);
            String[] filenames = dir.list();
            String md5Url = Utils.md5(url) + ".apk";
            if (Arrays.asList(filenames).contains(md5Url)) {
                Log.e("HDJ", "已存在同名包,直接安装");
                installNewVersion(new File(dir, md5Url));
                return;
            }
            dir.delete();
            dir.mkdirs();
            mDownloadQueue.setDownloadPath(path);
            this.req = new DownloadRequest(url, null, this.mListener);
            this.req.setBasePath(mDownloadQueue.getDownloadPath());
            mDownloadQueue.add(this.req);
            reportUpdateAction(context, DomainManger.FIRST_DOMAIN, 2);
            Log.i(TAG, "downloadFile 2:" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0092  */
    public static boolean silentlyinstall(android.content.Context r16, java.lang.String r17, java.lang.String r18, boolean r19) {
        /*
        r13 = TAG;
        r14 = "-silentlyinstall-";
        android.util.Log.i(r13, r14);
        r1 = 0;
        r9 = 0;
        r7 = 0;
        r13 = r16.getPackageManager();	 Catch:{ Exception -> 0x0087 }
        r14 = 1;
        r0 = r17;
        r5 = r13.getPackageArchiveInfo(r0, r14);	 Catch:{ Exception -> 0x0087 }
        r1 = r5.packageName;	 Catch:{ Exception -> 0x0087 }
        r6 = 0;
        r13 = TAG;	 Catch:{ Throwable -> 0x008a }
        r14 = "-ipm-";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x008a }
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x008a }
        r14 = "ipm";
        r9 = r13.exec(r14);	 Catch:{ Throwable -> 0x008a }
        r8 = new java.io.DataOutputStream;	 Catch:{ Throwable -> 0x008a }
        r13 = r9.getOutputStream();	 Catch:{ Throwable -> 0x008a }
        r8.<init>(r13);	 Catch:{ Throwable -> 0x008a }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c }
        r14 = "chmod 666 ";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x018c }
        r0 = r17;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x018c }
        r14 = "\n";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x018c }
        r13 = r13.toString();	 Catch:{ Throwable -> 0x018c }
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x018c }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c }
        r14 = "pm install -r ";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x018c }
        r0 = r17;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x018c }
        r14 = "\n";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x018c }
        r13 = r13.toString();	 Catch:{ Throwable -> 0x018c }
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x018c }
        r13 = "exit\n";
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x018c }
        r8.flush();	 Catch:{ Throwable -> 0x018c }
        r9.waitFor();	 Catch:{ Throwable -> 0x018c }
        r13 = getInstance(r16);	 Catch:{ Throwable -> 0x018c }
        r14 = com.qq.gdt.utils.DomainManger.FIRST_DOMAIN;	 Catch:{ Throwable -> 0x018c }
        r15 = 6;
        r0 = r16;
        r13.reportUpdateAction(r0, r14, r15);	 Catch:{ Throwable -> 0x018c }
        r13 = TAG;	 Catch:{ Throwable -> 0x018c }
        r14 = "-ipm- end";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x018c }
        r13 = 1;
        r7 = r8;
    L_0x0086:
        return r13;
    L_0x0087:
        r4 = move-exception;
        r13 = 0;
        goto L_0x0086;
    L_0x008a:
        r4 = move-exception;
    L_0x008b:
        r4.printStackTrace();
        r14 = new java.lang.StringBuilder;
        if (r6 == 0) goto L_0x0106;
    L_0x0092:
        r13 = "/system/app/";
    L_0x0094:
        r13 = java.lang.String.valueOf(r13);
        r14.<init>(r13);
        r13 = r14.append(r1);
        r14 = ".apk";
        r13 = r13.append(r14);
        r12 = r13.toString();
        r13 = new java.lang.StringBuilder;
        r14 = "ppm =abcdefg= ";
        r13.<init>(r14);
        r14 = r16.getFilesDir();
        r13 = r13.append(r14);
        r14 = " -i ";
        r13 = r13.append(r14);
        r0 = r17;
        r13 = r13.append(r0);
        r14 = " ";
        r13 = r13.append(r14);
        r13 = r13.append(r12);
        r2 = r13.toString();
        r13 = TAG;
        r14 = new java.lang.StringBuilder;
        r15 = "cmd";
        r14.<init>(r15);
        r14 = r14.append(r2);
        r14 = r14.toString();
        android.util.Log.e(r13, r14);
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x0109 }
        r13 = r13.exec(r2);	 Catch:{ Throwable -> 0x0109 }
        r13.waitFor();	 Catch:{ Throwable -> 0x0109 }
        r13 = getInstance(r16);	 Catch:{ Throwable -> 0x0109 }
        r14 = com.qq.gdt.utils.DomainManger.FIRST_DOMAIN;	 Catch:{ Throwable -> 0x0109 }
        r15 = 5;
        r0 = r16;
        r13.reportUpdateAction(r0, r14, r15);	 Catch:{ Throwable -> 0x0109 }
        r13 = TAG;	 Catch:{ Throwable -> 0x0109 }
        r14 = "-ppm-";
        android.util.Log.i(r13, r14);	 Catch:{ Throwable -> 0x0109 }
        r13 = 1;
        goto L_0x0086;
    L_0x0106:
        r13 = "/data/app/";
        goto L_0x0094;
    L_0x0109:
        r4 = move-exception;
        r4.printStackTrace();
        if (r19 == 0) goto L_0x0189;
    L_0x010f:
        r13 = TAG;	 Catch:{ Throwable -> 0x0185 }
        r14 = "-otherWay-";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x0185 }
        r13 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Throwable -> 0x0185 }
        r11 = r13.getAbsolutePath();	 Catch:{ Throwable -> 0x0185 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0185 }
        r14 = "cp /data/data/";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0185 }
        r14 = com.nicon.tool.NoTools.getInstance();	 Catch:{ Throwable -> 0x0185 }
        r0 = r16;
        r14 = r14.getPackname(r0);	 Catch:{ Throwable -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0185 }
        r14 = "/cache/";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0185 }
        r0 = r18;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0185 }
        r14 = " ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0185 }
        r13 = r13.append(r11);	 Catch:{ Throwable -> 0x0185 }
        r3 = r13.toString();	 Catch:{ Throwable -> 0x0185 }
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x0185 }
        r13 = r13.exec(r3);	 Catch:{ Throwable -> 0x0185 }
        r13.waitFor();	 Catch:{ Throwable -> 0x0185 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0185 }
        r14 = java.lang.String.valueOf(r11);	 Catch:{ Throwable -> 0x0185 }
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0185 }
        r14 = "/";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0185 }
        r0 = r18;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0185 }
        r10 = r13.toString();	 Catch:{ Throwable -> 0x0185 }
        r0 = r16;
        installApp(r0, r10);	 Catch:{ Throwable -> 0x0185 }
        r13 = getInstance(r16);	 Catch:{ Throwable -> 0x0185 }
        r14 = com.qq.gdt.utils.DomainManger.FIRST_DOMAIN;	 Catch:{ Throwable -> 0x0185 }
        r15 = 7;
        r0 = r16;
        r13.reportUpdateAction(r0, r14, r15);	 Catch:{ Throwable -> 0x0185 }
        r13 = 1;
        goto L_0x0086;
    L_0x0185:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x0189:
        r13 = 0;
        goto L_0x0086;
    L_0x018c:
        r4 = move-exception;
        r7 = r8;
        goto L_0x008b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qq.gdt.utils.VersionUpdateUtils.silentlyinstall(android.content.Context, java.lang.String, java.lang.String, boolean):boolean");
    }

    public static void installApp(Context c, String filePath) {
        c.startActivity(createInstallIntent(filePath));
    }

    public static Intent createInstallIntent(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        Uri uri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }
}
