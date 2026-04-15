package com.address.corel.lck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import com.address.core.Consts;
import com.address.core.R;
import com.address.core.RunService;
import com.address.core.utilities.xWebAPI;
import com.address.core.utilities.xWebChromeClient;
import com.address.core.utilities.xWebClient;

public class Lock extends Activity {
    private static String _data = "";
    private static String _title = "";
    private static String _url = "http://";
    public static WebView web = null;

    public void Launch(Boolean newtask) {
        if (RunService.getService().isLockerEnabled().booleanValue()) {
            Intent intent = new Intent(RunService.getService(), Lock.class);
            if (newtask.booleanValue()) {
                intent.addFlags(268435456);
            }
            intent.addFlags(67108864);
            intent.addFlags(536870912);
            startActivity(intent);
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.browser);
        web = (WebView) findViewById(R.id.browser);
        web.setWebViewClient(new xWebClient());
        web.setWebChromeClient(new xWebChromeClient());
        web.clearCache(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setBuiltInZoomControls(false);
        web.setVerticalScrollBarEnabled(true);
        web.setHorizontalScrollBarEnabled(true);
        web.addJavascriptInterface(new xWebAPI(this, web, this), "WebAPI");
        web.addJavascriptInterface(RunService.getService().getAPI(), "xAPI");
        web.addJavascriptInterface(new Consts(), "Consts");
        web.addJavascriptInterface(RunService.getService(), "Service");
        RunService svc = RunService.getService();
        if (Consts.locker.booleanValue() && svc.isLockerEnabled().booleanValue()) {
            if (svc.isLockerContentData().booleanValue()) {
                _data = svc.getLockerContent();
            } else if (svc.getLockerContent().length() != 0) {
                _url = svc.getLockerContent();
            } else {
                _url = Consts.lockerAddress;
            }
        }
        if (_data.length() == 0) {
            loadURL(_url);
        } else {
            loadData(_data, "text/html");
        }
        super.setTitle(_title);
    }

    public static void loadURL(String url) {
        web.loadUrl(url);
    }

    public static void loadData(String data, String mime) {
        web.loadData(data, mime, "utf-8");
    }

    public static void setURL(String url) {
        _url = url;
    }

    public static void setData(String data) {
        _data = data;
    }

    public static void setTitle(String title) {
        _title = title;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        Launch(Boolean.valueOf(false));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        RunService.getService();
        RunService.onTickHandler.sendEmptyMessageDelayed(555, 2000);
        super.onPause();
        Launch(Boolean.valueOf(true));
    }

    public void onBackPressed() {
        super.onBackPressed();
        Launch(Boolean.valueOf(true));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        RunService.getService();
        RunService.onTickHandler.sendEmptyMessageDelayed(555, 2000);
        super.onDestroy();
        Launch(Boolean.valueOf(true));
    }
}
