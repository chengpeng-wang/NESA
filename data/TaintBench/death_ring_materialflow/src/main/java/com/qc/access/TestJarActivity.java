package com.qc.access;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.entity.CustomInfo;
import com.qc.util.SystemUtil;

public class TestJarActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout rl1 = new RelativeLayout(this);
        rl1.setId(11);
        Button btn1 = new Button(this);
        btn1.setText("版本检测");
        btn1.setId(1);
        rl1.addView(btn1, new LayoutParams(-2, -2));
        LayoutParams lp1 = new LayoutParams(-2, -2);
        lp1.addRule(13);
        lp1.addRule(14, -1);
        relativeLayout.addView(rl1, lp1);
        RelativeLayout relativeLayout1 = new RelativeLayout(this);
        Button btn2 = new Button(this);
        btn2.setText("本地安装");
        btn2.setId(2);
        relativeLayout1.addView(btn2, new LayoutParams(-2, -2));
        LayoutParams lp11 = new LayoutParams(-2, -2);
        lp11.addRule(3, 11);
        lp11.addRule(14);
        relativeLayout.addView(relativeLayout1, lp11);
        RelativeLayout relativeLayout3 = new RelativeLayout(this);
        Button btn3 = new Button(this);
        btn3.setText("平台测试");
        btn3.setId(3);
        relativeLayout3.addView(btn3, new LayoutParams(-2, -2));
        LayoutParams lp13 = new LayoutParams(-2, -2);
        lp13.addRule(2, 11);
        lp13.addRule(14);
        relativeLayout.addView(relativeLayout3, lp13);
        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                OrderSet.customInfo = new CustomInfo();
                String version = OrderSet.customInfo.getVersion();
                if (version == null || version.length() < 1 || "0.0.0.0".equals(version)) {
                    version = Funs.getAppVersionName(TestJarActivity.this);
                }
                int isInline = SystemUtil.checkAppType(TestJarActivity.this, TestJarActivity.this.getPackageName());
                if (isInline == 0) {
                    TestJarActivity.this.startActivity(new Intent(TestJarActivity.this, Warming.class));
                }
                Toast.makeText(TestJarActivity.this, "平台版本号:" + version + ";平台内置:" + isInline, 1).show();
            }
        });
        btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TestJarActivity.this.startService(new Intent(TestJarActivity.this, LocalOsService.class));
            }
        });
        btn3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Funs.queryServices(TestJarActivity.this, Constant.DOUBLEPACKAGE_CHECK)) {
                    Intent i = new Intent(TestJarActivity.this, Warming.class);
                    i.addFlags(268435456);
                    TestJarActivity.this.startActivity(i);
                    return;
                }
                QCCache.getInstance().clearCache();
                QCCache.getInstance().clearQueue();
                OrderSet.aliveApps.clear();
                OrderSet.clickApps.clear();
                OrderSet.openPager.clear();
                OrderSet.customInfo = new CustomInfo();
                QCCache.getInstance().reSetValue("test", Integer.valueOf(1));
                TestJarActivity.this.startService(new Intent(Constant.INSTALLCFG_ACTION));
            }
        });
        setContentView(relativeLayout);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
