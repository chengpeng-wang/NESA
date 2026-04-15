package com.androidquery.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.androidquery.util.Constants;
import com.google.analytics.tracking.android.ModelFields;
import java.util.Locale;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

public class MarketService {
    private static final String BULLET = "•";
    public static final int MAJOR = 2;
    public static final int MINOR = 1;
    public static final int REVISION = 0;
    private static final String SKIP_VERSION = "aqs.skip";
    private static ApplicationInfo ai;
    private static PackageInfo pi;
    /* access modifiers changed from: private */
    public Activity act;
    /* access modifiers changed from: private */
    public AQuery aq;
    /* access modifiers changed from: private */
    public boolean completed;
    private long expire = 720000;
    /* access modifiers changed from: private */
    public boolean fetch;
    private boolean force;
    private Handler handler;
    private int level = 0;
    private String locale;
    /* access modifiers changed from: private */
    public int progress;
    /* access modifiers changed from: private */
    public String rateUrl;
    /* access modifiers changed from: private */
    public String updateUrl;
    /* access modifiers changed from: private */
    public String version;

    private class Handler implements OnClickListener, TagHandler {
        private Handler() {
        }

        /* synthetic */ Handler(MarketService marketService, Handler handler) {
            this();
        }

        public void marketCb(String url, JSONObject jo, AjaxStatus status) {
            if (!MarketService.this.act.isFinishing()) {
                if (jo != null) {
                    String s = jo.optString("status");
                    if ("1".equals(s)) {
                        if (jo.has("dialog")) {
                            cb(url, jo, status);
                        }
                        if (!MarketService.this.fetch && jo.optBoolean("fetch", false) && status.getSource() == 1) {
                            MarketService.this.fetch = true;
                            String marketUrl = jo.optString("marketUrl", null);
                            AjaxCallback<String> cb = new AjaxCallback();
                            ((AjaxCallback) ((AjaxCallback) cb.url(marketUrl)).type(String.class)).handler(this, "detailCb");
                            ((AQuery) MarketService.this.aq.progress(MarketService.this.progress)).ajax(cb);
                            return;
                        }
                        return;
                    } else if ("0".equals(s)) {
                        status.invalidate();
                        return;
                    } else {
                        cb(url, jo, status);
                        return;
                    }
                }
                cb(url, jo, status);
            }
        }

        private void cb(String url, JSONObject jo, AjaxStatus status) {
            if (!MarketService.this.completed) {
                MarketService.this.completed = true;
                MarketService.this.progress = 0;
                MarketService.this.callback(url, jo, status);
            }
        }

