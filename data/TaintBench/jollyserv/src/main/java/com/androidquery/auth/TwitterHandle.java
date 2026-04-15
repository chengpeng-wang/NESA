package com.androidquery.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.androidquery.AQuery;
import com.androidquery.WebDialog;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import java.net.HttpURLConnection;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import org.apache.http.HttpRequest;

public class TwitterHandle extends AccountHandle {
    private static final String CALLBACK_URI = "twitter://callback";
    private static final String CANCEL_URI = "twitter://cancel";
    private static final String OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
    private static final String OAUTH_AUTHORIZE = "https://api.twitter.com/oauth/authorize";
    private static final String OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
    private static final String TW_SECRET = "aq.tw.secret";
    private static final String TW_TOKEN = "aq.tw.token";
    /* access modifiers changed from: private */
    public Activity act;
    /* access modifiers changed from: private */
    public CommonsHttpOAuthConsumer consumer;
    /* access modifiers changed from: private */
    public WebDialog dialog;
    /* access modifiers changed from: private */
    public CommonsHttpOAuthProvider provider;
    /* access modifiers changed from: private */
    public String secret = fetchToken(TW_SECRET);
    /* access modifiers changed from: private */
    public String token = fetchToken(TW_TOKEN);

    private class Task2 extends AsyncTask<String, String, String> {
        private Task2() {
        }

        /* synthetic */ Task2(TwitterHandle twitterHandle, Task2 task2) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            try {
                TwitterHandle.this.provider.retrieveAccessToken(TwitterHandle.this.consumer, params[0]);
                return "";
            } catch (Exception e) {
                AQUtility.report(e);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String url) {
            if (url != null) {
                TwitterHandle.this.token = TwitterHandle.this.consumer.getToken();
                TwitterHandle.this.secret = TwitterHandle.this.consumer.getTokenSecret();
                AQUtility.debug("token", TwitterHandle.this.token);
                AQUtility.debug("secret", TwitterHandle.this.secret);
                TwitterHandle.this.storeToken(TwitterHandle.TW_TOKEN, TwitterHandle.this.token, TwitterHandle.TW_SECRET, TwitterHandle.this.secret);
                TwitterHandle.this.dismiss();
                TwitterHandle.this.success(TwitterHandle.this.act);
                TwitterHandle.this.authenticated(TwitterHandle.this.secret, TwitterHandle.this.token);
                return;
            }
            TwitterHandle.this.failure();
            TwitterHandle.this.authenticated(null, null);
        }
    }

    private class Task extends AsyncTask<String, String, String> implements OnCancelListener, Runnable {
        /* access modifiers changed from: private */
        public AbstractAjaxCallback<?, ?> cb;

        private Task() {
        }

        /* synthetic */ Task(TwitterHandle twitterHandle, Task task) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            try {
                return TwitterHandle.this.provider.retrieveRequestToken(TwitterHandle.this.consumer, TwitterHandle.CALLBACK_URI);
            } catch (Exception e) {
                AQUtility.report(e);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String url) {
            if (url != null) {
                TwitterHandle.this.dialog = new WebDialog(TwitterHandle.this.act, url, new TwWebViewClient(TwitterHandle.this, null));
                TwitterHandle.this.dialog.setOnCancelListener(this);
                TwitterHandle.this.show();
                TwitterHandle.this.dialog.load();
                return;
            }
            TwitterHandle.this.failure();
        }

        public void onCancel(DialogInterface arg0) {
            TwitterHandle.this.failure();
        }

        public void run() {
            TwitterHandle.this.auth(this.cb);
        }
    }

    private class TwWebViewClient extends WebViewClient {
        private TwWebViewClient() {
        }

        /* synthetic */ TwWebViewClient(TwitterHandle twitterHandle, TwWebViewClient twWebViewClient) {
            this();
        }

