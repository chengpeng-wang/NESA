package com.moceanmobile.mast;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.SslError;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mopub.common.Constants;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserDialog extends Dialog {
    private static final int ActionBarHeightDp = 40;
    /* access modifiers changed from: private */
    public ImageView backButton = null;
    /* access modifiers changed from: private */
    public ImageView forwardButton = null;
    /* access modifiers changed from: private|final */
    public final Handler handler;
    private OnTouchListener mButtonTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    v.setBackgroundColor(BrowserDialog.this.getContext().getResources().getColor(17170447));
                    break;
                default:
                    v.setBackgroundColor(BrowserDialog.this.getContext().getResources().getColor(17170446));
                    break;
            }
            return false;
        }
    };
    RelativeLayout mContentView;
    private String url = null;
    /* access modifiers changed from: private */
    public WebView webView = null;

    private class Client extends WebViewClient {
        ProgressBar progressBar;

        private Client() {
            this.progressBar = new ProgressBar(BrowserDialog.this.getContext(), null, 16842871);
        }

        /* synthetic */ Client(BrowserDialog browserDialog, Client client) {
            this();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (this.progressBar.getParent() == null) {
                LayoutParams layoutParams = new LayoutParams(-2, -2);
                layoutParams.addRule(13);
                BrowserDialog.this.mContentView.addView(this.progressBar, layoutParams);
            }
            this.progressBar.setVisibility(0);
            super.onPageStarted(view, url, favicon);
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            this.progressBar.setVisibility(8);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public void onPageFinished(WebView view, String url) {
            BrowserDialog.this.backButton.setEnabled(view.canGoBack());
            BrowserDialog.this.forwardButton.setEnabled(view.canGoForward());
            this.progressBar.setVisibility(8);
        }

        @SuppressLint({"DefaultLocale"})
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (new URI(url).getScheme().toLowerCase().startsWith(Constants.HTTP)) {
                    return false;
                }
            } catch (URISyntaxException e) {
            }
            BrowserDialog.this.handler.browserDialogOpenUrl(BrowserDialog.this, url, false);
            return true;
        }
    }

    public interface Handler {
        void browserDialogDismissed(BrowserDialog browserDialog);

        void browserDialogOpenUrl(BrowserDialog browserDialog, String str, boolean z);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public BrowserDialog(Context context, String url, Handler handler) {
        super(context, 16973833);
        this.url = url;
        this.handler = handler;
        Resources resources = getContext().getResources();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        this.mContentView = new RelativeLayout(getContext());
        this.mContentView.setBackgroundColor(-1);
        setContentView(this.mContentView, layoutParams);
        LayoutParams layoutParams2 = new LayoutParams(-1, dpToPx(40));
        layoutParams2.addRule(12);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setId(100);
        linearLayout.setBackgroundColor(-15066598);
        linearLayout.setOrientation(0);
        linearLayout.setVerticalGravity(16);
        this.mContentView.addView(linearLayout, layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -1, 1.0f);
        layoutParams3.setMargins(2, 4, 2, 2);
        ScaleType scaleType = ScaleType.FIT_CENTER;
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(scaleType);
        imageView.setImageDrawable(new BitmapDrawable(resources, BrowserDialog.class.getResourceAsStream("/ic_action_cancel.png")));
        imageView.setBackgroundColor(getContext().getResources().getColor(17170446));
        imageView.setOnTouchListener(this.mButtonTouchListener);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserDialog.this.dismiss();
            }
        });
        linearLayout.addView(imageView, layoutParams3);
        this.backButton = new ImageView(getContext());
        this.backButton.setImageDrawable(new BitmapDrawable(resources, BrowserDialog.class.getResourceAsStream("/ic_action_back.png")));
        this.backButton.setBackgroundColor(getContext().getResources().getColor(17170446));
        this.backButton.setScaleType(scaleType);
        this.backButton.setEnabled(false);
        imageView.setOnTouchListener(this.mButtonTouchListener);
        this.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserDialog.this.webView.goBack();
            }
        });
        linearLayout.addView(this.backButton, layoutParams3);
        this.forwardButton = new ImageView(getContext());
        this.forwardButton.setImageDrawable(new BitmapDrawable(resources, BrowserDialog.class.getResourceAsStream("/ic_action_forward.png")));
        this.forwardButton.setBackgroundColor(getContext().getResources().getColor(17170446));
        this.forwardButton.setScaleType(scaleType);
        this.forwardButton.setEnabled(false);
        imageView.setOnTouchListener(this.mButtonTouchListener);
        this.forwardButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserDialog.this.webView.goForward();
            }
        });
        linearLayout.addView(this.forwardButton, layoutParams3);
        imageView = new ImageView(getContext());
        imageView.setScaleType(scaleType);
        imageView.setImageDrawable(new BitmapDrawable(resources, BrowserDialog.class.getResourceAsStream("/ic_action_refresh.png")));
        imageView.setBackgroundColor(getContext().getResources().getColor(17170446));
        imageView.setOnTouchListener(this.mButtonTouchListener);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserDialog.this.webView.reload();
            }
        });
        linearLayout.addView(imageView, layoutParams3);
        imageView = new ImageView(getContext());
        imageView.setScaleType(scaleType);
        imageView.setImageDrawable(new BitmapDrawable(resources, BrowserDialog.class.getResourceAsStream("/ic_action_web_site.png")));
        imageView.setBackgroundColor(getContext().getResources().getColor(17170446));
        imageView.setOnTouchListener(this.mButtonTouchListener);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserDialog.this.handler.browserDialogOpenUrl(BrowserDialog.this, BrowserDialog.this.webView.getUrl(), true);
            }
        });
        linearLayout.addView(imageView, layoutParams3);
        LayoutParams layoutParams4 = new LayoutParams(-1, 0);
        layoutParams4.addRule(10);
        layoutParams4.addRule(2, linearLayout.getId());
        this.webView = new WebView(getContext());
        this.webView.setWebViewClient(new Client(this, null));
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.mContentView.addView(this.webView, layoutParams4);
        setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                BrowserDialog.this.handler.browserDialogDismissed(BrowserDialog.this);
            }
        });
    }

    public int pxToDp(float px) {
        return (int) ((px / Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public int dpToPx(int dp) {
        return (int) ((Resources.getSystem().getDisplayMetrics().density * ((float) dp)) + 0.5f);
    }

    public void loadUrl(String url) {
        this.url = url;
        this.webView.stopLoading();
        this.webView.clearHistory();
        this.webView.loadUrl(url);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.webView.loadUrl(this.url);
    }
}
