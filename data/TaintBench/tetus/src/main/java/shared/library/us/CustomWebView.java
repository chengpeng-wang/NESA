package shared.library.us;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebView extends WebView {
    /* access modifiers changed from: private */
    public Context ctx;
    /* access modifiers changed from: private */
    public ProgressDialog pd = null;

    private class CustomWebViewClient extends WebViewClient {
        private CustomWebViewClient() {
        }

        /* synthetic */ CustomWebViewClient(CustomWebView customWebView, CustomWebViewClient customWebViewClient) {
            this();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!CustomWebView.this.pd.isShowing()) {
                CustomWebView.this.pd.show();
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (CustomWebView.this.pd.isShowing()) {
                CustomWebView.this.pd.dismiss();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String str = "android.intent.action.VIEW";
            try {
                Intent intent;
                if (url.contains("market://")) {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(url));
                    CustomWebView.this.ctx.startActivity(intent);
                }
                if (url.contains(".mp3")) {
                    MediaPlayer mp = MediaPlayer.create(CustomWebView.this.ctx, Uri.parse(url));
                    mp.setOnCompletionListener(new OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    mp.setOnPreparedListener(new OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
                if (url.contains(".mp4")) {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    CustomWebView.this.ctx.startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
            } catch (Exception e) {
                Log.i("CustomWebViewClient", e.getMessage());
            }
            return true;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadData("<html><body><b>Internal error occur, Please try again</b></body></html>", "text/html", "utf8");
        }
    }

    public CustomWebView(Context context) {
        super(context);
        clearCache(true);
        clearFormData();
        clearHistory();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setUserAgentString(null);
        requestFocus(130);
        setWebViewClient(new CustomWebViewClient(this, null));
        this.pd = new ProgressDialog(context);
        this.pd.setMessage("Loading...");
        this.ctx = context;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !canGoBack()) {
            return super.onKeyDown(keyCode, event);
        }
        goBack();
        return true;
    }
}
