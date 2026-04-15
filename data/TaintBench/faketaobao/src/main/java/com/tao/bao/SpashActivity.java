package com.tao.bao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SpashActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SpashActivity.this.startActivity(new Intent(SpashActivity.this, MainActivity.class));
                SpashActivity.this.finish();
            }
        }, 3000);
    }
}
