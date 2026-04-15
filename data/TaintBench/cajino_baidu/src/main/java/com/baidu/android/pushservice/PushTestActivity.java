package com.baidu.android.pushservice;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.baidu.android.common.util.DeviceId;

public class PushTestActivity extends Activity {
    String a = "1.2b8a945ec32b6e4fea2971e78a98e040.86400.1335690526.2819575091-101962";
    private LinearLayout b;

    private void a() {
        TextView textView = new TextView(this);
        textView.setText("device id: " + DeviceId.getDeviceID(this));
        this.b.addView(textView);
        String str = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        str = telephonyManager != null ? "" + telephonyManager.getDeviceId() : "";
        String str2 = "" + Secure.getString(getContentResolver(), "android_id");
        TextView textView2 = new TextView(this);
        textView2.setText("imei: " + str);
        this.b.addView(textView2);
        textView = new TextView(this);
        textView.setText("ANDROID_ID: " + str2);
        this.b.addView(textView);
        b();
        c();
        h();
        d();
        e();
        f();
        g();
    }

    private void b() {
        Button button = new Button(this);
        button.setText("Bind (status 0)");
        this.b.addView(button);
        button.setOnClickListener(new p(this));
    }

    private void c() {
        Button button = new Button(this);
        button.setText("Bind (status 1)");
        this.b.addView(button);
        button.setOnClickListener(new q(this));
    }

    private void d() {
        Button button = new Button(this);
        button.setText("Unbind App");
        this.b.addView(button);
        button.setOnClickListener(new r(this));
    }

    private void e() {
        Button button = new Button(this);
        button.setText("Fetch");
        this.b.addView(button);
        button.setOnClickListener(new s(this));
    }

    private void f() {
        Button button = new Button(this);
        button.setText("Count");
        this.b.addView(button);
        button.setOnClickListener(new t(this));
    }

    private void g() {
        Button button = new Button(this);
        button.setText("Delete");
        this.b.addView(button);
        button.setOnClickListener(new u(this));
    }

    private void h() {
        Button button = new Button(this);
        button.setText("Unbind");
        this.b.addView(button);
        button.setOnClickListener(new v(this));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ScrollView scrollView = new ScrollView(this);
        setContentView(scrollView);
        this.b = new LinearLayout(this);
        this.b.setOrientation(1);
        scrollView.addView(this.b);
        a();
    }
}