        public void detailCb(String url, String html, AjaxStatus status) {
            if (html != null && html.length() > 1000) {
                String qurl = MarketService.this.getQueryUrl();
                AjaxCallback<JSONObject> cb = new AjaxCallback();
                ((AjaxCallback) ((AjaxCallback) cb.url(qurl)).type(JSONObject.class)).handler(this, "marketCb");
                cb.param("html", html);
                ((AQuery) MarketService.this.aq.progress(MarketService.this.progress)).ajax(cb);
            }
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case -3:
                    MarketService.setSkipVersion(MarketService.this.act, MarketService.this.version);
                    return;
                case -2:
                    MarketService.openUrl(MarketService.this.act, MarketService.this.updateUrl);
                    return;
                case -1:
                    MarketService.openUrl(MarketService.this.act, MarketService.this.rateUrl);
                    return;
                default:
                    return;
            }
        }

        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (!"li".equals(tag)) {
                return;
            }
            if (opening) {
                output.append("  ");
                output.append(MarketService.BULLET);
                output.append("  ");
                return;
            }
            output.append("\n");
        }
    }

    public MarketService(Activity act) {
        this.act = act;
        this.aq = new AQuery(act);
        this.handler = new Handler(this, null);
        this.locale = Locale.getDefault().toString();
        this.rateUrl = getMarketUrl();
        this.updateUrl = this.rateUrl;
    }

    public MarketService rateUrl(String url) {
        this.rateUrl = url;
        return this;
    }

    public MarketService level(int level) {
        this.level = level;
        return this;
    }

    public MarketService updateUrl(String url) {
        this.updateUrl = url;
        return this;
    }

    public MarketService locale(String locale) {
        this.locale = locale;
        return this;
    }

    public MarketService progress(int id) {
        this.progress = id;
        return this;
    }

    public MarketService force(boolean force) {
        this.force = force;
        return this;
    }

    public MarketService expire(long expire) {
        this.expire = expire;
        return this;
    }

    private ApplicationInfo getApplicationInfo() {
        if (ai == null) {
            ai = this.act.getApplicationInfo();
        }
        return ai;
    }

    private PackageInfo getPackageInfo() {
        if (pi == null) {
            try {
                pi = this.act.getPackageManager().getPackageInfo(getAppId(), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pi;
    }

    private String getHost() {
        return "https://androidquery.appspot.com";
    }

    /* access modifiers changed from: private */
    public String getQueryUrl() {
        String url = getHost() + "/api/market?app=" + getAppId() + "&locale=" + this.locale + "&version=" + getVersion() + "&code=" + getVersionCode() + "&aq=" + Constants.VERSION;
        if (this.force) {
            return new StringBuilder(String.valueOf(url)).append("&force=true").toString();
        }
        return url;
    }

    private String getAppId() {
        return getApplicationInfo().packageName;
    }

    private Drawable getAppIcon() {
        return getApplicationInfo().loadIcon(this.act.getPackageManager());
    }

    private String getVersion() {
        return getPackageInfo().versionName;
    }

    private int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    public void checkVersion() {
        String url = getQueryUrl();
        AjaxCallback<JSONObject> cb = new AjaxCallback();
        ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) ((AjaxCallback) cb.url(url)).type(JSONObject.class)).handler(this.handler, "marketCb")).fileCache(!this.force)).expire(this.expire);
        ((AQuery) this.aq.progress(this.progress)).ajax(cb);
    }

    /* access modifiers changed from: private|static */
    public static boolean openUrl(Activity act, String url) {
        if (url == null) {
            return false;
        }
        try {
            act.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getMarketUrl() {
        return "market://details?id=" + getAppId();
    }

    /* access modifiers changed from: protected */
    public void callback(String url, JSONObject jo, AjaxStatus status) {
        if (jo != null) {
            String latestVer = jo.optString("version", "0");
            int latestCode = jo.optInt("code", 0);
            AQUtility.debug("version", getVersion() + "->" + latestVer + ":" + getVersionCode() + "->" + latestCode);
            AQUtility.debug("outdated", Boolean.valueOf(outdated(latestVer, latestCode)));
            if (this.force || outdated(latestVer, latestCode)) {
                showUpdateDialog(jo);
            }
        }
    }

    private boolean outdated(String latestVer, int latestCode) {
        if (latestVer.equals(getSkipVersion(this.act))) {
            return false;
        }
        String version = getVersion();
        int code = getVersionCode();
        if (version.equals(latestVer) || code > latestCode) {
            return false;
        }
        return requireUpdate(version, latestVer, this.level);
    }

    private boolean requireUpdate(String existVer, String latestVer, int level) {
        if (existVer.equals(latestVer)) {
            return false;
        }
        try {
            String[] evs = existVer.split("\\.");
            String[] lvs = latestVer.split("\\.");
            if (evs.length < 3 || lvs.length < 3) {
                return true;
            }
            switch (level) {
                case 0:
                    if (!evs[evs.length - 1].equals(lvs[lvs.length - 1])) {
                        return true;
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return true;
            }
            if (!evs[evs.length - 2].equals(lvs[lvs.length - 2])) {
                return true;
            }
            if (evs[evs.length - 3].equals(lvs[lvs.length - 3])) {
                return false;
            }
            return true;
        } catch (Exception e) {
            AQUtility.report(e);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void showUpdateDialog(JSONObject jo) {
        if (jo != null && this.version == null && isActive()) {
            JSONObject dia = jo.optJSONObject("dialog");
            String update = dia.optString("update", "Update");
            String skip = dia.optString("skip", "Skip");
            String rate = dia.optString("rate", "Rate");
            String body = dia.optString("wbody", "");
            String title = dia.optString(ModelFields.TITLE, "Update Available");
            AQUtility.debug("wbody", body);
            this.version = jo.optString("version", null);
            AlertDialog dialog = new Builder(this.act).setIcon(getAppIcon()).setTitle(title).setPositiveButton(rate, this.handler).setNeutralButton(skip, this.handler).setNegativeButton(update, this.handler).create();
            dialog.setMessage(Html.fromHtml(patchBody(body), null, this.handler));
            this.aq.show(dialog);
        }
    }

    private static String patchBody(String body) {
        return "<small>" + body + "</small>";
    }

    /* access modifiers changed from: private|static */
    public static void setSkipVersion(Context context, String version) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SKIP_VERSION, version).commit();
    }

    private static String getSkipVersion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SKIP_VERSION, null);
    }

    private boolean isActive() {
        if (this.act.isFinishing()) {
            return false;
        }
        return true;
    }
}
