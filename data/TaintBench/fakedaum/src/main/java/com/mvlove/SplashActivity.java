package com.mvlove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.mvlove.service.TaskService;

public class SplashActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, TaskService.class));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!SplashActivity.this.isFinishing()) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }
}