        private boolean checkDone(String url) {
            if (url.startsWith(TwitterHandle.CALLBACK_URI)) {
                String verf = TwitterHandle.this.extract(url, "oauth_verifier");
                TwitterHandle.this.dismiss();
                new Task2(TwitterHandle.this, null).execute(new String[]{verf});
                return true;
            } else if (!url.startsWith(TwitterHandle.CANCEL_URI)) {
                return false;
            } else {
                TwitterHandle.this.failure();
                return true;
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return checkDone(url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            AQUtility.debug("started", url);
            if (!checkDone(url)) {
                super.onPageStarted(view, url, favicon);
            }
        }

        public void onPageFinished(WebView view, String url) {
            AQUtility.debug("finished", url);
            super.onPageFinished(view, url);
            TwitterHandle.this.show();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            TwitterHandle.this.failure();
        }
    }

    public TwitterHandle(Activity act, String consumerKey, String consumerSecret) {
        this.act = act;
        this.consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        if (!(this.token == null || this.secret == null)) {
            this.consumer.setTokenWithSecret(this.token, this.secret);
        }
        this.provider = new CommonsHttpOAuthProvider(OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN, OAUTH_AUTHORIZE);
    }

    public String getToken() {
        return this.token;
    }

    public String getSecret() {
        return this.secret;
    }

    /* access modifiers changed from: private */
    public void dismiss() {
        if (this.dialog != null) {
            new AQuery(this.act).dismiss(this.dialog);
            this.dialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void show() {
        if (this.dialog != null) {
            new AQuery(this.act).show(this.dialog);
        }
    }

    /* access modifiers changed from: private */
    public void failure() {
        dismiss();
        failure(this.act, 401, "cancel");
    }

    /* access modifiers changed from: protected */
    public void auth() {
        new Task(this, null).execute(new String[0]);
    }

    public void authenticate(boolean refreshToken) {
        if (refreshToken || this.token == null || this.secret == null) {
            auth();
        } else {
            authenticated(this.secret, this.token);
        }
    }

    /* access modifiers changed from: protected */
    public void authenticated(String secret, String token) {
    }

    private String fetchToken(String key) {
        return PreferenceManager.getDefaultSharedPreferences(this.act).getString(key, null);
    }

    /* access modifiers changed from: private */
    public void storeToken(String key1, String token1, String key2, String token2) {
        PreferenceManager.getDefaultSharedPreferences(this.act).edit().putString(key1, token1).putString(key2, token2).commit();
    }

    /* access modifiers changed from: private */
    public String extract(String url, String param) {
        return Uri.parse(url).getQueryParameter(param);
    }

    public boolean expired(AbstractAjaxCallback<?, ?> abstractAjaxCallback, AjaxStatus status) {
        int code = status.getCode();
        return code == 400 || code == 401;
    }

    public boolean reauth(AbstractAjaxCallback<?, ?> cb) {
        this.token = null;
        this.secret = null;
        storeToken(TW_TOKEN, null, TW_SECRET, null);
        new Task(this, null).cb = cb;
        AQUtility.post(cb);
        return false;
    }

    public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpRequest request) {
        AQUtility.debug("apply token", cb.getUrl());
        try {
            this.consumer.sign(request);
        } catch (Exception e) {
            AQUtility.report(e);
        }
    }

    public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpURLConnection conn) {
        AQUtility.debug("apply token multipart", cb.getUrl());
        OAuthConsumer oac = new DefaultOAuthConsumer(this.consumer.getConsumerKey(), this.consumer.getConsumerSecret());
        oac.setTokenWithSecret(this.consumer.getToken(), this.consumer.getTokenSecret());
        try {
            oac.sign(conn);
        } catch (Exception e) {
            AQUtility.report(e);
        }
    }

    public boolean authenticated() {
        return (this.token == null || this.secret == null) ? false : true;
    }

    public void unauth() {
        this.token = null;
        this.secret = null;
        CookieSyncManager.createInstance(this.act);
        CookieManager.getInstance().removeAllCookie();
        storeToken(TW_TOKEN, null, TW_SECRET, null);
    }
}
