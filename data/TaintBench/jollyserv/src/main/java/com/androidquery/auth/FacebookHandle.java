package com.androidquery.auth;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.androidquery.AQuery;
import com.androidquery.WebDialog;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

public class FacebookHandle extends AccountHandle {
    private static final String CANCEL_URI = "fbconnect:cancel";
    public static final String FB_APP_SIGNATURE = "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2";
    private static final String FB_PERMISSION = "aq.fb.permission";
    private static final String FB_TOKEN = "aq.fb.token";
    private static final String OAUTH_ENDPOINT = "https://graph.facebook.com/oauth/authorize";
    private static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";
    private static Boolean hasSSO;
    /* access modifiers changed from: private */
    public Activity act;
    private String appId;
    private WebDialog dialog;
    /* access modifiers changed from: private */
    public boolean first;
    private String message;
    /* access modifiers changed from: private */
    public String permissions;
    private int requestId;
    private boolean sso;
    /* access modifiers changed from: private */
    public String token;

    private class FbWebViewClient extends WebViewClient implements OnCancelListener {
        private FbWebViewClient() {
        }

        /* synthetic */ FbWebViewClient(FacebookHandle facebookHandle, FbWebViewClient fbWebViewClient) {
            this();
        }

        private boolean checkDone(String url) {
            if (url.startsWith(FacebookHandle.REDIRECT_URI)) {
                String error = FacebookHandle.parseUrl(url).getString("error_reason");
                AQUtility.debug("error", error);
                if (error == null) {
                    FacebookHandle.this.token = FacebookHandle.this.extractToken(url);
                }
                if (FacebookHandle.this.token != null) {
                    FacebookHandle.this.dismiss();
                    FacebookHandle.this.storeToken(FacebookHandle.this.token, FacebookHandle.this.permissions);
                    FacebookHandle.this.first = false;
                    FacebookHandle.this.authenticated(FacebookHandle.this.token);
                    FacebookHandle.this.success(FacebookHandle.this.act);
                    return true;
                }
                FacebookHandle.this.failure();
                return true;
            } else if (!url.startsWith(FacebookHandle.CANCEL_URI)) {
                return false;
            } else {
                AQUtility.debug((Object) "cancelled");
                FacebookHandle.this.failure();
                return true;
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AQUtility.debug("return url: " + url);
            return checkDone(url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            AQUtility.debug("started", url);
            if (!checkDone(url)) {
                super.onPageStarted(view, url, favicon);
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            FacebookHandle.this.show();
            AQUtility.debug("finished", url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            FacebookHandle.this.failure();
        }

        public void onCancel(DialogInterface dialog) {
            FacebookHandle.this.failure();
        }
    }

    public FacebookHandle(Activity act, String appId, String permissions) {
        this(act, appId, permissions, null);
    }

    public FacebookHandle(Activity act, String appId, String permissions, String accessToken) {
        this.appId = appId;
        this.act = act;
        this.permissions = permissions;
        this.token = accessToken;
        if (this.token == null && permissionOk(permissions, fetchPermission())) {
            this.token = fetchToken();
        }
        this.first = this.token == null;
    }

    public String getToken() {
        return this.token;
    }

    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FB_TOKEN, null);
    }

    public FacebookHandle sso(int requestId) {
        this.sso = true;
        this.requestId = requestId;
        return this;
    }

    private boolean permissionOk(String permissions, String old) {
        if (permissions == null) {
            return true;
        }
        if (old == null) {
            return false;
        }
        Set<String> oldSet = new HashSet(Arrays.asList(old.split("[,\\s]+")));
        String[] splits = permissions.split("[,\\s]+");
        int i = 0;
        while (i < splits.length) {
            if (oldSet.contains(splits[i])) {
                i++;
            } else {
                AQUtility.debug((Object) "perm mismatch");
                return false;
            }
        }
        return true;
    }

    public FacebookHandle message(String message) {
        this.message = message;
        return this;
    }

    public FacebookHandle setLoadingMessage(int resId) {
        this.message = this.act.getString(resId);
        return this;
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

    private void hide() {
        if (this.dialog != null) {
            try {
                this.dialog.hide();
            } catch (Exception e) {
                AQUtility.debug(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void failure() {
        failure("cancel");
    }

    private void failure(String message) {
        dismiss();
        failure(this.act, AjaxStatus.AUTH_ERROR, message);
    }

    /* access modifiers changed from: protected */
    public void auth() {
        if (!this.act.isFinishing()) {
            boolean ok = sso();
            AQUtility.debug("authing", Boolean.valueOf(ok));
            if (!ok) {
                webAuth();
            }
        }
    }

    private boolean sso() {
        if (this.sso) {
            return startSingleSignOn(this.act, this.appId, this.permissions, this.requestId);
        }
        return false;
    }

    private void webAuth() {
        AQUtility.debug((Object) "web auth");
        Bundle parameters = new Bundle();
        parameters.putString("client_id", this.appId);
        parameters.putString("type", "user_agent");
        if (this.permissions != null) {
            parameters.putString("scope", this.permissions);
        }
        parameters.putString("redirect_uri", REDIRECT_URI);
        String url = "https://graph.facebook.com/oauth/authorize?" + encodeUrl(parameters);
        FbWebViewClient client = new FbWebViewClient(this, null);
        this.dialog = new WebDialog(this.act, url, client);
        this.dialog.setLoadingMessage(this.message);
        this.dialog.setOnCancelListener(client);
        show();
        if (!(this.first && this.token == null)) {
            AQUtility.debug((Object) "auth hide");
            hide();
        }
        this.dialog.load();
        AQUtility.debug((Object) "auth started");
    }

    private String fetchToken() {
        return PreferenceManager.getDefaultSharedPreferences(this.act).getString(FB_TOKEN, null);
    }

    private String fetchPermission() {
        return PreferenceManager.getDefaultSharedPreferences(this.act).getString(FB_PERMISSION, null);
    }

    /* access modifiers changed from: private */
    public void storeToken(String token, String permission) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.act).edit();
        editor.putString(FB_TOKEN, token).putString(FB_PERMISSION, permission);
        AQUtility.apply(editor);
    }

    /* access modifiers changed from: private */
    public String extractToken(String url) {
        String token = Uri.parse(url.replace('#', '?')).getQueryParameter("access_token");
        AQUtility.debug("token", token);
        return token;
    }

    private static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(new StringBuilder(String.valueOf(key)).append("=").append(parameters.getString(key)).toString());
        }
        return sb.toString();
    }

    private static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            for (String parameter : s.split("&")) {
                String[] v = parameter.split("=");
                params.putString(v[0], v[1]);
            }
        }
        return params;
    }

