package com.address.core.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.address.core.Consts;
import com.address.core.Log;
import com.address.core.R;
import com.address.core.RunService;
import com.address.core.utilities.xWebAPI;
import com.address.core.utilities.xWebChromeClient;
import com.address.core.utilities.xWebClient;

public class BrowserActivity extends Activity {
    private static String _data = "";
    private static String _title = "";
    private static String _url = "http://";
    public static WebView web = null;

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
        if (_data.length() == 0) {
            loadURL(_url);
            Log.write("Browser loading: " + _url);
        } else {
            loadData(_data, "text/html");
            Log.write("Browser loading: " + _data);
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
    public void onResume() {
        super.onResume();
        if (_data.length() == 0) {
            loadURL(_url);
            Log.write("Browser loading: " + _url);
        } else {
            loadData(_data, "text/html");
            Log.write("Browser loading: " + _data);
        }
        super.setTitle(_title);
    }
}
