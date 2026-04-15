package com.mb.bdapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.mopub.volley.BuildConfig;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

public class ReferreUtils {
    private static final int SC_TEMPORARY_REDIRECT = 307;
    public static int jump_count = 0;
    public static String mLastUrl;
    /* access modifiers changed from: private */
    public String gid;
    /* access modifiers changed from: private */
    public Context mContext;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public String play_url;

    public ReferreUtils(Context context, String play_url, String gid) {
        this.mContext = context;
        this.play_url = play_url;
        this.gid = gid;
        this.mHandler = new Handler(context.getMainLooper());
    }

    public void webviewGetReferre() {
        this.mHandler.post(new Runnable() {
            @SuppressLint({"SetJavaScriptEnabled"})
            public void run() {
                WebView mWebView = new WebView(ReferreUtils.this.mContext);
                WebSettings webSetting = mWebView.getSettings();
                webSetting.setJavaScriptEnabled(true);
                webSetting.getUserAgentString();
                mWebView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        if (ReferreUtils.this.matchPlayUrl(url)) {
                            ReferreUtils.this.saveReferre(ReferreUtils.this.mContext, url, ReferreUtils.this.gid);
                        }
                    }
                });
                mWebView.loadUrl(ReferreUtils.this.play_url);
            }
        });
    }

    public void clienGetReferre() {
        new Thread(new Runnable() {
            public void run() {
                ReferreUtils.this.getNextUrl(ReferreUtils.this.play_url);
            }
        }).start();
    }

    @SuppressLint({"DefaultLocale"})
    private String generateUserAgent(Context context) {
        StringBuffer buffer = new StringBuffer();
        String version = VERSION.RELEASE;
        if (version.length() <= 0) {
            buffer.append(BuildConfig.VERSION_NAME);
        } else if (Character.isDigit(version.charAt(0))) {
            buffer.append(version);
        } else {
            buffer.append(BuildConfig.VERSION_NAME);
        }
        buffer.append("; ");
        String language = Locale.getDefault().getLanguage();
        if (language != null) {
            buffer.append(convertObsoleteLanguageCodeToNew(language));
            String country = Locale.getDefault().getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            buffer.append("en");
        }
        buffer.append(";");
        if ("REL".equals(VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append(" ");
                buffer.append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        int i = Resources.getSystem().getIdentifier("web_user_agent_target_content", "string", "android");
        String mobile = "";
        if (i > 0) {
            mobile = this.mContext.getResources().getText(i).toString();
        }
        return String.format(this.mContext.getResources().getText(Resources.getSystem().getIdentifier("web_user_agent", "string", "android")).toString(), new Object[]{buffer, mobile});
    }

    private static String convertObsoleteLanguageCodeToNew(String langCode) {
        if (langCode == null) {
            return null;
        }
        if ("iw".equals(langCode)) {
            return "he";
        }
        if ("in".equals(langCode)) {
            return MASTNativeAdConstants.ID_STRING;
        }
        if ("ji".equals(langCode)) {
            return "yi";
        }
        return langCode;
    }

    /* access modifiers changed from: private */
    public void getNextUrl(String requestUrl) {
        String resut = "";
        try {
            if (matchPlayUrl(requestUrl)) {
                saveReferre(this.mContext, requestUrl, this.gid);
            }
            HttpURLConnection mConnection = (HttpURLConnection) new URL(requestUrl).openConnection();
            mConnection.setInstanceFollowRedirects(false);
            mConnection.setRequestMethod("POST");
            mConnection.setConnectTimeout(5000);
            mConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            mConnection.addRequestProperty("User-Agent", generateUserAgent(this.mContext));
            mConnection.connect();
            switch (mConnection.getResponseCode()) {
                case CtaButton.WIDTH_DIPS /*200*/:
                case 206:
                    resut = requestUrl;
                    break;
                case 301:
                case 302:
                case 303:
                case SC_TEMPORARY_REDIRECT /*307*/:
                    resut = mConnection.getHeaderField("Location");
                    break;
            }
            if (matchPlayUrl(resut)) {
                saveReferre(this.mContext, resut, this.gid);
                return;
            }
            jump_count++;
            if (jump_count < 10) {
                getNextUrl(resut);
            } else {
                webviewGetReferre();
            }
        } catch (Exception e) {
            e.printStackTrace();
            webviewGetReferre();
        }
    }

    /* access modifiers changed from: private */
    public boolean matchPlayUrl(String url) {
        try {
            if (TextUtils.isEmpty(url) || !(url.contains("play.google.com") || url.contains("market://details?"))) {
                Log.e("HDJ", "发现非google应用");
                return false;
            }
            Log.e("HDJ", "发现google应用");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HDJ", "发现非google应用");
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void saveReferre(Context context, String url, String gid) {
        String pkgName = "";
        String referre = "";
        String[] reust = Uri.parse(url).getEncodedQuery().split(MASTNativeAdConstants.AMPERSAND);
        for (String split : reust) {
            String[] query = split.split(MASTNativeAdConstants.EQUAL);
            if (query != null && query.length >= 1) {
                if (MASTNativeAdConstants.ID_STRING.equals(query[0])) {
                    pkgName = query[1];
                } else if (DuAd.DB_REFERRER.equals(query[0])) {
                    referre = query[1];
                }
            }
        }
        if (!TextUtils.isEmpty(pkgName) && !TextUtils.isEmpty(referre)) {
            try {
                context.getSharedPreferences("spf", 0).edit().putString("referre_" + gid, URLDecoder.decode(referre, "utf-8")).commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