    /* access modifiers changed from: private|static */
    public static Bundle parseUrl(String url) {
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    public boolean expired(AbstractAjaxCallback<?, ?> cb, AjaxStatus status) {
        int code = status.getCode();
        if (code == 200) {
            return false;
        }
        String error = status.getError();
        if (error == null || !error.contains("OAuthException")) {
            String url = cb.getUrl();
            if (code == 400 && (url.endsWith("/likes") || url.endsWith("/comments") || url.endsWith("/checkins"))) {
                return false;
            }
            if (code == 403 && (url.endsWith("/feed") || url.contains("method=delete"))) {
                return false;
            }
            if (code == 400 || code == 401 || code == 403) {
                return true;
            }
            return false;
        }
        AQUtility.debug((Object) "fb token expired");
        return true;
    }

    public boolean reauth(final AbstractAjaxCallback<?, ?> cb) {
        AQUtility.debug((Object) "reauth requested");
        this.token = null;
        AQUtility.post(new Runnable() {
            public void run() {
                FacebookHandle.this.auth(cb);
            }
        });
        return false;
    }

    public String getNetworkUrl(String url) {
        Object stringBuilder;
        if (url.indexOf(63) == -1) {
            stringBuilder = new StringBuilder(String.valueOf(url)).append("?").toString();
        } else {
            stringBuilder = new StringBuilder(String.valueOf(url)).append("&").toString();
        }
        return new StringBuilder(String.valueOf(stringBuilder)).append("access_token=").append(this.token).toString();
    }

    public String getCacheUrl(String url) {
        return getNetworkUrl(url);
    }

    public boolean authenticated() {
        return this.token != null;
    }

    public void unauth() {
        this.token = null;
        CookieSyncManager.createInstance(this.act);
        CookieManager.getInstance().removeAllCookie();
        storeToken(null, null);
    }

    private boolean startSingleSignOn(Activity activity, String applicationId, String permissions, int activityCode) {
        boolean didSucceed = true;
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", applicationId);
        if (permissions != null) {
            intent.putExtra("scope", permissions);
        }
        if (!validateAppSignatureForIntent(activity, intent)) {
            return false;
        }
        try {
            activity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    public boolean isSSOAvailable() {
        if (hasSSO == null) {
            Intent intent = new Intent();
            intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
            hasSSO = Boolean.valueOf(validateAppSignatureForIntent(this.act, intent));
        }
        return hasSSO.booleanValue();
    }

    /* access modifiers changed from: protected */
    public void authenticated(String token) {
    }

    public void ajaxProfile(AjaxCallback<JSONObject> cb) {
        ajaxProfile(cb, 0);
    }

    public void ajaxProfile(AjaxCallback<JSONObject> cb, long expire) {
        ((AQuery) new AQuery(this.act).auth(this)).ajax("https://graph.facebook.com/me", JSONObject.class, expire, (AjaxCallback) cb);
    }

    private boolean validateAppSignatureForIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }
        try {
            for (Signature signature : pm.getPackageInfo(resolveInfo.activityInfo.packageName, 64).signatures) {
                if (signature.toCharsString().equals(FB_APP_SIGNATURE)) {
                    return true;
                }
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AQUtility.debug("on result", Integer.valueOf(resultCode));
        if (resultCode == -1) {
            String error = data.getStringExtra("error");
            if (error == null) {
                error = data.getStringExtra("error_type");
            }
            if (error != null) {
                AQUtility.debug("error", error);
                if (error.equals("service_disabled") || error.equals("AndroidAuthKillSwitchException")) {
                    webAuth();
                    return;
                }
                String description = data.getStringExtra("error_description");
                AQUtility.debug("fb error", description);
                Log.e("fb error", description);
                failure(description);
                return;
            }
            this.token = data.getStringExtra("access_token");
            AQUtility.debug("onComplete", this.token);
            if (this.token != null) {
                storeToken(this.token, this.permissions);
                this.first = false;
                authenticated(this.token);
                success(this.act);
                return;
            }
            failure();
        } else if (resultCode == 0) {
            failure();
        }
    }
}
